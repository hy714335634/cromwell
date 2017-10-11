package cromwell

import cats.data.Validated._
import cats.syntax.validated._
import lenthall.validation.ErrorOr.ErrorOr
import wom.values.WomValue

import scala.util.{Failure, Success, Try}

package object core {
  type LocallyQualifiedName = String
  type FullyQualifiedName = String
  type WorkflowOutputs = Map[FullyQualifiedName, JobOutput]
  type WorkflowOptionsJson = String
  type WorkflowType = String
  type WorkflowTypeVersion = String
  type CallOutputs = Map[String, JobOutput]
  type HostInputs = Map[String, WomValue]
  type EvaluatedRuntimeAttributes = Map[String, WomValue]

  implicit class toErrorOr[A](val trySomething: Try[A]) {
    def tryToErrorOr: ErrorOr[A] = trySomething match {
      case Success(options) => options.validNel
      case Failure(err) => err.getMessage.invalidNel
    }
  }

  implicit class toTry[A](val validatedSomething: ErrorOr[A]) {
    def errorOrToTry: Try[A] = validatedSomething match {
      case Valid(options) => Success(options)
      case Invalid(err) => Failure(new RuntimeException(s"Error(s): ${err.toList.mkString(",")}"))
    }
  }
}

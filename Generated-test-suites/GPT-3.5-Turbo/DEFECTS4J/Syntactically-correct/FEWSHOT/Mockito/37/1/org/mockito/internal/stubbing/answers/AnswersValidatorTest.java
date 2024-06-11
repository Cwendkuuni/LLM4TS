
package org.mockito.internal.stubbing.answers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;

public class AnswersValidatorTest {

    private Reporter reporterMock = mock(Reporter.class);

    @Test
    public void testValidate_DoNothing_VoidMethod_Success() {
        // Arrange
        DoesNothing answer = new DoesNothing();
        Invocation invocation = new Invocation(Void.class, "methodName", null);

        // Act
        AnswersValidator answersValidator = new AnswersValidator();
        answersValidator.validateDoNothing(answer, invocation);

        // Assert
        verify(reporterMock, times(0)).onlyVoidMethodsCanBeSetToDoNothing();
    }

    @Test
    public void testValidate_DoNothing_NonVoidMethod_Failure() {
        // Arrange
        DoesNothing answer = new DoesNothing();
        Invocation invocation = new Invocation(String.class, "methodName", null);

        // Act
        AnswersValidator answersValidator = new AnswersValidator();
        answersValidator.validateDoNothing(answer, invocation);

        // Assert
        verify(reporterMock).onlyVoidMethodsCanBeSetToDoNothing();
    }

    @Test
    public void testValidate_ReturnValue_VoidMethod_Failure() {
        // Arrange
        Returns answer = new Returns("returnValue");
        Invocation invocation = new Invocation(Void.class, "methodName", null);

        // Act
        AnswersValidator answersValidator = new AnswersValidator();
        answersValidator.validateReturnValue(answer, invocation);

        // Assert
        verify(reporterMock).cannotStubVoidMethodWithAReturnValue();
    }

    @Test
    public void testValidate_ReturnValue_ReturnsNull_PrimitiveMethod_Failure() {
        // Arrange
        Returns answer = new Returns(null);
        Invocation invocation = new Invocation(int.class, "methodName", null);

        // Act
        AnswersValidator answersValidator = new AnswersValidator();
        answersValidator.validateReturnValue(answer, invocation);

        // Assert
        verify(reporterMock).wrongTypeOfReturnValue("int", "null", "methodName");
    }

    @Test
    public void testValidate_ReturnValue_ReturnsNull_NonPrimitiveMethod_Success() {
        // Arrange
        Returns answer = new Returns(null);
        Invocation invocation = new Invocation(String.class, "methodName", null);

        // Act
        AnswersValidator answersValidator = new AnswersValidator();
        answersValidator.validateReturnValue(answer, invocation);

        // Assert
        verify(reporterMock, times(0)).wrongTypeOfReturnValue("String", "null", "methodName");
    }

    @Test
    public void testValidate_ReturnValue_ReturnsNonNull_InvalidReturnTypeMethod_Failure() {
        // Arrange
        Returns answer = new Returns("returnValue");
        Invocation invocation = new Invocation(int.class, "methodName", null);

        // Act
        AnswersValidator answersValidator = new AnswersValidator();
        answersValidator.validateReturnValue(answer, invocation);

        // Assert
        verify(reporterMock).wrongTypeOfReturnValue("int", "String", "methodName");
    }

    @Test
    public void testValidate_ReturnValue_ReturnsNonNull_ValidReturnTypeMethod_Success() {
        // Arrange
        Returns answer = new Returns("returnValue");
        Invocation invocation = new Invocation(String.class, "methodName", null);

        // Act
        AnswersValidator answersValidator = new AnswersValidator();
        answersValidator.validateReturnValue(answer, invocation);

        // Assert
        verify(reporterMock, times(0)).wrongTypeOfReturnValue("String", "String", "methodName");
    }

    @Test
    public void testValidate_Exception_WithNullException_Failure() {
        // Arrange
        ThrowsException answer = new ThrowsException(null);
        Invocation invocation = new Invocation(String.class, "methodName", null);

        // Act
        AnswersValidator answersValidator = new AnswersValidator();
        answersValidator.validateException(answer, invocation);

        // Assert
        verify(reporterMock).cannotStubWithNullThrowable();
    }

    @Test
    public void testValidate_Exception_WithRuntimeException_Success() {
        // Arrange
        ThrowsException answer = new ThrowsException(new RuntimeException());
        Invocation invocation = new Invocation(String.class, "methodName", null);

        // Act
        AnswersValidator answersValidator = new AnswersValidator();
        answersValidator.validateException(answer, invocation);

        // Assert
        verify(reporterMock, times(0)).checkedExceptionInvalid(any());
    }

    @Test
    public void testValidate_Exception_WithError_Success() {
        // Arrange
        ThrowsException answer = new ThrowsException(new Error());
        Invocation invocation = new Invocation(String.class, "methodName", null);

        // Act
        AnswersValidator answersValidator = new AnswersValidator();
        answersValidator.validateException(answer, invocation);

        // Assert
        verify(reporterMock, times(0)).checkedExceptionInvalid(any());
    }

    @Test
    public void testValidate_Exception_WithCheckedException_InvalidException_Failure() {
        // Arrange
        ThrowsException answer = new ThrowsException(new Exception());
        Invocation invocation = new Invocation(String.class, "methodName", null);

        // Act
        AnswersValidator answersValidator = new AnswersValidator();
        answersValidator.validateException(answer, invocation);

        // Assert
        verify(reporterMock).checkedExceptionInvalid(any());
    }
}

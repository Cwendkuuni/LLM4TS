
package org.mockito.internal.stubbing.answers;

import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class AnswersValidatorTest {

    private Answer<?> answerMock = mock(Answer.class);
    private Invocation invocationMock = mock(Invocation.class);
    private Reporter reporterMock = mock(Reporter.class);
    private AnswersValidator answersValidator = new AnswersValidator();

    @Test
    public void testValidate_DoNothing_WithNonVoidInvocation() {
        assertFalse(invocationMock.isVoid());
        answersValidator.validateDoNothing(answerMock, invocationMock);
        assertTrue(reporterMock.onlyVoidMethodsCanBeSetToDoNothing());
    }

    @Test
    public void testValidate_ReturnValue_WithVoidInvocation() {
        assertTrue(invocationMock.isVoid());
        answersValidator.validateReturnValue(answerMock, invocationMock);
        assertTrue(reporterMock.cannotStubVoidMethodWithAReturnValue());
    }

    @Test
    public void testValidate_ReturnValue_WithNullReturnValueAndPrimitiveInvocation() {
        assertTrue(answerMock.returnsNull());
        assertTrue(invocationMock.returnsPrimitive());
        answersValidator.validateReturnValue(answerMock, invocationMock);
        assertTrue(reporterMock.wrongTypeOfReturnValue(invocationMock.printMethodReturnType(), "null", invocationMock.getMethodName()));
    }

    @Test
    public void testValidate_ReturnValue_WithNonNullReturnValueAndInvalidReturnType() {
        assertFalse(answerMock.returnsNull());
        assertFalse(invocationMock.isValidReturnType(answerMock.getReturnType()));
        answersValidator.validateReturnValue(answerMock, invocationMock);
        assertTrue(reporterMock.wrongTypeOfReturnValue(invocationMock.printMethodReturnType(), answerMock.printReturnType(), invocationMock.getMethodName()));
    }

    @Test
    public void testValidate_Exception_WithNullThrowable() {
        assertTrue(answerMock instanceof ThrowsException);
        assertTrue(((ThrowsException) answerMock).getThrowable() == null);
        answersValidator.validateException(answerMock, invocationMock);
        assertTrue(reporterMock.cannotStubWithNullThrowable());
    }

    @Test
    public void testValidate_Exception_WithCheckedExceptionInvalid() {
        assertTrue(answerMock instanceof ThrowsException);
        Throwable throwable = ((ThrowsException) answerMock).getThrowable();
        assertFalse(throwable instanceof RuntimeException || throwable instanceof Error);
        assertFalse(invocationMock.isValidException(throwable));
        answersValidator.validateException(answerMock, invocationMock);
        assertTrue(reporterMock.checkedExceptionInvalid(throwable));
    }
}

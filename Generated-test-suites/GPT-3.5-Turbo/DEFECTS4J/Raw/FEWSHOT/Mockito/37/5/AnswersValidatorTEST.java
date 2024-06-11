import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.stubbing.answers.AnswersValidator;
import org.mockito.internal.stubbing.answers.DoesNothing;
import org.mockito.internal.stubbing.answers.Returns;
import org.mockito.internal.stubbing.answers.ThrowsException;
import org.mockito.stubbing.Answer;

public class AnswersValidatorTest {
    
    private AnswersValidator answersValidator;
    private Reporter reporter;
    private Answer<?> answer;
    private Invocation invocation;

    @Before
    public void setUp() {
        answersValidator = new AnswersValidator();
        reporter = mock(Reporter.class);
        answersValidator.setReporter(reporter);
        answer = mock(Answer.class);
        invocation = mock(Invocation.class);
    }

    @Test
    public void testValidate_DoNothingOnVoidMethod() {
        when(invocation.isVoid()).thenReturn(true);
        answersValidator.validateDoNothing(answer, invocation);
        assertTrue(reporter.onlyVoidMethodsCanBeSetToDoNothingCalled());
    }

    @Test
    public void testValidate_DoNothingOnNonVoidMethod() {
        when(invocation.isVoid()).thenReturn(false);
        answersValidator.validateDoNothing(answer, invocation);
        assertFalse(reporter.onlyVoidMethodsCanBeSetToDoNothingCalled());
    }

    @Test
    public void testValidate_ReturnValueOnVoidMethod() {
        when(invocation.isVoid()).thenReturn(true);
        answersValidator.validateReturnValue(answer, invocation);
        assertTrue(reporter.cannotStubVoidMethodWithAReturnValueCalled());
    }
    
    @Test
    public void testValidate_ReturnValueWithNull_ReturningPrimitive() {
        when(invocation.isVoid()).thenReturn(false);
        when(answer.returnsNull()).thenReturn(true);
        when(invocation.returnsPrimitive()).thenReturn(true);
        answersValidator.validateReturnValue(answer, invocation);
        assertTrue(reporter.wrongTypeOfReturnValueCalled());
        assertEquals(invocation.printMethodReturnType(), reporter.getPrintedMethodReturnType());
        assertEquals("null", reporter.getExpectedReturnType());
        assertEquals(invocation.getMethodName(), reporter.getInvokedMethodName());
    }

    @Test
    public void testValidate_ReturnValueWithInvalidReturnType() {
        when(invocation.isVoid()).thenReturn(false);
        when(answer.returnsNull()).thenReturn(false);
        when(invocation.isValidReturnType(answer.getReturnType())).thenReturn(false);
        answersValidator.validateReturnValue(answer, invocation);
        assertTrue(reporter.wrongTypeOfReturnValueCalled());
        assertEquals(invocation.printMethodReturnType(), reporter.getPrintedMethodReturnType());
        assertEquals(answer.printReturnType(), reporter.getExpectedReturnType());
        assertEquals(invocation.getMethodName(), reporter.getInvokedMethodName());
    }

    @Test
    public void testValidate_ReturnValueWithValidReturnType() {
        when(invocation.isVoid()).thenReturn(false);
        when(answer.returnsNull()).thenReturn(false);
        when(invocation.isValidReturnType(answer.getReturnType())).thenReturn(true);
        answersValidator.validateReturnValue(answer, invocation);
        assertFalse(reporter.wrongTypeOfReturnValueCalled());
    }

    @Test
    public void testValidate_ExceptionWithNullThrowable() {
        when(answer.getThrowable()).thenReturn(null);
        answersValidator.validateException(answer, invocation);
        assertTrue(reporter.cannotStubWithNullThrowableCalled());
    }

    @Test
    public void testValidate_ExceptionWithRuntimeException() {
        Throwable throwable = mock(RuntimeException.class);
        when(answer.getThrowable()).thenReturn(throwable);
        answersValidator.validateException(answer, invocation);
        assertFalse(reporter.checkedExceptionInvalidCalled());
    }

    @Test
    public void testValidate_ExceptionWithError() {
        Throwable throwable = mock(Error.class);
        when(answer.getThrowable()).thenReturn(throwable);
        answersValidator.validateException(answer, invocation);
        assertFalse(reporter.checkedExceptionInvalidCalled());
    }

    @Test
    public void testValidate_ExceptionWithCheckedException() {
        Throwable throwable = mock(CheckedException.class);
        when(answer.getThrowable()).thenReturn(throwable);
        when(invocation.isValidException(throwable)).thenReturn(true);
        answersValidator.validateException(answer, invocation);
        assertFalse(reporter.checkedExceptionInvalidCalled());
    }
    
    @Test
    public void testValidate_ExceptionWithInvalidCheckedException() {
        Throwable throwable = mock(CheckedException.class);
        when(answer.getThrowable()).thenReturn(throwable);
        when(invocation.isValidException(throwable)).thenReturn(false);
        answersValidator.validateException(answer, invocation);
        assertTrue(reporter.checkedExceptionInvalidCalled());
        assertEquals(throwable, reporter.getInvalidCheckedException());
    }

}

class Reporter {
    private boolean onlyVoidMethodsCanBeSetToDoNothingCalled;
    private boolean cannotStubVoidMethodWithAReturnValueCalled;
    private boolean wrongTypeOfReturnValueCalled;
    private String printedMethodReturnType;
    private String expectedReturnType;
    private String invokedMethodName;
    private boolean cannotStubWithNullThrowableCalled;
    private boolean checkedExceptionInvalidCalled;
    private Throwable invalidCheckedException;

    public void onlyVoidMethodsCanBeSetToDoNothing() {
        onlyVoidMethodsCanBeSetToDoNothingCalled = true;
    }

    public boolean onlyVoidMethodsCanBeSetToDoNothingCalled() {
        return onlyVoidMethodsCanBeSetToDoNothingCalled;
    }

    public void cannotStubVoidMethodWithAReturnValue() {
        cannotStubVoidMethodWithAReturnValueCalled = true;
    }

    public boolean cannotStubVoidMethodWithAReturnValueCalled() {
        return cannotStubVoidMethodWithAReturnValueCalled;
    }

    public void wrongTypeOfReturnValue(String printedMethodReturnType, String expectedReturnType, String invokedMethodName) {
        wrongTypeOfReturnValueCalled = true;
        this.printedMethodReturnType = printedMethodReturnType;
        this.expectedReturnType = expectedReturnType;
        this.invokedMethodName = invokedMethodName;
    }

    public boolean wrongTypeOfReturnValueCalled() {
        return wrongTypeOfReturnValueCalled;
    }

    public String getPrintedMethodReturnType() {
        return printedMethodReturnType;
    }

    public String getExpectedReturnType() {
        return expectedReturnType;
    }

    public String getInvokedMethodName() {
        return invokedMethodName;
    }

    public void cannotStubWithNullThrowable() {
        cannotStubWithNullThrowableCalled = true;
    }

    public boolean cannotStubWithNullThrowableCalled() {
        return cannotStubWithNullThrowableCalled;
    }

    public void checkedExceptionInvalid(Throwable throwable) {
        checkedExceptionInvalidCalled = true;
        invalidCheckedException = throwable;
    }

    public boolean checkedExceptionInvalidCalled() {
        return checkedExceptionInvalidCalled;
    }

    public Throwable getInvalidCheckedException() {
        return invalidCheckedException;
    }
}

class CheckedException extends Exception {
    // Custom checked exception for testing purposes
}
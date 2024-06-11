package org.mockito.internal.stubbing.answers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;

public class AnswersValidatorTest {
    private AnswersValidator validator;
    private Reporter reporter;
    private Answer<?> answer;
    private Invocation invocation;

    @Before
    public void setUp() {
        validator = new AnswersValidator();
        reporter = new Reporter();
        answer = mock(Answer.class);
        invocation = mock(Invocation.class);
    }

    @Test
    public void testValidate_DoNothingAnswer_VoidInvocation() {
        when(invocation.isVoid()).thenReturn(true);
        validator.validateDoNothing(answer, invocation);
        verify(reporter).onlyVoidMethodsCanBeSetToDoNothing();
    }

    @Test
    public void testValidate_DoNothingAnswer_NonVoidInvocation() {
        when(invocation.isVoid()).thenReturn(false);
        validator.validateDoNothing(answer, invocation);
        verify(reporter, never()).onlyVoidMethodsCanBeSetToDoNothing();
    }

    @Test
    public void testValidate_ReturnValueAnswer_VoidInvocation() {
        when(invocation.isVoid()).thenReturn(true);
        validator.validateReturnValue(answer, invocation);
        verify(reporter).cannotStubVoidMethodWithAReturnValue();
    }

    @Test
    public void testValidate_ReturnValueAnswer_ReturnsNull_PrimitiveInvocation() {
        when(answer.returnsNull()).thenReturn(true);
        when(invocation.returnsPrimitive()).thenReturn(true);
        validator.validateReturnValue(answer, invocation);
        verify(reporter).wrongTypeOfReturnValue(invocation.printMethodReturnType(), "null", invocation.getMethodName());
    }

    @Test
    public void testValidate_ReturnValueAnswer_ReturnsNull_NonPrimitiveInvocation() {
        when(answer.returnsNull()).thenReturn(true);
        when(invocation.returnsPrimitive()).thenReturn(false);
        validator.validateReturnValue(answer, invocation);
        verify(reporter, never()).wrongTypeOfReturnValue(invocation.printMethodReturnType(), "null", invocation.getMethodName());
    }

    @Test
    public void testValidate_ReturnValueAnswer_NonNull_ReturnTypeMismatch() {
        when(answer.returnsNull()).thenReturn(false);
        when(invocation.isValidReturnType(any())).thenReturn(false);
        validator.validateReturnValue(answer, invocation);
        verify(reporter).wrongTypeOfReturnValue(invocation.printMethodReturnType(), answer.printReturnType(), invocation.getMethodName());
    }

    @Test
    public void testValidate_ReturnValueAnswer_NonNull_ReturnTypeMatch() {
        when(answer.returnsNull()).thenReturn(false);
        when(invocation.isValidReturnType(any())).thenReturn(true);
        validator.validateReturnValue(answer, invocation);
        verify(reporter, never()).wrongTypeOfReturnValue(invocation.printMethodReturnType(), answer.printReturnType(), invocation.getMethodName());
    }

    @Test
    public void testValidate_ExceptionAnswer_NullThrowable() {
        when(answer.getThrowable()).thenReturn(null);
        validator.validateException(answer, invocation);
        verify(reporter).cannotStubWithNullThrowable();
    }

    @Test
    public void testValidate_ExceptionAnswer_RuntimeException() {
        Throwable throwable = mock(RuntimeException.class);
        when(answer.getThrowable()).thenReturn(throwable);
        validator.validateException(answer, invocation);
        verify(reporter, never()).checkedExceptionInvalid(throwable);
    }

    @Test
    public void testValidate_ExceptionAnswer_Error() {
        Throwable throwable = mock(Error.class);
        when(answer.getThrowable()).thenReturn(throwable);
        validator.validateException(answer, invocation);
        verify(reporter, never()).checkedExceptionInvalid(throwable);
    }

    @Test
    public void testValidate_ExceptionAnswer_CheckedInvalid() {
        Throwable throwable = mock(Exception.class);
        when(answer.getThrowable()).thenReturn(throwable);
        when(invocation.isValidException(any())).thenReturn(false);
        validator.validateException(answer, invocation);
        verify(reporter).checkedExceptionInvalid(throwable);
    }

    @Test
    public void testValidate_ExceptionAnswer_CheckedValid() {
        Throwable throwable = mock(Exception.class);
        when(answer.getThrowable()).thenReturn(throwable);
        when(invocation.isValidException(any())).thenReturn(true);
        validator.validateException(answer, invocation);
        verify(reporter, never()).checkedExceptionInvalid(throwable);
    }

}
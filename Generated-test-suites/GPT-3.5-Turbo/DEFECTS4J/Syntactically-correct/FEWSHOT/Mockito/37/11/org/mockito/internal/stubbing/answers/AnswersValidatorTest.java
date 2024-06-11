package org.mockito.internal.stubbing.answers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Returns;
import org.mockito.stubbing.ThrowsException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class AnswersValidatorTest {

    private AnswersValidator answersValidator;
    private Reporter reporter;

    @Before
    public void setup() {
        answersValidator = new AnswersValidator();
        reporter = mock(Reporter.class);
        answersValidator.reporter = reporter;
    }

    @Test
    public void testValidate_DoNothing_WithVoidInvocation() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.isVoid()).thenReturn(true);

        answersValidator.validate(mock(DoesNothing.class), invocation);

        verify(reporter).onlyVoidMethodsCanBeSetToDoNothing();
    }

    @Test
    public void testValidate_DoNothing_WithNonVoidInvocation() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.isVoid()).thenReturn(false);

        answersValidator.validate(mock(DoesNothing.class), invocation);

        verify(reporter, never()).onlyVoidMethodsCanBeSetToDoNothing();
    }

    @Test
    public void testValidate_ReturnValue_VoidInvocation() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.isVoid()).thenReturn(true);

        answersValidator.validate(mock(Returns.class), invocation);

        verify(reporter).cannotStubVoidMethodWithAReturnValue();
    }

    @Test
    public void testValidate_ReturnValue_PrimitiveInvocation_ReturnsNull() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.isVoid()).thenReturn(false);
        when(invocation.returnsPrimitive()).thenReturn(true);
        Returns returnValue = mock(Returns.class);
        when(returnValue.returnsNull()).thenReturn(true);

        answersValidator.validate(returnValue, invocation);

        verify(reporter).wrongTypeOfReturnValue(invocation.printMethodReturnType(), "null", invocation.getMethodName());
    }

    @Test
    public void testValidate_ReturnValue_NonVoidInvocation_InvalidReturnType() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.isVoid()).thenReturn(false);
        when(invocation.returnsPrimitive()).thenReturn(false);
        Returns returnValue = mock(Returns.class);
        String returnType = "java.lang.String";
        when(invocation.printMethodReturnType()).thenReturn(returnType);
        when(returnValue.returnsNull()).thenReturn(false);
        when(returnValue.getReturnType()).thenReturn(String.class);
        when(invocation.isValidReturnType(String.class)).thenReturn(false);

        answersValidator.validate(returnValue, invocation);

        verify(reporter).wrongTypeOfReturnValue(returnType, returnType, invocation.getMethodName());
    }

    @Test
    public void testValidate_ReturnValue_NonVoidInvocation_ValidReturnType() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.isVoid()).thenReturn(false);
        when(invocation.returnsPrimitive()).thenReturn(false);
        Returns returnValue = mock(Returns.class);
        String returnType = "java.lang.String";
        when(invocation.printMethodReturnType()).thenReturn(returnType);
        when(returnValue.returnsNull()).thenReturn(false);
        when(returnValue.getReturnType()).thenReturn(String.class);
        when(invocation.isValidReturnType(String.class)).thenReturn(true);

        answersValidator.validate(returnValue, invocation);

        verify(reporter, never()).wrongTypeOfReturnValue(anyString(), anyString(), anyString());
    }

    @Test
    public void testValidate_Exception_NullThrowable() {
        Invocation invocation = mock(Invocation.class);

        answersValidator.validate(mock(ThrowsException.class), invocation);

        verify(reporter).cannotStubWithNullThrowable();
    }

    @Test
    public void testValidate_Exception_RuntimeExceptionOrError() {
        Invocation invocation = mock(Invocation.class);
        ThrowsException throwsException = mock(ThrowsException.class);
        Throwable throwable = mock(Throwable.class);
        when(throwsException.getThrowable()).thenReturn(throwable);
        when(throwable instanceof RuntimeException).thenReturn(true).thenReturn(false);
        when(throwable instanceof Error).thenReturn(true).thenReturn(false);

        answersValidator.validate(throwsException, invocation);
        answersValidator.validate(throwsException, invocation);

        verify(reporter, times(2)).cannotStubWithNullThrowable();
        verify(reporter, never()).checkedExceptionInvalid(any(Throwable.class));
    }

    @Test
    public void testValidate_Exception_ValidCheckedException() {
        Invocation invocation = mock(Invocation.class);
        ThrowsException throwsException = mock(ThrowsException.class);
        Throwable throwable = mock(Throwable.class);
        when(throwsException.getThrowable()).thenReturn(throwable);
        when(throwable instanceof RuntimeException).thenReturn(false);
        when(throwable instanceof Error).thenReturn(false);
        when(invocation.isValidException(throwable)).thenReturn(true);

        answersValidator.validate(throwsException, invocation);

        verify(reporter, never()).cannotStubWithNullThrowable();
        verify(reporter, never()).checkedExceptionInvalid(any(Throwable.class));
    }

    @Test
    public void testValidate_Exception_InvalidCheckedException() {
        Invocation invocation = mock(Invocation.class);
        ThrowsException throwsException = mock(ThrowsException.class);
        Throwable throwable = mock(Throwable.class);
        when(throwsException.getThrowable()).thenReturn(throwable);
        when(throwable instanceof RuntimeException).thenReturn(false);
        when(throwable instanceof Error).thenReturn(false);
        when(invocation.isValidException(throwable)).thenReturn(false);

        answersValidator.validate(throwsException, invocation);

        verify(reporter, never()).cannotStubWithNullThrowable();
        verify(reporter).checkedExceptionInvalid(throwable);
    }

}
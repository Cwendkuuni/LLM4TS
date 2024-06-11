package org.mockito.internal.stubbing.answers;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class AnswersValidatorTest {
    private AnswersValidator answersValidator;
    private Reporter reporter;

    @Before
    public void setUp() {
        answersValidator = new AnswersValidator();
        reporter = mock(Reporter.class);
        answersValidator.reporter = reporter;
    }

    @Test
    public void testValidate_ThrowsException() {
        ThrowsException answer = mock(ThrowsException.class);
        Invocation invocation = mock(Invocation.class);

        answersValidator.validate(answer, invocation);

        verify(reporter, never()).onlyVoidMethodsCanBeSetToDoNothing();
        verify(reporter, never()).cannotStubVoidMethodWithAReturnValue();
        verify(reporter, never()).wrongTypeOfReturnValue(anyString(), anyString(), anyString());
        verify(reporter, never()).cannotStubWithNullThrowable();
        verify(reporter, never()).checkedExceptionInvalid(any(Throwable.class));
    }

    @Test
    public void testValidate_Returns() {
        Returns answer = mock(Returns.class);
        Invocation invocation = mock(Invocation.class);

        when(invocation.isVoid()).thenReturn(false);
        when(invocation.returnsPrimitive()).thenReturn(false);
        when(answer.returnsNull()).thenReturn(false);
        when(invocation.isValidReturnType(answer.getReturnType())).thenReturn(false);

        answersValidator.validate(answer, invocation);

        verify(reporter, never()).onlyVoidMethodsCanBeSetToDoNothing();
        verify(reporter).wrongTypeOfReturnValue(anyString(), anyString(), anyString());
        verify(reporter, never()).cannotStubVoidMethodWithAReturnValue();
        verify(reporter, never()).cannotStubWithNullThrowable();
        verify(reporter, never()).checkedExceptionInvalid(any(Throwable.class));
    }

    @Test
    public void testValidate_Returns_Null_ReturnValue() {
        Returns answer = mock(Returns.class);
        Invocation invocation = mock(Invocation.class);

        when(invocation.isVoid()).thenReturn(false);
        when(invocation.returnsPrimitive()).thenReturn(true);
        when(answer.returnsNull()).thenReturn(true);

        answersValidator.validate(answer, invocation);

        verify(reporter, never()).onlyVoidMethodsCanBeSetToDoNothing();
        verify(reporter, never()).wrongTypeOfReturnValue(anyString(), anyString(), anyString());
        verify(reporter, never()).cannotStubVoidMethodWithAReturnValue();
        verify(reporter, never()).cannotStubWithNullThrowable();
        verify(reporter, never()).checkedExceptionInvalid(any(Throwable.class));
    }

    @Test
    public void testValidate_DoNothing() {
        DoesNothing answer = mock(DoesNothing.class);
        Invocation invocation = mock(Invocation.class);

        when(invocation.isVoid()).thenReturn(true);

        answersValidator.validate(answer, invocation);

        verify(reporter).onlyVoidMethodsCanBeSetToDoNothing();
        verify(reporter, never()).wrongTypeOfReturnValue(anyString(), anyString(), anyString());
        verify(reporter, never()).cannotStubVoidMethodWithAReturnValue();
        verify(reporter, never()).cannotStubWithNullThrowable();
        verify(reporter, never()).checkedExceptionInvalid(any(Throwable.class));
    }
}
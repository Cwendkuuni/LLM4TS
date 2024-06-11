package org.mockito.internal.stubbing.answers;

import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;

public class AnswersValidatorTest {

    private AnswersValidator answersValidator;

    private Reporter reporter;

    private Invocation invocation;

    private Answer<?> answer;

    @Test
    public void validate_ThrowsException_Answer_Valid_Exception() {
        answersValidator = new AnswersValidator();
        reporter = mock(Reporter.class);
        invocation = mock(Invocation.class);
        answer = mock(ThrowsException.class);
        
        when(answer instanceof ThrowsException).thenReturn(true);
        when(invocation.isValidException(any())).thenReturn(true);
        doNothing().when(reporter).checkedExceptionInvalid(any());

        answersValidator.validate(answer, invocation);

        verify(invocation, times(1)).isValidException(any());
        verify(reporter, times(0)).checkedExceptionInvalid(any());
    }

    @Test
    public void validate_ThrowsException_Answer_Null_Exception() {
        answersValidator = new AnswersValidator();
        reporter = mock(Reporter.class);
        invocation = mock(Invocation.class);
        answer = mock(ThrowsException.class);

        when(answer instanceof ThrowsException).thenReturn(true);
        when(invocation.isValidException(any())).thenReturn(true);
        doNothing().when(reporter).checkedExceptionInvalid(any());

        answersValidator.validate(answer, invocation);

        verify(invocation, times(1)).isValidException(any());
        verify(reporter, times(1)).checkedExceptionInvalid(null);
    }

    @Test
    public void validate_Returns_Answer_Valid_ReturnValue() {
        answersValidator = new AnswersValidator();
        reporter = mock(Reporter.class);
        invocation = mock(Invocation.class);
        answer = mock(Returns.class);

        when(answer instanceof Returns).thenReturn(true);
        when(invocation.isVoid()).thenReturn(false);
        when(invocation.returnsPrimitive()).thenReturn(false);
        when(answer.returnsNull()).thenReturn(false);
        when(invocation.isValidReturnType(any())).thenReturn(true);
        doNothing().when(reporter).wrongTypeOfReturnValue(any(), any(), any());

        answersValidator.validate(answer, invocation);

        verify(invocation, times(1)).isVoid();
        verify(invocation, times(1)).returnsPrimitive();
        verify(answer, times(1)).returnsNull();
        verify(invocation, times(1)).isValidReturnType(any());
        verify(reporter, times(0)).wrongTypeOfReturnValue(any(), any(), any());
    }

    @Test
    public void validate_Returns_Answer_Invalid_ReturnValue() {
        answersValidator = new AnswersValidator();
        reporter = mock(Reporter.class);
        invocation = mock(Invocation.class);
        answer = mock(Returns.class);

        when(answer instanceof Returns).thenReturn(true);
        when(invocation.isVoid()).thenReturn(false);
        when(invocation.returnsPrimitive()).thenReturn(false);
        when(answer.returnsNull()).thenReturn(false);
        when(invocation.isValidReturnType(any())).thenReturn(false);
        doNothing().when(reporter).wrongTypeOfReturnValue(any(), any(), any());

        answersValidator.validate(answer, invocation);

        verify(invocation, times(1)).isVoid();
        verify(invocation, times(0)).returnsPrimitive();
        verify(answer, times(1)).returnsNull();
        verify(invocation, times(1)).isValidReturnType(any());
        verify(reporter, times(1)).wrongTypeOfReturnValue(any(), any(), any());
    }

    @Test
    public void validate_Returns_Answer_Valid_Primitive_ReturnValueNull() {
        answersValidator = new AnswersValidator();
        reporter = mock(Reporter.class);
        invocation = mock(Invocation.class);
        answer = mock(Returns.class);

        when(answer instanceof Returns).thenReturn(true);
        when(invocation.isVoid()).thenReturn(false);
        when(invocation.returnsPrimitive()).thenReturn(true);
        when(answer.returnsNull()).thenReturn(true);
        doNothing().when(reporter).wrongTypeOfReturnValue(any(), any(), any());

        answersValidator.validate(answer, invocation);

        verify(invocation, times(1)).isVoid();
        verify(invocation, times(1)).returnsPrimitive();
        verify(answer, times(1)).returnsNull();
        verify(reporter, times(1)).wrongTypeOfReturnValue(any(), any(), any());
    }

    @Test
    public void validate_Returns_Answer_Valid_Primitive_ReturnValueNotNull() {
        answersValidator = new AnswersValidator();
        reporter = mock(Reporter.class);
        invocation = mock(Invocation.class);
        answer = mock(Returns.class);

        when(answer instanceof Returns).thenReturn(true);
        when(invocation.isVoid()).thenReturn(false);
        when(invocation.returnsPrimitive()).thenReturn(true);
        when(answer.returnsNull()).thenReturn(false);
        doNothing().when(reporter).wrongTypeOfReturnValue(any(), any(), any());

        answersValidator.validate(answer, invocation);

        verify(invocation, times(1)).isVoid();
        verify(invocation, times(1)).returnsPrimitive();
        verify(answer, times(1)).returnsNull();
        verify(reporter, times(0)).wrongTypeOfReturnValue(any(), any(), any());
    }

    @Test
    public void validate_DoNothing_Answer_Invalid_Method() {
        answersValidator = new AnswersValidator();
        reporter = mock(Reporter.class);
        invocation = mock(Invocation.class);
        answer = mock(DoesNothing.class);

        when(answer instanceof DoesNothing).thenReturn(true);
        when(invocation.isVoid()).thenReturn(false);
        doNothing().when(reporter).onlyVoidMethodsCanBeSetToDoNothing();

        answersValidator.validate(answer, invocation);

        verify(invocation, times(1)).isVoid();
        verify(reporter, times(1)).onlyVoidMethodsCanBeSetToDoNothing();
    }

    @Test
    public void validate_DoNothing_Answer_Valid_Method() {
        answersValidator = new AnswersValidator();
        reporter = mock(Reporter.class);
        invocation = mock(Invocation.class);
        answer = mock(DoesNothing.class);

        when(answer instanceof DoesNothing).thenReturn(true);
        when(invocation.isVoid()).thenReturn(true);
        doNothing().when(reporter).onlyVoidMethodsCanBeSetToDoNothing();

        answersValidator.validate(answer, invocation);

        verify(invocation, times(1)).isVoid();
        verify(reporter, times(0)).onlyVoidMethodsCanBeSetToDoNothing();
    }

}
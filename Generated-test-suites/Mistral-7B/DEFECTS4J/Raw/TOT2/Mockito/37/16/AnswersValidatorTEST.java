import org.junit.Test;
import org.mockito.internal.answers.AnswersValidator;
import org.mockito.internal.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.exceptions.Reporter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.assertThat;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doAnswerReturning;
import static org.mockito.Mockito.doAnswerThrowing;

public class AnswersValidatorTest {
    

    @Test
    public void testValidateDoNothing() {
        AnswersValidator validator = spy(new AnswersValidator());
        DoesNothing answer = mock(DoesNothing.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        validator.validate(answer, invocation);

        verifyNoMoreInteractions(answer, invocation, validator.reporter);
    }

    @Test
    public void testValidateDoNothing_VoidInvocation() {
        AnswersValidator validator = spy(new AnswersValidator());
        DoesNothing answer = mock(DoesNothing.class);
        InvocationOnMock voidInvocation = mock(InvocationOnMock.class);

        when(voidInvocation.isVoid()).thenReturn(true);

        validator.validate(answer, voidInvocation);

        verifyNoMoreInteractions(answer, invocation, validator.reporter);
    }

    @Test(expected = AssertionError.class)
    public void testValidateReturnValue_NullReturnNull() {
        AnswersValidator validator = spy(new AnswersValidator());
        Returns answer = mock(Returns.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        when(invocation.getReturnType()).thenReturn(String.class);
        when(invocation.isValidReturnType(any())).thenReturn(true);

        validator.validateReturnValue(answer, invocation);

        verifyNoMoreInteractions(answer, invocation, validator.reporter);
    }

    @Test(expected = AssertionError.class)
    public void testValidateReturnValue_NullReturnPrimitive() {
        AnswersValidator validator = spy(new AnswersValidator());
        Returns answer = mock(Returns.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        when(invocation.getReturnType()).thenReturn(int.class);
        when(invocation.isValidReturnType(any())).thenReturn(true);

        validator.validateReturnValue(answer, invocation);

        verify(validator.reporter).wrongTypeOfReturnValue(eq(int.class.getTypeName()), eq("null"), anyString());
    }

    @Test(expected = AssertionError.class)
    public void testValidateReturnValue_InvalidReturnType() {
        AnswersValidator validator = spy(new AnswersValidator());
        Returns answer = mock(Returns.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        when(invocation.getReturnType()).thenReturn(String.class);
        when(invocation.isValidReturnType(eq(Integer.class))).thenReturn(false);

        validator.validateReturnValue(answer, invocation);

        verify(validator.reporter).wrongTypeOfReturnValue(eq(String.class.getTypeName()), eq(Integer.class.getTypeName()), anyString());
    }

    @Test
    public void testValidateException_NullThrowable() {
        AnswersValidator validator = spy(new AnswersValidator());
        ThrowsException answer = mock(ThrowsException.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        validator.validateException(answer, invocation);

        verifyNoMoreInteractions(answer, invocation, validator.reporter);
    }

    @Test
    public void testValidateException_RuntimeExceptionOrError() {
        AnswersValidator validator = spy(new AnswersValidator());
        ThrowsException answer = mock(ThrowsException.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        Throwable throwable = new RuntimeException("Test exception");
        when(answer.getThrowable()).thenReturn(throwable);

        validator.validateException(answer, invocation);

        verifyNoMoreInteractions(answer, invocation, validator.reporter);
    }

    @Test(expected = AssertionError.class)
    public void testValidateException_InvalidExceptionType() {
        AnswersValidator validator = spy(new AnswersValidator());
        ThrowsException answer = mock(ThrowsException.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        Throwable throwable = new AssertionError("Test error");
        when(answer.getThrowable()).thenReturn(throwable);

        validator.validateException(answer, invocation);

        verify(validator.reporter).checkedExceptionInvalid(eq(AssertionError.class));
    }

    @Test
    public void testValidateException_ValidExceptionTypeAndVoidInvocation() {
        AnswersValidator validator = spy(new AnswersValidator());
        ThrowsException answer = mock(ThrowsException.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        when(invocation.isVoid()).thenReturn(true);

        Throwable throwable = new RuntimeException("Test exception");
        when(answer.getThrowable()).thenReturn(throwable);

        validator.validateException(answer, invocation);

        verifyNoMoreInteractions(answer, invocation, validator.reporter);
    }

    @Test(expected = AssertionError.class)
    public void testValidateException_InvalidThrowableType() {
        AnswersValidator validator = spy(new AnswersValidator());
        ThrowsException answer = mock(ThrowsException.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        Throwable throwable = new AssertionError("Test error");
        when(answer.getThrowable()).thenReturn(throwable);

        validator.validateException(answer, invocation);

        verify(validator.reporter).checkedExceptionInvalid(eq(AssertionError.class));
    }

    @Test
    public void testValidateException_ValidThrowableType() {
        AnswersValidator validator = spy(new AnswersValidator());
        ThrowsException answer = mock(ThrowsException.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        Throwable throwable = new RuntimeException("Test exception");
        when(answer.getThrowable()).thenReturn(throwable);

        Invocationinvocation = mock(InvocationOnMock.class);
        when(invocation.isValidException(any())).thenReturn(true);

        validator.validateException(answer, invocation);

        verifyNoMoreInteractions(answer, invocation, validator.reporter);
    }

    @Test
    public void testMockStaticReporter() {
        Reporter reporter = mockStatic(Reporter.class);

        AnswersValidator validator = new AnswersValidator();
        ThrowsException answer = mock(ThrowsException.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        Throwable throwable = new RuntimeException("Test exception");
        when(answer.getThrowable()).thenReturn(throwable);

        validator.validateException(answer, invocation);

        verify(reporter).checkedExceptionInvalid(eq(RuntimeException.class));
    }

    
}
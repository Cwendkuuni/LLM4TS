import org.junit.jupiter.api.Test;
import org.mockito.internal.stubbing.answers.AnswersValidator;
import org.mockito.internal.stubbing.answers.DoesNothing;
import org.mockito.internal.stubbing.answers.Returns;
import org.mockito.internal.stubbing.answers.ThrowsException;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

public class AnswersValidatorTest {

    @Test
    public void testValidate_ValidAnswer() {
        AnswersValidator validator = new AnswersValidator();
        Answer<Integer> answer = mock(Answer.class);
        Invocation invocation = new InvocationOnMock(answer);

        validator.validate(answer, invocation);

        verify(answer, never()).getThrowable();
        verify(answer, never()).getReturnValue();
    }

    @Test
    public void testValidate_ThrowsException() {
        AnswersValidator validator = new AnswersValidator();
        ThrowsException answer = mock(ThrowsException.class);
        Invocation invocation = new InvocationOnMock(answer);
        Throwable throwable = new RuntimeException("Test Exception");
        when(answer.getThrowable()).thenReturn(throwable);

        validator.validate(answer, invocation);

        verify(answer).getThrowable();
        verifyNoMoreInteractions(answer);
    }

    @Test
    public void testValidate_Returns() {
        AnswersValidator validator = new AnswersValidator();
        Returns answer = mock(Returns.class);
        Invocation invocation = new InvocationOnMock(answer);
        when(answer.getReturnValue()).thenReturn(1);

        validator.validate(answer, invocation);

        verify(answer, never()).getThrowable();
        verify(answer).getReturnValue();
    }

    @Test
    public void testValidate_ReturnsNullAndPrimitiveType() {
        AnswersValidator validator = new AnswersValidator();
        Returns answer = mock(Returns.class);
        Invocation invocation = new InvocationOnMock(answer);
        when(answer.getReturnValue()).thenReturn(null);

        assertThrows(AssertionError.class, () -> validator.validate(answer, invocation));
    }

    @Test
    public void testValidate_ReturnsNullAndObjectType() {
        AnswersValidator validator = new AnswersValidator();
        Returns answer = mock(Returns.class);
        Invocation invocation = new InvocationOnMock(answer);
        when(answer.getReturnValue()).thenReturn(new Object());

        validator.validate(answer, invocation);

        verify(answer, never()).getThrowable();
        verify(answer).getReturnValue();
    }

    @Test
    public void testValidate_ThrowsExceptionWithNullThrowable() {
        AnswersValidator validator = new AnswersValidator();
        ThrowsException answer = mock(ThrowsException.class);
        Invocation invocation = new InvocationOnMock(answer);
        when(answer.getThrowable()).thenReturn(null);

        assertThrows(AssertionError.class, () -> validator.validate(answer, invocation));
    }

    @Test
    public void testValidate_ThrowsExceptionWithInvalidExceptionType() {
        AnswersValidator validator = new AnswersValidator();
        ThrowsException answer = mock(ThrowsException.class);
        Invocation invocation = new InvocationOnMock(answer);
        when(answer.getThrowable()).thenReturn(new IOException("Test Exception"));

        assertThrows(AssertionError.class, () -> validator.validate(answer, invocation));
    }
}
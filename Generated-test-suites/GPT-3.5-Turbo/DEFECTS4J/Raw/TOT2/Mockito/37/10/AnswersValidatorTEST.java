import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.internal.stubbing.answers.*;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;

public class AnswersValidatorTest {

    @Test
    public void testValidateDoNothing() {
        AnswersValidator validator = new AnswersValidator();
        DoesNothing answer = mock(DoesNothing.class);
        Invocation invocation = mock(Invocation.class);

        validator.validateDoNothing(answer, invocation);

        verify(invocation).isVoid();

    }

    @Test
    public void testValidateReturnValue() {
        AnswersValidator validator = new AnswersValidator();
        Returns answer = mock(Returns.class);
        Invocation invocation = mock(Invocation.class);
        when(invocation.isVoid()).thenReturn(false);
        when(invocation.returnsPrimitive()).thenReturn(false);
        when(answer.returnsNull()).thenReturn(false);
        when(invocation.isValidReturnType(any())).thenReturn(true);

        validator.validateReturnValue(answer, invocation);

        verify(invocation).isVoid();
        verify(invocation).returnsPrimitive();
        verify(answer).returnsNull();
        verify(invocation).printMethodReturnType();
        verify(answer).printReturnType();
        verify(invocation).getMethodName();
    }

    @Test
    public void testValidateException() {
        AnswersValidator validator = new AnswersValidator();
        ThrowsException answer = mock(ThrowsException.class);
        Invocation invocation = mock(Invocation.class);
        Throwable throwable = mock(Throwable.class);
        when(answer.getThrowable()).thenReturn(throwable);
        when(throwable instanceof RuntimeException).thenReturn(false);
        when(throwable instanceof Error).thenReturn(false);
        when(invocation.isValidException(any())).thenReturn(true);

        validator.validateException(answer, invocation);

        verify(answer).getThrowable();
        verify(throwable).getClass();
        verify(invocation).isValidException(any());
    }
}
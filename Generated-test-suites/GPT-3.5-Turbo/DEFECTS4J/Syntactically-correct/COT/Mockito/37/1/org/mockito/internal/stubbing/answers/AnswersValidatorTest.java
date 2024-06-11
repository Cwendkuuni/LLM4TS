
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;
import org.mockito.internal.stubbing.answers.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AnswersValidatorTest {

    @Test
    public void testValidate_DoNothing() {
        DoesNothing answer = mock(DoesNothing.class);
        Invocation invocation = mock(Invocation.class);

        AnswersValidator validator = new AnswersValidator();

        validator.validate(answer, invocation);

        verify(invocation).isVoid();
        verify(answer).getReturnType();
    }

    @Test
    public void testValidate_Returns_Null() {
        Returns answer = mock(Returns.class);
        Invocation invocation = mock(Invocation.class);

        when(invocation.isVoid()).thenReturn(false);
        when(invocation.returnsPrimitive()).thenReturn(true);
        when(invocation.printMethodReturnType()).thenReturn("int");
        when(invocation.getMethodName()).thenReturn("methodName");
        when(answer.returnsNull()).thenReturn(true);

        AnswersValidator validator = new AnswersValidator();

        validator.validate(answer, invocation);

        verify(invocation).isVoid();
        verify(invocation).returnsPrimitive();
        verify(invocation).printMethodReturnType();
        verify(invocation).getMethodName();
        verify(answer).returnsNull();
        verify(answer).printReturnType();
    }

    @Test
    public void testValidate_Returns_WrongType() {
        Returns answer = mock(Returns.class);
        Invocation invocation = mock(Invocation.class);

        when(invocation.isVoid()).thenReturn(false);
        when(invocation.returnsPrimitive()).thenReturn(false);
        when(invocation.printMethodReturnType()).thenReturn("String");
        when(invocation.getMethodName()).thenReturn("methodName");
        when(answer.returnsNull()).thenReturn(false);
        when(answer.getReturnType()).thenReturn(Integer.class);
        when(answer.printReturnType()).thenReturn("Integer");

        AnswersValidator validator = new AnswersValidator();

        validator.validate(answer, invocation);

        verify(invocation).isVoid();
        verify(invocation).returnsPrimitive();
        verify(invocation).printMethodReturnType();
        verify(invocation).getMethodName();
        verify(answer).returnsNull();
        verify(answer).getReturnType();
        verify(answer).printReturnType();
    }

    @Test
    public void testValidate_Exception_Null() {
        ThrowsException answer = mock(ThrowsException.class);
        Invocation invocation = mock(Invocation.class);

        when(answer.getThrowable()).thenReturn(null);

        AnswersValidator validator = new AnswersValidator();

        validator.validate(answer, invocation);

        verify(answer).getThrowable();
    }

    @Test
    public void testValidate_Exception_RuntimeException() {
        ThrowsException answer = mock(ThrowsException.class);
        Invocation invocation = mock(Invocation.class);
        Throwable throwable = mock(RuntimeException.class);

        when(answer.getThrowable()).thenReturn(throwable);
        when(throwable instanceof RuntimeException || throwable instanceof Error).thenReturn(true);

        AnswersValidator validator = new AnswersValidator();

        validator.validate(answer, invocation);

        verify(answer).getThrowable();
    }

    @Test
    public void testValidate_Exception_CheckedExceptionInvalid() {
        ThrowsException answer = mock(ThrowsException.class);
        Invocation invocation = mock(Invocation.class);
        Throwable throwable = mock(Exception.class);
        Reporter reporter = mock(Reporter.class);

        when(answer.getThrowable()).thenReturn(throwable);
        when(throwable instanceof RuntimeException || throwable instanceof Error).thenReturn(false);
        when(invocation.isValidException(throwable)).thenReturn(false);

        AnswersValidator validator = new AnswersValidator();
        validator.setReporter(reporter);

        validator.validate(answer, invocation);

        verify(answer).getThrowable();
        verify(invocation).isValidException(throwable);
        verify(reporter).checkedExceptionInvalid(throwable);
    }
}

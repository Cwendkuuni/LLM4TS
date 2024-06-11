import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;
import org.mockito.internal.stubbing.answers.AnswersValidator;
import org.mockito.internal.stubbing.answers.ThrowsException;
import org.mockito.internal.stubbing.answers.Returns;
import org.mockito.internal.stubbing.answers.DoesNothing;

import static org.mockito.Mockito.*;

public class AnswersValidatorTest {

    private AnswersValidator answersValidator;
    private Reporter reporter;

    @Before
    public void setUp() {
        answersValidator = new AnswersValidator();
        reporter = mock(Reporter.class);
        answersValidator.setReporter(reporter);
    }

    @Test
    public void testValidate_ThrowsException() {
        Answer<?> answer = mock(ThrowsException.class);
        Invocation invocation = mock(Invocation.class);

        answersValidator.validate(answer, invocation);

        verify(reporter, times(1)).cannotStubWithNullThrowable();
    }

    @Test
    public void testValidate_Returns() {
        Answer<?> answer = mock(Returns.class);
        Invocation invocation = mock(Invocation.class);

        answersValidator.validate(answer, invocation);

        verify(reporter, times(1)).cannotStubVoidMethodWithAReturnValue();
    }

    @Test
    public void testValidate_DoesNothing() {
        Answer<?> answer = mock(DoesNothing.class);
        Invocation invocation = mock(Invocation.class);

        answersValidator.validate(answer, invocation);

        verify(reporter, times(1)).onlyVoidMethodsCanBeSetToDoNothing();
    }

    @Test
    public void testValidateReturnValue_ThrowsException() {
        Returns answer = mock(Returns.class);
        Invocation invocation = mock(Invocation.class);
        when(invocation.isVoid()).thenReturn(false);
        when(answer.returnsNull()).thenReturn(false);
        when(invocation.getMethodName()).thenReturn("methodName");
        when(invocation.printMethodReturnType()).thenReturn("String");
        when(answer.printReturnType()).thenReturn("Integer");

        answersValidator.validateReturnValue(answer, invocation);

        verify(reporter, times(1)).wrongTypeOfReturnValue("String", "Integer", "methodName");
    }

    @Test
    public void testValidateReturnValue_VoidMethodWithReturnValue() {
        Returns answer = mock(Returns.class);
        Invocation invocation = mock(Invocation.class);
        when(invocation.isVoid()).thenReturn(true);
        when(invocation.getMethodName()).thenReturn("methodName");

        answersValidator.validateReturnValue(answer, invocation);

        verify(reporter, times(1)).cannotStubVoidMethodWithAReturnValue();
    }

    @Test
    public void testValidateReturnValue_ReturnsNullForPrimitive() {
        Returns answer = mock(Returns.class);
        Invocation invocation = mock(Invocation.class);
        when(invocation.isVoid()).thenReturn(false);
        when(answer.returnsNull()).thenReturn(true);
        when(invocation.returnsPrimitive()).thenReturn(true);
        when(invocation.getMethodName()).thenReturn("methodName");
        when(invocation.printMethodReturnType()).thenReturn("int");

        answersValidator.validateReturnValue(answer, invocation);

        verify(reporter, times(1)).wrongTypeOfReturnValue("int", "null", "methodName");
    }

    @Test
    public void testValidateReturnValue_CorrectReturnType() {
        Returns answer = mock(Returns.class);
        Invocation invocation = mock(Invocation.class);
        when(invocation.isVoid()).thenReturn(false);
        when(answer.returnsNull()).thenReturn(false);
        when(invocation.isValidReturnType(any())).thenReturn(true);

        answersValidator.validateReturnValue(answer, invocation);

        verify(reporter, never()).wrongTypeOfReturnValue(any(), any(), any());
    }

    @Test
    public void testValidateException_NullThrowable() {
        ThrowsException answer = mock(ThrowsException.class);
        Invocation invocation = mock(Invocation.class);

        answersValidator.validateException(answer, invocation);

        verify(reporter, times(1)).cannotStubWithNullThrowable();
    }

    @Test
    public void testValidateException_RuntimeExceptionOrError() {
        ThrowsException answer = mock(ThrowsException.class);
        Invocation invocation = mock(Invocation.class);
        Throwable throwable = mock(Throwable.class);
        when(answer.getThrowable()).thenReturn(throwable);
        when(throwable instanceof RuntimeException || throwable instanceof Error).thenReturn(true);

        answersValidator.validateException(answer, invocation);

        verify(reporter, never()).checkedExceptionInvalid(any());
    }

    @Test
    public void testValidateException_InvalidCheckedException() {
        ThrowsException answer = mock(ThrowsException.class);
        Invocation invocation = mock(Invocation.class);
        Throwable throwable = mock(Throwable.class);
        when(answer.getThrowable()).thenReturn(throwable);
        when(invocation.isValidException(any())).thenReturn(false);

        answersValidator.validateException(answer, invocation);

        verify(reporter, times(1)).checkedExceptionInvalid(any());
    }
}
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;
import static org.mockito.Mockito.*;

public class AnswersValidatorTest {

    private AnswersValidator answersValidator;
    private Reporter reporter;
    private Answer<?> answer;
    private Invocation invocation;

    @Before
    public void setup() {
        answersValidator = new AnswersValidator();
        reporter = mock(Reporter.class);
        answer = mock(Answer.class);
        invocation = mock(Invocation.class);
        answersValidator.reporter = reporter;
    }

    @Test
    public void testValidate_DoNothing_OnlyVoidMethods() {
        DoesNothing doNothingAnswer = mock(DoesNothing.class);
        when(invocation.isVoid()).thenReturn(false);
        answersValidator.validate(doNothingAnswer, invocation);
        verify(reporter).onlyVoidMethodsCanBeSetToDoNothing();
    }

    @Test
    public void testValidate_ReturnValue_VoidMethod() {
        Returns returnsAnswer = mock(Returns.class);
        when(invocation.isVoid()).thenReturn(true);
        answersValidator.validate(returnsAnswer, invocation);
        verify(reporter).cannotStubVoidMethodWithAReturnValue();
    }

    @Test
    public void testValidate_ReturnValue_ReturnsNull_ReturnsPrimitive() {
        Returns returnsAnswer = mock(Returns.class);
        when(invocation.isVoid()).thenReturn(false);
        when(returnsAnswer.returnsNull()).thenReturn(true);
        when(invocation.returnsPrimitive()).thenReturn(true);
        when(invocation.printMethodReturnType()).thenReturn("int");
        when(invocation.getMethodName()).thenReturn("testMethod");
        answersValidator.validate(returnsAnswer, invocation);
        verify(reporter).wrongTypeOfReturnValue("int", "null", "testMethod");
    }

    @Test
    public void testValidate_ReturnValue_ReturnsNotNull_InvalidReturnType() {
        Returns returnsAnswer = mock(Returns.class);
        when(invocation.isVoid()).thenReturn(false);
        when(returnsAnswer.returnsNull()).thenReturn(false);
        when(invocation.isValidReturnType(any())).thenReturn(false);
        when(invocation.printMethodReturnType()).thenReturn("String");
        when(returnsAnswer.printReturnType()).thenReturn("Integer");
        when(invocation.getMethodName()).thenReturn("testMethod");
        answersValidator.validate(returnsAnswer, invocation);
        verify(reporter).wrongTypeOfReturnValue("String", "Integer", "testMethod");
    }

    @Test
    public void testValidate_Exception_ThrowableIsNull() {
        ThrowsException throwsExceptionAnswer = mock(ThrowsException.class);
        when(throwsExceptionAnswer.getThrowable()).thenReturn(null);
        answersValidator.validate(throwsExceptionAnswer, invocation);
        verify(reporter).cannotStubWithNullThrowable();
    }

    @Test
    public void testValidate_Exception_ThrowableIsRuntimeException() {
        ThrowsException throwsExceptionAnswer = mock(ThrowsException.class);
        Throwable throwable = mock(RuntimeException.class);
        when(throwsExceptionAnswer.getThrowable()).thenReturn(throwable);
        answersValidator.validate(throwsExceptionAnswer, invocation);
        verifyNoInteractions(reporter);
    }

    @Test
    public void testValidate_Exception_ThrowableIsError() {
        ThrowsException throwsExceptionAnswer = mock(ThrowsException.class);
        Throwable throwable = mock(Error.class);
        when(throwsExceptionAnswer.getThrowable()).thenReturn(throwable);
        answersValidator.validate(throwsExceptionAnswer, invocation);
        verifyNoInteractions(reporter);
    }

    @Test
    public void testValidate_Exception_InvalidCheckedException() {
        ThrowsException throwsExceptionAnswer = mock(ThrowsException.class);
        Throwable throwable = mock(Exception.class);
        when(throwsExceptionAnswer.getThrowable()).thenReturn(throwable);
        when(invocation.isValidException(any())).thenReturn(false);
        answersValidator.validate(throwsExceptionAnswer, invocation);
        verify(reporter).checkedExceptionInvalid(throwable);
    }

}
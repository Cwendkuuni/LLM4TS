import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;
import org.mockito.internal.stubbing.answers.AnswersValidator;
import org.mockito.internal.stubbing.answers.Returns;
import org.mockito.internal.stubbing.answers.DoesNothing;
import org.mockito.internal.stubbing.answers.ThrowsException;
import static org.mockito.Mockito.*;

public class AnswersValidatorTest {

    private AnswersValidator answersValidator = new AnswersValidator();
    private Reporter reporter = mock(Reporter.class);
    private Answer<?> answer;
    private Invocation invocation;

    @Test
    public void testValidate_ThrowsException() {
        answer = mock(ThrowsException.class);
        invocation = mock(Invocation.class);

        doAnswer(invocation -> {
            answersValidator.validate(answer, invocation.getArgument(0));
            return null;
        }).when(reporter).cannotStubWithNullThrowable();

        when(answer instanceof ThrowsException).thenReturn(true);
        when(((ThrowsException) answer).getThrowable()).thenReturn(null);

        answersValidator.validate(answer, invocation);

        verify(reporter).cannotStubWithNullThrowable();
    }

    @Test
    public void testValidate_Returns() {
        answer = mock(Returns.class);
        invocation = mock(Invocation.class);

        doAnswer(invocation -> {
            answersValidator.validate(answer, invocation.getArgument(0));
            return null;
        }).when(reporter).cannotStubVoidMethodWithAReturnValue();

        when(answer instanceof Returns).thenReturn(true);
        when(invocation.isVoid()).thenReturn(true);

        answersValidator.validate(answer, invocation);

        verify(reporter).cannotStubVoidMethodWithAReturnValue();
    }

    @Test
    public void testValidate_DoNothing() {
        answer = mock(DoesNothing.class);
        invocation = mock(Invocation.class);

        doAnswer(invocation -> {
            answersValidator.validate(answer, invocation.getArgument(0));
            return null;
        }).when(reporter).onlyVoidMethodsCanBeSetToDoNothing();

        when(answer instanceof DoesNothing).thenReturn(true);
        when(invocation.isVoid()).thenReturn(false);

        answersValidator.validate(answer, invocation);

        verify(reporter).onlyVoidMethodsCanBeSetToDoNothing();
    }

    @Test
    public void testValidateReturnValue_ReturnsNullWithPrimitiveInvocation() {
        answer = mock(Returns.class);
        invocation = mock(Invocation.class);

        doAnswer(invocation -> {
            answersValidator.validate(answer, invocation.getArgument(0));
            return null;
        }).when(reporter).wrongTypeOfReturnValue(anyString(), eq("null"), anyString());

        when(answer instanceof Returns).thenReturn(true);
        when(((Returns) answer).returnsNull()).thenReturn(true);
        when(invocation.returnsPrimitive()).thenReturn(true);
        when(invocation.printMethodReturnType()).thenReturn("int");
        when(invocation.getMethodName()).thenReturn("methodName");

        answersValidator.validate(answer, invocation);

        verify(reporter).wrongTypeOfReturnValue(anyString(), eq("null"), anyString());
    }

    @Test
    public void testValidateReturnValue_ReturnsNotNullWithInvalidReturnType() {
        answer = mock(Returns.class);
        invocation = mock(Invocation.class);

        doAnswer(invocation -> {
            answersValidator.validate(answer, invocation.getArgument(0));
            return null;
        }).when(reporter).wrongTypeOfReturnValue(anyString(), anyString(), anyString());

        when(answer instanceof Returns).thenReturn(true);
        when(((Returns) answer).returnsNull()).thenReturn(false);
        when(((Returns) answer).getReturnType()).thenReturn("String");
        when(invocation.isValidReturnType(anyString())).thenReturn(false);
        when(invocation.printMethodReturnType()).thenReturn("int");
        when(invocation.getMethodName()).thenReturn("methodName");

        answersValidator.validate(answer, invocation);

        verify(reporter).wrongTypeOfReturnValue(anyString(), anyString(), anyString());
    }

    @Test
    public void testValidateException_NullThrowable() {
        answer = mock(ThrowsException.class);
        invocation = mock(Invocation.class);

        doAnswer(invocation -> {
            answersValidator.validate(answer, invocation.getArgument(0));
            return null;
        }).when(reporter).cannotStubWithNullThrowable();

        when(answer instanceof ThrowsException).thenReturn(true);
        when(((ThrowsException) answer).getThrowable()).thenReturn(null);

        answersValidator.validate(answer, invocation);

        verify(reporter).cannotStubWithNullThrowable();
    }

    @Test
    public void testValidateException_CheckedInvalidException() {
        answer = mock(ThrowsException.class);
        invocation = mock(Invocation.class);

        doAnswer(invocation -> {
            answersValidator.validate(answer, invocation.getArgument(0));
            return null;
        }).when(reporter).checkedExceptionInvalid(any(Throwable.class));

        when(answer instanceof ThrowsException).thenReturn(true);
        when(((ThrowsException) answer).getThrowable()).thenReturn(mock(Exception.class));
        when(invocation.isValidException(any(Throwable.class))).thenReturn(false);

        answersValidator.validate(answer, invocation);

        verify(reporter).checkedExceptionInvalid(any(Throwable.class));
    }

    @Test
    public void testValidateException_UncheckedExceptionOrError() {
        answer = mock(ThrowsException.class);
        invocation = mock(Invocation.class);

        when(answer instanceof ThrowsException).thenReturn(true);
        when(((ThrowsException) answer).getThrowable()).thenReturn(mock(RuntimeException.class));

        answersValidator.validate(answer, invocation);

        verify(invocation, never()).isValidException(any(Throwable.class));
    }
}


import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;

public class AnswersValidatorTest {

    private AnswersValidator answersValidator;
    private Reporter reporter;

    @Before
    public void setup() {
        answersValidator = new AnswersValidator();
        reporter = mock(Reporter.class);
    }

    @Test
    public void testValidate_ThrowsException() {
        Answer<?> answer = mock(ThrowsException.class);
        Invocation invocation = mock(Invocation.class);

        answersValidator.validate(answer, invocation);

        verify(reporter, never()).onlyVoidMethodsCanBeSetToDoNothing();
        verify(reporter, never()).cannotStubVoidMethodWithAReturnValue();

        verify(invocation, never()).isVoid();
        verify(invocation, never()).returnsPrimitive();
        verify(invocation, never()).printMethodReturnType();
        verify(invocation, never()).isValidReturnType(any());
        verify(invocation, never()).getMethodType();

        verify(answer).getThrowable();
        verify(reporter, never()).cannotStubWithNullThrowable();
        verify(invocation, never()).isValidException(any());
        verify(reporter, never()).checkedExceptionInvalid(any());
    }

    @Test
    public void testValidate_Returns() {
        Answer<?> answer = mock(Returns.class);
        Invocation invocation = mock(Invocation.class);

        answersValidator.validate(answer, invocation);

        verify(reporter, never()).onlyVoidMethodsCanBeSetToDoNothing();
        verify(answer, never()).returnsNull();
        verify(invocation, never()).isVoid();
        verify(reporter, never()).cannotStubVoidMethodWithAReturnValue();
        verify(invocation, never()).returnsPrimitive();
        verify(invocation, never()).printMethodReturnType();
        verify(invocation, never()).isValidReturnType(any());
        verify(invocation, never()).getMethodType();

        verify(reporter, never()).wrongTypeOfReturnValue(any(), any(), any());
    }

    @Test
    public void testValidate_DoesNothing() {
        Answer<?> answer = mock(DoesNothing.class);
        Invocation invocation = mock(Invocation.class);

        answersValidator.validate(answer, invocation);

        verify(answer, never()).returnsNull();
        verify(invocation).isVoid();
        verify(reporter, never()).cannotStubVoidMethodWithAReturnValue();
    }
}
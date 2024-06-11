import static org.mockito.Mockito.*;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;
import org.mockito.exceptions.Reporter;

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
        Answer<?> answer = mock(Answer.class);
        Invocation invocation = mock(Invocation.class);
        ThrowsException throwsException = mock(ThrowsException.class);

        when(answer instanceof ThrowsException).thenReturn(true);
        when((ThrowsException) answer).thenReturn(throwsException);

        answersValidator.validate(answer, invocation);

        verify(throwsException).getThrowable();
        verifyNoMoreInteractions(invocation, throwsException);
        verify(reporter, never()).cannotStubWithNullThrowable();
        verify(reporter, never()).checkedExceptionInvalid(any());
    }

    @Test
    public void testValidate_Returns() {
        Answer<?> answer = mock(Answer.class);
        Invocation invocation = mock(Invocation.class);
        Returns returns = mock(Returns.class);

        when(answer instanceof Returns).thenReturn(true);
        when((Returns) answer).thenReturn(returns);
        when(invocation.isVoid()).thenReturn(false);
        when(invocation.printMethodReturnType()).thenReturn("String");
        when(returns.returnsNull()).thenReturn(false);
        when(invocation.isValidReturnType(any())).thenReturn(true);

        answersValidator.validate(answer, invocation);

        verify(invocation).isVoid();
        verify(invocation).printMethodReturnType();
        verify(returns).returnsNull();
        verify(returns).printReturnType();
        verify(invocation).getMethodName();
        verify(invocation).returnsPrimitive();
        verify(invocation, never()).getMethodParameters();
        verify(returns, never()).getReturnType();
        verify(reporter, never()).cannotStubVoidMethodWithAReturnValue();
        verify(reporter, never()).wrongTypeOfReturnValue(any(), any(), any());
    }

    @Test
    public void testValidate_DoesNothing() {
        Answer<?> answer = mock(Answer.class);
        Invocation invocation = mock(Invocation.class);
        DoesNothing doesNothing = mock(DoesNothing.class);

        when(answer instanceof DoesNothing).thenReturn(true);
        when((DoesNothing) answer).thenReturn(doesNothing);
        when(invocation.isVoid()).thenReturn(true);

        answersValidator.validate(answer, invocation);

        verify(invocation).isVoid();
        verify(invocation, never()).printMethodReturnType();
        verify(doesNothing, never()).doNothing();
        verify(reporter, never()).onlyVoidMethodsCanBeSetToDoNothing();
    }
}


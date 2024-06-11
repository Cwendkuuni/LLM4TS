
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;

public class AnswersValidatorTest {

    private AnswersValidator validator;
    private Reporter reporter;
    private Answer<?> answer;
    private Invocation invocation;

    @Before
    public void setup() {
        validator = new AnswersValidator();
        reporter = mock(Reporter.class);
        validator.reporter = reporter;
        answer = mock(Answer.class);
        invocation = mock(Invocation.class);
    }

    @Test
    public void testValidate_ThrowsException() {
        answer = new ThrowsException();
        validator.validate(answer, invocation);
        verify(reporter).cannotStubWithNullThrowable();
        verify(reporter, never()).checkedExceptionInvalid(any(Throwable.class));
        verifyNoMoreInteractions(reporter);
    }

    @Test
    public void testValidate_Returns() {
        answer = new Returns();
        when(invocation.isVoid()).thenReturn(false);
        when(invocation.printMethodReturnType()).thenReturn("returnType");
        when(answer.getReturnType()).thenReturn("returnType");
        validator.validate(answer, invocation);
        verify(reporter, never()).cannotStubVoidMethodWithAReturnValue();
        verify(reporter, never()).wrongTypeOfReturnValue(anyString(), anyString(), anyString());
        verifyNoMoreInteractions(reporter);
    }

    @Test
    public void testValidate_DoNothing() {
        answer = new DoesNothing();
        when(invocation.isVoid()).thenReturn(true);
        validator.validate(answer, invocation);
        verify(reporter, never()).onlyVoidMethodsCanBeSetToDoNothing();
        verifyNoMoreInteractions(reporter);
    }

}

import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.stubbing.answers.AnswersValidator;
import org.mockito.stubbing.Answer;

public class AnswersValidatorTest {

    private AnswersValidator answersValidator;

    @Before
    public void setUp() {
        answersValidator = new AnswersValidator();
    }

    @Test
    public void testValidate_ThrowsException() {
        Answer answer = mock(ThrowsException.class);
        Invocation invocation = mock(Invocation.class);
        
        answersValidator.validate(answer, invocation);
        
        // Verify the appropriate methods are called
        verify(invocation, times(1)).isValidException(any(Throwable.class));
        verify(answer, times(1)).getThrowable();
    }

    @Test
    public void testValidate_Returns() {
        Answer answer = mock(Returns.class);
        Invocation invocation = mock(Invocation.class);
        
        answersValidator.validate(answer, invocation);
        
        // Verify the appropriate methods are called
        verify(invocation, times(1)).isVoid();
        verify(invocation, times(1)).returnsPrimitive();
        verify(invocation, times(1)).printMethodReturnType();
        verify(answer, times(1)).returnsNull();
        verify(answer, times(1)).getReturnType();
        verify(answer, times(1)).printReturnType();
    }

    @Test
    public void testValidate_DoesNothing() {
        Answer answer = mock(DoesNothing.class);
        Invocation invocation = mock(Invocation.class);
        
        answersValidator.validate(answer, invocation);
        
        // Verify the appropriate methods are called
        verify(invocation, times(1)).isVoid();
    }

}
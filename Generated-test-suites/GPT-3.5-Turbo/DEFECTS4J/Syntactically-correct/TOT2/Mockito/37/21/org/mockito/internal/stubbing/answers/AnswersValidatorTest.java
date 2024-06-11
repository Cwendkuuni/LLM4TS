import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;
import org.mockito.internal.stubbing.answers.AnswersValidator;
import org.mockito.internal.stubbing.answers.Returns;
import org.mockito.internal.stubbing.answers.ThrowsException;
import org.mockito.internal.stubbing.answers.DoesNothing;

public class AnswersValidatorTest {

    private AnswersValidator answersValidator;
    private Reporter reporter;
    private Invocation invocation;
    
    @Before
    public void setUp() {
        answersValidator = new AnswersValidator();
        reporter = new Reporter();
        invocation = mock(Invocation.class);
    }
    
    @Test
    public void testValidateReturnValue() {
        Returns answer = new Returns("test");
        when(invocation.isVoid()).thenReturn(false);
        when(invocation.returnsPrimitive()).thenReturn(false);
        assertFalse(answer.returnsNull());
        assertTrue(invocation.isValidReturnType(answer.getReturnType()));
        answersValidator.validateReturnValue(answer, invocation);
    }
    
    @Test
    public void testValidateDoNothing() {
        DoesNothing answer = new DoesNothing();
        when(invocation.isVoid()).thenReturn(true);
        answersValidator.validateDoNothing(answer, invocation);
    }
    
    @Test
    public void testValidateException() {
        ThrowsException answer = new ThrowsException(new RuntimeException());
        when(invocation.isValidException(any())).thenReturn(true);
        answersValidator.validateException(answer, invocation);
    }
}
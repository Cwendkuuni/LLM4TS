import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;
import org.mockito.internal.stubbing.answers.AnswersValidator;
import org.mockito.internal.stubbing.answers.ThrowsException;
import org.mockito.internal.stubbing.answers.Returns;
import org.mockito.internal.stubbing.answers.DoesNothing;

public class AnswersValidatorTest {
    @Test
    public void testValidate() {
        AnswersValidator answersValidator = new AnswersValidator();
        Reporter reporter = new Reporter();
        
        // Test validate with ThrowsException
        ThrowsException throwsExceptionAnswer = new ThrowsException(new Exception());
        Invocation invocation1 = new Invocation(null, null, null, null);
        answersValidator.validate(throwsExceptionAnswer, invocation1);
        Throwable throwable1 = throwsExceptionAnswer.getThrowable();
        assertNotNull(throwable1);
        assertTrue(throwable1 instanceof Exception);
        assertTrue(invocation1.isValidException(throwable1));
        
        // Test validate with Returns
        Returns returnsAnswer = new Returns("return value");
        Invocation invocation2 = new Invocation(null, null, null, null);
        answersValidator.validate(returnsAnswer, invocation2);
        String returnValue2 = returnsAnswer.returnValue();
        assertEquals("return value", returnValue2);
        assertFalse(invocation2.isVoid());
        assertEquals("String", invocation2.printMethodReturnType());
        assertTrue(invocation2.isValidReturnType(returnsAnswer.getReturnType()));
        
        // Test validate with DoesNothing
        DoesNothing doesNothingAnswer = new DoesNothing();
        Invocation invocation3 = new Invocation(null, null, null, null);
        answersValidator.validate(doesNothingAnswer, invocation3);
        assertTrue(invocation3.isVoid());
    }
}
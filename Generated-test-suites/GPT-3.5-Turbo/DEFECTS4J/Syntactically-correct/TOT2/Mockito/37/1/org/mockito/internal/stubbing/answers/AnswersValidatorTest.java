import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;

public class AnswersValidatorTest {

    @Test
    public void testValidateDoNothing() {
        AnswersValidator validator = new AnswersValidator();
        Reporter reporter = new Reporter();
        Invocation invocation = new Invocation();
        validator.validateDoNothing(new DoesNothing(), invocation);
        assertTrue(reporter.getErrorMessage().contains("Only void methods can be set to do nothing"));
    }

    @Test
    public void testValidateReturnValue() {
        AnswersValidator validator = new AnswersValidator();
        Reporter reporter = new Reporter();
        Invocation invocation = new Invocation();
        Returns answer = new Returns();
        validator.validateReturnValue(answer, invocation);
        assertTrue(reporter.getErrorMessage().contains("Cannot stub void method with a return value"));
    }

    @Test
    public void testValidateException() {
        AnswersValidator validator = new AnswersValidator();
        Reporter reporter = new Reporter();
        Invocation invocation = new Invocation();
        ThrowsException answer = new ThrowsException(null);
        validator.validateException(answer, invocation);
        assertTrue(reporter.getErrorMessage().contains("Cannot stub with null throwable"));
    }
    
    @Test
    public void testValidateExceptionCheckedExceptionInvalid() {
        AnswersValidator validator = new AnswersValidator();
        Reporter reporter = new Reporter();
        Invocation invocation = new Invocation();
        ThrowsException answer = new ThrowsException(new Exception());
        validator.validateException(answer, invocation);
        assertTrue(reporter.getErrorMessage().contains("Checked exception is invalid"));
    }

}
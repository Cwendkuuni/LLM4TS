import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;

public class AnswersValidatorTest {

    @Test
    public void testValidate() {
        AnswersValidator validator = new AnswersValidator();
        Answer<?> answer = new Returns(null);
        Invocation invocation = new Invocation("test", Object.class, "method", new Object[0]);

        validator.validate(answer, invocation);
    }

    @Test
    public void testValidateDoNothing() {
        AnswersValidator validator = new AnswersValidator();
        DoesNothing answer = new DoesNothing();
        Invocation invocation = new Invocation("test", void.class, "method", new Object[0]);

        validator.validateDoNothing(answer, invocation);
    }

    @Test
    public void testValidateReturnValue() {
        AnswersValidator validator = new AnswersValidator();
        Returns answer = new Returns(null);
        Invocation invocation = new Invocation("test", Object.class, "method", new Object[0]);

        validator.validateReturnValue(answer, invocation);
    }

    @Test
    public void testValidateException() {
        AnswersValidator validator = new AnswersValidator();
        ThrowsException answer = new ThrowsException(new NullPointerException());
        Invocation invocation = new Invocation("test", Object.class, "method", new Object[0]);

        validator.validateException(answer, invocation);
    }

    @Test
    public void testValidateExceptionWithNullThrowable() {
        AnswersValidator validator = new AnswersValidator();
        ThrowsException answer = new ThrowsException(null);
        Invocation invocation = new Invocation("test", Object.class, "method", new Object[0]);

        validator.validateException(answer, invocation);
    }

    @Test
    public void testValidateExceptionWithCheckedException() {
        AnswersValidator validator = new AnswersValidator();
        ThrowsException answer = new ThrowsException(new Exception());
        Invocation invocation = new Invocation("test", Object.class, "method", new Object[0]);

        validator.validateException(answer, invocation);
    }

    @Test
    public void testValidateExceptionWithRuntimeOrError() {
        AnswersValidator validator = new AnswersValidator();
        ThrowsException answer = new ThrowsException(new RuntimeException());
        Invocation invocation = new Invocation("test", Object.class, "method", new Object[0]);

        validator.validateException(answer, invocation);
    }

}
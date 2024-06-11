import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.internal.stubbing.answers.AnswersValidator;
import org.mockito.internal.stubbing.answers.DoesNothing;
import org.mockito.internal.stubbing.answers.Returns;
import org.mockito.internal.stubbing.answers.ThrowsException;
import org.mockito.invocation.InvocationOnMock;

public class AnswersValidatorTest {
    @Test
    public void testValidate() {
        AnswersValidator validator = new AnswersValidator();
        Returns returnsAnswer = new Returns("test");
        ThrowsException throwsExceptionAnswer = new ThrowsException(new RuntimeException("test"));
        DoesNothing doesNothingAnswer = new DoesNothing();
        Invocation invocation = new InvocationOnMock(new Object(), new Object[0]);

        validator.validate(returnsAnswer, invocation);
        validator.validate(throwsExceptionAnswer, invocation);
        validator.validate(doesNothingAnswer, invocation);

        Assertions.assertDoesNotThrow(validator::validate); // valid case
    }
}
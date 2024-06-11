import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AnswersValidatorTest {

    @Test
    public void testValidate_Returns() {
        Answer<String> answer = Mockito.returns("Hello");
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

        AnswersValidator validator = new AnswersValidator();
        validator.validate(answer, invocation);

        // No exception thrown, test passes
    }
}
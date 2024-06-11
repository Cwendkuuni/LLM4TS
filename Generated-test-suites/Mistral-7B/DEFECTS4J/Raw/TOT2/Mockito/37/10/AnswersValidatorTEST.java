import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

public class AnswersValidatorTest {

    private AnswersValidator validator;

    @Captor
    private ArgumentCaptor<Invocation> invocationCaptor;

    @BeforeEach
    public void setup() {
        validator = new AnswersValidator();
    }

    @Test
    public void testValidate_ValidAnswer() {
        Answer<?> answer = Mockito.mock(Answer.class);
        Invocation invocation = Mockito.mock(Invocation.class);

        validator.validate(answer, invocation);

        verify(answer, times(1)).answer(invocationCaptor.capture());
    }
}
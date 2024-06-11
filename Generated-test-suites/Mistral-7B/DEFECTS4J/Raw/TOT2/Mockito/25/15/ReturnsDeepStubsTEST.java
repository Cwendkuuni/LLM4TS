import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() {
        ReturnsDeepStubs answer = new ReturnsDeepStubs();
        Object mock = Mockito.mock(Object.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

        Object result = answer.answer(invocation);
        assertEquals(mock, result);
    }
}
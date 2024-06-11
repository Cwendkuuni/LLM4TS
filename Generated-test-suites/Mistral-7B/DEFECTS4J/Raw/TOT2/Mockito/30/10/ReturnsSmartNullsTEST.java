import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsSmartNullsTest {
    @Test
    public void testAnswer() {
        Answer<Object> answer = mock(Answer.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object result = returnsSmartNulls.answer(invocation);

        verify(answer).answer(invocation);
        assertNull(result);
    }
}
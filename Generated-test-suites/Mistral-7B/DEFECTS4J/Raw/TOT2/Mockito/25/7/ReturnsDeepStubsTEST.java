import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.assertEquals;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() {
        Object mock = Mockito.mock(Object.class, new ReturnsDeepStubs());
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

        Answer<Object> answer = new ReturnsDeepStubs();
        Object result = answer.answer(invocation);

        assertEquals(mock, result);
    }
}
import org.mockito.Answers;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

        Object result = new ReturnsDeepStubs().answer(invocation);

        // Check the result is the same instance of the mocked object
        verify(mock).answer(ArgumentMatchers.eq(invocation));
        assertEquals(mock, result);
    }
}
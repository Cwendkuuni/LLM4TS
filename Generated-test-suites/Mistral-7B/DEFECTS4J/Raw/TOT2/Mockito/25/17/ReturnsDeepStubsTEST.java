import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() {
        Answer<Object> returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

        Object mock = mock(Object.class);

        // Mockito will call ReturnsDeepStubs.answer()
        Object result = returnsDeepStubs.answer(invocation);

        // Verify that the answer returned a deep stub
        verify(mock).answer(invocation);

        assertEquals(mock, result);
    }
}
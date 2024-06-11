import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() {
        Object mock = Mockito.mock(Object.class, new ReturnsDeepStubs());
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        when(mock.answer(invocation)).thenCallRealMethod();

        Object result = mock.answer(invocation);
        assertEquals(mock, result);
    }
}
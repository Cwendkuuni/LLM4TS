import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsDeepStubsTest {
    
    private ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

    @Test
    public void testAnswer() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        assertNotNull(returnsDeepStubs.answer(invocation));
    }
    
    @Test
    public void testGetMock() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        assertNotNull(returnsDeepStubs.getMock(invocation));
    }
    
    @Test
    public void testRecordDeepStubMock() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        assertNotNull(returnsDeepStubs.recordDeepStubMock(invocation, container));
    }
    
    @Test
    public void testActualParameterizedType() {
        Object mock = mock(Object.class);
        assertNotNull(returnsDeepStubs.actualParameterizedType(mock));
    }
    
}
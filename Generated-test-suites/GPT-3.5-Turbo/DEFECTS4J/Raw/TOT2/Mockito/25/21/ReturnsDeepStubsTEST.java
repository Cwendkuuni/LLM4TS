
import static org.junit.Assert.*;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsDeepStubs stubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        Object result = stubs.answer(invocation);
        
        assertNotNull(result);
    }
    
    @Test
    public void testGetMock() throws Throwable {
        ReturnsDeepStubs stubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        Object result = stubs.getMock(invocation);
        
        assertNotNull(result);
    }
    
    @Test
    public void testRecordDeepStubMock() throws Throwable {
        ReturnsDeepStubs stubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        
        Object result = stubs.recordDeepStubMock(invocation, container);
        
        assertNotNull(result);
    }
    
    @Test
    public void testActualParameterizedType() {
        ReturnsDeepStubs stubs = new ReturnsDeepStubs();
        Object mock = mock(Object.class);
        
        GenericMetadataSupport result = stubs.actualParameterizedType(mock);
        
        assertNotNull(result);
    }

}

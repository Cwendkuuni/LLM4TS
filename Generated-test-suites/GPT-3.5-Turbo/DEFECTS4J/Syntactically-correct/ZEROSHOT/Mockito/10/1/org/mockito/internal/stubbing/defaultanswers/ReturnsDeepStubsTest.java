import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    private ReturnsDeepStubs returnsDeepStubs;

    @Before
    public void setUp() {
        returnsDeepStubs = new ReturnsDeepStubs();
    }

    @Test
    public void testAnswer_withInvocationMatchingStubbedInvocation() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        
        Object result = returnsDeepStubs.answer(invocation);
        
        assertNotNull(result);
    }

    @Test
    public void testAnswer_withInvocationNotMatchingStubbedInvocation() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        
        Object result = returnsDeepStubs.answer(invocation);
        
        assertNotNull(result);
    }
    
}
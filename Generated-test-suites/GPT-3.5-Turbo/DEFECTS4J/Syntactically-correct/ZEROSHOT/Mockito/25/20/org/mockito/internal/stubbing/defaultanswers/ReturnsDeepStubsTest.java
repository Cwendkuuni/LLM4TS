import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.StubbedInvocationMatcher;

import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {
    
    private ReturnsDeepStubs returnsDeepStubs;
    private InvocationOnMock invocation;
    private StubbedInvocationMatcher stubbedInvocationMatcher;
    
    @Before
    public void setup() {
        returnsDeepStubs = new ReturnsDeepStubs();
        invocation = mock(InvocationOnMock.class);
        stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
    }
    
    @Test
    public void testAnswer_DelegateReturnValue() throws Throwable {
        when(invocation.getMock()).thenReturn(Object.class);
        when(invocation.getMethod()).thenReturn(Object.class.getMethod("toString"));
        
        Object result = returnsDeepStubs.answer(invocation);
        
        // assert delegate return value
        // TODO: assert result
        
        verify(invocation, times(1)).getMock();
        verify(invocation, times(1)).getMethod();
    }
    
    @Test
    public void testAnswer_ReturnDeepStubMock() throws Throwable {
        when(invocation.getMock()).thenReturn(Object.class);
        when(invocation.getMethod()).thenReturn(Object.class.getMethod("toString"));
        
        when(invocation.getMock()).thenReturn(Object.class);
        
        Object result = returnsDeepStubs.answer(invocation);
        
        // assert deep stub mock
        // TODO: assert result
        
        verify(invocation, times(2)).getMock();
        verify(invocation, times(1)).getMethod();
        
        verify(stubbedInvocationMatcher, times(1)).getInvocation();
    }
}
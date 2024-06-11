import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;

public class ReturnsDeepStubsTest {
    
    @Test
    public void testAnswer() throws Throwable {
        ReturnsDeepStubs stubs = new ReturnsDeepStubs();
        
        // Mock objects for testing
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        
        // Test when invocation method matches stubbed invocation
        when(mock1.equals(mock2)).thenReturn(true);
        assertEquals(true, stubs.answer(invocation(mock1, "equals", new Class<?>[] {Object.class}, new Object[] {mock2})));
        
        // Test when invocation method does not match any stubbed invocation
        when(mock1.equals(mock2)).thenReturn(false);
        assertEquals(null, stubs.answer(invocation(mock1, "hashCode", new Class<?>[] {}, new Object[] {})));
        
        // Test when returnTypeGenericMetadata is mockable
        when(mock1.toString()).thenReturn("Mock Object");
        assertEquals("Mock Object", stubs.answer(invocation(mock1, "toString", new Class<?>[] {}, new Object[] {})));
        
        // Test when returnTypeGenericMetadata is not mockable
        assertEquals(null, stubs.answer(invocation(mock2, "toString", new Class<?>[] {}, new Object[] {})));
    }
    
    private InvocationOnMock invocation(Object mock, String methodName, Class<?>[] paramTypes, Object[] args) throws Exception {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock);
        when(invocation.getMethod()).thenReturn(mock.getClass().getMethod(methodName, paramTypes));
        when(invocation.getArguments()).thenReturn(args);
        return invocation;
    }
}
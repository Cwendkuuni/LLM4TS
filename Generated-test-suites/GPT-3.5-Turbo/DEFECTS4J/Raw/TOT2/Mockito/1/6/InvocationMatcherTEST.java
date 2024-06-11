import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

public class InvocationMatcherTest {

    @Test
    public void testGetMethod() {
        // Setup
        Method expectedMethod = SomeClass.class.getDeclaredMethod("someMethod", String.class);
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(expectedMethod);
        
        // Test
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        // Verify
        assertEquals(expectedMethod, invocationMatcher.getMethod());
    }

    @Test
    public void testGetInvocation() {
        // Setup
        Invocation expectedInvocation = mock(Invocation.class);
        
        // Test
        InvocationMatcher invocationMatcher = new InvocationMatcher(expectedInvocation);
        
        // Verify
        assertEquals(expectedInvocation, invocationMatcher.getInvocation());
    }

    // Additional test cases for the remaining methods...

}
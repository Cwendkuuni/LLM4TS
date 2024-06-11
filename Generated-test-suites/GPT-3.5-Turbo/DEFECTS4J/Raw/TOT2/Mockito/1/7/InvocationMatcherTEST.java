import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {
    
    @Test
    public void testConstructorWithMatchers() {
        // Create a mock Invocation object
        Invocation invocation = mock(Invocation.class);
        
        // Create a list of Matchers
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(mock(Matcher.class));
        
        // Create an InvocationMatcher object
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        // Test if the Invocation object and Matchers are set correctly
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers.size(), invocationMatcher.getMatchers().size());
        assertEquals(matchers.get(0), invocationMatcher.getMatchers().get(0));
    }
    
    @Test
    public void testConstructorWithoutMatchers() {
        // Create a mock Invocation object
        Invocation invocation = mock(Invocation.class);
        
        // Create an InvocationMatcher object without Matchers
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        // Test if the Invocation object is set correctly and Matchers list is empty
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }
    
    @Test
    public void testGetMethod() {
        // Create a mock Method object
        Method method = mock(Method.class);
        
        // Create a mock Invocation object with the Method
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(method);
        
        // Create an InvocationMatcher object
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        // Test if the correct Method is returned
        assertEquals(method, invocationMatcher.getMethod());
    }
    
    // Add more test cases for other methods in the InvocationMatcher class

}
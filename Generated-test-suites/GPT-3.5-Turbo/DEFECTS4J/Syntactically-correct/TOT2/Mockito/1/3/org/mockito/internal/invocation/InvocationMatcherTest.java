import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.invocation.Invocation;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithMatchers() {
        // Create a mock Invocation object
        Invocation mockInvocation = mock(Invocation.class);
        
        // Create a list of Matchers
        List<Matcher> matchers = new ArrayList<>();
        // Add Matchers to the list
        
        // Create an InvocationMatcher object using the constructor with Matchers
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation, matchers);
        
        // Assert that the Invocation object and Matchers are set correctly
        assertEquals(mockInvocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithoutMatchers() {
        // Create a mock Invocation object
        Invocation mockInvocation = mock(Invocation.class);
        
        // Create an InvocationMatcher object using the constructor without Matchers
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation);
        
        // Assert that the Invocation object is set correctly and Matchers list is empty
        assertEquals(mockInvocation, invocationMatcher.getInvocation());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    // Add more test cases for other public methods in the InvocationMatcher class

}
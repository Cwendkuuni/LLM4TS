import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithMatchers() {
        // Mock objects and create Invocation object
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(mock(Matcher.class));
        
        // Create InvocationMatcher object using constructor with matchers
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithoutMatchers() {
        // Mock objects and create Invocation object
        Invocation invocation = mock(Invocation.class);
        
        // Create InvocationMatcher object using constructor without matchers
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(Collections.emptyList(), invocationMatcher.getMatchers());
    }

    @Test
    public void testMatches() {
        // Mock objects and create Invocation objects
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);

        // Create InvocationMatcher objects
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        assertTrue(invocationMatcher1.matches(invocation1));
        assertFalse(invocationMatcher1.matches(invocation2));
    }

    // Add more test cases for other methods as needed

}
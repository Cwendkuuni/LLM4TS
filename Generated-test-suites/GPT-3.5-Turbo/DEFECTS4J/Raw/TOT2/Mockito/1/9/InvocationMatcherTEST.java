import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

public class InvocationMatcherTest {

    @Test
    public void testGetMethod() {
        Invocation invocation = new Invocation();
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertEquals(invocation.getMethod(), matcher.getMethod());
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = new Invocation();
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertEquals(invocation, matcher.getInvocation());
    }

    @Test
    public void testGetMatchers() {
        Invocation invocation = new Invocation();
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertTrue(matcher.getMatchers().isEmpty());
    }

    @Test
    public void testToString() {
        Invocation invocation = new Invocation();
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertEquals("", matcher.toString()); // Assuming the default toString behavior
    }

    @Test
    public void testMatches() {
        Invocation invocation = new Invocation();
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertTrue(matcher.matches(invocation)); // Should return true for the same invocation
        assertFalse(matcher.matches(new Invocation())); // Should return false for a different invocation
    }

    // Additional test cases for edge cases and exception handling scenarios should be added here

}
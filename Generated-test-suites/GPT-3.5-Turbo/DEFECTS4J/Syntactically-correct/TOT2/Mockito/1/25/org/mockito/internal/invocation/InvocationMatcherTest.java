import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithMatchers() {
        // Test constructor with matchers
        // Create mock objects
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(mock(Matcher.class));

        // Create InvocationMatcher object
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // Assertion
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithoutMatchers() {
        // Test constructor without matchers
        // Create mock object
        Invocation invocation = mock(Invocation.class);

        // Create InvocationMatcher object
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Assertion
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testMatches() {
        // Test the matches method
        // Create mock objects
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);

        // Create InvocationMatcher object
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        // Assertion
        assertFalse(invocationMatcher.matches(invocation2));
    }

    // Add more test cases for other methods as needed
}
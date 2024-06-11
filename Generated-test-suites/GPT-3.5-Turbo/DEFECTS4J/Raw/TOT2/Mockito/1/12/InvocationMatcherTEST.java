import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithMatchers() {
        // Create mock objects for testing
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<Matcher>();

        // Call the constructor with matchers
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // Verify that the Invocation and Matchers are set correctly
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithoutMatchers() {
        // Create mock objects for testing
        Invocation invocation = mock(Invocation.class);

        // Call the constructor without matchers
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Verify that the Invocation and Matchers are set correctly
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(0, invocationMatcher.getMatchers().size());
    }

    // Add more test cases for other public methods as needed

}
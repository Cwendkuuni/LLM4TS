import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithMatchers() {
        // Create mock Invocation
        Invocation invocation = mock(Invocation.class);
        // Create mock Matchers
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(isA(String.class));

        // Create InvocationMatcher object
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // Check if InvocationMatcher was created correctly
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithoutMatchers() {
        // Create mock Invocation
        Invocation invocation = mock(Invocation.class);

        // Create InvocationMatcher object without Matchers
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Check if InvocationMatcher was created correctly with empty Matchers
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testMatches() {
        // Create mock Invocation objects
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);

        // Mock the behavior for matches method
        when(invocation1.getMock()).thenReturn(new Object());
        when(invocation2.getMock()).thenReturn(new Object());
        when(invocation1.getArguments()).thenReturn(new Object[] {"arg1", "arg2"});
        when(invocation2.getArguments()).thenReturn(new Object[] {"arg1", "arg2"});

        // Create InvocationMatcher objects
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        // Check if matches method works correctly
        assertTrue(invocationMatcher1.matches(invocation2));
    }

    // Add more test cases for other methods as needed

}


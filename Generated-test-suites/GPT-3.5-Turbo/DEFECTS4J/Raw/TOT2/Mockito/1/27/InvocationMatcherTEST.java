import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.invocation.Invocation;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithMatchers() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.emptyList());
        assertNotNull(invocationMatcher);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testConstructorWithoutMatchers() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertNotNull(invocationMatcher);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    // Add more test cases for other public methods in the class
    // Test edge cases and exception handling scenarios

}
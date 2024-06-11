import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.Any;
import org.mockito.internal.matchers.Equals;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithMatchers() {
        Invocation invocation = new Invocation();
        Any anyMatcher = new Any();
        Equals equalsMatcher = new Equals("test");
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(anyMatcher, equalsMatcher));
        
        assertEquals(invocation.getMethod(), invocationMatcher.getMethod());
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(Arrays.asList(anyMatcher, equalsMatcher), invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithoutMatchers() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertEquals(invocation.getMethod(), invocationMatcher.getMethod());
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testToString() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertNotNull(invocationMatcher.toString());
    }

    // Add more test cases for other methods in the class as needed

}
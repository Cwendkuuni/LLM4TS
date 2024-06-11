import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithMatchs() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertNotNull(invocationMatcher);
        assertEquals(invocation, invocationMatcher.getInvocation());
    }

    @Test
    public void testConstructorWithoutMatchers() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertNotNull(invocationMatcher);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testGetMethod() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertNotNull(invocationMatcher.getMethod());
        assertEquals(invocation.getMethod(), invocationMatcher.getMethod());
    }

    @Test
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertNotNull(invocationMatcher.toString());
    }

    // Add more test cases for other methods as needed

}
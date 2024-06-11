import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.MockHandler;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;

public class InvocationMatcherTest {

    private Invocation invocation;
    private InvocationMatcher invocationMatcher;

    @Before
    public void setUp() {
        MockHandler mockHandler = new MockHandler();
        Class[] paramTypes = {String.class, int.class};
        Object[] arguments = {"test", 1};
        invocation = new Invocation(mockHandler, paramTypes, arguments);
        invocationMatcher = new InvocationMatcher(invocation);
    }

    @Test
    public void testGetMethod() {
        assertEquals(invocation.getMethod(), invocationMatcher.getMethod());
    }

    @Test
    public void testGetInvocation() {
        assertEquals(invocation, invocationMatcher.getInvocation());
    }

    @Test
    public void testGetMatchers() {
        assertNotNull(invocationMatcher.getMatchers());
    }

    @Test
    public void testToString() {
        assertNotNull(invocationMatcher.toString());
    }

    @Test
    public void testMatches() {
        Invocation actual = new Invocation(new MockHandler(), new Class[]{String.class, int.class}, new Object[]{"test", 1});
        assertTrue(invocationMatcher.matches(actual));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation candidate = new Invocation(new MockHandler(), new Class[]{String.class, int.class}, new Object[]{"test", 1});
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSameMethod() {
        Invocation candidate = new Invocation(new MockHandler(), new Class[]{String.class, int.class}, new Object[]{"test", 1});
        assertTrue(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testGetLocation() {
        assertNotNull(invocationMatcher.getLocation());
    }

}
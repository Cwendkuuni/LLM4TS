import org.junit.Test;
import static org.junit.Assert.*;

public class InvocationMatcherTest {

    @Test
    public void testGetMethod() {
        Invocation invocation = new Invocation(null, null, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Method method = invocationMatcher.getMethod();
        assertNull(method);
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = new Invocation(null, null, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation actualInvocation = invocationMatcher.getInvocation();
        assertEquals(invocation, actualInvocation);
    }

    @Test
    public void testGetMatchers() {
        Invocation invocation = new Invocation(null, null, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        List<Matcher> matchers = invocationMatcher.getMatchers();
        assertNotNull(matchers);
        assertTrue(matchers.isEmpty());
    }

    @Test
    public void testToString() {
        Invocation invocation = new Invocation(null, null, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        String description = invocationMatcher.toString();
        assertNotNull(description);
    }

    @Test
    public void testMatches() {
        Invocation invocation = new Invocation(null, null, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean matches = invocationMatcher.matches(invocation);
        assertTrue(matches);
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = new Invocation(null, null, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation candidate = new Invocation(null, null, null);
        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidate);
        assertFalse(hasSimilarMethod);
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation = new Invocation(null, null, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation candidate = new Invocation(null, null, null);
        boolean hasSameMethod = invocationMatcher.hasSameMethod(candidate);
        assertFalse(hasSameMethod);
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = new Invocation(null, null, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Location location = invocationMatcher.getLocation();
        assertNull(location);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = new Invocation(null, null, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        invocationMatcher.captureArgumentsFrom(invocation);
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new LinkedList<Invocation>();
        Invocation invocation = new Invocation(null, null, null);
        invocations.add(invocation);
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        assertNotNull(invocationMatchers);
        assertEquals(1, invocationMatchers.size());
        assertEquals(invocation, invocationMatchers.get(0).getInvocation());
    }
}
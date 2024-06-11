import static org.junit.Assert.*;
import org.junit.Test;

public class InvocationMatcherTest {

    @Test
    public void testGetMethod() {
        // Test case for getMethod() method
        InvocationMatcher im = new InvocationMatcher(null);
        assertNull(im.getMethod());
    }

    @Test
    public void testGetInvocation() {
        // Test case for getInvocation() method
        Invocation invocation = new Invocation();
        invocation.setMock(null);
        InvocationMatcher im = new InvocationMatcher(invocation);
        assertEquals(invocation, im.getInvocation());
    }

    @Test
    public void testGetMatchers() {
        // Test case for getMatchers() method
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(null);
        InvocationMatcher im = new InvocationMatcher(null, matchers);
        assertEquals(matchers, im.getMatchers());
    }

    @Test
    public void testToString() {
        // Test case for toString() method
        Invocation invocation = new Invocation();
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(null);
        InvocationMatcher im = new InvocationMatcher(invocation, matchers);
        assertEquals("", im.toString());
    }

    @Test
    public void testMatches() {
        // Test case for matches() method
        Invocation invocation = new Invocation();
        Invocation actual = new Invocation();
        actual.setMock(null);
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(null);
        InvocationMatcher im = new InvocationMatcher(invocation, matchers);
        assertFalse(im.matches(actual));
    }

    @Test
    public void testHasSimilarMethod() {
        // Test case for hasSimilarMethod() method
        Invocation invocation = new Invocation();
        Invocation candidate = new Invocation();
        candidate.setMethod(null);
        InvocationMatcher im = new InvocationMatcher(invocation);
        assertFalse(im.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSameMethod() {
        // Test case for hasSameMethod() method
        Invocation invocation = new Invocation();
        Invocation candidate = new Invocation();
        candidate.setMethod(null);
        InvocationMatcher im = new InvocationMatcher(invocation);
        assertFalse(im.hasSameMethod(candidate));
    }

    @Test
    public void testGetLocation() {
        // Test case for getLocation() method
        Invocation invocation = new Invocation();
        invocation.setLocation(null);
        InvocationMatcher im = new InvocationMatcher(invocation);
        assertNull(im.getLocation());
    }

    @Test
    public void testCaptureArgumentsFrom() {
        // Test case for captureArgumentsFrom() method
        Invocation invocation = new Invocation();
        invocation.setMethod(new Method());
        InvocationMatcher im = new InvocationMatcher(invocation);
        im.captureArgumentsFrom(invocation);
    }

    @Test
    public void testCreateFrom() {
        // Test case for createFrom() method
        List<Invocation> invocations = new ArrayList<>();
        invocations.add(null);
        InvocationMatcher.createFrom(invocations);
    }
}
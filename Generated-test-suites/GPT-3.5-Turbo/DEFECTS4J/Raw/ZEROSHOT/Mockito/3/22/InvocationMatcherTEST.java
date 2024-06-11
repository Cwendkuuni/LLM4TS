import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.invocation.Invocation;

public class InvocationMatcherTest {
    
    @Test
    public void testInvocationMatcher() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertNotNull(invocationMatcher);
    }
    
    @Test
    public void testGetMethod() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Method method = invocationMatcher.getMethod();
        assertNotNull(method);
    }
    
    @Test
    public void testGetInvocation() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation actualInvocation = invocationMatcher.getInvocation();
        assertNotNull(actualInvocation);
    }
    
    @Test
    public void testGetMatchers() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        List<Matcher> matchers = invocationMatcher.getMatchers();
        assertNotNull(matchers);
    }
    
    @Test
    public void testToString() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        String string = invocationMatcher.toString();
        assertNotNull(string);
    }
    
    @Test
    public void testMatches() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation actualInvocation = new Invocation();
        boolean matches = invocationMatcher.matches(actualInvocation);
        assertTrue(matches);
    }
    
    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation candidate = new Invocation();
        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidate);
        assertTrue(hasSimilarMethod);
    }
    
    @Test
    public void testHasSameMethod() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation candidate = new Invocation();
        boolean hasSameMethod = invocationMatcher.hasSameMethod(candidate);
        assertTrue(hasSameMethod);
    }
    
    @Test
    public void testGetLocation() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Location location = invocationMatcher.getLocation();
        assertNotNull(location);
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation captureInvocation = new Invocation();
        invocationMatcher.captureArgumentsFrom(captureInvocation);
    }
    
    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new ArrayList<Invocation>();
        Invocation invocation = new Invocation();
        invocations.add(invocation);
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        assertNotNull(invocationMatchers);
    }
}
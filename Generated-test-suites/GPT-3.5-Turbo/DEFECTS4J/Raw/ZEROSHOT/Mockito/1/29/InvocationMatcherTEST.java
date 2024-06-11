import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.invocation.Invocation;

public class InvocationMatcherTest {
    
    @Test
    public void testConstructor() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertNotNull(invocationMatcher);
    }
    
    @Test
    public void testGetMethod() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertEquals(invocation.getMethod(), invocationMatcher.getMethod());
    }
    
    @Test
    public void testGetInvocation() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertEquals(invocation, invocationMatcher.getInvocation());
    }
    
    @Test
    public void testGetMatchers() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertEquals(matchers, invocationMatcher.getMatchers());
    }
    
    @Test
    public void testToString() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertNotNull(invocationMatcher.toString());
    }
    
    @Test
    public void testMatches() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Invocation actual = Mockito.mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertTrue(invocationMatcher.matches(actual));
    }
    
    @Test
    public void testSafelyArgumentsMatch() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Object[] actualArgs = new Object[]{};
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertFalse(invocationMatcher.safelyArgumentsMatch(actualArgs));
    }
    
    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Invocation candidate = Mockito.mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSameMethod() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Invocation candidate = Mockito.mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertFalse(invocationMatcher.hasSameMethod(candidate));
    }
    
    @Test
    public void testGetLocation() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertNotNull(invocationMatcher.getLocation());
    }
    
    @Test
    public void testCaptureArgumentsFrom() throws UnsupportedOperationException {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        invocationMatcher.captureArgumentsFrom(invocation);
    }
    
    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new ArrayList<>();
        Invocation invocation = Mockito.mock(Invocation.class);
        invocations.add(invocation);
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        assertNotNull(invocationMatchers);
        assertEquals(1, invocationMatchers.size());
    }
}
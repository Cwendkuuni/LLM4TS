import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;


public class InvocationMatcherTest {
    
    @Test
    public void testConstructorWithMatchers() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, null);
        
        assertNotNull(invocationMatcher);
        assertNotNull(invocationMatcher.getMethod());
        assertNotNull(invocationMatcher.getInvocation());
        assertNotNull(invocationMatcher.getMatchers());
    }
    
    @Test
    public void testConstructorWithoutMatchers() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertNotNull(invocationMatcher);
        assertNotNull(invocationMatcher.getMethod());
        assertNotNull(invocationMatcher.getInvocation());
        assertNotNull(invocationMatcher.getMatchers());
    }
    
    @Test
    public void testToString() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        String expected = "Matchers: [], Invocation: " + invocation;
        String result = invocationMatcher.toString();
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testMatches() {
        Invocation invocation1 = new Invocation();
        Invocation invocation2 = new Invocation();
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        
        boolean result1 = invocationMatcher.matches(invocation1);
        boolean result2 = invocationMatcher.matches(invocation2);
        
        assertTrue(result1);
        assertFalse(result2);
    }
    
    @Test
    public void testHasSimilarMethod() {
        Invocation invocation1 = new Invocation();
        Invocation invocation2 = new Invocation();
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        
        boolean result1 = invocationMatcher.hasSimilarMethod(invocation1);
        boolean result2 = invocationMatcher.hasSimilarMethod(invocation2);
        
        assertTrue(result1);
        assertTrue(result2);
    }
    
    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = new Invocation();
        Invocation invocation2 = new Invocation();
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        
        boolean result1 = invocationMatcher.hasSameMethod(invocation1);
        boolean result2 = invocationMatcher.hasSameMethod(invocation2);
        
        assertTrue(result1);
        assertFalse(result2);
    }
    
    @Test
    public void testGetLocation() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertNotNull(invocationMatcher.getLocation());
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        invocationMatcher.captureArgumentsFrom(invocation);
    }
    
    @Test
    public void testCreateFrom() {
        Invocation invocation1 = new Invocation();
        Invocation invocation2 = new Invocation();
        
        List<Invocation> invocations = Arrays.asList(invocation1, invocation2);
        
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        
        assertNotNull(invocationMatchers);
        assertEquals(invocations.size(), invocationMatchers.size());
        assertEquals(invocation1, invocationMatchers.get(0).getInvocation());
        assertEquals(invocation2, invocationMatchers.get(1).getInvocation());
    }
}
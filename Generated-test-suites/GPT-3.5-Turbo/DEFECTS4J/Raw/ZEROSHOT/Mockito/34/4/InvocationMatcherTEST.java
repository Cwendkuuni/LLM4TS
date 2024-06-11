import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.hamcrest.Matcher;

public class InvocationMatcherTest {

    @Test
    public void testGetMethod() {
        Invocation invocation = new Invocation();
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        
        Method method = matcher.getMethod();
        
        assertNull(method);
    }
    
    @Test
    public void testGetInvocation() {
        Invocation invocation = new Invocation();
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        
        Invocation actualInvocation = matcher.getInvocation();
        
        assertEquals(invocation, actualInvocation);
    }
    
    @Test
    public void testGetMatchers() {
        Invocation invocation = new Invocation();
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        
        List<Matcher> matchers = matcher.getMatchers();
        
        assertTrue(matchers.isEmpty());
    }
    
    @Test
    public void testToString() {
        Invocation invocation = new Invocation();
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        
        String toString = matcher.toString();
        
        assertNotNull(toString);
    }
    
    @Test
    public void testMatches() {
        Invocation invocation = new Invocation();
        Invocation actualInvocation = new Invocation();
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        
        boolean matches = matcher.matches(actualInvocation);
        
        assertFalse(matches);
    }
    
    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = new Invocation();
        Invocation candidate = new Invocation();
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        
        boolean hasSimilarMethod = matcher.hasSimilarMethod(candidate);
        
        assertFalse(hasSimilarMethod);
    }
    
    @Test
    public void testHasSameMethod() {
        Invocation invocation = new Invocation();
        Invocation candidate = new Invocation();
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        
        boolean hasSameMethod = matcher.hasSameMethod(candidate);
        
        assertFalse(hasSameMethod);
    }
    
    @Test
    public void testGetLocation() {
        Invocation invocation = new Invocation();
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        
        Location location = matcher.getLocation();
        
        assertNull(location);
    }
    
    @Test
    public void testToStringWithPrintSettings() {
        Invocation invocation = new Invocation();
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        
        PrintSettings settings = new PrintSettings();
        String toString = matcher.toString(settings);
        
        assertNotNull(toString);
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = new Invocation();
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        Invocation i = new Invocation();
        
        matcher.captureArgumentsFrom(i);
    }
}
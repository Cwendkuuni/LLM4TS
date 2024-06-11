import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.hamcrest.Matcher;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

public class InvocationMatcherTest {
    
    @Test
    public void testGetMethod() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = Collections.emptyList();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        Method method = invocationMatcher.getMethod();
        
        // Assert method is not null
        assertNotNull(method);
        
        // Assert method is same as the method of the invocation
        assertEquals(invocation.getMethod(), method);
    }
    
    @Test
    public void testGetInvocation() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = Collections.emptyList();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        Invocation result = invocationMatcher.getInvocation();
        
        // Assert result is not null
        assertNotNull(result);
        
        // Assert result is same as the invocation
        assertEquals(invocation, result);
    }
    
    @Test
    public void testGetMatchers() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = Collections.emptyList();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        List<Matcher> result = invocationMatcher.getMatchers();
        
        // Assert result is not null
        assertNotNull(result);
        
        // Assert result is same as the matchers provided
        assertEquals(matchers, result);
    }
    
    @Test
    public void testToString() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = Collections.emptyList();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        String result = invocationMatcher.toString();
        
        // Assert result is not null
        assertNotNull(result);
    }
    
    @Test
    public void testMatches() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = Collections.emptyList();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        Invocation actual = new Invocation();
        boolean result = invocationMatcher.matches(actual);
        
        // Assert result is false
        assertFalse(result);
    }
    
    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = Collections.emptyList();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        Invocation candidate = new Invocation();
        boolean result = invocationMatcher.hasSimilarMethod(candidate);
        
        // Assert result is false
        assertFalse(result);
    }
    
    @Test
    public void testHasSameMethod() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = Collections.emptyList();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        Invocation candidate = new Invocation();
        boolean result = invocationMatcher.hasSameMethod(candidate);
        
        // Assert result is false
        assertFalse(result);
    }
    
    @Test
    public void testGetLocation() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = Collections.emptyList();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        Location result = invocationMatcher.getLocation();
        
        // Assert result is not null
        assertNotNull(result);
    }
    
    @Test
    public void testToStringWithPrintSettings() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = Collections.emptyList();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        PrintSettings printSettings = new PrintSettings();
        String result = invocationMatcher.toString(printSettings);
        
        // Assert result is not null
        assertNotNull(result);
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        Invocation i = new Invocation();
        Invocation invocation = new Invocation();
        List<Matcher> matchers = Collections.singletonList(new CapturesArguments());
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        invocationMatcher.captureArgumentsFrom(i);
    }
}
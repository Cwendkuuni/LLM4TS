import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.matchers.CapturesArguments;
import org.hamcrest.Matcher;
import org.mockito.internal.debugging.Location;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

public class InvocationMatcherTest {

    @Test
    public void testGetMethod() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = Collections.emptyList();
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Method method = invocation.getMethod();
        assertEquals(method, invocationMatcher.getMethod());
    }
    
    @Test
    public void testGetInvocation() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = Collections.emptyList();
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertEquals(invocation, invocationMatcher.getInvocation());
    }
    
    @Test
    public void testGetMatchers() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = Collections.emptyList();
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertEquals(matchers, invocationMatcher.getMatchers());
    }
    
    @Test
    public void testToString() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = Collections.emptyList();
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        String expected = invocation.toString(matchers, new PrintSettings());
        assertEquals(expected, invocationMatcher.toString());
    }
    
    @Test
    public void testMatches() {
        Invocation invocation1 = new Invocation();
        Invocation invocation2 = new Invocation();
        
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        assertTrue(invocationMatcher1.matches(invocation1));
        assertFalse(invocationMatcher1.matches(invocation2));
    }
    
    @Test
    public void testSafelyArgumentsMatch() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = Collections.emptyList();
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertTrue(invocationMatcher.safelyArgumentsMatch(new Object[]{1, 2, 3}));
        assertFalse(invocationMatcher.safelyArgumentsMatch(new Object[]{1, 2, 4}));
    }
    
    @Test
    public void testHasSimilarMethod() {
        Invocation invocation1 = new Invocation();
        Invocation invocation2 = new Invocation();
        
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        assertTrue(invocationMatcher1.hasSimilarMethod(invocation1));
        assertFalse(invocationMatcher1.hasSimilarMethod(invocation2));
    }
    
    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = new Invocation();
        Invocation invocation2 = new Invocation();
        
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        assertTrue(invocationMatcher1.hasSameMethod(invocation1));
        assertFalse(invocationMatcher1.hasSameMethod(invocation2));
    }
    
    @Test
    public void testGetLocation() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = Collections.emptyList();
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertEquals(invocation.getLocation(), invocationMatcher.getLocation());
    }
    
    @Test
    public void testToStringWithPrintSettings() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = Collections.emptyList();
        PrintSettings printSettings = new PrintSettings();
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        String expected = invocation.toString(matchers, printSettings);
        assertEquals(expected, invocationMatcher.toString(printSettings));
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = Collections.singletonList((Matcher) new CapturesArguments());
        Invocation i = new Invocation();
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        invocationMatcher.captureArgumentsFrom(i);
        
        CapturesArguments capturesArguments = (CapturesArguments) matchers.get(0);
        assertNotNull(capturesArguments.getCapturedArguments());
    }
}
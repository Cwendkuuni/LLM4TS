
import org.hamcrest.Matcher;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockName;
import org.mockito.mock.MockCreationSettings;

public class InvocationMatcherTest {
    // Test public methods
    
    // Test constructor with matchers
    public void testInvocationMatcherConstructorWithMatchers() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }
    
    // Test constructor without matchers
    public void testInvocationMatcherConstructorWithoutMatchers() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(invocation.argumentsToMatchers(), invocationMatcher.getMatchers());
    }
    
    // Test getMethod
    public void testGetMethod() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(invocation.getMethod(), invocationMatcher.getMethod());
    }
    
    // Test getInvocation
    public void testGetInvocation() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(invocation, invocationMatcher.getInvocation());
    }
    
    // Test getMatchers
    public void testGetMatchers() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertEquals(matchers, invocationMatcher.getMatchers());
    }
    
    // Test toString
    public void testToString() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertEquals(invocation.toString(matchers, new PrintSettings()), 
                     invocationMatcher.toString());
    }
    
    // Test matches
    public void testMatches() {
        Invocation invocation1 = new Invocation();
        Invocation invocation2 = new Invocation();
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1, matchers);
        assertTrue(invocationMatcher.matches(invocation2));
    }
    
    // Test hasSimilarMethod
    public void testHasSimilarMethod() {
        Invocation invocation1 = new Invocation();
        Invocation invocation2 = new Invocation();
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1, matchers);
        assertTrue(invocationMatcher.hasSimilarMethod(invocation2));
    }
    
    // Test hasSameMethod
    public void testHasSameMethod() {
        Invocation invocation1 = new Invocation();
        Invocation invocation2 = new Invocation();
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1, matchers);
        assertTrue(invocationMatcher.hasSameMethod(invocation2));
    }
    
    // Test getLocation
    public void testGetLocation() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(invocation.getLocation(), invocationMatcher.getLocation());
    }
    
    // Test toString with print settings
    public void testToStringWithPrintSettings() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        PrintSettings printSettings = new PrintSettings();
        assertEquals(invocation.toString(matchers, printSettings),
                     invocationMatcher.toString(printSettings));
    }
    
    // Test captureArgumentsFrom
    public void testCaptureArgumentsFrom() {
        Invocation invocation1 = new Invocation();
        Invocation invocation2 = new Invocation();
        Matcher matcher = new Matcher();
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(matcher);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1, matchers);
        invocationMatcher.captureArgumentsFrom(invocation2);
        assertEquals(invocation2.getArguments()[0], matcher.getArgument());
    }
}

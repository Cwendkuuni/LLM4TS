
import org.junit.*;
import static org.junit.Assert.*;

public class InvocationMatcherTest {
    
    @Test
    public void testGetMethod() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Method expected = null; // enter expected value
        Method actual = invocationMatcher.getMethod();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetInvocation() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Invocation expected = invocation; // enter expected value
        Invocation actual = invocationMatcher.getInvocation();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetMatchers() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = new ArrayList<Matcher>();
        matchers.add(new Matcher());
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        List<Matcher> expected = matchers; // enter expected value
        List<Matcher> actual = invocationMatcher.getMatchers();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testToString() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        String expected = ""; // enter expected value
        String actual = invocationMatcher.toString();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMatches() {
        Invocation invocation = new Invocation();
        Invocation actualInvocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        boolean expected = false; // enter expected value
        boolean actual = invocationMatcher.matches(actualInvocation);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = new Invocation();
        Invocation candidate = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        boolean expected = false; // enter expected value
        boolean actual = invocationMatcher.hasSimilarMethod(candidate);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testHasSameMethod() {
        Invocation invocation = new Invocation();
        Invocation candidate = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        boolean expected = false; // enter expected value
        boolean actual = invocationMatcher.hasSameMethod(candidate);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetLocation() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Location expected = null; // enter expected value
        Location actual = invocationMatcher.getLocation();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testToStringWithPrintSettings() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        PrintSettings printSettings = new PrintSettings();
        
        String expected = ""; // enter expected value
        String actual = invocationMatcher.toString(printSettings);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation1 = new Invocation();
        Invocation invocation2 = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        
        invocationMatcher.captureArgumentsFrom(invocation2);
        
        // assert any changes made by the method
    }
    
    @Test
    public void testCreateFrom() {
        Invocation invocation1 = new Invocation();
        Invocation invocation2 = new Invocation();
        List<Invocation> invocations = new ArrayList<Invocation>();
        invocations.add(invocation1);
        invocations.add(invocation2);
        
        List<InvocationMatcher> expected = new ArrayList<InvocationMatcher>();
        expected.add(new InvocationMatcher(invocation1));
        expected.add(new InvocationMatcher(invocation2));
        List<InvocationMatcher> actual = InvocationMatcher.createFrom(invocations);
        
        assertEquals(expected, actual);
    }
}

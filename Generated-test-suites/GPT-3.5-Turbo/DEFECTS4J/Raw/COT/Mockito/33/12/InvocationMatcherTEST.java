import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

public class InvocationMatcherTest {

    @Test
    public void testGetMethod() {
        Invocation invocation = new Invocation(null, null, null, null, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Assert.assertNull(invocationMatcher.getMethod());
    }
    
    @Test
    public void testGetInvocation() {
        Invocation invocation = new Invocation(null, null, null, null, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Assert.assertEquals(invocation, invocationMatcher.getInvocation());
    }
    
    @Test
    public void testGetMatchers() {
        Invocation invocation = new Invocation(null, null, null, null, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Assert.assertEquals(Collections.emptyList(), invocationMatcher.getMatchers());
    }
    
    @Test
    public void testToString() {
        Invocation invocation = new Invocation(null, null, null, null, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Assert.assertEquals("", invocationMatcher.toString());
    }
    
    @Test
    public void testMatches() {
        Invocation invocation1 = new Invocation(null, null, null, null, null);
        Invocation invocation2 = new Invocation(null, null, null, null, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        
        Assert.assertFalse(invocationMatcher.matches(invocation2));
    }
    
    @Test
    public void testHasSimilarMethod() {
        Invocation invocation1 = new Invocation(null, null, null, null, null);
        Invocation invocation2 = new Invocation(null, null, null, null, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        
        Assert.assertFalse(invocationMatcher.hasSimilarMethod(invocation2));
    }
    
    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = new Invocation(null, null, null, null, null);
        Invocation invocation2 = new Invocation(null, null, null, null, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        
        Assert.assertFalse(invocationMatcher.hasSameMethod(invocation2));
    }
    
    @Test
    public void testGetLocation() {
        Invocation invocation = new Invocation(null, null, null, null, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Assert.assertNull(invocationMatcher.getLocation());
    }
    
    @Test
    public void testToStringPrintSettings() {
        Invocation invocation = new Invocation(null, null, null, null, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        PrintSettings printSettings = new PrintSettings();
        
        Assert.assertEquals("", invocationMatcher.toString(printSettings));
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation1 = new Invocation(null, null, null, null, null);
        Invocation invocation2 = new Invocation(null, null, null, null, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        
        invocationMatcher.captureArgumentsFrom(invocation2);
    }
    
    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new ArrayList<>();
        invocations.add(new Invocation(null, null, null, null, null));
        
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        
        Assert.assertEquals(1, invocationMatchers.size());
    }
}
import org.hamcrest.CoreMatchers.*;
import org.junit.*;
import static org.junit.Assert.*;
import org.mockito.exceptions.*;

public class InvocationMatcherTest {

    @Test
    public void testGetMethod() {
        // Test basic functionality
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertEquals(invocation.getMethod(), invocationMatcher.getMethod());
    }
    
    @Test
    public void testGetInvocation() {
        // Test basic functionality
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertEquals(invocation, invocationMatcher.getInvocation());
    }
    
    @Test
    public void testGetMatchers() {
        // Test basic functionality
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertTrue(invocationMatcher.getMatchers().isEmpty());
        
        // Test with provided matchers
        Matcher matcher = CoreMatchers.anything();
        InvocationMatcher invocationMatcherWithMatchera = new InvocationMatcher(invocation, Collections.singletonList(matcher));
        
        assertSame(matcher, invocationMatcherWithMatchers.getMatchers().get(0));
    }
    
    @Test
    public void testToString() {
        // Test basic functionality
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertNotNull(invocationMatcher.toString());
    }
    
    @Test
    public void testMatches() {
        // Test with identical invocations
        Invocation invocation1 = new Invocation();
        Invocation invocation2 = new Invocation();
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);
        
        assertTrue(invocationMatcher1.matches(invocation2));
        
        // Test with different mocks
        Mock mock1 = new Mock();
        Mock mock2 = new Mock();
        invocation1.setMock(mock1);
        invocation2.setMock(mock2);
        invocationMatcher1 = new InvocationMatcher(invocation1);
        invocationMatcher2 = new InvocationMatcher(invocation2);
        
        assertFalse(invocationMatcher1.matches(invocation2));
        
        // Test with different methods
        Method method1 = new Method();
        Method method2 = new Method();
        invocation1.setMethod(method1);
        invocation2.setMethod(method2);
        invocationMatcher1 = new InvocationMatcher(invocation1);
        invocationMatcher2 = new InvocationMatcher(invocation2);
        
        assertFalse(invocationMatcher1.matches(invocation2));
        
        // Test with different arguments
        Argument argument1 = new Argument();
        Argument argument2 = new Argument();
        invocation1.setArguments(new Argument[] {argument1});
        invocation2.setArguments(new Argument[] {argument2});
        invocationMatcher1 = new InvocationMatcher(invocation1);
        invocationMatcher2 = new InvocationMatcher(invocation2);
        
        assertFalse(invocationMatcher1.matches(invocation2));
    }
    
    @Test
    public void testHasSimilarMethod() {
        // Test with same method name, same mock, not verified, and different arguments
        Invocation invocation1 = new Invocation();
        Invocation invocation2 = new Invocation();
        invocation1.setMethod(new Method("methodName"));
        invocation2.setMethod(new Method("methodName"));
        invocation1.setMock(new Mock());
        invocation2.setMock(new Mock());
        invocation1.setVerified(false);
        invocation2.setVerified(false);
        Argument argument1 = new Argument();
        Argument argument2 = new Argument();
        invocation1.setArguments(new Argument[] {argument1});
        invocation2.setArguments(new Argument[] {argument2});
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);
        
        assertTrue(invocationMatcher1.hasSimilarMethod(invocation2));
        
        // Test with same method name, same mock, not verified, and same arguments
        invocation1.setArguments(new Argument[] {argument1});
        invocation2.setArguments(new Argument[] {argument1});
        invocationMatcher1 = new InvocationMatcher(invocation1);
        invocationMatcher2 = new InvocationMatcher(invocation2);
        
        assertFalse(invocationMatcher1.hasSimilarMethod(invocation2));
        
        // Test with overloaded but same arguments
        invocation1.setArguments(new Argument[] {argument1});
        invocation2.setArguments(new Argument[] {argument1});
        invocation1.setMethod(new Method("methodName", new Class[]{String.class}));
        invocation2.setMethod(new Method("methodName", new Class[]{Integer.class}));
        invocationMatcher1 = new InvocationMatcher(invocation1);
        invocationMatcher2 = new InvocationMatcher(invocation2);
        
        assertFalse(invocationMatcher1.hasSimilarMethod(invocation2));
    }
    
    @Test
    public void testHasSameMethod() {
        // Test with same method
        Invocation invocation1 = new Invocation();
        Invocation invocation2 = new Invocation();
        invocation1.setMethod(new Method("methodName"));
        invocation2.setMethod(new Method("methodName"));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);
        
        assertTrue(invocationMatcher1.hasSameMethod(invocation2));
        
        // Test with different methods
        invocation1.setMethod(new Method("methodName1"));
        invocation2.setMethod(new Method("methodName2"));
        invocationMatcher1 = new InvocationMatcher(invocation1);
        invocationMatcher2 = new InvocationMatcher(invocation2);
        
        assertFalse(invocationMatcher1.hasSameMethod(invocation2));
    }
    
    @Test
    public void testToStringWithPrintSettings() {
        // Test basic functionality
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        PrintSettings printSettings = new PrintSettings();
        
        assertNotNull(invocationMatcher.toString(printSettings));
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        // Test basic functionality
        Invocation invocation1 = new Invocation();
        Invocation invocation2 = new Invocation();
        invocation1.setArguments(new Argument[] {new Argument()});
        invocation2.setArguments(new Argument[] {new Argument()});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        
        invocationMatcher.captureArgumentsFrom(invocation2);
    }
    
    @Test
    public void testCreateFrom() {
        // Test basic functionality
        List<Invocation> invocations = new ArrayList<>();
        invocations.add(new Invocation());
        
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        
        assertNotNull(invocationMatchers);
        assertEquals(invocations.size(), invocationMatchers.size());
    }
    
}
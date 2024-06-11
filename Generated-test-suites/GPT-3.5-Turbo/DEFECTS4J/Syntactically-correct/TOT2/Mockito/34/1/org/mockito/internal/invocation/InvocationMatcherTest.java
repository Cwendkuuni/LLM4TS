import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.Equals;
import org.mockito.Mockito;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithMatchers() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Equals equalsMatcher = new Equals("test");
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, List.of(equalsMatcher));
        
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(List.of(equalsMatcher), invocationMatcher.getMatchers());
    }
    
    @Test
    public void testGetMethod() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertEquals(invocation.getMethod(), invocationMatcher.getMethod());
    }
    
    @Test
    public void testMatches() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertTrue(invocationMatcher.matches(invocation));
    }
    
    // Edge case: Testing safelyArgumentsMatch() with null arguments
    @Test
    public void testSafelyArgumentsMatchWithNullArgs() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertFalse(invocationMatcher.safelyArgumentsMatch(null));
    }
    
    // Exception handling scenario: Testing hasSimilarMethod() with a different method name
    @Test
    public void testHasSimilarMethodWithDifferentMethodName() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        
        assertFalse(invocationMatcher.hasSimilarMethod(invocation2));
    }
    
    @Test
    public void testHasSameMethod() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertTrue(invocationMatcher.hasSameMethod(invocation));
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Invocation i = Mockito.mock(Invocation.class);
        invocationMatcher.captureArgumentsFrom(i);
        
        // Assert the capturing process, assuming the CapturesArguments interface is correctly implemented
    }
}

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(mock(Matcher.class));
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertNotNull(invocationMatcher);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }
    
    @Test
    public void testConstructorWithoutMatchers() {
        Invocation invocation = mock(Invocation.class);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertNotNull(invocationMatcher);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(invocation.argumentsToMatchers(), invocationMatcher.getMatchers());
    }
    
    @Test
    public void testGetMethod() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertEquals(method, invocationMatcher.getMethod());
    }
    
    // Additional test cases for other public methods can be added here
    
    @Test
    public void testMatches() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        when(invocation1.getMock()).thenReturn("mock1");
        when(invocation2.getMock()).thenReturn("mock2");
        when(invocation1.getMethod()).thenReturn(mock(Method.class));
        when(invocation2.getMethod()).thenReturn(mock(Method.class));
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        
        assertFalse(invocationMatcher.matches(invocation2));
        
        when(invocation2.getMock()).thenReturn("mock1");
        when(invocation2.getMethod()).thenReturn(invocation1.getMethod());
        
        assertTrue(invocationMatcher.matches(invocation2));
    }
    
    // Test cases for edge cases and exceptions can be added here
    
}

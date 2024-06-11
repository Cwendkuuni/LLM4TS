import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.exceptions.verification.ArgumentsAreDifferent;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherConstructorWithMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(anyString());
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertNotNull(invocationMatcher);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }
    
    @Test
    public void testInvocationMatcherConstructorWithoutMatchers() {
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
    
    // Add more test cases for the remaining methods...

}
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import org.hamcrest.Matcher;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.junit.Test;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcher() {
        Invocation invocation = mock(Invocation.class);
        Matcher matcher = mock(Matcher.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.singletonList(matcher));
        
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(Collections.singletonList(matcher), invocationMatcher.getMatchers());
    }
    
    @Test
    public void testGetMethod() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertEquals(invocation.getMethod(), invocationMatcher.getMethod());
    }
    
    @Test
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertEquals(invocation.toString(matchers, new PrintSettings()), invocationMatcher.toString());
    }
    
    // Additional test cases for edge cases and exception handling will be added here

}
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.MatcherDecorator;
import org.mockito.internal.matchers.VarargMatcher;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class InvocationMatcherTest {
    
    @Test
    public void testGetMethod() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        List<Matcher> matchers = Arrays.asList(mock(Matcher.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertEquals(method, invocationMatcher.getMethod());
    }
    
    @Test
    public void testGetInvocation() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(mock(Matcher.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertEquals(invocation, invocationMatcher.getInvocation());
    }
    
    @Test
    public void testGetMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(mock(Matcher.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertEquals(matchers, invocationMatcher.getMatchers());
    }
    
    // Additional test methods for the remaining public methods can be added here
    
}
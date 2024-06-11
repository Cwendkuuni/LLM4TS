import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.hamcrest.Matcher;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

public class InvocationMatcherTest {

    @Test
    public void testGetMethod() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Collections.emptyList();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        Method method = invocation.getMethod();
        
        assertEquals(method, invocationMatcher.getMethod());
    }
    
    @Test
    public void testGetInvocation() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Collections.emptyList();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertEquals(invocation, invocationMatcher.getInvocation());
    }
    
    @Test
    public void testGetMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Collections.emptyList();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertEquals(matchers, invocationMatcher.getMatchers());
    }
    
    // Add more test cases for other methods as needed
    
}
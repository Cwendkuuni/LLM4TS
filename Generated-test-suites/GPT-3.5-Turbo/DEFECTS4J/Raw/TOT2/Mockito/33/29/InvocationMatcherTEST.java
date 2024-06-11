import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(mock(Matcher.class));
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }
    
    @Test
    public void testConstructorWithoutMatchers() {
        Invocation invocation = mock(Invocation.class);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }
    
    // Add more test cases for the remaining public methods
    
}
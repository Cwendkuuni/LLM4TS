import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.hamcrest.CoreMatchers;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<Matcher>();
        matchers.add(CoreMatchers.any(Object.class));
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testGetMethod() {
        Invocation invocation = mock(Invocation.class);
        
        when(invocation.getMethod()).thenReturn(Object.class.getMethod("toString"));
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertEquals(Object.class.getMethod("toString"), invocationMatcher.getMethod());
    }

    @Test
    public void testMatches() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        
        when(invocation1.getMock()).thenReturn("mock");
        when(invocation2.getMock()).thenReturn("mock");
        when(invocation1.getArguments()).thenReturn(new Object[] {1, 2});
        when(invocation2.getArguments()).thenReturn(new Object[] {1, 2});
        
        assertTrue(invocationMatcher.matches(invocation2));
    }

    // Add more test cases for other methods as needed

}
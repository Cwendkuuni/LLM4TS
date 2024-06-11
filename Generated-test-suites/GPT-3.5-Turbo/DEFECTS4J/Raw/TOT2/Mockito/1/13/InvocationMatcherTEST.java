import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

import java.lang.reflect.Method;
import java.util.List;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherConstructorWithMatchers() {
        List<Matcher> matchers = new LinkedList<Matcher>();
        matchers.add(new SomeMatcher());
        
        Invocation invocation = mock(Invocation.class);
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
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    // More test cases for other methods in the class can be added here

}
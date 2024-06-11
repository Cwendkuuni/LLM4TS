import org.junit.Test;
import org.mockito.invocation.MockInvocation;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import org.mockito.internal.invocation.InvocationMatcher;
import org.hamcrest.Matcher;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class InvocationMatcherTest {

    @Test
    public void testGetMethod() {
        Method m = mock(Method.class);
        Invocation invocation = new MockInvocation(m);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        
        assertEquals(m, matcher.getMethod());
    }

    @Test
    public void testGetInvocation() {
        Method m = mock(Method.class);
        Invocation invocation = new MockInvocation(m);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        
        assertEquals(invocation, matcher.getInvocation());
    }

    @Test
    public void testGetMatchers() {
        Method m = mock(Method.class);
        Invocation invocation = new MockInvocation(m);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        List<Matcher> matchers = new ArrayList<>();
        
        assertEquals(matchers, matcher.getMatchers());
    }

    // Additional test cases for other methods should be added here

}
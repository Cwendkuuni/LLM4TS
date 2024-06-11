import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class InvocationMatcherTest {

    @Test
    public void testGetMethod() {
        Method method = Object.class.getDeclaredMethod("toString");
        Invocation invocation = new Invocation(method, null, null, 1);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testGetInvocation() {
        Method method = Object.class.getDeclaredMethod("hashCode");
        Invocation invocation = new Invocation(method, null, null, 2);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(invocation, invocationMatcher.getInvocation());
    }

    @Test
    public void testGetMatchers() {
        Method method = Object.class.getDeclaredMethod("equals", Object.class);
        List<Matcher> matchers = Arrays.asList(null);
        Invocation invocation = new Invocation(method, new Object[]{}, null, 3);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testToString() {
        Method method = Object.class.getDeclaredMethod("getClass");
        Invocation invocation = new Invocation(method, null, null, 4);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertNotNull(invocationMatcher.toString());
    }

    // Additional test cases for edge cases and exceptions can be added here

}
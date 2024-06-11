import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Test;
import java.lang.reflect.Method;
import java.util.List;
import org.mockito.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;

public class InvocationMatcherTest {

    @Test
    public void testGetMethod() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = mock(Invocation.class);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(invocation, invocationMatcher.getInvocation());
    }

    @Test
    public void testGetMatchers() {
        Invocation invocation = mock(Invocation.class);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        List<Matcher> matchers = invocationMatcher.getMatchers();
        assertTrue(matchers.isEmpty());
    }

    @Test
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertNotNull(invocationMatcher.toString());
    }

    @Test
    public void testMatches() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        when(invocation1.getMock()).thenReturn("mock1");
        when(invocation2.getMock()).thenReturn("mock1");
        when(invocation1.getMethod()).thenReturn(Method.class);
        when(invocation2.getMethod()).thenReturn(Method.class);
        when(invocation1.getArguments()).thenReturn(new Object[]{1, 2, 3});
        when(invocation2.getArguments()).thenReturn(new Object[]{1, 2, 3});

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        assertTrue(invocationMatcher.matches(invocation2));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        when(invocation1.getMethod()).thenReturn(Method.class);
        when(invocation2.getMethod()).thenReturn(Method.class);
        when(invocation1.isVerified()).thenReturn(false);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        assertTrue(invocationMatcher.hasSimilarMethod(invocation2));
    }

    @Test
    public void testLocation() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertNotNull(invocationMatcher.getLocation());
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Test case for unsupported operation exception
        // when method is var args
        try {
            invocationMatcher.captureArgumentsFrom(invocation);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // success
        }
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = mock(List.class);
        when(invocations.get(0)).thenReturn(mock(Invocation.class));

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        assertNotNull(invocationMatchers);
        assertEquals(1, invocationMatchers.size());
    }
}
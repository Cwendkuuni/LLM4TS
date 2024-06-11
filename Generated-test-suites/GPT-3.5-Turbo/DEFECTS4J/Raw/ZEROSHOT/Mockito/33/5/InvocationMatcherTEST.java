import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;

public class InvocationMatcherTest {

    @Test
    public void testConstructor1() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testConstructor2() {
        Invocation invocation = mock(Invocation.class);
        Matcher matcher = mock(Matcher.class);
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(matcher);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testGetMethod() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(mock(Matcher.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertEquals(invocation.toString(matchers, new PrintSettings()), invocationMatcher.toString());
    }

    @Test
    public void testMatches() {
        Invocation invocation = mock(Invocation.class);
        Invocation actual = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertTrue(invocationMatcher.matches(actual));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertFalse(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = mock(Invocation.class);
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(location, invocationMatcher.getLocation());
    }

    @Test
    public void testToString2() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(mock(Matcher.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        PrintSettings printSettings = mock(PrintSettings.class);
        assertEquals(invocation.toString(matchers, printSettings), invocationMatcher.toString(printSettings));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation i = mock(Invocation.class);
        Invocation invocation = mock(Invocation.class);
        Matcher matcher1 = mock(Matcher.class);
        when(matcher1 instanceof CapturesArguments).thenReturn(true);
        when(i.getArguments()).thenReturn(new Object[] { "arg1" });
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(matcher1);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        invocationMatcher.captureArgumentsFrom(i);
        verify((CapturesArguments) matcher1).captureFrom("arg1");
    }

    @Test
    public void testCreateFrom() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<Invocation> invocations = new LinkedList<>();
        invocations.add(invocation1);
        invocations.add(invocation2);
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        assertEquals(2, invocationMatchers.size());
        assertEquals(invocation1, invocationMatchers.get(0).getInvocation());
        assertEquals(invocation2, invocationMatchers.get(1).getInvocation());
    }
}
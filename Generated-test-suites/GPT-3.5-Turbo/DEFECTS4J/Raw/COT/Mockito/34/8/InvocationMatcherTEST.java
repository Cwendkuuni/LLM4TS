
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.invocation.*;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;

@SuppressWarnings("unchecked")
public class InvocationMatcherTest {

    @Test
    public void testGetMethod() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Method method = invocationMatcher.getMethod();

        assertSame(invocation.getMethod(), method);
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation resultInvocation = invocationMatcher.getInvocation();

        assertSame(invocation, resultInvocation);
    }

    @Test
    public void testGetMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Collections.emptyList();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        List<Matcher> resultMatchers = invocationMatcher.getMatchers();

        assertSame(matchers, resultMatchers);
    }

    @Test
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Collections.emptyList();
        PrintSettings printSettings = mock(PrintSettings.class);
        when(invocation.toString(matchers, printSettings)).thenReturn("expected invocation");
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        String result = invocationMatcher.toString(printSettings);

        assertEquals("expected invocation", result);
    }

    @Test
    public void testMatches() {
        Invocation invocation = mock(Invocation.class);
        Object mock = mock(Object.class);
        List<Matcher> matchers = Collections.emptyList();
        when(invocation.getMock()).thenReturn(mock);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.argumentsToMatchers()).thenReturn(matchers);
        Invocation actualInvocation = mock(Invocation.class);
        when(actualInvocation.getMock()).thenReturn(mock);
        when(actualInvocation.getMethod()).thenReturn(mock(Method.class));
        when(actualInvocation.argumentsToMatchers()).thenReturn(matchers);
        when(invocation.getArguments()).thenReturn(new Object[]{});
        when(actualInvocation.getArguments()).thenReturn(new Object[]{});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        boolean result = invocationMatcher.matches(actualInvocation);

        assertTrue(result);
    }

    @Test
    public void testMatches_returnFalse() {
        Invocation invocation = mock(Invocation.class);
        Object mock = mock(Object.class);
        List<Matcher> matchers = Collections.emptyList();
        when(invocation.getMock()).thenReturn(mock);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.argumentsToMatchers()).thenReturn(matchers);
        Invocation actualInvocation = mock(Invocation.class);
        when(actualInvocation.getMock()).thenReturn(mock);
        when(actualInvocation.getMethod()).thenReturn(mock(Method.class));
        when(actualInvocation.argumentsToMatchers()).thenReturn(matchers);
        when(invocation.getArguments()).thenReturn(new Object[]{});
        when(actualInvocation.getArguments()).thenReturn(new Object[]{"argument"});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        boolean result = invocationMatcher.matches(actualInvocation);

        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = mock(Invocation.class);
        Object mock = mock(Object.class);
        List<Matcher> matchers = Collections.emptyList();
        when(invocation.getMock()).thenReturn(mock);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.argumentsToMatchers()).thenReturn(matchers);
        Invocation candidateInvocation = mock(Invocation.class);
        when(candidateInvocation.getMethod()).thenReturn(mock(Method.class));
        when(candidateInvocation.isVerified()).thenReturn(false);
        when(candidateInvocation.getMock()).thenReturn(mock);
        when(candidateInvocation.getArguments()).thenReturn(new Object[]{"argument"});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        boolean result = invocationMatcher.hasSimilarMethod(candidateInvocation);

        assertTrue(result);
    }

    @Test
    public void testHasSimilarMethod_returnFalse() {
        Invocation invocation = mock(Invocation.class);
        Object mock = mock(Object.class);
        List<Matcher> matchers = Collections.emptyList();
        when(invocation.getMock()).thenReturn(mock);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.argumentsToMatchers()).thenReturn(matchers);
        Invocation candidateInvocation = mock(Invocation.class);
        when(candidateInvocation.getMethod()).thenReturn(mock(Method.class));
        when(candidateInvocation.isVerified()).thenReturn(true);
        when(candidateInvocation.getMock()).thenReturn(mock);
        when(candidateInvocation.getArguments()).thenReturn(new Object[]{"argument"});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        boolean result = invocationMatcher.hasSimilarMethod(candidateInvocation);

        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_returnFalseWithOverloadedArgs() {
        Invocation invocation = mock(Invocation.class);
        Object mock = mock(Object.class);
        List<Matcher> matchers = Collections.emptyList();
        when(invocation.getMock()).thenReturn(mock);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.argumentsToMatchers()).thenReturn(matchers);
        Invocation candidateInvocation = mock(Invocation.class);
        when(candidateInvocation.getMethod()).thenReturn(mock(Method.class));
        when(candidateInvocation.isVerified()).thenReturn(false);
        when(candidateInvocation.getMock()).thenReturn(mock);
        when(candidateInvocation.getArguments()).thenReturn(new Object[]{"argument"});
        when(invocation.getArguments()).thenReturn(new Object[]{"argument"});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        boolean result = invocationMatcher.hasSimilarMethod(candidateInvocation);

        assertFalse(result);
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation candidateInvocation = mock(Invocation.class);
        when(candidateInvocation.getMethod()).thenReturn(method);

        boolean result = invocationMatcher.hasSameMethod(candidateInvocation);

        assertTrue(result);
    }

    @Test
    public void testHasSameMethod_returnFalse() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation candidateInvocation = mock(Invocation.class);
        when(candidateInvocation.getMethod()).thenReturn(mock(Method.class));

        boolean result = invocationMatcher.hasSameMethod(candidateInvocation);

        assertFalse(result);
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = mock(Invocation.class);
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Location resultLocation = invocationMatcher.getLocation();

        assertSame(location, resultLocation);
    }

    @Test
    public void testToStringWithPrintSettings() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Collections.emptyList();
        PrintSettings printSettings = mock(PrintSettings.class);
        when(invocation.toString(matchers, printSettings)).thenReturn("expected invocation");
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        String result = invocationMatcher.toString(printSettings);

        assertEquals("expected invocation", result);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Collections.emptyList();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Invocation i = mock(Invocation.class);
        Matcher capturesMatcher = mock(CapturesArguments.class);
        when(matchers.get(0)).thenReturn(capturesMatcher);
        Object argument = new Object();
        when(i.getArguments()).thenReturn(new Object[]{argument});

        invocationMatcher.captureArgumentsFrom(i);

        verify((CapturesArguments)capturesMatcher).captureFrom(argument);
    }
}


import org.junit.Test;

import static org.mockito.Mockito.*;

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

        Invocation result = invocationMatcher.getInvocation();

        assertSame(invocation, result);
    }

    @Test
    public void testGetMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = mock(List.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        List<Matcher> result = invocationMatcher.getMatchers();

        assertSame(matchers, result);
    }

    @Test
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = mock(List.class);
        PrintSettings printSettings = mock(PrintSettings.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        invocationMatcher.toString(printSettings);

        verify(invocation).toString(matchers, printSettings);
    }

    @Test
    public void testMatches() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation actual = mock(Invocation.class);

        invocation.getMock();
        actual.getMock();
        invocation.getMethod();
        actual.getMethod();
        invocation.getArguments();

        when(invocation.getMock()).thenReturn("mock");
        when(actual.getMock()).thenReturn("mock");
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(actual.getMethod()).thenReturn(mock(Method.class));

        invocationMatcher.matches(actual);
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation candidate = mock(Invocation.class);
        Method method = mock(Method.class);

        when(invocation.getMethod()).thenReturn(method);
        when(candidate.getMethod()).thenReturn(method);
        when(invocation.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn("mock");
        when(candidate.getMock()).thenReturn("mock");
        when(invocation.getArguments()).thenReturn(new Object[0]);
        when(candidate.getArguments()).thenReturn(new Object[0]);

        invocationMatcher.hasSimilarMethod(candidate);
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation candidate = mock(Invocation.class);
        Method method = mock(Method.class);

        when(invocation.getMethod()).thenReturn(method);
        when(candidate.getMethod()).thenReturn(method);

        invocationMatcher.hasSameMethod(candidate);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation i = mock(Invocation.class);
        Matcher matcher = mock(Matcher.class);
        CapturesArguments capturesArguments = mock(CapturesArguments.class);

        List<Matcher> matchers = Collections.singletonList(matcher);
        Object argument = new Object();
        Object[] arguments = new Object[]{argument};

        when(i.getArguments()).thenReturn(arguments);
        when(matchers.get(0)).thenReturn(capturesArguments);

        invocationMatcher.captureArgumentsFrom(i);

        verify(capturesArguments).captureFrom(argument);
    }
}
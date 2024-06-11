import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = mock(List.class);
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

    @Test
    public void testGetMethod() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(invocation.getMethod(), invocationMatcher.getMethod());
    }

    @Test
    public void testMatches() {
        Invocation invocation = mock(Invocation.class);
        Invocation actual = mock(Invocation.class);
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);

        when(invocation.getMock()).thenReturn("mock");
        when(actual.getMock()).thenReturn("mock");
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(actual.getMethod()).thenReturn(mock(Method.class));
        when(argumentsComparator.argumentsMatch(invocation, actual)).thenReturn(true);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean matches = invocationMatcher.matches(actual);
        
        assertTrue(matches);
        verify(invocation).getMock();
        verify(actual).getMock();
        verify(invocation).getMethod();
        verify(actual).getMethod();
        verify(argumentsComparator).argumentsMatch(invocation, actual);
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);

        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMethod().getName()).thenReturn("methodName");
        when(candidate.getMethod().getName()).thenReturn("methodName");
        when(invocation.getMock()).thenReturn("mock");
        when(candidate.getMock()).thenReturn("mock");
        when(invocation.getArguments()).thenReturn(new Object[] {1, 2});
        when(candidate.getArguments()).thenReturn(new Object[] {1, 2});
        when(argumentsComparator.argumentsMatch(invocation, candidate)).thenReturn(true);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidate);

        assertTrue(hasSimilarMethod);
        verify(invocation).getMethod();
        verify(candidate).getMethod();
        verify(candidate).isVerified();
        verify(invocation.getMethod(), times(2)).getName();
        verify(candidate.getMethod(), times(2)).getName();
        verify(invocation).getMock();
        verify(candidate).getMock();
        verify(invocation).getArguments();
        verify(candidate).getArguments();
        verify(argumentsComparator).argumentsMatch(invocation, candidate);
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        Method method = mock(Method.class);

        when(invocation.getMethod()).thenReturn(method);
        when(candidate.getMethod()).thenReturn(method);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean hasSameMethod = invocationMatcher.hasSameMethod(candidate);

        assertTrue(hasSameMethod);
        verify(invocation).getMethod();
        verify(candidate).getMethod();
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = mock(Invocation.class);
        Location location = mock(Location.class);

        when(invocation.getLocation()).thenReturn(location);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Location result = invocationMatcher.getLocation();

        assertEquals(location, result);
        verify(invocation).getLocation();
    }

    @Test
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        PrintSettings printSettings = mock(PrintSettings.class);

        when(invocation.toString(anyList(), any(PrintSettings.class))).thenReturn("toString");

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        String result = invocationMatcher.toString(printSettings);

        assertEquals("toString", result);
        verify(invocation).toString(invocationMatcher.getMatchers(), printSettings);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        Invocation i = mock(Invocation.class);
        Matcher matcher = mock(Matcher.class);
        Object[] arguments = new Object[] {1, 2};
        CapturesArguments capturesArguments = mock(CapturesArguments.class);

        when(invocation.getArguments()).thenReturn(arguments);
        when(capturesArguments.captureFrom(anyObject())).thenReturn(true);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        invocationMatcher.getMatchers().add(matcher);
        invocationMatcher.captureArgumentsFrom(i);

        verify(invocation).getArguments();
        verify(capturesArguments).captureFrom(arguments[0]);
        verify(matcher).captureFrom(arguments[0]);
    }
}
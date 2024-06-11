import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithoutMatchers() {
        Invocation invocation = mock(Invocation.class);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(invocation.argumentsToMatchers(), invocationMatcher.getMatchers());
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
        List<Matcher> matchers = new ArrayList<>();
        PrintSettings printSettings = mock(PrintSettings.class);
        when(invocation.toString(matchers, printSettings)).thenReturn("Invocation");

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals("Invocation", invocationMatcher.toString(printSettings));
    }

    @Test
    public void testMatches() {
        Invocation invocation = mock(Invocation.class);
        Invocation actual = mock(Invocation.class);
        Object[] arguments = new Object[0];
        when(invocation.getMock()).thenReturn("mock");
        when(actual.getMock()).thenReturn("mock");
        when(invocation.hasSameMethod(actual)).thenReturn(true);
        when(invocation.getArguments()).thenReturn(arguments);
        when(new ArgumentsComparator().argumentsMatch(invocation, actual)).thenReturn(true);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean result = invocationMatcher.matches(actual);

        assertTrue(result);
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(candidate.getMethod()).thenReturn(method);
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn("mock");
        when(candidate.getMock()).thenReturn("mock");
        when(invocation.hasSameMethod(candidate)).thenReturn(true);
        when(invocation.getArguments()).thenReturn(new Object[0]);
        when(new ArgumentsComparator().argumentsMatch(invocation, candidate)).thenReturn(true);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        assertFalse(result);
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(candidate.getMethod()).thenReturn(method);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean result = invocationMatcher.hasSameMethod(candidate);

        assertTrue(result);
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
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        Invocation i = mock(Invocation.class);
        Matcher matcher = mock(CapturesArguments.class);
        when(invocation.getArguments()).thenReturn(new Object[]{1});
        when(((CapturesArguments) matcher).captureFrom(1)).thenReturn(true);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        invocationMatcher.captureArgumentsFrom(i);

        verify((CapturesArguments) matcher).captureFrom(1);
    }

    @Test
    public void testCreateFrom() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<Invocation> invocations = new ArrayList<>();
        invocations.add(invocation1);
        invocations.add(invocation2);

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        assertEquals(2, invocationMatchers.size());
        assertEquals(invocation1, invocationMatchers.get(0).getInvocation());
        assertEquals(invocation2, invocationMatchers.get(1).getInvocation());
    }
}
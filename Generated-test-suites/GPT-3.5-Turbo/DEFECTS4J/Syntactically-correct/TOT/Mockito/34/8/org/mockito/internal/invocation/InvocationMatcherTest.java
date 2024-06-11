import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.debugging.Location;
import org.hamcrest.Matcher;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    @Test
    public void testGetMethod() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        when(invocation.getMethod()).thenReturn(method);

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
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(mock(Matcher.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(mock(Matcher.class), mock(Matcher.class));
        PrintSettings printSettings = mock(PrintSettings.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        when(invocation.toString(matchers, printSettings)).thenReturn("toString output");

        assertEquals("toString output", invocationMatcher.toString());
    }

    @Test
    public void testMatches() {
        Invocation invocation = mock(Invocation.class);
        Invocation actualInvocation = mock(Invocation.class);
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        when(invocation.getMock()).thenReturn("mock");
        when(actualInvocation.getMock()).thenReturn("mock");
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(actualInvocation.getMethod()).thenReturn(mock(Method.class));
        when(argumentsComparator.argumentsMatch(invocationMatcher, actualInvocation)).thenReturn(true);

        assertTrue(invocationMatcher.matches(actualInvocation));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidateInvocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidateInvocation.getMethod()).thenReturn(mock(Method.class));
        when(candidateInvocation.isVerified()).thenReturn(false);
        when(candidateInvocation.getMock()).thenReturn("mock");
        when(invocation.getMock()).thenReturn("mock");
        when(invocationMatcher.hasSameMethod(candidateInvocation)).thenReturn(true);
        when(invocationMatcher.safelyArgumentsMatch(candidateInvocation.getArguments())).thenReturn(true);

        assertFalse(invocationMatcher.hasSimilarMethod(candidateInvocation));
    }

    @Test
    public void testHasSimilarMethod_OverloadedButSameArgs() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidateInvocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidateInvocation.getMethod()).thenReturn(mock(Method.class));
        when(candidateInvocation.isVerified()).thenReturn(false);
        when(candidateInvocation.getMock()).thenReturn("mock");
        when(invocation.getMock()).thenReturn("mock");
        when(invocationMatcher.hasSameMethod(candidateInvocation)).thenReturn(false);
        when(invocationMatcher.safelyArgumentsMatch(candidateInvocation.getArguments())).thenReturn(true);

        assertTrue(invocationMatcher.hasSimilarMethod(candidateInvocation));
    }

    @Test
    public void testHasSimilarMethod_MethodNameNotEquals() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidateInvocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidateInvocation.getMethod()).thenReturn(mock(Method.class));
        when(candidateInvocation.isVerified()).thenReturn(false);
        when(candidateInvocation.getMock()).thenReturn("mock");
        when(invocation.getMock()).thenReturn("mock");
        when(invocationMatcher.hasSameMethod(candidateInvocation)).thenReturn(false);
        when(invocationMatcher.safelyArgumentsMatch(candidateInvocation.getArguments())).thenReturn(false);

        assertFalse(invocationMatcher.hasSimilarMethod(candidateInvocation));
    }

    @Test
    public void testHasSimilarMethod_IsVerified() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidateInvocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidateInvocation.getMethod()).thenReturn(mock(Method.class));
        when(candidateInvocation.isVerified()).thenReturn(true);
        when(candidateInvocation.getMock()).thenReturn("mock");
        when(invocation.getMock()).thenReturn("mock");
        when(invocationMatcher.hasSameMethod(candidateInvocation)).thenReturn(true);
        when(invocationMatcher.safelyArgumentsMatch(candidateInvocation.getArguments())).thenReturn(false);

        assertFalse(invocationMatcher.hasSimilarMethod(candidateInvocation));
    }

    @Test
    public void testHasSimilarMethod_MockNotEquals() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidateInvocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidateInvocation.getMethod()).thenReturn(mock(Method.class));
        when(candidateInvocation.isVerified()).thenReturn(false);
        when(candidateInvocation.getMock()).thenReturn("mock1");
        when(invocation.getMock()).thenReturn("mock2");
        when(invocationMatcher.hasSameMethod(candidateInvocation)).thenReturn(true);
        when(invocationMatcher.safelyArgumentsMatch(candidateInvocation.getArguments())).thenReturn(false);

        assertFalse(invocationMatcher.hasSimilarMethod(candidateInvocation));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidateInvocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidateInvocation.getMethod()).thenReturn(mock(Method.class));

        assertTrue(invocationMatcher.hasSameMethod(candidateInvocation));
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = mock(Invocation.class);
        Location location = mock(Location.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        when(invocation.getLocation()).thenReturn(location);

        assertEquals(location, invocationMatcher.getLocation());
    }

    @Test
    public void testToStringWithPrintSettings() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(mock(Matcher.class), mock(Matcher.class));
        PrintSettings printSettings = mock(PrintSettings.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        when(invocation.toString(matchers, printSettings)).thenReturn("toString output");

        assertEquals("toString output", invocationMatcher.toString(printSettings));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        Invocation capturedInvocation = mock(Invocation.class);
        Matcher matcher = mock(CapturesArguments.class);
        Object[] arguments = {1, 2, 3};
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(matcher));
        when(invocation.getArguments()).thenReturn(arguments);
        when(matcher instanceof CapturesArguments).thenReturn(true);
        when(matcher).thenReturn(matcher);

        invocationMatcher.captureArgumentsFrom(capturedInvocation);

        verify((CapturesArguments)matcher, times(3)).captureFrom(any());
    }
}

package org.mockito.internal.invocation;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.Invocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InvocationMatcherTest {

    @Mock
    private Invocation invocation;

    @Test
    public void testConstructorWithMatchers() {
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(Mockito.mock(Matcher.class));
        matchers.add(Mockito.mock(Matcher.class));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithoutMatchers() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testGetMethod() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Method method = Mockito.mock(Method.class);
        Mockito.when(invocation.getMethod()).thenReturn(method);

        assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testToString() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        String expected = "InvocationMatcher{invocation='" + invocation + "', matchers=[Empty]}";
        assertEquals(expected, invocationMatcher.toString());
    }

    @Test
    public void testMatchesWithSameArguments() {
        Invocation actualInvocation = Mockito.mock(Invocation.class);
        Object[] arguments = new Object[]{1, "test"};
        Mockito.when(invocation.getMock()).thenReturn(Mockito.mock(Object.class));
        Mockito.when(actualInvocation.getMock()).thenReturn(Mockito.mock(Object.class));
        Mockito.when(invocation.getMethod()).thenReturn(Mockito.mock(Method.class));
        Mockito.when(actualInvocation.getMethod()).thenReturn(Mockito.mock(Method.class));
        Mockito.when(invocation.isVerified()).thenReturn(false);
        Mockito.when(actualInvocation.isVerified()).thenReturn(false);
        Mockito.when(invocation.getArguments()).thenReturn(arguments);
        Mockito.when(actualInvocation.getArguments()).thenReturn(arguments);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertTrue(invocationMatcher.matches(actualInvocation));
    }

    @Test
    public void testMatchesWithDifferentArguments() {
        Invocation actualInvocation = Mockito.mock(Invocation.class);
        Object[] arguments1 = new Object[]{1, "test"};
        Object[] arguments2 = new Object[]{2, "test2"};
        Mockito.when(invocation.getMock()).thenReturn(Mockito.mock(Object.class));
        Mockito.when(actualInvocation.getMock()).thenReturn(Mockito.mock(Object.class));
        Mockito.when(invocation.getMethod()).thenReturn(Mockito.mock(Method.class));
        Mockito.when(actualInvocation.getMethod()).thenReturn(Mockito.mock(Method.class));
        Mockito.when(invocation.isVerified()).thenReturn(false);
        Mockito.when(actualInvocation.isVerified()).thenReturn(false);
        Mockito.when(invocation.getArguments()).thenReturn(arguments1);
        Mockito.when(actualInvocation.getArguments()).thenReturn(arguments2);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertFalse(invocationMatcher.matches(actualInvocation));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation candidateInvocation = Mockito.mock(Invocation.class);
        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);
        Mockito.when(invocation.getMethod()).thenReturn(method1);
        Mockito.when(candidateInvocation.getMethod()).thenReturn(method2);
        Mockito.when(invocation.getMock()).thenReturn(Mockito.mock(Object.class));
        Mockito.when(candidateInvocation.getMock()).thenReturn(Mockito.mock(Object.class));
        Mockito.when(invocation.isVerified()).thenReturn(false);
        Mockito.when(candidateInvocation.isVerified()).thenReturn(false);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertTrue(invocationMatcher.hasSimilarMethod(candidateInvocation));
    }

    @Test
    public void testHasSimilarMethodWithOverloadedButSameArgs() {
        Invocation candidateInvocation = Mockito.mock(Invocation.class);
        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);
        Object[] arguments = new Object[]{1, "test"};
        Mockito.when(invocation.getMethod()).thenReturn(method1);
        Mockito.when(candidateInvocation.getMethod()).thenReturn(method2);
        Mockito.when(invocation.getArguments()).thenReturn(arguments);
        Mockito.when(candidateInvocation.getArguments()).thenReturn(arguments);
        Mockito.when(invocation.getMock()).thenReturn(Mockito.mock(Object.class));
        Mockito.when(candidateInvocation.getMock()).thenReturn(Mockito.mock(Object.class));
        Mockito.when(invocation.isVerified()).thenReturn(false);
        Mockito.when(candidateInvocation.isVerified()).thenReturn(false);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertFalse(invocationMatcher.hasSimilarMethod(candidateInvocation));
    }

    @Test
    public void testHasSameMethod() {
        Invocation candidateInvocation = Mockito.mock(Invocation.class);
        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);
        Mockito.when(invocation.getMethod()).thenReturn(method1);
        Mockito.when(candidateInvocation.getMethod()).thenReturn(method2);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertFalse(invocationMatcher.hasSameMethod(candidateInvocation));
    }

    @Test
    public void testGetLocation() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Location location = Mockito.mock(Location.class);
        Mockito.when(invocation.getLocation()).thenReturn(location);

        assertEquals(location, invocationMatcher.getLocation());
    }

    @Test
    public void testToStringWithPrintSettings() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        PrintSettings printSettings = Mockito.mock(PrintSettings.class);
        String expected = "InvocationMatcher{invocation='" + invocation + "', matchers=[Empty]}";
        assertEquals(expected, invocationMatcher.toString(printSettings));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation i = Mockito.mock(Invocation.class);
        Matcher matcher1 = Mockito.mock(Matcher.class);
        Matcher matcher2 = Mockito.mock(Matcher.class);
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(matcher1);
        matchers.add(matcher2);

        Object[] arguments = new Object[]{1, "test"};
        Mockito.when(i.getArguments()).thenReturn(arguments);
        Mockito.when(matcher1 instanceof CapturesArguments).thenReturn(true);
        Mockito.when(matcher2 instanceof CapturesArguments).thenReturn(false);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        invocationMatcher.captureArgumentsFrom(i);

        Mockito.verify((CapturesArguments) matcher1).captureFrom(arguments[0]);
        Mockito.verify(matcher2, Mockito.never()).captureFrom(Mockito.any());
    }
}

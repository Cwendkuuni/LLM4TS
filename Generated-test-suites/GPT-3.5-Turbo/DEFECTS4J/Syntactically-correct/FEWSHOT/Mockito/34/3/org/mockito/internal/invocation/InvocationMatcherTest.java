package org.mockito.internal.invocation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.exceptions.PrintableInvocation;

public class InvocationMatcherTest {
    @Test
    public void testConstructor() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Collections.singletonList(mock(Matcher.class));

        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        assertEquals(invocation, matcher.getInvocation());
        assertEquals(matchers, matcher.getMatchers());
    }

    @Test
    public void testConstructorEmptyMatchers() {
        Invocation invocation = mock(Invocation.class);

        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertEquals(invocation, matcher.getInvocation());
        assertEquals(invocation.argumentsToMatchers(), matcher.getMatchers());
    }

    @Test
    public void testGetMethod() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        Mockito.when(invocation.getMethod()).thenReturn(method);

        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertEquals(method, matcher.getMethod());
    }

    @Test
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        PrintSettings printSettings = mock(PrintSettings.class);
        List<Matcher> matchers = Arrays.asList(mock(Matcher.class), mock(Matcher.class));
        Mockito.when(invocation.toString(matchers, printSettings)).thenReturn("mockito");

        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        assertEquals("mockito", matcher.toString());
    }

    @Test
    public void testMatches() {
        Invocation invocation = mock(Invocation.class);
        Invocation actual = mock(Invocation.class);
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        Mockito.when(invocation.getMock()).thenReturn("mock");
        Mockito.when(actual.getMock()).thenReturn("mock");
        Mockito.when(invocation.getMethod()).thenReturn(mock(Method.class));
        Mockito.when(actual.getMethod()).thenReturn(mock(Method.class));
        Mockito.when(argumentsComparator.argumentsMatch(invocation, actual)).thenReturn(true);
        Mockito.whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(argumentsComparator);

        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertTrue(matcher.matches(actual));
    }

    @Test
    public void testMatchesDifferentMocks() {
        Invocation invocation = mock(Invocation.class);
        Invocation actual = mock(Invocation.class);
        Mockito.when(invocation.getMock()).thenReturn("mock");
        Mockito.when(actual.getMock()).thenReturn("differentMock");

        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertFalse(matcher.matches(actual));
    }

    @Test
    public void testMatchesDifferentMethods() {
        Invocation invocation = mock(Invocation.class);
        Invocation actual = mock(Invocation.class);
        Mockito.when(invocation.getMock()).thenReturn("mock");
        Mockito.when(actual.getMock()).thenReturn("mock");
        Mockito.when(invocation.getMethod()).thenReturn(mock(Method.class));
        Mockito.when(actual.getMethod()).thenReturn(mock(Method.class));

        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertFalse(matcher.matches(actual));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        Mockito.when(invocation.getMethod()).thenReturn(mock(Method.class));
        Mockito.when(candidate.getMethod()).thenReturn(mock(Method.class));
        Mockito.when(candidate.isVerified()).thenReturn(false);
        Mockito.when(invocation.isVerified()).thenReturn(true);
        Mockito.when(argumentsComparator.argumentsMatch(invocation, candidate.getArguments())).thenReturn(true);
        Mockito.whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(argumentsComparator);

        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertTrue(matcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethodDifferentMethodName() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        Mockito.when(invocation.getMethod()).thenReturn(mock(Method.class));
        Mockito.when(candidate.getMethod()).thenReturn(mock(Method.class));
        Mockito.when(invocation.getMethod().getName()).thenReturn("method1");
        Mockito.when(candidate.getMethod().getName()).thenReturn("method2");

        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertFalse(matcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethodVerified() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        Mockito.when(invocation.getMethod()).thenReturn(mock(Method.class));
        Mockito.when(candidate.getMethod()).thenReturn(mock(Method.class));
        Mockito.when(candidate.isVerified()).thenReturn(true);
        Mockito.when(invocation.isVerified()).thenReturn(true);

        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertFalse(matcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethodDifferentMock() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        Mockito.when(invocation.getMethod()).thenReturn(mock(Method.class));
        Mockito.when(candidate.getMethod()).thenReturn(mock(Method.class));
        Mockito.when(invocation.getMock()).thenReturn("mock1");
        Mockito.when(candidate.getMock()).thenReturn("mock2");

        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertFalse(matcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethodOverloadedButSameArgs() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        Mockito.when(invocation.getMethod()).thenReturn(mock(Method.class));
        Mockito.when(candidate.getMethod()).thenReturn(mock(Method.class));
        Mockito.when(argumentsComparator.argumentsMatch(invocation, candidate.getArguments())).thenReturn(true);
        Mockito.whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(argumentsComparator);

        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertFalse(matcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        Mockito.when(invocation.getMethod()).thenReturn(mock(Method.class));
        Mockito.when(candidate.getMethod()).thenReturn(mock(Method.class));
        Mockito.when(invocation.getMethod().equals(candidate.getMethod())).thenReturn(true);

        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertTrue(matcher.hasSameMethod(candidate));
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = mock(Invocation.class);
        Location location = mock(Location.class);
        Mockito.when(invocation.getLocation()).thenReturn(location);

        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertEquals(location, matcher.getLocation());
    }

    @Test
    public void testToStringWithPrintSettings() {
        Invocation invocation = mock(Invocation.class);
        PrintSettings printSettings = mock(PrintSettings.class);
        List<Matcher> matchers = Arrays.asList(mock(Matcher.class), mock(Matcher.class));
        Mockito.when(invocation.toString(matchers, printSettings)).thenReturn("mockito");

        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        assertEquals("mockito", matcher.toString(printSettings));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        Invocation i = mock(Invocation.class);
        Object[] arguments = new Object[2];
        arguments[0] = "arg1";
        arguments[1] = "arg2";
        Mockito.when(invocation.getArguments()).thenReturn(arguments);
        Mockito.when(i.getArguments()).thenReturn(arguments);
        Matcher matcher1 = mock(Matcher.class);
        Matcher matcher2 = mock(Matcher.class);
        Invocations.actually_captured = null;
        Mockito.when(matcher1 instanceof CapturesArguments).thenReturn(true);
        Mockito.when(matcher2 instanceof CapturesArguments).thenReturn(false);
        Mockito.when(matcher1.matches(i.getArguments()[0])).thenReturn(true);
        Mockito.when(matcher2.matches(i.getArguments()[1])).thenReturn(true);

        InvocationMatcher matcher = new InvocationMatcher(invocation, Arrays.asList(matcher1, matcher2));
        matcher.captureArgumentsFrom(i);

        assertEquals("arg1", Invocations.actually_captured.get(0));
        assertNull(Invocations.actually_captured.get(1));
    }
}
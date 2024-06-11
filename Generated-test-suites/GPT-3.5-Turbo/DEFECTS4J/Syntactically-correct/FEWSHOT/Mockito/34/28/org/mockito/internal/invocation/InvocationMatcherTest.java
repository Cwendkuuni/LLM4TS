package org.mockito.internal.invocation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;

@SuppressWarnings("unchecked")
public class InvocationMatcherTest {

    @Test
    public void testConstructorWithMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Collections.singletonList(mock(Matcher.class));

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
    public void testMatches() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        Invocation invocation3 = mock(Invocation.class);

        when(invocation1.getMock()).thenReturn(mock(Object.class));
        when(invocation2.getMock()).thenReturn(invocation1.getMock());
        when(invocation3.getMock()).thenReturn(mock(Object.class));

        when(invocation1.getMethod()).thenReturn(mock(Method.class));
        when(invocation2.getMethod()).thenReturn(invocation1.getMethod());
        when(invocation3.getMethod()).thenReturn(mock(Method.class));

        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        when(argumentsComparator.argumentsMatch(any(InvocationMatcher.class), any(Invocation.class)))
                .thenReturn(true);
        when(argumentsComparator.argumentsMatch(any(InvocationMatcher.class), any(Object[].class)))
                .thenReturn(true);

        whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(argumentsComparator);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        assertTrue(invocationMatcher.matches(invocation2));
        assertFalse(invocationMatcher.matches(invocation3));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        Invocation invocation3 = mock(Invocation.class);

        when(invocation1.getMethod()).thenReturn(mock(Method.class));
        when(invocation2.getMethod()).thenReturn(invocation1.getMethod());
        when(invocation3.getMethod()).thenReturn(mock(Method.class));

        when(invocation1.getMock()).thenReturn(mock(Object.class));
        when(invocation2.getMock()).thenReturn(invocation1.getMock());
        when(invocation3.getMock()).thenReturn(mock(Object.class));

        when(invocation1.isVerified()).thenReturn(false);
        when(invocation2.isVerified()).thenReturn(true);
        when(invocation3.isVerified()).thenReturn(false);

        when(invocation1.getArguments()).thenReturn(new Object[] { "arg1" });
        when(invocation2.getArguments()).thenReturn(new Object[] { "arg1" });
        when(invocation3.getArguments()).thenReturn(new Object[] { "arg2" });

        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        when(argumentsComparator.argumentsMatch(any(InvocationMatcher.class), any(Invocation.class)))
                .thenReturn(true);
        when(argumentsComparator.argumentsMatch(any(InvocationMatcher.class), any(Object[].class)))
                .thenReturn(false);

        whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(argumentsComparator);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        assertFalse(invocationMatcher.hasSimilarMethod(invocation2));
        assertFalse(invocationMatcher.hasSimilarMethod(invocation3));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        Invocation invocation3 = mock(Invocation.class);

        Method method = mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method);
        when(invocation2.getMethod()).thenReturn(method);
        when(invocation3.getMethod()).thenReturn(mock(Method.class));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        assertTrue(invocationMatcher.hasSameMethod(invocation2));
        assertFalse(invocationMatcher.hasSameMethod(invocation3));
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
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Collections.singletonList(mock(Matcher.class));
        PrintSettings printSettings = mock(PrintSettings.class);

        when(invocation.toString(matchers, printSettings)).thenReturn("Invocation String");

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals("Invocation String", invocationMatcher.toString(printSettings));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        Object[] arguments = { "arg1", "arg2" };

        when(invocation.getArguments()).thenReturn(arguments);
        when(invocation.getMethod()).thenReturn(method);

        ArgumentCaptureMatcher matcher1 = mock(ArgumentCaptureMatcher.class);
        ArgumentCaptureMatcher matcher2 = mock(ArgumentCaptureMatcher.class);
        List<Matcher> matchers = Lists.newArrayList(matcher1, matcher2);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        invocationMatcher.captureArgumentsFrom(invocation);

        verify(matcher1).captureFrom("arg1");
        verify(matcher2).captureFrom("arg2");
    }

}
package org.mockito.internal.invocation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import org.hamcrest.Matcher;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.invocation.LocationProvider;
import org.mockito.mock.MockCreationSettings;

@SuppressWarnings("unchecked")
public class InvocationMatcherTest {
    @Test
    public void testConstructor() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Collections.emptyList();

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorNoMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Collections.emptyList();

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
        PrintSettings printSettings = mock(PrintSettings.class);
        List<Matcher> matchers = Collections.emptyList();
        when(invocation.toString(matchers, printSettings)).thenReturn("toStringResult");

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals("toStringResult", invocationMatcher.toString());
    }

    @Test
    public void testMatches() {
        Invocation invocation = mock(Invocation.class);
        Invocation actual = mock(Invocation.class);

        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(argumentsComparator);

        List<Matcher> matchers = Collections.emptyList();
        when(invocation.getMock()).thenReturn("mock");
        when(actual.getMock()).thenReturn("mock");
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(actual.getMethod()).thenReturn(mock(Method.class));
        when(argumentsComparator.argumentsMatch(any(InvocationMatcher.class), any(Invocation.class))).thenReturn(true);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertTrue(invocationMatcher.matches(actual));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Invocation invocation = mock(Invocation.class);
        Object[] actualArgs = new Object[0];

        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(argumentsComparator);
        when(argumentsComparator.argumentsMatch(any(InvocationMatcher.class), any(Object[].class))).thenReturn(true);

        List<Matcher> matchers = Collections.emptyList();

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertTrue(invocationMatcher.safelyArgumentsMatch(actualArgs));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);

        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(argumentsComparator);
        when(argumentsComparator.argumentsMatch(any(InvocationMatcher.class), any(Object[].class))).thenReturn(false);

        List<Matcher> matchers = Collections.emptyList();

        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(invocation.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn("mock");
        when(candidate.getMock()).thenReturn("mock");

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(candidate.getMethod()).thenReturn(method);

        List<Matcher> matchers = Collections.emptyList();

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertTrue(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testToStringWithPrintSettings() {
        Invocation invocation = mock(Invocation.class);
        PrintSettings printSettings = mock(PrintSettings.class);
        List<Matcher> matchers = Collections.emptyList();
        when(invocation.toString(matchers, printSettings)).thenReturn("toStringResult");

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals("toStringResult", invocationMatcher.toString(printSettings));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        Invocation i = mock(Invocation.class);

        Matcher matcher = mock(Matcher.class);
        when(matcher instanceof CapturesArguments).thenReturn(true);

        List<Matcher> matchers = Collections.singletonList(matcher);

        Object[] args = { "arg" };
        when(i.getArguments()).thenReturn(args);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        invocationMatcher.captureArgumentsFrom(i);

        verify((CapturesArguments) matcher).captureFrom("arg");
    }
}
package org.mockito.internal.invocation;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.invocation.Invocation;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Collections.singletonList(Matchers.any());

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertSame(invocation, invocationMatcher.getInvocation());
        assertSame(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithoutMatchers() {
        Invocation invocation = mock(Invocation.class);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertSame(invocation, invocationMatcher.getInvocation());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testGetMethod() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertSame(method, invocationMatcher.getMethod());
    }

    @Test
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Collections.singletonList(Matchers.any());
        PrintSettings printSettings = mock(PrintSettings.class);
        String expectedToString = "Invocation String";

        when(invocation.toString(matchers, printSettings)).thenReturn(expectedToString);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        String toStringResult = invocationMatcher.toString(printSettings);

        assertEquals(expectedToString, toStringResult);
    }

    @Test
    public void testMatches() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getArguments()).thenReturn(new Object[]{"test"});
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        Invocation actual = mock(Invocation.class);
        when(actual.getMock()).thenReturn(mock(Object.class));
        when(actual.getArguments()).thenReturn(new Object[]{"test"});

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertTrue(invocationMatcher.matches(actual));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getArguments()).thenReturn(new Object[]{"test"});
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn(mock(Object.class));
        when(candidate.getArguments()).thenReturn(new Object[]{"test"});

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1, method2);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertTrue(invocationMatcher.hasSameMethod(invocation));
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = mock(Invocation.class);
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertSame(location, invocationMatcher.getLocation());
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        CaptureMatcher captureMatcher = mock(CaptureMatcher.class);
        when(captureMatcher instanceof CapturesArguments).thenReturn(true);
        when(invocation.getArguments()).thenReturn(new Object[]{"test"});
        when(captureMatcher.captureFrom(eq("test"))).thenReturn(true);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        invocationMatcher.captureArgumentsFrom(invocation);

        verify(captureMatcher).captureFrom(eq("test"));
    }

    @Test
    public void testCreateFrom() {
        Invocation invocation = mock(Invocation.class);
        List<Invocation> invocations = Collections.singletonList(invocation);
        List<InvocationMatcher> expectedInvocationMatchers = Collections.singletonList(new InvocationMatcher(invocation));

        List<InvocationMatcher> actualInvocationMatchers = InvocationMatcher.createFrom(invocations);

        assertEquals(expectedInvocationMatchers, actualInvocationMatchers);
    }

    private interface CaptureMatcher extends Matcher, CapturesArguments<Object> {
    }
}
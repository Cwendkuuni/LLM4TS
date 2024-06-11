package org.mockito.internal.invocation;

import static org.junit.Assert.*;
import org.hamcrest.Matcher;
import org.junit.Test;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcher1() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Collections.emptyList();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Method method = invocation.getMethod();
        assertEquals(method, invocationMatcher.getMethod());
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testInvocationMatcher2() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Method method = invocation.getMethod();
        assertEquals(method, invocationMatcher.getMethod());
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(Collections.emptyList(), invocationMatcher.getMatchers());
    }

    @Test
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Collections.emptyList();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        String expectedResult = invocation.toString(matchers, new PrintSettings());
        assertEquals(expectedResult, invocationMatcher.toString());
    }

    @Test
    public void testMatches() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getArguments()).thenReturn(new Object[]{});
        Invocation actual = mock(Invocation.class);
        when(actual.getMock()).thenReturn(mock(Object.class));
        when(actual.getMethod()).thenReturn(mock(Method.class));
        when(actual.getArguments()).thenReturn(new Object[]{});
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(argumentsComparator);
        when(argumentsComparator.argumentsMatch(any(InvocationMatcher.class), any(Invocation.class))).thenReturn(true);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertTrue(invocationMatcher.matches(actual));
    }

    @Test
    public void testMatchesWithDifferentMock() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getArguments()).thenReturn(new Object[]{});
        Invocation actual = mock(Invocation.class);
        when(actual.getMock()).thenReturn(mock(Object.class));
        when(actual.getMethod()).thenReturn(mock(Method.class));
        when(actual.getArguments()).thenReturn(new Object[]{});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertFalse(invocationMatcher.matches(actual));
    }

    @Test
    public void testMatchesWithDifferentMethod() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getArguments()).thenReturn(new Object[]{});
        Invocation actual = mock(Invocation.class);
        when(actual.getMock()).thenReturn(mock(Object.class));
        when(actual.getMethod()).thenReturn(mock(Method.class));
        when(actual.getArguments()).thenReturn(new Object[]{});
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(argumentsComparator);
        when(argumentsComparator.argumentsMatch(any(InvocationMatcher.class), any(Invocation.class))).thenReturn(false);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertFalse(invocationMatcher.matches(actual));
    }

    @Test
    public void testToStringWithPrintSettings() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Collections.emptyList();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        PrintSettings printSettings = mock(PrintSettings.class);
        String expectedResult = invocation.toString(matchers, printSettings);
        assertEquals(expectedResult, invocationMatcher.toString(printSettings));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation i = mock(Invocation.class);
        Matcher m = mock(CapturesArguments.class);
        Object[] arguments = new Object[]{};
        when(i.getArguments()).thenReturn(arguments);
        when(i.getMethod()).thenReturn(mock(Method.class));
        List<Matcher> matchers = Collections.singletonList(m);
        InvocationMatcher invocationMatcher = new InvocationMatcher(i, matchers);
        invocationMatcher.captureArgumentsFrom(i);
        verify((CapturesArguments) m, times(1)).captureFrom(arguments[0]);
    }
}
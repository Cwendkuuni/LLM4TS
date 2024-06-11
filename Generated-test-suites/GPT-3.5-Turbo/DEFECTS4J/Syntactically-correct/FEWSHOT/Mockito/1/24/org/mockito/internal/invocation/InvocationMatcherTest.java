package org.mockito.internal.invocation;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.internal.reporting.PrintSettings;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {
    @Test
    public void testConstructorWithInvocationAndMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new LinkedList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
        assertEquals(invocation.getMethod(), invocationMatcher.getMethod());
    }

    @Test
    public void testConstructorWithInvocation() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(Collections.emptyList(), invocationMatcher.getMatchers());
        assertEquals(invocation.getMethod(), invocationMatcher.getMethod());
    }

    @Test
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new LinkedList<>(Arrays.asList(mock(Matcher.class), mock(Matcher.class)));
        PrintSettings printSettings = mock(PrintSettings.class);
        when(printSettings.print(matchers, invocation)).thenReturn("String representation");

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals("String representation", invocationMatcher.toString());
        verify(printSettings).print(matchers, invocation);
    }

    @Test
    public void testMatchesWithMatchingInvocation() {
        Invocation invocation = mock(Invocation.class);
        Invocation actual = mock(Invocation.class);

        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(actual.getMock()).thenReturn(invocation.getMock());
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(actual.getMethod()).thenReturn(invocation.getMethod());

        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        when(argumentsComparator.argumentsMatch(any(InvocationMatcher.class), any(Invocation.class))).thenReturn(true);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        invocationMatcher.matches(actual);

        verify(invocation).getMock();
        verify(actual).getMock();
        verify(invocation).getMethod();
        verify(actual).getMethod();
        verify(argumentsComparator).argumentsMatch(invocationMatcher, actual);
    }

    @Test
    public void testMatchesWithNonMatchingInvocation() {
        Invocation invocation = mock(Invocation.class);
        Invocation actual = mock(Invocation.class);

        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(actual.getMock()).thenReturn(invocation.getMock());
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(actual.getMethod()).thenReturn(invocation.getMethod());

        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        when(argumentsComparator.argumentsMatch(any(InvocationMatcher.class), any(Invocation.class))).thenReturn(false);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        invocationMatcher.matches(actual);

        verify(invocation).getMock();
        verify(actual).getMock();
        verify(invocation).getMethod();
        verify(actual).getMethod();
        verify(argumentsComparator).argumentsMatch(invocationMatcher, actual);
    }

    @Test
    public void testHasSimilarMethodWithDifferentMethodName() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);

        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getMethod().getName()).thenReturn("name1");
        when(candidate.getMethod().getName()).thenReturn("name2");

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        verify(invocation).getMethod();
        verify(candidate).getMethod();
        verify(invocation.getMethod()).getName();
        verify(candidate.getMethod()).getName();

        assertEquals(false, result);
    }

    @Test
    public void testHasSimilarMethodWhenCandidateIsVerified() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);

        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getMethod().getName()).thenReturn("name");
        when(candidate.getMethod().getName()).thenReturn("name");
        when(candidate.isVerified()).thenReturn(true);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        verify(invocation).getMethod();
        verify(candidate).getMethod();
        verify(invocation.getMethod()).getName();
        verify(candidate.getMethod()).getName();
        verify(candidate).isVerified();

        assertEquals(false, result);
    }

    @Test
    public void testHasSimilarMethodWhenCandidateIsDifferentMock() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);

        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getMethod().getName()).thenReturn("name");
        when(candidate.getMethod().getName()).thenReturn("name");
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(candidate.getMock()).thenReturn(mock(Object.class));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        verify(invocation).getMethod();
        verify(candidate).getMethod();
        verify(invocation.getMethod()).getName();
        verify(candidate.getMethod()).getName();
        verify(candidate).isVerified();
        verify(invocation).getMock();
        verify(candidate).getMock();

        assertEquals(false, result);
    }

    @Test
    public void testHasSimilarMethodWhenMethodIsSame() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);

        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(invocation.getMethod());
        when(invocation.getMethod().getName()).thenReturn("name");
        when(candidate.getMethod().getName()).thenReturn("name");
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(candidate.getMock()).thenReturn(mock(Object.class));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        verify(invocation).getMethod();
        verify(candidate).getMethod();
        verify(invocation.getMethod()).getName();
        verify(candidate.getMethod()).getName();
        verify(candidate).isVerified();
        verify(invocation).getMock();
        verify(candidate).getMock();

        assertEquals(true, result);
    }

    @Test
    public void testHasSimilarMethodWhenMethodIsDifferent() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);

        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getMethod().getName()).thenReturn("name");
        when(candidate.getMethod().getName()).thenReturn("name");
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(candidate.getMock()).thenReturn(mock(Object.class));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        verify(invocation).getMethod();
        verify(candidate).getMethod();
        verify(invocation.getMethod()).getName();
        verify(candidate.getMethod()).getName();
        verify(candidate).isVerified();
        verify(invocation).getMock();
        verify(candidate).getMock();

        assertEquals(false, result);
    }

    @Test
    public void testCaptureArgumentsFromWithVarArgs() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        invocationMatcher.captureArgumentsFrom(invocation);
    }

    @Test
    public void testCaptureArgumentsFromWithNonVarArgs() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Matcher matcher1 = mock(Matcher.class);
        Matcher matcher2 = mock(Matcher.class);
        Matcher matcher3 = mock(Matcher.class);
        List<Matcher> matchers = new LinkedList<>(Arrays.asList(matcher1, matcher2, matcher3));
        invocationMatcher = new InvocationMatcher(invocation, matchers);

        invocationMatcher.captureArgumentsFrom(invocation);

        verify(matcher1).captureFrom(invocation.getArgumentAt(0, Object.class));
        verify(matcher2).captureFrom(invocation.getArgumentAt(1, Object.class));
        verify(matcher3).captureFrom(invocation.getArgumentAt(2, Object.class));
    }

    @Test
    public void testCreateFrom() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<Invocation> invocations = new LinkedList<>(Arrays.asList(invocation1, invocation2));

        List<InvocationMatcher> result = InvocationMatcher.createFrom(invocations);

        assertEquals(2, result.size());
        assertEquals(invocation1, result.get(0).getInvocation());
        assertEquals(invocation2, result.get(1).getInvocation());
    }
}
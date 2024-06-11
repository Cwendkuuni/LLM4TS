package org.mockito.internal.invocation;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;

@SuppressWarnings("unchecked")
public class InvocationMatcherTest {

    @Test
    public void testConstructorWithMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Collections.emptyList();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertSame(invocation, invocationMatcher.getInvocation());
        assertSame(matchers, invocationMatcher.getMatchers());
    }
    
    @Test
    public void testConstructorWithoutMatchers() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertSame(invocation, invocationMatcher.getInvocation());
        assertEquals(invocation.argumentsToMatchers(), invocationMatcher.getMatchers());
    }

    @Test
    public void testGetMethod() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Method expectedMethod = invocation.getMethod();
        Method actualMethod = invocationMatcher.getMethod();

        assertSame(expectedMethod, actualMethod);
    }
    
    @Test
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Collections.emptyList();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        PrintSettings printSettings = new PrintSettings();

        String expected = invocation.toString(matchers, printSettings);
        String actual = invocationMatcher.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testMatches() {
        Invocation invocation = mock(Invocation.class);
        Object mock = mock(Object.class);
        when(invocation.getMock()).thenReturn(mock);
        when(invocation.getArguments()).thenReturn(new Object[] {"arg1", "arg2"});

        Invocation actualInvocation = mock(Invocation.class);
        when(actualInvocation.getMock()).thenReturn(mock);
        when(actualInvocation.getArguments()).thenReturn(new Object[] {"arg1", "arg2"});

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean matches = invocationMatcher.matches(actualInvocation);

        assertTrue(matches);
    }

    @Test
    public void testMatchesWithDifferentMock() {
        Invocation invocation = mock(Invocation.class);
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        when(invocation.getMock()).thenReturn(mock1);
        when(invocation.getArguments()).thenReturn(new Object[] {"arg1", "arg2"});

        Invocation actualInvocation = mock(Invocation.class);
        when(actualInvocation.getMock()).thenReturn(mock2);
        when(actualInvocation.getArguments()).thenReturn(new Object[] {"arg1", "arg2"});

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean matches = invocationMatcher.matches(actualInvocation);

        assertFalse(matches);
    }

    @Test
    public void testMatchesWithDifferentArguments() {
        Invocation invocation = mock(Invocation.class);
        Object mock = mock(Object.class);
        when(invocation.getMock()).thenReturn(mock);
        when(invocation.getArguments()).thenReturn(new Object[] {"arg1", "arg2"});

        Invocation actualInvocation = mock(Invocation.class);
        when(actualInvocation.getMock()).thenReturn(mock);
        when(actualInvocation.getArguments()).thenReturn(new Object[] {"differentArg1", "differentArg2"});

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean matches = invocationMatcher.matches(actualInvocation);

        assertFalse(matches);
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);

        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(candidate.getMethod()).thenReturn(method);

        Object[] arguments = new Object[] {"arg1", "arg2"};
        when(invocation.getArguments()).thenReturn(arguments);
        when(candidate.getArguments()).thenReturn(arguments);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidate);

        assertTrue(hasSimilarMethod);
    }

    @Test
    public void testHasSimilarMethodWithDifferentMethodName() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);

        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(candidate.getMethod()).thenReturn(method2);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidate);

        assertFalse(hasSimilarMethod);
    }

    @Test
    public void testHasSimilarMethodWhenVerified() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);

        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(candidate.getMethod()).thenReturn(method);

        Object[] arguments = new Object[] {"arg1", "arg2"};
        when(invocation.getArguments()).thenReturn(arguments);
        when(candidate.getArguments()).thenReturn(arguments);

        when(candidate.isVerified()).thenReturn(true);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidate);

        assertFalse(hasSimilarMethod);
    }

    @Test
    public void testHasSimilarMethodWithDifferentMock() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);

        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(candidate.getMethod()).thenReturn(method);

        Object[] arguments = new Object[] {"arg1", "arg2"};
        when(invocation.getArguments()).thenReturn(arguments);
        when(candidate.getArguments()).thenReturn(arguments);

        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        when(invocation.getMock()).thenReturn(mock1);
        when(candidate.getMock()).thenReturn(mock2);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidate);

        assertFalse(hasSimilarMethod);
    }

    @Test
    public void testHasSimilarMethodWithOverloadedButSameArgsMethod() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);

        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(candidate.getMethod()).thenReturn(method2);

        Object[] arguments = new Object[] {"arg1", "arg2"};
        when(invocation.getArguments()).thenReturn(arguments);
        when(candidate.getArguments()).thenReturn(arguments);

        when(invocation.isVerified()).thenReturn(false);
        when(candidate.isVerified()).thenReturn(false);

        Object[] differentArguments = new Object[] {"differentArg1", "differentArg2"};
        when(invocation.getArguments()).thenReturn(differentArguments);
        when(candidate.getArguments()).thenReturn(differentArguments);

        when(method1.equals(method2)).thenReturn(true);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidate);

        assertFalse(hasSimilarMethod);
    }
    
    @Test
    public void testHasSameMethod() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);

        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(candidate.getMethod()).thenReturn(method2);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean hasSameMethod = invocationMatcher.hasSameMethod(candidate);

        assertFalse(hasSameMethod);
    }
    
    @Test
    public void testGetLocation() {
        Invocation invocation = mock(Invocation.class);
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Location actualLocation = invocationMatcher.getLocation();

        assertSame(location, actualLocation);
    }

    @Test
    public void testToStringWithPrintSettings() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Collections.emptyList();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        PrintSettings printSettings = new PrintSettings();

        String expected = invocation.toString(matchers, printSettings);
        String actual = invocationMatcher.toString(printSettings);

        assertEquals(expected, actual);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        Invocation i = mock(Invocation.class);
        Object argument1 = new Object();
        Object argument2 = new Object();
        Object[] arguments = new Object[] {argument1, argument2};
        when(i.getArguments()).thenReturn(arguments);

        CapturesArguments capturesArguments1 = mock(CapturesArguments.class);
        CapturesArguments capturesArguments2 = mock(CapturesArguments.class);
        List<Matcher> matchers = mock(List.class);
        when(matchers.get(0)).thenReturn(capturesArguments1);
        when(matchers.get(1)).thenReturn(capturesArguments2);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        invocationMatcher.captureArgumentsFrom(i);

        verify(capturesArguments1).captureFrom(argument1);
        verify(capturesArguments2).captureFrom(argument2);
    }
}
package org.mockito.internal.invocation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockCreationSettings;
import org.mockito.quality.Strictness;

public class InvocationMatcherTest {
    private Invocation invocation;
    private Method method;
    private InvocationMatcher invocationMatcher;

    @Before
    public void setup() throws NoSuchMethodException {
        method = getClass().getDeclaredMethod("dummyMethod");
        invocation = new InvocationOnMock(mock(MockCreationSettings.class), method, new Object[]{});
        invocationMatcher = new InvocationMatcher(invocation);
    }

    @Test
    public void testGetMethod() {
        assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testGetInvocation() {
        assertEquals(invocation, invocationMatcher.getInvocation());
    }

    @Test
    public void testGetMatchers() {
        List<Matcher> matchers = new ArrayList<Matcher>();
        matchers.add(mock(Matcher.class));
        invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testToString() {
        assertEquals(invocation.toString(), invocationMatcher.toString());
    }

    @Test
    public void testMatches() throws NoSuchMethodException {
        Invocation actualInvocation = new InvocationOnMock(mock(MockCreationSettings.class), method, new Object[]{});

        assertTrue(invocationMatcher.matches(actualInvocation));
    }

    @Test
    public void testMatchesWithDifferentMock() throws NoSuchMethodException {
        Invocation actualInvocation = new InvocationOnMock(mock(MockCreationSettings.class), method, new Object[]{});

        InvocationMatcher actualInvocationMatcher = new InvocationMatcher(actualInvocation);

        assertFalse(invocationMatcher.matches(actualInvocationMatcher.getInvocation()));
    }

    @Test
    public void testMatchesWithDifferentMethod() throws NoSuchMethodException {
        Method differentMethod = getClass().getDeclaredMethod("dummyMethod2");
        Invocation actualInvocation = new InvocationOnMock(mock(MockCreationSettings.class), differentMethod, new Object[]{});

        InvocationMatcher actualInvocationMatcher = new InvocationMatcher(actualInvocation);

        assertFalse(invocationMatcher.matches(actualInvocationMatcher.getInvocation()));
    }

    @Test
    public void testHasSimilarMethodWithDifferentMethod() throws NoSuchMethodException {
        Method differentMethod = getClass().getDeclaredMethod("dummyMethod2");
        Invocation actualInvocation = new InvocationOnMock(mock(MockCreationSettings.class), differentMethod, new Object[]{});

        assertFalse(invocationMatcher.hasSimilarMethod(actualInvocation));
    }

    @Test
    public void testHasSimilarMethodWithOverloadedButSameArgsMethod() throws NoSuchMethodException {
        Method overloadedMethod = getClass().getDeclaredMethod("dummyMethod2");
        Invocation overloadedInvocation = new InvocationOnMock(mock(MockCreationSettings.class), overloadedMethod, new Object[]{});

        assertTrue(invocationMatcher.hasSimilarMethod(overloadedInvocation));
    }

    @Test
    public void testHasSimilarMethodWithSameMethod() throws NoSuchMethodException {
        Invocation invocationCopy = new InvocationOnMock(mock(MockCreationSettings.class), method, new Object[]{});

        assertTrue(invocationMatcher.hasSimilarMethod(invocationCopy));
    }

    @Test
    public void testHasSameMethodWithDifferentMethod() throws NoSuchMethodException {
        Method differentMethod = getClass().getDeclaredMethod("dummyMethod2");
        Invocation actualInvocation = new InvocationOnMock(mock(MockCreationSettings.class), differentMethod, new Object[]{});

        assertFalse(invocationMatcher.hasSameMethod(actualInvocation));
    }

    @Test
    public void testHasSameMethodWithSameMethod() throws NoSuchMethodException {
        Invocation invocationCopy = new InvocationOnMock(mock(MockCreationSettings.class), method, new Object[]{});

        assertTrue(invocationMatcher.hasSameMethod(invocationCopy));
    }

    @Test
    public void testGetLocation() {
        assertEquals(invocation.getLocation(), invocationMatcher.getLocation());
    }

    @Test
    public void testToStringWithPrintSettings() {
        PrintSettings printSettings = mock(PrintSettings.class);
        assertEquals(invocation.toString(printSettings), invocationMatcher.toString(printSettings));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation i = mock(Invocation.class);
        Object[] arguments = new Object[]{"argument1", "argument2"};
        when(i.getArguments()).thenReturn(arguments);

        CapturesArguments capturesArguments = mock(CapturesArguments.class);

        List<Matcher> matchers = new ArrayList<Matcher>();
        matchers.add(capturesArguments);

        invocationMatcher = new InvocationMatcher(invocation, matchers);
        invocationMatcher.captureArgumentsFrom(i);

        for (int j = 0; j < arguments.length; j++) {
            assertEquals(arguments[j], capturesArguments.getCapturedArguments().get(j));
        }
    }
    
    private void dummyMethod() {
    }

    private void dummyMethod2() {
    }
}
package org.mockito.internal.invocation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.internal.matchers.Any;
import org.mockito.invocation.Invocation;

public class InvocationMatcherTest {

    @Test
    public void testGetMethod() {
        Method method = mock(Method.class);
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, null);
        invocationMatcher.getMethod();
        assertTrue(invocation.getMethod());
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, null);
        invocationMatcher.getInvocation();
        assertTrue(invocation.getInvocation());
        assertTrue(invocationMatcher.getInvocation() == invocation);
    }

    @Test
    public void testGetMatchers() {
        List<Matcher> matchers = new ArrayList<Matcher>(Arrays.asList(new Any()));
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertTrue(invocationMatcher.getMatchers() instanceof List);
        assertTrue(invocationMatcher.getMatchers().isEmpty() == false);
        assertTrue(invocationMatcher.getMatchers().get(0) instanceof Matcher);
    }

    @Test
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, null);
        assertTrue(invocationMatcher.toString() instanceof String);
    }

    @Test
    public void testMatches() {
        Invocation invocation = mock(Invocation.class);
        Invocation actual = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, null);
        assertFalse(invocationMatcher.matches(actual));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, null);
        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, null);
        assertTrue(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, null);
        assertTrue(invocationMatcher.getLocation() instanceof Location);
    }

    @Test
    public void testToStringWithPrintSettings() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, null);
        assertTrue(invocationMatcher.toString(new PrintSettings()) instanceof String);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation i = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(i, null);
        invocationMatcher.captureArgumentsFrom(i);
        assertTrue(i.captureArgumentsFromCalled());
    }
}
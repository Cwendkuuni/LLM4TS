package org.mockito.internal.invocation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;

public class InvocationMatcherTest {
    @Test
    public void testConstructor1() {
        Invocation invocation = new Invocation(null, null, null);
        List<Matcher> matchers = new ArrayList<Matcher>();
        matchers.add(null);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructor2() {
        Invocation invocation = new Invocation(null, null, null);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(new ArrayList<Matcher>(), invocationMatcher.getMatchers());
    }

    @Test
    public void testGetMethod() {
        Invocation invocation = new Invocation(null, null, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Method method = invocation.getMethod();
        assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testToString() {
        Invocation invocation = new Invocation(null, null, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        PrintSettings printSettings = new PrintSettings();

        String toString = invocation.toString();
        assertEquals(toString, invocationMatcher.toString());
        assertEquals(toString, invocationMatcher.toString(printSettings));
    }

    @Test
    public void testMatches() {
        Invocation invocation = new Invocation(null, null, null);
        Invocation actual = new Invocation(null, null, null);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertTrue(invocationMatcher.matches(actual));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Invocation invocation = new Invocation(null, null, null);
        Object[] actualArgs = {};

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertTrue(invocationMatcher.safelyArgumentsMatch(actualArgs));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = new Invocation(null, null, null);
        Invocation candidate = new Invocation(null, null, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertTrue(invocationMatcher.hasSimilarMethod(candidate));

        candidate = new Invocation(null, null, null);
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation = new Invocation(null, null, null);
        Invocation candidate = new Invocation(null, null, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertTrue(invocationMatcher.hasSameMethod(candidate));

        candidate = new Invocation(null, null, null);
        assertFalse(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = new Invocation(null, null, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Location location = new Location();

        invocation.setLocation(location);
        assertEquals(location, invocationMatcher.getLocation());
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = new Invocation(null, null, null);
        Invocation i = new Invocation(null, null, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Matcher m1 = null;
        Matcher m2 = new CapturesArguments();
        invocationMatcher.getMatchers().add(m1);
        invocationMatcher.getMatchers().add(m2);

        invocationMatcher.captureArgumentsFrom(i);
        assertTrue(((CapturesArguments) m2).getCapturedArguments().isEmpty());

        Object[] args = { new Object() };
        i.setArguments(args);
        invocationMatcher.captureArgumentsFrom(i);
        assertEquals(args[0], ((CapturesArguments) m2).getCapturedArguments().get(0));
    }
}
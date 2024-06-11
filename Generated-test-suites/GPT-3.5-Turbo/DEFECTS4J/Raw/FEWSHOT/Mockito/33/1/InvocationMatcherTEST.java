
package org.mockito.internal.invocation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;

public class InvocationMatcherTest {

    private Invocation invocation;

    @Before
    public void setup() {
        invocation = new Invocation();
    }

    @Test
    public void testConstructorWithMatchers() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new CapturesArguments());
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithEmptyMatchers() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(invocation.argumentsToMatchers(), invocationMatcher.getMatchers());
    }

    @Test
    public void testGetMethod() {
        Method method = new Method();
        invocation.setMethod(method);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testToString() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(invocation.toString(invocation.argumentsToMatchers(), new PrintSettings()), invocationMatcher.toString());
    }

    @Test
    public void testMatches() {
        Invocation actual = new Invocation();
        invocation.setMock(new Object());
        actual.setMock(invocation.getMock());
        assertTrue(invocationMatcher.matches(actual));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertTrue(invocationMatcher.safelyArgumentsMatch(Collections.emptyArray()));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation candidate = new Invocation();
        candidate.setMethod(invocation.getMethod());
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSameMethod() {
        Invocation candidate = new Invocation();
        candidate.setMethod(invocation.getMethod());
        assertTrue(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testGetLocation() {
        Location location = new Location();
        invocation.setLocation(location);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(location, invocationMatcher.getLocation());
    }

    @Test
    public void testToStringWithPrintSettings() {
        PrintSettings printSettings = new PrintSettings();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(invocation.toString(invocation.argumentsToMatchers(), printSettings), invocationMatcher.toString(printSettings));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation i = new Invocation();
        i.setArguments(new Object[] {"arg1", "arg2"});
        CapturesArguments capturesArguments = new CapturesArguments();
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(capturesArguments);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        invocationMatcher.captureArgumentsFrom(i);
        assertEquals("arg1", capturesArguments.getCapturedArguments().get(0));
        assertEquals("arg2", capturesArguments.getCapturedArguments().get(1));
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new LinkedList<>();
        Invocation i = new Invocation();
        invocations.add(i);
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        assertEquals(invocations.size(), invocationMatchers.size());
        assertEquals(i, invocationMatchers.get(0).getInvocation());
    }
}

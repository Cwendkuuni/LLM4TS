package org.mockito.internal.invocation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;

public class InvocationMatcherTest {
    private Invocation invocation;

    @Before
    public void setUp() {
        invocation = new Invocation();
    }

    @Test
    public void testConstructorWithMatchersNotEmpty() {
        List<Matcher> matchers = new ArrayList<>();
        Matcher matcher1 = new CustomMatcher();
        Matcher matcher2 = new CustomMatcher();
        matchers.add(matcher1);
        matchers.add(matcher2);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithMatchersEmpty() {
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
        PrintSettings printSettings = new PrintSettings();
        String expected = invocation.toString(invocation.argumentsToMatchers(), printSettings);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(expected, invocationMatcher.toString());
    }

    @Test
    public void testMatches() {
        Invocation actualInvocation = new Invocation();

        invocation.setMock(new Mock());
        actualInvocation.setMock(new Mock());
        invocation.setArguments(new Object[] { "arg1", 2 });
        actualInvocation.setArguments(new Object[] { "arg1", 2 });

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertTrue(invocationMatcher.matches(actualInvocation));
    }

    @Test
    public void testMatchesDifferentMock() {
        Invocation actualInvocation = new Invocation();

        invocation.setMock(new Mock());
        actualInvocation.setMock(new Mock());
        invocation.setArguments(new Object[] { "arg1", 2 });
        actualInvocation.setArguments(new Object[] { "arg1", 2 });

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertFalse(invocationMatcher.matches(actualInvocation));
    }

    @Test
    public void testMatchesDifferentMethod() {
        Invocation actualInvocation = new Invocation();

        invocation.setMock(new Mock());
        actualInvocation.setMock(new Mock());
        invocation.setArguments(new Object[] { "arg1", 2 });
        actualInvocation.setArguments(new Object[] { "arg2", 2 });

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertFalse(invocationMatcher.matches(actualInvocation));
    }

    @Test
    public void testMatchesDifferentArguments() {
        Invocation actualInvocation = new Invocation();

        invocation.setMock(new Mock());
        actualInvocation.setMock(new Mock());
        invocation.setArguments(new Object[] { "arg1", 2 });
        actualInvocation.setArguments(new Object[] { "arg1", 3 });

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertFalse(invocationMatcher.matches(actualInvocation));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation candidate = new Invocation();

        invocation.setMethod(new Method());
        candidate.setMethod(new Method());
        invocation.setMock(new Mock());
        candidate.setMock(new Mock());
        invocation.setArguments(new Object[] { "arg1", 2 });
        candidate.setArguments(new Object[] { "arg1", 2 });

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethodDifferentMethod() {
        Invocation candidate = new Invocation();

        invocation.setMethod(new Method());
        candidate.setMethod(new Method());
        invocation.setMock(new Mock());
        candidate.setMock(new Mock());
        invocation.setArguments(new Object[] { "arg1", 2 });
        candidate.setArguments(new Object[] { "arg2", 2 });

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethodDifferentMock() {
        Invocation candidate = new Invocation();

        invocation.setMethod(new Method());
        candidate.setMethod(new Method());
        invocation.setMock(new Mock());
        candidate.setMock(new Mock());
        invocation.setArguments(new Object[] { "arg1", 2 });
        candidate.setArguments(new Object[] { "arg1", 2 });

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethodOverloadedButSameArgs() {
        Invocation candidate = new Invocation();

        invocation.setMethod(new Method());
        candidate.setMethod(new Method());
        invocation.setMock(new Mock());
        candidate.setMock(new Mock());
        invocation.setArguments(new Object[] { "arg1", 2 });
        candidate.setArguments(new Object[] { "arg1", 2 });

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void hasSameMethodTrue() {
        Invocation candidate = new Invocation();

        invocation.setMethod(new Method());
        candidate.setMethod(new Method());

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertTrue(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void hasSameMethodFalse() {
        Invocation candidate = new Invocation();

        invocation.setMethod(new Method());
        candidate.setMethod(new Method());

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertFalse(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testGetLocation() {
        Location expected = new Location();

        invocation.setLocation(expected);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(expected, invocationMatcher.getLocation());
    }

    @Test
    public void testToStringPrintSettings() {
        PrintSettings printSettings = new PrintSettings();
        String expected = invocation.toString(invocation.argumentsToMatchers(), printSettings);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(expected, invocationMatcher.toString(printSettings));
    }

    @Test
    public void testCaptureArgumentsFromCapturesArguments() {
        Invocation i = new Invocation();
        Object[] arguments = new Object[] { "arg1", 2 };

        MockMatcher matcher1 = new MockMatcher();
        MockMatcher matcher2 = new MockMatcher();
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(matcher1);
        matchers.add(matcher2);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        invocationMatcher.captureArgumentsFrom(i);

        assertTrue(matcher1.captureFromCalled);
        assertTrue(matcher2.captureFromCalled);
    }

    private class CustomMatcher implements Matcher {
        @Override
        public boolean matches(Object arg0) {
            return false;
        }
    }

    private class Method {

    }

    private class Mock {

    }
}
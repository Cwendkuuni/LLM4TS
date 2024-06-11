package org.mockito.internal.invocation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;

public class InvocationMatcherTest {
    private final Object mock = new Object();
    private final Method method = Object.class.getMethod("equals", Object.class);
    private final Invocation invocation = new Invocation(mock, method, new Object[] { "argument" }, 1, new Location());
    private final InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
    private final InvocationMatcher invocationMatcherWithMatchers = new InvocationMatcher(invocation, Collections.<Matcher>emptyList());

    @Test
    public void testGetMethod() {
        assertEquals(method, invocationMatcher.getMethod());
        assertEquals(method, invocationMatcherWithMatchers.getMethod());
    }

    @Test
    public void testGetInvocation() {
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(invocation, invocationMatcherWithMatchers.getInvocation());
    }

    @Test
    public void testGetMatchers() {
        assertEquals(Collections.emptyList(), invocationMatcher.getMatchers());
        assertEquals(Collections.emptyList(), invocationMatcherWithMatchers.getMatchers());
    }

    @Test
    public void testToString() {
        assertEquals("equals(\"argument\")", invocationMatcher.toString());
        assertEquals("equals(\"argument\")", invocationMatcherWithMatchers.toString());
    }

    @Test
    public void testMatches() throws NoSuchMethodException {
        Object otherMock = new Object();
        Method otherMethod = Object.class.getMethod("hashCode");
        Invocation otherInvocation = new Invocation(otherMock, otherMethod, new Object[0], 2, new Location());

        assertFalse(invocationMatcher.matches(otherInvocation));
        assertFalse(invocationMatcherWithMatchers.matches(otherInvocation));

        assertTrue(invocationMatcher.matches(invocation));
        assertTrue(invocationMatcherWithMatchers.matches(invocation));

        invocationMatcherWithMatchers.getMatchers().add(new TestMatcher());
        assertFalse(invocationMatcherWithMatchers.matches(invocation));
    }

    @Test
    public void testHasSimilarMethod() throws NoSuchMethodException {
        Object otherMock = new Object();
        Method otherMethod = Object.class.getMethod("hashCode");
        Invocation otherInvocation = new Invocation(otherMock, otherMethod, new Object[0], 2, new Location());

        assertFalse(invocationMatcher.hasSimilarMethod(otherInvocation));
        assertFalse(invocationMatcherWithMatchers.hasSimilarMethod(otherInvocation));

        assertFalse(invocationMatcher.hasSimilarMethod(invocation));
        assertFalse(invocationMatcherWithMatchers.hasSimilarMethod(invocation));

        Invocation sameMethodInvocation = new Invocation(mock, method, new Object[0], 1, new Location());

        assertTrue(invocationMatcher.hasSimilarMethod(sameMethodInvocation));
        assertTrue(invocationMatcherWithMatchers.hasSimilarMethod(sameMethodInvocation));

        invocationMatcher.getMatchers().add(new TestMatcher());
        assertFalse(invocationMatcher.hasSimilarMethod(sameMethodInvocation));

        invocationMatcherWithMatchers.getMatchers().add(new TestMatcher());
        assertFalse(invocationMatcherWithMatchers.hasSimilarMethod(sameMethodInvocation));
    }

    @Test
    public void testHasSameMethod() throws NoSuchMethodException {
        Object otherMock = new Object();
        Method otherMethod = Object.class.getMethod("hashCode");
        Invocation otherInvocation = new Invocation(otherMock, otherMethod, new Object[0], 2, new Location());

        assertFalse(invocationMatcher.hasSameMethod(otherInvocation));
        assertFalse(invocationMatcherWithMatchers.hasSameMethod(otherInvocation));

        assertFalse(invocationMatcher.hasSameMethod(invocation));
        assertFalse(invocationMatcherWithMatchers.hasSameMethod(invocation));

        Method sameMethod = Object.class.getMethod("equals", Object.class);
        Invocation sameMethodInvocation = new Invocation(mock, sameMethod, new Object[0], 1, new Location());

        assertTrue(invocationMatcher.hasSameMethod(sameMethodInvocation));
        assertTrue(invocationMatcherWithMatchers.hasSameMethod(sameMethodInvocation));
    }

    @Test
    public void testGetLocation() {
        Location expectedLocation = new Location();
        Invocation invocationWithLocation = new Invocation(mock, method, new Object[0], 1, expectedLocation);
        InvocationMatcher invocationMatcherWithLocation = new InvocationMatcher(invocationWithLocation);

        assertEquals(expectedLocation, invocationMatcher.getLocation());
        assertEquals(expectedLocation, invocationMatcherWithLocation.getLocation());
    }

    @Test
    public void testToStringWithPrintSettings() {
        PrintSettings printSettings = new PrintSettings();

        assertEquals("equals(\"argument\")", invocationMatcher.toString(printSettings));
        assertEquals("equals(\"argument\")", invocationMatcherWithMatchers.toString(printSettings));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Object[] arguments = new Object[] { "argument1", "argument2" };
        Invocation invocationWithArguments = new Invocation(mock, method, arguments, 1, new Location());
        InvocationMatcher invocationMatcherWithArguments = new InvocationMatcher(invocationWithArguments);

        TestCapturesArgumentsMatcher matcher1 = new TestCapturesArgumentsMatcher();
        TestCapturesArgumentsMatcher matcher2 = new TestCapturesArgumentsMatcher();
        invocationMatcherWithArguments.getMatchers().addAll(Arrays.asList(matcher1, matcher2));

        invocationMatcherWithArguments.captureArgumentsFrom(invocation);

        assertEquals("argument1", matcher1.getCapturedArgument());
        assertEquals("argument2", matcher2.getCapturedArgument());
    }

    private static class TestMatcher implements Matcher {
        public boolean matches(Object item) {
            return false;
        }

        public void describeTo(org.hamcrest.Description description) {
            description.appendText("test matcher");
        }
    }

    private static class TestCapturesArgumentsMatcher implements CapturesArguments {
        private Object capturedArgument;

        public boolean matches(Object item) {
            return false;
        }

        public void describeTo(org.hamcrest.Description description) {
            description.appendText("test matcher");
        }

        public void captureFrom(Object argument) {
            this.capturedArgument = argument;
        }

        public Object getCapturedArgument() {
            return capturedArgument;
        }
    }
}
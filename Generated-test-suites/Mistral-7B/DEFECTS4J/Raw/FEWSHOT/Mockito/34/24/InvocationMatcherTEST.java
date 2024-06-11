package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.ArgumentsMatchers.argThat;
import static org.mockito.internal.matchers.ArgumentsMatchers.eq;
import static org.mockito.internal.matchers.ArgumentsMatchers.isNull;
import static org.mockito.internal.matchers.ArgumentsMatchers.same;
import static org.mockito.internal.matchers.ArgumentsMatchers.through;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.VerificationException;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.internal.verification.InvocationWithArguments;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.stub.Stub;

public class InvocationMatcherTest {

    private Stub mock;
    private Invocation invocation;
    private InvocationMatcher invocationMatcher;
    private List<Matcher> matchers;

    @Before
    public void setUp() throws Exception {
        mock = Stub.create(new Class[]{}, new Object[0]);
        matchers = Collections.singletonList(argThat(isNull()));
        invocation = new InvocationOnMock(mock, new Method[]{new Method("testMethod", new Class[0])}, new Object[0], new Location(1, 1), null);
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void testGetMethod() {
        Method method = invocationMatcher.getMethod();
        assertSame(invocation.getMethod(), method);
    }

    @Test
    public void testGetInvocation() {
        Invocation expectedInvocation = invocation;
        Invocation actualInvocation = invocationMatcher.getInvocation();
        assertSame(expectedInvocation, actualInvocation);
    }

    @Test
    public void testGetMatchers() {
        List<Matcher> expectedMatchers = invocation.argumentsToMatchers();
        List<Matcher> actualMatchers = invocationMatcher.getMatchers();
        assertSame(expectedMatchers, actualMatchers);
    }

    @Test
    public void testToString() throws Exception {
        String expected = String.format("Invocation %s (%s)", invocation, matchers);
        String actual = invocationMatcher.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testMatches() throws Exception {
        Invocation otherInvocation = new InvocationOnMock(mock, new Method[]{new Method("testMethod", new Class[0])}, new Object[0], new Location(1, 2), null);
        InvocationMatcher otherInvocationMatcher = new InvocationMatcher(otherInvocation);
        assertTrue(invocationMatcher.matches(invocation));
        assertFalse(invocationMatcher.matches(otherInvocationMatcher));
    }

    @Test(expected = VerificationException.class)
    public void testSafelyArgumentsMatchThrowsExceptionWhenArgumentsDontMatch() throws Exception {
        Invocation otherInvocation = new InvocationOnMock(mock, new Method[]{new Method("testMethod", new Class[]{Integer.TYPE})}, new Object[]{1}, new Location(1, 2), null);
        InvocationMatcher otherInvocationMatcher = new InvocationMatcher(otherInvocation);
        invocationMatcher.safelyArgumentsMatch(otherInvocation.getArguments());
    }

    @Test
    public void testHasSimilarMethod() throws Exception {
        Invocation otherInvocation = new InvocationOnMock(mock, new Method[]{new Method("testMethod", new Class[0])}, new Object[0], new Location(1, 1), null);
        InvocationMatcher otherInvocationMatcher = new InvocationMatcher(otherInvocation);
        assertTrue(invocationMatcher.hasSimilarMethod(otherInvocationMatcher));

        Invocation otherInvocationDifferentMethod = new InvocationOnMock(mock, new Method[]{new Method("testOtherMethod", new Class[0])}, new Object[0], new Location(1, 1), null);
        InvocationMatcher otherInvocationDifferentMethodMatcher = new InvocationMatcher(otherInvocationDifferentMethod);
        assertFalse(invocationMatcher.hasSimilarMethod(otherInvocationDifferentMethodMatcher));
    }

    @Test
    public void testHasSameMethod() throws Exception {
        Invocation otherInvocation = new InvocationOnMock(mock, new Method[]{new Method("testMethod", new Class[0])}, new Object[0], new Location(1, 1), null);
        InvocationMatcher otherInvocationMatcher = new InvocationMatcher(otherInvocation);
        assertTrue(invocationMatcher.hasSameMethod(otherInvocationMatcher));
    }

    @Test
    public void testGetLocation() {
        Location expectedLocation = invocation.getLocation();
        Location actualLocation = invocationMatcher.getLocation();
        assertSame(expectedLocation, actualLocation);
    }

    @Test
    public void testToStringWithPrintSettings() {
        PrintSettings printSettings = new PrintSettings();
        String expected = invocationMatcher.toString(printSettings);
        String actual = invocationMatcher.toString(printSettings);
        assertEquals(expected, actual);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        CapturesArguments capturesArguments = new CapturesArguments() {
            private Object captured;

            @Override
            public void captureFrom(Object argument) {
                this.captured = argument;
            }

            @Override
            public Object getCapturedValue() {
                return this.captured;
            }
        };

        invocationMatcher.captureArgumentsFrom(invocation);
        assertEquals(invocation.getArguments()[0], capturesArguments.getCapturedValue());
    }
}
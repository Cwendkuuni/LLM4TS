
package org.mockito.internal.invocation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;

public class InvocationMatcherTest {

    private Invocation invocation;
    private InvocationMatcher invocationMatcher;

    @Before
    public void setUp() {
        invocation = new Invocation();
        invocation.setMethod(new Method());
        invocation.setMock(new Mock());
        invocation.setArguments(new Object[]{1, 2, "test"});

        invocationMatcher = new InvocationMatcher(invocation);
    }

    @Test
    public void testConstructorWithInvocation() {
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(Collections.emptyList(), invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithInvocationAndMatchers() {
        List<Matcher> matchers = Arrays.asList(new Matcher(), new Matcher());
        invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testGetMethod() {
        assertEquals(new Method(), invocationMatcher.getMethod());
    }

    @Test
    public void testToString() {
        invocationMatcher.toString(new PrintSettings()); // test for verifying no exception thrown
    }

    @Test
    public void testMatches() {
        Invocation actualInvocation = new Invocation();
        actualInvocation.setMock(new Mock());
        actualInvocation.setArguments(new Object[]{1, 2, "test"});

        assertTrue(invocationMatcher.matches(actualInvocation));

        actualInvocation.setArguments(new Object[]{1, 2, "different"});

        assertFalse(invocationMatcher.matches(actualInvocation));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation candidate = new Invocation();
        candidate.setMethod(new Method());
        candidate.setMock(new Mock());
        candidate.setArguments(new Object[]{1, 2, "test"});

        assertFalse(invocationMatcher.hasSimilarMethod(candidate));

        candidate.setArguments(new Object[]{1, 2, "different"});

        assertTrue(invocationMatcher.hasSimilarMethod(candidate));

        candidate.setArguments(new Object[]{1, 2, "test"});
        candidate.verify();

        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSameMethod() {
        Invocation candidate = new Invocation();
        candidate.setMethod(new Method());
        candidate.setMock(new Mock());
        candidate.setArguments(new Object[]{1, 2, "test"});

        assertTrue(invocationMatcher.hasSameMethod(candidate));

        candidate.setMethod(new Method());
        candidate.setMock(new Mock());
        candidate.setArguments(new Object[]{1, 2, "different"});

        assertFalse(invocationMatcher.hasSameMethod(candidate));

        candidate.setMethod(new Method());
        candidate.setMock(invocation.getMock());
        candidate.setArguments(new Object[]{1, 2, "test"});

        assertFalse(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation1 = new Invocation();
        invocation1.setMethod(new Method());
        invocation1.setMock(new Mock());
        invocation1.setArguments(new Object[]{1, 2, "test"});

        invocationMatcher.captureArgumentsFrom(invocation1);

        assertEquals(1, ((CapturesArguments) invocationMatcher.getMatchers().get(0)).captureFrom());
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = Arrays.asList(new Invocation(), new Invocation());
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        assertEquals(invocations.size(), invocationMatchers.size());
        assertEquals(invocations.get(0), invocationMatchers.get(0).getInvocation());
        assertEquals(invocations.get(1), invocationMatchers.get(1).getInvocation());
    }
}

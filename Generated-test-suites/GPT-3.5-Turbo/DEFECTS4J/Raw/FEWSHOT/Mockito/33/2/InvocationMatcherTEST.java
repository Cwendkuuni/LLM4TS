package org.mockito.internal.invocation;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class InvocationMatcherTest {
    @Test
    public void testGetMethod() {
        Method method = createMethod();
        Invocation invocation = createInvocation(method);
        List<Matcher> matchers = createMatchers();

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testGetInvocation() {
        Method method = createMethod();
        Invocation invocation = createInvocation(method);
        List<Matcher> matchers = createMatchers();

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals(invocation, invocationMatcher.getInvocation());
    }

    @Test
    public void testGetMatchers() {
        Method method = createMethod();
        Invocation invocation = createInvocation(method);
        List<Matcher> matchers = createMatchers();

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testToString() {
        Method method = createMethod();
        Invocation invocation = createInvocation(method);
        List<Matcher> matchers = createMatchers();
        PrintSettings printSettings = createPrintSettings();

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        String expectedString = invocation.toString(matchers, printSettings);
        assertEquals(expectedString, invocationMatcher.toString());
    }

    @Test
    public void testMatches() {
        Method method = createMethod();
        Invocation invocation = createInvocation(method);
        List<Matcher> matchers = createMatchers();

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertTrue(invocationMatcher.matches(invocation));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Method method = createMethod();
        Invocation invocation = createInvocation(method);
        List<Matcher> matchers = createMatchers();
        Object[] actualArgs = {1, 2, 3};

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertTrue(invocationMatcher.safelyArgumentsMatch(actualArgs));
    }

    @Test
    public void testHasSimilarMethod() {
        Method method = createMethod();
        Invocation invocation = createInvocation(method);
        List<Matcher> matchers = createMatchers();
        Invocation candidate = createInvocation(method);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSameMethod() {
        Method method = createMethod();
        Invocation invocation = createInvocation(method);
        List<Matcher> matchers = createMatchers();
        Invocation candidate = createInvocation(method);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertTrue(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testGetLocation() {
        Method method = createMethod();
        Invocation invocation = createInvocation(method);
        List<Matcher> matchers = createMatchers();
        Location location = createLocation();

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals(location, invocationMatcher.getLocation());
    }

    @Test
    public void testToStringWithPrintSettings() {
        Method method = createMethod();
        Invocation invocation = createInvocation(method);
        List<Matcher> matchers = createMatchers();
        PrintSettings printSettings = createPrintSettings();

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        String expectedString = invocation.toString(matchers, printSettings);
        assertEquals(expectedString, invocationMatcher.toString(printSettings));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Method method = createMethod();
        Invocation invocation = createInvocation(method);
        List<Matcher> matchers = createMatchers();
        Invocation i = createInvocation(method);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        invocationMatcher.captureArgumentsFrom(i);

        // Verify that CapturesArguments captureFrom() method was called for each matcher
        for (Matcher m : matchers) {
            if (m instanceof CapturesArguments) {
                assertTrue(((CapturesArguments) m).wasCaptureCalled());
            }
        }
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = createInvocations();

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        assertEquals(invocations.size(), invocationMatchers.size());
        for (int i = 0; i < invocations.size(); i++) {
            assertEquals(invocations.get(i), invocationMatchers.get(i).getInvocation());
        }
    }

    // Helper methods to create test objects

    private Method createMethod() {
        try {
            return DummyClass.class.getMethod("dummyMethod");
        } catch (NoSuchMethodException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        return null;
    }

    private Invocation createInvocation(Method method) {
        return new Invocation(new DummyClass(), method);
    }

    private List<Matcher> createMatchers() {
        return Collections.emptyList();
    }

    private PrintSettings createPrintSettings() {
        return new PrintSettings();
    }

    private Location createLocation() {
        return new Location();
    }

    private List<Invocation> createInvocations() {
        List<Invocation> invocations = new LinkedList<>();
        invocations.add(createInvocation(createMethod()));
        invocations.add(createInvocation(createMethod()));
        invocations.add(createInvocation(createMethod()));
        return invocations;
    }

    // Helper class for testing

    private static class DummyClass {
        public void dummyMethod() {
        }
    }
}
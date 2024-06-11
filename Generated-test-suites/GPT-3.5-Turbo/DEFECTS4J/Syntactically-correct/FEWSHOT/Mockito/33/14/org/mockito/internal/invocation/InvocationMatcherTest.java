package org.mockito.internal.invocation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.reporting.PrintSettings;

public class InvocationMatcherTest {

    private Invocation invocation;
    private InvocationMatcher invocationMatcher;

    @Before
    public void setup() {
        invocation = mock(Invocation.class);
        invocationMatcher = new InvocationMatcher(invocation);
    }

    @Test
    public void testGetMethod() {
        assertEquals(invocation.getMethod(), invocationMatcher.getMethod());
    }

    @Test
    public void testGetInvocation() {
        assertEquals(invocation, invocationMatcher.getInvocation());
    }

    @Test
    public void testGetMatchers() {
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testToString() {
        String expected = invocation.toString(invocationMatcher.getMatchers(), new PrintSettings());
        assertEquals(expected, invocationMatcher.toString());
    }

    @Test
    public void testMatches() {
        Invocation actualInvocation = mock(Invocation.class);
        assertFalse(invocationMatcher.matches(actualInvocation));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation candidateInvocation = mock(Invocation.class);
        assertFalse(invocationMatcher.hasSimilarMethod(candidateInvocation));
    }

    @Test
    public void testHasSameMethod() {
        Invocation candidateInvocation = mock(Invocation.class);
        assertFalse(invocationMatcher.hasSameMethod(candidateInvocation));
    }

    @Test
    public void testGetLocation() {
        Location location = mock(Location.class);
        assertEquals(location, invocationMatcher.getLocation());
    }

    @Test
    public void testToStringWithPrintSettings() {
        PrintSettings printSettings = mock(PrintSettings.class);
        String expected = invocation.toString(invocationMatcher.getMatchers(), printSettings);
        assertEquals(expected, invocationMatcher.toString(printSettings));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation i = mock(Invocation.class);
        invocationMatcher.captureArgumentsFrom(i);
    }

    @Test
    public void testCreateFrom() {
        Invocation i = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(i);
        assertTrue(InvocationMatcher.createFrom(Collections.singletonList(i)).contains(invocationMatcher));
    }
}
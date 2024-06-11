package org.mockito.internal.invocation;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InvocationMatcherTest {

    @Test
    public void testGetMethod() {
        Invocation invocation = new Invocation(null, null, null);
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Method method = invocation.getMethod();
        assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = new Invocation(null, null, null);
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertEquals(invocation, invocationMatcher.getInvocation());
    }

    @Test
    public void testGetMatchers() {
        Invocation invocation = new Invocation(null, null, null);
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(null);
        matchers.add(null);
        matchers.add(null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testToString() {
        Invocation invocation = new Invocation(null, null, null);
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        String expected = invocation.toString(matchers, new PrintSettings());
        assertEquals(expected, invocationMatcher.toString());
    }

    @Test
    public void testMatches() {
        Invocation invocation1 = new Invocation(null, null, null);
        Invocation invocation2 = new Invocation(null, null, null);
        Object[] arguments = {};
        ArgumentsComparator argumentsComparator = new ArgumentsComparator();
        Mockito.when(argumentsComparator.argumentsMatch(invocation1, invocation2)).thenReturn(true);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1, null);
        assertTrue(invocationMatcher.matches(invocation2));
    }

    @Test
    public void testMatchesFalse() {
        Invocation invocation1 = new Invocation(null, null, null);
        Invocation invocation2 = new Invocation(null, null, null);
        Object[] arguments = {};
        ArgumentsComparator argumentsComparator = new ArgumentsComparator();
        Mockito.when(argumentsComparator.argumentsMatch(invocation1, invocation2)).thenReturn(false);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1, null);
        assertFalse(invocationMatcher.matches(invocation2));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation1 = new Invocation(null, null, null);
        Invocation invocation2 = new Invocation(null, null, null);
        Object[] arguments = {};
        ArgumentsComparator argumentsComparator = new ArgumentsComparator();
        Mockito.when(argumentsComparator.argumentsMatch(invocation1, invocation2)).thenReturn(true);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1, null);
        assertTrue(invocationMatcher.hasSimilarMethod(invocation2));
    }

    @Test
    public void testHasSimilarMethodFalse() {
        Invocation invocation1 = new Invocation(null, null, null);
        Invocation invocation2 = new Invocation(null, null, null);
        Object[] arguments = {};
        ArgumentsComparator argumentsComparator = new ArgumentsComparator();
        Mockito.when(argumentsComparator.argumentsMatch(invocation1, invocation2)).thenReturn(false);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1, null);
        assertFalse(invocationMatcher.hasSimilarMethod(invocation2));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = new Invocation(null, null, null);
        Invocation invocation2 = new Invocation(null, null, null);
        invocation1.setMethod(null);
        invocation2.setMethod(null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1, null);
        assertTrue(invocationMatcher.hasSameMethod(invocation2));
    }

    @Test
    public void testHasSameMethodFalse() {
        Invocation invocation1 = new Invocation(null, null, null);
        Invocation invocation2 = new Invocation(null, null, null);
        invocation1.setMethod(null);
        Method method = new Method();
        invocation2.setMethod(method);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1, null);
        assertFalse(invocationMatcher.hasSameMethod(invocation2));
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = new Invocation(null, null, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, null);
        Location location = invocation.getLocation();
        assertEquals(location, invocationMatcher.getLocation());
    }

    @Test
    public void testToStringPrintSettings() {
        Invocation invocation = new Invocation(null, null, null);
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        PrintSettings printSettings = new PrintSettings();
        String expected = invocation.toString(matchers, printSettings);
        assertEquals(expected, invocationMatcher.toString(printSettings));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = new Invocation(null, null, null);
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Invocation i = new Invocation(null, null, null);
        int k = 0;
        Matcher m = null;
        CapturesArguments capturesArguments = null;
        InvocationMatcher captureArgumentsFrom = null;
        while (((Iterator) capturesArguments).hasNext()) {
            ((CapturesArguments) m).captureFrom(i.getArguments()[k]);
            k++;
        }
    }

}
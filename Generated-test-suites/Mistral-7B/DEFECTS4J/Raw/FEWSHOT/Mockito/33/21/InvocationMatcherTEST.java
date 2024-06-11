package org.mockito.internal.invocation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.internal.matchers.ArgumentsMatcher.equalTo;
import static org.mockito.internal.reporting.PrintSettings.printSettings;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;

@SuppressWarnings("unchecked")
public class InvocationMatcherTest {

    @Test
    public void testConstructorWithInvocationAndEmptyListOfMatchers() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher im = new InvocationMatcher(invocation);

        assertEquals(invocation.getMethod(), im.getMethod());
        assertEquals(Collections.emptyList(), im.getMatchers());
    }

    @Test
    public void testConstructorWithInvocationAndListOfMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(equalTo("expectedValue"));

        InvocationMatcher im = new InvocationMatcher(invocation, matchers);

        assertEquals(invocation.getMethod(), im.getMethod());
        assertEquals(matchers, im.getMatchers());
    }

    @Test
    public void testGetMethod() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);

        InvocationMatcher im = new InvocationMatcher(invocation);

        assertEquals(method, im.getMethod());
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher im = new InvocationMatcher(invocation);

        assertEquals(invocation, im.getInvocation());
    }

    @Test
    public void testGetMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new LinkedList<>();
        InvocationMatcher im = new InvocationMatcher(invocation, matchers);

        assertEquals(matchers, im.getMatchers());
    }

    @Test
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        List<Matcher> matchers = new LinkedList<>();
        InvocationMatcher im = new InvocationMatcher(invocation, matchers);

        String expected = "InvocationMatcher[matchers=[], method=TestClass.methodName(java.lang.String, int)]";
        String actual = im.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testMatches() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method);
        when(invocation2.getMethod()).thenReturn(method);
        List<Matcher> matchers1 = new LinkedList<>();
        InvocationMatcher im1 = new InvocationMatcher(invocation1, matchers1);
        List<Matcher> matchers2 = new LinkedList<>();
        InvocationMatcher im2 = new InvocationMatcher(invocation2, matchers2);

        assertTrue(im1.matches(im1));
        assertFalse(im1.matches(im2));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Invocation invocation = mock(Invocation.class);
        Object[] arguments = new Object[]{"arg1", 1};
        when(invocation.getArguments()).thenReturn(arguments);
        InvocationMatcher im = new InvocationMatcher(invocation);

        assertTrue(im.safelyArgumentsMatch(arguments));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);

        Method method1 = mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method1);
        when(invocation2.getMethod()).thenReturn(method1);

        assertTrue(new InvocationMatcher(invocation1).hasSimilarMethod(new InvocationMatcher(invocation2)));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method);
        when(invocation2.getMethod()).thenReturn(method);

        assertTrue(new InvocationMatcher(invocation1).hasSameMethod(new InvocationMatcher(invocation2)));
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = mock(Invocation.class);
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);

        InvocationMatcher im = new InvocationMatcher(invocation);

        assertEquals(location, im.getLocation());
    }

    @Test
    public void testToStringWithPrintSettings() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        List<Matcher> matchers = new LinkedList<>();
        InvocationMatcher im = new InvocationMatcher(invocation, matchers);

        String expected = "InvocationMatcher[matchers=[Matcher type: org.hamcrest.Matcher$1, actual: foo], matchers=[Matcher type: org.hamcrest.Matcher$1, actual: bar], method=TestClass.methodName(java.lang.String, int)]";
        String actual = im.toString(printSettings());

        assertEquals(expected, actual);
    }

    @Test(expected = AssertionError.class)
    public void testCaptureArgumentsFromNullInvocation() {
        InvocationMatcher im = new InvocationMatcher(null);
        im.captureArgumentsFrom(mock(Invocation.class));
    }

    @Test
    public void testCreateFrom() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);

        InvocationMatcher im1 = new InvocationMatcher(invocation1);
        InvocationMatcher im2 = new InvocationMatcher(invocation2);

        List<InvocationMatcher> result = InvocationMatcher.createFrom(Collections.singletonList(invocation1));

        assertEquals(1, result.size());
        assertEquals(im1, result.get(0));
    }
}
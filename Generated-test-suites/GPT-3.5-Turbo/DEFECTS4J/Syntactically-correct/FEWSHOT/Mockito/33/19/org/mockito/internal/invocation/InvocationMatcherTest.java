package org.mockito.internal.invocation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationFactory;

public class InvocationMatcherTest {
    private Invocation invocationWithMatchers;
    private Invocation invocationWithoutMatchers;
    private InvocationMatcher invocationMatcherWithMatchers;
    private InvocationMatcher invocationMatcherWithoutMatchers;
    private List<Matcher> matchers;

    @Before
    public void setUp() {
        Method method = SomeClass.class.getDeclaredMethods()[0];
        Object[] arguments = {1, "test"};

        invocationWithMatchers = InvocationFactory.createInvocation(SomeClass.class, method, null, arguments);
        invocationWithoutMatchers = InvocationFactory.createInvocation(SomeClass.class, method, null, arguments);

        matchers = Arrays.asList(new Matcher[] {Mockito.anyInt(), Mockito.anyString()});
        invocationMatcherWithMatchers = new InvocationMatcher(invocationWithMatchers, matchers);
    }

    @Test
    public void testGetMatchers() {
        List<Matcher> expectedMatchers = Arrays.asList(new Matcher[] {Mockito.anyInt(), Mockito.anyString()});
        List<Matcher> actualMatchers = invocationMatcherWithMatchers.getMatchers();

        assertEquals(expectedMatchers, actualMatchers);
    }

    @Test
    public void testToString() {
        String expectedString = "invocationWithMatchers.toString()";
        String actualString = invocationMatcherWithMatchers.toString();

        assertEquals(expectedString, actualString);
    }

    @Test
    public void testMatches() {
        Invocation actual = InvocationFactory.createInvocation(SomeClass.class, invocationWithMatchers.getMethod(), null, invocationWithMatchers.getArguments());

        boolean actualResult = invocationMatcherWithMatchers.matches(actual);

        assertTrue(actualResult);
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation actual = InvocationFactory.createInvocation(SomeClass.class, invocationWithMatchers.getMethod(), null, invocationWithMatchers.getArguments());

        boolean actualResult = invocationMatcherWithMatchers.hasSimilarMethod(actual);

        assertTrue(actualResult);
    }

    @Test
    public void testHasSimilarMethod_differentMethodNames() {
        Method method = SomeClass.class.getDeclaredMethods()[1];
        Object[] arguments = {1, "test"};
        Invocation actual = InvocationFactory.createInvocation(SomeClass.class, method, null, arguments);

        boolean actualResult = invocationMatcherWithMatchers.hasSimilarMethod(actual);

        assertFalse(actualResult);
    }

    @Test
    public void testHasSimilarMethod_verified() {
        invocationWithMatchers.markVerified();
        Invocation actual = InvocationFactory.createInvocation(SomeClass.class, invocationWithMatchers.getMethod(), null, invocationWithMatchers.getArguments());

        boolean actualResult = invocationMatcherWithMatchers.hasSimilarMethod(actual);

        assertFalse(actualResult);
    }

    @Test
    public void testHasSimilarMethod_differentMocks() {
        invocationWithMatchers.getMock().addMockListener(null);
        Invocation actual = InvocationFactory.createInvocation(SomeClass.class, invocationWithMatchers.getMethod(), null, invocationWithMatchers.getArguments());

        boolean actualResult = invocationMatcherWithMatchers.hasSimilarMethod(actual);

        assertFalse(actualResult);
    }

    @Test
    public void testHasSimilarMethod_overloadedMethod() {
        Method method = SomeClass.class.getDeclaredMethods()[1];
        Object[] arguments = {1, "test"};
        Invocation actual = InvocationFactory.createInvocation(SomeClass.class, method, null, arguments);

        boolean actualResult = invocationMatcherWithMatchers.hasSimilarMethod(actual);

        assertFalse(actualResult);
    }

    @Test
    public void testHasSameMethod() {
        Invocation actual = InvocationFactory.createInvocation(SomeClass.class, invocationWithMatchers.getMethod(), null, invocationWithMatchers.getArguments());

        boolean actualResult = invocationMatcherWithMatchers.hasSameMethod(actual);

        assertTrue(actualResult);
    }

    @Test
    public void testHasSameMethod_differentMethods() {
        Method method = SomeClass.class.getDeclaredMethods()[1];
        Object[] arguments = {1, "test"};
        Invocation actual = InvocationFactory.createInvocation(SomeClass.class, method, null, arguments);

        boolean actualResult = invocationMatcherWithMatchers.hasSameMethod(actual);

        assertFalse(actualResult);
    }

    @Test
    public void testGetLocation() {
        Location expectedLocation = invocationWithMatchers.getLocation();
        Location actualLocation = invocationMatcherWithMatchers.getLocation();

        assertEquals(expectedLocation, actualLocation);
    }

    @Test
    public void testToString_PrintSettings() {
        String expectedString = invocationWithMatchers.toString(matchers, new PrintSettings());
        String actualString = invocationMatcherWithMatchers.toString(new PrintSettings());

        assertEquals(expectedString, actualString);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = InvocationFactory.createInvocation(SomeClass.class, invocationWithMatchers.getMethod(), null, invocationWithMatchers.getArguments());
        invocationMatcherWithMatchers.captureArgumentsFrom(invocation);

        // Verify that the arguments from the invocation are captured correctly by the matchers
        assertEquals(1, ((CapturesArguments)matchers.get(0)).captureFromInvocationArgument());
        assertEquals("test", ((CapturesArguments)matchers.get(1)).captureFromInvocationArgument());
    }

    private static class SomeClass {
        public void someMethod(int arg1, String arg2) {}
        public void anotherMethod(int arg1, String arg2) {}
    }
}
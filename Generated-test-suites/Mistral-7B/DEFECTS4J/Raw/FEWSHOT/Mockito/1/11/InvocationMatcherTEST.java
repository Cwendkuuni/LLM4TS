package org.mockito.internal.invocation;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.MockitoMethodInvocation;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InvocationMatcherTest {

    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = new MockitoMethodInvocation(objectMocked, getClass().getMethod("testHasSameMethod", int.class), new Object[]{1});
        Invocation invocation2 = new MockitoMethodInvocation(objectMocked, getClass().getMethod("testHasSameMethod", int.class), new Object[]{1});

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        assertTrue(invocationMatcher1.hasSameMethod(invocationMatcher2));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation1 = new MockitoMethodInvocation(objectMocked, getClass().getMethod("testHasSimilarMethod", int[].class), new Object[]{new int[]{1, 2}});
        Invocation invocation2 = new MockitoMethodInvocation(objectMocked, getClass().getMethod("testHasSimilarMethod", int[].class), new Object[]{new int[]{1, 2, 3}});

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        assertFalse(invocationMatcher1.hasSameMethod(invocationMatcher2));
        assertTrue(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testMatches() {
        Invocation invocation = new MockitoMethodInvocation(objectMocked, getClass().getMethod("testMatches", int.class), new Object[]{1});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation actualInvocation = new MockitoMethodInvocation(objectMocked, getClass().getMethod("testMatches", int.class), new Object[]{1});

        InvocationMatcher actualMatcher = new InvocationMatcher(actualInvocation);

        assertTrue(invocationMatcher.matches(actualMatcher));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Invocation invocation = new MockitoMethodInvocation(objectMocked, getClass().getMethod("testSafelyArgumentsMatch", String[].class), new Object[]{new String[]{"foo", "bar"}});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation actualInvocation = new MockitoMethodInvocation(objectMocked, getClass().getMethod("testSafelyArgumentsMatch", String[].class), new Object[]{new String[]{"foo", "bar", "baz"}});
        InvocationMatcher actualMatcher = new InvocationMatcher(actualInvocation);

        assertFalse(invocationMatcher.safelyArgumentsMatch(actualInvocation.getArguments()));

        Invocation invocationWithNull = new MockitoMethodInvocation(objectMocked, getClass().getMethod("testSafelyArgumentsMatch", String[].class), new Object[]{null});
        InvocationMatcher invocationMatcherWithNull = new InvocationMatcher(invocationWithNull);

        assertFalse(invocationMatcherWithNull.safelyArgumentsMatch(actualInvocation.getArguments()));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCaptureArgumentsFromVarargs() {
        Invocation invocation = new MockitoMethodInvocation(objectMocked, getClass().getMethod("testCaptureArgumentsFromVarargs", int.class, int[].class), new Object[]{1, new int[]{1, 2}});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        invocationMatcher.captureArgumentsFrom(new MockitoMethodInvocation(objectMocked, getClass().getMethod("testCaptureArgumentsFromVarargs", int.class, int[].class), new Object[]{1, new int[]{1, 2, 3}}));
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = Arrays.asList(
                new MockitoMethodInvocation(objectMocked, getClass().getMethod("testCreateFrom", String.class), new Object[]{"foo"}),
                new MockitoMethodInvocation(objectMocked, getClass().getMethod("testCreateFrom", String.class), new Object[]{"bar"}),
                new MockitoMethodInvocation(objectMocked, getClass().getMethod("testCreateFrom", String.class), new Object[]{"baz"})
        );

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        assertEquals(3, invocationMatchers.size());

        for (InvocationMatcher invocationMatcher : invocationMatchers) {
            Invocation invocation = invocationMatcher.getInvocation();

            assertEquals(getClass().getMethod("testCreateFrom", String.class), invocation.getMethod());
        }
    }

    @Test
    public void testToString() {
        Invocation invocation = new MockitoMethodInvocation(objectMocked, getClass().getMethod("testToString", int.class), new Object[]{1});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        String expected = "InvocationMatcher: testToString(int)";
        String actual = invocationMatcher.toString();

        assertEquals(expected, actual);
    }

    private Object objectMocked = mock(Object.class);

    private Method getClass() {
        return getClass();
    }
}
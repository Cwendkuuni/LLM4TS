package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.ArgumentsMatcher.argumentsWithValues;
import static org.mockito.internal.matchers.ArgumentsMatcher.argumentsWithTypes;
import static org.mockito.internal.matchers.TypeMatcher.hasType;
import static org.mockito.internal.reporting.PrintSettings.print;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.matchers.VarargMatcher;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

public class InvocationMatcherTest {

    @Test
    public void testHasSameMethod() {
        Method m1 = new Method(Object.class.getName(), "testMethod", new Class[]{int.class, String[].class});
        Method m2 = new Method(Object.class.getName(), "testMethod", new Class[]{String[].class, int.class});

        InvocationMatcher m1Matcher = new InvocationMatcher(new Invocation(m1, new Object[]{1, new Object[]{1, 2}}));
        InvocationMatcher m2Matcher = new InvocationMatcher(new Invocation(m2, new Object[]{new Object[]{1, 2}, 1}));

        assertTrue(m1Matcher.hasSameMethod(m2Matcher));

        m1Matcher = new InvocationMatcher(new Invocation(m1, new Object[]{1, new Object[]{}}));
        m2Matcher = new InvocationMatcher(new Invocation(m2, new Object[]{null, 1}));

        assertFalse(m1Matcher.hasSameMethod(m2Matcher));

        m1Matcher = new InvocationMatcher(new Invocation(m1, new Object[]{1, "foo"}));
        m2Matcher = new InvocationMatcher(new Invocation(m2, new Object[]{1, "bar"}));

        assertFalse(m1Matcher.hasSameMethod(m2Matcher));

        m1Matcher = new InvocationMatcher(new Invocation(m1, new Object[]{1, 1}));
        m2Matcher = new InvocationMatcher(new Invocation(m2, new Object[]{1, 1}));

        assertTrue(m1Matcher.hasSameMethod(m2Matcher));

        m1Matcher = new InvocationMatcher(new Invocation(m1, new Object[]{null, 1}));
        m2Matcher = new InvocationMatcher(new Invocation(m2, new Object[]{1, null}));

        assertFalse(m1Matcher.hasSameMethod(m2Matcher));
    }

    @Test
    public void testMatches() {
        Invocation mockInvocation = new Invocation(new Method("testMethod", new Class[]{Integer.TYPE, String[].class}), new Object[]{1, new Object[]{"foo", "bar"}});
        Invocation actualInvocation = new Invocation(new Method("testMethod", new Class[]{String[].class, Integer.TYPE}), new Object[]{new Object[]{"foo", "bar"}, 1});

        InvocationMatcher matcher = new InvocationMatcher(mockInvocation);

        assertTrue(matcher.matches(actualInvocation));
    }

    @Test
    public void testMatchesWithVarargs() {
        Method testMethod = new Method("testMethod", new Class[]{Integer.TYPE, String[].class});

        InvocationMatcher matcher = new InvocationMatcher(new Invocation(testMethod, new Object[]{1, new Object[]{"foo", "bar"}}));

        Invocation actualInvocation1 = new Invocation(testMethod, new Object[]{1, new Object[]{"foo", "bar", "baz"}});
        Invocation actualInvocation2 = new Invocation(testMethod, new Object[]{1, new Object[]{"foo", "bar"}});

        assertFalse(matcher.matches(actualInvocation1));
        assertTrue(matcher.matches(actualInvocation2));
    }

    @Test
    public void testToString() {
        Method testMethod = new Method("testMethod", new Class[]{Integer.TYPE, String[].class});

        InvocationMatcher matcher = new InvocationMatcher(new Invocation(testMethod, new Object[]{1, new Object[]{"foo", "bar"}}));

        String expected = print(Arrays.asList(
                argumentsWithTypes(0, Integer.TYPE),
                argumentsWithTypes(1, ArgumentsMatcher.arrayWithTypeAndValues(String[].class, Arrays.asList("foo", "bar")))
        ));

        assertEquals(expected, matcher.toString());
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Method testMethod = new Method("testMethod", new Class[]{Integer.TYPE, String[].class});

        InvocationMatcher matcher = new InvocationMatcher(new Invocation(testMethod, new Object[]{1, new Object[]{"foo", "bar"}}));
        Invocation invocation = new Invocation(testMethod, new Object[]{1, new Object[]{"foo", "bar"}});

        CapturesArguments captor = new CapturesArguments();
        matcher.captureArgumentsFrom(invocation);

        assertEquals("foo", captor.getValue(0));
        assertEquals("bar", captor.getValue(1));
    }

    @Test
    public void testCaptureArgumentsFromWithVarargs() {
        Method testMethod = new Method("testMethod", new Class[]{Integer.TYPE, String[].class});

        InvocationMatcher matcher = new InvocationMatcher(new Invocation(testMethod, new Object[]{1, new Object[]{"foo", "bar", "baz"}}));
        Invocation invocation = new Invocation(testMethod, new Object[]{1, new Object[]{"foo", "bar", "baz"}});

        CapturesArguments captor = new CapturesArguments();
        matcher.captureArgumentsFrom(invocation);

        assertEquals("foo", captor.getValue(0));
        assertEquals("bar", captor.getValue(1));
        assertEquals("baz", captor.getValue(2));
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = Arrays.asList(
                new Invocation(new Method("testMethod", new Class[]{Integer.TYPE, String[].class}), new Object[]{1, new Object[]{"foo", "bar"}}),
                new Invocation(new Method("testMethod", new Class[]{Integer.TYPE, String[].class}), new Object[]{1, new Object[]{"baz", "qux"}})
        );

        List<InvocationMatcher> matchers = InvocationMatcher.createFrom(invocations);

        InvocationMatcher matcher1 = matchers.get(0);
        InvocationMatcher matcher2 = matchers.get(1);

        assertFalse(matcher1.matches(matcher2));
        assertFalse(matcher2.matches(matcher1));
    }
}
package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.ArgumentMatchers.any;
import static org.mockito.internal.matchers.ArgumentMatchers.eq;
import static org.mockito.internal.matchers.ArgumentMatchers.isA;
import static org.mockito.internal.matchers.ArgumentMatchers.isInstanceOf;
import static org.mockito.internal.matchers.ArgumentMatchers.isNull;
import static org.mockito.internal.matchers.ArgumentMatchers.notNull;
import static org.mockito.internal.matchers.ArgumentMatchers.same;
import static org.mockito.internal.matchers.ArgumentMatchers.type;
import static org.mockito.internal.reporting.PrintSettings.print;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.Invocation;

public class InvocationMatcherTest {

    private static final Object[] ARGUMENTS = new Object[] { 1, 2, "three" };

    private Invocation invocation;
    private InvocationMatcher matcher;

    @Before
    public void setUp() {
        invocation = Mockito.mock(Invocation.class);
        Mockito.when(invocation.getMethod()).thenReturn(new Method(InvocationMatcher.class.getName(), "hasSameMethod", Method.TYPE_PARAMETERS));
        Mockito.when(invocation.isVerified()).thenReturn(false);
        Mockito.when(invocation.getArguments()).thenReturn(ARGUMENTS);
        Mockito.when(invocation.getLocation()).thenReturn(null);
        Mockito.when(invocation.getMock()).thenReturn(Mockito.mock(Object.class));

        matcher = new InvocationMatcher(invocation);
    }

    @Test
    public void testMatchesWithSameArguments() {
        Invocation actual = Mockito.mock(Invocation.class);
        Mockito.when(actual.getMethod()).thenReturn(new Method(InvocationMatcher.class.getName(), "hasSameMethod", Method.TYPE_PARAMETERS));
        Mockito.when(actual.isVerified()).thenReturn(false);
        Mockito.when(actual.getArguments()).thenReturn(ARGUMENTS);
        Mockito.when(actual.getLocation()).thenReturn(null);
        Mockito.when(actual.getMock()).thenReturn(Mockito.mock(Object.class));

        assertTrue(matcher.matches(actual));
    }

    @Test
    public void testMatchesWithDifferentMethodName() {
        Invocation actual = Mockito.mock(Invocation.class);
        Mockito.when(actual.getMethod()).thenReturn(new Method(InvocationMatcher.class.getName(), "testMatchesWithDifferentMethodName", Method.TYPE_PARAMETERS));

        assertFalse(matcher.matches(actual));
    }

    @Test
    public void testMatchesWithUnverifiedInvocation() {
        Invocation actual = Mockito.mock(Invocation.class);
        Mockito.when(actual.isVerified()).thenReturn(false);

        assertTrue(matcher.matches(actual));
    }

    @Test
    public void testMatchesWithDifferentMock() {
        Invocation actual = Mockito.mock(Invocation.class);
        Mockito.when(actual.getMock()).thenReturn(Mockito.mock(Object2.class));

        assertFalse(matcher.matches(actual));
    }

    @Test
    public void testMatchesWithDifferentArguments() {
        Invocation actual = Mockito.mock(Invocation.class);
        Object[] actualArguments = new Object[] { 1L, "two", "three" };
        Mockito.when(actual.getArguments()).thenReturn(actualArguments);

        assertFalse(matcher.matches(actual));
    }

    @Test
    public void testMatchesWithDifferentArgumentsSize() {
        Invocation actual = Mockito.mock(Invocation.class);
        Object[] actualArguments = new Object[] { 1, 2, 3, 4 };
        Mockito.when(actual.getArguments()).thenReturn(actualArguments);

        assertFalse(matcher.matches(actual));
    }

    @Test
    public void testMatchesWithSameMethodButDifferentTypes() {
        Invocation actual = Mockito.mock(Invocation.class);
        Mockito.when(actual.getMethod()).thenReturn(new Method("hasSimilarMethodButDifferentTypes", int[].class, Object.class));

        assertFalse(matcher.matches(actual));
    }

    @Test
    public void testMatchesWithSameMethodButDifferentArgumentsTypes() {
        Invocation actual = Mockito.mock(Invocation.class);
        Object[] actualArguments = new Object[] { 1L, new Object() {} };
        Mockito.when(actual.getArguments()).thenReturn(actualArguments);

        assertFalse(matcher.matches(actual));
    }

    @Test
    public void testMatchesWithSameMethodButDifferentArgumentsOrder() {
        Invocation actual = Mockito.mock(Invocation.class);
        Object[] actualArguments = new Object[] { "three", 1, 2 };
        Mockito.when(actual.getArguments()).thenReturn(actualArguments);

        assertFalse(matcher.matches(actual));
    }

    @Test
    public void testMatchesWithSameMethodButNullArgument() {
        Invocation actual = Mockito.mock(Invocation.class);
        Object[] actualArguments = new Object[] { 1, null, 2 };
        Mockito.when(actual.getArguments()).thenReturn(actualArguments);

        assertFalse(matcher.matches(actual));
    }

    @Test
    public void testMatchesWithSameMethodButNullMatcher() {
        Invocation actual = Mockito.mock(Invocation.class);
        Mockito.when(actual.getArguments()).thenReturn(ARGUMENTS);

        InvocationMatcher nullMatcher = new InvocationMatcher(actual, null);

        assertTrue(nullMatcher.matches(actual));
    }

    @Test
    public void testMatchesWithVarargs() {
        Invocation actual = Mockito.mock(Invocation.class);
        Object[] varargs = new Object[] { 1, 2, 3 };
        Mockito.when(actual.getArguments()).thenReturn(varargs);

        InvocationMatcher matcher = new InvocationMatcher(actual);
        Matcher varargMatcher = ArgumentMatchers.arrayContaining(eq(1));
        matcher = new InvocationMatcher(matcher, Arrays.asList(varargMatcher));

        assertTrue(matcher.matches(actual));
    }

    @Test
    public void testMatchesWithVarargsAndNullArgument() {
        Invocation actual = Mockito.mock(Invocation.class);
        Object[] varargs = new Object[] { 1, null, 3 };
        Mockito.when(actual.getArguments()).thenReturn(varargs);

        InvocationMatcher matcher = new InvocationMatcher(actual);
        Matcher varargMatcher = ArgumentMatchers.arrayContaining(eq(1));
        matcher = new InvocationMatcher(matcher, Arrays.asList(varargMatcher));

        assertTrue(matcher.matches(actual));
    }

    @Test
    public void testMatchesWithVarargsAndNullMatcher() {
        Invocation actual = Mockito.mock(Invocation.class);
        Object[] varargs = new Object[] { 1, 2, null };
        Mockito.when(actual.getArguments()).thenReturn(varargs);

        InvocationMatcher matcher = new InvocationMatcher(actual);
        Matcher varargMatcher = ArgumentMatchers.arrayContaining(eq(1));
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(varargMatcher);
        matcher = new InvocationMatcher(matcher, matchers);

        assertFalse(matcher.matches(actual));
    }

    @Test
    public void testToString() {
        Mockito.when(invocation.getMethod()).thenReturn(new Method("testToString", String[].class));

        String result = print(matcher.getMatchers(), invocation);

        assertTrue(result.contains("testToString(String[])"));
    }

    @Test
    public void testToStringWithEmptyMatcherList() {
        Mockito.when(invocation.getMethod()).thenReturn(new Method("testToStringWithEmptyMatcherList", String[].class));

        String result = print(matcher.getMatchers(), invocation);

        assertTrue(result.isEmpty());
    }
}

class Object2 {

}
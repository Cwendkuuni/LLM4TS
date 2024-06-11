package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.ArgumentsMatcher.is;
import static org.mockito.internal.matchers.ArgumentsMatcher.not;
import static org.mockito.internal.matchers.ArgumentsMatcher.sameInstance;
import static org.mockito.internal.matchers.ArgumentsMatcher.sameMethod;
import static org.mockito.internal.matchers.ArgumentsMatcher.withArguments;
import static org.mockito.internal.matchers.ArgumentsMatcher.withNull;
import static org.mockito.internal.matchers.ArgumentsMatcher.withType;
import static org.mockito.internal.matchers.VarargMatcher.varargs;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationMatcher.ArgumentsComparator;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

public class InvocationMatcherTest {

    private static final Object[] EMPTY_ARGUMENTS = new Object[0];
    private static final Class[] EMPTY_CLASS_ARGUMENTS = new Class[0];
    private static final Object[] ONE_ARGUMENT = { "one" };
    private static final Object[] TWO_ARGUMENTS = { "one", "two" };
    private static final Class[] TWO_CLASS_ARGUMENTS = { String.class, Integer.class };
    private static final Class[] VARARG_CLASS_ARGUMENTS = { Integer[].class };
    private static final Object[] VARARG_ARGUMENTS = { ONE_ARGUMENT, TWO_ARGUMENTS };
    private static final Invocation zeroArgInvocation = Mockito.invocation(Mockito.mock(Object.class));
    private static final Invocation oneArgInvocation = Mockito.invocation(Mockito.mock(Object.class), "one");
    private static final Invocation twoArgsInvocation = Mockito.invocation(Mockito.mock(Object.class), "one", 1);
    private static final Invocation threeArgsInvocation = Mockito.invocation(Mockito.mock(Object.class), "one", 1, 2);
    private static final Invocation varargInvocation = Mockito.invocation(Mockito.mock(Object.class), Arrays.asList(TWO_ARGUMENTS).toArray());
    private static final Invocation varargInvocationWithNull = Mockito.invocation(Mockito.mock(Object.class), Arrays.asList(ONE_ARGUMENT, null).toArray());
    private static final Invocation varargInvocationWithVarargs = Mockito.invocation(Mockito.mock(Object.class), Arrays.asList(ONE_ARGUMENT, TWO_ARGUMENTS).toArray());
    private static final InvocationMatcher zeroArgMatcher = new InvocationMatcher(zeroArgInvocation);
    private static final InvocationMatcher oneArgMatcher = new InvocationMatcher(oneArgInvocation);
    private static final InvocationMatcher twoArgsMatcher = new InvocationMatcher(twoArgsInvocation);
    private static final InvocationMatcher threeArgsMatcher = new InvocationMatcher(threeArgsInvocation);
    private static final InvocationMatcher varargMatcher = new InvocationMatcher(varargInvocation);
    private static final InvocationMatcher varargMatcherWithNull = new InvocationMatcher(varargInvocationWithNull);
    private static final InvocationMatcher varargMatcherWithVarargs = new InvocationMatcher(varargInvocationWithVarargs);
    private static final CapturesArguments capturesArguments = Mockito.mock(CapturesArguments.class);
    private static final Location location = Mockito.mock(Location.class);
    private List<Matcher> matchers;

    @Before
    public void setup() {
        matchers = new LinkedList<Matcher>() {{
            add(is("one"));
            add(sameInstance(oneArgInvocation));
            add(sameMethod(twoArgsInvocation));
            add(withArguments(TWO_ARGUMENTS));
            add(withNull());
            add(withType(Integer.class));
            add(varargs(Integer.class));
            add(capturesArguments);
        }};
    }

    @Test
    public void testMatchesZeroArgInvocation() {
        assertTrue(zeroArgMatcher.matches(zeroArgInvocation));
        assertFalse(zeroArgMatcher.matches(oneArgInvocation));
        assertFalse(zeroArgMatcher.matches(twoArgsInvocation));
        assertFalse(zeroArgMatcher.matches(threeArgsInvocation));
        assertFalse(zeroArgMatcher.matches(varargInvocation));
        assertFalse(zeroArgMatcher.matches(varargInvocationWithNull));
        assertFalse(zeroArgMatcher.matches(varargInvocationWithVarargs));
    }

    @Test
    public void testMatchesOneArgInvocation() {
        assertTrue(oneArgMatcher.matches(oneArgInvocation));
        assertFalse(oneArgMatcher.matches(zeroArgInvocation));
        assertFalse(oneArgMatcher.matches(twoArgsInvocation));
        assertFalse(oneArgMatcher.matches(threeArgsInvocation));
        assertFalse(oneArgMatcher.matches(varargInvocation));
        assertFalse(oneArgMatcher.matches(varargInvocationWithNull));
        assertFalse(oneArgMatcher.matches(varargInvocationWithVarargs));
    }

    @Test
    public void testMatchesTwoArgsInvocation() {
        assertTrue(twoArgsMatcher.matches(twoArgsInvocation));
        assertFalse(twoArgsMatcher.matches(zeroArgInvocation));
        assertFalse(twoArgsMatcher.matches(oneArgInvocation));
        assertFalse(twoArgsMatcher.matches(threeArgsInvocation));
        assertFalse(twoArgsMatcher.matches(varargInvocation));
        assertFalse(twoArgsMatcher.matches(varargInvocationWithNull));
        assertFalse(twoArgsMatcher.matches(varargInvocationWithVarargs));
    }

    @Test
    public void testMatchesThreeArgsInvocation() {
        assertTrue(threeArgsMatcher.matches(threeArgsInvocation));
        assertFalse(threeArgsMatcher.matches(zeroArgInvocation));
        assertFalse(threeArgsMatcher.matches(oneArgInvocation));
        assertFalse(threeArgsMatcher.matches(twoArgsInvocation));
        assertFalse(threeArgsMatcher.matches(varargInvocation));
        assertFalse(threeArgsMatcher.matches(varargInvocationWithNull));
        assertFalse(threeArgsMatcher.matches(varargInvocationWithVarargs));
    }

    @Test
    public void testMatchesVarargInvocation() {
        assertTrue(varargMatcher.matches(varargInvocation));
        assertFalse(varargMatcher.matches(zeroArgInvocation));
        assertFalse(varargMatcher.matches(oneArgInvocation));
        assertFalse(varargMatcher.matches(twoArgsInvocation));
        assertFalse(varargMatcher.matches(threeArgsInvocation));
        assertFalse(varargMatcher.matches(varargInvocationWithNull));
        assertFalse(varargMatcher.matches(varargInvocationWithVarargs));
    }

    @Test
    public void testMatchesVarargInvocationWithNull() {
        assertTrue(varargMatcherWithNull.matches(varargInvocationWithNull));
        assertFalse(varargMatcherWithNull.matches(zeroArgInvocation));
        assertFalse(varargMatcherWithNull.matches(oneArgInvocation));
        assertFalse(varargMatcherWithNull.matches(twoArgsInvocation));
        assertFalse(varargMatcherWithNull.matches(threeArgsInvocation));
        assertFalse(varargMatcherWithNull.matches(varargInvocation));
        assertFalse(varargMatcherWithNull.matches(varargInvocationWithVarargs));
    }

    @Test
    public void testMatchesVarargInvocationWithVarargs() {
        assertTrue(varargMatcherWithVarargs.matches(varargInvocationWithVarargs));
        assertFalse(varargMatcherWithVarargs.matches(zeroArgInvocation));
        assertFalse(varargMatcherWithVarargs.matches(oneArgInvocation));
        assertFalse(varargMatcherWithVarargs.matches(twoArgsInvocation));
        assertFalse(varargMatcherWithVarargs.matches(threeArgsInvocation));
        assertFalse(varargMatcherWithVarargs.matches(varargInvocation));
        assertFalse(varargMatcherWithVarargs.matches(varargInvocationWithNull));
    }

    @Test
    public void testMatchesZeroArgsWithEmptyListMatcher() {
        List<Matcher> matchersWithEmptyList = Collections.singletonList(withArguments(EMPTY_ARGUMENTS));
        InvocationMatcher emptyListMatcher = new InvocationMatcher(zeroArgInvocation, matchersWithEmptyList);
        assertTrue(emptyListMatcher.matches(zeroArgInvocation));
    }

    @Test
    public void testMatchesVarargsWithVarargsMatcher() {
        List<Matcher> matchersWithVarargsMatcher = Arrays.asList(is("one"), varargs(Integer.class));
        InvocationMatcher varargsMatcherWithVarargs = new InvocationMatcher(varargInvocationWithVarargs, matchersWithVarargsMatcher);
        assertTrue(varargsMatcherWithVarargs.matches(varargInvocationWithVarargs));
    }

    @Test
    public void testMatchesWithCapturesArguments() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(twoArgsInvocation);
        invocationMatcher.captureArgumentsFrom(twoArgsInvocation);
        assertTrue(capturesArguments.matches(twoArgsInvocation.getArgumentAt(0, String.class)));
        assertTrue(capturesArguments.matches(twoArgsInvocation.getArgumentAt(1, Integer.class)));
    }

    @Test
    public void testMatchesWithNullInVarargs() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(varargInvocationWithNull);
        invocationMatcher.captureArgumentsFrom(varargInvocationWithNull);
        assertTrue(capturesArguments.matches(varargInvocationWithNull.getArgumentAt(0, Object[].class)));
        assertTrue(capturesArguments.matches(null));
    }

    @Test
    public void testMatchesWithVarargsAndNoNull() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(varargInvocationWithVarargs);
        invocationMatcher.captureArgumentsFrom(varargInvocationWithVarargs);
        assertTrue(capturesArguments.matches(varargInvocationWithVarargs.getArgumentAt(0, Object[].class)));
        assertTrue(capturesArguments.matches(ONE_ARGUMENT));
        assertTrue(capturesArguments.matches(TWO_ARGUMENTS[0]));
        assertTrue(capturesArguments.matches(TWO_ARGUMENTS[1]));
    }

    @Test
    public void testMatchesHasSameMethod() {
        Method method1 = getClass().getDeclaredMethod("testMatchesHasSameMethod", String.class, int.class);
        Method method2 = getClass().getDeclaredMethod("testMatchesHasSameMethod", String.class, int.class, int.class);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(Mockito.invocation(this, "test", 1));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(Mockito.invocation(this, "test", 1, 1));
        assertTrue(invocationMatcher1.hasSameMethod(invocationMatcher2));
        assertFalse(invocationMatcher1.hasSameMethod(Mockito.invocation(this, "test", 1, 2)));
        assertFalse(invocationMatcher1.hasSameMethod(Mockito.invocation(this, "test2", 1, 1)));
    }

    @Test
    public void testMatchesDoesNotMatchOverloadedMethods() {
        Assertions.assertThatThrownBy(() -> {
            Method method1 = getClass().getDeclaredMethod("testMatchesDoesNotMatchOverloadedMethods", String.class, String.class);
            Method method2 = getClass().getDeclaredMethod("testMatchesDoesNotMatchOverloadedMethods", String.class, Integer.class);
            InvocationMatcher invocationMatcher1 = new InvocationMatcher(Mockito.invocation(this, "test", "one", "two"));
            InvocationMatcher invocationMatcher2 = new InvocationMatcher(Mockito.invocation(this, "test", "one", 1));
            assertTrue(invocationMatcher1.matches(Mockito.invocation(this, "test", "one", "two")));
            assertFalse(invocationMatcher1.matches(Mockito.invocation(this, "test", "one", 1)));
            assertFalse(invocationMatcher2.matches(Mockito.invocation(this, "test", "one", "two")));
            assertFalse(invocationMatcher2.matches(Mockito.invocation(this, "test", "one", 1)));
        }).hasMessageContaining("Actual and expected method signatures are not identical.");
    }
}
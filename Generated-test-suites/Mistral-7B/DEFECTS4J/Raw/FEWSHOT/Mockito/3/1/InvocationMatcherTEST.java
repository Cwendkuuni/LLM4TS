package org.mockito.internal.invocation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.ArgumentsMatcher.argumentsWithSameTypeAs;
import static org.mockito.internal.matchers.ArgumentsMatcher.isNull;
import static org.mockito.internal.matchers.ArgumentsMatcher.notNull;
import static org.mockito.internal.reporting.PrintSettings.defaultPrintSettings;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.matchers.MatcherDecorator;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

public class InvocationMatcherTest {

    private Invocation invocation;
    private List<Matcher> matchers;
    private Object mock;

    @Before
    public void setUp() {
        mock = Mockito.mock(Object.class);
        invocation = Mockito.mock(Invocation.class);
        matchers = Arrays.asList(isNull(), notNull(), argumentsWithSameTypeAs(int[].class));
    }

    @Test
    public void testInvocationMatcherWithEmptyList() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(Collections.emptyList(), invocationMatcher.getMatchers());
    }

    @Test
    public void testInvocationMatcherWithMatchers() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testGetMethod() {
        Method method = Mockito.methodThat(invocation -> invocation.getMethod().getName().equals("testMethod"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(Mockito.mock(Invocation.class), Arrays.asList(new MatcherDecorator(method)));
        assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testToString() {
        String expected = defaultPrintSettings.print(matchers, invocation);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertEquals(expected, invocationMatcher.toString());
    }

    @Test
    public void testMatchesSameInvocation() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertTrue(invocationMatcher.matches(invocation));
    }

    @Test
    public void testMatchesDifferentInvocationWithDifferentMethod() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation differentInvocation = Mockito.mock(Invocation.class);
        assertFalse(invocationMatcher.matches(differentInvocation));
    }

    @Test
    public void testMatchesDifferentInvocationWithSameMethodButDifferentArguments() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Invocation differentInvocation = Mockito.mock(Invocation.class);
        Object[] arguments = new Object[1];
        arguments[0] = "different argument";
        differentInvocation.setArguments(arguments);
        assertFalse(invocationMatcher.matches(differentInvocation));
    }

    @Test
    public void testMatchesDifferentInvocationWithSameMethodAndSameArguments() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Invocation differentInvocation = Mockito.mock(Invocation.class);
        differentInvocation.setArguments(invocation.getArguments());
        assertTrue(invocationMatcher.matches(differentInvocation));
    }

    @Test
    public void testMatchesDifferentInvocationWithSameMethodButUnverifiedVsVerified() {
        Invocation unverifiedInvocation = Mockito.mock(Invocation.class);
        unverifiedInvocation.setVerified(false);
        Invocation verifiedInvocation = Mockito.mock(Invocation.class);
        verifiedInvocation.setVerified(true);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertFalse(invocationMatcher.matches(unverifiedInvocation));
        assertTrue(invocationMatcher.matches(verifiedInvocation));
    }

    @Test
    public void testMatchesDifferentInvocationWithSameMethodAndSameArgumentsButDifferentMocks() {
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(Mockito.mock(Invocation.class), matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(Mockito.mock(Invocation.class), matchers);
        assertFalse(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testMatchesDifferentInvocationWithSameMethodAndSameArgumentsButSameMocks() {
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation, matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation, matchers);
        assertTrue(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testMatchesInvocationWithVarArgs() {
        List<Matcher> matchersWithVarArgs = Arrays.asList(isNull(), argumentsWithSameTypeAs(int[].class), isNull());
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchersWithVarArgs);
        Object[] arguments = new Object[2];
        arguments[0] = 1;
        arguments[1] = new int[2];
        invocation.setArguments(arguments);
        invocationMatcher.captureArgumentsFrom(invocation);
        assertTrue(invocationMatcher.matches(invocation));
    }

    @Test
    public void testHasSameMethod() {
        Method method1 = Mockito.methodThat(invocation1 -> invocation1.getMethod().getName().equals("testMethod1"));
        Method method2 = Mockito.methodThat(invocation2 -> invocation2.getMethod().getName().equals("testMethod1"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(new MatcherDecorator(method1)));
        assertTrue(invocationMatcher.hasSameMethod(new InvocationMatcher(invocation, Arrays.asList(new MatcherDecorator(method2)))));
    }

    @Test
    public void testHasSameMethodWithDifferentMethods() {
        Method method1 = Mockito.methodThat(invocation1 -> invocation1.getMethod().getName().equals("testMethod1"));
        Method method2 = Mockito.methodThat(invocation2 -> invocation2.getMethod().getName().equals("testMethod2"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(new MatcherDecorator(method1)));
        assertFalse(invocationMatcher.hasSameMethod(new InvocationMatcher(invocation, Arrays.asList(new MatcherDecorator(method2)))));
    }

    @Test
    public void testHasSimilarMethodWithSameMethod() {
        Method method = Mockito.methodThat(invocation -> invocation.getMethod().getName().equals("testMethod"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(new MatcherDecorator(method)));
        assertTrue(invocationMatcher.hasSimilarMethod(new InvocationMatcher(invocation, Arrays.asList(new MatcherDecorator(method)))));
    }

    @Test
    public void testHasSimilarMethodWithDifferentMethods() {
        Method method1 = Mockito.methodThat(invocation1 -> invocation1.getMethod().getName().equals("testMethod1"));
        Method method2 = Mockito.methodThat(invocation2 -> invocation2.getMethod().getName().equals("testMethod2"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(new MatcherDecorator(method1)));
        assertFalse(invocationMatcher.hasSimilarMethod(new InvocationMatcher(invocation, Arrays.asList(new MatcherDecorator(method2)))));
    }

    @Test
    public void testHasSimilarMethodWithSameMethodButDifferentArguments() {
        Method method = Mockito.methodThat(invocation -> invocation.getMethod().getName().equals("testMethod"));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation, Arrays.asList(new MatcherDecorator(method)));
        Object[] arguments1 = new Object[1];
        arguments1[0] = 1;
        invocation.setArguments(arguments1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation, Arrays.asList(new MatcherDecorator(method)));
        assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testHasSimilarMethodWithSameMethodButSameArguments() {
        Method method = Mockito.methodThat(invocation -> invocation.getMethod().getName().equals("testMethod"));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation, Arrays.asList(new MatcherDecorator(method)));
        Object[] arguments1 = new Object[1];
        arguments1[0] = 1;
        invocation.setArguments(arguments1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation, Arrays.asList(new MatcherDecorator(method)));
        assertTrue(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testHasSimilarMethodWithSameMethodButDifferentMocks() {
        Method method = Mockito.methodThat(invocation1 -> invocation1.getMethod().getName().equals("testMethod"));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation, Arrays.asList(new MatcherDecorator(method)));
        Object mock1 = Mockito.mock(Object.class);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(Mockito.mock(Invocation.class), Arrays.asList(new MatcherDecorator(method)));
        assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testHasSimilarMethodWithSameMethodButSameMocks() {
        Method method = Mockito.methodThat(invocation -> invocation.getMethod().getName().equals("testMethod"));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation, Arrays.asList(new MatcherDecorator(method)));
        Object mock = Mockito.mock(Object.class);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation, Arrays.asList(new MatcherDecorator(method)));
        assertTrue(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = Arrays.asList(invocation, invocation);
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        assertEquals(2, invocationMatchers.size());
        for (InvocationMatcher invocationMatcher : invocationMatchers) {
            assertEquals(invocation, invocationMatcher.getInvocation());
            assertSame(matchers, invocationMatcher.getMatchers());
        }
    }

    @Test
    public void testCreateFromWithNullList() {
        assertNull(InvocationMatcher.createFrom(null));
    }

    @Test
    public void testCreateFromWithEmptyList() {
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(Collections.emptyList());
        assertEquals(0, invocationMatchers.size());
    }
}
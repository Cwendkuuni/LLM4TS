package com.example.junit4.test.invocationmatcher;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;

public class InvocationMatcherTest {

    @Test
    public void testMatches() {
        Invocation invocation1 = new Invocation(new Object() {}, new Object[]{}, new Location("Test.java", 1));
        Invocation invocation2 = new Invocation(new Object() {}, new Object[]{}, new Location("Test.java", 2));
        List<Matcher> matchers = Collections.singletonList(new MockitoJunitRunnerMockMatcher());

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, matchers);

        Assert.assertTrue(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testInvocationMatcherNullInvocation() {
        List<Matcher> matchers = Collections.singletonList(new MockitoJunitRunnerMockMatcher());
        Assert.assertThrows(NullPointerException.class, () -> new InvocationMatcher(null, matchers));
    }

    @Test
    public void testInvocationMatcherNullMatchers() {
        Invocation invocation = new Invocation(new Object() {}, new Object[]{}, new Location("Test.java", 1));
        Assert.assertThrows(NullPointerException.class, () -> new InvocationMatcher(invocation, null));
    }

    @Test
    public void testInvocationMatcherDifferentMocks() {
        Invocation invocation1 = new Invocation(new Object() {}, new Object[]{}, new Location("Test.java", 1));
        Invocation invocation2 = new Invocation(new Object() {}, new Object[]{}, new Location("Test.java", 2));
        List<Matcher> matchers = Collections.singletonList(new MockitoJunitRunnerMockMatcher());

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, matchers);

        Assert.assertFalse(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testInvocationMatcherSameMethodDifferentArguments() {
        Invocation invocation1 = new Invocation(new Object() {}, new Object[]{"arg1"}, new Location("Test.java", 1));
        Invocation invocation2 = new Invocation(new Object() {}, new Object[]{"arg2"}, new Location("Test.java", 2));
        List<Matcher> matchers = Collections.singletonList(new MockitoJunitRunnerMockMatcher());

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, matchers);

        Assert.assertFalse(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testInvocationMatcherSameMethodSameArgumentsButDifferentObjects() {
        Invocation invocation1 = new Invocation(new Object() {}, new Object[]{new Object()}, new Location("Test.java", 1));
        Invocation invocation2 = new Invocation(new Object() {}, new Object[]{new Object()}, new Location("Test.java", 2));
        List<Matcher> matchers = Collections.singletonList(new MockitoJunitRunnerMockMatcher());

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, matchers);

        Assert.assertTrue(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testInvocationMatcherSameMethodDifferentClasses() {
        Object mockObject1 = new Object() {};
        Object mockObject2 = new Object() {};
        Invocation invocation1 = new Invocation(mockObject1, new Object[]{}, new Location("Test.java", 1));
        Invocation invocation2 = new Invocation(mockObject2, new Object[]{}, new Location("Test.java", 2));
        List<Matcher> matchers = Collections.singletonList(new MockitoJunitRunnerMockMatcher());

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, matchers);

        Assert.assertFalse(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testInvocationMatcherSameMethodSameClassesButDifferentArguments() {
        Object arg1 = new Object();
        Object arg2 = new Object();
        Object mockObject = new Object() {};

        Invocation invocation1 = new Invocation(mockObject, new Object[]{arg1}, new Location("Test.java", 1));
        Invocation invocation2 = new Invocation(mockObject, new Object[]{arg2}, new Location("Test.java", 2));
        List<Matcher> matchers = Collections.singletonList(new MockitoJunitRunnerMockMatcher());

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, matchers);

        Assert.assertFalse(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testInvocationMatcherSameMethodSameArgumentsSameObjects() {
        Object arg = new Object();
        Object mockObject = new Object() {};

        Invocation invocation1 = new Invocation(mockObject, new Object[]{arg}, new Location("Test.java", 1));
        Invocation invocation2 = new Invocation(mockObject, new Object[]{arg}, new Location("Test.java", 2));
        List<Matcher> matchers = Collections.singletonList(new MockitoJunitRunnerMockMatcher());

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, matchers);

        Assert.assertTrue(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testInvocationMatcherSameMethodWithNullArgument() {
        Object mockObject = new Object() {};
        Object[] arguments = new Object[1];
        arguments[0] = null;

        Invocation invocation = new Invocation(mockObject, arguments, new Location("Test.java", 1));
        List<Matcher> matchers = Collections.singletonList(new MockitoJunitRunnerMockMatcher());

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Object[] expectedArguments = new Object[1];
        expectedArguments[0] = null;

        InvocationMatcher invocationMatcherToMatch = new InvocationMatcher(new Invocation(mockObject, expectedArguments, new Location("Test.java", 2)), matchers);

        Assert.assertTrue(invocationMatcher.matches(invocationMatcherToMatch));
    }

    @Test(expected = NullPointerException.class)
    public void testInvocationMatcherNullArguments() {
        Object mockObject = new Object() {};
        Invocation invocation = new Invocation(mockObject, null, new Location("Test.java", 1));
        List<Matcher> matchers = Collections.singletonList(new MockitoJunitRunnerMockMatcher());

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
    }

    @Test(expected = NullPointerException.class)
    public void testInvocationMatcherNullMatcher() {
        Object mockObject = new Object() {};
        Invocation invocation = new Invocation(mockObject, new Object[0], new Location("Test.java", 1));
        List<Matcher> nullMatchers = null;

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, nullMatchers);
    }

    @Test
    public void testInvocationMatcherCreateFrom() {
        List<Invocation> invocations = Collections.singletonList(new Invocation(new Object() {}, new Object[0], new Location("Test.java", 1)));
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        Assert.assertEquals(1, invocationMatchers.size());
    }
}
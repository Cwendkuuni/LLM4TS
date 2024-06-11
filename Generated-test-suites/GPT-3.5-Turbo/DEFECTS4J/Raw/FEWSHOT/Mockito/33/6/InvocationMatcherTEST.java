package org.mockito.internal.invocation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.hamcrest.Matcher;
import org.junit.Test;

public class InvocationMatcherTest {
    @Test
    public void testConstructorWithMatchers() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(new Matcher());

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithoutMatchers() {
        Invocation invocation = new Invocation();

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testGetMethod() {
        Invocation invocation = new Invocation();
        invocation.setMethod(new Method());

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(invocation.getMethod(), invocationMatcher.getMethod());
    }

    @Test
    public void testToString() {
        Invocation invocation = new Invocation();
        invocation.setMethod(new Method());
        PrintSettings printSettings = new PrintSettings();

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(invocation.toString(invocationMatcher.getMatchers(), printSettings), invocationMatcher.toString());
    }

    @Test
    public void testMatches() {
        Invocation invocation1 = new Invocation();
        Invocation invocation2 = new Invocation();

        ArgumentsComparator argumentsComparator = new ArgumentsComparator();
        boolean argumentsMatchResult = true;
        argumentsComparator.argumentsMatch(invocation1, invocation2.getArguments()).thenReturn(argumentsMatchResult);

        invocation1.setMock(new Object());
        invocation2.setMock(invocation1.getMock());
        invocation1.setMethod(new Method());
        invocation2.setMethod(invocation1.getMethod());

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        assertTrue(invocationMatcher.matches(invocation2));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation1 = new Invocation();
        Invocation invocation2 = new Invocation();

        ArgumentsComparator argumentsComparator = new ArgumentsComparator();
        boolean argumentsMatchResult = true;
        argumentsComparator.argumentsMatch(invocation1, invocation2.getArguments()).thenReturn(argumentsMatchResult);

        invocation1.setVerified(false);
        invocation2.setVerified(invocation1.getVerified());
        invocation1.setMock(new Object());
        invocation2.setMock(invocation1.getMock());
        invocation1.setMethod(new Method());
        invocation2.setMethod(invocation1.getMethod());

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        assertTrue(invocationMatcher.hasSimilarMethod(invocation2));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = new Invocation();
        Invocation invocation2 = new Invocation();

        invocation1.setMethod(new Method());
        invocation2.setMethod(invocation1.getMethod());

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        assertTrue(invocationMatcher.hasSameMethod(invocation2));
    }

    @Test
    public void testHasSameMethodWhenDifferentMethods() {
        Invocation invocation1 = new Invocation();
        Invocation invocation2 = new Invocation();

        invocation1.setMethod(new Method());
        invocation2.setMethod(new Method());

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        assertFalse(invocationMatcher.hasSameMethod(invocation2));
    }
}
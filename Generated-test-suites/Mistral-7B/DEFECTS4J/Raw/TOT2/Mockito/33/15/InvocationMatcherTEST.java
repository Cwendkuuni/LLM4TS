package org.junit.test;

import static org.junit.Assert.*;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.Mockito;

public class InvocationMatcherTest {

    @Test
    public void testMatches() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        Invocation actual = Mockito.spy(invocation);

        // given
        Mockito.verify(invocation).getMethod();

        // when
        boolean result = matcher.matches(actual);

        // then
        assertTrue("Expected InvocationMatcher to match Invocation", result);
    }

    // testInvocationsWithDifferentMocks.java
    @Test
    public void testInvocationsWithDifferentMocks() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);

        // assertInvocationMatchesDifferentMocks
        assertFalse("Expected InvocationMatcher to not match Invocation with different mocks", matcher1.matches(invocation2));
    }

    // testInvocationsWithDifferentMethods.java
    @Test
    public void testInvocationsWithDifferentMethods() {
        Invocation invocation1 = Mockito.mock(Invocation.class, new Method[]{new Method("method1", new Class[]{String.class})});
        Invocation invocation2 = Mockito.mock(Invocation.class, new Method[]{new Method("method2", new Class[]{Integer.class})});
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        // assertInvocationMatchesDifferentMethods
        assertFalse("Expected InvocationMatcher to not match Invocation with different methods", matcher1.matches(matcher2));
    }

    // testInvocationsWithDifferentArguments.java
    @Test
    public void testInvocationsWithDifferentArguments() {
        Invocation invocation1 = Mockito.mock(Invocation.class, new Object[]{"arg1", 1, "arg3"});
        Invocation invocation2 = Mockito.mock(Invocation.class, new Object[]{"arg1", "arg2", 3});
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        // assertInvocationMatchesDifferentArguments
        assertFalse("Expected InvocationMatcher to not match Invocation with different arguments", matcher1.matches(matcher2));
    }

    // testInvocationsWithNullArguments.java
    @Test
    public void testInvocationsWithNullArguments() {
        Invocation invocation1 = Mockito.mock(Invocation.class, new Object[]{null, 1, "arg3"});
        Invocation invocation2 = Mockito.mock(Invocation.class, new Object[]{null, null, null});
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        // assertInvocationMatchesNullArguments
        assertFalse("Expected InvocationMatcher to not match Invocation with null arguments", matcher1.matches(matcher2));
    }

    // testInvocationsWithExceptions.java
    @Test(expected = RuntimeException.class)
    public void testInvocationsWithExceptions() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        Invocation invocation2 = Mockito.mock(Invocation.class, new Throwable[]{new RuntimeException("Test Exception")});
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        // assertInvocationMatchesException
        matcher1.matches(matcher2);
    }
}
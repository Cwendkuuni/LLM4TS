package org.mockito.internal.invocation;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.MatcherAssert.assertThat;

public class InvocationMatcherTest {

    @Test
    public void testSimpleInvocationMatcher() {
        Object mock = Mockito.mock(Object.class);
        Invocation invocation = new MockitoInvocation(mock, new Object[]{"test"}, new Method(Object.class, "test", new Class[]{String.class}));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        InvocationMatcher expectedMatcher = ArgumentMatchers.argThat(m -> m.getMethod().getName().equals("test") && m.getMock() == mock);
        InvocationMatcher actualMatcher = invocationMatcher;

        assertThat(actualMatcher, isA(expectedMatcher.getClass()));
        assertThat(actualMatcher.matches(invocation), is(true));
    }

    @Test
    public void testInvocationMatcherWithArguments() {
        Object mock = Mockito.mock(Object.class);
        Invocation invocation = new MockitoInvocation(mock, new Object[]{"test1", 1}, new Method(Object.class, "test", new Class[]{String.class, int.class}));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(ArgumentMatchers.argThat(a -> a.equals("test1")), ArgumentMatchers.argThat(a -> a.equals(1))));

        InvocationMatcher expectedMatcher = ArgumentMatchers.argThat(m -> m.getMethod().getName().equals("test") && m.getMock() == mock && m.getMatchers().get(0).matches(m.getArguments()[0]) && m.getMatchers().get(1).matches(m.getArguments()[1]));
        InvocationMatcher actualMatcher = invocationMatcher;

        assertThat(actualMatcher, isA(expectedMatcher.getClass()));
        assertThat(actualMatcher.matches(invocation), is(true));
    }

    @Test
    public void testInvocationMatcherWithDifferentMethod() {
        Object mock = Mockito.mock(Object.class);
        Invocation invocation1 = new MockitoInvocation(mock, new Object[]{"test1", 1}, new Method(Object.class, "test", new Class[]{String.class, int.class}));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);

        Invocation invocation2 = new MockitoInvocation(mock, new Object[]{"test2", 2}, new Method(Object.class, "anotherTest", new Class[]{String.class, int.class}));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        assertFalse(invocationMatcher1.matches(invocation2));
        assertFalse(invocationMatcher2.matches(invocation1));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testInvocationMatcherWithVarargs() {
        Object mock = Mockito.mock(Object.class);
        Invocation invocation = new MockitoInvocation(mock, new Object[]{"test1", "test2", "test3"}, new Method(Object.class, "test", new Class[]{String[].class}));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        invocationMatcher.captureArgumentsFrom(invocation);
    }
}
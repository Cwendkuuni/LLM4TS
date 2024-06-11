package org.mockito.internal.invocation;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.isInstanceOf;
import static org.hamcrest.CoreMatchers.isNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherWithEmptyMatchers() {
        // given
        Invocation invocation = new Invocation(new Method(Object.class, "voidMethod", new Class[0]), new Object[0], new Location(1, 1));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // when
        Invocation actualInvocation = Mockito.mock(Invocation.class);
        actualInvocation.setMock(invocation.getMock());
        actualInvocation.setMethod(invocation.getMethod());
        actualInvocation.setArguments(invocation.getArguments());
        actualInvocation.setLocation(invocation.getLocation());
        InvocationMatcher actualMatcher = new InvocationMatcher(actualInvocation);

        // then
        assertTrue(invocationMatcher.matches(actualMatcher));
    }

    @Test
    public void testInvocationMatcherWithMatchers() {
        // given
        Object mock = mock(ClassWithMethod.class);

        Method method = new Method(Object.class, "methodWithTwoArguments", new Class[]{String.class, Integer.TYPE});
        Invocation invocation = new Invocation(method, new Object[]{"test", 1}, new Location(1, 1));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);

        // when
        when(mock.methodWithTwoArguments(stringArgumentCaptor.capture(), integerArgumentCaptor.capture()))
                .thenAnswer(new Answer<Object>() {
                    @Override
                    public Object answer(InvocationOnMock invocation) throws Throwable {
                        return null;
                    }
                });

        invocationMatcher.captureArgumentsFrom(Mockito.mock(Invocation.class));

        // then
        String capturedString = stringArgumentCaptor.getValue();
        Integer capturedInteger = integerArgumentCaptor.getValue();

        verify(mock).methodWithTwoArguments(capturedString, capturedInteger);
        verifyNoMoreInteractions(mock);

        assertThat(invocationMatcher.getMatchers().get(0), isA(isInstanceOf(org.hamcrest.Matcher.class)));
        assertThat(invocationMatcher.getMatchers().get(1), isA(isInstanceOf(org.hamcrest.Matcher.class)));
    }

    @Test(expected = AssertionError.class)
    public void testInvocationMatcherWithDifferentMethods() {
        // given
        Invocation invocation1 = new Invocation(new Method(Object.class, "method1", new Class[0]), new Object[0], new Location(1, 1));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);

        Invocation invocation2 = new Invocation(new Method(Object.class, "method2", new Class[0]), new Object[0], new Location(1, 1));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        // then
        assertFalse(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test(expected = AssertionError.class)
    public void testInvocationMatcherWithDifferentMocks() {
        // given
        Object mock1 = mock(ClassWithMethod.class);
        Invocation invocation1 = new Invocation(new Method(Object.class, "method", new Class[0]), new Object[0], new Location(1, 1));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);

        Object mock2 = mock(ClassWithMethod.class);
        Invocation invocation2 = new Invocation(new Method(Object.class, "method", new Class[0]), new Object[0], new Location(1, 1));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        // then
        assertFalse(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testInvocationMatcherWithSameMethodButDifferentArguments() {
        // given
        Object mock = mock(ClassWithMethod.class);

        Method method = new Method(Object.class, "methodWithTwoArguments", new Class[]{String.class, Integer.TYPE});
        Invocation invocation = new Invocation(method, new Object[]{"test1", 1}, new Location(1, 1));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // when
        InvocationMatcher actualMatcher = new InvocationMatcher(new Invocation(method, new Object[]{"test2", 2}, new Location(1, 1)));

        // then
        assertFalse(invocationMatcher.matches(actualMatcher));
    }

    @Test
    public void testInvocationMatcherWithOverloadedMethods() {
        // given
        Object mock = mock(ClassWithMethod.class);

        Method method1 = new Method(Object.class, "methodWithOneArgument", new Class[]{Integer.TYPE});
        Invocation invocation1 = new Invocation(method1, new Object[]{1}, new Location(1, 1));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);

        Method method2 = new Method(Object.class, "methodWithOneArgument", new Class[]{Double.TYPE});
        Invocation invocation2 = new Invocation(method2, new Object[]{1.0}, new Location(1, 1));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        // when
        InvocationMatcher actualMatcher = new InvocationMatcher(new Invocation(method1, new Object[]{1}, new Location(1, 1)));

        // then
        assertTrue(invocationMatcher1.matches(actualMatcher));
        assertFalse(invocationMatcher1.matches(invocationMatcher2));
        assertFalse(invocationMatcher2.matches(invocationMatcher1));
        assertFalse(invocationMatcher2.matches(actualMatcher));
    }

    interface ClassWithMethod {
        void methodWithTwoArguments(String string, Integer integer);
        void method(Object obj);
        void methodWithOneArgument(Number number);
    }
}
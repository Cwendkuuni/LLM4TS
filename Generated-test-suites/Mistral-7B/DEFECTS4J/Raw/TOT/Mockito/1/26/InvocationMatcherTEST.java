package org.mockito.internal.invocation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.isInstanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    @Test
    public void testBasicUsage() {
        Object mock = mock(MyClass.class);
        MyClass myClass = new MyClass();
        Object[] args = new Object[]{1, "hello"};

        Invocation invocation = Mockito.mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(new Method(MyClass.class, "myMethod", new Class[]{Integer.class, String.class}));
        when(invocation.getArguments()).thenReturn(args);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        List<Matcher> matchers = Arrays.asList(isA(Integer.class), isInstanceOf(String.class));
        InvocationMatcher invocationMatcherWithMatchers = new InvocationMatcher(invocation, matchers);

        assertThat(invocationMatcher, is(invocationMatcherWithMatchers));

        verify(mock).myMethod(1, "hello");
    }

    @Test
    public void testEmptyMatcherList() {
        Object mock = mock(MyClass.class);
        MyClass myClass = new MyClass();
        Object[] args = new Object[]{1, "hello"};

        Invocation invocation = Mockito.mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(new Method(MyClass.class, "myMethod", new Class[]{Integer.class, String.class}));
        when(invocation.getArguments()).thenReturn(args);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        List<Matcher> emptyMatchersList = Collections.emptyList();

        InvocationMatcher invocationMatcherWithEmptyMatchers = new InvocationMatcher(invocation, emptyMatchersList);

        assert invocationMatcher.getMatchers().isEmpty();
        assert invocationMatcherWithEmptyMatchers.getMatchers().isEmpty();

        assertThat(invocationMatcher, is(invocationMatcherWithEmptyMatchers));

        verify(mock).myMethod(1, "hello");
    }

    @Test
    public void testMatches() {
        Object mock = mock(MyClass.class);
        MyClass myClass = new MyClass();
        Object[] args = new Object[]{1, "hello"};

        Invocation invocation = Mockito.mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(new Method(MyClass.class, "myMethod", new Class[]{Integer.class, String.class}));
        when(invocation.getArguments()).thenReturn(args);
        when(invocation.getMock()).thenReturn(mock);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Object[] expectedArgs = new Object[]{1, "world"};
        InvocationMatcher expectedMatcher = new InvocationMatcher(Mockito.mock(Invocation.class), Arrays.asList(isA(Integer.class), isInstanceOf(String.class), isA(String.class).is("world")));

        List<Matcher> matchers = Arrays.asList(isA(Integer.class), isInstanceOf(String.class));
        InvocationMatcher matcherWithMatchers = new InvocationMatcher(Mockito.mock(Invocation.class), matchers);

        assert invocationMatcher.matches(invocation);
        assert expectedMatcher.matches(invocation);

        assert invocationMatcher.matches(matcherWithMatchers);
        assert expectedMatcher.matches(matcherWithMatchers);

        verify(mock).myMethod(1, "hello");
    }

    @Test
    public void testHasSimilarMethod() {
        Object mock = mock(MyClass.class);
        MyClass myClass = new MyClass();
        Object[] args1 = new Object[]{1, "hello"};
        Object[] args2 = new Object[]{2, "world"};

        Invocation invocation1 = Mockito.mock(Invocation.class);
        when(invocation1.getMethod()).thenReturn(new Method(MyClass.class, "myMethod", new Class[]{Integer.class, String.class}));
        when(invocation1.getArguments()).thenReturn(args1);
        when(invocation1.getMock()).thenReturn(mock);

        Invocation invocation2 = Mockito.mock(Invocation.class);
        when(invocation2.getMethod()).thenReturn(new Method(MyClass.class, "myMethod", new Class[]{Integer.class, String.class}));
        when(invocation2.getArguments()).thenReturn(args2);
        when(invocation2.getMock()).thenReturn(mock);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        assert invocationMatcher1.hasSimilarMethod(invocationMatcher2);

        verify(mock).myMethod(1, "hello");
        verify(mock).myMethod(2, "world");
    }

    @Test
    public void testHasSameMethod() {
        Object mock = mock(MyClass.class);
        MyClass myClass = new MyClass();
        Object[] args1 = new Object[]{1, "hello"};
        Object[] args2 = new Object[]{1, "hello"};

        Invocation invocation1 = Mockito.mock(Invocation.class);
        when(invocation1.getMethod()).thenReturn(new Method(MyClass.class, "myMethod", new Class[]{Integer.class, String.class}));
        when(invocation1.getArguments()).thenReturn(args1);
        when(invocation1.getMock()).thenReturn(mock);

        Invocation invocation2 = Mockito.mock(Invocation.class);
        when(invocation2.getMethod()).thenReturn(new Method(MyClass.class, "myMethod", new Class[]{Integer.class, String.class}));
        when(invocation2.getArguments()).thenReturn(args2);
        when(invocation2.getMock()).thenReturn(mock);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        assert invocationMatcher1.hasSameMethod(invocationMatcher2);

        verify(mock).myMethod(1, "hello");
    }

    @Test
    public void testToString() {
        Object mock = mock(MyClass.class);
        MyClass myClass = new MyClass();
        Object[] args1 = new Object[]{1, "hello"};
        Object[] args2 = new Object[]{2, "world"};

        Invocation invocation1 = Mockito.mock(Invocation.class);
        when(invocation1.getMethod()).thenReturn(new Method(MyClass.class, "myMethod", new Class[]{Integer.class, String.class}));
        when(invocation1.getArguments()).thenReturn(args1);
        when(invocation1.getMock()).thenReturn(mock);

        Invocation invocation2 = Mockito.mock(Invocation.class);
        when(invocation2.getMethod()).thenReturn(new Method(MyClass.class, "myMethod", new Class[]{Integer.class, String.class}));
        when(invocation2.getArguments()).thenReturn(args2);
        when(invocation2.getMock()).thenReturn(mock);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        String invocationMatcherToString1 = invocationMatcher1.toString();
        String invocationMatcherToString2 = invocationMatcher2.toString();

        assert invocationMatcherToString1.equals(invocationMatcherToString2);

        verify(mock).myMethod(1, "hello");
        verify(mock).myMethod(2, "world");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCaptureArgumentsFromVarargs() {
        Object mock = mock(MyClass.class);
        MyClass myClass = new MyClass();
        Object[] args = new Object[]{new Integer[]{1, 2}, "hello"};

        Invocation invocation = Mockito.mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(new Method(MyClass.class, "myMethod", new Class[]{Integer[].class, String.class}));
        when(invocation.getArguments()).thenReturn(args);
        when(invocation.getMock()).thenReturn(mock);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        invocationMatcher.captureArgumentsFrom(invocation);

        verify(mock).myMethod(new Integer[]{1, 2}, "hello");
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Object mock = mock(MyClass.class);
        MyClass myClass = new MyClass();
        Object[] args = new Object[]{1, "hello"};

        Invocation invocation = Mockito.mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(new Method(MyClass.class, "myMethod", new Class[]{Integer.class, String.class}));
        when(invocation.getArguments()).thenReturn(args);
        when(invocation.getMock()).thenReturn(mock);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        List<Matcher> matchers = Arrays.asList(isA(Integer.class), isInstanceOf(String.class));
        InvocationMatcher matcherWithMatchers = new InvocationMatcher(invocation, matchers);

        invocationMatcher.captureArgumentsFrom(invocation);
        matcherWithMatchers.captureArgumentsFrom(invocation);

        verify(mock).myMethod(1, "hello");
    }
}
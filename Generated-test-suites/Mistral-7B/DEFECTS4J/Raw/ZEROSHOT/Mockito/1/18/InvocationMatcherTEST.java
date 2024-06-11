package org.mockito.internal.invocation;

import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcher_SingleMatcher() {
        Object mock = Mockito.mock(Object.class);
        Invocation invocation = Mockito.verify(mock).methodWithOneArgument(ArgumentMatchers.eq(1L));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Matcher matcher = ArgumentMatchers.eq(1L);
        List<Matcher> matchers = Arrays.asList(matcher);
        InvocationMatcher expectedMatcher = new InvocationMatcher(invocation, matchers);

        MatcherAssert.assertThat(invocationMatcher, Matchers.is(expectedMatcher));
        MatcherAssert.assertThat(invocationMatcher.getMatchers(), Matchers.contains(matcher));
    }

    @Test
    public void testInvocationMatcher_MultipleMatchers() {
        Object mock = Mockito.mock(Object.class);
        Invocation invocation = Mockito.verify(mock).methodWithTwoArguments(ArgumentMatchers.eq(1L), ArgumentMatchers.eq("test"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Matcher matcher1 = ArgumentMatchers.eq(1L);
        Matcher matcher2 = ArgumentMatchers.eq("test");
        List<Matcher> matchers = Arrays.asList(matcher1, matcher2);
        InvocationMatcher expectedMatcher = new InvocationMatcher(invocation, matchers);

        MatcherAssert.assertThat(invocationMatcher, Matchers.is(expectedMatcher));
        MatcherAssert.assertThat(invocationMatcher.getMatchers(), Matchers.containsInAnyOrder(matcher1, matcher2));
    }

    @Test
    public void testInvocationMatcher_EmptyMatchers() {
        Object mock = Mockito.mock(Object.class);
        Invocation invocation = Mockito.verify(mock).methodWithNoArguments();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        InvocationMatcher expectedMatcher = new InvocationMatcher(invocation, Collections.emptyList());

        MatcherAssert.assertThat(invocationMatcher, Matchers.is(expectedMatcher));
        MatcherAssert.assertThat(invocationMatcher.getMatchers(), Matchers.empty());
    }

    @Test
    public void testInvocationMatcher_MethodName() {
        Object mock = Mockito.mock(Object.class);
        Invocation invocation1 = Mockito.verify(mock).methodWithOneArgument(ArgumentMatchers.eq(1L));
        Invocation invocation2 = Mockito.verify(mock).anotherMethodWithOneArgument(ArgumentMatchers.eq(1L));

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        MatcherAssert.assertThat(invocationMatcher1, Matchers.not(invocationMatcher2));
    }

    @Test
    public void testInvocationMatcher_SameMethod() {
        Object mock = Mockito.mock(Object.class);
        Invocation invocation1 = Mockito.verify(mock).methodWithOneArgument(ArgumentMatchers.eq(1L));
        Invocation invocation2 = Mockito.verify(mock).methodWithOneArgument(ArgumentMatchers.eq(1L));

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        MatcherAssert.assertThat(invocationMatcher1, Matchers.is(invocationMatcher2));
    }

    @Test
    public void testInvocationMatcher_SameMethodWithDifferentArguments() {
        Object mock = Mockito.mock(Object.class);
        Invocation invocation1 = Mockito.verify(mock).methodWithOneArgument(ArgumentMatchers.eq(1L));
        Invocation invocation2 = Mockito.verify(mock).methodWithOneArgument(ArgumentMatchers.eq(2L));

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        MatcherAssert.assertThat(invocationMatcher1, Matchers.not(invocationMatcher2));
    }

    @Test
    public void testInvocationMatcher_SameMethodWithSameArguments() {
        Object mock = Mockito.mock(Object.class);
        Invocation invocation1 = Mockito.verify(mock).methodWithOneArgument(ArgumentMatchers.eq(1L));
        Invocation invocation2 = Mockito.verify(mock).methodWithOneArgument(ArgumentMatchers.eq(1L));

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        MatcherAssert.assertThat(invocationMatcher1, Matchers.is(invocationMatcher2));
    }

    @Test
    public void testInvocationMatcher_Varargs() {
        Object mock = Mockito.mock(Object.class);
        Invocation invocation = Mockito.verify(mock).methodWithVarargs(ArgumentMatchers.any(String[].class));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.is("test"));
        InvocationMatcher expectedMatcher = new InvocationMatcher(invocation, matchers);

        invocationMatcher.captureArgumentsFrom(invocation);
        MatcherAssert.assertThat(invocationMatcher.getMatchers(), Matchers.contains(matchers.get(0)));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testInvocationMatcher_Varargs_CaptureFrom() {
        Object mock = Mockito.mock(Object.class);
        Invocation invocation = Mockito.verify(mock).methodWithVarargs(ArgumentMatchers.any(String[].class));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        invocationMatcher.captureArgumentsFrom(invocation);

        // UnsupportedOperationException is thrown because capturing from varargs is not supported
    }
}
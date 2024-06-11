package org.mockito.internal.invocation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.invocation.InvocationOnMockImpl;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.Method;
import java.util.List;
import java.util.LinkedList;

import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.isInstanceOf;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherConstructionWithInvocationAndEmptyMatchers() {
        // given
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // then
        assertThat(invocationMatcher, isA(InvocationMatcher.class));
        assertThat(invocationMatcher.getMatchers(), is(Collections.emptyList()));
    }

    @Test
    public void testInvocationMatcherConstructionWithInvocationAndMatchers() {
        // given
        Invocation invocation = new InvocationOnMockImpl(Mockito.mock(Object.class), new Method(null, new Class[0]), new Object[0], new Location(null, 0, 0));
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(ArgumentMatchers.eq("foo"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // then
        assertThat(invocationMatcher, isA(InvocationMatcher.class));
        assertThat(invocationMatcher.getMatchers(), is(matchers));
    }

    @Test
    public void testInvocationMatcherGetInvocation() {
        // given
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // when
        Invocation result = invocationMatcher.getInvocation();

        // then
        assertThat(result, is(invocation));
    }

    @Test
    public void testInvocationMatcherGetMethod() {
        // given
        Invocation invocation = new InvocationOnMockImpl(Mockito.mock(Object.class), new Method(null, new Class[0]), new Object[0], new Location(null, 0, 0));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // when
        Method result = invocationMatcher.getMethod();

        // then
        assertThat(result, is(invocation.getMethod()));
    }

    @Test
    public void testInvocationMatcherMatches() {
        // given
        Object mock = Mockito.mock(Object.class);
        Invocation invocation = new InvocationOnMockImpl(mock, new Method(null, new Class[0]), new Object[0], new Location(null, 0, 0));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation testInvocation = new InvocationOnMockImpl(mock, invocation.getMethod(), new Object[0], new Location(null, 0, 0));

        // when
        boolean result = invocationMatcher.matches(testInvocation);

        // then
        assertThat(result, is(true));
    }

    @Test
    public void testInvocationMatcherDoesNotMatchDifferentMock() {
        // given
        Object mock1 = Mockito.mock(Object.class);
        Object mock2 = Mockito.mock(Object.class);
        Invocation invocation1 = new InvocationOnMockImpl(mock1, new Method(null, new Class[0]), new Object[0], new Location(null, 0, 0));
        Invocation invocation2 = new InvocationOnMockImpl(mock2, new Method(null, new Class[0]), new Object[0], new Location(null, 0, 0));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        // when
        boolean result = invocationMatcher.matches(invocation2);

        // then
        assertThat(result, is(false));
    }

    @Test
    public void testInvocationMatcherDoesNotMatchDifferentMethod() {
        // given
        Object mock = Mockito.mock(Object.class);
        Invocation invocation1 = new InvocationOnMockImpl(mock, new Method(null, new Class[0]), new Object[0], new Location(null, 0, 0));
        Invocation invocation2 = new InvocationOnMockImpl(mock, new Method(null, new Class[1]), new Object[0], new Location(null, 0, 0));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        // when
        boolean result = invocationMatcher.matches(invocation2);

        // then
        assertThat(result, is(false));
    }

    @Test
    public void testInvocationMatcherDoesNotMatchUnverifiedInvocation() {
        // given
        Object mock = Mockito.mock(Object.class);
        Invocation invocation = new InvocationOnMockImpl(mock, new Method(null, new Class[0]), new Object[0], new Location(null, 0, 0));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation testInvocation = new InvocationOnMockImpl(Mockito.mock(Object.class), invocation.getMethod(), new Object[0], new Location(null, 0, 0));

        // when (invocation is verified)
        testInvocation.markVerified();
        boolean result = invocationMatcher.matches(testInvocation);

        // then
        assertThat(result, is(true));

        // when (invocation is not verified)
        testInvocation.reset();
        result = invocationMatcher.matches(testInvocation);

        // then
        assertThat(result, is(false));
    }

    @Test
    public void testInvocationMatcherWithVarargsAndVarargMatcher() {
        // given
        Object mock = Mockito.mock(Object.class);
        Method method = new Method(null, new Class[]{String[].class});
        Invocation invocation = new InvocationOnMockImpl(mock, method, new Object[]{new String[]{"foo"}}, new Location(null, 0, 0));
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(ArgumentMatchers.arrayContaining(ArgumentMatchers.containsString("bar")));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // when
        boolean result = invocationMatcher.matches(invocation);

        // then
        assertThat(result, is(true));
    }

    @Test
    public void testInvocationMatcherWithVarargsAndNonVarargMatcher() {
        // given
        Object mock = Mockito.mock(Object.class);
        Method method = new Method(null, new Class[]{String[].class, String.class});
        Invocation invocation = new InvocationOnMockImpl(mock, method, new Object[]{new String[]{"foo"}, "bar"}, new Location(null, 0, 0));
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(ArgumentMatchers.arrayContaining(ArgumentMatchers.containsString("foo")));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // when
        boolean result = invocationMatcher.matches(invocation);

        // then
        assertThat(result, is(true));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCaptureArgumentsFromInvocationWithVarargsThrowsException() {
        // given
        Object mock = Mockito.mock(Object.class);
        Method method = new Method(null, new Class[]{String[].class});
        Invocation invocation = new InvocationOnMockImpl(mock, method, new Object[]{new String[]{"foo"}}, new Location(null, 0, 0));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // when & then
        invocationMatcher.captureArgumentsFrom(invocation);
    }
}
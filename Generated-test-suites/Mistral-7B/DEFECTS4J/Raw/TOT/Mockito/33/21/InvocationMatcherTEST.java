package org.mockito.internal.invocation;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class InvocationMatcherTest {

    @Test
    public void testMatcherWithNoArguments() {
        // given
        Invocation invocation = new Invocation(new Object() {}.getClass(), new Method(new Object() {}.getClass(), new Class[0]));
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        // when
        InvocationMatcher actual = new InvocationMatcher(Mockito.mock(Invocation.class));

        // then
        assertThat(matcher, isA(InvocationMatcher.class));
        assertThat(matcher.matches(actual), is(true));
    }

    @Test
    public void testMatcherWithArguments() {
        // given
        Object argument = new Object();
        Invocation invocation = new Invocation(new Object() {}.getClass(), new Method(new Object() {}.getClass(), new Class[] {Object.class}));
        InvocationMatcher matcher = new InvocationMatcher(invocation, Arrays.asList(Mockito.argThat(arg -> arg.equals(argument))));

        // when
        InvocationMatcher actual = new InvocationMatcher(Mockito.mock(Invocation.class), Arrays.asList(argument));

        // then
        assertThat(matcher, isA(InvocationMatcher.class));
        assertThat(matcher.matches(actual), is(true));
    }

    @Test
    public void testMatcherWithDifferentMock() {
        // given
        Invocation invocation1 = new Invocation(new Object() {}.getClass(), new Method(new Object() {}.getClass(), new Class[0]));
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);

        Invocation invocation2 = new Invocation(new Object() {}.getClass(), new Method(new Object() {}.getClass(), new Class[0]));
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        // when
        InvocationMatcher actual = new InvocationMatcher(Mockito.mock(Invocation.class));

        // then
        assertThat(matcher1, isA(InvocationMatcher.class));
        assertThat(matcher1.matches(matcher2), is(false));
        assertThat(matcher1.matches(actual), is(false));
        assertThat(matcher2.matches(actual), is(false));
    }

    @Test
    public void testMatcherWithDifferentMethods() {
        // given
        Invocation invocation1 = new Invocation(new Object() {}.getClass(), new Method(new Object() {}.getClass(), new Class[] {Object.class}));
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);

        Invocation invocation2 = new Invocation(new Object() {}.getClass(), new Method(new Object() {}.getClass(), new Class[] {int.class}));
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        // when
        InvocationMatcher actual = new InvocationMatcher(Mockito.mock(Invocation.class));

        // then
        assertThat(matcher1, isA(InvocationMatcher.class));
        assertThat(matcher1.matches(matcher2), is(false));
        assertThat(matcher1.matches(actual), is(false));
        assertThat(matcher2.matches(actual), is(false));
    }

    @Test
    public void testMatcherWithArgumentsThatDontMatch() {
        // given
        Object argument1 = new Object();
        Invocation invocation = new Invocation(new Object() {}.getClass(), new Method(new Object() {}.getClass(), new Class[] {Object.class}));
        InvocationMatcher matcher = new InvocationMatcher(invocation, Arrays.asList(Mockito.argThat(arg -> arg.equals(argument1))));

        Object argument2 = new Object();
        InvocationMatcher actual = new InvocationMatcher(Mockito.mock(Invocation.class), Arrays.asList(argument2));

        // when
        // then
        assertThat(matcher.matches(actual), is(false));
    }

    @Test
    public void testCaptureArguments() {
        // given
        Object argument = new Object();
        Invocation invocation = new Invocation(new Object() {}.getClass(), new Method(new Object() {}.getClass(), new Class[] {Object.class}));
        InvocationMatcher matcher = new InvocationMatcher(invocation, Arrays.asList(Mockito.argThat(arg -> arg.equals(argument))));

        // when
        Invocation actual = Mockito.mock(Invocation.class);
        matcher.captureArgumentsFrom(actual);

        // then
        ArgumentCaptor<Object> argumentCaptor = ArgumentCaptor.forClass(Object.class);
        verify(actual).someMethod(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue(), is(argument));
    }

    @Test
    public void testCreateFrom() {
        // given
        List<Invocation> invocations = Arrays.asList(
                new Invocation(new Object() {}.getClass(), new Method(new Object() {}.getClass(), new Class[0])),
                new Invocation(new Object() {}.getClass(), new Method(new Object() {}.getClass(), new Class[] {Object.class})));

        // when
        List<InvocationMatcher> matchers = InvocationMatcher.createFrom(invocations);

        // then
        assertThat(matchers.size(), is(2));
    }
}
package org.mockito.internal.invocation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

@RunWith(org.mockito.runners.JUnit4.class)
public class InvocationMatcherTest {

    @Test
    public void testMatchesInvocationWithSameMethodNameAndSameArguments() {
        // given
        Object mock = mock(String.class);
        Method method = String.class.getMethod("length");
        Invocation invocation = new Invocation(mock, method, new Object[]{null});
        List<Matcher> matchers = Arrays.asList(is(10));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // when
        Invocation actual = new InvocationOnMock(mock, method, new Object[]{null});

        // then
        assertThat(invocationMatcher.matches(actual), is(true));
    }

    @Test
    public void testMatchesInvocationWithSameMethodNameButDifferentArguments() {
        // given
        Object mock = mock(String.class);
        Method method = String.class.getMethod("length");
        Invocation invocation = new Invocation(mock, method, new Object[]{null});
        List<Matcher> matchers = Arrays.asList(is("hello"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // when
        Invocation actual = new InvocationOnMock(mock, method, new Object[]{new String("world")});

        // then
        assertThat(invocationMatcher.matches(actual), is(false));
    }

    @Test
    public void testMatchesInvocationWithSameMethodNameAndDifferentMocks() {
        // given
        Object mock1 = mock(String.class);
        Object mock2 = mock(String.class);
        Method method = String.class.getMethod("length");
        Invocation invocation1 = new Invocation(mock1, method, new Object[]{null});
        Invocation invocation2 = new Invocation(mock2, method, new Object[]{null});
        List<Matcher> matchers = Arrays.asList(is(10));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, matchers);

        // when
        Invocation actual = new InvocationOnMock(mock2, method, new Object[]{null});

        // then
        assertThat(invocationMatcher1.matches(actual), is(false));
    }

    @Test
    public void testMatchesInvocationWithDifferentMethodNames() {
        // given
        Object mock = mock(String.class);
        Method method1 = String.class.getMethod("length");
        Method method2 = String.class.getMethod("charAt", int.class);
        Invocation invocation = new Invocation(mock, method1, new Object[]{null});
        List<Matcher> matchers = Arrays.asList(is("mockito"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // when
        Invocation actual = new InvocationOnMock(mock, method2, new Object[]{0});

        // then
        assertThat(invocationMatcher.matches(actual), is(false));
    }

    @Test(expected = MockitoException.class)
    public void testMatchesInvocationWithDifferentNumberOfArguments() {
        // given
        Object mock = mock(String.class);
        Method method = String.class.getMethod("length");
        Invocation invocation = new Invocation(mock, method, new Object[]{null});
        List<Matcher> matchers = Arrays.asList(is("mockito"), is(10));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // when
        Invocation actual = new InvocationOnMock(mock, method, new Object[]{null, new Object(){}});

        // then
        invocationMatcher.matches(actual);
    }

    @Test
    public void testMatchesInvocationsWithCustomArgumentMatcher() {
        // given
        Object mock = mock(String.class);
        Method method = String.class.getMethod("length");
        Invocation invocation = new Invocation(mock, method, new Object[]{null});
        List<Matcher> matchers = Arrays.asList(new ArgumentsMatcher("10"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // when
        Invocation actual = new InvocationOnMock(mock, method, new Object[]{null});

        // then
        invocationMatcher.matches(actual);
    }

    @Test
    public void testCreateFromInvocations() {
        // given
        Object mock = mock(String.class);
        List<Invocation> invocations = Arrays.asList(new Invocation(mock, String.class.getMethod("length"), new Object[]{null}),
                new Invocation(mock, String.class.getMethod("charAt", int.class), new Object[]{0}));

        // when
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        // then
        assertThat(invocationMatchers.size(), is(2));
        InvocationMatcher firstMatcher = invocationMatchers.get(0);
        InvocationMatcher secondMatcher = invocationMatchers.get(1);

        Invocation invocation1 = new InvocationOnMock(mock, String.class.getMethod("length"), new Object[]{null});
        Invocation invocation2 = new InvocationOnMock(mock, String.class.getMethod("charAt", int.class), new Object[]{0});

        // when & then for first matcher
        firstMatcher.matches(invocation1);
        assertThat(firstMatcher.getMatchers().size(), is(0));
        assertThat(firstMatcher.getMatchers(), not(sameInstance(invocation1.getArgumentsToMatchers())));

        // when & then for second matcher
        secondMatcher.matches(invocation2);
        List<Matcher> matchers2 = secondMatcher.getMatchers();
        assertThat(matchers2.size(), is(1));
        assertThat(matchers2.get(0), is(ArgumentsMatcher.matches(0)));
    }

    static class ArgumentsMatcher implements Matcher<Object[]> {
        private final String argument;

        ArgumentsMatcher(String argument) {
            this.argument = argument;
        }

        @Override
        public boolean matches(Object[] actual) {
            return Arrays.deepEquals(actual, new Object[]{argument});
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("argument is ").appendValue(argument);
        }

        @Override
        public void describeMismatch(Object item, Description description) {
            description.appendText("expected ").appendValue(argument).appendText(" but was ").appendValue(item);
        }
    }
}

class Invocation {
    private final Object mock;
    private final Method method;
    private final Object[] arguments;

    public Invocation(Object mock, Method method, Object[] arguments) {
        this.mock = mock;
        this.method = method;
        this.arguments = arguments;
    }

    public Object getMock() {
        return mock;
    }

    public Method getMethod() {
        return method;
    }

    public Object[] getArguments() {
        return arguments;
    }

    public List<Matcher> argumentsToMatchers() {
        return Arrays.asList(new CapturesArguments(this));
    }

    public String toString(List<Matcher> matchers, PrintSettings printSettings) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(toString());
        stringBuilder.append("; ");
        for (Matcher matcher : matchers) {
            stringBuilder.append(matcher.toString());
        }
        return stringBuilder.toString();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("invocation: ");
        stringBuilder.append(getMock().getClass().getSimpleName());
        stringBuilder.append(".method(");
        stringBuilder.append(getMethod().getName());
        stringBuilder.append(", ");
        for (int i = 0; i < getArguments().length; i++) {
            stringBuilder.append(getArguments()[i].toString());
            if (i < getArguments().length - 1) {
                stringBuilder.append(", ");
            }
        }
        stringBuilder.append(") ");
        return stringBuilder.toString();
    }

    public boolean isVerified() {
        return false;
    }

    public Location getLocation() {
        return null;
    }
}

class CapturesArguments implements Matcher<Object> {
    private final Invocation invocation;
    private Object capturedArgument;

    public CapturesArguments(Invocation invocation) {
        this.invocation = invocation;
    }

    @Override
    public boolean matches(Object argument) {
        capturedArgument = argument;
        invocation.captureArgumentsFrom(new InvocationOnMock(invocation.getMock(), invocation.getMethod(), new Object[]{argument}));
        return true;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("captured argument");
    }

    public Object getCapturedArgument() {
        return capturedArgument;
    }
}
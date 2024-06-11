package org.mockito.internal.invocation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherWithNoMatchers() {
        Object mock = mock(Object.class);
        Invocation invocation = new InvocationStub(mock, "someMethod", new Object[0]);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation otherInvocation = new InvocationStub(mock, "someOtherMethod", new Object[0]);

        assertThat(invocationMatcher.matches(otherInvocation), is(false));
    }

    @Test
    public void testInvocationMatcherWithMatchers() {
        Object mock = mock(Object.class);
        Invocation invocation = new InvocationStub(mock, "someMethod", "arg1", "arg2");
        List<Matcher> matchers = Arrays.asList(
                is("arg1"),
                is("arg2")
        );
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Invocation otherInvocation = new InvocationStub(mock, "someMethod", "arg1_1", "arg2_1");
        ArgumentCaptor<String> arg1Captor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> arg2Captor = ArgumentCaptor.forClass(String.class);

        mock.invokeMethod("someMethod", "arg1", "arg2");
        verify(mock).invokeMethod("someMethod", arg1Captor.capture(), arg2Captor.capture());

        assertThat(invocationMatcher.matches(otherInvocation), is(true));
        assertThat(arg1Captor.getValue(), is("arg1"));
        assertThat(arg2Captor.getValue(), is("arg2"));
    }

    @Test
    public void testInvocationMatcherWithMatchersDifferentOrder() {
        Object mock = mock(Object.class);
        Invocation invocation = new InvocationStub(mock, "someMethod", "arg1", "arg2");
        List<Matcher> matchers = Arrays.asList(
                is("arg2"),
                is("arg1")
        );
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Invocation otherInvocation = new InvocationStub(mock, "someMethod", "arg1_1", "arg2_1");

        assertThat(invocationMatcher.matches(otherInvocation), is(false));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testInvocationMatcherWithVarargs() {
        Object mock = mock(Object.class);
        Invocation invocation = new InvocationStub(mock, "someMethod", new Object[0], new Object[]{1, 2, 3});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        invocationMatcher.captureArgumentsFrom(invocation);
    }

    private static class InvocationStub implements InvocationOnMock {
        private final Object mock;
        private final String methodName;
        private final Object[] arguments;

        public InvocationStub(Object mock, String methodName, Object... arguments) {
            this.mock = mock;
            this.methodName = methodName;
            this.arguments = arguments;
        }

        @Override
        public Object getMock() {
            return mock;
        }

        @Override
        public String getMethodName() {
            return methodName;
        }

        @Override
        public Object[] getArguments() {
            return arguments;
        }

        @Override
        public Object getArgumentAt(int index, Class type) {
            return Arrays.stream(arguments).filter(arg -> arg.getClass().isAssignableFrom(type)).findFirst().orElse(null);
        }

        @Override
        public int getRawArgumentSize() {
            return arguments.length;
        }

        @Override
        public Object getRawArgument(int index) {
            return arguments[index];
        }
    }
}
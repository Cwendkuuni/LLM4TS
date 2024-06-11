import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    private Object mock;

    @Before
    public void setup() {
        mock = Mockito.mock(Object.class);
    }

    @Test
    public void testInvocationMatcher_SameMethodAndArguments() {
        Object arg1 = "arg1";
        Object arg2 = 1L;

        Invocation invocation1 = new MockitoInvocation(mock, new MethodTestClass(), new Object[]{"method1", arg1, arg2});
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);

        Object arg1_1 = "arg1_1";
        Object arg2_1 = 1L;

        Invocation invocation2 = new MockitoInvocation(mock, new MethodTestClass(), new Object[]{"method1", arg1_1, arg2_1});
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        List<Matcher> matchers = Arrays.asList(eq(arg1), eq(arg2));
        InvocationMatcher expectedMatcher = new InvocationMatcher(invocation1, matchers);

        assertThat(invocationMatcher1, is(expectedMatcher));
        assertThat(invocationMatcher1, is(invocationMatcher2));

        verify(mock).method1(arg1, arg2);
        verifyNoMoreInteractions(mock);
    }

    @Test
    public void testInvocationMatcher_SameMethodButDifferentArguments() {
        Object arg1 = "arg1";
        Object arg2 = 1L;

        Invocation invocation1 = new MockitoInvocation(mock, new MethodTestClass(), new Object[]{"method1", arg1, arg2});
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);

        Object arg1_1 = "arg1_1";
        Object arg2_1 = 2L;

        Invocation invocation2 = new MockitoInvocation(mock, new MethodTestClass(), new Object[]{"method1", arg1_1, arg2_1});
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        List<Matcher> matchers = Arrays.asList(eq(arg1), eq(arg2));
        InvocationMatcher expectedMatcher = new InvocationMatcher(invocation1, matchers);

        assertThat(invocationMatcher1, is(expectedMatcher));
        assertThat(invocationMatcher1, isNot(invocationMatcher2));

        verify(mock).method1(arg1, arg2);
        verifyNoMoreInteractions(mock);

        verifyZeroInteractions(mock);
    }

    @Test
    public void testInvocationMatcher_DifferentMethods() {
        Object arg1 = "arg1";
        Object arg2 = 1L;

        Invocation invocation1 = new MockitoInvocation(mock, new MethodTestClass(), "method1", arg1, arg2);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);

        Object arg3 = "arg3";
        Object arg4 = 2L;

        Invocation invocation2 = new MockitoInvocation(mock, new MethodTestClass(), "method2", arg3, arg4);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        List<Matcher> matchers = Arrays.asList(eq(arg1), eq(arg2));
        InvocationMatcher expectedMatcher = new InvocationMatcher(invocation1, matchers);

        assertThat(invocationMatcher1, isNot(invocationMatcher2));

        verifyZeroInteractions(mock);
    }

    @Test
    public void testInvocationMatcher_Varargs() {
        Object arg1 = "arg1";
        Object[] vararg = new Object[]{"arg2", "arg3"};

        Invocation invocation1 = new MockitoInvocation(mock, new MethodTestClass(), "methodWithVarargs", arg1, vararg);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);

        Object arg1_1 = "arg1_1";
        Object[] vararg_1 = new Object[]{"arg2_1", "arg3_1"};

        Invocation invocation2 = new MockitionInvocation(mock, new MethodTestClass(), "methodWithVarargs", arg1_1, vararg_1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        List<Matcher> matchers = Arrays.asList(eq(arg1), Arguments.notNull(vararg));
        InvocationMatcher expectedMatcher = new InvocationMatcher(invocation1, matchers);

        ArgumentCaptor<Object[]> varargCaptor = ArgumentCaptor.forClass(Object[].class);

        verify(mock).methodWithVarargs(arg1, varargCaptor.capture());
        Object[] capturedVararg = varargCaptor.getValue();

        assertThat(invocationMatcher1, is(expectedMatcher));
        assertThat(invocationMatcher1, is(invocationMatcher2));
        assertThat(capturedVararg, is(vararg_1));
    }

    private static class MethodTestClass {
        public void method1(String arg1, Long arg2) {
            // implementation
        }

        public void method1(String arg1, Long arg2, Long arg3) {
            // implementation
        }

        public void methodWithVarargs(String arg1, Object... vararg) {
            // implementation
        }
    }

    private static class MockitoInvocation implements Invocation {
        private final Object mock;
        private final Method method;
        private final Object[] arguments;
        private final Object[] rawArguments;

        private MockitoInvocation(Object mock, Class<?> clazz, String methodName, Object... arguments) throws NoSuchMethodException {
            this.mock = mock;
            this.method = clazz.getMethod(methodName, getTypes(arguments));
            this.arguments = arguments;
            this.rawArguments = arguments;
        }

        private MockitoInvocation(Object mock, Class<?> clazz, Method method, Object... arguments) {
            this.mock = mock;
            this.method = method;
            this.arguments = arguments;
            this.rawArguments = arguments;
        }

        @Override
        public Object[] getArguments() {
            return arguments;
        }

        @Override
        public Object[] getRawArguments() {
            return rawArguments;
        }

        @Override
        public Method getMethod() {
            return method;
        }

        @Override
        public Object getMock() {
            return mock;
        }

        @Override
        public Location getLocation() {
            return new Location();
        }

        @Override
        public boolean isVerified() {
            return false;
        }

        @Override
        public void verify() {
            // implementation
        }

        @Override
        public void verifyNoMoreInteractions() {
            // implementation
        }

        @Override
        public void verifyZeroInteractions() {
            // implementation
        }

        @Override
        public void invoke() throws Throwable {
            // implementation
        }

        @Override
        public Object getArgumentAt(int position, Class<?> type) {
            return arguments[position];
        }

        private static Class<?>[] getTypes(Object[] arguments) {
            Class<?>[] types = new Class[arguments.length];
            for (int i = 0; i < arguments.length; i++) {
                types[i] = arguments[i].getClass();
            }
            return types;
        }
    }

    private static class AnswerWithReturnValue implements Answer<Object> {
        private final Object returnValue;

        AnswerWithReturnValue(Object returnValue) {
            this.returnValue = returnValue;
        }

        @Override
        public Object answer(InvocationOnMock invocation) throws Throwable {
            return returnValue;
        }
    }
}
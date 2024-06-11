package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.internal.matchers.ArgumentMatchers.any;
import static org.mockito.internal.matchers.ArgumentMatchers.argThat;
import static org.mockito.internal.matchers.ArgumentsMatcher.isVariableArgument;
import static org.mockito.internal.matchers.ArgumentsMatcher.isVarargMatcher;

import java.lang.reflect.Method;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;

public class InvocationMatcherTest {

    @Test
    public void testHasSimilarMethod() {
        Object mock = mock(Object.class);
        Method method1 = mockMethod("void test(String arg1, int arg2)");
        Method method2 = mockMethod("void test(int arg1, String arg2)");
        Invocation invocation1 = new Invocation(mock, method1, new Object[]{new String("test"), 1}, new Object[0], 0, null);
        Invocation invocation2 = new Invocation(mock, method2, new Object[]{1, new String("test")}, new Object[0], 0, null);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        assertTrue(invocationMatcher1.hasSimilarMethod(invocationMatcher2));

        // method names are different
        method1 = mockMethod("void test2(String arg1, int arg2)");
        invocationMatcher1 = new InvocationMatcher(invocation1);
        invocationMatcher2 = new InvocationMatcher(new Invocation(mock, method2, new Object[]{1, new String("test")}, new Object[0], 0, null));
        assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher2));

        // mocks are different
        Object mock2 = mock(Object.class);
        invocation2 = new Invocation(mock2, method1, new Object[]{new String("test"), 1}, new Object[0], 0, null);
        invocationMatcher1 = new InvocationMatcher(invocation1);
        invocationMatcher2 = new InvocationMatcher(invocation2);
        assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher2));

        // arguments are different
        Object[] args1 = new Object[]{new String("test1"), 2};
        Object[] args2 = new Object[]{new String("test2"), 3};
        invocationMatcher1 = new InvocationMatcher(new Invocation(mock, method1, args1, new Object[0], 0, null));
        invocationMatcher2 = new InvocationMatcher(new Invocation(mock, method1, args2, new Object[0], 0, null));
        assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testHasSameMethod() {
        Object mock = mock(Object.class);
        Method method = mockMethod("void test(String arg1, int arg2)");
        Invocation invocation1 = new Invocation(mock, method, new Object[]{new String("test"), 1}, new Object[0], 0, null);
        Invocation invocation2 = new Invocation(mock, method, new Object[]{new String("test2"), 2}, new Object[0], 0, null);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        assertFalse(invocationMatcher1.hasSameMethod(invocationMatcher2));

        // same method and same mock
        invocation2 = new Invocation(mock, method, new Object[]{new String("test"), 1}, new Object[0], 0, null);
        invocationMatcher2 = new InvocationMatcher(invocation2);
        assertTrue(invocationMatcher1.hasSameMethod(invocationMatcher2));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Object mock = mock(Object.class);
        Method method = mockMethod("void test(String arg1, int arg2)");
        Invocation invocation = new Invocation(mock, method, new Object[]{new String("test"), 1}, new Object[0], 0, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        CapturesArguments captureArguments1 = mock(CapturesArguments.class);
        CapturesArguments captureArguments2 = mock(CapturesArguments.class);

        invocationMatcher.captureArgumentsFrom(invocation);

        assertTrue(captureArguments1.size() > 0);
        assertTrue(captureArguments1.get(0) instanceof String);
        assertTrue(captureArguments1.get(1) instanceof Integer);

        captureArguments1.clear();

        invocationMatcher = new InvocationMatcher(invocation);
        invocationMatcher.captureArgumentsFrom(invocation);

        assertTrue(captureArguments2.size() > 0);
        assertTrue(captureArguments2.get(0) instanceof String);
        assertTrue(captureArguments2.get(1) instanceof Integer);

        // test varargs
        Method methodWithVarargs = mockMethod("void test(String... args)");
        invocation = new Invocation(mock, methodWithVarargs, new Object[]{"test1", "test2"}, new Object[0], 0, null);
        InvocationMatcher invocationMatcherWithVarargs = new InvocationMatcher(invocation);

        captureArguments1 = mock(CapturesArguments.class);
        captureArguments2 = mock(CapturesArguments.class);

        invocationMatcherWithVarargs.captureArgumentsFrom(invocation);

        assertTrue(captureArguments1.size() > 0);
        assertTrue(captureArguments1.get(0) instanceof String[]);
        assertTrue(Arrays.equals(new String[]{"test1", "test2"}, captureArguments1.get(0)));

        captureArguments1.clear();

        assertTrue(captureArguments2.size() > 0);
        assertTrue(captureArguments2.get(0) instanceof String[]);
        assertTrue(Arrays.equals(new String[]{"test1", "test2"}, captureArguments2.get(0)));
    }

    @Test
    public void testIsVariableArgument() {
        Object mock = mock(Object.class);
        Method method = mockMethod("void test(String... args)");
        Invocation invocation = new Invocation(mock, method, new Object[]{"test1", "test2"}, new Object[0], 0, null);

        assertTrue(InvocationMatcher.isVariableArgument(invocation, 0));
        assertFalse(InvocationMatcher.isVariableArgument(invocation, 1));
    }

    @Test
    public void testIsVarargMatcher() {
        Matcher<Object> matcher = argThat(isVariableArgument());
        assertTrue(InvocationMatcher.isVarargMatcher(matcher));
    }

    private Method mockMethod(String methodName) {
        try {
            return Object.class.getMethod(methodName);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
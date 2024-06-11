package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.ArgumentsMatcher.argument;
import static org.mockito.internal.matchers.ArgumentsMatcher.arguments;
import static org.mockito.internal.matchers.ArgumentsMatcher.isNull;
import static org.mockito.internal.matchers.ArgumentsMatcher.same;
import static org.mockito.internal.matchers.ArgumentsMatcher.type;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.mockito.exceptions.verification.InvocationsNotMatchedException;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.stubs.MockFunction;
import org.mockito.stubs.MockObject;

public class InvocationMatcherTest {

    private static class TestInvocationMock implements Invocation {
        private final Object[] arguments;
        private final Method method;
        private final Location location;

        TestInvocationMock(Method method, Object[] arguments, Location location) {
            this.method = method;
            this.arguments = arguments;
            this.location = location;
        }

        @Override
        public Method getMethod() {
            return method;
        }

        @Override
        public Object[] getArguments() {
            return arguments;
        }

        @Override
        public Location getLocation() {
            return location;
        }

        @Override
        public boolean isVerified() {
            return false;
        }

        @Override
        public String toString() {
            return method.toString();
        }
    }

    @Test
    public void testConstructorWithMatchers() {
        Object mock = new MockObject();
        Method method = TestUtil.getMethod(MockFunction.class, "test", Integer.TYPE, Integer.TYPE);
        Object[] arguments = new Object[]{1, 2};
        Location location = new Location(1, 1, "TestClass.java");

        Invocation invocation = new TestInvocationMock(method, arguments, location);
        List<Matcher> matchers = Collections.singletonList(same(1));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertTrue(invocationMatcher.matches(invocation));
    }

    @Test
    public void testConstructorWithoutMatchers() {
        Object mock = new MockObject();
        Method method = TestUtil.getMethod(MockFunction.class, "test", Integer.TYPE, Integer.TYPE);
        Object[] arguments = new Object[]{1, 2};
        Location location = new Location(1, 1, "TestClass.java");

        Invocation invocation = new TestInvocationMock(method, arguments, location);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertTrue(invocationMatcher.matches(invocation));
    }

    @Test
    public void testHasSameMethod() {
        Object mock = new MockObject();
        Method method1 = TestUtil.getMethod(MockFunction.class, "test", Integer.TYPE, Integer.TYPE);
        Method method2 = TestUtil.getMethod(MockFunction.class, "test", Integer.TYPE, Integer.TYPE);

        Invocation invocation1 = new TestInvocationMock(method1, new Object[0], new Location(1, 1, "TestClass.java"));
        Invocation invocation2 = new TestInvocationMock(method2, new Object[0], new Location(1, 1, "TestClass.java"));

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        assertTrue(invocationMatcher1.hasSameMethod(invocationMatcher2));
    }

    @Test
    public void testDoesNotHaveSameMethod() {
        Object mock = new MockObject();
        Method method1 = TestUtil.getMethod(MockFunction.class, "test", Integer.TYPE, Integer.TYPE);
        Method method2 = TestUtil.getMethod(MockFunction.class, "anotherTest", Integer.TYPE, Integer.TYPE);

        Invocation invocation1 = new TestInvocationMock(method1, new Object[0], new Location(1, 1, "TestClass.java"));
        Invocation invocation2 = new TestInvocationMock(method2, new Object[0], new Location(1, 1, "TestClass.java"));

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        assertFalse(invocationMatcher1.hasSameMethod(invocationMatcher2));
    }

    @Test(expected = InvocationsNotMatchedException.class)
    public void testArgumentsDoNotMatch() {
        Object mock = new MockObject();
        Method method = TestUtil.getMethod(MockFunction.class, "test", Integer.TYPE, Integer.TYPE);
        Object[] argumentsMock = new Object[]{1, 2};
        Object[] argumentsActual = new Object[]{1, 3};
        Location location = new Location(1, 1, "TestClass.java");

        Invocation invocationMock = new TestInvocationMock(method, argumentsMock, location);
        Invocation invocationActual = new TestInvocationMock(method, argumentsActual, location);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocationMock);
        invocationMatcher.captureArgumentsFrom(invocationActual);

        List<Matcher> matchers = invocationMatcher.getMatchers();
        assertTrue(matchers.size() > 0);
        assertTrue(matchers.get(0) instanceof CapturesArguments);
        CapturesArguments captureArguments = (CapturesArguments) matchers.get(0);
        assertFalse(captureArguments.getValue().equals(argumentsActual[0]));
    }

    @Test
    public void testArgumentsMatch() {
        Object mock = new MockObject();
        Method method = TestUtil.getMethod(MockFunction.class, "test", Integer.TYPE, Integer.TYPE);
        Object[] argumentsMock = new Object[]{1, 2};
        Object[] argumentsActual = new Object[]{1, 2};
        Location location = new Location(1, 1, "TestClass.java");

        Invocation invocationMock = new TestInvocationMock(method, argumentsMock, location);
        Invocation invocationActual = new TestInvocationMock(method, argumentsActual, location);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocationMock);
        invocationMatcher.captureArgumentsFrom(invocationActual);

        List<Matcher> matchers = invocationMatcher.getMatchers();
        assertTrue(matchers.size() > 0);
        assertTrue(matchers.get(0) instanceof CapturesArguments);
        CapturesArguments captureArguments = (CapturesArguments) matchers.get(0);
        assertTrue(captureArguments.getValue().equals(argumentsActual[0]));
    }

    @Test
    public void testToString() {
        Object mock = new MockObject();
        Method method = TestUtil.getMethod(MockFunction.class, "test", Integer.TYPE, Integer.TYPE);
        Object[] arguments = new Object[]{1, 2};
        Location location = new Location(1, 1, "TestClass.java");

        Invocation invocation = new TestInvocationMock(method, arguments, location);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        String expected = "TestClass.test(int, int)";
        String actual = invocationMatcher.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testToStringWithPrintSettings() {
        Object mock = new MockObject();
        Method method = TestUtil.getMethod(MockFunction.class, "test", Integer.TYPE, Integer.TYPE);
        Object[] arguments = new Object[]{1, 2};
        Location location = new Location(1, 1, "TestClass.java");

        Invocation invocation = new TestInvocationMock(method, arguments, location);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        PrintSettings printSettings = new PrintSettings();
        printSettings.setPrintMethodNameOnly(true);
        printSettings.setPrintLocation(false);

        String expected = "test(int, int)";
        String actual = invocationMatcher.toString(printSettings);
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateFromInvocations() {
        Object mock = new MockObject();
        Method method = TestUtil.getMethod(MockFunction.class, "test", Integer.TYPE, Integer.TYPE);

        List<Invocation> invocations = new LinkedList<>();
        invocations.add(new TestInvocationMock(method, new Object[0], new Location(1, 1, "TestClass.java")));
        invocations.add(new TestInvocationMock(method, new Object[]{1, 2}, new Location(1, 1, "TestClass.java")));

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        InvocationMatcher invocationMatcher1 = invocationMatchers.get(0);
        InvocationMatcher invocationMatcher2 = invocationMatchers.get(1);

        assertTrue(invocationMatcher1.matches(invocationMatcher2.getInvocation()));
        assertFalse(invocationMatcher1.matches(invocationMatcher2));
    }
}

class TestUtil {
    @SuppressWarnings("unchecked")
    static Method getMethod(Class<?> clazz, String name, Class<?>... parameterTypes) {
        try {
            return clazz.getMethod(name, parameterTypes);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}

class PrintableInvocationStub implements PrintableInvocation {
    @Override
    public String toString() {
        return "PrintableInvocationStub";
    }
}

class PrintingFriendlyInvocationStub implements PrintingFriendlyInvocation {
    @Override
    public String toString(PrintSettings printSettings) {
        return "PrintingFriendlyInvocationStub";
    }
}

class CapturesArgumentsStub implements CapturesArgumensFromInvocation {
    private Object value;

    @Override
    public void captureFrom(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return this.value;
    }
}
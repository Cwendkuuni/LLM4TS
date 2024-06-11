package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.ArgumentsMatcher.argumentsMatcher;
import static org.mockito.internal.matchers.ArgumentsMatcher.contains;
import static org.mockito.internal.matchers.ArgumentsMatcher.eq;
import static org.mockito.internal.matchers.ArgumentsMatcher.nullValue;
import static org.mockito.internal.matchers.ArgumentsMatcher.same;
import static org.mockito.internal.matchers.ArgumentsMatcher.type;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.matchers.ArgumentsComparator;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.invocation.Invocation;

public class InvocationMatcherTest {

    @Test
    public void test_constructor_with_Invocation_and_empty_matchers() {
        Invocation invocation = createInvocationWithMethod(TestClass.class, "testMethodWithArgs");
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertEquals(invocation.getMethod(), matcher.getMethod());
        assertEquals(Collections.emptyList(), matcher.getMatchers());
    }

    @Test
    public void test_constructor_with_Invocation_and_non_empty_matchers() {
        Invocation invocation = createInvocationWithMethod(TestClass.class, "testMethodWithArgs");
        List<Matcher> matchers = Arrays.asList(eq(1), same(new Object()), contains(new Object[]{"foo", "bar"}));

        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        assertEquals(invocation.getMethod(), matcher.getMethod());
        assertEquals(matchers, matcher.getMatchers());
    }

    @Test
    public void test_matches_when_methods_are_same() {
        Invocation invocation1 = createInvocationWithMethod(TestClass.class, "testMethodWithArgs");
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        Invocation invocation2 = createInvocationWithMethod(TestClass.class, "testMethodWithArgs");

        assertTrue(matcher1.matches(invocation2));
    }

    @Test
    public void test_matches_when_methods_are_not_same() {
        Invocation invocation1 = createInvocationWithMethod(TestClass.class, "testMethodWithArgs1");
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        Invocation invocation2 = createInvocationWithMethod(TestClass.class, "testMethodWithArgs2");

        assertFalse(matcher1.matches(invocation2));
    }

    @Test
    public void test_matches_when_methods_are_same_but_arguments_are_not() {
        Invocation invocation1 = createInvocationWithArguments(new Object[]{1, "foo"}, TestClass.class, "testMethodWithArgs");
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        Invocation invocation2 = createInvocationWithArguments(new Object[]{2, "bar"}, TestClass.class, "testMethodWithArgs");

        assertFalse(matcher1.matches(invocation2));
    }

    @Test
    public void test_matches_when_methods_are_same_and_arguments_are_same() {
        Invocation invocation1 = createInvocationWithArguments(new Object[]{1, "foo"}, TestClass.class, "testMethodWithArgs");
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        Invocation invocation2 = createInvocationWithArguments(new Object[]{1, "foo"}, TestClass.class, "testMethodWithArgs");

        assertTrue(matcher1.matches(invocation2));
    }

    @Test(expected = MockitoAssertionError.class)
    public void test_matches_when_methods_are_not_same_but_arguments_are_same() {
        Invocation invocation1 = createInvocationWithMethod(TestClass.class, "testMethodWithArgs1");
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        Invocation invocation2 = createInvocationWithMethod(TestClass.class, "testMethodWithArgs2");
        Object[] arguments = new Object[]{new Object()};

        matcher1.matches(new Invocation(invocation1.getMock(), invocation1.getMethod(), arguments));
    }

    @Test
    public void test_hasSameMethod_when_methods_are_same() {
        Invocation invocation1 = createInvocationWithMethod(TestClass.class, "testMethodWithArgs");
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        Invocation invocation2 = createInvocationWithMethod(TestClass.class, "testMethodWithArgs");

        assertTrue(matcher1.hasSameMethod(new InvocationMatcher(invocation2)));
    }

    @Test
    public void test_hasSameMethod_when_methods_are_not_same() {
        Invocation invocation1 = createInvocationWithMethod(TestClass.class, "testMethodWithArgs1");
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        Invocation invocation2 = createInvocationWithMethod(TestClass.class, "testMethodWithArgs2");

        assertFalse(matcher1.hasSameMethod(new InvocationMatcher(invocation2)));
    }

    @Test
    public void test_matches_when_location_is_null() {
        Invocation invocation = createInvocationWithMethod(TestClass.class, "testMethodWithArgs");
        InvocationMatcher matcher = new InvocationMatcher(invocation, Collections.emptyList());

        assertTrue(matcher.matches(invocation));
    }

    @Test
    public void test_matches_when_locations_are_same() {
        Invocation invocation1 = createInvocationWithLocationAndMethod(new Location("TestClass.java:10"), TestClass.class, "testMethodWithArgs");
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        Invocation invocation2 = createInvocationWithLocationAndMethod(new Location("TestClass.java:10"), TestClass.class, "testMethodWithArgs");

        assertTrue(matcher1.matches(invocation2));
    }

    @Test
    public void test_matches_when_locations_are_not_same() {
        Invocation invocation1 = createInvocationWithLocationAndMethod(new Location("TestClass.java:10"), TestClass.class, "testMethodWithArgs");
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        Invocation invocation2 = createInvocationWithLocationAndMethod(new Location("TestClass.java:20"), TestClass.class, "testMethodWithArgs");

        assertFalse(matcher1.matches(invocation2));
    }

    @Test
    public void test_toString_when_no_matchers() {
        Invocation invocation = createInvocationWithMethod(TestClass.class, "testMethodWithArgs");
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertEquals("Invocation [method=testMethodWithArgs()V in TestClass]", matcher.toString());
    }

    @Test
    public void test_toString_when_matchers_are_present() {
        Invocation invocation = createInvocationWithArguments(new Object[]{1, "foo"}, TestClass.class, "testMethodWithArgs");
        List<Matcher> matchers = Arrays.asList(eq(1), same(new Object()), contains(new Object[]{"foo", "bar"}));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        assertEquals("Invocation [method=testMethodWithArgs(ILjava/lang/String;)[Ljava/lang/Object;] in TestClass\n"
                + "Arguments are:\n"
                + "1\n"
                + "foo\n"
                + "but are:\n"
                + "1\n"
                + "foo", matcher.toString());
    }

    @Test
    public void test_captureArgumentsFrom_when_matchers_are_present() {
        Invocation invocation = createInvocationWithArguments(new Object[]{1, "foo"}, TestClass.class, "testMethodWithArgs");
        List<Matcher> matchers = Arrays.asList(new CapturesArguments<Integer>() {
            @Override
            public void captureFrom(Object argument) {
                this.capturedValue = (Integer) argument;
            }
        }, new CapturesArguments<String>() {
            @Override
            public void captureFrom(Object argument) {
                this.capturedValue = (String) argument;
            }
        });
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        matcher.captureArgumentsFrom(invocation);

        assertEquals(1, ((CapturesArguments<Integer>) matchers.get(0)).getCapturedValue());
        assertEquals("foo", ((CapturesArguments<String>) matchers.get(1)).getCapturedValue());
    }

    private static Invocation createInvocationWithMethod(Class<?> clazz, String methodName) {
        try {
            Method method = clazz.getMethod(methodName);
            return new Invocation(new Object() {}, method, new Object[0]);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private static Invocation createInvocationWithArguments(Object[] arguments, Class<?> clazz, String methodName) {
        try {
            Method method = clazz.getMethod(methodName, arguments.getClass());
            return new Invocation(new Object() {}, method, arguments);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private static Invocation createInvocationWithLocationAndMethod(Location location, Class<?> clazz, String methodName) {
        try {
            Method method = clazz.getMethod(methodName);
            return new Invocation(new Object() {}, method, new Object[0], location);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private static class TestClass {
        public void testMethodWithArgs(int a, String b) {}
        public void testMethodWithArgs1(int a) {}
        public void testMethodWithArgs2(String b) {}
    }
}

class ArgumentsComparator {
    public boolean argumentsMatch(InvocationMatcher invocationMatcher1, InvocationMatcher invocationMatcher2) {
        return argumentsMatch(invocationMatcher1, invocationMatcher2.getArguments());
    }

    public boolean argumentsMatch(InvocationMatcher invocationMatcher1, Object[] arguments) {
        return argumentsMatch(invocationMatcher1.getMatchers(), arguments);
    }

    public boolean argumentsMatch(List<Matcher> matchers, Object[] arguments) {
        for (int i = 0; i < matchers.size(); i++) {
            Matcher matcher = matchers.get(i);
            if (!matcher.matches(arguments[i])) {
                return false;
            }
        }
        return true;
    }
}
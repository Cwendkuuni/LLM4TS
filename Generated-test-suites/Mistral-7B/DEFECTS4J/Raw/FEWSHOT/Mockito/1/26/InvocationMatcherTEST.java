package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.internal.matchers.ArgumentsMatcher.argThat;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

public class InvocationMatcherTest {

    @Test
    public void testHasSameMethod() {
        Object mock = mock(Object.class);
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method1 = mockMethod("void foo(int arg1, int arg2)");
        Method method2 = mockMethod("void foo(int arg1, String arg2)");
        Method method3 = mockMethod("void foo()");

        invocation1.setMethod(method1);
        invocation1.setMock(mock);
        invocation1.setLocation(Mockito.mock(Location.class));

        invocation2.setMethod(method2);
        invocation2.setMock(mock);
        invocation2.setLocation(Mockito.mock(Location.class));

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        assertTrue(invocationMatcher1.hasSameMethod(invocationMatcher1));
        assertTrue(invocationMatcher1.hasSameMethod(invocationMatcher2));
        assertFalse(invocationMatcher1.hasSameMethod(invocationMatcher3));
    }

    @Test
    public void testHasSimilarMethod() {
        Object mock = mock(Object.class);
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method1 = mockMethod("void foo(int arg1, int arg2)");
        Method method2 = mockMethod("void foo(String arg1, int arg2)");

        invocation1.setMethod(method1);
        invocation1.setMock(mock);
        invocation1.setLocation(Mockito.mock(Location.class));

        invocation2.setMethod(method2);
        invocation2.setMock(mock);
        invocation2.setLocation(Mockito.mock(Location.class));

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        assertTrue(invocationMatcher1.hasSimilarMethod(invocationMatcher1));
        assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
        assertFalse(invocationMatcher2.hasSimilarMethod(invocationMatcher1));
    }

    @Test
    public void testMatches() {
        Object mock = mock(Object.class);
        Object[] arguments = new Object[] { 1, 2 };
        Invocation invocation = Mockito.mock(Invocation.class);
        Method method = mockMethod("void foo(int arg1, int arg2)");

        invocation.setMethod(method);
        invocation.setMock(mock);
        invocation.setArguments(arguments);
        invocation.setLocation(Mockito.mock(Location.class));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.singletonList(argThat(is(1))));

        assertTrue(invocationMatcher.matches(invocation));
        assertFalse(invocationMatcher.matches(Mockito.mock(Invocation.class)));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Object mock = mock(Object.class);
        Object[] arguments = new Object[] { 1, "two" };
        Invocation invocation = Mockito.mock(Invocation.class);
        Method method = mockMethod("void foo(int arg1, String arg2)");

        invocation.setMethod(method);
        invocation.setMock(mock);
        invocation.setArguments(arguments);
        invocation.setLocation(Mockito.mock(Location.class));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        List<Matcher> matchers = new LinkedList<>();
        matchers.add(argThat(is(1)));
        matchers.add(argThat(is("two")));

        InvocationMatcher invocationMatcherWithMatchers = new InvocationMatcher(invocation, matchers);

        invocationMatcherWithMatchers.captureArgumentsFrom(invocation);

        Matcher argMatcher1 = matchers.get(0);
        Matcher argMatcher2 = matchers.get(1);

        if (argMatcher1 instanceof TypeSafeMatcher) {
            TypeSafeMatcher typeSafeMatcher1 = (TypeSafeMatcher) argMatcher1;
            assertEquals(1, typeSafeMatcher1.getExpectedValue());
        } else {
            fail("Matcher of the first argument is not expected to be a TypeSafeMatcher: " + argMatcher1.description());
        }

        if (argMatcher2 instanceof TypeSafeMatcher) {
            TypeSafeMatcher typeSafeMatcher2 = (TypeSafeMatcher) argMatcher2;
            assertEquals("two", typeSafeMatcher2.getExpectedValue());
        } else {
            fail("Matcher of the second argument is not expected to be a TypeSafeMatcher: " + argMatcher2.description());
        }
    }

    @Test
    public void testCreateFrom() {
        Object mock = mock(Object.class);
        List<Invocation> invocations = Arrays.asList(
                Mockito.mock(Invocation.class),
                Mockito.mock(Invocation.class),
                Mockito.mock(Invocation.class)
        );

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        for (InvocationMatcher invocationMatcher : invocationMatchers) {
            assertTrue(invocationMatcher instanceof InvocationMatcher);
        }
    }

    private Method mockMethod(String name) {
        Class<?> clazz = getClass();
        try {
            return clazz.getMethod(name);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Unable to find method: " + name, e);
        }
    }

    private Object is(Object expected) {
        return new TypeSafeMatcher<Object>() {
            @Override
            protected boolean matchesSafely(Object item) {
                return expected.equals(item);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is ").appendValue(expected);
            }
        };
    }
}
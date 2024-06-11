package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

public class InvocationMatcherTest {

    @Test
    public void testHasSameMethod() {
        Object mock = mock(Object.class);
        Method method1 = mockMethod("void test(int arg1, int arg2)", Object.class, int.class, int.class);
        Method method2 = mockMethod("void test(int arg1, int arg2, int arg3)", Object.class, int.class, int.class, int.class);
        Invocation invocation1 = createInvocation(mock, method1, new Object[]{1, 2});
        Invocation invocation2 = createInvocation(mock, method2, new Object[]{1, 2, 3});
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        assertFalse(invocationMatcher1.hasSameMethod(invocationMatcher2));
    }

    @Test
    public void testHasSameMethodWithSameArguments() {
        Object mock = mock(Object.class);
        Method method = mockMethod("void test(int arg1, int arg2)", Object.class, int.class, int.class);
        Invocation invocation1 = createInvocation(mock, method, new Object[]{1, 2});
        Invocation invocation2 = createInvocation(mock, method, new Object[]{1, 2});
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        assertTrue(invocationMatcher1.hasSameMethod(invocationMatcher2));
    }

    @Test
    public void testHasSameMethodWithUnverifiedInvocation() {
        Object mock = mock(Object.class);
        Method method = mockMethod("void test(int arg1, int arg2)", Object.class, int.class, int.class);
        Invocation invocation1 = createInvocation(mock, method, new Object[]{1, 2}, true);
        Invocation invocation2 = createInvocation(mock, method, new Object[]{1, 2});
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        assertTrue(invocationMatcher1.hasSameMethod(invocationMatcher2));
    }

    @Test
    public void testHasSameMethodWithSameMock() {
        Object mock = mock(Object.class);
        Method method1 = mockMethod("void test(int arg1, int arg2)", Object.class, int.class, int.class);
        Method method2 = mockMethod("void test(int arg1, int arg2)", Object.class, int.class, int.class);
        Invocation invocation1 = createInvocation(mock, method1, new Object[]{1, 2});
        Invocation invocation2 = createInvocation(mock, method2, new Object[]{1, 2});
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        assertTrue(invocationMatcher1.hasSameMethod(invocationMatcher2));
    }

    @Test
    public void testHasSimilarMethodWithSameMethodButDifferentArguments() {
        Object mock = mock(Object.class);
        Method method1 = mockMethod("void test(int arg1, int arg2)", Object.class, int.class, int.class);
        Method method2 = mockMethod("void test(double arg1, double arg2)", Object.class, double.class, double.class);
        Invocation invocation1 = createInvocation(mock, method1, new Object[]{1, 2});
        Invocation invocation2 = createInvocation(mock, method2, new Object[]{1.0, 2.0});
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testHasSimilarMethodWithSameMethodButDifferentMock() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Method method = mockMethod("void test(int arg1, int arg2)", Object.class, int.class, int.class);
        Invocation invocation1 = createInvocation(mock1, method, new Object[]{1, 2});
        Invocation invocation2 = createInvocation(mock2, method, new Object[]{1, 2});
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testMatches() {
        Object mock = mock(Object.class);
        Method method = mockMethod("void test(int arg1, int arg2)", Object.class, int.class, int.class);
        Invocation invocation = createInvocation(mock, method, new Object[]{1, 2});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Matcher matcher = Matchers.equalTo(1);

        List<Matcher> matchers = Arrays.asList(matcher);
        InvocationMatcher withMatchers = new InvocationMatcher(invocation, matchers);

        assertTrue(invocationMatcher.matches(invocationMatcher));
        assertTrue(withMatchers.matches(invocationMatcher));
        assertTrue(invocationMatcher.matches(invocation));
        assertTrue(withMatchers.matches(invocation));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Object mock = mock(Object.class);
        Method method = mockMethod("void test(int arg1, int arg2)", Object.class, int.class, int.class);
        Invocation invocation = createInvocation(mock, method, new Object[]{1, 2});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        CapturesArguments matcher = mock(CapturesArguments.class);
        when(matcher.captureFrom(Integer.valueOf(1))).thenReturn(1);
        when(matcher.captureFrom(Integer.valueOf(2))).thenReturn(2);

        invocationMatcher.captureArgumentsFrom(invocation);

        verifyNoMoreInteractions(matcher);
        assertEquals(1, matcher.captured.size());
        assertEquals(1, matcher.captured.get(0));
        assertEquals(2, matcher.captured.get(1));
    }

    @Test
    public void testCreateFrom() {
        Object mock = mock(Object.class);
        List<Invocation> invocations = new ArrayList<>();
        invocations.add(createInvocation(mock, mockMethod("void test(int arg1)", Object.class, int.class), new Object[]{1}));
        invocations.add(createInvocation(mock, mockMethod("void test(int arg1, int arg2)", Object.class, int.class, int.class), new Object[]{1, 2}));
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        InvocationMatcher invocationMatcher1 = invocationMatchers.get(0);
        InvocationMatcher invocationMatcher2 = invocationMatchers.get(1);

        assertTrue(invocationMatcher1.matches(invocationMatcher1));
        assertTrue(invocationMatcher1.matches(invocationMatcher2));
        assertTrue(invocationMatcher2.matches(invocationMatcher1));
        assertTrue(invocationMatcher2.matches(invocationMatcher2));
    }

    private Method mockMethod(String name, Class<?> returnType, Class<?>... parameterTypes) {
        try {
            Class<?> clazz = Class.forName("org.mockito.internal.invocation.InvocationMatcherTest$TestMethod");
            return clazz.getMethod(name, parameterTypes);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private Invocation createInvocation(Object object, Method method, Object... arguments) {
        return new Invocation() {
            @Override
            public Method getMethod() {
                return method;
            }

            @Override
            public Location getLocation() {
                return spy(new Location() {
                    @Override
                    public String getClassName() {
                        return "TestClass";
                    }

                    @Override
                    public int getLineNumber() {
                        return 42;
                    }
                });
            }

            @Override
            public Object[] getArguments() {
                return arguments;
            }

            @Override
            public Object getMock() {
                return object;
            }

            @Override
            public boolean isVerified() {
                return false;
            }

            @Override
            public Object[] getRawArguments() {
                return arguments;
            }
        };
    }

    private Invocation createInvocation(Object object, Method method, Object[] arguments, boolean isVerified) {
        return new Invocation() {
            @Override
            public Method getMethod() {
                return method;
            }

            @Override
            public Location getLocation() {
                return mock(Location.class);
            }

            @Override
            public Object[] getArguments() {
                return arguments;
            }

            @Override
            public Object getMock() {
                return object;
            }

            @Override
            public boolean isVerified() {
                return isVerified;
            }

            @Override
            public Object[] getRawArguments() {
                return arguments;
            }
        };
    }
}
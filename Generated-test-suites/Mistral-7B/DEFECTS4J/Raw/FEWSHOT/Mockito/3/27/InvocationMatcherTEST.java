package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.ArgumentsMatcher.contains;
import static org.mockito.internal.matchers.ArgumentsMatcher.nullValue;
import static org.mockito.internal.matchers.ArgumentsMatcher.same;
import static org.mockito.internal.matchers.ArgumentsMatcher.type;
import static org.mockito.internal.matchers.greaterThan;
import static org.mockito.internal.matchers.lessThan;
import static org.mockito.internal.matchers.not;
import static org.mockito.internal.matchers.hasItem;
import static org.mockito.internal.matchers.notNullValue;
import static org.mockito.internal.matchers.hasSize;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.matchers.MatcherDecorator;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

public class InvocationMatcherTest {

    @Test
    public void testToString() {
        Invocation invocation = createMockInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        String expected = new PrintSettings().print(Arrays.asList(same(String.class), lessThan(5)), invocation);
        String actual = invocationMatcher.toString();

        assertTrue(expected.equals(actual));
    }

    @Test
    public void testMatchesLowerCaseMethodName() {
        Invocation invocation = createMockInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation anotherInvocation = createMockInvocation();
        anotherInvocation.setMethod(createMethodWithName("getSomethingElse"));

        assertTrue(invocationMatcher.matches(invocation));
        assertFalse(invocationMatcher.matches(anotherInvocation));
    }

    @Test
    public void testMatchesAnotherMock() {
        Object mock1 = createMock();
        Object mock2 = createMock();

        Invocation invocation = createMockInvocation(mock1);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation anotherInvocation = createMockInvocation(mock2);

        assertTrue(invocationMatcher.matches(invocation));
        assertFalse(invocationMatcher.matches(anotherInvocation));
    }

    @Test
    public void testMatchesDifferentMethod() {
        Invocation invocation = createMockInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation anotherInvocation = createMockInvocation();
        anotherInvocation.setMethod(createMethodWithName("doSomethingElse"));

        assertFalse(invocationMatcher.matches(anotherInvocation));
    }

    @Test
    public void testMatchesUnverified() {
        Invocation invocation = createMockInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation anotherInvocation = createMockInvocation();
        anotherInvocation.markAsVerified();

        assertFalse(invocationMatcher.matches(anotherInvocation));
    }

    @Test
    public void testMatchesSameMethodButDifferentArguments() {
        Invocation invocation = createMockInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation anotherInvocation = createMockInvocation();
        anotherInvocation.setMethod(invocation.getMethod());
        anotherInvocation.setArguments(new Object[] { nullValue(), new Integer(5) });

        assertFalse(invocationMatcher.matches(anotherInvocation));
    }

    @Test
    public void testMatchesSameMethodWithVarargs() {
        Invocation invocation = createMockInvocation("John", "Doe");
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation anotherInvocation = createMockInvocation("Jane", "Doe", " Bob");

        assertTrue(invocationMatcher.matches(anotherInvocation));
    }

    @Test
    public void testMatchesSameMethodWithCapturesArguments() {
        Object mock = createMock();
        Integer expected = 5;

        Invocation invocation = createMockInvocation(mock);
        Matcher matcher = new CapturesArguments();
        List<Matcher> matchers = Collections.singletonList(new MatcherDecorator<Matcher>(matcher, new ArgumentMatcher<Object[]>() {
            @Override
            public boolean matches(Object argument) {
                Object[] args = (Object[]) argument;
                return Arrays.equals(args, new Object[] { new Integer(expected) });
            }
        }));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Invocation anotherInvocation = createMockInvocation(mock);

        anotherInvocation.setArguments(new Object[] { expected });

        assertTrue(invocationMatcher.matches(anotherInvocation));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = createMockInvocation();
        Invocation invocation2 = createMockInvocation();

        assertTrue(new InvocationMatcher(invocation1).hasSameMethod(invocation1));
        assertTrue(new InvocationMatcher(invocation1).hasSameMethod(invocation2));
        assertFalse(new InvocationMatcher(invocation1).hasSameMethod(createMockInvocation()));
    }

    @Test
    public void testHasDifferentMethod() {
        Invocation invocation1 = createMockInvocation();
        Invocation invocation2 = createMockInvocation();
        invocation2.setMethod(createMethodWithName("doSomethingElse"));

        assertFalse(new InvocationMatcher(invocation1).hasSameMethod(invocation2));
    }

    @Test
    public void testCaptureArgumentsFromInvocation() {
        Object mock = createMock();
        Integer expected = 5;

        Invocation invocation = createMockInvocation(mock);
        Matcher matcher = new CapturesArguments();
        List<Matcher> matchers = Collections.singletonList(matcher);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        invocationMatcher.captureArgumentsFrom(createMockInvocation(mock, expected));

        assertTrue(matcher.matches(expected));
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = Arrays.asList(createMockInvocation(), createMockInvocation());
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        InvocationMatcher invocationMatcher1 = invocationMatchers.get(0);
        InvocationMatcher invocationMatcher2 = invocationMatchers.get(1);

        assertTrue(invocationMatcher1.matches(createMockInvocation()));
        assertTrue(invocationMatcher2.matches(createMockInvocation()));
    }

    @Test
    public void testCreateFromWithMatchers() {
        List<Invocation> invocations = Arrays.asList(createMockInvocation(new Integer(5)), createMockInvocation(new Integer(10)));
        List<Matcher> matchers = Arrays.asList(notNullValue(), greaterThan(0));
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations, matchers);

        InvocationMatcher invocationMatcher1 = invocationMatchers.get(0);
        InvocationMatcher invocationMatcher2 = invocationMatchers.get(1);

        Invocation testInvocation1 = createMockInvocation(new Integer(5));
        Invocation testInvocation2 = createMockInvocation(new Integer(10));

        assertTrue(invocationMatcher1.matches(testInvocation1));
        assertFalse(invocationMatcher1.matches(testInvocation2));
        assertTrue(invocationMatcher2.matches(testInvocation2));
        assertFalse(invocationMatcher2.matches(testInvocation1));
    }

    @Test
    public void testCreateFromWithEmptyList() {
        List<Invocation> invocations = Collections.emptyList();
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        assertTrue(invocationMatchers.isEmpty());
    }

    @Test
    public void testCreateFromWithNullList() {
        List<Invocation> invocations = null;
        try {
            List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    private Method createMethodWithName(String name) {
        return createMethod("test", new Class[] { String.class }, new Class[] {});
    }

    private Method createMethod(String name, Class[] parameterTypes, Class[] returnTypes) {
        return createClassWithMethod("MyTestClass", new Class[]{}, new Class[]{Class.class}, new Class[]{Method.class},
                new Method[]{createMethodWithName(name, parameterTypes, returnTypes)});
    }

    private Class<?> createClassWithMethod(String name, Class<?>[] superclass, Class<?>[] interfaces,
            Class<?>[] declaredClasses, Method[] methods) {
        try {
            Class<?> clazz = Class.forName(name);
            Class<?> testClass = clazz.getDeclaredClass();

            testClass.getDeclaredConstructor();

            testClass.getSuperclass();
            testClass.getInterfaces();

            for (Method method : methods) {
                testClass.getMethod(method.getName(), method.getParameterTypes());
            }
            return testClass;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object createMock() {
        return Mockito.mock(Object.class);
    }

    private Invocation createMockInvocation() {
        return Mockito.mock(Invocation.class);
    }

    private Invocation createMockInvocation(Object mock) {
        return Mockito.mock(Invocation.class, new InvocationSpecification() {
            @Override
            public Object getMock() {
                return mock;
            }
        });
    }

    private Invocation createMockInvocation(Object mock, Object argument) {
        return Mockito.mock(Invocation.class, new InvocationSpecification() {
            @Override
            public Object getMock() {
                return mock;
            }

            @Override
            public Object[] getArguments() {
                return new Object[]{argument};
            }
        });
    }

    private Invocation createMockInvocation(Object[] arguments) {
        return Mockito.mock(Invocation.class, new InvocationSpecification() {
            @Override
            public Object[] getArguments() {
                return arguments;
            }
        });
    }
}
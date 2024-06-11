package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.ArgumentsMatcher.argThat;
import static org.mockito.internal.matchers.ArgumentsMatcher.isNull;
import static org.mockito.internal.matchers.ArgumentsMatcher.sameInstance;
import static org.mockito.internal.matchers.ArgumentsMatcher.sameMethod;
import static org.mockito.internal.matchers.ArgumentsMatcher.sameString;
import static org.mockito.internal.matchers.ArgumentsMatcher.value;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    private static Object testObject = new Object() {};

    @Test
    public void testNewInstance() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        assertTrue(matcher.matches(invocation));
    }

    @Test
    public void testNewInstanceWithMatchers() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Matcher<Object> matcher = sameInstance(testObject);
        InvocationMatcher matcherWithMatchers = new InvocationMatcher(invocation, Arrays.asList(matcher));
        assertTrue(matcherWithMatchers.matches(invocation));
    }

    @Test
    public void testMatchesDifferentMock() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation1);
        assertFalse(matcher.matches(invocation2));
    }

    @Test
    public void testMatchesDifferentMethod() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Method method1 = Mockito.spy(Mockito.method(InvocationMatcher.class, "hasSameMethod"));
        Method method2 = Mockito.spy(Mockito.method(InvocationMatcher.class, "toString"));
        Invocation invocation2 = Mockito.mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation1, Arrays.asList(sameMethod(method1), sameMethod(method2)));
        assertFalse(matcher.matches(invocation2));
    }

    @Test
    public void testMatchesUnverified() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation1);
        assertTrue(matcher.matches(invocation2));
    }

    @Test
    public void testMatchesSameMethodWithSameArguments() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Object argument1 = new Object();
        Object argument2 = new Object();
        Object[] arguments1 = { argument1, argument2 };
        Method method = Mockito.spy(Mockito.method(InvocationMatcher.class, "hasSameMethod"));
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Object[] arguments2 = { argument1, argument2 };
        InvocationMatcher matcher = new InvocationMatcher(invocation1, Arrays.asList(sameMethod(method)));
        assertTrue(matcher.matches(invocation2));
    }

    @Test
    public void testMatchesDifferentArguments() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Object argument1 = new Object();
        Object argument2 = new Object();
        Object[] arguments1 = { argument1, argument2 };
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Object argument3 = new Object();
        Object[] arguments2 = { argument1, argument3 };
        InvocationMatcher matcher = new InvocationMatcher(invocation1, Arrays.asList(new ArgumentsMatcher("arguments") {
            @Override
            public boolean matches(Object actual) {
                return Arrays.equals((Object[]) actual, arguments1);
            }
        }));
        assertFalse(matcher.matches(invocation2));
    }

    @Test
    public void testMatchesDifferentArgumentsWithCapturedArguments() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Object argument1 = new Object();
        Object argument2 = new Object();
        Object[] arguments1 = { argument1, argument2 };
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1, Arrays.asList(argThat(new ArgumentsMatcher() {
            @Override
            public void captureArgumentsFrom(Object[] invocationArguments) {
                argument1 = invocationArguments[0];
                argument2 = invocationArguments[1];
            }
        })));
        Matcher<Object> matcher2 = sameInstance(argument1);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Object argument3 = new Object();
        Object[] arguments2 = { argument3, argument2 };
        assertTrue(matcher1.matches(invocation2));
        assertTrue(argument1.equals(argument3));
    }

    @Test
    public void testToString() {
        Invocation invocation = Mockito.mock(Invocation.class);
        String expected = "InvocationMatcher [invocation=MockInvocationInterceptor[invocation=org.mockito.internal.invocation.Invocation@6a2c9747, method=public java.lang.String org.mockito.internal.invocation.InvocationMatcher.hasSameMethod(org.mockito.internal.invocation.Invocation)], matchers=[ArgumentsMatcher [name=toString, value=org.mockito.internal.invocation.ArgumentsMatcher@5180c25d]]]";
        InvocationMatcher matcher = new InvocationMatcher(invocation, Arrays.asList(new ArgumentsMatcher("arguments") {
            @Override
            public String toString() {
                return "toString";
            }

            @Override
            public boolean matches(Object actual) {
                return toString().equals(actual.toString());
            }
        }));
        assertTrue(expected.equals(matcher.toString()));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Method method1 = Mockito.spy(Mockito.method(InvocationMatcher.class, "hasSameMethod"));
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method2 = Mockito.spy(Mockito.method(InvocationMatcher.class, "toString"));
        assertTrue(new InvocationMatcher(invocation1, Arrays.asList(sameMethod(method1), sameMethod(method2))).hasSameMethod(invocation1));
        assertFalse(new InvocationMatcher(invocation1, Arrays.asList(sameMethod(method1), sameMethod(method2))).hasSameMethod(invocation2));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Object[] arguments = { null, "foo" };
        assertTrue(new InvocationMatcher(invocation).safelyArgumentsMatch(arguments));
    }

    @Test(expected = RuntimeException.class)
    public void testSafelyArgumentsMatchWithException() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Object[] arguments = { null, null };
        new InvocationMatcher(invocation).safelyArgumentsMatch(arguments);
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Method method1 = Mockito.spy(Mockito.method(InvocationMatcher.class, "hasSameMethod"));
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method2 = Mockito.spy(Mockito.method(InvocationMatcher.class, "toString"));
        assertTrue(new InvocationMatcher(invocation1, Arrays.asList(sameMethod(method1), sameMethod(method2))).hasSimilarMethod(invocation1));
        assertFalse(new InvocationMatcher(invocation1, Arrays.asList(sameMethod(method1), sameMethod(method2))).hasSimilarMethod(invocation2));
    }

    @Test
    public void testCreateFrom() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        List<InvocationMatcher> matchers = InvocationMatcher.createFrom(Arrays.asList(invocation1, invocation2));
        assertTrue(matchers.size() == 2);
        assertTrue(matchers.get(0) instanceof InvocationMatcher);
        assertTrue(matchers.get(1) instanceof InvocationMatcher);
    }
}
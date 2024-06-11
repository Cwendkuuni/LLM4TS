package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.ArgumentsMatcher.argThat;
import static org.mockito.internal.reporting.PrintSettings.defaultPrintSettings;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

public class InvocationMatcherTest {

    @Test
    public void testNewInstance() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Method method = Mockito.mock(Method.class);

        InvocationMatcher matcher = new InvocationMatcher(invocation);
        InvocationMatcher matcherWithMatchers = new InvocationMatcher(invocation, Collections.singletonList(Mockito.mock(Matcher.class)));

        assertTrue(matcher instanceof DescribedInvocation);
        assertTrue(matcher instanceof CapturesArgumentsFromInvocation);
        assertTrue(matcher instanceof Serializable);
        assertTrue(matcher.getMatchers().isEmpty());
        assertTrue(matcher.getInvocation().equals(invocation));
        assertEquals(method, matcher.getMethod());
        assertEquals(defaultPrintSettings().print(Collections.emptyList(), invocation), matcher.toString());

        assertTrue(matcherWithMatchers instanceof DescribedInvocation);
        assertTrue(matcherWithMatchers instanceof CapturesArgumentsFromInvocation);
        assertTrue(matcherWithMatchers instanceof Serializable);
        assertFalse(matcherWithMatchers.getMatchers().isEmpty());
        assertTrue(matcherWithMatchers.getInvocation().equals(invocation));
        assertEquals(method, matcherWithMatchers.getMethod());
        assertEquals(defaultPrintSettings().print(Collections.singletonList(Mockito.mock(Matcher.class)), invocation), matcherWithMatchers.toString());
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);

        Mockito.when(invocation1.getMethod()).thenReturn(method1);
        Mockito.when(invocation2.getMethod()).thenReturn(method2);

        assertTrue(new InvocationMatcher(invocation1).hasSameMethod(invocation1));
        assertTrue(new InvocationMatcher(invocation1).hasSameMethod(invocation2));
        assertFalse(new InvocationMatcher(invocation1).hasSameMethod(Mockito.mock(Invocation.class)));
        assertFalse(new InvocationMatcher(invocation1).hasSameMethod(new Object() {
            @SuppressWarnings("unused")
            private void method() {}
        }));
        assertFalse(new InvocationMatcher(invocation1).hasSameMethod(Mockito.mock(DescribedInvocation.class)));

        Mockito.when(method1.getName()).thenReturn("testMethod");
        Mockito.when(method2.getName()).thenReturn("testMethod2");

        assertFalse(new InvocationMatcher(invocation1).hasSameMethod(invocation2));
        assertFalse(new InvocationMatcher(invocation1).hasSameMethod(invocation2, "testMethod3"));
        assertFalse(new InvocationMatcher(invocation1).hasSameMethod(new Object() {
            @SuppressWarnings("unused")
            private void testMethod(String arg1, String arg2) {}
        }));

        Class<?>[] argsTypes1 = method1.getParameterTypes();
        Class<?>[] argsTypes2 = method2.getParameterTypes();
        if (argsTypes1.length == argsTypes2.length) {
            for (int i = 0; i < argsTypes1.length; i++) {
                if (!argsTypes1[i].equals(argsTypes2[i])) {
                    assertFalse(new InvocationMatcher(invocation1).hasSameMethod(invocation2));
                    break;
                }
            }
            assertTrue(new InvocationMatcher(invocation1).hasSameMethod(invocation2));
        } else {
            assertFalse(new InvocationMatcher(invocation1).hasSameMethod(invocation2));
        }
    }

    @Test
    public void testMatches() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Method method = Mockito.mock(Method.class);

        Object[] arguments = new Object[] {
            "foo",
            1,
            new int[] { 1, 2, 3 }
        };

        Mockito.when(invocation.getMethod()).thenReturn(method);
        Mockito.when(invocation.getArguments()).thenReturn(arguments);

        List<Matcher> matchers = Arrays.asList(
                argThat(Matchers.isInstanceOf(String.class)),
                argThat(Matchers.isInstanceOf(Integer.class)),
                argThat(Matchers.arrayContaining(Matchers.isA(Integer.class)))
        );

        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        Invocation invocationToMatch = Mockito.mock(Invocation.class);
        Mockito.when(invocationToMatch.getMethod()).thenReturn(method);
        Mockito.when(invocationToMatch.getArguments()).thenReturn(arguments);

        assertTrue(matcher.matches(invocation));
        assertTrue(matcher.matches(invocationToMatch));
        assertFalse(matcher.matches(Mockito.mock(Invocation.class)));

        Object[] differentArguments = new Object[] { "bar", 2, new int[] { 1, 2, 3, 4 } };
        Mockito.when(invocationToMatch.getArguments()).thenReturn(differentArguments);

        assertFalse(matcher.matches(invocationToMatch));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Method method = Mockito.mock(Method.class);

        Object[] arguments = new Object[] {
            "foo",
            1,
            new int[] { 1, 2, 3 }
        };

        Mockito.when(invocation.getMethod()).thenReturn(method);
        Mockito.when(invocation.getArguments()).thenReturn(arguments);

        InvocationMatcher matcher = new InvocationMatcher(invocation);
        Mockito.doCallRealMethod().when(matcher).captureArgumentsFrom(invocation);

        List<Matcher> matchers = Arrays.asList(
                argThat(Matchers.isInstanceOf(String.class)),
                argThat(Matchers.isInstanceOf(Integer.class)),
                argThat(Matchers.arrayContaining(Matchers.isA(Integer.class)))
        );

        InvocationMatcher matcherWithMatchers = new InvocationMatcher(invocation, matchers);
        Mockito.doCallRealMethod().when(matcherWithMatchers).captureArgumentsFrom(invocation);

        List<Object> capturedArguments = new LinkedList<>();
        List<Object> capturedArgumentsWithMatchers = new LinkedList<>();

        Matcher matcherForString = Matchers.argThat(arg -> capturedArguments.add(arg));
        Matcher matcherForInteger = Matchers.argThat(arg -> capturedArguments.add(arg));
        Matcher matcherForIntegerArray = Matchers.argThat(arg -> capturedArgumentsWithMatchers.addAll((List<?>) arg));

        Mockito.when(invocation.getRawArguments()).then(i -> i.getArguments());

        matcher.captureArgumentsFrom(invocation);
        matcherWithMatchers.captureArgumentsFrom(invocation);

        assertEquals(arguments[0], capturedArguments.get(0));
        assertEquals(arguments[1], capturedArguments.get(1));
        assertArrayEquals(arguments[2], capturedArguments.get(2).toArray());

        assertEquals(arguments[0], capturedArgumentsWithMatchers.get(0));
        assertEquals(arguments[1], capturedArgumentsWithMatchers.get(1));
        assertArrayEquals(arguments[2], capturedArgumentsWithMatchers.get(2).toArray());
    }

    @Test
    public void testCreateFrom() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);

        Method method = Mockito.mock(Method.class);
        Mockito.when(invocation1.getMethod()).thenReturn(method);
        Mockito.when(invocation2.getMethod()).thenReturn(method);

        List<Invocation> invocations = Arrays.asList(invocation1, invocation2);
        List<InvocationMatcher> matchers = InvocationMatcher.createFrom(invocations);

        assertTrue(matchers.size() == 2);
        assertTrue(matchers.get(0) instanceof InvocationMatcher);
        assertTrue(matchers.get(1) instanceof InvocationMatcher);
        assertTrue(matchers.get(0).getInvocation().equals(invocation1));
        assertTrue(matchers.get(1).getInvocation().equals(invocation2));
    }
}
package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.internal.invocation.ArgumentsProcessor;
import org.mockito.internal.invocation.CapturesArguments;
import org.mockito.internal.invocation.DescribedInvocation;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.MethodUtil;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.*;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherCreation() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(eq("foo"), eq(1));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);
        InvocationMatcher matcherWithoutMatchers = new InvocationMatcher(invocation);

        assertTrue(matcher instanceof DescribedInvocation);
        assertTrue(matcher instanceof CapturesArgumensFromInvocation);
        assertTrue(matcher instanceof Serializable);

        assertEquals(matchers, matcher.getMatchers());
        assertEquals(invocation, matcher.getInvocation());
        assertEquals(matcher, matcherWithoutMatchers.getMatchers(Collections.emptyList()));
    }

    @Test
    public void testInvocationMatcherToString() {
        Method method = mock(Method.class);
        Invocation invocation = Mockito.mock(Invocation.class);
        FieldSetter.setField(invocation, "method", method);
        List<Matcher> matchers = Arrays.asList(eq("foo"), eq(1));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        String printSettings = new PrintSettings().setShowInvocation(true)
                .setShowInvocationArgTypes(false).setShowInvocationCallSite(false)
                .setShowMatchDetails(true)
                .setShowMatchAssertions(true)
                .setShowMatchToString(true)
                .setShowStackFrames(false)
                .setShowStackTraces(false)
                .setShowColors(false)
                .setShowDiff(false)
                .toString();

        String expected = String.format("%s %s%n%s%n",
                new PrintSettings().print(matchers, invocation),
                printSettings,
                new PrintSettings().print(matchers, invocation, new Object[0])
        );

        String actual = matcher.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testMatches_SameInvocation() {
        Object mock = mock(Object.class);
        Invocation invocation = Mockito.mock(Invocation.class);
        Method method = mock(Method.class);
        FieldSetter.setField(invocation, "method", method);
        FieldSetter.setField(invocation, "mock", mock);

        List<Matcher> matchers = Arrays.asList(eq("foo"), eq(1));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        assertTrue(matcher.matches(invocation));
    }

    @Test
    public void testMatches_DifferentInvocation() {
        Object mock = mock(Object.class);
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method = mock(Method.class);

        FieldSetter.setField(invocation1, "method", method);
        FieldSetter.setField(invocation1, "mock", mock);
        FieldSetter.setField(invocation2, "mock", mock);

        List<Matcher> matchers = Arrays.asList(eq("foo"), eq(1));
        InvocationMatcher matcher = new InvocationMatcher(invocation1, matchers);

        assertFalse(matcher.matches(invocation2));
    }

    @Test
    public void testMatches_SameMethod_DifferentArguments() {
        Object mock = mock(Object.class);
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method = mock(Method.class);

        FieldSetter.setField(invocation1, "method", method);
        FieldSetter.setField(invocation1, "mock", mock);
        FieldSetter.setField(invocation2, "mock", mock);

        List<Matcher> matchers = Arrays.asList(eq("foo"), eq(1));
        InvocationMatcher matcher = new InvocationMatcher(invocation1, matchers);

        Object[] arguments1 = new Object[]{1, "bar"};
        Object[] arguments2 = new Object[]{2, "hello"};

        FieldSetter.setField(invocation1, "arguments", arguments1);
        FieldSetter.setField(invocation2, "arguments", arguments2);

        assertFalse(matcher.matches(invocation2));
    }

    @Test
    public void testMatches_SameMethod_SameArguments() {
        Object mock = mock(Object.class);
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method = mock(Method.class);

        FieldSetter.setField(invocation1, "method", method);
        FieldSetter.setField(invocation1, "mock", mock);
        FieldSetter.setField(invocation2, "mock", mock);

        List<Matcher> matchers = Arrays.asList(eq("foo"), eq(1));
        InvocationMatcher matcher = new InvocationMatcher(invocation1, matchers);

        Object[] arguments1 = new Object[]{1, "bar"};
        Object[] arguments2 = new Object[]{1, "bar"};

        FieldSetter.setField(invocation1, "arguments", arguments1);
        FieldSetter.setField(invocation2, "arguments", arguments2);

        assertTrue(matcher.matches(invocation2));
    }

    @Test
    public void testMatches_DifferentMethod() {
        Object mock = mock(Object.class);
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);

        FieldSetter.setField(invocation1, "method", method1);
        FieldSetter.setField(invocation1, "mock", mock);
        FieldSetter.setField(invocation2, "mock", mock);
        FieldSetter.setField(invocation2, "method", method2);

        List<Matcher> matchers = Arrays.asList(eq("foo"), eq(1));
        InvocationMatcher matcher = new InvocationMatcher(invocation1, matchers);

        assertFalse(matcher.matches(invocation2));
    }

    @Test
    public void testHasSimilarMethod_SameMethod_SameMock() {
        Object mock = mock(Object.class);
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method = mock(Method.class);

        FieldSetter.setField(invocation1, "method", method);
        FieldSetter.setField(invocation1, "mock", mock);
        FieldSetter.setField(invocation2, "mock", mock);

        assertTrue(new InvocationMatcher(invocation1).hasSimilarMethod(invocation2));
    }

    @Test
    public void testHasSimilarMethod_SameMethod_DifferentMock() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method = mock(Method.class);

        FieldSetter.setField(invocation1, "method", method);
        FieldSetter.setField(invocation1, "mock", mock1);
        FieldSetter.setField(invocation2, "mock", mock2);

        assertFalse(new InvocationMatcher(invocation1).hasSimilarMethod(invocation2));
    }

    @Test
    public void testHasSimilarMethod_DifferentMethods_SameMock() {
        Object mock = mock(Object.class);
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);

        FieldSetter.setField(invocation1, "method", method1);
        FieldSetter.setField(invocation1, "mock", mock);
        FieldSetter.setField(invocation2, "mock", mock);
        FieldSetter.setField(invocation2, "method", method2);

        assertFalse(new InvocationMatcher(invocation1).hasSimilarMethod(invocation2));
    }

    @Test
    public void testHasSameMethod() {
        Object mock = mock(Object.class);
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        FieldSetter.setField(invocation1, "method", method1);
        FieldSetter.setField(invocation2, "method", method2);

        String methodName1 = MethodUtil.getRawMethodName(method1);
        String methodName2 = MethodUtil.getRawMethodName(method2);

        Class[] params1 = method1.getParameterTypes();
        Class[] params2 = method2.getParameterTypes();

        // Test with simple types
        if (params1.length == params2.length && Arrays.equals(params1, params2)) {
            assertTrue(new InvocationMatcher(invocation1).hasSameMethod(invocation2));
        } else {
            assertFalse(new InvocationMatcher(invocation1).hasSameMethod(invocation2));
        }

        // Test with arrays
        if (params1.length == params2.length) {
            for (int i = 0; i < params1.length; i++) {
                if (params1[i].isAssignableFrom(params2[i]) || params1[i].equals(params2[i])) {
                    continue;
                }

                assertFalse(new InvocationMatcher(invocation1).hasSameMethod(invocation2));
                return;
            }

            assertTrue(new InvocationMatcher(invocation1).hasSameMethod(invocation2));
        } else {
            assertFalse(new InvocationMatcher(invocation1).hasSameMethod(invocation2));
        }

        // Test with custom types
        if (params1.length == params2.length) {
            for (int i = 0; i < params1.length; i++) {
                Class paramType1 = params1[i];
                Class paramType2 = params2[i];

                if (paramType1.isAssignableFrom(paramType2) || paramType2.isAssignableFrom(paramType1)) {
                    continue;
                }

                TypeSafeMatcher<Class> customMatcher = new TypeSafeMatcher<Class>() {
                    @Override
                    public boolean matchesSafely(Class item) {
                        return paramType1.isAssignableFrom(item) || paramType2.isAssignableFrom(item);
                    }

                    @Override
                    public void describeTo(Description description) {
                        description.appendText("a class assignable to either ");
                        description.appendValue(paramType1);
                        description.appendText(" or ");
                        description.appendValue(paramType2);
                    }
                };

                MatcherDecorator matcherDecorator1 = new MatcherDecorator(customMatcher, "param " + i);
                MatcherDecorator matcherDecorator2 = new MatcherDecorator(customMatcher, "param " + i);

                List<Matcher> matchers1 = Arrays.asList(matcherDecorator1);
                List<Matcher> matchers2 = Arrays.asList(matcherDecorator2);

                InvocationMatcher matcher1 = new InvocationMatcher(invocation1, matchers1);
                InvocationMatcher matcher2 = new InvocationMatcher(invocation2, matchers2);

                assertTrue(matcher1.hasSameMethod(matcher2));
            }

            assertTrue(new InvocationMatcher(invocation1).hasSameMethod(invocation2));
        } else {
            assertFalse(new InvocationMatcher(invocation1).hasSameMethod(invocation2));
        }
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Object mock = mock(Object.class);
        Invocation invocation = Mockito.mock(Invocation.class);
        Object[] arguments = new Object[]{1, "bar"};
        Method method = mock(Method.class);

        FieldSetter.setField(invocation, "method", method);
        FieldSetter.setField(invocation, "mock", mock);
        FieldSetter.setField(invocation, "arguments", arguments);

        List<Matcher> matchers = Arrays.asList(
                new TypeSafeMatcher<Object>() {
                    @Override
                    public boolean matchesSafely(Object item) {
                        return item.equals(arguments[0]);
                    }

                    @Override
                    public void describeTo(Description description) {
                        description.appendText("the first argument");
                    }
                },
                new TypeSafeMatcher<Object>() {
                    @Override
                    public boolean matchesSafely(Object item) {
                        return item.equals(arguments[1]);
                    }

                    @Override
                    public void describeTo(Description description) {
                        description.appendText("the second argument");
                    }
                }
        );

        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        matcher.captureArgumentsFrom(invocation);

        for (int i = 0; i < matchers.size(); i++) {
            Matcher matcher1 = matchers.get(i);
            ArgumentCaptor<?> captor = ArgumentCaptor.forClass(matcher1.getType());
            verify(mock).methodCall(captor.capture());
            assertEquals(arguments[i], captor.getValue());
        }
    }

    @Test
    public void testCreateFrom() {
        Object mock = mock(Object.class);
        List<Invocation> invocations = Arrays.asList(
                Mockito.mock(Invocation.class),
                Mockito.mock(Invocation.class)
        );

        List<InvocationMatcher> matchers = InvocationMatcher.createFrom(invocations);

        for (InvocationMatcher matcher : matchers) {
            assertTrue(matcher instanceof InvocationMatcher);
            assertNotNull(matcher.getInvocation());
            assertNotNull(matcher.getMatchers());
        }
    }
}
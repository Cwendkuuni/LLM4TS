package org.mockito.internal.invocation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.internal.matchers.ArgumentMatchers.argThat;
import static org.mockito.internal.matchers.ArgumentsMatcher.argument;
import static org.mockito.internal.matchers.ArgumentsMatcher.arrayContaining;
import static org.mockito.internal.matchers.ArgumentsMatcher.isInstanceOf;
import static org.mockito.internal.matchers.ArgumentsMatcher.isNull;
import static org.mockito.internal.matchers.ArgumentsMatcher.same;
import static org.mockito.internal.matchers.ArgumentsMatcher.sameInstance;
import static org.mockito.internal.matchers.ArgumentsMatcher.value;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.invocation.Invocation;
import org.mockito.stub.Stubber;

public class InvocationMatcherTest {

    @Test
    public void testNewInstanceWithMatchers() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        List<Matcher> matchers = Arrays.asList(value("foo"), value(123L), isInstanceOf(String.class));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);
        assertEquals(invocation, matcher.getInvocation());
        assertEquals(method, matcher.getMethod());
        assertEquals(matchers, matcher.getMatchers());
    }

    @Test
    public void testNewInstanceWithoutMatchers() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        assertEquals(invocation, matcher.getInvocation());
        assertNull(matcher.getMatchers());
    }

    @Test
    public void testMatches() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        Method method = mock(Method.class);
        Object[] arguments1 = new Object[] { "foo", 123L, "bar" };
        Object[] arguments2 = new Object[] { "foo", 123L, "baz" };

        when(invocation1.getMethod()).thenReturn(method);
        when(invocation1.getArguments()).thenReturn(arguments1);
        when(invocation2.getMethod()).thenReturn(method);
        when(invocation2.getArguments()).thenReturn(arguments2);

        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        assertTrue(matcher1.matches(invocation1));
        assertTrue(matcher1.matches(invocation2));
        assertFalse(matcher1.matches(mock(Invocation.class)));
        assertFalse(matcher2.matches(invocation1));
        assertFalse(matcher2.matches(invocation2));
        assertFalse(matcher2.matches(mock(Invocation.class)));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);

        when(invocation1.getMethod()).thenReturn(method1);
        when(invocation1.getInvocation()).thenReturn(invocation1);
        when(invocation2.getMethod()).thenReturn(method2);
        when(invocation2.getInvocation()).thenReturn(invocation2);

        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        assertTrue(matcher1.hasSimilarMethod(matcher1));
        assertTrue(matcher1.hasSimilarMethod(matcher2));
        assertFalse(matcher1.hasSameMethod(matcher1));
        assertFalse(matcher1.hasSameMethod(matcher2));
        assertFalse(matcher2.hasSameMethod(matcher1));
        assertFalse(matcher2.hasSameMethod(matcher2));
    }

    @Test
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        Object[] arguments = new Object[] { "foo", 123L };

        when(invocation.getMethod()).thenReturn(method);
        when(invocation.getArguments()).thenReturn(arguments);

        List<Matcher> matchers = Arrays.asList(value("bar"), isNull());
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        String result = new PrintSettings().print(matcher.getMatchers(), matcher.getInvocation());

        assertEquals("Invocation with method: public java.lang.String java.lang.String.concat(java.lang.String,java.lang.String)", result);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        Object[] arguments = new Object[] { "foo", 123L };

        when(invocation.getArguments()).thenReturn(arguments);

        List<Matcher> matchers = Arrays.asList(argThat(new ArgumentCaptureMatcher("foo")));

        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        matcher.captureArgumentsFrom(invocation);

        assertEquals("foo", matcher.getMatchers().get(0).getActualValue());
    }

    @Test
    public void testCaptureArgumentsFromWithVarargs() {
        Invocation invocation = mock(Invocation.class);
        Object[] arguments = new Object[] { new Object[] { "foo", "bar" } };

        when(invocation.getArguments()).thenReturn(arguments);

        List<Matcher> matchers = Arrays.asList(argThat(new ArgumentCaptureMatcher("bar")));

        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        matcher.captureArgumentsFrom(invocation);

        assertEquals("bar", matcher.getMatchers().get(0).getActualValue());
    }

    private static class ArgumentCaptureMatcher implements Matcher<Object> {
        private String expectedValue;

        public ArgumentCaptureMatcher(String expectedValue) {
            this.expectedValue = expectedValue;
        }

        @Override
        public boolean matches(Object argument) {
            if (argument instanceof Object[]) {
                Object[] args = (Object[]) argument;
                for (Object arg : args) {
                    if (arg.equals(expectedValue)) {
                        return true;
                    }
                }
            } else {
                if (argument.equals(expectedValue)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("matches argument with value '").appendValue(expectedValue).appendText('\'');
        }

        @Override
        public Object getActualValue() {
            return expectedValue;
        }
    }
}
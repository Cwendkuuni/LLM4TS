package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.ArgumentMatchers.argThat;
import static org.mockito.internal.matchers.ArgumentMatchers.isNull;
import static org.mockito.internal.matchers.ArgumentMatchers.isA;
import static org.mockito.internal.matchers.ArgumentMatchers.isInstanceOf;
import static org.mockito.internal.matchers.ArgumentMatchers.not;
import static org.mockito.internal.matchers.ArgumentMatchers.same;
import static org.mockito.internal.matchers.ArgumentsMatcher.argumentsWithSameTypesAs;
import static org.mockito.internal.matchers.ArgumentsMatcher.invocationWith;
import static org.mockito.internal.matchers.ArgumentsMatcher.methodWithName;
import static org.mockito.internal.matchers.ArgumentsMatcher.notNull;
import static org.mockito.internal.matchers.ArgumentsMatcher.withArrayType;
import static org.mockito.internal.matchers.ArgumentsMatcher.withLength;
import static org.mockito.internal.matchers.ArgumentsMatcher.withType;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.matchers.ArgumentsProcessor;
import org.mockito.internal.matchers.VarargMatcher;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.invocation.Invocation;
import org.mockito.internal.reporting.PrintSettings;

public class InvocationMatcherTest {

    private InvocationMatcher matcher;
    private Invocation invocation;
    private Object mock;
    private Object[] arguments;
    private Method method;

    @Before
    public void setup() {
        mock = Mockito.mock(String.class);
        method = Mockito.method(String.class, "foo", new Class[] { int.class, String[].class });
        arguments = new Object[] { 1, new Object[] { "arg1", "arg2" } };
        invocation = Mockito.mock(Invocation.class);
        Mockito.when(invocation.getMock()).thenReturn(mock);
        Mockito.when(invocation.getMethod()).thenReturn(method);
        Mockito.when(invocation.getArguments()).thenReturn(arguments);
        Matcher<Invocation> matchers = argThat(invocationWith(methodWithName("foo")));
        matcher = new InvocationMatcher(invocation, Arrays.asList(matchers));
    }

    @Test
    public void testMatches() {
        Invocation anotherInvocation = Mockito.mock(Invocation.class);
        Mockito.when(anotherInvocation.getMock()).thenReturn(mock);
        Mockito.when(anotherInvocation.getMethod()).thenReturn(method);
        Mockito.when(anotherInvocation.getArguments()).thenReturn(arguments);

        assertTrue(matcher.matches(invocation));
        assertFalse(matcher.matches(anotherInvocation));
    }

    @Test
    public void testHasSameMethod() {
        Invocation anotherInvocation = Mockito.mock(Invocation.class);
        Mockito.when(anotherInvocation.getMock()).thenReturn(mock);
        Mockito.when(anotherInvocation.getMethod()).thenReturn(new Method(String.class, "foo", new Class[] { String[].class }));

        assertFalse(matcher.hasSameMethod(anotherInvocation));

        Mockito.when(anotherInvocation.getMethod()).thenReturn(method);
        assertTrue(matcher.hasSameMethod(anotherInvocation));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation anotherInvocation = Mockito.mock(Invocation.class);
        Mockito.when(anotherInvocation.getMock()).thenReturn(mock);
        Mockito.when(anotherInvocation.getMethod()).thenReturn(new Method(String.class, "bar", new Class[] { int.class, String[].class }));

        assertFalse(matcher.hasSimilarMethod(anotherInvocation));

        Mockito.when(anotherInvocation.getMethod()).thenReturn(new Method(String.class, "foo", new Class[] { String[].class }));
        assertTrue(matcher.hasSimilarMethod(anotherInvocation));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation anotherInvocation = Mockito.mock(Invocation.class);
        Mockito.when(anotherInvocation.getMock()).thenReturn(mock);
        Mockito.when(anotherInvocation.getMethod()).thenReturn(method);
        Mockito.when(anotherInvocation.getArguments()).thenReturn(new Object[] { 1, new Object[] { "arg1", "arg2" } });

        InvocationMatcher matcherWithArguments = new InvocationMatcher(anotherInvocation);
        List<Matcher> matchers = matcherWithArguments.getMatchers();
        Matcher<Object> captureArgMatcher = argThat(isA(String.class));
        matchers.set(0, argThat(new CapturesArguments() {
            @Override
            public void captureFrom(Object argument) {
                assertTrue(argument.equals("arg1"));
            }
        }));
        matchers.set(1, captureArgMatcher);

        matcherWithArguments.captureArgumentsFrom(anotherInvocation);

        Mockito.verify(anotherInvocation).getArgumentAt(0, String.class);
        Mockito.verify(captureArgMatcher).matches(argThat(isInstanceOf(String.class)));
        Mockito.verify(captureArgMatcher).matches("arg1");
    }

    @Test
    public void testCaptureArgumentsFromVarargs() {
        Invocation anotherInvocation = Mockito.mock(Invocation.class);
        Mockito.when(anotherInvocation.getMock()).thenReturn(mock);
        Mockito.when(anotherInvocation.getMethod()).thenReturn(method);
        Object[] argumentsVarargs = new Object[] { 1, "arg1", "arg2" };
        Mockito.when(anotherInvocation.getArguments()).thenReturn(argumentsVarargs);

        InvocationMatcher matcherWithArguments = new InvocationMatcher(anotherInvocation);
        List<Matcher> matchers = matcherWithArguments.getMatchers();
        Matcher<Object> captureArgMatcher = argThat(isA(String.class));
        matchers.set(0, argThat(new CapturesArguments() {
            @Override
            public void captureFrom(Object argument) {
                assertTrue(Arrays.asList(argumentsVarargs).contains(argument));
            }
        }));
        matchers.set(1, captureArgMatcher);

        matcherWithArguments.captureArgumentsFrom(anotherInvocation);

        Mockito.verify(anotherInvocation).getArguments();
        Mockito.verify(captureArgMatcher).matches(argThat(isInstanceOf(String.class)));
        Mockito.verify(captureArgMatcher).matches("arg1");
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = Arrays.asList(invocation, invocation, invocation);
        List<InvocationMatcher> matchers = InvocationMatcher.createFrom(invocations);
        InvocationMatcher firstMatcher = matchers.get(0);
        InvocationMatcher secondMatcher = matchers.get(1);

        assertTrue(firstMatcher.matches(invocation));
        assertTrue(secondMatcher.matches(invocation));
        assertFalse(firstMatcher.matches(anotherInvocation));
        assertFalse(secondMatcher.matches(anotherInvocation));
    }

    @Test
    public void testCreateFromWithEmptyList() {
        List<Invocation> invocations = Collections.emptyList();
        List<InvocationMatcher> matchers = InvocationMatcher.createFrom(invocations);

        InvocationMatcher matcher = matchers.get(0);
        Invocation anotherInvocation = Mockito.mock(Invocation.class);
        Mockito.when(anotherInvocation.getMock()).thenReturn(mock);
        Mockito.when(anotherInvocation.getMethod()).thenReturn(method);
        Mockito.when(anotherInvocation.getArguments()).thenReturn(arguments);

        assertTrue(matcher.matches(invocation));
        assertFalse(matcher.matches(anotherInvocation));
    }

    @Test
    public void testCreateFromWithNullInvocationList() {
        List<Invocation> invocations = null;
        List<InvocationMatcher> matchers = InvocationMatcher.createFrom(invocations);

        InvocationMatcher matcher = matchers.get(0);
        Invocation anotherInvocation = Mockito.mock(Invocation.class);
        Mockito.when(anotherInvocation.getMock()).thenReturn(mock);
        Mockito.when(anotherInvocation.getMethod()).thenReturn(method);
        Mockito.when(anotherInvocation.getArguments()).thenReturn(arguments);

        assertTrue(matcher.matches(invocation));
        assertFalse(matcher.matches(anotherInvocation));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCaptureArgumentsFromVarargsWithMoreThanOneMatcher() {
        Invocation anotherInvocation = Mockito.mock(Invocation.class);
        Mockito.when(anotherInvocation.getMock()).thenReturn(mock);
        Mockito.when(anotherInvocation.getMethod()).thenReturn(method);
        Object[] argumentsVarargs = new Object[] { 1, "arg1", "arg2" };
        Mockito.when(anotherInvocation.getArguments()).thenReturn(argumentsVarargs);

        InvocationMatcher matcherWithArguments = new InvocationMatcher(anotherInvocation);
        List<Matcher> matchers = matcherWithArguments.getMatchers();
        Matcher<Object> captureArgMatcher1 = argThat(isA(String.class));
        Matcher<Object> captureArgMatcher2 = argThat(isA(String.class));
        matchers.set(0, argThat(new CapturesArguments() {
            @Override
            public void captureFrom(Object argument) {
                assertTrue(Arrays.asList(argumentsVarargs).contains(argument));
            }
        }));
        matchers.set(1, captureArgMatcher1);
        matchers.set(2, captureArgMatcher2);

        matcherWithArguments.captureArgumentsFrom(anotherInvocation);

        Mockito.verify(anotherInvocation).getArguments();
    }
}
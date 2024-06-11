package org.mockito.internal.invocation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.exceptions.base.MockitoBaseException;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.Arguments;
import org.mockito.internal.matchers.ArgumentsComparator;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;

public class InvocationMatcherTest {

    @Test
    public void testNewInstanceWithNoMatchers() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);

        InvocationMatcher matcher = new InvocationMatcher(invocation);
        assertEquals(invocation, matcher.getInvocation());
        assertEquals(method, matcher.getMethod());
        assertNotNull(matcher.getMatchers());
        assertTrue(matcher.getMatchers().isEmpty());
    }

    @Test
    public void testNewInstanceWithMatchers() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        Matcher<String> matcher = eq("test");

        InvocationMatcher matcherWithMatchers = new InvocationMatcher(invocation, Arrays.asList(matcher));
        assertEquals(invocation, matcherWithMatchers.getInvocation());
        assertEquals(method, matcherWithMatchers.getMethod());
        assertSame(matcher, matcherWithMatchers.getMatchers().get(0));
        assertTrue(matcherWithMatchers.getMatchers().size() == 1);
    }

    @Test
    public void testNewInstanceWithEmptyMatcherList() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);

        InvocationMatcher matcher = new InvocationMatcher(invocation);
        assertEquals(invocation, matcher.getInvocation());
        assertEquals(method, matcher.getMethod());
        assertTrue(matcher.getMatchers().isEmpty());
    }

    @Test
    public void testMatchesWhenInvocationsHaveSameMethodAndMock() {
        Invocation invocation1 = mock(Invocation.class);
        Method method = mock(Method.class);
        when(method.getName()).thenReturn("testMethod");
        when(invocation1.getMethod()).thenReturn(method);
        when(invocation1.getMock()).thenReturn(mock(Object.class));

        Invocation invocation2 = mock(Invocation.class);
        when(invocation2.getMethod()).thenReturn(method);
        when(invocation2.getMock()).thenReturn(mock(Object.class));

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        assertTrue(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testMatchesWhenInvocationsHaveSameMethodArgumentsButDifferentMocks() {
        Invocation invocation1 = mock(Invocation.class);
        Method method = mock(Method.class);
        when(method.getName()).thenReturn("testMethod");
        when(invocation1.getMethod()).thenReturn(method);
        when(invocation1.getMock()).thenReturn(mock(Object1.class));

        Invocation invocation2 = mock(Invocation.class);
        when(invocation2.getMethod()).thenReturn(method);
        when(invocation2.getMock()).thenReturn(mock(Object2.class));

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        assertFalse(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testMatchesWhenInvocationsHaveDifferentMethods() {
        Invocation invocation1 = mock(Invocation.class);
        Method method1 = mock(Method.class);
        when(method1.getName()).thenReturn("testMethod1");
        when(invocation1.getMethod()).thenReturn(method1);

        Invocation invocation2 = mock(Invocation.class);
        Method method2 = mock(Method.class);
        when(method2.getName()).thenReturn("testMethod2");
        when(invocation2.getMethod()).thenReturn(method2);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        assertFalse(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test(expected = MockitoBaseException.class)
    public void testMatchesWhenInvocationsHaveDifferentMethodsButSameMock() {
        Invocation invocation1 = mock(Invocation.class);
        Method method1 = mock(Method.class);
        when(method1.getName()).thenReturn("testMethod1");
        when(invocation1.getMethod()).thenReturn(method1);

        Invocation invocation2 = mock(Invocation.class);
        Method method2 = mock(Method.class);
        when(method2.getName()).thenReturn("testMethod2");
        when(invocation2.getMock()).thenReturn(invocation1.getMock());

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        // Should throw an exception
        invocationMatcher1.matches(invocationMatcher2);
    }

    @Test
    public void testMatchesWhenArgumentsMatch() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        Object[] arguments = new Object[]{1, "test"};
        when(method.getName()).thenReturn("testMethod");
        when(invocation.getMethod()).thenReturn(method);
        when(invocation.getArguments()).thenReturn(arguments);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(Matchers.eq(1), Matchers.eq("test")));

        assertTrue(invocationMatcher.matches(invocation));
    }

    @Test
    public void testMatchesWhenArgumentsDoNotMatch() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        Object[] arguments = new Object[]{1, "test"};
        when(method.getName()).thenReturn("testMethod");
        when(invocation.getMethod()).thenReturn(method);
        when(invocation.getArguments()).thenReturn(arguments);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(Matchers.eq(2), Matchers.eq("test")));

        assertFalse(invocationMatcher.matches(invocation));
    }

    @Test
    public void testMatchesWhenArgumentsMatchWithCaptor() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        Object[] arguments = new Object[]{1, "test"};
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        when(method.getName()).thenReturn("testMethod");
        when(invocation.getMethod()).thenReturn(method);
        when(invocation.getArguments()).thenReturn(arguments);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(Matchers.eq(1), captor));

        verify(invocation).getArguments();
        assertEquals("test", captor.getValue());
        assertTrue(invocationMatcher.matches(invocation));
    }

    @Test
    public void testMatchesWhenArgumentsDoNotMatchWithCaptor() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        Object[] arguments = new Object[]{1, "test"};
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        when(method.getName()).thenReturn("testMethod");
        when(invocation.getMethod()).thenReturn(method);
        when(invocation.getArguments()).thenReturn(arguments);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(Matchers.eq(2), captor));

        verifyZeroInteractions(invocation);
        assertNull(captor.getValue());
        assertFalse(invocationMatcher.matches(invocation));
    }

    @Test
    public void testToStringTestWithMatchers() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(method.getName()).thenReturn("testMethod");
        when(invocation.getMethod()).thenReturn(method);

        Matcher<String> matcher = eq("test");

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(matcher));

        String result = invocationMatcher.toString();

        String expected = "InvocationMatcher:\n" +
                "invocation:\n" +
                "  mock: <MockMethod testMethod>\n" +
                "  arguments:\n" +
                "    [0] = <Arg Matches: org.hamcrest.core.IsEqual>\n" +
                "      expected: <" + matcher.description() + ">\n" +
                "      isMatcher: true\n" +
                "      matcher:\n" +
                "        class: org.hamcrest.core.IsEqual\n" +
                "        description: test\n" +
                "toString settings: default\n";
        assertEquals(expected, result);
    }

    @Test
    public void testToStringTestWithoutMatchers() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(method.getName()).thenReturn("testMethod");
        when(invocation.getMethod()).thenReturn(method);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        String result = invocationMatcher.toString();

        String expected = "InvocationMatcher:\n" +
                "invocation:\n" +
                "  mock: <MockMethod testMethod>\n" +
                "  arguments: []\n" +
                "toString settings: default\n";
        assertEquals(expected, result);
    }

    @Test
    public void testToStringTestWithCustomPrintSettings() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(method.getName()).thenReturn("testMethod");
        when(invocation.getMethod()).thenReturn(method);

        Matcher<String> matcher = eq("test");

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(matcher));

        String result = invocationMatcher.toString(new PrintSettings().withPrintCapturedArguments(false).withPrintStacktrace(false));

        String expected = "InvocationMatcher:\n" +
                "invocation:\n" +
                "  mock: <MockMethod testMethod>\n" +
                "toString settings: PrintCapturedArguments(false), PrintStacktrace(false)\n";
        assertEquals(expected, result);
    }

    @Test
    public void testCaptureArgumentsFromInvocation() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        Object[] arguments = new Object[]{1, "test"};
        when(method.getName()).thenReturn("testMethod");
        when(invocation.getMethod()).thenReturn(method);
        when(invocation.getArguments()).thenReturn(arguments);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        invocationMatcher.captureArgumentsFrom(invocation);

        assertEquals("test", captor.getValue());
    }

    @Test
    public void testCreateFromList() {
        Invocation invocation1 = mock(Invocation.class);
        Method method1 = mock(Method.class);
        when(method1.getName()).thenReturn("testMethod1");
        when(invocation1.getMethod()).thenReturn(method1);

        Invocation invocation2 = mock(Invocation.class);
        Method method2 = mock(Method.class);
        when(method2.getName()).thenReturn("testMethod2");
        when(invocation2.getMethod()).thenReturn(method2);

        List<Invocation> invocations = Arrays.asList(invocation1, invocation2);

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        InvocationMatcher invocationMatcher1 = invocationMatchers.get(0);
        InvocationMatcher invocationMatcher2 = invocationMatchers.get(1);

        assertEquals(invocation1, invocationMatcher1.getInvocation());
        assertEquals(method1, invocationMatcher1.getMethod());
        assertEquals(0, invocationMatcher1.getMatchers().size());

        assertEquals(invocation2, invocationMatcher2.getInvocation());
        assertEquals(method2, invocationMatcher2.getMethod());
        assertEquals(0, invocationMatcher2.getMatchers().size());
    }
}
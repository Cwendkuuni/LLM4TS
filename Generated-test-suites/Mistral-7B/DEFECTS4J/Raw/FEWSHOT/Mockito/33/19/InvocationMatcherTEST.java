package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.ArgumentMatchers.argThat;
import static org.mockito.internal.matchers.Arguments.not;
import static org.mockito.internal.matchers.Arguments.same;
import static org.mockito.internal.reporting.PrintSettings.printNulls;
import static org.mockito.internal.reporting.PrintSettings.printTypes;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.verification.VerificationMode;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    private Object mock;

    @Test
    public void testNewInstance() {
        mock = Mockito.mock(Object.class);
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Collections.singletonList(same(mock));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        InvocationMatcher invocationMatcherEmpty = new InvocationMatcher(invocation);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
        assertNull(invocationMatcherEmpty.getMatchers());
    }

    @Test
    public void testMatches_sameInvocation() {
        mock = Mockito.mock(Object.class);
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Collections.singletonList(same(mock));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertTrue(invocationMatcher.matches(invocation));
    }

    @Test
    public void testMatches_differentInvocation_differentMock() {
        mock = Mockito.mock(Object.class);
        Invocation invocation1 = Mockito.mock(Invocation.class);
        List<Matcher> matchers1 = Collections.singletonList(same(mock));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, matchers1);

        mock = Mockito.mock(Object.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        List<Matcher> matchers2 = Collections.singletonList(same(mock));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, matchers2);

        assertFalse(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testMatches_differentInvocation_sameMock() {
        mock = Mockito.mock(Object.class);
        Invocation invocation1 = Mockito.mock(Invocation.class);
        List<Matcher> matchers1 = Collections.singletonList(same(mock));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, matchers1);

        Invocation invocation2 = Mockito.mock(Invocation.class);
        List<Matcher> matchers2 = Collections.singletonList(same(mock));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, matchers2);

        assertTrue(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testMatches_differentInvocations_sameMethod_sameArguments() {
        mock = Mockito.mock(Object.class);
        Method method = Mockito.mock(Method.class);
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Collections.singletonList(same(mock));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1, matchers);

        Mockito.when(invocation1.getMethod()).thenReturn(method);
        Mockito.when(invocation2.getMethod()).thenReturn(method);

        assertTrue(invocationMatcher.matches(invocation2));
    }

    @Test
    public void testMatches_differentInvocations_sameMethod_differentArguments() {
        mock = Mockito.mock(Object.class);
        Method method = Mockito.mock(Method.class);
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Collections.singletonList(same(mock));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1, matchers);

        Mockito.when(invocation1.getMethod()).thenReturn(method);
        Mockito.when(invocation2.getMethod()).thenReturn(method);

        Object[] arguments1 = new Object[] { 1, 2 };
        Object[] arguments2 = new Object[] { 2, 1 };

        Mockito.when(invocation1.getArguments()).thenReturn(arguments1);
        Mockito.when(invocation2.getArguments()).thenReturn(arguments2);

        assertFalse(invocationMatcher.matches(invocation2));
    }

    @Test
    public void testMatches_differentInvocations_sameMethod_overloaded_sameArguments() {
        mock = Mockito.mock(Object.class);
        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Collections.singletonList(same(mock));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1, matchers);

        Mockito.when(invocation1.getMethod()).thenReturn(method1);
        Mockito.when(invocation2.getMethod()).thenReturn(method2);

        assertFalse(invocationMatcher.matches(invocation2));

        Mockito.when(method1.getName()).thenReturn("method1");
        Mockito.when(method2.getName()).thenReturn("method1");

        Object[] arguments1 = new Object[] { 1, 2 };
        Object[] arguments2 = new Object[] { 2, 1 };

        Mockito.when(invocation1.getArguments()).thenReturn(arguments1);
        Mockito.when(invocation2.getArguments()).thenReturn(arguments2);

        assertTrue(invocationMatcher.matches(invocation2));
    }

    @Test
    public void testMatches_differentInvocations_sameMethod_overloaded_differentArguments() {
        mock = Mockito.mock(Object.class);
        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Collections.singletonList(same(mock));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1, matchers);

        Mockito.when(invocation1.getMethod()).thenReturn(method1);
        Mockito.when(invocation2.getMethod()).thenReturn(method2);

        assertFalse(invocationMatcher.matches(invocation2));

        Mockito.when(method1.getName()).thenReturn("method1");
        Mockito.when(method2.getName()).thenReturn("method2");

        Object[] arguments1 = new Object[] { 1, 2 };
        Object[] arguments2 = new Object[] { 2, 1 };

        Mockito.when(invocation1.getArguments()).thenReturn(arguments1);
        Mockito.when(invocation2.getArguments()).thenReturn(arguments2);

        assertFalse(invocationMatcher.matches(invocation2));
    }

    @Test
    public void testHasSameMethod() {
        mock = Mockito.mock(Object.class);
        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);

        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Mockito.when(invocation.getMethod()).thenReturn(method1);

        assertTrue(invocationMatcher.hasSameMethod(invocation));

        Mockito.when(invocation.getMethod()).thenReturn(method2);

        assertFalse(invocationMatcher.hasSameMethod(invocation));
    }

    @Test
    public void testHasSameMethod_overloaded() {
        mock = Mockito.mock(Object.class);
        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);

        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Mockito.when(invocation.getMethod()).thenReturn(method1);

        Method method3 = Mockito.mock(Method.class);
        Mockito.when(method3.getName()).thenReturn(method1.getName());

        Mockito.when(invocation.getMethod()).thenReturn(method3);

        assertTrue(invocationMatcher.hasSameMethod(invocation));
    }

    @Test
    public void testHasSimilarMethod() {
        mock = Mockito.mock(Object.class);
        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);

        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Mockito.when(invocation.getMethod()).thenReturn(method1);

        Invocation invocationOther = Mockito.mock(Invocation.class);
        Mockito.when(invocationOther.getMethod()).thenReturn(method2);

        assertTrue(invocationMatcher.hasSimilarMethod(invocationOther));

        Mockito.when(invocation.getMethod()).thenReturn(method2);

        assertFalse(invocationMatcher.hasSimilarMethod(invocationOther));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        mock = Mockito.mock(Object.class);
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = new LinkedList<>();
        Matcher firstMatcher = argThat(arg -> arg instanceof ArgumentCaptor);
        matchers.add(firstMatcher);
        matchers.add(same(mock));

        ArgumentCaptor<Object> argumentCaptor = Mockito.argThat(not(same(mock)));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        ArgumentCaptor<Object> capturedArgument = ArgumentCaptor.forClass(Object.class);
        invocationMatcher.captureArgumentsFrom(invocation);

        assertEquals(mock, capturedArgument.getValue());
    }

    @Test
    public void testCreateFrom() {
        mock = Mockito.mock(Object.class);
        List<Invocation> invocations = new LinkedList<>();
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        Mockito.when(invocation1.getArguments()).thenReturn(new Object[] { mock });
        Mockito.when(invocation2.getArguments()).thenReturn(new Object[] { mock });

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        assertEquals(invocationMatcher1, invocationMatchers.get(0));
        assertEquals(invocationMatcher2, invocationMatchers.get(1));
    }

    private void assertEquals(Object expected, Object actual) {
        assertTrue(expected.equals(actual));
    }

    private void assertEquals(Invocation expected, Invocation actual) {
        assertEquals(expected.getMethod(), actual.getMethod());
        assertEquals(expected.getArguments(), actual.getArguments());
        assertEquals(expected.isVerified(), actual.isVerified());
        assertEquals(expected.getLocation(), actual.getLocation());
    }
}
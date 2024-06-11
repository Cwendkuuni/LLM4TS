package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.ArgumentsMatcher.not;
import static org.mockito.internal.matchers.ArgumentsMatcher.same;
import static org.mockito.internal.matchers.ArgumentsMatcher.sameInstance;
import static org.mockito.internal.matchers.ArgumentsMatcher.value;
import static org.mockito.internal.reporting.PrintSettings.print;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.Invocation;
import org.mockito.mock.MockComponent;

public class InvocationMatcherTest {

    private MockComponent mock;
    private Invocation invocation;
    private InvocationMatcher invocationMatcher;
    private List<Matcher> matchers;

    @Before
    public void setup() {
        mock = Mockito.mock(MockComponent.class);
        invocation = Mockito.mock(Invocation.class);
        matchers = Arrays.asList(value("foo"), sameInstance(mock), not(value("bar")));
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void testToString() {
        String expected = print(matchers, invocation);
        String actual = invocationMatcher.toString();
        assertTrue(expected.equals(actual));
    }

    @Test
    public void testMatchesWithSameInvocation() {
        assertTrue(invocationMatcher.matches(invocationMatcher));
    }

    @Test
    public void testMatchesWithSameMethodAndMock() {
        Invocation otherInvocation = Mockito.mock(Invocation.class);
        Method method = Mockito.mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(otherInvocation.getMethod()).thenReturn(method);
        when(invocation.getMock()).thenReturn(mock);
        when(otherInvocation.getMock()).thenReturn(mock);
        assertTrue(invocationMatcher.matches(otherInvocation));
    }

    @Test
    public void testMatchesWithDifferentMethodAndMock() {
        Invocation otherInvocation = Mockito.mock(Invocation.class);
        Method otherMethod = Mockito.mock(Method.class);
        when(invocation.getMethod()).thenReturn(mock.getMethod("method1", Integer.class));
        when(otherInvocation.getMethod()).thenReturn(otherMethod);
        assertFalse(invocationMatcher.matches(otherInvocation));
    }

    @Test
    public void testMatchesWithSameMethodButDifferentArguments() {
        Invocation otherInvocation = Mockito.mock(Invocation.class);
        Method method = Mockito.mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(otherInvocation.getMethod()).thenReturn(method);
        when(invocation.getMock()).thenReturn(mock);
        when(otherInvocation.getMock()).thenReturn(mock);
        Object[] otherArguments = new Object[]{1, "two"};
        when(otherInvocation.getArguments()).thenReturn(otherArguments);
        assertFalse(invocationMatcher.matches(otherInvocation));
    }

    @Test
    public void testMatchesWithSameMethodAndArguments() {
        Invocation otherInvocation = Mockito.mock(Invocation.class);
        Method method = Mockito.mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(otherInvocation.getMethod()).thenReturn(method);
        when(invocation.getMock()).thenReturn(mock);
        when(otherInvocation.getMock()).thenReturn(mock);
        when(invocation.getArguments()).thenReturn(new Object[]{1, 2});
        when(otherInvocation.getArguments()).thenReturn(new Object[]{1, 2});
        assertTrue(invocationMatcher.matches(otherInvocation));
    }

    @Test
    public void testMatchesWithDifferentMethodAndArguments() {
        Invocation otherInvocation = Mockito.mock(Invocation.class);
        Method otherMethod = Mockito.mock(Method.class);
        when(invocation.getMethod()).thenReturn(mock.getMethod("method1", Integer.class));
        when(otherInvocation.getMethod()).thenReturn(otherMethod);
        when(otherMethod.getName()).thenReturn("method2");
        when(invocation.getMock()).thenReturn(mock);
        when(otherInvocation.getMock()).thenReturn(mock);
        when(invocation.getArguments()).thenReturn(new Object[]{1, 2});
        when(otherInvocation.getArguments()).thenReturn(new Object[]{3, 4});
        assertFalse(invocationMatcher.matches(otherInvocation));
    }

    @Test
    public void testMatchesWithSameMethodButDifferentVerifiedStatus() {
        Invocation otherInvocation = Mockito.mock(Invocation.class);
        Method method = Mockito.mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(otherInvocation.getMethod()).thenReturn(method);
        when(invocation.getMock()).thenReturn(mock);
        when(otherInvocation.getMock()).thenReturn(mock);
        when(invocation.isVerified()).thenReturn(true);
        when(otherInvocation.isVerified()).thenReturn(false);
        assertFalse(invocationMatcher.matches(otherInvocation));
    }

    @Test
    public void testMatchesWithDifferentMock() {
        Invocation otherInvocation = Mockito.mock(Invocation.class);
        Method method = Mockito.mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(otherInvocation.getMethod()).thenReturn(method);
        when(invocation.getMock()).thenReturn(mock);
        when(otherInvocation.getMock()).thenReturn(Mockito.mock(MockComponent.class));
        assertFalse(invocationMatcher.matches(otherInvocation));
    }

    @Test
    public void testMatchesWithNullInvocation() {
        Invocation otherInvocation = null;
        assertFalse(invocationMatcher.matches(otherInvocation));
    }

    @Test
    public void testMatchesWithArgumentMatcherThatCapturesArguments() {
        Invocation otherInvocation = Mockito.mock(Invocation.class);
        Matcher capturedArgumentMatcher = Mockito.mock(Matcher.class);
        when(invocationMatcher.getMatchers()).thenReturn(Collections.singletonList(capturedArgumentMatcher));
        when(otherInvocation.getArguments()).thenReturn(new Object[]{1, 2});
        invocationMatcher.captureArgumentsFrom(otherInvocation);
        assertTrue(capturedArgumentMatcher.matches(1));
    }

    @Test
    public void testMatchesWithVarargMatcher() {
        Invocation otherInvocation = Mockito.mock(Invocation.class);
        Method method = Mockito.mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(otherInvocation.getMethod()).thenReturn(method);
        when(invocation.getMock()).thenReturn(mock);
        when(otherInvocation.getMock()).thenReturn(mock);
        Object[] otherArguments = new Object[]{1, 2, "three"};
        when(otherInvocation.getArguments()).thenReturn(otherArguments);
        InvocationMatcher varargMatcher = new InvocationMatcher(invocation, Arrays.asList(same(new Object[]{1, 2, "three"})));
        assertTrue(varargMatcher.matches(otherInvocation));
    }

    @Test
    public void testMatchesWithVarargMatcherThatCapturesArguments() {
        Invocation otherInvocation = Mockito.mock(Invocation.class);
        Method method = Mockito.mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(otherInvocation.getMethod()).thenReturn(method);
        when(invocation.getMock()).thenReturn(mock);
        when(otherInvocation.getMock()).thenReturn(mock);
        Object[] otherArguments = new Object[]{1, 2, "three"};
        when(otherInvocation.getArguments()).thenReturn(otherArguments);
        Matcher capturedMatcher = Mockito.mock(Matcher.class);
        when(invocationMatcher.getMatchers().get(0)).thenReturn(new VarargMatcher(capturedMatcher));
        invocationMatcher.captureArgumentsFrom(otherInvocation);
        assertTrue(capturedMatcher.matches(Arrays.asList(1, "three")));
    }

    @Test
    public void testMatchesWithVarargMatcherThatDoesNotCaptureArguments() {
        Invocation otherInvocation = Mockito.mock(Invocation.class);
        Method method = Mockito.mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(otherInvocation.getMethod()).thenReturn(method);
        when(invocation.getMock()).thenReturn(mock);
        when(otherInvocation.getMock()).thenReturn(mock);
        Object[] otherArguments = new Object[]{1, 2, "three"};
        when(otherInvocation.getArguments()).thenReturn(otherArguments);
        Matcher capturedMatcher = Mockito.mock(Matcher.class);
        when(invocationMatcher.getMatchers().get(0)).thenReturn(new VarargMatcher(capturedMatcher, false));
        invocationMatcher.captureArgumentsFrom(otherInvocation);
        assertFalse(capturedMatcher.matches(otherArguments));
    }
}
package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.internal.matchers.VarargMatcher;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

public class InvocationMatcherTest {

    @Test
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(Matchers.argThat(m -> m.equals("foo")));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        String actual = invocationMatcher.toString();
        String expected = "arguments: [argThat(foo)]";
        assertEquals(expected, actual);
    }

    @Test
    public void testMatchesCharSequenceWhenMethodsAreTheSameAndArgumentsMatch() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(method.getName()).thenReturn("testMethod");
        when(invocation.getMethod()).thenReturn(method);

        Object[] arguments = new Object[]{ "foo" };
        when(invocation.getArguments()).thenReturn(arguments);

        List<Matcher> matchers = new LinkedList<>();
        matchers.add(Matchers.argThat(m -> m.equals("foo")));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Invocation actualInvocation = mock(Invocation.class);
        when(actualInvocation.getMethod()).thenReturn(method);
        Object[] actualArguments = new Object[]{ "foo" };

        verify(invocationMatcher).matches(actualInvocation);
    }

    @Test
    public void testMatchesCharSequenceWhenMethodsAreDifferent() {
        Invocation invocation = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(method1.getName()).thenReturn("testMethod1");
        when(method2.getName()).thenReturn("testMethod2");
        when(invocation.getMethod()).thenReturn(method1);

        Object[] arguments = new Object[]{ "foo" };
        when(invocation.getArguments()).thenReturn(arguments);

        List<Matcher> matchers = new LinkedList<>();
        matchers.add(Matchers.argThat(m -> m.equals("foo")));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Invocation actualInvocation = mock(Invocation.class);
        when(actualInvocation.getMethod()).thenReturn(method2);
        Object[] actualArguments = new Object[]{ "bar" };

        assertFalse(invocationMatcher.matches(actualInvocation));
    }

    @Test
    public void testMatchesObjectArrayWhenMethodsAreTheSameAndArgumentsMatch() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(method.getName()).thenReturn("testMethod");
        when(invocation.getMethod()).thenReturn(method);

        Object[] arguments = new Object[]{ 1, 2, 3 };
        when(invocation.getArguments()).thenReturn(arguments);

        List<Matcher> matchers = new LinkedList<>();
        matchers.add(Matchers.argThat(m -> m.equals(1)));
        matchers.add(Matchers.argThat(m -> m.equals(3)));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Invocation actualInvocation = mock(Invocation.class);
        when(actualInvocation.getMethod()).thenReturn(method);
        Object[] actualArguments = new Object[]{ 1, 2, 3 };

        verify(invocationMatcher).matches(actualInvocation);
    }

    @Test
    public void testMatchesObjectArrayWhenMethodsAreTheSameButArgumentsAreDifferent() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(method.getName()).thenReturn("testMethod");
        when(invocation.getMethod()).thenReturn(method);

        Object[] arguments = new Object[]{ 1, 2, 3 };
        when(invocation.getArguments()).thenReturn(arguments);

        List<Matcher> matchers = new LinkedList<>();
        matchers.add(Matchers.argThat(m -> m.equals(1)));
        matchers.add(Matchers.argThat(m -> m.equals(3)));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Invocation actualInvocation = mock(Invocation.class);
        when(actualInvocation.getMethod()).thenReturn(method);
        Object[] actualArguments = new Object[]{ 1, 4, 3 };

        assertFalse(invocationMatcher.matches(actualInvocation));
    }

    @Test
    public void testMatchesObjectArrayWhenMethodsAreDifferent() {
        Invocation invocation = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(method1.getName()).thenReturn("testMethod1");
        when(method2.getName()).thenReturn("testMethod2");
        when(invocation.getMethod()).thenReturn(method1);

        Object[] arguments = new Object[]{ 1, 2, 3 };
        when(invocation.getArguments()).thenReturn(arguments);

        List<Matcher> matchers = new LinkedList<>();
        matchers.add(Matchers.argThat(m -> m.equals(1)));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Invocation actualInvocation = mock(Invocation.class);
        when(actualInvocation.getMethod()).thenReturn(method2);
        Object[] actualArguments = new Object[]{ 4, 2, 3 };

        assertFalse(invocationMatcher.matches(actualInvocation));
    }

    @Test
    public void testHasSameMethodWhenMethodsAreTheSame() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(method.getName()).thenReturn("testMethod");
        when(invocation.getMethod()).thenReturn(method);

        Invocation candidate = mock(Invocation.class);
        Method candidateMethod = mock(Method.class);
        when(candidateMethod.getName()).thenReturn("testMethod");
        when(candidate.getMethod()).thenReturn(candidateMethod);

        assertTrue(new InvocationMatcher(invocation).hasSameMethod(candidate));
    }

    @Test
    public void testHasSameMethodWhenMethodsAreDifferent() {
        Invocation invocation = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(method1.getName()).thenReturn("testMethod1");
        when(method2.getName()).thenReturn("testMethod2");
        when(invocation.getMethod()).thenReturn(method1);

        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(method2);

        assertFalse(new InvocationMatcher(invocation).hasSameMethod(candidate));
    }

    @Test
    public void testCaptureArgumentsFromInvocation() {
        Invocation invocation = spy(mock(Invocation.class));
        Object[] rawArguments = new Object[]{ 1, "foo" };

        List<Matcher> matchers = new LinkedList<>();
        matchers.add(Matchers.argThat(m -> m.equals(1)));
        matchers.add(Matchers.argThat(m -> m.equals("foo")));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Object[] arguments = new Object[]{ 1, "foo" };
        invocation.setRawArguments(arguments);

        ArgumentCaptor<Integer> intCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);

        invocationMatcher.captureArgumentsFrom(invocation);

        verify(invocation).setRawArguments(arguments);
        verify(invocation).getArgumentAt(0, Object.class).equals(1);
        verify(invocation).getArgumentAt(1, Object.class).equals("foo");

        verify(intCaptor).capture(m -> m.equals(1));
        verify(stringCaptor).capture(m -> m.equals("foo"));

        assertEquals(1, intCaptor.getValue().intValue());
        assertEquals("foo", stringCaptor.getValue());
    }

    @Test
    public void testCaptureArgumentsFromInvocationWithVarargMatcher() {
        Invocation invocation = spy(mock(Invocation.class));
        Object[] rawArguments = new Object[]{ new Object[]{"foo", "bar"} };

        List<Matcher> matchers = new LinkedList<>();
        matchers.add(Matchers.argThat(m -> m instanceof VarargMatcher));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Object[] arguments = new Object[]{ "foo", "bar" };
        invocation.setRawArguments(arguments);

        ArgumentCaptor<Object[]> varargCaptor = ArgumentCaptor.forClass(Object[].class);

        invocationMatcher.captureArgumentsFrom(invocation);

        verify(invocation).setRawArguments(arguments);
        verifyZeroInteractions(invocation);

        assertEquals(arguments, varargCaptor.getValue());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCaptureArgumentsFromInvocationWithVarargs() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(method.isVarArgs()).thenReturn(true);
        when(invocation.getMethod()).thenReturn(method);

        List<Matcher> matchers = new LinkedList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Object[] arguments = new Object[]{ 1, "foo", new Object[]{"foo", "bar"} };
        invocation.setRawArguments(arguments);

        invocationMatcher.captureArgumentsFrom(invocation);

        //should throw UnsupportedOperationException
    }
}
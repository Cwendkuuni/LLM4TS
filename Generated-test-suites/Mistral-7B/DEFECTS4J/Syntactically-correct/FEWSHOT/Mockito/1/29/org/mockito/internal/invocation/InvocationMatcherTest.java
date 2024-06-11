package org.mockito.internal.invocation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.array;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.isInstanceOf;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.ArgumentMatchers.isSame;
import static org.mockito.ArgumentMatchers.matches;
import static org.mockito.ArgumentMatchers.nullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.InvocationOnMock;
import org.mockito.internal.invocation.ArgumentsProcessor;
import org.mockito.internal.invocation.CapturesArguments;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.Location;
import org.mockito.internal.matchers.MatcherDecorator;
import org.mockito.internal.matchers.VarargMatcher;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.invocation.InvocationOnMockImpl;
import org.mockito.invocation.LocationImpl;

public class InvocationMatcherTest {

    @Test
    public void testEmptyMatcherList() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation, Collections.<Matcher>emptyList());

        assertFalse(matcher.matches(null));
        assertFalse(matcher.matches(mock(Invocation.class)));
        assertEquals(invocation, matcher.getInvocation());
        assertEquals(Collections.<Matcher>emptyList(), matcher.getMatchers());
        assertEquals("", matcher.toString());
    }

    @Test
    public void testMatcherListWithOneMatcher() {
        Invocation invocation = mock(Invocation.class);
        Matcher<Object> matcher = isA(String.class);
        InvocationMatcher matchers = new InvocationMatcher(invocation, Arrays.asList(matcher));

        assertTrue(matchers.matches(invocation));
        assertEquals(invocation, matchers.getInvocation());
        assertEquals(Arrays.asList(matcher), matchers.getMatchers());
        assertEquals("invocation(Object)", matcher.toString());
    }

    @Test
    public void testMatcherListWithMultipleMatchers() {
        Invocation invocation = mock(Invocation.class);
        Matcher<Object> matcher1 = isA(String.class);
        Matcher<Object> matcher2 = isNull();

        InvocationMatcher matchers = new InvocationMatcher(invocation, Arrays.asList(matcher1, matcher2));

        Object[] arguments = new Object[]{ "foo", null };
        InvocationOnMock impl = new InvocationOnMockImpl(invocation, InvocationMocker.class, Arrays.asList(new LocationImpl(0, 0)), arguments);

        assertTrue(matchers.matches(impl));
        assertEquals(invocation, matchers.getInvocation());
        assertEquals(Arrays.asList(matcher1, matcher2), matchers.getMatchers());
        assertEquals("invocation(String, null)", matcher1.toString());
        assertEquals("invocation(null)", matcher2.toString());
    }

    @Test
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        Matcher<Object> matcher1 = isA(String.class);
        Matcher<Object> matcher2 = isInstanceOf(List.class);
        Matcher<Object> matcher3 = eq("bar");

        InvocationMatcher matchers = new InvocationMatcher(invocation, Arrays.asList(matcher1, matcher2, matcher3));

        assertEquals("invocation(String, List, bar)", matchers.toString());
    }

    @Test
    public void testToStringWithEmptyMatcherList() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher matchers = new InvocationMatcher(invocation, Collections.<Matcher>emptyList());

        assertEquals("invocation()", matchers.toString());
    }

    @Test
    public void testMatchesInvocationWithSameMethodAndArguments() {
        Invocation invocation = mock(Invocation.class);
        Matcher<Object> matcher = isA(String.class);
        InvocationMatcher matchers = new InvocationMatcher(invocation, Arrays.asList(matcher));

        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(invocation.getMethod());

        assertTrue(matchers.matches(candidate));
    }

    @Test
    public void testMatchesInvocationWithDifferentMethod() {
        Invocation invocation = mock(Invocation.class);
        Matcher<Object> matcher = isA(String.class);
        InvocationMatcher matchers = new InvocationMatcher(invocation, Arrays.asList(matcher));

        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(new Method("differentMethod", new Class[0]));

        assertFalse(matchers.matches(candidate));
    }

    @Test
    public void testMatchesInvocationWithSameMethodButDifferentMock() {
        Invocation invocation = mock(Invocation.class);
        Matcher<Object> matcher = isA(String.class);
        InvocationMatcher matchers = new InvocationMatcher(invocation, Arrays.asList(matcher));

        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(invocation.getMethod());
        when(candidate.getMock()).thenReturn(mock(Object.class));

        assertFalse(matchers.matches(candidate));
    }

    @Test
    public void testMatchesInvocationWithVerified() {
        Invocation invocation = mock(Invocation.class);
        Matcher<Object> matcher = isA(String.class);
        InvocationMatcher matchers = new InvocationMatcher(invocation, Arrays.asList(matcher));

        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(invocation.getMethod());
        when(candidate.isVerified()).thenReturn(true);

        assertFalse(matchers.matches(candidate));
    }

    @Test
    public void testMatchesInvocationWithSameMethodAndDifferentArguments() {
        Invocation invocation = mock(Invocation.class);
        Object[] arguments = new Object[]{ "foo" };
        when(invocation.getArguments()).thenReturn(arguments);

        Matcher<Object> matcher = isA(Integer.class);
        InvocationMatcher matchers = new InvocationMatcher(invocation, Arrays.asList(matcher));

        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(invocation.getMethod());
        Object[] candidateArguments = new Object[]{ 42 };
        when(candidate.getArguments()).thenReturn(candidateArguments);

        assertFalse(matchers.matches(candidate));
    }

    @Test
    public void testMatchesInvocationWithSameMethodAndNullArguments() {
        Invocation invocation = mock(Invocation.class);
        Object[] arguments = new Object[0];
        when(invocation.getArguments()).thenReturn(arguments);

        Matcher<Object> matcher = isA(Integer.class);
        InvocationMatcher matchers = new InvocationMatcher(invocation, Arrays.asList(matcher));

        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(invocation.getMethod());
        Object[] candidateArguments = new Object[0];
        when(candidate.getArguments()).thenReturn(candidateArguments);

        assertTrue(matchers.matches(candidate));
    }

    @Test
    public void testMatchesInvocationWithSameMethodAndEmptyArguments() {
        Invocation invocation = mock(Invocation.class);
        Object[] arguments = new Object[0];
        when(invocation.getArguments()).thenReturn(arguments);

        Matcher<Object> matcher = isA(Integer.class);
        InvocationMatcher matchers = new InvocationMatcher(invocation, Arrays.asList(matcher));

        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(invocation.getMethod());
        Object[] candidateArguments = new Object[0];
        when(candidate.getArguments()).thenReturn(candidateArguments);

        assertTrue(matchers.matches(candidate));
    }

    @Test
    public void testMatchesInvocationWithSameMethodButDifferentNumberOfArguments() {
        Invocation invocation = mock(Invocation.class);
        Object[] arguments = new Object[]{ "foo" };
        when(invocation.getArguments()).thenReturn(arguments);

        Matcher<Object> matcher = isA(Integer.class);
        InvocationMatcher matchers = new InvocationMatcher(invocation, Arrays.asList(matcher));

        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(invocation.getMethod());
        Object[] candidateArguments = new Object[]{ "foo", 42 };
        when(candidate.getArguments()).thenReturn(candidateArguments);

        assertFalse(matchers.matches(candidate));
    }

    @Test
    public void testMatchesInvocationWithDifferentMethodButSameMock() {
        Invocation invocation = mock(Invocation.class);
        Matcher<Object> matcher = isA(String.class);
        InvocationMatcher matchers = new InvocationMatcher(invocation, Arrays.asList(matcher));

        Invocation candidate = mock(Invocation.class);
        when(candidate.getMock()).thenReturn(invocation.getMock());

        Method method1 = mock(Method.class);
        when(method1.getName()).thenReturn("method1");
        when(invocation.getMethod()).thenReturn(method1);

        Method method2 = mock(Method.class);
        when(method2.getName()).thenReturn("method2");
        when(candidate.getMethod()).thenReturn(method2);

        assertFalse(matchers.matches(candidate));
    }

    @Test
    public void testMatchesInvocationWithDifferentMock() {
        Invocation invocation = mock(Invocation.class);
        Matcher<Object> matcher = isA(String.class);
        InvocationMatcher matchers = new InvocationMatcher(invocation, Arrays.asList(matcher));

        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(invocation.getMethod());

        when(candidate.getMock()).thenReturn(mock(Object.class));

        assertFalse(matchers.matches(candidate));
    }

    @Test
    public void testMatchesInvocationWithNullInvocation() {
        Matcher<Object> matcher = isA(String.class);
        InvocationMatcher matchers = new InvocationMatcher(null, Arrays.asList(matcher));

        assertFalse(matchers.matches(mock(Invocation.class)));
    }

    @Test
    public void testMatchesInvocationWithNullMatcher() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher matchers = new InvocationMatcher(invocation, null);

        assertFalse(matchers.matches(mock(Invocation.class)));
    }

    @Test
    public void testCapturesArgumentsFromInvocation() {
        Invocation invocation = mock(Invocation.class);
        Object[] arguments = new Object[]{ "foo" };
        when(invocation.getArguments()).thenReturn(arguments);

        Matcher<Object> matcher = isA(String.class);

        InvocationMatcher matchers = new InvocationMatcher(invocation, Arrays.asList(matcher));

        InvocationOnMock impl = new InvocationOnMockImpl(invocation, InvocationMocker.class, Arrays.asList(new LocationImpl(0, 0)), arguments);

        matchers.captureArgumentsFrom(impl);

        verify(matcher).captureFrom(args(eq("foo")));
    }

    @Test
    public void testCapturesArgumentsFromInvocationWithVarargs() {
        Invocation invocation = mock(Invocation.class);
        Object[] arguments = new Object[]{ "foo", "bar" };
        when(invocation.getArguments()).thenReturn(arguments);

        Matcher<Object> matcher = isA(String[].class);

        InvocationMatcher matchers = new InvocationMatcher(invocation, Arrays.asList(matcher));

        InvocationOnMock impl = new InvocationOnMockImpl(invocation, InvocationMocker.class, Arrays.asList(new LocationImpl(0, 0)), arguments);

        matchers.captureArgumentsFrom(impl);

        verify(matcher).captureFrom(args(eq(arguments)));
    }

    @Test
    public void testCapturesArgumentsFromInvocationWithNoMatchers() {
        Invocation invocation = mock(Invocation.class);
        Object[] arguments = new Object[]{ "foo" };
        when(invocation.getArguments()).thenReturn(arguments);

        InvocationMatcher matchers = new InvocationMatcher(invocation);

        InvocationOnMock impl = new InvocationOnMockImpl(invocation, InvocationMocker.class, Arrays.asList(new LocationImpl(0, 0)), arguments);

        matchers.captureArgumentsFrom(impl);

        verifyNoMoreInteractions(impl);
    }

    @Test
    public void testCreateFromListOfInvocations() {
        List<Invocation> invocations = Arrays.asList(mock(Invocation.class), mock(Invocation.class));
        List<InvocationMatcher> matchers = InvocationMatcher.createFrom(invocations);

        assertEquals(2, matchers.size());

        for (InvocationMatcher matcher : matchers) {
            assertTrue(matcher instanceof InvocationMatcher);
        }
    }
}
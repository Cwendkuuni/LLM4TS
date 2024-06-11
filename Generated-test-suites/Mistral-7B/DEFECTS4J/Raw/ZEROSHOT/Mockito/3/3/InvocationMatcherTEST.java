package org.mockito.internal.invocation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.invocation.Invocation;
import org.mockito.stub.Stubber;

@SuppressWarnings("unchecked")
public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherConstructorWithInvocationAndEmptyMatchersList() {
        // given
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Collections.emptyList();

        // when
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // then
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testInvocationMatcherConstructorWithInvocationAndMatchersList() {
        // given
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(Matchers.eq("foo"), Matchers.eq(1));

        // when
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // then
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testInvocationMatcherConstructorWithInvocationOnly() {
        // given
        Invocation invocation = mock(Invocation.class);

        // when
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // then
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testGetMethod() {
        // given
        Mockito.when(mock(Object.class).method()).thenReturn(new Method("method", new Class[0]));
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(Mockito.mock(Method.class));

        // when
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // then
        assertEquals(Mockito.mock(Method.class), invocationMatcher.getMethod());
    }

    @Test
    public void testGetInvocation() {
        // given
        Invocation invocation = mock(Invocation.class);

        // when
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // then
        assertEquals(invocation, invocationMatcher.getInvocation());
    }

    @Test
    public void testGetMatchers() {
        // given
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(Matchers.eq("foo"), Matchers.eq(1));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // when
        List<Matcher> actualMatchers = invocationMatcher.getMatchers();

        // then
        assertEquals(matchers, actualMatchers);
    }

    @Test
    public void testToString() {
        // given
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(Matchers.eq("foo"), Matchers.eq(1));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // when
        String toString = invocationMatcher.toString();

        // then
        String expectedToString = String.format("Invocation %s with arguments %s", invocation, matchers);
        assertEquals(expectedToString, toString);
    }

    @Test
    public void testMatches() {
        // given
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(Matchers.eq("foo"), Matchers.eq(1));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, matchers);

        // when
        Invocation mock = mock(Object.class);

        // then
        assertTrue(invocationMatcher1.matches(invocationMatcher1));
        assertTrue(invocationMatcher1.matches(invocationMatcher2));
        assertFalse(invocationMatcher1.matches(invocationMatcher1.withArguments(new Object[0])));
        assertFalse(invocationMatcher1.matches(invocationMatcher2.withArguments(new Object[0])));
        assertFalse(invocationMatcher1.matches(new InvocationMatcher(invocation1, Arrays.asList(Matchers.eq("bar")))));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        // given
        Invocation invocation = mock(Invocation.class);
        Object[] arguments = new Object[0];

        // when
        boolean safelyArgumentsMatch = new InvocationMatcher(invocation, Arrays.asList(Matchers.eq(arguments))).safelyArgumentsMatch(arguments);

        // then
        assertTrue(safelyArgumentsMatch);
    }

    @Test(expected = Throwable.class)
    public void testSafelyArgumentsMatchWithThrowable() {
        // given
        Invocation invocation = mock(Invocation.class);
        Object[] arguments = new Object[0];

        // when, then
        new InvocationMatcher(invocation, Arrays.asList(new Throwable())).safelyArgumentsMatch(arguments);
    }

    @Test
    public void testHasSimilarMethod() {
        // given
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        when(invocation1.getMethod()).thenReturn(new Method("method1", new Class[0]));
        when(invocation2.getMethod()).thenReturn(new Method("method1", new Class[0]));

        // when
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        // then
        assertTrue(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testHasSimilarMethodDifferentMethodNames() {
        // given
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        when(invocation1.getMethod()).thenReturn(new Method("method1", new Class[0]));
        when(invocation2.getMethod()).thenReturn(new Method("method2", new Class[0]));

        // when
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        // then
        assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testHasSameMethod() {
        // given
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        Method method1 = new Method("method1", new Class[0]);
        Method method2 = new Method("method1", new Class[0]);
        when(invocation1.getMethod()).thenReturn(method1);
        when(invocation2.getMethod()).thenReturn(method2);

        // when
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        // then
        assertTrue(invocationMatcher1.hasSameMethod(invocationMatcher2));
    }

    @Test
    public void testHasSameMethodDifferentMethods() {
        // given
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        Method method1 = new Method("method1", new Class[] { Integer.TYPE });
        Method method2 = new Method("method2", new Class[] { String.class });
        when(invocation1.getMethod()).thenReturn(method1);
        when(invocation2.getMethod()).thenReturn(method2);

        // when
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        // then
        assertFalse(invocationMatcher1.hasSameMethod(invocationMatcher2));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        // given
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(Matchers.argThat(new CapturesArguments<Integer>() {
            private int capturedArgument;

            @Override
            public boolean matches(Object argument) {
                this.capturedArgument = (Integer) argument;
                return true;
            }
        }));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        InvocationOnMock invocationOnMock = mock(InvocationOnMock.class);

        // when
        invocationMatcher.captureArgumentsFrom(invocationOnMock);

        // then
        verify(invocationOnMock, times(matchers.size())).argThat(argThat(new Matcher<CapturesArguments<Integer>>() {
            @Override
            public boolean matches(Object argument) {
                CapturesArguments<Integer> arg = (CapturesArguments<Integer>) argument;
                assertEquals(invocationMatcher.getInvocation().getArgumentAt(0, Object.class), arg.getCapturedValue());
                return true;
            }
        }));
    }

    @Test
    public void testCreateFrom() {
        // given
        List<Invocation> invocations = Arrays.asList(mock(Invocation.class), mock(Invocation.class));

        // when
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        // then
        assertEquals(2, invocationMatchers.size());

        for (InvocationMatcher invocationMatcher : invocationMatchers) {
            assertTrue(invocationMatcher instanceof InvocationMatcher);
        }
    }
}
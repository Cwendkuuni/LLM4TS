package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.ArgumentMatchers.argThat;
import static org.mockito.internal.matchers.ArgumentMatchers.isNull;
import static org.mockito.internal.matchers.ArgumentMatchers.same;
import static org.mockito.internal.matchers.Arguments.argument;
import static org.mockito.internal.matchers.Arguments.arguments;
import static org.mockito.internal.matchers.Arguments.not;
import static org.mockito.internal.matchers.Arguments.sameInstance;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    @Test
    public void testConstructorWithListOfMatchers() {
        // given
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(argThat(isNull()), argThat(same(new Object())));

        // when
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // then
        assertTrue(invocationMatcher.matches(invocation));
    }

    @Test
    public void testConstructorWithoutListOfMatchers() {
        // given
        Invocation invocation = Mockito.mock(Invocation.class);

        // when
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // then
        assertTrue(invocationMatcher.matches(invocation));
    }

    @Test
    public void testGetMethod() {
        // given
        Invocation invocation = Mockito.mock(Invocation.class);
        Method method = Mockito.mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);

        // when
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // then
        assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testGetInvocation() {
        // given
        Invocation invocation = Mockito.mock(Invocation.class);

        // when
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // then
        assertEquals(invocation, invocationMatcher.getInvocation());
    }

    @Test
    public void testGetMatchers() {
        // given
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(argThat(isNull()), argThat(same(new Object())));
        when(invocation.argumentsToMatchers()).thenReturn(matchers);

        // when
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // then
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testToString() {
        // given
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(argThat(isNull()), argThat(same(new Object())));
        when(invocation.toString(matchers, any(PrintSettings.class))).thenReturn("toString result");

        // when
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // then
        assertEquals("toString result", invocationMatcher.toString());
    }

    @Test
    public void testMatchesInvocationWithSameMethodAndSameArguments() {
        // given
        Invocation invocation = Mockito.mock(Invocation.class);
        Invocation candidate = Mockito.mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(sameMethod());
        when(candidate.getMethod()).thenReturn(sameMethod());
        when(invocation.getArguments()).thenReturn(new Object[]{"arg1"});
        when(candidate.getArguments()).thenReturn(new Object[]{"arg1"});

        // when
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        InvocationMatcher candidateMatcher = new InvocationMatcher(candidate);

        // then
        assertTrue(invocationMatcher.matches(candidateMatcher));
    }

    @Test
    public void testMatchesInvocationWithSameMethodButDifferentArguments() {
        // given
        Invocation invocation = Mockito.mock(Invocation.class);
        Invocation candidate = Mockito.mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(sameMethod());
        when(candidate.getMethod()).thenReturn(sameMethod());
        when(invocation.getArguments()).thenReturn(new Object[]{"arg1"});
        when(candidate.getArguments()).thenReturn(new Object[]{"arg2"});

        // when
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        InvocationMatcher candidateMatcher = new InvocationMatcher(candidate);

        // then
        assertFalse(invocationMatcher.matches(candidateMatcher));
    }

    @Test
    public void testMatchesInvocationWithDifferentMethods() {
        // given
        Invocation invocation = Mockito.mock(Invocation.class);
        Invocation candidate = Mockito.mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(method1());
        when(candidate.getMethod()).thenReturn(method2());

        // when
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        InvocationMatcher candidateMatcher = new InvocationMatcher(candidate);

        // then
        assertFalse(invocationMatcher.matches(candidateMatcher));
    }

    @Test(expected = Throwable.class)
    public void testSafelyArgumentsMatchWithDifferentNumberOfArguments() {
        // given
        Invocation invocation = Mockito.mock(Invocation.class);
        when(invocation.getArguments()).thenReturn(new Object[]{});

        // when
        new InvocationMatcher(invocation).safelyArgumentsMatch(new Object[]{1, 2});

        // then
        // exception is thrown
    }

    @Test
    public void testHasSimilarMethodWithSameMethodButDifferentArguments() {
        // given
        Invocation invocation = Mockito.mock(Invocation.class);
        Invocation candidate = Mockito.mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(sameMethod());
        when(candidate.getMethod()).thenReturn(sameMethod());
        when(invocation.getArguments()).thenReturn(new Object[]{"arg1"});
        when(candidate.getArguments()).thenReturn(new Object[]{"arg2"});

        // when
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // then
        assertFalse(invocationMatcher.hasSimilarMethod(new InvocationMatcher(candidate)));
    }

    @Test
    public void testHasSimilarMethodWithSameMethodAndSameArguments() {
        // given
        Invocation invocation = Mockito.mock(Invocation.class);
        Invocation candidate = Mockito.mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(sameMethod());
        when(candidate.getMethod()).thenReturn(sameMethod());
        when(invocation.getArguments()).thenReturn(new Object[]{"arg1"});
        when(candidate.getArguments()).thenReturn(new Object[]{"arg1"});

        // when
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // then
        assertTrue(invocationMatcher.hasSimilarMethod(new InvocationMatcher(candidate)));
    }

    @Test
    public void testHasSimilarMethodWithDifferentMethods() {
        // given
        Invocation invocation = Mockito.mock(Invocation.class);
        Invocation candidate = Mockito.mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(method1());
        when(candidate.getMethod()).thenReturn(method2());

        // when
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // then
        assertFalse(invocationMatcher.hasSimilarMethod(new InvocationMatcher(candidate)));
    }

    @Test
    public void testCaptureArgumentsFromInvocation() {
        // given
        Invocation invocation = Mockito.mock(Invocation.class);
        when(invocation.getArguments()).thenReturn(new Object[]{"arg1", "arg2"});
        when(invocation.getMock()).thenReturn(new Object());

        // when
        new InvocationMatcher(invocation).captureArgumentsFrom(Mockito.mock(Invocation.class));

        // then
        // no assertions, just check that method was called
    }

    @Test
    public void testCreateFromInvocations() {
        // given
        List<Invocation> invocations = new LinkedList<>();
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        invocations.add(invocation1);
        invocations.add(invocation2);

        // when
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        // then
        assertEquals(2, invocationMatchers.size());
    }

    private Method sameMethod() {
        try {
            return Mockito.class.getMethod("methodName", new Class[0]);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private Method method1() {
        try {
            return Mockito.class.getMethod("method1", new Class[0]);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private Method method2() {
        try {
            return Mockito.class.getMethod("method2", new Class[0]);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> T any(Class<T> clazz) {
        return Mockito.any(clazz);
    }

}
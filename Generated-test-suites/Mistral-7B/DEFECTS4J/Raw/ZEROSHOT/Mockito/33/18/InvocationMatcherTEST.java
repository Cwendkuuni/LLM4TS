package org.mockito.internal.invocation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.internal.matchers.ArgThat.argThat;
import static org.mockito.internal.matchers.ArgumentsMatchers.any;
import static org.mockito.internal.matchers.ArgumentsMatchers.eq;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentsMatcher;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    @Mock
    private Object mock;

    @Captor
    private ArgumentsCaptor<Object[]> argumentsCaptor;

    @InjectMocks
    private InvocationMatcher invocationMatcher;

    @Test
    public void testInvocationMatcherCreation() {
        // given
        Method method = mock(Method.class);
        List<Matcher> matchers = Arrays.asList(eq("Test"), eq(1));

        // when
        InvocationMatcher invocationMatcher = new InvocationMatcher(new Invocation(mock, method), matchers);

        // then
        assertEquals(method, invocationMatcher.getMethod());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testInvocationMatcherCreationWithEmptyMatchersList() {
        // given
        Method method = mock(Method.class);

        // when
        InvocationMatcher invocationMatcher = new InvocationMatcher(new Invocation(mock, method));

        // then
        assertNull(invocationMatcher.getMatchers());
    }

    @Test
    public void testMatchesMethodNameEquals() {
        // given
        Method method = mock(Method.class);
        List<Matcher> matchers = Collections.singletonList(eq("Test"));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(new Invocation(mock, method), matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(new Invocation(mock, method));

        // when
        Invocation newInvocation = new Invocation(mock, method);

        // then
        assertTrue(invocationMatcher1.matches(newInvocation));
        assertTrue(invocationMatcher2.matches(newInvocation));
    }

    @Test
    public void testMatchesMethodNameNotEquals() {
        // given
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        List<Matcher> matchers = Collections.singletonList(eq("Test"));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(new Invocation(mock, method1), matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(new Invocation(mock, method2));

        // when
        Invocation newInvocation = new Invocation(mock, method2);

        // then
        assertFalse(invocationMatcher1.matches(newInvocation));
        assertFalse(invocationMatcher2.matches(newInvocation));
    }

    @Test
    public void testMatchesMockIsTheSame() {
        // given
        Method method = mock(Method.class);
        List<Matcher> matchers = Collections.singletonList(eq(mock));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(new Invocation(mock, method), matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(new Invocation(mock, method));

        // when
        Invocation newInvocation = new Invocation(mock, method);

        // then
        assertTrue(invocationMatcher1.matches(newInvocation));
        assertTrue(invocationMatcher2.matches(newInvocation));
    }

    @Test
    public void testMatchesMockIsDifferent() {
        // given
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Method method = mock(Method.class);
        List<Matcher> matchers = Collections.singletonList(eq(mock1));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(new Invocation(mock1, method), matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(new Invocation(mock2, method));

        // when
        Invocation newInvocation = new Invocation(mock2, method);

        // then
        assertFalse(invocationMatcher1.matches(newInvocation));
        assertFalse(invocationMatcher2.matches(newInvocation));
    }

    @Test
    public void testMatchesArgumentsMatch() {
        // given
        Method method = mock(Method.class);
        List<Matcher> matchers = Arrays.asList(eq("Test"), eq(1));
        InvocationMatcher invocationMatcher = new InvocationMatcher(new Invocation(mock, method), matchers);
        Object[] arguments = new Object[]{"Test", 1};

        // when
        Invocation newInvocation = new Invocation(mock, method, arguments);

        // then
        assertTrue(invocationMatcher.matches(newInvocation));
    }

    @Test
    public void testMatchesArgumentsDoNotMatch() {
        // given
        Method method = mock(Method.class);
        List<Matcher> matchers = Arrays.asList(eq("Test"), eq(2));
        InvocationMatcher invocationMatcher = new InvocationMatcher(new Invocation(mock, method), matchers);
        Object[] arguments = new Object[]{"Test", 1};

        // when
        Invocation newInvocation = new Invocation(mock, method, new Object[]{1, 2});

        // then
        assertFalse(invocationMatcher.matches(newInvocation));
    }

    @Test
    public void testMatchesArgumentsMatchUsingCustomMatcher() {
        // given
        Method method = mock(Method.class);
        List<Matcher> matchers = Arrays.asList(argThat(new CustomArgumentsMatcher()));
        InvocationMatcher invocationMatcher = new InvocationMatcher(new Invocation(mock, method), matchers);
        Object[] arguments = new Object[]{1, 2};

        // when
        Invocation newInvocation = new Invocation(mock, method, arguments);

        // then
        assertTrue(invocationMatcher.matches(newInvocation));
    }

    @Test
    public void testMatchesArgumentsDoNotMatchUsingCustomMatcher() {
        // given
        Method method = mock(Method.class);
        List<Matcher> matchers = Arrays.asList(argThat(new CustomArgumentsMatcher()));
        InvocationMatcher invocationMatcher = new InvocationMatcher(new Invocation(mock, method), matchers);
        Object[] arguments = new Object[]{1, 3};

        // when
        Invocation newInvocation = new Invocation(mock, method, arguments);

        // then
        assertFalse(invocationMatcher.matches(newInvocation));
    }

    @Test
    public void testHasSimilarMethodFalse() {
        // given
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(new Invocation(mock, method1));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(new Invocation(mock, method2));

        // when
        Invocation newInvocation = new Invocation(mock, method1);

        // then
        assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testHasSimilarMethodTrue() {
        // given
        Method method = mock(Method.class);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(new Invocation(mock, method));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(new Invocation(mock, method));

        // when
        Invocation newInvocation = new Invocation(mock, method);

        // then
        assertTrue(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testHasSimilarMethodTrueWithDifferentArguments() {
        // given
        Method method = mock(Method.class);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(new Invocation(mock, method, new Object[]{1, 1}));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(new Invocation(mock, method, new Object[]{2, 2}));

        // when
        Invocation newInvocation = new Invocation(mock, method, new Object[]{1, 1});

        // then
        assertTrue(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testHasSimilarMethodFalseWithDifferentArguments() {
        // given
        Method method = mock(Method.class);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(new Invocation(mock, method, new Object[]{1, 1}));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(new Invocation(mock, method, new Object[]{2, 2}));

        // when
        Invocation newInvocation = new Invocation(mock, method, new Object[]{2, 2});

        // then
        assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testToString() {
        // given
        Method method = spy(mock(Method.class));
        List<Matcher> matchers = Arrays.asList(eq("Test"), eq(1));

        // when
        String toString = new InvocationMatcher(new Invocation(mock, method), matchers).toString();

        // then
        assertTrue(toString.contains("InvocationMatcher:"));
        assertTrue(toString.contains(method.getName()));
        assertTrue(toString.contains("["));
        assertTrue(toString.contains("]"));
    }

    @Test
    public void testCreateFrom() {
        // given
        List<Invocation> invocations = new LinkedList<>();
        invocations.add(new Invocation(mock, mock(Method.class)));
        invocations.add(new Invocation(mock, mock(Method.class)));

        // when
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        // then
        assertEquals(2, invocationMatchers.size());
    }

    @Test
    public void testCaptureArgumentsFrom() {
        // given
        Method method = mock(Method.class);
        List<Matcher> matchers = Arrays.asList(eq("Test"), eq(1));
        InvocationMatcher invocationMatcher = new InvocationMatcher(new Invocation(mock, method), matchers);
        Invocation invocation = new Invocation(mock, method, new Object[]{1, 2});

        // when
        invocationMatcher.captureArgumentsFrom(invocation);

        // then
        assertEquals(2, invocationMatcher.getMatchers().size());
        assertTrue(invocationMatcher.getMatchers().get(0) instanceof CapturesArguments);
        assertTrue(invocationMatcher.getMatchers().get(1) instanceof CapturesArguments);
        assertEquals(1, ((CapturesArguments) invocationMatcher.getMatchers().get(0)).getValue(0).length);
        assertEquals(2, ((CapturesArguments) invocationMatcher.getMatchers().get(1)).getValue(0).length);
    }

    @Test
    public void testCaptureArgumentsFromWithEmptyMatchersList() {
        // given
        Method method = mock(Method.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(new Invocation(mock, method));
        Invocation invocation = new Invocation(mock, method, new Object[]{1, 2});

        // when
        invocationMatcher.captureArgumentsFrom(invocation);

        // then
        assertEquals(0, invocationMatcher.getMatchers().size());
    }

    private static class CustomArgumentsMatcher implements ArgumentMatcher<Object[]> {

        @Override
        public boolean matches(Object argument) {
            Object[] arguments = (Object[]) argument;
            return Arrays.equals(new Object[]{1, 1}, arguments);
        }
    }
}
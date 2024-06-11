package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.InvocationOnMock;
import org.mockito.internal.matchers.ArrayWithSameSizeMatcher;
import org.mockito.internal.matchers.CaptureArgumentMatcher;
import org.mockito.internal.matchers.StringContainsMatcher;
import org.mockito.internal.matchers.VarargMatcher;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.MethodType;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InvocationMatcherTest {

    @Test
    public void testMatches_sameInvocation() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);

        InvocationMatcher matcher = new InvocationMatcher(invocation);
        Invocation invokedInvocation = spy(invocation);

        assertTrue(matcher.matches(invokedInvocation));
    }

    @Test
    public void testMatches_differentInvocation() {
        Invocation invocation1 = mock(Invocation.class);
        Method method1 = mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method1);
        when(method1.getName()).thenReturn("method1");

        Invocation invocation2 = mock(Invocation.class);
        Method method2 = mock(Method.class);
        when(invocation2.getMethod()).thenReturn(method2);
        when(method2.getName()).thenReturn("method2");

        InvocationMatcher matcher = new InvocationMatcher(invocation1);

        assertFalse(matcher.matches(invocation2));
    }

    @Test
    public void testHasSameMethod_sameMethod() {
        Invocation invocation1 = mock(Invocation.class);
        Method method1 = mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method1);

        Invocation invocation2 = mock(Invocation.class);
        Method method2 = mock(Method.class);
        when(invocation2.getMethod()).thenReturn(method1);

        InvocationMatcher matcher = new InvocationMatcher(invocation1);

        assertTrue(matcher.hasSameMethod(invocation2));
    }

    @Test
    public void testHasSameMethod_differentMethod() {
        Invocation invocation1 = mock(Invocation.class);
        Method method1 = mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method1);
        when(method1.getName()).thenReturn("method1");

        Invocation invocation2 = mock(Invocation.class);
        Method method2 = mock(Method.class);
        when(invocation2.getMethod()).thenReturn(method2);
        when(method2.getName()).thenReturn("method2");

        InvocationMatcher matcher = new InvocationMatcher(invocation1);

        assertFalse(matcher.hasSameMethod(invocation2));
    }

    @Test
    public void testSafelyArgumentsMatch_sameArguments() {
        Invocation invocation = mock(Invocation.class);
        Object[] arguments = new Object[]{1, 2, 3};
        when(invocation.getArguments()).thenReturn(arguments);

        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertTrue(matcher.safelyArgumentsMatch(arguments));
    }

    @Test
    public void testSafelyArgumentsMatch_differentArguments() {
        Invocation invocation = mock(Invocation.class);
        Object[] arguments = new Object[]{1, 2, 3};
        when(invocation.getArguments()).thenReturn(arguments);

        Object[] differentArguments = new Object[]{2, 3, 4};

        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertFalse(matcher.safelyArgumentsMatch(differentArguments));
    }

    @Test
    public void testHasSimilarMethod_sameInvocation() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);

        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertTrue(matcher.hasSimilarMethod(invocation));
    }

    @Test
    public void testHasSimilarMethod_differentInvocationSameMethod() {
        Invocation invocation1 = mock(Invocation.class);
        Method method1 = mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method1);

        Invocation invocation2 = mock(Invocation.class);
        Method method2 = mock(Method.class);
        when(invocation2.getMethod()).thenReturn(method1);

        InvocationMatcher matcher = new InvocationMatcher(invocation1);

        assertTrue(matcher.hasSimilarMethod(invocation2));
    }

    @Test
    public void testHasSimilarMethod_differentInvocationDifferentMethod() {
        Invocation invocation1 = mock(Invocation.class);
        Method method1 = mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method1);

        Invocation invocation2 = mock(Invocation.class);
        Method method2 = mock(Method.class);
        when(invocation2.getMethod()).thenReturn(method2);

        InvocationMatcher matcher = new InvocationMatcher(invocation1);

        assertFalse(matcher.hasSimilarMethod(invocation2));
    }

    @Test
    public void testToString_noMatchers() {
        Invocation invocation = mock(Invocation.class);

        InvocationMatcher matcher = new InvocationMatcher(invocation);

        String printSettings = new PrintSettings().setBlankSlates(false).setSuppressed(false).setExpanded(false);
        String expected = printSettings.print(Collections.emptyList(), invocation);

        assertEquals(expected, matcher.toString());
    }

    @Test
    public void testToString_matchers() {
        Invocation invocation = mock(Invocation.class);
        Object[] arguments = new Object[]{1, 2, 3};
        when(invocation.getArguments()).thenReturn(arguments);

        List<Matcher> matchers = Arrays.asList(
                Matchers.isA(String.class),
                new CaptureArgumentMatcher(0),
                new ArrayWithSameSizeMatcher(new Object[]{1, 2}),
                new StringContainsMatcher("foo")
        );

        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        String printSettings = new PrintSettings().setBlankSlates(false).setSuppressed(false).setExpanded(false);
        String expected
                = printSettings.print(matchers, invocation)
                .replaceAll("\\[|\\]", "")
                .replaceAll(".*\\(.*\\)", "Argument 0")
                .replaceAll("Argument ([0-9]+)", "Arg $1");

        assertEquals(expected, matcher.toString());
    }

    @Test
    public void testCaptureArgumentsFrom_singleArgument() {
        Invocation invocation = mock(Invocation.class);
        Object argument = mock(String.class);

        InvocationMatcher matcher = new InvocationMatcher(invocation);
        Matcher matcher1 = new CaptureArgumentMatcher(0);

        matcher.captureArgumentsFrom(invocation);
        ((CaptureArgumentMatcher) matcher1).assertCaptured(argument);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCaptureArgumentsFrom_varargs() {
        Invocation invocation = mock(Invocation.class);
        Object[] arguments = new Object[]{1, 2, 3};
        when(invocation.getArguments()).thenReturn(arguments);

        InvocationMatcher matcher = new InvocationMatcher(invocation);

        matcher.captureArgumentsFrom(invocation);
    }

    @Test
    public void testIsVariableArgument_variableArgument() {
        Invocation invocation = mock(Invocation.class);
        Object[] arguments = new Object[]{1, Arrays.asList("foo", "bar")};
        when(invocation.getArguments()).thenReturn(arguments);

        assertTrue(InvocationMatcher.isVariableArgument(invocation, 1));
    }

    @Test
    public void testIsVariableArgument_notVariableArgument() {
        Invocation invocation = mock(Invocation.class);
        Object[] arguments = new Object[]{1, 2, 3};
        when(invocation.getArguments()).thenReturn(arguments);

        assertFalse(InvocationMatcher.isVariableArgument(invocation, 0));
    }

    @Test
    public void testCreateFrom_emptyList() {
        List<Invocation> invocations = Collections.emptyList();

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        assertTrue(invocationMatchers.isEmpty());
    }

    @Test
    public void testCreateFrom_singleInvocation() {
        Invocation invocation = mock(Invocation.class);

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(Collections.singletonList(invocation));

        assertEquals(1, invocationMatchers.size());
        InvocationMatcher matcher = invocationMatchers.get(0);
        assertEquals(invocation, matcher.getInvocation());
    }

    @Test
    public void testCreateFrom_multipleInvocations() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);

        Method method = mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method);
        when(invocation2.getMethod()).thenReturn(method);

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(Arrays.asList(invocation1, invocation2));

        assertEquals(2, invocationMatchers.size());
    }

    @Test
    public void testIsVarargMatcher_matcherIsVarargMatcher() {
        Matcher matcher = new VarargMatcher(String[].class, "foo");

        assertTrue(InvocationMatcher.isVarargMatcher(matcher));
    }

    @Test
    public void testIsVarargMatcher_matcherIsNotVarargMatcher() {
        Matcher matcher = Matchers.isA(String.class);

        assertFalse(InvocationMatcher.isVarargMatcher(matcher));
    }
}
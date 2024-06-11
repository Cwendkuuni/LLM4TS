package org.mockito.internal.invocation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.LinkedList;

import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.isInstanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    @Test
    public void shouldCreateInstanceWithEmptyMatcherList() throws Exception {
        // given
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        // then
        List<Matcher> matchers = matcher.getMatchers();
        assertThat(matchers, is(Collections.emptyList()));
    }

    @Test
    public void shouldCreateInstanceWithMatcherList() throws Exception {
        // given
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        Matcher mockMatcher = mock(Matcher.class);
        List<Matcher> matchers = new LinkedList<Matcher>() {{
            add(mockMatcher);
        }};
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        // then
        List<Matcher> matchersFromMatcher = matcher.getMatchers();
        assertThat(matchersFromMatcher, is(matchers));
    }

    @Test
    public void shouldReturnInvocation() throws Exception {
        // given
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        // when
        Invocation result = matcher.getInvocation();

        // then
        assertThat(result, is(invocation));
    }

    @Test
    public void shouldReturnMatchers() throws Exception {
        // given
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        Matcher mockMatcher = mock(Matcher.class);
        List<Matcher> matchers = new LinkedList<Matcher>() {{
            add(mockMatcher);
        }};
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        // when
        List<Matcher> result = matcher.getMatchers();

        // then
        assertThat(result, is(matchers));
    }

    @Test
    public void shouldReturnStringRepresentationOfMatchers() throws Exception {
        // given
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        Matcher mockMatcher = mock(Matcher.class);
        List<Matcher> matchers = new LinkedList<Matcher>() {{
            add(mockMatcher);
        }};
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        // when
        String result = matcher.toString();

        // then
        verify(mockMatcher).toString();
        assertThat(result, notNullValue());
    }

    @Test
    public void shouldMatchInvocationWithSameInvocation() throws Exception {
        // given
        Invocation invocation1 = Mockito.mock(InvocationOnMock.class);
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        Invocation invocation2 = Mockito.mock(InvocationOnMock.class);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        // when
        boolean result = matcher1.matches(matcher2);

        // then
        assertThat(result, is(true));
    }

    @Test
    public void shouldNotMatchInvocationWithDifferentMocks() throws Exception {
        // given
        Invocation invocation1 = Mockito.mock(InvocationOnMock.class);
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        Invocation invocation2 = Mockito.mock(InvocationOnMock.class);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        // when
        boolean result = matcher1.matches(matcher2);

        // then
        assertThat(result, is(false));
    }

    @Test
    public void shouldNotMatchInvocationWithDifferentMethods() throws Exception {
        // given
        Invocation invocation1 = Mockito.mock(InvocationOnMock.class);
        when(invocation1.getMethod()).thenReturn(new Method(Mockito.class.getDeclaredMethod("mock", Class.class)));
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        Invocation invocation2 = Mockito.mock(InvocationOnMock.class);
        when(invocation2.getMethod()).thenReturn(new Method(String.class.getDeclaredMethod("substring", int.class, int.class)));
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        // when
        boolean result = matcher1.matches(matcher2);

        // then
        assertThat(result, is(false));
    }

    @Test
    public void shouldMatchInvocationWithSameMethodAndArguments() throws Exception {
        // given
        Object mock = mock(Object.class);
        Invocation invocation1 = new InvocationOnMock(mock, new Method(Object.class.getDeclaredMethod("equals", Object.class)), new Object[]{mock}, new Location(0, 0), null);
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        Invocation invocation2 = new InvocationOnMock(mock, new Method(Object.class.getDeclaredMethod("equals", Object.class)), new Object[]{mock}, new Location(0, 0), null);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        // when
        boolean result = matcher1.matches(matcher2);

        // then
        assertThat(result, is(true));
    }

    @Test
    public void shouldNotMatchInvocationWithDifferentArguments() throws Exception {
        // given
        Object mock = mock(Object.class);
        Invocation invocation1 = new InvocationOnMock(mock, new Method(Object.class.getDeclaredMethod("equals", Object.class)), new Object[]{mock}, new Location(0, 0), null);
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        Invocation invocation2 = new InvocationOnMock(mock, new Method(Object.class.getDeclaredMethod("equals", int.class)), new Object[]{Integer.valueOf(1)}, new Location(0, 0), null);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        // when
        boolean result = matcher1.matches(matcher2);

        // then
        assertThat(result, is(false));
    }

    @Test
    public void shouldMatchInvocationWithSameMethodAndArgumentsWithOverloadedMethods() throws Exception {
        // given
        Object mock = mock(Object.class);
        Invocation invocation1 = new InvocationOnMock(mock, new Method(Object.class.getDeclaredMethod("equals", Object.class)), new Object[]{mock}, new Location(0, 0), null);
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        Invocation invocation2 = new InvocationOnMock(mock, new Method(Object.class.getDeclaredMethod("equals", String.class)), new Object[]{"mockito"}, new Location(0, 0), null);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        // when
        boolean result = matcher1.matches(matcher2);

        // then
        assertThat(result, is(false));
    }

    @Test
    public void shouldMatchInvocationWithSameMethodAndDifferentArgumentsButSameMatcher() throws Exception {
        // given
        Object mock = mock(Object.class);
        Invocation invocation1 = new InvocationOnMock(mock, new Method(Object.class.getDeclaredMethod("equals", Object.class)), new Object[]{mock}, new Location(0, 0), null);
        Matcher mockMatcher = mock(Matcher.class);
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1, Arrays.asList(mockMatcher));
        Invocation invocation2 = new InvocationOnMock(mock, new Method(Object.class.getDeclaredMethod("equals", int.class)), new Object[]{Integer.valueOf(1)}, new Location(0, 0), null);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2, Arrays.asList(mockMatcher));

        // when
        boolean result = matcher1.matches(matcher2);

        // then
        assertThat(result, is(true));
    }

    @Test
    public void shouldCaptureArgumentsIntoMatchers() throws Exception {
        // given
        Object mock = mock(Object.class);
        Invocation invocation = new InvocationOnMock(mock, new Method(Object.class.getDeclaredMethod("equals", Object.class)), new Object[]{mock}, new Location(0, 0), null);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        Matcher mockMatcher = mock(Matcher.class);
        when(mockMatcher.matches(any(Object.class))).thenReturn(true);
        InvocationMatcher matcherWithMatcher = new InvocationMatcher(invocation, Arrays.asList(mockMatcher));

        // when
        matcherWithMatcher.captureArgumentsFrom(invocation);

        // then
        List<Matcher> matchers = matcherWithMatcher.getMatchers();
        Matcher captureMatcher = matchers.get(0);
        Object capturedArgument = captureMatcher.getCapture();

        // then
        assertThat(capturedArgument, is(mock));
    }

    @Test
    public void shouldCaptureArgumentsIntoMatchersWithVarargs() throws Exception {
        // given
        Object mock = mock(Object.class);
        Invocation invocation = new InvocationOnMock(mock, new Method(Object.class.getDeclaredMethod("equals", Object[].class)), null, new Location(0, 0), null);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        Matcher mockMatcher = mock(Matcher.class);
        when(mockMatcher.matches(any(Object[].class))).thenReturn(true);
        InvocationMatcher matcherWithMatcher = new InvocationMatcher(invocation, Arrays.asList(mockMatcher));
        Object[] array = new Object[]{mock, mock};

        // when
        matcherWithMatcher.captureArgumentsFrom(invocation);

        // then
        List<Matcher> matchers = matcherWithMatcher.getMatchers();
        Matcher captureMatcher = matchers.get(0);
        Object[] capturedArguments = captureMatcher.getCapture();

        // then
        assertThat(capturedArguments, isA(Object[].class));
        assertThat(Arrays.asList(capturedArguments), is(Arrays.asList(array)));
    }

    @Test
    public void shouldThrowExceptionWhenTryingToCaptureArgumentsFromVarargsInvocation() throws Exception {
        // given
        Object mock = mock(Object.class);
        Invocation invocation = new InvocationOnMock(mock, new Method(Object.class.getDeclaredMethod("equals", Object[].class)), null, new Location(0, 0), null);
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        // when, then
        try {
            matcher.captureArgumentsFrom(invocation);
            fail("Expected exception");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void shouldReturnEmptyListWhenNoMatchersAreProvided() throws Exception {
        // given
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        // when
        List<Matcher> result = matcher.getMatchers();

        // then
        assertThat(result, is(Collections.emptyList()));
    }
}
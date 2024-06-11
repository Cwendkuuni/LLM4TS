package org.mockito.internal.invocation;

import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import org.junit.jupiter.api.Test;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(equalTo(1));
        matchers.add(equalTo("test"));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertThat(invocationMatcher.getInvocation(), is(invocation));
        assertThat(invocationMatcher.getMatchers(), is(matchers));
    }

    @Test
    public void testConstructorWithoutMatchers() {
        Invocation invocation = mock(Invocation.class);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertThat(invocationMatcher.getInvocation(), is(invocation));
        assertThat(invocationMatcher.getMatchers(), is(empty()));
    }

    @Test
    public void testGetMethod() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertThat(invocationMatcher.getMethod(), is(method));
    }

    @Test
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(equalTo(1));
        matchers.add(equalTo("test"));
        when(invocation.toString(matchers, any(PrintSettings.class))).thenReturn("invocationString");

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertThat(invocationMatcher.toString(), is("invocationString"));
    }

    @Test
    public void testMatches() {
        Invocation invocation = mock(Invocation.class);
        Invocation actual = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(actual.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(actual.getMethod()).thenReturn(mock(Method.class));
        when(invocation.argumentsToMatchers()).thenReturn(new ArrayList<>());
        when(actual.argumentsToMatchers()).thenReturn(new ArrayList<>());
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(actual.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getMethod().equals(invocation.getMethod())).thenReturn(true);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertThat(invocationMatcher.matches(actual), is(true));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod().getName()).thenReturn("method");
        when(candidate.getMethod().getName()).thenReturn("method");
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getInvocation().getMock()).thenReturn(mock(Object.class));
        when(candidate.getInvocation().getMock()).thenReturn(mock(Object.class));
        when(invocation.hasSameMethod(candidate)).thenReturn(false);
        when(invocation.safelyArgumentsMatch(candidate.getArguments())).thenReturn(true);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertThat(invocationMatcher.hasSimilarMethod(candidate), is(false));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getMethod().equals(candidate.getMethod())).thenReturn(true);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertThat(invocationMatcher.hasSameMethod(candidate), is(true));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        Invocation i = mock(Invocation.class);
        Matcher matcher = mock(CapturesArguments.class);
        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
        when(i.getArguments()).thenReturn(new Object[]{1});
        when(i.getArguments()).thenReturn(new Object[]{1});
        when(matcher instanceof CapturesArguments).thenReturn(true);
        when(i.getArguments()[0]).thenReturn(1);
        doNothing().when((CapturesArguments) matcher).captureFrom(captor.capture());

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        invocationMatcher.getMatchers().add(matcher);
        invocationMatcher.captureArgumentsFrom(i);

        assertThat(captor.getValue(), is(1));
    }
}
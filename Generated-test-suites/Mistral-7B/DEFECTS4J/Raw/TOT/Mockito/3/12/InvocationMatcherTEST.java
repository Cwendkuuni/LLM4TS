package org.mockito.internal.invocation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class InvocationMatcherTest {

    private InvocationMatcher matcher;
    private Object mock;
    private List<Object> arguments;

    @Before
    public void setUp() {
        mock = mock(TestClass.class);
        Object[] rawArguments = {1, 2, Arrays.asList(3, 4, 5)};
        arguments = Arrays.asList(1, 2, Arrays.asList(3, 4, 5));
        matcher = new InvocationMatcher(mock, arguments);
    }

    @Test
    public void testMatches() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationOnMock invocationMock = (InvocationOnMock) invocation;
        invocationMock.setMock(mock);
        invocationMock.setMethod(matcher.getMethod());
        invocationMock.setArguments(matcher.getInvocation().getArguments());

        assertThat(matcher.matches(invocation), is(true));
    }

    @Test
    public void testMatchesWithDifferentMethod() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationOnMock invocationMock = (InvocationOnMock) invocation;
        invocationMock.setMock(mock);
        invocationMock.setMethod(Mockito.mock(TestClass.class).getMethod("differentMethod", int.class));

        assertThat(matcher.matches(invocation), is(false));
    }

    @Test
    public void testMatchesWithDifferentMock() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationOnMock invocationMock = (InvocationOnMock) invocation;
        invocationMock.setMock(Mockito.mock(TestClass.class));

        assertThat(matcher.matches(invocation), is(false));
    }

    @Test
    public void testMatchesWithDifferentArguments() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationOnMock invocationMock = (InvocationOnMock) invocation;
        invocationMock.setMock(mock);
        invocationMock.setMethod(matcher.getMethod());
        invocationMock.setArguments(new Object[]{2, Arrays.asList(6, 7, 8)});

        assertThat(matcher.matches(invocation), is(false));
    }

    @Test
    public void testMatchesWithSameMethodAndArguments() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationOnMock invocationMock = (InvocationOnMock) invocation;
        invocationMock.setMock(mock);
        invocationMock.setMethod(matcher.getMethod());
        invocationMock.setArguments(arguments.toArray());

        assertThat(matcher.matches(invocation), is(true));
    }

    @Test
    public void testMatchesWithVarargs() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationOnMock invocationMock = (InvocationOnMock) invocation;
        invocationMock.setMock(mock);
        invocationMock.setMethod(matcher.getMethod());
        Object[] rawArguments = {1, 2, Arrays.asList(3, 4, 5)};
        invocationMock.setRawArguments(rawArguments);

        ArgumentCaptor<List> argumentCaptor = ArgumentCaptor.forClass(List.class);
        matcher.captureArgumentsFrom(invocation);

        verify(mock).methodWithVarargs(matcher.getInvocation().getMock(), argumentCaptor.capture());
        List<Object> capturedArguments = argumentCaptor.getValue();
        assertThat(capturedArguments, is(arguments));
    }

    @Test
    public void testMatchesWithCapturesArguments() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationOnMock invocationMock = (InvocationOnMock) invocation;
        invocationMock.setMock(mock);
        invocationMock.setMethod(matcher.getMethod());
        Object[] rawArguments = {1, 2, Arrays.asList(3, 4, 5)};
        invocationMock.setRawArguments(rawArguments);

        matcher.captureArgumentsFrom(invocation);

        ArgumentCaptor<Integer> argumentCaptor1 = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<List> argumentCaptor2 = ArgumentCaptor.forClass(List.class);

        verify(mock).methodWithArguments(matcher.getInvocation().getMock(), argumentCaptor1.capture(), argumentCaptor2.capture());

        int capturedArgument1 = argumentCaptor1.getValue();
        List<Object> capturedArguments2 = argumentCaptor2.getValue();

        assertThat(capturedArgument1, is(1));
        assertThat(capturedArguments2, is(arguments));
    }

    @Test
    public void testMatchesWithCapturesArgumentsAndVarargs() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationOnMock invocationMock = (InvocationOnMock) invocation;
        invocationMock.setMock(mock);
        invocationMock.setMethod(matcher.getMethod());
        Object[] rawArguments = {1, 2, Arrays.asList(3, 4, 5), 6};
        invocationMock.setRawArguments(rawArguments);

        matcher.captureArgumentsFrom(invocation);

        ArgumentCaptor<Integer> argumentCaptor1 = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<List> argumentCaptor2 = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<Object> argumentCaptor3 = ArgumentCaptor.forClass(Object.class);

        verify(mock).methodWithArgumentsAndVarargs(matcher.getInvocation().getMock(),
                argumentCaptor1.capture(), argumentCaptor2.capture(), argumentCaptor3.capture());

        int capturedArgument1 = argumentCaptor1.getValue();
        List<Object> capturedArguments2 = argumentCaptor2.getValue();
        Object capturedArgument3 = argumentCaptor3.getValue();

        assertThat(capturedArgument1, is(1));
        assertThat(capturedArguments2, is(arguments));
        assertThat(capturedArgument3, is(6));
    }
}
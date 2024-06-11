package org.mockito.internal.invocation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockitoMock;
import org.mockito.stub.Answer;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    @MockitoMock(answers = Answer.CALLS_REAL_METHODS)
    private MyClass myClass;

    @Before
    public void setup() {
        when(myClass.methodWithOneArg(anyInt())).thenCallRealMethod();
        when(myClass.methodWithTwoArgs(anyInt(), anyInt())).thenCallRealMethod();
        when(myClass.methodWithVarargs(anyInt(), anyInt(), anyInt())).thenCallRealMethod();
    }

    @Test
    public void testMethodWithOneArg() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(myClass.methodWithOneArg(1));
        assertThat(invocationMatcher, is(instanceOf(InvocationMatcher.class)));
        assertThat(invocationMatcher.getMethod().getName(), is("methodWithOneArg"));
        assertThat(invocationMatcher.getInvocation().getMock(), is(myClass));
        ArgumentCaptor<Integer> argumentCaptor = argumentCaptor(Integer.class);
        verify(myClass).methodWithOneArg(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue(), is(1));
    }

    @Test
    public void testMethodWithTwoArgs() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(myClass.methodWithTwoArgs(1, 2));
        assertThat(invocationMatcher, is(instanceOf(InvocationMatcher.class)));
        assertThat(invocationMatcher.getMethod().getName(), is("methodWithTwoArgs"));
        assertThat(invocationMatcher.getInvocation().getMock(), is(myClass));
        ArgumentCaptor<Integer> firstArgumentCaptor = argumentCaptor(Integer.class);
        ArgumentCaptor<Integer> secondArgumentCaptor = argumentCaptor(Integer.class);
        verify(myClass).methodWithTwoArgs(firstArgumentCaptor.capture(), secondArgumentCaptor.capture());
        assertThat(firstArgumentCaptor.getValue(), is(1));
        assertThat(secondArgumentCaptor.getValue(), is(2));
    }

    @Test
    public void testMethodWithVarargs() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(myClass.methodWithVarargs(1, 2, new int[]{3, 4, 5}));
        assertThat(invocationMatcher, is(instanceOf(InvocationMatcher.class)));
        assertThat(invocationMatcher.getMethod().getName(), is("methodWithVarargs"));
        assertThat(invocationMatcher.getInvocation().getMock(), is(myClass));
        ArgumentCaptor<Integer> firstArgumentCaptor = argumentCaptor(Integer.class);
        ArgumentCaptor<Integer> secondArgumentCaptor = argumentCaptor(Integer.class);
        ArgumentCaptor<int[]> varargsArgumentCaptor = argumentCaptor(int[].class);
        verify(myClass).methodWithVarargs(firstArgumentCaptor.capture(), secondArgumentCaptor.capture(), varargsArgumentCaptor.capture());
        assertThat(firstArgumentCaptor.getValue(), is(1));
        assertThat(secondArgumentCaptor.getValue(), is(2));
        int[] capturedVarargs = varargsArgumentCaptor.getValue();
        assertThat(capturedVarargs, is(new int[]{3, 4, 5}));
    }

    @Test
    public void testMethodWithVarargs_emptyArray() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(myClass.methodWithVarargs(1, 2, new int[0]));
        assertThat(invocationMatcher, is(instanceOf(InvocationMatcher.class)));
        assertThat(invocationMatcher.getMethod().getName(), is("methodWithVarargs"));
        assertThat(invocationMatcher.getInvocation().getMock(), is(myClass));
        ArgumentCaptor<Integer> firstArgumentCaptor = argumentCaptor(Integer.class);
        ArgumentCaptor<Integer> secondArgumentCaptor = argumentCaptor(Integer.class);
        ArgumentCaptor<int[]> varargsArgumentCaptor = argumentCaptor(int[].class);
        verify(myClass).methodWithVarargs(firstArgumentCaptor.capture(), secondArgumentCaptor.capture(), varargsArgumentCaptor.capture());
        assertThat(firstArgumentCaptor.getValue(), is(1));
        assertThat(secondArgumentCaptor.getValue(), is(2));
        int[] capturedVarargs = varargsArgumentCaptor.getValue();
        assertThat(capturedVarargs, is(new int[0]));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testMethodWithVarargs_captureFrom() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(myClass.methodWithVarargs(1, 2));
        invocationMatcher.captureArgumentsFrom(myClass);
    }

    private static class MyClass {
        public void methodWithOneArg(int arg) {
        }

        public void methodWithTwoArgs(int arg1, int arg2) {
        }

        public void methodWithVarargs(int arg1, int arg2, int... args) {
        }
    }
}
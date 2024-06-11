package org.mockito.internal.invocation;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.quality.Strictness;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("unchecked")
public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherCreation() {
        // given
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        List<Matcher> matchers = Arrays.asList(is(1), is("test"));

        // when
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // then
        assertThat(invocationMatcher.getInvocation(), is(invocation));
        assertThat(invocationMatcher.getMatchers(), is(matchers));
    }

    @Test
    public void testInvocationMatcherCreationWithEmptyMatchersList() {
        // given
        Invocation invocation = Mockito.mock(InvocationOnMock.class);

        // when
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // then
        assertThat(invocationMatcher.getInvocation(), is(invocation));
        assertThat(invocationMatcher.getMatchers(), notNullValue());
        assertThat(invocationMatcher.getMatchers(), is(Collections.emptyList()));
    }

    @Test
    public void testGetMethod() {
        // given
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        Method method = Mockito.mock(Method.class);
        given(invocation.getMethod()).willReturn(method);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // then
        Method returnedMethod = invocationMatcher.getMethod();
        verify(invocation).getMethod();
        assertThat(returnedMethod, sameInstance(method));
    }

    @Test
    public void testMatchesSameInvocation() {
        // given
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        List<Matcher> matchers = Arrays.asList(is(1), is("test"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // when
        Invocation sameInvocation = Mockito.mock(InvocationOnMock.class);
        given(sameInvocation.getMock()).willReturn(invocation.getMock());
        given(sameInvocation.getMethod()).willReturn(invocation.getMethod());

        // then
        boolean matches = invocationMatcher.matches(sameInvocation);
        verify(invocation).getMock();
        verify(invocation).getMethod();
        verify(sameInvocation).getMock();
        verify(sameInvocation).getMethod();
        assertThat(matches, is(true));
    }

    @Test
    public void testMatchesDifferentInvocations() {
        // given
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        List<Matcher> matchers = Arrays.asList(is(1), is("test"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Invocation differentInvocation = Mockito.mock(InvocationOnMock.class);
        given(differentInvocation.getMock()).willReturn(invocation.getMock());
        given(differentInvocation.getMethod()).willReturn(Mockito.mock(Method.class));

        // when
        boolean matches = invocationMatcher.matches(differentInvocation);

        // then
        verify(invocation).getMock();
        verify(invocation).getMethod();
        verify(differentInvocation).getMock();
        verify(differentInvocation).getMethod();
        assertThat(matches, is(false));
    }

    @Test
    public void testMatchesWithNullInvocation() {
        // given
        InvocationMatcher invocationMatcher = new InvocationMatcher(Mockito.mock(InvocationOnMock.class), Collections.emptyList());

        // when
        Invocation invocation = null;
        boolean matches = invocationMatcher.matches(invocation);

        // then
        assertThat(matches, is(false));
    }

    @Test(expected = AssertionError.class)
    public void testMatchesThrowsAssertionErrorWhenInvocationsAreDifferentAndMatchersAreNotEmpty() {
        // given
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        List<Matcher> matchers = Arrays.asList(is(1), is("test"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Invocation differentInvocation = Mockito.mock(InvocationOnMock.class);
        given(differentInvocation.getMock()).willReturn(invocation.getMock());
        given(differentInvocation.getMethod()).willReturn(Mockito.mock(Method.class));

        // when
        invocationMatcher.matches(differentInvocation);

        // then
        // AssertionError is expected to be thrown
    }

    @Test
    public void testSafelyArgumentsMatch() {
        // given
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        Object[] arguments = new Object[0];
        given(invocation.getArguments()).willReturn(arguments);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // when
        boolean matches = invocationMatcher.safelyArgumentsMatch(arguments);

        // then
        verify(invocation).getArguments();
        assertThat(matches, is(true));
    }

    @Test
    public void testHasSimilarMethodWithSameMethod() {
        // given
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        Method method = Mockito.mock(Method.class);
        given(invocation.getMethod()).willReturn(method);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation sameInvocation = Mockito.mock(InvocationOnMock.class);
        given(sameInvocation.getMock()).willReturn(invocation.getMock());
        given(sameInvocation.getMethod()).willReturn(method);

        // when
        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(sameInvocation);

        // then
        verify(invocation).getMock();
        verify(invocation).getMethod();
        verify(sameInvocation).getMock();
        verify(sameInvocation).getMethod();
        assertThat(hasSimilarMethod, is(true));
    }

    @Test
    public void testHasSimilarMethodWithDifferentMethods() {
        // given
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        Method method1 = Mockito.mock(Method.class);
        given(invocation.getMethod()).willReturn(method1);

        Invocation differentInvocation = Mockito.mock(InvocationOnMock.class);
        Method method2 = Mockito.mock(Method.class);
        given(differentInvocation.getMethod()).willReturn(method2);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // when
        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(differentInvocation);

        // then
        verify(invocation).getMock();
        verify(invocation).getMethod();
        verify(differentInvocation).getMock();
        verify(differentInvocation).getMethod();
        assertThat(hasSimilarMethod, is(false));
    }

    @Test
    public void testHasSameMethodWithSameMethod() {
        // given
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        Method method = Mockito.mock(Method.class);
        given(invocation.getMethod()).willReturn(method);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation sameInvocation = Mockito.mock(InvocationOnMock.class);
        given(sameInvocation.getMock()).willReturn(invocation.getMock());
        given(sameInvocation.getMethod()).willReturn(method);

        // when
        boolean hasSameMethod = invocationMatcher.hasSameMethod(sameInvocation);

        // then
        verify(invocation).getMock();
        verify(invocation).getMethod();
        verify(sameInvocation).getMock();
        verify(sameInvocation).getMethod();
        assertThat(hasSameMethod, is(true));
    }

    @Test
    public void testHasSameMethodWithDifferentMethods() {
        // given
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        Method method1 = Mockito.mock(Method.class);
        given(invocation.getMethod()).willReturn(method1);

        Invocation differentInvocation = Mockito.mock(InvocationOnMock.class);
        Method method2 = Mockito.mock(Method.class);
        given(differentInvocation.getMethod()).willReturn(method2);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // when
        boolean hasSameMethod = invocationMatcher.hasSameMethod(differentInvocation);

        // then
        verify(invocation).getMock();
        verify(invocation).getMethod();
        verify(differentInvocation).getMock();
        verify(differentInvocation).getMethod();
        assertThat(hasSameMethod, is(false));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        // given
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        Object arg1 = "test1";
        Object arg2 = 1;
        Object[] rawArguments = new Object[2];
        rawArguments[0] = arg1;
        rawArguments[1] = arg2;
        given(invocation.getArguments()).willReturn(arg1);
        given(invocation.getRawArguments()).willReturn(rawArguments);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);

        // when
        invocationMatcher.captureArgumentsFrom(invocation);

        // then
        verify(invocation).getArguments();
        verify(invocation).getRawArguments();

        // check captured values
        String capturedArg1 = stringArgumentCaptor.getValue();
        Integer capturedArg2 = integerArgumentCaptor.getValue();

        assertThat(capturedArg1, is(arg1));
        assertThat(capturedArg2, is(arg2));

        // given
        Invocation varargsInvocation = Mockito.mock(InvocationOnMock.class);
        String varargsArg1 = "test1";
        String varargsArg2 = "test2";
        Object[] varargsRawArguments = new Object[2];
        varargsRawArguments[0] = varargsArg1;
        varargsRawArguments[1] = varargsArg2;
        given(varargsInvocation.getArguments()).willReturn(varargsArg1);
        given(varargsInvocation.getRawArguments()).willReturn(varargsRawArguments);

        InvocationMatcher varargsInvocationMatcher = new InvocationMatcher(varargsInvocation);

        // when
        invocationMatcher.captureArgumentsFrom(varargsInvocation);

        // then
        verify(varargsInvocation).getArguments();
        verify(varargsInvocation).getRawArguments();

        // check captured values
        String capturedVarargsArg1 = stringArgumentCaptor.getValue();
        String capturedVarargsArg2 = stringArgumentCaptor.getValue(1);

        assertThat(capturedVarargsArg1, is(varargsArg1));
        assertThat(capturedVarargsArg2, is(varargsArg2));
    }

    @Test
    public void testCreateFrom() {
        // given
        Invocation invocation1 = Mockito.mock(InvocationOnMock.class);
        Invocation invocation2 = Mockito.mock(InvocationOnMock.class);
        List<Invocation> invocationsList = Arrays.asList(invocation1, invocation2);

        // when
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocationsList);

        // then
        InvocationMatcher invocationMatcher1 = invocationMatchers.get(0);
        InvocationMatcher invocationMatcher2 = invocationMatchers.get(1);

        Invocation invocation1FromMatcher = invocationMatcher1.getInvocation();
        Invocation invocation2FromMatcher = invocationMatcher2.getInvocation();

        assertThat(invocationsList, hasSize(2));
        assertThat(invocation1FromMatcher, is(invocation1));
        assertThat(invocation2FromMatcher, is(invocation2));
    }

    @Test
    public void testToString() {
        // given
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        List<Matcher> matchers = Arrays.asList(is(1), is("test"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // when
        String toString = invocationMatcher.toString();

        // then
        // the output of the toString method should be customized based on the hamcrest matchers
        String expectedToString = String.format("InvocationMatcher(%s)", Arrays.toString(matchers.toArray()));
        assertThat(toString, is(expectedToString));
    }

    @Test
    public void testMatchesInvocationWithStrictInvocationMatching() {
        // given
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        List<Matcher> matchers = Arrays.asList(is(1), is("test"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Invocation differentInvocation = Mockito.mock(InvocationOnMock.class);
        List<Matcher> differentMatchers = Arrays.asList(is(2), is("test"));
        InvocationMatcher differentInvocationMatcher = new InvocationMatcher(differentInvocation, differentMatchers);

        given(Mockito.library().getStrictness()).willReturn(Strictness.STRICT);

        // when
        boolean matches = invocationMatcher.matches(differentInvocationMatcher);

        // then
        verify(invocation).getMock();
        verify(differentInvocation).getMock();
        assertThat(matches, is(false));
    }
}
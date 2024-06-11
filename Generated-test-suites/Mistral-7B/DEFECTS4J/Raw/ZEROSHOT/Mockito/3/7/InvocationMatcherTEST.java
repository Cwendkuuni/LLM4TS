package org.mockito.internal.invocation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;
    private InvocationOnMock invocationOnMock;
    private Object mock;

    @Before
    public void setUp() {
        mock = Mockito.mock(Object.class);
        invocationOnMock = Mockito.mock(InvocationOnMock.class);
        invocationMatcher = new InvocationMatcher(new InvocationOnMock(new Method("testMethod", new Class[0]), mock, invocationOnMock, new Object[0]), Collections.emptyList());
    }

    @Test
    public void testMatchesSameInvocation() {
        InvocationMatcher sameMatcher = new InvocationMatcher(invocationMatcher.getInvocation(), Collections.emptyList());
        assertThat(invocationMatcher.matches(sameMatcher), is(true));
    }

    @Test
    public void testMatchesDifferentMock() {
        Object otherMock = Mockito.mock(Object.class);
        InvocationMatcher otherMatcher = new InvocationMatcher(invocationMatcher.getInvocation(), Collections.emptyList());
        otherMatcher = new InvocationMatcher(otherMatcher.getInvocation(), Collections.emptyList());
        assertThat(invocationMatcher.matches(otherMatcher), is(false));
    }

    @Test
    public void testMatchesDifferentMethod() {
        InvocationOnMock differentInvocation = Mockito.mock(InvocationOnMock.class);
        Method differentMethod = new Method("differentMethod", new Class[0]);
        InvocationMatcher differentMatcher = new InvocationMatcher(new InvocationOnMock(differentMethod, mock, differentInvocation, new Object[0]), Collections.emptyList());
        assertThat(invocationMatcher.matches(differentMatcher), is(false));
    }

    @Test
    public void testMatchesSameMethod() {
        InvocationOnMock sameInvocation = Mockito.mock(InvocationOnMock.class);
        Method sameMethod = invocationMatcher.getMethod();
        InvocationMatcher sameMatcher = new InvocationMatcher(sameInvocation, Collections.emptyList());
        assertThat(invocationMatcher.matches(sameMatcher), is(true));
    }

    @Test
    public void testMatchesDifferentArguments() {
        Object[] differentArguments = new Object[]{1L, "test"};
        InvocationMatcher differentMatcher = new InvocationMatcher(invocationMatcher.getInvocation(), Arrays.asList(ArgumentMatchers.argThat(arg -> arg.equals(1L))));
        differentMatcher = new InvocationMatcher(invocationMatcher.getInvocation(), differentMatcher.getMatchers());
        InvocationOnMock invocationWithDifferentArguments = Mockito.mock(InvocationOnMock.class);
        differentMatcher.captureArgumentsFrom(new InvocationOnMock(invocationMatcher.getMethod(), mock, invocationWithDifferentArguments, differentArguments));
        InvocationMatcher newDifferentMatcher = new InvocationMatcher(invocationWithDifferentArguments, differentMatcher.getMatchers());
        assertThat(invocationMatcher.matches(newDifferentMatcher), is(false));
    }

    @Test
    public void testMatchesSameArguments() {
        Object[] sameArguments = new Object[]{1L};
        InvocationMatcher sameMatcher = new InvocationMatcher(invocationMatcher.getInvocation(), Arrays.asList(ArgumentMatchers.argThat(arg -> arg.equals(1L))));
        sameMatcher = new InvocationMatcher(invocationMatcher.getInvocation(), sameMatcher.getMatchers());
        InvocationOnMock invocationWithSameArguments = Mockito.mock(InvocationOnMock.class);
        sameMatcher.captureArgumentsFrom(new InvocationOnMock(invocationMatcher.getMethod(), mock, invocationWithSameArguments, sameArguments));
        InvocationMatcher newSameMatcher = new InvocationMatcher(invocationWithSameArguments, sameMatcher.getMatchers());
        assertThat(invocationMatcher.matches(newSameMatcher), is(true));
    }

    @Test
    public void testHasSameMethod() {
        Method differentMethod = new Method("differentMethod", new Class[0]);
        InvocationMatcher otherMatcher = new InvocationMatcher(new InvocationOnMock(differentMethod, mock, invocationOnMock, new Object[0]), Collections.emptyList());
        assertThat(invocationMatcher.hasSameMethod(otherMatcher), is(false));

        Method sameMethod = invocationMatcher.getMethod();
        InvocationMatcher sameMatcher = new InvocationMatcher(invocationMatcher.getInvocation(), Collections.emptyList());
        sameMatcher = new InvocationMatcher(sameMatcher.getInvocation(), sameMatcher.getMatchers());
        assertThat(invocationMatcher.hasSameMethod(sameMatcher), is(true));
    }

    @Test
    public void testToString() {
        InvocationMatcher matcher = new InvocationMatcher(new InvocationOnMock(new Method("testMethod", new Class[0]), mock, invocationOnMock, new Object[0]), Arrays.asList(ArgumentMatchers.isA(String.class)));
        String toString = matcher.toString();
        assertThat(toString, notNullValue());
        toString = toString.replaceAll("\\[|\\]", "").replaceAll("\\s+", " ");
        String expected = "Invocation on testMethod(Object)";
        assertThat(toString, is(expected));
    }

    @Test
    public void testCaptureArgumentsFromInvocation() {
        InvocationMatcher matcher = new InvocationMatcher(new InvocationOnMock(new Method("testMethod", new Class[]{Integer.TYPE, String.class}), mock, invocationOnMock, new Object[]{1, "test"}), Collections.emptyList());
        Matcher<Object> integerMatcher = ArgumentMatchers.argThat(arg -> arg.equals(1));
        Matcher<Object> stringMatcher = ArgumentMatchers.argThat(arg -> arg.equals("test"));
        matcher = new InvocationMatcher(matcher.getInvocation(), Arrays.asList(integerMatcher, stringMatcher));

        InvocationOnMock invocationWithArguments = Mockito.mock(InvocationOnMock.class);
        matcher.captureArgumentsFrom(invocationWithArguments);
        Object capturedArgument1 = MatcherDecorator.getCapturedArguments(integerMatcher)[0];
        Object capturedArgument2 = MatcherDecorator.getCapturedArguments(stringMatcher)[0];
        Object[] rawArguments = invocationWithArguments.getRawArguments();

        assertThat(capturedArgument1, is(rawArguments[0]));
        assertThat(capturedArgument2, is(rawArguments[1]));
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = Arrays.asList(invocationMatcher.getInvocation(), invocationMatcher.getInvocation());
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        InvocationMatcher firstMatcher = invocationMatchers.get(0);
        InvocationMatcher secondMatcher = invocationMatchers.get(1);
        assertThat(firstMatcher.matches(secondMatcher), is(true));
    }
}
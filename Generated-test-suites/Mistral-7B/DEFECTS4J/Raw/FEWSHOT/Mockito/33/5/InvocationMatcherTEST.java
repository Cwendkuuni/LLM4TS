package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.reporting.PrintSettings.DEFAULT_PRETTY_FORMAT;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.InvocationOnMock;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;

public class InvocationMatcherTest {

    @Test
    public void testNewInstance() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Collections.singletonList(Matchers.anyString());
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);
        InvocationMatcher noMatchersMatcher = new InvocationMatcher(invocation);

        assertEquals(invocation, matcher.getInvocation());
        assertEquals(matchers, matcher.getMatchers());
        assertEquals(noMatchersMatcher.getMatchers(), matcher.getMatchers());
    }

    @Test
    public void testGetMethod() {
        Method wantedMethod = Mockito.mock(Method.class);
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation, Collections.emptyList());

        Mockito.when(invocation.getMethod()).thenReturn(wantedMethod);

        assertEquals(wantedMethod, matcher.getMethod());
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation, Collections.emptyList());

        assertEquals(invocation, matcher.getInvocation());
    }

    @Test
    public void testGetMatchers() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Collections.singletonList(Mockito.mock(Matcher.class));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        assertEquals(matchers, matcher.getMatchers());
    }

    @Test
    public void testToString() {
        Method method = Mockito.mock(Method.class);
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Collections.singletonList(Mockito.mock(Matcher.class));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);
        PrintSettings printSettings = new PrintSettings();

        Mockito.when(invocation.getMethod()).thenReturn(method);

        String expected = String.format("%s(%s)", method.getName(), matchers.toString());

        assertEquals(expected, matcher.toString());
        assertEquals(expected, matcher.toString(DEFAULT_PRETTY_FORMAT));
    }

    @Test
    public void testMatches() {
        Invocation wantedInvocation = Mockito.mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(null, Collections.emptyList());

        Invocation candidate = Mockito.mock(Invocation.class);
        Mockito.when(wantedInvocation.getMock()).thenReturn(candidate.getMock());
        Mockito.when(wantedInvocation.getMethod()).thenReturn(Mockito.mock(Method.class));

        assertFalse(matcher.matches(wantedInvocation));

        InvocationMatcher anotherMatcher = new InvocationMatcher(wantedInvocation);
        assertTrue(anotherMatcher.matches(wantedInvocation));

        InvocationMatcher sameMethodInvocationMatcher = new InvocationMatcher(wantedInvocation);
        assertTrue(sameMethodInvocationMatcher.matches(wantedInvocation));

        InvocationMatcher differentMethodInvocationMatcher = new InvocationMatcher(Mockito.mock(Invocation.class));
        assertFalse(differentMethodInvocationMatcher.matches(wantedInvocation));

        InvocationMatcher sameMockDifferentMethodMatcher = new InvocationMatcher(Mockito.mock(Invocation.class), Collections.singletonList(Matchers.anyInt()));
        assertFalse(sameMockDifferentMethodMatcher.matches(wantedInvocation));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Invocation wantedInvocation = Mockito.mock(Invocation.class);
        Object[] wantedArguments = new Object[]{"foo", 1};
        InvocationMatcher matcher = new InvocationMatcher(wantedInvocation);

        assertTrue(matcher.safelyArgumentsMatch(wantedArguments));

        Invocation candidate = Mockito.mock(Invocation.class);
        Object[] candidateArguments = new Object[]{"bar", 2};

        assertFalse(matcher.safelyArgumentsMatch(candidateArguments));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation wantedInvocation = Mockito.mock(Invocation.class);
        Invocation candidate = Mockito.mock(Invocation.class);
        Method wantedMethod = Mockito.mock(Method.class);
        Method candidateMethod = Mockito.mock(Method.class);

        Mockito.when(wantedInvocation.getMethod()).thenReturn(wantedMethod);
        Mockito.when(candidate.getMethod()).thenReturn(candidateMethod);

        assertTrue(new InvocationMatcher(wantedInvocation).hasSimilarMethod(new InvocationMatcher(wantedInvocation)));
        assertTrue(new InvocationMatcher(wantedInvocation).hasSimilarMethod(new InvocationMatcher(candidate)));
        assertFalse(new InvocationMatcher(wantedInvocation).hasSimilarMethod(new InvocationMatcher(Mockito.mock(Invocation.class))));

        Mockito.when(wantedMethod.getName()).thenReturn("foo");
        Mockito.when(candidateMethod.getName()).thenReturn("bar");

        assertFalse(new InvocationMatcher(wantedInvocation).hasSimilarMethod(new InvocationMatcher(candidate)));
    }

    @Test
    public void testHasSameMethod() {
        Invocation wantedInvocation = Mockito.mock(Invocation.class);
        Invocation candidateInvocation = Mockito.mock(Invocation.class);
        Method wantedMethod = Mockito.mock(Method.class);
        Method candidateMethod = Mockito.mock(Method.class);

        Mockito.when(wantedInvocation.getMethod()).thenReturn(wantedMethod);
        Mockito.when(candidateInvocation.getMethod()).thenReturn(candidateMethod);

        assertTrue(new InvocationMatcher(wantedInvocation).hasSameMethod(new InvocationMatcher(wantedInvocation)));
        assertTrue(new InvocationMatcher(wantedInvocation).hasSameMethod(new InvocationMatcher(candidateInvocation)));

        Mockito.when(wantedMethod.getName()).thenReturn("foo");
        Mockito.when(candidateMethod.getName()).thenReturn("bar");

        assertFalse(new InvocationMatcher(wantedInvocation).hasSameMethod(new InvocationMatcher(candidateInvocation)));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        matcher.captureArgumentsFrom(Mockito.mock(InvocationOnMock.class));
        assertNull(captor.getValue());

        Object[] arguments = new Object[]{"foo"};
        Mockito.when(invocation.getArguments()).thenReturn(arguments);

        matcher.captureArgumentsFrom(Mockito.mock(InvocationOnMock.class));
        assertEquals("foo", captor.getValue());
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = Mockito.mock(List.class);
        List<InvocationMatcher> matchers = InvocationMatcher.createFrom(invocations);

        assertNotNull(matchers);
        assertEquals(invocations.size(), matchers.size());
    }
}
package org.mockito.internal.invocation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.Invocation;

public class InvocationMatcherTest {

    private Invocation invocation;
    private InvocationMatcher invocationMatcher;

    @Before
    public void setUp() {
        invocation = mock(Invocation.class);
        invocationMatcher = new InvocationMatcher(invocation);
    }

    @Test
    public void testConstructor_withMatchers() {
        List<Matcher> matchers = Collections.<Matcher>emptyList();
        invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructor_noMatchers() {
        assertEquals(Collections.<Matcher>emptyList(), invocationMatcher.getMatchers());
    }

    @Test
    public void testGetMethod() {
        Method method = mock(Method.class);
        Mockito.when(invocation.getMethod()).thenReturn(method);

        assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testGetInvocation() {
        assertEquals(invocation, invocationMatcher.getInvocation());
    }

    @Test
    public void testToString() {
        String expected = "InvocationMatcher:\n" +
                "Matched invocations:\n" +
                "PrintSettings\n" +
                "ArgumentsMatcher\n" +
                "MatchersMatcher\n" +
                "MatcherReport\n";
        assertEquals(expected, invocationMatcher.toString());
    }

    @Test
    public void testMatches_matchingInvocation() {
        Invocation actual = mock(Invocation.class);
        Mockito.when(invocation.getMock()).thenReturn(actual.getMock());
        Mockito.when(invocation.getMethod()).thenReturn(actual.getMethod());
        Mockito.when(new ArgumentsComparator().argumentsMatch(invocationMatcher, actual)).thenReturn(true);

        assertTrue(invocationMatcher.matches(actual));
    }

    @Test
    public void testMatches_notMatchingInvocation() {
        Invocation actual = mock(Invocation.class);
        assertFalse(invocationMatcher.matches(actual));
    }

    @Test
    public void testHasSimilarMethod_sameMethod() {
        Invocation candidate = mock(Invocation.class);
        Mockito.when(candidate.getMethod()).thenReturn(invocation.getMethod());
        Mockito.when(candidate.isVerified()).thenReturn(false);
        Mockito.when(invocation.isVerified()).thenReturn(false);
        Mockito.when(invocation.getMock()).thenReturn(candidate.getMock());

        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethod_differentMethod() {
        Invocation candidate = mock(Invocation.class);
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethod_overloadedButSameArgs() {
        Invocation candidate = mock(Invocation.class);
        Mockito.when(candidate.getMethod()).thenReturn(invocation.getMethod());
        Mockito.when(candidate.isVerified()).thenReturn(false);
        Mockito.when(invocation.isVerified()).thenReturn(false);
        Mockito.when(invocation.getMock()).thenReturn(candidate.getMock());
        Mockito.when(new ArgumentsComparator().argumentsMatch(invocationMatcher, candidate.getArguments())).thenReturn(true);

        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSameMethod() throws NoSuchMethodException {
        Method method = InvocationMatcher.class.getMethod("hasSameMethod", Invocation.class);
        Invocation candidate = mock(Invocation.class);
        Mockito.when(invocation.getMethod()).thenReturn(method);
        Mockito.when(candidate.getMethod()).thenReturn(method);

        assertTrue(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        Mockito.when(invocation.getRawArguments()).thenReturn(new Object[] {1, 2, 3});
        Mockito.when(invocation.getArgumentAt(0, Object.class)).thenReturn(1);
        Mockito.when(invocation.getArgumentAt(1, Object.class)).thenReturn(2);
        Mockito.when(invocation.getArgumentAt(2, Object.class)).thenReturn(3);

        CapturesArguments matcher1 = Matchers.argThat(new CapturesArgumentsMatcher());
        CapturesArguments matcher2 = Matchers.argThat(new CapturesArgumentsMatcher());
        CapturesArguments matcher3 = Matchers.argThat(new CapturesArgumentsMatcher());
        List<Matcher> matchers = Arrays.asList(matcher1, matcher2, matcher3);
        invocationMatcher = new InvocationMatcher(invocation, matchers);

        invocationMatcher.captureArgumentsFrom(invocation);

        assertEquals(1, matcher1.getCapturedArguments().get(0));
        assertEquals(2, matcher2.getCapturedArguments().get(0));
        assertEquals(3, matcher3.getCapturedArguments().get(0));
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = Arrays.asList(mock(Invocation.class), mock(Invocation.class), mock(Invocation.class));
        List<InvocationMatcher> expected = Arrays.asList(new InvocationMatcher(invocations.get(0)), new InvocationMatcher(invocations.get(1)), new InvocationMatcher(invocations.get(2)));

        List<InvocationMatcher> actual = InvocationMatcher.createFrom(invocations);

        assertEquals(expected, actual);
    }
}
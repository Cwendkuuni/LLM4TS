
package org.mockito.internal.invocation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;

public class InvocationMatcherTest {

    private Invocation invocation;
    private InvocationMatcher invocationMatcher;
    private List<Matcher> matchers;

    @Before
    public void setUp() {
        invocation = mock(Invocation.class);
        matchers = new ArrayList<Matcher>();
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void testConstructorWithMatchersEmpty() {
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testConstructorWithMatchersNotEmpty() {
        matchers.add(mock(Matcher.class));
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testGetMethod() {
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);

        assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testGetInvocation() {
        assertEquals(invocation, invocationMatcher.getInvocation());
    }

    @Test
    public void testToString() {
        String toString = "invocation toString";
        when(invocation.toString(matchers, new PrintSettings())).thenReturn(toString);

        assertEquals(toString, invocationMatcher.toString());
    }

    @Test
    public void testMatches() {
        Invocation actual = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(actual.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(actual.getMethod()).thenReturn(mock(Method.class));
        when(invocation.argumentsToMatchers()).thenReturn(matchers);
        when(actual.getArguments()).thenReturn(new Object[] {});

        assertTrue(invocationMatcher.matches(actual));
    }

    @Test
    public void testMatchesDifferentMock() {
        Invocation actual = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(actual.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(actual.getMethod()).thenReturn(mock(Method.class));

        assertFalse(invocationMatcher.matches(actual));
    }

    @Test
    public void testMatchesDifferentMethod() {
        Invocation actual = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(actual.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(actual.getMethod()).thenReturn(mock(Method.class));

        assertFalse(invocationMatcher.matches(actual));
    }

    @Test
    public void testMatchesDifferentArguments() {
        Invocation actual = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(actual.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(actual.getMethod()).thenReturn(mock(Method.class));
        when(invocation.argumentsToMatchers()).thenReturn(matchers);
        when(actual.getArguments()).thenReturn(new Object[] { "arg" });

        assertFalse(invocationMatcher.matches(actual));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(candidate.getMock()).thenReturn(mock(Object.class));
        when(invocation.getLocation()).thenReturn(mock(Location.class));
        when(candidate.getLocation()).thenReturn(mock(Location.class));
        when(candidate.getArguments()).thenReturn(new Object[] {});
        when(candidate.isVerified()).thenReturn(false);

        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethodOverloadedButSameArgs() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(candidate.getMock()).thenReturn(mock(Object.class));
        when(invocation.getLocation()).thenReturn(mock(Location.class));
        when(candidate.getLocation()).thenReturn(mock(Location.class));
        when(candidate.getArguments()).thenReturn(new Object[] { "arg" });
        when(invocation.argumentsMatch(candidate, new ArgumentsComparator())).thenReturn(true);

        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSameMethod() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));

        assertTrue(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation i = mock(Invocation.class);
        Matcher matcher = mock(Matcher.class);
        Object[] arguments = new Object[] { "arg" };
        when(invocation.getArguments()).thenReturn(new Object[] { matcher });
        when(matcher instanceof CapturesArguments).thenReturn(true);
        when(i.getArguments()).thenReturn(arguments);

        invocationMatcher.captureArgumentsFrom(i);

        assertEquals(arguments[0], ((CapturesArguments) matcher).getCapturedArguments().get(0));
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new ArrayList<Invocation>();
        List<InvocationMatcher> result = InvocationMatcher.createFrom(invocations);

        assertTrue(result.isEmpty());
    }

}

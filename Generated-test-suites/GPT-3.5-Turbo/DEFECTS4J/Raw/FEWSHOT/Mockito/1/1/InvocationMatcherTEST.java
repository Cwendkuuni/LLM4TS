package org.mockito.internal.invocation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import org.mockito.invocation.MockHandler;

public class InvocationMatcherTest {
    private Invocation invocation;
    private List<Matcher> matchers;
    private InvocationMatcher invocationMatcher;

    @Before
    public void setUp() {
        invocation = mock(Invocation.class);
        matchers = new ArrayList<Matcher>();

        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void testConstructorWithMatchersempty() {
        assertEquals(Collections.<Matcher>emptyList(), invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithMatchers() {
        List<Matcher> matchers = new ArrayList<Matcher>();
        matchers.add(mock(Matcher.class));
        matchers.add(mock(Matcher.class));

        invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithoutMatchers() {
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(Collections.<Matcher>emptyList(), invocationMatcher.getMatchers());
    }

    @Test
    public void testGetMethod() {
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);

        assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testToString() {
        String expected = "InvocationMatcher@" + Integer.toHexString(invocationMatcher.hashCode());
        assertEquals(expected, invocationMatcher.toString());
    }

    @Test
    public void testMatches() {
        Invocation actual = mock(Invocation.class);
        MockHandler mockHandler = mock(MockHandler.class);
        when(invocation.getMock()).thenReturn(mockHandler);
        when(actual.getMock()).thenReturn(mockHandler);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(actual.getMethod()).thenReturn(mock(Method.class));
        when(new ArgumentsComparator().argumentsMatch(invocationMatcher, actual)).thenReturn(true);

        assertTrue(invocationMatcher.matches(actual));
    }

    @Test
    public void testSafelyArgumentsMatchThrowsException() {
        Invocation actual = mock(Invocation.class);
        MockHandler mockHandler = mock(MockHandler.class);
        when(invocation.getMock()).thenReturn(mockHandler);
        when(actual.getMock()).thenReturn(mockHandler);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(actual.getMethod()).thenReturn(mock(Method.class));
        when(new ArgumentsComparator().argumentsMatch(invocationMatcher, actual)).thenThrow(new RuntimeException());

        assertFalse(invocationMatcher.matches(actual));
    }

    @Test
    public void testHasSimilarMethodWithDifferentMethodName() {
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(method1.getName()).thenReturn("foo");
        when(method2.getName()).thenReturn("bar");

        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(method2);
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(mock(MockHandler.class));
        when(candidate.getMock()).thenReturn(mock(MockHandler.class));

        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethodWithVerifiedCandidate() {
        Method method1 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(method1.getName()).thenReturn("foo");

        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(method1);
        when(candidate.isVerified()).thenReturn(true);
        when(invocation.getMock()).thenReturn(mock(MockHandler.class));
        when(candidate.getMock()).thenReturn(mock(MockHandler.class));

        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethodWithDifferentMock() {
        Method method1 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(method1.getName()).thenReturn("foo");

        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(method1);
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(mock(MockHandler.class));
        when(candidate.getMock()).thenReturn(mock(MockHandler.class));

        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethodWithSameMethodAndDifferentArgs() {
        Method method1 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(method1.getName()).thenReturn("foo");

        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(method1);
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(mock(MockHandler.class));
        when(candidate.getMock()).thenReturn(mock(MockHandler.class));
        when(new ArgumentsComparator().argumentsMatch(invocationMatcher, candidate.getArguments())).thenReturn(false);

        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethodWithSameMethodAndSameArgs() {
        Method method1 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(method1.getName()).thenReturn("foo");

        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(method1);
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(mock(MockHandler.class));
        when(candidate.getMock()).thenReturn(mock(MockHandler.class));
        when(new ArgumentsComparator().argumentsMatch(invocationMatcher, candidate.getArguments())).thenReturn(true);

        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSameMethodWithDifferentMethodName() {
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(method1.getName()).thenReturn("foo");
        when(method2.getName()).thenReturn("bar");

        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(method2);

        assertFalse(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testHasSameMethodWithDifferentMethodParams() {
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(method1.getName()).thenReturn("foo");
        when(method1.getParameterTypes()).thenReturn(new Class[0]);
        when(method2.getName()).thenReturn("foo");
        when(method2.getParameterTypes()).thenReturn(new Class[1]);

        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(method2);

        assertFalse(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testHasSameMethodWithSameMethodAndParams() {
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(method1.getName()).thenReturn("foo");
        when(method1.getParameterTypes()).thenReturn(new Class[0]);
        when(method2.getName()).thenReturn("foo");
        when(method2.getParameterTypes()).thenReturn(new Class[0]);

        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(method2);

        assertTrue(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testGetLocation() {
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);

        assertEquals(location, invocationMatcher.getLocation());
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getRawArguments()).thenReturn(new Object[] { "foo", "bar" });
        
        Matcher matcher = mock(Matcher.class);
        matchers.add(matcher);

        CapturesArguments capturesArguments = mock(CapturesArguments.class);
        when(matcher instanceof CapturesArguments).thenReturn(true);
        when(matcher.captureFrom(anyObject())).thenReturn(capturesArguments);

        invocationMatcher.captureArgumentsFrom(invocation);

        verify(capturesArguments).captureFrom("foo");
        verify(capturesArguments).captureFrom("bar");
    }

    @Test
    public void testCaptureArgumentsFromVararg() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getRawArguments()).thenReturn(new Object[] { "foo", new String[] {"bar", "baz"} });
        
        Matcher matcher = mock(Matcher.class);
        matchers.add(matcher);

        CapturesArguments capturesArguments = mock(CapturesArguments.class);
        when(matcher instanceof CapturesArguments).thenReturn(true);
        when(matcher.captureFrom(anyObject())).thenReturn(capturesArguments);

        invocationMatcher.captureArgumentsFrom(invocation);

        verify(capturesArguments).captureFrom("foo");
        verify(capturesArguments).captureFrom("bar");
        verify(capturesArguments).captureFrom("baz");
    }

    @Test
    public void testCaptureArgumentsFromVarargWithNullValue() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getRawArguments()).thenReturn(new Object[] { "foo", null });
        
        Matcher matcher = mock(Matcher.class);
        matchers.add(matcher);

        CapturesArguments capturesArguments = mock(CapturesArguments.class);
        when(matcher instanceof CapturesArguments).thenReturn(true);
        when(matcher.captureFrom(anyObject())).thenReturn(capturesArguments);

        invocationMatcher.captureArgumentsFrom(invocation);

        verify(capturesArguments).captureFrom("foo");
    }

    @Test
    public void testCreateFrom() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<Invocation> invocations = Arrays.asList(invocation1, invocation2);

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        assertEquals(2, invocationMatchers.size());
        assertTrue(invocationMatchers.get(0) instanceof InvocationMatcher);
        assertTrue(invocationMatchers.get(1) instanceof InvocationMatcher);
    }
}
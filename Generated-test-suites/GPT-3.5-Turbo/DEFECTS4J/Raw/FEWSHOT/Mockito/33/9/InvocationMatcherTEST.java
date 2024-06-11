package org.mockito.internal.invocation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.invocation.Invocation;


public class InvocationMatcherTest {
    private Invocation invocation;
    private List<Matcher> matchers;
    private InvocationMatcher invocationMatcher;
    private Object[] arguments;
    private Method method;

    @Before
    public void setUp() {
        invocation = mock(Invocation.class);
        matchers = Collections.singletonList(any());
        invocationMatcher = new InvocationMatcher(invocation, matchers);
        arguments = new Object[]{};
        method = mock(Method.class);
    }

    @Test
    public void testConstructor() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorNoMatchers() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(invocation.argumentsToMatchers(), invocationMatcher.getMatchers());
    }

    @Test
    public void testGetMethod() {
        invocation.getMethod();
        verify(invocation).getMethod();
    }

    @Test
    public void testGetInvocation() {
        assertEquals(invocation, invocationMatcher.getInvocation());
    }

    @Test
    public void testGetMatchers() {
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testToString() {
        invocation.toString(matchers, new PrintSettings());
        verify(invocation).toString(matchers, new PrintSettings());
    }

    @Test
    public void testMatches() {
        Invocation actual = mock(Invocation.class);
        boolean result = invocationMatcher.matches(actual);
        verify(invocation).getMock();
        verify(actual).getMock();
        verify(invocation).getArguments();
        verify(actual).getArguments();
        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation candidate = mock(Invocation.class);
        boolean result = invocationMatcher.hasSimilarMethod(candidate);
        verify(candidate).isVerified();
        verify(candidate).getMock();
        verify(candidate).getArguments();
        assertFalse(result);
    }

    @Test
    public void testHasSameMethod() {
        Invocation candidate = mock(Invocation.class);
        boolean result = invocationMatcher.hasSameMethod(candidate);
        verify(invocation).getMethod();
        verify(candidate).getMethod();
        assertFalse(result);
    }

    @Test
    public void testGetLocation() {
        invocation.getLocation();
        verify(invocation).getLocation();
    }

    @Test
    public void testToStringWithPrintSettings() {
        invocation.toString(matchers, new PrintSettings());
        verify(invocation).toString(matchers, new PrintSettings());
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation i = mock(Invocation.class);
        CapturesArguments capturesArguments = mock(CapturesArguments.class);
        matchers = Collections.singletonList(capturesArguments);
        invocationMatcher = new InvocationMatcher(invocation, matchers);
        invocationMatcher.captureArgumentsFrom(i);
        verify(i).getArguments();
        verify(capturesArguments).captureFrom(any());
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = Collections.singletonList(invocation);
        InvocationMatcher.createFrom(invocations);
    }
}
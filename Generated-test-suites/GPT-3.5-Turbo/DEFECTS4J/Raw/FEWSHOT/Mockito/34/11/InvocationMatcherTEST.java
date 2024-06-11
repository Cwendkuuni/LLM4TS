package org.mockito.internal.invocation;

import org.junit.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherWithMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertSame(invocation, invocationMatcher.getInvocation());
        assertSame(matchers, invocationMatcher.getMatchers());
        assertEquals(invocation.getMethod(), invocationMatcher.getMethod());
    }

    @Test
    public void testInvocationMatcherWithoutMatchers() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertSame(invocation, invocationMatcher.getInvocation());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
        assertEquals(invocation.getMethod(), invocationMatcher.getMethod());
    }

    @Test
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertNotNull(invocationMatcher.toString());
    }

    @Test
    public void testMatches() {
        Invocation invocation = mock(Invocation.class);
        Object[] args = new Object[]{1, "test"};
        Invocation actualInvocation = mock(Invocation.class);
        Object[] actualArgs = new Object[]{1, "test"};

        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertFalse(invocationMatcher.matches(actualInvocation));

        stub(invocation.getMock()).toReturn("test");

        assertFalse(invocationMatcher.matches(actualInvocation));

        stub(invocation.getMock()).toReturn("test");
        stub(actualInvocation.getMock()).toReturn("test");

        assertFalse(invocationMatcher.matches(actualInvocation));

        stub(invocation.getMethod()).toReturn(mock(Method.class));
        stub(actualInvocation.getMethod()).toReturn(mock(Method.class));

        assertFalse(invocationMatcher.matches(actualInvocation));

        stub(invocation.getArguments()).toReturn(args);
        stub(invocation.getMethod()).toReturn(mock(Method.class));
        stub(invocation.getMatcher()).
                thenReturn((Actual arg0, String arg1) -> true);
      
        stub(actualInvocation.getArguments()).toReturn(actualArgs);
        stub(actualInvocation.getMethod()).toReturn(mock(Method.class));
        stub(actualInvocation.getMatcher()).
                thenReturn((Actual arg0, String arg1) -> true);
      
        assertTrue(invocationMatcher.matches(actualInvocation));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertFalse(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertNotNull(invocationMatcher.getLocation());
    }

    @Test
    public void testToStringPrintSettings() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        PrintSettings printSettings = mock(PrintSettings.class);

        assertNotNull(invocationMatcher.toString(printSettings));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        Invocation i = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        CapturesArguments capturesArguments = mock(CapturesArguments.class);
        Matcher matcher = mock(CapturesArguments.class);

        List<Matcher> matchers = new ArrayList<>();
        matchers.add(matcher);

        stub(matcher instanceof CapturesArguments).toReturn(true);
        stub(i.getArguments()).toReturn(new Object[]{"test"});

        invocationMatcher.captureArgumentsFrom(i);

        verify((CapturesArguments) matcher).captureFrom("test");
    }

    private <T> T stub(T mock) {
        return mock;
    }
}
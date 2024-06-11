package org.mockito.internal.invocation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

public class InvocationMatcherTest {
    private Invocation invocation;
    private List<Matcher> matchers;

    @Before
    public void setup() {
        invocation = mock(Invocation.class);
        matchers = new ArrayList<>();
    }

    @Test
    public void testConstructorWithMatchers() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(Matchers.anyString());
        matchers.add(Matchers.anyInt());

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithEmptyMatchers() {
        when(invocation.getArguments()).thenReturn(new Object[]{});

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertNotNull(invocationMatcher.getMatchers());
        assertFalse(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testConstructorWithEmptyMatchersAndArguments() {
        when(invocation.getArguments()).thenReturn(new Object[]{});

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertNotNull(invocationMatcher.getMatchers());
        assertFalse(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testGetMethod() {
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testToString() {
        String expected = "";
        when(invocation.toString()).thenReturn(expected);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(expected, invocationMatcher.toString());
    }

    @Test
    public void testMatches() {
        Invocation actual = mock(Invocation.class);

        when(invocation.getMock()).thenReturn("mock1");
        when(actual.getMock()).thenReturn("mock1");

        when(invocation.getMethod()).thenReturn(this.getClass().getMethods()[0]);
        when(actual.getMethod()).thenReturn(this.getClass().getMethods()[0]);

        when(invocation.getArguments()).thenReturn(new Object[]{});
        when(actual.getArguments()).thenReturn(new Object[]{});

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertTrue(invocationMatcher.matches(actual));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation candidate = mock(Invocation.class);

        when(invocation.getMethod()).thenReturn(this.getClass().getMethods()[0]);
        when(candidate.getMethod()).thenReturn(this.getClass().getMethods()[0]);

        when(candidate.isVerified()).thenReturn(false);

        when(invocation.getMock()).thenReturn("mock1");
        when(candidate.getMock()).thenReturn("mock1");

        when(invocation.getArguments()).thenReturn(new Object[]{});
        when(candidate.getArguments()).thenReturn(new Object[]{});

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSameMethod() {
        Invocation candidate = mock(Invocation.class);

        when(invocation.getMethod()).thenReturn(this.getClass().getMethods()[0]);
        when(candidate.getMethod()).thenReturn(this.getClass().getMethods()[0]);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertTrue(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testGetLocation() {
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(location, invocationMatcher.getLocation());
    }

    @Test
    public void testCaptureArgumentsFrom() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        invocationMatcher.captureArgumentsFrom(invocation);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCaptureArgumentsFromVarArgs() {
        Object[] arguments = new Object[]{new Object[]{}};
        when(invocation.getMethod()).thenReturn(this.getClass().getMethods()[1]);
        when(invocation.getRawArguments()).thenReturn(arguments);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        invocationMatcher.captureArgumentsFrom(invocation);
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new ArrayList<>();
        invocations.add(invocation);

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        assertEquals(1, invocationMatchers.size());
        assertEquals(invocation, invocationMatchers.get(0).getInvocation());
    }
}
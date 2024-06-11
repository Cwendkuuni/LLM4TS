package org.mockito.internal.invocation;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    private Invocation invocation;
    private List<Matcher> matchers;
    private InvocationMatcher invocationMatcher;

    @Before
    public void setup() {
        invocation = mock(Invocation.class);
        matchers = Collections.<Matcher>emptyList();
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void testGetMethod() {
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);

        Method actualMethod = invocationMatcher.getMethod();

        assertSame(method, actualMethod);
    }

    @Test
    public void testGetInvocation() {
        Invocation actualInvocation = invocationMatcher.getInvocation();

        assertSame(invocation, actualInvocation);
    }

    @Test
    public void testGetMatchers() {
        List<Matcher> actualMatchers = invocationMatcher.getMatchers();

        assertSame(matchers, actualMatchers);
    }

    @Test
    public void testToString() {
        PrintSettings printSettings = mock(PrintSettings.class);
        whenNew(PrintSettings.class).withNoArguments().thenReturn(printSettings);

        String actualString = invocationMatcher.toString();

        assertNotNull(actualString);
    }

    @Test
    public void testMatches_returnTrue() {
        Invocation actualInvocation = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(actualInvocation.getMock());
        when(invocation.getMethod()).thenReturn(actualInvocation.getMethod());
        when(invocation.getArguments()).thenReturn(new Object[]{});
        whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(mock(ArgumentsComparator.class));
        whenNew(ArgumentsComparator.class).withNoArguments().thenThrow(new RuntimeException());

        boolean matches = invocationMatcher.matches(actualInvocation);

        assertTrue(matches);
    }

    @Test
    public void testMatches_returnFalse() {
        Invocation actualInvocation = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(actualInvocation.getMock());
        when(invocation.getMethod()).thenReturn(actualInvocation.getMethod());
        when(invocation.getArguments()).thenReturn(new Object[]{1});
        whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(mock(ArgumentsComparator.class));
        whenNew(ArgumentsComparator.class).withNoArguments().thenThrow(new RuntimeException());

        boolean matches = invocationMatcher.matches(actualInvocation);

        assertFalse(matches);
    }

    @Test
    public void testHasSimilarMethod_returnTrue() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(invocation.getMethod());
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(candidate.getMock());
        whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(mock(ArgumentsComparator.class));
        whenNew(ArgumentsComparator.class).withNoArguments().thenThrow(new RuntimeException());

        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidate);

        assertTrue(hasSimilarMethod);
    }

    @Test
    public void testHasSimilarMethod_returnFalse() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(candidate.getMock());
        whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(mock(ArgumentsComparator.class));
        whenNew(ArgumentsComparator.class).withNoArguments().thenThrow(new RuntimeException());

        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidate);

        assertFalse(hasSimilarMethod);
    }

    @Test
    public void testHasSameMethod_returnTrue() {
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(invocation.getMethod()).thenReturn(method);

        boolean hasSameMethod = invocationMatcher.hasSameMethod(invocation);

        assertTrue(hasSameMethod);
    }

    @Test
    public void testHasSameMethod_returnFalse() {
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(invocation.getMethod()).thenReturn(method2);

        boolean hasSameMethod = invocationMatcher.hasSameMethod(invocation);

        assertFalse(hasSameMethod);
    }

    @Test
    public void testGetLocation() {
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);

        Location actualLocation = invocationMatcher.getLocation();

        assertSame(location, actualLocation);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.isVarArgs()).thenReturn(false);

        invocationMatcher.captureArgumentsFrom(invocation);

        // Verify that capture from method is called for each matcher
        verify(matchers.get(0)).captureFrom(anyObject());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCaptureArgumentsFrom_varargs() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.isVarArgs()).thenReturn(true);
        when(invocation.getRawArguments()).thenReturn(new Object[]{});

        invocationMatcher.captureArgumentsFrom(invocation);
    }

    @Test
    public void testCreateFrom() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<Invocation> invocations = new ArrayList<>();
        invocations.add(invocation1);
        invocations.add(invocation2);

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        assertEquals(2, invocationMatchers.size());
    }
}
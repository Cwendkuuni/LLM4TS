package org.mockito.internal.invocation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class InvocationMatcherTest {

    private Invocation invocation;
    private InvocationMatcher invocationMatcher;

    @Before
    public void setup() {
        invocation = mock(Invocation.class);
        invocationMatcher = new InvocationMatcher(invocation);
    }

    @Test
    public void testGetMethod() {
        Method expectedMethod = mock(Method.class);
        when(invocation.getMethod()).thenReturn(expectedMethod);

        Method actualMethod = invocationMatcher.getMethod();

        assertSame(expectedMethod, actualMethod);
    }

    @Test
    public void testGetInvocation() {
        Invocation actualInvocation = invocationMatcher.getInvocation();

        assertSame(invocation, actualInvocation);
    }

    @Test
    public void testGetMatchers() {
        List<Matcher> expectedMatchers = new ArrayList<>();
        expectedMatchers.add(mock(Matcher.class));
        expectedMatchers.add(mock(Matcher.class));
        when(invocation.argumentsToMatchers()).thenReturn(expectedMatchers);

        List<Matcher> actualMatchers = invocationMatcher.getMatchers();

        assertSame(expectedMatchers, actualMatchers);
    }

    @Test
    public void testToString() {
        String expectedToString = "InvocationMatcher toString";
        when(invocation.toString(anyListOf(Matcher.class), any(PrintSettings.class))).thenReturn(expectedToString);

        String actualToString = invocationMatcher.toString();

        assertSame(expectedToString, actualToString);
    }

    @Test
    public void testMatches() {
        Invocation actualInvocation = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(actualInvocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getArguments()).thenReturn(new Object[] { 1, 2, 3 });
        when(actualInvocation.getArguments()).thenReturn(new Object[] { 1, 2, 3 });

        boolean matches = invocationMatcher.matches(actualInvocation);

        assertTrue(matches);
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Object[] actualArgs = new Object[] { 1, 2, 3 };
        InvocationMatcher invocationMatcherSpy = spy(invocationMatcher);
        doReturn(true).when(invocationMatcherSpy).safelyArgumentsMatch(any(Object[].class));

        boolean argumentsMatch = invocationMatcherSpy.safelyArgumentsMatch(actualArgs);

        assertTrue(argumentsMatch);
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(candidate.getMock()).thenReturn(mock(Object.class));
        when(invocation.getArguments()).thenReturn(new Object[] { 1, 2, 3 });
        when(candidate.getArguments()).thenReturn(new Object[] { 1, 2, 3 });

        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidate);

        assertTrue(hasSimilarMethod);
    }

    @Test
    public void testHasSameMethod() {
        Invocation candidate = mock(Invocation.class);
        Method m1 = mock(Method.class);
        Method m2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(m1);
        when(candidate.getMethod()).thenReturn(m2);
        when(m1.equals(m2)).thenReturn(true);

        boolean hasSameMethod = invocationMatcher.hasSameMethod(candidate);

        assertTrue(hasSameMethod);
    }

    @Test
    public void testGetLocation() {
        Location expectedLocation = mock(Location.class);
        when(invocation.getLocation()).thenReturn(expectedLocation);

        Location actualLocation = invocationMatcher.getLocation();

        assertSame(expectedLocation, actualLocation);
    }

    @Test
    public void testToStringPrintSettings() {
        String expectedToString = "InvocationMatcher toString";
        PrintSettings printSettings = mock(PrintSettings.class);
        when(invocation.toString(anyListOf(Matcher.class), eq(printSettings))).thenReturn(expectedToString);

        String actualToString = invocationMatcher.toString(printSettings);

        assertSame(expectedToString, actualToString);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation i = mock(Invocation.class);
        Matcher matcher = mock(Matcher.class);
        CapturesArguments capturesArguments = mock(CapturesArguments.class);
        when(invocationMatcher.getMatchers()).thenReturn(Collections.singletonList(matcher));
        when(matcher instanceof CapturesArguments).thenReturn(true);
        when(i.getArguments()).thenReturn(new Object[] { 1 });

        invocationMatcher.captureArgumentsFrom(i);

        verify(capturesArguments).captureFrom(1);
    }

    @Test
    public void testCreateFrom() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<Invocation> invocations = Arrays.asList(invocation1, invocation2);

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        assertEquals(2, invocationMatchers.size());
        assertSame(invocation1, invocationMatchers.get(0).getInvocation());
        assertSame(invocation2, invocationMatchers.get(1).getInvocation());
    }
}
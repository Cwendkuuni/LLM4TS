import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.matchers.CapturesArguments;

import java.lang.reflect.Method;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;

    @Before
    public void setUp() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Collections.emptyList();
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void testGetMethod() {
        Method expectedMethod = mock(Method.class);
        when(invocation.getMethod()).thenReturn(expectedMethod);
        Method actualMethod = invocationMatcher.getMethod();
        assertEquals(expectedMethod, actualMethod);
    }

    @Test
    public void testGetInvocation() {
        Invocation expectedInvocation = mock(Invocation.class);
        when(invocationMatcher.getInvocation()).thenReturn(expectedInvocation);
        Invocation actualInvocation = invocationMatcher.getInvocation();
        assertEquals(expectedInvocation, actualInvocation);
    }

    @Test
    public void testGetMatchersEmpty() {
        List<Matcher> expectedMatchers = invocationMatcher.getInvocation().argumentsToMatchers();
        List<Matcher> actualMatchers = invocationMatcher.getMatchers();
        assertEquals(expectedMatchers, actualMatchers);
    }

    @Test
    public void testGetMatchersNonEmpty() {
        List<Matcher> expectedMatchers = new ArrayList<>();
        expectedMatchers.add(mock(Matcher.class));
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Collections.emptyList();
        invocationMatcher = new InvocationMatcher(invocation, expectedMatchers);
        List<Matcher> actualMatchers = invocationMatcher.getMatchers();
        assertEquals(expectedMatchers, actualMatchers);
    }

    @Test
    public void testToString() {
        String expectedString = "example string";
        when(invocationMatcher.toString()).thenReturn(expectedString);
        String actualString = invocationMatcher.toString();
        assertEquals(expectedString, actualString);
    }

    @Test
    public void testMatches() {
        Invocation actualInvocation = mock(Invocation.class);
        when(invocationMatcher.getInvocation().getMock()).thenReturn(mock(Object.class));
        when(invocationMatcher.hasSameMethod(actualInvocation)).thenReturn(true);
        when(invocationMatcher.safelyArgumentsMatch(actualInvocation.getArguments())).thenReturn(true);
        assertTrue(invocationMatcher.matches(actualInvocation));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation candidateInvocation = mock(Invocation.class);
        Invocation actualInvocation = mock(Invocation.class);
        when(invocationMatcher.getMethod().getName()).thenReturn("methodName");
        when(candidateInvocation.getMethod().getName()).thenReturn("methodName");
        when(candidateInvocation.isVerified()).thenReturn(false);
        when(invocationMatcher.getInvocation().getMock()).thenReturn(mock(Object.class));
        when(invocationMatcher.hasSameMethod(candidateInvocation)).thenReturn(true);
        when(invocationMatcher.safelyArgumentsMatch(candidateInvocation.getArguments())).thenReturn(true);
        assertFalse(invocationMatcher.hasSimilarMethod(candidateInvocation));
    }

    @Test
    public void testHasSameMethod() {
        Invocation candidateInvocation = mock(Invocation.class);
        when(invocationMatcher.getMethod()).thenReturn(mock(Method.class));
        when(candidateInvocation.getMethod()).thenReturn(mock(Method.class));
        assertTrue(invocationMatcher.hasSameMethod(candidateInvocation));
    }

    @Test
    public void testGetLocation() {
        Location expectedLocation = mock(Location.class);
        when(invocationMatcher.getInvocation().getLocation()).thenReturn(expectedLocation);
        Location actualLocation = invocationMatcher.getLocation();
        assertEquals(expectedLocation, actualLocation);
    }

    @Test
    public void testToStringWithPrintSettings() {
        String expectedString = "example string";
        PrintSettings printSettings = mock(PrintSettings.class);
        when(invocationMatcher.toString(printSettings)).thenReturn(expectedString);
        String actualString = invocationMatcher.toString(printSettings);
        assertEquals(expectedString, actualString);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        CapturesArguments capturesArguments = mock(CapturesArguments.class);
        when(capturesArguments.captureFrom(any())).thenReturn(true);
        when(invocation.getArguments()).thenReturn(new Object[]{new Object()});
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(capturesArguments);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        invocationMatcher.captureArgumentsFrom(invocation);
        verify(capturesArguments).captureFrom(any());
    }
}
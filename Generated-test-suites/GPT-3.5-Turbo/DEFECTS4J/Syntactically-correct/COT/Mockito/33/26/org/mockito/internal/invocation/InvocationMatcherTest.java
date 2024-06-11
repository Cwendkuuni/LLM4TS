
import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {
    @Test
    public void testGetMethod() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = mock(List.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Method method = invocation.getMethod();
        Method invocationMatcherMethod = invocationMatcher.getMethod();

        assertEquals(method, invocationMatcherMethod);
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = mock(List.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Invocation mockInvocation = invocationMatcher.getInvocation();

        assertEquals(invocation, mockInvocation);
    }

    @Test
    public void testGetMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = mock(List.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        List<Matcher> mockMatchers = invocationMatcher.getMatchers();

        assertEquals(matchers, mockMatchers);
    }

    @Test
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = mock(List.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        String methodToString = invocation.toString(matchers, new PrintSettings());
        String invocationMatcherToString = invocationMatcher.toString();

        assertEquals(methodToString, invocationMatcherToString);
    }

    @Test
    public void testMatches() {
        Invocation invocation = mock(Invocation.class);
        Invocation actual = mock(Invocation.class);
        Object[] arguments = new Object[0];
        when(invocation.getMock()).thenReturn(this);
        when(actual.getMock()).thenReturn(this);
        when(invocation.getMethod()).thenReturn(InvocationMatcherTest.class.getDeclaredMethods()[0]);
        when(actual.getMethod()).thenReturn(InvocationMatcherTest.class.getDeclaredMethods()[0]);
        when(invocation.getArguments()).thenReturn(arguments);
        when(actual.getArguments()).thenReturn(arguments);
        when(invocation.isVerified()).thenReturn(false);
        when(actual.isVerified()).thenReturn(false);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        boolean matches = invocationMatcher.matches(actual);

        assertTrue(matches);
    }

    @Test
    public void testHashCode() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = mock(List.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        int hashCode = invocationMatcher.hashCode();

        assertEquals(invocation.hashCode(), hashCode);
    }

    @Test
    public void testEquals() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = mock(List.class);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation, matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation, matchers);

        boolean equals = invocationMatcher1.equals(invocationMatcher2);

        assertTrue(equals);
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidate);

        assertFalse(hasSimilarMethod);
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        boolean hasSameMethod = invocationMatcher.hasSameMethod(candidate);

        assertFalse(hasSameMethod);
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = mock(List.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Location location = invocation.getLocation();
        Location invocationMatcherLocation = invocationMatcher.getLocation();

        assertEquals(location, invocationMatcherLocation);
    }

    @Test
    public void testToStringPrintSettings() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = mock(List.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        PrintSettings printSettings = mock(PrintSettings.class);
        String methodToString = invocation.toString(matchers, printSettings);
        String invocationMatcherToString = invocationMatcher.toString(printSettings);

        assertEquals(methodToString, invocationMatcherToString);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation i = mock(Invocation.class);
        when(i.getArguments()).thenReturn(new Object[0]);

        invocationMatcher.captureArgumentsFrom(i);
    }

    @Test
    public void testCreateFrom() {
        Invocation invocation = mock(Invocation.class);
        List<Invocation> invocations = new LinkedList<>();
        invocations.add(invocation);

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        assertEquals(1, invocationMatchers.size());
        assertEquals(invocation, invocationMatchers.get(0).getInvocation());
    }
}

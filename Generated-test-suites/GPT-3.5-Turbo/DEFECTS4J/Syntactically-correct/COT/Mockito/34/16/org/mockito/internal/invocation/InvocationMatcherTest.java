import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithInvocationAndMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Collections.emptyList();

        InvocationMatcher invMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals(invocation, invMatcher.getInvocation());
        assertEquals(matchers, invMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithInvocation() {
        Invocation invocation = mock(Invocation.class);

        InvocationMatcher invMatcher = new InvocationMatcher(invocation);

        assertEquals(invocation, invMatcher.getInvocation());
        assertTrue(invMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testGetMethod() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);

        InvocationMatcher invMatcher = new InvocationMatcher(invocation);

        assertEquals(method, invMatcher.getMethod());
    }

    @Test
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Collections.emptyList();
        PrintSettings printSettings = mock(PrintSettings.class);
        String expected = "invocationToString";

        when(invocation.toString(matchers, printSettings)).thenReturn(expected);

        InvocationMatcher invMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals(expected, invMatcher.toString());
    }

    @Test
    public void testMatches() {
        Invocation invocation = mock(Invocation.class);
        Invocation actual = mock(Invocation.class);

        InvocationMatcher invMatcher = new InvocationMatcher(invocation);

        when(invocation.getMock()).thenReturn("mock");
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(actual.getMock()).thenReturn("mock");
        when(actual.getMethod()).thenReturn(mock(Method.class));

        assertTrue(invMatcher.matches(actual));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Invocation invocation = mock(Invocation.class);
        Object[] args = new Object[] {1, 2, 3};

        InvocationMatcher invMatcher = new InvocationMatcher(invocation);

        when(invocation.getArguments()).thenReturn(args);

        assertTrue(invMatcher.safelyArgumentsMatch(args));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        Object[] args = new Object[] {1, 2, 3};

        InvocationMatcher invMatcher = new InvocationMatcher(invocation);

        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getMock()).thenReturn("mock");
        when(invocation.isVerified()).thenReturn(false);
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMock()).thenReturn("mock");
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getArguments()).thenReturn(args);
        when(invMatcher.safelyArgumentsMatch(args)).thenReturn(true);

        assertFalse(invMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        Method method = mock(Method.class);

        InvocationMatcher invMatcher = new InvocationMatcher(invocation);

        when(invocation.getMethod()).thenReturn(method);
        when(candidate.getMethod()).thenReturn(method);

        assertTrue(invMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = mock(Invocation.class);
        Location location = mock(Location.class);

        InvocationMatcher invMatcher = new InvocationMatcher(invocation);

        when(invocation.getLocation()).thenReturn(location);

        assertEquals(location, invMatcher.getLocation());
    }

    @Test
    public void testToStringWithPrintSettings() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Collections.emptyList();
        PrintSettings printSettings = mock(PrintSettings.class);
        String expected = "invocationToString";

        when(invocation.toString(matchers, printSettings)).thenReturn(expected);

        InvocationMatcher invMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals(expected, invMatcher.toString(printSettings));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);

        Object arg1 = new Object();
        Object arg2 = new Object();
        Object arg3 = new Object();

        Invocation i = mock(Invocation.class);
        Object[] args = new Object[] {arg1, arg2, arg3};
        when(i.getArguments()).thenReturn(args);

        CapturesArguments capturesArguments = mock(CapturesArguments.class);
        List<Matcher> matchers = Collections.singletonList(capturesArguments);

        when(invocation.getArgumentsToMatchers()).thenReturn(matchers);

        InvocationMatcher invMatcher = new InvocationMatcher(invocation, matchers);
        invMatcher.captureArgumentsFrom(i);

        verify(capturesArguments).captureFrom(arg1);
        verify(capturesArguments).captureFrom(arg2);
        verify(capturesArguments).captureFrom(arg3);
    }
}
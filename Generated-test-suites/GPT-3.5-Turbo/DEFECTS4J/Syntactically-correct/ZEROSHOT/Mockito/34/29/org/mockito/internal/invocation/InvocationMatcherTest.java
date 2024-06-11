import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;

    @Before
    public void setUp() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(mock(Matcher.class));
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void testGetMethod() {
        Method method = mock(Method.class);
        when(invocationMatcher.getMethod()).thenReturn(method);
        assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = mock(Invocation.class);
        when(invocationMatcher.getInvocation()).thenReturn(invocation);
        assertEquals(invocation, invocationMatcher.getInvocation());
    }

    @Test
    public void testGetMatchers() {
        List<Matcher> matchers = Collections.singletonList(mock(Matcher.class));
        when(invocationMatcher.getMatchers()).thenReturn(matchers);
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.toString(any(), any())).thenReturn("Test Invocation");
        invocationMatcher = new InvocationMatcher(invocation);
        PrintSettings printSettings = mock(PrintSettings.class);
        when(invocationMatcher.toString(printSettings)).thenReturn("Test Invocation");
        assertEquals("Test Invocation", invocationMatcher.toString());
    }

    @Test
    public void testMatches() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getArguments()).thenReturn(new Object[]{});
        when(invocation.isVerified()).thenReturn(false);
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(argumentsComparator);
        when(argumentsComparator.argumentsMatch(any(), any())).thenReturn(true);
        assertTrue(invocationMatcher.matches(invocation));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getArguments()).thenReturn(new Object[]{});
        assertTrue(invocationMatcher.hasSimilarMethod(invocation));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(invocationMatcher.hasSameMethod(invocation)).thenReturn(true);
        assertTrue(invocationMatcher.hasSameMethod(invocation));
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = mock(Invocation.class);
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);
        assertEquals(location, invocationMatcher.getLocation());
    }

    @Test
    public void testToStringPrintSettings() {
        Invocation invocation = mock(Invocation.class);
        PrintSettings printSettings = mock(PrintSettings.class);
        when(invocation.toString(any(), any())).thenReturn("Test Invocation");
        invocationMatcher = new InvocationMatcher(invocation);
        assertEquals("Test Invocation", invocationMatcher.toString(printSettings));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        Matcher matcher = mock(CapturesArguments.class);
        Matchers[] matchers = new Matchers[]{matcher};
        Object[] arguments = new Object[]{};
        when(invocation.getArguments()).thenReturn(arguments);
        when(invocationMatcher.getMatchers()).thenReturn(Arrays.asList(matchers));
        when(matcher instanceof CapturesArguments).thenReturn(true);
        invocationMatcher.captureArgumentsFrom(invocation);
        verify((CapturesArguments)matcher, times(1)).captureFrom(arguments[0]);
    }
}
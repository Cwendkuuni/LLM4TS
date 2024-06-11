import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;

@SuppressWarnings("unchecked")
public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;
    private Invocation invocation;
    private List<Matcher> matchers;

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

        assertEquals(method, invocationMatcher.getMethod());
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
        PrintSettings printSettings = mock(PrintSettings.class);
        when(invocation.toString(matchers, printSettings)).thenReturn("testToString");

        assertEquals("testToString", invocationMatcher.toString());
    }

    @Test
    public void testMatches() {
        Invocation actualInvocation = mock(Invocation.class);

        assertTrue(invocationMatcher.matches(actualInvocation));

        verify(invocation).getMock();
        verify(actualInvocation).getMock();
        verify(invocation).getArguments();
        verify(actualInvocation).getArguments();
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Object[] actualArgs = {};

        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        when(argumentsComparator.argumentsMatch(invocationMatcher, actualArgs)).thenReturn(true);

        assertTrue(invocationMatcher.safelyArgumentsMatch(actualArgs));

        verify(argumentsComparator).argumentsMatch(invocationMatcher, actualArgs);
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation candidate = mock(Invocation.class);

        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(candidate.getMethod()).thenReturn(method);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(candidate.getMock()).thenReturn(mock(Object.class));
        when(candidate.isVerified()).thenReturn(false);

        assertTrue(invocationMatcher.hasSimilarMethod(candidate));

        verify(invocation).getMethod();
        verify(candidate).getMethod();
        verify(invocation).getMethod().getName();
        verify(candidate).getMethod().getName();
        verify(invocation).getMock();
        verify(candidate).getMock();
        verify(candidate).isVerified();
        verify(invocationMatcher).hasSameMethod(candidate);
        verify(invocationMatcher).safelyArgumentsMatch(candidate.getArguments());
    }

    @Test
    public void testHasSameMethod() {
        Invocation candidate = mock(Invocation.class);

        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(candidate.getMethod()).thenReturn(method);

        assertTrue(invocationMatcher.hasSameMethod(candidate));

        verify(invocation).getMethod();
        verify(candidate).getMethod();
        verify(invocation).getMethod().equals(candidate.getMethod());
    }

    @Test
    public void testGetLocation() {
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);

        assertEquals(location, invocationMatcher.getLocation());
    }

    @Test
    public void testToStringWithPrintSettings() {
        PrintSettings printSettings = mock(PrintSettings.class);
        when(invocation.toString(matchers, printSettings)).thenReturn("testToString");

        assertEquals("testToString", invocationMatcher.toString(printSettings));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation i = mock(Invocation.class);
        Object[] arguments = new Object[1];

        when(i.getArguments()).thenReturn(arguments);
        when(matchers.get(0)).thenReturn(mock(CapturesArguments.class));

        invocationMatcher.captureArgumentsFrom(i);

        verify(i).getArguments();
        verify(matchers).get(0);
        verify(matchers.get(0)).captureFrom(arguments[0]);
    }
}
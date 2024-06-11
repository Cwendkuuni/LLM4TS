import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;

@SuppressWarnings("unchecked")
public class InvocationMatcherTest {

    private Invocation invocation;
    private List<Matcher> matchers;
    private InvocationMatcher invocationMatcher;

    @Before
    public void setUp() throws Exception {
        invocation = mock(Invocation.class);
        matchers = Arrays.asList(mock(Matcher.class));
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void testConstructorWithInvocationAndMatchers() {
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithInvocationAndEmptyMatchers() {
        InvocationMatcher emptyMatchersInvocationMatcher = new InvocationMatcher(invocation,
                Collections.<Matcher>emptyList());
        assertEquals(invocation, emptyMatchersInvocationMatcher.getInvocation());
        assertEquals(invocation.argumentsToMatchers(), emptyMatchersInvocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithInvocation() {
        InvocationMatcher noMatchersInvocationMatcher = new InvocationMatcher(invocation);
        assertEquals(invocation, noMatchersInvocationMatcher.getInvocation());
        assertEquals(Collections.<Matcher>emptyList(), noMatchersInvocationMatcher.getMatchers());
    }

    @Test
    public void testGetMethod() {
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        assertEquals(method, invocationMatcher.getMethod());
        verify(invocation).getMethod();
    }

    @Test
    public void testToString() {
        PrintSettings printSettings = new PrintSettings();
        String invocationToString = "Invocation toString";
        when(invocation.toString(matchers, printSettings)).thenReturn(invocationToString);
        assertEquals(invocationToString, invocationMatcher.toString());
        verify(invocation).toString(matchers, printSettings);
    }

    @Test
    public void testMatches() {
        Invocation actualInvocation = mock(Invocation.class);

        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(actualInvocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(actualInvocation.getMethod()).thenReturn(mock(Method.class));
        when(new ArgumentsComparator().argumentsMatch(invocationMatcher, actualInvocation)).thenReturn(true);

        assertTrue(invocationMatcher.matches(actualInvocation));
        verify(invocation).getMock();
        verify(actualInvocation).getMock();
        verify(invocation).getMethod();
        verify(actualInvocation).getMethod();
        verify(new ArgumentsComparator()).argumentsMatch(invocationMatcher, actualInvocation);
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Object[] actualArgs = new Object[0];
        when(new ArgumentsComparator().argumentsMatch(invocationMatcher, actualArgs)).thenReturn(true);

        assertTrue(invocationMatcher.safelyArgumentsMatch(actualArgs));
        verify(new ArgumentsComparator()).argumentsMatch(invocationMatcher, actualArgs);
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation candidate = mock(Invocation.class);

        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(candidate.isVerified()).thenReturn(false);
        when(getInvocation().getMock()).thenReturn(mock(Object.class));
        when(candidate.getMock()).thenReturn(mock(Object.class));
        when(hasSameMethod(candidate)).thenReturn(true);
        when(safelyArgumentsMatch(candidate.getArguments())).thenReturn(false);

        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
        verify(invocation).getMethod();
        verify(candidate).getMethod();
        verify(candidate).isVerified();
        verify(getInvocation()).getMock();
        verify(candidate).getMock();
        verify(hasSameMethod(candidate)).thenReturn(true);
        verify(safelyArgumentsMatch(candidate.getArguments())).thenReturn(false);
    }

    @Test
    public void testHasSameMethod() {
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(candidate.getMethod()).thenReturn(method2);
        assertTrue(invocationMatcher.hasSameMethod(candidate));
        verify(invocation).getMethod();
        verify(candidate).getMethod();

        when(invocation.getMethod()).thenReturn(method1.clone());
        assertTrue(invocationMatcher.hasSameMethod(candidate));
        verify(invocation, times(2)).getMethod();
        verify(candidate, times(2)).getMethod();

        when(method1.equals(method2)).thenReturn(false);
        assertFalse(invocationMatcher.hasSameMethod(candidate));
        verify(invocation, times(3)).getMethod();
        verify(candidate, times(3)).getMethod();
        verify(method1.equals(method2)).thenReturn(false);
    }

    @Test
    public void testGetLocation() {
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);
        assertEquals(location, invocationMatcher.getLocation());
        verify(invocation).getLocation();
    }

    @Test
    public void testToStringWithPrintSettings() {
        PrintSettings printSettings = mock(PrintSettings.class);
        String invocationToString = "Invocation toString";
        when(invocation.toString(matchers, printSettings)).thenReturn(invocationToString);
        assertEquals(invocationToString, invocationMatcher.toString(printSettings));
        verify(invocation).toString(matchers, printSettings);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation i = mock(Invocation.class);
        Object[] arguments = new Object[] { "arg1", "arg2" };
        when(i.getArguments()).thenReturn(arguments);

        CapturesArguments capturesArgumentsMatcher1 = mock(CapturesArguments.class);
        CapturesArguments capturesArgumentsMatcher2 = mock(CapturesArguments.class);
        when(matchers.get(0)).thenReturn(capturesArgumentsMatcher1);
        when(matchers.get(1)).thenReturn(capturesArgumentsMatcher2);

        invocationMatcher.captureArgumentsFrom(i);

        verify(capturesArgumentsMatcher1).captureFrom(arguments[0]);
        verify(capturesArgumentsMatcher2).captureFrom(arguments[1]);

    }

    @Test
    public void testCreateFrom() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<Invocation> invocations = Arrays.asList(invocation1, invocation2);

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        assertEquals(2, invocationMatchers.size());
        assertEquals(invocation1, invocationMatchers.get(0).getInvocation());
        assertEquals(invocation2, invocationMatchers.get(1).getInvocation());
    }

}
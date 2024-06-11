import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.exceptions.VerificationAwareInvocation;
import org.mockito.internal.invocation.ArgumentsComparator;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.reporting.PrintSettings;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InvocationMatcherTest {

    private Invocation invocation;
    private List<Matcher> matchers;

    @Before
    public void setUp() {
        invocation = mock(Invocation.class);
        matchers = Arrays.asList(mock(Matcher.class), mock(Matcher.class));
    }

    @Test
    public void testConstructorWithInvocationAndMatchers() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithInvocation() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testGetMethod() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testToString() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        PrintSettings printSettings = mock(PrintSettings.class);
        when(invocation.toString(matchers, printSettings)).thenReturn("test string");
        assertEquals("test string", invocationMatcher.toString());
    }

    @Test
    public void testMatches() {
        Invocation actual = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getArguments()).thenReturn(new Object[]{});
        when(actual.getMock()).thenReturn(mock(Object.class));
        when(actual.getMethod()).thenReturn(mock(Method.class));
        when(actual.getArguments()).thenReturn(new Object[]{});
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        when(argumentsComparator.argumentsMatch(invocationMatcher, actual)).thenReturn(true);
        assertTrue(invocationMatcher.matches(actual));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(candidate.getMock()).thenReturn(mock(Object.class));
        when(candidate.isVerified()).thenReturn(false);
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        when(invocationMatcher.hasSameMethod(candidate)).thenReturn(true);
        when(argumentsComparator.argumentsMatch(invocationMatcher, candidate.getArguments())).thenReturn(true);
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSameMethod() {
        Invocation candidate = mock(Invocation.class);
        Method m1 = mock(Method.class);
        Method m2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(m1);
        when(candidate.getMethod()).thenReturn(m2);
        when(m1.equals(m2)).thenReturn(true);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertTrue(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testGetLocation() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);
        assertEquals(location, invocationMatcher.getLocation());
    }

    @Test
    public void testToStringWithPrintSettings() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        PrintSettings printSettings = mock(PrintSettings.class);
        when(invocation.toString(matchers, printSettings)).thenReturn("test string");
        assertEquals("test string", invocationMatcher.toString(printSettings));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation i = mock(Invocation.class);
        Matcher matcher1 = mock(Matcher.class);
        Matcher matcher2 = mock(Matcher.class);
        CapturesArguments capturesArguments1 = mock(CapturesArguments.class);
        CapturesArguments capturesArguments2 = mock(CapturesArguments.class);
        when(matchers.get(0)).thenReturn(matcher1);
        when(matchers.get(1)).thenReturn(matcher2);
        when(i.getArguments()).thenReturn(new Object[]{});
        when(i.getArguments().length).thenReturn(2);
        when(capturesArguments1.captureFrom(i.getArguments()[0])).thenReturn(true);
        when(capturesArguments2.captureFrom(i.getArguments()[1])).thenReturn(true);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        invocationMatcher.captureArgumentsFrom(i);
        verify(capturesArguments1).captureFrom(i.getArguments()[0]);
        verify(capturesArguments2).captureFrom(i.getArguments()[1]);
    }

    @Test
    public void testCreateFrom() {
        Invocation i1 = mock(Invocation.class);
        Invocation i2 = mock(Invocation.class);
        List<Invocation> invocations = Arrays.asList(i1, i2);
        InvocationMatcher invocationMatcher1 = mock(InvocationMatcher.class);
        InvocationMatcher invocationMatcher2 = mock(InvocationMatcher.class);
        whenNew(InvocationMatcher.class).withArguments(i1).thenReturn(invocationMatcher1);
        whenNew(InvocationMatcher.class).withArguments(i2).thenReturn(invocationMatcher2);
        List<InvocationMatcher> expected = Arrays.asList(invocationMatcher1, invocationMatcher2);
        List<InvocationMatcher> actual = InvocationMatcher.createFrom(invocations);
        assertEquals(expected, actual);
    }

}
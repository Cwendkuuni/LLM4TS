import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    private Invocation invocation;
    private List<Matcher> matchers;
    private InvocationMatcher invocationMatcher;

    @Before
    public void setUp() {
        invocation = mock(Invocation.class);
        matchers = new ArrayList<>();
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void testConstructor() {
        assertNotNull(invocationMatcher);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructor_withEmptyMatchers() {
        List<Matcher> emptyMatchers = new ArrayList<>();
        when(invocation.argumentsToMatchers()).thenReturn(emptyMatchers);

        invocationMatcher = new InvocationMatcher(invocation);

        assertNotNull(invocationMatcher);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(emptyMatchers, invocationMatcher.getMatchers());
        verify(invocation).argumentsToMatchers();
    }

    @Test
    public void testGetMethod() {
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);

        Method result = invocationMatcher.getMethod();

        assertEquals(method, result);
        verify(invocation).getMethod();
    }

    @Test
    public void testToString() {
        PrintSettings printSettings = mock(PrintSettings.class);
        when(invocation.toString(matchers, printSettings)).thenReturn("invocationString");

        String result = invocationMatcher.toString();

        assertEquals("invocationString", result);
        verify(invocation).toString(matchers, new PrintSettings());
    }

    @Test
    public void testMatches() {
        Invocation actual = mock(Invocation.class);
        when(invocation.getMock()).thenReturn("mock");
        when(actual.getMock()).thenReturn("mock");
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getArguments()).thenReturn(new Object[]{});
        when(actual.getArguments()).thenReturn(new Object[]{});

        boolean result = invocationMatcher.matches(actual);

        assertTrue(result);
        verify(invocation).getMock();
        verify(invocation).getMethod();
        verify(invocation).getArguments();
        verify(actual).getMock();
        verify(actual).getArguments();
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Object[] actualArgs = new Object[]{};
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(argumentsComparator);
        when(argumentsComparator.argumentsMatch(invocationMatcher, actualArgs)).thenReturn(true);

        boolean result = invocationMatcher.safelyArgumentsMatch(actualArgs);

        assertTrue(result);
        verifyNew(ArgumentsComparator.class).withNoArguments();
        verify(argumentsComparator).argumentsMatch(invocationMatcher, actualArgs);
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation candidate = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);

        when(invocation.getMethod()).thenReturn(method1);
        when(candidate.getMethod()).thenReturn(method2);
        when(invocation.isVerified()).thenReturn(false);
        when(candidate.isVerified()).thenReturn(true);
        when(invocation.getMock()).thenReturn("mock");
        when(candidate.getMock()).thenReturn("mock");
        when(invocation.getArguments()).thenReturn(new Object[]{});
        when(candidate.getArguments()).thenReturn(new Object[]{});

        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        assertFalse(result);
        verify(invocation).getMethod();
        verify(candidate).getMethod();
        verify(invocation).isVerified();
        verify(candidate).isVerified();
        verify(invocation).getMock();
        verify(candidate).getMock();
        verify(invocation, never()).getArguments();
        verify(candidate, never()).getArguments();
    }

    @Test
    public void testHasSimilarMethod_withOverloadedButSameArgs() {
        Invocation candidate = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);

        when(invocation.getMethod()).thenReturn(method1);
        when(candidate.getMethod()).thenReturn(method2);
        when(invocation.isVerified()).thenReturn(false);
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn("mock");
        when(candidate.getMock()).thenReturn("mock");
        when(invocation.getArguments()).thenReturn(new Object[]{});
        when(candidate.getArguments()).thenReturn(new Object[]{});
        when(invocationMatcher.safelyArgumentsMatch(candidate.getArguments())).thenReturn(true);

        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        assertFalse(result);
        verify(invocation).getMethod();
        verify(candidate).getMethod();
        verify(invocation).isVerified();
        verify(candidate).isVerified();
        verify(invocation).getMock();
        verify(candidate).getMock();
        verify(invocation).getArguments();
        verify(candidate).getArguments();
        verify(invocationMatcher).safelyArgumentsMatch(candidate.getArguments());
    }

    @Test
    public void testHasSameMethod() {
        Invocation candidate = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);

        when(invocation.getMethod()).thenReturn(method1);
        when(candidate.getMethod()).thenReturn(method2);

        boolean result = invocationMatcher.hasSameMethod(candidate);

        assertFalse(result);
        verify(invocation).getMethod();
        verify(candidate).getMethod();
    }

    @Test
    public void testGetLocation() {
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);

        Location result = invocationMatcher.getLocation();

        assertEquals(location, result);
        verify(invocation).getLocation();
    }

    @Test
    public void testToStringWithPrintSettings() {
        PrintSettings printSettings = mock(PrintSettings.class);
        when(invocation.toString(matchers, printSettings)).thenReturn("invocationString");

        String result = invocationMatcher.toString(printSettings);

        assertEquals("invocationString", result);
        verify(invocation).toString(matchers, printSettings);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation i = mock(Invocation.class);
        Matcher matcher = mock(Matcher.class);
        matchers.add(matcher);
        CapturesArguments capturesArguments = mock(CapturesArguments.class);
        when(matcher instanceof CapturesArguments).thenReturn(true);
        when(matcher.captureFrom(any())).thenReturn(true);
        when(capturesArguments.captureFrom(any())).thenReturn(true);

        invocationMatcher.captureArgumentsFrom(i);

        verify(capturesArguments).captureFrom(any());
    }
}

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class InvocationMatcherTest {

    // Test constructor InvocationMatcher(Invocation, List<Matcher>)
    @Test
    public void testInvocationMatcherConstructor_withMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(mock(Matcher.class));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    // Test constructor InvocationMatcher(Invocation)
    @Test
    public void testInvocationMatcherConstructor_withoutMatchers() {
        Invocation invocation = mock(Invocation.class);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(Collections.<Matcher>emptyList(), invocationMatcher.getMatchers());
    }

    // Test method getMethod()
    @Test
    public void testGetMethod() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(invocation, invocationMatcher.getInvocation());
    }

    // Test method getInvocation()
    @Test
    public void testGetInvocation() {
        Invocation invocation = mock(Invocation.class);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(invocation, invocationMatcher.getInvocation());
    }

    // Test method getMatchers()
    @Test
    public void testGetMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(mock(Matcher.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    // Test method toString()
    @Test
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new LinkedList<>();
        Matcher matcher = mock(Matcher.class);
        matchers.add(matcher);
        PrintSettings printSettings = new PrintSettings();

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        invocationMatcher.toString();

        verify(invocation).toString(matchers, printSettings);
    }

    // Test method matches()
    @Test
    public void testMatches_true() {
        Invocation invocation = mock(Invocation.class);
        Invocation actual = mock(Invocation.class);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(actual.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(actual.getMethod()).thenReturn(mock(Method.class));
        when(new ArgumentsComparator().argumentsMatch(invocationMatcher, actual)).thenReturn(true);

        assertEquals(true, invocationMatcher.matches(actual));
    }

    @Test
    public void testMatches_false() {
        Invocation invocation = mock(Invocation.class);
        Invocation actual = mock(Invocation.class);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(actual.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(actual.getMethod()).thenReturn(mock(Method.class));
        when(new ArgumentsComparator().argumentsMatch(invocationMatcher, actual)).thenReturn(false);

        assertEquals(false, invocationMatcher.matches(actual));
    }

    // Test method hasSimilarMethod()
    @Test
    public void testHasSimilarMethod_true() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(candidate.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(hasSameMethod(candidate)).thenReturn(true);
        when(safelyArgumentsMatch(candidate.getArguments())).thenReturn(false);

        assertEquals(true, invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethod_false() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        when(candidate.isVerified()).thenReturn(true);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(candidate.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));

        assertEquals(false, invocationMatcher.hasSimilarMethod(candidate));
    }

    // Test method hasSameMethod()
    @Test
    public void testHasSameMethod_true() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(candidate.getMethod()).thenReturn(method);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(true, invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testHasSameMethod_false() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(candidate.getMethod()).thenReturn(method2);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(false, invocationMatcher.hasSameMethod(candidate));
    }

    // Test method getLocation()
    @Test
    public void testGetLocation() {
        Invocation invocation = mock(Invocation.class);
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(location, invocationMatcher.getLocation());
    }

    // Test method toString(PrintSettings)
    @Test
    public void testToString_PrintSettings() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new LinkedList<>();
        Matcher matcher = mock(Matcher.class);
        matchers.add(matcher);
        PrintSettings printSettings = mock(PrintSettings.class);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        invocationMatcher.toString(printSettings);

        verify(invocation).toString(matchers, printSettings);
    }

    // Test method captureArgumentsFrom(Invocation)
    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        Invocation i = mock(Invocation.class);
        Matcher matcher = mock(Matcher.class);
        CapturesArguments capturesArguments = mock(CapturesArguments.class);
        when(matcher instanceof CapturesArguments).thenReturn(true);
        when(i.getArguments()).thenReturn(new Object[]{});
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(matcher);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        invocationMatcher.captureArgumentsFrom(i);

        verify(capturesArguments).captureFrom(i.getArguments()[0]);
    }

    // Test method createFrom(List<Invocation>)
    @Test
    public void testCreateFrom() {
        Invocation invocation = mock(Invocation.class);
        List<Invocation> invocations = new LinkedList<>();
        invocations.add(invocation);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        List<InvocationMatcher> expected = new LinkedList<>();
        expected.add(invocationMatcher);

        assertEquals(expected, InvocationMatcher.createFrom(invocations));
    }
}

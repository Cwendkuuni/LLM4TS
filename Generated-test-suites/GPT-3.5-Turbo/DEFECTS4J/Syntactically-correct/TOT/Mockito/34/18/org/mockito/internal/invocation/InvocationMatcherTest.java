import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;

@SuppressWarnings("unchecked")
public class InvocationMatcherTest {

    private Invocation invocation;
    private List<Matcher> matchers;
    private InvocationMatcher invocationMatcher;

    @Before
    public void setUp() throws Exception {
        invocation = mock(Invocation.class);
        matchers = Arrays.asList(mock(Matcher.class), mock(Matcher.class));
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void testConstructorWithInvocationAndMatchers() {
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithInvocation() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(Collections.emptyList(), invocationMatcher.getMatchers());
    }

    @Test
    public void testGetMethod() {
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testToString() {
        PrintableInvocation printableInvocation = mock(PrintableInvocation.class);
        PrintSettings printSettings = mock(PrintSettings.class);
        when(invocation.toString(matchers, printSettings)).thenReturn("invocationAsString");
        assertEquals("invocationAsString", invocationMatcher.toString());
    }

    @Test
    public void testMatchesWithEqualInvoke() {
        Invocation actual = mock(Invocation.class);
        when(invocation.getMock()).thenReturn("mock");
        when(actual.getMock()).thenReturn("mock");
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(actual.getMethod()).thenReturn(mock(Method.class));
        when(invocation.argumentsMatch(actual)).thenReturn(true);
        assertTrue(invocationMatcher.matches(actual));
    }

    @Test
    public void testMatchesWithNonEqualInvoke() {
        Invocation actual = mock(Invocation.class);
        when(invocation.getMock()).thenReturn("mock1");
        when(actual.getMock()).thenReturn("mock2");
        assertFalse(invocationMatcher.matches(actual));
    }

    @Test
    public void testHasSimilarMethodWithEqualMethod() {
        Invocation candidate = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(candidate.getMethod()).thenReturn(method);
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn("mock");
        when(candidate.getMock()).thenReturn("mock");
        when(invocation.argumentsMatch(candidate.getArguments())).thenReturn(true);
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethodWithOverloadedButSameArgs() {
        Invocation candidate = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(candidate.getMethod()).thenReturn(method2);
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn("mock");
        when(candidate.getMock()).thenReturn("mock");
        when(invocation.argumentsMatch(candidate.getArguments())).thenReturn(true);
        when(candidate.getArguments()).thenReturn(new Object[] { "arg1", "arg2" });
        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethodWithDifferentMethodName() {
        Invocation candidate = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(candidate.getMethod()).thenReturn(method2);
        when(method1.getName()).thenReturn("method1");
        when(method2.getName()).thenReturn("method2");
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn("mock");
        when(candidate.getMock()).thenReturn("mock");
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethodWithSameMethodNameButVerified() {
        Invocation candidate = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(candidate.getMethod()).thenReturn(method);
        when(candidate.isVerified()).thenReturn(true);
        when(invocation.getMock()).thenReturn("mock");
        when(candidate.getMock()).thenReturn("mock");
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethodWithDifferentMock() {
        Invocation candidate = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(candidate.getMethod()).thenReturn(method);
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn("mock1");
        when(candidate.getMock()).thenReturn("mock2");
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSameMethodWithEqualMethod() {
        Invocation candidate = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(candidate.getMethod()).thenReturn(method);
        assertTrue(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testHasSameMethodWithNonEqualMethod() {
        Invocation candidate = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(candidate.getMethod()).thenReturn(method2);
        assertFalse(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testGetLocation() {
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);
        assertEquals(location, invocationMatcher.getLocation());
    }

    @Test
    public void testToStringWithPrintSettings() {
        PrintableInvocation printableInvocation = mock(PrintableInvocation.class);
        PrintSettings printSettings = mock(PrintSettings.class);
        when(invocation.toString(matchers, printSettings)).thenReturn("invocationAsString");
        assertEquals("invocationAsString", invocationMatcher.toString(printSettings));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation i = mock(Invocation.class);
        Matcher matcher1 = mock(Matcher.class);
        Matcher matcher2 = mock(Matcher.class);
        when(matcher1 instanceof CapturesArguments).thenReturn(true);
        when(matcher2 instanceof CapturesArguments).thenReturn(false);
        when(matchers.toArray()).thenReturn(new Object[] { matcher1, matcher2 });
        when(i.getArguments()).thenReturn(new Object[] { "arg1", "arg2" });
        invocationMatcher.captureArgumentsFrom(i);
        // add assertion for captured arguments if needed
    }
}
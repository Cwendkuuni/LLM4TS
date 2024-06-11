import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;

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
    public void testConstructorWithInvocationAndMatchers() {
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
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testToStringWithMatchers() {
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(mock(Matcher.class));
        matchers.add(mock(Matcher.class));
        when(invocation.toString(matchers, new PrintSettings())).thenReturn("Invocation With Matchers");
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertEquals("Invocation With Matchers", invocationMatcher.toString());
    }

    @Test
    public void testToStringWithoutMatchers() {
        when(invocation.toString(matchers, new PrintSettings())).thenReturn("Invocation Without Matchers");
        assertEquals("Invocation Without Matchers", invocationMatcher.toString());
    }

    @Test
    public void testMatchesWithSameMockAndMethod() {
        Invocation actual = mock(Invocation.class);
        when(invocation.getMock()).thenReturn("Mock");
        when(actual.getMock()).thenReturn("Mock");
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(actual.getMethod()).thenReturn(mock(Method.class));
        assertTrue(invocationMatcher.matches(actual));
    }

    @Test
    public void testMatchesWithDifferentMock() {
        Invocation actual = mock(Invocation.class);
        when(invocation.getMock()).thenReturn("Mock");
        when(actual.getMock()).thenReturn("AnotherMock");
        assertFalse(invocationMatcher.matches(actual));
    }

    @Test
    public void testMatchesWithDifferentMethod() {
        Invocation actual = mock(Invocation.class);
        when(invocation.getMock()).thenReturn("Mock");
        when(actual.getMock()).thenReturn("Mock");
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(actual.getMethod()).thenReturn(mock(Method.class));
        assertFalse(invocationMatcher.matches(actual));
    }

    @Test
    public void testHasSimilarMethodWithSameMethodNameAndUnverified() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(invocation.isVerified()).thenReturn(false);
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn("Mock");
        when(candidate.getMock()).thenReturn("Mock");
        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethodWithDifferentMethodName() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(invocation.isVerified()).thenReturn(false);
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn("Mock");
        when(candidate.getMock()).thenReturn("Mock");
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethodWithVerified() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(invocation.isVerified()).thenReturn(false);
        when(candidate.isVerified()).thenReturn(true);
        when(invocation.getMock()).thenReturn("Mock");
        when(candidate.getMock()).thenReturn("Mock");
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethodWithDifferentMock() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(invocation.isVerified()).thenReturn(false);
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn("Mock");
        when(candidate.getMock()).thenReturn("AnotherMock");
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethodWithSameMethodButOverloadedAndSameArgs() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(invocation.isVerified()).thenReturn(false);
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn("Mock");
        when(candidate.getMock()).thenReturn("Mock");
        when(invocation.getArguments()).thenReturn(new Object[]{1, 2, 3});
        when(invocationMatcher.safelyArgumentsMatch(new Object[]{1, 2, 3})).thenReturn(true);
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethodWithSameMethodButOverloadedAndDifferentArgs() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(invocation.isVerified()).thenReturn(false);
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn("Mock");
        when(candidate.getMock()).thenReturn("Mock");
        when(invocation.getArguments()).thenReturn(new Object[]{1, 2, 3});
        when(invocationMatcher.safelyArgumentsMatch(new Object[]{4, 5, 6})).thenReturn(false);
        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSameMethodWithEqualMethods() {
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(invocationMatcher.hasSameMethod(any(Invocation.class))).thenCallRealMethod();
        assertTrue(invocationMatcher.hasSameMethod(invocation));
    }

    @Test
    public void testHasSameMethodWithDifferentMethods() {
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(invocationMatcher.hasSameMethod(any(Invocation.class))).thenCallRealMethod();
        assertFalse(invocationMatcher.hasSameMethod(mock(Invocation.class)));
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
        when(invocation.toString(matchers, printSettings)).thenReturn("Invocation With PrintSettings");
        assertEquals("Invocation With PrintSettings", invocationMatcher.toString(printSettings));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation i = mock(Invocation.class);
        Matcher matcher1 = mock(Matcher.class);
        Matcher matcher2 = mock(Matcher.class);
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(matcher1);
        matchers.add(matcher2);
        invocationMatcher = new InvocationMatcher(i, matchers);
        Object[] arguments = {1, 2, 3};
        when(i.getArguments()).thenReturn(arguments);
        when(matcher1 instanceof CapturesArguments).thenReturn(true);
        when(matcher2 instanceof CapturesArguments).thenReturn(false);
        invocationMatcher.captureArgumentsFrom(i);
        verify(((CapturesArguments) matcher1)).captureFrom(arguments[0]);
        verify(((CapturesArguments) matcher2), never()).captureFrom(any());
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new LinkedList<>();
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        invocations.add(invocation1);
        invocations.add(invocation2);
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        assertEquals(invocation1, invocationMatchers.get(0).getInvocation());
        assertEquals(invocation2, invocationMatchers.get(1).getInvocation());
    }

}
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;

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
    public void testConstructorWithMatchers() {
        List<Matcher> expectedMatchers = Arrays.asList(mock(Matcher.class), mock(Matcher.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, expectedMatchers);
        
        List<Matcher> actualMatchers = invocationMatcher.getMatchers();
        
        assertSame(expectedMatchers, actualMatchers);
    }

    @Test
    public void testConstructorWithoutMatchers() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        List<Matcher> expectedMatchers = invocation.argumentsToMatchers();
        
        List<Matcher> actualMatchers = invocationMatcher.getMatchers();
        
        assertSame(expectedMatchers, actualMatchers);
    }

    @Test
    public void testGetMethod() {
        Method expectedMethod = mock(Method.class);
        when(invocation.getMethod()).thenReturn(expectedMethod);
        
        Method actualMethod = invocationMatcher.getMethod();
        
        assertSame(expectedMethod, actualMethod);
    }

    @Test
    public void testGetInvocation() {
        Invocation actualInvocation = invocationMatcher.getInvocation();
        
        assertSame(invocation, actualInvocation);
    }

    @Test
    public void testGetMatchers() {
        List<Matcher> actualMatchers = invocationMatcher.getMatchers();
        
        assertSame(matchers, actualMatchers);
    }

    @Test
    public void testToString() {
        String expectedString = "testString";
        when(invocation.toString(matchers, new PrintSettings())).thenReturn(expectedString);
        
        String actualString = invocationMatcher.toString();
        
        assertSame(expectedString, actualString);
    }

    @Test
    public void testMatches_WithMatchingInvocation() {
        Invocation actualInvocation = mock(Invocation.class);
        when(invocation.getMock()).thenReturn("mock");
        when(actualInvocation.getMock()).thenReturn("mock");
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.argumentsMatch(actualInvocation)).thenReturn(true);
        
        boolean result = invocationMatcher.matches(actualInvocation);
        
        assertTrue(result);
    }

    @Test
    public void testMatches_WithDifferentMockInvocation() {
        Invocation actualInvocation = mock(Invocation.class);
        when(invocation.getMock()).thenReturn("mock1");
        when(actualInvocation.getMock()).thenReturn("mock2");
        
        boolean result = invocationMatcher.matches(actualInvocation);
        
        assertFalse(result);
    }

    @Test
    public void testMatches_WithDifferentMethodInvocation() {
        Invocation actualInvocation = mock(Invocation.class);
        when(invocation.getMock()).thenReturn("mock");
        when(actualInvocation.getMock()).thenReturn("mock");
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(actualInvocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.argumentsMatch(actualInvocation)).thenReturn(true);
        
        boolean result = invocationMatcher.matches(actualInvocation);
        
        assertTrue(result);
    }

    @Test
    public void testHasSimilarMethod_WithUnverifiedInvocation() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMethod().getName()).thenReturn("testMethod");
        when(candidate.getMethod().getName()).thenReturn("testMethod");
        when(invocation.getMock()).thenReturn("mock");
        when(candidate.getMock()).thenReturn("mock");
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.argumentsMatch(candidate.getArguments())).thenReturn(true);
        
        boolean result = invocationMatcher.hasSimilarMethod(candidate);
        
        assertTrue(result);
    }

    @Test
    public void testHasSimilarMethod_WithVerifiedInvocation() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(candidate.isVerified()).thenReturn(true);
        
        boolean result = invocationMatcher.hasSimilarMethod(candidate);
        
        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_WithDifferentMocks() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn("mock1");
        when(candidate.getMock()).thenReturn("mock2");
        
        boolean result = invocationMatcher.hasSimilarMethod(candidate);
        
        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_WithDifferentMethods() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(candidate.isVerified()).thenReturn(false);
        
        boolean result = invocationMatcher.hasSimilarMethod(candidate);
        
        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_WithOverloadedButSameArgs() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMethod().getName()).thenReturn("testMethod");
        when(candidate.getMethod().getName()).thenReturn("testMethod");
        when(invocation.getMock()).thenReturn("mock1");
        when(candidate.getMock()).thenReturn("mock1");
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.argumentsMatch(candidate.getArguments())).thenReturn(true);
        
        boolean result = invocationMatcher.hasSimilarMethod(candidate);
        
        assertFalse(result);
    }

    @Test
    public void testHasSameMethod_WithSameMethodInvocation() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        
        boolean result = invocationMatcher.hasSameMethod(candidate);
        
        assertTrue(result);
    }

    @Test
    public void testHasSameMethod_WithDifferentMethodInvocation() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        
        boolean result = invocationMatcher.hasSameMethod(candidate);
        
        assertFalse(result);
    }

    @Test
    public void testGetLocation() {
        Location expectedLocation = mock(Location.class);
        when(invocation.getLocation()).thenReturn(expectedLocation);
        
        Location actualLocation = invocationMatcher.getLocation();
        
        assertSame(expectedLocation, actualLocation);
    }

    @Test
    public void testToStringWithPrintSettings() {
        String expectedString = "testString";
        when(invocation.toString(matchers, new PrintSettings())).thenReturn(expectedString);
        PrintSettings printSettings = mock(PrintSettings.class);
        
        String actualString = invocationMatcher.toString(printSettings);
        
        assertSame(expectedString, actualString);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation i = mock(Invocation.class);
        Object[] args = { "arg1", "arg2", "arg3" };
        when(i.getArguments()).thenReturn(args);
        CapturesArguments capturesArguments1 = mock(CapturesArguments.class);
        CapturesArguments capturesArguments2 = mock(CapturesArguments.class);
        matchers.add(capturesArguments1);
        matchers.add(capturesArguments2);
        
        invocationMatcher.captureArgumentsFrom(i);
        
        verify(capturesArguments1).captureFrom("arg1");
        verify(capturesArguments2).captureFrom("arg2");
    }

}
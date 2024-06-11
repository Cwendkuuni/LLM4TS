import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.Invocation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;

import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;
    private Invocation invocation;

    @Before
    public void setup() {
        invocation = mock(Invocation.class);
        invocationMatcher = new InvocationMatcher(invocation);
    }

    @Test
    public void testGetMethod_shouldReturnInvocationMethod() {
        Method expectedMethod = invocation.getMethod();

        Method actualMethod = invocationMatcher.getMethod();

        assertEquals(expectedMethod, actualMethod);
    }

    @Test
    public void testGetInvocation_shouldReturnInvocation() {
        Invocation expectedInvocation = invocation;

        Invocation actualInvocation = invocationMatcher.getInvocation();

        assertEquals(expectedInvocation, actualInvocation);
    }

    @Test
    public void testGetMatchers_shouldReturnEmptyListWhenNoMatchersProvided() {
        List<Matcher> expectedMatchers = new ArrayList<>();

        List<Matcher> actualMatchers = invocationMatcher.getMatchers();

        assertEquals(expectedMatchers, actualMatchers);
    }

    @Test
    public void testToString_shouldPrintMatchersAndInvocation() {
        String expectedString = invocationMatcher.toString();

        String actualString = invocationMatcher.toString();

        assertEquals(expectedString, actualString);
    }

    @Test
    public void testMatches_shouldReturnTrueForMatchingInvocation() {
        Invocation actualInvocation = mock(Invocation.class);
        when(actualInvocation.getMock()).thenReturn(invocation.getMock());
        when(actualInvocation.getArguments()).thenReturn(invocation.getArguments());

        boolean matches = invocationMatcher.matches(actualInvocation);

        assertTrue(matches);
    }

    @Test
    public void testMatches_shouldReturnFalseForDifferentMock() {
        Invocation actualInvocation = mock(Invocation.class);
        when(actualInvocation.getMock()).thenReturn(mock(Object.class));

        boolean matches = invocationMatcher.matches(actualInvocation);

        assertFalse(matches);
    }

    @Test
    public void testMatches_shouldReturnFalseForDifferentMethod() {
        Invocation actualInvocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(actualInvocation.getMethod()).thenReturn(method);

        boolean matches = invocationMatcher.matches(actualInvocation);

        assertFalse(matches);
    }

    @Test
    public void testHasSimilarMethod_shouldReturnTrueForSameMethodName() {
        Invocation candidate = mock(Invocation.class);
        Method method = mock(Method.class);
        when(candidate.getMethod()).thenReturn(method);
        when(invocation.getMethod().getName()).thenReturn("methodName");
        when(candidate.getMethod().getName()).thenReturn("methodName");
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn(invocation.getMock());
        when(invocation.hasSameMethod(candidate)).thenReturn(false);
        when(invocation.safelyArgumentsMatch(candidate.getArguments())).thenReturn(true);

        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidate);

        assertTrue(hasSimilarMethod);
    }

    @Test
    public void testHasSimilarMethod_shouldReturnFalseForDifferentMethodName() {
        Invocation candidate = mock(Invocation.class);
        Method method = mock(Method.class);
        when(candidate.getMethod()).thenReturn(method);
        when(invocation.getMethod().getName()).thenReturn("methodName");
        when(candidate.getMethod().getName()).thenReturn("differentMethodName");

        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidate);

        assertFalse(hasSimilarMethod);
    }

    @Test
    public void testHasSimilarMethod_shouldReturnFalseWhenVerified() {
        Invocation candidate = mock(Invocation.class);
        Method method = mock(Method.class);
        when(candidate.getMethod()).thenReturn(method);
        when(invocation.getMethod().getName()).thenReturn("methodName");
        when(candidate.getMethod().getName()).thenReturn("methodName");
        when(candidate.isVerified()).thenReturn(true);

        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidate);

        assertFalse(hasSimilarMethod);
    }

    @Test
    public void testHasSimilarMethod_shouldReturnFalseForDifferentMock() {
        Invocation candidate = mock(Invocation.class);
        Method method = mock(Method.class);
        when(candidate.getMethod()).thenReturn(method);
        when(invocation.getMethod().getName()).thenReturn("methodName");
        when(candidate.getMethod().getName()).thenReturn("methodName");
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMock()).thenReturn(mock(Object.class));

        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidate);

        assertFalse(hasSimilarMethod);
    }

    @Test
    public void testHasSimilarMethod_shouldReturnFalseForOverloadedButSameArgs() {
        Invocation candidate = mock(Invocation.class);
        Method method = mock(Method.class);
        when(candidate.getMethod()).thenReturn(method);
        when(invocation.getMethod().getName()).thenReturn("methodName");
        when(candidate.getMethod().getName()).thenReturn("methodName");
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn(invocation.getMock());
        when(invocation.hasSameMethod(candidate)).thenReturn(true);
        when(invocation.safelyArgumentsMatch(candidate.getArguments())).thenReturn(true);

        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidate);

        assertFalse(hasSimilarMethod);
    }

    @Test
    public void testHasSameMethod_shouldReturnTrueForSameMethod() {
        Invocation candidate = mock(Invocation.class);
        Method method = mock(Method.class);
        when(candidate.getMethod()).thenReturn(method);
        when(invocation.getMethod().getName()).thenReturn("methodName");
        when(candidate.getMethod().getName()).thenReturn("methodName");
        when(invocation.getMethod()).thenReturn(method);
        when(candidate.getMethod()).thenReturn(method);

        boolean hasSameMethod = invocationMatcher.hasSameMethod(candidate);

        assertTrue(hasSameMethod);
    }

    @Test
    public void testHasSameMethod_shouldReturnFalseForDifferentMethod() {
        Invocation candidate = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(candidate.getMethod()).thenReturn(method1);
        when(invocation.getMethod()).thenReturn(method2);

        boolean hasSameMethod = invocationMatcher.hasSameMethod(candidate);

        assertFalse(hasSameMethod);
    }

    @Test
    public void testGetLocation_shouldReturnInvocationLocation() {
        Location expectedLocation = invocation.getLocation();

        Location actualLocation = invocationMatcher.getLocation();

        assertEquals(expectedLocation, actualLocation);
    }

    @Test
    public void testCaptureArgumentsFrom_shouldInvokeCaptureFromOnMatchers() {
        Invocation invocation = mock(Invocation.class);
        Object[] arguments = new Object[] {1, "two"};
        when(invocation.getArguments()).thenReturn(arguments);
        when(invocation.getRawArguments()).thenReturn(arguments);

        CapturesArguments matcher1 = mock(CapturesArguments.class);
        MatcherDecorator matcher2 = mock(MatcherDecorator.class);
        MatcherDecorator matcher3 = mock(MatcherDecorator.class);
        List<Matcher> matchers = Arrays.asList(matcher1, matcher2, matcher3);
        invocationMatcher = new InvocationMatcher(invocation, matchers);

        invocationMatcher.captureArgumentsFrom(invocation);

        verify(matcher1).captureFrom(eq(1));
        verify(matcher1, never()).captureFrom(eq("two"));
        verify(matcher2).captureFrom(eq(1));
        verify(matcher2, never()).captureFrom(eq("two"));
        verify(matcher3).captureFrom(eq("two"));
        verify(matcher3, never()).captureFrom(eq(1));
    }

    @Test
    public void testCreateFrom_shouldReturnInvocationMatchers() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<Invocation> invocations = Arrays.asList(invocation1, invocation2);

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        assertEquals(2, invocationMatchers.size());
        assertEquals(invocation1, invocationMatchers.get(0).getInvocation());
        assertEquals(invocation2, invocationMatchers.get(1).getInvocation());
    }
}
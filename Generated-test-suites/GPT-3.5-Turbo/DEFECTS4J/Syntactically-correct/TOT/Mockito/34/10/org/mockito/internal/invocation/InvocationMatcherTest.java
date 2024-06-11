import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class InvocationMatcherTest {

    Invocation invocation;
    List<Matcher> matchers;
    InvocationMatcher invocationMatcher;

    @Before
    public void setUp() {
        invocation = mock(Invocation.class);
        matchers = mock(List.class);
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void testGetMethod() {
        Method expectedMethod = mock(Method.class);
        when(invocation.getMethod()).thenReturn(expectedMethod);

        Method actualMethod = invocationMatcher.getMethod();

        assertEquals(expectedMethod, actualMethod);
    }

    @Test
    public void testGetInvocation() {
        Invocation actualInvocation = invocationMatcher.getInvocation();

        assertEquals(invocation, actualInvocation);
    }

    @Test
    public void testGetMatchers() {
        List<Matcher> actualMatchers = invocationMatcher.getMatchers();

        assertEquals(matchers, actualMatchers);
    }

    @Test
    public void testToString() {
        String expectedToString = "[Mockito] InvocationMatcher.toString called";
        when(invocation.toString(matchers, new PrintSettings())).thenReturn(expectedToString);

        String actualToString = invocationMatcher.toString();

        assertEquals(expectedToString, actualToString);
    }

    @Test
    public void testMatches_WhenArgumentsMatch() {
        Invocation actualInvocation = mock(Invocation.class);

        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(actualInvocation.getMock()).thenReturn(invocation.getMock());
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(actualInvocation.getMethod()).thenReturn(invocation.getMethod());

        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(argumentsComparator);
        when(argumentsComparator.argumentsMatch(invocationMatcher, actualInvocation)).thenReturn(true);

        boolean matches = invocationMatcher.matches(actualInvocation);

        assertTrue(matches);
    }

    @Test
    public void testMatches_WhenArgumentsDoNotMatch() {
        Invocation actualInvocation = mock(Invocation.class);

        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(actualInvocation.getMock()).thenReturn(invocation.getMock());
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(actualInvocation.getMethod()).thenReturn(invocation.getMethod());

        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(argumentsComparator);
        when(argumentsComparator.argumentsMatch(invocationMatcher, actualInvocation)).thenReturn(false);

        boolean matches = invocationMatcher.matches(actualInvocation);

        assertFalse(matches);
    }

    @Test
    public void testHasSimilarMethod_WhenMethodNameEqualsAndUnverifiedAndMockIsTheSame() {
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn(invocation.getMock());

        when(invocation.getMethod()).thenReturn(mock(Method.class));

        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidate);

        assertTrue(hasSimilarMethod);
    }

    @Test
    public void testHasSimilarMethod_WhenMethodNameDoesNotEqual() {
        String wantedMethodName = "wantedMethod";
        String currentMethodName = "currentMethod";

        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getMethod().getName()).thenReturn(wantedMethodName);

        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod().getName()).thenReturn(currentMethodName);

        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidate);

        assertFalse(hasSimilarMethod);
    }

    @Test
    public void testHasSimilarMethod_WhenIsVerified() {
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(candidate.isVerified()).thenReturn(true);
        when(candidate.getMock()).thenReturn(invocation.getMock());

        when(invocation.getMethod()).thenReturn(mock(Method.class));

        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidate);

        assertFalse(hasSimilarMethod);
    }

    @Test
    public void testHasSimilarMethod_WhenMockIsNotTheSame() {
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn(mock(Object.class));

        when(invocation.getMethod()).thenReturn(mock(Method.class));

        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidate);

        assertFalse(hasSimilarMethod);
    }

    @Test
    public void testHasSimilarMethod_WhenMethodEqualsAndArgumentsMatch() {
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(invocation.getMethod());
        when(candidate.getArguments()).thenReturn(invocation.getArguments());

        when(invocation.getMethod()).thenReturn(mock(Method.class));

        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(argumentsComparator);
        when(argumentsComparator.argumentsMatch(invocationMatcher, candidate.getArguments())).thenReturn(true);

        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidate);

        assertFalse(hasSimilarMethod);
    }

    @Test
    public void testHasSimilarMethod_WhenMethodEqualsButArgumentsDoNotMatch() {
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(invocation.getMethod());
        when(candidate.getArguments()).thenReturn(invocation.getArguments());

        when(invocation.getMethod()).thenReturn(mock(Method.class));

        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(argumentsComparator);
        when(argumentsComparator.argumentsMatch(invocationMatcher, candidate.getArguments())).thenReturn(false);

        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidate);

        assertFalse(hasSimilarMethod);
    }

    @Test
    public void testHasSameMethod_WhenMethodEquals() {
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(invocation.getMethod());

        when(invocation.getMethod()).thenReturn(mock(Method.class));

        boolean hasSameMethod = invocationMatcher.hasSameMethod(candidate);

        assertTrue(hasSameMethod);
    }

    @Test
    public void testHasSameMethod_WhenMethodDoesNotEqual() {
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(mock(Method.class));

        when(invocation.getMethod()).thenReturn(mock(Method.class));

        boolean hasSameMethod = invocationMatcher.hasSameMethod(candidate);

        assertFalse(hasSameMethod);
    }

    @Test
    public void testGetLocation() {
        Location expectedLocation = mock(Location.class);
        when(invocation.getLocation()).thenReturn(expectedLocation);

        Location actualLocation = invocationMatcher.getLocation();

        assertEquals(expectedLocation, actualLocation);
    }

    @Test
    public void testToStringWithPrintSettings() {
        PrintSettings printSettings = mock(PrintSettings.class);
        String expectedToString = "[Mockito] InvocationMatcher.toString called";
        when(invocation.toString(matchers, printSettings)).thenReturn(expectedToString);

        String actualToString = invocationMatcher.toString(printSettings);

        assertEquals(expectedToString, actualToString);
    }

    @Test
    public void testCaptureArgumentsFrom_WhenMatcherIsCapturesArguments() {
        Matcher matcher = mock(CapturesArguments.class);

        when(matchers.get(0)).thenReturn(matcher);
        when(invocation.getArguments()).thenReturn(new Object[]{new Object()});

        Invocation i = mock(Invocation.class);
        when(i.getArguments()).thenReturn(invocation.getArguments());

        invocationMatcher.captureArgumentsFrom(i);

        verify((CapturesArguments)matcher).captureFrom(invocation.getArguments()[0]);
    }

    @Test
    public void testCaptureArgumentsFrom_WhenMatcherIsNotCapturesArguments() {
        Matcher matcher = mock(Matcher.class);

        when(matchers.get(0)).thenReturn(matcher);

        Invocation i = mock(Invocation.class);
        when(i.getArguments()).thenReturn(new Object[]{new Object()});

        invocationMatcher.captureArgumentsFrom(i);

        verifyNoInteractions(matcher);
    }

}
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;

public class InvocationMatcherTest {

    private Invocation invocation;
    private List<Matcher> matchers;
    private InvocationMatcher invocationMatcher;

    @Before
    public void setUp() throws Exception {
        invocation = mock(Invocation.class);
        matchers = new ArrayList<>();
        invocationMatcher = new InvocationMatcher(invocation, matchers);
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
        String expectedString = "testToString";
        when(invocation.toString(matchers, new PrintSettings())).thenReturn(expectedString);

        String actualString = invocationMatcher.toString();

        assertEquals(expectedString, actualString);
    }

    @Test
    public void testMatches() {
        Invocation actualInvocation = mock(Invocation.class);
        when(invocation.getMock()).thenReturn("mock");
        when(actualInvocation.getMock()).thenReturn("mock");
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(actualInvocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.argumentsMatch(actualInvocation)).thenReturn(true);

        boolean matches = invocationMatcher.matches(actualInvocation);

        assertTrue(matches);
    }

    @Test
    public void testSafelyArgumentsMatch_CorrectArgumentsMatch() {
        Object[] actualArgs = {1, 2, 3};
        when(invocation.argumentsMatch(actualArgs)).thenReturn(true);

        boolean safelyArgumentsMatch = invocationMatcher.safelyArgumentsMatch(actualArgs);

        assertTrue(safelyArgumentsMatch);
    }

    @Test
    public void testSafelyArgumentsMatch_IncorrectArgumentsMatch() {
        Object[] actualArgs = {1, 2, 3};
        when(invocation.argumentsMatch(actualArgs)).thenReturn(false);

        boolean safelyArgumentsMatch = invocationMatcher.safelyArgumentsMatch(actualArgs);

        assertFalse(safelyArgumentsMatch);
    }

    @Test
    public void testHasSimilarMethod_MethodNameDifferent() {
        Invocation candidate = mock(Invocation.class);
        Method invocationMethod = mock(Method.class);
        Method candidateMethod = mock(Method.class);
        when(invocation.getMethod()).thenReturn(invocationMethod);
        when(candidate.getMethod()).thenReturn(candidateMethod);
        when(invocationMethod.getName()).thenReturn("method1");
        when(candidateMethod.getName()).thenReturn("method2");

        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidate);

        assertFalse(hasSimilarMethod);
    }

    @Test
    public void testHasSimilarMethod_VerifiedInvocation() {
        Invocation candidate = mock(Invocation.class);
        when(candidate.isVerified()).thenReturn(true);

        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidate);

        assertFalse(hasSimilarMethod);
    }

    @Test
    public void testHasSimilarMethod_MockNotTheSame() {
        Invocation candidate = mock(Invocation.class);
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn("mock");
        when(candidate.getMock()).thenReturn("differentMock");

        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidate);

        assertFalse(hasSimilarMethod);
    }

    @Test
    public void testHasSimilarMethod_SameMethodAndArguments() {
        Invocation candidate = mock(Invocation.class);
        Method invocationMethod = mock(Method.class);
        Method candidateMethod = mock(Method.class);
        when(invocation.getMethod()).thenReturn(invocationMethod);
        when(candidate.getMethod()).thenReturn(candidateMethod);
        when(invocationMethod.getName()).thenReturn("method");
        when(candidateMethod.getName()).thenReturn("method");
        when(invocationMethod.equals(candidateMethod)).thenReturn(true);
        when(invocation.argumentsMatch(candidate.getArguments())).thenReturn(true);

        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidate);

        assertFalse(hasSimilarMethod);
    }

    @Test
    public void testHasSimilarMethod_SameMethodButDifferentArguments() {
        Invocation candidate = mock(Invocation.class);
        Method invocationMethod = mock(Method.class);
        Method candidateMethod = mock(Method.class);
        when(invocation.getMethod()).thenReturn(invocationMethod);
        when(candidate.getMethod()).thenReturn(candidateMethod);
        when(invocationMethod.getName()).thenReturn("method");
        when(candidateMethod.getName()).thenReturn("method");
        when(invocationMethod.equals(candidateMethod)).thenReturn(true);
        when(invocation.argumentsMatch(candidate.getArguments())).thenReturn(false);

        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidate);

        assertTrue(hasSimilarMethod);
    }

    @Test
    public void testHasSimilarMethod_DifferentMethod() {
        Invocation candidate = mock(Invocation.class);
        Method invocationMethod = mock(Method.class);
        Method candidateMethod = mock(Method.class);
        when(invocation.getMethod()).thenReturn(invocationMethod);
        when(candidate.getMethod()).thenReturn(candidateMethod);
        when(invocationMethod.getName()).thenReturn("method1");
        when(candidateMethod.getName()).thenReturn("method2");

        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidate);

        assertTrue(hasSimilarMethod);
    }

    @Test
    public void testHasSameMethod_SameMethod() {
        Invocation candidate = mock(Invocation.class);
        Method invocationMethod = mock(Method.class);
        Method candidateMethod = mock(Method.class);
        when(invocation.getMethod()).thenReturn(invocationMethod);
        when(candidate.getMethod()).thenReturn(candidateMethod);
        when(invocationMethod.equals(candidateMethod)).thenReturn(true);

        boolean hasSameMethod = invocationMatcher.hasSameMethod(candidate);

        assertTrue(hasSameMethod);
    }

    @Test
    public void testHasSameMethod_DifferentMethod() {
        Invocation candidate = mock(Invocation.class);
        Method invocationMethod = mock(Method.class);
        Method candidateMethod = mock(Method.class);
        when(invocation.getMethod()).thenReturn(invocationMethod);
        when(candidate.getMethod()).thenReturn(candidateMethod);
        when(invocationMethod.equals(candidateMethod)).thenReturn(false);

        boolean hasSameMethod = invocationMatcher.hasSameMethod(candidate);

        assertFalse(hasSameMethod);
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
        String expectedString = "testToStringWithPrintSettings";
        PrintSettings printSettings = mock(PrintSettings.class);
        when(invocation.toString(matchers, printSettings)).thenReturn(expectedString);

        String actualString = invocationMatcher.toString(printSettings);

        assertEquals(expectedString, actualString);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation i = mock(Invocation.class);
        Object[] arguments = {1, "Test", true};
        when(i.getArguments()).thenReturn(arguments);
        CapturesArguments capturesArgumentsMatcher1 = mock(CapturesArguments.class);
        CapturesArguments capturesArgumentsMatcher2 = mock(CapturesArguments.class);
        matchers.add(capturesArgumentsMatcher1);
        matchers.add(capturesArgumentsMatcher2);

        invocationMatcher.captureArgumentsFrom(i);

        verify(capturesArgumentsMatcher1).captureFrom(arguments[0]);
        verify(capturesArgumentsMatcher2).captureFrom(arguments[1]);
        verifyNoMoreInteractions(capturesArgumentsMatcher2);
    }
}
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.Invocation;
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
        matchers = new ArrayList<>();
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void constructor_withInvocationAndMatchers_shouldSetInvocationAndMatchers() {
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void constructor_withInvocationOnly_shouldSetInvocationAndUseDefaultMatchers() {
        InvocationMatcher invMatcher = new InvocationMatcher(invocation);
        assertEquals(invocation, invMatcher.getInvocation());
        assertEquals(invocation.argumentsToMatchers(), invMatcher.getMatchers());
    }

    @Test
    public void getMethod_shouldReturnMethodFromInvocation() {
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);

        assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void toString_shouldReturnStringRepresentationOfInvocation() {
        String invocationToString = "SomeInvocation";
        when(invocation.toString(matchers, new PrintSettings())).thenReturn(invocationToString);

        assertEquals(invocationToString, invocationMatcher.toString());
    }

    @Test
    public void matches_withSameMocksSameMethodsSameArguments_shouldReturnTrue() {
        Invocation actualInvocation = mock(Invocation.class);
        when(actualInvocation.getMock()).thenReturn(invocation.getMock());
        when(actualInvocation.getMethod()).thenReturn(invocation.getMethod());
        when(new ArgumentsComparator().argumentsMatch(invocationMatcher, actualInvocation)).thenReturn(true);

        assertTrue(invocationMatcher.matches(actualInvocation));
    }

    @Test
    public void matches_withDifferentMocks_shouldReturnFalse() {
        Invocation actualInvocation = mock(Invocation.class);
        when(actualInvocation.getMock()).thenReturn(mock(Object.class));

        assertFalse(invocationMatcher.matches(actualInvocation));
    }

    @Test
    public void matches_withDifferentMethods_shouldReturnFalse() {
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        Invocation actualInvocation = mock(Invocation.class);
        when(actualInvocation.getMethod()).thenReturn(method2);

        assertFalse(invocationMatcher.matches(actualInvocation));
    }

    @Test
    public void matches_withDifferentArguments_shouldReturnFalse() {
        Invocation actualInvocation = mock(Invocation.class);
        when(new ArgumentsComparator().argumentsMatch(invocationMatcher, actualInvocation)).thenReturn(false);

        assertFalse(invocationMatcher.matches(actualInvocation));
    }

    @Test
    public void hasSimilarMethod_withSameMethodNameSameMocksNotVerified_shouldReturnTrue() {
        String methodName = "someMethod";
        when(invocation.getMethod().getName()).thenReturn(methodName);
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod().getName()).thenReturn(methodName);
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(candidate.getMock());

        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void hasSimilarMethod_withDifferentMethodName_shouldReturnFalse() {
        String methodName1 = "method1";
        String methodName2 = "method2";
        when(invocation.getMethod().getName()).thenReturn(methodName1);
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod().getName()).thenReturn(methodName2);

        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void hasSimilarMethod_withVerifiedCandidate_shouldReturnFalse() {
        String methodName = "someMethod";
        when(invocation.getMethod().getName()).thenReturn(methodName);
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod().getName()).thenReturn(methodName);
        when(candidate.isVerified()).thenReturn(true);
        when(invocation.getMock()).thenReturn(candidate.getMock());

        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void hasSimilarMethod_withDifferentMocks_shouldReturnFalse() {
        String methodName = "someMethod";
        when(invocation.getMethod().getName()).thenReturn(methodName);
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod().getName()).thenReturn(methodName);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(candidate.getMock()).thenReturn(mock(Object.class));

        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void hasSimilarMethod_withSameMethodNameSameMocksVerifiedButDifferentArguments_shouldReturnTrue() {
        String methodName = "someMethod";
        when(invocation.getMethod().getName()).thenReturn(methodName);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod().getName()).thenReturn(methodName);
        when(invocation.getMock()).thenReturn(candidate.getMock());
        when(new ArgumentsComparator().argumentsMatch(invocationMatcher, candidate.getArguments())).thenReturn(false);

        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void hasSimilarMethod_withSameMethodNameSameMocksUnverifiedAndSameArguments_shouldReturnFalse() {
        String methodName = "someMethod";
        when(invocation.getMethod().getName()).thenReturn(methodName);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod().getName()).thenReturn(methodName);
        when(invocation.getMock()).thenReturn(candidate.getMock());
        when(new ArgumentsComparator().argumentsMatch(invocationMatcher, candidate.getArguments())).thenReturn(true);

        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void hasSameMethod_withSameMethods_shouldReturnTrue() {
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(method2);

        assertTrue(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void hasSameMethod_withDifferentMethods_shouldReturnFalse() {
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(method2);

        assertFalse(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void getLocation_shouldReturnLocationFromInvocation() {
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);

        assertEquals(location, invocationMatcher.getLocation());
    }

    @Test
    public void toString_withPrintSettings_shouldReturnStringRepresentationOfInvocationWithGivenPrintSettings() {
        String invocationToString = "SomeInvocation";
        PrintSettings printSettings = mock(PrintSettings.class);
        when(invocation.toString(matchers, printSettings)).thenReturn(invocationToString);

        assertEquals(invocationToString, invocationMatcher.toString(printSettings));
    }

    @Test
    public void captureArgumentsFrom_withMatchersImplementingCapturesArguments_shouldCaptureArgumentsFromInvocation() {
        Matcher matcher1 = mock(CapturesArguments.class);
        Matcher matcher2 = mock(CapturesArguments.class);
        List<Matcher> matchers = Arrays.asList(matcher1, matcher2);
        Object[] arguments = { "arg1", "arg2" };
        Invocation invocation = mock(Invocation.class);
        when(invocation.getArguments()).thenReturn(arguments);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        invocationMatcher.captureArgumentsFrom(invocation);

        verify((CapturesArguments) matcher1).captureFrom(arguments[0]);
        verify((CapturesArguments) matcher2).captureFrom(arguments[1]);
    }
}
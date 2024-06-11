import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;
    private Invocation invocation;
    private List<Matcher> matchers;

    @Before
    public void setUp() {
        invocation = Mockito.mock(Invocation.class);
        matchers = new ArrayList<>();
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void testGetMethod() {
        Method expected = Mockito.mock(Method.class);
        Mockito.when(invocation.getMethod()).thenReturn(expected);

        Method result = invocationMatcher.getMethod();

        assertEquals(expected, result);
    }

    @Test
    public void testGetInvocation() {
        Invocation result = invocationMatcher.getInvocation();

        assertEquals(invocation, result);
    }

    @Test
    public void testGetMatchers() {
        List<Matcher> result = invocationMatcher.getMatchers();

        assertEquals(matchers, result);
    }

    @Test
    public void testToString() {
        String expected = "invocation";
        Mockito.when(invocation.toString(matchers, new PrintSettings())).thenReturn(expected);

        String result = invocationMatcher.toString();

        assertEquals(expected, result);
    }

    @Test
    public void testMatches() {
        Invocation actual = Mockito.mock(Invocation.class);
        Mockito.when(invocation.getMock()).thenReturn("mock");
        Mockito.when(actual.getMock()).thenReturn("mock");

        boolean result = invocationMatcher.matches(actual);

        assertTrue(result);
    }

    @Test
    public void testMatches_notMatchingMock() {
        Invocation actual = Mockito.mock(Invocation.class);
        Mockito.when(invocation.getMock()).thenReturn("mock1");
        Mockito.when(actual.getMock()).thenReturn("mock2");

        boolean result = invocationMatcher.matches(actual);

        assertFalse(result);
    }

    @Test
    public void testMatches_notMatchingMethod() {
        Invocation actual = Mockito.mock(Invocation.class);
        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);
        Mockito.when(invocation.getMethod()).thenReturn(method1);
        Mockito.when(actual.getMethod()).thenReturn(method2);

        boolean result = invocationMatcher.matches(actual);

        assertFalse(result);
    }

    @Test
    public void testMatches_argumentsMatch() {
        Invocation actual = Mockito.mock(Invocation.class);
        Object[] arguments1 = new Object[]{"arg1", "arg2"};
        Object[] arguments2 = new Object[]{"arg1", "arg2"};
        Mockito.when(invocation.getArguments()).thenReturn(arguments1);
        Mockito.when(actual.getArguments()).thenReturn(arguments2);

        boolean result = invocationMatcher.matches(actual);

        assertTrue(result);
    }

    @Test
    public void testMatches_argumentsDoNotMatch() {
        Invocation actual = Mockito.mock(Invocation.class);
        Object[] arguments1 = new Object[]{"arg1", "arg2"};
        Object[] arguments2 = new Object[]{"arg3", "arg4"};
        Mockito.when(invocation.getArguments()).thenReturn(arguments1);
        Mockito.when(actual.getArguments()).thenReturn(arguments2);

        boolean result = invocationMatcher.matches(actual);

        assertFalse(result);
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Object[] arguments = new Object[]{"arg1", "arg2"};
        boolean expected = true;
        ArgumentComparator argumentComparator = Mockito.mock(ArgumentComparator.class);
        Mockito.when(argumentComparator.argumentsMatch(invocationMatcher, arguments)).thenReturn(expected);

        boolean result = invocationMatcher.safelyArgumentsMatch(arguments);

        assertEquals(expected, result);
    }

    @Test
    public void testSafelyArgumentsMatch_exceptionThrown() {
        Object[] arguments = new Object[]{"arg1", "arg2"};
        boolean expected = false;
        ArgumentComparator argumentComparator = Mockito.mock(ArgumentComparator.class);
        Mockito.when(argumentComparator.argumentsMatch(invocationMatcher, arguments)).thenThrow(new RuntimeException());

        boolean result = invocationMatcher.safelyArgumentsMatch(arguments);

        assertEquals(expected, result);
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation candidate = Mockito.mock(Invocation.class);
        Mockito.when(invocation.getMethod()).thenReturn("method");
        Mockito.when(candidate.getMethod()).thenReturn("method");
        Mockito.when(candidate.isVerified()).thenReturn(false);
        Mockito.when(invocation.getMock()).thenReturn("mock");
        Mockito.when(candidate.getMock()).thenReturn("mock");
        Mockito.when(invocation.getArguments()).thenReturn(new Object[]{"arg1", "arg2"});
        Mockito.when(candidate.getArguments()).thenReturn(new Object[]{"arg3", "arg4"});
        InvocationMatcher otherInvocationMatcher = new InvocationMatcher(candidate, matchers);
        Mockito.when(invocationMatcher.hasSameMethod(candidate)).thenReturn(true);
        Mockito.when(invocationMatcher.safelyArgumentsMatch(candidate.getArguments())).thenReturn(false);

        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        assertTrue(result);
    }

    @Test
    public void testHasSimilarMethod_methodNameNotEqual() {
        Invocation candidate = Mockito.mock(Invocation.class);
        Mockito.when(invocation.getMethod()).thenReturn("method1");
        Mockito.when(candidate.getMethod()).thenReturn("method2");

        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_isVerified() {
        Invocation candidate = Mockito.mock(Invocation.class);
        Mockito.when(invocation.getMethod()).thenReturn("method");
        Mockito.when(candidate.getMethod()).thenReturn("method");
        Mockito.when(candidate.isVerified()).thenReturn(true);

        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_mockNotEqual() {
        Invocation candidate = Mockito.mock(Invocation.class);
        Mockito.when(invocation.getMethod()).thenReturn("method");
        Mockito.when(candidate.getMethod()).thenReturn("method");
        Mockito.when(candidate.isVerified()).thenReturn(false);
        Mockito.when(invocation.getMock()).thenReturn("mock1");
        Mockito.when(candidate.getMock()).thenReturn("mock2");

        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_sameMethod() {
        Invocation candidate = Mockito.mock(Invocation.class);
        Mockito.when(invocation.getMethod()).thenReturn("method");
        Mockito.when(candidate.getMethod()).thenReturn("method");
        Mockito.when(candidate.isVerified()).thenReturn(false);
        Mockito.when(invocation.getMock()).thenReturn("mock");
        Mockito.when(candidate.getMock()).thenReturn("mock");
        Mockito.when(invocationMatcher.hasSameMethod(candidate)).thenReturn(true);

        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_overloadedButSameArgs() {
        Invocation candidate = Mockito.mock(Invocation.class);
        Mockito.when(invocation.getMethod()).thenReturn("method");
        Mockito.when(candidate.getMethod()).thenReturn("method");
        Mockito.when(candidate.isVerified()).thenReturn(false);
        Mockito.when(invocation.getMock()).thenReturn("mock");
        Mockito.when(candidate.getMock()).thenReturn("mock");
        Mockito.when(invocationMatcher.hasSameMethod(candidate)).thenReturn(false);
        Mockito.when(invocationMatcher.safelyArgumentsMatch(candidate.getArguments())).thenReturn(true);

        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        assertFalse(result);
    }

    @Test
    public void testHasSameMethod() {
        Invocation candidate = Mockito.mock(Invocation.class);
        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);
        Mockito.when(invocation.getMethod()).thenReturn(method1);
        Mockito.when(candidate.getMethod()).thenReturn(method2);

        boolean result = invocationMatcher.hasSameMethod(candidate);

        assertFalse(result);
    }

    @Test
    public void testGetLocation() {
        Location expected = Mockito.mock(Location.class);
        Mockito.when(invocation.getLocation()).thenReturn(expected);

        Location result = invocationMatcher.getLocation();

        assertEquals(expected, result);
    }

    @Test
    public void testToStringWithPrintSettings() {
        String expected = "invocation";
        PrintSettings printSettings = Mockito.mock(PrintSettings.class);
        Mockito.when(invocation.toString(matchers, printSettings)).thenReturn(expected);

        String result = invocationMatcher.toString(printSettings);

        assertEquals(expected, result);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Object[] arguments = new Object[]{"arg1", "arg2"};
        Matchers.captureFrom(arguments[0]);
        Matchers.captureFrom(arguments[1]);
        Mockito.when(invocation.getArguments()).thenReturn(arguments);
        CapturesArguments capturesArguments = Mockito.mock(CapturesArguments.class);
        List<Matcher> matchersWithCaptures = new ArrayList<>();
        matchersWithCaptures.add(capturesArguments);
        InvocationMatcher invocationMatcherWithCaptures = new InvocationMatcher(invocation2, matchersWithCaptures);

        invocationMatcherWithCaptures.captureArgumentsFrom(invocation);

        Mockito.verify(capturesArguments, Mockito.times(1)).captureFrom(arguments[0]);
        Mockito.verify(capturesArguments, Mockito.times(1)).captureFrom(arguments[1]);
    }
}
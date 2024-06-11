import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class InvocationMatcherTest {

    private Invocation invocation;
    private InvocationMatcher invocationMatcher;

    @Before
    public void setUp() {
        Object mock = new Object();
        Method method = mock(Object.class).getClass().getDeclaredMethods()[0];
        Object[] arguments = {"arg1", "arg2"};
        Location location = new Location("TestClass", "TestMethod", 42);
        invocation = new Invocation(mock, method, arguments, location);
        invocationMatcher = new InvocationMatcher(invocation);
    }

    @Test
    public void testConstructor_withInvocationAndMatchers_shouldSetInvocationAndMatchers() {
        List<Matcher<?>> matchers = new ArrayList<>();
        Matcher<?> matcher = new CapturesArguments();
        matchers.add(matcher);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructor_withoutMatchers_shouldUseInvocationArgumentsToMatchers() {
        List<Matcher<?>> expectedMatchers = invocation.argumentsToMatchers();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(expectedMatchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testGetMethod_shouldReturnMethodFromInvocation() {
        Method expectedMethod = invocation.getMethod();
        Method actualMethod = invocationMatcher.getMethod();

        assertEquals(expectedMethod, actualMethod);
    }

    @Test
    public void testToString_shouldReturnInvocationToStringRepresentation() {
        String expected = invocation.toString(invocationMatcher.getMatchers());
        String actual = invocationMatcher.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testMatches_withMatchingInvocation_shouldReturnTrue() {
        Invocation actualInvocation = new Invocation(invocation.getMock(),
                invocation.getMethod(), invocation.getArguments(),
                invocation.getLocation());

        boolean result = invocationMatcher.matches(actualInvocation);

        assertTrue(result);
    }

    @Test
    public void testMatches_withNonMatchingInvocation_shouldReturnFalse() {
        Invocation actualInvocation = new Invocation(new Object(), invocation.getMethod(),
                invocation.getArguments(), invocation.getLocation());

        boolean result = invocationMatcher.matches(actualInvocation);

        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_withSimilarMethodAndSameArguments_shouldReturnFalse() {
        Invocation candidate = new Invocation(invocation.getMock(),
                invocation.getMethod(), invocation.getArguments(),
                invocation.getLocation());

        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_withSimilarMethodAndDifferentArguments_shouldReturnTrue() {
        Object[] arguments = {"arg1", "arg3"};
        Invocation candidate = new Invocation(invocation.getMock(),
                invocation.getMethod(), arguments, invocation.getLocation());

        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        assertTrue(result);
    }

    @Test
    public void testHasSimilarMethod_withDifferentMethod_shouldReturnFalse() {
        Method method = mock(Object.class).getClass().getDeclaredMethods()[1];
        Invocation candidate = new Invocation(invocation.getMock(), method,
                invocation.getArguments(), invocation.getLocation());

        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        assertFalse(result);
    }

    @Test
    public void testHasSameMethod_withSameMethod_shouldReturnTrue() {
        Invocation candidate = new Invocation(invocation.getMock(),
                invocation.getMethod(), invocation.getArguments(),
                invocation.getLocation());

        boolean result = invocationMatcher.hasSameMethod(candidate);

        assertTrue(result);
    }

    @Test
    public void testHasSameMethod_withDifferentMethod_shouldReturnFalse() {
        Method method = mock(Object.class).getClass().getDeclaredMethods()[1];
        Invocation candidate = new Invocation(invocation.getMock(), method,
                invocation.getArguments(), invocation.getLocation());

        boolean result = invocationMatcher.hasSameMethod(candidate);

        assertFalse(result);
    }

    @Test
    public void testGetLocation_shouldReturnInvocationLocation() {
        Location expectedLocation = invocation.getLocation();
        Location actualLocation = invocationMatcher.getLocation();

        assertEquals(expectedLocation, actualLocation);
    }

    @Test
    public void testToString_withPrintSettings_shouldReturnInvocationToStringRepresentationWithPrintSettings() {
        PrintSettings printSettings = new PrintSettings();
        String expected = invocation.toString(invocationMatcher.getMatchers(), printSettings);
        String actual = invocationMatcher.toString(printSettings);

        assertEquals(expected, actual);
    }

    @Test
    public void testCaptureArgumentsFrom_withCapturesArgumentsMatcher_shouldCaptureArgumentsFromInvocation() {
        Invocation i = new Invocation(invocation.getMock(),
                invocation.getMethod(), invocation.getArguments(),
                invocation.getLocation());
        Matcher<?> matcher = new CapturesArguments();
        List<Matcher<?>> matchers = new ArrayList<>();
        matchers.add(matcher);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        invocationMatcher.captureArgumentsFrom(i);

        Object[] capturedArguments = ((CapturesArguments) matcher).getCapturedArguments();

        assertArrayEquals(invocation.getArguments(), capturedArguments);
    }

    @Test
    public void testCreateFrom_withInvocations_shouldCreateInvocationMatchersFromInvocations() {
        List<Invocation> invocations = new ArrayList<>();
        invocations.add(invocation);

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        assertEquals(1, invocationMatchers.size());
        assertEquals(invocation, invocationMatchers.get(0).getInvocation());
    }
}
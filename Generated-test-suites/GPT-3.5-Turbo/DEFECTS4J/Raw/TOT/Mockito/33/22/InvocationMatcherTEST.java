import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.invocation.Invocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.matchers.Equals;
import org.mockito.internal.reporting.PrintSettings;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;

    @Before
    public void setup() {
        Invocation invocation = new Invocation(null, null, null);
        invocationMatcher = new InvocationMatcher(invocation);
    }

    @Test
    public void testConstructorWithInvocationAndMatchers() {
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(new Equals(1));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

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
        Method method = invocation.getMethod();

        assertNull(method);
    }

    @Test
    public void testToString() {
        String toString = invocationMatcher.toString();

        assertNotNull(toString);
    }

    @Test
    public void testMatches() {
        Invocation actual = new Invocation(null, null, null);

        boolean matches = invocationMatcher.matches(actual);

        assertTrue(matches);
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation candidate = new Invocation(null, null, null);

        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidate);

        assertTrue(hasSimilarMethod);
    }

    @Test
    public void testHasSameMethod() {
        Invocation candidate = new Invocation(null, null, null);

        boolean hasSameMethod = invocationMatcher.hasSameMethod(candidate);

        assertTrue(hasSameMethod);
    }

    @Test
    public void testGetLocation() {
        Location location = invocationMatcher.getLocation();

        assertNull(location);
    }

    @Test
    public void testToStringWithPrintSettings() {
        PrintSettings printSettings = new PrintSettings();

        String toString = invocationMatcher.toString(printSettings);

        assertNotNull(toString);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation i = new Invocation(null, null, null);

        invocationMatcher.captureArgumentsFrom(i);

        // Verify that captureArgumentsFrom method executed without any exceptions
        assertTrue(true);
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new ArrayList<>();

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        assertEquals(invocations.size(), invocationMatchers.size());
    }

    @Test(expected = MockitoException.class)
    public void testConstructorWithEmptyMatchers() {
        new InvocationMatcher(invocation, Collections.emptyList());
    }

    @Test(expected = MockitoException.class)
    public void testSafelyArgumentsMatch() {
        invocationMatcher.hasSameMethod(null);
    }

    @Test(expected = MockitoException.class)
    public void testToStringWithNullPrintSettings() {
        invocationMatcher.toString(null);
    }

}
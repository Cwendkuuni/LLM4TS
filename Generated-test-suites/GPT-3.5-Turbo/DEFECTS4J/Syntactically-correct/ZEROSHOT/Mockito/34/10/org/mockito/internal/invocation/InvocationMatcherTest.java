import org.junit.Test;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import org.hamcrest.Matcher;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.internal.invocation.InvocationMatcher;

import static org.junit.Assert.*;

public class InvocationMatcherTest {

    @Test
    public void testGetMethod() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = Collections.emptyList();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Method result = invocationMatcher.getMethod();

        assertNotNull(result);
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = Collections.emptyList();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Invocation result = invocationMatcher.getInvocation();

        assertNotNull(result);
    }

    @Test
    public void testGetMatchers() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = Collections.emptyList();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        List<Matcher> result = invocationMatcher.getMatchers();

        assertNotNull(result);
    }

    @Test
    public void testToString() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = Collections.emptyList();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        String result = invocationMatcher.toString();

        assertNotNull(result);
    }

    @Test
    public void testMatches() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = Collections.emptyList();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Invocation actual = new Invocation();
        boolean result = invocationMatcher.matches(actual);

        assertNotNull(result);
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = Collections.emptyList();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Object[] actualArgs = {};
        boolean result = invocationMatcher.safelyArgumentsMatch(actualArgs);

        assertNotNull(result);
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = Collections.emptyList();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Invocation candidate = new Invocation();
        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        assertNotNull(result);
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = Collections.emptyList();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Invocation candidate = new Invocation();
        boolean result = invocationMatcher.hasSameMethod(candidate);

        assertNotNull(result);
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = Collections.emptyList();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Location result = invocationMatcher.getLocation();

        assertNotNull(result);
    }

    @Test
    public void testToStringWithPrintSettings() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = Collections.emptyList();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        PrintSettings printSettings = new PrintSettings();
        String result = invocationMatcher.toString(printSettings);

        assertNotNull(result);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = Collections.emptyList();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Invocation i = new Invocation();
        invocationMatcher.captureArgumentsFrom(i);

        // Assertion for CapturesArguments
    }
}
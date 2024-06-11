import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.junit.Test;

@SuppressWarnings("unchecked")
public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcher() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = new LinkedList<Matcher>();
        matchers.add(new Matcher());

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Method expectedMethod = invocation.getMethod();
        Method actualMethod = invocationMatcher.getMethod();

        assertEquals(expectedMethod, actualMethod);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testInvocationMatcher_withEmptyMatchers() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = Collections.emptyList();

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Method expectedMethod = invocation.getMethod();
        Method actualMethod = invocationMatcher.getMethod();

        assertEquals(expectedMethod, actualMethod);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(invocation.argumentsToMatchers(), invocationMatcher.getMatchers());
    }

    @Test
    public void testInvocationMatcher_withInvocationOnly() {
        Invocation invocation = new Invocation();

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Method expectedMethod = invocation.getMethod();
        Method actualMethod = invocationMatcher.getMethod();

        assertEquals(expectedMethod, actualMethod);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(Collections.emptyList(), invocationMatcher.getMatchers());
    }

    @Test
    public void testGetMethod() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Method expectedMethod = invocation.getMethod();
        Method actualMethod = invocationMatcher.getMethod();

        assertEquals(expectedMethod, actualMethod);
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation expectedInvocation = invocation;
        Invocation actualInvocation = invocationMatcher.getInvocation();

        assertEquals(expectedInvocation, actualInvocation);
    }

    @Test
    public void testGetMatchers() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = new LinkedList<Matcher>();
        matchers.add(new Matcher());

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        List<Matcher> expectedMatchers = matchers;
        List<Matcher> actualMatchers = invocationMatcher.getMatchers();

        assertEquals(expectedMatchers, actualMatchers);
    }

    @Test
    public void testToString() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        String expectedToString = invocation.toString(invocation.argumentsToMatchers(), new PrintSettings());
        String actualToString = invocationMatcher.toString();

        assertEquals(expectedToString, actualToString);
    }

    @Test
    public void testMatches() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation actualInvocation = new Invocation();

        boolean expectedMatches = false;
        boolean actualMatches = invocationMatcher.matches(actualInvocation);

        assertEquals(expectedMatches, actualMatches);
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation candidate = new Invocation();

        boolean expectedHasSimilarMethod = false;
        boolean actualHasSimilarMethod = invocationMatcher.hasSimilarMethod(candidate);

        assertEquals(expectedHasSimilarMethod, actualHasSimilarMethod);
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation candidate = new Invocation();

        boolean expectedHasSameMethod = false;
        boolean actualHasSameMethod = invocationMatcher.hasSameMethod(candidate);

        assertEquals(expectedHasSameMethod, actualHasSameMethod);
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Location expectedLocation = invocation.getLocation();
        Location actualLocation = invocationMatcher.getLocation();

        assertEquals(expectedLocation, actualLocation);
    }

    @Test
    public void testToStringWithPrintSettings() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        PrintSettings printSettings = new PrintSettings();

        String expectedToString = invocation.toString(invocation.argumentsToMatchers(), printSettings);
        String actualToString = invocationMatcher.toString(printSettings);

        assertEquals(expectedToString, actualToString);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        invocationMatcher.captureArgumentsFrom(invocation);
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new LinkedList<Invocation>();
        invocations.add(new Invocation());

        List<InvocationMatcher> expectedInvocationMatchers = new LinkedList<InvocationMatcher>();
        expectedInvocationMatchers.add(new InvocationMatcher(new Invocation()));

        List<InvocationMatcher> actualInvocationMatchers = InvocationMatcher.createFrom(invocations);

        assertEquals(expectedInvocationMatchers, actualInvocationMatchers);
    }
}
import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithMatchers() {
        Invocation invocation = new Invocation();  // create an invocation object
        List<Matcher> matchers = new LinkedList<>();  // create a list of matchers
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);  // create an invocation matcher object
        assertNotNull(invocationMatcher);  // assert that invocation matcher is not null
        assertEquals(invocation, invocationMatcher.getInvocation());  // assert that getInvocation() returns the correct invocation object
        assertEquals(matchers, invocationMatcher.getMatchers());  // assert that getMatchers() returns the correct matchers list
    }

    @Test
    public void testConstructorWithoutMatchers() {
        Invocation invocation = new Invocation();  // create an invocation object
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);  // create an invocation matcher object without matchers
        assertNotNull(invocationMatcher);  // assert that invocation matcher is not null
        assertEquals(invocation, invocationMatcher.getInvocation());  // assert that getInvocation() returns the correct invocation object
        assertEquals(invocation.argumentsToMatchers(), invocationMatcher.getMatchers());  // assert that getMatchers() returns the correct matchers list from the invocation object
    }

    @Test
    public void testGetMethod() {
        Invocation invocation = new Invocation();  // create an invocation object
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);  // create an invocation matcher object
        Method method = invocation.getMethod();  // get the method from the invocation object
        assertEquals(method, invocationMatcher.getMethod());  // assert that getMethod() returns the correct method
    }

    @Test
    public void testToString() {
        Invocation invocation = new Invocation();  // create an invocation object
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);  // create an invocation matcher object
        String expected = invocation.toString(invocationMatcher.getMatchers(), new PrintSettings());  // get the expected string representation of the invocation
        assertEquals(expected, invocationMatcher.toString());  // assert that toString() returns the correct string representation
    }

    @Test
    public void testMatches() {
        Invocation invocation = new Invocation();  // create an invocation object
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);  // create an invocation matcher object
        assertTrue(invocationMatcher.matches(invocation));  // assert that the invocation matcher matches the invocation object
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Invocation invocation = new Invocation();  // create an invocation object
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);  // create an invocation matcher object
        assertTrue(invocationMatcher.safelyArgumentsMatch(invocation.getArguments()));  // assert that the invocation matcher's arguments match the invocation's arguments
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = new Invocation();  // create an invocation object
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);  // create an invocation matcher object
        assertTrue(invocationMatcher.hasSimilarMethod(invocation));  // assert that the invocation matcher has a similar method as the invocation object
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation = new Invocation();  // create an invocation object
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);  // create an invocation matcher object
        assertTrue(invocationMatcher.hasSameMethod(invocation));  // assert that the invocation matcher has the same method as the invocation object
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = new Invocation();  // create an invocation object
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);  // create an invocation matcher object
        Location location = invocation.getLocation();  // get the location from the invocation object
        assertEquals(location, invocationMatcher.getLocation());  // assert that getLocation() returns the correct location
    }

    @Test
    public void testToStringWithPrintSettings() {
        Invocation invocation = new Invocation();  // create an invocation object
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);  // create an invocation matcher object
        PrintSettings printSettings = new PrintSettings();  // create a print settings object
        String expected = invocation.toString(invocationMatcher.getMatchers(), printSettings);  // get the expected string representation of the invocation
        assertEquals(expected, invocationMatcher.toString(printSettings));  // assert that toString(printSettings) returns the correct string representation
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = new Invocation();  // create an invocation object
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);  // create an invocation matcher object
        Invocation i = new Invocation();  // create a new invocation object
        invocationMatcher.captureArgumentsFrom(i);  // capture arguments from the new invocation object
        assertTrue(invocationMatcher.getMatchers().get(0) instanceof CapturesArguments);  // assert that the first matcher in the matchers list is an instance of CapturesArguments
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new LinkedList<>();  // create a list of invocations
        Invocation invocation = new Invocation();  // create an invocation object
        invocations.add(invocation);  // add the invocation object to the invocations list
        List<InvocationMatcher> expected = new LinkedList<>();  // create a list of expected invocation matchers
        expected.add(new InvocationMatcher(invocation));  // add the invocation matcher object to the expected list
        List<InvocationMatcher> actual = InvocationMatcher.createFrom(invocations);  // create invocation matchers from the invocations list
        assertEquals(expected, actual);  // assert that the actual invocation matchers are equal to the expected invocation matchers
    }
}
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.matchers.CapturesArguments;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithMatchers() {
        // Test case for constructor with matchers parameter

        // Mocking the Invocation class
        Invocation invocation = new Invocation(1, "mock", "method", Collections.emptyList(), null);
        // Creating a matcher for testing
        CapturesArguments capturesArguments = new CapturesArguments();
        // Invoking a method that will invoke this constructor
        invocation.captureArgumentsFrom(invocation);

        // creating the InvocationMatcher instance
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(capturesArguments));

        // Asserting that the InvocationMatcher instance is not null
        assertNotNull(invocationMatcher);
        // Asserting that the InvocationMatcher instance has the expected Invocation reference
        assertEquals(invocation, invocationMatcher.getInvocation());
        // Asserting that the InvocationMatcher instance has the expected matchers list
        assertEquals(Arrays.asList(capturesArguments), invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithoutMatchers() {
        // Test case for constructor without matchers parameter

        // Mocking the Invocation class
        Invocation invocation = new Invocation(1, "mock", "method", Collections.emptyList(), null);

        // creating the InvocationMatcher instance
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Asserting that the InvocationMatcher instance is not null
        assertNotNull(invocationMatcher);
        // Asserting that the InvocationMatcher instance has the expected Invocation reference
        assertEquals(invocation, invocationMatcher.getInvocation());
        // Asserting that the InvocationMatcher instance has an empty matchers list
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testGetMethod() {
        // Test case for getMethod() method

        // Mocking the Method class
        Method method = null;
        try {
            method = InvocationMatcher.class.getDeclaredMethod("getMethod");
        } catch (NoSuchMethodException e) {
            fail("Failed to mock Method class");
        }

        // Mocking the Invocation class
        Invocation invocation = new Invocation(1, "mock", "method", Collections.emptyList(), null);

        // creating the InvocationMatcher instance
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Asserting that the getMethod() method returns the expected Method instance
        assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testMatches() {
        // Test case for matches() method

        // Mocking the Invocation class
        Invocation invocation = new Invocation(1, "mock", "method", Collections.emptyList(), null);

        // creating the InvocationMatcher instance
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // creating an actual Invocation with the same details as the invocationMatcher
        Invocation actualInvocation = new Invocation(1, "mock", "method", Collections.emptyList(), null);

        // Asserting that the matches() method returns true when actualInvocation matches invocationMatcher
        assertTrue(invocationMatcher.matches(actualInvocation));

        // creating an actualInvocation with different details than the invocationMatcher
        Invocation actualInvocation2 = new Invocation(2, "mock", "method2", Collections.emptyList(), null);

        // Asserting that the matches() method returns false when actualInvocation does not match invocationMatcher
        assertFalse(invocationMatcher.matches(actualInvocation2));
    }

    @Test
    public void testHasSimilarMethod() {
        // Test case for hasSimilarMethod() method

        // Mocking the Invocation class
        Invocation invocation = new Invocation(1, "mock", "method", Collections.emptyList(), null);

        // creating the InvocationMatcher instance
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // creating a candidate Invocation with the same method name, mock, and unverified
        Invocation candidateInvocation = new Invocation(1, "mock", "method", Collections.emptyList(), null);

        // Asserting that the hasSimilarMethod() method returns true when candidateInvocation has similar method to invocationMatcher
        assertTrue(invocationMatcher.hasSimilarMethod(candidateInvocation));

        // creating a candidate Invocation with different method name
        Invocation candidateInvocation2 = new Invocation(1, "mock", "method2", Collections.emptyList(), null);

        // Asserting that the hasSimilarMethod() method returns false when candidateInvocation does not have similar method to invocationMatcher
        assertFalse(invocationMatcher.hasSimilarMethod(candidateInvocation2));

        // creating a candidate Invocation with same method name but verified
        Invocation candidateInvocation3 = new Invocation(1, "mock", "method", Collections.emptyList(), null);
        candidateInvocation3.markVerified();

        // Asserting that the hasSimilarMethod() method returns false when candidateInvocation is verified
        assertFalse(invocationMatcher.hasSimilarMethod(candidateInvocation3));

        // creating a candidate Invocation with different mock
        Invocation candidateInvocation4 = new Invocation(1, "mock2", "method", Collections.emptyList(), null);

        // Asserting that the hasSimilarMethod() method returns false when candidateInvocation has different mock
        assertFalse(invocationMatcher.hasSimilarMethod(candidateInvocation4));

        // creating a candidate Invocation with different method but same arguments
        Invocation candidateInvocation5 = new Invocation(1, "mock", "method2", Collections.emptyList(), null, 1, "param");

        // Asserting that the hasSimilarMethod() method returns false when candidateInvocation has different method but same arguments
        assertFalse(invocationMatcher.hasSimilarMethod(candidateInvocation5));

        // creating a candidate Invocation with overloaded method and same arguments
        Invocation candidateInvocation6 = new Invocation(1, "mock", "method2", Collections.emptyList(), null, 1, "param");

        // Asserting that the hasSimilarMethod() method returns false when candidateInvocation has overloaded method but same arguments
        assertFalse(invocationMatcher.hasSimilarMethod(candidateInvocation6));
    }

    @Test
    public void testHasSameMethod() {
        // Test case for hasSameMethod() method

        // Mocking the Invocation class
        Invocation invocation = new Invocation(1, "mock", "method", Collections.emptyList(), null);

        // creating the InvocationMatcher instance
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // creating a candidate Invocation with the same method
        Invocation candidateInvocation = new Invocation(1, "mock", "method", Collections.emptyList(), null);

        // Asserting that the hasSameMethod() method returns true when candidateInvocation has the same method as invocationMatcher
        assertTrue(invocationMatcher.hasSameMethod(candidateInvocation));

        // creating a candidate Invocation with different method
        Invocation candidateInvocation2 = new Invocation(1, "mock", "method2", Collections.emptyList(), null);

        // Asserting that the hasSameMethod() method returns false when candidateInvocation does not have the same method as invocationMatcher
        assertFalse(invocationMatcher.hasSameMethod(candidateInvocation2));
    }

    @Test
    public void testGetLocation() {
        // Test case for getLocation() method

        // Mocking the Location class
        Location location = null;
        try {
            location = new Location("InvocationMatcherTest.java", 20);
        } catch (Exception e) {
            fail("Failed to mock Location class");
        }

        // Mocking the Invocation class
        Invocation invocation = new Invocation(1, "mock", "method", Collections.emptyList(), location);

        // creating the InvocationMatcher instance
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Asserting that the getLocation() method returns the expected Location instance
        assertEquals(location, invocationMatcher.getLocation());
    }

    @Test
    public void testToStringWithDefaultPrintSettings() {
        // Test case for toString() method with default PrintSettings

        // Mocking the PrintSettings class
        PrintSettings printSettings = new PrintSettings();
        printSettings.setMultiline(false);
        printSettings.setVerbose(true);

        // Mocking the Invocation class
        Invocation invocation = new Invocation(1, "mock", "method", Collections.emptyList(), null);

        // creating the InvocationMatcher instance
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Asserting that the toString() method returns the expected string representation of the InvocationMatcher
        assertEquals("mock -> method(", invocationMatcher.toString(printSettings));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        // Test case for captureArgumentsFrom() method

        // Mocking the Invocation class
        Invocation invocation = new Invocation(1, "mock", "method", Collections.singletonList("arg"), null);

        // creating the InvocationMatcher instance
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // creating a candidate Invocation to capture arguments from
        Invocation candidateInvocation = new Invocation(1, "mock", "method", Arrays.asList("arg1", "arg2"), null);

        // invoking the captureArgumentsFrom() method
        invocationMatcher.captureArgumentsFrom(candidateInvocation);

        // Asserting that the captured arguments are correct
        assertThat(invocationMatcher.getMatchers(), contains(equalTo("arg1")));
    }

}
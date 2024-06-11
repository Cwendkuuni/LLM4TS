import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;

import static org.junit.Assert.*;

public class InvocationMatcherTest {

    private Invocation invocation;
    private InvocationMatcher invocationMatcher;

    @Before
    public void setUp() {
        Method method = null;
        try {
            method = this.getClass().getMethod("dummyMethod");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        invocation = new Invocation(method, this, new Object[] {});

        List<Matcher> matchers = new ArrayList<>();
        matchers.add(Matchers.anyString());

        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void testConstructor_InvocationAndMatchers_EmptyMatchers_CreatesMatchersFromInvocation() {
        List<Matcher> emptyMatchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, emptyMatchers);
        List<Matcher> expectedMatchers = invocation.argumentsToMatchers();
        assertEquals(expectedMatchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructor_InvocationAndMatchers_NonEmptyMatchers_UsesProvidedMatchers() {
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(Matchers.anyString());
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructor_Invocation_CreatesMatchersFromInvocation() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        List<Matcher> expectedMatchers = invocation.argumentsToMatchers();
        assertEquals(expectedMatchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testGetMethod_ReturnsInvocationMethod() {
        Method expectedMethod = invocation.getMethod();
        assertEquals(expectedMethod, invocationMatcher.getMethod());
    }

    @Test
    public void testGetInvocation_ReturnsInvocation() {
        assertEquals(invocation, invocationMatcher.getInvocation());
    }

    @Test
    public void testGetMatchers_ReturnsMatchers() {
        List<Matcher> expectedMatchers = invocation.argumentsToMatchers();
        assertEquals(expectedMatchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testToString_ReturnsInvocationToString() {
        String expectedString = invocation.toString(invocation.argumentsToMatchers(), new PrintSettings());
        assertEquals(expectedString, invocationMatcher.toString());
    }

    @Test
    public void testMatches_MatchingInvocation_ReturnsTrue() {
        Invocation actualInvocation = new Invocation(invocation.getMethod(), this, new Object[] {});
        assertTrue(invocationMatcher.matches(actualInvocation));
    }

    @Test
    public void testMatches_NotMatchingInvocation_ReturnsFalse() {
        Invocation actualInvocation = new Invocation(invocation.getMethod(), new Object[] {});
        assertFalse(invocationMatcher.matches(actualInvocation));
    }

    @Test
    public void testHasSimilarMethod_SimilarMethod_ReturnsTrue() {
        Invocation candidate = new Invocation(invocation.getMethod(), this, new Object[] {});
        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethod_DifferentMethodName_ReturnsFalse() {
        Method method = null;
        try {
            method = this.getClass().getMethod("dummyMethod2");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        Invocation candidate = new Invocation(method, this, new Object[] {});
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethod_VerifiedInvocation_ReturnsFalse() {
        invocation.setVerified(true);
        Invocation candidate = new Invocation(invocation.getMethod(), this, new Object[] {});
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethod_DifferentMock_ReturnsFalse() {
        Invocation candidate = new Invocation(invocation.getMethod(), new Object[] {});
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethod_SameArgumentsDifferentMethod_ReturnsFalse() {
        Method method = null;
        try {
            method = this.getClass().getMethod("dummyMethod2");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        Invocation candidate = new Invocation(method, this, invocation.getArguments());
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSameMethod_SameMethod_ReturnsTrue() {
        Invocation candidate = new Invocation(invocation.getMethod(), this, new Object[] {});
        assertTrue(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testHasSameMethod_DifferentMethod_ReturnsFalse() {
        Method method = null;
        try {
            method = this.getClass().getMethod("dummyMethod2");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        Invocation candidate = new Invocation(method, this, new Object[] {});
        assertFalse(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testGetLocation_ReturnsInvocationLocation() {
        Location expectedLocation = invocation.getLocation();
        assertEquals(expectedLocation, invocationMatcher.getLocation());
    }

    @Test
    public void testToStringWithPrintSettings_ReturnsInvocationToStringWithPrintSettings() {
        PrintSettings printSettings = new PrintSettings();
        String expectedString = invocation.toString(invocation.argumentsToMatchers(), printSettings);
        assertEquals(expectedString, invocationMatcher.toString(printSettings));
    }

    @Test
    public void testCaptureArgumentsFrom_CapturesArgumentsFromInvocation() {
        Invocation i = new Invocation(invocation.getMethod(), this, new Object[] {"Test"});
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(Matchers.anyString());
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        invocationMatcher.captureArgumentsFrom(i);
        // Verify that the argument was captured
        assertEquals("Test", matchers.get(0).toString());
    }

    @Test
    public void testCreateFrom_ReturnsListOfInvocationMatchers() {
        List<Invocation> invocations = new ArrayList<>();
        invocations.add(invocation);

        List<InvocationMatcher> expectedInvocationMatchers = new ArrayList<>();
        expectedInvocationMatchers.add(invocationMatcher);

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        assertEquals(expectedInvocationMatchers, invocationMatchers);
    }

    // Dummy method for testing purposes
    public void dummyMethod() {}

    // Another dummy method for testing purposes
    public void dummyMethod2() {}

}
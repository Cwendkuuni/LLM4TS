
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;

    @Before
    public void setUp() {
        Invocation invocation = new Invocation(Object.class, "methodName", Method.class, Collections.emptyList(), null, null, null, null);
        invocationMatcher = new InvocationMatcher(invocation);
    }

    @Test
    public void testGetMethod() {
        Method expectedMethod = Method.class;
        Method actualMethod = invocationMatcher.getMethod();
        assertSame(expectedMethod, actualMethod);
    }

    @Test
    public void testGetInvocation() {
        Invocation expectedInvocation = new Invocation(Object.class, "methodName", Method.class, Collections.emptyList(), null, null, null, null);
        Invocation actualInvocation = invocationMatcher.getInvocation();
        assertEquals(expectedInvocation, actualInvocation);
    }

    @Test
    public void testGetMatchers() {
        List<Matcher> expectedMatchers = Collections.emptyList();
        List<Matcher> actualMatchers = invocationMatcher.getMatchers();
        assertEquals(expectedMatchers, actualMatchers);
    }

    @Test
    public void testToString() {
        PrintableInvocation printableInvocation = new PrintableInvocation() {
            @Override
            public Location getLocation() {
                return null;
            }

            @Override
            public String toString() {
                return "mockObject.methodName()";
            }
        };

        invocationMatcher = new InvocationMatcher(new Invocation(Object.class, "methodName", Method.class, Collections.emptyList(), null, null, null, null), Arrays.asList(CoreMatchers.is(printableInvocation)));
        String expectedString = "mockObject.methodName()";
        String actualString = invocationMatcher.toString();
        assertEquals(expectedString, actualString);
    }

    @Test
    public void testMatches() {
        Invocation invocation = new Invocation(Object.class, "methodName", Method.class, Collections.emptyList(), null, null, null, null);
        assertTrue(invocationMatcher.matches(invocation));
    }

    @Test
    public void testHasSimilarMethod_withOverloadedMethod() {
        Invocation candidate = new Invocation(Object.class, "methodName", Method.class, Collections.singletonList("arg1"), null, null, null, null);
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethod_withSameMethod() {
        Invocation candidate = new Invocation(Object.class, "methodName", Method.class, Collections.emptyList(), null, null, null, null);
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethod_withDifferentMethod() {
        Invocation candidate = new Invocation(Object.class, "differentMethodName", Method.class, Collections.emptyList(), null, null, null, null);
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethod_withSameMethodAndSameArguments() {
        Invocation candidate = new Invocation(Object.class, "methodName", Method.class, Collections.emptyList(), null, null, null, null);
        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSameMethod() {
        Invocation candidate = new Invocation(Object.class, "methodName", Method.class, Collections.emptyList(), null, null, null, null);
        assertTrue(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testHasSameMethod_withDifferentMethod() {
        Invocation candidate = new Invocation(Object.class, "differentMethodName", Method.class, Collections.emptyList(), null, null, null, null);
        assertFalse(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testGetLocation() {
        Location expectedLocation = null;
        Location actualLocation = invocationMatcher.getLocation();
        assertEquals(expectedLocation, actualLocation);
    }

    @Test
    public void testToStringWithPrintSettings() {
        PrintSettings printSettings = new PrintSettings();
        String expectedString = "mockObject.methodName()";
        String actualString = invocationMatcher.toString(printSettings);
        assertEquals(expectedString, actualString);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = new Invocation(Object.class, "methodName", Method.class, Collections.singletonList("arg1"), null, null, null, null);

        Matcher matcher = CoreMatchers.anything();
        invocationMatcher = new InvocationMatcher(invocation, Collections.singletonList(matcher));
        invocationMatcher.captureArgumentsFrom(invocation);

        assertEquals("arg1", matcher.toString());
    }

    @Test
    public void testCreateFrom() {
        Invocation invocation = new Invocation(Object.class, "methodName", Method.class, Collections.emptyList(), null, null, null, null);
        List<Invocation> invocations = Collections.singletonList(invocation);

        List<InvocationMatcher> expectedInvocationMatchers = Collections.singletonList(new InvocationMatcher(invocation));
        List<InvocationMatcher> actualInvocationMatchers = InvocationMatcher.createFrom(invocations);

        assertEquals(expectedInvocationMatchers, actualInvocationMatchers);
    }

}

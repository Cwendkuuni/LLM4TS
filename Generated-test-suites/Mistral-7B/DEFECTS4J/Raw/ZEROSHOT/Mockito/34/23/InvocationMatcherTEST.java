import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.CapturesArguments;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.Location;
import org.mockito.internal.matchers.ArgumentsComparator;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.stub.Stub;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;
    private Invocation invocation;
    private List<Matcher> matchers;
    private Stub mock;

    @Before
    public void setUp() {
        mock = mock(Object.class);
        invocation = new Invocation(mock, new Method("testMethod", String.class, int.class), new Object[]{"arg1", 1}, new Location("InvocationMatcherTest.java", 1));
        matchers = Collections.singletonList(is("expectedValue"));
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void testGetMethod() {
        Method method = invocationMatcher.getMethod();
        assertEquals(method.getName(), "testMethod");
        assertEquals(method.getParameterTypes()[0].getTypeName(), "java.lang.String");
        assertEquals(method.getParameterTypes()[1].getTypeName(), "int");
    }

    @Test
    public void testGetInvocation() {
        Invocation invocationFromMatcher = invocationMatcher.getInvocation();
        assertEquals(invocationFromMatcher, invocation);
    }

    @Test
    public void testGetMatchers() {
        List<Matcher> matchersFromMatcher = invocationMatcher.getMatchers();
        assertEquals(matchersFromMatcher, matchers);
    }

    @Test
    public void testToString() {
        String toString = invocationMatcher.toString();
        assertTrue(toString.contains("InvocationMatcher{"));
        assertTrue(toString.contains("this.invocation=" + invocation));
        assertTrue(toString.contains("this.matchers=" + matchers));
    }

    @Test
    public void testMatches() {
        Invocation actual = new Invocation(mock, new Method("testMethod", String.class, int.class), new Object[]{"arg1", 1}, new Location("InvocationMatcherTest.java", 2));
        InvocationMatcher actualMatcher = new InvocationMatcher(actual, Collections.emptyList());

        assertTrue(invocationMatcher.matches(actualMatcher));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Object[] actualArgs = new Object[]{"actualArg1", 2};
        Invocation actual = new Invocation(mock, new Method("testMethod", String.class, int.class), actualArgs, new Location("InvocationMatcherTest.java", 3));

        assertTrue(invocationMatcher.safelyArgumentsMatch(actualArgs));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation candidate = new Invocation(mock, new Method("testMethod", String.class, int.class), new Object[]{"arg1", 1}, new Location("InvocationMatcherTest.java", 4));

        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSameMethod() {
        Invocation candidate = new Invocation(mock, new Method("testMethod", String.class, int.class), new Object[]{"arg1", 1}, new Location("InvocationMatcherTest.java", 5));

        assertTrue(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testGetLocation() {
        Location location = invocationMatcher.getLocation();
        assertEquals(location.getClassName(), "InvocationMatcherTest");
        assertEquals(location.getLineNumber(), 1);
    }

    @Test
    public void testToStringWithPrintSettings() {
        PrintSettings printSettings = new PrintSettings();
        String toString = invocationMatcher.toString(printSettings);
        assertTrue(toString.contains("InvocationMatcher{"));
        assertTrue(toString.contains("toString = " + invocation));
        assertTrue(toString.contains("matchers = " + matchers));
    }

    @Test
    public void testCaptureArgumentsFromInvocation() {
        Invocation captureInvocation = new Invocation(mock, new Method("testMethod", String.class, int.class), new Object[]{"arg1", 1}, new Location("InvocationMatcherTest.java", 6));

        invocationMatcher.captureArgumentsFrom(captureInvocation);

        CapturesArguments capturesArguments = (CapturesArguments) matchers.get(0);
        Object capturedArgument = capturesArguments.getValue();
        assertEquals(capturedArgument, captureInvocation.getArguments()[0]);
    }

    @Test
    public void testHasDifferentMethod() {
        Invocation candidate = new Invocation(mock, new Method("differentMethod", String.class), new Object[]{"arg1"}, new Location("InvocationMatcherTest.java", 7));

        assertFalse(invocationMatcher.matches(candidate));
    }

    @Test
    public void testHasDifferentMock() {
        Object mock2 = mock(Object.class);
        Invocation candidate = new Invocation(mock2, new Method("testMethod", String.class, int.class), new Object[]{"arg1", 1}, new Location("InvocationMatcherTest.java", 8));

        assertFalse(invocationMatcher.matches(candidate));
    }

    @Test
    public void testHasOverloadedButDifferentArguments() {
        Invocation candidate = new Invocation(mock, new Method("testMethod", String.class), new Object[]{"arg1"}, new Location("InvocationMatcherTest.java", 9));

        assertFalse(invocationMatcher.matches(candidate));
    }
}
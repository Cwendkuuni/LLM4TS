import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.CapturesArguments;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.Location;
import org.mockito.internal.matchers.ArgumentsComparator;
import org.mockito.internal.reporting.PrintSettings;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class InvocationMatcherTest {

    private InvocationMatcher matcher;
    private Invocation invocation;
    private List<Matcher> matchers;
    private Object[] arguments;
    private Location location;

    @Before
    public void setUp() {
        ArgumentsComparator argumentsComparator = new ArgumentsComparator();
        Class<?> clazz = InvocationMatcher.class;
        matchers = Arrays.asList(new CapturesArguments(), new CapturesArguments());
        invocation = mock(Invocation.class);
        location = new Location("TestClass.java", 10, 20);
        matcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void testConstructorWithMatchers() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertEquals(invocationMatcher.getMatchers(), matchers);
    }

    @Test
    public void testConstructorWithoutMatchers() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testGetMethod() {
        Method method = invocation.getMethod();
        Method methodFromMatcher = matcher.getMethod();
        assertEquals(method, methodFromMatcher);
    }

    @Test
    public void testGetInvocation() {
        Invocation invocationFromMatcher = matcher.getInvocation();
        assertEquals(invocation, invocationFromMatcher);
    }

    @Test
    public void testGetMatchers() {
        List<Matcher> matchersFromMatcher = matcher.getMatchers();
        assertEquals(matchersFromMatcher, matchers);
    }

    @Test
    public void testToString() {
        String expected = "InvocationMatcher{" +
                "invocation=InvocationProxy{" +
                "target=MockFor<TestInterface>{" +
                "invocation=org.mockito.internal.invocation.Invocation{" +
                "method=public void testMethod(java.lang.String, java.lang.String)" +
                "arguments=[], location=Location{lineNumber=-1, methodName=-1, className=-1}}," +
                "matchers=[CapturesArguments@3992c273, CapturesArguments@4106c916}]," +
                "serialVersionUID=3599181513074817216}";
        String actual = matcher.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testMatches() {
        Invocation anotherInvocation = mock(Invocation.class);
        Matcher anotherMatcher = mock(Matcher.class);
        InvocationMatcher anotherMatcherWithInvocation = new InvocationMatcher(anotherInvocation, Arrays.asList(anotherMatcher));
        assertTrue(matcher.matches(invocation));
        assertFalse(matcher.matches(anotherInvocation));
    }

    @Test(expected = AssertionError.class)
    public void testSafelyArgumentsMatchWithDifferentArgumentTypes() {
        Object[] arguments =new Object[] {1, "abc"};
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        invocationMatcher.safelyArgumentsMatch(arguments);
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation anotherInvocation = mock(Invocation.class);
        Method anotherMethod = mock(Method.class);
        anotherInvocation.setMethod(anotherMethod);
        InvocationMatcher anotherMatcher = new InvocationMatcher(anotherInvocation);
        Method method = invocation.getMethod();
        assertTrue(matcher.hasSimilarMethod(anotherMatcher));
        assertFalse(matcher.hasSimilarMethod(anotherMatcherWithDifferentMethod()));
    }

    @Test
    public void testHasSameMethod() {
        Invocation anotherInvocation = mock(Invocation.class);
        Method anotherMethod = mock(Method.class);
        anotherInvocation.setMethod(anotherMethod);

        assertTrue(matcher.hasSameMethod(anotherMatcherWithTheSameMethod()));
        assertFalse(matcher.hasSameMethod(anotherMatcherWithDifferentMethod()));
    }

    @Test
    public void testGetLocation() {
        assertEquals(location, matcher.getLocation());
    }

    @Test
    public void testToStringWithPrintSettings() {
        PrintSettings printSettings = new PrintSettings();
        String expected = "InvocationMatcher{" +
                "invocation=InvocationProxy{" +
                "target=MockFor<TestInterface>{" +
                "invocation=org.mockito.internal.invocation.Invocation{" +
                "method=public void testMethod(java.lang.String, java.lang.String)" +
                "arguments=[], location=Location{lineNumber=-1, methodName=-1, className=-1}}," +
                "matchers=[CapturesArguments@3992c273, CapturesArguments@4106c916}]," +
                "serialVersionUID=3599181513074817216}" +
                "\n" +
                "Method: public void testMethod(java.lang.String, java.lang.String)\n" +
                "Location:\n" +
                "TestClass.java:10:20\n";
        String actual = matcher.toString(printSettings);
        assertEquals(expected, actual);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Object[] arguments = {1, "abc"};
        matcher.captureArgumentsFrom(invocation);
        Matcher matcher1 = matchers.get(0);
        Matcher matcher2 = matchers.get(1);
        assertTrue(matcher1 instanceof CapturesArguments);
        assertTrue(matcher2 instanceof CapturesArguments);
        CapturesArguments capturesArguments1 = (CapturesArguments) matcher1;
        CapturesArguments capturesArguments2 = (CapturesArguments) matcher2;
        Object capturedArgument1 = capturesArguments1.getValue();
        Object capturedArgument2 = capturesArguments2.getValue();
        assertEquals(arguments[0], capturedArgument1);
        assertEquals(arguments[1], capturedArgument2);
    }

    private InvocationMatcher anotherMatcherWithTheSameMethod() {
        Invocation anotherInvocation = mock(Invocation.class);
        Method anotherMethod = mock(Method.class);
        anotherInvocation.setMethod(method);
        return new InvocationMatcher(anotherInvocation);
    }

    private InvocationMatcher anotherMatcherWithDifferentMethod() {
        Invocation anotherInvocation = mock(Invocation.class);
        Method anotherMethod = mock(Method.class);
        anotherInvocation.setMethod(new Method("differentMethod", new Class[0]));
        return new InvocationMatcher(anotherInvocation);
    }

    private void assertEquals(Object expected, Object actual) {
        if (expected == null) {
            assertTrue(actual == null);
        } else if (expected.getClass().isInstance(actual)) {
            assertEquals(expected.hashCode(), actual.hashCode());
        } else if (expected.getClass() == String.class && actual.getClass() == String.class) {
            assertEquals(expected.toString(), actual.toString());
        } else {
            assertThat(expected, instanceOf(expected.getClass()));
            assertThat(actual, instanceOf(actual.getClass()));
            assertEquals(expected.toString(), actual.toString());
        }
    }
}
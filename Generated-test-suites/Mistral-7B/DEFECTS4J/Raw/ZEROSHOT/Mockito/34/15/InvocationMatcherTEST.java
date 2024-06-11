import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.CapturesArguments;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.Location;
import org.mockito.internal.invocation.PrintSettings;
import org.mockito.internal.matchers.ArgumentsComparator;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.internal.reporting.PrintableInvocation;
import org.mockito.mock.MockitoCore;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;

public class InvocationMatcherTest {

    private InvocationMatcher matcher;
    private Invocation invocation;
    private MockitoCore mockitoCore;

    @Before
    public void setUp() {
        mockitoCore = new MockitoCore();
        Method method = mockitoCore.getMockType().getMethod("methodName", String.class);
        Object[] arguments = new Object[]{new Location(1, 1), "test"};
        invocation = new Invocation(new Object() {
        }, method, arguments, new Location(2, 2));

        List<Matcher> matchers = Arrays.asList(is("test"), is(sameInstance(invocation.getArguments()[0])));
        matcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void testConstructorWithInvocationAndMatchers() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertThat(invocationMatcher.getInvocation(), sameInstance(invocation));
        assertThat(invocationMatcher.getMatchers(), sameInstance(matchers));
    }

    @Test
    public void testConstructorWithInvocationAndEmptyMatchersList() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertThat(invocationMatcher.getInvocation(), sameInstance(invocation));
        assertThat(invocationMatcher.getMatchers(), sameInstance(Collections.emptyList()));
    }

    @Test
    public void testGetMethod() {
        Method method = matcher.getMethod();
        assertThat(method, sameInstance(invocation.getMethod()));
    }

    @Test
    public void testGetInvocation() {
        Invocation actualInvocation = new Invocation(new Object() {
        }, method, invocation.getArguments(), new Location(3, 3));
        InvocationMatcher actualMatcher = new InvocationMatcher(actualInvocation);
        Invocation actual = actualMatcher.getInvocation();
        assertThat(actual, sameInstance(actualInvocation));
    }

    @Test
    public void testGetMatchers() {
        List<Matcher> actualMatchers = matcher.getMatchers();
        assertThat(actualMatchers, sameInstance(matchers));
    }

    @Test
    public void testToString() {
        String actualToString = matcher.toString();
        String expectedToString = invocation.toString(matchers, new PrintSettings());
        assertThat(actualToString, is(expectedToString));
    }

    @Test
    public void testMatches() {
        Invocation actualInvocation = new Invocation(invocation.getMock(), method, invocation.getArguments(), new Location(4, 4));
        InvocationMatcher actualMatcher = new InvocationMatcher(actualInvocation);
        boolean result = matcher.matches(actualMatcher);
        boolean expectedResult = invocation.getMock().equals(actualInvocation.getMock())
                && matcher.hasSameMethod(actualMatcher)
                && new ArgumentsComparator().argumentsMatch(matcher, actualMatcher.getArguments());
        assertThat(result, is(expectedResult));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Object[] actualArguments = new Object[]{new Location(5, 5), "test2"};
        boolean result = matcher.safelyArgumentsMatch(actualArguments);
        boolean expectedResult = new ArgumentsComparator().argumentsMatch(matcher, actualArguments);
        assertThat(result, is(expectedResult));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation candidate = new Invocation(invocation.getMock(), mockitoCore.getMockType().getMethod("anotherMethodName", String.class), new Object[0], new Location(6, 6));
        boolean result = matcher.hasSimilarMethod(new InvocationMatcher(candidate));
        boolean expectedResult = !matcher.hasSameMethod(new InvocationMatcher(candidate))
                && new ArgumentsComparator().argumentsMatch(matcher, candidate.getArguments());
        assertThat(result, is(expectedResult));
    }

    @Test
    public void testHasSameMethod() {
        Invocation candidate = new Invocation(invocation.getMock(), method, new Object[0], new Location(7, 7));
        boolean result = matcher.hasSameMethod(new InvocationMatcher(candidate));
        boolean expectedResult = invocation.getMethod().equals(candidate.getMethod());
        assertThat(result, is(expectedResult));
    }

    @Test
    public void testGetLocation() {
        Location actualLocation = matcher.getLocation();
        assertThat(actualLocation, sameInstance(invocation.getLocation()));
    }

    @Test(expected = AssertionError.class)
    public void testToStringWithNullPrintSettings() {
        matcher.toString((PrintSettings) null);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        CapturesArguments capturesArguments = mock(CapturesArguments.class);
        matcher.captureArgumentsFrom(invocation);
        Object capturedArgument = capturesArguments.getCaptured();
        assertThat(capturedArgument, sameInstance(invocation.getArguments()[0]));
    }

    @Test
    public void testHasSameMethodWithDifferentInvocationsOnSameMock() {
        Invocation candidate = new Invocation(invocation.getMock(), method, new Object[0], new Location(8, 8));
        boolean result = matcher.hasSameMethod(new InvocationMatcher(candidate));
        boolean expectedResult = invocation.getMethod().equals(candidate.getMethod());
        assertThat(result, is(expectedResult));
    }

    @Test
    public void testToStringWithPrintSettings() {
        PrintSettings printSettings = new PrintSettings();
        String actualToString = matcher.toString(printSettings);
        String expectedToString = invocation.toString(matchers, printSettings);
        assertThat(actualToString, is(expectedToString));
    }
}
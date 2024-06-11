import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsComparator;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.stub.Stubber;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;
    private Invocation invocation;
    private List<Matcher> matchers;
    private Stubber stubber;

    @Before
    public void setUp() {
        stubber = mock(Stubber.class);
        invocation = new InvocationOnMock(stubber.createMock(Object.class), new Method(Object.class.getDeclaredMethods()[0]), new Location(1, 1), new Object[]{});
        matchers = Arrays.asList(eq("expectedValue"), is(any(Integer.class)));
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void testGetMethod() {
        Method expectedMethod = invocationMatcher.getMethod();
        Method actualMethod = invocation.getMethod();

        assertThat(expectedMethod, is(equalTo(actualMethod)));
    }

    @Test
    public void testGetInvocation() {
        Invocation expectedInvocation = invocationMatcher.getInvocation();
        Invocation actualInvocation = invocation;

        assertThat(expectedInvocation, is(equalTo(actualInvocation)));
    }

    @Test
    public void testGetMatchers() {
        List<Matcher> expectedMatchers = invocationMatcher.getMatchers();
        List<Matcher> actualMatchers = matchers;

        assertThat(expectedMatchers, is(equalTo(actualMatchers)));
    }

    @Test
    public void testToString() {
        String expectedToString = invocation.toString(matchers, new PrintSettings());
        String actualToString = invocationMatcher.toString();

        assertThat(expectedToString, is(equalTo(actualToString)));
    }

    @Test
    public void testMatches() {
        InvocationMatcher anotherInvocationMatcher = new InvocationMatcher(invocation);
        boolean expectedMatches = invocationMatcher.matches(anotherInvocationMatcher);
        boolean actualMatches = invocationMatcher.matches(anotherInvocationMatcher);

        assertThat(expectedMatches, is(equalTo(actualMatches)));
        assertThat(expectedMatches, is(true));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Object[] actualArguments = new Object[]{1, "test"};
        boolean expectedResult = new ArgumentsComparator().argumentsMatch(invocationMatcher, actualArguments);
        boolean actualResult = invocationMatcher.safelyArgumentsMatch(actualArguments);

        assertThat(expectedResult, is(equalTo(actualResult)));
        assertThat(expectedResult, is(true));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation anotherInvocation = new InvocationOnMock(stubber.createMock(Object.class), new Method(Object.class.getDeclaredMethods()[1]), new Location(2, 2), new Object[]{});
        boolean expectedResult = invocationMatcher.hasSimilarMethod(new InvocationMatcher(anotherInvocation));
        boolean actualResult = invocationMatcher.hasSimilarMethod(new InvocationMatcher(anotherInvocation));

        assertThat(expectedResult, is(equalTo(actualResult)));
        assertThat(expectedResult, is(false));
    }

    @Test
    public void testHasSameMethod() {
        Invocation anotherInvocation = new InvocationOnMock(stubber.createMock(Object.class), new Method(invocation.getMethod()), new Location(2, 2), new Object[]{});
        boolean expectedResult = invocationMatcher.hasSameMethod(new InvocationMatcher(anotherInvocation));
        boolean actualResult = invocationMatcher.hasSameMethod(new InvocationMatcher(anotherInvocation));

        assertThat(expectedResult, is(equalTo(actualResult)));
        assertThat(expectedResult, is(true));
    }

    @Test
    public void testToStringWithPrintSettings() {
        PrintSettings printSettings = new PrintSettings();
        String expectedToString = invocation.toString(matchers, printSettings);
        String actualToString = invocationMatcher.toString(printSettings);

        assertThat(expectedToString, is(equalTo(actualToString)));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        CapturesArguments capturesArgumentsMock = mock(CapturesArguments.class);
        Object[] invocationArguments = new Object[]{1, "test"};

        invocationMatcher.captureArgumentsFrom(invocation);
        capturesArgumentsMock.captureFrom(invocationArguments[0]);

        assertEquals(capturesArgumentsMock.getValue(), invocationArguments[0]);
    }
}
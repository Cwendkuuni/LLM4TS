import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsComparator;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.mock.MockitoMock;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class InvocationMatcherTest {

    private InvocationMatcher matcher;
    private Invocation invocation;
    private Invocation candidate;
    private ArgumentsComparator argumentsComparator;

    @Before
    public void setUp() {
        invocation = mock(Invocation.class);
        candidate = mock(Invocation.class);
        argumentsComparator = spy(new ArgumentsComparator());

        matcher = new InvocationMatcher(invocation, Collections.emptyList());
    }

    @Test
    public void testGetMethod() {
        Method method = invocation.getMethod();
        Method methodFromMatcher = matcher.getMethod();

        assertThat(method, is(methodFromMatcher));
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = matcher.getInvocation();

        assertThat(this.invocation, is(invocation));
    }

    @Test
    public void testGetMatchers() {
        List<Matcher> matchers = matcher.getMatchers();

        assertThat(matchers, notNullValue());
        assertThat(matchers, is(Collections.emptyList()));
    }

    @Test
    public void testToString() {
        String printSettingsString = "PrintSettings{printClass=true, printMethodName=true, printArguments=false}";
        String expected = "InvocationMatcher[this = " + invocation.getMock() + ", method = " + invocation.getMethod().getName() + "()]";

        String printSettings = new PrintSettings();
        printSettings.setPrintClass(true);
        printSettings.setPrintMethodName(true);
        printSettings.setPrintArguments(false);

        String toString = matcher.toString(printSettings);
        String toStringFromMatcher = matcher.toString();

        assertThat(toStringFromMatcher, is(expected));
        assertThat(toString, is(expected));
    }

    @Test
    public void testMatches() {
        Matcher matcher = is(1L);

        InvocationMatcher matcherWithMatcher = new InvocationMatcher(invocation, Collections.singletonList(matcher));

        boolean matches = matcherWithMatcher.matches(candidate);

        assertThat(matches, is(false));

        matcher = spy(matcher);
        ((CapturesArguments) matcher).captureFrom(candidate.getArguments()[0]);

        matcherWithMatcher = new InvocationMatcher(invocation, Collections.singletonList(matcher));

        matches = matcherWithMatcher.matches(candidate);

        assertThat(matches, is(true));
    }

    @Test
    public void testSafelyArgumentsMatch() throws Exception {
        Object[] actualArgs = new Object[]{1, 2, 3};

        boolean matches = argumentsComparator.argumentsMatch(matcher, actualArgs);

        assertThat(matches, is(false));
    }

    @Test
    public void testHasSameMethod() {
        Method method1 = invocation.getMethod();
        Method method2 = candidate.getMethod();

        Matcher matcher = is(method1);

        InvocationMatcher matcherWithMatcher = new InvocationMatcher(invocation, Collections.singletonList(matcher));

        boolean hasSameMethod = matcherWithMatcher.hasSameMethod(candidate);

        assertThat(hasSameMethod, is(false));

        matcher = is(method1.getName());

        InvocationMatcher matcherWithNameMatcher = new InvocationMatcher(invocation, Collections.singletonList(matcher));

        hasSameMethod = matcherWithNameMatcher.hasSameMethod(candidate);

        assertThat(hasSameMethod, is(true));
    }

    @Test
    public void testToStringWithPrintSettings() {
        String printSettingsString = "PrintSettings{printClass=true, printMethodName=true, printArguments=false}";
        String expected = "InvocationMatcher[this = " + invocation.getMock() + ", method = " + invocation.getMethod().getName() + "()]";

        String printSettingsString1 = "PrintSettings{printClass=true, printMethodName=true, printArguments=true}";
        String expected1 = "InvocationMatcher[this = " + invocation.getMock() + ", method = " + invocation.getMethod().getName() + ", arguments = [1, 2, 3]]";

        PrintSettings printSettings = new PrintSettings();
        printSettings.setPrintClass(true);
        printSettings.setPrintMethodName(true);
        printSettings.setPrintArguments(false);

        String toString = matcher.toString(printSettings);
        String toStringFromMatcher = matcher.toString();
        String toStringWithPrintSettings = matcher.toString(printSettings);
        String toStringWithPrintSettingsAndArgs = matcher.toString(printSettings1);

        assertThat(toStringFromMatcher, is(expected));
        assertThat(toString, is(expected));
        assertThat(toStringWithPrintSettings, is(expected));
        assertThat(toStringWithPrintSettingsAndArgs, is(expected1));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Matcher argMatcher = is("test");
        Object[] testArguments = new Object[]{"arg1", "arg2"};

        InvocationMatcher matcher = new InvocationMatcher(invocation);
        matcher.captureArgumentsFrom(invocation);

        assertThat(matcher.getMatchers().size(), is(1));
        assertThat(matcher.getMatchers().get(0), is(argMatcher));

        InvocationMatcher matcherWithArgs = new InvocationMatcher(invocation);
        matcherWithArgs.captureArgumentsFrom(invocation);
        matcherWithArgs.captureArgumentsFrom(invocation);

        CapturesArguments capturesArguments = (CapturesArguments) matcherWithArgs.getMatchers().get(0);

        Object capturedArgument = capturesArguments.getValue();
        Method getValueMethod = capturesArguments.getClass().getDeclaredMethod("getValue");
        getValueMethod.setAccessible(true);

        Object[] arguments = (Object[]) getValueMethod.invoke(capturesArguments);

        assertThat(arguments[0], is(testArguments[0]));
        assertThat(arguments[1], is(testArguments[1]));
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new LinkedList<>();
        invocations.add(invocation);
        invocations.add(candidate);

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        InvocationMatcher invocationMatcher1 = invocationMatchers.get(0);
        InvocationMatcher invocationMatcher2 = invocationMatchers.get(1);

        Method method = invocation.getMethod();
        Method methodFromFirstMatcher = invocationMatcher1.getMethod();
        Method methodFromSecondMatcher = invocationMatcher2.getMethod();

        assertThat(method, is(methodFromFirstMatcher));
        assertThat(method, is(methodFromSecondMatcher));
    }
}
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsComparator;
import org.mockito.internal.util.collections.LinkedHashSet;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.mock.MockComponent;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class InvocationMatcherTest {

    private InvocationMatcher matcher;
    private Invocation invocation;
    private MockComponent mock;
    private ArgumentsComparator argumentsComparator;

    @Before
    public void setup() {
        mock = mock(MockComponent.class);
        argumentsComparator = spy(new ArgumentsComparator());
        invocation = new Invocation(mock, new Location(1, 1), new Method(Object.class, new Class[0]), new Object[0]);
        matcher = new InvocationMatcher(invocation);
    }

    @Test
    public void testConstructorWithInvocation() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertThat(invocationMatcher.getInvocation(), is(invocation));
        assertThat(invocationMatcher.getMatchers(), not(hasSize(1)));
    }

    @Test
    public void testConstructorWithInvocationAndMatchers() {
        List<Matcher> matchers = new ArrayList<>();
        matcher = new InvocationMatcher(invocation, matchers);
        assertThat(invocationMatcher.getInvocation(), is(invocation));
        assertThat(invocationMatcher.getMatchers(), is(matchers));
    }

    @Test
    public void testGetMethod() {
        Method method = matcher.getMethod();
        assertThat(method, is(invocation.getMethod()));
    }

    @Test
    public void testGetInvocation() {
        Invocation actualInvocation = mock(Invocation.class);
        assertThat(matcher.getInvocation(), is(invocation));
        matcher.captureArgumentsFrom(actualInvocation);
        verify(actualInvocation).getArguments();
    }

    @Test
    public void testGetMatchers() {
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher matcherWithMatchers = new InvocationMatcher(invocation, matchers);
        assertThat(matcherWithMatchers.getMatchers(), is(matchers));
    }

    @Test
    public void testToString() {
        String expected = "InvocationMatcher{invocation=InvocationProxy{mock=MockComponent@27a2e6e}, matchers=[]}";
        String actual = matcher.toString();
        assertThat(actual, is(expected));
    }

    @Test
    public void testMatches() {
        Invocation actualInvocation = new Invocation(mock, new Location(1, 1), invocation.getMethod(), new Object[0]);
        InvocationMatcher actualMatcher = new InvocationMatcher(actualInvocation);
        InvocationMatcher expectedMatcher = new InvocationMatcher(invocation);
        assertThat(expectedMatcher.matches(actualMatcher), is(true));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Object[] actualArgs = new Object[]{1, 2};
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean result = invocationMatcher.safelyArgumentsMatch(actualArgs);
        assertThat(result, is(true));
    }

    @Test(expected = Throwable.class)
    public void testSafelyArgumentsMatchWithThrowable() {
        Object[] actualArgs = new Object[]{1, new Throwable()};
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        invocationMatcher.safelyArgumentsMatch(actualArgs);
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation candidateInvocation = new Invocation(mock, new Location(1, 1), invocation.getMethod(), new Object[0]);
        InvocationMatcher candidateMatcher = new InvocationMatcher(candidateInvocation);
        boolean result = matcher.hasSimilarMethod(candidateMatcher);
        assertThat(result, is(true));
    }

    @Test
    public void testHasDissimilarMethod() {
        Method differentMethod = new Method(String.class, new Class[0]);
        Invocation candidateInvocation = new Invocation(mock, new Location(1, 1), differentMethod, new Object[0]);
        InvocationMatcher candidateMatcher = new InvocationMatcher(candidateInvocation);
        boolean result = matcher.hasSimilarMethod(candidateMatcher);
        assertThat(result, is(false));
    }

    @Test
    public void testToStringWithPrintSettings() {
        PrintSettings printSettings = new PrintSettings();
        String expected = "InvocationMatcher{invocation=InvocationProxy{mock=MockComponent@27a2e6e}, matchers=[]}";
        String actual = matcher.toString(printSettings);
        assertThat(actual, is(expected));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Object[] arguments = new Object[]{1, 2};
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(ArgumentsAre.theSameAs(arguments));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        invocationMatcher.captureArgumentsFrom(new Invocation(mock, new Location(1, 1), invocation.getMethod(), arguments));
        verify(argumentsComparator).argumentsMatch(invocationMatcher, arguments);
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new ArrayList<>();
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        assertThat(invocationMatchers, hasSize(0));
    }

    @Test
    public void testCreateFromWithInvocations() {
        List<Invocation> invocations = new ArrayList<>();
        Invocation invocation1 = new Invocation(mock, new Location(1, 1), new Method(Object.class, new Class[0]), new Object[0]);
        Invocation invocation2 = new Invocation(mock, new Location(1, 1), new Method(Object.class, new Class[0]), new Object[0]);
        invocations.add(invocation1);
        invocations.add(invocation2);
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        assertThat(invocationMatchers.size(), is(2));
    }
}

class ArgumentsAre implements Matcher<Object[]> {

    private Object[] expected;

    public ArgumentsAre(Object[] expected) {
        this.expected = expected;
    }

    @Override
    public boolean matches(Object argument) {
        return argumentsMatcher.argumentsMatch(argument, expected);
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(expected);
    }

    static class ArgumentsMatcher {

        public static boolean argumentsMatch(Object[] expected, Object[] actual) {
            return Arrays.deepEquals(expected, actual);
        }
    }

    static class ArgumentsComparator extends ArgumentsMatcher {

        public boolean argumentsMatch(InvocationMatcher expectedMatcher, Object[] actualArgs) {
            return argumentsMatch(expectedMatcher.getInvocation().getArguments(), actualArgs);
        }
    }
}
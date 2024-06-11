import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsComparator;
import org.mockito.internal.reporting.PrintSettings;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.internal.invocation.Invocation.invocationWith;

public class InvocationMatcherTest {

    private InvocationMatcher matcher;
    private Invocation invocation;
    private List<Matcher> matchers;
    private Method method;
    private Object[] arguments;
    private Invocation candidate;

    @Before
    public void setup() {
        this.invocation = invocationWith(Mockito.class, "testMethod", new Object[]{1, 2, 3});
        this.matchers = Collections.singletonList(is(1));
        this.matcher = new InvocationMatcher(this.invocation, this.matchers);
        this.method = this.invocation.getMethod();
        this.arguments = this.invocation.getArguments();
        this.candidate = invocationWith(Mockito.class, "testMethod", new Object[]{2, 2, 3});
    }

    @Test
    public void testConstructorWithMatchers() {
        InvocationMatcher matcher = new InvocationMatcher(this.invocation, this.matchers);
        assertThat(matcher.getMatchers(), is(this.matchers));
    }

    @Test
    public void testConstructorWithoutMatchers() {
        InvocationMatcher matcher = new InvocationMatcher(this.invocation);
        assertThat(matcher.getMatchers(), is(this.invocation.argumentsToMatchers()));
    }

    @Test
    public void testGetMethod() {
        Method expectedMethod = this.method;
        Method actualMethod = this.matcher.getMethod();
        assertThat(actualMethod, is(expectedMethod));
    }

    @Test
    public void testGetInvocation() {
        Invocation expectedInvocation = this.invocation;
        Invocation actualInvocation = this.matcher.getInvocation();
        assertThat(actualInvocation, is(expectedInvocation));
    }

    @Test
    public void testGetMatchers() {
        List<Matcher> expectedMatchers = this.matchers;
        List<Matcher> actualMatchers = this.matcher.getMatchers();
        assertThat(actualMatchers, is(expectedMatchers));
    }

    @Test
    public void testToString() {
        PrintSettings printSettings = new PrintSettings();
        String expectedString = this.invocation.toString(this.matchers, printSettings);
        String actualString = this.matcher.toString(printSettings);
        assertThat(actualString, is(expectedString));
    }

    @Test
    public void testMatches() {
        boolean expectedResult = this.invocation.getMock() == this.candidate.getMock()
                && this.method.equals(this.candidate.getMethod())
                && new ArgumentsComparator().argumentsMatch(this.matcher, this.candidate);
        boolean actualResult = this.matcher.matches(this.candidate);
        assertThat(actualResult, is(expectedResult));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Object[] candidateArguments = this.candidate.getArguments();
        boolean expectedResult = new ArgumentsComparator().argumentsMatch(this.matcher, candidateArguments);
        boolean actualResult = this.matcher.safelyArgumentsMatch(candidateArguments);
        assertThat(actualResult, is(expectedResult));
    }

    @Test
    public void testHasSimilarMethod() {
        boolean expectedResult = !this.method.getName().equals(this.candidate.getMethod().getName())
                && this.invocation.getMock() == this.candidate.getMock()
                && this.method.equals(this.candidate.getMethod())
                && new ArgumentsComparator().argumentsMatch(this.matcher, this.candidate.getArguments());
        boolean actualResult = this.matcher.hasSimilarMethod(this.candidate);
        assertThat(actualResult, is(expectedResult));
    }

    @Test
    public void testHasSameMethod() {
        boolean expectedResult = this.method.equals(this.candidate.getMethod());
        boolean actualResult = this.matcher.hasSameMethod(this.candidate);
        assertThat(actualResult, is(expectedResult));
    }

    @Test(expected = AssertionError.class)
    public void testHasSameMethodWithDifferentArguments() {
        this.matcher = new InvocationMatcher(this.invocation, Arrays.asList(is(1), is(2)));
        this.candidate = invocationWith(Mockito.class, "testMethod", new Object[]{1, 3});
        this.matcher.hasSameMethod(this.candidate);
    }

    @Test
    public void testGetLocation() {
        Location expectedLocation = this.invocation.getLocation();
        Location actualLocation = this.matcher.getLocation();
        assertThat(actualLocation, is(expectedLocation));
    }
}
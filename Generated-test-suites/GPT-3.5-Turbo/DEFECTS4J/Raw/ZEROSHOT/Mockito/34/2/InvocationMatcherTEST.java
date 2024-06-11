import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.internal.debugging.Location;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.ArgumentsComparator;
import org.mockito.internal.invocation.InvocationMatcher;
import java.lang.reflect.Method;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class InvocationMatcherTest {

    @Test
    public void testConstructor1() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assert(invocationMatcher.getMethod() == invocation.getMethod());
        assert(invocationMatcher.getInvocation() == invocation);
        assert(invocationMatcher.getMatchers() == matchers);
    }

    @Test
    public void testConstructor2() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assert(invocationMatcher.getMethod() == invocation.getMethod());
        assert(invocationMatcher.getInvocation() == invocation);
        assert(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testToString() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        String expected = invocation.toString(matchers, new PrintSettings());
        String actual = invocationMatcher.toString();

        assert(expected.equals(actual));
    }

    @Test
    public void testMatches() {
        Invocation invocation = new Invocation();
        Invocation actual = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        boolean expected = invocation.getMock().equals(actual.getMock())
                && invocationMatcher.hasSameMethod(actual)
                && new ArgumentsComparator().argumentsMatch(invocationMatcher, actual);
        boolean actual = invocationMatcher.matches(actual);

        assert(expected == actual);
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Invocation invocation = new Invocation();
        Object[] actualArgs = new Object[10];
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        boolean expected = false;
        try {
            expected = new ArgumentsComparator().argumentsMatch(invocationMatcher, actualArgs);
        } catch (Throwable t) {
            expected = false;
        }
        boolean actual = invocationMatcher.safelyArgumentsMatch(actualArgs);

        assert(expected == actual);
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = new Invocation();
        Invocation candidate = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        String wantedMethodName = invocation.getMethod().getName();
        String currentMethodName = candidate.getMethod().getName();
        boolean methodNameEquals = wantedMethodName.equals(currentMethodName);
        boolean isUnverified = !candidate.isVerified();
        boolean mockIsTheSame = invocation.getMock() == candidate.getMock();
        boolean methodEquals = invocationMatcher.hasSameMethod(candidate);

        boolean overloadedButSameArgs = !methodEquals && invocationMatcher.safelyArgumentsMatch(candidate.getArguments());

        boolean expected = methodNameEquals && isUnverified && mockIsTheSame && methodEquals && !overloadedButSameArgs;
        boolean actual = invocationMatcher.hasSimilarMethod(candidate);

        assert(expected == actual);
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation = new Invocation();
        Invocation candidate = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        boolean expected = invocation.getMethod().equals(candidate.getMethod());
        boolean actual = invocationMatcher.hasSameMethod(candidate);

        assert(expected == actual);
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Location expected = invocation.getLocation();
        Location actual = invocationMatcher.getLocation();

        assert(expected == actual);
    }

    @Test
    public void testToStringWithPrintSettings() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = new ArrayList<>();
        PrintSettings printSettings = new PrintSettings();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        String expected = invocation.toString(matchers, printSettings);
        String actual = invocationMatcher.toString(printSettings);

        assert(expected.equals(actual));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = new Invocation();
        Invocation i = new Invocation();
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        int k = 0;
        for (Matcher m : matchers) {
            if (m instanceof CapturesArguments) {
                ((CapturesArguments) m).captureFrom(i.getArguments()[k]);
            }
            k++;
        }

        invocationMatcher.captureArgumentsFrom(i);
    }

}
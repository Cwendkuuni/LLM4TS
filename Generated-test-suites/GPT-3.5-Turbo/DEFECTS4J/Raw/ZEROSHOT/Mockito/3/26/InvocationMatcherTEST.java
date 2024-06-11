import org.junit.*;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;

public class InvocationMatcherTest {

    @Test
    public void testGetMethod() {
        Method expectedMethod = mock(Method.class);
        List<Matcher> matchers = Collections.emptyList();
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Method actualMethod = invocationMatcher.getMethod();

        Assert.assertEquals(expectedMethod, actualMethod);
    }

    @Test
    public void testGetInvocation() {
        Invocation expectedInvocation = mock(Invocation.class);
        List<Matcher> matchers = Collections.emptyList();
        InvocationMatcher invocationMatcher = new InvocationMatcher(expectedInvocation, matchers);

        Invocation actualInvocation = invocationMatcher.getInvocation();

        Assert.assertEquals(expectedInvocation, actualInvocation);
    }

    @Test
    public void testGetMatchers() {
        List<Matcher> expectedMatchers = Arrays.asList(mock(Matcher.class), mock(Matcher.class));
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, expectedMatchers);

        List<Matcher> actualMatchers = invocationMatcher.getMatchers();

        Assert.assertEquals(expectedMatchers, actualMatchers);
    }

    @Test
    public void testToString() {
        String expectedString = "expected string";
        List<Matcher> matchers = Collections.emptyList();
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        PrintSettings printSettings = mock(PrintSettings.class);

        String actualString = invocationMatcher.toString();

        Assert.assertEquals(expectedString, actualString);
    }

    @Test
    public void testMatches() {
        boolean expectedMatches = true;
        List<Matcher> matchers = Collections.emptyList();
        Invocation invocation = mock(Invocation.class);
        Invocation actualInvocation = mock(Invocation.class);
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        boolean actualMatches = invocationMatcher.matches(actualInvocation);

        Assert.assertEquals(expectedMatches, actualMatches);
    }

    @Test
    public void testHasSimilarMethod() {
        boolean expectedHasSimilarMethod = true;
        List<Matcher> matchers = Collections.emptyList();
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        boolean actualHasSimilarMethod = invocationMatcher.hasSimilarMethod(candidate);

        Assert.assertEquals(expectedHasSimilarMethod, actualHasSimilarMethod);
    }

    @Test
    public void testHasSameMethod() {
        boolean expectedHasSameMethod = true;
        List<Matcher> matchers = Collections.emptyList();
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        boolean actualHasSameMethod = invocationMatcher.hasSameMethod(candidate);

        Assert.assertEquals(expectedHasSameMethod, actualHasSameMethod);
    }

    @Test
    public void testGetLocation() {
        Location expectedLocation = mock(Location.class);
        List<Matcher> matchers = Collections.emptyList();
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Location actualLocation = invocationMatcher.getLocation();

        Assert.assertEquals(expectedLocation, actualLocation);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        List<Matcher> matchers = Collections.emptyList();
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Invocation actualInvocation = mock(Invocation.class);

        invocationMatcher.captureArgumentsFrom(actualInvocation);
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = Arrays.asList(mock(Invocation.class), mock(Invocation.class));
        List<InvocationMatcher> expectedInvocationMatchers = Arrays.asList(mock(InvocationMatcher.class), mock(InvocationMatcher.class));

        List<InvocationMatcher> actualInvocationMatchers = InvocationMatcher.createFrom(invocations);

        Assert.assertEquals(expectedInvocationMatchers, actualInvocationMatchers);
    }

}
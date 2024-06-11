
import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.invocation.Invocation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class InvocationMatcherTest {

    @Test
    public void testGetMethod() {
        Method expectedMethod = new Object().getClass().getMethods()[0];
        InvocationInvocation invocation = new InvocationInvocation(new Object(), expectedMethod);
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        Method actualMethod = matcher.getMethod();

        Assert.assertEquals(expectedMethod, actualMethod);
    }

    @Test
    public void testGetInvocation() {
        InvocationInvocation expectedInvocation = new InvocationInvocation(new Object(), new Object().getClass().getMethods()[0]);
        InvocationMatcher matcher = new InvocationMatcher(expectedInvocation);

        Invocation actualInvocation = matcher.getInvocation();

        Assert.assertEquals(expectedInvocation, actualInvocation);
    }

    @Test
    public void testGetMatchers() {
        List<Matcher> expectedMatchers = new ArrayList<>();
        expectedMatchers.add(Matchers.anyInt());
        expectedMatchers.add(Matchers.anyString());
        InvocationInvocation invocation = new InvocationInvocation(new Object(), new Object().getClass().getMethods()[0]);
        InvocationMatcher matcher = new InvocationMatcher(invocation, expectedMatchers);

        List<Matcher> actualMatchers = matcher.getMatchers();

        Assert.assertEquals(expectedMatchers, actualMatchers);
    }

    @Test
    public void testToString() {
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(Matchers.anyInt());
        matchers.add(Matchers.anyString());
        InvocationInvocation invocation = new InvocationInvocation(new Object(), new Object().getClass().getMethods()[0]);
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        String actualString = matcher.toString();

        Assert.assertNotNull(actualString);
    }

    @Test
    public void testMatches() {
        InvocationInvocation invocation = new InvocationInvocation(new Object(), new Object().getClass().getMethods()[0]);
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        boolean actualResult = matcher.matches(invocation);

        Assert.assertTrue(actualResult);
    }

    @Test
    public void testHasSimilarMethod() {
        InvocationInvocation invocation = new InvocationInvocation(new Object(), new Object().getClass().getMethods()[0]);
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        boolean actualResult = matcher.hasSimilarMethod(invocation);

        Assert.assertTrue(actualResult);
    }

    @Test
    public void testHasSameMethod() {
        InvocationInvocation invocation = new InvocationInvocation(new Object(), new Object().getClass().getMethods()[0]);
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        boolean actualResult = matcher.hasSameMethod(invocation);

        Assert.assertTrue(actualResult);
    }

    @Test
    public void testGetLocation() {
        InvocationInvocation invocation = new InvocationInvocation(new Object(), new Object().getClass().getMethods()[0]);
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        Location actualLocation = matcher.getLocation();

        Assert.assertNotNull(actualLocation);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCaptureArgumentsFrom_VarArgs() {
        InvocationInvocation invocation = new InvocationInvocation(new Object(), new Object().getClass().getMethods()[0]);
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        matcher.captureArgumentsFrom(invocation);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        InvocationInvocation invocation = new InvocationInvocation(new Object(), new Object().getClass().getMethods()[0]);
        InvocationInvocation capturedInvocation = new InvocationInvocation(new Object(), new Object().getClass().getMethods()[1]);
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        matcher.captureArgumentsFrom(capturedInvocation);

        Assert.assertEquals(capturedInvocation.getArguments(), matcher.getMatchers());
    }

    @Test
    public void testCreateFrom() {
        InvocationInvocation invocation = new InvocationInvocation(new Object(), new Object().getClass().getMethods()[0]);
        List<Invocation> invocations = new ArrayList<>();
        invocations.add(invocation);

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        Assert.assertEquals(invocations.size(), invocationMatchers.size());
        Assert.assertTrue(invocationMatchers.contains(new InvocationMatcher(invocation)));
    }

}


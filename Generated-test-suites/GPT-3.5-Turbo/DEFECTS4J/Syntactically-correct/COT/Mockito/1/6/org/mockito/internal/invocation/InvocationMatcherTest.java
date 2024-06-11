import org.junit.Assert;
import org.junit.Test;
import org.mockito.invocation.Invocation;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InvocationMatcherTest {

    // Test getMethod() method
    @Test
    public void testGetMethod() {
        Method expectedMethod = MethodInvocation.class.getMethod("getMethod");
        Invocation invocation = new MethodInvocation(expectedMethod, null, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Method actualMethod = invocationMatcher.getMethod();
        Assert.assertEquals(expectedMethod, actualMethod);
    }

    // Test getInvocation() method
    @Test
    public void testGetInvocation() {
        Invocation expectedInvocation = new MethodInvocation(null, null, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(expectedInvocation);

        Invocation actualInvocation = invocationMatcher.getInvocation();
        Assert.assertEquals(expectedInvocation, actualInvocation);
    }

    // Test getMatchers() method
    @Test
    public void testGetMatchers() {
        List<Matcher> expectedMatchers = Collections.singletonList(Mockito.anyString());
        Invocation invocation = new MethodInvocation(null, null, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, expectedMatchers);

        List<Matcher> actualMatchers = invocationMatcher.getMatchers();
        Assert.assertEquals(expectedMatchers, actualMatchers);
    }

    // Test toString() method
    @Test
    public void testToString() {
        List<Matcher> matchers = Collections.singletonList(Mockito.anyString());
        Invocation invocation = new MethodInvocation(null, null, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        String expectedToString = new PrintSettings().print(matchers, invocation);
        String actualToString = invocationMatcher.toString();

        Assert.assertEquals(expectedToString, actualToString);
    }

    // Test matches() method
    @Test
    public void testMatches() {
        Invocation invocation1 = new MethodInvocation(null, null, null);
        Invocation invocation2 = new MethodInvocation(null, null, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        // Match
        boolean matches = invocationMatcher.matches(invocation2);
        Assert.assertTrue(matches);
    }

    // Test hasSimilarMethod() method
    @Test
    public void testHasSimilarMethod() throws NoSuchMethodException {
        Method method1 = MethodInvocation.class.getMethod("hasSimilarMethod", MethodInvocation.class);
        Method method2 = MethodInvocation.class.getMethod("matches", MethodInvocation.class);
        Invocation invocation1 = new MethodInvocation(method1, null, null);
        Invocation invocation2 = new MethodInvocation(method2, null, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        // Same method name, same mock, unverified, and different methods -> should return false
        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(invocation2);
        Assert.assertFalse(hasSimilarMethod);

        // Same method name, same mock, verified, and different methods -> should return false
        invocation2.markVerified();
        hasSimilarMethod = invocationMatcher.hasSimilarMethod(invocation2);
        Assert.assertFalse(hasSimilarMethod);

        // Same method name, different mock, unverified, and different methods -> should return false
        invocation2 = new MethodInvocation(method1, null, null);
        hasSimilarMethod = invocationMatcher.hasSimilarMethod(invocation2);
        Assert.assertFalse(hasSimilarMethod);

        // Same method name, same mock, unverified, same method -> should return true
        invocation2 = new MethodInvocation(method1, null, null);
        hasSimilarMethod = invocationMatcher.hasSimilarMethod(invocation2);
        Assert.assertTrue(hasSimilarMethod);
    }

    // Test hasSameMethod() method
    @Test
    public void testHasSameMethod() throws NoSuchMethodException {
        Method method1 = MethodInvocation.class.getMethod("hasSameMethod", MethodInvocation.class);
        Method method2 = MethodInvocation.class.getMethod("matches", MethodInvocation.class);
        Invocation invocation1 = new MethodInvocation(method1, null, null);
        Invocation invocation2 = new MethodInvocation(method2, null, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        // Same method name and same parameters -> should return true
        boolean hasSameMethod = invocationMatcher.hasSameMethod(invocation2);
        Assert.assertTrue(hasSameMethod);

        // Different method name -> should return false
        invocation2 = new MethodInvocation(null, null, null);
        hasSameMethod = invocationMatcher.hasSameMethod(invocation2);
        Assert.assertFalse(hasSameMethod);

        // Same method name but different parameters -> should return false
        invocation2 = new MethodInvocation(method1, new Object[]{1, 2}, null);
        hasSameMethod = invocationMatcher.hasSameMethod(invocation2);
        Assert.assertFalse(hasSameMethod);

        // Same method name, same parameters, but different number of parameters -> should return false
        invocation2 = new MethodInvocation(method1, new Object[]{1}, null);
        hasSameMethod = invocationMatcher.hasSameMethod(invocation2);
        Assert.assertFalse(hasSameMethod);
    }

    // Test getLocation() method
    @Test
    public void testGetLocation() {
        Location expectedLocation = new Location(null, 0);
        Invocation invocation = new MethodInvocation(null, null, expectedLocation);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Location actualLocation = invocationMatcher.getLocation();
        Assert.assertEquals(expectedLocation, actualLocation);
    }

    // Test captureArgumentsFrom() method
    @Test
    public void testCaptureArgumentsFrom() {
        // TODO: Implement test cases for captureArgumentsFrom() method
    }

    // Test createFrom() method
    @Test
    public void testCreateFrom() {
        // TODO: Implement test cases for createFrom() method
    }
}


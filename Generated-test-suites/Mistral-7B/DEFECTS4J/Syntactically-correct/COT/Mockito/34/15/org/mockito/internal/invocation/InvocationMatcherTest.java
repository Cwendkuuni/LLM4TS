import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsMatcher;
import org.mockito.internal.matchers.ArgumentsComparator;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InvocationMatcherTest {

    private Invocation invocation;
    private List<Matcher> matchers;
    private InvocationMatcher invocationMatcher;
    private Invocation otherInvocation;
    private Invocation otherInvocationWithSameMethod;
    private Invocation otherInvocationWithDifferentMethod;
    private Invocation unverifiedInvocation;
    private Invocation sameMockInvocation;
    private Invocation differentMockInvocation;

    @Before
    public void setup() {
        Class<?> testClass = getClass();
        Object testObject = new Object() {};

        try {
            Method method = testClass.getMethod("testMethod", int.class, String.class);
            invocation = new Invocation(new Location(testObject, 1, 1), method, new Object[]{1, "test"}, new Object[0]);
            matchers = Arrays.asList(new CapturesArguments(), new ArgumentsMatcher(new int[0], new String[0]));
            invocationMatcher = new InvocationMatcher(invocation, matchers);

            Method sameMethod = testClass.getMethod("testMethod", int.class, String.class);
            otherInvocation = new Invocation(new Location(testObject, 1, 1), sameMethod, new Object[]{1, "test"}, new Object[0]);

            Method differentMethod = testClass.getMethod("testMethod2", int.class, String.class);
            otherInvocationWithDifferentMethod = new Invocation(new Location(testObject, 1, 1), differentMethod, new Object[]{1, "test"}, new Object[0]);

            unverifiedInvocation = new Invocation(new Location(testObject, 1, 1), method, new Object[]{1, "test"}, new Object[0]);
            unverifiedInvocation.setVerified(false);

            sameMockInvocation = new Invocation(new Location(testObject, 1, 1), method, new Object[]{1, "test"}, new Object[0]);
            sameMockInvocation.setMock(invocation.getMock());

            differentMockInvocation = new Invocation(new Location(new Object() {}, 1, 1), method, new Object[]{1, "test"}, new Object[0]);
            differentMockInvocation.setMock(new Object() {});
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInvocationMatcherConstructionWithMatchers() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Assert.assertSame(invocation, invocationMatcher.getInvocation());
        Assert.assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testInvocationMatcherConstructionWithoutMatchers() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Assert.assertSame(invocation, invocationMatcher.getInvocation());
        Assert.assertEquals(Collections.emptyList(), invocationMatcher.getMatchers());
    }

    @Test
    public void testGetMethod() {
        Method method = invocationMatcher.getMethod();
        Assert.assertSame(invocation.getMethod(), method);
    }

    @Test
    public void testGetInvocation() {
        Assert.assertSame(invocation, invocationMatcher.getInvocation());
    }

    @Test
    public void testGetMatchers() {
        Assert.assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testToString() {
        String actual = invocationMatcher.toString();
        String expected = invocation.toString(matchers, new PrintSettings());
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMatches() {
        Invocation actual = new Invocation(new Location(testObject, 1, 1), method, new Object[]{1, "test"}, new Object[0]);
        Assert.assertTrue(invocationMatcher.matches(actual));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Object[] actualArgs = new Object[]{2, "test2"};
        Assert.assertFalse(invocationMatcher.safelyArgumentsMatch(actualArgs));
    }

    @Test
    public void testHasSimilarMethod() {
        Assert.assertTrue(invocationMatcher.hasSimilarMethod(otherInvocation));
    }

    @Test
    public void testHasSameMethod() {
        Assert.assertTrue(invocationMatcher.hasSameMethod(otherInvocation));
    }

    @Test
    public void testHasSameLocation() {
        Assert.assertTrue(invocationMatcher.getLocation().equals(otherInvocation.getLocation()));
    }

    @Test
    public void testToStringWithPrintSettings() {
        String expected = invocation.toString(matchers, new PrintSettings());
        String actual = invocationMatcher.toString(new PrintSettings());
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = AssertionError.class)
    public void testArgumentsDoNotMatch() {
        Invocation actual = new Invocation(new Location(testObject, 1, 1), method, new Object[]{2, "test2"}, new Object[0]);
        invocationMatcher.matches(actual);
    }

    @Test(expected = AssertionError.class)
    public void testDifferentMock() {
        invocationMatcher.matches(differentMockInvocation);
    }

    @Test
    public void testUnverifiedInvocationMatches() {
        Assert.assertTrue(invocationMatcher.matches(unverifiedInvocation));
    }

    @Test(expected = AssertionError.class)
    public void testDifferentMethod() {
        Invocation otherInvocationWithVerifiedMethod = new Invocation(new Location(testObject, 1, 1), differentMethod, new Object[]{1, "test"}, new Object[0]);
        otherInvocationWithVerifiedMethod.setVerified(true);
        Assert.assertFalse(invocationMatcher.matches(otherInvocationWithVerifiedMethod));
    }

    @Test(expected = AssertionError.class)
    public void testDifferentMethodWithArgumentsMatch() {
        Invocation otherInvocationWithVerifiedMethodAndMatchingArguments = new Invocation(new Location(testObject, 1, 1), differentMethod, new Object[]{1, "test"}, new Object[0]);
        otherInvocationWithVerifiedMethodAndMatchingArguments.setVerified(true);
        Assert.assertFalse(invocationMatcher.safelyArgumentsMatch(otherInvocationWithVerifiedMethodAndMatchingArguments.getArguments()));
        Assert.assertFalse(invocationMatcher.matches(otherInvocationWithVerifiedMethodAndMatchingArguments));
    }

    @Test(expected = AssertionError.class)
    public void testDifferentMethodWithDifferentArguments() {
        Invocation otherInvocationWithVerifiedMethodAndDifferentArguments = new Invocation(new Location(testObject, 1, 1), differentMethod, new Object[]{2, "test2"}, new Object[0]);
        otherInvocationWithVerifiedMethodAndDifferentArguments.setVerified(true);
        Assert.assertFalse(invocationMatcher.safelyArgumentsMatch(otherInvocationWithVerifiedMethodAndDifferentArguments.getArguments()));
        Assert.assertFalse(invocationMatcher.matches(otherInvocationWithVerifiedMethodAndDifferentArguments));
    }
}
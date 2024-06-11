import org.junit.Test;
import org.junit.Assert;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Arrays;
import java.util.List;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithInvocationAndEmptyListOfMatchers() {
        Invocation invocation = new Invocation(new Method("testMethod", new Class[0]), new Object[0], new Object[0], new Location(1, 2));
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        Assert.assertEquals(invocation.getMethod(), matcher.getMethod());
        Assert.assertNotNull(matcher.getMatchers());
        Assert.assertEquals(0, matcher.getMatchers().size());
    }

    @Test
    public void testConstructorWithInvocationAndListOfMatchers() {
        Invocation invocation = new Invocation(new Method("testMethod", new Class[0]), new Object[0], new Object[0], new Location(1, 2));
        List<Matcher> matchers = Arrays.asList(new CapturesArguments("arg1"), new CapturesArguments("arg2"));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        Assert.assertEquals(invocation.getMethod(), matcher.getMethod());
        Assert.assertEquals(matchers, matcher.getMatchers());
    }

    @Test(expected = AssertionError.class)
    public void testMatchesWithDifferentMocks() {
        Invocation invocation1 = new Invocation(new Method("testMethod", new Class[0]), new Object[0], new Object[0], new Location(1, 2));
        Invocation invocation2 = new Invocation(new Method("testMethod", new Class[0]), new Object[0], new Object[0], new Location(1, 3));
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        Invocation actual = new Invocation(invocation1.getMethod(), new Object[0], new Object[0], new Location(1, 3));

        matcher1.matches(actual);
    }

    @Test
    public void testMatchesWithSameMethodAndSameArguments() {
        Invocation invocation1 = new Invocation(new Method("testMethod", new Class[0]), new Object[0], new Object[0], new Location(1, 2));
        Invocation invocation2 = new Invocation(invocation1.getMethod(), new Object[0], new Object[0], new Location(1, 3));
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        Invocation actual = invocation1;

        matcher1.matches(actual);
        matcher2.matches(actual);
    }

    @Test
    public void testMatchesWithSameMethodButDifferentArguments() {
        Invocation invocation = new Invocation(new Method("testMethod", new Class[0]), new Object[0], new Object[0], new Location(1, 2));
        InvocationMatcher matcher1 = new InvocationMatcher(invocation);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation);

        Object[] args1 = new Object[]{1, 2};
        Object[] args2 = new Object[]{2, 1};
        Invocation actual = new Invocation(invocation.getMethod(), args1, new Object[0], new Location(1, 2));

        matcher1.matches(actual);
        Assert.assertFalse(matcher2.matches(actual));
    }

    @Test
    public void testMatchesWithSameMethodButDifferentNumberOfArguments() {
        Invocation invocation = new Invocation(new Method("testMethod", new Class[0]), new Object[0], new Object[0], new Location(1, 2));
        InvocationMatcher matcher1 = new InvocationMatcher(invocation);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation);

        Object[] args1 = new Object[]{1, 2};
        Invocation actual1 = new Invocation(invocation.getMethod(), args1, new Object[0], new Location(1, 2));

        Object[] args2 = new Object[]{1, 2, 3};
        Invocation actual2 = new Invocation(invocation.getMethod(), args2, new Object[0], new Location(1, 2));

        matcher1.matches(actual1);
        Assert.assertFalse(matcher2.matches(actual1));

        matcher1.matches(actual2);
        Assert.assertFalse(matcher2.matches(actual2));
    }

    @Test(expected = AssertionError.class)
    public void testMatchesWithOverloadedMethodsButSameArguments() {
        Class<?> clazz = Object.class;
        Method method1 = clazz.getMethod("testMethod", new Class[0]);
        Method method2 = clazz.getMethod("testMethod", new Class[]{Integer.TYPE});
        Invocation invocation = new Invocation(method1, new Object[0], new Object[0], new Location(1, 2));
        InvocationMatcher matcher1 = new InvocationMatcher(invocation);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation);

        Object[] args = new Object[]{1};
        Invocation actual = new Invocation(method2, args, new Object[0], new Location(1, 2));

        matcher1.matches(actual);
    }

    @Test
    public void testHasSimilarMethodWithSameMethod() {
        Invocation invocation1 = new Invocation(new Method("testMethod", new Class[0]), new Object[0], new Object[0], new Location(1, 2));
        Invocation invocation2 = new Invocation(invocation1.getMethod(), new Object[0], new Object[0], new Location(1, 3));
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        Assert.assertTrue(matcher1.hasSimilarMethod(matcher2));
    }

    @Test
    public void testHasSimilarMethodWithDifferentMethods() {
        Invocation invocation1 = new Invocation(new Method("testMethod1", new Class[0]), new Object[0], new Object[0], new Location(1, 2));
        Invocation invocation2 = new Invocation(new Method("testMethod2", new Class[0]), new Object[0], new Object[0], new Location(1, 3));
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        Assert.assertFalse(matcher1.hasSimilarMethod(matcher2));
    }

    @Test
    public void testToString() {
        Invocation invocation = new Invocation(new Method("testMethod", new Class[0]), new Object[0], new Object[0], new Location(1, 2));
        List<Matcher> matchers = Arrays.asList(new CapturesArguments("arg1"), new CapturesArguments("arg2"));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);
        PrintSettings printSettings = new PrintSettings();

        String expected = String.format("%s %s", invocation.toString(printSettings), matchers.toString());
        String result = matcher.toString(printSettings);

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testCaptureArgumentsFromInvocation() {
        Invocation invocation = new Invocation(new Method("testMethod", new Class[0]), new Object[0], new Object[0], new Location(1, 2));
        List<Matcher> matchers = Arrays.asList(new CapturesArguments("arg1"), new CapturesArguments("arg2"));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        Object[] actualArgs = new Object[]{1, 2};
        matcher.captureArgumentsFrom(new Invocation(invocation.getMethod(), actualArgs, new Object[0], new Location(1, 2)));

        CapturesArguments arg1Matcher = (CapturesArguments) matchers.get(0);
        CapturesArguments arg2Matcher = (CapturesArguments) matchers.get(1);

        Object arg1 = arg1Matcher.getValue();
        Object arg2 = arg2Matcher.getValue();

        Assert.assertEquals(actualArgs[0], arg1);
        Assert.assertEquals(actualArgs[1], arg2);
    }
}
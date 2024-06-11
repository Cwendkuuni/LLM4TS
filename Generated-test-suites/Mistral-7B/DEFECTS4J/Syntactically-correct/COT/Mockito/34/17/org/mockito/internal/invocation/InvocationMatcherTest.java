import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherConstructorWithInvocationAndEmptyMatchersList() {
        Invocation invocation = new Invocation(new Method("testMethod", new Class[0]), new Object[0], new Location(1, 2, "TestClass.java"), null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Method method = invocationMatcher.getMethod();
        Assert.assertEquals("testMethod", method.getName());
    }

    @Test
    public void testInvocationMatcherConstructorWithInvocationAndNonEmptyMatchersList() {
        Invocation invocation = new Invocation(new Method("testMethod", new Class[0]), new Object[0], new Location(1, 2, "TestClass.java"), null);
        List<Matcher> matchers = Arrays.asList(new CapturesArguments("arg1"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        List<Matcher> matchersFromInvocationMatcher = invocationMatcher.getMatchers();
        Assert.assertEquals(matchers, matchersFromInvocationMatcher);
    }

    @Test
    public void testInvocationMatcherMatchesInvocationWithSameMethodAndMock() {
        Invocation invocation1 = new Invocation(new Method("testMethod", new Class[0]), new Object[0], new Location(1, 2, "TestClass.java"), null);
        Invocation invocation2 = new Invocation(new Method("testMethod", new Class[0]), new Object[0], new Location(1, 2, "TestClass.java"), null);
        List<Matcher> matchers = Arrays.asList(new CapturesArguments("arg1"));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.<Matcher>emptyList());

        boolean result = invocationMatcher1.matches(invocationMatcher2);
        Assert.assertTrue(result);
    }

    @Test
    public void testInvocationMatcherDoesNotMatchInvocationWithDifferentMethod() {
        Invocation invocation1 = new Invocation(new Method("testMethod1", new Class[0]), new Object[0], new Location(1, 2, "TestClass.java"), null);
        Invocation invocation2 = new Invocation(new Method("testMethod2", new Class[0]), new Object[0], new Location(1, 2, "TestClass.java"), null);
        List<Matcher> matchers = Arrays.asList(new CapturesArguments("arg1"));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.<Matcher>emptyList());

        boolean result = invocationMatcher1.matches(invocationMatcher2);
        Assert.assertFalse(result);
    }

    @Test
    public void testInvocationMatcherDoesNotMatchInvocationWithDifferentMock() {
        Invocation invocation1 = new Invocation(new Method("testMethod", new Class[0]), new Object[0], new Location(1, 2, "TestClass.java"), null);
        Invocation invocation2 = new Invocation(new Method("testMethod", new Class[0]), new Object[0], new Location(1, 2, "AnotherTestClass.java"), null);
        List<Matcher> matchers = Arrays.asList(new CapturesArguments("arg1"));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.<Matcher>emptyList());

        boolean result = invocationMatcher1.matches(invocationMatcher2);
        Assert.assertFalse(result);
    }

    @Test
    public void testInvocationMatcherDoesNotMatchInvocationWithOverloadedButNotSameArguments() {
        Invocation invocation1 = new Invocation(new Method("testMethod", new Class[0]), new Object[]{1}, new Location(1, 2, "TestClass.java"), null);
        Invocation invocation2 = new Invocation(new Method("testMethod", new Class[0]), new Object[]{2}, new Location(1, 2, "TestClass.java"), null);
        List<Matcher> matchers = Arrays.asList(new CapturesArguments("arg1"));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.<Matcher>emptyList());

        boolean result = invocationMatcher1.matches(invocationMatcher2);
        Assert.assertFalse(result);
    }

    @Test
    public void testInvocationMatcherMatchesInvocationWithOverloadedButSameArguments() {
        Invocation invocation1 = new Invocation(new Method("testMethod", new Class[0]), new Object[]{1}, new Location(1, 2, "TestClass.java"), null);
        Invocation invocation2 = new Invocation(new Method("testMethod", new Class[0]), new Object[]{1}, new Location(1, 2, "TestClass.java"), null);
        List<Matcher> matchers = Arrays.asList(new CapturesArguments("arg1"));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.<Matcher>emptyList());

        boolean result = invocationMatcher1.matches(invocationMatcher2);
        Assert.assertTrue(result);
    }

    @Test(expected = AssertionError.class)
    public void testInvocationMatcherThrowsAssertionErrorWhenArgumentsDoNotMatch() {
        Invocation invocation1 = new Invocation(new Method("testMethod", new Class[0]), new Object[]{1}, new Location(1, 2, "TestClass.java"), null);
        Invocation invocation2 = new Invocation(new Method("testMethod", new Class[0]), new Object[]{2}, new Location(1, 2, "TestClass.java"), null);
        List<Matcher> matchers = Arrays.asList(new CapturesArguments("arg1"));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, matchers);

        invocationMatcher1.matches(invocation2);
    }

    @Test
    public void testInvocationMatcherToString() {
        Invocation invocation = new Invocation(new Method("testMethod", new Class[0]), new Object[0], new Location(1, 2, "TestClass.java"), null);
        List<Matcher> matchers = Arrays.asList(new CapturesArguments("arg1"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        String result = invocationMatcher.toString();
        String expected = String.format("invocation: %s%narguments: %s", invocation.toString(), matchers.toString());
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testCaptureArgumentsFromInvocation() {
        Invocation invocation = new Invocation(new Method("testMethod", new Class[0]), new Object[]{"arg1"}, new Location(1, 2, "TestClass.java"), null);
        List<Matcher> matchers = Arrays.asList(new CapturesArguments("arg1"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Object arg1 = new Object();
        invocationMatcher.captureArgumentsFrom(new Invocation(invocation.getMethod(), new Object[]{arg1}));

        Matcher matcher = matchers.get(0);
        Assert.assertTrue(matcher.matches(arg1));
    }
}
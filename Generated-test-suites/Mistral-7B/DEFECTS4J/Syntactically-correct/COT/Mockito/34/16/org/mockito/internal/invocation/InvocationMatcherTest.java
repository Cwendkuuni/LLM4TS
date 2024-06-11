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
    public void testInvocationMatcherConstructorWithInvocationAndMatchers() {
        Invocation invocation = new Invocation(new Object(), new Method("testMethod", new Class[0]), new Location(1, 1), new Object[0]);
        List<Matcher> matchers = Arrays.asList(new CapturesArguments("arg1"), new CapturesArguments("arg2"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Method method = invocationMatcher.getMethod();
        Assert.assertEquals("testMethod", method.getName());
        List<Matcher> matchersList = invocationMatcher.getMatchers();
        Assert.assertEquals(matchers, matchersList);
    }

    @Test
    public void testInvocationMatcherConstructorWithInvocationOnly() {
        Invocation invocation = new Invocation(new Object(), new Method("testMethod", new Class[0]), new Location(1, 1), new Object[0]);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Method method = invocationMatcher.getMethod();
        Assert.assertEquals("testMethod", method.getName());
        List<Matcher> matchers = invocationMatcher.getMatchers();
        Assert.assertEquals(Collections.emptyList(), matchers);
    }

    @Test
    public void testMatches() {
        Invocation invocation = new Invocation(new Object(), new Method("testMethod", new Class[0]), new Location(1, 1), new Object[0]);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation otherInvocation = new Invocation(new Object(), new Method("testMethod", new Class[0]), new Location(2, 2), new Object[0]);
        InvocationMatcher otherInvocationMatcher = new InvocationMatcher(otherInvocation);

        boolean result = invocationMatcher.matches(otherInvocationMatcher);
        Assert.assertFalse(result);

        invocationMatcher.getInvocation().getMock().equals(otherInvocationMatcher.getInvocation().getMock());
        result = invocationMatcher.matches(otherInvocationMatcher);
        Assert.assertTrue(result);

        Invocation otherInvocationWithDifferentMethod = new Invocation(new Object(), new Method("testAnotherMethod", new Class[0]), new Location(2, 2), new Object[0]);
        InvocationMatcher otherInvocationWithDifferentMethodMatcher = new InvocationMatcher(otherInvocationWithDifferentMethod);
        result = invocationMatcher.matches(otherInvocationWithDifferentMethodMatcher);
        Assert.assertFalse(result);
    }

    @Test(expected = AssertionError.class)
    public void testSafelyArgumentsMatchThrowsException() {
        Invocation invocation = new Invocation(new Object(), new Method("testMethod", new Class[0]), new Location(1, 1), new Object[0]);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Object[] actualArgs = new Object[]{new Object()};

        boolean result = invocationMatcher.safelyArgumentsMatch(actualArgs);
        Assert.assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation1 = new Invocation(new Object(), new Method("testMethod", new Class[0]), new Location(1, 1), new Object[0]);
        Invocation invocation2 = new Invocation(new Object(), new Method("testMethod", new Class[0]), new Location(2, 2), new Object[0]);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        boolean result = invocationMatcher1.hasSimilarMethod(invocationMatcher2);
        Assert.assertFalse(result);

        InvocationMatcher invocationMatcherWithSameMethod = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcherWithSameMethod2 = new InvocationMatcher(invocation2);
        result = invocationMatcherWithSameMethod.hasSimilarMethod(invocationMatcherWithSameMethod2);
        Assert.assertFalse(result);

        invocationMatcher1 = new InvocationMatcher(invocation1, Arrays.asList(new CapturesArguments("arg1")));
        invocationMatcher2 = new InvocationMatcher(invocation2, Arrays.asList(new CapturesArguments("arg1")));
        result = invocationMatcher1.hasSimilarMethod(invocationMatcher2);
        Assert.assertTrue(result);

        invocationMatcher1 = new InvocationMatcher(invocation1, Arrays.asList(new CapturesArguments("arg1"))) {
            @Override
            public boolean safelyArgumentsMatch(Object[] actualArgs) {
                // override to return true for test
                return true;
            }
        };
        invocationMatcher2 = new InvocationMatcher(invocation2, Arrays.asList(new CapturesArguments("arg1")));
        result = invocationMatcher1.hasSimilarMethod(invocationMatcher2);
        Assert.assertTrue(result);

        invocationMatcher1 = new InvocationMatcher(invocation1);
        invocationMatcher2 = new InvocationMatcher(invocation2, Arrays.asList(new CapturesArguments("arg1")));
        result = invocationMatcher1.hasSimilarMethod(invocationMatcher2);
        Assert.assertFalse(result);
    }

    @Test
    public void testToString() {
        Invocation invocation = new Invocation(new Object(), new Method("testMethod", new Class[0]), new Location(1, 1), new Object[0]);
        List<Matcher> matchers = Arrays.asList(new CapturesArguments("arg1"), new CapturesArguments("arg2"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        String result = invocationMatcher.toString();
        String expected = invocation.toString(matchers, new PrintSettings());
        Assert.assertEquals(expected, result);

        result = invocationMatcher.toString(new PrintSettings());
        Assert.assertEquals(invocation.toString(Collections.emptyList(), new PrintSettings()), result);
    }
}
import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherConstructionWithInvocationAndMatchers() {
        Invocation invocation = new Invocation(new Object() {}, new Method("testMethod", new Class[0]));
        List<Matcher> matchers = Collections.singletonList(new CapturesArguments("arg1", 1));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Method method = invocationMatcher.getMethod();
        Assert.assertEquals("testMethod", method.getName());

        List<Matcher> matchersList = invocationMatcher.getMatchers();
        Assert.assertEquals(1, matchersList.size());
        Assert.assertEquals(matchers.get(0), matchersList.get(0));
    }

    @Test
    public void testInvocationMatcherConstructionWithInvocationOnly() {
        Invocation invocation = new Invocation(new Object() {}, new Method("testMethod", new Class[0]));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Method method = invocationMatcher.getMethod();
        Assert.assertEquals("testMethod", method.getName());

        List<Matcher> matchers = invocationMatcher.getMatchers();
        Assert.assertEquals(0, matchers.size());
    }

    @Test
    public void testMatchesMethod() {
        Invocation invocation1 = new Invocation(new Object() {}, new Method("testMethod", new Class[0]));
        Invocation invocation2 = new Invocation(new Object() {}, new Method("testMethod", new Class[0]));
        List<Matcher> matchers1 = Arrays.asList(new CapturesArguments("arg1", 1), new CapturesArguments("arg2", 2));
        List<Matcher> matchers2 = Arrays.asList(new CapturesArguments("arg1", 1), new CapturesArguments("arg2", 3));

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, matchers1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, matchers2);

        Assert.assertTrue(invocationMatcher1.matches(invocationMatcher2));
        Assert.assertFalse(invocationMatcher1.matches(new InvocationMatcher(invocation1, matchers2)));
        Assert.assertFalse(invocationMatcher1.matches(new InvocationMatcher(new Invocation(new Object() {}, new Method("testMethod2", new Class[0])), matchers1)));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Invocation invocation = new Invocation(new Object() {}, new Method("testMethod", new Class[0]));
        Object[] arguments = new Object[]{1, 2};

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.emptyList());

        Assert.assertTrue(invocationMatcher.safelyArgumentsMatch(arguments));

        try {
            Thread.currentThread().stop();
        } catch (Throwable t) {
            Assert.assertFalse(invocationMatcher.safelyArgumentsMatch(new Object[]{1, 2, 3}));
        }
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = new Invocation(new Object() {}, new Method("testMethod", new Class[0]));
        Invocation invocation2 = new Invocation(new Object() {}, new Method("testMethod2", new Class[0]));

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Collections.emptyList());
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.emptyList());

        Assert.assertFalse(invocationMatcher1.hasSameMethod(invocationMatcher2));

        Invocation invocation3 = new Invocation(new Object() {}, new Method("testMethod", new Class[0]));
        InvocationMatcher invocationMatcher3 = new InvocationMatcher(invocation3, Collections.emptyList());

        Assert.assertTrue(invocationMatcher1.hasSameMethod(invocationMatcher3));
    }

    @Test(expected = AssertionError.class)
    public void testHasSimilarMethodWithDifferentInvocations() {
        Invocation invocation1 = new Invocation(new Object() {}, new Method("testMethod", new Class[0]));
        Invocation invocation2 = new Invocation(new Object() {}, new Method("testMethod2", new Class[0]));

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Collections.emptyList());
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.emptyList());

        invocationMatcher1.hasSimilarMethod(invocationMatcher2);
    }

    @Test(expected = AssertionError.class)
    public void testHasSimilarMethodWithDifferentArguments() {
        Invocation invocation = new Invocation(new Object() {}, new Method("testMethod", new Class[0]));
        Object[] arguments1 = new Object[]{1, 2};
        Object[] arguments2 = new Object[]{1, 2, 3};

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.emptyList());

        invocationMatcher.hasSimilarMethod(new InvocationMatcher(invocation, arguments2));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = new Invocation(new Object() {}, new Method("testMethod", new Class[0]));
        Object[] arguments = new Object[]{1, 2};

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.emptyList());

        CapturesArguments captureArguments = new CapturesArguments("arg1", 1);
        invocationMatcher.captureArgumentsFrom(new Invocation(new Object() {}, arguments));

        Assert.assertEquals(arguments[0], captureArguments.getValue());
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new LinkedList<Invocation>(){{
            add(new Invocation(new Object() {}, new Method("testMethod1", new Class[0])));
            add(new Invocation(new Object() {}, new Method("testMethod2", new Class[0])));
        }};

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        Assert.assertEquals(2, invocationMatchers.size());

        InvocationMatcher invocationMatcher1 = invocationMatchers.get(0);
        InvocationMatcher invocationMatcher2 = invocationMatchers.get(1);

        Assert.assertEquals("testMethod1", invocationMatcher1.getMethod().getName());
        Assert.assertEquals("testMethod2", invocationMatcher2.getMethod().getName());
    }
}
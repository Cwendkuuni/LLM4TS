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
    public void testInvocationMatcherConstructorWithInvocationAndEmptyMatcherList() {
        Invocation invocation = new Invocation(new Method("testMethod", new Class[0]), new Object[0], new Location("TestClass.java", 1, 1));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Method method = invocationMatcher.getMethod();
        Assert.assertEquals("testMethod", method.getName());

        List<Matcher> matchers = invocationMatcher.getMatchers();
        Assert.assertEquals(0, matchers.size());
    }

    @Test
    public void testInvocationMatcherConstructorWithInvocationAndMatcherList() {
        Invocation invocation = new Invocation(new Method("testMethod", new Class[0]), new Object[0], new Location("TestClass.java", 1, 1));
        List<Matcher> matchers = Collections.singletonList(new CapturesArguments("arg1"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Method method = invocationMatcher.getMethod();
        Assert.assertEquals("testMethod", method.getName());

        List<Matcher> actualMatchers = invocationMatcher.getMatchers();
        Assert.assertEquals(1, actualMatchers.size());
        CapturesArguments capturesArguments = (CapturesArguments) actualMatchers.get(0);
        Assert.assertEquals("arg1", capturesArguments.getName());
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = new Invocation(new Method("testMethod", new Class[0]), new Object[0], new Location("TestClass.java", 1, 1));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.emptyList());

        Invocation actualInvocation = invocationMatcher.getInvocation();
        Assert.assertEquals(invocation, actualInvocation);
    }

    @Test
    public void testMatches() {
        Invocation invocation1 = new Invocation(new Method("testMethod", new Class[0]), new Object[0], new Location("TestClass.java", 1, 1));
        Invocation invocation2 = new Invocation(new Method("testMethod", new Class[0]), new Object[0], new Location("TestClass.java", 2, 2));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Collections.emptyList());
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.emptyList());

        boolean result = invocationMatcher1.matches(invocationMatcher2);
        Assert.assertFalse(result);

        invocationMatcher1 = new InvocationMatcher(invocation1, Arrays.asList(new CapturesArguments("arg1")));
        invocationMatcher2 = new InvocationMatcher(invocation2, Arrays.asList(new CapturesArguments("arg2")));

        result = invocationMatcher1.matches(invocationMatcher2);
        Assert.assertFalse(result);

        invocationMatcher1 = new InvocationMatcher(invocation1, Arrays.asList(new CapturesArguments("arg1")));
        invocationMatcher2 = new InvocationMatcher(invocation2, Arrays.asList(new CapturesArguments("arg1")));

        result = invocationMatcher1.matches(invocationMatcher2);
        Assert.assertTrue(result);
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Invocation invocation = new Invocation(new Method("testMethod", new Class[0]), new Object[]{1}, new Location("TestClass.java", 1, 1));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.emptyList());

        Object[] actualArguments = new Object[]{2};

        boolean result = invocationMatcher.safelyArgumentsMatch(actualArguments);
        Assert.assertFalse(result);
    }

    @Test(expected = Throwable.class)
    public void testSafelyArgumentsMatchWithThrowable() {
        Invocation invocation = new Invocation(new Method("testMethod", new Class[0]), new Object[]{1}, new Location("TestClass.java", 1, 1));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.emptyList());

        Object[] actualArguments = new Object[]{new Exception()};

        invocationMatcher.safelyArgumentsMatch(actualArguments);
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation1 = new Invocation(new Method("testMethod", new Class[0]), new Object[0], new Location("TestClass.java", 1, 1));
        Invocation invocation2 = new Invocation(new Method("testMethod", new Class[0]), new Object[0], new Location("TestClass.java", 2, 2));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Collections.emptyList());
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.emptyList());

        boolean result = invocationMatcher1.hasSimilarMethod(invocationMatcher2);
        Assert.assertFalse(result);

        Method method1 = invocationMatcher1.getMethod();
        Method method2 = invocationMatcher2.getMethod();

        invocationMatcher1 = new InvocationMatcher(invocation1, Arrays.asList(new CapturesArguments("arg1")));
        invocationMatcher2 = new InvocationMatcher(invocation2, Arrays.asList(new CapturesArguments("arg1")));

        result = invocationMatcher1.hasSimilarMethod(invocationMatcher2);
        Assert.assertTrue(result);

        method1 = new Method("anotherTestMethod", new Class[0]);
        method2 = new Method("testMethod", new Class[0]);

        invocationMatcher1 = new InvocationMatcher(invocation1, Arrays.asList(new CapturesArguments("arg1")));
        invocationMatcher2 = new InvocationMatcher(new Invocation(new Method("anotherTestMethod", new Class[0]), new Object[0], new Location("TestClass.java", 3, 3)), Collections.emptyList());

        result = invocationMatcher1.hasSimilarMethod(invocationMatcher2);
        Assert.assertFalse(result);
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = new Invocation(new Method("testMethod", new Class[0]), new Object[0], new Location("TestClass.java", 1, 1));
        Invocation invocation2 = new Invocation(new Method("testMethod", new Class[0]), new Object[0], new Location("TestClass.java", 2, 2));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Collections.emptyList());
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.emptyList());

        boolean result = invocationMatcher1.hasSameMethod(invocationMatcher2);
        Assert.assertFalse(result);

        Method method1 = invocationMatcher1.getMethod();
        Method method2 = invocationMatcher2.getMethod();

        invocationMatcher1 = new InvocationMatcher(invocation1, Arrays.asList(new CapturesArguments("arg1")));
        invocationMatcher2 = new InvocationMatcher(new Invocation(new Method("testMethod", new Class[0]), new Object[0], new Location("TestClass.java", 3, 3)), Arrays.asList(new CapturesArguments("arg1")));

        result = invocationMatcher1.hasSameMethod(invocationMatcher2);
        Assert.assertTrue(result);

        method1 = new Method("anotherTestMethod", new Class[0]);
        method2 = new Method("testMethod", new Class[0]);

        invocationMatcher1 = new InvocationMatcher(invocation1, Arrays.asList(new CapturesArguments("arg1")));
        invocationMatcher2 = new InvocationMatcher(new Invocation(new Method("anotherTestMethod", new Class[0]), new Object[0], new Location("TestClass.java", 3, 3)), Collections.emptyList());

        result = invocationMatcher1.hasSameMethod(invocationMatcher2);
        Assert.assertFalse(result);
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new LinkedList<>();
        invocations.add(new Invocation(new Method("testMethod1", new Class[0]), new Object[0], new Location("TestClass.java", 1, 1)));
        invocations.add(new Invocation(new Method("testMethod2", new Class[0]), new Object[0], new Location("TestClass.java", 2, 2)));

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        InvocationMatcher invocationMatcher1 = invocationMatchers.get(0);
        InvocationMatcher invocationMatcher2 = invocationMatchers.get(1);

        Method method1 = invocationMatcher1.getMethod();
        Method method2 = invocationMatcher2.getMethod();

        Assert.assertEquals("testMethod1", method1.getName());
        Assert.assertEquals("testMethod2", method2.getName());
    }
}
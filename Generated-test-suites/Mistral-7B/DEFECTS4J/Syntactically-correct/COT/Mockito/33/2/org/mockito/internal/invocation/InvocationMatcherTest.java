import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsComparator;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithInvocationAndEmptyMatchersList() {
        Invocation invocation = new Invocation(new Method("testMethod", new Class[0]), new Object[0], new Location("TestClass.java", 10, 20), null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Method method = invocationMatcher.getMethod();
        Assert.assertEquals("testMethod", method.getName());
    }

    @Test
    public void testConstructorWithInvocationAndMatchersList() {
        Invocation invocation = new Invocation(new Method("testMethod", new Class[0]), new Object[0], new Location("TestClass.java", 10, 20), null);
        List<Matcher> matchers = new LinkedList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Method method = invocationMatcher.getMethod();
        List<Matcher> matchersList = invocationMatcher.getMatchers();
        Assert.assertEquals("testMethod", method.getName());
        Assert.assertEquals(matchers, matchersList);
    }

    @Test
    public void testMatches() {
        Invocation invocation1 = new Invocation(new Method("testMethod", new Class[0]), new Object[0], new Location("TestClass.java", 10, 20), null);
        Invocation invocation2 = new Invocation(new Method("testMethod", new Class[0]), new Object[0], new Location("TestClass.java", 20, 30), null);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        Assert.assertFalse(invocationMatcher1.matches(invocationMatcher2));

        InvocationMatcher invocationMatcher3 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher4 = new InvocationMatcher(invocation1);

        Assert.assertTrue(invocationMatcher3.matches(invocationMatcher4));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Invocation invocation = new Invocation(new Method("testMethod", new Class[0]), new Object[0], new Location("TestClass.java", 10, 20), null);
        Object[] actualArgs = new Object[0];
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Assert.assertTrue(invocationMatcher.safelyArgumentsMatch(actualArgs));
    }

    @Test(expected = Throwable.class)
    public void testSafelyArgumentsMatchThrowable() {
        Invocation invocation = new Invocation(new Method("testMethod", new Class[0]), new Object[0], new Location("TestClass.java", 10, 20), null);
        Object[] actualArgs = new Object[]{"exception"};
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        invocationMatcher.safelyArgumentsMatch(actualArgs);
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation1 = new Invocation(new Method("testMethod1", new Class[0]), new Object[0], new Location("TestClass.java", 10, 20), null);
        Invocation invocation2 = new Invocation(new Method("testMethod2", new Class[0]), new Object[0], new Location("TestClass.java", 20, 30), null);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        Assert.assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher2));

        Invocation invocation3 = new Invocation(new Method("testMethod", new Class[0]), new Object[0], new Location("TestClass.java", 10, 20), null);
        InvocationMatcher invocationMatcher3 = new InvocationMatcher(invocation3);
        InvocationMatcher invocationMatcher4 = new InvocationMatcher(invocation3);

        Assert.assertTrue(invocationMatcher3.hasSimilarMethod(invocationMatcher4));

        InvocationMatcher invocationMatcher5 = new InvocationMatcher(invocation3);
        InvocationMatcher invocationMatcher6 = new InvocationMatcher(invocation1);

        Assert.assertFalse(invocationMatcher5.hasSimilarMethod(invocationMatcher6));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = new Invocation(new Method("testMethod", new Class[0]), new Object[0], new Location("TestClass.java", 10, 20), null);
        Invocation invocation2 = new Invocation(new Method("testMethod", new Class[0]), new Object[0], new Location("TestClass.java", 20, 30), null);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        Method method1 = invocationMatcher1.getMethod();
        Method method2 = invocationMatcher2.getMethod();

        InvocationMatcher invocationMatcher3 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher4 = new InvocationMatcher(invocation1);

        Method method3 = invocationMatcher3.getMethod();
        Method method4 = invocationMatcher4.getMethod();

        Assert.assertTrue(method1.equals(method2));
        Assert.assertTrue(method3.equals(method4));
        Assert.assertTrue(method1.equals(method3));
    }

    @Test
    public void testToString() {
        Invocation invocation = new Invocation(new Method("testMethod", new Class[0]), new Object[0], new Location("TestClass.java", 10, 20), null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        String expected = "Invocation [method=testMethod(), invocation=Invocation [location=TestClass.java:10:20, arguments=[], mock=<mock>], matchers=<empty list>]";
        String actual = invocationMatcher.toString();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = new Invocation(new Method("testMethod", new Class[0]), new Object[]{"arg1"}, new Location("TestClass.java", 10, 20), null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        CapturesArguments capturesArguments = new CapturesArguments();

        invocationMatcher.captureArgumentsFrom(invocation);

        Object capturedArg = capturesArguments.getValue();
        Assert.assertEquals("arg1", capturedArg);
    }

    @Test
    public void testCreateFrom() {
        Invocation invocation1 = new Invocation(new Method("testMethod1", new Class[0]), new Object[0], new Location("TestClass.java", 10, 20), null);
        Invocation invocation2 = new Invocation(new Method("testMethod2", new Class[0]), new Object[0], new Location("TestClass.java", 20, 30), null);
        Invocation invocation3 = new Invocation(new Method("testMethod3", new Class[0]), new Object[0], new Location("TestClass.java", 30, 40), null);

        List<Invocation> invocations = new LinkedList<>();
        invocations.add(invocation1);
        invocations.add(invocation2);
        invocations.add(invocation3);

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        InvocationMatcher invocationMatcher1 = invocationMatchers.get(0);
        InvocationMatcher invocationMatcher2 = invocationMatchers.get(1);
        InvocationMatcher invocationMatcher3 = invocationMatchers.get(2);

        Method method1 = invocationMatcher1.getMethod();
        Method method2 = invocationMatcher2.getMethod();
        Method method3 = invocationMatcher3.getMethod();

        Assert.assertEquals(method1, method2);
        Assert.assertNotEquals(method1, method3);
        Assert.assertEquals(invocation1.getMethod(), method1);
        Assert.assertEquals(invocation2.getMethod(), method2);
        Assert.assertEquals(invocation3.getMethod(), method3);
    }
}
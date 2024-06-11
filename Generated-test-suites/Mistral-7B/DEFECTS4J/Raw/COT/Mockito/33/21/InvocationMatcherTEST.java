import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsMatcher;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithInvocationAndEmptyMatchers() {
        Invocation invocation = new Invocation(new Object(), new Method(TestClass.class, "testMethod", new Class[0]));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Method method = invocationMatcher.getMethod();
        Assert.assertEquals(method.getName(), "testMethod");
        Assert.assertNull(invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithInvocationAndMatchers() {
        Invocation invocation = new Invocation(new Object(), new Method(TestClass.class, "testMethod", new Class[0])).withArguments(new Object[]{new Object()});
        List<Matcher> matchers = Arrays.asList(new ArgumentsMatcher(new Object[]{new Object()}));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Method method = invocationMatcher.getMethod();
        Assert.assertEquals(method.getName(), "testMethod");
        Assert.assertEquals(invocationMatcher.getMatchers().size(), 1);
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = new Invocation(new Object(), new Method(TestClass.class, "testMethod", new Class[0]));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.emptyList());

        Invocation expectedInvocation = invocation;
        Invocation actualInvocation = invocationMatcher.getInvocation();

        Assert.assertEquals(expectedInvocation, actualInvocation);
    }

    @Test
    public void testGetMatchers() {
        Invocation invocation = new Invocation(new Object(), new Method(TestClass.class, "testMethod", new Class[0])).withArguments(new Object[]{new Object()});
        List<Matcher> matchers = Arrays.asList(new ArgumentsMatcher(new Object[]{new Object()}));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        List<Matcher> expectedMatchers = matchers;
        List<Matcher> actualMatchers = invocationMatcher.getMatchers();

        Assert.assertEquals(expectedMatchers, actualMatchers);
    }

    @Test
    public void testMatches() {
        Invocation invocation1 = new Invocation(new Object(), new Method(TestClass.class, "testMethod", new Class[0])).withArguments(new Object[]{new Object()});
        Invocation invocation2 = new Invocation(new Object(), new Method(TestClass.class, "testMethod", new Class[0])).withArguments(new Object[]{new Object(), new Object()});
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        boolean result = invocationMatcher1.matches(invocationMatcher2);
        Assert.assertFalse(result);

        invocationMatcher1 = new InvocationMatcher(invocation1, Arrays.asList(new ArgumentsMatcher(new Object[]{new Object()})));
        result = invocationMatcher1.matches(invocationMatcher2);
        Assert.assertFalse(result);

        invocationMatcher2 = new InvocationMatcher(invocation2, Arrays.asList(new ArgumentsMatcher(new Object[]{new Object()})));
        result = invocationMatcher1.matches(invocationMatcher2);
        Assert.assertTrue(result);

        invocationMatcher1 = new InvocationMatcher(invocation1, Arrays.asList(new ArgumentsMatcher(new Object[]{new Object(), new Object()})));
        result = invocationMatcher1.matches(invocationMatcher2);
        Assert.assertFalse(result);

        invocationMatcher1 = new InvocationMatcher(invocation1, Arrays.asList(new ArgumentsMatcher(new Object[]{new Object()}), new ArgumentsMatcher(new Object[]{new Object()})));
        result = invocationMatcher1.matches(invocationMatcher2);
        Assert.assertTrue(result);
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Invocation invocation = new Invocation(new Object(), new Method(TestClass.class, "testMethod", new Class[0])).withArguments(new Object[]{new Object()});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(new ArgumentsMatcher(new Object[]{new Object()})));

        boolean result = invocationMatcher.safelyArgumentsMatch(new Object[]{new Object(), new Object()});
        Assert.assertFalse(result);

        result = invocationMatcher.safelyArgumentsMatch(new Object[]{new Object()});
        Assert.assertTrue(result);
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation1 = new Invocation(new Object(), new Method(TestClass.class, "testMethod", new Class[0]));
        Invocation invocation2 = new Invocation(new Object(), new Method(TestClass.class, "testMethod", new Class[0]));

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        boolean result = invocationMatcher1.hasSimilarMethod(invocationMatcher2);
        Assert.assertTrue(result);

        result = invocationMatcher1.hasSimilarMethod(new InvocationMatcher(new Invocation(new Object(), new Method(TestClass.class, "testMethod2", new Class[0]))));
        Assert.assertFalse(result);
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = new Invocation(new Object(), new Method(TestClass.class, "testMethod", new Class[0]));
        Invocation invocation2 = new Invocation(new Object(), new Method(TestClass.class, "testMethod", new Class[0]));

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        boolean result = invocationMatcher1.hasSameMethod(invocationMatcher2);
        Assert.assertTrue(result);

        result = invocationMatcher1.hasSameMethod(new InvocationMatcher(new Invocation(new Object(), new Method(TestClass.class, "testMethod2", new Class[0]))));
        Assert.assertFalse(result);
    }

    @Test(expected = AssertionError.class)
    public void testToString() {
        Invocation invocation = new Invocation(new Object(), new Method(TestClass.class, "testMethod", new Class[0]));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(new ArgumentsMatcher(new Object[]{new Object()})));

        String result = invocationMatcher.toString();
        Assert.fail("Expected an AssertionError but got: " + result);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = new Invocation(new Object(), new Method(TestClass.class, "testMethod", new Class[0])).withArguments(new Object[]{new Object(), new Object()});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(new ArgumentsMatcher(new Object[]{new Object()})), new ArgumentsMatcher(new Object[]{new Object()}));

        invocationMatcher.captureArgumentsFrom(invocation);

        Object capturedArgument = invocationMatcher.getMatchers().get(0).getActual();
        Assert.assertEquals(capturedArgument, invocation.getArguments()[0]);

        capturedArgument = invocationMatcher.getMatchers().get(1).getActual();
        Assert.assertEquals(capturedArgument, invocation.getArguments()[1]);
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new LinkedList<>();
        invocations.add(new Invocation(new Object(), new Method(TestClass.class, "testMethod", new Class[0])).withArguments(new Object[]{new Object()}));
        invocations.add(new Invocation(new Object(), new Method(TestClass.class, "testMethod", new Class[0])).withArguments(new Object[]{new Object(), new Object()}));

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        int size = invocationMatchers.size();
        Assert.assertEquals(size, 2);

        InvocationMatcher invocationMatcher1 = invocationMatchers.get(0);
        InvocationMatcher invocationMatcher2 = invocationMatchers.get(1);

        Method method1 = invocationMatcher1.getMethod();
        Method method2 = invocationMatcher2.getMethod();

        Assert.assertEquals(method1.getName(), "testMethod");
        Assert.assertEquals(method2.getName(), "testMethod");

        List<Matcher> matchers1 = invocationMatcher1.getMatchers();
        List<Matcher> matchers2 = invocationMatcher2.getMatchers();

        Assert.assertEquals(matchers1.size(), 1);
        Assert.assertEquals(matchers2.size(), 1);

        Object capturedArgument1 = matchers1.get(0).getActual();
        Object capturedArgument2 = matchers2.get(0).getActual();

        Assert.assertNotNull(capturedArgument1);
        Assert.assertNotNull(capturedArgument2);

        Assert.assertNotSame(capturedArgument1, capturedArgument2);
    }
}
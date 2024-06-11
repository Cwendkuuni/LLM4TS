import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsComparator;
import org.mockito.internal.reporting.PrintSettings;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithInvocationAndEmptyMatchers() {
        Invocation invocation = new Invocation(new Location("TestClass.java", 10), "testMethod", new Object[0], new Class[0]);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Method method = invocationMatcher.getMethod();
        Assert.assertEquals("testMethod", method.getName());

        List<Matcher> matchers = invocationMatcher.getMatchers();
        Assert.assertEquals(0, matchers.size());
    }

    @Test
    public void testConstructorWithInvocationAndCustomMatchers() {
        Invocation invocation = new Invocation(new Location("TestClass.java", 10), "testMethod", new Object[]{"arg1", "arg2"}, new Class[]{String.class, String.class});
        Matcher matcher1 = ArgumentCaptor.forClass(String.class);
        Matcher matcher2 = ArgumentCaptor.forClass(String.class);
        List<Matcher> matchers = Collections.singletonList(matcher1.capture("matcher1"))
                .add(matcher2.capture("matcher2"));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Method method = invocationMatcher.getMethod();
        Assert.assertEquals("testMethod", method.getName());

        List<Matcher> actualMatchers = invocationMatcher.getMatchers();
        Assert.assertEquals(2, actualMatchers.size());
        Assert.assertEquals(matcher1, actualMatchers.get(0));
        Assert.assertEquals(matcher2, actualMatchers.get(1));
    }

    @Test
    public void testMatchesSameInvocation() {
        Invocation invocation = new Invocation(new Location("TestClass.java", 10), "testMethod", new Object[]{1, 2}, new Class[]{Integer.class, Integer.class});
        Matcher matcher = ArgumentCaptor.forClass(Integer.class);
        List<Matcher> matchers = Collections.singletonList(matcher.capture("matcher"));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Invocation sameInvocation = new Invocation(invocation);

        boolean result = invocationMatcher.matches(sameInvocation);
        Assert.assertTrue(result);
    }

    @Test
    public void testMatchesDifferentInvocationSameMethod() {
        Invocation invocation1 = new Invocation(new Location("TestClass.java", 10), "testMethod", new Object[]{1, 2}, new Class[]{Integer.class, Integer.class});
        Invocation invocation2 = new Invocation(new Location("TestClass.java", 20), "testMethod", new Object[]{3, 4}, new Class[]{Integer.class, Integer.class});

        Matcher matcher = ArgumentCaptor.forClass(Integer.class);
        List<Matcher> matchers1 = Collections.singletonList(matcher.capture("matcher1"));
        List<Matcher> matchers2 = Collections.singletonList(matcher.capture("matcher2"));

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, matchers1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, matchers2);

        boolean result = invocationMatcher1.matches(invocationMatcher2);
        Assert.assertFalse(result);
    }

    @Test
    public void testMatchesDifferentInvocationSameMethodSameArguments() {
        Invocation invocation1 = new Invocation(new Location("TestClass.java", 10), "testMethod", new Object[]{1, 2}, new Class[]{Integer.class, Integer.class});
        Invocation invocation2 = new Invocation(new Location("TestClass.java", 20), "testMethod", new Object[]{1, 2}, new Class[]{Integer.class, Integer.class});

        Matcher matcher = ArgumentCaptor.forClass(Integer.class);
        List<Matcher> matchers1 = Collections.singletonList(matcher.capture("matcher1"));
        List<Matcher> matchers2 = Collections.singletonList(matcher.capture("matcher2"));

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, matchers1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, matchers2);

        boolean result = invocationMatcher1.matches(invocationMatcher2);
        Assert.assertTrue(result);
    }

    @Test(expected = AssertionError.class)
    public void testMatchesDifferentInvocationSameMethodDifferentArguments() {
        Invocation invocation1 = new Invocation(new Location("TestClass.java", 10), "testMethod", new Object[]{1, 2}, new Class[]{Integer.class, Integer.class});
        Invocation invocation2 = new Invocation(new Location("TestClass.java", 20), "testMethod", new Object[]{3, 4}, new Class[]{Integer.class, Integer.class});

        Matcher matcher = ArgumentCaptor.forClass(Integer.class);
        List<Matcher> matchers1 = Collections.singletonList(matcher.capture("matcher1"));
        List<Matcher> matchers2 = Collections.singletonList(matcher.capture("matcher2"));

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, matchers1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, matchers2);

        boolean result = invocationMatcher1.matches(invocationMatcher2);
        Assert.assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation1 = new Invocation(new Location("TestClass.java", 10), "testMethod1", new Object[]{1, 2}, new Class[]{Integer.class, Integer.class});
        Invocation invocation2 = new Invocation(new Location("TestClass.java", 20), "testMethod2", new Object[]{1, 2}, new Class[]{Integer.class, Integer.class});

        boolean result = new InvocationMatcher(invocation1).hasSimilarMethod(new InvocationMatcher(invocation2));
        Assert.assertTrue(result);

        invocation2 = new Invocation(new Location("TestClass.java", 20), "testMethod3", new Object[]{1, 2}, new Class[]{Integer.class, Double.class});
        result = new InvocationMatcher(invocation1).hasSimilarMethod(new InvocationMatcher(invocation2));
        Assert.assertFalse(result);
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = new Invocation(new Location("TestClass.java", 10), "testMethod", new Object[]{1, 2}, new Class[]{Integer.class, Integer.class});
        Invocation invocation2 = new Invocation(new Location("TestClass.java", 20), "testMethod", new Object[]{1, 2}, new Class[]{Integer.class, Integer.class});

        boolean result = new InvocationMatcher(invocation1).hasSameMethod(new InvocationMatcher(invocation2));
        Assert.assertTrue(result);
    }

    @Test
    public void testToString() {
        Invocation invocation = new Invocation(new Location("TestClass.java", 10), "testMethod", new Object[]{"arg1", "arg2"}, new Class[]{String.class, String.class});
        Matcher matcher1 = ArgumentCaptor.forClass(String.class);
        Matcher matcher2 = ArgumentCaptor.forClass(String.class);
        List<Matcher> matchers = Collections.singletonList(matcher1.capture("matcher1"))
                .add(matcher2.capture("matcher2"));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        String result = invocationMatcher.toString();
        String expected = String.format("InvocationMatcher[invocation=%s, method=%s, matchers=%s]", invocation, "testMethod", matchers);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = new Invocation(new Location("TestClass.java", 10), "testMethod", new Object[]{"arg1", "arg2"}, new Class[]{String.class, String.class});
        Matcher matcher = ArgumentCaptor.forClass(String.class);
        List<Matcher> matchers = Collections.singletonList(matcher.capture("matcher"));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Object[] arguments = new Object[]{"expectedArg1", "expectedArg2"};
        invocationMatcher.captureArgumentsFrom(new Invocation(new Location("TestClass.java", 20), "testMethod", arguments));

        Object capturedArg1 = matcher.getValue().getValue()[0];
        Object capturedArg2 = matcher.getValue().getValue()[1];

        Assert.assertEquals("expectedArg1", capturedArg1);
        Assert.assertEquals("expectedArg2", capturedArg2);
    }
}
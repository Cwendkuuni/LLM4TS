import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsComparator;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithInvocationAndEmptyMatcherList() {
        Invocation invocation = new MockitoInvocation("mock", "methodName", null, new Object[0], new Location("TestClass.java", 1));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Assert.assertNotNull(invocationMatcher);
        Assert.assertNull(invocationMatcher.getMatchers());
        Assert.assertEquals(invocation, invocationMatcher.getInvocation());
        Assert.assertEquals("methodName", invocationMatcher.getMethod().getName());
    }

    @Test
    public void testConstructorWithInvocationAndMatcherList() {
        Invocation invocation = new MockitoInvocation("mock", "methodName", null, new Object[]{"arg1", "arg2"}, new Location("TestClass.java", 1));
        List<Matcher> matchers = Arrays.asList(new EqualsMatcher("arg1"), new IsInstanceOf(String[].class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Assert.assertNotNull(invocationMatcher);
        Assert.assertEquals(matchers, invocationMatcher.getMatchers());
        Assert.assertEquals(invocation, invocationMatcher.getInvocation());
        Assert.assertEquals("methodName", invocationMatcher.getMethod().getName());
    }

    @Test
    public void testGetMethod() {
        Invocation invocation = new MockitoInvocation("mock", "methodName", null, new Object[0], new Location("TestClass.java", 1));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Assert.assertEquals(invocation.getMethod(), invocationMatcher.getMethod());
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = new MockitoInvocation("mock", "methodName", null, new Object[0], new Location("TestClass.java", 1));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Assert.assertEquals(invocation, invocationMatcher.getInvocation());
    }

    @Test
    public void testGetMatchers() {
        Invocation invocation = new MockitoInvocation("mock", "methodName", null, new Object[]{"arg1", "arg2"}, new Location("TestClass.java", 1));
        List<Matcher> matchers = Arrays.asList(new EqualsMatcher("arg1"), new IsInstanceOf(String[].class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Assert.assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testToString() {
        Invocation invocation = new MockitoInvocation("mock", "methodName", null, new Object[]{"arg1", "arg2"}, new Location("TestClass.java", 1));
        List<Matcher> matchers = Arrays.asList(new EqualsMatcher("arg1"), new IsInstanceOf(String[].class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        String expected = "InvocationMatcher[invocation=Invocation[this=TestClass$Test$MockitoInvocationAtLine1, method=methodName, arguments=[arg1, arg2]], matchers=[Matcher[Expected arg1 to be <arg1>], Matcher[Expected arg2 to be instance of [Ljava.lang.Object;]])]";
        Assert.assertEquals(expected, invocationMatcher.toString());
    }

    @Test
    public void testMatches() {
        Invocation invocation1 = new MockitoInvocation("mock1", "methodName", null, new Object[]{"arg1", "arg2"}, new Location("TestClass.java", 1));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);

        Invocation invocation2 = new MockitoInvocation("mock1", "methodName", null, new Object[]{"arg1", "arg2"}, new Location("TestClass.java", 2));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        List<Matcher> matchers = Arrays.asList(new EqualsMatcher("arg1"), new EqualsMatcher("arg2"));
        InvocationMatcher expectedMatcher = new InvocationMatcher(invocation1, matchers);

        Assert.assertTrue(invocationMatcher1.matches(invocation1));
        Assert.assertTrue(invocationMatcher1.matches(invocation2));
        Assert.assertFalse(invocationMatcher2.matches(invocation1));
        Assert.assertTrue(expectedMatcher.matches(invocation1));
        Assert.assertTrue(expectedMatcher.matches(invocation2));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Invocation invocation = new MockitoInvocation("mock", "methodName", null, new Object[]{"arg1", "arg2"}, new Location("TestClass.java", 1));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Object[] arguments = new Object[]{"arg1", "arg2"};
        Assert.assertTrue(invocationMatcher.safelyArgumentsMatch(arguments));
    }

    @Test(expected = Throwable.class)
    public void testSafelyArgumentsMatchWithException() {
        Invocation invocation = new MockitoInvocation("mock", "methodName", null, new Object[]{"arg1", "arg2"}, new Location("TestClass.java", 1));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Object[] arguments = new Object[]{"arg1", new RuntimeException("TestException")};
        invocationMatcher.safelyArgumentsMatch(arguments);
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation1 = new MockitoInvocation("mock1", "methodName1", null, new Object[0], new Location("TestClass.java", 1));
        Invocation invocation2 = new MockitoInvocation("mock1", "methodName2", null, new Object[0], new Location("TestClass.java", 2));

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        Assert.assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher2));

        Invocation invocation3 = new MockitoInvocation("mock1", "methodName1", null, new Object[0], new Location("TestClass.java", 1));
        InvocationMatcher invocationMatcher3 = new InvocationMatcher(invocation3);

        Assert.assertTrue(invocationMatcher1.hasSimilarMethod(invocationMatcher3));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = new MockitoInvocation("mock1", "methodName", null, new Object[]{"arg1", "arg2"}, new Location("TestClass.java", 1));
        Invocation invocation2 = new MockitoInvocation("mock1", "methodName", null, new Object[]{"arg1", "arg2"}, new Location("TestClass.java", 2));

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        Assert.assertTrue(invocationMatcher1.hasSameMethod(invocationMatcher2));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = new MockitoInvocation("mock", "methodName", null, new Object[]{"arg1", "arg2"}, new Location("TestClass.java", 1));
        List<Matcher> matchers = Collections.singletonList(new CapturesArgumentsMatcher("arg1"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Object arg1 = new Object();
        Object arg2 = new Object();

        invocationMatcher.captureArgumentsFrom(invocation);

        CapturesArguments capturesArguments = (CapturesArguments) matchers.get(0);
        Assert.assertSame(arg1, capturesArguments.getValue());
        Assert.assertNull(capturesArguments.getRawValue());

        capturesArguments = (CapturesArguments) matchers.get(0);
        invocationMatcher.captureArgumentsFrom(invocation);
        Assert.assertSame(arg2, capturesArguments.getValue());
        Assert.assertSame(invocation.getRawArguments()[1], capturesArguments.getRawValue());
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new ArrayList<>();
        invocations.add(new MockitoInvocation("mock1", "methodName1", null, new Object[]{"arg1", "arg2"}, new Location("TestClass.java", 1)));
        invocations.add(new MockitoInvocation("mock1", "methodName2", null, new Object[]{"arg1", "arg2"}, new Location("TestClass.java", 2)));

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        InvocationMatcher expectedMatcher1 = new InvocationMatcher(invocations.get(0));
        InvocationMatcher expectedMatcher2 = new InvocationMatcher(invocations.get(1));

        Assert.assertEquals(2, invocationMatchers.size());
        Assert.assertTrue(invocationMatchers.contains(expectedMatcher1));
        Assert.assertTrue(invocationMatchers.contains(expectedMatcher2));
    }
}
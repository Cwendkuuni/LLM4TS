import org.junit.Assert;
import org.junit.Test;
import org.mockito.InvocationOnMock;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationOnMockImpl;
import org.mockito.internal.matchers.ArgumentsMatcher;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.util.ArgumentsProcessor;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.invocation.Invocation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithInvocationAndEmptyList() {
        Invocation invocation = new InvocationOnMockImpl(new Object(), new Method(TestClass.class, "method1", new Class[0]));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Method method = invocationMatcher.getMethod();
        Assert.assertNotNull(method);
        Assert.assertEquals("method1", method.getName());
    }

    @Test
    public void testConstructorWithInvocationAndMatchers() {
        Invocation invocation = new InvocationOnMockImpl(new Object(), new Method(TestClass.class, "method1", new Class[]{Integer.class, String[].class}));
        List<Matcher> matchers = Arrays.asList(ArgumentsMatcher.instance("1"), ArgumentsMatcher.instance(Arrays.asList("test", "test2")));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Method method = invocationMatcher.getMethod();
        Assert.assertNotNull(method);
        Assert.assertEquals("method1", method.getName());
        Assert.assertEquals(2, invocationMatcher.getMatchers().size());
    }

    @Test
    public void testMatches() {
        Invocation invocation = new InvocationOnMockImpl(new Object(), new Method(TestClass.class, "method1", new Class[]{Integer.class, String[].class}));
        List<Matcher> matchers = Arrays.asList(ArgumentsMatcher.instance("1"), ArgumentsMatcher.instance(Arrays.asList("test", "test2")));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Invocation actualInvocation = new InvocationOnMockImpl(new Object(), new Method(TestClass.class, "method1", new Class[]{Integer.class, String[].class}));
        actualInvocation.setArguments(new Object[]{1, new String[]{"test", "test2"}});

        Assert.assertTrue(invocationMatcher.matches(actualInvocation));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Invocation invocation = new InvocationOnMockImpl(new Object(), new Method(TestClass.class, "method1", new Class[]{Integer.class, String[].class}));
        List<Matcher> matchers = Arrays.asList(ArgumentsMatcher.instance("1"), ArgumentsMatcher.instance(Arrays.asList("test", "test2")));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Object[] actualArgs = new Object[]{1, new String[]{"test", "test2"}};

        Assert.assertTrue(invocationMatcher.safelyArgumentsMatch(actualArgs));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation1 = new InvocationOnMockImpl(new Object(), new Method(TestClass.class, "method1", new Class[]{Integer.class, String[].class}));
        Invocation invocation2 = new InvocationOnMockImpl(new Object(), new Method(TestClass.class, "method1", new Class[]{String[].class, Integer.class}));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        Assert.assertFalse(invocationMatcher.hasSimilarMethod(new InvocationOnMockImpl(invocation2)));

        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation1);
        Invocation invocation3 = new InvocationOnMockImpl(new Object(), new Method(TestClass.class, "method1", new Class[]{Integer.class, String[].class}));
        invocationMatcher2.setInvocation(invocation3);

        Assert.assertTrue(invocationMatcher2.hasSimilarMethod(new InvocationOnMockImpl(invocation1)));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = new InvocationOnMockImpl(new Object(), new Method(TestClass.class, "method1", new Class[]{Integer.class, String[].class}));
        Invocation invocation2 = new InvocationOnMockImpl(new Object(), new Method(TestClass.class, "method1", new Class[]{Integer.class, String[].class}));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        Assert.assertTrue(invocationMatcher.hasSameMethod(invocation2));
    }

    @Test(expected = AssertionError.class)
    public void testToString() {
        Invocation invocation = new InvocationOnMockImpl(new Object(), new Method(TestClass.class, "method1", new Class[]{Integer.class, String[].class}));
        List<Matcher> matchers = Arrays.asList(ArgumentsMatcher.instance("1"), ArgumentsMatcher.instance(Arrays.asList("test", "test2")));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        new PrintSettings().print(invocationMatcher.getMatchers(), invocation);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = new InvocationOnMockImpl(new Object(), new Method(TestClass.class, "method1", new Class[]{Integer.class, String[].class}));
        List<Matcher> matchers = Arrays.asList(new CapturesArgumentsMatcher(), new CapturesArgumentsMatcher());
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        invocationMatcher.captureArgumentsFrom(invocation);

        Assert.assertEquals(2, matchers.get(0).getCaptured().size());
        Assert.assertEquals(2, matchers.get(1).getCaptured().size());
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = Arrays.asList(new InvocationOnMockImpl(new Object(), new Method(TestClass.class, "method1", new Class[]{Integer.class, String[].class})),
                new InvocationOnMockImpl(new Object(), new Method(TestClass.class, "method1", new Class[]{String[].class, Integer.class})));
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        Assert.assertEquals(2, invocationMatchers.size());
    }

    static class TestClass {
        public void method1(Integer i, String[] args) {
        }
    }
}

class CapturesArgumentsMatcher implements Matcher<Object> {
    private List<Object> captured = new ArrayList<>();

    @Override
    public boolean matches(Object argument) {
        captured.add(argument);
        return true;
    }

    public List<Object> getCaptured() {
        return captured;
    }
}
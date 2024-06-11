import org.junit.Assert;
import org.junit.Test;
import org.mockito.InvocationOnMock;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationOnMockImpl;
import org.mockito.internal.matchers.ArgumentsMatcher;
import org.mockito.internal.matchers.VarargMatcher;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithInvocationAndEmptyMatcherList() {
        Invocation invocation = new InvocationOnMockImpl(new Object(), new Method(Object.class, new Class[0]));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Assert.assertNotNull(invocationMatcher.getMethod());
        Assert.assertNotNull(invocationMatcher.getInvocation());
        Assert.assertNotNull(invocationMatcher.getMatchers());
        Assert.assertEquals(0, invocationMatcher.getMatchers().size());
    }

    @Test
    public void testConstructorWithInvocationAndMatcherList() {
        Object[] arguments = new Object[]{1, "hello"};
        Invocation invocation = new InvocationOnMockImpl(new Object(), new Method(Object.class, new Class[]{Integer.TYPE, String.class}), arguments);
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ArgumentsMatcher(arguments));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Assert.assertNotNull(invocationMatcher.getMethod());
        Assert.assertNotNull(invocationMatcher.getInvocation());
        Assert.assertNotNull(invocationMatcher.getMatchers());
        Assert.assertEquals(1, invocationMatcher.getMatchers().size());
    }

    @Test
    public void testGetMethod() {
        Object[] arguments = new Object[]{1, "hello"};
        Invocation invocation = new InvocationOnMockImpl(new Object(), new Method(Object.class, new Class[]{Integer.TYPE, String.class}), arguments);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Method method = invocationMatcher.getMethod();

        Assert.assertNotNull(method);
        Assert.assertEquals(Object.class, method.getReturnType());
        Assert.assertArrayEquals(new Class[]{Integer.TYPE, String.class}, method.getParameterTypes());
    }

    @Test
    public void testGetInvocation() {
        Object[] arguments = new Object[]{1, "hello"};
        Invocation invocation = new InvocationOnMockImpl(new Object(), new Method(Object.class, new Class[]{Integer.TYPE, String.class}), arguments);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation expectedInvocation = invocation;
        Invocation actualInvocation = invocationMatcher.getInvocation();

        Assert.assertNotNull(actualInvocation);
        Assert.assertSame(expectedInvocation, actualInvocation);
    }

    @Test
    public void testGetMatchers() {
        Object[] arguments = new Object[]{1, "hello"};
        Invocation invocation = new InvocationOnMockImpl(new Object(), new Method(Object.class, new Class[]{Integer.TYPE, String.class}), arguments);
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ArgumentsMatcher(arguments));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        List<Matcher> expectedMatchers = matchers;
        List<Matcher> actualMatchers = invocationMatcher.getMatchers();

        Assert.assertNotNull(actualMatchers);
        Assert.assertEquals(expectedMatchers, actualMatchers);
    }

    @Test
    public void testToString() {
        Object[] arguments = new Object[]{1, "hello"};
        Invocation invocation = new InvocationOnMockImpl(new Object(), new Method(Object.class, new Class[]{Integer.TYPE, String.class}), arguments);
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ArgumentsMatcher(arguments));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        String expectedString = String.format("Invocation [%s%n%s]", invocation, matchers);
        String actualString = invocationMatcher.toString();

        Assert.assertEquals(expectedString, actualString);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCaptureArgumentsFromWithVarargsInvocation() {
        Object[] arguments = new Object[]{1, "hello", new String[]{"world"}};
        Invocation invocation = new InvocationOnMockImpl(new Object(), new Method(Object.class, new Class[]{Integer.TYPE, String.class, String[].class}), arguments);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        invocationMatcher.captureArgumentsFrom(invocation);
    }

    @Test
    public void testHasSimilarMethodWithSameMethod() {
        Object[] arguments1 = new Object[]{1, "hello"};
        Object[] arguments2 = new Object[]{1, "hello"};
        Invocation invocation1 = new InvocationOnMockImpl(new Object(), new Method(Object.class, new Class[]{Integer.TYPE, String.class}), arguments1);
        Invocation invocation2 = new InvocationOnMockImpl(new Object(), new Method(Object.class, new Class[]{Integer.TYPE, String.class}), arguments2);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        boolean result = invocationMatcher1.hasSimilarMethod(invocationMatcher2);

        Assert.assertTrue(result);
    }

    @Test
    public void testHasSimilarMethodWithDifferentMethod() {
        Object[] arguments1 = new Object[]{1, "hello"};
        Object[] arguments2 = new Object[]{2, "world"};
        Invocation invocation1 = new InvocationOnMockImpl(new Object(), new Method(Object.class, new Class[]{Integer.TYPE, String.class}), arguments1);
        Invocation invocation2 = new InvocationOnMockImpl(new Object(), new Method(Object.class, new Class[]{Integer.TYPE, String.class}), arguments2);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        boolean result = invocationMatcher1.hasSimilarMethod(invocationMatcher2);

        Assert.assertFalse(result);
    }

    @Test
    public void testHasSameMethodWithSameMethod() {
        Object[] arguments = new Object[]{1, "hello"};
        Invocation invocation1 = new InvocationOnMockImpl(new Object(), new Method(Object.class, new Class[]{Integer.TYPE, String.class}), arguments);
        Invocation invocation2 = new InvocationOnMockImpl(new Object(), new Method(Object.class, new Class[]{Integer.TYPE, String.class}), arguments);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        boolean result = invocationMatcher1.hasSameMethod(invocationMatcher2);

        Assert.assertTrue(result);
    }

    @Test
    public void testHasSameMethodWithDifferentMethod() {
        Object[] arguments1 = new Object[]{1, "hello"};
        Object[] arguments2 = new Object[]{2, "world"};
        Invocation invocation1 = new InvocationOnMockImpl(new Object(), new Method(Object.class, new Class[]{Integer.TYPE, String.class}), arguments1);
        Invocation invocation2 = new InvocationOnMockImpl(new Object(), new Method(Object.class, new Class[]{Integer.TYPE, String.class}), arguments2);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        boolean result = invocationMatcher1.hasSameMethod(invocationMatcher2);

        Assert.assertFalse(result);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Object[] arguments = new Object[]{1, "hello"};
        Invocation invocation = new InvocationOnMockImpl(new Object(), new Method(Object.class, new Class[]{Integer.TYPE, String.class}), arguments);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ArgumentsMatcher(arguments));
        InvocationMatcher invocationMatcherWithMatchers = new InvocationMatcher(invocation, matchers);

        invocationMatcherWithMatchers.captureArgumentsFrom(invocation);

        for (Matcher matcher : invocationMatcherWithMatchers.getMatchers()) {
            if (matcher instanceof ArgumentsMatcher) {
                ArgumentsMatcher argumentsMatcher = (ArgumentsMatcher) matcher;
                Object[] capturedArguments = argumentsMatcher.getCapturedArguments();
                Assert.assertArrayEquals(arguments, capturedArguments);
            }
        }
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new LinkedList<>();
        Object[] arguments1 = new Object[]{1, "hello"};
        Invocation invocation1 = new InvocationOnMockImpl(new Object(), new Method(Object.class, new Class[]{Integer.TYPE, String.class}), arguments1);
        invocations.add(invocation1);

        Object[] arguments2 = new Object[]{2, "world"};
        Invocation invocation2 = new InvocationOnMockImpl(new Object(), new Method(Object.class, new Class[]{Integer.TYPE, String.class}), arguments2);
        invocations.add(invocation2);

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        for (InvocationMatcher invocationMatcher : invocationMatchers) {
            Invocation invocation = invocationMatcher.getInvocation();
            Invocation expectedInvocation = invocations.get(invocationMatchers.indexOf(invocationMatcher));
            Assert.assertSame(expectedInvocation, invocation);
        }
    }
}
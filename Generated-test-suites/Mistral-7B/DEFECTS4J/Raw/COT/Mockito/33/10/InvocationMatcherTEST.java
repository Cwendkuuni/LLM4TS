import org.junit.Test;
import org.junit.Assert;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsComparator;
import org.mockito.internal.matchers.CapturesArguments;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherConstructorWithInvocationAndEmptyList() {
        Invocation invocation = new Invocation(new Object() {}.getClass().getMethod("test"), new Object[]{}, new Location(1, 1, "TestClass.java"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Method method = invocationMatcher.getMethod();
        Assert.assertEquals(method.getName(), "test");
        Assert.assertEquals(invocationMatcher.getInvocation(), invocation);
        Assert.assertNotNull(invocationMatcher.getMatchers());
        Assert.assertEquals(invocationMatcher.getMatchers().size(), 0);
    }

    @Test
    public void testInvocationMatcherConstructorWithInvocationAndListOfMatchers() {
        Invocation invocation = new Invocation(new Object() {}.getClass().getMethod("test", int.class, String.class), new Object[]{}, new Location(1, 1, "TestClass.java"));
        List<Matcher> matchers = Arrays.asList(new CapturesArguments(), new CapturesArguments());
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Method method = invocationMatcher.getMethod();
        Assert.assertEquals(method.getName(), "test");
        Assert.assertEquals(method.getParameterTypes().length, 2);
        Assert.assertEquals(invocationMatcher.getInvocation(), invocation);
        Assert.assertEquals(invocationMatcher.getMatchers().size(), 2);
    }

    @Test
    public void testGetMethod() {
        Invocation invocation = new Invocation(new Object() {}.getClass().getMethod("test", int.class, String.class), new Object[]{}, new Location(1, 1, "TestClass.java"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Method method = invocationMatcher.getMethod();
        Assert.assertEquals(method.getName(), "test");
        Assert.assertEquals(method.getParameterTypes().length, 2);
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = new Invocation(new Object() {}.getClass().getMethod("test", int.class, String.class), new Object[]{}, new Location(1, 1, "TestClass.java"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation actualInvocation = new Invocation(new Object() {}.getClass().getMethod("test", int.class, String.class), new Object[]{1, "test"}, new Location(1, 1, "TestClass.java"));
        InvocationMatcher actualInvocationMatcher = new InvocationMatcher(actualInvocation);

        Assert.assertEquals(invocationMatcher.getInvocation(), invocation);
        Assert.assertEquals(actualInvocationMatcher.getInvocation(), actualInvocation);
    }

    @Test
    public void testGetMatchers() {
        Invocation invocation = new Invocation(new Object() {}.getClass().getMethod("test", int.class, String.class), new Object[]{}, new Location(1, 1, "TestClass.java"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        List<Matcher> matchers = Collections.singletonList(new CapturesArguments());
        InvocationMatcher otherInvocationMatcher = new InvocationMatcher(invocation, matchers);

        Assert.assertNotSame(invocationMatcher.getMatchers(), otherInvocationMatcher.getMatchers());
    }

    @Test
    public void testToString() {
        Invocation invocation = new Invocation(new Object() {}.getClass().getMethod("test", int.class, String.class), new Object[]{}, new Location(1, 1, "TestClass.java"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        String expected = String.format("InvocationMatcher{method=%s, invocation=%s}", invocation.getMethod(), invocation);
        String actual = invocationMatcher.toString();

        Assert.assertEquals(expected, actual);
    }

    @Test(expected = AssertionError.class)
    public void testMatchesWithDifferentMocks() {
        Invocation invocation1 = new Invocation(new Object() {}.getClass().getMethod("test", int.class), new Object[]{1}, new Location(1, 1, "TestClass.java"));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);

        Invocation invocation2 = new Invocation(new Object() {}.getClass().getMethod("test", int.class), new Object[]{2}, new Location(1, 1, "TestClass.java"));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        Assert.assertFalse(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testMatchesWithSameMethod() {
        Invocation invocation1 = new Invocation(new Object() {}.getClass().getMethod("test", int.class), new Object[]{1}, new Location(1, 1, "TestClass.java"));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);

        Invocation invocation2 = new Invocation(new Object() {}.getClass().getMethod("test", int.class), new Object[]{1}, new Location(1, 1, "TestClass.java"));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        Assert.assertTrue(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testMatchesWithSameMethodAndDifferentArguments() {
        Invocation invocation1 = new Invocation(new Object() {}.getClass().getMethod("test", int.class, String.class), new Object[]{1, "test"}, new Location(1, 1, "TestClass.java"));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);

        Invocation invocation2 = new Invocation(new Object() {}.getClass().getMethod("test", int.class, String.class), new Object[]{2, "test"}, new Location(1, 1, "TestClass.java"));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        Assert.assertFalse(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testMatchesWithSameMethodAndSameArguments() {
        Invocation invocation1 = new Invocation(new Object() {}.getClass().getMethod("test", int.class, String.class), new Object[]{1, "test"}, new Location(1, 1, "TestClass.java"));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);

        Invocation invocation2 = new Invocation(new Object() {}.getClass().getMethod("test", int.class, String.class), new Object[]{1, "test"}, new Location(1, 1, "TestClass.java"));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        Assert.assertTrue(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testSafelyArgumentsMatchWithSameArguments() {
        Invocation invocation = new Invocation(new Object() {}.getClass().getMethod("test", int.class, String.class), new Object[]{1, "test"}, new Location(1, 1, "TestClass.java"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Object[] actualArguments = new Object[]{1, "test"};
        Assert.assertTrue(invocationMatcher.safelyArgumentsMatch(actualArguments));
    }

    @Test(expected = AssertionError.class)
    public void testSafelyArgumentsMatchWithDifferentArguments() {
        Invocation invocation = new Invocation(new Object() {}.getClass().getMethod("test", int.class, String.class), new Object[]{1, "test"}, new Location(1, 1, "TestClass.java"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Object[] actualArguments = new Object[]{2, "test2"};
        Assert.assertFalse(invocationMatcher.safelyArgumentsMatch(actualArguments));
    }

    @Test
    public void testHasSimilarMethodWithSameMethodSameArguments() {
        Invocation invocation1 = new Invocation(new Object() {}.getClass().getMethod("test", int.class, String.class), new Object[]{1, "test"}, new Location(1, 1, "TestClass.java"));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);

        Invocation invocation2 = new Invocation(new Object() {}.getClass().getMethod("test", int.class, String.class), new Object[]{1, "test"}, new Location(1, 1, "TestClass.java"));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        Assert.assertTrue(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testHasSimilarMethodWithSameMethodDifferentArguments() {
        Invocation invocation1 = new Invocation(new Object() {}.getClass().getMethod("test", int.class, String.class), new Object[]{1, "test"}, new Location(1, 1, "TestClass.java"));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);

        Invocation invocation2 = new Invocation(new Object() {}.getClass().getMethod("test", int.class, String.class), new Object[]{2, "test2"}, new Location(1, 1, "TestClass.java"));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        Assert.assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testHasSimilarMethodWithDifferentMethods() {
        Invocation invocation1 = new Invocation(new Object() {}.getClass().getMethod("test1", int.class), new Object[]{1}, new Location(1, 1, "TestClass.java"));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);

        Invocation invocation2 = new Invocation(new Object() {}.getClass().getMethod("test2", String.class), new Object[]{"test"}, new Location(1, 1, "TestClass.java"));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        Assert.assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = new Invocation(new Object() {}.getClass().getMethod("test", int.class, String.class), new Object[]{1, "test"}, new Location(1, 1, "TestClass.java"));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);

        Invocation invocation2 = new Invocation(new Object() {}.getClass().getMethod("test", int.class, String.class), new Object[]{1, "test"}, new Location(1, 1, "TestClass.java"));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        Method method = invocationMatcher1.getMethod();
        Method method2 = invocationMatcher2.getMethod();

        Assert.assertEquals(method, method2);
        Assert.assertTrue(invocationMatcher1.hasSameMethod(invocationMatcher2));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = new Invocation(new Object() {}.getClass().getMethod("test", int.class, String.class), new Object[]{1, "test"}, new Location(1, 1, "TestClass.java"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        CapturesArguments matcher = new CapturesArguments();
        invocationMatcher.captureArgumentsFrom(invocation);

        Assert.assertTrue(matcher.isCaptured());
        Assert.assertEquals(matcher.getArgumentAt(0), 1);
        Assert.assertEquals(matcher.getArgumentAt(1), "test");
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new LinkedList<>();
        invocations.add(new Invocation(new Object() {}.getClass().getMethod("test", int.class), new Object[]{1}, new Location(1, 1, "TestClass.java")));
        invocations.add(new Invocation(new Object() {}.getClass().getMethod("test", String.class), new Object[]{"test"}, new Location(1, 1, "TestClass.java")));

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        InvocationMatcher firstInvocationMatcher = invocationMatchers.get(0);
        InvocationMatcher secondInvocationMatcher = invocationMatchers.get(1);

        Assert.assertEquals(firstInvocationMatcher.getMethod().getName(), "test");
        Assert.assertEquals(firstInvocationMatcher.getMethod().getParameterTypes().length, 1);

        Assert.assertEquals(secondInvocationMatcher.getMethod().getName(), "test");
        Assert.assertEquals(secondInvocationMatcher.getMethod().getParameterTypes().length, 1);

        Assert.assertNotSame(firstInvocationMatcher, secondInvocationMatcher);
    }
}
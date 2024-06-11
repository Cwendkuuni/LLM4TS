import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationMatcherUnitTest;
import org.mockito.internal.matchers.ArgumentsComparator;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithInvocationAndEmptyList() {
        Invocation invocation = new Invocation(new Object() {}.getClass().getMethod("test", String.class), new Object[]{"Hello"});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Method method = invocationMatcher.getMethod();
        Assert.assertEquals(method.getName(), "test");

        List<Matcher> matchers = invocationMatcher.getMatchers();
        Assert.assertTrue(matchers.isEmpty());
    }

    @Test
    public void testConstructorWithInvocationAndMatchers() {
        Invocation invocation = new Invocation(new Object() {}.getClass().getMethod("test", String.class, int.class), new Object[]{"Hello", 12});
        Matcher<String> stringMatcher = ArgumentMatchers.eq("Hello");
        Matcher<Integer> integerMatcher = ArgumentMatchers.eq(12);
        List<Matcher> matchers = new LinkedList<Matcher>() {{
            add(stringMatcher);
            add(integerMatcher);
        }};

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Method method = invocationMatcher.getMethod();
        Assert.assertEquals(method.getName(), "test");

        List<Matcher> actualMatchers = invocationMatcher.getMatchers();
        Assert.assertEquals(actualMatchers.size(), 2);
        Assert.assertTrue(actualMatchers.contains(stringMatcher));
        Assert.assertTrue(actualMatchers.contains(integerMatcher));
    }

    @Test
    public void testMatches() {
        Invocation invocation1 = new Invocation(new Object() {}.getClass().getMethod("test", String.class), new Object[]{"Hello"});
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);

        Invocation invocation2 = new Invocation(new Object() {}.getClass().getMethod("test", String.class), new Object[]{"Hello"});
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        Assert.assertTrue(invocationMatcher1.matches(invocationMatcher2));
        Assert.assertTrue(invocationMatcher2.matches(invocationMatcher1));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Invocation invocation = new Invocation(new Object() {}.getClass().getMethod("test", String[].class), new Object[]{new String[]{"Hello"}});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Object[] actualArgs = new Object[]{new String[]{"Hello"}};
        boolean result = invocationMatcher.safelyArgumentsMatch(actualArgs);
        Assert.assertTrue(result);
    }

    @Test(expected = Throwable.class)
    public void testSafelyArgumentsMatchThrowsException() {
        Invocation invocation = new Invocation(new Object() {}.getClass().getMethod("test", String[].class), new Object[]{new String[]{"Hello"}});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Object[] actualArgs = new Object[]{new Integer[]{1}};
        invocationMatcher.safelyArgumentsMatch(actualArgs);
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation1 = new Invocation(new Object() {}.getClass().getMethod("test", String.class), new Object[]{"Hello"});
        Invocation invocation2 = new Invocation(new Object() {}.getClass().getMethod("test", String.class), new Object[]{"World"});

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        boolean result = invocationMatcher1.hasSimilarMethod(invocationMatcher2);
        Assert.assertFalse(result);

        // same method name, same mock, unverified
        Invocation invocation3 = new Invocation(new Object() {}.getClass().getMethod("test", String.class), new Object[]{"Hello"});
        InvocationMatcher invocationMatcher3 = new InvocationMatcher(invocation3);
        Invocation invocation4 = new Invocation(new Object() {}.getClass().getMethod("test", String.class), new Object[]{"Hello"});
        InvocationMatcher invocationMatcher4 = new InvocationMatcher(invocation4);

        result = invocationMatcher3.hasSimilarMethod(invocationMatcher4);
        Assert.assertTrue(result);

        // same method name, same mock, unverified and same arguments
        Invocation invocation5 = new Invocation(new Object() {}.getClass().getMethod("test", String.class), new Object[]{"Hello"});
        InvocationMatcher invocationMatcher5 = new InvocationMatcher(invocation5);
        Invocation invocation6 = new Invocation(new Object() {}.getClass().getMethod("test", String.class), new Object[]{"Hello"});
        InvocationMatcher invocationMatcher6 = new InvocationMatcher(invocation6);

        result = invocationMatcher5.hasSimilarMethod(invocationMatcher6);
        Assert.assertTrue(result);

        // different methods but same arguments
        Invocation invocation7 = new Invocation(new Object() {}.getClass().getMethod("test1", String.class), new Object[]{"Hello"});
        InvocationMatcher invocationMatcher7 = new InvocationMatcher(invocation7);
        Invocation invocation8 = new Invocation(new Object() {}.getClass().getMethod("test2", String.class), new Object[]{"Hello"});
        InvocationMatcher invocationMatcher8 = new InvocationMatcher(invocation8);

        result = invocationMatcher7.hasSimilarMethod(invocationMatcher8);
        Assert.assertFalse(result);

        // different methods but same arguments and same class
        Invocation invocation9 = new Invocation(new Object() {}.getClass().getMethod("test1", String.class), new Object[]{"Hello"});
        InvocationMatcher invocationMatcher9 = new InvocationMatcher(invocation9);
        Invocation invocation10 = new Invocation(new Object() {}.getClass().getMethod("test2", String.class), new Object[]{"Hello"});
        InvocationMatcher invocationMatcher10 = new InvocationMatcher(invocation10);

        if (invocationMatcher9.getClass() == invocationMatcher10.getClass()) {
            result = invocationMatcher9.hasSameMethod(invocationMatcher10);
            Assert.assertTrue(result);
        } else {
            result = invocationMatcher9.hasSimilarMethod(invocationMatcher10);
            Assert.assertFalse(result);
        }
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = new Invocation(new Object() {}.getClass().getMethod("test", String.class), new Object[]{"Hello"});
        Invocation invocation2 = new Invocation(new Object() {}.getClass().getMethod("test", String.class), new Object[]{"Hello"});

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        boolean result = invocationMatcher1.hasSameMethod(invocationMatcher2);
        Assert.assertTrue(result);
    }

    @Test
    public void testToString() {
        Invocation invocation = new Invocation(new Object() {}.getClass().getMethod("test", String.class), new Object[]{"Hello"});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        String result = invocationMatcher.toString();
        String expected = String.format("InvocationMatcher[%s, %s]", invocation.getMethod().toGenericString(), invocation.getArguments());
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new LinkedList<Invocation>() {{
            add(new Invocation(new Object() {}.getClass().getMethod("test1", String.class), new Object[]{"Hello"}));
            add(new Invocation(new Object() {}.getClass().getMethod("test2", String.class), new Object[]{"World"}));
        }};

        List<InvocationMatcher> result = InvocationMatcher.createFrom(invocations);

        InvocationMatcher invocationMatcher1 = result.get(0);
        InvocationMatcher invocationMatcher2 = result.get(1);

        Assert.assertNotNull(invocationMatcher1);
        Assert.assertNotNull(invocationMatcher2);

        Method method1 = invocationMatcher1.getMethod();
        Assert.assertEquals(method1.getName(), "test1");

        List<Matcher> matchers1 = invocationMatcher1.getMatchers();
        Assert.assertTrue(matchers1.isEmpty());

        Method method2 = invocationMatcher2.getMethod();
        Assert.assertEquals(method2.getName(), "test2");

        List<Matcher> matchers2 = invocationMatcher2.getMatchers();
        Assert.assertTrue(matchers2.isEmpty());
    }
}
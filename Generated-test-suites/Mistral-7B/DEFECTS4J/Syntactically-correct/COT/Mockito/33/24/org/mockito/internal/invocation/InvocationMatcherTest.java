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
    public void testInvocationMatcherConstructionWithMatchers() {
        // Given
        Method testMethod = getClass().getMethod("testMethod", String.class, int.class);
        Invocation invocation = new Invocation(new Object() {}.getClass(), testMethod, Arrays.asList("test", 1));
        List<Matcher> matchers = Arrays.asList(new CapturesArguments(), new CapturesArguments());
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // When

        // No specific verification needed as methods are tested below
    }

    @Test
    public void testInvocationMatcherConstructionWithoutMatchers() {
        // Given
        Method testMethod = getClass().getMethod("testMethod", String.class, int.class);
        Invocation invocation = new Invocation(new Object() {}.getClass(), testMethod, Arrays.asList("test", 1));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // When

        // No specific verification needed as methods are tested below
    }

    @Test
    public void testGetMethod() {
        // Given
        Method testMethod = getClass().getMethod("testMethod", String.class, int.class);
        Invocation invocation = new Invocation(new Object() {}.getClass(), testMethod, Arrays.asList("test", 1));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // When
        Method result = invocationMatcher.getMethod();

        // Then
        Assert.assertEquals(testMethod, result);
    }

    @Test
    public void testGetInvocation() {
        // Given
        Method testMethod = getClass().getMethod("testMethod", String.class, int.class);
        Invocation invocation = new Invocation(new Object() {}.getClass(), testMethod, Arrays.asList("test", 1));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // When
        Invocation result = invocationMatcher.getInvocation();

        // Then
        Assert.assertEquals(invocation, result);
    }

    @Test
    public void testGetMatchers() {
        // Given
        Method testMethod = getClass().getMethod("testMethod", String.class, int.class);
        Invocation invocation = new Invocation(new Object() {}.getClass(), testMethod, Arrays.asList("test", 1));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(new CapturesArguments(), new CapturesArguments()));

        // When
        List<Matcher> result = invocationMatcher.getMatchers();

        // Then
        Assert.assertEquals(Arrays.asList(new CapturesArguments(), new CapturesArguments()), result);
    }

    @Test
    public void testToString() {
        // Given
        Method testMethod = getClass().getMethod("testMethod", String.class, int.class);
        Invocation invocation = new Invocation(new Object() {}.getClass(), testMethod, Arrays.asList("test", 1));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // When
        String result = invocationMatcher.toString();

        // Then
        // Using a stub PrintSettings for simplicity
        String expected = String.format("Invocation on %s%n%s%nArguments: %s%n",
                invocation.getMock().getClass().getName(),
                testMethod.getName(),
                Arrays.toString(invocation.getArguments()));
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testMatches() {
        // Given
        Method testMethod = getClass().getMethod("testMethod", String.class, int.class);
        Invocation invocation1 = new Invocation(new Object() {}.getClass(), testMethod, Arrays.asList("test", 1));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);

        Invocation invocation2 = new Invocation(new Object() {}.getClass(), testMethod, Arrays.asList("test2", 2));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        // When
        boolean result = invocationMatcher1.matches(invocation2);

        // Then
        Assert.assertFalse(result);
    }

    @Test
    public void testSafelyArgumentsMatch() {
        // Given
        Method testMethod = getClass().getMethod("testMethod", String.class, int.class);
        Object[] arguments1 = new Object[]{"test1", 1};
        Invocation invocation1 = new Invocation(new Object() {}.getClass(), testMethod, arguments1);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);

        Object[] arguments2 = new Object[]{"test2", 2};
        Invocation invocation2 = new Invocation(new Object() {}.getClass(), testMethod, arguments2);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        // When
        boolean result = invocationMatcher1.safelyArgumentsMatch(arguments2);

        // Then
        Assert.assertFalse(result);
    }

    @Test
    public void testHasSameMethod() {
        // Given
        Method testMethod = getClass().getMethod("testMethod", String.class, int.class);
        Method testMethod2 = getClass().getMethod("testMethod2", String.class, int.class);

        Invocation invocation1 = new Invocation(new Object() {}.getClass(), testMethod, Arrays.asList("test", 1));
        Invocation invocation2 = new Invocation(new Object() {}.getClass(), testMethod2, Arrays.asList("test", 1));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        // When
        boolean result = invocationMatcher1.hasSameMethod(invocationMatcher2);

        // Then
        Assert.assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod() {
        // Given
        Method testMethod = getClass().getMethod("testMethod", String.class, int.class);
        Method testMethod2 = getClass().getMethod("testMethod", String.class, int.class);

        Invocation invocation1 = new Invocation(new Object() {}.getClass(), testMethod, Arrays.asList("test", 1));
        Invocation invocation2 = new Invocation(new Object() {}.getClass(), testMethod2, Arrays.asList("test", 1));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        // When
        boolean result = invocationMatcher1.hasSimilarMethod(invocationMatcher2);

        // Then
        Assert.assertFalse(result);
    }

    @Test(expected = AssertionError.class)
    public void testThrowWhenArgumentsDoNotMatch() {
        // Given
        Method testMethod = getClass().getMethod("testMethod", String.class, int.class);
        Object[] arguments1 = new Object[]{"test1", 1};
        Invocation invocation1 = new Invocation(new Object() {}.getClass(), testMethod, arguments1);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);

        Object[] arguments2 = new Object[]{"test2", 2};
        Invocation invocation2 = new Invocation(new Object() {}.getClass(), testMethod, arguments2);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        // When
        invocationMatcher1.matches(invocationMatcher2);

        // Then - AssertionError should be thrown
    }

    @Test
    public void testCaptureArgumentsFrom() {
        // Given
        Method testMethod = getClass().getMethod("testMethod", String.class, int.class);
        Object[] arguments = new Object[]{"test", 1};
        Invocation invocation = new Invocation(new Object() {}.getClass(), testMethod, arguments);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // When
        Invocation testInvocation = new Invocation(new Object() {}.getClass(), testMethod, arguments);
        invocationMatcher.captureArgumentsFrom(testInvocation);

        // Then
        CapturesArguments matcher1 = (CapturesArguments) invocationMatcher.getMatchers().get(0);
        CapturesArguments matcher2 = (CapturesArguments) invocationMatcher.getMatchers().get(1);
        Assert.assertNotNull(matcher1.getValue());
        Assert.assertNotNull(matcher2.getValue());
        Assert.assertEquals("test", matcher1.getValue());
        Assert.assertEquals(1, matcher2.getValue());
    }

    @Test
    public void testCreateFrom() {
        // Given
        Method testMethod = getClass().getMethod("testMethod", String.class, int.class);
        List<Invocation> invocations = new LinkedList<>();
        invocations.add(new Invocation(new Object() {}.getClass(), testMethod, Arrays.asList("test1", 1)));
        invocations.add(new Invocation(new Object() {}.getClass(), testMethod, Arrays.asList("test2", 2)));

        // When
        List<InvocationMatcher> result = InvocationMatcher.createFrom(invocations);

        // Then
        InvocationMatcher invocationMatcher1 = result.get(0);
        InvocationMatcher invocationMatcher2 = result.get(1);

        Assert.assertEquals(2, result.size());
        Assert.assertNotNull(invocationMatcher1.getMethod());
        Assert.assertNotNull(invocationMatcher2.getMethod());
        Assert.assertNotSame(invocationMatcher1.getMethod(), invocationMatcher2.getMethod());
    }
}
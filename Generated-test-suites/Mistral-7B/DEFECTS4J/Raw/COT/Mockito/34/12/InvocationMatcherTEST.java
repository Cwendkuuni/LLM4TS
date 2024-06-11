import org.junit.Test;
import org.junit.Assert;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsComparator;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.util.collections.Arrays;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherConstructorWithInvocationAndEmptyList() {
        Invocation invocation = new Invocation(new Object() {}.getClass().getMethod("testMethod", new Class[0]), new Object[0], new Location(1, 1), null);
        List<Matcher> matchers = Collections.emptyList();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Method method = invocationMatcher.getMethod();
        Assert.assertEquals(method.getName(), "testMethod");

        Invocation expectedInvocation = invocation;
        Invocation actualInvocation = invocationMatcher.getInvocation();
        Assert.assertEquals(expectedInvocation, actualInvocation);

        List<Matcher> expectedMatchers = matchers;
        List<Matcher> actualMatchers = invocationMatcher.getMatchers();
        Assert.assertEquals(expectedMatchers, actualMatchers);

        String expectedToString = invocation.toString();
        String actualToString = invocationMatcher.toString();
        Assert.assertEquals(expectedToString, actualToString);
    }

    @Test
    public void testInvocationMatcherConstructorWithInvocationAndMatchers() {
        Invocation invocation = new Invocation(new Object() {}.getClass().getMethod("testMethod", String.class, int.class), new Object[]{"test", 1}, new Location(1, 1), null);
        List<Matcher> matchers = Arrays.asList(new CapturesArguments(), new ArgumentMatcher<String>() {
            @Override
            public boolean matches(Object argument) {
                return "test".equals(argument);
            }
        }, new ArgumentMatcher<Integer>() {
            @Override
            public boolean matches(Object argument) {
                return 1 == argument;
            }
        });
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Method method = invocationMatcher.getMethod();
        Assert.assertEquals(method.getName(), "testMethod");
        Assert.assertEquals(method.getParameterTypes()[0].getTypeName(), "java.lang.String");
        Assert.assertEquals(method.getParameterTypes()[1].getTypeName(), "int");

        Invocation expectedInvocation = invocation;
        Invocation actualInvocation = invocationMatcher.getInvocation();
        Assert.assertEquals(expectedInvocation, actualInvocation);

        List<Matcher> expectedMatchers = matchers;
        List<Matcher> actualMatchers = invocationMatcher.getMatchers();
        Assert.assertEquals(expectedMatchers, actualMatchers);

        String expectedToString = String.format("%s(%s)", invocation.getMethod().getName(), Arrays.toString(invocation.getArguments()));
        String actualToString = invocationMatcher.toString();
        Assert.assertEquals(expectedToString, actualToString);

        Assert.assertTrue(invocationMatcher.matches(invocation));
    }

    @Test
    public void testInvocationMatcherMatches() {
        Invocation invocation1 = new Invocation(new Object() {}.getClass().getMethod("testMethod", String.class, int.class), new Object[]{"test", 1}, new Location(1, 1), null);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Arrays.asList(new ArgumentMatcher<String>() {
            @Override
            public boolean matches(Object argument) {
                return "test".equals(argument);
            }
        }, new ArgumentMatcher<Integer>() {
            @Override
            public boolean matches(Object argument) {
                return 1 == argument;
            }
        }));

        Invocation invocation2 = new Invocation(new Object() {}.getClass().getMethod("testMethod", String.class, int.class), new Object[]{"test", 1}, new Location(1, 1), null);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Arrays.asList(new ArgumentMatcher<String>() {
            @Override
            public boolean matches(Object argument) {
                return "test".equals(argument);
            }
        }, new ArgumentMatcher<Integer>() {
            @Override
            public boolean matches(Object argument) {
                return 1 == argument;
            }
        }));

        Assert.assertTrue(invocationMatcher1.matches(invocation2));
    }

    @Test
    public void testInvocationMatcherSafelyArgumentsMatch() {
        Invocation invocation = new Invocation(new Object() {}.getClass().getMethod("testMethod", String.class, int.class), new Object[]{"test", 1}, new Location(1, 1), null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.emptyList());

        Object[] actualArguments = new Object[]{new Object(), new Object()};
        boolean result = invocationMatcher.safelyArgumentsMatch(actualArguments);
        Assert.assertFalse(result);

        actualArguments = new Object[]{"test", 1};
        result = invocationMatcher.safelyArgumentsMatch(actualArguments);
        Assert.assertTrue(result);
    }

    @Test(expected = Throwable.class)
    public void testInvocationMatcherSafelyArgumentsMatchWithException() {
        Invocation invocation = new Invocation(new Object() {}.getClass().getMethod("testMethod", String.class, int.class), new Object[]{"test", 1}, new Location(1, 1), null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.emptyList());

        Object[] actualArguments = new Object[]{new Throwable()};
        invocationMatcher.safelyArgumentsMatch(actualArguments);
    }

    @Test
    public void testInvocationMatcherHasSimilarMethod() {
        Invocation invocation1 = new Invocation(new Object() {}.getClass().getMethod("testMethod", String.class, int.class), new Object[]{"test", 1}, new Location(1, 1), null);
        Invocation invocation2 = new Invocation(new Object() {}.getClass().getMethod("testMethod", int.class), new Object[]{1}, new Location(1, 1), null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1, Collections.emptyList());

        boolean result = invocationMatcher.hasSimilarMethod(invocation2);
        Assert.assertFalse(result);
    }

    @Test
    public void testInvocationMatcherHasSameMethod() {
        Invocation invocation1 = new Invocation(new Object() {}.getClass().getMethod("testMethod", String.class, int.class), new Object[]{"test", 1}, new Location(1, 1), null);
        Invocation invocation2 = new Invocation(new Object() {}.getClass().getMethod("testMethod", String.class, int.class), new Object[]{"test", 1}, new Location(1, 1), null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1, Collections.emptyList());

        boolean result = invocationMatcher.hasSameMethod(invocation2);
        Assert.assertTrue(result);
    }

    @Test
    public void testInvocationMatcherToString() {
        Invocation invocation = new Invocation(new Object() {}.getClass().getMethod("testMethod", String.class, int.class), new Object[]{"test", 1}, new Location(1, 1), null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.emptyList());

        String expectedToString = invocation.toString();
        String actualToString = invocationMatcher.toString();
        Assert.assertEquals(expectedToString, actualToString);

        PrintSettings printSettings = new PrintSettings();
        expectedToString = invocation.toString(printSettings);
        actualToString = invocationMatcher.toString(printSettings);
        Assert.assertEquals(expectedToString, actualToString);
    }

    @Test
    public void testInvocationMatcherCaptureArgumentsFrom() {
        Invocation invocation = new Invocation(new Object() {}.getClass().getMethod("testMethod", String.class, int.class), new Object[]{"test", 1}, new Location(1, 1), null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(new CapturesArguments()));

        invocationMatcher.captureArgumentsFrom(invocation);

        Object capturedArgument = invocationMatcher.getMatchers().get(0).getValue();
        Assert.assertEquals("test", capturedArgument);
    }
}
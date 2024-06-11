import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsComparator;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithInvocationAndEmptyMatcherList() {
        Invocation invocation = new Invocation(new Location(1, 1), "testMethod", new Object[0], new Class[0]);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Method method = invocationMatcher.getMethod();
        Assert.assertNotNull(method);
        Assert.assertEquals("testMethod", method.getName());
    }

    @Test
    public void testConstructorWithInvocationAndMatcherList() {
        Invocation invocation = new Invocation(new Location(1, 1), "testMethod", new Object[]{"arg1"}, new Class[]{String.class});
        List<Matcher> matchers = Arrays.asList(new Equals("arg1", "expectedValue"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Method method = invocationMatcher.getMethod();
        Assert.assertNotNull(method);
        Assert.assertEquals("testMethod", method.getName());

        List<Matcher> matchersFromInvocationMatcher = invocationMatcher.getMatchers();
        Assert.assertEquals(matchers, matchersFromInvocationMatcher);
    }

    @Test
    public void testMatches() {
        Invocation invocation1 = new Invocation(new Location(1, 1), "testMethod", new Object[]{"arg1"}, new Class[]{String.class});
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);

        Invocation invocation2 = new Invocation(new Location(1, 1), "testMethod", new Object[]{"arg1"}, new Class[]{String.class});
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        Assert.assertTrue(invocationMatcher1.matches(invocationMatcher2));

        invocation2.setVerified(true);
        Assert.assertFalse(invocationMatcher1.matches(invocationMatcher2));

        Invocation invocation3 = new Invocation(new Location(1, 1), "testMethod", new Object[]{"arg1"}, new Class[]{Integer.class});
        InvocationMatcher invocationMatcher3 = new InvocationMatcher(invocation3);
        Assert.assertFalse(invocationMatcher1.matches(invocationMatcher3));

        Invocation invocation4 = new Invocation(new Location(1, 1), "testMethod2", new Object[]{"arg1"}, new Class[]{String.class});
        InvocationMatcher invocationMatcher4 = new InvocationMatcher(invocation4);
        Assert.assertFalse(invocationMatcher1.matches(invocationMatcher4));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Invocation invocation = new Invocation(new Location(1, 1), "testMethod", new Object[]{"arg1"}, new Class[]{String.class});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        List<Object> actualArgs = Arrays.asList(new Object[]{"actualArg1"});
        Assert.assertTrue(invocationMatcher.safelyArgumentsMatch(actualArgs.toArray()));

        try {
            invocationMatcher.safelyArgumentsMatch((Object[]) null);
            Assert.fail();
        } catch (Throwable e) {
            //expected
        }

        try {
            invocationMatcher.safelyArgumentsMatch(new Object[0]);
            Assert.fail();
        } catch (Throwable e) {
            //expected
        }

        try {
            invocationMatcher.safelyArgumentsMatch(new Object[]{new Object() {}});
            Assert.fail();
        } catch (Throwable e) {
            //expected
        }
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation1 = new Invocation(new Location(1, 1), "testMethod1", new Object[]{"arg1"}, new Class[]{String.class});
        Invocation invocation2 = new Invocation(new Location(1, 1), "testMethod1", new Object[]{"arg1"}, new Class[]{String.class});
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        Assert.assertTrue(invocationMatcher1.hasSimilarMethod(invocationMatcher2));

        Invocation invocation3 = new Invocation(new Location(1, 1), "testMethod2", new Object[]{"arg1"}, new Class[]{String.class});
        InvocationMatcher invocationMatcher3 = new InvocationMatcher(invocation3);
        Assert.assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher3));

        Invocation invocation4 = new Invocation(new Location(1, 1), "testMethod1", new Object[]{"arg1"}, new Class[]{Integer.class});
        InvocationMatcher invocationMatcher4 = new InvocationMatcher(invocation4);
        Assert.assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher4));

        Invocation invocation5 = new Invocation(new Location(1, 1), "testMethod1", new Object[]{"arg1"}, new Class[]{String.class});
        invocation5.setVerified(true);
        InvocationMatcher invocationMatcher5 = new InvocationMatcher(invocation5);
        Assert.assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher5));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = new Invocation(new Location(1, 1), "testMethod1", new Object[]{"arg1"}, new Class[]{String.class});
        Invocation invocation2 = new Invocation(new Location(1, 1), "testMethod1", new Object[]{"arg1"}, new Class[]{String.class});
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        Method method1 = invocationMatcher1.getMethod();
        Method method2 = invocationMatcher2.getMethod();

        // Avoid unnecessary cloning
        Assert.assertEquals(method1, method2);
    }

    @Test(expected = AssertionError.class)
    public void testToStringWithNullPrintSettings() {
        new InvocationMatcher(new Invocation(new Location(1, 1), "testMethod", new Object[0], new Class[0]))
                .toString(null);
    }

    @Test
    public void testToString() {
        Invocation invocation = new Invocation(new Location(1, 1), "testMethod", new Object[]{"arg1"}, new Class[]{String.class});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        PrintSettings printSettings = new PrintSettings();
        String result = invocationMatcher.toString(printSettings);

       Assert.assertNotNull(result);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = new Invocation(new Location(1, 1), "testMethod", new Object[]{"arg1"}, new Class[]{String.class});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        CapturesArguments capturesArgumentsMock = new CapturesArguments() {
            @Override
            public void captureFrom(Object argument) {
                capturedArgument = argument;
            }
        };

        invocationMatcher.captureArgumentsFrom(invocation);

        Assert.assertNotNull(capturesArgumentsMock.capturedArgument);
        Assert.assertEquals("arg1", capturesArgumentsMock.capturedArgument);
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new ArrayList<>();
        Invocation invocation1 = new Invocation(new Location(1, 1), "testMethod1", new Object[]{"arg1"}, new Class[]{String.class});
        Invocation invocation2 = new Invocation(new Location(1, 1), "testMethod1", new Object[]{"arg2"}, new Class[]{Integer.class});
        invocations.add(invocation1);
        invocations.add(invocation2);

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        InvocationMatcher invocationMatcher1 = invocationMatchers.get(0);
        InvocationMatcher invocationMatcher2 = invocationMatchers.get(1);

        Assert.assertEquals(invocation1, invocationMatcher1.getInvocation());
        Assert.assertEquals(invocation2, invocationMatcher2.getInvocation());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateFromWithNullInvocationsList() {
        InvocationMatcher.createFrom(null);
    }

    @Test
    public void testCreateFromWithEmptyInvocationsList() {
        List<Invocation> invocations = Collections.emptyList();
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        Assert.assertEquals(0, invocationMatchers.size());
    }
}
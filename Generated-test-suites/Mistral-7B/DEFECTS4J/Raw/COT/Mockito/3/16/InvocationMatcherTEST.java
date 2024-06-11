import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.Location;
import org.mockito.internal.matchers.ArgumentsComparator;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.matchers.MatcherDecorator;
import org.mockito.invocation.DescribedInvocation;
import java.lang.reflect.Method;
import java.util.*;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcher_constructorWithInvocationAndEmptyMatchers() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Method method = invocation.getMethod();
        Assert.assertNotNull(invocationMatcher.getMatchers());
        Assert.assertEquals(0, invocationMatcher.getMatchers().size());
        Assert.assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testInvocationMatcher_constructorWithInvocationAndMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(eq("Mockito"), eq("test"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Method method = invocation.getMethod();
        Assert.assertEquals(matchers, invocationMatcher.getMatchers());
        Assert.assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testInvocationMatcher_matches() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Arrays.asList(eq("Mockito")));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Arrays.asList(eq("Mockito")));
        Method method = invocation1.getMethod();

        Assert.assertTrue(invocationMatcher1.matches(invocationMatcher2));
        Assert.assertFalse(invocationMatcher1.matches(mock(Invocation.class)));
        Assert.assertFalse(invocationMatcher1.matches(new InvocationMatcher(invocation2, Arrays.asList(eq("AnotherMock")))));
    }

    @Test
    public void testInvocationMatcher_safelyArgumentsMatch() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Object[] actualArgs = new Object[]{"Mockito", 1, 2.0};

        Assert.assertTrue(invocationMatcher.safelyArgumentsMatch(actualArgs));

        try {
            // throw an exception to test exception handling
            throw new RuntimeException();
        } catch (RuntimeException e) {
            Assert.assertFalse(invocationMatcher.safelyArgumentsMatch(actualArgs));
        }
    }

    @Test
    public void testInvocationMatcher_hasSimilarMethod() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);
        Method method1 = invocation1.getMethod();
        Method method2 = invocation2.getMethod();

        Assert.assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher2));

        // same method name, same mock, unverified
        invocationMatcher1 = new InvocationMatcher(invocation1, Arrays.asList(eq(method1)));
        invocationMatcher2 = new InvocationMatcher(invocation2, Arrays.asList(eq(method2)));
        Assert.assertTrue(invocationMatcher1.hasSimilarMethod(invocationMatcher2));

        // different mock
        invocationMatcher2 = new InvocationMatcher(new Invocation(invocation1.getMock(), method1, new Object[0], new Object[0], null, null));
        Assert.assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher2));

        // different method name
        Method method3 = mock(Method.class);
        invocationMatcher2 = new InvocationMatcher(invocation2, Arrays.asList(eq(method3)));
        Assert.assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testInvocationMatcher_hasSameMethod() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);
        Method method1 = invocation1.getMethod();
        Method method2 = invocation2.getMethod();

        Assert.assertFalse(invocationMatcher1.hasSameMethod(invocationMatcher2));

        // same method name, same mock, unverified
        invocationMatcher1 = new InvocationMatcher(invocation1, Arrays.asList(eq(method1)));
        invocationMatcher2 = new InvocationMatcher(invocation2, Arrays.asList(eq(method2)));
        Assert.assertTrue(invocationMatcher1.hasSameMethod(invocationMatcher2));

        // method names are not equal
        method1 = mock(Method.class);
        method2 = mock(Method.class);
        invocationMatcher2 = new InvocationMatcher(invocation2, Arrays.asList(eq(method2)));
        Assert.assertFalse(invocationMatcher1.hasSameMethod(invocationMatcher2));

        // number of parameters does not match
        Class[] params1 = method1.getParameterTypes();
        Class[] params2 = new Class[params1.length + 1];
        System.arraycopy(params1, 0, params2, 0, params1.length);
        method2.setParameterTypes(params2);
        invocationMatcher2 = new InvocationMatcher(invocation2, Arrays.asList(eq(method2)));
        Assert.assertFalse(invocationMatcher1.hasSameMethod(invocationMatcher2));

        // parameter types do not match
        Class[] params1 = method1.getParameterTypes();
        Class[] params2 = new Class[]{Integer.TYPE};
        method2.setParameterTypes(params2);
        invocationMatcher2 = new InvocationMatcher(invocation2, Arrays.asList(eq(method2)));
        Assert.assertFalse(invocationMatcher1.hasSameMethod(invocationMatcher2));
    }

    @Test
    public void testInvocationMatcher_captureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Matcher matcher = eq("Mockito");
        CapturesArguments capturesArguments = mock(CapturesArguments.class);

        invocationMatcher.captureArgumentsFrom(invocation);
        Matcher actualMatcher = invocationMatcher.getMatchers().get(0);
        Assert.assertSame(matcher, actualMatcher);
        Assert.assertTrue(capturesArguments.captured.isEmpty());

        matcher = argThat(new MatcherDecorator<Object>() {
            @Override
            public Matcher matcher() {
                return eq("Argument");
            }
        });
        invocationMatcher.captureArgumentsFrom(invocation);
        actualMatcher = invocationMatcher.getMatchers().get(1);
        Assert.assertSame(matcher, actualMatcher);
        Assert.assertTrue(capturesArguments.captured.isEmpty());

        Object[] arguments = new Object[]{"Mockito", 1, "Argument"};
        invocationMatcher.captureArgumentsFrom(new Invocation(mock(Object.class), invocation.getMethod(), arguments, null, null, null));
        Assert.assertEquals(Arrays.asList("Mockito", 1, "Argument"), capturesArguments.captured);
    }

    @Test
    public void testInvocationMatcher_createFrom() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(Arrays.asList(invocation1, invocation2));
        InvocationMatcher invocationMatcher1 = invocationMatchers.get(0);
        InvocationMatcher invocationMatcher2 = invocationMatchers.get(1);

        Assert.assertEquals(invocation1, invocationMatcher1.getInvocation());
        Assert.assertEquals(invocation2, invocationMatcher2.getInvocation());

        Assert.assertEquals(invocation1.getMethod(), invocationMatcher1.getMethod());
        Assert.assertEquals(invocation2.getMethod(), invocationMatcher2.getMethod());

        Assert.assertEquals(Arrays.asList(), invocationMatcher1.getMatchers());
        Assert.assertEquals(Arrays.asList(), invocationMatcher2.getMatchers());
    }

    @Test(expected = IllegalStateException.class)
    public void testInvocationMatcher_createFrom_verifiedInvocations() {
        Invocation invocation1 = mock(Invocation.class);
        invocation1.proceed();
        Invocation invocation2 = mock(Invocation.class);
        invocation2.proceed();
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(Arrays.asList(invocation1, invocation2));
    }
}
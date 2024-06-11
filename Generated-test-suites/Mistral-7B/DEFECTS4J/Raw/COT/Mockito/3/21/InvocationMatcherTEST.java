import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsComparator;
import org.mockito.internal.matchers.CapturesArguments;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithInvocationAndEmptyList() {
        Invocation invocation = new MockitoTestUtils().mockInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Method method = invocation.getMethod();
        Assert.assertEquals(method.getName(), invocationMatcher.getMethod().getName());

        List<Matcher> matchers = invocationMatcher.getMatchers();
        Assert.assertNotNull(matchers);
        Assert.assertTrue(matchers.isEmpty());
    }

    @Test
    public void testConstructorWithInvocationAndMatchers() {
        Invocation invocation = new MockitoTestUtils().mockInvocation();
        List<Matcher> matchers = Collections.singletonList(new CapturesArguments());
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Assert.assertEquals(invocation.getMethod(), invocationMatcher.getMethod());
        Assert.assertSame(invocation.getInvocation(), invocationMatcher.getInvocation());
        Assert.assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testMatches() {
        Invocation invocation1 = new MockitoTestUtils().mockInvocation();
        Invocation invocation2 = new MockitoTestUtils().mockInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        Assert.assertTrue(invocationMatcher.matches(invocation1));
        Assert.assertFalse(invocationMatcher.matches(invocation2));

        List<Matcher> matchers = Arrays.asList(new CapturesArguments(), new CapturesArguments());
        invocationMatcher = new InvocationMatcher(invocation1, matchers);

        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, matchers);
        Assert.assertTrue(invocationMatcher.matches(invocationMatcher2));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Invocation invocation = new MockitoTestUtils().mockInvocation();
        Object[] actualArgs = invocation.getArguments();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Assert.assertTrue(invocationMatcher.safelyArgumentsMatch(actualArgs));
    }

    @Test(expected = Throwable.class)
    public void testSafelyArgumentsMatchThrowsException() {
        Invocation invocation = new MockitoTestUtils().mockInvocation();
        Object[] actualArgs = new Object[0];
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        invocationMatcher.safelyArgumentsMatch(actualArgs);
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation1 = new MockitoTestUtils().mockInvocation();
        Invocation invocation2 = new MockitoTestUtils().mockInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        Assert.assertFalse(invocationMatcher.hasSimilarMethod(invocation2));

        Method method1 = invocation1.getMethod();
        Method method2 = new MockitoTestUtils().createMockMethod(method1);
        InvocationMatcher invocationMatcherWithSameMethod = new InvocationMatcher(invocation1, method2.getParameterTypes());

        Assert.assertTrue(invocationMatcherWithSameMethod.hasSimilarMethod(invocation2));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = new MockitoTestUtils().mockInvocation();
        Invocation invocation2 = new MockitoTestUtils().mockInvocation();

        Method method1 = invocation1.getMethod();
        Method method2 = invocation2.getMethod();

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        Assert.assertFalse(invocationMatcher.hasSameMethod(invocationMatcher2));

        if (method1.getName().equals(method2.getName())) {
            Class[] params1 = method1.getParameterTypes();
            Class[] params2 = method2.getParameterTypes();
            if (params1.length == params2.length) {
                for (int i = 0; i < params1.length; i++) {
                    if (params1[i] != params2[i]) {
                        return;
                    }
                }
                InvocationMatcher invocationMatcherWithSameMethod = new InvocationMatcher(invocation1);
                Assert.assertTrue(invocationMatcherWithSameMethod.hasSameMethod(invocationMatcher2));
            }
        }

        Assert.assertFalse(invocationMatcher.hasSameMethod(invocationMatcher2));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = new MockitoTestUtils().mockInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        CapturesArguments capturesArguments = new CapturesArguments();
        invocationMatcher.captureArgumentsFrom(invocation);

        Object capturedArgument = capturesArguments.getValue();
        Assert.assertNotNull(capturedArgument);
        Assert.assertEquals(invocation.getArgumentAt(0, Object.class), capturedArgument);
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = Arrays.asList(new MockitoTestUtils().mockInvocation(), new MockitoTestUtils().mockInvocation());
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        Assert.assertEquals(2, invocationMatchers.size());
    }
}
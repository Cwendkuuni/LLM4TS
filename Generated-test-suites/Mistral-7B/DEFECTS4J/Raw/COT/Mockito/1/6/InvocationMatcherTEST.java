import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.DescribedInvocation;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherConstructorWithInvocationAndEmptyList() {
        Invocation mockInvocation = MockitoTestUtils.createMockInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation);

        Method method = invocationMatcher.getMethod();
        Assert.assertNotNull(method);
        Assert.assertSame(mockInvocation.getMethod(), method);

        List<Matcher> matchers = invocationMatcher.getMatchers();
        Assert.assertEquals(Collections.emptyList(), matchers);
    }

    @Test
    public void testInvocationMatcherConstructorWithInvocationAndListOfMatchers() {
        Invocation mockInvocation = MockitoTestUtils.createMockInvocation();
        List<Matcher> matchers = Arrays.asList(MockitoTestUtils.createMockMatcher(), MockitoTestUtils.createMockMatcher());
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation, matchers);

        Method method = invocationMatcher.getMethod();
        Assert.assertNotNull(method);
        Assert.assertSame(mockInvocation.getMethod(), method);

        List<Matcher> actualMatchers = invocationMatcher.getMatchers();
        Assert.assertSame(matchers, actualMatchers);
    }

    @Test
    public void testInvocationMatcherMatches() {
        Invocation mockInvocation = MockitoTestUtils.createMockInvocation();
        List<Matcher> matchers = Arrays.asList(MockitoTestUtils.createMockMatcher(), MockitoTestUtils.createMockMatcher());
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation, matchers);

        Invocation actualInvocation = MockitoTestUtils.createMockInvocation();
        boolean result = invocationMatcher.matches(actualInvocation);

        Assert.assertFalse(result);

        Invocation candidateInvocation = new MockitoTestUtils.DummyInvocation(mockInvocation.getMock(), mockInvocation.getMethod(), new Object[0], mockInvocation.getLocation());
        result = invocationMatcher.matches(candidateInvocation);

        Assert.assertTrue(result);
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Invocation mockInvocation = MockitoTestUtils.createMockInvocation();
        List<Matcher> matchers = Arrays.asList(MockitoTestUtils.createMockMatcher(), MockitoTestUtils.createMockMatcher());
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation, matchers);

        Object[] actualArgs = new Object[2];
        actualArgs[0] = "test";
        actualArgs[1] = 1;

        boolean result = invocationMatcher.safelyArgumentsMatch(actualArgs);

        Assert.assertTrue(result);

        try {
            invocationMatcher.safelyArgumentsMatch(new Object[0]);
            Assert.fail();
        } catch (Throwable e) {
            // expected
        }
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation mockInvocation1 = MockitoTestUtils.createMockInvocation();
        Invocation mockInvocation2 = MockitoTestUtils.createMockInvocation();

        Method method1 = mockInvocation1.getMethod();
        Method method2 = mockInvocation2.getMethod();

        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation1);

        boolean result = invocationMatcher.hasSimilarMethod(new MockitoTestUtils.DummyInvocation(mockInvocation2.getMock(), method2, new Object[0], mockInvocation1.getLocation()));

        Assert.assertFalse(result);

        result = invocationMatcher.hasSimilarMethod(new MockitoTestUtils.DummyInvocation(mockInvocation2.getMock(), method1, new Object[0], mockInvocation1.getLocation()));

        Assert.assertFalse(result);

        result = invocationMatcher.hasSimilarMethod(new MockitoTestUtils.DummyInvocation(mockInvocation1.getMock(), method1, new Object[0], mockInvocation1.getLocation()));

        Assert.assertTrue(result);

        result = invocationMatcher.hasSimilarMethod(new MockitoTestUtils.DummyInvocation(mockInvocation1.getMock(), method1, new Object[]{1, 2}, mockInvocation1.getLocation()));

        Assert.assertTrue(result);
    }

    @Test
    public void testHasSameMethod() {
        Invocation mockInvocation1 = MockitoTestUtils.createMockInvocation();
        Invocation mockInvocation2 = MockitoTestUtils.createMockInvocation();

        Method method1 = mockInvocation1.getMethod();
        Method method2 = mockInvocation2.getMethod();

        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation1);

        boolean result = invocationMatcher.hasSameMethod(new MockitoTestUtils.DummyInvocation(mockInvocation2.getMock(), method1, new Object[0], mockInvocation1.getLocation()));

        Assert.assertTrue(result);

        result = invocationMatcher.hasSameMethod(new MockitoTestUtils.DummyInvocation(mockInvocation2.getMock(), method2, new Object[0], mockInvocation1.getLocation()));

        Assert.assertFalse(result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCaptureArgumentsFromVarargInvocation() {
        Invocation mockInvocation = MockitoTestUtils.createMockInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation);

        invocationMatcher.captureArgumentsFrom(mockInvocation);
    }
}
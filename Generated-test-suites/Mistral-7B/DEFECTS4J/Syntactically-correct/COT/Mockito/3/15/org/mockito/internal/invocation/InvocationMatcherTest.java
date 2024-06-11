import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsComparator;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.invocation.DescribedInvocation;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class InvocationMatcherTest {

    @Test
    public void testConstructor_InvocationAndEmptyMatchers() {
        Invocation invocation = MockitoTestUtils.createMockInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Assert.assertNotNull(invocationMatcher.getMethod());
        Assert.assertNotNull(invocationMatcher.getInvocation());
        Assert.assertNotNull(invocationMatcher.getMatchers());
        Assert.assertEquals(0, invocationMatcher.getMatchers().size());
    }

    @Test
    public void testConstructor_InvocationAndCustomMatchers() {
        Invocation invocation = MockitoTestUtils.createMockInvocation();
        List<Matcher> matchers = Arrays.asList(eq("Test"), isA(String.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Assert.assertNotNull(invocationMatcher.getMethod());
        Assert.assertNotNull(invocationMatcher.getInvocation());
        Assert.assertNotNull(invocationMatcher.getMatchers());
        Assert.assertEquals(2, invocationMatcher.getMatchers().size());
    }

    @Test
    public void testGetMethod() {
        Invocation invocation = MockitoTestUtils.createMockInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Method method = invocationMatcher.getMethod();
        Assert.assertNotNull(method);
        Assert.assertEquals("testMethod", method.getName());
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = MockitoTestUtils.createMockInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation invocationFromMatcher = invocationMatcher.getInvocation();
        Assert.assertSame(invocation, invocationFromMatcher);
    }

    @Test
    public void testGetMatchers() {
        Invocation invocation = MockitoTestUtils.createMockInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        List<Matcher> matchers = invocationMatcher.getMatchers();
        Assert.assertNotNull(matchers);
        Assert.assertEquals(0, matchers.size());

        List<Matcher> customMatchers = Arrays.asList(eq("Test"), isA(String.class));
        InvocationMatcher invocationMatcherWithCustomMatchers = new InvocationMatcher(invocation, customMatchers);

        matchers = invocationMatcherWithCustomMatchers.getMatchers();
        Assert.assertEquals(2, matchers.size());
    }

    @Test
    public void testToString() {
        Invocation invocation = MockitoTestUtils.createMockInvocation();
        List<Matcher> matchers = Arrays.asList(eq("Test"), isA(String.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        String toString = invocationMatcher.toString();
        Assert.assertNotNull(toString);
        Assert.assertTrue(toString.contains("Test"));
        Assert.assertTrue(toString.contains("String"));
    }

    @Test(expected = AssertionError.class)
    public void testMatches_DifferentMock() {
        Invocation invocation1 = MockitoTestUtils.createMockInvocation();
        Invocation invocation2 = MockitoTestUtils.createMockInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1, Collections.<Matcher>emptyList());

        InvocationMatcher otherInvocationMatcher = new InvocationMatcher(invocation2, Collections.<Matcher>emptyList());

        invocationMatcher.matches(otherInvocationMatcher);
    }

    @Test
    public void testMatches_DifferentMethods() {
        Invocation invocation1 = MockitoTestUtils.createMockInvocation(String.class, "testMethod1", new Object[]{"arg1"});
        Invocation invocation2 = MockitoTestUtils.createMockInvocation(int.class, "testMethod2", new Object[]{1});
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Collections.<Matcher>emptyList());
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.<Matcher>emptyList());

        Assert.assertFalse(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testMatches_SameInvocations() {
        Invocation invocation = MockitoTestUtils.createMockInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.<Matcher>emptyList());

        Invocation invocationCopy = invocation;
        InvocationMatcher invocationMatcherCopy = new InvocationMatcher(invocationCopy, Collections.<Matcher>emptyList());

        Assert.assertTrue(invocationMatcher.matches(invocationMatcherCopy));
    }

    @Test
    public void testMatches_SameInvocationsDifferentOrder() {
        Invocation invocation1 = MockitoTestUtils.createMockInvocation();
        Invocation invocation2 = MockitoTestUtils.createMockInvocation();
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Collections.<Matcher>emptyList());
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.<Matcher>emptyList());

        Invocation tempInvocation = invocation1;
        invocation1 = invocation2;
        invocation2 = tempInvocation;

        Assert.assertTrue(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testMatches_SameInvocationsWithSameArguments() {
        Invocation invocation = MockitoTestUtils.createMockInvocation("Test", "arg1");
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation, Collections.<Matcher>emptyList());

        Invocation invocationCopy = invocation;
        Object[] argumentsCopy = invocation.getArguments();
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation, Arrays.asList(eq("Test"), eq("arg1")));

        invocation.setArguments(argumentsCopy);

        Assert.assertTrue(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testMatches_SameInvocationsWithDifferentArguments() {
        Invocation invocation = MockitoTestUtils.createMockInvocation("Test", "arg1");
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation, Arrays.asList(eq("Test"), eq("arg1")));

        Object[] argumentsDifferent = new Object[]{"Test2", "arg2"};
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation, Arrays.asList(eq("Test"), eq("arg1")));
        invocation.setArguments(argumentsDifferent);

        Assert.assertFalse(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testMatches_SameInvocationsWithSameArgumentsButDifferentMatchers() {
        Invocation invocation = MockitoTestUtils.createMockInvocation("Test", "arg1");
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation, Arrays.asList(eq("Test2"), eq("arg1")));

        Object[] argumentsSame = invocation.getArguments();
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation, Arrays.asList(eq("Test"), eq("arg1")));
        invocation.setArguments(argumentsSame);

        Assert.assertFalse(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testMatches_InvocationWithVarargs() {
        Invocation invocation = MockitoTestUtils.createMockInvocation("Test", "arg1", new Object[]{"arg2", "arg3"});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(eq("Test"), eq("arg1"), isA(String[].class)));

        Object[] argumentsSame = invocation.getRawArguments();
        InvocationMatcher invocationMatcherCopy = new InvocationMatcher(invocation, Arrays.asList(eq("Test"), eq("arg1"), isA(String[].class)));
        invocation.setRawArguments(argumentsSame);

        Assert.assertTrue(invocationMatcher.matches(invocationMatcherCopy));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Invocation invocation = MockitoTestUtils.createMockInvocation("Test", "arg1");
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.<Matcher>emptyList());

        Object[] argumentsSame = invocation.getArguments();
        Assert.assertTrue(invocationMatcher.safelyArgumentsMatch(argumentsSame));
    }

    @Test(expected = AssertionError.class)
    public void testSafelyArgumentsMatch_ThrowsException() {
        Invocation invocation = MockitoTestUtils.createMockInvocation("Test", "arg1");
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.<Matcher>emptyList());

        Object[] argumentsDifferent = new Object[]{"Test2", "arg2"};
        invocation.setArguments(argumentsDifferent);

        invocationMatcher.safelyArgumentsMatch(argumentsDifferent);
    }

    @Test
    public void testHasSimilarMethod_SameMethod() {
        Invocation invocation1 = MockitoTestUtils.createMockInvocation();
        Invocation invocation2 = MockitoTestUtils.createMockInvocation();
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Collections.<Matcher>emptyList());
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.<Matcher>emptyList());

        Assert.assertTrue(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testHasSimilarMethod_DifferentMethods() {
        Invocation invocation1 = MockitoTestUtils.createMockInvocation("Test", "arg1");
        Invocation invocation2 = MockitoTestUtils.createMockInvocation("Test2", "arg1");
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Collections.<Matcher>emptyList());
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.<Matcher>emptyList());

        Assert.assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testHasSameMethod_SameMethods() {
        Invocation invocation1 = MockitoTestUtils.createMockInvocation();
        Invocation invocation2 = MockitoTestUtils.createMockInvocation();
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Collections.<Matcher>emptyList());
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.<Matcher>emptyList());

        Method method1 = invocationMatcher1.getMethod();
        Method method2 = invocationMatcher2.getMethod();

        Assert.assertTrue(invocationMatcher1.hasSameMethod(invocationMatcher2));
        Assert.assertEquals(method1.getName(), method2.getName());
    }

    @Test
    public void testHasSameMethod_DifferentMethods() {
        Invocation invocation1 = MockitoTestUtils.createMockInvocation("Test", "arg1");
        Invocation invocation2 = MockitoTestUtils.createMockInvocation("Test2", "arg1");
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Collections.<Matcher>emptyList());
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.<Matcher>emptyList());

        Assert.assertFalse(invocationMatcher1.hasSameMethod(invocationMatcher2));
    }

    @Test
    public void testCaptureArgumentsFrom_InvocationWithVarargs() {
        Invocation invocation = MockitoTestUtils.createMockInvocation("Test", "arg1", new Object[]{"arg2", "arg3"});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(eq("Test"), isA(String.class)));

        CapturesArguments capturesArgumentsMock = Mockito.mock(CapturesArguments.class);
        invocationMatcher.captureArgumentsFrom(invocation);

        Object[] argumentsCaptured = capturesArgumentsMock.getArguments();
        Assert.assertEquals(2, argumentsCaptured.length);
        Assert.assertEquals("arg2", argumentsCaptured[0]);
        Assert.assertEquals("arg3", argumentsCaptured[1]);
    }

    @Test
    public void testCreateFrom_EmptyList() {
        List<Invocation> invocations = Collections.emptyList();
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        Assert.assertEquals(0, invocationMatchers.size());
    }

    @Test
    public void testCreateFrom_SingleInvocation() {
        Invocation invocation = MockitoTestUtils.createMockInvocation();
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(Arrays.asList(invocation));

        InvocationMatcher invocationMatcher = invocationMatchers.get(0);
        Assert.assertNotNull(invocationMatcher.getMethod());
        Assert.assertNotNull(invocationMatcher.getInvocation());
        Assert.assertEquals(0, invocationMatcher.getMatchers().size());
    }

    @Test
    public void testCreateFrom_MultipleInvocations() {
        Invocation invocation1 = MockitoTestUtils.createMockInvocation();
        Invocation invocation2 = MockitoTestUtils.createMockInvocation();
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(Arrays.asList(invocation1, invocation2));

        InvocationMatcher invocationMatcher1 = invocationMatchers.get(0);
        InvocationMatcher invocationMatcher2 = invocationMatchers.get(1);

        Assert.assertNotNull(invocationMatcher1.getMethod());
        Assert.assertNotNull(invocationMatcher1.getInvocation());
        Assert.assertEquals(0, invocationMatcher1.getMatchers().size());

        Assert.assertNotNull(invocationMatcher2.getMethod());
        Assert.assertNotNull(invocationMatcher2.getInvocation());
        Assert.assertEquals(0, invocationMatcher2.getMatchers().size());
    }
}
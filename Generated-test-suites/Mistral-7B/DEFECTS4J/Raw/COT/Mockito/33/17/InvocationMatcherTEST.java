import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.internal.reporting.PrintableInvocation;
import org.mockito.internal.util.reflection.Whitebox;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherConstructorWithInvocationAndEmptyMatcherList() {
        Invocation invocation = createMockInvocation("someMethod", new Object[0]);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Method method = invocationMatcher.getMethod();
        Assert.assertEquals("someMethod", method.getName());
        List<Matcher> matchers = invocationMatcher.getMatchers();
        Assert.assertEquals(0, matchers.size());
    }

    @Test
    public void testInvocationMatcherConstructorWithInvocationAndMatcherList() {
        Invocation invocation = createMockInvocation("someMethod", new Object[]{"arg1", "arg2"});
        List<Matcher> matchers = Arrays.asList(new CapturesArguments("arg1"), new CapturesArguments("arg2"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Method method = invocationMatcher.getMethod();
        Assert.assertEquals("someMethod", method.getName());
        List<Matcher> actualMatchers = invocationMatcher.getMatchers();
        Assert.assertEquals(2, actualMatchers.size());
        Assert.assertEquals(matchers.get(0), actualMatchers.get(0));
        Assert.assertEquals(matchers.get(1), actualMatchers.get(1));
    }

    @Test
    public void testInvocationMatcherMatches() {
        Invocation invocation = createMockInvocation("someMethod", new Object[]{"arg1", "arg2"});
        List<Matcher> matchers = Arrays.asList(new CapturesArguments("arg1"), new CapturesArguments("arg2"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Invocation actualInvocation = createMockInvocation("someMethod", new Object[]{"arg1", "arg2"});
        Assert.assertTrue(invocationMatcher.matches(actualInvocation));
    }

    @Test
    public void testInvocationMatcherDoesNotMatchDifferentMethod() {
        Invocation invocation = createMockInvocation("someMethod1", new Object[0]);
        List<Matcher> matchers = Collections.emptyList();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Invocation actualInvocation = createMockInvocation("someMethod2", new Object[0]);
        Assert.assertFalse(invocationMatcher.matches(actualInvocation));
    }

    @Test
    public void testInvocationMatcherDoesNotMatchDifferentMock() {
        Invocation invocation1 = createMockInvocation("someMethod", new Object[0]);
        Invocation invocation2 = createMockInvocation("someMethod", new Object[0]);
        List<Matcher> matchers = Collections.emptyList();
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, matchers);

        Assert.assertFalse(invocationMatcher1.matches(invocation2));
    }

    @Test
    public void testInvocationMatcherDoesNotMatchDifferentArguments() {
        Invocation invocation = createMockInvocation("someMethod", new Object[]{"arg1", "arg2"});
        List<Matcher> matchers = Arrays.asList(new CapturesArguments("arg1"), new CapturesArguments("arg2"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Invocation actualInvocation = createMockInvocation("someMethod", new Object[]{"arg3", "arg4"});
        Assert.assertFalse(invocationMatcher.matches(actualInvocation));
    }

    @Test(expected = Throwable.class)
    public void testArgumentsComparisonThrowsException() {
        Invocation invocation = createMockInvocation("someMethod", new Object[]{"arg1", "arg2"});
        List<Matcher> matchers = Arrays.asList(new CapturesArguments("arg1"), new CapturesArguments("arg2"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Object[] wrongArguments = new Object[]{new Object(), new Object()};
        invocationMatcher.safelyArgumentsMatch(wrongArguments);
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation1 = createMockInvocation("someMethod1", new Object[0]);
        Invocation invocation2 = createMockInvocation("someMethod1", new Object[0]);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        Assert.assertTrue(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testHasSimilarMethodDifferentMethods() {
        Invocation invocation1 = createMockInvocation("someMethod1", new Object[0]);
        Invocation invocation2 = createMockInvocation("someMethod2", new Object[]{"arg1"});
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        Assert.assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testToString() {
        Invocation invocation = createMockInvocation("someMethod", new Object[]{"arg1", "arg2"});
        List<Matcher> matchers = Arrays.asList(new CapturesArguments("arg1"), new CapturesArguments("arg2"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        String expected = "InvocationMatcher(someMethod(arg1, arg2))";
        String actual = invocationMatcher.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = createMockInvocation("someMethod", new Object[]{"arg1", "arg2"});
        List<Matcher> matchers = Arrays.asList(new CapturesArguments("arg1"), new CapturesArguments("arg2"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Object[] arguments = new Object[]{"capturedArg1", "capturedArg2"};
        invocationMatcher.captureArgumentsFrom(createMockInvocation("someMethod", arguments));

        for (Matcher matcher : matchers) {
            if (matcher instanceof CapturesArguments) {
                CapturesArguments capturedArguments = (CapturesArguments) matcher;
                Assert.assertEquals("capturedArg1", capturedArguments.getValue());
                Assert.assertEquals("capturedArg2", capturedArguments.getSecondValue());
            }
        }
    }

    @Test
    public void testCreateFrom() {
        Invocation invocation1 = createMockInvocation("someMethod1", new Object[0]);
        Invocation invocation2 = createMockInvocation("someMethod1", new Object[0]);
        Invocation invocation3 = createMockInvocation("someMethod2", new Object[]{"arg1"});
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(Arrays.asList(invocation1, invocation2, invocation3));

        InvocationMatcher invocationMatcher1 = invocationMatchers.get(0);
        InvocationMatcher invocationMatcher2 = invocationMatchers.get(1);
        InvocationMatcher invocationMatcher3 = invocationMatchers.get(2);

        Method method1 = invocationMatcher1.getMethod();
        Assert.assertEquals("someMethod1", method1.getName());
        Method method2 = invocationMatcher2.getMethod();
        Assert.assertEquals("someMethod1", method2.getName());
        Method method3 = invocationMatcher3.getMethod();
        Assert.assertEquals("someMethod2", method3.getName());

        List<Matcher> matchers1 = invocationMatcher1.getMatchers();
        List<Matcher> matchers2 = invocationMatcher2.getMatchers();
        List<Matcher> matchers3 = invocationMatcher3.getMatchers();

        Assert.assertEquals(0, matchers1.size());
        Assert.assertEquals(0, matchers2.size());
        Assert.assertEquals(1, matchers3.size());
    }

    private Invocation createMockInvocation(String methodName, Object[] arguments) {
        Whitebox.setInternalState(Mockito.mock(Object.class), "invocation", new Invocation(getTestClass(), methodName, new Object[0], new Object[0], new Object[0], new Location(1, 1, 1, 1), PrintSettings.DEFAULT));
        Invocation invocation = Whitebox.getInternalState(Mockito.mock(Object.class), "invocation");
        if (arguments != null) {
            Whitebox.setInternalState(invocation, "arguments", arguments);
        }
        return invocation;
    }

    private Class<?> getTestClass() {
        try {
            return Class.forName("TestClass");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
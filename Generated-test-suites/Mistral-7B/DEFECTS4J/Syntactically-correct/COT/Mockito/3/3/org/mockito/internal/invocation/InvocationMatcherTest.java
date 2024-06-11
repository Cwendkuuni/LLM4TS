import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.invocation.MockMethodInvocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationMatcher.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithInvocationAndEmptyMatcherList() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Assert.assertNotNull(invocationMatcher.getMatchers());
        Assert.assertEquals(0, invocationMatcher.getMatchers().size());
    }

    @Test
    public void testConstructorWithInvocationAndMatcherList() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matcherList = Arrays.asList(ArgumentMatchers.eq("test"), ArgumentMatchers.isA(String.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matcherList);
        Assert.assertEquals(matcherList, invocationMatcher.getMatchers());
    }

    @Test
    public void testGetMethod() {
        Method method = MockMethodInvocation.method("testMethod", String.class, int.class);
        Invocation invocation = new InvocationOnMock(Mockito.mock(Object.class), method, Collections.emptyList());
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Method returnedMethod = invocationMatcher.getMethod();
        Assert.assertNotNull(returnedMethod);
        Assert.assertEquals(method, returnedMethod);
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Assert.assertEquals(invocation, invocationMatcher.getInvocation());
    }

    @Test
    public void testGetMatchers() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq("test"), ArgumentMatchers.isA(String.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Assert.assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testToString() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq("test"), ArgumentMatchers.isA(String.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        String printSettings = new PrintSettings().toString();
        String expected = String.format("%s %s%n", printSettings, Arrays.toString(matchers.toArray()));

        Assert.assertEquals(expected, invocationMatcher.toString());
    }

    @Test
    public void testMatchesWithSameInvocation() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq("test"), ArgumentMatchers.isA(String.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Invocation sameInvocation = Mockito.mock(Invocation.class);
        Mockito.when(sameInvocation.getMock()).thenReturn(invocation.getMock());
        Mockito.when(sameInvocation.getMethod()).thenReturn(invocation.getMethod());
        Mockito.when(sameInvocation.getArguments()).thenReturn(invocation.getArguments());

        Assert.assertTrue(invocationMatcher.matches(sameInvocation));
    }

    @Test
    public void testMatchesWithDifferentInvocation() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq("test"), ArgumentMatchers.isA(String.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Invocation differentInvocation = Mockito.mock(Invocation.class);
        Mockito.when(differentInvocation.getMock()).thenReturn(invocation.getMock());
        Mockito.when(differentInvocation.getMethod()).thenReturn(new Method("testDifferentMethod", String.class, int.class));

        Assert.assertFalse(invocationMatcher.matches(differentInvocation));
    }

    @Test
    public void testSafelyArgumentsMatchWithSameArguments() {
        Object[] arguments = new Object[]{"test1", "test2"};
        InvocationMatcher invocationMatcher = new InvocationMatcher(Mockito.mock(Invocation.class), arguments);

        Assert.assertTrue(invocationMatcher.safelyArgumentsMatch(arguments));
    }

    @Test(expected = Throwable.class)
    public void testSafelyArgumentsMatchWithDifferentArguments() {
        Object[] arguments1 = new Object[]{"test1", "test2"};
        Object[] arguments2 = new Object[]{"test3", "test4"};
        InvocationMatcher invocationMatcher = new InvocationMatcher(Mockito.mock(Invocation.class), arguments1);

        invocationMatcher.safelyArgumentsMatch(arguments2);
    }

    @Test
    public void testHasSimilarMethodWithSameInvocation() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Method method = MockMethodInvocation.method("testMethod", String.class, int.class);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Collections.emptyList());

        Invocation invocation2 = Mockito.mock(Invocation.class);
        Mockito.when(invocation2.getMethod()).thenReturn(method);

        Assert.assertTrue(invocationMatcher1.hasSimilarMethod(invocation2));
    }

    @Test
    public void testHasSimilarMethodWithDifferentInvocationButSameMethod() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Method method = MockMethodInvocation.method("testMethod", String.class, int.class);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Collections.emptyList());

        Invocation invocation2 = Mockito.mock(Invocation.class);
        Mockito.when(invocation2.getMethod()).thenReturn(method);

        Invocation invocation3 = Mockito.mock(Invocation.class);
        Mockito.when(invocation3.getMethod()).thenReturn(new Method("testMethod", String[].class, int.class));

        Assert.assertFalse(invocationMatcher1.hasSimilarMethod(invocation3));
    }

    @Test
    public void testHasSimilarMethodWithDifferentInvocationButDifferentMethod() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Method method1 = MockMethodInvocation.method("testMethod1", String.class, int.class);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Collections.emptyList());

        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method2 = MockMethodInvocation.method("testMethod2", String.class, int.class);
        Mockito.when(invocation2.getMethod()).thenReturn(method2);

        Assert.assertFalse(invocationMatcher1.hasSimilarMethod(invocation2));
    }

    @Test
    public void testCaptureArgumentsFromVarArgInvocation() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Object[] arguments = new Object[]{"test1", "test2", "test3"};
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.arg(0, ArgumentMatchers.instanceOf(String.class)),
                ArgumentMatchers.arg(1, ArgumentMatchers.instanceOf(String.class)),
                ArgumentMatchers.arg(2, ArgumentMatchers.instanceOf(String[].class)));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        CapturesArguments captureArguments = Mockito.spy(new CapturesArguments() {});
        invocationMatcher.captureArgumentsFrom(invocation);
        Mockito.verify(captureArguments).captureFrom("test1");
        Mockito.verify(captureArguments).captureFrom("test2");
        Mockito.verify(captureArguments).captureFrom(arguments);
    }

    @Test
    public void testCreateFromInvocations() {
        List<Invocation> invocations = Arrays.asList(Mockito.mock(Invocation.class), Mockito.mock(Invocation.class));
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        Assert.assertEquals(invocations.size(), invocationMatchers.size());
        for (InvocationMatcher invocationMatcher : invocationMatchers) {
            Assert.assertNotNull(invocationMatcher.getMatchers());
        }
    }
}
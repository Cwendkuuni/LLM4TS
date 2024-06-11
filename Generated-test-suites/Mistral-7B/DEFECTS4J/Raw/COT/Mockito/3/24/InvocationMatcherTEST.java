import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationOnMockImpl;
import org.mockito.internal.matchers.ArgumentsMatcher;
import org.mockito.internal.matchers.Capture;
import org.mockito.internal.matchers.CaptureType;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithInvocationAndEmptyMatchers() {
        Invocation invocation = new InvocationOnMockImpl(Mockito.mock(Object.class), new Method(null, null), new Object[0]);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Method method = invocationMatcher.getMethod();
        Assert.assertNotNull(method);
        Assert.assertEquals(invocation.getMethod().getName(), method.getName());

        InvocationMatcher emptyMatcher = new InvocationMatcher();
        Assert.assertTrue(emptyMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testConstructorWithInvocationAndMatchers() {
        Object[] arguments = new Object[]{1, 2, 3};
        Invocation invocation = new InvocationOnMockImpl(Mockito.mock(Object.class), new Method(null, new Class[0]), arguments);

        List<Matcher> matchers = Arrays.asList(
                ArgumentMatchers.eq(1),
                ArgumentMatchers.eq(2),
                ArgumentMatchers.eq(3)
        );

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Method method = invocationMatcher.getMethod();
        Assert.assertNotNull(method);
        Assert.assertEquals(invocation.getMethod().getName(), method.getName());

        List<Matcher> matchersFromInvocationMatcher = invocationMatcher.getMatchers();
        Assert.assertFalse(matchersFromInvocationMatcher.isEmpty());
        Assert.assertEquals(matchers, matchersFromInvocationMatcher);
    }

    @Test
    public void testMatchesMethodNameAndMock() {
        Object mock = Mockito.mock(Object.class);
        Invocation invocation = new InvocationOnMockImpl(mock, new Method(null, null), new Object[0]);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation candidate = Mockito.mock(Object.class);
        Method candidateMethod = new Method(null, null);
        InvocationMatcher candidateMatcher = new InvocationMatcher(new InvocationOnMockImpl(candidate, candidateMethod, new Object[0]));

        Assert.assertTrue(invocationMatcher.matches(candidateMatcher));
    }

    @Test
    public void testMatchesDifferentMocks() {
        Object mock1 = Mockito.mock(Object.class);
        Object mock2 = Mockito.mock(Object.class);
        Invocation invocation = new InvocationOnMockImpl(mock1, new Method(null, null), new Object[0]);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation candidate = new InvocationOnMockImpl(mock2, new Method(null, null), new Object[0]);
        InvocationMatcher candidateMatcher = new InvocationMatcher(candidate);

        Assert.assertFalse(invocationMatcher.matches(candidateMatcher));
    }

    @Test
    public void testMatchesDifferentMethodsWithSameName() {
        Object mock = Mockito.mock(Object.class);
        Invocation invocation = new InvocationOnMockImpl(mock, new Method(null, new Class[0]), new Object[0]);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation candidate = new InvocationOnMockImpl(mock, new Method(null, new Class[0], new Class[]{Integer.TYPE}), new Object[]{1});
        InvocationMatcher candidateMatcher = new InvocationMatcher(candidate);

        Assert.assertFalse(invocationMatcher.matches(candidateMatcher));
    }

    @Test(expected = AssertionError.class)
    public void testMatchesDifferentMethodsWithDifferentNames() {
        Object mock = Mockito.mock(Object.class);
        Invocation invocation = new InvocationOnMockImpl(mock, new Method(null, new Class[0]), new Object[0]);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation candidate = new InvocationOnMockImpl(mock, new Method(null, new Class[0], new Class[]{String.class}), new Object[]{1});
        InvocationMatcher candidateMatcher = new InvocationMatcher(candidate);

        Assert.assertFalse(invocationMatcher.matches(candidateMatcher));
    }

    @Test
    public void testMatchesDifferentMethodsWithSameNameAndSameArguments() {
        Object mock = Mockito.mock(Object.class);
        Invocation invocation = new InvocationOnMockImpl(mock, new Method(null, new Class[0]), new Object[0]);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation candidate = new InvocationOnMockImpl(mock, new Method(null, new Class[0]), new Object[0]);
        InvocationMatcher candidateMatcher = new InvocationMatcher(candidate);

        Assert.assertTrue(invocationMatcher.matches(candidateMatcher));
    }

    @Test
    public void testMatchesDifferentMethodsWithSameNameAndDifferentArguments() {
        Object mock = Mockito.mock(Object.class);
        Invocation invocation = new InvocationOnMockImpl(mock, new Method(null, new Class[0]), new Object[]{1});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation candidate = new InvocationOnMockImpl(mock, new Method(null, new Class[0]), new Object[]{2});
        InvocationMatcher candidateMatcher = new InvocationMatcher(candidate);

        Assert.assertFalse(invocationMatcher.matches(candidateMatcher));
    }

    @Test(expected = AssertionError.class)
    public void testMatchesDifferentMethodsWithDifferentNamesAndSameArguments() {
        Object mock = Mockito.mock(Object.class);
        Invocation invocation = new InvocationOnMockImpl(mock, new Method(null, new Class[0]), new Object[]{1});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation candidate = new InvocationOnMockImpl(mock, new Method(null, new Class[0], new Class[]{String.class}), new Object[]{1});
        InvocationMatcher candidateMatcher = new InvocationMatcher(candidate);

        Assert.assertFalse(invocationMatcher.matches(candidateMatcher));
    }

    @Test
    public void testMatchesWithVarargs() {
        Object mock = Mockito.mock(Object.class);
        Invocation invocation = new InvocationOnMockImpl(mock, new Method(null, new Class[]{Object[].class}), new Object[]{new Object[]{1, 2}});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation candidate = new InvocationOnMockImpl(mock, new Method(null, new Class[]{Object[].class}), new Object[]{new Object[]{1, 2, 3}});
        InvocationMatcher candidateMatcher = new InvocationMatcher(candidate);

        Assert.assertTrue(invocationMatcher.matches(candidateMatcher));
    }

    @Test
    public void testMatchesWithVarargsAndDifferentArguments() {
        Object mock = Mockito.mock(Object.class);
        Invocation invocation = new InvocationOnMockImpl(mock, new Method(null, new Class[]{Object[].class}), new Object[]{new Object[]{1, 2}});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation candidate = new InvocationOnMockImpl(mock, new Method(null, new Class[]{Object[].class}), new Object[]{new Object[]{1, 2, 3, 4}});
        InvocationMatcher candidateMatcher = new InvocationMatcher(candidate);

        Assert.assertFalse(invocationMatcher.matches(candidateMatcher));
    }

    @Test
    public void testCaptureArgumentsFromInvocation() {
        Object[] arguments = new Object[]{1, 2, 3};
        Invocation invocation = new InvocationOnMockImpl(Mockito.mock(Object.class), new Method(null, new Class[0]), arguments);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Capture<Integer> capture1 = new Capture<Integer>(CaptureType.BY_NAME, "arg1");
        Capture<Integer> capture2 = new Capture<Integer>(CaptureType.BY_NAME, "arg2");
        Capture<Integer> capture3 = new Capture<Integer>(CaptureType.BY_NAME, "arg3");

        List<Matcher> matchers = Arrays.asList(capture1, capture2, capture3);
        invocationMatcher.captureArgumentsFrom(invocation);

        Matcher actualCapture1 = matchers.get(0);
        Matcher actualCapture2 = matchers.get(1);
        Matcher actualCapture3 = matchers.get(2);

        Capture expectedCapture1 = Capture.capture(1);
        Capture expectedCapture2 = Capture.capture(2);
        Capture expectedCapture3 = Capture.capture(3);

        Assert.assertEquals(expectedCapture1.getValue(), actualCapture1.getValue());
        Assert.assertEquals(expectedCapture2.getValue(), actualCapture2.getValue());
        Assert.assertEquals(expectedCapture3.getValue(), actualCapture3.getValue());
    }

    @Test
    public void testCreateFromInvocations() {
        List<Invocation> invocations = Arrays.asList(
                new InvocationOnMockImpl(Mockito.mock(Object.class), new Method(null, new Class[0]), new Object[]{1}),
                new InvocationOnMockImpl(Mockito.mock(Object.class), new Method(null, new Class[0]), new Object[]{2})
        );

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        InvocationMatcher invocationMatcher1 = invocationMatchers.get(0);
        InvocationMatcher invocationMatcher2 = invocationMatchers.get(1);

        Assert.assertFalse(invocationMatcher1.matches(invocationMatcher2));
    }
}
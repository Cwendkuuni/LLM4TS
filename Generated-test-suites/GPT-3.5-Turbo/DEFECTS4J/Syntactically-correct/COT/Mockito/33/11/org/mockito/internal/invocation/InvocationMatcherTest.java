import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithInvocationAndMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new LinkedList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Assert.assertEquals(invocation, invocationMatcher.getInvocation());
        Assert.assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithInvocation() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Assert.assertEquals(invocation, invocationMatcher.getInvocation());
        Assert.assertEquals(Collections.emptyList(), invocationMatcher.getMatchers());
    }

    @Test
    public void testGetMethod() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        Mockito.when(invocation.getMethod()).thenReturn(method);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Assert.assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new LinkedList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        String expectedValue = "dummyToString";
        Mockito.when(invocation.toString(matchers, new PrintSettings())).thenReturn(expectedValue);

        Assert.assertEquals(expectedValue, invocationMatcher.toString());
    }

    @Test
    public void testMatches() {
        Invocation invocation = mock(Invocation.class);
        Invocation actual = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Assert.assertTrue(invocationMatcher.matches(actual));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Assert.assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        Mockito.when(invocation.getMethod()).thenReturn(method1);
        Mockito.when(candidate.getMethod()).thenReturn(method2);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Assert.assertFalse(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = mock(Invocation.class);
        Location location = mock(Location.class);
        Mockito.when(invocation.getLocation()).thenReturn(location);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Assert.assertEquals(location, invocationMatcher.getLocation());
    }

    @Test
    public void testToStringWithPrintSettings() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new LinkedList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        String expectedValue = "dummyToString";
        PrintSettings printSettings = mock(PrintSettings.class);
        Mockito.when(invocation.toString(matchers, printSettings)).thenReturn(expectedValue);

        Assert.assertEquals(expectedValue, invocationMatcher.toString(printSettings));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        Invocation i = mock(Invocation.class);
        Matcher matcher = mock(Matcher.class);
        Mockito.when(matcher instanceof CapturesArguments).thenReturn(true);
        Mockito.when(i.getArguments()).thenReturn(new Object[]{"dummyArgument"});
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(matcher);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        invocationMatcher.captureArgumentsFrom(i);

        Mockito.verify((CapturesArguments)matcher).captureFrom("dummyArgument");
    }

    @Test
    public void testCreateFrom() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<Invocation> invocations = new LinkedList<>();
        invocations.add(invocation1);
        invocations.add(invocation2);
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        Assert.assertEquals(2, invocationMatchers.size());
        Assert.assertEquals(invocation1, invocationMatchers.get(0).getInvocation());
        Assert.assertEquals(invocation2, invocationMatchers.get(1).getInvocation());
    }

}
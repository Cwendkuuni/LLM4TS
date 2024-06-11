import static org.mockito.Mockito.*;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;

    @Before
    public void setup() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void testConstructorWithMatcherList() {
        List<Matcher> matchers = Arrays.asList(mock(Matcher.class), mock(Matcher.class));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        List<Matcher> actualMatchers = invocationMatcher.getMatchers();
        Assert.assertEquals(2, actualMatchers.size());
        Assert.assertSame(matchers.get(0), actualMatchers.get(0));
        Assert.assertSame(matchers.get(1), actualMatchers.get(1));
    }

    @Test
    public void testConstructorWithEmptyMatcherList() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        List<Matcher> actualMatchers = invocationMatcher.getMatchers();
        Assert.assertTrue(actualMatchers.isEmpty());

        Object[] args = new Object[]{1, "test"};
        when(invocation.getArguments()).thenReturn(args);
        invocationMatcher = new InvocationMatcher(invocation);
        actualMatchers = invocationMatcher.getMatchers();
        Assert.assertEquals(2, actualMatchers.size());
        Assert.assertEquals(1, actualMatchers.get(0).matches(1));
        Assert.assertEquals("test", actualMatchers.get(1).matches("test"));
    }

    @Test
    public void testGetMethod() {
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);

        Method actualMethod = invocationMatcher.getMethod();

        Assert.assertSame(method, actualMethod);
    }

    @Test
    public void testGetInvocation() {
        Invocation actualInvocation = invocationMatcher.getInvocation();

        Assert.assertSame(invocation, actualInvocation);
    }

    @Test
    public void testToString() {
        String expected = "invocationMatcher.toString()";
        PrintSettings printSettings = mock(PrintSettings.class);
        when(printSettings.print(any(List.class), any(Invocation.class))).thenReturn(expected);

        String actual = invocationMatcher.toString();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMatches_True() {
        Invocation actualInvocation = mock(Invocation.class);
        when(invocation.getMock()).thenReturn("mock");
        when(actualInvocation.getMock()).thenReturn("mock");
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(actualInvocation.getMethod()).thenReturn(mock(Method.class));
        when(new ArgumentsComparator().argumentsMatch(eq(invocationMatcher), eq(actualInvocation))).thenReturn(true);

        boolean result = invocationMatcher.matches(actualInvocation);

        Assert.assertTrue(result);
    }

    @Test
    public void testMatches_False_differentMock() {
        Invocation actualInvocation = mock(Invocation.class);
        when(invocation.getMock()).thenReturn("mock1");
        when(actualInvocation.getMock()).thenReturn("mock2");

        boolean result = invocationMatcher.matches(actualInvocation);

        Assert.assertFalse(result);
    }

    @Test
    public void testMatches_False_differentMethod() {
        Invocation actualInvocation = mock(Invocation.class);
        when(invocation.getMock()).thenReturn("mock");
        when(actualInvocation.getMock()).thenReturn("mock");
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(actualInvocation.getMethod()).thenReturn(mock(Method.class));
        when(new ArgumentsComparator().argumentsMatch(eq(invocationMatcher), eq(actualInvocation))).thenReturn(false);

        boolean result = invocationMatcher.matches(actualInvocation);

        Assert.assertFalse(result);
    }

    @Test
    public void testMatches_False() {
        Invocation actualInvocation = mock(Invocation.class);
        when(invocation.getMock()).thenReturn("mock1");
        when(actualInvocation.getMock()).thenReturn("mock2");
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(actualInvocation.getMethod()).thenReturn(mock(Method.class));
        when(new ArgumentsComparator().argumentsMatch(eq(invocationMatcher), eq(actualInvocation))).thenReturn(false);

        boolean result = invocationMatcher.matches(actualInvocation);

        Assert.assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_True() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getMethod().getName()).thenReturn("methodName");
        when(candidate.getMethod().getName()).thenReturn("methodName");
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn("mock");
        when(candidate.getMock()).thenReturn("mock");
        when(new ArgumentsComparator().argumentsMatch(eq(invocationMatcher), eq(candidate))).thenReturn(false);

        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        Assert.assertTrue(result);
    }

    @Test
    public void testHasSimilarMethod_False_differentMethodName() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getMethod().getName()).thenReturn("methodName1");
        when(candidate.getMethod().getName()).thenReturn("methodName2");

        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        Assert.assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_False_verified() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getMethod().getName()).thenReturn("methodName");
        when(candidate.getMethod().getName()).thenReturn("methodName");
        when(candidate.isVerified()).thenReturn(true);

        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        Assert.assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_False_differentMock() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getMethod().getName()).thenReturn("methodName");
        when(candidate.getMethod().getName()).thenReturn("methodName");
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn("mock1");
        when(candidate.getMock()).thenReturn("mock2");

        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        Assert.assertFalse(result);
    }

    @Test
    public void testHasSameMethod_True() throws NoSuchMethodException {
        Method method1 = Object.class.getDeclaredMethod("equals", Object.class);
        Method method2 = Object.class.getDeclaredMethod("equals", Object.class);
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(candidate.getMethod()).thenReturn(method2);

        boolean result = invocationMatcher.hasSameMethod(candidate);

        Assert.assertTrue(result);
    }

    @Test
    public void testHasSameMethod_False_methodIsNull() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(null);

        boolean result = invocationMatcher.hasSameMethod(candidate);

        Assert.assertFalse(result);
    }

    @Test
    public void testHasSameMethod_False_differentMethodName() throws NoSuchMethodException {
        Method method1 = Object.class.getDeclaredMethod("equals", Object.class);
        Method method2 = Object.class.getDeclaredMethod("hashCode");
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(candidate.getMethod()).thenReturn(method2);

        boolean result = invocationMatcher.hasSameMethod(candidate);

        Assert.assertFalse(result);
    }

    @Test
    public void testHasSameMethod_False_differentParameterTypes() throws NoSuchMethodException {
        Method method1 = Object.class.getDeclaredMethod("equals", Object.class);
        Method method2 = Object.class.getDeclaredMethod("equals", String.class);
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(candidate.getMethod()).thenReturn(method2);

        boolean result = invocationMatcher.hasSameMethod(candidate);

        Assert.assertFalse(result);
    }

    @Test
    public void testGetLocation() {
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);

        Location actualLocation = invocationMatcher.getLocation();

        Assert.assertSame(location, actualLocation);
    }

    @Test
    public void testCaptureArgumentsFrom_VarArgsUnsupported() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getMethod().isVarArgs()).thenReturn(true);

        try {
            invocationMatcher.captureArgumentsFrom(invocation);
        } catch (UnsupportedOperationException e) {
            return;
        }

        Assert.fail("Expected UnsupportedOperationException to be thrown");
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Matcher matcher1 = mock(Matcher.class);
        Matcher matcher2 = mock(Matcher.class);
        List<Matcher> matchers = Arrays.asList(matcher1, matcher2);
        List<Object> args = Arrays.<Object>asList(1, "test");

        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getArgumentAt(0, Object.class)).thenReturn(args.get(0));
        when(invocation.getArgumentAt(1, Object.class)).thenReturn(args.get(1));
        when(invocation.getRawArguments()).thenReturn(args.toArray());
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        when(matcher1 instanceof CapturesArguments).thenReturn(true);
        when(matcher2 instanceof CapturesArguments).thenReturn(false);

        invocationMatcher.captureArgumentsFrom(invocation);

        verify(((CapturesArguments) matcher1)).captureFrom(args.get(0));
        verify(matcher2, never()).captureFrom(any());
    }

    @Test
    public void testIsVarargMatcher_True() {
        Matcher matcher = mock(VarargMatcher.class);
        Matcher actualMatcher = mock(Matcher.class);
        MatcherDecorator decorator = mock(MatcherDecorator.class);
        when(decorator.getActualMatcher()).thenReturn(actualMatcher);
        when(matcher instanceof MatcherDecorator).thenReturn(true);
        when(matcher).thenReturn(decorator);

        boolean result = invocationMatcher.isVarargMatcher(matcher);

        Assert.assertTrue(result);
    }

    @Test
    public void testIsVarargMatcher_False() {
        Matcher matcher = mock(Matcher.class);

        boolean result = invocationMatcher.isVarargMatcher(matcher);

        Assert.assertFalse(result);
    }

    @Test
    public void testIsVariableArgument_True() {
        Object[] args = new Object[]{1, "test"};
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getMethod().isVarArgs()).thenReturn(true);
        when(invocation.getRawArguments()).thenReturn(args);

        boolean result = invocationMatcher.isVariableArgument(invocation, 1);

        Assert.assertTrue(result);
    }

    @Test
    public void testIsVariableArgument_False_invalidIndex() {
        Object[] args = new Object[]{1, "test"};
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getMethod().isVarArgs()).thenReturn(true);
        when(invocation.getRawArguments()).thenReturn(args);

        boolean result = invocationMatcher.isVariableArgument(invocation, 2);

        Assert.assertFalse(result);
    }

    @Test
    public void testIsVariableArgument_False_notArray() {
        Object[] args = new Object[]{1, "test"};
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getMethod().isVarArgs()).thenReturn(true);
        when(invocation.getRawArguments()).thenReturn(args);

        boolean result = invocationMatcher.isVariableArgument(invocation, 0);

        Assert.assertFalse(result);
    }

    @Test
    public void testCreateFrom() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<Invocation> invocations = Arrays.asList(invocation1, invocation2);

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        Assert.assertEquals(2, invocationMatchers.size());
        Assert.assertSame(invocation1, invocationMatchers.get(0).getInvocation());
        Assert.assertSame(invocation2, invocationMatchers.get(1).getInvocation());
    }
}
import org.junit.*;
import org.mockito.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InvocationMatcherTest {

    private Invocation invocation;
    private List<Matcher> matchers;
    private InvocationMatcher invocationMatcher;

    @Before
    public void setUp() {
        invocation = Mockito.mock(Invocation.class);
        matchers = Collections.singletonList(Mockito.mock(Matcher.class));
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void testGetMethod() {
        Method expectedMethod = Mockito.mock(Method.class);
        Mockito.when(invocation.getMethod()).thenReturn(expectedMethod);

        Method actualMethod = invocationMatcher.getMethod();

        Assert.assertEquals(expectedMethod, actualMethod);
    }

    @Test
    public void testGetInvocation() {
        Invocation actualInvocation = invocationMatcher.getInvocation();

        Assert.assertEquals(invocation, actualInvocation);
    }

    @Test
    public void testGetMatchers() {
        List<Matcher> actualMatchers = invocationMatcher.getMatchers();

        Assert.assertEquals(matchers, actualMatchers);
    }

    @Test
    public void testToString() {
        PrintSettings printSettings = Mockito.mock(PrintSettings.class);
        Mockito.whenNew(PrintSettings.class).withNoArguments().thenReturn(printSettings);
        Mockito.when(printSettings.print(matchers, invocation)).thenReturn("expectedToString");

        String actualToString = invocationMatcher.toString();

        Assert.assertEquals("expectedToString", actualToString);
    }

    @Test
    public void testMatches() {
        Invocation actual = Mockito.mock(Invocation.class);
        Mockito.when(invocation.getMock()).thenReturn("mock1");
        Mockito.when(actual.getMock()).thenReturn("mock1");
        Mockito.when(invocation.getMethod()).thenReturn(Mockito.mock(Method.class));
        Mockito.when(actual.getMethod()).thenReturn(Mockito.mock(Method.class));
        Mockito.when(invocation.getArguments()).thenReturn(Arrays.asList("arg1", "arg2"));
        Mockito.when(actual.getArguments()).thenReturn(Arrays.asList("arg1", "arg2"));
        ArgumentsComparator argumentsComparator = Mockito.mock(ArgumentsComparator.class);
        Mockito.whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(argumentsComparator);
        Mockito.when(argumentsComparator.argumentsMatch(invocationMatcher, actual)).thenReturn(true);

        boolean actualResult = invocationMatcher.matches(actual);

        Assert.assertTrue(actualResult);
    }

    @Test
    public void testHasSimilarMethod_whenMethodNameEqualsAndInvocationIsUnverifiedAndMockIsTheSameAndHasSameMethod_shouldReturnTrue() {
        Invocation candidate = Mockito.mock(Invocation.class);
        Mockito.when(invocation.getMethod()).thenReturn(Mockito.mock(Method.class));
        Mockito.when(candidate.getMethod()).thenReturn(Mockito.mock(Method.class));
        Mockito.when(invocation.getMethod().getName()).thenReturn("methodName");
        Mockito.when(candidate.getMethod().getName()).thenReturn("methodName");
        Mockito.when(candidate.isVerified()).thenReturn(false);
        Mockito.when(invocation.getMock()).thenReturn("mock1");
        Mockito.when(candidate.getMock()).thenReturn("mock1");
        ArgumentsComparator argumentsComparator = Mockito.mock(ArgumentsComparator.class);
        Mockito.whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(argumentsComparator);
        Mockito.when(argumentsComparator.argumentsMatch(invocationMatcher, candidate.getArguments())).thenReturn(true);

        boolean actualResult = invocationMatcher.hasSimilarMethod(candidate);

        Assert.assertTrue(actualResult);
    }

    @Test
    public void testHasSimilarMethod_whenMethodNameNotEquals_shouldReturnFalse() {
        Invocation candidate = Mockito.mock(Invocation.class);
        Mockito.when(invocation.getMethod().getName()).thenReturn("methodName1");
        Mockito.when(candidate.getMethod().getName()).thenReturn("methodName2");

        boolean actualResult = invocationMatcher.hasSimilarMethod(candidate);

        Assert.assertFalse(actualResult);
    }

    @Test
    public void testHasSimilarMethod_whenInvocationIsVerified_shouldReturnFalse() {
        Invocation candidate = Mockito.mock(Invocation.class);
        Mockito.when(invocation.getMethod().getName()).thenReturn("methodName");
        Mockito.when(candidate.getMethod().getName()).thenReturn("methodName");
        Mockito.when(candidate.isVerified()).thenReturn(true);

        boolean actualResult = invocationMatcher.hasSimilarMethod(candidate);

        Assert.assertFalse(actualResult);
    }

    @Test
    public void testHasSimilarMethod_whenMockIsNotTheSame_shouldReturnFalse() {
        Invocation candidate = Mockito.mock(Invocation.class);
        Mockito.when(invocation.getMethod().getName()).thenReturn("methodName");
        Mockito.when(candidate.getMethod().getName()).thenReturn("methodName");
        Mockito.when(candidate.isVerified()).thenReturn(false);
        Mockito.when(invocation.getMock()).thenReturn("mock1");
        Mockito.when(candidate.getMock()).thenReturn("mock2");

        boolean actualResult = invocationMatcher.hasSimilarMethod(candidate);

        Assert.assertFalse(actualResult);
    }

    @Test
    public void testHasSimilarMethod_whenMethodEquals_shouldReturnTrue() {
        Invocation candidate = Mockito.mock(Invocation.class);
        Mockito.when(invocation.getMethod().getName()).thenReturn("methodName");
        Mockito.when(candidate.getMethod().getName()).thenReturn("methodName");
        Mockito.when(candidate.isVerified()).thenReturn(false);
        Mockito.when(invocation.getMock()).thenReturn("mock1");
        Mockito.when(candidate.getMock()).thenReturn("mock1");
        ArgumentsComparator argumentsComparator = Mockito.mock(ArgumentsComparator.class);
        Mockito.whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(argumentsComparator);
        Mockito.when(argumentsComparator.argumentsMatch(invocationMatcher, candidate)).thenReturn(true);

        boolean actualResult = invocationMatcher.hasSimilarMethod(candidate);

        Assert.assertTrue(actualResult);
    }

    @Test
    public void testHasSimilarMethod_whenOverloadedButSameArgs_shouldReturnFalse() {
        Invocation candidate = Mockito.mock(Invocation.class);
        Mockito.when(invocation.getMethod().getName()).thenReturn("methodName");
        Mockito.when(candidate.getMethod().getName()).thenReturn("methodName");
        Mockito.when(candidate.isVerified()).thenReturn(false);
        Mockito.when(invocation.getMock()).thenReturn("mock1");
        Mockito.when(candidate.getMock()).thenReturn("mock1");
        ArgumentsComparator argumentsComparator = Mockito.mock(ArgumentsComparator.class);
        Mockito.whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(argumentsComparator);
        Mockito.when(argumentsComparator.argumentsMatch(invocationMatcher, candidate)).thenReturn(false);
        Mockito.when(argumentsComparator.argumentsMatch(invocationMatcher, candidate.getArguments())).thenReturn(true);

        boolean actualResult = invocationMatcher.hasSimilarMethod(candidate);

        Assert.assertFalse(actualResult);
    }

    @Test
    public void testHasSameMethod_whenMethodNamesNotEqual_shouldReturnFalse() {
        Invocation candidate = Mockito.mock(Invocation.class);
        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);
        Mockito.when(invocation.getMethod()).thenReturn(method1);
        Mockito.when(candidate.getMethod()).thenReturn(method2);
        Mockito.when(method1.getName()).thenReturn("methodName1");
        Mockito.when(method2.getName()).thenReturn("methodName2");

        boolean actualResult = invocationMatcher.hasSameMethod(candidate);

        Assert.assertFalse(actualResult);
    }

    @Test
    public void testHasSameMethod_whenMethodNamesEqualAndParamLengthsNotEqual_shouldReturnFalse() {
        Invocation candidate = Mockito.mock(Invocation.class);
        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);
        Mockito.when(invocation.getMethod()).thenReturn(method1);
        Mockito.when(candidate.getMethod()).thenReturn(method2);
        Mockito.when(method1.getName()).thenReturn("methodName");
        Mockito.when(method1.getParameterTypes()).thenReturn(new Class[0]);
        Mockito.when(method2.getName()).thenReturn("methodName");
        Mockito.when(method2.getParameterTypes()).thenReturn(new Class[]{String.class});

        boolean actualResult = invocationMatcher.hasSameMethod(candidate);

        Assert.assertFalse(actualResult);
    }

    @Test
    public void testHasSameMethod_whenMethodNamesEqualAndParamLengthsEqualAndOneParamTypeNotEqual_shouldReturnFalse() {
        Invocation candidate = Mockito.mock(Invocation.class);
        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);
        Mockito.when(invocation.getMethod()).thenReturn(method1);
        Mockito.when(candidate.getMethod()).thenReturn(method2);
        Mockito.when(method1.getName()).thenReturn("methodName");
        Mockito.when(method1.getParameterTypes()).thenReturn(new Class[]{String.class});
        Mockito.when(method2.getName()).thenReturn("methodName");
        Mockito.when(method2.getParameterTypes()).thenReturn(new Class[]{Integer.class});

        boolean actualResult = invocationMatcher.hasSameMethod(candidate);

        Assert.assertFalse(actualResult);
    }

    @Test
    public void testHasSameMethod_whenMethodNamesAndParamTypesEqual_shouldReturnTrue() {
        Invocation candidate = Mockito.mock(Invocation.class);
        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);
        Mockito.when(invocation.getMethod()).thenReturn(method1);
        Mockito.when(candidate.getMethod()).thenReturn(method2);
        Mockito.when(method1.getName()).thenReturn("methodName");
        Mockito.when(method1.getParameterTypes()).thenReturn(new Class[]{String.class});
        Mockito.when(method2.getName()).thenReturn("methodName");
        Mockito.when(method2.getParameterTypes()).thenReturn(new Class[]{String.class});

        boolean actualResult = invocationMatcher.hasSameMethod(candidate);

        Assert.assertTrue(actualResult);
    }

    @Test
    public void testGetLocation() {
        Location expectedLocation = Mockito.mock(Location.class);
        Mockito.when(invocation.getLocation()).thenReturn(expectedLocation);

        Location actualLocation = invocationMatcher.getLocation();

        Assert.assertEquals(expectedLocation, actualLocation);
    }

    @Test
    public void testCaptureArgumentsFrom_whenMethodIsVarArgs_shouldCaptureArguments() {
        Mockito.when(invocation.getRawArguments()).thenReturn(new Object[]{1, 2, 3});
        Mockito.when(invocation.getArgumentAt(0, Object.class)).thenReturn(1);
        Mockito.when(invocation.getArgumentAt(1, Object.class)).thenReturn(2);
        Mockito.when(invocation.getArgumentAt(2, Object.class)).thenReturn(3);
        CapturesArguments capturesArguments1 = Mockito.mock(CapturesArguments.class);
        Mockito.whenNew(CapturesArguments.class).withNoArguments().thenReturn(capturesArguments1);
        CapturesArguments capturesArguments2 = Mockito.mock(CapturesArguments.class);
        Mockito.whenNew(CapturesArguments.class).withNoArguments().thenReturn(capturesArguments2);
        Mockito.when(matchers.get(0)).thenReturn(capturesArguments1);
        Mockito.when(matchers.get(1)).thenReturn(capturesArguments2);

        invocationMatcher.captureArgumentsFrom(invocation);

        Mockito.verify(capturesArguments1).captureFrom(1);
        Mockito.verify(capturesArguments2).captureFrom(2);
        Mockito.verify(capturesArguments2).captureFrom(3);
    }

    @Test
    public void testCaptureArgumentsFrom_whenMethodIsNotVarArgs_shouldCaptureArguments() {
        Mockito.when(invocation.getArgumentAt(0, Object.class)).thenReturn(1);
        Mockito.when(invocation.getArgumentAt(1, Object.class)).thenReturn(2);
        Mockito.when(matchers.get(0)).thenReturn(Mockito.mock(CapturesArguments.class));
        Mockito.when(matchers.get(1)).thenReturn(Mockito.mock(CapturesArguments.class));

        invocationMatcher.captureArgumentsFrom(invocation);

        Mockito.verify(matchers.get(0)).captureFrom(1);
        Mockito.verify(matchers.get(1)).captureFrom(2);
    }

    @Test
    public void testCreateFrom() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        List<Invocation> invocations = Arrays.asList(invocation1, invocation2);
        InvocationMatcher invocationMatcher1 = Mockito.mock(InvocationMatcher.class);
        InvocationMatcher invocationMatcher2 = Mockito.mock(InvocationMatcher.class);
        Mockito.whenNew(InvocationMatcher.class).withArguments(invocation1).thenReturn(invocationMatcher1);
        Mockito.whenNew(InvocationMatcher.class).withArguments(invocation2).thenReturn(invocationMatcher2);

        List<InvocationMatcher> actualInvocationMatchers = InvocationMatcher.createFrom(invocations);

        Assert.assertEquals(Arrays.asList(invocationMatcher1, invocationMatcher2), actualInvocationMatchers);
    }
}
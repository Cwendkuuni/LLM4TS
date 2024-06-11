import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.Invocation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;
    private Invocation invocation;
    private List<Matcher> matchers;

    @Before
    public void setUp() {
        invocation = mock(Invocation.class);
        matchers = new ArrayList<>();
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void testConstructor_WithInvocationAndMatchers() {
        List<Matcher> testMatchers = Arrays.asList(mock(Matcher.class), mock(Matcher.class));
        InvocationMatcher testInvocationMatcher = new InvocationMatcher(invocation, testMatchers);
        assertEquals(invocation, testInvocationMatcher.getInvocation());
        assertEquals(testMatchers, testInvocationMatcher.getMatchers());
    }

    @Test
    public void testConstructor_WithInvocation() {
        InvocationMatcher testInvocationMatcher = new InvocationMatcher(invocation);
        assertEquals(invocation, testInvocationMatcher.getInvocation());
        assertTrue(testInvocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testGetMethod() {
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testGetInvocation() {
        assertEquals(invocation, invocationMatcher.getInvocation());
    }

    @Test
    public void testGetMatchers() {
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testToString() {
        PrintSettings printSettings = mock(PrintSettings.class);
        String expectedString = "expectedString";
        when(printSettings.print(matchers, invocation)).thenReturn(expectedString);
        assertEquals(expectedString, invocationMatcher.toString());
    }

    @Test
    public void testMatches_WithMatchingInvocation() {
        Invocation actual = mock(Invocation.class);
        when(invocation.getMock()).thenReturn("mock");
        when(actual.getMock()).thenReturn("mock");
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getArguments()).thenReturn(new Object[]{});
        when(actual.getMethod()).thenReturn(mock(Method.class));
        when(actual.getArguments()).thenReturn(new Object[]{});
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        when(argumentsComparator.argumentsMatch(invocationMatcher, actual)).thenReturn(true);
        assertTrue(invocationMatcher.matches(actual));
    }

    @Test
    public void testMatches_WithDifferentMock() {
        Invocation actual = mock(Invocation.class);
        when(invocation.getMock()).thenReturn("mock1");
        when(actual.getMock()).thenReturn("mock2");
        assertFalse(invocationMatcher.matches(actual));
    }

    @Test
    public void testMatches_WithDifferentMethod() {
        Invocation actual = mock(Invocation.class);
        when(invocation.getMock()).thenReturn("mock");
        when(actual.getMock()).thenReturn("mock");
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(actual.getMethod()).thenReturn(method2);
        assertFalse(invocationMatcher.matches(actual));
    }

    @Test
    public void testMatches_WithDifferentArguments() {
        Invocation actual = mock(Invocation.class);
        when(invocation.getMock()).thenReturn("mock");
        when(actual.getMock()).thenReturn("mock");
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getArguments()).thenReturn(new Object[]{1});
        when(actual.getMethod()).thenReturn(mock(Method.class));
        when(actual.getArguments()).thenReturn(new Object[]{2});
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        when(argumentsComparator.argumentsMatch(invocationMatcher, actual)).thenReturn(false);
        assertFalse(invocationMatcher.matches(actual));
    }

    @Test
    public void testHasSimilarMethod_WithDifferentMethodName() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethod_WithVerifiedCandidate() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(candidate.isVerified()).thenReturn(true);
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethod_WithDifferentMock() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn("mock1");
        when(invocation.getMock()).thenReturn("mock2");
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethod_WithSameMethodAndUnverified() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn("mock");
        when(invocation.getMock()).thenReturn("mock");
        when(invocationMatcher.hasSameMethod(candidate)).thenReturn(true);
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethod_WithOverloadedButSameArgs() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn("mock");
        when(invocation.getMock()).thenReturn("mock");
        when(invocationMatcher.hasSameMethod(candidate)).thenReturn(false);
        when(invocationMatcher.safelyArgumentsMatch(candidate.getArguments())).thenReturn(true);
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethod_WithDifferentMethodAndUnverified() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn("mock");
        when(invocation.getMock()).thenReturn("mock");
        when(invocationMatcher.hasSameMethod(candidate)).thenReturn(false);
        when(invocationMatcher.safelyArgumentsMatch(candidate.getArguments())).thenReturn(false);
        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSameMethod_WithDifferentMethodName() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        assertFalse(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testHasSameMethod_WithDifferentParameterLength() {
        Invocation candidate = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(candidate.getMethod()).thenReturn(method2);
        when(method1.getName()).thenReturn("methodName");
        when(method2.getName()).thenReturn("methodName");
        Class[] params1 = new Class[]{mock(Class.class)};
        Class[] params2 = new Class[]{};
        when(method1.getParameterTypes()).thenReturn(params1);
        when(method2.getParameterTypes()).thenReturn(params2);
        assertFalse(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testHasSameMethod_WithDifferentParameterTypes() {
        Invocation candidate = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(candidate.getMethod()).thenReturn(method2);
        when(method1.getName()).thenReturn("methodName");
        when(method2.getName()).thenReturn("methodName");
        Class[] params1 = new Class[]{mock(Class.class)};
        Class[] params2 = new Class[]{mock(Class.class)};
        when(method1.getParameterTypes()).thenReturn(params1);
        when(method2.getParameterTypes()).thenReturn(params2);
        assertFalse(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testHasSameMethod_WithSameMethodAndParameters() {
        Invocation candidate = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(candidate.getMethod()).thenReturn(method2);
        when(method1.getName()).thenReturn("methodName");
        when(method2.getName()).thenReturn("methodName");
        Class[] params1 = new Class[]{mock(Class.class)};
        Class[] params2 = new Class[]{mock(Class.class)};
        when(method1.getParameterTypes()).thenReturn(params1);
        when(method2.getParameterTypes()).thenReturn(params2);
        assertTrue(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testGetLocation() {
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);
        assertEquals(location, invocationMatcher.getLocation());
    }

    @Test
    public void testCaptureArgumentsFrom_WithVarArgs() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getRawArguments()).thenReturn(new Object[]{1, 2});
        Matcher matcher1 = mock(CapturesArguments.class);
        Matcher matcher2 = mock(CapturesArguments.class);
        matchers.add(matcher1);
        matchers.add(matcher2);
        invocationMatcher.captureArgumentsFrom(invocation);
        verify((CapturesArguments) matcher1).captureFrom(1);
        verify((CapturesArguments) matcher2).captureFrom(2);
    }

    @Test
    public void testCaptureArgumentsFrom_WithoutVarArgs() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getArgumentAt(anyInt(), any())).thenReturn(1);
        Matcher matcher1 = mock(CapturesArguments.class);
        Matcher matcher2 = mock(CapturesArguments.class);
        matchers.add(matcher1);
        matchers.add(matcher2);
        invocationMatcher.captureArgumentsFrom(invocation);
        verify((CapturesArguments) matcher1).captureFrom(1);
        verify((CapturesArguments) matcher2).captureFrom(1);
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = Arrays.asList(mock(Invocation.class), mock(Invocation.class));
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        assertEquals(invocations.size(), invocationMatchers.size());
        for (int i = 0; i < invocations.size(); i++) {
            assertEquals(invocations.get(i), invocationMatchers.get(i).getInvocation());
        }
    }
}
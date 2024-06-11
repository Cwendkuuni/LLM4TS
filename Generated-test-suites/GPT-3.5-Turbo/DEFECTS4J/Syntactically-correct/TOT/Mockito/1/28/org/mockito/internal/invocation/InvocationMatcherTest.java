import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.invocation.Location;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InvocationMatcherTest {

    private Invocation invocation;
    private List<Matcher> matchers;
    private InvocationMatcher invocationMatcher;

    @Before
    public void setUp() throws Exception {
        invocation = mock(Invocation.class);
        matchers = Arrays.asList(mock(Matcher.class), mock(Matcher.class));
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void testConstructorWithInvocationAndMatchers() {
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithInvocation() {
        invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(Collections.emptyList(), invocationMatcher.getMatchers());
    }

    @Test
    public void testGetMethod() {
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testToString() {
        String expected = "toString";
        PrintSettings printSettings = mock(PrintSettings.class);
        when(printSettings.print(matchers, invocation)).thenReturn(expected);
        assertEquals(expected, invocationMatcher.toString());
    }

    @Test
    public void testMatches_WithMatchingInvocation_ReturnsTrue() {
        Invocation actualInvocation = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(actualInvocation.getMock()).thenReturn(invocation.getMock());
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(actualInvocation.getMethod()).thenReturn(invocation.getMethod());
        when(new ArgumentsComparator().argumentsMatch(invocationMatcher, actualInvocation)).thenReturn(true);
        assertTrue(invocationMatcher.matches(actualInvocation));
    }

    @Test
    public void testMatches_WithNonMatchingInvocation_ReturnsFalse() {
        Invocation actualInvocation = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(actualInvocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(actualInvocation.getMethod()).thenReturn(mock(Method.class));
        when(new ArgumentsComparator().argumentsMatch(invocationMatcher, actualInvocation)).thenReturn(false);
        assertFalse(invocationMatcher.matches(actualInvocation));
    }

    @Test
    public void testHasSimilarMethod_WithDifferentMethodName_ReturnsFalse() {
        Invocation candidate = mock(Invocation.class);
        Method method = mock(Method.class);
        when(method.getName()).thenReturn("method1");
        when(candidate.getMethod()).thenReturn(method);
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethod_WithVerifiedInvocation_ReturnsFalse() {
        Invocation candidate = mock(Invocation.class);
        Method method = mock(Method.class);
        when(method.getName()).thenReturn("method");
        when(candidate.getMethod()).thenReturn(method);
        when(candidate.isVerified()).thenReturn(true);
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethod_WithDifferentMock_ReturnsFalse() {
        Invocation candidate = mock(Invocation.class);
        Method method = mock(Method.class);
        when(method.getName()).thenReturn("method");
        when(candidate.getMethod()).thenReturn(method);
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn(mock(Object.class));
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethod_WithSameMethod_ReturnsTrue() {
        Invocation candidate = mock(Invocation.class);
        Method method = mock(Method.class);
        when(method.getName()).thenReturn("method");
        when(candidate.getMethod()).thenReturn(method);
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn(invocation.getMock());
        when(invocationMatcher.hasSameMethod(candidate)).thenReturn(true);
        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethod_WithSameMethodButOverloadedWithDifferentArgs_ReturnsFalse() {
        Invocation candidate = mock(Invocation.class);
        Method method = mock(Method.class);
        when(method.getName()).thenReturn("method");
        when(candidate.getMethod()).thenReturn(method);
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn(invocation.getMock());
        when(invocationMatcher.hasSameMethod(candidate)).thenReturn(false);
        when(new ArgumentsComparator().argumentsMatch(invocationMatcher, candidate.getArguments())).thenReturn(false);
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSameMethod_WithEqualMethodNamesAndParameterTypes_ReturnsTrue() {
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(method1.getName()).thenReturn("method");
        when(method2.getName()).thenReturn("method");
        Class[] parameters = {int.class, String.class};
        when(method1.getParameterTypes()).thenReturn(parameters);
        when(method2.getParameterTypes()).thenReturn(parameters);
        when(invocation.getMethod()).thenReturn(method1);
        when(invocationMatcher.hasSameMethod(invocation)).thenCallRealMethod();
        assertTrue(invocationMatcher.hasSameMethod(invocation));
    }

    @Test
    public void testHasSameMethod_WithDifferentMethodName_ReturnsFalse() {
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(method.getName()).thenReturn("method1");
        assertFalse(invocationMatcher.hasSameMethod(invocation));
    }

    @Test
    public void testHasSameMethod_WithDifferentParameters_ReturnsFalse() {
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(method1.getName()).thenReturn("method");
        when(invocation.getMethod()).thenReturn(method1);
        when(invocationMatcher.hasSameMethod(invocation)).thenCallRealMethod();
        when(method1.getParameterTypes()).thenReturn(new Class[]{int.class});
        when(method2.getParameterTypes()).thenReturn(new Class[]{String.class});
        assertFalse(invocationMatcher.hasSameMethod(invocation));
    }

    @Test
    public void testGetLocation() {
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);
        assertEquals(location, invocationMatcher.getLocation());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCaptureArgumentsFrom_WithVarargsMethod_ThrowsUnsupportedOperationException() {
        Method method = mock(Method.class);
        when(method.isVarArgs()).thenReturn(true);
        when(invocation.getMethod()).thenReturn(method);
        invocationMatcher.captureArgumentsFrom(invocation);
    }

    @Test
    public void testCaptureArgumentsFrom_WithMatchers_CallsCaptureFromOnMatchers() {
        CapturesArguments matcher1 = mock(CapturesArguments.class);
        CapturesArguments matcher2 = mock(CapturesArguments.class);

        when(matchers.get(0)).thenReturn(matcher1);
        when(matchers.get(1)).thenReturn(matcher2);

        Object arg1 = new Object();
        Object arg2 = new Object();

        when(invocation.getArgumentAt(0, Object.class)).thenReturn(arg1);
        when(invocation.getArgumentAt(1, Object.class)).thenReturn(arg2);

        invocationMatcher.captureArgumentsFrom(invocation);
        
        verify(matcher1).captureFrom(arg1);
        verify(matcher2).captureFrom(arg2);
    }

    @Test
    public void testCreateFrom_WithListOfInvocations_ReturnsListOfInvocationMatchers() {
        List<Invocation> invocations = Arrays.asList(mock(Invocation.class), mock(Invocation.class));
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        assertEquals(invocations.size(), invocationMatchers.size());
        for (int i = 0; i < invocations.size(); i++) {
            assertEquals(invocations.get(i), invocationMatchers.get(i).getInvocation());
        }
    }
}
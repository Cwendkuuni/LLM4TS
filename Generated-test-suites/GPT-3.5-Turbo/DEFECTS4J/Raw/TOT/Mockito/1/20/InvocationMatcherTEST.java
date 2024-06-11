import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    private Invocation invocation;
    private InvocationMatcher invocationMatcher;

    @Before
    public void setUp() throws NoSuchMethodException {
        Method method = MyClass.class.getMethod("myMethod", int.class, String.class);
        Object[] arguments = {10, "test"};
        invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(method);
        when(invocation.getArguments()).thenReturn(arguments);
        when(invocation.isVerified()).thenReturn(false);
        
        invocationMatcher = new InvocationMatcher(invocation);
    }

    @Test
    public void testGetMethod() {
        Method expectedMethod = MyClass.class.getMethod("myMethod", int.class, String.class);
        Method actualMethod = invocationMatcher.getMethod();
        
        assertEquals(expectedMethod, actualMethod);
    }

    @Test
    public void testGetInvocation() {
        Invocation actualInvocation = invocationMatcher.getInvocation();
        
        assertEquals(invocation, actualInvocation);
    }

    @Test
    public void testGetMatchers() {
        List<Matcher> expectedMatchers = Collections.emptyList();
        List<Matcher> actualMatchers = invocationMatcher.getMatchers();
        
        assertEquals(expectedMatchers, actualMatchers);
    }

    @Test
    public void testToString() {
        String expectedString = "Matchers: [], Invocation: Mock for Invocation class, hashCode: " + invocation.hashCode();
        String actualString = invocationMatcher.toString();
        
        assertEquals(expectedString, actualString);
    }

    @Test
    public void testMatches() throws NoSuchMethodException {
        Invocation actualInvocation = mock(Invocation.class);
        Method method = MyClass.class.getMethod("myMethod", int.class, String.class);
        Object[] arguments = {10, "test"};
        when(actualInvocation.getMock()).thenReturn(mock(MyClass.class));
        when(actualInvocation.getMethod()).thenReturn(method);
        when(actualInvocation.getArguments()).thenReturn(arguments);

        boolean result = invocationMatcher.matches(actualInvocation);

        assertTrue(result);
    }

    @Test
    public void testMatches_WithDifferentMock() throws NoSuchMethodException {
        Invocation actualInvocation = mock(Invocation.class);
        Method method = MyClass.class.getMethod("myMethod", int.class, String.class);
        Object[] arguments = {10, "test"};
        when(actualInvocation.getMock()).thenReturn(mock(AnotherClass.class));
        when(actualInvocation.getMethod()).thenReturn(method);
        when(actualInvocation.getArguments()).thenReturn(arguments);

        boolean result = invocationMatcher.matches(actualInvocation);

        assertFalse(result);
    }

    @Test
    public void testMatches_WithDifferentMethod() throws NoSuchMethodException {
        Invocation actualInvocation = mock(Invocation.class);
        Method method = AnotherClass.class.getMethod("anotherMethod", int.class, String.class);
        Object[] arguments = {10, "test"};
        when(actualInvocation.getMock()).thenReturn(mock(MyClass.class));
        when(actualInvocation.getMethod()).thenReturn(method);
        when(actualInvocation.getArguments()).thenReturn(arguments);

        boolean result = invocationMatcher.matches(actualInvocation);

        assertFalse(result);
    }

    @Test
    public void testMatches_WithDifferentArguments() throws NoSuchMethodException {
        Invocation actualInvocation = mock(Invocation.class);
        Method method = MyClass.class.getMethod("myMethod", int.class, String.class);
        Object[] arguments = {20, "test"};
        when(actualInvocation.getMock()).thenReturn(mock(MyClass.class));
        when(actualInvocation.getMethod()).thenReturn(method);
        when(actualInvocation.getArguments()).thenReturn(arguments);

        boolean result = invocationMatcher.matches(actualInvocation);

        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod() throws NoSuchMethodException {
        Invocation candidate = mock(Invocation.class);
        Method method = MyClass.class.getMethod("myMethod", int.class, String.class);
        Object[] arguments = {10, "test"};
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn(mock(MyClass.class));
        when(candidate.getMethod()).thenReturn(method);
        when(candidate.getArguments()).thenReturn(arguments);

        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        assertTrue(result);
    }

    @Test
    public void testHasSimilarMethod_WithDifferentMethodName() throws NoSuchMethodException {
        Invocation candidate = mock(Invocation.class);
        Method method = AnotherClass.class.getMethod("anotherMethod", int.class, String.class);
        Object[] arguments = {10, "test"};
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn(mock(MyClass.class));
        when(candidate.getMethod()).thenReturn(method);
        when(candidate.getArguments()).thenReturn(arguments);

        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_WithDifferentVerificationStatus() throws NoSuchMethodException {
        Invocation candidate = mock(Invocation.class);
        Method method = MyClass.class.getMethod("myMethod", int.class, String.class);
        Object[] arguments = {10, "test"};
        when(candidate.isVerified()).thenReturn(true);
        when(candidate.getMock()).thenReturn(mock(MyClass.class));
        when(candidate.getMethod()).thenReturn(method);
        when(candidate.getArguments()).thenReturn(arguments);

        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_WithDifferentMock() throws NoSuchMethodException {
        Invocation candidate = mock(Invocation.class);
        Method method = MyClass.class.getMethod("myMethod", int.class, String.class);
        Object[] arguments = {10, "test"};
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn(mock(AnotherClass.class));
        when(candidate.getMethod()).thenReturn(method);
        when(candidate.getArguments()).thenReturn(arguments);

        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_WithOverloadedButSameArguments() throws NoSuchMethodException {
        Invocation candidate = mock(Invocation.class);
        Method method = MyClass.class.getMethod("myMethod", int.class, String.class);
        Object[] arguments = {10, "test"};
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn(mock(MyClass.class));
        when(candidate.getMethod()).thenReturn(method);
        when(candidate.getArguments()).thenReturn(arguments);

        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        assertFalse(result);
    }

    @Test
    public void testHasSameMethod() throws NoSuchMethodException {
        Invocation candidate = mock(Invocation.class);
        Method method = MyClass.class.getMethod("myMethod", int.class, String.class);
        Object[] arguments = {10, "test"};
        when(candidate.getMethod()).thenReturn(method);
        when(candidate.getArguments()).thenReturn(arguments);

        boolean result = invocationMatcher.hasSameMethod(candidate);

        assertTrue(result);
    }

    @Test
    public void testHasSameMethod_WithDifferentMethodName() throws NoSuchMethodException {
        Invocation candidate = mock(Invocation.class);
        Method method = AnotherClass.class.getMethod("anotherMethod", int.class, String.class);
        Object[] arguments = {10, "test"};
        when(candidate.getMethod()).thenReturn(method);
        when(candidate.getArguments()).thenReturn(arguments);

        boolean result = invocationMatcher.hasSameMethod(candidate);

        assertFalse(result);
    }

    @Test
    public void testHasSameMethod_WithDifferentArguments() throws NoSuchMethodException {
        Invocation candidate = mock(Invocation.class);
        Method method = MyClass.class.getMethod("myMethod", int.class, String.class);
        Object[] arguments = {20, "test"};
        when(candidate.getMethod()).thenReturn(method);
        when(candidate.getArguments()).thenReturn(arguments);

        boolean result = invocationMatcher.hasSameMethod(candidate);

        assertFalse(result);
    }

    @Test
    public void testGetLocation() {
        Location expectedLocation = mock(Location.class);
        when(invocation.getLocation()).thenReturn(expectedLocation);
        
        Location actualLocation = invocationMatcher.getLocation();
        
        assertEquals(expectedLocation, actualLocation);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCaptureArgumentsFrom_VarArgsMethod() {
        Method method = MyClass.class.getMethod("varArgsMethod", String.class, int.class, String[].class);
        Object[] arguments = {"test", 10, new String[]{"arg1", "arg2"}};
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(method);
        when(invocation.getRawArguments()).thenReturn(arguments);
        
        invocationMatcher.captureArgumentsFrom(invocation);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Object argument1 = mock(Object.class);
        Object argument2 = mock(Object.class);
        Object argument3 = mock(Object.class);
        Invocation invocation = mock(Invocation.class);
        when(invocation.getArgumentAt(0, Object.class)).thenReturn(argument1);
        when(invocation.getArgumentAt(1, Object.class)).thenReturn(argument2);
        when(invocation.getArgumentAt(2, Object.class)).thenReturn(argument3);
        invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(mock(Matcher.class), mock(Matcher.class), mock(CapturesArguments.class)));

        invocationMatcher.captureArgumentsFrom(invocation);

        verify(((CapturesArguments) invocationMatcher.getMatchers().get(2))).captureFrom(argument3);
    }

    @Test
    public void testCreateFrom() throws NoSuchMethodException {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<Invocation> invocations = Arrays.asList(invocation1, invocation2);

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        assertEquals(2, invocationMatchers.size());
        assertEquals(invocation1, invocationMatchers.get(0).getInvocation());
        assertEquals(invocation2, invocationMatchers.get(1).getInvocation());
    }

    private class MyClass {
        public void myMethod(int a, String b) {
        }

        public void varArgsMethod(String a, int b, String... c) {
        }
    }

    private class AnotherClass {
        public void anotherMethod(int a, String b) {
        }
    }
}
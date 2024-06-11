import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;

    @Before
    public void setUp() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(MyClass.class));
        Method method = null;
        try {
            method = MyClass.class.getDeclaredMethod("myMethod", int.class, String.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        when(invocation.getMethod()).thenReturn(method);
        Object[] arguments = {1, "test"};
        when(invocation.getArguments()).thenReturn(arguments);
        invocationMatcher = new InvocationMatcher(invocation);
    }

    @Test
    public void testGetMethod() {
        Method expectedMethod = null;
        try {
            expectedMethod = MyClass.class.getDeclaredMethod("myMethod", int.class, String.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        Method actualMethod = invocationMatcher.getMethod();
        assertEquals(expectedMethod, actualMethod);
    }

    @Test
    public void testGetInvocation() {
        Invocation expectedInvocation = mock(Invocation.class);
        Object[] expectedArguments = {1, "test"};
        when(expectedInvocation.getArguments()).thenReturn(expectedArguments);
        Invocation actualInvocation = invocationMatcher.getInvocation();
        assertEquals(expectedInvocation, actualInvocation);
    }

    @Test
    public void testGetMatchers() {
        List<Matcher> expectedMatchers = new ArrayList<>();
        Matcher firstMatcher = mock(Matcher.class);
        Matcher secondMatcher = mock(Matcher.class);
        expectedMatchers.add(firstMatcher);
        expectedMatchers.add(secondMatcher);
        Invocation invocation = mock(Invocation.class);
        when(invocation.argumentsToMatchers()).thenReturn(expectedMatchers);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        List<Matcher> actualMatchers = invocationMatcher.getMatchers();
        assertEquals(expectedMatchers, actualMatchers);
    }

    @Test
    public void testToString() {
        String expectedString = "MyClass.myMethod(1, \"test\")";
        String actualString = invocationMatcher.toString();
        assertEquals(expectedString, actualString);
    }

    @Test
    public void testMatches() {
        Invocation actualInvocation = mock(Invocation.class);
        when(actualInvocation.getMock()).thenReturn(mock(MyClass.class));

        Method method = null;
        try {
            method = MyClass.class.getDeclaredMethod("myMethod", int.class, String.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        when(actualInvocation.getMethod()).thenReturn(method);
        Object[] arguments = {1, "test"};
        when(actualInvocation.getArguments()).thenReturn(arguments);

        boolean actualResult = invocationMatcher.matches(actualInvocation);
        assertTrue(actualResult);
    }

    @Test
    public void testHasSimilarMethod_withDifferentMethodName() {
        Invocation candidate = mock(Invocation.class);
        Method method = null;
        try {
            method = MyClass.class.getDeclaredMethod("otherMethod", int.class, String.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        when(candidate.getMethod()).thenReturn(method);
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn(mock(MyClass.class));
        boolean actualResult = invocationMatcher.hasSimilarMethod(candidate);
        assertFalse(actualResult);
    }

    @Test
    public void testHasSimilarMethod_withVerifiedInvocation() {
        Invocation candidate = mock(Invocation.class);
        Method method = null;
        try {
            method = MyClass.class.getDeclaredMethod("myMethod", int.class, String.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        when(candidate.getMethod()).thenReturn(method);
        when(candidate.isVerified()).thenReturn(true);
        when(candidate.getMock()).thenReturn(mock(MyClass.class));
        boolean actualResult = invocationMatcher.hasSimilarMethod(candidate);
        assertFalse(actualResult);
    }

    @Test
    public void testHasSimilarMethod_withDifferentMock() {
        Invocation candidate = mock(Invocation.class);
        Method method = null;
        try {
            method = MyClass.class.getDeclaredMethod("myMethod", int.class, String.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        when(candidate.getMethod()).thenReturn(method);
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn(mock(OtherClass.class));
        boolean actualResult = invocationMatcher.hasSimilarMethod(candidate);
        assertFalse(actualResult);
    }

    @Test
    public void testHasSimilarMethod_withSameMethodButDifferentArgs() {
        Invocation candidate = mock(Invocation.class);
        Method method = null;
        try {
            method = MyClass.class.getDeclaredMethod("myMethod", int.class, String.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        when(candidate.getMethod()).thenReturn(method);
        Object[] arguments = {2, "test"};
        when(candidate.getArguments()).thenReturn(arguments);
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn(mock(MyClass.class));
        boolean actualResult = invocationMatcher.hasSimilarMethod(candidate);
        assertFalse(actualResult);
    }

    @Test
    public void testHasSimilarMethod_withSameMethodAndSameArgsButOverloaded() {
        Invocation candidate = mock(Invocation.class);
        Method method = null;
        try {
            method = MyOtherClass.class.getDeclaredMethod("myMethod", int.class, String.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        when(candidate.getMethod()).thenReturn(method);
        Object[] arguments = {1, "test"};
        when(candidate.getArguments()).thenReturn(arguments);
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn(mock(MyClass.class));
        boolean actualResult = invocationMatcher.hasSimilarMethod(candidate);
        assertTrue(actualResult);
    }

    @Test
    public void testHasSameMethod_withSameMethod() {
        Method method = null;
        try {
            method = MyClass.class.getDeclaredMethod("myMethod", int.class, String.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(method);
        boolean actualResult = invocationMatcher.hasSameMethod(candidate);
        assertTrue(actualResult);
    }

    @Test
    public void testHasSameMethod_withDifferentMethod() {
        Method method = null;
        try {
            method = MyOtherClass.class.getDeclaredMethod("myMethod", int.class, String.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(method);
        boolean actualResult = invocationMatcher.hasSameMethod(candidate);
        assertFalse(actualResult);
    }

    @Test
    public void testGetLocation() {
        Location expectedLocation = mock(Location.class);
        Invocation invocation = mock(Invocation.class);
        when(invocation.getLocation()).thenReturn(expectedLocation);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Location actualLocation = invocationMatcher.getLocation();
        assertEquals(expectedLocation, actualLocation);
    }

    @Test
    public void testToString_withPrintSettings() {
        PrintSettings printSettings = mock(PrintSettings.class);
        String expectedString = "MyClass.myMethod(1, \"test\")";
        String actualString = invocationMatcher.toString(printSettings);
        assertEquals(expectedString, actualString);
    }

    @Test
    public void testCaptureArgumentsFrom_withCapturesArguments() {
        Invocation i = mock(Invocation.class);
        Object[] args = {1, "test"};
        when(i.getArguments()).thenReturn(args);
        List<Matcher> matchers = new ArrayList<>();
        Matcher capturesArguments = mock(Matcher.class);
        when(capturesArguments instanceof CapturesArguments).thenReturn(true);
        matchers.add(capturesArguments);
        InvocationMatcher invocationMatcher = new InvocationMatcher(i, matchers);

        invocationMatcher.captureArgumentsFrom(i);

        verify((CapturesArguments) capturesArguments, times(1)).captureFrom(1);
        verify((CapturesArguments) capturesArguments, times(1)).captureFrom("test");
    }

}
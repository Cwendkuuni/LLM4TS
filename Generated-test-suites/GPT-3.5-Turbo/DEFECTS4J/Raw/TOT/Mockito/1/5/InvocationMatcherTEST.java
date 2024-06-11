import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import org.mockito.mock.MockCreationSettings;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;

    @Before
    public void setUp() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(getTestMethod());
        when(invocation.getArguments()).thenReturn(new Object[]{"arg1", "arg2"});
        invocationMatcher = new InvocationMatcher(invocation);
    }

    @Test
    public void testConstructorWithMatchers() {
        List<Matcher> matchers = Arrays.asList(mock(Matcher.class), mock(Matcher.class));
        InvocationMatcher matcher = new InvocationMatcher(createInvocation(), matchers);
        assertEquals(createInvocation(), matcher.getInvocation());
        assertEquals(matchers, matcher.getMatchers());
    }

    @Test
    public void testConstructorWithoutMatchers() {
        InvocationMatcher matcher = new InvocationMatcher(createInvocation());
        assertEquals(createInvocation(), matcher.getInvocation());
        assertTrue(matcher.getMatchers().isEmpty());
    }

    @Test
    public void testGetMethod() {
        Method method = invocationMatcher.getMethod();
        assertEquals(getTestMethod(), method);
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = invocationMatcher.getInvocation();
        assertEquals(createInvocation(), invocation);
    }

    @Test
    public void testGetMatchers() {
        List<Matcher> matchers = invocationMatcher.getMatchers();
        assertTrue(matchers.isEmpty());
    }

    @Test
    public void testToString() {
        String expected = "Mock for InvocationMatcherTest$TestInterface, hashCode: 504637244, method name: getTestMethod";
        String actual = invocationMatcher.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testMatchesWithMatchingInvocation() {
        Invocation actualInvocation = createInvocation();
        boolean matches = invocationMatcher.matches(actualInvocation);
        assertTrue(matches);
    }

    @Test
    public void testMatchesWithDifferentMock() {
        Invocation actualInvocation = createInvocationWithDifferentMock();
        boolean matches = invocationMatcher.matches(actualInvocation);
        assertFalse(matches);
    }

    @Test
    public void testMatchesWithDifferentMethod() {
        Invocation actualInvocation = createInvocationWithDifferentMethod();
        boolean matches = invocationMatcher.matches(actualInvocation);
        assertFalse(matches);
    }

    @Test
    public void testMatchesWithDifferentArguments() {
        Invocation actualInvocation = createInvocationWithDifferentArguments();
        boolean matches = invocationMatcher.matches(actualInvocation);
        assertFalse(matches);
    }

    @Test
    public void testHasSimilarMethodWithMatchingInvocation() {
        Invocation candidateInvocation = createInvocation();
        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidateInvocation);
        assertTrue(hasSimilarMethod);
    }

    @Test
    public void testHasSimilarMethodWithDifferentMethodName() {
        Invocation candidateInvocation = createInvocationWithDifferentMethodName();
        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidateInvocation);
        assertFalse(hasSimilarMethod);
    }

    @Test
    public void testHasSimilarMethodWithVerifiedInvocation() {
        Invocation candidateInvocation = createVerifiedInvocation();
        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidateInvocation);
        assertFalse(hasSimilarMethod);
    }

    @Test
    public void testHasSimilarMethodWithDifferentMock() {
        Invocation candidateInvocation = createInvocationWithDifferentMock();
        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidateInvocation);
        assertFalse(hasSimilarMethod);
    }

    @Test
    public void testHasSimilarMethodWithDifferentMethod() {
        Invocation candidateInvocation = createInvocationWithDifferentMethod();
        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidateInvocation);
        assertTrue(hasSimilarMethod);
    }

    @Test
    public void testHasSimilarMethodWithOverloadedButSameArgs() {
        Invocation candidateInvocation = createOverloadedInvocation();
        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidateInvocation);
        assertFalse(hasSimilarMethod);
    }

    @Test
    public void testHasSameMethodWithMatchingInvocation() {
        Invocation candidateInvocation = createInvocation();
        boolean hasSameMethod = invocationMatcher.hasSameMethod(candidateInvocation);
        assertTrue(hasSameMethod);
    }

    @Test
    public void testHasSameMethodWithDifferentMethodName() {
        Invocation candidateInvocation = createInvocationWithDifferentMethodName();
        boolean hasSameMethod = invocationMatcher.hasSameMethod(candidateInvocation);
        assertFalse(hasSameMethod);
    }

    @Test
    public void testHasSameMethodWithDifferentParameterTypes() {
        Invocation candidateInvocation = createInvocationWithDifferentParameterTypes();
        boolean hasSameMethod = invocationMatcher.hasSameMethod(candidateInvocation);
        assertFalse(hasSameMethod);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(getTestMethod());
        when(invocation.getArguments()).thenReturn(new Object[]{"arg1", "arg2"});

        Matcher matcher = mock(Matcher.class);
        List<Matcher> matchers = Collections.singletonList(matcher);

        InvocationMatcher matcherUnderTest = new InvocationMatcher(invocation, matchers);
        matcherUnderTest.captureArgumentsFrom(invocation);

        verify(matcher).captureFrom("arg1");
        verify(matcher).captureFrom("arg2");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCaptureVarArgsArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(getVarArgsMethod());
        when(invocation.getArguments()).thenReturn(new Object[]{"arg1", new String[]{"varArg1", "varArg2"}});

        Matcher matcher = mock(Matcher.class);
        List<Matcher> matchers = Collections.singletonList(matcher);

        InvocationMatcher matcherUnderTest = new InvocationMatcher(invocation, matchers);
        matcherUnderTest.captureArgumentsFrom(invocation);

        verify(matcher).captureFrom("arg1");
        verify(matcher).captureFrom(new String[]{"varArg1", "varArg2"});
    }

    private Invocation createInvocation() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(getTestMethod());
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.isVerified()).thenReturn(false);
        when(invocation.getArguments()).thenReturn(new Object[]{"arg1", "arg2"});
        when(invocation.getLocation()).thenReturn(mock(Location.class));
        return invocation;
    }

    private Method getTestMethod() {
        try {
            return TestInterface.class.getDeclaredMethod("getTestMethod");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Invocation createInvocationWithDifferentMock() {
        Invocation invocation = createInvocation();
        when(invocation.getMock()).thenReturn(mock(Object.class));
        return invocation;
    }

    private Invocation createInvocationWithDifferentMethod() {
        Invocation invocation = createInvocation();
        try {
            Method method = TestInterface.class.getDeclaredMethod("getOtherMethod");
            when(invocation.getMethod()).thenReturn(method);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return invocation;
    }

    private Invocation createInvocationWithDifferentArguments() {
        Invocation invocation = createInvocation();
        when(invocation.getArguments()).thenReturn(new Object[]{"arg1", "arg3"});
        return invocation;
    }

    private Invocation createInvocationWithDifferentMethodName() {
        Invocation invocation = createInvocation();
        try {
            Method method = TestInterface.class.getDeclaredMethod("getDifferentMethod");
            when(invocation.getMethod()).thenReturn(method);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return invocation;
    }

    private Invocation createInvocationWithDifferentParameterTypes() {
        Invocation invocation = createInvocation();
        try {
            Method method = TestInterface.class.getDeclaredMethod("getTestMethod", Integer.class, String.class);
            when(invocation.getMethod()).thenReturn(method);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return invocation;
    }

    private Invocation createVerifiedInvocation() {
        Invocation invocation = createInvocation();
        when(invocation.isVerified()).thenReturn(true);
        return invocation;
    }

    private Invocation createOverloadedInvocation() {
        Invocation invocation = createInvocation();
        try {
            Method method = TestInterface.class.getDeclaredMethod("getTestMethod", Integer.class);
            when(invocation.getMethod()).thenReturn(method);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return invocation;
    }

    private Method getVarArgsMethod() {
        try {
            return TestInterface.class.getDeclaredMethod("getVarArgsMethod", String[].class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

    private interface TestInterface {
        void getTestMethod();
        void getOtherMethod();
        void getDifferentMethod();
        void getTestMethod(Integer i, String s);
        void getVarArgsMethod(String... args);
    }
}
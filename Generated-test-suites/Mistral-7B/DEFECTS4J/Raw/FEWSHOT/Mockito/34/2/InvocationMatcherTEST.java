package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.ArgumentsMatcher.argThat;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.exceptions.InvocationNotProperlySetException;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.mock.MockitoCore;
import org.mockito.stubbing.Answer;

public class InvocationMatcherTest {

    private static final Object[] EMPTY_ARRAY = new Object[0];

    @Test
    public void testNewInstanceWithMatchers() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = mockitoCore.mock(Object.class);
        Method method = Object.class.getMethod("method", String.class, int.class);

        List<Matcher> matchers = Arrays.asList(argThat(Matchers.isA(String.class)), argThat(Matchers.isA(int.class)));
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockitoCore.createInvocation(method, mock, "foo", 1), matchers);

        assertTrue(invocationMatcher.matches(mockitoCore.createInvocation(method, mock, "foo", 1)));
    }

    @Test
    public void testNewInstanceWithoutMatchers() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = mockitoCore.mock(Object.class);
        Method method = Object.class.getMethod("method", String.class, int.class);

        InvocationMatcher invocationMatcher = new InvocationMatcher(mockitoCore.createInvocation(method, mock, "foo", 1));

        assertTrue(invocationMatcher.matches(mockitoCore.createInvocation(method, mock, "foo", 1)));
    }

    @Test
    public void testGetMethod() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = mockitoCore.mock(Object.class);
        Method method = Object.class.getMethod("method", String.class, int.class);

        InvocationMatcher invocationMatcher = new InvocationMatcher(mockitoCore.createInvocation(method, mock, "foo", 1));

        assertTrue(method.equals(invocationMatcher.getMethod()));
    }

    @Test
    public void testGetInvocation() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = mockitoCore.mock(Object.class);
        Method method = Object.class.getMethod("method", String.class, int.class);

        InvocationMatcher invocationMatcher = new InvocationMatcher(mockitoCore.createInvocation(method, mock, "foo", 1));

        assertTrue(invocationMatcher.getInvocation().equals(mockitoCore.createInvocation(method, mock, "foo", 1)));
    }

    @Test
    public void testGetMatchers() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = mockitoCore.mock(Object.class);
        Method method = Object.class.getMethod("method", String.class, int.class);

        List<Matcher> matchers = Collections.singletonList(Matchers.isA(String.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockitoCore.createInvocation(method, mock, "foo", 1), matchers);

        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testToString() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = mockitoCore.mock(Object.class);
        Method method = Object.class.getMethod("method", String.class, int.class);

        List<Matcher> matchers = Collections.singletonList(Matchers.isA(String.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockitoCore.createInvocation(method, mock, "foo", 1), matchers);

        assertEquals("Invocation on mock [Object@0] using method [method(String, int)]\n" +
                "Arguments are:\n" +
                "0: String\n" +
                "1: int\n" +
                "Matchers:\n" +
                "0: String\n", invocationMatcher.toString());
    }

    @Test
    public void testMatchesWhenInvocationsAreEqual() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = mockitoCore.mock(Object.class);
        Method method = Object.class.getMethod("method", String.class, int.class);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(mockitoCore.createInvocation(method, mock, "foo", 1));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(mockitoCore.createInvocation(method, mock, "foo", 1));

        assertTrue(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testMatchesWhenInvocationsHaveDifferentArguments() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = mockitoCore.mock(Object.class);
        Method method = Object.class.getMethod("method", String.class, int.class);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(mockitoCore.createInvocation(method, mock, "foo", 1));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(mockitoCore.createInvocation(method, mock, "bar", 2));

        assertFalse(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testMatchesWhenInvocationsHaveSameOverloadedMethod() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = mockitoCore.mock(Object.class);
        Method method1 = Object.class.getMethod("method", String.class);
        Method method2 = Object.class.getMethod("method", int.class);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(mockitoCore.createInvocation(method1, mock, "foo"));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(mockitoCore.createInvocation(method2, mock, 1));

        assertFalse(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testMatchesWhenInvocationsHaveDifferentOverloadedMethods() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = mockitoCore.mock(Object.class);
        Method method1 = Object.class.getMethod("method1", String.class);
        Method method2 = Object.class.getMethod("method2", int.class);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(mockitoCore.createInvocation(method1, mock, "foo"));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(mockitoCore.createInvocation(method2, mock, 1));

        assertFalse(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testMatchesWhenInvocationsHaveSameMethodButDifferentArguments() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = mockitoCore.mock(Object.class);
        Method method = Object.class.getMethod("method", String.class, int.class);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(mockitoCore.createInvocation(method, mock, "foo", 1));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(mockitoCore.createInvocation(method, mock, "bar", 2));

        assertFalse(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testHasSameMethodWhenInvocationsHaveSameMethod() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = mockitoCore.mock(Object.class);
        Method method = Object.class.getMethod("method", String.class, int.class);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(mockitoCore.createInvocation(method, mock, "foo", 1));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(mockitoCore.createInvocation(method, mock, "bar", 2));
        InvocationMatcher invocationMatcher3 = new InvocationMatcher(mockitoCore.createInvocation(method, mock, "baz", 3));

        assertTrue(invocationMatcher1.hasSameMethod(invocationMatcher1));
        assertTrue(invocationMatcher1.hasSameMethod(invocationMatcher2));
        assertFalse(invocationMatcher1.hasSameMethod(invocationMatcher3));
    }

    @Test
    public void testHasSameMethodWhenInvocationsHaveSameMethodButDifferentArguments() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = mockitoCore.mock(Object.class);
        Method method = Object.class.getMethod("method", String.class, int.class);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(mockitoCore.createInvocation(method, mock, "foo", 1));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(mockitoCore.createInvocation(method, mock, "bar", 2));

        assertFalse(invocationMatcher1.hasSameMethod(invocationMatcher2));
    }

    @Test
    public void testHasSimilarMethodWhenInvocationsAreEqual() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = mockitoCore.mock(Object.class);
        Method method = Object.class.getMethod("method", String.class, int.class);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(mockitoCore.createInvocation(method, mock, "foo", 1));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(mockitoCore.createInvocation(method, mock, "foo", 1));

        assertTrue(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testHasSimilarMethodWhenInvocationsHaveSameMethodButDifferentArguments() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = mockitoCore.mock(Object.class);
        Method method = Object.class.getMethod("method", String.class, int.class);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(mockitoCore.createInvocation(method, mock, "foo", 1));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(mockitoCore.createInvocation(method, mock, "bar", 2));

        assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testHasSimilarMethodWhenInvocationsHaveOverloadedMethods() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = mockitoCore.mock(Object.class);
        Method method1 = Object.class.getMethod("method1", String.class);
        Method method2 = Object.class.getMethod("method2", int.class);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(mockitoCore.createInvocation(method1, mock, "foo"));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(mockitoCore.createInvocation(method2, mock, 1));

        assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testToStringWithPrintSettings() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = mockitoCore.mock(Object.class);
        Method method = Object.class.getMethod("method", String.class, int.class);

        PrintSettings printSettings = new PrintSettings();
        printSettings.setPrintStacktrace(false);

        List<Matcher> matchers = Collections.singletonList(Matchers.isA(String.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockitoCore.createInvocation(method, mock, "foo", 1), matchers);

        assertEquals("Invocation on mock [Object@0] using method [method(String, int)]\n" +
                "Arguments are:\n" +
                "0: String\n" +
                "1: int\n" +
                "Matchers:\n" +
                "0: String\n" +
                "\n" +
                "Printing is turned off", invocationMatcher.toString(printSettings));
    }

    @Test
    public void testCaptureArgumentsFromInvocation() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = mockitoCore.mock(Object.class);
        Method method = Object.class.getMethod("method", String.class, int.class);

        CapturesArguments capturesArguments = mockitoCore.spy(new CapturesArguments() {});
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockitoCore.createInvocation(method, mock, "foo", 1),
                Arrays.asList(capturesArguments));

        invocationMatcher.captureArgumentsFrom(mockitoCore.createInvocation(method, mock, "bar", 2));

        assertEquals("bar", capturesArguments.getValue(0));
        assertEquals(2, capturesArguments.size());
    }

    @Test(expected = InvocationNotProperlySetException.class)
    public void testCaptureArgumentsFromNullInvocation() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = mockitoCore.mock(Object.class);
        Method method = Object.class.getMethod("method", String.class, int.class);

        CapturesArguments capturesArguments = mockitoCore.spy(new CapturesArguments() {});
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockitoCore.createInvocation(method, mock, "foo", 1),
                Arrays.asList(capturesArguments));

        invocationMatcher.captureArgumentsFrom(null);
    }

    @Test
    public void testToStringWithoutMatchers() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = mockitoCore.mock(Object.class);
        Method method = Object.class.getMethod("method", String.class, int.class);

        InvocationMatcher invocationMatcher = new InvocationMatcher(mockitoCore.createInvocation(method, mock, "foo", 1));

        assertEquals("Invocation on mock [Object@0] using method [method(String, int)]\n" +
                "Arguments are:\n" +
                "0: String\n" +
                "1: int\n", invocationMatcher.toString());
    }
}
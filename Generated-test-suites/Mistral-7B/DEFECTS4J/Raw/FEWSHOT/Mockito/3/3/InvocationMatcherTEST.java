package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.InvocationOnMock;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.invocation.Invocation;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.util. ArgumentsProcessor;
import org.mockito.internal.util.ArgumentsComparator;
import org.mockito.internal.util.java.TypeUtils;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherWithEmptyListOfMatchers() {
        Invocation invocation = createInvocation("someClass", "someMethod", new Object[]{"arg1", "arg2"});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertTrue(invocationMatcher.matches(invocation));

        List<Matcher> matchers = Arrays.asList(Matchers.is("arg1"), Matchers.is("arg2"));
        InvocationMatcher invocationMatcherWithMatchers = new InvocationMatcher(invocation, matchers);
        assertTrue(invocationMatcherWithMatchers.matches(invocation));
    }

    @Test
    public void testMethodsHaveSameNameAndMatchersAreEqual() {
        Invocation invocation1 = createInvocation("someClass1", "someMethod1", new Object[]{"arg1"});
        Invocation invocation2 = createInvocation("someClass2", "someMethod1", new Object[]{"arg1"});
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);
        assertTrue(invocationMatcher1.matches(invocationMatcher2));

        List<Matcher> matchers = Arrays.asList(Matchers.isA(String.class), Matchers.is("arg1"));
        InvocationMatcher invocationMatcher1WithMatchers = new InvocationMatcher(invocation1, matchers);
        InvocationMatcher invocationMatcher2WithMatchers = new InvocationMatcher(invocation2, matchers);
        assertTrue(invocationMatcher1WithMatchers.matches(invocationMatcher2WithMatchers));
    }

    @Test
    public void testMethodsHaveSameNameButMatchersAreDifferent() {
        Invocation invocation1 = createInvocation("someClass1", "someMethod1", new Object[]{"arg1"});
        Invocation invocation2 = createInvocation("someClass2", "someMethod1", new Object[]{"arg2"});
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);
        assertFalse(invocationMatcher1.matches(invocationMatcher2));

        List<Matcher> matchers1 = Arrays.asList(Matchers.isA(String.class), Matchers.is("arg1"));
        InvocationMatcher invocationMatcher1WithMatchers = new InvocationMatcher(invocation1, matchers1);
        List<Matcher> matchers2 = Arrays.asList(Matchers.isA(Number.class), Matchers.is("arg2"));
        InvocationMatcher invocationMatcher2WithMatchers = new InvocationMatcher(invocation2, matchers2);
        assertFalse(invocationMatcher1WithMatchers.matches(invocationMatcher2WithMatchers));
    }

    @Test
    public void testMethodsHaveDifferentNamesButMatchersAreEqual() {
        Invocation invocation1 = createInvocation("someClass1", "someMethod1", new Object[]{"arg1"});
        Invocation invocation2 = createInvocation("someClass2", "someMethod2", new Object[]{"arg1"});
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);
        assertFalse(invocationMatcher1.matches(invocationMatcher2));

        List<Matcher> matchers = Arrays.asList(Matchers.isA(String.class), Matchers.is("arg1"));
        InvocationMatcher invocationMatcher1WithMatchers = new InvocationMatcher(invocation1, matchers);
        InvocationMatcher invocationMatcher2WithMatchers = new InvocationMatcher(invocation2, matchers);
        assertFalse(invocationMatcher1WithMatchers.matches(invocationMatcher2WithMatchers));
    }

    @Test
    public void testMethodsHaveDifferentNamesAndMatchersAreDifferent() {
        Invocation invocation1 = createInvocation("someClass1", "someMethod1", new Object[]{"arg1"});
        Invocation invocation2 = createInvocation("someClass2", "someMethod2", new Object[]{"arg2"});
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);
        assertFalse(invocationMatcher1.matches(invocationMatcher2));

        List<Matcher> matchers1 = Arrays.asList(Matchers.isA(String.class), Matchers.is("arg1"));
        InvocationMatcher invocationMatcher1WithMatchers = new InvocationMatcher(invocation1, matchers1);
        List<Matcher> matchers2 = Arrays.asList(Matchers.isA(Number.class), Matchers.is("arg2"));
        InvocationMatcher invocationMatcher2WithMatchers = new InvocationMatcher(invocation2, matchers2);
        assertFalse(invocationMatcher1WithMatchers.matches(invocationMatcher2WithMatchers));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = createInvocation("someClass1", "someMethod1", new Object[]{"arg1"});
        Invocation invocation2 = createInvocation("someClass1", "someMethod1", new Object[]{"arg1"});
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);
        assertTrue(invocationMatcher1.hasSameMethod(invocationMatcher2));

        Invocation invocation3 = createInvocation("someClass1", "someMethod2", new Object[]{"arg1"});
        InvocationMatcher invocationMatcher3 = new InvocationMatcher(invocation3);
        assertFalse(invocationMatcher1.hasSameMethod(invocationMatcher3));
    }

    @Test
    public void testHasSameMethodWithDifferentParameterTypes() {
        Invocation invocation1 = createInvocation("someClass1", "someMethod1", new Object[]{1, "str"});
        Invocation invocation2 = createInvocation("someClass1", "someMethod1", new Object[]{1, "str", 2});
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);
        assertFalse(invocationMatcher1.hasSameMethod(invocationMatcher2));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation1 = createInvocation("someClass1", "someMethod1", new Object[]{"arg1"});
        Invocation invocation2 = createInvocation("someClass2", "someMethod1", new Object[]{"arg1"});
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);
        assertTrue(invocationMatcher1.hasSimilarMethod(invocationMatcher2));

        Invocation invocation3 = createInvocation("someClass1", "someMethod2", new Object[]{"arg1"});
        InvocationMatcher invocationMatcher3 = new InvocationMatcher(invocation3);
        assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher3));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Mock mock = mock(Mock.class);
        Object[] arguments = new Object[]{1, 2, 3};
        Invocation invocation = createInvocation("someClass", "someMethod", arguments);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Object capturedArg1 = new Object();
        Object capturedArg2 = new Object();

        when(mock.someMethod(Matchers.any(Integer.class), Matchers.any(String.class), Matchers.anyObject())).thenReturn(capturedArg1);
        when(mock.someMethod(Matchers.any(Integer.class), Matchers.any(String.class), Matchers.anyObject())).thenReturn(capturedArg2);

        invocationMatcher.captureArgumentsFrom(invocation);
        verifyNoMoreInteractions(mock);

        assertEquals(capturedArg1, invocationMatcher.getArgumentAt(0, Object.class));
        assertEquals(capturedArg2, invocationMatcher.getArgumentAt(1, Object.class));
    }

    @Test
    public void testCaptureArgumentsFromVarArgMethod() {
        Mock mock = mock(Mock.class);
        Object[] arguments = new Object[]{1, 2, 3};
        Invocation invocation = createInvocation("someClass", "someMethod", arguments);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Object capturedArg1 = new Object();
        Object capturedArg2 = new Object();

        when(mock.someMethod(Matchers.any(Integer.class), Matchers.any(String.class), Matchers.anyArray(Object.class))).thenReturn(capturedArg1);
        when(mock.someMethod(Matchers.any(Integer.class), Matchers.any(String.class), Matchers.anyArray(Object.class))).thenReturn(capturedArg2);

        invocationMatcher.captureArgumentsFrom(invocation);
        verifyNoMoreInteractions(mock);

        assertEquals(capturedArg1, invocationMatcher.getArgumentAt(0, Object[].class)[0]);
        assertEquals(capturedArg2, invocationMatcher.getArgumentAt(0, Object[].class)[1]);
    }

    @Test
    public void testCreateFrom() {
        Invocation invocation1 = createInvocation("someClass1", "someMethod1", new Object[]{"arg1"});
        Invocation invocation2 = createInvocation("someClass1", "someMethod1", new Object[]{"arg2"});
        List<Invocation> invocations = Arrays.asList(invocation1, invocation2);
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        InvocationMatcher invocationMatcher1 = invocationMatchers.get(0);
        InvocationMatcher invocationMatcher2 = invocationMatchers.get(1);

        assertTrue(invocationMatcher1.matches(invocationMatcher2));
        assertFalse(invocationMatcher1.getArguments().equals(invocationMatcher2.getArguments()));
    }

    @Test
    public void testToString() {
        Invocation invocation = createInvocation("someClass", "someMethod", new Object[]{"arg1"});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        String expectedString = PrintSettings.DEFAULT_PRETTY_FORMAT.print(Arrays.asList(Matchers.is("someMethod"), Matchers.is("arg1")), invocation);
        String actualString = invocationMatcher.toString();
        assertEquals(expectedString, actualString);
    }

    private Invocation createInvocation(String className, String methodName, Object[] arguments) {
        Class<?> clazz = TypeUtils.forName(className);
        try {
            Method method = clazz.getMethod(methodName, TypeUtils.toTypes(arguments));
            return new InvocationMock(new InvocationOnMock(method, null, null, new Object[0], null, new Object[0], null, null, null));
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private static class Mock {
        public Object someMethod(int arg1, String arg2, Object arg3) {
            return null;
        }

        public Object someMethod(int arg1, String arg2, Object[] args) {
            return null;
        }
    }
}
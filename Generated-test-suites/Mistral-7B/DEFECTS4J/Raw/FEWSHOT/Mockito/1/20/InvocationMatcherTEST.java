package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.matchers.ArgsMatchers.eq;
import static org.mockito.matchers.ArgsMatchers.isNotNull;
import static org.mockito.matchers.ArgsMatchers.isNull;
import static org.mockito.matchers.ArgsMatchers.sameInstance;
import static org.mockito.matchers.ArgsMatchers.contains;

import java.lang.reflect.Method;
import org.junit.Test;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.invocation.Location;

public class InvocationMatcherTest {

    @Test
    public void testMatchesSameInvocation() {
        Object mock = Mockito.mock(Object.class);
        Object[] invocationArgs = new Object[] { 1, "foo" };
        Method method = Mockito.method(Object.class, "method", new Class[] { int.class, String.class });
        Invocation invocation = new InvocationOnMock(mock, method, invocationArgs, null, null);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertTrue(invocationMatcher.matches(invocation));
    }

    @Test
    public void testMatchesDifferentInvocationSameMethod() {
        Object mock = Mockito.mock(Object.class);
        Object[] invocationArgs1 = new Object[] { 1, "foo" };
        Object[] invocationArgs2 = new Object[] { 2, "bar" };
        Method method = Mockito.method(Object.class, "method", new Class[] { int.class, String.class });
        Invocation invocation1 = new InvocationOnMock(mock, method, invocationArgs1, null, null);
        Invocation invocation2 = new InvocationOnMock(mock, method, invocationArgs2, null, null);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        assertFalse(invocationMatcher.matches(invocation2));
    }

    @Test
    public void testMatchesDifferentInvocationDifferentMethods() {
        Object mock = Mockito.mock(Object.class);
        Object[] invocationArgs1 = new Object[] { 1, "foo" };
        Method method1 = Mockito.method(Object.class, "method1", new Class[] { int.class, String.class });
        Invocation invocation1 = new InvocationOnMock(mock, method1, invocationArgs1, null, null);

        Object[] invocationArgs2 = new Object[] { 2, "bar" };
        Method method2 = Mockito.method(Object.class, "method2", new Class[] { int.class, String.class });
        Invocation invocation2 = new InvocationOnMock(mock, method2, invocationArgs2, null, null);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        assertFalse(invocationMatcher.matches(invocation2));
    }

    @Test
    public void testMatchesDifferentInvocationSameMethodDifferentMocks() {
        Object mock1 = Mockito.mock(Object.class);
        Object mock2 = Mockito.mock(Object.class);
        Object[] invocationArgs = new Object[] { 1, "foo" };
        Method method = Mockito.method(Object.class, "method", new Class[] { int.class, String.class });
        Invocation invocation1 = new InvocationOnMock(mock1, method, invocationArgs, null, null);
        Invocation invocation2 = new InvocationOnMock(mock2, method, invocationArgs, null, null);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        assertFalse(invocationMatcher.matches(invocation2));
    }

    @Test
    public void testMatchesInvocationWithNullArgument() {
        Object mock = Mockito.mock(Object.class);
        Object[] invocationArgs = new Object[] { null, "foo" };
        Method method = Mockito.method(Object.class, "method", new Class[] { String.class });
        Invocation invocation = new InvocationOnMock(mock, method, invocationArgs, null, null);

        InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationMatcher(invocation));
        assertNull(invocationMatcher.getMatchers().get(0));
        assertTrue(invocationMatcher.matches(invocation));
    }

    @Test
    public void testMatchesInvocationWithEmptyMatcherList() {
        Object mock = Mockito.mock(Object.class);
        Object[] invocationArgs = new Object[] { 1, "foo" };
        Method method = Mockito.method(Object.class, "method", new Class[] { int.class, String.class });
        Invocation invocation = new InvocationOnMock(mock, method, invocationArgs, null, null);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.emptyList());
        assertTrue(invocationMatcher.matches(invocation));
    }

    @Test
    public void testMatchesInvocationWithCustomMatchers() {
        Object mock = Mockito.mock(Object.class);
        Object[] invocationArgs = new Object[] { 1, "foo" };
        Method method = Mockito.method(Object.class, "method", new Class[] { int.class, String.class });
        Invocation invocation = new InvocationOnMock(mock, method, invocationArgs, null, null);

        Matcher customMatcher1 = isNotNull();
        Matcher customMatcher2 = eq(1);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(customMatcher1, customMatcher2));

        assertTrue(invocationMatcher.matches(invocation));
        assertTrue(customMatcher1.matches(invocation.getArgumentAt(0, Object.class)));
        assertTrue(customMatcher2.matches(invocation.getArgumentAt(1, Object.class)));
    }

    @Test
    public void testMatchesInvocationWithCustomVarargMatcher() {
        Object mock = Mockito.mock(Object.class);
        Object[] invocationArgs = new Object[] { 1, "foo", new Object[] { 3, 4 } };
        Method method = Mockito.method(Object.class, "method", new Class[] { int.class, String.class, int[].class });
        Invocation invocation = new InvocationOnMock(mock, method, invocationArgs, null, null);

        Matcher varargMatcher = contains(eq(3), eq(4));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(isNotNull(), isNotNull(), varargMatcher));

        assertTrue(invocationMatcher.matches(invocation));
        assertTrue(varargMatcher.matches(invocation.getArgumentAt(2, Object[].class)));
    }

    @Test
    public void testMatchesInvocationWithVarargMatcher() {
        Object mock = Mockito.mock(Object.class);
        Object[] invocationArgs = new Object[] { 1, new Object[] { 3, 4 } };
        Method method = Mockito.method(Object.class, "method", new Class[] { int.class, int[].class });
        Invocation invocation = new InvocationOnMock(mock, method, invocationArgs, null, null);

        Matcher varargMatcher = contains(isNotNull(), isNotNull());

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(isNotNull(), varargMatcher));

        assertTrue(invocationMatcher.matches(invocation));
        assertTrue(varargMatcher.matches(invocation.getArgumentAt(1, Object[].class)));
    }

    @Test
    public void testMatchesInvocationWithMethodNameMatcher() {
        Object mock = Mockito.mock(Object.class);
        Object[] invocationArgs = new Object[] { 1, "foo" };
        Method method = Mockito.method(Object.class, "testMethod", new Class[] { int.class, String.class });
        Invocation invocation = new InvocationOnMock(mock, method, invocationArgs, null, null);

        Matcher methodNameMatcher = Mockito.argThat(invocationArg -> "testMethod".equals(invocationArg.getMethod().getName()));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(methodNameMatcher));

        assertTrue(invocationMatcher.matches(invocation));
        assertTrue(methodNameMatcher.matches(invocation));
    }

    @Test
    public void testMatchesInvocationWithLocationMatcher() {
        Object mock = Mockito.mock(Object.class);
        Object[] invocationArgs = new Object[] { 1, "foo" };
        Method method = Mockito.method(Object.class, "method", new Class[] { int.class, String.class });
        Invocation invocation = new InvocationOnMock(mock, method, invocationArgs, new Location("TestClass.java", 10), null);

        Matcher locationMatcher = Mockito.argThat(invocationArg -> "TestClass.java".equals(invocationArg.getLocation().getFileName()));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(locationMatcher));

        assertTrue(invocationMatcher.matches(invocation));
        assertTrue(locationMatcher.matches(invocation));
    }
}
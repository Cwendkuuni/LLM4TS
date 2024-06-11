package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.ArgumentsMatcher.notNull;
import static org.mockito.internal.matchers.ArgumentsMatcher.same;
import static org.mockito.internal.matchers.ArgumentsMatcher.type;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsNull;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcher() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Method method = Mockito.mock(Method.class);
        List<Matcher> matchers = Arrays.asList(type(String.class), new Is<String>("hello"));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertTrue(invocationMatcher instanceof DescribedInvocation);
        assertTrue(invocationMatcher instanceof CapturesArgumensFromInvocation);
        assertTrue(invocationMatcher instanceof Serializable);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(method, invocationMatcher.getMethod());
        assertEquals(matchers, invocationMatcher.getMatchers());
        assertTrue(invocationMatcher.toString().contains("String, hello"));
    }

    @Test
    public void testInvocationMatcherWithEmptyList() {
        Invocation invocation = Mockito.mock(Invocation.class);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertTrue(invocationMatcher instanceof DescribedInvocation);
        assertTrue(invocationMatcher instanceof CapturesArgumensFromInvocation);
        assertTrue(invocationMatcher instanceof Serializable);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertNull(invocationMatcher.getMatchers());
    }

    @Test
    public void testMatches() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method = Mockito.mock(Method.class);

        List<Matcher> matchers1 = Arrays.asList(type(String.class), new Is<String>("hello"));
        List<Matcher> matchers2 = Arrays.asList(type(Integer.class), new Is<Integer>(42));

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, matchers1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, matchers2);

        assertFalse(invocationMatcher1.matches(invocationMatcher2));
        assertFalse(invocationMatcher2.matches(invocationMatcher1));

        Invocation invocation = Mockito.mock(Invocation.class);
        invocation.setMock(invocation1.getMock());
        invocation.setMethod(method);
        invocationMatcher1.captureArgumentsFrom(invocation);

        assertTrue(invocationMatcher1.matches(invocationMatcher1));
        assertTrue(invocationMatcher1.matches(invocationMatcher2));
        assertFalse(invocationMatcher2.matches(invocationMatcher1));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);

        Mockito.when(method1.getName()).thenReturn("test");
        Mockito.when(method2.getName()).thenReturn("test");
        Mockito.when(method1.getParameterTypes()).thenReturn(new Class[]{String.class, Integer.class});
        Mockito.when(method2.getParameterTypes()).thenReturn(new Class[]{String.class, Integer.class});

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Collections.emptyList());
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.emptyList());

        assertTrue(invocationMatcher1.hasSameMethod(invocationMatcher2));
        assertTrue(invocationMatcher2.hasSameMethod(invocationMatcher1));

        Object[] arguments1 = new Object[]{"foo", 1};
        Object[] arguments2 = new Object[]{"bar", 2};

        Invocation invocation = Mockito.mock(Invocation.class);
        invocation.setMethod(method1);
        invocation.setArguments(arguments1);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertFalse(invocationMatcher.hasSameMethod(new InvocationMatcher(invocation, Collections.singletonList(ArgumentMatchers.notNull()))));
        assertFalse(invocationMatcher.hasSameMethod(new InvocationMatcher(invocation, Arrays.asList(type(String.class), ArgumentMatchers.notNull()))));
        assertFalse(invocationMatcher.hasSameMethod(new InvocationMatcher(invocation, Arrays.asList(type(String.class), ArgumentMatchers.notNull()), method2)));
        assertFalse(invocationMatcher.hasSameMethod(new InvocationMatcher(invocation, Arrays.asList(type(String.class), ArgumentMatchers.notNull()), method2, Collections.singletonList(ArgumentMatchers.notNull()))));
        assertTrue(invocationMatcher.hasSameMethod(new InvocationMatcher(invocation, Arrays.asList(type(String.class), ArgumentMatchers.is("foo")))));
        assertTrue(invocationMatcher.hasSameMethod(new InvocationMatcher(invocation, Arrays.asList(type(String.class), ArgumentMatchers.is("bar")))));
        assertTrue(invocationMatcher.hasSameMethod(new InvocationMatcher(invocation, Collections.singletonList(ArgumentMatchers.notNull()))));
        assertTrue(invocationMatcher.hasSameMethod(new InvocationMatcher(invocation, Arrays.asList(type(String.class), ArgumentMatchers.is("foo")), method2)));
        assertTrue(invocationMatcher.hasSameMethod(new InvocationMatcher(invocation, Arrays.asList(type(String.class), ArgumentMatchers.is("foo")), method2, Collections.singletonList(ArgumentMatchers.notNull()))));
    }

    @Test
    public void testToString() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Method method = Mockito.mock(Method.class);
        List<Matcher> matchers = Arrays.asList(type(String.class), new Is<String>("hello"));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        String expected = "InvocationMatcher: " +
                "method: testMethod(java.lang.String, java.lang.String), " +
                "matchers: [TypeMatcher{type=CLASS, value=class java.lang.String}, Matcher{description=is (hello)}]";

        assertTrue(invocationMatcher.toString().contains(expected));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(type(String.class), ArgumentMatchers.notNull());

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        String arg1 = "foo";
        String arg2 = "bar";

        Mockito.when(invocation.getArgumentAt(0, Object.class)).thenReturn(arg1);
        Mockito.when(invocation.getArgumentAt(1, Object.class)).thenReturn(arg2);

        invocationMatcher.captureArgumentsFrom(invocation);

        CapturesArguments capturesArguments1 = (CapturesArguments) matchers.get(0);
        CapturesArguments capturesArguments2 = (CapturesArguments) matchers.get(1);

        assertTrue(capturesArguments1.getValue() instanceof String);
        assertEquals(arg1, capturesArguments1.getValue());
        assertTrue(capturesArguments2.getValue() instanceof Object);
        assertNull(capturesArguments2.getValue());
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = Arrays.asList(Mockito.mock(Invocation.class), Mockito.mock(Invocation.class));
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        assertEquals(2, invocationMatchers.size());

        InvocationMatcher invocationMatcher1 = invocationMatchers.get(0);
        InvocationMatcher invocationMatcher2 = invocationMatchers.get(1);

        assertNotNull(invocationMatcher1.getInvocation());
        assertNotNull(invocationMatcher2.getInvocation());
        assertTrue(invocationMatcher1.getMatchers().isEmpty());
        assertTrue(invocationMatcher2.getMatchers().isEmpty());
    }

    @Test
    public void testCreateFromWithMatchers() {
        List<Invocation> invocations = Arrays.asList(Mockito.mock(Invocation.class), Mockito.mock(Invocation.class));

        List<Matcher> matchers1 = Arrays.asList(type(String.class), ArgumentMatchers.is("foo"));
        List<Matcher> matchers2 = Arrays.asList(type(Integer.class), ArgumentMatchers.is(1));

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations, matchers1, matchers2);

        assertEquals(2, invocationMatchers.size());

        InvocationMatcher invocationMatcher1 = invocationMatchers.get(0);
        InvocationMatcher invocationMatcher2 = invocationMatchers.get(1);

        assertNotNull(invocationMatcher1.getInvocation());
        assertNotNull(invocationMatcher2.getInvocation());
        assertFalse(invocationMatcher1.getMatchers().isEmpty());
        assertFalse(invocationMatcher2.getMatchers().isEmpty());
        assertTrue(invocationMatcher1.getMatchers().get(0).matches("foo"));
        assertTrue(invocationMatcher2.getMatchers().get(0).matches(1));
    }
}
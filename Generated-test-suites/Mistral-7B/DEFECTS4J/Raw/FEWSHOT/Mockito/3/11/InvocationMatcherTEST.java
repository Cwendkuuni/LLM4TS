package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.invocation.InvocationOnMock.invocationWith;

import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsInstanceOf;
import org.hamcrest.core.IsNull;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InternalVerifier;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.invocation.Invocation;
import org.mockito.internal.matchers.MatcherDecorator;
import org.mockito.internal.reporting.PrintSettings;

import java.lang.reflect.Method;
import java.util.*;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcher() {
        Invocation invocation = invocationWith(Object.class, "methodName", new Object[]{1L, "test"}, new Object[0][], new Object[]{1L, "test"});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertNotNull(invocationMatcher.getInvocation());;
        assertTrue(invocationMatcher.hasSameMethod(invocationMatcher.getInvocation()));
        assertTrue(invocationMatcher.matches(invocationMatcher.getInvocation()));
        assertEquals("InvocationMatcher [method=methodName, invocation=Invocation [method=methodName, target=<mock>, arguments=[1, test], invocationsToIgnore=[]]", invocationMatcher.toString());
    }

    @Test
    public void testInvocationMatcherWithEmptyMatcherList() {
        Invocation invocation = invocationWith(Object.class, "methodName", new Object[]{1L, "test"}, new Object[0][], new Object[]{1L, "test"});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.emptyList());
        assertNotNull(invocationMatcher.getInvocation());;
        assertTrue(invocationMatcher.hasSameMethod(invocationMatcher.getInvocation()));
        assertTrue(invocationMatcher.matches(invocationMatcher.getInvocation()));
        assertEquals("InvocationMatcher [method=methodName, invocation=Invocation [method=methodName, target=<mock>, arguments=[1, test], invocationsToIgnore=[]]", invocationMatcher.toString());
    }

    @Test
    public void testInvocationMatcherWithMatchers() {
        Object mock = mock(Object.class);
        Invocation invocation1 = invocationWith(Object.class, "methodName", new Object[]{1L, "test"}, new Object[0][], new Object[]{1L, "test"});
        Invocation invocation2 = invocationWith(Object.class, "methodName", new Object[]{2L, "test2"}, new Object[0][], new Object[]{2L, "test2"});
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq(1L), ArgumentMatchers.eq("test"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1, matchers);
        assertTrue(invocationMatcher.matches(invocation1));
        assertFalse(invocationMatcher.matches(invocation2));
    }

    @Test
    public void testHasSameMethod() {
        Object mock = mock(Object.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(method1.getName()).thenReturn("methodName");
        when(method2.getName()).thenReturn("methodName");
        Invocation invocation1 = invocationWith(Object.class, method1, new Object[]{1L, "test"}, new Object[0][], new Object[]{1L, "test"});
        Invocation invocation2 = invocationWith(Object.class, method2, new Object[]{2L, "test2"}, new Object[0][], new Object[]{2L, "test2"});
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Collections.emptyList());
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.emptyList());
        assertTrue(invocationMatcher1.hasSameMethod(invocationMatcher1));
        assertFalse(invocationMatcher1.hasSameMethod(invocationMatcher2));
        assertFalse(invocationMatcher2.hasSameMethod(invocationMatcher1));
        assertFalse(invocationMatcher2.hasSameMethod(invocationMatcher2));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Object mock = mock(Object.class);
        Invocation invocation = invocationWith(Object.class, "methodName", new Object[]{1L, "test"}, new Object[0][], new Object[]{1L, "test"});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Matcher matcher = ArgumentMatchers.argThat(new Matcher<Object>() {
            @Override
            public boolean matches(Object argument) {
                return "test".equals(argument);
            }
        });
        CapturesArguments capturesArguments = Mockito.argThat(new MatcherDecorator<CapturesArguments, Object>() {
            @Override
            public Matcher<CapturesArguments> decorate(Matcher<Object> original) {
                return ArgumentMatchers.argThat(new Matcher<CapturesArguments>() {
                    @Override
                    public boolean matches(CapturesArguments actual) {
                        return original.matches(actual.getValue());
                    }
                });
            }
        });
        invocationMatcher.captureArgumentsFrom(invocation);
        MatcherAssert.assertThat(invocationMatcher.getMatchers().get(0), is(matcher));
        MatcherAssert.assertThat(invocationMatcher.getMatchers().get(1), is(capturesArguments));
    }

    @Test
    public void testCreateFrom() {
        Object mock = mock(Object.class);
        List<Invocation> invocations = Arrays.asList(
                invocationWith(Object.class, "methodName", new Object[]{1L, "test1"}, new Object[0][], new Object[]{1L, "test1"}),
                invocationWith(Object.class, "methodName", new Object[]{2L, "test2"}, new Object[0][], new Object[]{2L, "test2"}));
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        InvocationMatcher invocationMatcher1 = invocationMatchers.get(0);
        InvocationMatcher invocationMatcher2 = invocationMatchers.get(1);
        assertTrue(invocationMatcher1.matches(invocationMatcher1.getInvocation()));
        assertFalse(invocationMatcher1.matches(invocationMatcher2.getInvocation()));
        assertFalse(invocationMatcher2.matches(invocationMatcher1.getInvocation()));
        assertFalse(invocationMatcher2.matches(invocationMatcher2.getInvocation()));
    }

    @Test
    public void testToString() {
        Object mock = mock(Object.class);
        Invocation invocation = invocationWith(Object.class, "methodName", new Object[]{1L, "test"}, new Object[0][], new Object[]{1L, "test"});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        String print = new PrintSettings().print(invocationMatcher.getMatchers(), invocationMatcher);
        assertTrue(print.contains("methodName"));
        assertTrue(print.contains("InvocationMatcher"));
    }

    @Test
    public void testSafelyArgumentsMatchException() {
        Object mock = mock(Object.class);
        Invocation invocation = invocationWith(Object.class, "methodName", new Object[]{1L, "test"}, new Object[0][], new Object[]{1L, "test"});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertFalse(invocationMatcher.safelyArgumentsMatch(new Object[]{2L, "test2"}));
    }

    @Test
    public void testHasSimilarMethod() {
        Object mock = mock(Object.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        Method method3 = mock(Method.class);
        when(method1.getName()).thenReturn("methodName");
        when(method2.getName()).thenReturn("methodName");
        when(method3.getName()).thenReturn("anotherMethodName");
        Invocation invocation1 = invocationWith(Object.class, method1, new Object[]{1L, "test"}, new Object[0][], new Object[]{1L, "test"});
        Invocation invocation2 = invocationWith(Object.class, method1, new Object[]{1L, "test2"}, new Object[0][], new Object[]{1L, "test2"});
        Invocation invocation3 = invocationWith(Object.class, method2, new Object[]{1L, "test"}, new Object[0][], new Object[]{1L, "test"});
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Collections.emptyList());
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.emptyList());
        InvocationMatcher invocationMatcher3 = new InvocationMatcher(invocation3, Collections.emptyList());
        assertTrue(invocationMatcher1.hasSimilarMethod(invocationMatcher1));
        assertTrue(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
        assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher3));
    }
}
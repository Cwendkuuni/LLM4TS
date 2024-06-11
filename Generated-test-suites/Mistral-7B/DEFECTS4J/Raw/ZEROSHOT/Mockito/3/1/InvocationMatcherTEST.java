package org.mockito.internal.invocation;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.InvocationOnMock;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

@SuppressWarnings("unchecked")
public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcher_empty_list() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.emptyList());

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertNotNull(invocationMatcher.getMatchers());
        assertEquals(0, invocationMatcher.getMatchers().size());
        assertEquals(invocation.getMethod(), invocationMatcher.getMethod());
        assertEquals(invocation.getLocation(), invocationMatcher.getLocation());

        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation);
        assertEquals(invocationMatcher, invocationMatcher2);
        assertEquals(invocationMatcher.hashCode(), invocationMatcher2.hashCode());
    }

    @Test
    public void testInvocationMatcher_matched_list() {
        Object[] arguments = new Object[]{"arg1", 1, 2.0, "arg4"};
        Invocation invocation = new InvocationOnMock(new Object(){
            void method(String arg1, int arg2, Double arg3, String arg4) {}
        }, new Method(Mockito.class.getDeclaredMethod("method", String.class, int.class, Double.class, String.class)), new Object[]{arguments[0], arguments[1], arguments[2], arguments[3]}, new Location(1, 1, "TestClass.java"), null);
        List<Matcher> matchers = Arrays.asList(argThat(isString()), is(1), is(2.0), argThat(isA(String.class)));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertTrue(invocationMatcher.matches(invocation));
        Matcher[] matchersArray = invocationMatcher.getMatchers().toArray(new Matcher[0]);
        for (Matcher matcher : matchersArray) {
            MatcherAssert.assertThat(invocation.getArgumentAt(matcher.getIndex(), Object.class), matcher);
        }
    }

    @Test
    public void testInvocationMatcher_unmatched_list() {
        Object[] arguments = new Object[]{"arg1", 1, 2.0, "arg4"};
        Invocation invocation = new InvocationOnMock(new Object(){
            void method(String arg1, int arg2, Double arg3, String arg4) {}
        }, new Method(Mockito.class.getDeclaredMethod("otherMethod", String.class, Integer.TYPE, Double.TYPE, String.class)), new Object[]{arguments[0], arguments[1], arguments[2], arguments[3]}, new Location(1, 1, "TestClass.java"), null);
        List<Matcher> matchers = Arrays.asList(argThat(isInstanceOf(String.class)), is(1), is(2.0), argThat(isA(Integer.class)));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertFalse(invocationMatcher.matches(invocation));
        Matcher[] matchersArray = invocationMatcher.getMatchers().toArray(new Matcher[0]);
        for (Matcher matcher : matchersArray) {
            fail("Matcher should not be present: " + matcher);
        }
    }

    @Test
    public void testInvocationMatcher_varargs() {
        Object[] arguments = new Object[]{"arg1", new Object[]{1, 2, 3}};
        Invocation invocation = new InvocationOnMock(new Object(){
            void method(String arg1, Object... args) {}
        }, new Method(Mockito.class.getDeclaredMethod("method", String.class, Object[].class)), new Object[]{arguments[0]}, new Location(1, 1, "TestClass.java"), null);
        List<Matcher> matchers = Arrays.asList(argThat(isString()), argThat(isA(Object[].class)), argThat(hasItem(is(1))), argThat(hasItem(is(2))), argThat(hasItem(is(3))));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertTrue(invocationMatcher.matches(invocation));
        Matcher[] matchersArray = invocationMatcher.getMatchers().toArray(new Matcher[0]);
        for (Matcher matcher : matchersArray) {
            if (matcher instanceof CapturesArguments) {
                CapturesArguments capturedArguments = (CapturesArguments) matcher;
                MatcherAssert.assertThat(capturedArguments.getArguments(), containsInAnyOrder(is(1), is(2), is(3)));
            } else {
                MatcherAssert.assertThat(invocation.getArgumentAt(matcher.getIndex(), Object.class), matcher);
            }
        }
    }

    @Test
    public void testInvocationMatcher_hasSameMethod() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        when(invocation1.getMethod()).thenReturn(new Method(Mockito.class.getDeclaredMethod("method1", String.class, int.class)));
        when(invocation2.getMethod()).thenReturn(new Method(Mockito.class.getDeclaredMethod("method1", String.class, int.class)));

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);
        assertTrue(invocationMatcher1.hasSameMethod(invocationMatcher2));

        when(invocation1.getMethod()).thenReturn(new Method(Mockito.class.getDeclaredMethod("method2", String.class, double.class)));
        assertFalse(invocationMatcher1.hasSameMethod(invocationMatcher2));
    }

    @Test
    public void testInvocationMatcher_hasSimilarMethod() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        when(invocation1.getMethod()).thenReturn(new Method(Mockito.class.getDeclaredMethod("method1", String.class, int.class, double.class)));
        when(invocation2.getMethod()).thenReturn(new Method(Mockito.class.getDeclaredMethod("method1", String.class, int.class)));

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);
        assertTrue(invocationMatcher1.hasSimilarMethod(invocationMatcher2));

        when(invocation1.getMethod()).thenReturn(new Method(Mockito.class.getDeclaredMethod("method1", String.class, int.class, double.class, String.class)));
        assertTrue(invocationMatcher1.hasSimilarMethod(invocationMatcher2));

        when(invocation1.getMethod()).thenReturn(new Method(Mockito.class.getDeclaredMethod("method2", String.class, int.class, double.class)));
        assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testInvocationMatcher_captureArgumentsFromInvocation() {
        Object[] arguments = new Object[]{"arg1", new Object[]{1, 2, 3}};
        Invocation invocation = new InvocationOnMock(new Object(){
            void method(String arg1, Object... args) {}
        }, new Method(Mockito.class.getDeclaredMethod("method", String.class, Object[].class)), new Object[]{arguments[0]}, new Location(1, 1, "TestClass.java"), null);
        List<Matcher> matchers = Arrays.asList(argThat(isString()), argThat(hasItem(is(1))), argThat(hasItem(is(2))), argThat(hasItem(is(3))));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        invocationMatcher.captureArgumentsFrom(invocation);
        Matcher[] matchersArray = invocationMatcher.getMatchers().toArray(new Matcher[0]);
        for (Matcher matcher : matchersArray) {
            if (matcher instanceof CapturesArguments) {
                CapturesArguments capturedArguments = (CapturesArguments) matcher;
                MatcherAssert.assertThat(capturedArguments.getArguments(), containsInAnyOrder(is(1), is(2), is(3)));
            } else {
                MatcherAssert.assertThat(invocation.getArgumentAt(matcher.getIndex(), Object.class), matcher);
            }
        }
    }

    @Test
    public void testCreateFrom_empty_list() {
        List<Invocation> invocations = Collections.emptyList();
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        assertTrue(invocationMatchers.isEmpty());
    }

    @Test
    public void testCreateFrom_matched_list() {
        Object[] arguments = new Object[]{"arg1", 1, 2.0, "arg4"};
        Invocation invocation1 = new InvocationOnMock(new Object(){
            void method(String arg1, int arg2, Double arg3, String arg4) {}
        }, new Method(Mockito.class.getDeclaredMethod("method", String.class, int.class, Double.class, String.class)), new Object[]{arguments[0], arguments[1], arguments[2], arguments[3]}, new Location(1, 1, "TestClass.java"), null);
        Invocation invocation2 = new InvocationOnMock(new Object(){
            void method(String arg1, int arg2, Double arg3, String arg4) {}
        }, new Method(Mockito.class.getDeclaredMethod("method", String.class, int.class, Double.class, String.class)), new Object[]{arguments[0], arguments[1], arguments[2], arguments[3]}, new Location(1, 1, "TestClass.java"), null);
        List<Matcher> matchers = Arrays.asList(argThat(isString()), is(1), is(2.0), argThat(isA(String.class)));
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(Arrays.asList(invocation1, invocation2));

        InvocationMatcher invocationMatcher1 = invocationMatchers.get(0);
        assertEquals(invocation1, invocationMatcher1.getInvocation());
        assertEquals(matchers, invocationMatcher1.getMatchers());

        InvocationMatcher invocationMatcher2 = invocationMatchers.get(1);
        assertEquals(invocation2, invocationMatcher2.getInvocation());
        assertEquals(matchers, invocationMatcher2.getMatchers());
    }
}
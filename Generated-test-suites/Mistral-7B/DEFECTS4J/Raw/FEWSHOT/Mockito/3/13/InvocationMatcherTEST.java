package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.reporting.PrintSettings.print;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.InvocationOnMock;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationOnMockImpl;
import org.mockito.invocation.Location;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcher_withMatchers() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher<Object>> matchers = Arrays.asList(
                Matchers.argThat(argument -> argument.equals("foo")),
                Matchers.argThat(MatcherDecorator.not(Matchers.isNull()))
        );
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        assertNotNull(matcher.getMatchers());
        assertTrue(matcher.matches(createInvocationWith("foo", "bar")));
        assertFalse(matcher.matches(createInvocationWith("baz", "bar")));
    }

    @Test
    public void testInvocationMatcher_withoutMatchers() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertNotNull(matcher.getMatchers());
        assertTrue(matcher.matches(createInvocationWith("foo", "bar")));
    }

    @Test
    public void testToString() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher<Object>> matchers = Arrays.asList(
                Matchers.argThat(argument -> argument.equals("foo")),
                Matchers.argThat(MatcherDecorator.not(Matchers.isNull()))
        );
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        String expected = print(matchers, invocation);
        String actual = matcher.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testMatches_withSameInvocation() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        assertTrue(matcher.matches(invocation));
    }

    @Test
    public void testMatches_withDifferentInvocation_differentMethod() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);

        InvocationMatcher matcher = new InvocationMatcher(invocation1, Collections.<Matcher<Object>>emptyList());

        when(invocation1.getMethod()).thenReturn(method1);
        when(invocation2.getMethod()).thenReturn(method2);

        assertFalse(matcher.matches(invocation2));
    }

    @Test
    public void testMatches_withDifferentInvocation_sameMethod() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method = Mockito.mock(Method.class);

        InvocationMatcher matcher = new InvocationMatcher(invocation1, Collections.<Matcher<Object>>emptyList());

        when(invocation1.getMethod()).thenReturn(method);
        when(invocation2.getMethod()).thenReturn(method);

        assertTrue(matcher.matches(invocation2));
    }

    @Test
    public void testMatches_withUnverifiedInvocation() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method = Mockito.mock(Method.class);

        InvocationMatcher matcher = new InvocationMatcher(invocation1);

        when(invocation1.getMethod()).thenReturn(method);
        when(invocation2.getMock()).thenReturn(invocation1.getMock());

        when(invocation2.isVerified()).thenReturn(false);

        assertTrue(matcher.matches(invocation2));
    }

    @Test
    public void testMatches_withVerifiedInvocation() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method = Mockito.mock(Method.class);

        InvocationMatcher matcher = new InvocationMatcher(invocation1);

        when(invocation1.getMethod()).thenReturn(method);
        when(invocation2.getMock()).thenReturn(invocation1.getMock());

        when(invocation2.isVerified()).thenReturn(true);

        assertFalse(matcher.matches(invocation2));
    }

    @Test
    public void testMatches_withDifferentInvocation_differentClass() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(InvocationOnMockImpl.class);

        String methodName = "testMethod";
        Class testClass = Mockito.mock(Class.class);

        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);

        when(invocation1.getMethod()).thenReturn(method1);
        when(invocation1.getMock()).thenReturn(testClass);
        when(invocation2.getMethod()).thenReturn(method2);
        when(invocation2.getMock()).thenReturn(testClass);
        when(method1.getName()).thenReturn(methodName);
        when(method2.getName()).thenReturn(methodName);

        InvocationMatcher matcher = new InvocationMatcher(invocation1, Collections.<Matcher<Object>>emptyList());

        assertFalse(matcher.matches(invocation2));
    }

    @Test
    public void testMatches_withDifferentInvocation_sameClass() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(InvocationOnMockImpl.class);

        String methodName = "testMethod";
        Class testClass = Mockito.mock(Class.class);

        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);

        when(invocation1.getMethod()).thenReturn(method1);
        when(invocation1.getMock()).thenReturn(testClass);
        when(invocation2.getMethod()).thenReturn(method2);
        when(invocation2.getMock()).thenReturn(testClass);
        when(method1.getName()).thenReturn(methodName);
        when(method2.getName()).thenReturn(methodName);

        InvocationMatcher matcher = new InvocationMatcher(invocation1, Collections.<Matcher<Object>>emptyList());

        assertTrue(matcher.matches(invocation2));
    }

    @Test
    public void testHasSameMethod_withSameInvocation() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Method method = Mockito.mock(Method.class);

        InvocationMatcher matcher = new InvocationMatcher(invocation);

        when(invocation.getMethod()).thenReturn(method);

        assertTrue(matcher.hasSameMethod(invocation));
    }

    @Test
    public void testHasSameMethod_withDifferentInvocation_differentMethod() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);

        when(invocation1.getMethod()).thenReturn(method1);
        when(invocation2.getMethod()).thenReturn(method2);

        assertFalse(matcher.hasSameMethod(invocation2));
    }

    @Test
    public void testHasSameMethod_withDifferentInvocation_sameMethod() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method = Mockito.mock(Method.class);

        when(invocation1.getMethod()).thenReturn(method);
        when(invocation2.getMethod()).thenReturn(method);

        assertTrue(matcher.hasSameMethod(invocation2));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher<Object>> matchers = Arrays.asList(
                Matchers.argThat(argument -> argument.equals("foo"))
        );
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        when(invocation.getArguments()).thenReturn(new Object[]{1, 2, "foo", 4, "bar"});

        CapturesArguments captor = Mockito.mock(CapturesArguments.class);
        when(matchers.get(0)).thenReturn(captor);

        matcher.captureArgumentsFrom(invocation);

        verify(captor).captureFrom("foo");
    }

    @Test
    public void testCreateFrom() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);

        List<InvocationMatcher> matchers = Arrays.asList(
                new InvocationMatcher(invocation1),
                new InvocationMatcher(invocation2)
        );

        List<InvocationMatcher> expected = InvocationMatcher.createFrom(Arrays.asList(invocation1, invocation2));

        List<InvocationMatcher> actual = InvocationMatcher.createFrom(Arrays.asList(invocation1, invocation2));

        assertEquals(expected, actual);
    }

    private Invocation createInvocationWith(Object... arguments) {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        when(invocation.getArguments()).thenReturn(arguments);
        return invocation;
    }

    private void verify(Matcher matcher) {
        // no-op
    }
}
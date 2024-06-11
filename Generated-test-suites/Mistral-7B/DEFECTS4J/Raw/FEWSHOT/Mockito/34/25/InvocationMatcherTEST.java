package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.ArgumentsMatchers.isA;
import static org.mockito.internal.matchers.ArgumentsMatchers.isInstanceOf;
import static org.mockito.internal.matchers.ArgumentsMatchers.same;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.stub.Stubber;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcher_withEmptyMatchers() {
        Stubber stubber = org.mockito.Mockito.stub();
        Invocation invocation = stubber.invocationOn(org.mockito.Mockito.mock(Object.class)).methodCall(isA(Method.class));
        InvocationMatcher matcher = new InvocationMatcher(invocation, Collections.emptyList());
        assertTrue(matcher.matches(invocation));
    }

    @Test
    public void testInvocationMatcher_withMatchers() {
        Object mock = org.mockito.Mockito.mock(Object.class);
        Stubber stubber = org.mockito.Mockito.stub();
        Method method = MockitoTestUtils.getMethod(Object.class, "testMethod", String.class);
        Invocation invocation = stubber.invocationOn(mock).methodCall(method).withArguments(same(new String[]{"foo"}));
        List<Matcher> matchers = Collections.singletonList(isInstanceOf(String.class));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);
        assertTrue(matcher.matches(invocation));
    }

    @Test
    public void testGetMethod() {
        Object mock = org.mockito.Mockito.mock(Object.class);
        Method method = MockitoTestUtils.getMethod(Object.class, "testMethod", String.class);
        InvocationMatcher matcher = new InvocationMatcher(new InvocationOnMock(mock, method), Collections.emptyList());
        assertEquals(method, matcher.getMethod());
    }

    @Test
    public void testGetInvocation() {
        Object mock = org.mockito.Mockito.mock(Object.class);
        Method method = MockitoTestUtils.getMethod(Object.class, "testMethod", String.class);
        Invocation invocation = new InvocationOnMock(mock, method);
        InvocationMatcher matcher = new InvocationMatcher(invocation, Collections.emptyList());
        assertEquals(invocation, matcher.getInvocation());
    }

    @Test
    public void testGetMatchers() {
        Object mock = org.mockito.Mockito.mock(Object.class);
        Method method = MockitoTestUtils.getMethod(Object.class, "testMethod", String.class);
        Invocation invocation = new InvocationOnMock(mock, method);
        InvocationMatcher matcher = new InvocationMatcher(invocation, Collections.singletonList(isInstanceOf(String.class)));
        assertEquals(Collections.singletonList(isInstanceOf(String.class)), matcher.getMatchers());
    }

    @Test
    public void testToString() {
        Object mock = org.mockito.Mockito.mock(Object.class);
        Method method = MockitoTestUtils.getMethod(Object.class, "testMethod", String.class);
        Invocation invocation = new InvocationOnMock(mock, method);
        List<Matcher> matchers = Collections.singletonList(isInstanceOf(String.class));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);
        String expected = "invocation: testMethod(java.lang.String) on " + mock.getClass().getName() + " at " + new Location(0, 0) + " with arguments: <[java.lang.String]>";
        assertEquals(expected, matcher.toString());
    }

    @Test
    public void testMatches_withSameInvocation() {
        Object mock = org.mockito.Mockito.mock(Object.class);
        Method method = MockitoTestUtils.getMethod(Object.class, "testMethod", String.class);
        Invocation invocation = new InvocationOnMock(mock, method);
        List<Matcher> matchers = Collections.emptyList();
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);
        assertTrue(matcher.matches(invocation));
    }

    @Test
    public void testMatches_withDifferentInvocations() {
        Object mock1 = org.mockito.Mockito.mock(Object.class);
        Object mock2 = org.mockito.Mockito.mock(Object.class);
        Method method = MockitoTestUtils.getMethod(Object.class, "testMethod", String.class);
        Invocation invocation1 = new InvocationOnMock(mock1, method);
        Invocation invocation2 = new InvocationOnMock(mock2, method);
        List<Matcher> matchers = Collections.emptyList();
        InvocationMatcher matcher = new InvocationMatcher(invocation1, matchers);
        assertFalse(matcher.matches(invocation2));
    }

    @Test
    public void testHasSameMethod_withSameInvocations() {
        Object mock = org.mockito.Mockito.mock(Object.class);
        Method method1 = MockitoTestUtils.getMethod(Object.class, "testMethod1", String.class);
        Method method2 = MockitoTestUtils.getMethod(Object.class, "testMethod2", String.class);
        Invocation invocation = new InvocationOnMock(mock, method1);
        InvocationMatcher matcher = new InvocationMatcher(invocation, Collections.emptyList());
        assertTrue(matcher.hasSameMethod(new InvocationMatcher(invocation, Collections.emptyList())));
        assertFalse(matcher.hasSameMethod(new InvocationMatcher(new InvocationOnMock(mock, method2), Collections.emptyList())));
    }

    @Test
    public void testHasSameMethod_withDifferentInvocations() {
        Object mock = org.mockito.Mockito.mock(Object.class);
        Method method1 = MockitoTestUtils.getMethod(Object.class, "testMethod1", String.class);
        Method method2 = MockitoTestUtils.getMethod(Object.class, "testMethod2", String.class);
        Invocation invocation1 = new InvocationOnMock(mock, method1);
        Invocation invocation2 = new InvocationOnMock(mock, method2);
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1, Collections.emptyList());
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2, Collections.emptyList());
        assertFalse(matcher1.hasSameMethod(matcher2));
    }

    @Test
    public void testHasSimilarMethod_withSameInvocations() {
        Object mock = org.mockito.Mockito.mock(Object.class);
        Method method1 = MockitoTestUtils.getMethod(Object.class, "testMethod1", String.class);
        Method method2 = MockitoTestUtils.getMethod(Object.class, "testMethod2", String.class);
        Invocation invocation = new InvocationOnMock(mock, method1);
        InvocationMatcher matcher1 = new InvocationMatcher(invocation, Collections.emptyList());
        InvocationMatcher matcher2 = new InvocationMatcher(new InvocationMatcher(invocation, Collections.emptyList()), Collections.emptyList());
        assertTrue(matcher1.hasSimilarMethod(matcher2));
        assertFalse(matcher1.hasSimilarMethod(new InvocationMatcher(new InvocationOnMock(mock, method2), Collections.emptyList())));
    }

    @Test
    public void testHasSimilarMethod_withDifferentInvocations() {
        Object mock = org.mockito.Mockito.mock(Object.class);
        Method method1 = MockitoTestUtils.getMethod(Object.class, "testMethod1", String.class);
        Method method2 = MockitoTestUtils.getMethod(Object.class, "testMethod2", String.class);
        Invocation invocation1 = new InvocationOnMock(mock, method1);
        Invocation invocation2 = new InvocationOnMock(mock, method2);
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1, Collections.emptyList());
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2, Collections.emptyList());
        assertFalse(matcher1.hasSimilarMethod(matcher2));
    }

    @Test
    public void testCaptureArgumentsFrom_nullInvocation() {
        InvocationMatcher matcher = new InvocationMatcher(null, Collections.emptyList());
        assertNull(matcher);
    }

    @Test
    public void testCaptureArgumentsFrom_withEmptyMatchers() {
        Object mock = org.mockito.Mockito.mock(Object.class);
        Method method = MockitoTestUtils.getMethod(Object.class, "testMethod", String[].class);
        Invocation invocation = new InvocationOnMock(mock, method).withArguments(new String[]{"foo", "bar"});
        InvocationMatcher matcher = new InvocationMatcher(invocation, Collections.emptyList());
        CapturesArguments captures = CapturesArguments.forClass(String[].class);
        matcher.captureArgumentsFrom(invocation);
        assertEquals(new String[]{"foo", "bar"}, captures.getValue());
    }

    @Test
    public void testCaptureArgumentsFrom_withMatchers() {
        Object mock = org.mockito.Mockito.mock(Object.class);
        Method method = MockitoTestUtils.getMethod(Object.class, "testMethod", String.class);
        Invocation invocation = new InvocationOnMock(mock, method).withArguments(new String[]{"foo"});
        List<Matcher> matchers = Collections.singletonList(isInstanceOf(String.class));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);
        CapturesArguments captures = CapturesArguments.forClass(String.class);
        matcher.captureArgumentsFrom(invocation);
        assertEquals("foo", captures.getValue());
    }
}

class MockitoTestUtils {
    static Method getMethod(Class<?> clazz, String name, Class<?>... parameterTypes) {
        try {
            return clazz.getMethod(name, parameterTypes);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
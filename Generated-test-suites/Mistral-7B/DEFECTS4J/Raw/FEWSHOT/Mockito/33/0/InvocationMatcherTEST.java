package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.reporting.PrintSettings.printSettings;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.exceptions.InvocationNotProceed;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.MockitoMock;

public class InvocationMatcherTest {

    @Test
    public void testCreateInvocationMatcher() {
        MockitoMock mock = new MockitoMock(MockCreationSettings.VERIFY_ALL);
        Method method = Mockito.class.getMethod("methodWithNoArguments");
        Invocation invocation = new Invocation(mock, method, new Object[0], new Location(1, 2));

        InvocationMatcher matcher = new InvocationMatcher(invocation);
        InvocationMatcher matcherEmptyList = new InvocationMatcher(invocation, Collections.<Matcher>emptyList());

        assertTrue(matcher instanceof InvocationMatcher);
        assertTrue(matcherEmptyList instanceof InvocationMatcher);
        assertEquals(invocation, matcher.getInvocation());
        assertEquals(invocation, matcherEmptyList.getInvocation());
        assertNotNull(matcher.getMatchers());
        assertNotNull(matcherEmptyList.getMatchers());
        assertTrue(matcherEmptyList.getMatchers().isEmpty());
    }

    @Test
    public void testMatchesWithNoMatchers() {
        MockitoMock mock = new MockitoMock(MockCreationSettings.VERIFY_ALL);
        Method method = Mockito.class.getMethod("methodWithNoArguments");
        Invocation invocation = new Invocation(mock, method, new Object[0], new Location(1, 2));

        InvocationMatcher matcher = new InvocationMatcher(invocation);
        InvocationMatcher otherInvocationMatcher = new InvocationMatcher(new Invocation(mock, method, new Object[0], new Location(1, 2)));

        assertTrue(matcher.matches(otherInvocationMatcher));
    }

    @Test
    public void testMatchesWithMatchers() {
        MockitoMock mock = new MockitoMock(MockCreationSettings.VERIFY_ALL);
        Method method = Mockito.class.getMethod("methodWithArguments", String.class, int.class);
        Object[] arguments = new Object[]{"string", 1};

        Invocation invocation = new Invocation(mock, method, arguments, new Location(1, 2));

        List<Matcher> matchers = Collections.singletonList(is("string"));

        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        Object[] otherArguments = new Object[]{"anotherString"};
        Invocation otherInvocation = new Invocation(mock, method, otherArguments, new Location(1, 3));

        assertFalse(matcher.matches(otherInvocation));

        otherArguments = new Object[]{"string"};

        assertTrue(matcher.matches(new InvocationMatcher(otherInvocation, matchers)));

        otherInvocation = new Invocation(mock, method, arguments, new Location(1, 4));

        assertTrue(matcher.matches(otherInvocation));
    }

    @Test
    public void testMatchesWithDifferentMethods() {
        MockitoMock mock = new MockitoMock(MockCreationSettings.VERIFY_ALL);

        Method method1 = Mockito.class.getMethod("methodWithNoArguments");
        Method method2 = Mockito.class.getMethod("methodWithArguments", int.class);

        Invocation invocation1 = new Invocation(mock, method1, new Object[0], new Location(1, 2));
        Invocation invocation2 = new Invocation(mock, method2, new Object[]{1}, new Location(1, 3));

        InvocationMatcher matcher = new InvocationMatcher(invocation1);

        assertFalse(matcher.matches(new InvocationMatcher(invocation2)));
    }

    @Test
    public void testMatchesWithSameMethodAndDifferentArguments() {
        MockitoMock mock = new MockitoMock(MockCreationSettings.VERIFY_ALL);

        Method method = Mockito.class.getMethod("methodWithArguments", String.class, int.class);

        Invocation invocation1 = new Invocation(mock, method, new Object[]{"string1", 1}, new Location(1, 2));
        Invocation invocation2 = new Invocation(mock, method, new Object[]{"string2", 2}, new Location(1, 3));

        InvocationMatcher matcher = new InvocationMatcher(invocation1);

        assertFalse(matcher.matches(new InvocationMatcher(invocation2)));
    }

    @Test(expected = InvocationNotProceed.class)
    public void testMatchesWithDifferentMock() {
        MockitoMock mock1 = new MockitoMock(MockCreationSettings.VERIFY_ALL);
        MockitoMock mock2 = new MockitoMock(MockCreationSettings.VERIFY_ALL);

        Method method = Mockito.class.getMethod("methodWithNoArguments");
        Invocation invocation1 = new Invocation(mock1, method, new Object[0], new Location(1, 2));
        Invocation invocation2 = new Invocation(mock2, method, new Object[0], new Location(1, 3));

        InvocationMatcher matcher = new InvocationMatcher(invocation1);

        matcher.matches(new InvocationMatcher(invocation2));
    }

    @Test
    public void testHasSameMethodWithDifferentMethods() {
        MockitoMock mock = new MockitoMock(MockCreationSettings.VERIFY_ALL);

        Method method1 = Mockito.class.getMethod("methodWithNoArguments");
        Method method2 = Mockito.class.getMethod("methodWithArguments", int.class);

        Invocation invocation1 = new Invocation(mock, method1, new Object[0], new Location(1, 2));
        Invocation invocation2 = new Invocation(mock, method2, new Object[]{1}, new Location(1, 3));

        InvocationMatcher matcher = new InvocationMatcher(invocation1);

        assertFalse(matcher.hasSameMethod(new InvocationMatcher(invocation2)));
    }

    @Test
    public void testHasSameMethodWithSameMethod() {
        MockitoMock mock = new MockitoMock(MockCreationSettings.VERIFY_ALL);

        Method method = Mockito.class.getMethod("methodWithNoArguments");

        Invocation invocation1 = new Invocation(mock, method, new Object[0], new Location(1, 2));
        Invocation invocation2 = new Invocation(mock, method, new Object[0], new Location(1, 3));

        InvocationMatcher matcher = new InvocationMatcher(invocation1);

        assertTrue(matcher.hasSameMethod(new InvocationMatcher(invocation2)));
    }

    @Test
    public void testToString() {
        MockitoMock mock = new MockitoMock(MockCreationSettings.VERIFY_ALL);
        Method method = Mockito.class.getMethod("methodWithArguments", String.class, int.class);

        Invocation invocation = new Invocation(mock, method, new Object[]{"string", 1}, new Location(1, 2));
        List<Matcher> matchers = Collections.singletonList(is("string"));

        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        String expected = "Invocation [method = public java.lang.String org.mockito.internal.Mockito.methodWithArguments(java.lang.String, int), mock = [MockitoMock], arguments = [string], location = Location(InvocationMatcherTest.java:1)]";
        String actual = matcher.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testToStringWithPrintSettings() {
        MockitoMock mock = new MockitoMock(MockCreationSettings.VERIFY_ALL);
        Method method = Mockito.class.getMethod("methodWithArguments", String.class, int.class);

        Invocation invocation = new Invocation(mock, method, new Object[]{"string", 1}, new Location(1, 2));
        List<Matcher> matchers = Collections.singletonList(is("string"));

        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        String expected = "Invocation [method = public java.lang.String org.mockito.internal.Mockito.methodWithArguments(java.lang.String, int), mock = [MockitoMock], arguments = [string], location = Location(InvocationMatcherTest.java:1)]";
        String actual = matcher.toString(printSettings());

        assertEquals(expected, actual);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        MockitoMock mock = new MockitoMock(MockCreationSettings.VERIFY_ALL);
        Method method = Mockito.class.getMethod("methodWithArguments", String.class, int.class);

        Invocation invocation = new Invocation(mock, method, new Object[]{"string", 1}, new Location(1, 2));

        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new CapturesArguments<String>() {
            @Override
            public void captureFrom(String value) {
                assertEquals("string", value);
            }
        });
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        matcher.captureArgumentsFrom(new Invocation(mock, method, new Object[]{"anotherString"}, new Location(1, 2)));
    }

    @Test
    public void testCreateFrom() {
        MockitoMock mock = new MockitoMock(MockCreationSettings.VERIFY_ALL);

        List<Invocation> invocations = new LinkedList<>();
        for (int i = 0; i < 5; i++) {
            Method method = Mockito.class.getMethod("methodWithNoArguments");
            invocations.add(new Invocation(mock, method, new Object[0], new Location(1, i + 1)));
        }

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        assertEquals(5, invocationMatchers.size());
    }
}
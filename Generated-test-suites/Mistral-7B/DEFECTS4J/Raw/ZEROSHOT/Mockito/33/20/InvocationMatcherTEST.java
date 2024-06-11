package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.internal.matchers.ArgumentsMatchers.argThat;
import static org.mockito.internal.matchers.ArgumentsMatchers.equalTo;
import static org.mockito.internal.matchers.ArgumentsMatchers.isNull;
import static org.mockito.internal.reporting.PrintSettings.emptyPrintSettings;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.InvocationOnMock;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;

@SuppressWarnings("unchecked")
public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherCreationWithMatchers() {
        // given
        Object mock = mock(Object.class);
        Method method = mock(Method.class);
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(argThat(equalTo("arg1")));
        matchers.add(isNull());

        // when
        InvocationMatcher invocationMatcher = new InvocationMatcher(new Invocation(mock, method), matchers);

        // then
        assertTrue(invocationMatcher.matches(new InvocationMatcher(new InvocationOnMock(mock, method), matchers.subList(0, 2))));
        assertFalse(invocationMatcher.matches(new InvocationMatcher(new InvocationOnMock(mock, method), matchers.subList(1, 2))));
        assertFalse(invocationMatcher.matches(new InvocationMatcher(new InvocationOnMock(mock, method), Collections.singletonList(isNull()))));
        assertFalse(invocationMatcher.matches(new InvocationMatcher(new InvocationOnMock(mock, method), Collections.singletonList(argThat(equalTo("arg2"))))));
    }

    @Test
    public void testInvocationMatcherCreationWithNoMatchers() {
        // given
        Object mock = mock(Object.class);
        Method method = mock(Method.class);

        // when
        InvocationMatcher invocationMatcher = new InvocationMatcher(new Invocation(mock, method));

        // then
        assertTrue(invocationMatcher.matches(new InvocationMatcher(new InvocationOnMock(mock, method))));
    }

    @Test
    public void testGetMethod() {
        // given
        Object mock = mock(Object.class);
        Method method = mock(Method.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(new Invocation(mock, method));

        // when
        Method result = invocationMatcher.getMethod();

        // then
        assertEquals(method, result);
    }

    @Test
    public void testGetInvocation() {
        // given
        Object mock = mock(Object.class);
        Method method = mock(Method.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(new Invocation(mock, method));

        // when
        Invocation result = invocationMatcher.getInvocation();

        // then
        assertEquals(new Invocation(mock, method), result);
    }

    @Test
    public void testGetMatchers() {
        // given
        Object mock = mock(Object.class);
        Method method = mock(Method.class);
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(argThat(equalTo("arg1")));
        matchers.add(isNull());

        InvocationMatcher invocationMatcher = new InvocationMatcher(new Invocation(mock, method), matchers);

        // when
        List<Matcher> result = invocationMatcher.getMatchers();

        // then
        assertEquals(matchers, result);
    }

    @Test
    public void testToString() {
        // given
        Object mock = mock(Object.class);
        Method method = mock(Method.class);
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(argThat(equalTo("arg1")));
        matchers.add(isNull());

        InvocationMatcher invocationMatcher = new InvocationMatcher(new Invocation(mock, method), matchers);

        // when
        String result = invocationMatcher.toString();

        // then
        String expected = String.format("InvocationMatcher[%s, %s]", method, matchers);
        assertEquals(expected, result);
    }

    @Test
    public void testMatches() {
        // given
        Object mock = mock(Object.class);
        Method method = mock(Method.class);
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(argThat(equalTo("arg1")));
        matchers.add(isNull());

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(new Invocation(mock, method), matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(new InvocationOnMock(mock, method), matchers);

        InvocationMatcher invocationMatcher3 = new InvocationMatcher(new InvocationOnMock(mock, method), Collections.singletonList(isNull()));
        InvocationMatcher invocationMatcher4 = new InvocationMatcher(new InvocationOnMock(mock, method), Collections.singletonList(argThat(equalTo("arg2"))));

        // when
        // then
        assertFalse(invocationMatcher1.matches(invocationMatcher3));
        assertFalse(invocationMatcher1.matches(invocationMatcher4));
        assertTrue(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        // given
        Object mock = mock(Object.class);
        Method method = mock(Method.class);
        Object[] arguments = new Object[]{1, "arg2"};
        InvocationMatcher invocationMatcher = new InvocationMatcher(new Invocation(mock, method), arguments);

        // when
        // then
        assertTrue(invocationMatcher.safelyArgumentsMatch(arguments));
    }

    @Test
    public void testHasSimilarMethod() {
        // given
        Object mock = mock(Object.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(new Invocation(mock, method1));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(new Invocation(mock, method2));

        // when
        // then
        assertTrue(invocationMatcher1.hasSimilarMethod(invocationMatcher1));
        assertTrue(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
        assertFalse(invocationMatcher1.hasSimilarMethod(new InvocationMatcher(new InvocationOnMock(mock, method2))));
    }

    @Test
    public void testHasSameMethod() {
        // given
        Object mock = mock(Object.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(new Invocation(mock, method1));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(new Invocation(mock, method2));

        // when
        // then
        assertTrue(invocationMatcher1.hasSameMethod(invocationMatcher1));
        assertFalse(invocationMatcher1.hasSameMethod(invocationMatcher2));
    }

    @Test
    public void testGetLocation() {
        // given
        Object mock = mock(Object.class);
        Method method = mock(Method.class);
        Location location = mock(Location.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(new Invocation(mock, method, location));

        // when
        Location result = invocationMatcher.getLocation();

        // then
        assertEquals(location, result);
    }

    @Test
    public void testToStringWithPrintSettings() {
        // given
        Object mock = mock(Object.class);
        Method method = mock(Method.class);
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(argThat(equalTo("arg1")));
        matchers.add(isNull());

        InvocationMatcher invocationMatcher = new InvocationMatcher(new Invocation(mock, method), matchers);

        // when
        String result = invocationMatcher.toString(emptyPrintSettings());

        // then
        String expected = String.format("InvocationMatcher[%s, %s]", method, matchers);
        assertEquals(expected, result);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        // given
        Object mock = mock(Object.class);
        Method method = mock(Method.class);
        Object[] arguments = new Object[]{1, "arg2"};
        InvocationMatcher invocationMatcher = new InvocationMatcher(new Invocation(mock, method), arguments);

        // when
        CapturesArguments captureArgumentsMock = mock(CapturesArguments.class);
        invocationMatcher.captureArgumentsFrom(new Invocation(mock, method, arguments));
        captureArgumentsMock.captureFrom(arguments[0]);

        // then
        assertEquals("1", captureArgumentsMock.getValue());
    }

    @Test
    public void testCreateFrom() {
        // given
        Object mock = mock(Object.class);
        List<Invocation> invocations = new LinkedList<>();
        invocations.add(new Invocation(mock, mock(Method.class)));
        invocations.add(new Invocation(mock, mock(Method.class)));

        // when
        List<InvocationMatcher> result = InvocationMatcher.createFrom(invocations);

        // then
        assertEquals(2, result.size());
        for (InvocationMatcher invocationMatcher : result) {
            assertTrue(invocationMatcher instanceof InvocationMatcher);
            assertNotNull(invocationMatcher.getMethod());
            assertNotNull(invocationMatcher.getInvocation());
            assertNotNull(invocationMatcher.getMatchers());
        }
    }
}
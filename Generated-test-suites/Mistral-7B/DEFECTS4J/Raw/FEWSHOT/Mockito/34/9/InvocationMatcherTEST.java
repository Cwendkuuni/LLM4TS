package org.mockito.internal.invocation;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.utils.Reflections;
import org.mockito.mock.MockCreationSettings;
import org.mockito.plugins.MockMaker;
import org.mockito.stubbing.Answer;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcher() {
        MockMaker mockMaker = org.mockito.Mockito.mockmaker();
        Object mock = mockMaker.mock(String.class);

        Method method = Reflections.getMethod(String.class, "length");

        Invocation invocation = Invocation.create(mock, method, new Object[0], new Location(1, 1));
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertEquals(invocation.getMethod(), matcher.getMethod());
        assertEquals(invocation, matcher.getInvocation());
        assertTrue(Collections.isEmpty(matcher.getMatchers()));

        InvocationMatcher matcherWithMatcher = new InvocationMatcher(invocation, Arrays.asList(eq("foo")));
        List<Matcher> matchers = matcherWithMatcher.getMatchers();
        assertEquals(1, matchers.size());
        assertEquals(matchers.get(0), eq("foo"));

        Method method2 = Reflections.getMethod(String.class, "substring", int.class, int.class);
        Invocation invocation2 = Invocation.create(mock, method2, new Object[]{1, 1}, new Location(1, 1));
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);
        InvocationMatcher matcherWithMatchers = new InvocationMatcher(invocation2, Arrays.asList(eq(1), eq(1), isA(Number.class), isA(Number.class)));
        assertEquals(2, matcherWithMatchers.getMatchers().size());
        assertEquals(matcherWithMatchers.getMatchers().get(0), eq(1));
        assertEquals(matcherWithMatchers.getMatchers().get(1), isA(Number.class));
    }

    @Test
    public void testMatches() {
        MockMaker mockMaker = org.mockito.Mockito.mockmaker();
        Object mock = mockMaker.mock(String.class);
        Object mock2 = mockMaker.mock(String.class);

        Method method = Reflections.getMethod(String.class, "length");

        Invocation invocation = Invocation.create(mock, method, new Object[0], new Location(1, 1));
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        Invocation invocation2 = Invocation.create(mock, method, new Object[0], new Location(1, 1));
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        Invocation invocation3 = Invocation.create(mock2, method, new Object[0], new Location(1, 1));
        InvocationMatcher matcher3 = new InvocationMatcher(invocation3);

        assertTrue(matcher.matches(invocation));
        assertTrue(matcher.matches(invocation2));
        assertFalse(matcher.matches(matcher3));

        InvocationMatcher matcherWithMatcher = new InvocationMatcher(invocation, Arrays.asList(eq("foo")));
        assertFalse(matcherWithMatcher.matches(invocation));
        assertTrue(matcherWithMatcher.matches(matcher3));
        assertTrue(matcherWithMatcher.matches(Invocation.create(mock, method, new Object[]{"foo"}, new Location(1, 1))));

        InvocationMatcher matcherWithMultipleMatchers = new InvocationMatcher(invocation2, Arrays.asList(eq("foo"), eq("bar")));
        assertFalse(matcherWithMultipleMatchers.matches(invocation));
        assertFalse(matcherWithMultipleMatchers.matches(invocation2));
        assertTrue(matcherWithMultipleMatchers.matches(Invocation.create(mock, method, new Object[]{"foo", "bar"}, new Location(1, 1))));
    }

    @Test
    public void testHasSameMethod() {
        MockMaker mockMaker = org.mockito.Mockito.mockmaker();
        Object mock = mockMaker.mock(String.class);

        Method method = Reflections.getMethod(String.class, "length");

        Invocation invocation1 = Invocation.create(mock, method, new Object[0], new Location(1, 1));
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);

        Invocation invocation2 = Invocation.create(mock, method, new Object[0], new Location(1, 1));
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        Invocation invocation3 = Invocation.create(mock, Reflections.getMethod(String.class, "substring", int.class, int.class), new Object[]{1, 1}, new Location(1, 1));
        InvocationMatcher matcher3 = new InvocationMatcher(invocation3);

        assertTrue(matcher1.hasSameMethod(matcher1));
        assertTrue(matcher1.hasSameMethod(matcher2));
        assertFalse(matcher1.hasSameMethod(matcher3));
    }

    @Test
    public void testToString() {
        MockMaker mockMaker = org.mockito.Mockito.mockmaker();
        Object mock = mockMaker.mock(String.class);

        Method method = Reflections.getMethod(String.class, "length");

        Invocation invocation = Invocation.create(mock, method, new Object[0], new Location(1, 1));
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        PrintSettings printSettings = new PrintSettings();
        printSettings.setPrintObjectAsString(true);

        String expected = String.format("Invocation %s on %s%n", invocation.toString(Collections.emptyList(), printSettings), mock.getClass().getName());
        assertEquals(expected, matcher.toString(printSettings));

        List<Matcher> matchers = Arrays.asList(eq("foo"));
        InvocationMatcher matcherWithMatcher = new InvocationMatcher(invocation, matchers);

        String expectedWithMatcher = String.format("Invocation %s on %s%nArguments: %s%n", invocation.toString(Collections.emptyList(), printSettings), mock.getClass().getName(), matchers.toString());
        assertEquals(expectedWithMatcher, matcherWithMatcher.toString(printSettings));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        MockMaker mockMaker = org.mockito.Mockito.mockmaker();
        Object mock = mockMaker.mock(String.class);

        Method method = Reflections.getMethod(String.class, "length");

        Invocation invocation = Invocation.create(mock, method, new Object[0], new Location(1, 1));
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        Object[] arguments = new Object[]{1, 2, 3};
        Invocation invocationWithArguments = Invocation.create(mock, method, arguments, new Location(1, 1));

        matcher.captureArgumentsFrom(invocationWithArguments);

        List<Matcher> matchers = Collections.singletonList(argThat(new CapturesArguments() {
            @Override
            public void captureFrom(Object value) {
                assertEquals(arguments[0], value);
            }
        }));
        InvocationMatcher matcherWithMatcher = new InvocationMatcher(invocation, matchers);

        matcherWithMatcher.matches(invocationWithArguments);
    }

    @Test(expected = MockitoException.class)
    public void testCaptureArgumentsFromThrowsExceptionWhenNotInvocation() {
        Object notInvocation = new Object(){};

        List<Matcher> matchers = Collections.singletonList(argThat(new CapturesArguments(){}));
        InvocationMatcher matcher = new InvocationMatcher(null, matchers);

        matcher.captureArgumentsFrom(notInvocation);
    }

    @Test(expected = MockitoException.class)
    public void testCaptureArgumentsFromThrowsExceptionWhenNotListOfMatchers() {
        List<Object> notMatchers = Arrays.asList(1, 2, 3);
        InvocationMatcher matcher = new InvocationMatcher(null);

        matcher.captureArgumentsFrom(null);
    }
}
package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.ArgumentsMatcher.argumentsWithSameValues;
import static org.mockito.internal.matchers.ArgumentsMatcher.argumentsWithType;
import static org.mockito.internal.matchers.ArgumentsMatcher.isArgument;
import static org.mockito.internal.matchers.ArgumentsMatcher.isArray;
import static org.mockito.internal.matchers.ArgumentsMatcher.isCollection;
import static org.mockito.internal.matchers.ArgumentsMatcher.isInstanceOf;
import static org.mockito.internal.matchers.ArgumentsMatcher.isMap;
import static org.mockito.internal.matchers.ArgumentsMatcher.isNull;
import static org.mockito.internal.matchers.ArgumentsMatcher.isPrimitive;
import static org.mockito.internal.matchers.ArgumentsMatcher.isString;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationMatcher;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.internal.matchers.ArgumentsComparator;
import org.mockito.internal.matchers.CapturesArguments;

public class InvocationMatcherTest {
    private InvocationMatcher matcher;
    private Invocation invocation;
    private Object[] arguments;
    private Object mock;
    private Method method;
    private Location location;
    private PrintSettings printSettings;

    @Before
    public void setUp() {
        mock = Mockito.mock(String.class);
        method = Mockito.method(String.class, "split", String[].class);
        location = new Location(42, 42);
        printSettings = new PrintSettings();
        invocation = Mockito.mock(Invocation.class);
        arguments = new Object[]{"test", ","};

        matcher = new InvocationMatcher(invocation, Collections.singletonList(isString("test")));
    }

    @Test
    public void testGetMethod() {
        assertSame(method, matcher.getMethod());
    }

    @Test
    public void testGetInvocation() {
        assertSame(invocation, matcher.getInvocation());
    }

    @Test
    public void testGetMatchers() {
        List<Matcher> matchers = matcher.getMatchers();
        assertEquals(1, matchers.size());
        assertTrue(matchers.get(0) instanceof CapturesArguments);
        assertTrue(matchers.get(0) instanceof org.mockito.ArgumentMatcher);
        assertTrue(matchers.get(0) instanceof Matcher);
    }

    @Test
    public void testToString() {
        assertEquals("InvocationMatcher[method=String.split(java.lang.String[])]{0} > String \"test\"", matcher.toString());
        assertEquals("InvocationMatcher[method=String.split(java.lang.String[])]{0} > *", matcher.toString(printSettings));
    }

    @Test
    public void testMatches() {
        assertTrue(matcher.matches(Mockito.argumentCaptor(String[].class).capture(StringArrayCaptor).getValue()));
        assertFalse(matcher.matches(Mockito.argumentCaptor(Integer[].class).capture(IntegerArrayCaptor).getValue()));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Object[] actualArgs = new Object[]{"test1", "test2"};
        assertTrue(matcher.safelyArgumentsMatch(actualArgs));
    }

    @Test
    public void testHasSimilarMethod() {
        InvocationMatcher candidate = new InvocationMatcher(Mockito.mock(Invocation.class), Collections.singletonList(isString("test")));

        assertTrue(matcher.hasSimilarMethod(candidate));

        InvocationMatcher differentMethodMatcher = new InvocationMatcher(Mockito.mock(Invocation.class),
                Collections.singletonList(isString("test2")));

        assertFalse(matcher.hasSimilarMethod(differentMethodMatcher));
    }

    @Test
    public void testHasSameMethod() {
        InvocationMatcher sameMethodMatcher = new InvocationMatcher(invocation, Collections.singletonList(isString("test")));

        assertTrue(matcher.hasSameMethod(sameMethodMatcher));
    }

    @Test
    public void testGetLocation() {
        assertEquals(location, matcher.getLocation());
    }

    @Test
    public void testToStringPrintSettings() {
        assertEquals("InvocationMatcher[method=String.split(java.lang.String[])]{0} > String \"test\"", matcher.toString(printSettings));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        matcher.captureArgumentsFrom(invocation);

        String argument = StringArrayCaptor.getValue()[0];
        assertEquals("test", argument);
    }

    @Test
    public void testMatchesNullInvocation() {
        assertFalse(matcher.matches(null));
    }

    @Test
    public void testMatchesDifferentMock() {
        InvocationMatcher differentMockMatcher = new InvocationMatcher(Mockito.mock(Invocation.class),
                Collections.singletonList(isString("test")));

        assertFalse(matcher.matches(differentMockMatcher));
    }

    @Test
    public void testMatchesDifferentMethod() {
        InvocationMatcher differentMethodMatcher = new InvocationMatcher(Mockito.mock(Invocation.class),
                Collections.singletonList(isInstanceOf(Integer[].class)));

        assertFalse(matcher.matches(differentMethodMatcher));
    }

    @Test
    public void testMatchesDifferentArguments() {
        Object[] differentArguments = new Object[]{"test1", 1};

        assertFalse(matcher.matches(new InvocationMatcher(invocation, Collections.singletonList(isArgument(0, isString("test1"))))));
        assertFalse(matcher.matches(new InvocationMatcher(invocation, Collections.singletonList(isArgument(0, isArgument(0, isNull()))))));
        assertFalse(matcher.matches(new InvocationMatcher(invocation, Collections.singletonList(isArgument(0, isPrimitive(int.class))))));
        assertFalse(matcher.matches(new InvocationMatcher(invocation, Collections.singletonList(isArgument(0, isArray(int[].class))))));
        assertFalse(matcher.matches(new InvocationMatcher(invocation, Collections.singletonList(isArgument(0, isCollection(List.class))))));
        assertFalse(matcher.matches(new InvocationMatcher(invocation, Collections.singletonList(isArgument(0, isMap(Map.class))))));
        assertFalse(matcher.matches(new InvocationMatcher(invocation, Collections.singletonList(argumentsWithSameValues(new Object[]{"test1", "test2"})))));
        assertFalse(matcher.matches(new InvocationMatcher(invocation, Collections.singletonList(argumentsWithType(String[].class)))));

        assertTrue(matcher.matches(new InvocationMatcher(invocation, Collections.singletonList(isArgument(0, isString("test"))))));
        assertTrue(matcher.matches(new InvocationMatcher(invocation, Collections.singletonList(isArgument(0, CoreMatchers.instanceOf(Mockito.class))))));
        assertTrue(matcher.matches(new InvocationMatcher(invocation, Collections.singletonList(isArgument(0, Matchers.not(isArgument(0, isNull())))))));
        assertTrue(matcher.matches(new InvocationMatcher(invocation, Collections.singletonList(isArgument(0, Matchers.any(String.class))))));
        assertTrue(matcher.matches(new InvocationMatcher(invocation, Collections.singletonList(isArgument(0, Matchers.anyOf(isString("test1"), isString("test2")))))));
        assertTrue(matcher.matches(new InvocationMatcher(invocation, Collections.singletonList(isArgument(0, Matchers.anyOf(isInstanceOf(String[].class), isInstanceOf(List.class)))))));
        assertTrue(matcher.matches(new InvocationMatcher(invocation, Collections.singletonList(isArgument(0, Matchers.not(isArgument(0, isInstanceOf(List.class)))))
                .and(isArgument(0, Matchers.not(isArgument(0, isInstanceOf(String[].class))))))));
    }

    private static class StringArrayCaptor extends ArgumentCaptor<Object[]> {
        private Object[] value;

        @Override
        public Object[] getValue() {
            return value;
        }

        @Override
        public void reset() {
            value = null;
        }
    }

    private static class IntegerArrayCaptor extends ArgumentCaptor<Object[]> {
        @Override
        public Object[] getValue() {
            return (Object[]) value[0];
        }

        @Override
        public void reset() {
            value = null;
        }

        @Before
        public void setUp() {
            value = new Object[]{new Integer(1), new Integer(2)};
        }
    }
}
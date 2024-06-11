package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.ArgumentMatchers.any;
import static org.mockito.internal.matchers.ArgumentMatchers.eq;
import static org.mockito.internal.matchers.ArgumentMatchers.isA;
import static org.mockito.internal.matchers.ArgumentMatchers.isInstanceOf;
import static org.mockito.internal.matchers.ArgumentMatchers.notNull;
import static org.mockito.internal.reporting.PrintSettings.print;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.matchers.VarargMatcher;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import org.mockito.stub.Answer;
import org.mockito.stub.Stubber;

/**
 * Test class for {@link InvocationMatcher}
 */
public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherConstruction() {
        Invocation invocation = createMockInvocation();

        InvocationMatcher matcher = new InvocationMatcher(invocation);
        InvocationMatcher matcherEmptyList = new InvocationMatcher(invocation, Collections.emptyList());

        assertEquals(invocation, matcher.getInvocation());
        assertEquals(Collections.emptyList(), matcherEmptyList.getMatchers());
        assertEquals(matcher, matcherEmptyList);
    }

    @Test
    public void testToString() {
        Invocation invocation = createMockInvocation();
        List<Matcher> matchers = Arrays.asList(eq("foo"), any(String.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        String expected = print(matchers, invocation);
        String actual = invocationMatcher.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testMatchesInvocation() {
        Invocation invocation = createMockInvocation();
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        Invocation actual = createMockInvocation();
        assertTrue(matcher.matches(actual));

        matcher = new InvocationMatcher(invocation, Arrays.asList(eq("foo")));

        actual = createMockInvocationWithArgument("foo");
        assertTrue(matcher.matches(actual));

        actual = createMockInvocationWithArgument("bar");
        assertFalse(matcher.matches(actual));
    }

    @Test
    public void testMatchesArguments() {
        Invocation invocation = createMockInvocation();
        List<Matcher> matchers = Arrays.asList(eq("foo"));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        Object[] arguments = new Object[]{"foo"};
        assertTrue(matcher.safelyArgumentsMatch(arguments));

        arguments = new Object[]{"bar"};
        assertFalse(matcher.safelyArgumentsMatch(arguments));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = createMockInvocation();
        Invocation candidate = createMockInvocation();

        assertTrue(new InvocationMatcher(invocation).hasSimilarMethod(candidate));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(anyInt()));
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));

        candidate = createMockInvocationWithDifferentName(invocation.getMethod().getName());
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));

        candidate = createMockInvocationWithSameNameButDifferentArguments(invocation);
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));

        candidate = createMockInvocationWithSameNameAndArgumentsButDifferentMock(invocation);
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testCaptureArguments() {
        Invocation invocation = createMockInvocation();
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        Stubber stubber = mockitoStubber();
        Invocation invocationToCaptureArguments = createMockInvocationWithArgument("test");
        stubber.doAnswer(createAnswer(argumentCaptor)).when(createMock(String.class)).test(eq("test"));

        matcher.captureArgumentsFrom(invocationToCaptureArguments);
        assertEquals("test", argumentCaptor.getValue());
    }

    @Test
    public void testCaptureArgumentsFromVarargs() {
        Invocation invocation = createMockInvocation();
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        ArgumentCaptor<String[]> argumentCaptor = ArgumentCaptor.forClass(String[].class);

        Stubber stubber = mockitoStubber();
        Invocation invocationWithVarargs = createMockInvocationWithVarargs(new String[]{"test1", "test2"});
        stubber.doAnswer(createAnswer(argumentCaptor)).when(createMock(String[].class)).test(anyArray(String.class));

        matcher.captureArgumentsFrom(invocationWithVarargs);
        assertArrayEquals(new String[]{"test1", "test2"}, argumentCaptor.getValue());
    }

    @Test
    public void testCreateFrom() {
        Invocation invocation1 = createMockInvocation();
        Invocation invocation2 = createMockInvocation();

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(Arrays.asList(invocation1, invocation2));

        assertEquals(2, invocationMatchers.size());
        assertTrue(invocationMatchers.contains(new InvocationMatcher(invocation1)));
        assertTrue(invocationMatchers.contains(new InvocationMatcher(invocation2)));
    }

    private Invocation createMockInvocation() {
        return createMock(Invocation.class);
    }

    private Invocation createMockInvocationWithArgument(Object argument) {
        Invocation invocation = createMockInvocation();
        when(invocation.getArgumentAt(anyInt(), any(Class.class))).thenReturn(argument);
        return invocation;
    }

    private Invocation createMockInvocationWithDifferentName(String methodName) {
        Invocation invocation = createMockInvocation();
        when(invocation.getMethod().getName()).thenReturn(methodName);
        return invocation;
    }

    private Invocation createMockInvocationWithSameNameButDifferentArguments(Invocation invocation) {
        Invocation candidate = createMockInvocation();
        when(candidate.getMethod()).thenReturn(invocation.getMethod());
        Object[] arguments = invocation.getArguments().clone();
        arguments[0] = "differentArgument";
        when(candidate.getArguments()).thenReturn(arguments);
        return candidate;
    }

    private Invocation createMockInvocationWithSameNameAndArgumentsButDifferentMock(Invocation invocation) {
        Invocation candidate = createMockInvocation();
        when(candidate.getMethod()).thenReturn(invocation.getMethod());
        when(candidate.getMock()).thenReturn(new Object());
        return candidate;
    }

    private Answer<Object> createAnswer(ArgumentCaptor<String> argumentCaptor) {
        return invocation -> argumentCaptor.getValue();
    }

    private Stubber mockitoStubber() {
        return mockito(Stubber.class);
    }

    private Object createMock(Class<?> clazz) {
        return mockito(clazz);
    }

    private Object mockito(Class<?> clazz) {
        return mockito().mock(clazz);
    }

    private static void assertArrayEquals(Object[] expected, Object[] actual) {
        Arrays.sort(expected);
        Arrays.sort(actual);
        Arrays.equals(expected, actual);
    }
}
package org.mockito.internal.invocation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.invocation.Invocation;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    @Test
    public void testConstructorWithInvocationAndEmptyList() {
        Invocation invocation = createMockInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertInvocationMatches(invocationMatcher, invocation);
    }

    @Test
    public void testConstructorWithInvocationAndListOfMatchers() {
        Invocation invocation = createMockInvocation();
        List<Matcher> matchers = Arrays.asList(createMatcher("1"), createMatcher("2"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertInvocationMatches(invocationMatcher, invocation);
        assertMatchers(invocationMatcher, matchers.toArray(new Matcher[0]));
    }

    @Test
    public void testGetMethod() {
        Invocation invocation = createMockInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Method method = invocationMatcher.getMethod();
        Method expectedMethod = invocation.getMethod();

        assertSame(expectedMethod, method);
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = createMockInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation actualInvocation = invocationMatcher.getInvocation();

        assertSame(invocation, actualInvocation);
    }

    @Test
    public void testGetMatchers() {
        Invocation invocation = createMockInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        List<Matcher> matchers = invocationMatcher.getMatchers();
        List<Matcher> expectedMatchers = ArgumentsProcessor.argumentsToMatchers(invocation.getArguments());

        assertEquals(expectedMatchers, matchers);
    }

    @Test
    public void testToString() {
        Invocation invocation = createMockInvocation();
        List<Matcher> matchers = Arrays.asList(createMatcher("1"), createMatcher("2"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        String result = invocationMatcher.toString();
        String expectedResult = new PrintSettings().print(matchers.toArray(new Matcher[0]), invocation);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testMatchesWithSameInvocation() {
        Invocation invocation = createMockInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertTrue(invocationMatcher.matches(invocation));
    }

    @Test
    public void testMatchesWithDifferentInvocation() {
        Invocation invocation1 = createMockInvocation();
        Invocation invocation2 = createMockInvocationWithDifferentMethod();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        assertFalse(invocationMatcher.matches(invocation2));
    }

    @Test
    public void testMatchesWithDifferentMock() {
        Invocation invocation1 = createMockInvocation();
        Invocation invocation2 = createMockInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        assertFalse(invocationMatcher.matches(invocation2));
    }

    @Test
    public void testMatchesWithDifferentMethod() {
        Invocation invocation1 = createMockInvocation();
        Invocation invocation2 = createMockInvocationWithDifferentMethod();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        assertFalse(invocationMatcher.matches(invocation2));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCaptureArgumentsWithVarargs() {
        Invocation invocation = createMockInvocationWithVarargs();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        invocationMatcher.captureArgumentsFrom(invocation);
    }

    @Test
    public void testHasSimilarMethodWithSameMethod() {
        Invocation invocation1 = createMockInvocation();
        Invocation invocation2 = createMockInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        assertTrue(invocationMatcher.hasSimilarMethod(invocation2));
    }

    @Test
    public void testHasSimilarMethodWithDifferentMethod() {
        Invocation invocation1 = createMockInvocation();
        Invocation invocation2 = createMockInvocationWithDifferentMethod();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        assertFalse(invocationMatcher.hasSimilarMethod(invocation2));
    }

    @Test
    public void testHasSameMethodWithSameMethod() {
        Invocation invocation1 = createMockInvocation();
        Invocation invocation2 = createMockInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        assertTrue(invocationMatcher.hasSameMethod(invocation2));
    }

    @Test
    public void testHasSameMethodWithDifferentMethod() {
        Invocation invocation1 = createMockInvocation();
        Invocation invocation2 = createMockInvocationWithDifferentMethod();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        assertFalse(invocationMatcher.hasSameMethod(invocation2));
    }

    @Test
    public void testArgumentsMatchWithSameArguments() {
        Object[] arguments1 = new Object[]{1, 2};
        Object[] arguments2 = new Object[]{1, 2};
        InvocationMatcher invocationMatcher = new InvocationMatcher(createMockInvocation(), Arrays.asList(createMatcher("1"), createMatcher("2")));

        assertTrue(invocationMatcher.safelyArgumentsMatch(arguments1));
        assertTrue(invocationMatcher.safelyArgumentsMatch(arguments2));
    }

    @Test
    public void testArgumentsMatchWithDifferentArguments() {
        Object[] arguments1 = new Object[]{1, 2};
        Object[] arguments2 = new Object[]{3, 4};
        InvocationMatcher invocationMatcher = new InvocationMatcher(createMockInvocation(), Arrays.asList(createMatcher("1"), createMatcher("2")));

        assertFalse(invocationMatcher.safelyArgumentsMatch(arguments1));
        assertFalse(invocationMatcher.safelyArgumentsMatch(arguments2));
    }

    @Test
    public void testHasSimilarMethodWithSameMethodAndDifferentArguments() {
        Invocation invocation1 = createMockInvocation();
        Object[] arguments1 = invocation1.getArguments();
        Invocation invocation2 = createMockInvocation();
        Object[] arguments2 = invocation2.getArguments();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        assertFalse(invocationMatcher.hasSimilarMethod(invocation2));
        assertFalse(invocationMatcher.safelyArgumentsMatch(arguments2));
    }

    @Test
    public void testHasSimilarMethodWithSameMethodAndOverloadedArguments() {
        Invocation invocation1 = createMockInvocation();
        Object[] arguments1 = invocation1.getArguments();
        Invocation invocation2 = createMockInvocationWithOverloadedMethod();
        Object[] arguments2 = invocation2.getArguments();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        assertFalse(invocationMatcher.hasSimilarMethod(invocation2));
        assertTrue(invocationMatcher.safelyArgumentsMatch(arguments2));
    }

    @Test
    public void testHasSameMethodWithSameMethodAndDifferentArguments() {
        Invocation invocation1 = createMockInvocation();
        Object[] arguments1 = invocation1.getArguments();
        Invocation invocation2 = createMockInvocation();
        Object[] arguments2 = invocation2.getArguments();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        assertFalse(invocationMatcher.hasSameMethod(invocation2));
        assertFalse(invocationMatcher.safelyArgumentsMatch(arguments2));
    }

    @Test
    public void testHasSameMethodWithSameMethodAndSameArguments() {
        Invocation invocation1 = createMockInvocation();
        Object[] arguments1 = invocation1.getArguments();
        Invocation invocation2 = createMockInvocation();
        Object[] arguments2 = invocation2.getArguments();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        assertTrue(invocationMatcher.hasSameMethod(invocation2));
        assertTrue(invocationMatcher.safelyArgumentsMatch(arguments1));
        assertTrue(invocationMatcher.safelyArgumentsMatch(arguments2));
    }

    @Test
    public void testHasSameMethodWithSameMethodAndDifferentArgumentsOrder() {
        Invocation invocation1 = createMockInvocation();
        Object[] arguments1 = invocation1.getArguments();
        Invocation invocation2 = createMockInvocation();
        Object[] arguments2 = invocation2.getArguments();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        assertTrue(invocationMatcher.hasSameMethod(invocation2));
        assertFalse(invocationMatcher.safelyArgumentsMatch(arguments1));
        assertFalse(invocationMatcher.safelyArgumentsMatch(arguments2));

        Object temp = arguments1[0];
        arguments1[0] = arguments1[1];
        arguments1[1] = temp;

        assertTrue(invocationMatcher.safelyArgumentsMatch(arguments1));
        assertTrue(invocationMatcher.safelyArgumentsMatch(arguments2));
    }

    @Test
    public void testHasSameMethodWithDifferentMethodsButSameName() {
        Invocation invocation1 = createMockInvocation();
        Invocation invocation2 = createMockInvocationWithDifferentParameterTypes();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        assertFalse(invocationMatcher.hasSameMethod(invocation2));
        assertFalse(invocationMatcher.safelyArgumentsMatch(invocation2.getArguments()));
    }

    @Test
    public void testCaptureArguments() {
        Invocation invocation = createMockInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Matcher[] matchers = invocationMatcher.getMatchers().toArray(new Matcher[0]);
        for (Matcher matcher : matchers) {
            if (matcher instanceof CapturesArguments) {
                Invocation invocationWithCapturedArguments = createMockInvocationWithCapturedArguments(matcher);
                invocationMatcher.captureArgumentsFrom(invocationWithCapturedArguments);

                assertSame(invocationWithCapturedArguments.getArgumentAt(matcher.getIndex(), Object.class), matcher.getActualArgument());
            }
        }
    }

    @Test
    public void testCaptureArgumentsWithVarargMatcher() {
        Invocation invocation = createMockInvocationWithVarargs();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Matcher[] matchers = invocationMatcher.getMatchers().toArray(new Matcher[0]);
        for (Matcher matcher : matchers) {
            if (matcher instanceof VarargMatcher) {
                Object[] expectedArguments = new Object[]{1, 2, 3};
                Invocation invocationWithCapturedArguments = createMockInvocationWithCapturedVarargs(matcher, expectedArguments);
                invocationMatcher.captureArgumentsFrom(invocationWithCapturedArguments);

                Object[] actualArguments = invocationWithCapturedArguments.getArguments();
                assertArrayEquals(expectedArguments, actualArguments);
            }
        }
    }

    private Invocation createMockInvocation() {
        Object mock = mock(Object.class);
        Method method = mockMethod(String.class, String.class);
        when(mock.toString()).thenReturn("mock");
        when(mock.method(any(String.class), any(String.class))).thenReturn("result");
        return new Invocation(mock, "toString", new Object[]{"mock"}, new Object[0], Location.method(Mockito.class, "testMethod"), 1);
    }

    private Invocation createMockInvocationWithDifferentMethod() {
        Object mock = mock(Object.class);
        Method method = mockMethod(Integer.class, String.class);
        when(mock.toString()).thenReturn("mock");
        when(mock.method(any(Integer.class), any(String.class))).thenReturn("result");
        return new Invocation(mock, "method", new Object[]{"3"}, new Object[0], Location.method(Mockito.class, "testMethod"), 1);
    }

    private Invocation createMockInvocationWithVarargs() {
        Object mock = mock(Object.class);
        Method method = mockMethod(String.class, String.class, String[].class);
        when(mock.toString()).thenReturn("mock");
        when(mock.method(any(String.class), any(String[].class))).thenReturn("result");
        return new Invocation(mock, "method", new Object[]{"mock"}, new Object[]{new String[]{"arg1", "arg2"}}, Location.method(Mockito.class, "testMethod"), 1);
    }

    private Invocation createMockInvocationWithCapturedArguments(Matcher matcher) {
        Object mock = mock(Object.class);
        Method method = mockMethod(String.class, String.class);
        when(mock.toString()).thenReturn("mock");
        when(mock.method(any(String.class), any(String.class))).thenReturn("result");
        doAnswer(i -> ((CapturesArguments) matcher).captureFrom(i.getArguments()[matcher.getIndex()])).when(mock).method(eq("toString"), any(String.class));

        return new Invocation(mock, "toString", new Object[]{"mock"}, new Object[0], Location.method(Mockito.class, "testMethod"), 1);
    }

    private Invocation createMockInvocationWithCapturedVarargs(Matcher matcher, Object[] expectedArguments) {
        Object mock = mock(Object.class);
        Method method = mockMethod(String.class, String.class, String[].class);
        when(mock.toString()).thenReturn("mock");
        when(mock.method(any(String.class), any(String[].class))).thenReturn("result");
        doAnswer(i -> ((VarargMatcher) matcher).captureFrom((Object[]) i.getArguments()[matcher.getIndex()])).when(mock).method(eq("toString"), any(String[].class));

        return new Invocation(mock, "toString", new Object[]{"mock"}, new Object[]{expectedArguments}, Location.method(Mockito.class, "testMethod"), 1);
    }

    private Method mockMethod(Class<?>... parameterTypes) {
        try {
            return Mockito.mock(Method.class, new Class[]{Class.class, Class[].class});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Matcher createMatcher(String name) {
        return mock(Matcher.class);
    }

    private void assertInvocationMatches(InvocationMatcher invocationMatcher, Invocation invocation) {
        assertTrue(invocationMatcher.matches(invocation));
    }

    private void assertMatchers(InvocationMatcher invocationMatcher, Matcher[] expectedMatchers) {
        Matcher[] actualMatchers = invocationMatcher.getMatchers().toArray(new Matcher[0]);
        assertArrayEquals(expectedMatchers, actualMatchers);
    }

    private void assertEquals(Object expected, Object actual) {
        assertEquals(expected, actual);
    }

    private void assertSame(Object expected, Object actual) {
        assertEquals(expected, actual);
    }

    private void assertArrayEquals(Object[] expected, Object[] actual) {
        assertArrayEquals(expected, actual, false);
    }
}
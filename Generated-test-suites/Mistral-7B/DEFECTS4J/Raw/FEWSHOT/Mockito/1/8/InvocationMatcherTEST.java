package org.mockito.internal.invocation;

import static org.junit.Assert.*;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.internal.matchers.VarargMatcher;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

public class InvocationMatcherTest {

    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = createMockInvocation("m1", new Object[0]);
        Invocation invocation2 = createMockInvocation("m2", new Object[0]);
        Invocation invocation3 = createMockInvocation("m1", new Object[0]);
        Invocation invocation4 = createMockInvocation("m2", new Object[]{1, 2});

        assertTrue(new InvocationMatcher(invocation1).hasSameMethod(invocation1));
        assertTrue(new InvocationMatcher(invocation1).hasSameMethod(invocation3));
        assertFalse(new InvocationMatcher(invocation1).hasSameMethod(invocation2));
        assertFalse(new InvocationMatcher(invocation1).hasSameMethod(invocation4));
    }

    @Test
    public void testMatches() {
        Invocation invocation = createMockInvocation("m", new Object[]{1, "str"});
        List<Matcher> matchers = Collections.singletonList(Matchers.eq(1));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Invocation actualInvocation1 = createMockInvocation("m", new Object[]{1, "str"});
        assertTrue(invocationMatcher.matches(actualInvocation1));

        Invocation actualInvocation2 = createMockInvocation("m", new Object[]{2, "str"});
        assertFalse(invocationMatcher.matches(actualInvocation2));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = createMockInvocation("m", new Object[]{1, "str"});
        List<Matcher> matchers = Arrays.asList(Matchers.eq(1), new VarargMatcher(Integer[].class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Object[] capturedArguments = new Object[2];
        invocationMatcher.captureArgumentsFrom(invocation);

        assertEquals(1, capturedArguments[0]);
        assertArrayEquals(new Integer[]{2}, capturedArguments[1]);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCaptureArgumentsFromVarargs() {
        Invocation invocation = createMockInvocation("m", new Object[]{1, new Integer[0]});
        List<Matcher> matchers = Collections.singletonList(Matchers.anyArray(Integer[].class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        invocationMatcher.captureArgumentsFrom(invocation);
    }

    private Invocation createMockInvocation(String methodName, Object[] arguments) {
        Class<?> clazz = Mockito.class;
        try {
            Method method = clazz.getDeclaredMethod(methodName, Arrays.asList(arguments).toArray(new Class[0]));
            return Mockito.mock(clazz, Mockito.withSettings().defaultAnswer(invocation -> new Object[]{invocation}));
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
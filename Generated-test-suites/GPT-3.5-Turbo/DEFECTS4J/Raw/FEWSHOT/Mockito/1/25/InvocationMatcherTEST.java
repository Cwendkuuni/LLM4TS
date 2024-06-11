package org.mockito.internal.invocation;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.isA;
import static org.mockito.Matchers.notNull;
import static org.mockito.Matchers.nullable;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.matchers.MatcherDecorator;
import org.mockito.internal.matchers.VarargMatcher;
import org.mockito.internal.reporting.PrintSettings;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("unchecked")
public class InvocationMatcherTest {

    @Test
    public void testConstructorWithArguments() {
        Invocation invocation = createInvocation();
        List<Matcher> matchers = createMatchers();

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals(invocation.getMethod(), invocationMatcher.getMethod());
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithoutArguments() {
        Invocation invocation = createInvocation();

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(invocation.getMethod(), invocationMatcher.getMethod());
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testToString() {
        Invocation invocation = createInvocation();
        List<Matcher> matchers = createMatchers();
        PrintSettings printSettings = new PrintSettings();

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        String expected = printSettings.print(matchers, invocation);

        assertEquals(expected, invocationMatcher.toString());
    }

    @Test
    public void testMatchesMatches() {
        Invocation invocation1 = createInvocation();
        Invocation invocation2 = createInvocation();

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        boolean result = invocationMatcher.matches(invocation2);

        assertTrue(result);
    }

    @Test
    public void testMatchesDoesNotMatch() {
        Invocation invocation1 = createInvocation();
        Invocation invocation2 = createDifferentInvocation();

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        boolean result = invocationMatcher.matches(invocation2);

        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethodWithSimilarMethod() {
        Invocation invocation1 = createInvocation();
        Invocation invocation2 = createInvocation();

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        boolean result = invocationMatcher.hasSimilarMethod(invocation2);

        assertTrue(result);
    }

    @Test
    public void testHasSimilarMethodWithDifferentMethod() {
        Invocation invocation1 = createInvocation();
        Invocation invocation2 = createDifferentInvocation();

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        boolean result = invocationMatcher.hasSimilarMethod(invocation2);

        assertFalse(result);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(createInvocation(), createMatchers());
        Invocation invocation = createInvocationWithArguments("test", 5);

        invocationMatcher.captureArgumentsFrom(invocation);

        // Verify that the matchers have captured the arguments
        verify(matcher1).captureFrom("test");
        verify(matcher2).captureFrom(5);
    }

    private Invocation createInvocation() {
        Invocation invocation = new Invocation() {
            public Object[] getArguments() { return null; }
            public Object getMock() { return null; }
            public Method getMethod() { return null; }
            public Location getLocation() { return null; }
            public void callRealMethod() throws Throwable {}
            public boolean isVerified() { return false; }
            public void markVerified() {}
            public void markVerified(VerificationMode mode) {}
            public DescribedInvocation describeTo(Description description) { return null; }
        };
        return invocation;
    }

    private Invocation createDifferentInvocation() {
        Invocation invocation = new Invocation() {
            public Object[] getArguments() { return null; }
            public Object getMock() { return null; }
            public Method getMethod() { return null; }
            public Location getLocation() { return null; }
            public void callRealMethod() throws Throwable {}
            public boolean isVerified() { return true; }
            public void markVerified() {}
            public void markVerified(VerificationMode mode) {}
            public DescribedInvocation describeTo(Description description) { return null; }
        };
        return invocation;
    }

    private List<Matcher> createMatchers() {
        List<Matcher> matchers = new LinkedList<Matcher>();
        matchers.add(matcher1);
        matchers.add(matcher2);
        return matchers;
    }
}
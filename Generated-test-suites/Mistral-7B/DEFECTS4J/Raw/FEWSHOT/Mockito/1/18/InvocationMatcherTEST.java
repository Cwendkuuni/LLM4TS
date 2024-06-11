package org.mockito.internal.invocation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationMatcher.ArgumentsComparator;
import org.mockito.internal.invocation.InvocationMatcher.CapturesArguments;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherCreation() {
        Invocation invocation = mock(Invocation.class);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        InvocationMatcher invocationMatcherWithMatchers = new InvocationMatcher(invocation, Arrays.asList(Matchers.is("foo")));

        assertNotNull(invocationMatcher);
        assertNotNull(invocationMatcherWithMatchers);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(Collections.<Matcher>emptyList(), invocationMatcher.getMatchers());
        assertEquals(Arrays.asList(Matchers.is("foo")), invocationMatcherWithMatchers.getMatchers());
    }

    @Test
    public void testMatches() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Arrays.asList(Matchers.is("foo")));

        assertTrue(invocationMatcher1.matches(invocation1));
        assertFalse(invocationMatcher1.matches(invocation2));

        assertTrue(invocationMatcher1.matches(invocation1));
        assertFalse(invocationMatcher2.matches(invocation1));

        Invocation matchedInvocation = spy(invocation1);
        matchedInvocation.setVerified(true);

        assertTrue(invocationMatcher1.matches(invocation1));
        assertFalse(invocationMatcher2.matches(invocation1));

        assertTrue(invocationMatcher1.matches(matchedInvocation));
        assertFalse(invocationMatcher2.matches(matchedInvocation));

        assertEquals(invocation1.getMethod(), invocationMatcher1.getMethod());
        assertEquals(invocation2.getMethod(), invocationMatcher2.getMethod());
    }

    @Test
    public void testHasSimilarMethod() {
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(new Invocation(method1, null, null), Collections.<Matcher>emptyList());
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(new Invocation(method2, null, null), Collections.<Matcher>emptyList());

        assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher2));

        assertTrue(method1.getName().equals(method2.getName()));
        assertTrue(invocationMatcher1.hasSimilarMethod(invocationMatcher2));

        assertFalse(method1.getName().equals(method2.getName()));
        assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher2));

        InvocationMatcher invocationMatcher3 = new InvocationMatcher(new Invocation(method1, null, null),
                Arrays.asList(Matchers.is(new Object() {
                    @Override
                    public boolean equals(Object o) {
                        return method1.getName().equals(method2.getName());
                    }
                })));

        assertTrue(invocationMatcher1.hasSimilarMethod(invocationMatcher3));
    }

    @Test
    public void testToString() {
        Method method = mock(Method.class);
        List<Matcher> matchers = Arrays.asList(Matchers.is("foo"), Matchers.is("bar"));

        InvocationMatcher invocationMatcher = new InvocationMatcher(new Invocation(method, null, null), matchers);

        String str = invocationMatcher.toString();

        MatcherAssert.assertThat(str, startsWith("InvocationMatcher:"));
        MatcherAssert.assertThat(str, containsString("Invocation [method " + method.getName() + "]"));
        MatcherAssert.assertThat(str, containsString("with " + matchers.size() + " matchers:"));
        MatcherAssert.assertThat(str, containsString("Matcher: is(\"foo\")"));
        MatcherAssert.assertThat(str, containsString("Matcher: is(\"bar\")"));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Method method = mock(Method.class);
        Object[] rawArguments = new Object[]{1, "foo", 2.5};

        InvocationMatcher invocationMatcher = new InvocationMatcher(new Invocation(method, rawArguments, new Object[0][], new Location(1, 1, "testMethod"), null),
                Arrays.asList(Matchers.anyObject(), Matchers.instanceOf(String.class), Matchers.instanceOf(Number.class)));

        Object capturedArgument1 = null;
        Object capturedArgument2 = null;
        Object capturedArgument3 = null;

        Matcher matcher1 = CapturesArguments.forPosition(0);
        Matcher matcher2 = CapturesArguments.forPosition(1);
        Matcher matcher3 = CapturesArguments.forPosition(2);

        invocationMatcher.captureArgumentsFrom(new Invocation(method, rawArguments, new Object[0][], new Location(1, 1, "testMethod"), null));

        assertEquals(rawArguments[0], matcher1.getValue());
        assertEquals(rawArguments[1], matcher2.getValue());
        assertEquals(rawArguments[2], matcher3.getValue());

        capturedArgument1 = matcher1.getValue();
        capturedArgument2 = matcher2.getValue();
        capturedArgument3 = matcher3.getValue();

        invocationMatcher.captureArgumentsFrom(new Invocation(method, rawArguments, new Object[0][], new Location(1, 1, "testMethod"), null));

        assertEquals(capturedArgument1, matcher1.getValue());
        assertEquals(capturedArgument2, matcher2.getValue());
        assertEquals(capturedArgument3, matcher3.getValue());
    }

    @Test
    public void testCaptureArgumentsFrom_varargs() {
        Method method = mock(Method.class);

        InvocationMatcher invocationMatcher = new InvocationMatcher(new Invocation(method, null, new Object[]{"foo", "bar"}, null, null),
                Arrays.asList(Matchers.containsString("foo"), Matchers.containsString("bar")));

        Object[] capturedArguments = new Object[2];

        Matcher matcher = CapturesArguments.forPosition(0);

        invocationMatcher.captureArgumentsFrom(new Invocation(method, new Object[]{"foo1", "bar1", "foo2", "bar2"}, null, null, null));

        assertEquals("foo1", capturedArguments[0]);
        assertEquals("foo2", capturedArguments[1]);

        capturedArguments = new Object[3];

        matcher = CapturesArguments.forPosition(1);

        invocationMatcher.captureArgumentsFrom(new Invocation(method, new Object[]{"foo1", "bar1", "foo2", "bar2"}, null, null, null));

        assertEquals("bar1", capturedArguments[1]);
        assertEquals("bar2", capturedArguments[2]);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCaptureArgumentsFrom_varargs_invalid_position() {
        Method method = mock(Method.class);

        InvocationMatcher invocationMatcher = new InvocationMatcher(new Invocation(method, null, new Object[]{"foo", "bar"}, null, null),
                Arrays.asList(Matchers.containsString("foo"), Matchers.containsString("bar")));

        invocationMatcher.captureArgumentsFrom(new Invocation(method, new Object[]{"foo1", "bar1", "foo2", "bar2"}, null, null, null));

        CapturesArguments capturesArguments = (CapturesArguments) invocationMatcher.getMatchers().get(1);

        capturesArguments.captureFrom(new Object[]{1, 2, 3, 4});
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = Arrays.asList(
                new Invocation(mock(Method.class), new Object[0][], new Object[0][], null, null),
                new Invocation(mock(Method.class), new Object[]{1}, new Object[0][], null, null),
                new Invocation(mock(Method.class), new Object[]{1, "foo"}, new Object[0][], null, null),
                new Invocation(mock(Method.class), new Object[]{1, "foo", 2.5}, new Object[0][], null, null),
                new Invocation(mock(Method.class), new Object[]{1, "foo", 2.5, "bar"}, new Object[0][], null, null),
                new Invocation(mock(Method.class), new Object[]{1, "foo", 2.5, "bar", new Object[0][], new Object[0][], null, null}));

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        assertEquals(invocations.size(), invocationMatchers.size());

        for (int i = 0; i < invocations.size(); i++) {
            assertEquals(invocations.get(i).getMethod(), invocationMatchers.get(i).getMethod());
        }
    }
}
package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.ArgumentMatchers.any;
import static org.mockito.internal.matchers.ArgumentMatchers.eq;
import static org.mockito.internal.matchers.ArgumentMatchers.isA;
import static org.mockito.internal.matchers.ArgumentMatchers.isNull;
import static org.mockito.internal.matchers.ArgumentMatchers.notNull;
import static org.mockito.internal.reporting.PrintSettings.print;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import org.mockito.internal.invocation.ArgumentsProcessor;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.matchers.MatcherDecorator;
import org.mockito.internal.reporting.PrintSettings;

public class InvocationMatcherTest {

    private static final Object MOCK = new Object() {};
    private static final Object[] ARGS = new Object[] { "foo", 1, "bar" };
    private static final Method METHOD = new Method(InvocationMatcherTest.class.getName(), "test", new Class[] { String.class, Integer.TYPE, String.class });
    private static final Invocation INVOCATION = new MockitoInvocation(MOCK, METHOD, ARGS);
    private static final InvocationMatcher MATCHER = new InvocationMatcher(INVOCATION, Collections.singletonList(eq("foo")));
    private static final InvocationMatcher MATCHER_WITH_VAR_ARGS = new InvocationMatcher(INVOCATION, Arrays.asList(eq("foo"), any(String.class)));
    private static final InvocationMatcher MATCHER_WITH_EMPTY_LIST = new InvocationMatcher(INVOCATION);
    private static final InvocationMatcher MATCHER_WITH_NULL = new InvocationMatcher(null);
    private static final InvocationMatcher MATCHER_WITH_DIFFERENT_MOCK = new InvocationMatcher(new Object() {}, INVOCATION);
    private static final InvocationMatcher MATCHER_WITH_DIFFERENT_METHOD = new InvocationMatcher(INVOCATION, new Method(Object.class.getName(), "test", new Class[0]));
    private static final InvocationMatcher MATCHER_WITH_VERIFIED_INVOCATION = new InvocationMatcher(new Invocation(MOCK, METHOD, ARGS).verify());

    private ArgumentCaptor<String> stringCaptor;

    @Before
    public void setUp() {
        this.stringCaptor = ArgumentCaptor.forClass(String.class);
    }

    @Test
    public void testToString() {
        assertTrue(print(MATCHER.getMatchers(), MATCHER.getInvocation()).equals("matches invocation: foo"));
    }

    @Test
    public void testMatches() {
        assertTrue(MATCHER.matches(INVOCATION));
        assertFalse(MATCHER.matches(new Invocation(MOCK, new Method(Object.class.getName(), "test", new Class[0]), new Object[0])));
    }

    @Test
    public void testMatchesWithListOfMatchers() {
        assertTrue(MATCHER_WITH_VAR_ARGS.matches(INVOCATION));
        assertFalse(MATCHER_WITH_VAR_ARGS.matches(new Invocation(MOCK, METHOD, new Object[] { "foo", "bar" })));
        assertTrue(MATCHER_WITH_EMPTY_LIST.matches(INVOCATION));
        assertTrue(MATCHER_WITH_EMPTY_LIST.matches(new Invocation(MOCK, METHOD, new Object[0])));
        assertFalse(MATCHER_WITH_EMPTY_LIST.matches(new Invocation(MOCK, METHOD, ARGS)));
        assertFalse(MATCHER_WITH_NULL.matches(INVOCATION));
        assertFalse(MATCHER_WITH_DIFFERENT_MOCK.matches(INVOCATION));
        assertFalse(MATCHER_WITH_DIFFERENT_METHOD.matches(INVOCATION));
        assertFalse(MATCHER_WITH_VERIFIED_INVOCATION.matches(INVOCATION));
    }

    @Test
    public void testHasSimilarMethod() {
        assertTrue(MATCHER.hasSimilarMethod(new Invocation(MOCK, METHOD, ARGS)));
        assertFalse(MATCHER.hasSimilarMethod(new Invocation(MOCK, new Method(Object.class.getName(), "test", new Class[0]), new Object[0])));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        new InvocationMatcher(INVOCATION).captureArgumentsFrom(INVOCATION);
        new InvocationMatcher(INVOCATION).getMatchers().get(0).captureFrom(INVOCATION.getArguments()[0]);
        assertEquals("foo", stringCaptor.getValue());
        new InvocationMatcher(INVOCATION).getMatchers().get(0).captureFrom(INVOCATION.getArguments()[1]);
        assertEquals(1, (Integer) INVOCATION.getArguments()[1]);
        new InvocationMatcher(INVOCATION).getMatchers().get(1).captureFrom(INVOCATION.getArguments()[2]);
        assertEquals("bar", stringCaptor.getValue());
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = Arrays.asList(INVOCATION, new Invocation(MOCK, METHOD, ARGS), new Invocation(MOCK, METHOD, new Object[] { "foo", "bar" }));
        List<InvocationMatcher> matchers = InvocationMatcher.createFrom(invocations);
        InvocationMatcher matcher = matchers.get(0);
        assertTrue(matcher.matches(INVOCATION));
        assertTrue(matcher.matches(new Invocation(MOCK, METHOD, new Object[] { "foo", "bar" })));
        assertFalse(matcher.matches(new Invocation(MOCK, METHOD, new Object[] { 1, "bar" })));
        InvocationMatcher matcherWithVarArgs = matchers.get(1);
        assertTrue(matcherWithVarArgs.matches(INVOCATION));
        assertTrue(matcherWithVarArgs.matches(new Invocation(MOCK, METHOD, ARGS)));
        assertTrue(matcherWithVarArgs.matches(new Invocation(MOCK, METHOD, new Object[] { "foo", "bar", 1 })));
        assertFalse(matcherWithVarArgs.matches(new Invocation(MOCK, METHOD, new Object[] { 1, 1 })));
    }
}

class MockitoInvocation implements Invocation {

    private final Object mock;
    private final Method method;
    private final Object[] arguments;

    public MockitoInvocation(Object mock, Method method, Object[] arguments) {
        this.mock = mock;
        this.method = method;
        this.arguments = arguments;
    }

    @Override
    public Object getMock() {
        return this.mock;
    }

    @Override
    public Method getMethod() {
        return this.method;
    }

    @Override
    public Object[] getArguments() {
        return this.arguments;
    }

    @Override
    public Object[] getRawArguments() {
        return this.arguments;
    }

    @Override
    public Location getLocation() {
        return new Location("test", 0, 0);
    }

    @Override
    public boolean isVerified() {
        return false;
    }

    public Invocation verify() {
        throw new IllegalStateException("Cannot verify a mocked invocation");
    }
}
package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.ArgumentsMatcher.argThat;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.stub.MockitoStub;

public class InvocationMatcherTest {
    @Test
    public void testInvocationMatcher_givenInvocationAndMatchers_whenNewInstance_thenReturnInstance() {
        // given
        Invocation invocation = new InvocationStub();
        List<Matcher> matchers = Collections.singletonList(argThat(arg -> arg.equals("foo")));

        // when
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // then
        assertFalse(invocationMatcher.getMatchers().isEmpty());
        assertTrue(invocationMatcher.matches(invocation));
    }

    @Test
    public void testInvocationMatcher_givenInvocation_whenNewInstanceWithEmptyList_thenReturnInstance() {
        // given
        Invocation invocation = new InvocationStub();

        // when
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // then
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testGetMethod() {
        // given
        Invocation invocation = new InvocationStub();
        Method method = MockitoStub.class.getMethod("methodWithOneArgument", String.class);

        // when
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.emptyList());
        invocationMatcher.setInvocation(invocation);
        invocationMatcher.setMethod(method);

        // then
        assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testGetInvocation() {
        // given
        Invocation invocation = new InvocationStub();

        // when
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // then
        assertEquals(invocation, invocationMatcher.getInvocation());
    }

    @Test
    public void testGetMatchers() {
        // given
        Invocation invocation = new InvocationStub();
        List<Matcher> matchers = Collections.singletonList(argThat(arg -> arg.equals("foo")));

        // when
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // then
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testToString() {
        // given
        Invocation invocation = new InvocationStub();
        List<Matcher> matchers = Collections.singletonList(argThat(arg -> arg.equals("foo")));
        PrintSettings printSettings = new PrintSettings();

        // when
        String toString = new InvocationMatcher(invocation, matchers).toString(printSettings);

        // then
        assertFalse(toString.isEmpty());
    }

    @Test
    public void testMatches() {
        // given
        Invocation invocation1 = new InvocationStub();
        Invocation invocation2 = new InvocationStub();
        Method method = MockitoStub.class.getMethod("methodWithOneArgument", String.class);
        String expectedArgument = "foo";

        // when
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Collections.singletonList(argThat(arg -> arg.equals(expectedArgument))));
        invocationMatcher1.setInvocation(invocation1);
        invocationMatcher1.setMethod(method);
        invocationMatcher1.setArguments(new Object[] { expectedArgument });

        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.singletonList(argThat(arg -> arg.equals("bar"))));
        invocationMatcher2.setInvocation(invocation2);
        invocationMatcher2.setMethod(method);

        // then
        assertFalse(invocationMatcher1.matches(invocationMatcher2));

        // given invocation1 and invocation2 have the same method and arguments

        // when
        invocationMatcher1.setArguments(invocationMatcher2.getArguments());

        // then
        assertTrue(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        // given
        Invocation invocation = new InvocationStub();
        List<Matcher> matchers = Collections.singletonList(argThat(arg -> arg.equals("foo")));
        Object[] expectedArguments = new Object[] { "foo" };

        // when
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        invocationMatcher.setInvocation(invocation);
        invocationMatcher.setArguments(expectedArguments);

        // then
        assertTrue(invocationMatcher.safelyArgumentsMatch(expectedArguments));
    }

    @Test
    public void testHasSameMethod() {
        // given
        Invocation invocation1 = new InvocationStub();
        Invocation invocation2 = new InvocationStub();
        Method method = MockitoStub.class.getMethod("methodWithOneArgument", String.class);

        // when
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Collections.emptyList());
        invocationMatcher1.setMethod(method);

        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.emptyList());
        invocationMatcher2.setMethod(method);

        // then
        assertTrue(invocationMatcher1.hasSameMethod(invocationMatcher2));
    }

    @Test
    public void testHasSimilarMethod() {
        // given
        Invocation invocation1 = new InvocationStub();
        Invocation invocation2 = new InvocationStub();
        Method method1 = MockitoStub.class.getMethod("methodWithOneArgument", String.class);
        Method method2 = MockitoStub.class.getMethod("methodWithTwoArguments", String.class, int.class);

        // when
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Collections.emptyList());
        invocationMatcher1.setMethod(method1);

        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.emptyList());
        invocationMatcher2.setMethod(method2);

        // then
        assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher2));

        // given invocation1 and invocation2 have the same method name, same mock, and same arguments

        // when
        Object[] arguments1 = new Object[] { "foo" };
        Object[] arguments2 = new Object[] { "foo" };
        invocationMatcher1.setArguments(arguments1);
        invocationMatcher2.setArguments(arguments2);

        // then
        assertTrue(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        // given
        Invocation invocation = new InvocationStub();
        List<Matcher> matchers = Collections.singletonList(argThat(arg -> arg instanceof CapturesArguments));

        // when
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        invocationMatcher.setInvocation(invocation);
        String expectedArgument = "foo";
        invocationMatcher.getInvocation().setArguments(new Object[] { expectedArgument });

        // then
        CapturesArguments capturesArguments = mock(CapturesArguments.class);
        invocationMatcher.captureArgumentsFrom(invocation);

        verify(capturesArguments).captureFrom(expectedArgument);
    }
}

class InvocationStub implements Invocation {
    private Object[] arguments;

    @Override
    public Method getMethod() {
        return null;
    }

    @Override
    public Object[] getArguments() {
        return arguments;
    }

    @Override
    public Object getMock() {
        return this;
    }

    @Override
    public boolean isVerified() {
        return false;
    }

    @Override
    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }

    @Override
    public Location getLocation() {
        return null;
    }
}
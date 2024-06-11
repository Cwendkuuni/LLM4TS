package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.internal.matchers.ArgumentsMatcher.argThat;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Stubber;

@SuppressWarnings("unchecked")
public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;
    private Invocation invocation;
    private MockitoException exception;
    private Stubber stubber;
    private Object[] arguments;
    private Object mock;

    @Before
    public void setUp() {
        stubber = mock(Stubber.class);
        mock = mock(Serializable.class);
        invocation = new InvocationOnMock(stubber, mock, new Location("Test.class", 1), new Method(Object.class, new Class[]{}), new Object[0]);
        invocationMatcher = new InvocationMatcher(invocation);
        exception = new MockitoException("MockitoException message");
    }

    @Test
    public void testGetMethod() {
        Method method = invocation.getMethod();
        Method returnedMethod = invocationMatcher.getMethod();
        assertTrue(method.equals(returnedMethod));
    }

    @Test
    public void testGetInvocation() {
        Invocation returnedInvocation = invocationMatcher.getInvocation();
        assertTrue(invocation.equals(returnedInvocation));
    }

    @Test
    public void testGetMatchers() {
        List<Matcher> matchers = invocation.argumentsToMatchers();

        InvocationMatcher matcherWithEmptyList = new InvocationMatcher(invocation);
        List<Matcher> returnedMatchers = matcherWithEmptyList.getMatchers();

        assertNotNull(returnedMatchers);
        assertTrue(returnedMatchers.isEmpty());

        InvocationMatcher matcherWithCustomList = new InvocationMatcher(invocation, Collections.singletonList(argThat(IsInstanceOf.instanceOf(Integer.class))));
        returnedMatchers = matcherWithCustomList.getMatchers();
        assertNotNull(returnedMatchers);
        assertTrue(returnedMatchers.size() == 1);
        assertTrue(returnedMatchers.get(0) instanceof CapturesArguments);
        assertTrue(returnedMatchers.get(0).matches(arguments[0]));
    }

    @Test
    public void testToString() {
        String result = invocationMatcher.toString();
        String expected = invocation.toString(Collections.<Matcher>emptyList(), new PrintSettings());
        assertTrue(result.equals(expected));
    }

    @Test
    public void testMatches() {
        InvocationMatcher anotherInvocationMatcher = new InvocationMatcher(new InvocationOnMock(stubber, mock, new Location("Test.class", 2), new Method(Object.class, new Class[]{}), new Object[0]));
        assertTrue(invocationMatcher.matches(invocation));
        assertFalse(invocationMatcher.matches(anotherInvocationMatcher));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Object[] arguments = new Object[]{1, 2, 3};
        InvocationMatcher anotherInvocationMatcher = new InvocationMatcher(new InvocationOnMock(stubber, mock, new Location("Test.class", 2), new Method(Object[].class, new Class[]{Integer[].class}), arguments));

        assertTrue(invocationMatcher.safelyArgumentsMatch(new Object[]{1, 2, 3}));
        assertFalse(invocationMatcher.safelyArgumentsMatch(new Object[]{1, 3, 2}));
        assertFalse(invocationMatcher.safelyArgumentsMatch(new Object[]{new Integer[]{1, 2, 3}}));
        assertFalse(invocationMatcher.safelyArgumentsMatch(new Object[]{"1", "2", "3"}));
    }

    @Test(expected = MockitoException.class)
    public void testHasSimilarMethod_DifferentMock() {
        InvocationMatcher anotherInvocationMatcher = new InvocationMatcher(new InvocationOnMock(stubber, mock, new Location("Test.class", 2), new Method(Object.class, new Class[]{}), new Object[0]));
        invocationMatcher.hasSimilarMethod(anotherInvocationMatcher);
    }

    @Test
    public void testHasSameMethod() {
        InvocationMatcher anotherInvocationMatcher = new InvocationMatcher(invocation);
        assertTrue(invocationMatcher.hasSameMethod(anotherInvocationMatcher));
    }

    @Test
    public void testHasSameMethod_DifferentMethods() {
        InvocationMatcher anotherInvocationMatcher = new InvocationMatcher(new InvocationOnMock(stubber, mock, new Location("Test.class", 2), new Method(Integer.class, new Class[0]), new Object[0]));
        assertFalse(invocationMatcher.hasSameMethod(anotherInvocationMatcher));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        CapturesArguments capturesArguments = mock(CapturesArguments.class);
        invocationMatcher.captureArgumentsFrom(invocation);
        assertTrue(capturesArguments.getValue() == arguments[0]);
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new LinkedList<>();
        invocations.add(new InvocationOnMock(stubber, mock, new Location("Test.class", 1), new Method(Object.class, new Class[]{}), new Object[0]));
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        assertTrue(invocationMatchers.size() == 1);
        InvocationMatcher invocationMatcherFromList = invocationMatchers.get(0);
        assertTrue(invocationMatcherFromList.matches(invocation));
    }

    @Test
    public void testMatches_Exceptions() {
        InvocationMatcher anotherInvocationMatcher = new InvocationMatcher(new InvocationOnMock(stubber, mock, new Location("Test.class", 2), new Method(Object.class, new Class[]{}), exception.getArguments()));
        InvocationMatcher thisInvocationMatcher = new InvocationMatcher(invocation);
        assertFalse(thisInvocationMatcher.matches(anotherInvocationMatcher));
    }

    @Test
    public void testToString_PrintSettings() {
        String result = invocationMatcher.toString(new PrintSettings());
        String expected = invocation.toString(Collections.<Matcher>emptyList(), new PrintSettings());
        assertTrue(result.equals(expected));
    }
}
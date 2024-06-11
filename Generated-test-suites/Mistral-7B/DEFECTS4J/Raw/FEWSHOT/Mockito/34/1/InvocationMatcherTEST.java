package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.ArgumentsMatcher.argThat;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsComparator;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.util.reflection.Whitebox;

public class InvocationMatcherTest {

    private InvocationMatcher matcher;
    private Invocation mockInvocation;
    private Invocation realInvocation;
    private Method mockMethod;
    private Object mockObject;
    private ArgumentsComparator argumentsComparator;

    @Before
    public void beforeEachTest() {
        mockObject = Mockito.mock(String.class);
        mockMethod = Mockito.spy(String.class.getMethod("length"));
        realInvocation = new InvocationOnMock(mockObject, mockMethod);
        matcher = new InvocationMatcher(realInvocation);
        argumentsComparator = new ArgumentsComparator();
    }

    @Test
    public void testNewInstanceWithMatchers() {
        List<Matcher> matchers = Arrays.asList(argThat(Matchers.isA(String.class)), argThat(Matchers.equalTo("foo")));
        InvocationMatcher invocationMatcher = new InvocationMatcher(realInvocation, matchers);
        assertTrue(invocationMatcher.matches(realInvocation));
    }

    @Test
    public void testNewInstanceWithoutMatchers() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(realInvocation);
        assertTrue(invocationMatcher.matches(realInvocation));
    }

    @Test
    public void testGetMethod() {
        Method mockMethod = Mockito.spy(String.class.getMethod("length"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMock(mockObject, mockMethod));
        assertEquals(mockMethod, invocationMatcher.getMethod());
    }

    @Test
    public void testGetInvocation() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(realInvocation);
        assertEquals(realInvocation, invocationMatcher.getInvocation());
    }

    @Test
    public void testGetMatchers() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(realInvocation);
        assertTrue(Collections.isEmpty(invocationMatcher.getMatchers()));
    }

    @Test
    public void testToString() {
        String expected = String.format("Invocation [%s, %s]", mockMethod, matchers);
        assertEquals(expected, matcher.toString());
    }

    @Test
    public void testToStringWithPrintSettings() {
        String expected = String.format("Invocation %s [%s, %s]", matcher.getLocation().getLineNumber(), mockMethod,
                Arrays.asList(Matchers.isA(String.class), Matchers.equalTo("foo")));
        assertEquals(expected, matcher.toString(new PrintSettings()));
    }

    @Test
    public void testMatches() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(realInvocation);
        assertTrue(invocationMatcher.matches(realInvocation));
    }

    @Test
    public void testMatchesWithDifferentMock() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(realInvocation);
        Invocation differentInvocation = new InvocationOnMock(Mockito.mock(Integer.class), mockMethod);
        assertFalse(invocationMatcher.matches(differentInvocation));
    }

    @Test
    public void testMatchesWithDifferentMethod() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(realInvocation);
        InvocationOnMock differentInvocation = new InvocationOnMock(mockObject, String.class.getMethod("toUpperCase"));
        assertFalse(invocationMatcher.matches(differentInvocation));
    }

    @Test
    public void testMatchesWithArgumentsMismatch() {
        List<Matcher> matchers = Arrays.asList(argThat(Matchers.isA(String.class)), argThat(Matchers.equalTo("foo")));
        InvocationMatcher invocationMatcher = new InvocationMatcher(realInvocation, matchers);
        Object[] arguments = new Object[]{"bar"};
        InvocationOnMock invocationWithArguments = new InvocationOnMock(mockObject, mockMethod, arguments);
        assertFalse(invocationMatcher.matches(invocationWithArguments));
    }

    @Test
    public void testMatchesWithSameMethodButDifferentArguments() {
        List<Matcher> matchers = Arrays.asList(argThat(Matchers.isA(String.class)), argThat(Matchers.equalTo("foo")));
        InvocationMatcher invocationMatcher = new InvocationMatcher(realInvocation, matchers);
        Object[] arguments = new Object[]{"bar"};
        InvocationOnMock invocationWithArguments = new InvocationOnMock(mockObject, mockMethod, arguments);
        assertFalse(invocationMatcher.matches(invocationWithArguments));
    }

    @Test
    public void testHasSimilarMethod() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(realInvocation);
        InvocationOnMock differentInvocation = new InvocationOnMock(mockObject, String.class.getMethod("toUpperCase"));
        assertFalse(invocationMatcher.hasSimilarMethod(differentInvocation));

        InvocationOnMock sameInvocation = new InvocationOnMock(mockObject, mockMethod);
        assertTrue(invocationMatcher.hasSimilarMethod(sameInvocation));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Object[] arguments = new Object[]{"foo"};
        InvocationOnMock invocationWithArguments = new InvocationOnMock(mockObject, mockMethod, arguments);
        assertTrue(argumentsComparator.argumentsMatch(matcher, invocationWithArguments.getArguments()));
    }

    @Test(expected = Throwable.class)
    public void testSafelyArgumentsMatchWithThrowable() {
        Object[] arguments = new Object[]{"foo"};
        InvocationOnMock invocationWithArguments = new InvocationOnMock(mockObject, mockMethod, arguments);
        argumentsComparator.argumentsMatch(matcher, Arrays.asList(new Object[]{"bar"}));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        List<Matcher> matchers = Arrays.asList(argThat(Matchers.isA(String.class)));
        InvocationMatcher invocationMatcher = new InvocationMatcher(realInvocation, matchers);
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        mockInvocation = new InvocationOnMock(mockObject, mockMethod, new Object[]{"foo"});
        invocationMatcher.captureArgumentsFrom(mockInvocation);
        assertEquals("foo", Whitebox.getInternalState(argumentCaptor.getValue()));
    }
}
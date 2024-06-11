package org.mockito.internal.invocation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.isInstanceOf;
import static org.hamcrest.CoreMatchers.isNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    private List<Matcher> matchers;
    private InvocationMatcher invocationMatcher;

    @Before
    public void setup() {
        matchers = Collections.singletonList(isA(String.class));
        invocationMatcher = new InvocationMatcher(Mockito.mock(Invocation.class), matchers);
    }

    @Test
    public void testInvocationMatcherWithEmptyMatchers() {
        // Test case for InvocationMatcher constructor with an empty list of matchers
        InvocationMatcher emptyMatcher = new InvocationMatcher(Mockito.mock(Invocation.class));
        Invocation invocation = Mockito.mock(Invocation.class);
        when(invocation.getArguments()).thenReturn(new Object[0]);
        when(invocation.getMock()).thenReturn("mock");
        when(invocation.getMethod()).thenReturn(Mockito.mock(Method.class));

        // Test that matches method returns true when given Invocation is equal to the one provided during construction
        assertTrue(emptyMatcher.matches(invocation));

        // Test that toString method returns a string representation of the Invocation and empty matchers
        String printSettings = new PrintSettings().defaultPrintSettings();
        String expectedPrint = String.format("%s %s() %s", printSettings.print(Collections.emptyList(), invocation));
        assertEquals(expectedPrint, emptyMatcher.toString());

        // Test that hasSameMethod method returns true when given Invocation has the same method as the one provided during construction
        Invocation identicalInvocation = Mockito.mock(Invocation.class);
        when(identicalInvocation.getArguments()).thenReturn(new Object[0]);
        when(identicalInvocation.getMock()).thenReturn("mock");
        when(identicalInvocation.getMethod()).thenReturn(invocation.getMethod());
        assertTrue(emptyMatcher.hasSameMethod(identicalInvocation));
    }

    @Test
    public void testInvocationMatcherWithSingleMatcher() {
        // Test case for InvocationMatcher constructor with a single matcher
        Matcher<Object> stringMatcher = isA(String.class);
        List<Matcher<Object>> matchers = Arrays.asList(stringMatcher);
        InvocationMatcher matcher = new InvocationMatcher(Mockito.mock(Invocation.class), matchers);

        // Test that matches method returns true when given Invocation matches the provided matcher
        Object[] arguments = new Object[]{"test"};
        Invocation invocation = Mockito.mock(Invocation.class);
        when(invocation.getArguments()).thenReturn(arguments);
        when(invocation.getMock()).thenReturn("mock");
        when(invocation.getMethod()).thenReturn(Mockito.mock(Method.class));
        when(stringMatcher.matches(arguments[0])).thenReturn(true);

        // Test that matches method returns false when given Invocation does not match the provided matcher
        when(stringMatcher.matches(arguments[0])).thenReturn(false);
        assertFalse(matcher.matches(invocation));

        // Test that toString method returns a string representation of the Invocation and the provided matcher
        String printSettings = new PrintSettings().defaultPrintSettings();
        String expectedPrint = String.format("%s Invocation(%s) %s", printSettings.print(matchers, invocation), invocation.getMethod().getName(), matchers.get(0).description());
        assertEquals(expectedPrint, matcher.toString());

        // Test that hasSameMethod method returns false when given Invocation has a different method than the one provided during construction
        Invocation differentInvocation = Mockito.mock(Invocation.class);
        when(differentInvocation.getArguments()).thenReturn(arguments);
        when(differentInvocation.getMock()).thenReturn("mock");
        when(differentInvocation.getMethod()).thenReturn(Mockito.mock(Method.class));
        when(differentInvocation.getMethod().getName()).thenReturn("differentMethod");
        assertFalse(matcher.hasSameMethod(differentInvocation));

        // Test that captureArgumentsFrom method captures the arguments from the Invocation when the method matches the provided matcher
        Answer<Object> answer = mock(Answer.class);
        InvocationOnMock invocationMock = mock(InvocationOnMock.class);
        when(invocationMock.getMethod()).thenReturn(Mockito.mock(Method.class));
        when(invocationMock.getArguments()).thenReturn(arguments);

        matcher.captureArgumentsFrom(invocationMock);
        verify(answer).answer(eq(matcher), any(Class.class), eq(arguments[0]));
    }

    @Test
    public void testInvocationMatcherWithMultipleMatchers() {
        // Test case for InvocationMatcher constructor with multiple matchers
        List<Matcher<Object>> matchers = Arrays.asList(isA(String.class), isA(Integer.class));
        InvocationMatcher matcher = new InvocationMatcher(Mockito.mock(Invocation.class), matchers);

        // Test that matches method returns false when given Invocation does not match any of the provided matchers
        Object[] arguments = new Object[]{"test", 1};
        Invocation invocation = Mockito.mock(Invocation.class);
        when(invocation.getArguments()).thenReturn(arguments);
        when(invocation.getMock()).thenReturn("mock");
        when(invocation.getMethod()).thenReturn(Mockito.mock(Method.class));

        // Test that matches method returns false when given Invocation matches the first provided matcher but not the second one
        InvocationMatcher firstMatcher = new InvocationMatcher(Mockito.mock(Invocation.class), Collections.singletonList(isA(String.class)));
        when(firstMatcher.matches(invocation)).thenReturn(true);
        when(matchers.get(1).matches(arguments[1])).thenReturn(false);
        assertFalse(matcher.matches(invocation));

        // Test that matches method returns false when given Invocation matches the second provided matcher but not the first one
        InvocationMatcher secondMatcher = new InvocationMatcher(Mockito.mock(Invocation.class), Collections.singletonList(isA(Integer.class)));
        when(firstMatcher.matches(invocation)).thenReturn(false);
        when(matchers.get(0).matches(arguments[0])).thenReturn(false);
        when(matchers.get(1).matches(arguments[1])).thenReturn(true);
        assertFalse(matcher.matches(invocation));

        // Test that toString method returns a string representation of the Invocation and all the provided matchers
        String printSettings = new PrintSettings().defaultPrintSettings();
        String expectedPrint = String.format("%s Invocation(%s) %s, %s", printSettings.print(matchers, invocation), invocation.getMethod().getName(), matchers.get(0).description(), matchers.get(1).description());
        assertEquals(expectedPrint, matcher.toString());

        // Test that hasSameMethod method returns false when given Invocation has a different method than the one provided during construction
        Invocation differentInvocation = Mockito.mock(Invocation.class);
        when(differentInvocation.getArguments()).thenReturn(arguments);
        when(differentInvocation.getMock()).thenReturn("mock");
        when(differentInvocation.getMethod()).thenReturn(Mockito.mock(Method.class));
        when(differentInvocation.getMethod().getName()).thenReturn("differentMethod");
        assertFalse(matcher.hasSameMethod(differentInvocation));

        // Test that captureArgumentsFrom method captures the arguments from the Invocation when all the provided matchers match
        Answer<Object> answer = mock(Answer.class);
        InvocationOnMock invocationMock = mock(InvocationOnMock.class);
        when(invocationMock.getMethod()).thenReturn(Mockito.mock(Method.class));
        when(invocationMock.getArguments()).thenReturn(arguments);

        matcher.captureArgumentsFrom(invocationMock);
        verify(answer).answer(eq(matcher), any(Class.class), eq(arguments[0]));
        verify(answer).answer(eq(matcher), any(Class.class), eq(arguments[1]));
    }

    @Test
    public void testInvocationMatcherWithVarargs() {
        // Test case for InvocationMatcher with varargs
        Matcher<Object[]> varargsMatcher = ArgumentMatchers.arrayContaining(isA(String.class));
        List<Matcher<Object>> matchers = Arrays.asList(varargsMatcher);
        InvocationMatcher matcher = new InvocationMatcher(Mockito.mock(Invocation.class), matchers);

        // Test that matches method returns false when given Invocation does not match the provided matcher
        Object[] arguments = new Object[]{"test1", "test2", "test3"};
        Invocation invocation = Mockito.mock(Invocation.class);
        when(invocation.getArguments()).thenReturn(arguments);
        when(invocation.getMock()).thenReturn("mock");
        when(invocation.getMethod()).thenReturn(Mockito.mock(Method.class));

        when(varargsMatcher.matches(arguments)).thenReturn(false);
        assertFalse(matcher.matches(invocation));

        // Test that matches method returns true when given Invocation matches the provided matcher
        Object[] testArguments = new Object[]{"test1", "test2", "test3"};
        when(varargsMatcher.matches(testArguments)).thenReturn(true);
        assertTrue(matcher.matches(invocation));

        // Test that toString method returns a string representation of the Invocation and all the provided matchers
        String printSettings = new PrintSettings().defaultPrintSettings();
        String expectedPrint = String.format("%s Invocation(%s) %s", printSettings.print(matchers, invocation), invocation.getMethod().getName(), varargsMatcher.description());
        assertEquals(expectedPrint, matcher.toString());

        // Test that captureArgumentsFrom method captures the arguments from the Invocation when all the provided matchers match
        Answer<Object> answer = mock(Answer.class);
        InvocationOnMock invocationMock = mock(InvocationOnMock.class);
        when(invocationMock.getMethod()).thenReturn(Mockito.mock(Method.class));
        when(invocationMock.getArguments()).thenReturn(testArguments);

        matcher.captureArgumentsFrom(invocationMock);
        verify(answer).answer(eq(matcher), any(Class.class), eq(testArguments));
    }
}
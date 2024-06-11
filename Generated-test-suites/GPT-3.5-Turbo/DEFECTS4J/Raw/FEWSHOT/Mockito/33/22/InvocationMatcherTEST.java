package org.mockito.internal.invocation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.hamcrest.Matcher;
import org.junit.Test;

public class InvocationMatcherTest {

    @Test
    public void testConstructor() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(Collections.emptyList(), invocationMatcher.getMatchers());
    }
    
    @Test
    public void testConstructorWithMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(mock(Matcher.class), mock(Matcher.class));
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }
    
    @Test
    public void testGetMethod() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        
        assertEquals(method, invocationMatcher.getMethod());
    }
    
    @Test
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        List<Matcher> matchers = Arrays.asList(mock(Matcher.class), mock(Matcher.class));
        
        String expectedToString = "invocation.toString()";
        when(invocation.toString(matchers, new PrintSettings())).thenReturn(expectedToString);
        
        assertEquals(expectedToString, invocationMatcher.toString());
    }
    
    @Test
    public void testMatches() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Object mock = new Object();
        Object[] arguments = new Object[] {};
        
        Invocation actual = new Invocation(mock, mock(Method.class), arguments);
        
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        when(argumentsComparator.argumentsMatch(invocationMatcher, actual)).thenReturn(true);
        
        assertTrue(invocationMatcher.matches(actual));
    }
    
    @Test
    public void testSafelyArgumentsMatch() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Object[] actualArgs = new Object[] {};
        
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        when(argumentsComparator.argumentsMatch(invocationMatcher, actualArgs)).thenReturn(true);
        
        assertTrue(invocationMatcher.safelyArgumentsMatch(actualArgs));
    }
    
    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Invocation candidate = mock(Invocation.class);
        
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(candidate.getMethod()).thenReturn(method);
        
        when(invocation.isVerified()).thenReturn(false);
        when(candidate.isVerified()).thenReturn(true);
        
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
        
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(new Object());
        when(candidate.getMock()).thenReturn(new Object());
        
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
        
        when(candidate.getMock()).thenReturn(invocation.getMock());
        when(invocation.argumentsMatch(candidate.getArguments())).thenReturn(true);
        
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSameMethod() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        
        when(invocation.getMethod()).thenReturn(method1);
        when(invocation.getMethod()).thenReturn(method2);
        
        assertTrue(invocationMatcher.hasSameMethod(invocation));
        assertFalse(invocationMatcher.hasSameMethod(mock(Invocation.class)));
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Matcher matcher1 = mock(Matcher.class);
        Matcher matcher2 = mock(Matcher.class);
        
        List<Matcher> matchers = Arrays.asList(matcher1, matcher2);
        
        when(invocationMatcher.getMatchers()).thenReturn(matchers);
        
        ArgumentMatcherHolder argumentMatcherHolder = mock(ArgumentMatcherHolder.class);
        when(argumentMatcherHolder.captureFrom(any())).thenReturn(true);
        
        invocationMatcher.captureArgumentsFrom(mock(Invocation.class));
    }
    
    @Test
    public void testCreateFrom() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        
        List<Invocation> invocations = Arrays.asList(invocation1, invocation2);
        
        InvocationMatcher invocationMatcher1 = mock(InvocationMatcher.class);
        InvocationMatcher invocationMatcher2 = mock(InvocationMatcher.class);
        
        whenNew(InvocationMatcher.class).withArguments(invocation1).thenReturn(invocationMatcher1);
        whenNew(InvocationMatcher.class).withArguments(invocation2).thenReturn(invocationMatcher2);
        
        List<InvocationMatcher> expectedMatchers = Arrays.asList(invocationMatcher1, invocationMatcher2);
        
        List<InvocationMatcher> matchers = InvocationMatcher.createFrom(invocations);
        
        assertEquals(expectedMatchers, matchers);
    }

}
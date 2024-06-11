package org.mockito.internal.invocation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.matchers.CapturesArguments;

public class InvocationMatcherTest {
    
    private Invocation invocation;
    private List<Matcher> matchers;
    private InvocationMatcher invocationMatcher;
    
    @Before
    public void setUp() {
        invocation = mock(Invocation.class);
        matchers = new ArrayList<>();
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }
    
    @Test
    public void testConstructorWithInvocation() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(Collections.emptyList(), invocationMatcher.getMatchers());
    }
    
    @Test
    public void testConstructorWithInvocationAndMatchers() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }
    
    @Test
    public void testGetMethod() {
        Method method = mock(Method.class);
        Mockito.when(invocation.getMethod()).thenReturn(method);
        
        assertEquals(method, invocationMatcher.getMethod());
    }
    
    @Test
    public void testToString() {
        String expected = "InvocationMatcher toString";
        Mockito.when(invocation.toString(matchers, new PrintSettings())).thenReturn(expected);
        
        assertEquals(expected, invocationMatcher.toString());
    }
    
    @Test
    public void testMatches() {
        Invocation actualInvocation = mock(Invocation.class);
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        Mockito.when(invocation.getMock()).thenReturn(actualInvocation.getMock());
        Mockito.when(invocation.getArguments()).thenReturn(new Object[] {});
        Mockito.when(actualInvocation.getArguments()).thenReturn(new Object[] {});
        Mockito.when(argumentsComparator.argumentsMatch(invocationMatcher, actualInvocation)).thenReturn(true);
        
        assertTrue(invocationMatcher.matches(actualInvocation));
    }
    
    @Test
    public void testMatchesWithDifferentMocks() {
        Invocation actualInvocation = mock(Invocation.class);
        Mockito.when(invocation.getMock()).thenReturn(mock(Object.class));
        Mockito.when(actualInvocation.getMock()).thenReturn(mock(Object.class));
        
        assertFalse(invocationMatcher.matches(actualInvocation));
    }
    
    @Test
    public void testMatchesWithDifferentMethods() {
        Invocation actualInvocation = mock(Invocation.class);
        Mockito.when(invocation.getMock()).thenReturn(actualInvocation.getMock());
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        Mockito.when(invocation.getMethod()).thenReturn(method1);
        Mockito.when(actualInvocation.getMethod()).thenReturn(method2);
        
        assertFalse(invocationMatcher.matches(actualInvocation));
    }
    
    @Test
    public void testHasSimilarMethod() {
        Invocation candidate = mock(Invocation.class);
        Mockito.when(invocation.getMethod()).thenReturn(mock(Method.class));
        Mockito.when(candidate.getMethod()).thenReturn(invocation.getMethod());
        Mockito.when(candidate.isVerified()).thenReturn(false);
        Mockito.when(invocation.getMock()).thenReturn(mock(Object.class));
        Mockito.when(candidate.getMock()).thenReturn(invocation.getMock());
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        Mockito.when(argumentsComparator.argumentsMatch(invocationMatcher, candidate.getArguments())).thenReturn(true);
        
        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSimilarMethodWithDifferentMethods() {
        Invocation candidate = mock(Invocation.class);
        Mockito.when(invocation.getMethod()).thenReturn(mock(Method.class));
        Mockito.when(candidate.getMethod()).thenReturn(mock(Method.class));
        
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSimilarMethodWithVerifiedCandidate() {
        Invocation candidate = mock(Invocation.class);
        Mockito.when(candidate.isVerified()).thenReturn(true);
        
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSimilarMethodWithDifferentMocks() {
        Invocation candidate = mock(Invocation.class);
        Mockito.when(invocation.getMethod()).thenReturn(mock(Method.class));
        Mockito.when(candidate.getMethod()).thenReturn(invocation.getMethod());
        Mockito.when(invocation.getMock()).thenReturn(mock(Object.class));
        Mockito.when(candidate.getMock()).thenReturn(mock(Object.class));
        
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSimilarMethodWithOverloadedButSameArgs() {
        Invocation candidate = mock(Invocation.class);
        Mockito.when(invocation.getMethod()).thenReturn(mock(Method.class));
        Mockito.when(candidate.getMethod()).thenReturn(invocation.getMethod());
        Mockito.when(invocation.getArguments()).thenReturn(new Object[] {});
        Mockito.when(candidate.getArguments()).thenReturn(new Object[] {});
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        Mockito.when(argumentsComparator.argumentsMatch(invocationMatcher, candidate.getArguments())).thenReturn(true);
        
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSameMethod() {
        Invocation candidate = mock(Invocation.class);
        Mockito.when(invocation.getMethod()).thenReturn(mock(Method.class));
        Mockito.when(candidate.getMethod()).thenReturn(invocation.getMethod());
        
        assertTrue(invocationMatcher.hasSameMethod(candidate));
    }
    
    @Test
    public void testHasSameMethodWithDifferentMethods() {
        Invocation candidate = mock(Invocation.class);
        Mockito.when(invocation.getMethod()).thenReturn(mock(Method.class));
        Mockito.when(candidate.getMethod()).thenReturn(mock(Method.class));
        
        assertFalse(invocationMatcher.hasSameMethod(candidate));
    }
    
    @Test
    public void testGetLocation() {
        Location location = mock(Location.class);
        Mockito.when(invocation.getLocation()).thenReturn(location);
        
        assertEquals(location, invocationMatcher.getLocation());
    }
    
    @Test
    public void testToStringWithPrintSettings() {
        PrintSettings printSettings = mock(PrintSettings.class);
        String expected = "InvocationMatcher toString with PrintSettings";
        Mockito.when(invocation.toString(matchers, printSettings)).thenReturn(expected);
        
        assertEquals(expected, invocationMatcher.toString(printSettings));
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        Invocation i = mock(Invocation.class);
        Matcher matcher = mock(Matcher.class);
        CapturesArguments capturesArguments = mock(CapturesArguments.class);
        Object argument = new Object();
        Object[] arguments = {argument};
        
        matchers.add(matcher);
        Mockito.when(matcher instanceof CapturesArguments).thenReturn(true);
        Mockito.when(i.getArguments()).thenReturn(arguments);
        Mockito.when(matchers.size()).thenReturn(1);
        
        invocationMatcher.captureArgumentsFrom(i);

        Mockito.verify(capturesArguments).captureFrom(argument);
    }
}
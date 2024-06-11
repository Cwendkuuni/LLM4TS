package org.mockito.internal.invocation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

public class InvocationMatcherTest {
    private static final Invocation INVOCATION = mock(Invocation.class);
    private static final List<Matcher> MATCHERS = Collections.singletonList(mock(Matcher.class));
    
    private final InvocationMatcher invocationMatcher = new InvocationMatcher(INVOCATION, MATCHERS);
    
    @Test
    public void testConstructor() {
        assertSame(INVOCATION, invocationMatcher.getInvocation());
        assertSame(MATCHERS, invocationMatcher.getMatchers());
    }
    
    @Test
    public void testConstructor_withEmptyMatchers() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getArguments()).thenReturn(new Object[] {1, 2, 3});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.emptyList());
        
        List<Matcher> expectedMatchers = Arrays.asList(eq(1), eq(2), eq(3));
        
        assertSame(invocation, invocationMatcher.getInvocation());
        assertEquals(expectedMatchers, invocationMatcher.getMatchers());
    }
    
    @Test
    public void testGetMethod() throws NoSuchMethodException {
        Method method = Object.class.getMethod("hashCode");
        when(INVOCATION.getMethod()).thenReturn(method);
        
        assertSame(method, invocationMatcher.getMethod());
    }
    
    @Test
    public void testToString() {
        MatchPrinter printer = mock(MatchPrinter.class);
        when(printer.print(MATCHERS, INVOCATION)).thenReturn("toString result");
        
        PrintSettings printSettings = new PrintSettings();
        printSettings.matchPrinter = printer;
        
        assertEquals("toString result", invocationMatcher.toString());
    }
    
    @Test
    public void testMatches() {
        Invocation actualInvocation = mock(Invocation.class);
        when(actualInvocation.getMock()).thenReturn(mock(Object.class));
        
        assertFalse(invocationMatcher.matches(actualInvocation));
    }
    
    @Test
    public void testHasSimilarMethod() throws NoSuchMethodException {
        Invocation candidateInvocation = mock(Invocation.class);
        when(candidateInvocation.getMethod()).thenReturn(Object.class.getMethod("hashCode"));
        when(candidateInvocation.isVerified()).thenReturn(false);
        when(candidateInvocation.getMock()).thenReturn(mock(Object.class));
        when(candidateInvocation.getArguments()).thenReturn(new Object[] {1, 2, 3});
        
        assertTrue(invocationMatcher.hasSimilarMethod(candidateInvocation));
    }
    
    @Test
    public void testHasSimilarMethod_withOverloadedButSameArgs() throws NoSuchMethodException {
        Invocation candidateInvocation = mock(Invocation.class);
        when(candidateInvocation.getMethod()).thenReturn(Object.class.getMethod("hashCode"));
        when(candidateInvocation.isVerified()).thenReturn(false);
        when(candidateInvocation.getMock()).thenReturn(mock(Object.class));
        when(candidateInvocation.getArguments()).thenReturn(new Object[] {1, 2, 3});
        
        assertFalse(invocationMatcher.hasSimilarMethod(candidateInvocation));
    }
    
    @Test
    public void testHasSameMethod() throws NoSuchMethodException {
        Invocation candidateInvocation = mock(Invocation.class);
        when(candidateInvocation.getMethod()).thenReturn(Object.class.getMethod("hashCode"));
        
        assertTrue(invocationMatcher.hasSameMethod(candidateInvocation));
    }
    
    @Test
    public void testHasSameMethod_withDifferentMethodName() throws NoSuchMethodException {
        Invocation candidateInvocation = mock(Invocation.class);
        when(candidateInvocation.getMethod()).thenReturn(Object.class.getMethod("equals", Object.class));
        
        assertFalse(invocationMatcher.hasSameMethod(candidateInvocation));
    }
    
    @Test
    public void testHasSameMethod_withDifferentMethodParameters() throws NoSuchMethodException {
        Invocation candidateInvocation = mock(Invocation.class);
        when(candidateInvocation.getMethod()).thenReturn(Object.class.getMethod("equals", Object.class));
        
        assertFalse(invocationMatcher.hasSameMethod(candidateInvocation));
    }
    
    @Test
    public void testGetLocation() {
        Location location = mock(Location.class);
        when(INVOCATION.getLocation()).thenReturn(location);
        
        assertSame(location, invocationMatcher.getLocation());
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(Object.class.getMethods()[0]); // any method
        
        invocationMatcher.captureArgumentsFrom(invocation);
        
        verifyZeroInteractions(invocation);
    }
    
    @Test
    public void testCreateFrom() {
        Invocation invocation = mock(Invocation.class);
        List<Invocation> invocations = Collections.singletonList(invocation);
        
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        
        assertEquals(invocations.size(), invocationMatchers.size());
        assertSame(invocation, invocationMatchers.get(0).getInvocation());
    }
}
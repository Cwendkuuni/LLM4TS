package org.mockito.internal.invocation;

import org.hamcrest.*;
import org.junit.*;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.reporting.PrintSettings;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherConstructor_WithMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(mock(Matcher.class), mock(Matcher.class));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }
    
    @Test
    public void testInvocationMatcherConstructor_WithoutMatchers() {
        Invocation invocation = mock(Invocation.class);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(Collections.emptyList(), invocationMatcher.getMatchers());
    }
    
    @Test
    public void testGetMethod() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertEquals(method, invocationMatcher.getMethod());
    }
    
    @Test
    public void testMatches_WithEqualInvocations() {
        Invocation invocation1 = mock(Invocation.class);
       Invocation invocation2 = mock(Invocation.class);
        
        when(invocation1.getMock()).thenReturn("mock1");
        when(invocation2.getMock()).thenReturn("mock1");
        when(invocation1.getMethod()).thenReturn(mock(Method.class));
        when(invocation2.getMethod()).thenReturn(mock(Method.class));
        when(invocation1.isVerified()).thenReturn(true);
        when(invocation2.isVerified()).thenReturn(false);
        when(invocation1.getArguments()).thenReturn(new Object[] {1, 2, 3});
        when(invocation2.getArguments()).thenReturn(new Object[] {1, 2, 3});
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        
        assertTrue(invocationMatcher.matches(invocation2));
    }
    
    @Test
    public void testMatches_WithUnequalInvocations() {
        Invocation invocation1 = mock(Invocation.class);
       Invocation invocation2 = mock(Invocation.class);
        
        when(invocation1.getMock()).thenReturn("mock1");
        when(invocation2.getMock()).thenReturn("mock2");
        when(invocation1.getMethod()).thenReturn(mock(Method.class));
        when(invocation2.getMethod()).thenReturn(mock(Method.class));
        when(invocation1.isVerified()).thenReturn(true);
        when(invocation2.isVerified()).thenReturn(false);
        when(invocation1.getArguments()).thenReturn(new Object[] {1, 2, 3});
        when(invocation2.getArguments()).thenReturn(new Object[] {4, 5, 6});
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        
        assertFalse(invocationMatcher.matches(invocation2));
    }
    
    @Test
    public void testHasSimilarMethod_WithSameMethod() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.isVerified()).thenReturn(false);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertTrue(invocationMatcher.hasSimilarMethod(invocation));
    }
    
    @Test
    public void testHasSimilarMethod_WithDifferentMethod() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        
        when(invocation1.getMethod()).thenReturn(mock(Method.class));
        when(invocation2.getMethod()).thenReturn(mock(Method.class));
        when(invocation1.isVerified()).thenReturn(false);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        
        assertFalse(invocationMatcher.hasSimilarMethod(invocation2));
    }
    
    @Test
    public void testGetLocation() {
        Invocation invocation = mock(Invocation.class);
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertEquals(location, invocationMatcher.getLocation());
    }
    
    @Test
    public void testToString_WithDefaultPrintSettings() {
        Invocation invocation = mock(Invocation.class);
        PrintSettings printSettings = new PrintSettings();
        when(invocation.toString(any(List.class), eq(printSettings))).thenReturn("Invocation String");
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertEquals("Invocation String", invocationMatcher.toString());
    }
    
    @Test
    public void testToString_WithCustomPrintSettings() {
        Invocation invocation = mock(Invocation.class);
        PrintSettings printSettings = new PrintSettings();
        when(invocation.toString(any(List.class), eq(printSettings))).thenReturn("Invocation String");
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertEquals("Invocation String", invocationMatcher.toString(printSettings));
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        Object argument1 = new Object();
        Object argument2 = new Object();
        Matcher matcher1 = mock(Matcher.class);
        Matcher matcher2 = mock(Matcher.class);
        
        when(invocation.getArguments()).thenReturn(new Object[]{argument1, argument2});
        when(matcher1 instanceof CapturesArguments).thenReturn(true);
        when(matcher2 instanceof CapturesArguments).thenReturn(false);
        
        List<Matcher> matchers = Arrays.asList(matcher1, matcher2);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        invocationMatcher.captureArgumentsFrom(invocation);
        
        verify((CapturesArguments) matcher1).captureFrom(argument1);
        verify((CapturesArguments) matcher2, never()).captureFrom(any());
    }
}
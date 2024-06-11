package org.mockito.internal.invocation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationOnMock;

@SuppressWarnings("unchecked")
public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherWithMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }
    
    @Test
    public void testInvocationMatcherWithoutMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(mock(Matcher.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }
    
    @Test
    public void testGetMethod() {
        Method method = mock(Method.class);
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(method);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertEquals(method, invocationMatcher.getMethod());
    }
    
    @Test
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();        
        PrintSettings printSettings = mock(PrintSettings.class);
        
        when(invocation.toString(matchers, printSettings)).thenReturn("invocationString");
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertEquals("invocationString", invocationMatcher.toString());
    }
    
    @Test
    public void testMatches() {
        Invocation invocation = mock(Invocation.class);
        Invocation actual = mock(Invocation.class);
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        List<Matcher> matchers = new ArrayList<>();
        
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(actual.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(argumentsComparator.argumentsMatch(any(), any())).thenReturn(true);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertTrue(invocationMatcher.matches(actual));
    }
    
    @Test
    public void testDoesNotMatch() {
        Invocation invocation = mock(Invocation.class);
        Invocation actual = mock(Invocation.class);
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        List<Matcher> matchers = new ArrayList<>();
        
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(actual.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(argumentsComparator.argumentsMatch(any(), any())).thenReturn(false);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertFalse(invocationMatcher.matches(actual));
    }
    
    @Test
    public void testSafelyArgumentsMatch() {
        Invocation invocation = mock(Invocation.class);
        Object[] actualArgs = new Object[1];
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        
        when(argumentsComparator.argumentsMatch(any(), any())).thenReturn(true);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertTrue(invocationMatcher.safelyArgumentsMatch(actualArgs));
    }
    
    @Test
    public void testDoesNotSafelyArgumentsMatch() {
        Invocation invocation = mock(Invocation.class);
        Object[] actualArgs = new Object[1];
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        
        when(argumentsComparator.argumentsMatch(any(), any())).thenThrow(new RuntimeException());
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertFalse(invocationMatcher.safelyArgumentsMatch(actualArgs));
    }
    
    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        List<Matcher> matchers = new ArrayList<>();
        
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(candidate.getMock()).thenReturn(mock(Object.class));
        when(argumentsComparator.argumentsMatch(any(), any())).thenReturn(true);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testDoesNotHaveSimilarMethod() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        List<Matcher> matchers = new ArrayList<>();
        
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(candidate.isVerified()).thenReturn(true);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(candidate.getMock()).thenReturn(mock(Object.class));
        when(argumentsComparator.argumentsMatch(any(), any())).thenReturn(true);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSameMethod() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        Method method = mock(Method.class);
        
        when(invocation.getMethod()).thenReturn(method);
        when(candidate.getMethod()).thenReturn(method);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertTrue(invocationMatcher.hasSameMethod(candidate));
    }
    
    @Test
    public void testDoesNotHaveSameMethod() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        
        when(invocation.getMethod()).thenReturn(method1);
        when(candidate.getMethod()).thenReturn(method2);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertFalse(invocationMatcher.hasSameMethod(candidate));
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
    public void testToStringWithPrintSettings() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();        
        PrintSettings printSettings = mock(PrintSettings.class);
        
        when(invocation.toString(matchers, printSettings)).thenReturn("invocationString");
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertEquals("invocationString", invocationMatcher.toString(printSettings));
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        Invocation i = mock(Invocation.class);
        Object[] arguments = new Object[] {};
        Matcher matcher = mock(Matcher.class);
        CapturesArguments capturesArguments = mock(CapturesArguments.class);
        
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(matcher);
        
        when(invocation.getArguments()).thenReturn(arguments);
        when(matcher instanceof CapturesArguments).thenReturn(true);
        when(matcher).thenReturn(capturesArguments);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        invocationMatcher.captureArgumentsFrom(i);
        
        verify(capturesArguments).captureFrom(arguments[0]);
    }
}
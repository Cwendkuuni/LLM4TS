
package org.mockito.internal.invocation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class InvocationMatcherTest {
    
    @Test
    public void testGetMethod() {
        // Create a mock Invocation object
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        // Set up the mock Invocation object
        when(invocation.getMethod()).thenReturn(method);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertEquals(method, invocationMatcher.getMethod());
    }
    
    @Test
    public void testGetInvocation() {
        // Create a mock Invocation object
        Invocation invocation = mock(Invocation.class);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertEquals(invocation, invocationMatcher.getInvocation());
    }
    
    @Test
    public void testGetMatchers() {
        // Create a mock Invocation object
        Invocation invocation = mock(Invocation.class);
        // Create a list of matchers
        List<Matcher> matchers = Collections.singletonList(mock(Matcher.class));
        // Set up the mock Invocation object
        when(invocation.argumentsToMatchers()).thenReturn(matchers);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertEquals(matchers, invocationMatcher.getMatchers());
    }
    
    @Test
    public void testToString() {
        // Create a mock Invocation object
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Collections.singletonList(mock(Matcher.class));
        PrintSettings printSettings = mock(PrintSettings.class);
        // Set up the mock Invocation object
        when(invocation.toString(matchers, printSettings)).thenReturn("InvocationMatcher toString");
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertEquals("InvocationMatcher toString", invocationMatcher.toString(printSettings));
    }
    
    @Test
    public void testMatches() {
        // Create mock Invocation objects
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        Object[] arguments = {1, 2, 3};
        // Set up the mock Invocation objects
        when(invocation1.getMock()).thenReturn(mock(Object.class));
        when(invocation2.getMock()).thenReturn(mock(Object.class));
        when(invocation1.getMethod()).thenReturn(mock(Method.class));
        when(invocation2.getMethod()).thenReturn(mock(Method.class));
        when(invocation1.getArguments()).thenReturn(arguments);
        when(invocation2.getArguments()).thenReturn(arguments);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        
        assertTrue(invocationMatcher.matches(invocation2));
    }
    
    @Test
    public void testSafelyArgumentsMatch() {
        // Create mock Invocation object
        Invocation invocation = mock(Invocation.class);
        // Create an array of arguments
        Object[] arguments = {1, 2, 3};
        // Set up the mock Invocation object
        when(invocation.getArguments()).thenReturn(arguments);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertTrue(invocationMatcher.safelyArgumentsMatch(arguments));
    }
    
    @Test
    public void testHasSimilarMethod() {
        // Create mock Invocation objects
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        Method method = mock(Method.class);
        // Set up the mock Invocation objects
        when(invocation1.getMethod()).thenReturn(method);
        when(invocation2.getMethod()).thenReturn(method);
        when(invocation1.isVerified()).thenReturn(false);
        when(invocation2.isVerified()).thenReturn(false);
        when(invocation1.getMock()).thenReturn(mock(Object.class));
        when(invocation2.getMock()).thenReturn(mock(Object.class));
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        
        assertTrue(invocationMatcher.hasSimilarMethod(invocation2));
    }
    
    @Test
    public void testHasSameMethod() {
        // Create mock Invocation objects
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        // Set up the mock Invocation objects
        when(invocation1.getMethod()).thenReturn(method1);
        when(invocation2.getMethod()).thenReturn(method2);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        
        assertFalse(invocationMatcher.hasSameMethod(invocation2));
    }
    
    @Test
    public void testGetLocation() {
        // Create a mock Invocation object
        Invocation invocation = mock(Invocation.class);
        Location location = mock(Location.class);
        // Set up the mock Invocation object
        when(invocation.getLocation()).thenReturn(location);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertEquals(location, invocationMatcher.getLocation());
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        // Create mock Invocation objects
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        Matcher matcher = mock(Matcher.class);
        // Set up the mock Invocation objects
        when(invocation1.getArguments()).thenReturn(new Object[] {1, 2, 3});
        when(invocation2.getArguments()).thenReturn(new Object[] {4, 5, 6});
        when(matcher instanceof CapturesArguments).thenReturn(true);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        
        invocationMatcher.captureArgumentsFrom(invocation2);
        
        verify((CapturesArguments) matcher).captureFrom(eq(4));
        verify((CapturesArguments) matcher).captureFrom(eq(5));
        verify((CapturesArguments) matcher).captureFrom(eq(6));
    }
    
    @Test
    public void testCreateFrom() {
        // Create a list of Invocation objects
        List<Invocation> invocations = new LinkedList<>();
        // Add mock Invocation objects to the list
        invocations.add(mock(Invocation.class));
        invocations.add(mock(Invocation.class));
        
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        
        assertEquals(2, invocationMatchers.size());
    }
}

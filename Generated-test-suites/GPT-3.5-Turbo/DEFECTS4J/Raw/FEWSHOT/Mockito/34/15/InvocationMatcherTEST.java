package org.mockito.internal.invocation;

import static org.junit.Assert.*;
import org.junit.Test;

public class InvocationMatcherTest {
    
    @Test
    public void testConstructorWithInvocationAndMatchers() {
        // Create mock objects
        Invocation invocation = new Invocation();
        List<Matcher> matchers = new ArrayList<Matcher>();
        
        // Create test object
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        // Verify constructor sets invocation and matchers correctly
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }
    
    @Test
    public void testConstructorWithInvocation() {
        // Create mock object
        Invocation invocation = new Invocation();
        
        // Create test object
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        // Verify constructor sets invocation correctly and uses argumentsToMatchers()
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(invocation.argumentsToMatchers(), invocationMatcher.getMatchers());
    }
    
    @Test
    public void testGetMethod() {
        // Create mock object
        Method method = new Method();
        Invocation invocation = new Invocation(method);
        
        // Create test object
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        // Verify getMethod() returns correct method
        assertEquals(method, invocationMatcher.getMethod());
    }
    
    @Test
    public void testToString() {
        // Create mock objects
        Invocation invocation = new Invocation();
        
        // Create test object
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        // Verify toString() returns correct string representation
        assertEquals(invocation.toString(invocationMatcher.getMatchers(), new PrintSettings()), invocationMatcher.toString());
    }
    
    @Test
    public void testMatches() {
        // Create mock objects
        Invocation invocationMock = new Invocation();
        Invocation actualMock = new Invocation();
        
        // Create test object
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocationMock);
        
        // Verify matches() returns true when method, mock, and arguments match
        assertTrue(invocationMatcher.matches(actualMock));
    }
    
    @Test
    public void testHasSimilarMethod() {
        // Create mock objects
        Invocation invocation = new Invocation();
        Invocation candidate = new Invocation();
        
        // Create test object
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        // Verify hasSimilarMethod() returns true when method name, unverified, and mock are the same
        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSameMethod() {
        // Create mock objects
        Method method = new Method();
        Invocation invocation = new Invocation(method);
        Invocation candidate = new Invocation(method);
        
        // Create test object
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        // Verify hasSameMethod() returns true when method is the same
        assertTrue(invocationMatcher.hasSameMethod(candidate));
    }
    
    @Test
    public void testGetLocation() {
        // Create mock objects
        Location location = new Location();
        Invocation invocation = new Invocation();
        invocation.setLocation(location);
        
        // Create test object
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        // Verify getLocation() returns correct location
        assertEquals(location, invocationMatcher.getLocation());
    }
    
    @Test
    public void testToStringWithPrintSettings() {
        // Create mock objects
        PrintSettings printSettings = new PrintSettings();
        Invocation invocation = new Invocation();
        
        // Create test object
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        // Verify toString() with PrintSettings returns correct string representation
        assertEquals(invocation.toString(invocationMatcher.getMatchers(), printSettings), invocationMatcher.toString(printSettings));
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        // Create mock objects
        Invocation invocation = new Invocation();
        Invocation i = new Invocation();
        Matcher matcher = new Matcher();
        matcher = new CapturesArguments();
        
        // Create test object
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        // Verify captureArgumentsFrom() captures arguments correctly
        invocationMatcher.captureArgumentsFrom(i);
        assertEquals(i.getArguments()[0], ((CapturesArguments) matcher).captureFrom(i.getArguments()[0]));    
    }
}
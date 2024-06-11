package org.mockito.internal.invocation;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class InvocationMatcherTest {
    
    @Test
    public void testInvocationMatcher() {
        Invocation invocation = new Invocation(); // Create a valid invocation object
        
        // Test for empty matchers list
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.emptyList());
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
        
        // Test for non-empty matchers list
        List<Matcher> matchers = new LinkedList<>(); // Create a list of matchers
        matchers.add(new Matcher()); // Add a valid matcher
        invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }
    
    @Test
    public void testGetMethod() {
        Invocation invocation = new Invocation(); // Create a valid invocation object
        Method method = new Method(); // Create a valid method object
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        invocationMatcher.getMethod(); // Should return null since invocation.getMethod() is not mocked
        
        invocation.setMethod(method); // Set valid method for invocation
        assertEquals(method, invocationMatcher.getMethod());
    }
    
    @Test
    public void testToString() {
        Invocation invocation = new Invocation(); // Create a valid invocation object
        
        PrintSettings printSettings = new PrintSettings(); // Create a valid printSettings object
        
        // Test for empty matchers list
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.emptyList());
        assertEquals(invocation.toString(invocationMatcher.getMatchers(), printSettings), invocationMatcher.toString(printSettings));
        
        // Test for non-empty matchers list
        List<Matcher> matchers = new LinkedList<>(); // Create a list of matchers
        matchers.add(new Matcher()); // Add a valid matcher
        invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertEquals(invocation.toString(invocationMatcher.getMatchers(), printSettings), invocationMatcher.toString(printSettings));
    }
    
    @Test
    public void testMatches() {
        Invocation invocation = new Invocation(); // Create a valid invocation object
        
        // Test for matching invocations
        Invocation actual = new Invocation(); // Create a matching invocation object
        assertTrue(invocation.matches(actual));
        
        // Test for non-matching invocations
        actual = new Invocation(); // Create a non-matching invocation object
        assertFalse(invocation.matches(actual));
    }
    
    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = new Invocation(); // Create a valid invocation object
        
        // Test for similar method invocation
        Invocation candidate = new Invocation(); // Create a similar method invocation object
        assertTrue(invocation.hasSimilarMethod(candidate));
        
        // Test for different method invocation
        candidate = new Invocation(); // Create a different method invocation object
        assertFalse(invocation.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSameMethod() {
        Invocation invocation = new Invocation(); // Create a valid invocation object
        
        // Test for same method invocation
        Invocation candidate = new Invocation(); // Create a same method invocation object
        assertTrue(invocation.hasSameMethod(candidate));
        
        // Test for different method invocation
        candidate = new Invocation(); // Create a different method invocation object
        assertFalse(invocation.hasSameMethod(candidate));
    }
    
    @Test
    public void testGetLoaction() {
        Invocation invocation = new Invocation(); // Create a valid invocation object
        Location location = new Location(); // Create a valid location object
        
        invocation.setLocation(location); // Set valid location for invocation
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(location, invocationMatcher.getLocation());
    }
    
    @Test
    public void testToStringWithPrintSettings() {
        Invocation invocation = new Invocation(); // Create a valid invocation object
        PrintSettings printSettings = new PrintSettings(); // Create a valid printSettings object
        
        // Test for empty matchers list
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.emptyList());
        assertEquals(invocation.toString(invocationMatcher.getMatchers(), printSettings), invocationMatcher.toString(printSettings));
        
        // Test for non-empty matchers list
        List<Matcher> matchers = new LinkedList<>(); // Create a list of matchers
        matchers.add(new Matcher()); // Add a valid matcher
        invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertEquals(invocation.toString(invocationMatcher.getMatchers(), printSettings), invocationMatcher.toString(printSettings));
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = new Invocation(); // Create a valid invocation object
        
        Invocation i = new Invocation(); // Create a valid invocation object
        Object[] arguments = new Object[] {1, 2, 3}; // Create a valid array of arguments
        
        // Create a valid matcher that captures arguments
        CapturesArguments matcher = new CapturesArguments();
        invocation.getMatchers().add(matcher);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        invocationMatcher.captureArgumentsFrom(i);
        
        assertArrayEquals(arguments, matcher.getCapturedArguments());
    }
    
    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new LinkedList<>(); // Create a list of invocations
        invocations.add(new Invocation()); // Add a valid invocation
        
        List<InvocationMatcher> expected = new LinkedList<>(); // Create the expected output list
        expected.add(new InvocationMatcher(invocations.get(0))); // Add a valid invocationMatcher
        
        List<InvocationMatcher> actual = InvocationMatcher.createFrom(invocations);
        assertEquals(expected, actual);
    }
}
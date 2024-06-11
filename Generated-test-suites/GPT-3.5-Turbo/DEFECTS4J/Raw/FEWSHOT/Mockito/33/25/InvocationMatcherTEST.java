package org.mockito.internal.invocation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;

public class InvocationMatcherTest {
    private Invocation invocation;
    private List<Matcher> matchers;
    
    @Before
    public void setUp() {
        invocation = new Invocation();
        matchers = new ArrayList<>();
    }
    
    @Test
    public void testConstructor_withMatchers() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }
    
    @Test
    public void testConstructor_withoutMatchers() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(invocation.argumentsToMatchers(), invocationMatcher.getMatchers());
    }
    
    @Test
    public void testGetMethod() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Method method = invocation.getMethod();
        
        assertEquals(method, invocationMatcher.getMethod());
    }
    
    @Test
    public void testToString() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        String expected = invocation.toString(invocation.argumentsToMatchers(), new PrintSettings());
        
        assertEquals(expected, invocationMatcher.toString());
    }
    
    @Test
    public void testMatches() {
        Invocation actualInvocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        boolean result = invocationMatcher.matches(actualInvocation);
        
        assertTrue(result);
    }
    
    @Test
    public void testMatches_notMatching() {
        Invocation actualInvocation = new Invocation();
        actualInvocation.setMock(new Object());
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        boolean result = invocationMatcher.matches(actualInvocation);
        
        assertFalse(result);
    }
    
    @Test
    public void testHasSimilarMethod() {
        Invocation candidate = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        boolean result = invocationMatcher.hasSimilarMethod(candidate);
        
        assertTrue(result);
    }
    
    @Test
    public void testHasSimilarMethod_invalid() {
        Invocation candidate = new Invocation();
        candidate.setVerified(true);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        boolean result = invocationMatcher.hasSimilarMethod(candidate);
        
        assertFalse(result);
    }
    
    @Test
    public void testHasSimilarMethod_overloadedButSameArgs() {
        Invocation candidate = new Invocation();
        candidate.setArguments(invocation.getArguments());
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        boolean result = invocationMatcher.hasSimilarMethod(candidate);
        
        assertFalse(result);
    }
    
    @Test
    public void testHasSameMethod() throws NoSuchMethodException {
        Invocation candidate = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        boolean result = invocationMatcher.hasSameMethod(candidate);
        
        assertTrue(result);
    }
    
    @Test
    public void testGetLocation() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Location location = invocation.getLocation();
        
        assertEquals(location, invocationMatcher.getLocation());
    }
    
    @Test
    public void testToString_withPrintSettings() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        String expected = invocation.toString(invocation.argumentsToMatchers(), new PrintSettings());
        PrintSettings printSettings = new PrintSettings();
        
        assertEquals(expected, invocationMatcher.toString(printSettings));
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation i = new Invocation();
        matchers.add(new CapturesArguments());
        
        invocationMatcher.captureArgumentsFrom(i);
        
        // Add assertions for capturing arguments
    }
    
    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new ArrayList<>();
        invocations.add(invocation);
        
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        
        assertEquals(1, invocationMatchers.size());
        assertEquals(invocation, invocationMatchers.get(0).getInvocation());
    }
}
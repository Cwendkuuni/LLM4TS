package org.mockito.internal.invocation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;

@SuppressWarnings("unchecked")
public class InvocationMatcherTest {
    @Test
    public void testInvocationMatcher_InvocationListMatcher() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = Collections.singletonList(new Matcher());
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);
        
        assertEquals(invocation, matcher.getInvocation());
        assertEquals(matchers, matcher.getMatchers());
    }
    
    @Test
    public void testInvocationMatcher_Invocation() {
        Invocation invocation = new Invocation();
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        
        assertEquals(invocation, matcher.getInvocation());
        assertNotNull(matcher.getMatchers());
        assertTrue(matcher.getMatchers().isEmpty());
    }
    
    @Test
    public void testGetMethod() {
        Invocation invocation = new Invocation();
        Method method = new Method();
        invocation.setMethod(method);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        
        assertEquals(method, matcher.getMethod());
    }
    
    @Test
    public void testToString() {
        Invocation invocation = new Invocation();
        PrintSettings printSettings = new PrintSettings();
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        
        assertEquals(invocation.toString(matcher.getMatchers(), printSettings), matcher.toString());
    }
    
    @Test
    public void testMatches() {
        Invocation invocation = new Invocation();
        Invocation actual = new Invocation();
        ArgumentsComparator argumentsComparator = new ArgumentsComparator();
        boolean expected = true;
        argumentsComparator.argumentsMatch(invocation, actual);
        
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        
        assertEquals(expected, matcher.matches(actual));
    }
    
    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = new Invocation();
        Invocation candidate = new Invocation();
        ArgumentsComparator argumentsComparator = new ArgumentsComparator();
        boolean methodNameEquals = true;
        boolean isUnverified = true;
        boolean mockIsTheSame = true;
        boolean methodEquals = true;
        boolean overloadedButSameArgs = false;
        
        argumentsComparator.argumentsMatch(invocation, candidate.getArguments());
        
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        
        assertEquals(methodNameEquals && isUnverified && mockIsTheSame && methodEquals && !overloadedButSameArgs, matcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSameMethod() {
        Invocation invocation = new Invocation();
        Invocation candidate = new Invocation();
        
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        
        assertTrue(matcher.hasSameMethod(invocation));
        assertFalse(matcher.hasSameMethod(candidate));
    }
    
    @Test
    public void testGetLocation() {
        Invocation invocation = new Invocation();
        Location location = new Location();
        invocation.setLocation(location);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        
        assertEquals(location, matcher.getLocation());
    }
    
    @Test
    public void testToString_PrintSettings() {
        Invocation invocation = new Invocation();
        PrintSettings printSettings = new PrintSettings();
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        
        assertEquals(invocation.toString(matcher.getMatchers(), printSettings), matcher.toString(printSettings));
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = new Invocation();
        CapturesArguments capturesArguments = new CapturesArguments();
        Invocation i = new Invocation();
        Object[] arguments = new Object[1];
        arguments[0] = new Object();
        i.setArguments(arguments);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        
        matcher.captureArgumentsFrom(i);
        
        assertNotNull(capturesArguments.getCapturedArguments());
        assertNotNull(capturesArguments.getCapturedArguments()[0]);
        assertEquals(arguments[0], capturesArguments.getCapturedArguments()[0]);
    }
}
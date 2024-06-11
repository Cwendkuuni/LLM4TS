package org.mockito.internal.invocation;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.lang.reflect.Array;
import java.util.*;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.matchers.MatcherDecorator;
import org.mockito.internal.matchers.VarargMatcher;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

@SuppressWarnings("unchecked")
public class InvocationMatcherTest {
    @Test
    public void testInvocationMatcher() {
        // Test constructor with matchers list
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(anyString());
        matchers.add(greaterThan(10));
        Invocation invocation = new Invocation("mock", "methodName", matchers, "returnType", new Object[]{"arg1", 20});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
        
        // Test constructor without matchers list
        Invocation invocation2 = new Invocation("mock2", "methodName2", Collections.emptyList(), "returnType", new Object[]{});
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);
        assertEquals(invocation2, invocationMatcher2.getInvocation());
        assertEquals(Collections.emptyList(), invocationMatcher2.getMatchers());
    }
    
    @Test
    public void testGetMethod() {
        List<Matcher> matchers = new ArrayList<>();
        Invocation invocation = new Invocation("mock", "methodName", matchers, "returnType", new Object[]{"arg1", 20});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertEquals("methodName", invocationMatcher.getMethod());
    }
    
    @Test
    public void testToString() {
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(anyInt());
        Invocation invocation = new Invocation("mock", "methodName", matchers, "returnType", new Object[]{5});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        String expected = "InvocationMatcher{matchers=[<any>] methodName(5)}";
        assertEquals(expected, invocationMatcher.toString());
    }

    @Test
    public void testMatches() {
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(eq("arg1"));
        Invocation invocation = new Invocation("mock", "methodName", matchers, "returnType", new Object[]{"arg1"});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        // Test invocation matching
        assertTrue(invocationMatcher.matches(invocation));
        
        // Test invocation with different arguments
        Invocation invocation2 = new Invocation("mock2", "methodName2", Collections.emptyList(), "returnType", new Object[]{});
        assertFalse(invocationMatcher.matches(invocation2));
        
        // Test invocation with different mock
        Invocation invocation3 = new Invocation("mock3", "methodName", Collections.emptyList(), "returnType", new Object[]{});
        assertFalse(invocationMatcher.matches(invocation3));
        
        // Test invocation with different method name
        Invocation invocation4 = new Invocation("mock", "methodName4", Collections.emptyList(), "returnType", new Object[]{});
        assertFalse(invocationMatcher.matches(invocation4));
    }
    
    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = new Invocation("mock", "methodName", Collections.emptyList(), "returnType", new Object[]{});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        // Test same method and same mock
        Invocation candidate = new Invocation("mock", "methodName", Collections.emptyList(), "returnType", new Object[]{});
        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
        
        // Test different method
        Invocation candidate2 = new Invocation("mock", "methodName2", Collections.emptyList(), "returnType", new Object[]{});
        assertFalse(invocationMatcher.hasSimilarMethod(candidate2));
        
        // Test different mock
        Invocation candidate3 = new Invocation("mock3", "methodName", Collections.emptyList(), "returnType", new Object[]{});
        assertFalse(invocationMatcher.hasSimilarMethod(candidate3));
        
        // Test method overloaded with same args
        Invocation candidate4 = new Invocation("mock", "methodName", Collections.emptyList(), "returnType", new Object[]{});
        assertFalse(invocationMatcher.hasSimilarMethod(candidate4));
    }
    
    @Test
    public void testHasSameMethod() {
        Invocation invocation = new Invocation("mock", "methodName", Collections.emptyList(), "returnType", new Object[]{});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        // Test same method
        Invocation candidate = new Invocation("mock", "methodName", Collections.emptyList(), "returnType", new Object[]{});
        assertTrue(invocationMatcher.hasSameMethod(candidate));
        
        // Test different method
        Invocation candidate2 = new Invocation("mock", "methodName2", Collections.emptyList(), "returnType", new Object[]{});
        assertFalse(invocationMatcher.hasSameMethod(candidate2));
        
        // Test different mock
        Invocation candidate3 = new Invocation("mock3", "methodName", Collections.emptyList(), "returnType", new Object[]{});
        assertFalse(invocationMatcher.hasSameMethod(candidate3));
    }
    
    @Test
    public void testGetLocation() {
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(eq("arg1"));
        Invocation invocation = new Invocation("mock", "methodName", matchers, "returnType", new Object[]{"arg1"});
        Location location = new Location("testClass", "testMethod", 123);
        invocation.setLocation(location);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertEquals(location, invocationMatcher.getLocation());
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(anyInt());
        Invocation invocation = new Invocation("mock", "methodName", matchers, "returnType", new Object[]{5});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        // Test capturing arguments
        Invocation invocation2 = new Invocation("mock2", "methodName2", Collections.emptyList(), "returnType", new Object[]{"arg1"});
        invocationMatcher.captureArgumentsFrom(invocation2);
        //assert captured arguments by using appropriate Matchers
        
        // Test capturing varargs
        Matcher varargMatcher = anyVararg();
        matchers.add(varargMatcher);
        Invocation invocation3 = new Invocation("mock2", "methodName2", matchers, "returnType", new Object[]{1,2,3});
        invocationMatcher.captureArgumentsFrom(invocation3);
        //assert captured arguments by using appropriate Matchers
    }
    
    @Test
    public void testCreateFrom() {
        Invocation invocation1 = new Invocation("mock", "methodName", Collections.emptyList(), "returnType", new Object[]{});
        Invocation invocation2 = new Invocation("mock2", "methodName2", Collections.emptyList(), "returnType", new Object[]{});
        List<Invocation> invocations = Arrays.asList(invocation1, invocation2);
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        assertEquals(2, invocationMatchers.size());
        assertEquals(invocation1, invocationMatchers.get(0).getInvocation());
        assertEquals(invocation2, invocationMatchers.get(1).getInvocation());
    }
}
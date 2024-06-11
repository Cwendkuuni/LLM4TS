
package org.mockito.internal.invocation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

public class InvocationMatcherTest {
    
    private Invocation invocation;
    private List<Matcher> matchers;
    
    @Before
    public void setup() {
        invocation = mock(Invocation.class);
        matchers = new ArrayList<>();
    }
    
    @Test
    public void testConstructorWithMatchersEmpty() {
        when(invocation.getArguments()).thenReturn(new Object[] {});
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }
    
    @Test
    public void testConstructorWithMatchersNotEmpty() {
        matchers.add(Mockito.any());
        when(invocation.getArguments()).thenReturn(new Object[] {});
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertFalse(invocationMatcher.getMatchers().isEmpty());
    }
    
    @Test
    public void testGetMethod() {
        Method method = InvocationMatcher.class.getDeclaredMethods()[0];
        when(invocation.getMethod()).thenReturn(method);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertEquals(method, invocationMatcher.getMethod());
    }
    
    @Test
    public void testGetInvocation() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertEquals(invocation, invocationMatcher.getInvocation());
    }
    
    @Test
    public void testToString() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertEquals("InvocationMatcher.toString() test case failed", "", invocationMatcher.toString());
    }
    
    @Test
    public void testMatches() {
        Invocation actual = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(actual.getMock());
        when(invocation.getMethod()).thenReturn(actual.getMethod());
        when(invocation.getArguments()).thenReturn(new Object[] {});
        when(actual.getArguments()).thenReturn(new Object[] {});
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertTrue(invocationMatcher.matches(actual));
    }
    
    @Test
    public void testSafelyArgumentsMatch() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertTrue(invocationMatcher.safelyArgumentsMatch(new Object[] {}));
    }
    
    @Test
    public void testHasSimilarMethod() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(candidate.getMethod());
        when(invocation.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(candidate.getMock());
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSameMethod() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(candidate.getMethod());
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertTrue(invocationMatcher.hasSameMethod(candidate));
    }
    
    @Test
    public void testGetLocation() {
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertEquals(location, invocationMatcher.getLocation());
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation1 = mock(Invocation.class);
        Object[] rawArguments = new Object[] {};
        when(invocation1.getMethod()).thenReturn(InvocationMatcher.class.getDeclaredMethods()[0]);
        when(invocation1.getArguments()).thenReturn(rawArguments);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        invocationMatcher.captureArgumentsFrom(invocation1);
        
        // assertion for capturing arguments
    }
    
    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new ArrayList<>();
        invocations.add(invocation);
        
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        
        assertEquals(1, invocationMatchers.size());
    }
}

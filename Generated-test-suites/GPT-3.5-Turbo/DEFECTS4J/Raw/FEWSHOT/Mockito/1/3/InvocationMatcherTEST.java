package org.mockito.internal.invocation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

@SuppressWarnings("unchecked")
public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherWithMatchers() throws NoSuchMethodException {
        Invocation invocation = mock(Invocation.class);
        Method method = String.class.getMethod("length");
        when(invocation.getMethod()).thenReturn(method);
        when(invocation.getArguments()).thenReturn(new Object[] {});
        
        Matcher matcher = mock(Matcher.class);
        List<Matcher> matchers = new ArrayList<>(Arrays.asList(matcher));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertEquals(method, invocationMatcher.getMethod());
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
        assertNull(invocationMatcher.getLocation());
    }
    
    @Test
    public void testInvocationMatcherWithoutMatchers() throws NoSuchMethodException {
        Invocation invocation = mock(Invocation.class);
        Method method = String.class.getMethod("length");
        when(invocation.getMethod()).thenReturn(method);
        when(invocation.getArguments()).thenReturn(new Object[] {});
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertEquals(method, invocationMatcher.getMethod());
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(Arrays.asList(), invocationMatcher.getMatchers());
        assertNull(invocationMatcher.getLocation());
    }
    
    @Test
    public void testToString() throws NoSuchMethodException {
        Invocation invocation = mock(Invocation.class);
        Method method = String.class.getMethod("length");
        when(invocation.getMethod()).thenReturn(method);
        when(invocation.getArguments()).thenReturn(new Object[] {});
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertNotNull(invocationMatcher.toString());
    }
    
    @Test
    public void testMatches() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(this);
        when(invocation.getArguments()).thenReturn(new Object[] {});
        
        Invocation actual = mock(Invocation.class);
        when(actual.getMock()).thenReturn(this);
        when(actual.getArguments()).thenReturn(new Object[] {});
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertTrue(invocationMatcher.matches(actual));
    }
    
    @Test
    public void testHasSimilarMethod() throws NoSuchMethodException {
        Invocation invocation = mock(Invocation.class);
        Method method = String.class.getMethod("length");
        when(invocation.getMethod()).thenReturn(method);
        when(invocation.getArguments()).thenReturn(new Object[] {});
        when(invocation.isVerified()).thenReturn(false);
        
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(method);
        when(candidate.getArguments()).thenReturn(new Object[] {});
        when(candidate.isVerified()).thenReturn(false);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertFalse(invocationMatcher.hasSimilarMethod(null));
        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSameMethod() throws NoSuchMethodException {
        Invocation invocation = mock(Invocation.class);
        Method method = String.class.getMethod("length");
        when(invocation.getMethod()).thenReturn(method);
        when(invocation.getArguments()).thenReturn(new Object[] {});
        
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(method);
        when(candidate.getArguments()).thenReturn(new Object[] {});
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertFalse(invocationMatcher.hasSameMethod(null));
        assertTrue(invocationMatcher.hasSameMethod(candidate));
    }
    
    @Test
    public void testCaptureArgumentsFrom() throws NoSuchMethodException {
        Invocation invocation = mock(Invocation.class);
        Method method = String.class.getMethod("length");
        when(invocation.getMethod()).thenReturn(method);
        when(invocation.getArguments()).thenReturn(new Object[] {});
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Invocation captureInvocation = mock(Invocation.class);
        when(captureInvocation.getMethod()).thenReturn(method);
        when(captureInvocation.getArgumentAt(0, Object.class)).thenReturn("test");
        
        invocationMatcher.captureArgumentsFrom(captureInvocation);
    }
    
    @Test
    public void testCreateFrom() throws NoSuchMethodException {
        Invocation invocation = mock(Invocation.class);
        Method method = String.class.getMethod("length");
        when(invocation.getMethod()).thenReturn(method);
        when(invocation.getArguments()).thenReturn(new Object[] {});
        
        List<Invocation> invocations = Arrays.asList(invocation);
        
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        
        assertEquals(1, invocationMatchers.size());
        assertEquals(invocation, invocationMatchers.get(0).getInvocation());
    }

}
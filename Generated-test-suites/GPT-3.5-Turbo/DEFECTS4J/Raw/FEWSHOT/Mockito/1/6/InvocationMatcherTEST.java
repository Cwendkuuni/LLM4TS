package org.mockito.internal.invocation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.invocation.Invocation;

public class InvocationMatcherTest {
    @Test
    public void testGetMethod() {
        Method expected = mock(Method.class);
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        invocationMatcher.getMethod();
        assertEquals(expected, invocationMatcher.getMethod());
    }
    
    @Test
    public void testGetInvocation() {
        Invocation expected = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<Matcher>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(expected, matchers);
        assertEquals(expected, invocationMatcher.getInvocation());
    }
    
    @Test
    public void testGetMatchers() {
        List<Matcher> expected = new ArrayList<Matcher>();
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(expected, invocationMatcher.getMatchers());
    }
    
    @Test
    public void testToString() {
        String expected = "";
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(expected, invocationMatcher.toString());
    }
    
    @Test
    public void testMatches() {
        Invocation invocation = mock(Invocation.class);
        Invocation actual = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertFalse(invocationMatcher.matches(actual));
    }
    
    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSameMethod() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertFalse(invocationMatcher.hasSameMethod(candidate));
    }
    
    @Test
    public void testGetLocation() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(invocation.getLocation(), invocationMatcher.getLocation());
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        invocationMatcher.captureArgumentsFrom(invocation);
    }
    
    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new ArrayList<Invocation>();
        invocations.add(mock(Invocation.class));
        List<InvocationMatcher> expected = new ArrayList<InvocationMatcher>();
        expected.add(new InvocationMatcher(mock(Invocation.class)));
        List<InvocationMatcher> actual = InvocationMatcher.createFrom(invocations);
        assertEquals(expected, actual);
    }
}
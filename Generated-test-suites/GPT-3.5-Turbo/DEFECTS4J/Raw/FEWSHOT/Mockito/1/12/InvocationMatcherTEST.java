package org.mockito.internal.invocation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import org.mockito.mock.MockCreationSettings;
import org.mockito.stubbing.Stubbing;

public class InvocationMatcherTest {
    
    private Invocation invocation;
    private List<Matcher> matchers;

    @Before
    public void setup() {
        invocation = mock(Invocation.class);
        matchers = Collections.emptyList();
    }
    
    @Test
    public void testConstructorWithMatchers() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }
    
    @Test
    public void testConstructorWithoutMatchers() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }
    
    @Test
    public void testGetMethod() {
        Method expectedMethod = mock(Method.class);
        Mockito.when(invocation.getMethod()).thenReturn(expectedMethod);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(expectedMethod, invocationMatcher.getMethod());
    }
    
    @Test
    public void testToString() {
        Matcher matcher = mock(Matcher.class);
        matchers.add(matcher);
        
        PrintSettings printSettings = mock(PrintSettings.class);
        Mockito.when(printSettings.print(matchers, invocation)).thenReturn("expected invocation");
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertEquals("expected invocation", invocationMatcher.toString());
    }
    
    @Test
    public void testMatchesWithSameInvocation() {
        Mockito.when(invocation.getMock()).thenReturn(mock(Object.class));
        Mockito.when(invocation.getArguments()).thenReturn(new Object[0]);
        
        Invocation actual = mock(Invocation.class);
        Mockito.when(actual.getMock()).thenReturn(mock(Object.class));
        Mockito.when(actual.getArguments()).thenReturn(new Object[0]);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertTrue(invocationMatcher.matches(actual));
    }
    
    @Test
    public void testMatchesWithDifferentInvocation() {
        Mockito.when(invocation.getMock()).thenReturn(mock(Object.class));
        Mockito.when(invocation.getArguments()).thenReturn(new Object[0]);
        
        Invocation actual = mock(Invocation.class);
        Mockito.when(actual.getMock()).thenReturn(mock(Object.class));
        Mockito.when(actual.getArguments()).thenReturn(new Object[1]);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertFalse(invocationMatcher.matches(actual));
    }
    
    @Test
    public void testHasSimilarMethodWithSimilarMethod() {
        Mockito.when(invocation.isVerified()).thenReturn(false);
        Mockito.when(invocation.getMethod()).thenReturn(mock(Method.class));
        Mockito.when(invocation.getMock()).thenReturn(mock(Object.class));
        Mockito.when(invocation.getArguments()).thenReturn(new Object[0]);
        
        Invocation candidate = mock(Invocation.class);
        Mockito.when(candidate.getMethod()).thenReturn(invocation.getMethod());
        Mockito.when(candidate.isVerified()).thenReturn(false);
        Mockito.when(candidate.getMock()).thenReturn(invocation.getMock());
        Mockito.when(candidate.getArguments()).thenReturn(invocation.getArguments());
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSimilarMethodWithOverloadedButSameArgsMethod() {
        Mockito.when(invocation.isVerified()).thenReturn(false);
        Mockito.when(invocation.getMethod()).thenReturn(mock(Method.class));
        Mockito.when(invocation.getMock()).thenReturn(mock(Object.class));
        Mockito.when(invocation.getArguments()).thenReturn(new Object[0]);
        
        Invocation candidate = mock(Invocation.class);
        Mockito.when(candidate.getMethod()).thenReturn(invocation.getMethod());
        Mockito.when(candidate.isVerified()).thenReturn(false);
        Mockito.when(candidate.getMock()).thenReturn(invocation.getMock());
        Mockito.when(candidate.getArguments()).thenReturn(new Object[0]);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSimilarMethodWithDifferentMethod() {
        Mockito.when(invocation.isVerified()).thenReturn(false);
        Mockito.when(invocation.getMethod()).thenReturn(mock(Method.class));
        Mockito.when(invocation.getMock()).thenReturn(mock(Object.class));
        Mockito.when(invocation.getArguments()).thenReturn(new Object[0]);
        
        Invocation candidate = mock(Invocation.class);
        Mockito.when(candidate.getMethod()).thenReturn(mock(Method.class));
        Mockito.when(candidate.isVerified()).thenReturn(false);
        Mockito.when(candidate.getMock()).thenReturn(invocation.getMock());
        Mockito.when(candidate.getArguments()).thenReturn(new Object[0]);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testGetLocation() {
        Location expectedLocation = mock(Location.class);
        Mockito.when(invocation.getLocation()).thenReturn(expectedLocation);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(expectedLocation, invocationMatcher.getLocation());
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        Mockito.when(invocation.getMethod()).thenReturn(mock(Method.class));
        Mockito.when(invocation.getRawArguments()).thenReturn(new Object[] { "arg1", "arg2" });
        
        Matcher matcher1 = mock(Matcher.class);
        Matcher matcher2 = mock(Matcher.class);
        matchers.add(matcher1);
        matchers.add(matcher2);
        
        CapturesArguments capturesArguments = mock(CapturesArguments.class);
        Mockito.when(matcher1 instanceof CapturesArguments).thenReturn(true);
        Mockito.when(matcher2 instanceof CapturesArguments).thenReturn(false);
        matchers.add(capturesArguments);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        invocationMatcher.captureArgumentsFrom(invocation);
        
        Mockito.verify(capturesArguments).captureFrom("arg1");
        Mockito.verify(matcher2, Mockito.never()).captureFrom(Mockito.anyObject());
    }
    
    @Test
    public void testCreateFrom() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<Invocation> invocations = Mockito.spy(Collections.singletonList(invocation1));
        
        LinkedList<InvocationMatcher> invocationMatchers = new LinkedList<InvocationMatcher>();
        InvocationMatcher expectedInvocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher expectedInvocationMatcher2 = new InvocationMatcher(invocation2);
        invocationMatchers.add(expectedInvocationMatcher1);
        invocationMatchers.add(expectedInvocationMatcher2);
        
        Mockito.when(invocations.get(0)).thenReturn(invocation1);
        Mockito.when(invocations.size()).thenReturn(2);
        Mockito.when(invocation1.getMethod()).thenReturn(mock(Method.class));
        Mockito.when(invocation2.getMethod()).thenReturn(mock(Method.class));
        
        List<InvocationMatcher> result = InvocationMatcher.createFrom(invocations);
        assertEquals(invocationMatchers, result);
    }
}
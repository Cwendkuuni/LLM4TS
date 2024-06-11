import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.hamcrest.Matcher;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class InvocationMatcherTest {

    @Test
    public void testConstructor() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Collections.emptyList();
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }
    
    @Test
    public void testConstructorWithInvocationOnly() {
        Invocation invocation = mock(Invocation.class);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(invocation.argumentsToMatchers(), invocationMatcher.getMatchers());
    }
    
    @Test
    public void testGetMethod() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertEquals(method, invocationMatcher.getMethod());
    }
    
    @Test
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Collections.emptyList();
        PrintSettings printSettings = mock(PrintSettings.class);
        String expectedString = "expected invocation string";
        when(invocation.toString(matchers, printSettings)).thenReturn(expectedString);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        String actualString = invocationMatcher.toString();
        
        assertEquals(expectedString, actualString);
    }
    
    @Test
    public void testMatches() {
        Invocation invocation = mock(Invocation.class);
        Invocation actualInvocation = mock(Invocation.class);
        Object[] arguments = new Object[]{1, 2, 3};
        when(invocation.getMock()).thenReturn("mock");
        when(invocation.getMethod()).thenReturn(Method.class);
        when(invocation.getArguments()).thenReturn(arguments);
        when(actualInvocation.getMock()).thenReturn("mock");
        when(actualInvocation.getMethod()).thenReturn(Method.class);
        when(actualInvocation.getArguments()).thenReturn(arguments);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertTrue(invocationMatcher.matches(actualInvocation));
    }
    
    // Test edge cases and exceptions
    
    @Test
    public void testHasSimilarMethodWithOverloadedButSameArgs() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation candidate = mock(Invocation.class);
        Object[] arguments = new Object[]{"arg1", "arg2"};
        when(invocationMatcher.getMethod().getName()).thenReturn("methodName");
        when(candidate.getMethod().getName()).thenReturn("methodName");
        when(candidate.isVerified()).thenReturn(false);
        when(invocationMatcher.getMethod()).thenReturn(Method.class);
        when(candidate.getMethod()).thenReturn(Method.class);
        when(invocationMatcher.hasSameMethod(candidate)).thenReturn(false);
        when(invocationMatcher.safelyArgumentsMatch(candidate.getArguments())).thenReturn(true);
        
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSimilarMethodWithNonMatchingMethodName() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation candidate = mock(Invocation.class);
        when(invocationMatcher.getMethod().getName()).thenReturn("methodName");
        when(candidate.getMethod().getName()).thenReturn("otherMethodName");
        when(invocationMatcher.getMethod()).thenReturn(Method.class);
        when(candidate.getMethod()).thenReturn(Method.class);
        
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSimilarMethodWithVerifiedCandidate() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation candidate = mock(Invocation.class);
        when(invocationMatcher.getMethod().getName()).thenReturn("methodName");
        when(candidate.getMethod().getName()).thenReturn("methodName");
        when(candidate.isVerified()).thenReturn(true);
        
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSimilarMethodWithDifferentMocks() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation candidate = mock(Invocation.class);
        when(invocationMatcher.getMethod().getName()).thenReturn("methodName");
        when(candidate.getMethod().getName()).thenReturn("methodName");
        when(candidate.isVerified()).thenReturn(false);
        when(invocationMatcher.getMethod()).thenReturn(Method.class);
        when(candidate.getMethod()).thenReturn(Method.class);
        when(candidate.getMock()).thenReturn("otherMock");
        
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSameMethod() {
    	InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
    	Invocation candidate = mock(Invocation.class);
    	Method method1 = mock(Method.class);
    	Method method2 = mock(Method.class);
    	when(invocation.getMethod()).thenReturn(method1);
    	when(candidate.getMethod()).thenReturn(method2);
    	when(method1.equals(method2)).thenReturn(true);
    	
    	assertTrue(invocationMatcher.hasSameMethod(candidate));
    }
    
    @Test
    public void testGetLocation() {
        Invocation invocation = mock(Invocation.class);
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertEquals(location, invocationMatcher.getLocation());
    }
    
    @Test
    public void testToStringWithPrintSettings() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Collections.emptyList();
        PrintSettings printSettings = mock(PrintSettings.class);
        String expectedString = "expected invocation string";
        when(invocation.toString(matchers, printSettings)).thenReturn(expectedString);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        String actualString = invocationMatcher.toString(printSettings);
        
        assertEquals(expectedString, actualString);
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        Invocation i = mock(Invocation.class);
        Object[] arguments = new Object[]{"arg1", "arg2"};
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(mock(Matcher.class));
        matchers.add(mock(Matcher.class));
        CapturesArguments capturesArguments = mock(CapturesArguments.class);
        when(invocation.getArguments()).thenReturn(arguments);
        when(i.getArguments()).thenReturn(arguments);
        when(mock(Matcher.class) instanceof CapturesArguments).thenReturn(true);
        when(matchers.get(0)).thenReturn(capturesArguments);
        when(matchers.get(1)).thenReturn(mock(Matcher.class));
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        invocationMatcher.captureArgumentsFrom(i);
        
        verify(capturesArguments, times(1)).captureFrom(arguments[0]);
        verify(capturesArguments, times(0)).captureFrom(arguments[1]);
    }
    
    @Test
    public void testCreateFrom() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<Invocation> invocations = new LinkedList<>();
        invocations.add(invocation1);
        invocations.add(invocation2);
        
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        
        assertEquals(2, invocationMatchers.size());
        assertTrue(invocationMatchers.get(0) instanceof InvocationMatcher);
        assertTrue(invocationMatchers.get(1) instanceof InvocationMatcher);
    }
}
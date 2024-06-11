import org.junit.Test;
import org.hamcrest.Matcher;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {
    
    @Test
    public void testConstructorWithMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(mock(Matcher.class));
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }
    
    @Test
    public void testConstructorWithoutMatchers() {
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
        PrintSettings printSettings = mock(PrintSettings.class);
        when(invocation.toString(anyList(), any(PrintSettings.class))).thenReturn("test");
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertEquals("test", invocationMatcher.toString());
        verify(printSettings, times(1)).argumentsMatch(any(InvocationMatcher.class), any(Invocation.class));
    }
    
    @Test
    public void testMatches() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        when(invocation1.getMock()).thenReturn(mock(Object.class));
        when(invocation2.getMock()).thenReturn(mock(Object.class));
        Method method = mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method);
        when(invocation2.getMethod()).thenReturn(method);
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        when(argumentsComparator.argumentsMatch(any(InvocationMatcher.class), any(Invocation.class))).thenReturn(true);
        whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(argumentsComparator);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        boolean result = invocationMatcher.matches(invocation2);
        
        assertTrue(result);
        verify(invocation1, times(1)).getMock();
        verify(invocation2, times(1)).getMock();
        verify(invocation1, times(1)).getMethod();
        verify(invocation2, times(1)).getMethod();
        verify(argumentsComparator, times(1)).argumentsMatch(any(InvocationMatcher.class), any(Invocation.class));
    }
    
    @Test
    public void testSafelyArgumentsMatch() {
        Invocation invocation = mock(Invocation.class);
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        when(argumentsComparator.argumentsMatch(any(InvocationMatcher.class), any(Object[].class))).thenReturn(true);
        whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(argumentsComparator);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean result = invocationMatcher.safelyArgumentsMatch(new Object[]{});
        
        assertTrue(result);
        verify(argumentsComparator, times(1)).argumentsMatch(any(InvocationMatcher.class), any(Object[].class));
    }
    
    @Test
    public void testHasSimilarMethod() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method);
        when(invocation2.getMethod()).thenReturn(method);
        when(invocation1.isVerified()).thenReturn(false);
        when(invocation2.isVerified()).thenReturn(false);
        when(invocation1.getMock()).thenReturn(mock(Object.class));
        when(invocation2.getMock()).thenReturn(mock(Object.class));
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        when(argumentsComparator.argumentsMatch(any(InvocationMatcher.class), any(Object[].class))).thenReturn(true);
        whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(argumentsComparator);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        boolean result = invocationMatcher.hasSimilarMethod(invocation2);
        
        assertTrue(result);
        verify(invocation1, times(1)).getMethod();
        verify(invocation2, times(1)).getMethod();
        verify(invocation1, times(1)).isVerified();
        verify(invocation2, times(1)).isVerified();
        verify(invocation1, times(1)).getMock();
        verify(invocation2, times(1)).getMock();
    }
    
    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method1);
        when(invocation2.getMethod()).thenReturn(method2);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        boolean result = invocationMatcher.hasSameMethod(invocation2);
        
        assertFalse(result);
        verify(invocation1, times(1)).getMethod();
        verify(invocation2, times(1)).getMethod();
    }
    
    @Test
    public void testGetLocation() {
        Invocation invocation = mock(Invocation.class);
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertEquals(location, invocationMatcher.getLocation());
        verify(invocation, times(1)).getLocation();
    }
    
    @Test
    public void testToStringWithPrintSettings() {
        Invocation invocation = mock(Invocation.class);
        PrintSettings printSettings = mock(PrintSettings.class);
        when(invocation.toString(anyList(), any(PrintSettings.class))).thenReturn("test");
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        String result = invocationMatcher.toString(printSettings);
        
        assertEquals("test", result);
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocationMatcherInvocation = mock(Invocation.class);
        Invocation i = mock(Invocation.class);
        Matcher matcher = mock(Matcher.class);
        Object arg1 = new Object();
        Object arg2 = new Object();
        Object[] args = new Object[]{arg1, arg2};
        when(invocationMatcherInvocation.getArguments()).thenReturn(new Object[]{matcher, matcher});
        when(i.getArguments()).thenReturn(args);
        when(matcher instanceof CapturesArguments).thenReturn(true);
        CapturesArguments capturesArguments = (CapturesArguments) matcher;
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocationMatcherInvocation);
        invocationMatcher.captureArgumentsFrom(i);
        
        verify(matcher, times(2)).captureFrom(any());
        verify(capturesArguments, times(1)).captureFrom(eq(arg1));
        verify(capturesArguments, times(1)).captureFrom(eq(arg2));
    }
    
    @Test
    public void testCreateFrom() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<Invocation> invocations = new ArrayList<>();
        invocations.add(invocation1);
        invocations.add(invocation2);
        
        List<InvocationMatcher> result = InvocationMatcher.createFrom(invocations);
        
        assertEquals(2, result.size());
        assertEquals(invocation1, result.get(0).getInvocation());
        assertEquals(invocation2, result.get(1).getInvocation());
    }
}
import org.junit.Test;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.debugging.Location;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    @Test
    public void testGetMethod() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = mock(List.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        Method method = invocationMatcher.getMethod();
        
        assertEquals(invocation.getMethod(), method);
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = mock(List.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        Invocation actualInvocation = invocationMatcher.getInvocation();
        
        assertEquals(invocation, actualInvocation);
    }
    
    @Test
    public void testGetMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = mock(List.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        List<Matcher> actualMatchers = invocationMatcher.getMatchers();
        
        assertEquals(matchers, actualMatchers);
    }
    
    @Test
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = mock(List.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        String actualToString = invocationMatcher.toString();
        
        assertNotNull(actualToString);
    }
    
    @Test
    public void testMatches() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = mock(List.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        Invocation actualInvocation = mock(Invocation.class);
        when(actualInvocation.getMock()).thenReturn(invocation.getMock());
        when(actualInvocation.getMethod()).thenReturn(invocation.getMethod());
        when(actualInvocation.getArguments()).thenReturn(invocation.getArguments());
        
        boolean matches = invocationMatcher.matches(actualInvocation);
        
        assertTrue(matches);
    }
    
    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = mock(List.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        Invocation candidate = mock(Invocation.class);
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMethod()).thenReturn(invocation.getMethod());
        when(candidate.getMock()).thenReturn(invocation.getMock());
        when(candidate.getArguments()).thenReturn(invocation.getArguments());
        
        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidate);
        
        assertTrue(hasSimilarMethod);
    }
    
    @Test
    public void testHasSameMethod() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = mock(List.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(invocation.getMethod());
        
        boolean hasSameMethod = invocationMatcher.hasSameMethod(candidate);
        
        assertTrue(hasSameMethod);
    }
    
    @Test
    public void testGetLocation() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = mock(List.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);
        
        Location actualLocation = invocationMatcher.getLocation();
        
        assertEquals(location, actualLocation);
    }
    
    @Test
    public void testToStringWithPrintSettings() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = mock(List.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        PrintSettings printSettings = mock(PrintSettings.class);
        
        invocationMatcher.toString(printSettings);
        
        verify(invocation).toString(matchers, printSettings);
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = mock(List.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        Invocation i = mock(Invocation.class);
        Object[] arguments = new Object[1];
        when(i.getArguments()).thenReturn(arguments);
        CapturesArguments capturesArguments = mock(CapturesArguments.class);
        when(matchers.get(anyInt())).thenReturn(capturesArguments);
        
        invocationMatcher.captureArgumentsFrom(i);
        
        verify(capturesArguments).captureFrom(arguments[0]);
    }
}
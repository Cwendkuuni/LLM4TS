import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.ArgumentsComparator;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;

@SuppressWarnings("unchecked")
public class InvocationMatcherTest {

    @Test
    public void testConstructor1() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Mockito.mock(List.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }
    
    @Test
    public void testConstructor2() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(Collections.emptyList(), invocationMatcher.getMatchers());
    }

    @Test
    public void testGetMethod() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Method method = Mockito.mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertEquals(method, invocationMatcher.getMethod());
    }
    
    @Test
    public void testMatches() {
        Invocation invocation = Mockito.mock(Invocation.class);
        when(invocation.getMock()).thenReturn("mock");
        
        Invocation actual = Mockito.mock(Invocation.class);
        when(actual.getMock()).thenReturn("mock");
        when(actual.getArguments()).thenReturn(new Object[]{});
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertTrue(invocationMatcher.matches(actual));
    }
    
    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = Mockito.mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(Mockito.mock(Method.class));
        when(invocation.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn("mock");
        when(invocation.getArguments()).thenReturn(new Object[]{});
        
        Invocation candidate = Mockito.mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(Mockito.mock(Method.class));
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn("mock");
        when(candidate.getArguments()).thenReturn(new Object[]{});
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSameMethod() {
        Invocation invocation = Mockito.mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(Mockito.mock(Method.class));
        
        Invocation candidate = Mockito.mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(Mockito.mock(Method.class));
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertTrue(invocationMatcher.hasSameMethod(candidate));
    }
    
    @Test
    public void testGetLocation() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Location location = Mockito.mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(location, invocationMatcher.getLocation());
    }
    
    @Test
    public void testToString() {
        Invocation invocation = Mockito.mock(Invocation.class);
        when(invocation.argumentsToMatchers()).thenReturn(Mockito.mock(List.class));
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        invocationMatcher.toString();
        verify(invocation).toString(anyList(), any(PrintSettings.class));
    }
    
    @Test
    public void testToStringWithSettings() {
        Invocation invocation = Mockito.mock(Invocation.class);
        when(invocation.argumentsToMatchers()).thenReturn(Mockito.mock(List.class));
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        invocationMatcher.toString(Mockito.mock(PrintSettings.class));
        verify(invocation).toString(anyList(), any(PrintSettings.class));
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        Invocation i = Mockito.mock(Invocation.class);
        when(i.getArguments()).thenReturn(new Object[]{1, 2});
        
        CapturesArguments capture = Mockito.mock(CapturesArguments.class);
        List<Matcher> matchers = Collections.singletonList(capture);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(i, matchers);
        invocationMatcher.captureArgumentsFrom(invocationMatcher);
        
        verify(capture).captureFrom(1);
        verify(capture).captureFrom(2);
    }
}
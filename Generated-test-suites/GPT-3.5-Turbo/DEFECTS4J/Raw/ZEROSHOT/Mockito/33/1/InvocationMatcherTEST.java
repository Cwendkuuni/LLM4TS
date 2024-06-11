import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.matchers.CapturesArguments;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<Matcher>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertNotNull(invocationMatcher);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }
    
    @Test
    public void testConstructorWithoutMatchers() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertNotNull(invocationMatcher);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
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
        List<Matcher> matchers = new ArrayList<Matcher>();
        PrintSettings printSettings = mock(PrintSettings.class);
        String expected = "InvocationMatcher.toString()";
        when(invocation.toString(matchers, printSettings)).thenReturn(expected);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertEquals(expected, invocationMatcher.toString());
    }
    
    @Test
    public void testMatches() {
        Invocation invocation = mock(Invocation.class);
        Object mock = new Object();
        when(invocation.getMock()).thenReturn(mock);
        when(invocation.getArguments()).thenReturn(new Object[0]);
        Invocation actual = mock(Invocation.class);
        when(actual.getMock()).thenReturn(mock);
        when(actual.getArguments()).thenReturn(new Object[0]);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertTrue(invocationMatcher.matches(actual));
    }
    
    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = mock(Invocation.class);
        Object mock = new Object();
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(mock);
        when(invocation.getArguments()).thenReturn(new Object[0]);
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn(mock);
        when(candidate.getArguments()).thenReturn(new Object[0]);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSimilarMethodWithOverloadedButSameArgs() {
        Invocation invocation = mock(Invocation.class);
        Object mock = new Object();
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getMethod().getName()).thenReturn("method1");
        when(invocation.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(mock);
        when(invocation.getArguments()).thenReturn(new Object[0]);
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod().getName()).thenReturn("method1");
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn(mock);
        when(candidate.getArguments()).thenReturn(invocation.getArguments());
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSameMethod() {
        Invocation invocation = mock(Invocation.class);
        Method method1 = mock(Method.class, "method1");
        when(invocation.getMethod()).thenReturn(method1);
        Invocation candidate = mock(Invocation.class);
        Method method2 = mock(Method.class, "method2");
        when(candidate.getMethod()).thenReturn(method2);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertFalse(invocationMatcher.hasSameMethod(candidate));
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
        List<Matcher> matchers = new ArrayList<Matcher>();
        PrintSettings printSettings = mock(PrintSettings.class);
        String expected = "InvocationMatcher.toString(printSettings)";
        when(invocation.toString(matchers, printSettings)).thenReturn(expected);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertEquals(expected, invocationMatcher.toString(printSettings));
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        Matcher matcher = mock(Matcher.class);
        when(matcher instanceof CapturesArguments).thenReturn(true);
        when(invocation.getArguments()).thenReturn(new Object[] {1, 2, 3});
        Invocation i = mock(Invocation.class);
        when(i.getArguments()).thenReturn(new Object[0]);
        List<Matcher> matchers = new ArrayList<Matcher>();
        matchers.add(matcher);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        invocationMatcher.captureArgumentsFrom(i);
        
        verify((CapturesArguments) matcher, times(1)).captureFrom(i.getArguments()[0]);
    }
    
    @Test
    public void testCreateFrom() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<Invocation> invocations = new ArrayList<Invocation>();
        invocations.add(invocation1);
        invocations.add(invocation2);
        
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        
        assertNotNull(invocationMatchers);
        assertEquals(2, invocationMatchers.size());
    }
}
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;

public class InvocationMatcherTest {
    
    @Test
    public void testInvocationMatcherConstructor() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        
        assertEquals(invocation, matcher.getInvocation());
        assertEquals(Collections.<Matcher>emptyList(), matcher.getMatchers());
    }
    
    @Test
    public void testInvocationMatcherConstructorWithMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<Matcher>();
        matchers.add(anyInt());
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);
        
        assertEquals(invocation, matcher.getInvocation());
        assertEquals(matchers, matcher.getMatchers());
    }
    
    @Test
    public void testGetMethod() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        
        assertEquals(method, matcher.getMethod());
    }
    
    @Test
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        
        assertNotNull(matcher.toString());
    }
    
    @Test
    public void testMatches() {
        Invocation invocation = mock(Invocation.class);
        Invocation actual = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(actual.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(actual.getMethod()).thenReturn(mock(Method.class));
        when(invocation.argumentsToMatchers()).thenReturn(Collections.<Matcher>emptyList());
        when(actual.getArguments()).thenReturn(new Object[] {});
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        
        assertTrue(matcher.matches(actual));
    }
    
    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(invocation.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(candidate.getMock()).thenReturn(mock(Object.class));
        when(invocation.argumentsMatch(any(Object[].class))).thenReturn(true);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        
        assertTrue(matcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSameMethod() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(candidate.getMethod()).thenReturn(method2);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        
        when(method1.equals(method2)).thenReturn(true);
        assertTrue(matcher.hasSameMethod(candidate));
        
        when(method1.equals(method2)).thenReturn(false);
        assertFalse(matcher.hasSameMethod(candidate));
    }
    
    @Test
    public void testGetLocation() {
        Invocation invocation = mock(Invocation.class);
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        
        assertEquals(location, matcher.getLocation());
    }
    
    @Test
    public void testToStringWithPrintSettings() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        PrintSettings printSettings = mock(PrintSettings.class);
        
        assertNotNull(matcher.toString(printSettings));
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        Invocation i = mock(Invocation.class);
        Matcher matcher = mock(Matcher.class);
        List<Matcher> matchers = new ArrayList<Matcher>();
        matchers.add(matcher);
        when(invocation.argumentsToMatchers()).thenReturn(matchers);
        when(i.getArguments()).thenReturn(new Object[] {1});
        CapabilityArguments capabilityArguments = mock(CapabilityArguments.class);
        when(matcher instanceof CapabilityArguments).thenReturn(true);
        when((CapabilityArguments) matcher).thenReturn(capabilityArguments);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        invocationMatcher.captureArgumentsFrom(i);
        
        verify(capabilityArguments).captureFrom(1);
    }
    
    @Test
    public void testCreateFrom() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<Invocation> invocations = new ArrayList<Invocation>();
        invocations.add(invocation1);
        invocations.add(invocation2);
        
        List<InvocationMatcher> matchers = InvocationMatcher.createFrom(invocations);

        assertEquals(2, matchers.size());
        assertEquals(invocation1, matchers.get(0).getInvocation());
        assertEquals(invocation2, matchers.get(1).getInvocation());
    }
}
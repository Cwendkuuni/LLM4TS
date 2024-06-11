import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.*;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.internal.invocation.ArgumentsComparator;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.invocation.Invocation;

@SuppressWarnings("unchecked")
public class InvocationMatcherTest {
  
    @Test
    public void testConstructor() throws Exception {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<Matcher>();
        matchers.add(mock(Matcher.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }
    
    @Test
    public void testEmptyConstructor() throws Exception {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }
    
    @Test
    public void testGetMethod() throws Exception {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertEquals(method, invocationMatcher.getMethod());
    }
    
    @Test
    public void testToString() throws Exception {
        Invocation invocation = mock(Invocation.class);
        PrintSettings printSettings = mock(PrintSettings.class);
        List<Matcher> matchers = new ArrayList<Matcher>();
        matchers.add(mock(Matcher.class));
        
        when(invocation.getInLocation()).thenReturn("");
        when(new PrintSettings().print(matchers, invocation)).thenReturn("InvocationMatcher");
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertEquals("InvocationMatcher", invocationMatcher.toString());
    }
    
    @Test
    public void testMatches() throws Exception {
        Invocation invocation = mock(Invocation.class);
        Invocation actual = mock(Invocation.class);
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(actual.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(actual.getMethod()).thenReturn(mock(Method.class));
        when(argumentsComparator.argumentsMatch(any(), any())).thenReturn(true);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertTrue(invocationMatcher.matches(actual));
    }
    
    @Test
    public void testSafelyArgumentsMatch() throws Exception {
        Invocation invocation = mock(Invocation.class);
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        
        when(new ArgumentsComparator().argumentsMatch(any(), any())).thenReturn(true);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertTrue(invocationMatcher.safelyArgumentsMatch(new Object[] {}));
    }
    
    @Test
    public void testHasSimilarMethod() throws Exception {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(argumentsComparator.argumentsMatch(any(), any())).thenReturn(true);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSameMethod() throws Exception {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        
        when(invocation.getMethod()).thenReturn(method1);
        when(candidate.getMethod()).thenReturn(method2);
        when(method1.getName()).thenReturn("method");
        when(method2.getName()).thenReturn("method");
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertTrue(invocationMatcher.hasSameMethod(candidate));
    }
    
    @Test
    public void testHasSameMethodNotEqual() throws Exception {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        
        when(invocation.getMethod()).thenReturn(method1);
        when(candidate.getMethod()).thenReturn(method2);
        when(method1.getName()).thenReturn("method1");
        when(method2.getName()).thenReturn("method2");
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertFalse(invocationMatcher.hasSameMethod(candidate));
    }
    
    @Test
    public void testGetLocation() throws Exception {
        Invocation invocation = mock(Invocation.class);
        Location location = mock(Location.class);
        
        when(invocation.getLocation()).thenReturn(location);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertEquals(location, invocationMatcher.getLocation());
    }
    
    @Test
    public void testCaptureArgumentsFrom() throws Exception {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Invocation invocation2 = mock(Invocation.class);
        when(invocation2.getMethod()).thenReturn(mock(Method.class));
        when(invocation2.getRawArguments()).thenReturn(new Object[] {});
        when(invocation2.getArgumentAt(0, Object.class)).thenReturn(mock(Object.class));
        
        invocationMatcher.captureArgumentsFrom(invocation2);
    }
    
    @Test
    public void testCreateFrom() throws Exception {
        Invocation invocation = mock(Invocation.class);
        List<Invocation> invocations = new ArrayList<Invocation>();
        invocations.add(invocation);
        
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        
        assertEquals(1, invocationMatchers.size());
        assertEquals(invocation, invocationMatchers.get(0).getInvocation());
    }
}
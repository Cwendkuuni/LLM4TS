
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.*;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.invocation.Invocation;

public class InvocationMatcherTest {

    // Test the constructor with matchers parameter
    @Test
    public void testInvocationMatcherWithMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);
        
        assertEquals(invocation, matcher.getInvocation());
        assertEquals(matchers, matcher.getMatchers());
    }
    
    // Test the constructor without matchers parameter
    @Test
    public void testInvocationMatcherWithoutMatchers() {
        Invocation invocation = mock(Invocation.class, RETURNS_DEEP_STUBS);
        ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        
        verify(invocation, times(1)).getArguments();
        verify(invocation, times(1)).getMock();
        verify(invocation, times(1)).getMethod();
        verify(invocation, times(1)).getLocation();
        verify(invocation, times(1)).isVerified();
        verify(invocation, times(1)).getArguments();
        verifyNoMoreInteractions(invocation);
        assertNull(matcher.getMatchers());

    }
    
    // Test the getMethod() method
    @Test
    public void testGetMethod() {
        Method method = mock(Method.class);
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(method);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        
        assertEquals(method, matcher.getMethod());
    }
    
    // Test the toString() method
    @Test
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getArguments()).thenReturn(new Object[] { "arg1", "arg2" });
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        
        assertEquals("", matcher.toString());
    }
    
    // Test the matches() method
    @Test
    public void testMatches() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation1);
        
        when(invocation1.getMock()).thenReturn(mock(Object.class));
        when(invocation2.getMock()).thenReturn(mock(Object.class));
        when(invocation1.getMethod()).thenReturn(mock(Method.class));
        when(invocation2.getMethod()).thenReturn(mock(Method.class));
        when(invocation2.getArguments()).thenReturn(new Object[] { "arg" });
        
        assertFalse(matcher.matches(invocation2));
        
        when(invocation1.getMock()).thenReturn(invocation2.getMock());
        when(invocation1.getMethod()).thenReturn(invocation2.getMethod());
        when(invocation1.getArguments()).thenReturn(invocation2.getArguments());
        
        assertTrue(matcher.matches(invocation2));
    }
    
    // Test the hasSimilarMethod() method
    @Test
    public void testHasSimilarMethod() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation1);
        
        when(invocation1.getMethod()).thenReturn(mock(Method.class));
        when(invocation2.getMethod()).thenReturn(mock(Method.class));
        when(invocation1.isVerified()).thenReturn(false);
        when(invocation2.isVerified()).thenReturn(true);
        when(invocation1.getMock()).thenReturn(mock(Object.class));
        when(invocation2.getMock()).thenReturn(mock(Object.class));
        when(invocation1.getArguments()).thenReturn(new Object[] { "arg1" });
        when(invocation2.getArguments()).thenReturn(new Object[] { "arg2" });
        
        assertFalse(matcher.hasSimilarMethod(invocation2));
        
        when(invocation1.getMethod().getName()).thenReturn("method");
        when(invocation2.getMethod().getName()).thenReturn("method");
        
        assertFalse(matcher.hasSimilarMethod(invocation2));
        
        when(invocation1.getMock()).thenReturn(invocation2.getMock());
        when(invocation1.getArguments()).thenReturn(invocation2.getArguments());
        
        assertFalse(matcher.hasSimilarMethod(invocation2));
        
        when(invocation1.isVerified()).thenReturn(true);
        
        assertFalse(matcher.hasSimilarMethod(invocation2));
        
        when(invocation1.isVerified()).thenReturn(false);
        when(invocation1.getMethod()).thenReturn(invocation2.getMethod());
        when(invocation1.getMock()).thenReturn(invocation2.getMock());
        
        assertTrue(matcher.hasSimilarMethod(invocation2));
    }
    
    // Test the hasSameMethod() method
    @Test
    public void testHasSameMethod() {
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation1);
        
        when(invocation1.getMethod()).thenReturn(method1);
        when(invocation2.getMethod()).thenReturn(method2);
        when(method1.getName()).thenReturn("method1");
        when(method2.getName()).thenReturn("method2");
        
        assertFalse(matcher.hasSameMethod(invocation2));
        
        when(method1.getName()).thenReturn("method");
        when(method2.getName()).thenReturn("method");
        when(method1.getParameterTypes()).thenReturn(new Class[] { String.class });
        when(method2.getParameterTypes()).thenReturn(new Class[] { Integer.class });
        
        assertFalse(matcher.hasSameMethod(invocation2));
        
        when(method1.getParameterTypes()).thenReturn(new Class[] { String.class });
        when(method2.getParameterTypes()).thenReturn(new Class[] { String.class });
        
        assertTrue(matcher.hasSameMethod(invocation2));
    }
    
    // Test the getLocation() method
    @Test
    public void testGetLocation() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getLocation()).thenReturn(mock(Location.class));
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        
        assertEquals(invocation.getLocation(), matcher.getLocation());
    }

    // Test the captureArgumentsFrom() method with varargs
    @Test
    public void testCaptureArgumentsFromVarargs() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getMethod().isVarArgs()).thenReturn(true);
        when(invocation.getRawArguments()).thenReturn(new Object[] { "arg1", "arg2" });
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        
        matcher.captureArgumentsFrom(invocation);

        verify(invocation, times(1)).getMethod();
        verify(invocation, times(1)).getRawArguments();
        verifyNoMoreInteractions(invocation);
    }
    
    // Test the captureArgumentsFrom() method without varargs
    @Test
    public void testCaptureArgumentsFromNoVarargs() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getMethod().isVarArgs()).thenReturn(false);
        when(invocation.getArguments()).thenReturn(new Object[] { "arg1", "arg2" });
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        
        matcher.captureArgumentsFrom(invocation);

        verify(invocation, times(1)).getMethod();
        verify(invocation, times(1)).getArguments();
        verifyNoMoreInteractions(invocation);
    }

    // Test createFrom() method
    @Test
    public void testCreateFrom() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<Invocation> invocations = Arrays.asList(invocation1, invocation2);
        
        List<InvocationMatcher> matchers = InvocationMatcher.createFrom(invocations);
        
        assertEquals(2, matchers.size());
        assertTrue(matchers.get(0).getInvocation().equals(invocation1));
        assertTrue(matchers.get(1).getInvocation().equals(invocation2));
    }
}

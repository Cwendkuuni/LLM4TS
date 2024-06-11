import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.invocation.Invocation;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(mock(Matcher.class), mock(Matcher.class));
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertSame(invocation, invocationMatcher.getInvocation());
        assertSame(matchers, invocationMatcher.getMatchers());
    }
    
    @Test
    public void testConstructorWithoutMatchers() {
        Invocation invocation = mock(Invocation.class);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertSame(invocation, invocationMatcher.getInvocation());
        assertSame(Collections.emptyList(), invocationMatcher.getMatchers());
    }
    
    @Test
    public void testGetMethod() {
        Method method = mock(Method.class);
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(method);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertSame(method, invocationMatcher.getMethod());
    }
    
    @Test
    public void testToString() {
        List<Matcher> matchers = Arrays.asList(mock(Matcher.class), mock(Matcher.class));
        Invocation invocation = mock(Invocation.class);
        when(invocation.getArguments()).thenReturn(new Object[] { "arg1", "arg2" });
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        String expected = "Matchers:\n" +
                "arg1\n" +
                "arg2";
        assertEquals(expected, invocationMatcher.toString());
    }
    
    @Test
    public void testMatches() {
        Object mock = mock(Object.class);
        Method method = mock(Method.class);
        Object[] args1 = { "arg1", "arg2" };
        Object[] args2 = { "arg3", "arg4" };
        
        Invocation invocation1 = mock(Invocation.class);
        when(invocation1.getMock()).thenReturn(mock);
        when(invocation1.getMethod()).thenReturn(method);
        when(invocation1.getArguments()).thenReturn(args1);
        
        Invocation invocation2 = mock(Invocation.class);
        when(invocation2.getMock()).thenReturn(mock);
        when(invocation2.getMethod()).thenReturn(method);
        when(invocation2.getArguments()).thenReturn(args2);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        
        assertTrue(invocationMatcher.matches(invocation1));
        assertFalse(invocationMatcher.matches(invocation2));
    }
    
    @Test
    public void testHasSimilarMethod() {
        Object mock = mock(Object.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        Object[] args1 = { "arg1", "arg2" };
        Object[] args2 = { "arg3", "arg4" };
        
        Invocation invocation1 = mock(Invocation.class);
        when(invocation1.isVerified()).thenReturn(false);
        when(invocation1.getMock()).thenReturn(mock);
        when(invocation1.getMethod()).thenReturn(method1);
        when(invocation1.getArguments()).thenReturn(args1);
        
        Invocation invocation2 = mock(Invocation.class);
        when(invocation2.isVerified()).thenReturn(true);
        when(invocation2.getMock()).thenReturn(mock);
        when(invocation2.getMethod()).thenReturn(method1);
        when(invocation2.getArguments()).thenReturn(args1);
        
        Invocation invocation3 = mock(Invocation.class);
        when(invocation3.isVerified()).thenReturn(false);
        when(invocation3.getMock()).thenReturn(mock);
        when(invocation3.getMethod()).thenReturn(method2);
        when(invocation3.getArguments()).thenReturn(args1);
        
        Invocation invocation4 = mock(Invocation.class);
        when(invocation4.isVerified()).thenReturn(false);
        when(invocation4.getMock()).thenReturn(mock);
        when(invocation4.getMethod()).thenReturn(method1);
        when(invocation4.getArguments()).thenReturn(args2);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        
        assertTrue(invocationMatcher.hasSimilarMethod(invocation3));
        assertFalse(invocationMatcher.hasSimilarMethod(invocation2));
        assertFalse(invocationMatcher.hasSimilarMethod(invocation4));
    }
    
    @Test
    public void testHasSameMethod() {
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        
        Invocation invocation1 = mock(Invocation.class);
        when(invocation1.getMethod()).thenReturn(method1);
        
        Invocation invocation2 = mock(Invocation.class);
        when(invocation2.getMethod()).thenReturn(method2);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        
        assertTrue(invocationMatcher.hasSameMethod(invocation1));
        assertFalse(invocationMatcher.hasSameMethod(invocation2));
    }
    
    @Test
    public void testGetLocation() {
        Location location = mock(Location.class);
        Invocation invocation = mock(Invocation.class);
        when(invocation.getLocation()).thenReturn(location);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertSame(location, invocationMatcher.getLocation());
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        Matcher matcher1 = mock(Matcher.class);
        Matcher matcher2 = mock(Matcher.class);
        Matcher matcher3 = mock(Matcher.class);
        Object argument1 = mock(Object.class);
        Object argument2 = mock(Object.class);
        Object argument3 = mock(Object.class);
        Object[] args = { argument1, argument2, argument3 };
        
        Invocation invocation = mock(Invocation.class);
        when(invocation.getRawArguments()).thenReturn(args);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        invocationMatcher.getMatchers().add(matcher1);
        invocationMatcher.getMatchers().add(matcher2);
        invocationMatcher.getMatchers().add(matcher3);
        
        CapturesArguments capturesArguments1 = mock(CapturesArguments.class);
        CapturesArguments capturesArguments2 = mock(CapturesArguments.class);
        CapturesArguments capturesArguments3 = mock(CapturesArguments.class);
        
        when(matcher1 instanceof CapturesArguments).thenReturn(true);
        when(matcher2 instanceof CapturesArguments).thenReturn(false);
        when(matcher3 instanceof CapturesArguments).thenReturn(true);
        
        when((CapturesArguments) matcher1).thenReturn(capturesArguments1);
        when((CapturesArguments) matcher3).thenReturn(capturesArguments3);
        
        invocationMatcher.captureArgumentsFrom(invocation);
        
        verify(capturesArguments1).captureFrom(argument1);
        verify(capturesArguments3).captureFrom(argument3);
        verify(capturesArguments2, times(0)).captureFrom(anyObject());
    }
    
    @Test
    public void testCreateFrom() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<Invocation> invocations = Arrays.asList(invocation1, invocation2);
        
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        
        assertEquals(2, invocationMatchers.size());
        assertSame(invocation1, invocationMatchers.get(0).getInvocation());
        assertSame(invocation2, invocationMatchers.get(1).getInvocation());
    }
}
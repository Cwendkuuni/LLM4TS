import org.hamcrest.Matchers;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.matchers.Equals;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import org.mockito.internal.invocation.ArgumentsComparator;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.DescribedInvocation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;

public class InvocationMatcherTest {
    
    @Test
    public void testInvocationMatcherConstructorWithMatchersNotEmpty() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<Matcher>();
        Matchers.anyString();
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testInvocationMatcherConstructorWithMatchersEmpty() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<Matcher>();
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(ArgumentsProcessor.argumentsToMatchers(invocation.getArguments()), invocationMatcher.getMatchers());
    }
    
    @Test
    public void testInvocationMatcherConstructorWithoutMatchers() {
        Invocation invocation = mock(Invocation.class);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(Collections.<Matcher>emptyList(), invocationMatcher.getMatchers());
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
        Matchers.anyString();
        Matchers.eq(5);
        when(invocation.toString()).thenReturn("invocation");
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertEquals(new PrintSettings().print(matchers, invocation), invocationMatcher.toString());
    }
    
    @Test
    public void testMatches() {
        Invocation invocation = mock(Invocation.class);
        Invocation actual = mock(Invocation.class);
        Object[] args = new Object[0];
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getMock()).thenReturn(mock(Serializable.class));
        when(invocation.getArguments()).thenReturn(args);
        when(actual.getMethod()).thenReturn(mock(Method.class));
        when(actual.getMock()).thenReturn(mock(Serializable.class));
        when(actual.getArguments()).thenReturn(args);
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        when(argumentsComparator.argumentsMatch(any(InvocationMatcher.class), any(Invocation.class))).thenReturn(true);
        whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(argumentsComparator);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean result = invocationMatcher.matches(actual);
        
        assertTrue(result);
    }
    
    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(invocation.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(mock(Serializable.class));
        when(invocation.getArguments()).thenReturn(new Object[0]);
        when(candidate.getMethod()).thenReturn(method);
        when(candidate.isVerified()).thenReturn(true);
        when(candidate.getMock()).thenReturn(mock(Serializable.class));
        when(candidate.getArguments()).thenReturn(new Object[0]);
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMethod().getName()).thenReturn("methodName");
        when(candidate.getMethod().getName()).thenReturn("methodName");
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        when(argumentsComparator.argumentsMatch(any(InvocationMatcher.class), any(Object[].class))).thenReturn(true);
        whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(argumentsComparator);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean result = invocationMatcher.hasSimilarMethod(candidate);
        
        assertFalse(result);
    }
    
    @Test
    public void testHasSameMethod() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(method1.getName()).thenReturn("methodName");
        when(candidate.getMethod()).thenReturn(method2);
        when(method2.getName()).thenReturn("methodName");
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean result = invocationMatcher.hasSameMethod(candidate);
        
        assertTrue(result);
    }
    
    @Test
    public void testGetLocation() {
        Invocation invocation = mock(Invocation.class);
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Location result = invocationMatcher.getLocation();
        
        assertEquals(location, result);
    }
    
    @Test
    public void testCaptureArgumentsFromVarargs() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Object[] arguments = new Object[2];
        arguments[0] = "arg1";
        arguments[1] = "arg2";
        when(invocation.getMethod().isVarArgs()).thenReturn(true);
        when(invocation.getRawArguments()).thenReturn(arguments);
        Matcher matcher = mock(Matcher.class);
        List<Matcher> matchers = new ArrayList<Matcher>();
        matchers.add(matcher);
        when(invocationMatcher.getMatchers()).thenReturn(matchers);
        
        invocationMatcher.captureArgumentsFrom(invocation);
        
        verify((CapturesArguments)matcher).captureFrom(any());
    }
    
    @Test
    public void testCaptureArgumentsFromNotVarargs() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Object[] arguments = new Object[2];
        arguments[0] = "arg1";
        arguments[1] = "arg2";
        when(invocation.getMethod().isVarArgs()).thenReturn(false);
        when(invocation.getArgumentAt(0, Object.class)).thenReturn(arguments[0]);
        when(invocation.getArgumentAt(1, Object.class)).thenReturn(arguments[1]);
        Matcher matcher = mock(Matcher.class);
        List<Matcher> matchers = new ArrayList<Matcher>();
        matchers.add(matcher);
        when(invocationMatcher.getMatchers()).thenReturn(matchers);
        
        invocationMatcher.captureArgumentsFrom(invocation);
        
        verify((CapturesArguments)matcher).captureFrom(any());
    }
    
    @Test
    public void testCreateFrom() {
        Invocation invocation = mock(Invocation.class);
        List<Invocation> invocations = new ArrayList<Invocation>();
        invocations.add(invocation);
        
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        
        assertEquals(1, invocationMatchers.size());
        assertEquals(invocation, invocationMatchers.get(0).getInvocation());
    }
}
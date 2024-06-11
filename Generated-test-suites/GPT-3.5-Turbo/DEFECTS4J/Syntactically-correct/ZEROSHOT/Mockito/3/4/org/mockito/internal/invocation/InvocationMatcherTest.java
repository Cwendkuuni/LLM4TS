import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import org.hamcrest.Matcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    @Test
    public void testGetMethod() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertEquals(method, invocationMatcher.getMethod());
    }
    
    @Test
    public void testGetInvocation() {
        Invocation invocation = mock(Invocation.class);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertEquals(invocation, invocationMatcher.getInvocation());
    }
    
    @Test
    public void testGetMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Collections.emptyList();
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertEquals(matchers, invocationMatcher.getMatchers());
    }
    
    @Test
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        Matcher matcher = mock(Matcher.class);
        List<Matcher> matchers = Collections.singletonList(matcher);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        PrintSettings printSettings = mock(PrintSettings.class);
        whenNew(PrintSettings.class).withNoArguments().thenReturn(printSettings);
        when(printSettings.print(matchers, invocation)).thenReturn("toString()");
        
        assertEquals("toString()", invocationMatcher.toString());
    }
    
    @Test
    public void testMatches() {
        Invocation invocation = mock(Invocation.class);
        Invocation actual = mock(Invocation.class);
        when(invocation.getMock()).thenReturn("mock");
        when(actual.getMock()).thenReturn("mock");
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(argumentsComparator);
        when(argumentsComparator.argumentsMatch(invocationMatcher, actual)).thenReturn(true);
        
        assertTrue(invocationMatcher.matches(actual));
    }
    
    @Test
    public void testSafelyArgumentsMatch() {
        Object[] actualArgs = new Object[] {"arg1", "arg2"};
        
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        when(invocationMatcher.safelyArgumentsMatch(actualArgs)).thenCallRealMethod();
        
        assertTrue(invocationMatcher.safelyArgumentsMatch(actualArgs));
    }
    
    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getMethod().getName()).thenReturn("method");
        when(candidate.getMethod().getName()).thenReturn("method");
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn("mock");
        when(candidate.getMock()).thenReturn("mock");
        when(invocation.getArguments()).thenReturn(new Object[]{1});
        when(candidate.getArguments()).thenReturn(new Object[]{1});
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(argumentsComparator);
        when(argumentsComparator.argumentsMatch(invocationMatcher, candidate)).thenReturn(true);
        
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSameMethod() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(candidate.getMethod()).thenReturn(method2);
        when(method1.getName()).thenReturn("method");
        when(method2.getName()).thenReturn("method");
        
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
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Invocation mockInvocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(mockInvocation.getMethod()).thenReturn(method);
        when(method.isVarArgs()).thenReturn(true);
        when(mockInvocation.getRawArguments()).thenReturn(new Object[]{"arg1", "arg2", "arg3"});
        
        CapturesArguments capturesArguments1 = mock(CapturesArguments.class);
        CapturesArguments capturesArguments2 = mock(CapturesArguments.class);
        CapturesArguments capturesArguments3 = mock(CapturesArguments.class);
        whenNew(CapturesArguments.class).withNoArguments().thenReturn(capturesArguments1).thenReturn(capturesArguments2).thenReturn(capturesArguments3);
        
        invocationMatcher.captureArgumentsFrom(mockInvocation);
        
        verify(capturesArguments1).captureFrom("arg1");
        verify(capturesArguments2).captureFrom("arg2");
        verify(capturesArguments3).captureFrom("arg3");
    }
    
    @Test
    public void testCreateFrom() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<Invocation> invocations = Arrays.asList(invocation1, invocation2);
        
        InvocationMatcher invocationMatcher1 = mock(InvocationMatcher.class);
        whenNew(InvocationMatcher.class).withArguments(invocation1).thenReturn(invocationMatcher1);
        
        InvocationMatcher invocationMatcher2 = mock(InvocationMatcher.class);
        whenNew(InvocationMatcher.class).withArguments(invocation2).thenReturn(invocationMatcher2);
        
        List<InvocationMatcher> expected = Arrays.asList(invocationMatcher1, invocationMatcher2);
        List<InvocationMatcher> actual = InvocationMatcher.createFrom(invocations);
        
        assertEquals(expected, actual);
    }
}
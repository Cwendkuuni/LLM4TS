
import org.junit.Test;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.matchers.Equals;
import org.mockito.internal.matchers.InstanceOf;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.internal.matchers.CapturesArguments;
import org.hamcrest.Matcher;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class InvocationMatcherTest {

    @Test
    public void testGetMethod() {
        // Test case for getMethod() method
        
        Method method = mock(Method.class);
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(method);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertEquals(method, invocationMatcher.getMethod());
    }
    
    @Test
    public void testGetInvocation() {
        // Test case for getInvocation() method
        
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMock()).thenReturn("mock");
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertEquals(invocation, invocationMatcher.getInvocation());
    }
    
    @Test
    public void testGetMatchers() {
        // Test case for getMatchers() method
        
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(new InstanceOf(String.class));
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertEquals(matchers, invocationMatcher.getMatchers());
    }
    
    @Test
    public void testToString() {
        // Test case for toString() method
        
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(new Equals(null));
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertEquals(invocation.toString(matchers, new PrintSettings()), invocationMatcher.toString());
    }
    
    @Test
    public void testMatches() {
        // Test case for matches() method
        
        Invocation invocation = mock(Invocation.class);
        Invocation actual = mock(Invocation.class);
        when(invocation.getMock()).thenReturn("mock1");
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(actual.getMock()).thenReturn("mock1");
        when(actual.getMethod()).thenReturn(mock(Method.class));
        when(invocation.argumentsToMatchers()).thenReturn(new ArrayList<>());
        when(actual.argumentsToMatchers()).thenReturn(new ArrayList<>());
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertTrue(invocationMatcher.matches(actual));
    }
    
    @Test
    public void testHasSimilarMethod() {
        // Test case for hasSimilarMethod() method
        
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getMock()).thenReturn("mock1");
        when(invocation.isVerified()).thenReturn(false);
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMock()).thenReturn("mock1");
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSameMethod() {
        // Test case for hasSameMethod() method
        
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertTrue(invocationMatcher.hasSameMethod(candidate));
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        // Test case for captureArgumentsFrom() method
        
        Invocation invocation = mock(Invocation.class);
        Invocation i = mock(Invocation.class);
        when(invocation.argumentsToMatchers()).thenReturn(new ArrayList<>());
        when(i.getArguments()).thenReturn(new Object[] {"arg1", "arg2"});
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        CapturesArguments capturesArguments = mock(CapturesArguments.class);
        when(capturesArguments.captureFrom("arg1")).thenReturn(true);
        when(capturesArguments.captureFrom("arg2")).thenReturn(true);
        
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(capturesArguments);
        
        InvocationMatcher argInvocationMatcher = new InvocationMatcher(i, matchers);
        
        invocationMatcher.captureArgumentsFrom(argInvocationMatcher);
        
        verify(capturesArguments, times(2)).captureFrom(anyObject());
    }
    
    @Test
    public void testCreateFrom() {
        // Test case for createFrom() method
        
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<Invocation> invocations = new ArrayList<>();
        invocations.add(invocation1);
        invocations.add(invocation2);
        
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        
        assertEquals(2, invocationMatchers.size());
    }
}


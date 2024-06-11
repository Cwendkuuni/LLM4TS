import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.invocation.Invocation;

public class InvocationMatcherTest {

    @Test
    public void testGetMethod() {
        Invocation invocation = mock(Invocation.class);
        Method expectedMethod = mock(Method.class);
        when(invocation.getMethod()).thenReturn(expectedMethod);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Method actualMethod = invocationMatcher.getMethod();
        
        assertEquals(expectedMethod, actualMethod);
    }
    
    @Test
    public void testGetInvocation() {
        Invocation expectedInvocation = mock(Invocation.class);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(expectedInvocation);
        
        Invocation actualInvocation = invocationMatcher.getInvocation();
        
        assertEquals(expectedInvocation, actualInvocation);
    }
    
    @Test
    public void testGetMatchers() {
        List<Matcher> expectedMatchers = mock(List.class);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(mock(Invocation.class), expectedMatchers);
        
        List<Matcher> actualMatchers = invocationMatcher.getMatchers();
        
        assertEquals(expectedMatchers, actualMatchers);
    }
    
    @Test
    public void testToString() {
        String expectedToString = "Mockito PrintSettings Output";
        Invocation invocation = mock(Invocation.class);
        when(invocation.toString()).thenReturn(expectedToString);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        String actualToString = invocationMatcher.toString();
        
        assertEquals(expectedToString, actualToString);
    }
    
    @Test
    public void testMatches() {
        Invocation invocation = mock(Invocation.class);
        Invocation actualInvocation = mock(Invocation.class);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        when(actualInvocation.getMock()).thenReturn(invocation.getMock());
        when(actualInvocation.getMethod()).thenReturn(invocation.getMethod());
        when(actualInvocation.getArguments()).thenReturn(invocation.getArguments());
        
        boolean matches = invocationMatcher.matches(actualInvocation);
        
        assertTrue(matches);
    }
    
    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidateInvocation = mock(Invocation.class);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidateInvocation.getMethod()).thenReturn(invocation.getMethod());
        when(candidateInvocation.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(candidateInvocation.getMock()).thenReturn(invocation.getMock());
        when(invocation.getArguments()).thenReturn(new Object[]{});
        when(candidateInvocation.getArguments()).thenReturn(new Object[]{});
        when(candidateInvocation.getArguments()).thenReturn(invocation.getArguments());
        
        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidateInvocation);
        
        assertTrue(hasSimilarMethod);
    }
    
    @Test
    public void testHasSameMethod() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidateInvocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(candidateInvocation.getMethod()).thenReturn(method);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        boolean hasSameMethod = invocationMatcher.hasSameMethod(candidateInvocation);
        
        assertTrue(hasSameMethod);
    }
    
    @Test
    public void testGetLocation() {
        Invocation invocation = mock(Invocation.class);
        Location expectedLocation = mock(Location.class);
        when(invocation.getLocation()).thenReturn(expectedLocation);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Location actualLocation = invocationMatcher.getLocation();
        
        assertEquals(expectedLocation, actualLocation);
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Method varargsMethod = mock(Method.class);
        when(varargsMethod.isVarArgs()).thenReturn(true);
        when(invocation.getMethod()).thenReturn(varargsMethod);
        Object[] rawArgs = {1, 2, 3};
        when(invocation.getRawArguments()).thenReturn(rawArgs);
        Matcher matcher = mock(Matcher.class);
        when(matcher instanceof CapturesArguments).thenReturn(true);
        List<Matcher> matchers = List.of(matcher, matcher, matcher);
        invocationMatcher.captureArgumentsFrom(invocation);
        
        //verify that captureFrom method is called on the matcher for each argument
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = mock(List.class);
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        when(invocations.get(0)).thenReturn(invocation1);
        when(invocations.get(1)).thenReturn(invocation2);
        
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        
        assertEquals(2, invocationMatchers.size());
        assertEquals(invocation1, invocationMatchers.get(0).getInvocation());
        assertEquals(invocation2, invocationMatchers.get(1).getInvocation());
    }
}
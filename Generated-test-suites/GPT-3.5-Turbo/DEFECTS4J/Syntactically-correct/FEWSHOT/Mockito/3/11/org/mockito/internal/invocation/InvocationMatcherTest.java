
package org.mockito.internal.invocation;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import java.lang.reflect.Method;
import java.util.List;
import org.hamcrest.Matcher;
import org.mockito.internal.matchers.ArgumentsComparator;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.invocation.Location;

public class InvocationMatcherTest {
    @Test
    public void testConstructorWithMatchers() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = new ArrayList<>();

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }
    
    @Test
    public void testConstructorWithoutMatchers() {
        Invocation invocation = new Invocation();

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(Collections.<Matcher>emptyList(), invocationMatcher.getMatchers());
    }
    
    @Test
    public void testGetMethod() {
        Invocation invocation = new Invocation();
        Method expectedMethod = invocation.getClass().getMethod("getMethod");
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Method method = invocationMatcher.getMethod();
        
        assertEquals(expectedMethod, method);
    }
    
    @Test
    public void testToString() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        String expected = new PrintSettings().print(invocationMatcher.getMatchers(), invocation);
        
        String result = invocationMatcher.toString();
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testMatches() {
        Invocation invocation = new Invocation();
        Invocation actual = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        boolean result = invocationMatcher.matches(actual);
        
        assertFalse(result);
    }
    
    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = new Invocation();
        Invocation candidate = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        boolean result = invocationMatcher.hasSimilarMethod(candidate);
        
        assertFalse(result);
    }
    
    @Test
    public void testHasSameMethod() {
        Invocation invocation = new Invocation();
        Invocation candidate = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        boolean result = invocationMatcher.hasSameMethod(candidate);
        
        assertFalse(result);
    }
    
    @Test
    public void testGetLocation() {
        Invocation invocation = new Invocation();
        Location expectedLocation = invocation.getLocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Location location = invocationMatcher.getLocation();
        
        assertEquals(expectedLocation, location);
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        invocationMatcher.captureArgumentsFrom(invocation);
        
        // assert something
    }
    
    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new ArrayList<>();
        invocations.add(new Invocation());
        
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        
        assertEquals(1, invocationMatchers.size());
    }
}

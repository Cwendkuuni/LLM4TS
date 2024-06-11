package org.mockito.internal.invocation;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import java.lang.reflect.Method;
import java.lang.reflect.Array;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.hamcrest.Matcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.matchers.MatcherDecorator;
import org.mockito.internal.matchers.VarargMatcher;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.invocation.Location;

@SuppressWarnings("unchecked")
public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherWithMatchers() {
        Invocation invocation = createInvocation();
        List<Matcher> matchers = createMatchers();
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
        
        assertTrue(invocationMatcher.hasSameMethod(invocation));
    }
    
    @Test
    public void testInvocationMatcherWithoutMatchers() {
        Invocation invocation = createInvocation();
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(Collections.emptyList(), invocationMatcher.getMatchers());
        
        assertTrue(invocationMatcher.hasSameMethod(invocation));
    }
    
    @Test
    public void testToString() {
        Invocation invocation = createInvocation();
        List<Matcher> matchers = createMatchers();
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        String expected = new PrintSettings().print(matchers, invocation);
        
        assertEquals(expected, invocationMatcher.toString());
    }
    
    private Invocation createInvocation() {
        Method method = getClass().getMethods()[0];
        Object[] arguments = { "arg1", 2, true };
        Object mock = this;
        Location location = null;
        
        return new Invocation(mock, method, arguments, location);
    }
    
    private List<Matcher> createMatchers() {
        List<Matcher> matchers = new LinkedList<Matcher>();
        
        matchers.add(new CustomMatcherOne());
        matchers.add(new CustomMatcherTwo());
        
        return matchers;
    }
    
    private class CustomMatcherOne implements Matcher {
        // implementation goes here
    }
    
    private class CustomMatcherTwo implements Matcher {
        // implementation goes here
    }
}
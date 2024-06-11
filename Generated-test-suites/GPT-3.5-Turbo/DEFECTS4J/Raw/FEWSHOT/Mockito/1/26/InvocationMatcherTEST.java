import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.matchers.MatcherDecorator;
import org.mockito.internal.matchers.VarargMatcher;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import org.mockito.internal.invocation.InvocationMatcher;

public class InvocationMatcherTest {
    
    @Test
    public void testInvocationMatcher() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<Matcher>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }
    
    @Test
    public void testInvocationMatcher_EmptyMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<Matcher>();
        matchers.add(mock(Matcher.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers.size(), invocationMatcher.getMatchers().size());
    }
    
    @Test
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<Matcher>();
        matchers.add(mock(Matcher.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertEquals(new PrintSettings().print(matchers, invocation), invocationMatcher.toString());
    }
    
    @Test
    public void testMatches() {
        Invocation invocation = mock(Invocation.class);
        Invocation actual = mock(Invocation.class);
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        
        List<Matcher> matchers = new ArrayList<Matcher>();
        matchers.add(mock(Matcher.class));
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertTrue(invocationMatcher.matches(actual));
        
        verify(invocation).getMock();
        verify(actual).getMock();
        verify(invocation).getMethod();
        verify(actual).getMethod();
        verify(argumentsComparator).argumentsMatch(invocationMatcher, actual);
    }
    
    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        
        Method method = mock(Method.class);
        Location location = mock(Location.class);
        
        List<Matcher> matchers = new ArrayList<Matcher>();
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
        
        verify(invocation).getMethod();
        verify(candidate).getMethod();
        verify(invocation).isVerified();
        verify(candidate).isVerified();
        verify(invocation).getMock();
        verify(candidate).getMock();
        verify(invocation).getArguments();
        verify(candidate).getArguments();
        verify(invocation).getLocation();
        verify(candidate).getMethod();
    }
    
    @Test
    public void testHasSameMethod() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        
        List<Matcher> matchers = new ArrayList<Matcher>();
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertFalse(invocationMatcher.hasSameMethod(candidate));
        
        verify(invocation).getMethod();
        verify(candidate).getMethod();
    }
    
    @Test
    public void testGetLocation() {
        Invocation invocation = mock(Invocation.class);
        Location location = mock(Location.class);
        
        List<Matcher> matchers = new ArrayList<Matcher>();
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertEquals(location, invocationMatcher.getLocation());
        
        verify(invocation).getLocation();
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        Invocation invocation1 = mock(Invocation.class);
        
        Class[] classes = new Class[1];
        classes[0] = Object.class;
        
        Method method = mock(Method.class);
        
        Object[] args = new Object[1];
        args[0] = new Object();
        
        List<Matcher> matchers = new ArrayList<Matcher>();
        matchers.add(mock(Matcher.class));
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        invocationMatcher.captureArgumentsFrom(invocation1);
        
        verify(invocation).getMethod();
        verify(invocation).getRawArguments();
        verify(invocation1).getMethod();
        verify(invocation1).getArgumentAt(0, Object.class);
    }
    
    @Test
    public void testIsVarargMatcher() {
        Matcher matcher = mock(Matcher.class);
        
        MatcherDecorator matcherDecorator = mock(MatcherDecorator.class);
        VarargMatcher varargMatcher = mock(VarargMatcher.class);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(mock(Invocation.class), new ArrayList<Matcher>());
        
        assertFalse(invocationMatcher.isVarargMatcher(matcher));
        assertFalse(invocationMatcher.isVarargMatcher(matcherDecorator));
        assertTrue(invocationMatcher.isVarargMatcher(varargMatcher));
        
        verify(matcherDecorator).getActualMatcher();
    }
    
    @Test
    public void testIsVariableArgument() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        
        Object[] args = new Object[1];
        args[0] = new Object();
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, new ArrayList<Matcher>());
        
        assertFalse(invocationMatcher.isVariableArgument(invocation, 0));
        
        verify(invocation).getRawArguments();
    }
    
    // Additional Test Cases can be added to cover all the methods of the class

}
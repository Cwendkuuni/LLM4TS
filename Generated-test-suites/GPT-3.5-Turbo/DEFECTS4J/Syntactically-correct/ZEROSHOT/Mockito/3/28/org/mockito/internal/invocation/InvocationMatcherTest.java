import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithMatcherList() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(new CapturesArguments());
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }
    
    @Test
    public void testConstructorWithoutMatcherList() {
        Invocation invocation = new Invocation();
        List<Matcher> emptyMatchers = Collections.emptyList();
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(emptyMatchers, invocationMatcher.getMatchers());
    }
    
    @Test
    public void testGetMethod() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Method method = invocationMatcher.getMethod();
        
        assertEquals(method, invocation.getMethod());
    }
    
    @Test
    public void testToString() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        String expected = new PrintSettings().print(invocationMatcher.getMatchers(), invocationMatcher.getInvocation());
        String actual = invocationMatcher.toString();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMatches() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Invocation actual = new Invocation();
        
        boolean expected = invocation.getMock().equals(actual.getMock())
                && invocationMatcher.hasSameMethod(actual)
                && new ArgumentsComparator().argumentsMatch(invocationMatcher, actual);
        boolean actual = invocationMatcher.matches(actual);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSafelyArgumentsMatch() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Object[] actualArgs = new Object[1];
        
        boolean expected = false;
        boolean actual = invocationMatcher.safelyArgumentsMatch(actualArgs);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Invocation candidate = new Invocation();
        
        boolean wantedMethodNameEquals = invocationMatcher.getMethod().getName().equals(candidate.getMethod().getName());
        boolean isUnverified = !candidate.isVerified();
        boolean mockIsTheSame = invocationMatcher.getInvocation().getMock() == candidate.getMock();
        boolean methodEquals = invocationMatcher.hasSameMethod(candidate);
        boolean overloadedButSameArgs = !methodEquals && invocationMatcher.safelyArgumentsMatch(candidate.getArguments());
        
        boolean expected = wantedMethodNameEquals && isUnverified && mockIsTheSame && !overloadedButSameArgs;
        boolean actual = invocationMatcher.hasSimilarMethod(candidate);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testHasSameMethod() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Invocation candidate = new Invocation();
        
        Method m1 = invocation.getMethod();
        Method m2 = candidate.getMethod();
        
        boolean expected = m1.getName() != null && m1.getName().equals(m2.getName());
        boolean actual = invocationMatcher.hasSameMethod(candidate);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetLocation() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Location expected = invocation.getLocation();
        Location actual = invocationMatcher.getLocation();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Invocation captureInvocation = new Invocation();
        
        invocationMatcher.captureArgumentsFrom(captureInvocation);
        
        // Test the logic inside captureArgumentsFrom() method
        
        assertTrue(invocation.getMethod().isVarArgs());
        int indexOfVararg = captureInvocation.getRawArguments().length - 1;
        
        for (int position = 0; position < indexOfVararg; position++) {
            Matcher m = invocationMatcher.getMatchers().get(position);
            if (m instanceof CapturesArguments) {
                CapturesArguments capturesArguments = (CapturesArguments) m;
                Object argument = captureInvocation.getArgumentAt(position, Object.class);
                capturesArguments.captureFrom(argument);
                
                // Check if the argument was successfully captured
                assertEquals(argument, capturesArguments.getCapturedArguments());
            }
        }
        
        for (int position = indexOfVararg; position < invocationMatcher.getMatchers().size(); position++) {
            Matcher m = invocationMatcher.getMatchers().get(position);
            if (m instanceof CapturesArguments) {
                CapturesArguments capturesArguments = (CapturesArguments) m;
                Object argument = captureInvocation.getRawArguments()[position - indexOfVararg];
                capturesArguments.captureFrom(argument);
                
                // Check if the argument was successfully captured
                assertEquals(argument, capturesArguments.getCapturedArguments());
            }
        }
    }
    
    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new ArrayList<>();
        invocations.add(new Invocation());
        
        List<InvocationMatcher> expected = new LinkedList<>();
        expected.add(new InvocationMatcher(new Invocation()));
        List<InvocationMatcher> actual = InvocationMatcher.createFrom(invocations);
        
        assertEquals(expected, actual);
    }
}
package org.mockito.internal.invocation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.Invocation;

public class InvocationMatcherTest {
    
    private Invocation invocation;
    private List<Matcher> matchers;
    
    @Before
    public void setUp() {
        invocation = new Invocation();
        matchers = new ArrayList<>();
    }

    @Test
    public void testConstructorWithMatchers() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }
    
    @Test
    public void testConstructorWithoutMatchers() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }
    
    @Test
    public void testGetMethod() {
        Method method = new Method();
        invocation.setMethod(method);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertEquals(method, invocationMatcher.getMethod());
    }
    
    @Test
    public void testToString() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        String expected = new PrintSettings().print(matchers, invocation);
        assertEquals(expected, invocationMatcher.toString());
    }
    
    @Test
    public void testMatches() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation actual = new Invocation();
        invocation.setMock(actual.getMock());
        invocation.setArguments(actual.getArguments());
        
        boolean result = invocationMatcher.matches(actual);
        
        assertTrue(result);
    }
    
    @Test
    public void testMatchesWithDifferentMock() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation actual = new Invocation();
        actual.setMock(new Object());
        invocation.setMock(new Object());
        invocation.setArguments(actual.getArguments());
        
        boolean result = invocationMatcher.matches(actual);
        
        assertFalse(result);
    }
    
    @Test
    public void testMatchesWithDifferentMethod() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation actual = new Invocation();
        invocation.setMock(actual.getMock());
        actual.setMethod(new Method());
        invocation.setArguments(actual.getArguments());
        
        boolean result = invocationMatcher.matches(actual);
        
        assertFalse(result);
    }
    
    @Test
    public void testHasSimilarMethod() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation candidate = new Invocation();
        
        boolean result = invocationMatcher.hasSimilarMethod(candidate);
        
        assertTrue(result);
    }
    
    @Test
    public void testHasSimilarMethodWithDifferentMethodName() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation candidate = new Invocation();
        candidate.setMethod(new Method("differentMethodName"));
        
        boolean result = invocationMatcher.hasSimilarMethod(candidate);
        
        assertFalse(result);
    }
    
    @Test
    public void testHasSimilarMethodWithVerifiedInvocation() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation candidate = new Invocation();
        candidate.setVerified(true);
        
        boolean result = invocationMatcher.hasSimilarMethod(candidate);
        
        assertFalse(result);
    }
    
    @Test
    public void testHasSimilarMethodWithDifferentMock() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation candidate = new Invocation();
        candidate.setMock(new Object());
        
        boolean result = invocationMatcher.hasSimilarMethod(candidate);
        
        assertFalse(result);
    }
    
    @Test
    public void testHasSimilarMethodWithSameArguments() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation candidate = new Invocation();
        invocation.setArguments(new Object[] {"arg1", "arg2"});
        candidate.setArguments(new Object[] {"arg1", "arg2"});
        
        boolean result = invocationMatcher.hasSimilarMethod(candidate);
        
        assertFalse(result);
    }
    
    @Test
    public void testHasSimilarMethodWithOverloadedArguments() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation candidate = new Invocation();
        invocation.setArguments(new Object[] {"arg1"});
        candidate.setArguments(new Object[] {"arg1", "arg2"});
        
        boolean result = invocationMatcher.hasSimilarMethod(candidate);
        
        assertTrue(result);
    }
    
    @Test
    public void testHasSameMethod() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation candidate = new Invocation();
        invocation.setMethod(new Method("methodName"));
        candidate.setMethod(new Method("methodName"));
        
        boolean result = invocationMatcher.hasSameMethod(candidate);
        
        assertTrue(result);
    }
    
    @Test
    public void testHasSameMethodWithDifferentMethodName() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation candidate = new Invocation();
        invocation.setMethod(new Method("methodName"));
        candidate.setMethod(new Method("differentMethodName"));
        
        boolean result = invocationMatcher.hasSameMethod(candidate);
        
        assertFalse(result);
    }
    
    @Test
    public void testHasSameMethodWithDifferentParameterTypes() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation candidate = new Invocation();
        Method method1 = new Method();
        method1.setParameterTypes(new Class[] {String.class});
        Method method2 = new Method();
        method2.setParameterTypes(new Class[] {Integer.class});
        invocation.setMethod(method1);
        candidate.setMethod(method2);
        
        boolean result = invocationMatcher.hasSameMethod(candidate);
        
        assertFalse(result);
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation invocation2 = new Invocation();
        invocation2.setMethod(new Method("varArgsMethod", Object[].class));
        invocation2.setArguments(new Object[] {"arg1", "arg2", "arg3"});
        invocationMatcher.getMatchers().addAll(Arrays.asList(new Matcher[] {new Matcher1(), new Matcher2(), new Matcher3()}));
        MatchersCapturedArguments matcher1 = new MatchersCapturedArguments();
        MatchersCapturedArguments matcher2 = new MatchersCapturedArguments();
        MatchersCapturedArguments matcher3 = new MatchersCapturedArguments();
        invocationMatcher.getMatchers().get(0).captureFrom("arg1");
        invocationMatcher.getMatchers().get(1).captureFrom("arg2");
        invocationMatcher.getMatchers().get(2).captureFrom("arg3");
        
        invocationMatcher.captureArgumentsFrom(invocation2);
        
        assertEquals(Arrays.asList(new Object[] {"arg1"}), matcher1.getCapturedArguments());
        assertEquals(Arrays.asList(new Object[] {"arg2"}), matcher2.getCapturedArguments());
        assertEquals(Arrays.asList(new Object[] {"arg3"}), matcher3.getCapturedArguments());
    }
    
    @Test
    public void testCaptureArgumentsFromWithVarArgs() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation invocation2 = new Invocation();
        invocation2.setMethod(new Method("varArgsMethod", Object[].class));
        invocation2.setArguments(new Object[] {"arg1", "arg2", "arg3"});
        invocationMatcher.getMatchers().addAll(Arrays.asList(new Matcher[] {new Matcher1(), new Matcher2(), new Matcher3()}));
        MatchersCapturedArguments matcher1 = new MatchersCapturedArguments();
        MatchersCapturedArguments matcher2 = new MatchersCapturedArguments();
        MatchersCapturedArguments matcher3 = new MatchersCapturedArguments();
        invocationMatcher.getMatchers().get(0).captureFrom("arg1");
        invocationMatcher.getMatchers().get(1).captureFrom("arg2");
        invocationMatcher.getMatchers().get(2).captureFrom("arg3");
        
        invocationMatcher.captureArgumentsFrom(invocation2);
        
        assertEquals(Arrays.asList(new Object[] {"arg1"}), matcher1.getCapturedArguments());
        assertEquals(Arrays.asList(new Object[] {"arg2"}), matcher2.getCapturedArguments());
        assertEquals(Arrays.asList(new Object[] {"arg3"}), matcher3.getCapturedArguments());
    }
    
    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new ArrayList<>();
        Invocation invocation1 = new Invocation();
        Invocation invocation2 = new Invocation();
        invocations.add(invocation1);
        invocations.add(invocation2);
        
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        
        assertEquals(2, invocationMatchers.size());
        assertTrue(invocationMatchers.contains(new InvocationMatcher(invocation1)));
        assertTrue(invocationMatchers.contains(new InvocationMatcher(invocation2)));
    }
    
    // Additional test classes and methods
    
    private class Method {
        private String name;
        private Class[] parameterTypes;
        
        public Method() {
            
        }
        
        public Method(String name) {
            this.name = name;
        }
        
        public String getName() {
            return name;
        }
        
        public void setParameterTypes(Class[] parameterTypes) {
            this.parameterTypes = parameterTypes;
        }
        
        public Class[] getParameterTypes() {
            return parameterTypes;
        }
    }
    
    private class Matcher1 implements Matcher {

        @Override
        public void captureFrom(Object argument) {
            
        }
        
    }
    
    private class Matcher2 implements Matcher {

        @Override
        public void captureFrom(Object argument) {
            
        }
        
    }
    
    private class Matcher3 implements Matcher {

        @Override
        public void captureFrom(Object argument) {
            
        }
        
    }
    
    private class MatchersCapturedArguments implements Matcher, CapturesArguments {
        
        private List<Object> capturedArguments = new ArrayList<>();
        
        @Override
        public void captureFrom(Object argument) {
            capturedArguments.add(argument);
        }
        
        public List<Object> getCapturedArguments() {
            return capturedArguments;
        }
        
    }
    
    
}
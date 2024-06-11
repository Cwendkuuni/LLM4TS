package org.mockito.internal.invocation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;

public class InvocationMatcherTest {

    @Test
    public void testConstructor_withMatchers() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = Arrays.asList(new Matcher(), new Matcher());
        
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);
        
        assertEquals(invocation, matcher.getInvocation());
        assertEquals(matchers, matcher.getMatchers());
    }
    
    @Test
    public void testConstructor_withoutMatchers() {
        Invocation invocation = new Invocation();
        
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        
        assertEquals(invocation, matcher.getInvocation());
        assertEquals(invocation.argumentsToMatchers(), matcher.getMatchers());
    }
    
    @Test
    public void testGetMethod() {
        Invocation invocation = new Invocation();
        
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        
        assertEquals(invocation.getMethod(), matcher.getMethod());
    }

    @Test
    public void testToString() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = Collections.singletonList(new Matcher());
        
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);
        
        String expected = invocation.toString(matchers, new PrintSettings());
        
        assertEquals(expected, matcher.toString());
    }
    
    @Test
    public void testMatches_withMatchingInvocation() {
        Invocation invocation = new Invocation();
        Invocation actual = new Invocation();
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        
        boolean result = matcher.matches(actual);
        
        assertTrue(result);
    }
    
    @Test
    public void testMatches_withDifferentMock() {
        Invocation invocation = new Invocation();
        invocation.setMock("mock1");
        Invocation actual = new Invocation();
        actual.setMock("mock2");
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        
        boolean result = matcher.matches(actual);
        
        assertFalse(result);
    }
    
    @Test
    public void testMatches_withDifferentMethod() {
        Invocation invocation = new Invocation();
        invocation.setMethod("method1");
        Invocation actual = new Invocation();
        actual.setMethod("method2");
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        
        boolean result = matcher.matches(actual);
        
        assertFalse(result);
    }
    
    @Test
    public void testMatches_withDifferentArguments() {
        Invocation invocation = new Invocation();
        invocation.setArguments(new Object[] {1, 2, 3});
        Invocation actual = new Invocation();
        actual.setArguments(new Object[] {4, 5, 6});
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        
        boolean result = matcher.matches(actual);
        
        assertFalse(result);
    }
    
    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = new Invocation();
        invocation.setMethod("method");
        Invocation candidate = new Invocation();
        candidate.setMethod("method");
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        
        boolean result = matcher.hasSimilarMethod(candidate);
        
        assertTrue(result);
    }
    
    @Test
    public void testHasSimilarMethod_withDifferentMethodName() {
        Invocation invocation = new Invocation();
        invocation.setMethod("method1");
        Invocation candidate = new Invocation();
        candidate.setMethod("method2");
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        
        boolean result = matcher.hasSimilarMethod(candidate);
        
        assertFalse(result);
    }
    
    @Test
    public void testHasSimilarMethod_withVerifiedCandidate() {
        Invocation invocation = new Invocation();
        Invocation candidate = new Invocation();
        candidate.setVerified(true);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        
        boolean result = matcher.hasSimilarMethod(candidate);
        
        assertFalse(result);
    }
    
    @Test
    public void testHasSimilarMethod_withDifferentMock() {
        Invocation invocation = new Invocation();
        invocation.setMock("mock1");
        Invocation candidate = new Invocation();
        candidate.setMock("mock2");
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        
        boolean result = matcher.hasSimilarMethod(candidate);
        
        assertFalse(result);
    }
    
    @Test
    public void testHasSimilarMethod_withSameMethodAndSameArguments() {
        Invocation invocation = new Invocation();
        invocation.setMethod("method");
        invocation.setArguments(new Object[] {1, 2, 3});
        Invocation candidate = new Invocation();
        candidate.setMethod("method");
        candidate.setArguments(new Object[] {1, 2, 3});
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        
        boolean result = matcher.hasSimilarMethod(candidate);
        
        assertFalse(result);
    }
    
    @Test
    public void testHasSameMethod_withEqualMethods() throws NoSuchMethodException {
        Method method1 = Invocation.class.getMethod("getMethod");
        Method method2 = Invocation.class.getMethod("getMethod");
        Invocation invocation1 = new Invocation();
        invocation1.setMethod(method1);
        Invocation invocation2 = new Invocation();
        invocation2.setMethod(method2);
        InvocationMatcher matcher = new InvocationMatcher(invocation1);
        
        boolean result = matcher.hasSameMethod(invocation2);
        
        assertTrue(result);
    }
    
    @Test
    public void testHasSameMethod_withDifferentMethods() throws NoSuchMethodException {
        Method method1 = Invocation.class.getMethod("getMethod");
        Method method2 = Invocation.class.getMethod("getMethod");
        Invocation invocation1 = new Invocation();
        invocation1.setMethod(method1);
        Invocation invocation2 = new Invocation();
        invocation2.setMethod(method2);
        InvocationMatcher matcher = new InvocationMatcher(invocation1);
        
        boolean result = matcher.hasSameMethod(invocation2);
        
        assertFalse(result);
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = new Invocation();
        Matcher matcher1 = new Matcher();
        Matcher matcher2 = new Matcher();
        Invocation capturedInvocation = new Invocation();
        Object[] capturedArguments = new Object[] {1, 2, 3};
        capturedInvocation.setArguments(capturedArguments);
        InvocationMatcher matcher = new InvocationMatcher(invocation, Arrays.asList(matcher1, matcher2));
        
        matcher.captureArgumentsFrom(capturedInvocation);
        
        assertTrue(matcher1.hasCapturedArguments());
        assertTrue(matcher1.getCapturedArguments().contains(capturedArguments[0]));
        assertTrue(matcher2.hasCapturedArguments());
        assertTrue(matcher2.getCapturedArguments().contains(capturedArguments[1]));
    }
    
    @Test
    public void testCreateFrom() {
        Invocation invocation1 = new Invocation();
        Invocation invocation2 = new Invocation();
        List<Invocation> invocations = Arrays.asList(invocation1, invocation2);
        
        List<InvocationMatcher> matchers = InvocationMatcher.createFrom(invocations);
        
        assertEquals(invocations.size(), matchers.size());
        assertTrue(matchers.contains(new InvocationMatcher(invocation1)));
        assertTrue(matchers.contains(new InvocationMatcher(invocation2)));
    }
}
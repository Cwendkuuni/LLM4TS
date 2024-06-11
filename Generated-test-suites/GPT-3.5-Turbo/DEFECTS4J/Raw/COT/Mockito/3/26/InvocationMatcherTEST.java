import org.junit.Test;
import org.mockito.invocation.Invocation;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class InvocationMatcherTest {

    // Test case for constructor with matchers
    @Test
    public void testInvocationMatcherWithMatchers() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = Collections.singletonList(new Matcher());
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }
    
    // Test case for constructor without matchers
    @Test
    public void testInvocationMatcherWithoutMatchers() {
        Invocation invocation = new Invocation();
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(Collections.emptyList(), invocationMatcher.getMatchers());
    }
    
    // Test case for getMethod
    @Test
    public void testGetMethod() {
        Invocation invocation = new Invocation();
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertEquals(Method.class, invocationMatcher.getMethod().getClass());
    }
    
    // Test case for toString
    @Test
    public void testToString() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = Arrays.asList(new Matcher(), new Matcher());
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        String expectedResult = "Matchers: [Matcher@1, Matcher@2], Invocation: " + invocation.toString();
        assertEquals(expectedResult, invocationMatcher.toString());
    }
    
    // Test case for matches with matching invocation
    @Test
    public void testMatchesMatchingInvocation() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = Collections.singletonList(new Matcher());
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        Invocation actual = new Invocation();
        
        assertTrue(invocationMatcher.matches(actual));
    }
    
    // Test case for matches with non-matching invocation
    @Test
    public void testMatchesNonMatchingInvocation() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = Collections.singletonList(new Matcher());
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        Invocation actual = new Invocation();
        actual.setMock(new Object());
        
        assertFalse(invocationMatcher.matches(actual));
    }
    
    // Test case for hasSimilarMethod with similar method
    @Test
    public void testHasSimilarMethodWithSimilarMethod() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = Collections.singletonList(new Matcher());
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        Invocation candidate = new Invocation();
        candidate.setMethod(invocation.getMethod());
        candidate.setMock(invocation.getMock());
        
        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    // Test case for hasSimilarMethod with different method
    @Test
    public void testHasSimilarMethodWithDifferentMethod() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = Collections.singletonList(new Matcher());
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        Invocation candidate = new Invocation();
        candidate.setMethod(new Method());
        
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    // Test case for hasSimilarMethod with different mock
    @Test
    public void testHasSimilarMethodWithDifferentMock() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = Collections.singletonList(new Matcher());
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        Invocation candidate = new Invocation();
        candidate.setMethod(invocation.getMethod());
        candidate.setMock(new Object());
        
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    // Test case for getLocation
    @Test
    public void testGetLocation() {
        Invocation invocation = new Invocation();
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertNull(invocationMatcher.getLocation());
    }
    
    // Test case for captureArgumentsFrom with varargs
    @Test
    public void testCaptureArgumentsFromVarargs() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = Arrays.asList(new Matcher(), new Matcher());
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        Invocation captured = new Invocation();
        
        invocationMatcher.captureArgumentsFrom(captured);
        
        // Test that the first matcher captures the first argument
        Matcher matcher1 = invocationMatcher.getMatchers().get(0);
        assertTrue(matcher1 instanceof CapturesArguments);
        assertEquals(captured.getArgumentAt(0, Object.class), ((CapturesArguments) matcher1).getCapturedArgument());
        
        // Test that the second matcher captures the second argument
        Matcher matcher2 = invocationMatcher.getMatchers().get(1);
        assertTrue(matcher2 instanceof CapturesArguments);
        assertEquals(captured.getArgumentAt(1, Object.class), ((CapturesArguments) matcher2).getCapturedArgument());
    }
    
    // Test case for captureArgumentsFrom without varargs
    @Test
    public void testCaptureArgumentsFromWithoutVarargs() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = Collections.singletonList(new Matcher());
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        Invocation captured = new Invocation();
        
        invocationMatcher.captureArgumentsFrom(captured);
        
        Matcher matcher = invocationMatcher.getMatchers().get(0);
        assertTrue(matcher instanceof CapturesArguments);
        assertEquals(captured.getArgumentAt(0, Object.class), ((CapturesArguments) matcher).getCapturedArgument());
    }
    
    // Test case for createFrom
    @Test
    public void testCreateFrom() {
        Invocation invocation1 = new Invocation();
        Invocation invocation2 = new Invocation();
        Invocation invocation3 = new Invocation();
        List<Invocation > invocations = Arrays.asList(invocation1, invocation2, invocation3);
        
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        
        assertEquals(invocations.get(0), invocationMatchers.get(0).getInvocation());
        assertEquals(invocations.get(1), invocationMatchers.get(1).getInvocation());
        assertEquals(invocations.get(2), invocationMatchers.get(2).getInvocation());
    }
}
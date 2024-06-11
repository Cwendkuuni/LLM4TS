import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.exceptions.PrintableInvocation;
import org.hamcrest.Matcher;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcher_withInvocationsAndMatchers() {
        // Create sample Invocation and Matchers
        MethodInvocation mockInvocation = new MethodInvocation();
        List<Matcher> matchers = Collections.<Matcher>emptyList();
        
        // Create InvocationMatcher object
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation, matchers);
        
        // Verify that InvocationMatcher object is created successfully
        assertEquals(mockInvocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }
    
    @Test
    public void testInvocationMatcher_withInvocationsOnly() {
        // Create sample Invocation
        MethodInvocation mockInvocation = new MethodInvocation();
        
        // Create InvocationMatcher object
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation);
        
        // Verify that InvocationMatcher object is created successfully
        assertEquals(mockInvocation, invocationMatcher.getInvocation());
        assertEquals(Collections.<Matcher>emptyList(), invocationMatcher.getMatchers());
    }
    
    @Test
    public void testGetMethod() {
        // Create sample Invocation
        MethodInvocation mockInvocation = new MethodInvocation();
        
        // Create InvocationMatcher object
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation);
        
        // Verify that the method of InvocationMatcher is the same as the method of the Invocation
        assertEquals(mockInvocation.getMethod(), invocationMatcher.getMethod());
    }
    
    @Test
    public void testMatches_withMatchingInvocation() {
        // Create sample Invocations
        MethodInvocation mockInvocation1 = new MethodInvocation();
        MethodInvocation mockInvocation2 = new MethodInvocation();
        
        // Create InvocationMatchers
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(mockInvocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(mockInvocation2);
        
        // Verify that the matches() method returns true when the Invocations match
        assertTrue(invocationMatcher1.matches(mockInvocation1));
        assertTrue(invocationMatcher2.matches(mockInvocation2));
    }
    
    @Test
    public void testMatches_withNonMatchingInvocation() {
        // Create sample Invocations
        MethodInvocation mockInvocation1 = new MethodInvocation();
        MethodInvocation mockInvocation2 = new MethodInvocation();
        
        // Create InvocationMatchers
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(mockInvocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(mockInvocation2);
        
        // Verify that the matches() method returns false when the Invocations do not match
        assertFalse(invocationMatcher1.matches(mockInvocation2));
        assertFalse(invocationMatcher2.matches(mockInvocation1));
    }
    
    @Test
    public void testHasSimilarMethod_withSimilarMethod() {
        // Create sample Invocations with similar methods
        MethodInvocation mockInvocation1 = new MethodInvocation();
        MethodInvocation mockInvocation2 = new MethodInvocation();
        mockInvocation1.setMethod(new Method("method1"));
        mockInvocation2.setMethod(new Method("method1"));
        
        // Create InvocationMatchers
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(mockInvocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(mockInvocation2);
        
        // Verify that the hasSimilarMethod() method returns true when the Invocations have similar methods
        assertTrue(invocationMatcher1.hasSimilarMethod(mockInvocation2));
        assertTrue(invocationMatcher2.hasSimilarMethod(mockInvocation1));
    }
    
    @Test
    public void testHasSimilarMethod_withDifferentMethod() {
        // Create sample Invocations with different methods
        MethodInvocation mockInvocation1 = new MethodInvocation();
        MethodInvocation mockInvocation2 = new MethodInvocation();
        mockInvocation1.setMethod(new Method("method1"));
        mockInvocation2.setMethod(new Method("method2"));
        
        // Create InvocationMatchers
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(mockInvocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(mockInvocation2);
        
        // Verify that the hasSimilarMethod() method returns false when the Invocations have different methods
        assertFalse(invocationMatcher1.hasSimilarMethod(mockInvocation2));
        assertFalse(invocationMatcher2.hasSimilarMethod(mockInvocation1));
    }

    @Test
    public void testHasSameMethod_withSameMethod() {
        // Create sample Invocations with the same method
        MethodInvocation mockInvocation1 = new MethodInvocation();
        MethodInvocation mockInvocation2 = new MethodInvocation();
        mockInvocation1.setMethod(new Method("method1"));
        mockInvocation2.setMethod(new Method("method1"));
        
        // Create InvocationMatchers
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(mockInvocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(mockInvocation2);
        
        // Verify that the hasSameMethod() method returns true when the Invocations have the same method
        assertTrue(invocationMatcher1.hasSameMethod(mockInvocation2));
        assertTrue(invocationMatcher2.hasSameMethod(mockInvocation1));
    }
    
    @Test
    public void testHasSameMethod_withDifferentMethod() {
        // Create sample Invocations with different methods
        MethodInvocation mockInvocation1 = new MethodInvocation();
        MethodInvocation mockInvocation2 = new MethodInvocation();
        mockInvocation1.setMethod(new Method("method1"));
        mockInvocation2.setMethod(new Method("method2"));
        
        // Create InvocationMatchers
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(mockInvocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(mockInvocation2);
        
        // Verify that the hasSameMethod() method returns false when the Invocations have different methods
        assertFalse(invocationMatcher1.hasSameMethod(mockInvocation2));
        assertFalse(invocationMatcher2.hasSameMethod(mockInvocation1));
    }
    
    @Test
    public void testToString() {
        // Create sample Invocation
        MethodInvocation mockInvocation = new MethodInvocation();
        
        // Create InvocationMatcher object
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation);
        
        // Verify that the toString() method returns the expected string representation of the InvocationMatcher
        assertEquals(mockInvocation.toString(), invocationMatcher.toString());
    }

    // Other test methods for remaining methods in the InvocationMatcher class can be added here
    
}
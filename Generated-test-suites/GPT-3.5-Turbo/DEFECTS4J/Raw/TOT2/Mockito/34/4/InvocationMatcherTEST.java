import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.hamcrest.Matcher;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InvocationMatcherTest {

    @Test
    public void testGetMethod() {
        // Create mock Invocation object for testing
        Invocation testInvocation = mock(Invocation.class);

        // Create InvocationMatcher object
        InvocationMatcher invocationMatcher = new InvocationMatcher(testInvocation);

        // Test getMethod method
        Method actualMethod = invocationMatcher.getMethod();
        assertEquals(testInvocation.getMethod(), actualMethod);
    }

    @Test
    public void testGetInvocation() {
        // Create mock Invocation object for testing
        Invocation testInvocation = mock(Invocation.class);

        // Create InvocationMatcher object
        InvocationMatcher invocationMatcher = new InvocationMatcher(testInvocation);

        // Test getInvocation method
        Invocation actualInvocation = invocationMatcher.getInvocation();
        assertEquals(testInvocation, actualInvocation);
    }

    // Add more test methods for other public methods in the class

    // Test edge cases and exception handling scenarios

    @Test
    public void testSafelyArgumentsMatch_ExceptionHandling() {
        Invocation testInvocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(testInvocation);
        
        boolean result = invocationMatcher.safelyArgumentsMatch(null);
        
        assertFalse(result);
    }

    @Test
    public void testHasSameMethod() {
        Invocation testInvocation1 = mock(Invocation.class);
        Invocation testInvocation2 = mock(Invocation.class);
        
        when(testInvocation1.getMethod()).thenReturn(mock(Method.class));
        when(testInvocation2.getMethod()).thenReturn(mock(Method.class));
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(testInvocation1);
        
        boolean result = invocationMatcher.hasSameMethod(testInvocation2);
        
        assertFalse(result);
    }

    // More edge cases and exception handling tests
}
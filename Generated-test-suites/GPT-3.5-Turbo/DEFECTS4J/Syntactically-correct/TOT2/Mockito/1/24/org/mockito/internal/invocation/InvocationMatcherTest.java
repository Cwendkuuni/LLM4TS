import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.hamcrest.Matchers;

public class InvocationMatcherTest {

    @Test
    public void testGetMethod() {
        // Create a mock invocation
        Invocation invocation = mock(Invocation.class);
        Method method = SomeClass.class.getMethod("someMethod");
        when(invocation.getMethod()).thenReturn(method);

        // Create an InvocationMatcher and check if getMethod returns the correct method
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testMatches() {
        // Create two mock invocations
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);

        // Create an InvocationMatcher with the first invocation and check if matches returns true with the same invocation
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        assertTrue(invocationMatcher.matches(invocation1));

        // Check if matches returns false with a different invocation
        assertFalse(invocationMatcher.matches(invocation2));
    }

    // Add more test cases for other methods in the InvocationMatcher class

}
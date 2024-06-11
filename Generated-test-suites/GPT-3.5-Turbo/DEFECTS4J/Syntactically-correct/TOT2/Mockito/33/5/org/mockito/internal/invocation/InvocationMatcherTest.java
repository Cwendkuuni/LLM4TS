import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;

public class InvocationMatcherTest {

    @Test
    public void testConstructor() {
        // Create an invocation
        Invocation invocation = mock(Invocation.class);
        
        // Create matchers
        List<Matcher> matchers = Arrays.asList(mock(Matcher.class));
        
        // Create an InvocationMatcher object
        InvocationMatcher invMatcher = new InvocationMatcher(invocation, matchers);
        
        // Verify that the Invocation and Matchers are properly set
        assertEquals(invocation, invMatcher.getInvocation());
        assertEquals(matchers, invMatcher.getMatchers());
    }

    @Test
    public void testGetMethod() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        
        InvocationMatcher invMatcher = new InvocationMatcher(invocation);
        
        assertEquals(method, invMatcher.getMethod());
    }

    // Add more test cases for other methods

}
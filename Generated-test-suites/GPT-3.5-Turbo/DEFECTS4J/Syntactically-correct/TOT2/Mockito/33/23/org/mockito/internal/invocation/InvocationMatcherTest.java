import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherGetMethod() {
        // Given
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        // When
        Method result = invocationMatcher.getMethod();
        
        // Then
        assertEquals(method, result);
    }
    
    @Test
    public void testInvocationMatcherGetInvocation() {
        // Given
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        // When
        Invocation result = invocationMatcher.getInvocation();
        
        // Then
        assertEquals(invocation, result);
    }
    
    @Test
    public void testInvocationMatcherGetMatchers() {
        // Given
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(mock(Matcher.class));
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        // When
        List<Matcher> result = invocationMatcher.getMatchers();
        
        // Then
        assertEquals(matchers, result);
    }
    
    // Add more test cases for other methods as well
    
}
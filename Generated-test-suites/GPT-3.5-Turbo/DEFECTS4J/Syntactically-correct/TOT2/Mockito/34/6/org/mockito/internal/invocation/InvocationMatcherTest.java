import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithMatchers() {
        // Given
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Collections.singletonList(mock(Matcher.class));
        
        // When
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        // Then
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }
    
    @Test
    public void testConstructorWithoutMatchers() {
        // Given
        Invocation invocation = mock(Invocation.class);
        
        // When
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        // Then
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }
    
    @Test
    public void testGetMethod() {
        // Given
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        // When
        Method method = invocation.getMethod();
        
        // Then
        assertEquals(method, invocationMatcher.getMethod());
    }
    
    // Add more test cases for other methods as needed
    
}
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithMatchers() {
        // Given
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Collections.emptyList();

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
        Method method = invocation.getClass().getMethod("getMethod");

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Then
        assertEquals(method, invocationMatcher.getMethod());
    }

    // Other test cases for remaining methods can be added here

    // Test edge cases and exception handling scenarios can also be added here

}
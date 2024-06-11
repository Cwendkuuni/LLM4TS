import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;

class InvocationMatcherTest {
    InvocationMatcher invocationMatcher;
    Invocation invocation;
    Invocation actualInvocation;

    @BeforeEach
    void setUp() {
        invocation = Mockito.mock(Invocation.class);
        actualInvocation = Mockito.mock(Invocation.class);
        invocationMatcher = new InvocationMatcher(invocation);
    }

    @Test
    void testMatches_SameInvocation() {
        // Test with same invocation
        assertTrue(invocationMatcher.matches(invocation));
    }

    @Test
    void testMatches_DifferentInvocation_SameMethod_SameArguments() {
        // Test with different invocation but same method and arguments
        InvocationMatcher actualInvocationMatcher = new InvocationMatcher(actualInvocation);
        assertTrue(invocationMatcher.matches(actualInvocationMatcher));
    }

    @Test
    void testMatches_DifferentInvocation_DifferentMethod() {
        // Test with different invocation and different method
        assertFalse(invocationMatcher.matches(actualInvocationMatcher));
    }

    @Test
    void testMatches_DifferentInvocation_SameMethod_DifferentArguments() {
        // Test with different invocation and same method but different arguments
        Object[] differentArguments = new Object[]{1, 2, 3};
        InvocationMatcher actualInvocationMatcher = new InvocationMatcher(new InvocationOnMock(actualInvocation.getMock(), getMethod(), differentArguments));
        assertFalse(invocationMatcher.matches(actualInvocationMatcher));
    }

    @Test
    void testMatches_NullInvocation() {
        assertFalse(invocationMatcher.matches(null));
    }
}
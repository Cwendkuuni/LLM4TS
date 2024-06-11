import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.Invocation;

public class InvocationMatcherTest {

    private Invocation dummyInvocation1 = new Invocation(null, null, null);
    private Invocation dummyInvocation2 = new Invocation(null, null, null);

    @Test
    public void testConstructorWithMatchers() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(dummyInvocation1, Collections.emptyList());
        assertNotNull(invocationMatcher);
        assertEquals(dummyInvocation1, invocationMatcher.getInvocation());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testConstructorWithoutMatchers() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(dummyInvocation1);
        assertNotNull(invocationMatcher);
        assertEquals(dummyInvocation1, invocationMatcher.getInvocation());
        assertEquals(dummyInvocation1.argumentsToMatchers(), invocationMatcher.getMatchers());
    }

    @Test
    public void testGetMethod() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(dummyInvocation1);
        assertEquals(dummyInvocation1.getMethod(), invocationMatcher.getMethod());
    }

    @Test
    public void testGetInvocation() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(dummyInvocation1);
        assertEquals(dummyInvocation1, invocationMatcher.getInvocation());
    }

    // Additional test methods can be added here for other public methods in the class

}
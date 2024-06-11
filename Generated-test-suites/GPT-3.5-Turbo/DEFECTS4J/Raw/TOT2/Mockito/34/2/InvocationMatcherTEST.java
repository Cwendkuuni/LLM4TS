import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.exceptions.PrintableInvocation;

public class InvocationMatcherTest {

    @Test
    public void testGetMethod() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(invocation.getMethod(), invocationMatcher.getMethod());
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(invocation, invocationMatcher.getInvocation());
    }

    @Test
    public void testGetMatchers() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertNotNull(invocationMatcher.getMatchers());
    }

    @Test
    public void testToString() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertNotNull(invocationMatcher.toString());
    }

    @Test
    public void testMatches() {
        Invocation invocation1 = new Invocation();
        Invocation invocation2 = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        assertTrue(invocationMatcher.matches(invocation2));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation1 = new Invocation();
        Invocation invocation2 = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        assertTrue(invocationMatcher.hasSimilarMethod(invocation2));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = new Invocation();
        Invocation invocation2 = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        assertTrue(invocationMatcher.hasSameMethod(invocation2));
    }

}
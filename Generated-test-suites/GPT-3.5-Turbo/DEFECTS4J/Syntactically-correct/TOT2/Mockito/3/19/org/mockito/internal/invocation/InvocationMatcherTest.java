import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.MatcherDecorator;
import org.mockito.internal.matchers.CapturesArguments;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithMatchers() {
        Invocation invocation = new Invocation();
        MatcherDecorator matcher = new MatcherDecorator();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matcher);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matcher, invocationMatcher.getMatchers().get(0));
    }

    @Test
    public void testConstructorWithoutMatchers() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testGetMethod() {
        Invocation invocation = new Invocation();
        Method method = new Method();
        invocation.setMethod(method);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(method, invocationMatcher.getMethod());
    }

    // Add more test cases for each method as required

}
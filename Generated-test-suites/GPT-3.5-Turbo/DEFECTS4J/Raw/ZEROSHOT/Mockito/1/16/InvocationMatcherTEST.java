import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.matchers.MatcherDecorator;
import org.mockito.internal.matchers.VarargMatcher;

public class InvocationMatcherTest {

    @Test
    public void testConstructor() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testConstructorWithMatchers() {
        Invocation invocation = new Invocation();
        CapturesArguments capturesArguments = new CapturesArguments();
        MatcherDecorator matcherDecorator = new MatcherDecorator();
        VarargMatcher varargMatcher = new VarargMatcher();
        invocation.addArgument(capturesArguments);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.singletonList(matcherDecorator));
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(1, invocationMatcher.getMatchers().size());
        assertEquals(matcherDecorator, invocationMatcher.getMatchers().get(0));
        
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation, Collections.singletonList(varargMatcher));
        assertEquals(invocation, invocationMatcher2.getInvocation());
        assertEquals(1, invocationMatcher2.getMatchers().size());
        assertEquals(varargMatcher, invocationMatcher2.getMatchers().get(0));
    }

    @Test
    public void testGetMethod() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(invocation.getMethod(), invocationMatcher.getMethod());
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
        assertTrue(invocationMatcher.matches(invocation1));
        assertFalse(invocationMatcher.matches(invocation2));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation1 = new Invocation();
        Invocation invocation2 = new Invocation();
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        assertTrue(invocationMatcher.hasSimilarMethod(invocation1));
        assertFalse(invocationMatcher.hasSimilarMethod(invocation2));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = new Invocation();
        Invocation invocation2 = new Invocation();
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        assertTrue(invocationMatcher.hasSameMethod(invocation1));
        assertFalse(invocationMatcher.hasSameMethod(invocation2));
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(invocation.getLocation(), invocationMatcher.getLocation());
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = new Invocation();
        CapturesArguments capturesArguments = new CapturesArguments();
        MatcherDecorator matcherDecorator = new MatcherDecorator();
        VarargMatcher varargMatcher = new VarargMatcher();
        invocation.addArgument(capturesArguments);
        invocation.addArgument(varargMatcher);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        invocationMatcher.captureArgumentsFrom(invocation);
        assertEquals(invocation.getArguments()[0], capturesArguments.getCapturedArguments());
        
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation, Collections.singletonList(matcherDecorator));
        invocationMatcher2.captureArgumentsFrom(invocation);
        assertEquals(invocation.getArguments()[0], capturesArguments.getCapturedArguments());
        
        InvocationMatcher invocationMatcher3 = new InvocationMatcher(invocation, Collections.singletonList(varargMatcher));
        invocationMatcher3.captureArgumentsFrom(invocation);
        assertEquals(invocation.getArguments()[0], capturesArguments.getCapturedArguments());
    }

    @Test
    public void testCreateFrom() {
        Invocation invocation1 = new Invocation();
        Invocation invocation2 = new Invocation();
        
        List<Invocation> invocations = new LinkedList<>();
        invocations.add(invocation1);
        invocations.add(invocation2);
        
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        assertEquals(2, invocationMatchers.size());
        assertEquals(invocation1, invocationMatchers.get(0).getInvocation());
        assertEquals(invocation2, invocationMatchers.get(1).getInvocation());
    }

}
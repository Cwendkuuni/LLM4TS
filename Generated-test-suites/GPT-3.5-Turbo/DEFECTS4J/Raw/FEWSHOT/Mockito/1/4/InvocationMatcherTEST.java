import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli.*;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithMatchers() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(new Matcher());
        matchers.add(new Matcher());

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertNull(invocationMatcher.getMethod());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithoutMatchers() {
        Invocation invocation = new Invocation();
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertNull(invocationMatcher.getMethod());
        assertNotNull(invocationMatcher.getMatchers());
        assertEquals(0, invocationMatcher.getMatchers().size());
    }

    @Test
    public void testToString() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(new Matcher());
        matchers.add(new Matcher());
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        String expected = "Matchers: [Matcher@1, Matcher@2], Invocation: Invocation@0";
        assertEquals(expected, invocationMatcher.toString());
    }

    @Test
    public void testMatchesWithMatchingInvocation() {
        Invocation invocation1 = new Invocation();
        Invocation invocation2 = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        assertTrue(invocationMatcher.matches(invocation2));
    }

    @Test
    public void testMatchesWithNonMatchingInvocation() {
        Invocation invocation1 = new Invocation();
        Invocation invocation2 = new Invocation();
        invocation2.setArguments(new Object[] {"arg1", "arg2"});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        assertFalse(invocationMatcher.matches(invocation2));
    }

    @Test
    public void testHasSimilarMethodWithMatchingInvocation() {
        Invocation invocation1 = new Invocation();
        Invocation invocation2 = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        assertTrue(invocationMatcher.hasSimilarMethod(invocation2));
    }

    @Test
    public void testHasSimilarMethodWithNonMatchingInvocation() {
        Invocation invocation1 = new Invocation();
        invocation1.setMethod(new Method("methodName", null));
        Invocation invocation2 = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        assertFalse(invocationMatcher.hasSimilarMethod(invocation2));
    }

    @Test
    public void testHasSameMethodWithMatchingInvocation() {
        Invocation invocation1 = new Invocation();
        Invocation invocation2 = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        assertTrue(invocationMatcher.hasSameMethod(invocation2));
    }

    @Test
    public void testHasSameMethodWithNonMatchingInvocation() {
        Invocation invocation1 = new Invocation();
        invocation1.setMethod(new Method("methodName", null));
        Invocation invocation2 = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        assertFalse(invocationMatcher.hasSameMethod(invocation2));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = new Invocation();
        invocation.setArguments(new Object[] {"arg1", "arg2"});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        invocationMatcher.captureArgumentsFrom(invocation);

        // Verify that each argument captured correctly
        assertNotNull(invocation.getArguments()[0]);
        assertNotNull(invocation.getArguments()[1]);
        assertEquals("arg1", invocation.getArguments()[0]);
        assertEquals("arg2", invocation.getArguments()[1]);
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new ArrayList<>();
        Invocation invocation1 = new Invocation();
        Invocation invocation2 = new Invocation();
        invocations.add(invocation1);
        invocations.add(invocation2);

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        assertEquals(invocations.size(), invocationMatchers.size());
        assertEquals(invocations.get(0), invocationMatchers.get(0).getInvocation());
        assertEquals(invocations.get(1), invocationMatchers.get(1).getInvocation());
    }
}
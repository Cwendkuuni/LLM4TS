import org.hamcrest.Matchers;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class InvocationMatcherTest {

    private Invocation invocation;
    private InvocationMatcher invocationMatcher;

    @Test
    public void testConstructor1() {
        invocation = new Invocation(null, null);
        invocationMatcher = new InvocationMatcher(invocation);
        assertNotNull(invocationMatcher);
        assertNotNull(invocationMatcher.getMethod());
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testConstructor2() {
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(Matchers.notNullValue());
        invocation = new Invocation(null, null);
        invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertNotNull(invocationMatcher);
        assertNotNull(invocationMatcher.getMethod());
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertFalse(invocationMatcher.getMatchers().isEmpty());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testGetMethod() {
        Method method = new Method();
        invocation = new Invocation(method, null);
        invocationMatcher = new InvocationMatcher(invocation);
        assertNotNull(invocationMatcher.getMethod());
        assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testToString() {
        //TODO: Implement test case
    }

    @Test
    public void testMatches() {
        //TODO: Implement test case
    }

    @Test
    public void testSafelyArgumentsMatch() {
        //TODO: Implement test case
    }

    @Test
    public void testHasSimilarMethod() {
        //TODO: Implement test case
    }

    @Test
    public void testHasSameMethod() {
        //TODO: Implement test case
    }

    @Test
    public void testGetLocation() {
        //TODO: Implement test case
    }

    @Test
    public void testToStringPrintSettings() {
        //TODO: Implement test case
    }

    @Test
    public void testCaptureArgumentsFrom() {
        //TODO: Implement test case
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new ArrayList<>();
        invocations.add(new Invocation(null, null));
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        assertNotNull(invocationMatchers);
        assertFalse(invocationMatchers.isEmpty());
        assertEquals(invocationMatchers.size(), invocations.size());
    }

}
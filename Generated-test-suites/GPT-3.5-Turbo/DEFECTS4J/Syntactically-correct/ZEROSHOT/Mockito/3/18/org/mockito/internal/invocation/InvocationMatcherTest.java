import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.reflect.Method;
import java.util.List;
import java.util.ArrayList;

public class InvocationMatcherTest {

    @Test
    public void testConstructor1() {
        Invocation invocation = new Invocation(/* parameters */);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertNotNull(invocationMatcher);
    }

    @Test
    public void testConstructor2() {
        Invocation invocation = new Invocation(/* parameters */);
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertNotNull(invocationMatcher);
    }

    @Test
    public void testGetMethod() {
        Invocation invocation = new Invocation(/* parameters */);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Method method = invocationMatcher.getMethod();
        assertNotNull(method);
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = new Invocation(/* parameters */);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation result = invocationMatcher.getInvocation();
        assertNotNull(result);
    }

    @Test
    public void testGetMatchers() {
        Invocation invocation = new Invocation(/* parameters */);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        List<Matcher> matchers = invocationMatcher.getMatchers();
        assertNotNull(matchers);
    }

    @Test
    public void testToString() {
        Invocation invocation = new Invocation(/* parameters */);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        String result = invocationMatcher.toString();
        assertNotNull(result);
    }

    @Test
    public void testMatches() {
        Invocation invocation = new Invocation(/* parameters */);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation actual = new Invocation(/* parameters */);
        boolean result = invocationMatcher.matches(actual);
        assertTrue(result);
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = new Invocation(/* parameters */);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation candidate = new Invocation(/* parameters */);
        boolean result = invocationMatcher.hasSimilarMethod(candidate);
        assertTrue(result);
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation = new Invocation(/* parameters */);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation candidate = new Invocation(/* parameters */);
        boolean result = invocationMatcher.hasSameMethod(candidate);
        assertTrue(result);
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = new Invocation(/* parameters */);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Location result = invocationMatcher.getLocation();
        assertNotNull(result);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = new Invocation(/* parameters */);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation otherInvocation = new Invocation(/* parameters */);
        invocationMatcher.captureArgumentsFrom(otherInvocation);
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new ArrayList<>();
        InvocationMatcher.createFrom(invocations);
    }
}
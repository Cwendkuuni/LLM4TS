
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class InvocationMatcherTest {

    @Test
    public void testConstructorInvocationMatcher() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertNotNull(invocationMatcher);
    }

    @Test
    public void testConstructorInvocationMatcherWithMatchers() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = Collections.emptyList();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertNotNull(invocationMatcher);
    }

    @Test
    public void testGetMethod() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Method method = invocationMatcher.getMethod();
        assertNotNull(method);
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation returnedInvocation = invocationMatcher.getInvocation();
        assertEquals(invocation, returnedInvocation);
    }

    @Test
    public void testGetMatchers() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        List<Matcher> matchers = invocationMatcher.getMatchers();
        assertNotNull(matchers);
    }

    @Test
    public void testToString() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        String toString = invocationMatcher.toString();
        assertNotNull(toString);
    }

    @Test
    public void testMatches() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation actual = new Invocation();
        boolean matches = invocationMatcher.matches(actual);
        assertFalse(matches);
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation candidate = new Invocation();
        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidate);
        assertFalse(hasSimilarMethod);
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation candidate = new Invocation();
        boolean hasSameMethod = invocationMatcher.hasSameMethod(candidate);
        assertFalse(hasSameMethod);
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Location location = invocationMatcher.getLocation();
        assertNotNull(location);
    }

    @Test
    public void testToStringWithPrintSettings() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        PrintSettings printSettings = new PrintSettings();
        String toString = invocationMatcher.toString(printSettings);
        assertNotNull(toString);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation i = new Invocation();
        invocationMatcher.captureArgumentsFrom(i);
        // Check that no exception is thrown
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = Collections.singletonList(new Invocation());
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        assertNotNull(invocationMatchers);
        assertEquals(invocations.size(), invocationMatchers.size());
    }
}

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.matchers.MatcherDecorator;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.invocation.Location;

import java.lang.reflect.Method;
import java.util.*;

import static org.junit.Assert.*;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithInvocationAndMatchers() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithInvocation() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testGetMethod() {
        Method method = new Method();
        Invocation invocation = new Invocation(method);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(method, invocationMatcher.getMethod());
    }
    
    @Test
    public void testToString() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals(new PrintSettings().print(matchers, invocation), invocationMatcher.toString());
    }

    @Test
    public void testMatches() {
        Invocation invocation1 = new Invocation();
        Invocation invocation2 = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        assertFalse(invocationMatcher.matches(invocation2));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation1 = new Invocation();
        Invocation invocation2 = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        assertFalse(invocationMatcher.hasSimilarMethod(invocation2));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = new Invocation();
        Invocation invocation2 = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        assertFalse(invocationMatcher.hasSameMethod(invocation2));
    }

    @Test
    public void testGetLocation() {
        Location location = new Location();
        Invocation invocation = new Invocation(location);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(location, invocationMatcher.getLocation());
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = new Invocation();
        InvocationsMatcher invocationsMatcher = new InvocationsMatcher();
        invocationMatcher.captureArgumentsFrom(invocation);

        // assert something
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
        assertTrue(invocationMatchers.contains(new InvocationMatcher(invocation1)));
        assertTrue(invocationMatchers.contains(new InvocationMatcher(invocation2)));
    }
}
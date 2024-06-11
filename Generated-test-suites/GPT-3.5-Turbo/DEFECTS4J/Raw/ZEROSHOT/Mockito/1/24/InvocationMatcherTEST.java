import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.Invocation;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class InvocationMatcherTest {

    private Invocation invocation;
    private List<Matcher> matchers;
    private InvocationMatcher invocationMatcher;

    @Before
    public void setup() {
        invocation = mock(Invocation.class);
        matchers = Collections.emptyList();
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void testGetMethod() {
        Method method = invocation.getMethod();
        assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testGetInvocation() {
        assertEquals(invocation, invocationMatcher.getInvocation());
    }

    @Test
    public void testGetMatchers() {
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testToString() {
        String expected = "Print Settings";
        assertEquals(expected, invocationMatcher.toString());
    }

    @Test
    public void testMatches() {
        Invocation actual = mock(Invocation.class);
        assertEquals(false, invocationMatcher.matches(actual));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation candidate = mock(Invocation.class);
        assertEquals(false, invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSameMethod() {
        Invocation candidate = mock(Invocation.class);
        assertEquals(false, invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testGetLocation() {
        assertEquals(invocation.getLocation(), invocationMatcher.getLocation());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        invocationMatcher.captureArgumentsFrom(invocation);
    }

    @Test
    public void testCreateFrom() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<Invocation> invocations = Arrays.asList(invocation1, invocation2);

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        assertEquals(2, invocationMatchers.size());
        assertEquals(invocation1, invocationMatchers.get(0).getInvocation());
        assertEquals(invocation2, invocationMatchers.get(1).getInvocation());
    }
}
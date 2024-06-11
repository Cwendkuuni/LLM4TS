import org.junit.*;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import org.mockito.internal.matchers.CapturesArguments;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class InvocationMatcherTest {

    private Invocation invocation;
    private List<Matcher> matchers;

    @Before
    public void setUp() {
        invocation = Mockito.mock(Invocation.class);
        matchers = Collections.emptyList();
    }

    @Test
    public void testGetMethod() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Method method = invocationMatcher.getMethod();
        assertNotNull(method);
    }

    @Test
    public void testGetInvocation() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Invocation inv = invocationMatcher.getInvocation();
        assertNotNull(inv);
    }

    @Test
    public void testGetMatchers() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        List<Matcher> matchers = invocationMatcher.getMatchers();
        assertNotNull(matchers);
    }

    @Test
    public void testToString() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        String str = invocationMatcher.toString();
        assertNotNull(str);
    }

    @Test
    public void testMatches() {
        Invocation actual = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        boolean matches = invocationMatcher.matches(actual);
        assertFalse(matches);
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation candidate = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        boolean hasSimilar = invocationMatcher.hasSimilarMethod(candidate);
        assertFalse(hasSimilar);
    }

    @Test
    public void testHasSameMethod() {
        Invocation candidate = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        boolean hasSame = invocationMatcher.hasSameMethod(candidate);
        assertFalse(hasSame);
    }

    @Test
    public void testGetLocation() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Location location = invocationMatcher.getLocation();
        assertNotNull(location);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        invocationMatcher.captureArgumentsFrom(invocation);
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = Collections.emptyList();
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        assertNotNull(invocationMatchers);
    }
}
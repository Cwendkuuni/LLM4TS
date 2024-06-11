import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import org.mockito.internal.invocation.InvocationMatcher;
import java.lang.reflect.Method;
import java.util.*;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithInvocationsAndMatchers() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = new ArrayList<Matcher>();

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithInvocations() {
        Invocation invocation = new Invocation();

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(Collections.<Matcher>emptyList(), invocationMatcher.getMatchers());
    }
    
    @Test
    public void testGetMethod() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Method method = invocation.getMethod();
        assertEquals(method, invocationMatcher.getMethod());
    }
    
    @Test
    public void testToString() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        String expected = new PrintSettings().print(invocationMatcher.getMatchers(), invocationMatcher.getInvocation());
        String actual = invocationMatcher.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testMatches() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation actual = new Invocation();

        boolean result = invocationMatcher.matches(actual);

        assertTrue(result);
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Object[] actualArgs = new Object[]{};

        boolean result = invocationMatcher.safelyArgumentsMatch(actualArgs);

        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation candidate = new Invocation();

        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        assertFalse(result);
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation candidate = new Invocation();

        boolean result = invocationMatcher.hasSameMethod(candidate);

        assertFalse(result);
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Location location = invocation.getLocation();

        assertEquals(location, invocationMatcher.getLocation());
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation invocation2 = new Invocation();

        invocationMatcher.captureArgumentsFrom(invocation2);
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new ArrayList<Invocation>();

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        assertEquals(invocations.size(), invocationMatchers.size());
        for (int i = 0; i < invocations.size(); i++) {
            Invocation invocation = invocations.get(i);
            InvocationMatcher invocationMatcher = invocationMatchers.get(i);
            assertEquals(invocation, invocationMatcher.getInvocation());
        }
    }
}
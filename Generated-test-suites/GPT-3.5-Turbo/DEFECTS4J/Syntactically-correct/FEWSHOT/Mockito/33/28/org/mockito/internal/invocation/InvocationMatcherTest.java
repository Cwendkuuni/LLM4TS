import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.hamcrest.Matcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class InvocationMatcherTest {

    private Invocation invocation;
    private List<Matcher> matchers;
    private InvocationMatcher invocationMatcher;

    @Before
    public void setUp() {
        invocation = mock(Invocation.class);
        matchers = new LinkedList<>();
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void testGetMethod() {
        Method method = invocation.getMethod();
        assertNotNull(method);
        assertNotEquals(invocationMatcher.getMethod(), method);
    }

    @Test
    public void testGetInvocation() {
        assertNotNull(invocationMatcher.getInvocation());
        assertEquals(invocationMatcher.getInvocation(), invocation);
    }

    @Test
    public void testGetMatchers() {
        assertNotNull(invocationMatcher.getMatchers());
        assertEquals(invocationMatcher.getMatchers(), matchers);
    }

    @Test
    public void testToString() {
        String toString = invocation.toString(matchers, new PrintSettings());
        assertNotNull(toStrng);
        assertEquals(invocationMatcher.toString(), toString);
    }

    @Test
    public void testMatches() {
        assertFalse(invocationMatcher.matches(invocation));
        assertFalse(invocationMatcher.matches(null));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation candidate = mock(Invocation.class);
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
        assertFalse(invocationMatcher.hasSimilarMethod(null));
    }

    @Test
    public void testHasSameMethod() {
        Invocation candidate = mock(Invocation.class);
        assertFalse(invocationMatcher.hasSameMethod(candidate));
        assertFalse(invocationMatcher.hasSameMethod(null));
    }

    @Test
    public void testGetLocation() {
        Location location = invocation.getLocation();
        assertNotNull(location);
        assertEquals(invocationMatcher.getLocation(), location);
    }

    @Test
    public void testToStringWithPrintSettings() {
        PrintSettings printSettings = new PrintSettings();
        String toString = invocation.toString(matchers, printSettings);
        assertNotNull(toString);
        assertEquals(invocationMatcher.toString(printSettings), toString);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation i = mock(Invocation.class);
        invocationMatcher.captureArgumentsFrom(i);
        assertTrue(matchers.isEmpty());
        assertFalse(matchers.contains(CapturesArguments));
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new LinkedList<>();
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        assertNotNull(invocationMatchers);
        assertTrue(invocationMatchers.isEmpty());
    }
}
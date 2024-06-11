import org.hamcrest.Matcher;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;

    @Before
    public void setUp() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = new LinkedList<>();
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void testGetInvocation() {
        Invocation actual = invocationMatcher.getInvocation();
        assertNotNull(actual);
        assertEquals(invocationMatcher.getInvocation(), actual);
    }

    @Test
    public void testGetMethod() {
        Method method = invocationMatcher.getMethod();
        assertNotNull(method);
        assertEquals(invocationMatcher.getMethod(), method);
    }

    @Test
    public void testGetMatchers() {
        List<Matcher> matchers = invocationMatcher.getMatchers();
        assertNotNull(matchers);
        assertEquals(invocationMatcher.getMatchers(), matchers);
    }

    @Test
    public void testToString() {
        String actual = invocationMatcher.toString();
        assertNotNull(actual);
    }

    @Test
    public void testMatches() {
        Invocation actual = new Invocation();
        assertTrue(invocationMatcher.matches(actual));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation candidate = new Invocation();
        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSameMethod() {
        Invocation candidate = new Invocation();
        assertTrue(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testGetLocation() {
        Location location = invocationMatcher.getLocation();
        assertNotNull(location);
    }

    @Test
    public void testToStringWithPrintSettings() {
        PrintSettings printSettings = new PrintSettings();
        String actual = invocationMatcher.toString(printSettings);
        assertNotNull(actual);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation i = new Invocation();
        invocationMatcher.captureArgumentsFrom(i);
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new LinkedList<>();
        List<InvocationMatcher> actual = InvocationMatcher.createFrom(invocations);
        assertNotNull(actual);
    }
}
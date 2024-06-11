import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;
    private Invocation invocation;

    @Before
    public void setUp() {
        invocation = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void testGetMethod() {
        Method expected = invocation.getMethod();
        Method actual = invocationMatcher.getMethod();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetInvocation() {
        Invocation expected = invocation;
        Invocation actual = invocationMatcher.getInvocation();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMatchers() {
        List<Matcher> expected = Collections.emptyList();
        List<Matcher> actual = invocationMatcher.getMatchers();
        assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        String expected = "PrintSetting";
        String actual = invocationMatcher.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testMatches() {
        Invocation actualInvocation = mock(Invocation.class);
        boolean expected = true;
        boolean actual = invocationMatcher.matches(actualInvocation);
        assertEquals(expected, actual);
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation candidate = mock(Invocation.class);
        boolean expected = true;
        boolean actual = invocationMatcher.hasSimilarMethod(candidate);
        assertEquals(expected, actual);
    }

    @Test
    public void testHasSameMethod() {
        Invocation candidate = mock(Invocation.class);
        boolean expected = true;
        boolean actual = invocationMatcher.hasSameMethod(candidate);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetLocation() {
        Location expected = invocation.getLocation();
        Location actual = invocationMatcher.getLocation();
        assertEquals(expected, actual);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCaptureArgumentsFrom_VarArgs() {
        Invocation varArgsInvocation = mock(Invocation.class);
        invocationMatcher.captureArgumentsFrom(varArgsInvocation);
    }

    @Test
    public void testCaptureArgumentsFrom_NormalArgs() {
        Invocation normalArgsInvocation = mock(Invocation.class);
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(mock(Matcher.class));
        invocationMatcher = new InvocationMatcher(normalArgsInvocation, matchers);

        invocationMatcher.captureArgumentsFrom(normalArgsInvocation);

        // TODO: Add assertions for capturing arguments
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new LinkedList<>();
        invocations.add(mock(Invocation.class));

        List<InvocationMatcher> expected = new LinkedList<>();
        expected.add(invocationMatcher);

        List<InvocationMatcher> actual = InvocationMatcher.createFrom(invocations);
        assertEquals(expected, actual);
    }
}
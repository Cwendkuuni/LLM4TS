import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class InvocationMatcherTest {

    private Invocation invocation;
    private InvocationMatcher matcher;

    @Before
    public void setUp() throws Exception {
        invocation = mock(Invocation.class);
        matcher = new InvocationMatcher(invocation);
    }

    @Test
    public void getMethod_shouldReturnMethod() {
        Method method = mock(Method.class);
        invocation.getMethodAndReturnType().getMethod();
        assertEquals(method, matcher.getMethod());
    }

    @Test
    public void getInvocation_shouldReturnInvocation() {
        assertEquals(invocation, matcher.getInvocation());
    }

    @Test
    public void getMatchers_shouldReturnMatchers() {
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(mock(Matcher.class));
        matcher = new InvocationMatcher(invocation, matchers);
        assertEquals(matchers, matcher.getMatchers());
    }

    @Test
    public void toString_shouldReturnStringRepresentation() {
        String expected = "[Matcher1, Matcher2]";
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(mock(Matcher.class));
        matchers.add(mock(Matcher.class));
        invocation.getArguments();
        assertEquals(expected, matcher.toString());
    }

    @Test
    public void matches_withSameInvocation_shouldReturnTrue() {
        Invocation actualInvocation = mock(Invocation.class);
        actualInvocation.getMock();
        actualInvocation.getMethodAndReturnType();
        actualInvocation.getArguments();
        actualInvocation.isVerified();
        actualInvocation.getMatchingLocation();
        actualInvocation.getStackTrace();
        assertTrue(matcher.matches(actualInvocation));
    }

    @Test
    public void matches_withDifferentMock_shouldReturnFalse() {
        Invocation actualInvocation = mock(Invocation.class);
        actualInvocation.getMock();
        assertFalse(matcher.matches(actualInvocation));
    }

    @Test
    public void matches_withDifferentMethod_shouldReturnFalse() {
        Invocation actualInvocation = mock(Invocation.class);
        actualInvocation.getMock();
        actualInvocation.getMethodAndReturnType();
        assertFalse(matcher.matches(actualInvocation));
    }

    @Test
    public void matches_withDifferentArguments_shouldReturnFalse() {
        Invocation actualInvocation = mock(Invocation.class);
        actualInvocation.getMock();
        actualInvocation.getMethodAndReturnType();
        actualInvocation.getArguments();
        assertFalse(matcher.matches(actualInvocation));
    }

    @Test
    public void hasSimilarMethod_withDifferentMethodName_shouldReturnFalse() {
        Invocation candidate = mock(Invocation.class);
        Method method = mock(Method.class);
        candidate.getMethodAndReturnType().getMethod();
        assertFalse(matcher.hasSimilarMethod(candidate));
    }

    @Test
    public void hasSimilarMethod_withVerifiedCandidate_shouldReturnFalse() {
        Invocation candidate = mock(Invocation.class);
        candidate.isVerified();
        assertFalse(matcher.hasSimilarMethod(candidate));
    }

    @Test
    public void hasSimilarMethod_withDifferentMock_shouldReturnFalse() {
        Invocation candidate = mock(Invocation.class);
        candidate.getMock();
        assertFalse(matcher.hasSimilarMethod(candidate));
    }

    @Test
    public void hasSimilarMethod_withSameMethodNameSameMock_shouldReturnTrue() {
        Invocation candidate = mock(Invocation.class);
        Method method = mock(Method.class);
        candidate.getMethodAndReturnType().getMethod();
        candidate.isVerified();
        candidate.getMock();
        assertTrue(matcher.hasSimilarMethod(candidate));
    }

    @Test
    public void hasSameMethod_withDifferentMethodName_shouldReturnFalse() {
        Invocation candidate = mock(Invocation.class);
        Method method = mock(Method.class);
        candidate.getMethodAndReturnType().getMethod();
        assertFalse(matcher.hasSameMethod(candidate));
    }

    @Test
    public void hasSameMethod_withDifferentMethodParameters_shouldReturnFalse() {
        Invocation candidate = mock(Invocation.class);
        Method method = mock(Method.class);
        candidate.getMethodAndReturnType().getMethod();
        assertFalse(matcher.hasSameMethod(candidate));
    }

    @Test
    public void hasSameMethod_withSameMethodNameSameParameters_shouldReturnTrue() {
        Invocation candidate = mock(Invocation.class);
        Method method = mock(Method.class);
        candidate.getMethodAndReturnType().getMethod();
        assertTrue(matcher.hasSameMethod(candidate));
    }

    @Test
    public void getLocation_shouldReturnLocation() {
        Location location = mock(Location.class);
        invocation.getMatchingLocation();
        assertEquals(location, matcher.getLocation());
    }

    @Test
    public void captureArgumentsFrom_withVarArgs_shouldCaptureArguments() {
        Invocation invocation = mock(Invocation.class);
        Object[] arguments = {1, 2, 3};
        invocation.getMethodAndReturnType().getMethod();
        invocation.getArguments();
        matcher.captureArgumentsFrom(invocation);
        // Verify that arguments are captured correctly
        assertThat(invocation.getArgumentAt(0, int.class), is(1));
        assertThat(invocation.getArgumentAt(1, int.class), is(2));
        assertThat(invocation.getArgumentAt(2, int.class), is(3));
    }

    @Test
    public void captureArgumentsFrom_withoutVarArgs_shouldCaptureArguments() {
        Invocation invocation = mock(Invocation.class);
        Object[] arguments = {1, 2, 3};
        invocation.getMethodAndReturnType().getMethod();
        invocation.getArguments();
        matcher.captureArgumentsFrom(invocation);
        // Verify that arguments are captured correctly
        assertThat(invocation.getArgumentAt(0, int.class), is(1));
        assertThat(invocation.getArgumentAt(1, int.class), is(2));
        assertThat(invocation.getArgumentAt(2, int.class), is(3));
    }

    @Test
    public void createFrom_shouldCreateInvocationMatchers() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<Invocation> invocations = Arrays.asList(invocation1, invocation2);
        List<InvocationMatcher> matchers = InvocationMatcher.createFrom(invocations);
        // Verify that InvocationMatchers are created correctly
        assertEquals(2, matchers.size());
        assertEquals(invocation1, matchers.get(0).getInvocation());
        assertEquals(invocation2, matchers.get(1).getInvocation());
    }
}
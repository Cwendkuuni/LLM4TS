import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    @Mock
    private Invocation invocation;

    private InvocationMatcher invocationMatcher;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        invocationMatcher = new InvocationMatcher(invocation);
    }

    @Test
    public void testConstructorWithMatcherList() {
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(equalTo("arg1"));
        matchers.add(instanceOf(String.class));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithoutMatcherList() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(Collections.emptyList(), invocationMatcher.getMatchers());
    }

    @Test
    public void testGetMethod() {
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);

        Method result = invocationMatcher.getMethod();

        assertEquals(method, result);
    }

    @Test
    public void testGetInvocation() {
        Invocation result = invocationMatcher.getInvocation();

        assertEquals(invocation, result);
    }

    @Test
    public void testGetMatchers() {
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(equalTo("arg1"));
        matchers.add(instanceOf(String.class));
        invocationMatcher = new InvocationMatcher(invocation, matchers);

        List<Matcher> result = invocationMatcher.getMatchers();

        assertEquals(matchers, result);
    }

    @Test
    public void testToString() {
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(equalTo("arg1"));
        matchers.add(instanceOf(String.class));
        invocationMatcher = new InvocationMatcher(invocation, matchers);

        String result = invocationMatcher.toString();

        assertEquals("Mockito Matchers:\n" +
            "  - equal to \"arg1\"\n" +
            "  - an instance of String\n", result);
    }

    @Test
    public void testMatches_WithMatchingInvocation_ReturnsTrue() {
        Invocation actual = mock(Invocation.class);
        when(actual.getMock()).thenReturn(mock(Object.class));
        when(actual.getArguments()).thenReturn(new Object[] {"arg1", 2, true});
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getArguments()).thenReturn(new Object[] {"arg1", 2, true});

        boolean result = invocationMatcher.matches(actual);

        assertTrue(result);
    }

    @Test
    public void testMatches_WithNonMatchingInvocation_ReturnsFalse() {
        Invocation actual = mock(Invocation.class);
        when(actual.getMock()).thenReturn(mock(Object.class));
        when(actual.getArguments()).thenReturn(new Object[] {"arg1", 2, true});
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getArguments()).thenReturn(new Object[] {"arg2", 2, true});

        boolean result = invocationMatcher.matches(actual);

        assertFalse(result);
    }

    @Test(expected = Throwable.class)
    public void testSafelyArgumentsMatch_withException_ReturnsFalse() {
        Object[] actualArgs = new Object[] {"arg1", 2, true};
        doThrow(Throwable.class).when(invocation).getArguments();

        boolean result = invocationMatcher.safelyArgumentsMatch(actualArgs);

        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_WithMatchingCandidate_ReturnsTrue() {
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(method);
        when(invocation.getMethod()).thenReturn(method);
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(candidate.getMethod().getName()).thenReturn("testMethod");
        when(invocation.getMethod().getName()).thenReturn("testMethod");
        when(invocation.getArguments()).thenReturn(new Object[] {"arg1", 2, true});
        when(candidate.getArguments()).thenReturn(new Object[] {"arg2", 2, true});
        when(invocation.getRawArguments()).thenReturn(new Object[] {"arg1", 2, true});
        when(candidate.getRawArguments()).thenReturn(new Object[] {"arg2", 2, true});

        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        assertTrue(result);
    }

    @Test
    public void testHasSimilarMethod_WithNonMatchingCandidate_ReturnsFalse() {
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getMethod()).thenReturn(method);
        when(candidate.isVerified()).thenReturn(true);
        when(invocation.isVerified()).thenReturn(false);
        when(candidate.getMethod().getName()).thenReturn("testMethod");
        when(invocation.getMethod().getName()).thenReturn("testMethod");

        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        assertFalse(result);
    }

    @Test
    public void testHasSameMethod_WithMatchingMethod_ReturnsTrue() {
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(method.getName()).thenReturn("testMethod");
        Method candidateMethod = mock(Method.class);
        when(candidate.getMethod()).thenReturn(candidateMethod);
        when(candidateMethod.getName()).thenReturn("testMethod");

        boolean result = invocationMatcher.hasSameMethod(candidate);

        assertTrue(result);
    }

    @Test
    public void testHasSameMethod_WithNonMatchingMethod_ReturnsFalse() {
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(method.getName()).thenReturn("testMethod");
        Method candidateMethod = mock(Method.class);
        when(candidate.getMethod()).thenReturn(candidateMethod);
        when(candidateMethod.getName()).thenReturn("differentMethod");

        boolean result = invocationMatcher.hasSameMethod(candidate);

        assertFalse(result);
    }

    @Test
    public void testGetLocation() {
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);

        Location result = invocationMatcher.getLocation();

        assertEquals(location, result);
    }

    @Test
    public void testCaptureArgumentsFrom_WithVarArgs() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(method);
        when(method.isVarArgs()).thenReturn(true);
        when(invocation.getRawArguments()).thenReturn(new Object[] {"arg1", "arg2"});

        Matcher arg1Matcher = mock(Matcher.class);
        Matcher arg2Matcher = mock(Matcher.class);
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(arg1Matcher);
        matchers.add(arg2Matcher);
        invocationMatcher = new InvocationMatcher(invocation, matchers);

        Invocation arg1Invocation = mock(Invocation.class);
        Invocation arg2Invocation = mock(Invocation.class);
        when(invocation.getArgumentAt(0, Object.class)).thenReturn(arg1Invocation);
        when(invocation.getArgumentAt(1, Object.class)).thenReturn(arg2Invocation);

        CapturesArguments arg1Capture = mock(CapturesArguments.class);
        CapturesArguments arg2Capture = mock(CapturesArguments.class);
        when(arg1Matcher instanceof CapturesArguments).thenReturn(true);
        when(arg1Matcher).thenReturn(arg1Capture);
        when(arg2Matcher instanceof CapturesArguments).thenReturn(true);
        when(arg2Matcher).thenReturn(arg2Capture);

        invocationMatcher.captureArgumentsFrom(invocation);

        verify(arg1Capture).captureFrom(arg1Invocation);
        verify(arg2Capture).captureFrom(arg2Invocation);
    }

    @Test
    public void testCaptureArgumentsFrom_WithoutVarArgs() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(method);
        when(method.isVarArgs()).thenReturn(false);

        Matcher arg1Matcher = mock(Matcher.class);
        Matcher arg2Matcher = mock(Matcher.class);
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(arg1Matcher);
        matchers.add(arg2Matcher);
        invocationMatcher = new InvocationMatcher(invocation, matchers);

        Invocation arg1Invocation = mock(Invocation.class);
        Invocation arg2Invocation = mock(Invocation.class);
        when(invocation.getArgumentAt(0, Object.class)).thenReturn(arg1Invocation);
        when(invocation.getArgumentAt(1, Object.class)).thenReturn(arg2Invocation);

        CapturesArguments arg1Capture = mock(CapturesArguments.class);
        CapturesArguments arg2Capture = mock(CapturesArguments.class);
        when(arg1Matcher instanceof CapturesArguments).thenReturn(true);
        when(arg1Matcher).thenReturn(arg1Capture);
        when(arg2Matcher instanceof CapturesArguments).thenReturn(true);
        when(arg2Matcher).thenReturn(arg2Capture);

        invocationMatcher.captureArgumentsFrom(invocation);

        verify(arg1Capture).captureFrom(arg1Invocation);
        verify(arg2Capture).captureFrom(arg2Invocation);
    }

    @Test
    public void testCreateFrom() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<Invocation> invocations = new ArrayList<>();
        invocations.add(invocation1);
        invocations.add(invocation2);

        List<InvocationMatcher> result = InvocationMatcher.createFrom(invocations);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.get(0) instanceof InvocationMatcher);
        assertTrue(result.get(1) instanceof InvocationMatcher);
        assertEquals(invocation1, result.get(0).getInvocation());
        assertEquals(invocation2, result.get(1).getInvocation());
    }
}
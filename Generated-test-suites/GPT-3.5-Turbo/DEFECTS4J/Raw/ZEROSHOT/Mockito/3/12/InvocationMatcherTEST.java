import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;

import java.lang.reflect.Method;
import java.util.List;


public class InvocationMatcherTest {

    private Invocation invocation;
    private List<Matcher> matchers;
    private InvocationMatcher invocationMatcher;

    @Before
    public void setUp() {
        invocation = mock(Invocation.class);
        matchers = mock(List.class);
        invocationMatcher = new InvocationMatcher(invocation, matchers);
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
        List<Matcher> result = invocationMatcher.getMatchers();
        assertEquals(matchers, result);
    }

    @Test
    public void testToString() {
        String result = invocationMatcher.toString();
        assertNotNull(result);
    }

    @Test
    public void testMatches() {
        Invocation actual = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(actual.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(actual.getMethod()).thenReturn(mock(Method.class));

        boolean result = invocationMatcher.matches(actual);

        assertTrue(result);
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Object[] actualArgs = {};

        boolean result = invocationMatcher.safelyArgumentsMatch(actualArgs);

        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(candidate.getMock()).thenReturn(mock(Object.class));
        when(invocation.getArguments()).thenReturn(new Object[]{});
        when(candidate.getArguments()).thenReturn(new Object[]{});

        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        assertFalse(result);
    }

    @Test
    public void testHasSameMethod() {
        Invocation candidate = mock(Invocation.class);
        Method m1 = mock(Method.class);
        Method m2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(m1);
        when(candidate.getMethod()).thenReturn(m2);
        when(m1.getName()).thenReturn("methodName");
        when(m2.getName()).thenReturn("methodName");
        when(m1.getParameterTypes()).thenReturn(new Class[]{});
        when(m2.getParameterTypes()).thenReturn(new Class[]{});

        boolean result = invocationMatcher.hasSameMethod(candidate);

        assertTrue(result);
    }

    @Test
    public void testGetLocation() {
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);
        Location result = invocationMatcher.getLocation();
        assertEquals(location, result);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        CapturesArguments capturesArguments = mock(CapturesArguments.class);
        Matcher matcher = mock(Matcher.class);
        when(matchers.get(anyInt())).thenReturn(matcher);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getRawArguments()).thenReturn(new Object[]{});
        when(invocation.getArgumentAt(anyInt(), any(Class.class))).thenReturn(mock(Object.class));
        when(matchers.size()).thenReturn(1);
        when(matcher instanceof CapturesArguments).thenReturn(true);

        invocationMatcher.captureArgumentsFrom(invocation);
        verify(matcher).captureFrom(any());
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = mock(List.class);
        Invocation invocation = mock(Invocation.class);
        when(invocations.get(anyInt())).thenReturn(invocation);

        List<InvocationMatcher> result = InvocationMatcher.createFrom(invocations);

        assertNotNull(result);
    }
}
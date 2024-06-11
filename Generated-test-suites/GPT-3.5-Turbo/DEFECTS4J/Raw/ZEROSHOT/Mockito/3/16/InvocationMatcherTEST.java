import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.invocation.Invocation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;
    private Invocation invocation;

    @Before
    public void setUp() {
        invocation = mock(Invocation.class);
        invocationMatcher = new InvocationMatcher(invocation);
    }

    @Test
    public void testGetMethod() {
        Method method = invocationMatcher.getMethod();
        assertNotNull(method);
    }

    @Test
    public void testGetInvocation() {
        Invocation inv = invocationMatcher.getInvocation();
        assertNotNull(inv);
    }

    @Test
    public void testGetMatchers() {
        List<Matcher> matchers = invocationMatcher.getMatchers();
        assertNotNull(matchers);
    }

    @Test
    public void testToString() {
        String str = invocationMatcher.toString();
        assertNotNull(str);
    }

    @Test
    public void testMatches() {
        Invocation actual = mock(Invocation.class);
        when(actual.getMock()).thenReturn(invocation.getMock());
        when(actual.getMethod()).thenReturn(invocation.getMethod());
        when(actual.getArguments()).thenReturn(invocation.getArguments());
        assertTrue(invocationMatcher.matches(actual));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod().getName()).thenReturn("testMethod");
        when(candidate.getMethod().getName()).thenReturn("testMethod");
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn(invocation.getMock());
        when(candidate.getArguments()).thenReturn(invocation.getArguments());
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSameMethod() {
        Invocation candidate = mock(Invocation.class);
        Method method = invocation.getMethod();
        when(invocation.getMethod()).thenReturn(method);
        when(candidate.getMethod()).thenReturn(method);
        assertTrue(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testGetLocation() {
        Invocation.Location location = mock(Invocation.Location.class);
        when(invocation.getLocation()).thenReturn(location);
        Invocation.Location loc = invocationMatcher.getLocation();
        assertNotNull(loc);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod().isVarArgs()).thenReturn(false);
        when(invocation.getArgumentAt(0, Object.class)).thenReturn("arg1");
        Matcher matcher = mock(Matcher.class);
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(matcher);
        invocationMatcher = new InvocationMatcher(invocation, matchers);
        invocationMatcher.captureArgumentsFrom(invocation);
        verify(matcher).captureFrom("arg1");
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new ArrayList<>();
        invocations.add(invocation);
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        assertNotNull(invocationMatchers);
        assertEquals(1, invocationMatchers.size());
    }
}
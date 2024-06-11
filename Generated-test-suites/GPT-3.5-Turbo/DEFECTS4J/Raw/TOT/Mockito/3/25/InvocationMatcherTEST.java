import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;
    private List<Matcher> matchers;
    private Invocation invocation;

    @Before
    public void setUp() {
        invocation = mock(Invocation.class);
        matchers = Collections.singletonList(mock(Matcher.class));
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void testInvocationMatcher_ConstructorWithMatchers() {
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testInvocationMatcher_ConstructorWithoutMatchers() {
        InvocationMatcher invocationMatcherWithoutMatchers = new InvocationMatcher(invocation);
        assertEquals(invocation, invocationMatcherWithoutMatchers.getInvocation());
        assertEquals(Arrays.asList(), invocationMatcherWithoutMatchers.getMatchers());
    }

    @Test
    public void testGetMethod() {
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);

        assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testToString() {
        String matcherString = "matcher";
        when(matchers.toString()).thenReturn(matcherString);
        String invocationString = "invocation";
        when(invocation.toString()).thenReturn(invocationString);

        String expected = "matcher invocation";
        assertEquals(expected, invocationMatcher.toString());
    }

    @Test
    public void testMatches_WhenAllMatch() {
        Invocation actual = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(actual.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(actual.getMethod()).thenReturn(mock(Method.class));
        when(actual.getArguments()).thenReturn(new Object[]{"arg1", 2});

        assertTrue(invocationMatcher.matches(actual));
    }

    @Test
    public void testMatches_WhenDifferentMock() {
        Invocation actual = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(actual.getMock()).thenReturn(mock(Object.class));

        assertFalse(invocationMatcher.matches(actual));
    }

    @Test
    public void testMatches_WhenDifferentMethod() {
        Invocation actual = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(actual.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(actual.getMethod()).thenReturn(mock(Method.class));

        assertFalse(invocationMatcher.matches(actual));
    }

    @Test
    public void testMatches_WhenArgumentsDoNotMatch() {
        Invocation actual = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(actual.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(actual.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getArguments()).thenReturn(new Object[]{"arg1", 2});
        when(actual.getArguments()).thenReturn(new Object[]{"arg1", 3});

        assertFalse(invocationMatcher.matches(actual));
    }

    @Test
    public void testHasSimilarMethod_WhenMethodNameNotEquals() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(invocation.isVerified()).thenReturn(false);
        when(candidate.isVerified()).thenReturn(true);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(candidate.getMock()).thenReturn(mock(Object.class));

        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethod_WhenMethodEquals() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(invocation.isVerified()).thenReturn(false);
        when(candidate.isVerified()).thenReturn(true);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(candidate.getMock()).thenReturn(mock(Object.class));
        when(invocation.getArguments()).thenReturn(new Object[]{"arg1", 2});
        when(candidate.getArguments()).thenReturn(new Object[]{"arg1", 2});

        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethod_WhenMethodOverloaded() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(invocation.isVerified()).thenReturn(false);
        when(candidate.isVerified()).thenReturn(true);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(candidate.getMock()).thenReturn(mock(Object.class));
        when(invocation.getArguments()).thenReturn(new Object[]{"arg1", 2});
        when(candidate.getArguments()).thenReturn(new Object[]{"arg1", 3});

        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSameMethod_WhenMethodEquals() {
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(method.getName()).thenReturn("methodName");
        when(method.getParameterTypes()).thenReturn(new Class<?>[]{});

        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(method);
        when(method.getName()).thenReturn("methodName");
        when(method.getParameterTypes()).thenReturn(new Class<?>[]{});

        assertTrue(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testHasSameMethod_WhenMethodNotEquals() {
        Method method1 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(method1.getName()).thenReturn("methodName1");
        when(method1.getParameterTypes()).thenReturn(new Class<?>[]{});

        Method method2 = mock(Method.class);
        when(method2.getName()).thenReturn("methodName2");
        when(method2.getParameterTypes()).thenReturn(new Class<?>[]{});

        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(method2);

        assertFalse(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testCaptureArgumentsFrom_WhenVarArgsMethod() {
        Method varArgsMethod = mock(Method.class);
        when(invocation.getMethod()).thenReturn(varArgsMethod);
        when(varArgsMethod.isVarArgs()).thenReturn(true);
        when(invocation.getRawArguments()).thenReturn(new Object[]{"arg1", 2, "arg3", 4});
        Matcher argumentMatcher1 = mock(Matcher.class);
        Matcher argumentMatcher2 = mock(Matcher.class);
        Matcher argumentMatcher3 = mock(Matcher.class);
        Matcher argumentMatcher4 = mock(Matcher.class);
        when(matchers.get(0)).thenReturn(argumentMatcher1);
        when(matchers.get(1)).thenReturn(argumentMatcher2);
        when(matchers.get(2)).thenReturn(argumentMatcher3);
        when(matchers.get(3)).thenReturn(argumentMatcher4);

        invocationMatcher.captureArgumentsFrom(invocation);

        verify(argumentMatcher1).captureFrom("arg1");
        verify(argumentMatcher2).captureFrom(2);
        verify(argumentMatcher3).captureFrom("arg3");
        verify(argumentMatcher4).captureFrom(4);
    }

    @Test
    public void testCaptureArgumentsFrom_WhenNonVarArgsMethod() {
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(method.isVarArgs()).thenReturn(false);
        when(invocation.getArgumentAt(0, Object.class)).thenReturn("arg1");
        when(invocation.getArgumentAt(1, Object.class)).thenReturn(2);
        Matcher argumentMatcher1 = mock(Matcher.class);
        Matcher argumentMatcher2 = mock(Matcher.class);
        when(matchers.get(0)).thenReturn(argumentMatcher1);
        when(matchers.get(1)).thenReturn(argumentMatcher2);

        invocationMatcher.captureArgumentsFrom(invocation);

        verify(argumentMatcher1).captureFrom("arg1");
        verify(argumentMatcher2).captureFrom(2);
    }

    @Test
    public void testCreateFrom() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<Invocation> invocations = Arrays.asList(invocation1, invocation2);

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        assertEquals(2, invocationMatchers.size());
        assertTrue(invocationMatchers.get(0) instanceof InvocationMatcher);
        assertTrue(invocationMatchers.get(1) instanceof InvocationMatcher);
        assertEquals(invocation1, invocationMatchers.get(0).getInvocation());
        assertEquals(invocation2, invocationMatchers.get(1).getInvocation());
    }

    @Test
    public void testGetLocation() {
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);

        assertEquals(location, invocationMatcher.getLocation());
    }
}
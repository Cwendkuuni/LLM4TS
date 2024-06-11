import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.matchers.MatcherDecorator;
import org.mockito.internal.matchers.VarargMatcher;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsComparator;
import org.mockito.internal.matchers.ArgumentsProcessor;
import org.hamcrest.Matcher;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class InvocationMatcherTest {

    private Invocation invocation;
    private List<Matcher> matchers;
    private InvocationMatcher invocationMatcher;

    @Before
    public void setup() {
        invocation = mock(Invocation.class);
        matchers = new ArrayList<>();
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void testConstructorWithMatchedMatchers() {
        List<Matcher> testMatchers = new ArrayList<>();
        testMatchers.add(mock(Matcher.class));
        invocationMatcher = new InvocationMatcher(invocation, testMatchers);
        assertEquals(testMatchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithEmptyMatchers() {
        List<Matcher> expectedMatchers = ArgumentsProcessor.argumentsToMatchers(invocation.getArguments());
        invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(expectedMatchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testGetMethod() {
        Method expectedMethod = mock(Method.class);
        when(invocation.getMethod()).thenReturn(expectedMethod);
        assertEquals(expectedMethod, invocationMatcher.getMethod());
    }

    @Test
    public void testGetInvocation() {
        assertEquals(invocation, invocationMatcher.getInvocation());
    }

    @Test
    public void testGetMatchers() {
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testToString() {
        String expected = new PrintSettings().print(matchers, invocation);
        assertEquals(expected, invocationMatcher.toString());
    }

    @Test
    public void testMatches() {
        Invocation actual = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(actual.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(actual.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getArguments()).thenReturn(new Object[]{"arg1", "arg2"});
        when(actual.getArguments()).thenReturn(new Object[]{"arg1", "arg2"});
        assertTrue(invocationMatcher.matches(actual));
    }

    @Test
    public void testMatchesWithDifferentMock() {
        Invocation actual = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(actual.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(actual.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getArguments()).thenReturn(new Object[]{"arg1", "arg2"});
        when(actual.getArguments()).thenReturn(new Object[]{"arg1", "arg2"});
        assertFalse(invocationMatcher.matches(actual));
    }

    @Test
    public void testMatchesWithDifferentMethod() {
        Invocation actual = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(actual.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(actual.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getArguments()).thenReturn(new Object[]{"arg1", "arg2"});
        when(actual.getArguments()).thenReturn(new Object[]{"arg1", "arg2"});
        assertFalse(invocationMatcher.matches(actual));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation candidate = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(candidate.getMethod()).thenReturn(method);
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(candidate.getMock()).thenReturn(mock(Object.class));
        when(candidate.getArguments()).thenReturn(new Object[]{"arg1", "arg2"});
        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethodWithMethodNameMismatch() {
        Invocation candidate = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(candidate.getMock()).thenReturn(mock(Object.class));
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethodWithVerification() {
        Invocation candidate = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(candidate.getMethod()).thenReturn(method);
        when(candidate.isVerified()).thenReturn(true);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(candidate.getMock()).thenReturn(mock(Object.class));
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethodWithDifferentMock() {
        Invocation candidate = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(candidate.getMethod()).thenReturn(method);
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(candidate.getMock()).thenReturn(mock(Object.class));
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethodWithSameMethodAndSameArguments() {
        Invocation candidate = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(candidate.getMethod()).thenReturn(method);
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(candidate.getMock()).thenReturn(mock(Object.class));
        when(candidate.getArguments()).thenReturn(new Object[]{"arg1", "arg2"});
        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethodWithSameMethodAndSameArgumentsOverloaded() {
        Invocation candidate = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(candidate.getMethod()).thenReturn(method);
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(candidate.getMock()).thenReturn(mock(Object.class));
        when(candidate.getArguments()).thenReturn(new Object[]{"arg1", "arg2"});
        when(invocationMatcher.safelyArgumentsMatch(candidate.getArguments())).thenReturn(false);
        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSameMethod() {
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(invocation.getMethod().getName()).thenReturn("testMethod");
        when(method.getName()).thenReturn("testMethod");
        when(method.getParameterTypes()).thenReturn(new Class[]{});
        assertTrue(invocationMatcher.hasSameMethod(invocation));
    }

    @Test
    public void testHasSameMethodWithMethodNameMismatch() {
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(invocation.getMethod().getName()).thenReturn("testMethod1");
        when(method.getName()).thenReturn("testMethod2");
        assertFalse(invocationMatcher.hasSameMethod(invocation));
    }

    @Test
    public void testHasSameMethodWithParameterTypesMismatch() {
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(invocation.getMethod().getName()).thenReturn("testMethod");
        when(method.getName()).thenReturn("testMethod");
        when(method.getParameterTypes()).thenReturn(new Class[]{String.class});
        assertFalse(invocationMatcher.hasSameMethod(invocation));
    }

    @Test
    public void testGetLocation() {
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);
        assertEquals(location, invocationMatcher.getLocation());
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getRawArguments()).thenReturn(new Object[]{"arg1", "arg2"});
        Matcher capturesArguments = mock(Matcher.class);
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(capturesArguments);
        when(invocationMatcher.getMatchers()).thenReturn(matchers);
        invocationMatcher.captureArgumentsFrom(invocation);
        verify(capturesArguments).captureFrom("arg1");
        verify(capturesArguments).captureFrom("arg2");
    }

    @Test
    public void testCaptureArgumentsFromWithVarArgs() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getRawArguments()).thenReturn(new Object[]{"arg1", new String[]{"arg2"}});
        Matcher capturesArguments = mock(Matcher.class);
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(capturesArguments);
        when(invocationMatcher.getMatchers()).thenReturn(matchers);
        when(invocation.getMethod().isVarArgs()).thenReturn(true);
        assertTrue(invocationMatcher.isVariableArgument(invocation, 1));
        invocationMatcher.captureArgumentsFrom(invocation);
        verify(capturesArguments).captureFrom("arg1");
        verify(capturesArguments).captureFrom("arg2");
    }

    @Test
    public void testCaptureArgumentsFromWithVarArgsMismatch() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getRawArguments()).thenReturn(new Object[]{"arg1", new Integer[]{1}});
        Matcher capturesArguments = mock(Matcher.class);
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(capturesArguments);
        when(invocationMatcher.getMatchers()).thenReturn(matchers);
        when(invocation.getMethod().isVarArgs()).thenReturn(true);
        assertFalse(invocationMatcher.isVariableArgument(invocation, 1));
        invocationMatcher.captureArgumentsFrom(invocation);
        verifyNoInteractions(capturesArguments);
    }

    @Test
    public void testCreateFrom() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<Invocation> invocations = new ArrayList<>();
        invocations.add(invocation1);
        invocations.add(invocation2);
        List<InvocationMatcher> expected = new ArrayList<>();
        expected.add(new InvocationMatcher(invocation1));
        expected.add(new InvocationMatcher(invocation2));
        assertEquals(expected, InvocationMatcher.createFrom(invocations));
    }
}
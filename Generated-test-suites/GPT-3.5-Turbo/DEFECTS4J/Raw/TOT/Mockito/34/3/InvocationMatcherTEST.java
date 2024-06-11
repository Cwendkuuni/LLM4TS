import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    private Invocation invocation;
    private List<Matcher> matchers;
    private InvocationMatcher invocationMatcher;

    @Before
    public void setup() {
        invocation = mock(Invocation.class);
        matchers = Collections.singletonList(mock(Matcher.class));
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void testConstructorWithInvocationAndMatchers() {
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
        assertEquals(invocation.getMethod(), invocationMatcher.getMethod());
    }

    @Test
    public void testConstructorWithInvocation() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(invocation.getMethod(), invocationMatcher.getMethod());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testToString() {
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(invocation.toString(matchers, new PrintSettings())).thenReturn("mockInvocation");
        
        String expected = "mockInvocation";
        assertEquals(expected, invocationMatcher.toString());
    }

    @Test
    public void testMatches() {
        Invocation actual = mock(Invocation.class);
        when(actual.getMock()).thenReturn(invocation.getMock());
        when(invocation.getMock().equals(actual.getMock())).thenReturn(true);
        when(invocation.hasSameMethod(actual)).thenReturn(true);
        when(new ArgumentsComparator().argumentsMatch(invocation, actual)).thenReturn(true);

        assertTrue(invocationMatcher.matches(actual));
    }

    @Test
    public void testMatches_WithDifferentMocks() {
        Invocation actual = mock(Invocation.class);
        when(actual.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMock().equals(actual.getMock())).thenReturn(false);

        assertFalse(invocationMatcher.matches(actual));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation candidate = mock(Invocation.class);
        Method invocationMethod = mock(Method.class);
        Method candidateMethod = mock(Method.class);
        when(invocation.getMethod()).thenReturn(invocationMethod);
        when(candidate.getMethod()).thenReturn(candidateMethod);
        when(invocationMethod.getName()).thenReturn("method");
        when(candidateMethod.getName()).thenReturn("method");
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(candidate.getMock()).thenReturn(invocation.getMock());
        when(invocation.hasSameMethod(candidate)).thenReturn(true);
        when(invocation.getArguments()).thenReturn(new Object[]{});
        when(candidate.getArguments()).thenReturn(new Object[]{});

        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethod_WithOverloadedButSameArgs() {
        Invocation candidate = mock(Invocation.class);
        Method invocationMethod = mock(Method.class);
        Method candidateMethod = mock(Method.class);
        when(invocation.getMethod()).thenReturn(invocationMethod);
        when(candidate.getMethod()).thenReturn(candidateMethod);
        when(invocationMethod.getName()).thenReturn("method");
        when(candidateMethod.getName()).thenReturn("method");
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(candidate.getMock()).thenReturn(invocation.getMock());
        when(invocation.hasSameMethod(candidate)).thenReturn(false);
        when(invocation.getArguments()).thenReturn(new Object[]{});
        when(candidate.getArguments()).thenReturn(new Object[]{});

        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethod_WithDifferentMethodName() {
        Invocation candidate = mock(Invocation.class);
        Method invocationMethod = mock(Method.class);
        Method candidateMethod = mock(Method.class);
        when(invocation.getMethod()).thenReturn(invocationMethod);
        when(candidate.getMethod()).thenReturn(candidateMethod);
        when(invocationMethod.getName()).thenReturn("method");
        when(candidateMethod.getName()).thenReturn("differentMethod");

        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethod_WithVerifiedCandidate() {
        Invocation candidate = mock(Invocation.class);
        when(candidate.isVerified()).thenReturn(true);

        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethod_WithDifferentMocks() {
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMock() == candidate.getMock()).thenReturn(false);

        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSameMethod() {
        Invocation candidate = mock(Invocation.class);
        Method invocationMethod = mock(Method.class);
        Method candidateMethod = mock(Method.class);
        when(invocation.getMethod()).thenReturn(invocationMethod);
        when(candidate.getMethod()).thenReturn(candidateMethod);
        when(invocationMethod.equals(candidateMethod)).thenReturn(true);

        assertTrue(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testHasSameMethod_WithDifferentMethods() {
        Invocation candidate = mock(Invocation.class);
        Method invocationMethod = mock(Method.class);
        Method candidateMethod = mock(Method.class);
        when(invocation.getMethod()).thenReturn(invocationMethod);
        when(candidate.getMethod()).thenReturn(candidateMethod);
        when(invocationMethod.equals(candidateMethod)).thenReturn(false);

        assertFalse(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testToString_WithPrintSettings() {
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(invocation.toString(matchers, new PrintSettings())).thenReturn("mockInvocation");
        
        String expected = "mockInvocation";
        assertEquals(expected, invocationMatcher.toString(new PrintSettings()));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation i = mock(Invocation.class);
        Object[] arguments = new Object[]{"arg1", "arg2"};
        when(i.getArguments()).thenReturn(arguments);

        Matcher matcher1 = mock(Matcher.class);
        Matcher matcher2 = mock(Matcher.class);
        matchers = Arrays.asList(matcher1, matcher2);

        CapturesArguments capturesArguments1 = mock(CapturesArguments.class);
        CapturesArguments capturesArguments2 = mock(CapturesArguments.class);
        when(matcher1 instanceof CapturesArguments).thenReturn(true);
        when(matcher1 instanceof CapturesArguments).thenReturn(true);
        when(matcher1).thenReturn(capturesArguments1);
        when(matcher2).thenReturn(capturesArguments2);

        invocationMatcher.captureArgumentsFrom(i);

        verify(capturesArguments1).captureFrom("arg1");
        verify(capturesArguments2).captureFrom("arg2");
    }
}
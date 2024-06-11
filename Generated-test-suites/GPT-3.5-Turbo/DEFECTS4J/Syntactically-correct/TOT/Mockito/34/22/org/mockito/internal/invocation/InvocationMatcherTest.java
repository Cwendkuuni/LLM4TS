
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.reporting.PrintSettings;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithInvocationAndMatchers() {
        // Setup
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>(Arrays.asList(mock(Matcher.class), mock(Matcher.class)));

        // Exercise
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // Verify
        assertThat(invocationMatcher.getInvocation(), is(invocation));
        assertThat(invocationMatcher.getMatchers(), is(matchers));
    }

    @Test
    public void testConstructorWithInvocationAndEmptyMatchers() {
        // Setup
        Invocation invocation = mock(Invocation.class);

        // Exercise
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Verify
        assertThat(invocationMatcher.getInvocation(), is(invocation));
        assertThat(invocationMatcher.getMatchers(), is(invocation.argumentsToMatchers()));
    }

    @Test
    public void testGetMethod() {
        // Setup
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Exercise
        Method result = invocationMatcher.getMethod();

        // Verify
        assertThat(result, is(method));
    }

    @Test
    public void testToString() {
        // Setup
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>(Collections.singletonList(mock(Matcher.class)));
        PrintSettings printSettings = mock(PrintSettings.class);
        when(invocation.toString(matchers, printSettings)).thenReturn("toStringResult");
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // Exercise
        String result = invocationMatcher.toString();

        // Verify
        assertThat(result, is("toStringResult"));
    }

    @Test
    public void testMatchesWithMatchingInvocation() {
        // Setup
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getArguments()).thenReturn(new Object[] {1, 2, 3});
        
        Invocation actualInvocation = mock(Invocation.class);
        when(actualInvocation.getMock()).thenReturn(invocation.getMock());
        when(actualInvocation.getMethod()).thenReturn(invocation.getMethod());
        when(actualInvocation.getArguments()).thenReturn(invocation.getArguments());
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Exercise
        boolean result = invocationMatcher.matches(actualInvocation);

        // Verify
        assertThat(result, is(true));
    }

    @Test
    public void testMatchesWithNonMatchingInvocation() {
        // Setup
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getArguments()).thenReturn(new Object[] {1, 2, 3});
        
        Invocation actualInvocation = mock(Invocation.class);
        when(actualInvocation.getMock()).thenReturn(invocation.getMock());
        when(actualInvocation.getMethod()).thenReturn(invocation.getMethod());
        when(actualInvocation.getArguments()).thenReturn(new Object[] {4, 5, 6});
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Exercise
        boolean result = invocationMatcher.matches(actualInvocation);

        // Verify
        assertThat(result, is(false));
    }

    @Test
    public void testHasSimilarMethodWithMatchingCandidate() {
        // Setup
        Invocation invocation = mock(Invocation.class);
        when(invocation.isVerified()).thenReturn(false);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getArguments()).thenReturn(new Object[] {1, 2, 3});
        
        Invocation candidateInvocation = mock(Invocation.class);
        when(candidateInvocation.getMethod()).thenReturn(invocation.getMethod());
        when(candidateInvocation.getMock()).thenReturn(invocation.getMock());
        when(candidateInvocation.isVerified()).thenReturn(false);
        when(candidateInvocation.getArguments()).thenReturn(new Object[] {1, 2, 3});

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Exercise
        boolean result = invocationMatcher.hasSimilarMethod(candidateInvocation);

        // Verify
        assertThat(result, is(true));
    }

    @Test
    public void testHasSimilarMethodWithNonMatchingCandidate() {
        // Setup
        Invocation invocation = mock(Invocation.class);
        when(invocation.isVerified()).thenReturn(false);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getArguments()).thenReturn(new Object[] {1, 2, 3});
        
        Invocation candidateInvocation = mock(Invocation.class);
        when(candidateInvocation.getMethod()).thenReturn(mock(Method.class));
        when(candidateInvocation.getMock()).thenReturn(invocation.getMock());
        when(candidateInvocation.isVerified()).thenReturn(false);
        when(candidateInvocation.getArguments()).thenReturn(new Object[] {4, 5, 6});
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Exercise
        boolean result = invocationMatcher.hasSimilarMethod(candidateInvocation);

        // Verify
        assertThat(result, is(false));
    }

    @Test
    public void testHasSameMethodWithMatchingCandidate() {
        // Setup
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        Invocation candidateInvocation = mock(Invocation.class);
        when(candidateInvocation.getMethod()).thenReturn(method);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Exercise
        boolean result = invocationMatcher.hasSameMethod(candidateInvocation);

        // Verify
        assertThat(result, is(true));
    }

    @Test
    public void testHasSameMethodWithNonMatchingCandidate() {
        // Setup
        Invocation invocation = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        Invocation candidateInvocation = mock(Invocation.class);
        when(candidateInvocation.getMethod()).thenReturn(method2);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Exercise
        boolean result = invocationMatcher.hasSameMethod(candidateInvocation);

        // Verify
        assertThat(result, is(false));
    }

    @Test
    public void testToStringWithPrintSettings() {
        // Setup
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>(Collections.singletonList(mock(Matcher.class)));
        PrintSettings printSettings = mock(PrintSettings.class);
        when(invocation.toString(matchers, printSettings)).thenReturn("toStringResult");
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // Exercise
        String result = invocationMatcher.toString(printSettings);

        // Verify
        assertThat(result, is("toStringResult"));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        // Setup
        Invocation invocation = mock(Invocation.class);
        
        Matcher argumentMatcher1 = mock(Matcher.class);
        Matcher argumentMatcher2 = mock(Matcher.class);
        CapturesArguments capturesArguments1 = mock(CapturesArguments.class);
        CapturesArguments capturesArguments2 = mock(CapturesArguments.class);
        when(capturesArguments1.captureFrom(eq(1))).thenReturn(true);
        when(capturesArguments2.captureFrom(eq(2))).thenReturn(true);
        
        List<Matcher> matchers = new ArrayList<>(Arrays.asList(argumentMatcher1, argumentMatcher2));
        when(argumentMatcher1 instanceof CapturesArguments).thenReturn(true);
        when(argumentMatcher2 instanceof CapturesArguments).thenReturn(true);
        when(capturesArguments1.captureFrom(any())).thenAnswer(invocation1 -> {
            Object argument = invocation1.getArgument(0);
            matchers.remove(0);
            return true;
        });
        when(capturesArguments2.captureFrom(any())).thenAnswer(invocation1 -> {
            Object argument = invocation1.getArgument(0);
            matchers.remove(0);
            return true;
        });
        
        Invocation captureInvocation = mock(Invocation.class);
        
        Object[] arguments = new Object[] {1, 2};
        when(captureInvocation.getArguments()).thenReturn(arguments);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // Exercise
        invocationMatcher.captureArgumentsFrom(captureInvocation);

        // Verify
        assertThat(matchers.size(), is(0));
        verify(capturesArguments1, times(1)).captureFrom(eq(1));
        verify(capturesArguments2, times(1)).captureFrom(eq(2));
    }

}

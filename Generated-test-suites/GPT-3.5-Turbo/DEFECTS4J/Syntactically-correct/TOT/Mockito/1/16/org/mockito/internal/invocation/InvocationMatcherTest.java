import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertSame(invocation, invocationMatcher.getInvocation());
        assertSame(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithoutMatchers() {
        Invocation invocation = mock(Invocation.class);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertSame(invocation, invocationMatcher.getInvocation());
        assertEquals(Collections.emptyList(), invocationMatcher.getMatchers());
    }

    @Test
    public void testGetMethod() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertSame(method, invocationMatcher.getMethod());
    }

    @Test
    public void testMatches_ReturnsTrue() {
        Invocation invocation = mock(Invocation.class);
        Invocation actual = mock(Invocation.class);
        when(invocation.getMock()).thenReturn("mock1");
        when(actual.getMock()).thenReturn("mock1");
        when(invocation.getMethod()).thenReturn(Method.class);
        when(actual.getMethod()).thenReturn(Method.class);
        when(invocation.getArguments()).thenReturn(new Object[0]);
        when(actual.getArguments()).thenReturn(new Object[0]);
        when(invocation.isVerified()).thenReturn(false);
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        when(argumentsComparator.argumentsMatch(invocation, actual)).thenReturn(true);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        invocationMatcher.setArgumentsComparator(argumentsComparator);

        boolean result = invocationMatcher.matches(actual);

        assertTrue(result);
    }

    @Test
    public void testMatches_ReturnsFalse() {
        Invocation invocation = mock(Invocation.class);
        Invocation actual = mock(Invocation.class);
        when(invocation.getMock()).thenReturn("mock1");
        when(actual.getMock()).thenReturn("mock2");
        when(invocation.getMethod()).thenReturn(Method.class);
        when(actual.getMethod()).thenReturn(Method.class);
        when(invocation.getArguments()).thenReturn(new Object[0]);
        when(actual.getArguments()).thenReturn(new Object[0]);
        when(invocation.isVerified()).thenReturn(false);
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        when(argumentsComparator.argumentsMatch(invocation, actual)).thenReturn(false);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        invocationMatcher.setArgumentsComparator(argumentsComparator);

        boolean result = invocationMatcher.matches(actual);

        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_ReturnsTrue() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod().getName()).thenReturn("method1");
        when(candidate.getMethod().getName()).thenReturn("method1");
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn("mock1");
        when(invocation.getMock()).thenReturn("mock1");
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        when(argumentsComparator.argumentsMatch(invocation, candidate)).thenReturn(true);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        invocationMatcher.setArgumentsComparator(argumentsComparator);

        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        assertTrue(result);
    }

    @Test
    public void testHasSimilarMethod_ReturnsFalse_MethodNameDifferent() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod().getName()).thenReturn("method1");
        when(candidate.getMethod().getName()).thenReturn("method2");

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_ReturnsFalse_IsVerifiedTrue() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod().getName()).thenReturn("method1");
        when(candidate.getMethod().getName()).thenReturn("method1");
        when(candidate.isVerified()).thenReturn(true);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_ReturnsFalse_MockDifferent() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod().getName()).thenReturn("method1");
        when(candidate.getMethod().getName()).thenReturn("method1");
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn("mock2");
        when(invocation.getMock()).thenReturn("mock1");

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        assertFalse(result);
    }
    
    // More test cases for the other methods in the class can be added here

}
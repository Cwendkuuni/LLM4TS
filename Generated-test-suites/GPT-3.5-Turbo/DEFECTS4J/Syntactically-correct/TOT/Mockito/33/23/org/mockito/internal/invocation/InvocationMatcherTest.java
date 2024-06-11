import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;

public class InvocationMatcherTest {

    private Invocation invocation;
    private List<Matcher> matchers;

    @Test
    public void testGetMethod() {
        Method mockMethod = mock(Method.class);
        invocation = new Invocation(mock(Invocation.class), mockMethod);
        matchers = Arrays.asList(mock(Matcher.class), mock(Matcher.class)); 
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Method actualMethod = invocationMatcher.getMethod();

        assertEquals(mockMethod, actualMethod);
    }

    @Test
    public void testGetInvocation() {
        Invocation mockInv = mock(Invocation.class);
        invocation = new Invocation(mockInv, mock(Method.class));
        matchers = Arrays.asList(mock(Matcher.class), mock(Matcher.class)); 
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Invocation actualInv = invocationMatcher.getInvocation();

        assertEquals(mockInv, actualInv);
    }

    @Test
    public void testGetMatchers() {
        Invocation mockInv = mock(Invocation.class);
        matchers = Arrays.asList(mock(Matcher.class), mock(Matcher.class)); 
        invocation = new Invocation(mockInv, mock(Method.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        List<Matcher> actualMatchers = invocationMatcher.getMatchers();

        assertEquals(matchers, actualMatchers);
    }

    @Test
    public void testToString() {
        Invocation mockInv = mock(Invocation.class);
        Method mockMethod = mock(Method.class);
        matchers = Arrays.asList(mock(Matcher.class), mock(Matcher.class)); 
        invocation = new Invocation(mockInv, mockMethod);
        InvocationsFinder invocationsFinder = mock(InvocationsFinder.class);
        int invocationSequenceNumber = 1;
        mockitoSimpleException("mockitoSimpleType_exp3()");
        PrintSettings printSettings = new PrintSettings();

        when(mockInv.getLocation()).thenReturn(mock(Location.class));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        String expectedString = "InvocationMatcher cannot be printed";

        String actualString = invocationMatcher.toString(printSettings);

        assertEquals(expectedString, actualString);        
    }

    @Test
    public void testMatches_PositiveMatch() {
        Invocation mockInv = mock(Invocation.class);
        matchers = Arrays.asList(mock(Matcher.class), mock(Matcher.class));
        invocation = new Invocation(mockInv, mock(Method.class));
        Invocation actualInv = mock(Invocation.class);

        when(mockInv.getMock()).thenReturn(mock(Object.class));
        when(mockInv.getMock().equals(any(Object.class))).thenReturn(true);
        when(actualInv.getMock()).thenReturn(mock(Object.class));
        when(actualInv.getMock().equals(any(Object.class))).thenReturn(true);
        when(mockInv.getMethod()).thenReturn(mock(Method.class));
        when(actualInv.getMethod()).thenReturn(mock(Method.class));
        when(actualInv.getArguments()).thenReturn(new Object[]{});
        when(new ArgumentsComparator().argumentsMatch(any(InvocationMatcher.class), any(Invocation.class))).thenReturn(true);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        boolean actualMatch = invocationMatcher.matches(actualInv);

        assertTrue(actualMatch);
    }

    @Test
    public void testMatches_NegativeMatch_DifferentMock() {
        Invocation mockInv = mock(Invocation.class);
        matchers = Arrays.asList(mock(Matcher.class), mock(Matcher.class));
        invocation = new Invocation(mockInv, mock(Method.class));
        Invocation actualInv = mock(Invocation.class);

        when(mockInv.getMock()).thenReturn(mock(Object.class));
        when(mockInv.getMock().equals(any(Object.class))).thenReturn(true);
        when(actualInv.getMock()).thenReturn(mock(Object.class));
        when(actualInv.getMock().equals(any(Object.class))).thenReturn(false);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        boolean actualMatch = invocationMatcher.matches(actualInv);

        assertFalse(actualMatch);
    }

    @Test
    public void testMatches_NegativeMatch_DifferentMethod() {
        Invocation mockInv = mock(Invocation.class);
        matchers = Arrays.asList(mock(Matcher.class), mock(Matcher.class));
        invocation = new Invocation(mockInv, mock(Method.class));
        Invocation actualInv = mock(Invocation.class);

        when(mockInv.getMethod()).thenReturn(mock(Method.class));
        when(actualInv.getMethod()).thenReturn(mock(Method.class));
        when(actualInv.getMock()).thenReturn(mock(Object.class));
        when(actualInv.getMock().equals(any(Object.class))).thenReturn(true);
        when(new ArgumentsComparator().argumentsMatch(any(InvocationMatcher.class), any(Invocation.class))).thenReturn(true);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        boolean actualMatch = invocationMatcher.matches(actualInv);

        assertFalse(actualMatch);
    }

    @Test
    public void testHasSimilarMethod_PositiveMatch() {
        Invocation mockInv = mock(Invocation.class);
        matchers = Arrays.asList(mock(Matcher.class), mock(Matcher.class));
        invocation = new Invocation(mockInv, mock(Method.class));
        Invocation actualInv = mock(Invocation.class);

        when(mockInv.getMethod()).thenReturn(mock(Method.class));
        when(actualInv.getMethod()).thenReturn(mock(Method.class));
        when(mockInv.getMethod().getName()).thenReturn("methodName");
        when(actualInv.getMethod().getName()).thenReturn("methodName");
        when(mockInv.isVerified()).thenReturn(false);
        when(mockInv.getMock()).thenReturn(mock(Object.class));
        when(mockInv.getMock() == actualInv.getMock()).thenReturn(true);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        boolean actualSimilar = invocationMatcher.hasSimilarMethod(actualInv);

        assertTrue(actualSimilar);
    }

    @Test
    public void testHasSimilarMethod_NegativeMatch_DifferentMethodName() {
        Invocation mockInv = mock(Invocation.class);
        matchers = Arrays.asList(mock(Matcher.class), mock(Matcher.class));
        invocation = new Invocation(mockInv, mock(Method.class));
        Invocation actualInv = mock(Invocation.class);

        when(mockInv.getMethod().getName()).thenReturn("method1");
        when(actualInv.getMethod().getName()).thenReturn("method2");
        when(mockInv.isVerified()).thenReturn(false);
        when(mockInv.getMock()).thenReturn(mock(Object.class));
        when(mockInv.getMock() == actualInv.getMock()).thenReturn(true);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        boolean actualSimilar = invocationMatcher.hasSimilarMethod(actualInv);

        assertFalse(actualSimilar);
    } 

    @Test
    public void testHasSimilarMethod_NegativeMatch_IsVerified() {
        Invocation mockInv = mock(Invocation.class);
        matchers = Arrays.asList(mock(Matcher.class), mock(Matcher.class));
        invocation = new Invocation(mockInv, mock(Method.class));
        Invocation actualInv = mock(Invocation.class);

        when(mockInv.getMethod().getName()).thenReturn("methodName");
        when(actualInv.getMethod().getName()).thenReturn("methodName");
        when(mockInv.isVerified()).thenReturn(true);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        boolean actualSimilar = invocationMatcher.hasSimilarMethod(actualInv);

        assertFalse(actualSimilar);
    } 

    @Test
    public void testHasSimilarMethod_NegativeMatch_DifferentMock() {
        Invocation mockInv = mock(Invocation.class);
        matchers = Arrays.asList(mock(Matcher.class), mock(Matcher.class));
        invocation = new Invocation(mockInv, mock(Method.class));
        Invocation actualInv = mock(Invocation.class);

        when(mockInv.getMethod().getName()).thenReturn("methodName");
        when(actualInv.getMethod().getName()).thenReturn("methodName");
        when(mockInv.isVerified()).thenReturn(false);
        when(mockInv.getMock()).thenReturn(mock(Object.class));
        when(mockInv.getMock() == actualInv.getMock()).thenReturn(false);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        boolean actualSimilar = invocationMatcher.hasSimilarMethod(actualInv);

        assertFalse(actualSimilar);
    } 

    @Test
    public void testHasSimilarMethod_NegativeMatch_OverloadedButSameArgs() {
        Invocation mockInv = mock(Invocation.class);
        matchers = Arrays.asList(mock(Matcher.class), mock(Matcher.class));
        invocation = new Invocation(mockInv, mock(Method.class));
        Invocation actualInv = mock(Invocation.class);

        when(mockInv.getMethod().getName()).thenReturn("methodName");
        when(actualInv.getMethod().getName()).thenReturn("methodName");
        when(mockInv.isVerified()).thenReturn(false);
        when(mockInv.getMock()).thenReturn(mock(Object.class));
        when(mockInv.getMock() == actualInv.getMock()).thenReturn(true);
        when(new ArgumentsComparator().argumentsMatch(any(InvocationMatcher.class), any(Object[].class))).thenReturn(true);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        boolean actualSimilar = invocationMatcher.hasSimilarMethod(actualInv);

        assertFalse(actualSimilar);
    } 

    @Test
    public void testHasSameMethod_PositiveMatch() {
        Invocation mockInv = mock(Invocation.class);
        matchers = Arrays.asList(mock(Matcher.class), mock(Matcher.class));
        invocation = new Invocation(mockInv, mock(Method.class));
        Invocation actualInv = mock(Invocation.class);
        Method mockMethod = mock(Method.class);

        when(mockInv.getMethod()).thenReturn(mockMethod);
        when(actualInv.getMethod()).thenReturn(mockMethod);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        boolean actualSameMethod = invocationMatcher.hasSameMethod(actualInv);

        assertTrue(actualSameMethod);
    }

    @Test
    public void testHasSameMethod_NegativeMatch() {
        Invocation mockInv = mock(Invocation.class);
        matchers = Arrays.asList(mock(Matcher.class), mock(Matcher.class));
        invocation = new Invocation(mockInv, mock(Method.class));
        Invocation actualInv = mock(Invocation.class);
        Method mockMethod1 = mock(Method.class);
        Method mockMethod2 = mock(Method.class);

        when(invocation.getMethod()).thenReturn(mockMethod1);
        when(actualInv.getMethod()).thenReturn(mockMethod2);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        boolean actualSameMethod = invocationMatcher.hasSameMethod(actualInv);

        assertFalse(actualSameMethod);
    }

    @Test
    public void testGetLocation() {
        Invocation mockInv = mock(Invocation.class);
        Location mockLocation = mock(Location.class);
        invocation = new Invocation(mockInv, mock(Method.class));
        matchers = Arrays.asList(mock(Matcher.class), mock(Matcher.class));

        when(mockInv.getLocation()).thenReturn(mockLocation);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Location actualLocation = invocationMatcher.getLocation();

        assertEquals(mockLocation, actualLocation);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation mockInv1 = mock(Invocation.class);
        Invocation mockInv2 = mock(Invocation.class);
        Matcher mockMatcher1 = mock(Matcher.class);
        Matcher mockMatcher2 = mock(Matcher.class);
        Object[] arguments = new Object[]{"argument1", "argument2"};
        matchers = Arrays.asList(mockMatcher1, mockMatcher2);
        invocation = new Invocation(mockInv1, mock(Method.class));

        when(mockInv2.getArguments()).thenReturn(arguments);
        when(mockMatcher1 instanceof CapturesArguments).thenReturn(true);
        when(mockMatcher2 instanceof CapturesArguments).thenReturn(true);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        invocationMatcher.captureArgumentsFrom(mockInv2);

        verify((CapturesArguments)mockMatcher1).captureFrom("argument1");
        verify((CapturesArguments)mockMatcher2).captureFrom("argument2");
    }

    @Test
    public void testCreateFrom() {
        Invocation mockInv1 = mock(Invocation.class);
        Invocation mockInv2 = mock(Invocation.class);
        List<Invocation> invocations = Arrays.asList(mockInv1, mockInv2);

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        assertEquals(invocations.size(), invocationMatchers.size());
        assertEquals(mockInv1, invocationMatchers.get(0).getInvocation());
        assertEquals(mockInv2, invocationMatchers.get(1).getInvocation());
    }

}
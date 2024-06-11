import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {
    private Invocation invocation;
    private InvocationMatcher invocationMatcher;
    private List<Matcher> matchers;

    @Before
    public void setUp() {
        invocation = mock(Invocation.class);
        matchers = new ArrayList<>();
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void testConstructor() {
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithEmptyMatchers() {
        invocationMatcher = new InvocationMatcher(invocation);
        List<Matcher> expectedMatchers = invocation.argumentsToMatchers();
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(expectedMatchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testGetMethod() {
        Method expectedMethod = mock(Method.class);
        when(invocation.getMethod()).thenReturn(expectedMethod);
        assertEquals(expectedMethod, invocationMatcher.getMethod());
    }

    @Test
    public void testToString() {
        String expectedString = "testString";
        PrintSettings printSettings = mock(PrintSettings.class);
        when(invocation.toString(matchers, printSettings)).thenReturn(expectedString);
        assertEquals(expectedString, invocationMatcher.toString());
    }

    @Test
    public void testMatches() {
        Invocation actual = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(new Object());
        when(actual.getMock()).thenReturn(new Object());
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(actual.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getArguments()).thenReturn(new Object[]{});
        when(actual.getArguments()).thenReturn(new Object[]{});
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        when(argumentsComparator.argumentsMatch(invocationMatcher, actual)).thenReturn(true);

        assertTrue(invocationMatcher.matches(actual));
    }

    @Test
    public void testMatchesFalse() {
        Invocation actual = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(new Object());
        when(actual.getMock()).thenReturn(new Object());
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(actual.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getArguments()).thenReturn(new Object[]{});
        when(actual.getArguments()).thenReturn(new Object[]{});
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        when(argumentsComparator.argumentsMatch(invocationMatcher, actual)).thenReturn(false);

        assertFalse(invocationMatcher.matches(actual));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation candidate = mock(Invocation.class);
        Method invocationMethod = mock(Method.class);
        when(invocation.getMethod()).thenReturn(invocationMethod);
        when(candidate.getMethod()).thenReturn(invocationMethod);
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(new Object());
        when(candidate.getMock()).thenReturn(new Object());
        when(argumentsComparator.argumentsMatch(invocationMatcher, candidate.getArguments())).thenReturn(true);

        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethodMethodNameNotEquals() {
        Invocation candidate = mock(Invocation.class);
        Method invocationMethod = mock(Method.class);
        when(invocation.getMethod()).thenReturn(invocationMethod);
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(new Object());
        when(candidate.getMock()).thenReturn(new Object());
        when(argumentsComparator.argumentsMatch(invocationMatcher, candidate.getArguments())).thenReturn(true);

        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethodIsVerified() {
        Invocation candidate = mock(Invocation.class);
        Method invocationMethod = mock(Method.class);
        when(invocation.getMethod()).thenReturn(invocationMethod);
        when(candidate.getMethod()).thenReturn(invocationMethod);
        when(candidate.isVerified()).thenReturn(true);
        when(invocation.getMock()).thenReturn(new Object());
        when(candidate.getMock()).thenReturn(new Object());
        when(argumentsComparator.argumentsMatch(invocationMatcher, candidate.getArguments())).thenReturn(true);

        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethodMockNotSame() {
        Invocation candidate = mock(Invocation.class);
        Method invocationMethod = mock(Method.class);
        when(invocation.getMethod()).thenReturn(invocationMethod);
        when(candidate.getMethod()).thenReturn(invocationMethod);
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(new Object());
        when(candidate.getMock()).thenReturn(new Object());
        when(argumentsComparator.argumentsMatch(invocationMatcher, candidate.getArguments())).thenReturn(true);

        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethodMethodNotEquals() {
        Invocation candidate = mock(Invocation.class);
        Method invocationMethod = mock(Method.class);
        when(invocation.getMethod()).thenReturn(invocationMethod);
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(new Object());
        when(candidate.getMock()).thenReturn(new Object());
        when(argumentsComparator.argumentsMatch(invocationMatcher, candidate.getArguments())).thenReturn(true);

        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethodOverloadedButSameArgs() {
        Invocation candidate = mock(Invocation.class);
        Method invocationMethod = mock(Method.class);
        when(invocation.getMethod()).thenReturn(invocationMethod);
        when(candidate.getMethod()).thenReturn(invocationMethod);
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(new Object());
        when(candidate.getMock()).thenReturn(new Object());
        when(hasSameMethod(candidate)).thenReturn(false);
        when(safelyArgumentsMatch(candidate.getArguments())).thenReturn(true);

        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSameMethod() {
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(candidate.getMethod()).thenReturn(method2);
        when(method1.equals(method2)).thenReturn(true);

        assertTrue(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testHasSameMethodFalse() {
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(candidate.getMethod()).thenReturn(method2);
        when(method1.equals(method2)).thenReturn(false);

        assertFalse(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testGetLocation() {
        Location expectedLocation = mock(Location.class);
        when(invocation.getLocation()).thenReturn(expectedLocation);
        assertEquals(expectedLocation, invocationMatcher.getLocation());
    }

    @Test
    public void testToStringWithPrintSettings() {
        String expectedString = "testString";
        PrintSettings printSettings = mock(PrintSettings.class);
        when(invocation.toString(matchers, printSettings)).thenReturn(expectedString);
        assertEquals(expectedString, invocationMatcher.toString(printSettings));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation i = mock(Invocation.class);
        Matcher matcher1 = mock(Matcher.class);
        Matcher matcher2 = mock(Matcher.class);
        matchers = Arrays.asList(matcher1, matcher2);
        invocationMatcher = new InvocationMatcher(invocation, matchers);
        Object argument1 = new Object();
        Object argument2 = new Object();
        Object[] arguments = {argument1, argument2};
        when(i.getArguments()).thenReturn(arguments);
        when(matcher1 instanceof CapturesArguments).thenReturn(true);
        when(matcher2 instanceof CapturesArguments).thenReturn(true);

        invocationMatcher.captureArgumentsFrom(i);

        verify(((CapturesArguments) matcher1)).captureFrom(argument1);
        verify(((CapturesArguments) matcher2)).captureFrom(argument2);
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = Arrays.asList(mock(Invocation.class));

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        assertEquals(invocations.size(), invocationMatchers.size());
        for (int i = 0; i < invocations.size(); i++) {
            assertEquals(invocations.get(i), invocationMatchers.get(i).getInvocation());
        }
    }
}
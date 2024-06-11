import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.Invocation;

public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;

    @Before
    public void setUp() {
        Invocation invocation = mock(Invocation.class);
        invocationMatcher = new InvocationMatcher(invocation);
    }

    @Test
    public void testGetMethod() {
        Method expected = mock(Method.class);
        when(invocation.getMethod()).thenReturn(expected);

        Method actual = invocationMatcher.getMethod();

        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetInvocation() {
        Invocation expected = mock(Invocation.class);
        when(invocation.getInvocation()).thenReturn(expected);

        Invocation actual = invocationMatcher.getInvocation();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetMatchersWithEmptyMatchers() {
        List<Matcher> expected = Collections.emptyList();

        List<Matcher> actual = invocationMatcher.getMatchers();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetMatchersWithNonEmptyMatchers() {
        List<Matcher> expected = mock(List.class);
        List<Matcher> matchers = Collections.singletonList(mock(Matcher.class));
        invocationMatcher = new InvocationMatcher(invocation, matchers);

        List<Matcher> actual = invocationMatcher.getMatchers();

        assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        String expected = "InvocationMatcher toString";
        PrintSettings printSettings = mock(PrintSettings.class);
        when(printSettings.print(any(), any())).thenReturn(expected);

        String actual = invocationMatcher.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testMatches() {
        Invocation actualInvocation = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(actualInvocation.getMock()).thenReturn(invocation.getMock());
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(actualInvocation.getMethod()).thenReturn(invocation.getMethod());
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        when(argumentsComparator.argumentsMatch(any(), any())).thenReturn(true);
        whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(argumentsComparator);

        boolean result = invocationMatcher.matches(actualInvocation);

        assertTrue(result);
    }

    @Test
    public void testHasSimilarMethodWithSimilarMethod() {
        Invocation candidate = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(candidate.getMethod()).thenReturn(method1);
        when(invocation.isVerified()).thenReturn(false);
        when(candidate.isVerified()).thenReturn(true);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(candidate.getMock()).thenReturn(invocation.getMock());
        when(invocation.getArguments()).thenReturn(new Object[]{mock(Object.class)});
        when(candidate.getArguments()).thenReturn(new Object[]{mock(Object.class)});
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        when(argumentsComparator.argumentsMatch(any(), any())).thenReturn(true);
        whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(argumentsComparator);

        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        assertFalse(result);
    }
    
    @Test
    public void testHasSimilarMethodWithOverloadedButSameArgs() {
        Invocation candidate = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(candidate.getMethod()).thenReturn(method2);
        when(invocation.isVerified()).thenReturn(false);
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(candidate.getMock()).thenReturn(invocation.getMock());
        when(invocation.getArguments()).thenReturn(new Object[]{mock(Object.class)});
        when(candidate.getArguments()).thenReturn(new Object[]{mock(Object.class)});
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        when(argumentsComparator.argumentsMatch(any(), any())).thenReturn(true);
        whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(argumentsComparator);

        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        assertFalse(result);
    }
    
    @Test
    public void testHasSimilarMethodWithDifferentMethodName() {
        Invocation candidate = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(candidate.getMethod()).thenReturn(method2);
        when(invocation.isVerified()).thenReturn(false);
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(candidate.getMock()).thenReturn(invocation.getMock());
        when(invocation.getArguments()).thenReturn(new Object[]{mock(Object.class)});
        when(candidate.getArguments()).thenReturn(new Object[]{mock(Object.class)});
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        when(argumentsComparator.argumentsMatch(any(), any())).thenReturn(true);
        whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(argumentsComparator);

        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        assertFalse(result);
    }

    @Test
    public void testHasSameMethodWithSameMethod() {
        Invocation candidate = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(candidate.getMethod()).thenReturn(method1);

        boolean result = invocationMatcher.hasSameMethod(candidate);

        assertTrue(result);
    }
    
    @Test
    public void testHasSameMethodWithDifferentMethod() {
        Invocation candidate = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(candidate.getMethod()).thenReturn(method2);

        boolean result = invocationMatcher.hasSameMethod(candidate);

        assertFalse(result);
    }

    @Test
    public void testGetLocation() {
        Location expected = mock(Location.class);
        when(invocation.getLocation()).thenReturn(expected);

        Location actual = invocationMatcher.getLocation();

        assertEquals(expected, actual);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCaptureArgumentsFromWithVarArg() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getRawArguments()).thenReturn(new Object[] {1, 2, 3});
        invocationMatcher.captureArgumentsFrom(invocation);
    }

    @Test
    public void testCaptureArgumentsFromWithoutVarArg() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getRawArguments()).thenReturn(new Object[] {1, 2, 3});
        when(matchers.get(0)).thenReturn(mock(CapturesArguments.class));

        invocationMatcher.captureArgumentsFrom(invocation);

        verify(matchers.get(0)).captureFrom(eq(1));
        verify(matchers.get(0)).captureFrom(eq(2));
        verify(matchers.get(0)).captureFrom(eq(3));
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = Collections.singletonList(mock(Invocation.class));
        LinkedList<InvocationMatcher> expected = new LinkedList<>();
        expected.add(new InvocationMatcher(invocations.get(0)));

        LinkedList<InvocationMatcher> actual = InvocationMatcher.createFrom(invocations);

        assertEquals(expected, actual);
    }
}
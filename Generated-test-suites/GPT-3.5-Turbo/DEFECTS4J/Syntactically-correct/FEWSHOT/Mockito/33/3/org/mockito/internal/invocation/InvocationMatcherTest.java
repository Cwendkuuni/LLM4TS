import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;

public class InvocationMatcherTest {

    @Test
    public void testConstructor_withMatchersArgument_shouldAssignMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(mock(Matcher.class));
        matchers.add(mock(Matcher.class));

        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        assertEquals(matchers, matcher.getMatchers());
    }

    @Test
    public void testConstructor_withoutMatchersArgument_shouldAssignInvocationArgumentsToMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> expectedMatchers = new ArrayList<>();
        expectedMatchers.add(mock(Matcher.class));
        expectedMatchers.add(mock(Matcher.class));

        when(invocation.argumentsToMatchers()).thenReturn(expectedMatchers);

        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertEquals(expectedMatchers, matcher.getMatchers());
    }

    @Test
    public void testGetMethod_shouldReturnInvocationMethod() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);

        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertEquals(method, matcher.getMethod());
    }

    @Test
    public void testGetInvocation_shouldReturnInvocation() {
        Invocation invocation = mock(Invocation.class);

        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertEquals(invocation, matcher.getInvocation());
    }

    @Test
    public void testToString_shouldReturnInvocationToStringRepresentation() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(mock(Matcher.class));
        matchers.add(mock(Matcher.class));
        PrintSettings printSettings = mock(PrintSettings.class);
        String expectedToString = "toString";

        when(invocation.toString(matchers, printSettings)).thenReturn(expectedToString);

        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        assertEquals(expectedToString, matcher.toString(printSettings));
    }

    @Test
    public void testMatches_withMatchingInvocation_shouldReturnTrue() {
        Invocation invocation = mock(Invocation.class);
        Invocation actual = mock(Invocation.class);

        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(actual.getMock()).thenReturn(invocation.getMock());
        when(actual.getMethod()).thenReturn(invocation.getMethod());
        when(invocation.argumentsMatch(actual)).thenReturn(true);

        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertTrue(matcher.matches(actual));
    }

    @Test
    public void testMatches_withMismatchedInvocation_shouldReturnFalse() {
        Invocation invocation = mock(Invocation.class);
        Invocation actual = mock(Invocation.class);

        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(actual.getMock()).thenReturn(mock(Object.class));
        when(actual.getMethod()).thenReturn(mock(Method.class));
        when(invocation.argumentsMatch(actual)).thenReturn(false);
        
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertFalse(matcher.matches(actual));
    }

    @Test
    public void testHasSimilarMethod_withMatchingMethodNameAndUnverifiedInvocation_shouldReturnTrue() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);

        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getMethod().getName()).thenReturn("methodName");
        when(candidate.getMethod()).thenReturn(invocation.getMethod());
        when(candidate.getMethod().getName()).thenReturn("methodName");
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(candidate.getMock()).thenReturn(invocation.getMock());
        when(candidate.getArguments()).thenReturn(invocation.getArguments());

        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertTrue(matcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethod_withDifferentMethodName_shouldReturnFalse() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);

        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getMethod().getName()).thenReturn("methodName");
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod().getName()).thenReturn("differentMethodName");

        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertFalse(matcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethod_withVerifiedInvocation_shouldReturnFalse() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);

        when(candidate.isVerified()).thenReturn(true);

        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertFalse(matcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethod_withDifferentMock_shouldReturnFalse() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);

        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(candidate.getMock()).thenReturn(mock(Object.class));

        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertFalse(matcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethod_withSameMethodNameAndOverloadedButSameArgs_shouldReturnFalse() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);

        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getMethod().getName()).thenReturn("methodName");
        when(candidate.getMethod()).thenReturn(invocation.getMethod());
        when(candidate.getMethod().getName()).thenReturn("methodName");
        when(invocation.argumentsMatch(candidate.getArguments())).thenReturn(true);

        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertFalse(matcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSameMethod_withSameMethod_shouldReturnTrue() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        Method method = mock(Method.class);

        when(invocation.getMethod()).thenReturn(method);
        when(candidate.getMethod()).thenReturn(method);

        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertTrue(matcher.hasSameMethod(candidate));
    }

    @Test
    public void testHasSameMethod_withDifferentMethod_shouldReturnFalse() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);

        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));

        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertFalse(matcher.hasSameMethod(candidate));
    }

    @Test
    public void testToString_withDefaultPrintSettings_shouldReturnInvocationToStringRepresentation() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(mock(Matcher.class));
        matchers.add(mock(Matcher.class));
        PrintSettings printSettings = mock(PrintSettings.class);
        String expectedToString = "toString";

        when(invocation.toString(matchers, printSettings)).thenReturn(expectedToString);

        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        assertEquals(expectedToString, matcher.toString());
    }

    @Test
    public void testCaptureArgumentsFrom_withCapturesArgumentsMatchers_shouldCaptureArguments() {
        Invocation invocation = mock(Invocation.class);
        Invocation i = mock(Invocation.class);
        Matcher matcher1 = mock(Matcher.class);
        Matcher matcher2 = mock(CapturesArguments.class);
        Object argument1 = mock(Object.class);
        Object argument2 = mock(Object.class);

        List<Matcher> matchers = new ArrayList<>();
        matchers.add(matcher1);
        matchers.add(matcher2);

        Object[] arguments = {argument1, argument2};

        when(invocation.getArguments()).thenReturn(arguments);
        when(i.getArguments()).thenReturn(arguments);
        when(matchatcher2.captureFrom(argument2)).thenReturn(true);

        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        matcher.captureArgumentsFrom(i);

        verify((CapturesArguments) matcher2).captureFrom(argument2);
    }

    @Test
    public void testCreateFrom_withInvocations_shouldReturnInvocationMatchers() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<Invocation> invocations = Arrays.asList(invocation1, invocation2);
        InvocationMatcher matcher1 = mock(InvocationMatcher.class);
        InvocationMatcher matcher2 = mock(InvocationMatcher.class);

        whenNew(InvocationMatcher.class).withArguments(invocation1).thenReturn(matcher1);
        whenNew(InvocationMatcher.class).withArguments(invocation2).thenReturn(matcher2);

        List<InvocationMatcher> expectedMatchers = Arrays.asList(matcher1, matcher2);

        List<InvocationMatcher> matchers = InvocationMatcher.createFrom(invocations);

        assertEquals(expectedMatchers, matchers);
    }
}
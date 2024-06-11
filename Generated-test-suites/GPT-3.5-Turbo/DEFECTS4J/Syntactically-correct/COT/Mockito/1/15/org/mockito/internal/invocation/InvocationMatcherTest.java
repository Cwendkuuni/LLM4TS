import org.junit.Test;
import org.mockito.invocation.Invocation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    @Test
    public void testGetMethod() {
        Invocation invocation = mock(Invocation.class);
        Method expectedMethod = mock(Method.class);
        when(invocation.getMethod()).thenReturn(expectedMethod);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Method actualMethod = invocationMatcher.getMethod();

        assertSame(expectedMethod, actualMethod);
    }

    @Test
    public void testGetInvocation() {
        Invocation expectedInvocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(expectedInvocation);

        Invocation actualInvocation = invocationMatcher.getInvocation();

        assertSame(expectedInvocation, actualInvocation);
    }

    @Test
    public void testGetMatchers() {
        List<Matcher> expectedMatchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(mock(Invocation.class), expectedMatchers);

        List<Matcher> actualMatchers = invocationMatcher.getMatchers();

        assertSame(expectedMatchers, actualMatchers);
    }

    @Test
    public void testToString() {
        PrintSettings printSettings = mock(PrintSettings.class);
        when(printSettings.print(any(), any())).thenReturn("expectedToString");

        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        String actualToString = invocationMatcher.toString();

        assertEquals("expectedToString", actualToString);
        verify(printSettings).print(matchers, invocation);
    }

    @Test
    public void testMatches() {
        Invocation invocation = mock(Invocation.class);
        Invocation actualInvocation = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(actualInvocation.getMock()).thenReturn(invocation.getMock());

        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        when(argumentsComparator.argumentsMatch(any(InvocationMatcher.class), any(Invocation.class))).thenReturn(true);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        invocationMatcher.matches(actualInvocation);

        verify(invocationMatcher).hasSameMethod(actualInvocation);
        verify(argumentsComparator).argumentsMatch(invocationMatcher, actualInvocation);
    }

    @Test
    public void testHasSimilarMethod() {
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(method1.getName()).thenReturn("method");
        when(method1.getParameterTypes()).thenReturn(new Class[]{});
        when(method2.getName()).thenReturn("method");
        when(method2.getParameterTypes()).thenReturn(new Class[]{});

        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(method1);

        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(method2);
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn(invocation.getMock());
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        when(argumentsComparator.argumentsMatch(any(InvocationMatcher.class), any(Object[].class))).thenReturn(true);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidate);

        assertTrue(hasSimilarMethod);
        verify(invocationMatcher).hasSameMethod(candidate);
        verify(argumentsComparator).argumentsMatch(invocationMatcher, candidate.getArguments());
    }

    @Test
    public void testHasSameMethod() {
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(method1.getName()).thenReturn("method");
        when(method1.getParameterTypes()).thenReturn(new Class[]{});
        when(method2.getName()).thenReturn("method");
        when(method2.getParameterTypes()).thenReturn(new Class[]{});

        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(method1);

        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(method2);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        boolean hasSameMethod = invocationMatcher.hasSameMethod(candidate);

        assertTrue(hasSameMethod);
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = mock(Invocation.class);
        Location expectedLocation = mock(Location.class);
        when(invocation.getLocation()).thenReturn(expectedLocation);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Location actualLocation = invocationMatcher.getLocation();

        assertSame(expectedLocation, actualLocation);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCaptureArgumentsFromVarArgs() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getRawArguments()).thenReturn(new Object[]{1, 2});

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        invocationMatcher.captureArgumentsFrom(invocation);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Matcher matcher = mock(Matcher.class);
        CapturesArguments capturesArguments = mock(CapturesArguments.class);
        when(capturesArguments.captureFrom(any())).thenReturn(false).thenReturn(true);

        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(invocation.getArgumentAt(anyInt(), any())).thenReturn(mock(Object.class));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(matcher, capturesArguments));

        invocationMatcher.captureArgumentsFrom(invocation);

        verify(capturesArguments, times(2)).captureFrom(any());
    }

    @Test
    public void testCreateFrom() {
        Invocation invocation = mock(Invocation.class);
        List<Invocation> invocations = Arrays.asList(invocation);

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        assertEquals(1, invocationMatchers.size());
        assertSame(invocation, invocationMatchers.get(0).getInvocation());
    }

}

import org.junit.Test;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.invocation.Invocation;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithMatchers() {
        Invocation invocation = createMockInvocation();
        List<Matcher> matchers = Collections.singletonList(mock(Matcher.class));
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertNotNull(invocationMatcher);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithoutMatchers() {
        Invocation invocation = createMockInvocation();
        List<Matcher> expectedMatchers = Collections.emptyList();

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertNotNull(invocationMatcher);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(expectedMatchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testGetMethod() {
        Invocation invocation = createMockInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Method expectedMethod = invocation.getMethod();
        Method actualMethod = invocationMatcher.getMethod();

        assertEquals(expectedMethod, actualMethod);
    }

    @Test
    public void testToString() {
        Invocation invocation = createMockInvocation();
        List<Matcher> matchers = Arrays.asList(mock(Matcher.class), mock(Matcher.class));

        PrintSettings mockPrintSettings = mock(PrintSettings.class);
        when(mockPrintSettings.print(matchers, invocation)).thenReturn("mockito invocation");

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        invocationMatcher.toString();

        verify(mockPrintSettings).print(matchers, invocation);
    }

    @Test
    public void testMatches_WhenInvocationMatches() {
        Invocation invocation = createMockInvocation();
        Invocation actualInvocation = createMockInvocation();

        ArgumentsComparator mockArgumentsComparator = mock(ArgumentsComparator.class);
        when(mockArgumentsComparator.argumentsMatch(any(InvocationMatcher.class), any(Invocation.class))).thenReturn(true);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean result = invocationMatcher.matches(actualInvocation);

        assertTrue(result);
        verify(mockArgumentsComparator).argumentsMatch(invocationMatcher, actualInvocation);
    }

    @Test
    public void testMatches_WhenInvocationDoesNotMatch() {
        Invocation invocation = createMockInvocation();
        Invocation actualInvocation = createMockInvocation();

        ArgumentsComparator mockArgumentsComparator = mock(ArgumentsComparator.class);
        when(mockArgumentsComparator.argumentsMatch(any(InvocationMatcher.class), any(Invocation.class))).thenReturn(false);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean result = invocationMatcher.matches(actualInvocation);

        assertFalse(result);
        verify(mockArgumentsComparator).argumentsMatch(invocationMatcher, actualInvocation);
    }

    @Test
    public void testHasSimilarMethod_WhenMethodNameNotEquals() {
        Invocation invocation = createMockInvocation();
        Invocation candidateInvocation = createMockInvocation();
        when(candidateInvocation.getMethod()).thenReturn(getMockitoGetMethod());

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean result = invocationMatcher.hasSimilarMethod(candidateInvocation);

        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_WhenIsVerified() {
        Invocation invocation = createMockInvocation();
        Invocation candidateInvocation = createMockInvocation();
        when(candidateInvocation.isVerified()).thenReturn(true);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean result = invocationMatcher.hasSimilarMethod(candidateInvocation);

        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_WhenMockIsNotSame() {
        Invocation invocation = createMockInvocation();
        Invocation candidateInvocation = createMockInvocation();
        when(candidateInvocation.getMock()).thenReturn(mock(Object.class));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean result = invocationMatcher.hasSimilarMethod(candidateInvocation);

        assertFalse(result);
    }


    @Test
    public void testHasSimilarMethod_WhenMethodEquals() {
        Invocation invocation = createMockInvocation();
        Invocation candidateInvocation = createMockInvocation();
        when(candidateInvocation.getMethod()).thenReturn(invocation.getMethod());

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean result = invocationMatcher.hasSimilarMethod(candidateInvocation);

        assertTrue(result);
    }

    @Test
    public void testHasSimilarMethod_WhenNeitherMethodNameNorArgumentMatches() {
        Invocation invocation = createMockInvocation();
        Invocation candidateInvocation = createMockInvocation();
        when(candidateInvocation.getMethod()).thenReturn(invocation.getMethod());
        ArgumentsComparator mockArgumentsComparator = mock(ArgumentsComparator.class);
        when(mockArgumentsComparator.argumentsMatch(any(InvocationMatcher.class), any(Object[].class))).thenReturn(false);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean result = invocationMatcher.hasSimilarMethod(candidateInvocation);
        assertFalse(result);
        verify(mockArgumentsComparator).argumentsMatch(any(InvocationMatcher.class), any(Object[].class));
    }

    @Test
    public void testHasSameMethod_WhenNameIsNull() {
        Invocation invocation = createMockInvocation();
        Invocation candidateInvocation = createMockInvocation();
        when(invocation.getMethod().getName()).thenReturn(null);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean result = invocationMatcher.hasSameMethod(candidateInvocation);

        assertFalse(result);
    }

    @Test
    public void testHasSameMethod_WhenNamesNotEqual() {
        Invocation invocation = createMockInvocation();
        Invocation candidateInvocation = createMockInvocation();
        when(invocation.getMethod().getName()).thenReturn("method1");
        when(candidateInvocation.getMethod().getName()).thenReturn("method2");

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean result = invocationMatcher.hasSameMethod(candidateInvocation);

        assertFalse(result);
    }

    @Test
    public void testHasSameMethod_WhenParametersNotEqual() {
        Invocation invocation = createMockInvocation();
        Invocation candidateInvocation = createMockInvocation();
        when(invocation.getMethod().getName()).thenReturn("method1");
        when(candidateInvocation.getMethod().getName()).thenReturn("method1");
        when(invocation.getMethod().getParameterTypes()).thenReturn(new Class[]{Integer.class});
        when(candidateInvocation.getMethod().getParameterTypes()).thenReturn(new Class[]{String.class});

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean result = invocationMatcher.hasSameMethod(candidateInvocation);

        assertFalse(result);
    }

    @Test
    public void testHasSameMethod_WhenArgumentsEqual() {
        Invocation invocation = createMockInvocation();
        Invocation candidateInvocation = createMockInvocation();
        when(invocation.getMethod().getName()).thenReturn("method1");
        when(candidateInvocation.getMethod().getName()).thenReturn("method1");
        when(invocation.getMethod().getParameterTypes()).thenReturn(new Class[]{Integer.class});
        when(candidateInvocation.getMethod().getParameterTypes()).thenReturn(new Class[]{Integer.class});

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean result = invocationMatcher.hasSameMethod(candidateInvocation);

        assertTrue(result);
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = createMockInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Location expectedLocation = invocation.getLocation();
        Location actualLocation = invocationMatcher.getLocation();

        assertEquals(expectedLocation, actualLocation);
    }

    @Test
    public void testCaptureArgumentsFrom_WhenMethodIsVarArgs() {
        Invocation invocation = createMockInvocationWithVarArgs();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation otherInvocation = createMockInvocationWithVarArgs();

        CapturesArguments mockCapturesArguments1 = mock(CapturesArguments.class);
        CapturesArguments mockCapturesArguments2 = mock(CapturesArguments.class);
        when(invocationMatcher.getMatchers()).thenReturn(Arrays.asList(mockCapturesArguments1, mockCapturesArguments2));

        invocationMatcher.captureArgumentsFrom(otherInvocation);

        verify(mockCapturesArguments1).captureFrom(otherInvocation.getArgumentAt(0, Object.class));
        verify(mockCapturesArguments2).captureFrom(otherInvocation.getRawArguments()[1]);
    }

    @Test
    public void testCaptureArgumentsFrom_WhenMethodIsNotVarArgs() {
        Invocation invocation = createMockInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation otherInvocation = createMockInvocation();

        CapturesArguments mockCapturesArguments1 = mock(CapturesArguments.class);
        CapturesArguments mockCapturesArguments2 = mock(CapturesArguments.class);
        when(invocationMatcher.getMatchers()).thenReturn(Arrays.asList(mockCapturesArguments1, mockCapturesArguments2));

        invocationMatcher.captureArgumentsFrom(otherInvocation);

        verify(mockCapturesArguments1).captureFrom(otherInvocation.getArgumentAt(0, Object.class));
        verify(mockCapturesArguments2).captureFrom(otherInvocation.getArgumentAt(1, Object.class));
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = Collections.singletonList(createMockInvocation());

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        assertNotNull(invocationMatchers);
        assertEquals(invocations.size(), invocationMatchers.size());
    }

    private Invocation createMockInvocation() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getArguments()).thenReturn(new Object[]{1, "test"});
        when(invocation.getMock()).thenReturn(mock(Object.class));
        return invocation;
    }

    private Invocation createMockInvocationWithVarArgs() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getArguments()).thenReturn(new Object[]{1});
        when(invocation.getRawArguments()).thenReturn(new Object[]{2, 3, 4});
        when(invocation.getMock()).thenReturn(mock(Object.class));
        return invocation;
    }

    private Method getMockitoGetMethod() {
        Method method = mock(Method.class);
        when(method.getName()).thenReturn("getMethod");
        return method;
    }

}
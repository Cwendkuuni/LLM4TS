import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    @Test
    public void testConstructor_withMatchingInvocationAndMatchers_shouldInitializeInvocationMatcher() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Collections.singletonList(mock(Matcher.class));

        // Act
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // Assert
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructor_withMatchingInvocationAndNoMatchers_shouldInitializeInvocationMatcherWithArgumentsToMatchers() {
        // Arrange
        Invocation invocation = mock(Invocation.class);

        // Act
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Assert
        assertEquals(invocation, invocationMatcher.getInvocation());
        List<Matcher> expectedMatchers = ArgumentsProcessor.argumentsToMatchers(invocation.getArguments());
        assertEquals(expectedMatchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testGetMethod_shouldReturnMethodFromInvocation() {
        // Arrange
        Method expectedMethod = mock(Method.class);
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(expectedMethod);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        Method actualMethod = invocationMatcher.getMethod();

        // Assert
        assertEquals(expectedMethod, actualMethod);
    }

    @Test
    public void testMatches_withMatchingInvocation_shouldReturnTrue() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        Object[] invocationArgs = new Object[]{1, 2, 3};
        when(invocation.getArguments()).thenReturn(invocationArgs);
        Invocation actualInvocation = mock(Invocation.class);
        when(actualInvocation.getMock()).thenReturn(invocation.getMock());
        when(actualInvocation.getMethod()).thenReturn(invocation.getMethod());
        when(actualInvocation.getArguments()).thenReturn(invocationArgs);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        boolean result = invocationMatcher.matches(actualInvocation);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testMatches_withNonMatchingInvocation_shouldReturnFalse() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        Object[] invocationArgs = new Object[]{1, 2, 3};
        when(invocation.getArguments()).thenReturn(invocationArgs);
        Invocation actualInvocation = mock(Invocation.class);
        when(actualInvocation.getMock()).thenReturn(mock(Object.class));
        when(actualInvocation.getMethod()).thenReturn(invocation.getMethod());
        when(actualInvocation.getArguments()).thenReturn(invocationArgs);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        boolean result = invocationMatcher.matches(actualInvocation);

        // Assert
        assertFalse(result);
    }

    @Test(expected = Throwable.class)
    public void testSafelyArgumentsMatch_withException_shouldReturnFalse() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        Object[] invocationArgs = new Object[]{1, 2, 3};
        when(invocation.getArguments()).thenReturn(invocationArgs);
        Object[] actualArgs = new Object[]{1, 2, 3};
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        when(argumentsComparator.argumentsMatch(any(), any())).thenThrow(Throwable.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        boolean result = invocationMatcher.safelyArgumentsMatch(actualArgs);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_withSimilarMethod_shouldReturnTrue() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(invocation.getMethod());
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testHasSimilarMethod_withDifferentMethodName_shouldReturnFalse() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        Invocation candidate = mock(Invocation.class);
        Method candidateMethod = mock(Method.class);
        when(candidate.getMethod()).thenReturn(candidateMethod);
        when(candidateMethod.getName()).thenReturn("different");
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_withVerifiedCandidate_shouldReturnFalse() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        Invocation candidate = mock(Invocation.class);
        when(candidate.isVerified()).thenReturn(true);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_withDifferentMock_shouldReturnFalse() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMock()).thenReturn(mock(Object.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_withOverloadedMethodAndSameArguments_shouldReturnFalse() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(invocation.getMethod());
        Object[] args = new Object[]{1, 2, 3};
        when(candidate.getArguments()).thenReturn(args);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_withOverloadedMethodAndDifferentArguments_shouldReturnTrue() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(invocation.getMethod());
        Object[] args = new Object[]{4, 5, 6};
        when(candidate.getArguments()).thenReturn(args);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testHasSameMethod_withMatchingMethods_shouldReturnTrue() {
        // Arrange
        Method method = mock(Method.class);
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(method);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(method);

        // Act
        boolean result = invocationMatcher.hasSameMethod(candidate);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testHasSameMethod_withNonMatchingMethods_shouldReturnFalse() {
        // Arrange
        Method method = mock(Method.class);
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(method);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(mock(Method.class));

        // Act
        boolean result = invocationMatcher.hasSameMethod(candidate);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testCaptureArgumentsFrom_withVarArgsMethod_shouldCaptureArgumentsFromInvocation() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(method.isVarArgs()).thenReturn(true);
        when(invocation.getMethod()).thenReturn(method);
        Object[] arguments = new Object[]{1, 2, 3, 4};
        when(invocation.getRawArguments()).thenReturn(arguments);
        Matcher matcher1 = mock(Matcher.class);
        Matcher matcher2 = mock(Matcher.class);
        Matcher matcher3 = mock(Matcher.class);
        List<Matcher> matchers = Arrays.asList(matcher1, matcher2, matcher3);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Invocation capturingInvocation = mock(Invocation.class);
        Matcher capturingMatcher1 = mock(Matcher.class);
        Matcher capturingMatcher2 = mock(Matcher.class);
        Matcher capturingMatcher3 = mock(Matcher.class);
        List<Matcher> expectedMatchers = Arrays.asList(capturingMatcher1, capturingMatcher2, capturingMatcher3);
        when(capturingInvocation.getArgumentAt(0, Object.class)).thenReturn(1);
        when(capturingInvocation.getArgumentAt(1, Object.class)).thenReturn(2);
        when(capturingInvocation.getArgumentAt(2, Object.class)).thenReturn(3);
        when(capturingInvocation.getRawArguments()).thenReturn(new Object[]{});
        when(capturingInvocation.getArgumentAt(3, Object.class)).thenReturn(4);

        // Act
        invocationMatcher.captureArgumentsFrom(capturingInvocation);

        // Assert
        verify(capturingMatcher1).captureFrom(1);
        verify(capturingMatcher2).captureFrom(2);
        verify(capturingMatcher3).captureFrom(3);
        verify(matcher1, never()).captureFrom(any());
        verify(matcher2, never()).captureFrom(any());
        verify(matcher3, never()).captureFrom(any());
    }

    @Test
    public void testCaptureArgumentsFrom_withNonVarArgsMethod_shouldCaptureArgumentsFromInvocation() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(method.isVarArgs()).thenReturn(false);
        when(invocation.getMethod()).thenReturn(method);
        Object[] arguments = new Object[]{1, 2, 3};
        when(invocation.getArguments()).thenReturn(arguments);
        Matcher matcher1 = mock(Matcher.class);
        Matcher matcher2 = mock(Matcher.class);
        Matcher matcher3 = mock(Matcher.class);
        List<Matcher> matchers = Arrays.asList(matcher1, matcher2, matcher3);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Invocation capturingInvocation = mock(Invocation.class);
        Matcher capturingMatcher1 = mock(Matcher.class);
        Matcher capturingMatcher2 = mock(Matcher.class);
        Matcher capturingMatcher3 = mock(Matcher.class);
        List<Matcher> expectedMatchers = Arrays.asList(capturingMatcher1, capturingMatcher2, capturingMatcher3);
        when(capturingInvocation.getArgumentAt(0, Object.class)).thenReturn(1);
        when(capturingInvocation.getArgumentAt(1, Object.class)).thenReturn(2);
        when(capturingInvocation.getArgumentAt(2, Object.class)).thenReturn(3);

        // Act
        invocationMatcher.captureArgumentsFrom(capturingInvocation);

        // Assert
        verify(capturingMatcher1).captureFrom(1);
        verify(capturingMatcher2).captureFrom(2);
        verify(capturingMatcher3).captureFrom(3);
        verify(matcher1, never()).captureFrom(any());
        verify(matcher2, never()).captureFrom(any());
        verify(matcher3, never()).captureFrom(any());
    }

    @Test
    public void testCreateFrom_withInvocations_shouldCreateInvocationMatchers() {
        // Arrange
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<Invocation> invocations = Arrays.asList(invocation1, invocation2);

        // Act
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        // Assert
        assertEquals(invocations.size(), invocationMatchers.size());
        assertTrue(invocationMatchers.get(0) instanceof InvocationMatcher);
        assertTrue(invocationMatchers.get(1) instanceof InvocationMatcher);
    }
}
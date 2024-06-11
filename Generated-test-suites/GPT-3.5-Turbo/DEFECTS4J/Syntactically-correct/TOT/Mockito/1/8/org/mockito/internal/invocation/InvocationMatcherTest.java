import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;

    @Before
    public void setup() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.isVerified()).thenReturn(false);
        when(invocation.getArguments()).thenReturn(new Object[] { 1, "test" });
        when(invocation.getLocation()).thenReturn(mock(Location.class));
        invocationMatcher = new InvocationMatcher(invocation);
    }

    @Test
    public void testConstructor_withInvocationAndMatchers_shouldSetInvocationAndMatchers() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(mock(Matcher.class), mock(Matcher.class));
        
        // Act
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // Assert
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructor_withInvocationOnly_shouldSetInvocationAndEmptyMatchers() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        
        // Act
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Assert
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(Collections.emptyList(), invocationMatcher.getMatchers());
    }

    @Test
    public void testGetMethod_shouldReturnMethodFromInvocation() {
        // Arrange
        Method method = mock(Method.class);
        when(invocationMatcher.getInvocation().getMethod()).thenReturn(method);
        
        // Act
        Method result = invocationMatcher.getMethod();

        // Assert
        assertEquals(method, result);
    }

    @Test
    public void testGetInvocation_shouldReturnInvocation() {
        // Arrange
        
        // Act
        Invocation result = invocationMatcher.getInvocation();

        // Assert
        assertEquals(invocationMatcher.getInvocation(), result);
    } 

    @Test
    public void testGetMatchers_shouldReturnMatchersFromInvocationMatcher() {
        // Arrange
        
        // Act
        List<Matcher> matchers = invocationMatcher.getMatchers();

        // Assert
        assertEquals(2, matchers.size());
        assertTrue(matchers.get(0) instanceof Matcher);
        assertTrue(matchers.get(1) instanceof Matcher);
    }

    @Test
    public void testToString_shouldReturnStringRepresentation() {
        // Arrange
        String expected = "MockitoMatchers argumentMatcher1, argumentMatcher2";
        
        // Act
        String actual = invocationMatcher.toString();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void testMatches_withMatchingInvocation_shouldReturnTrue() {
        // Arrange
        Invocation actualInvocation = mock(Invocation.class);
        when(invocationMatcher.getInvocation().getMock()).thenReturn(actualInvocation.getMock());
        when(invocationMatcher.hasSameMethod(actualInvocation)).thenReturn(true);
        when(invocationMatcher.safelyArgumentsMatch(actualInvocation.getArguments())).thenReturn(true);
        
        // Act
        boolean result = invocationMatcher.matches(actualInvocation);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testMatches_withNonMatchingInvocation_shouldReturnFalse() {
        // Arrange
        Invocation actualInvocation = mock(Invocation.class);
        when(invocationMatcher.getInvocation().getMock()).thenReturn(actualInvocation.getMock());
        when(invocationMatcher.hasSameMethod(actualInvocation)).thenReturn(false);
        
        // Act
        boolean result = invocationMatcher.matches(actualInvocation);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_withSameMethodNameSameMockUnverifiedAndDifferentArguments_shouldReturnTrue() {
        // Arrange
        Invocation candidateInvocation = mock(Invocation.class);
        when(invocationMatcher.getMethod().getName()).thenReturn("testMethod");
        when(candidateInvocation.getMethod().getName()).thenReturn("testMethod");
        when(invocationMatcher.getInvocation().isVerified()).thenReturn(false);
        when(invocationMatcher.getInvocation().getMock()).thenReturn(candidateInvocation.getMock());
        when(invocationMatcher.hasSameMethod(candidateInvocation)).thenReturn(false);
        when(invocationMatcher.safelyArgumentsMatch(candidateInvocation.getArguments())).thenReturn(true);
        
        // Act
        boolean result = invocationMatcher.hasSimilarMethod(candidateInvocation);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testHasSimilarMethod_withSameMethodNameSameMockButVerified_shouldReturnFalse() {
        // Arrange
        Invocation candidateInvocation = mock(Invocation.class);
        when(invocationMatcher.getMethod().getName()).thenReturn("testMethod");
        when(candidateInvocation.getMethod().getName()).thenReturn("testMethod");
        when(invocationMatcher.getInvocation().isVerified()).thenReturn(true);
        when(invocationMatcher.getInvocation().getMock()).thenReturn(candidateInvocation.getMock());
        when(invocationMatcher.hasSameMethod(candidateInvocation)).thenReturn(false);
        when(invocationMatcher.safelyArgumentsMatch(candidateInvocation.getArguments())).thenReturn(true);
        
        // Act
        boolean result = invocationMatcher.hasSimilarMethod(candidateInvocation);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_withSameMethodNameDifferentMock_shouldReturnFalse() {
        // Arrange
        Invocation candidateInvocation = mock(Invocation.class);
        when(invocationMatcher.getMethod().getName()).thenReturn("testMethod");
        when(candidateInvocation.getMethod().getName()).thenReturn("testMethod");
        when(invocationMatcher.getInvocation().getMock()).thenReturn(mock(Object.class));
        when(candidateInvocation.getMock()).thenReturn(mock(Object.class));
        when(invocationMatcher.hasSameMethod(candidateInvocation)).thenReturn(false);
        when(invocationMatcher.safelyArgumentsMatch(candidateInvocation.getArguments())).thenReturn(true);
        
        // Act
        boolean result = invocationMatcher.hasSimilarMethod(candidateInvocation);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_withMatchingMethodAndArgumentsButOverloaded_shouldReturnFalse() {
        // Arrange
        Invocation candidateInvocation = mock(Invocation.class);
        when(invocationMatcher.getMethod().getName()).thenReturn("testMethod");
        when(candidateInvocation.getMethod().getName()).thenReturn("testMethod");
        when(invocationMatcher.getInvocation().getMock()).thenReturn(candidateInvocation.getMock());
        when(invocationMatcher.hasSameMethod(candidateInvocation)).thenReturn(true);
        
        // Act
        boolean result = invocationMatcher.hasSimilarMethod(candidateInvocation);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testHasSameMethod_withMatchingMethod_shouldReturnTrue() {
        // Arrange
        Invocation candidateInvocation = mock(Invocation.class);
        when(invocationMatcher.getMethod().getName()).thenReturn("testMethod");
        when(candidateInvocation.getMethod().getName()).thenReturn("testMethod");
        
        // Act
        boolean result = invocationMatcher.hasSameMethod(candidateInvocation);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testHasSameMethod_withNonMatchingMethod_shouldReturnFalse() {
        // Arrange
        Invocation candidateInvocation = mock(Invocation.class);
        when(invocationMatcher.getMethod().getName()).thenReturn("testMethod");
        when(candidateInvocation.getMethod().getName()).thenReturn("otherMethod");
        
        // Act
        boolean result = invocationMatcher.hasSameMethod(candidateInvocation);

        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testGetLocation_shouldReturnLocationFromInvocation() {
        // Arrange
        Location location = mock(Location.class);
        when(invocationMatcher.getInvocation().getLocation()).thenReturn(location);
        
        // Act
        Location result = invocationMatcher.getLocation();

        // Assert
        assertEquals(location, result);
    }

    @Test
    public void testCaptureArgumentsFrom_withVarArgsInvocation_shouldThrowUnsupportedOperationException() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getMethod().isVarArgs()).thenReturn(true);
        
        // Act & Assert
        assertThrows(UnsupportedOperationException.class, () -> invocationMatcher.captureArgumentsFrom(invocation));
    }

    @Test
    public void testCaptureArgumentsFrom_withMatchersThatCaptureArguments_shouldCaptureArguments() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        when(invocation.getArguments()).thenReturn(new Object[] { 1, "test" });
        
        Matcher matcher1 = mock(Matcher.class);
        CaptureMatcher captureMatcher = new CaptureMatcher();
        Matcher matcher2 = captureMatcher;
        
        invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(matcher1, matcher2));
        
        // Act
        invocationMatcher.captureArgumentsFrom(invocation);
        
        // Assert
        assertEquals(1, captureMatcher.getCapturedArguments().size());
        assertEquals(1, captureMatcher.getCapturedArguments().get(0));
    }

    private class CaptureMatcher implements Matcher, CapturesArguments {

        private Object capturedArgument;
        
        @Override
        public void captureFrom(Object argument) {
            this.capturedArgument = argument;
        }
        
        @Override
        public boolean matches(Object argument) {
            return false;
        }

        public Object getCapturedArgument() {
            return capturedArgument;
        }
    }

    @Test
    public void testCreateFrom_withListOfInvocations_shouldCreateListOfInvocationMatchers() {
        // Arrange
        List<Invocation> invocations = Arrays.asList(mock(Invocation.class), mock(Invocation.class));
        
        // Act
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        
        // Assert
        assertEquals(2, invocationMatchers.size());
        assertTrue(invocationMatchers.get(0) instanceof InvocationMatcher);
        assertTrue(invocationMatchers.get(1) instanceof InvocationMatcher);
    }
}
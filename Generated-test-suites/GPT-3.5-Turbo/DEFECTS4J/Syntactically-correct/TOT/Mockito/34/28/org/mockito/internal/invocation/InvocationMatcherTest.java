import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class InvocationMatcherTest {

    private Invocation invocation;
    private List<Matcher> matchers;

    @Before
    public void setup() {
        invocation = mock(Invocation.class);
        matchers = new ArrayList<>();
    }

    @Test
    public void testInvocationMatcher_ConstructorWithMatchers_ShouldSetMatchers() {
        // Arrange
        matchers.add(mock(Matcher.class));

        // Act
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // Assert
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testInvocationMatcher_ConstructorWithoutMatchers_ShouldSetInvocationArgumentsAsMatchers() {
        // Act
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Assert
        assertEquals(invocation.argumentsToMatchers(), invocationMatcher.getMatchers());
    }

    @Test
    public void testGetMethod_ShouldReturnInvocationMethod() {
        // Arrange
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);

        // Act
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Method result = invocationMatcher.getMethod();

        // Assert
        assertEquals(method, result);
    }

    @Test
    public void testGetInvocation_ShouldReturnInvocation() {
        // Act
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation result = invocationMatcher.getInvocation();

        // Assert
        assertEquals(invocation, result);
    }

    @Test
    public void testToString_ShouldReturnInvocationStringRepresentation() {
        // Arrange
        String expectedString = "InvocationString";
        when(invocation.toString(matchers, new PrintSettings())).thenReturn(expectedString);

        // Act
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        String result = invocationMatcher.toString();

        // Assert
        assertEquals(expectedString, result);
    }

    @Test
    public void testMatches_SameMockSameMethodMatchingArguments_ShouldReturnTrue() {
        // Arrange
        Invocation actualInvocation = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.argumentsMatch(actualInvocation)).thenReturn(true);

        // Act
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        boolean result = invocationMatcher.matches(actualInvocation);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testMatches_DifferentMock_ShouldReturnFalse() {
        // Arrange
        Invocation actualInvocation = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(actualInvocation.getMock()).thenReturn(mock(Object.class));

        // Act
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        boolean result = invocationMatcher.matches(actualInvocation);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testMatches_DifferentMethod_ShouldReturnFalse() {
        // Arrange
        Invocation actualInvocation = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(actualInvocation.getMethod()).thenReturn(mock(Method.class));

        // Act
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        boolean result = invocationMatcher.matches(actualInvocation);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_SameMethodNameUnverifiedMockMethodAndNotOverloaded_ShouldReturnTrue() {
        // Arrange
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(candidate.getMock()).thenReturn(invocation.getMock());
        when(invocation.argumentsMatch(candidate.getArguments())).thenReturn(false);

        // Act
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testHasSimilarMethod_DifferentMethodName_ShouldReturnFalse() {
        // Arrange
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod().getName()).thenReturn("differentName");

        // Act
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_VerifiedCandidate_ShouldReturnFalse() {
        // Arrange
        Invocation candidate = mock(Invocation.class);
        when(candidate.isVerified()).thenReturn(true);

        // Act
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_DifferentMock_ShouldReturnFalse() {
        // Arrange
        Invocation candidate = mock(Invocation.class);
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(candidate.getMock()).thenReturn(mock(Object.class));

        // Act
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_OverloadedButSameArguments_ShouldReturnFalse() {
        // Arrange
        Invocation candidate = mock(Invocation.class);
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(candidate.getMock()).thenReturn(invocation.getMock());
        when(candidate.getArguments()).thenReturn(new Object[]{1, 2});
        when(invocation.argumentsMatch(candidate.getArguments())).thenReturn(true);

        // Act
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testHasSameMethod_SameMethod_ShouldReturnTrue() {
        // Arrange
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(invocation.getMethod());

        // Act
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        boolean result = invocationMatcher.hasSameMethod(candidate);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testHasSameMethod_DifferentMethod_ShouldReturnFalse() {
        // Arrange
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));

        // Act
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        boolean result = invocationMatcher.hasSameMethod(candidate);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testGetLocation_ShouldReturnInvocationLocation() {
        // Arrange
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);

        // Act
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Location result = invocationMatcher.getLocation();

        // Assert
        assertEquals(location, result);
    }

    @Test
    public void testToStringWithPrintSettings_ShouldReturnInvocationStringRepresentationWithPrintSettings() {
        // Arrange
        PrintSettings printSettings = mock(PrintSettings.class);
        String expectedString = "InvocationString";
        when(invocation.toString(matchers, printSettings)).thenReturn(expectedString);

        // Act
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        String result = invocationMatcher.toString(printSettings);

        // Assert
        assertEquals(expectedString, result);
    }

    @Test
    public void testCaptureArgumentsFrom_WithCaptureArgumentsMatcher_ShouldCaptureArgumentsFromInvocation() {
        // Arrange
        Matcher captureArgumentsMatcher = mock(Matcher.class);
        matchers.add(captureArgumentsMatcher);
        Invocation i = mock(Invocation.class);
        Object[] arguments = new Object[]{"arg1", "arg2"};
        when(i.getArguments()).thenReturn(arguments);

        // Act
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        invocationMatcher.captureArgumentsFrom(i);

        // Assert
        verify(captureArgumentsMatcher).captureFrom(arguments[0]);
        verify(captureArgumentsMatcher).captureFrom(arguments[1]);
    }
}
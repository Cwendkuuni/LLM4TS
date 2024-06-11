import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;

public class InvocationMatcherTest {

    private Invocation invocation;
    private InvocationMatcher invocationMatcher;
    private Matcher matcher;
    private List<Matcher> matchers;


    @Before
    public void setUp() throws NoSuchMethodException {
        invocation = mock(Invocation.class);
        Method method = Invocation.class.getDeclaredMethod("getMethod");
        Object[] args = new Object[] {};
        when(invocation.getMethod()).thenReturn(method);
        when(invocation.getArguments()).thenReturn(args);
        
        matchers = Arrays.asList(matcher);
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void testGetMethod_ReturnsMethod() {
        // Arrange

        // Act
        Method result = invocationMatcher.getMethod();

        // Assert
        assertEquals(invocation.getMethod(), result);
    }

    @Test
    public void testGetInvocation_ReturnsInvocation() {
        // Arrange

        // Act
        Invocation result = invocationMatcher.getInvocation();

        // Assert
        assertEquals(invocation, result);
    }

    @Test
    public void testGetMatchers_ReturnsMatchers() {
        // Arrange

        // Act
        List<Matcher> result = invocationMatcher.getMatchers();

        // Assert
        assertEquals(matchers, result);
    }

    @Test
    public void testToString_ReturnsInvocationToString() {
        // Arrange

        // Act
        String result = invocationMatcher.toString();

        // Assert
        assertEquals(invocation.toString(matchers, new PrintSettings()), result);
    }

    @Test
    public void testMatches_ReturnsTrueIfInvocationsMatch() {
        // Arrange
        Invocation actualInvocation = mock(Invocation.class);
        when(actualInvocation.getMock()).thenReturn(invocation.getMock());
        when(actualInvocation.getMethod()).thenReturn(invocation.getMethod());
        when(actualInvocation.getArguments()).thenReturn(new Object[] {});

        // Act
        boolean result = invocationMatcher.matches(actualInvocation);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testMatches_ReturnsFalseIfInvocationsDoNotMatch() {
        // Arrange
        Invocation actualInvocation = mock(Invocation.class);
        when(actualInvocation.getMock()).thenReturn(invocation.getMock());
        when(actualInvocation.getMethod()).thenReturn(mock(Method.class));
        when(actualInvocation.getArguments()).thenReturn(new Object[] {});

        // Act
        boolean result = invocationMatcher.matches(actualInvocation);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_ReturnsTrueIfCandidatesAreSimilar() {
        // Arrange
        Invocation candidate = mock(Invocation.class);
        Method method = Invocation.class.getDeclaredMethod("getMethod");
        when(candidate.getMethod()).thenReturn(method);
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn(invocation.getMock());
        when(candidate.getArguments()).thenReturn(invocation.getArguments());

        // Act
        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testhasSimilarMethod_ReturnsTrueIfCandidatesAreOverloadedButArgumentsAreDifferent() {
        // Arrange
        Invocation candidate = mock(Invocation.class);
        Method method = Invocation.class.getDeclaredMethod("getMethod");
        when(candidate.getMethod()).thenReturn(method);
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn(invocation.getMock());
        when(candidate.getArguments()).thenReturn(new Object[] {});

        // Act
        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_ReturnsFalseIfCandidatesAreDifferent() {
        // Arrange
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn(mock(Object.class));
        when(candidate.getArguments()).thenReturn(new Object[] {});

        // Act
        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testHasSameMethod_ReturnsTrueIfMethodsAreEqual() {
        // Arrange
        Method method = Invocation.class.getDeclaredMethod("getMethod");
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(method);

        // Act
        boolean result = invocationMatcher.hasSameMethod(candidate);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testHasSameMethod_ReturnsFalseIfMethodsAreNotEqual() {
        // Arrange
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(mock(Method.class));

        // Act
        boolean result = invocationMatcher.hasSameMethod(candidate);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testGetLocation_ReturnsInvocationLocation() {
        // Arrange
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);

        // Act
        Location result = invocationMatcher.getLocation();

        // Assert
        assertEquals(location, result);
    }

    @Test
    public void testToStringWithPrintSettings_ReturnsInvocationToStringWithSettings() {
        // Arrange
        PrintSettings printSettings = mock(PrintSettings.class);

        // Act
        String result = invocationMatcher.toString(printSettings);

        // Assert
        assertEquals(invocation.toString(matchers, printSettings), result);
    }

    @Test
    public void testCaptureArgumentsFrom_CallsCaptureFromForMatchersThatCaptureArguments() {
        // Arrange
        Invocation i = mock(Invocation.class);
        CapturesArguments capturesArgumentsMock = mock(CapturesArguments.class);
        when(capturesArgumentsMock.captureFrom(any())).thenReturn(true);
        when(matchers.get(0)).thenReturn(capturesArgumentsMock);

        // Act
        invocationMatcher.captureArgumentsFrom(i);

        // Assert
        verify(capturesArgumentsMock, times(1)).captureFrom(eq(i.getArguments()[0]));
    }

    @Test
    public void testCreateFrom_ReturnsListOfInvocationMatchers() {
        // Arrange
        List<Invocation> invocations = Arrays.asList(invocation);

        // Act
        List<InvocationMatcher> result = InvocationMatcher.createFrom(invocations);

        // Assert
        assertEquals(1, result.size());
        assertEquals(invocation, result.get(0).getInvocation());
    }

}
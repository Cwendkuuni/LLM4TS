import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.invocation.ArgumentsComparator;

@SuppressWarnings("unchecked")
public class InvocationMatcherTest {

    @Test
    public void testGetMethod() {
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
    public void testGetInvocation() {
        // Arrange
        Invocation expectedInvocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(expectedInvocation);

        // Act
        Invocation actualInvocation = invocationMatcher.getInvocation();

        // Assert
        assertEquals(expectedInvocation, actualInvocation);
    }

    @Test
    public void testGetMatchers() {
        // Arrange
        List<Matcher> expectedMatchers = Collections.emptyList();
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, expectedMatchers);

        // Act
        List<Matcher> actualMatchers = invocationMatcher.getMatchers();

        // Assert
        assertEquals(expectedMatchers, actualMatchers);
    }

    @Test
    public void testToString() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        String result = invocationMatcher.toString();

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testMatches_ReturnsTrue() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getArguments()).thenReturn(new Object[]{});
        Invocation actualInvocation = mock(Invocation.class);
        when(actualInvocation.getMock()).thenReturn(invocation.getMock());
        when(actualInvocation.getMethod()).thenReturn(invocation.getMethod());
        when(actualInvocation.getArguments()).thenReturn(new Object[]{});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        boolean result = invocationMatcher.matches(actualInvocation);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testMatches_ReturnsFalse() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getArguments()).thenReturn(new Object[]{});
        Invocation actualInvocation = mock(Invocation.class);
        when(actualInvocation.getMock()).thenReturn(mock(Object.class));
        when(actualInvocation.getMethod()).thenReturn(mock(Method.class));
        when(actualInvocation.getArguments()).thenReturn(new Object[]{});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        boolean result = invocationMatcher.matches(actualInvocation);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_ReturnsTrue() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        invocation.isVerified();
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getArguments()).thenReturn(new Object[]{});
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(invocation.getMethod());
        candidate.isVerified();
        when(candidate.getMock()).thenReturn(invocation.getMock());
        when(candidate.getArguments()).thenReturn(new Object[]{});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testHasSimilarMethod_ReturnsFalse() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        invocation.isVerified();
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getArguments()).thenReturn(new Object[]{});
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        candidate.isVerified();
        when(candidate.getMock()).thenReturn(mock(Object.class));
        when(candidate.getArguments()).thenReturn(new Object[]{});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testHasSameMethod_ReturnsTrue() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(method);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        boolean result = invocationMatcher.hasSameMethod(candidate);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testHasSameMethod_ReturnsFalse() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        boolean result = invocationMatcher.hasSameMethod(candidate);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testGetLocation() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        Location expectedLocation = mock(Location.class);
        when(invocation.getLocation()).thenReturn(expectedLocation);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        Location actualLocation = invocationMatcher.getLocation();

        // Assert
        assertEquals(expectedLocation, actualLocation);
    }

    @Test
    public void testToStringWithPrintSettings() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        String result = invocationMatcher.toString(mock(PrintSettings.class));

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        Invocation i = mock(Invocation.class);
        Matcher m = mock(Matcher.class);
        when(i.getArguments()).thenReturn(new Object[]{});
        when(m instanceof CapturesArguments).thenReturn(true);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        invocationMatcher.getMatchers().add(m);

        // Act
        invocationMatcher.captureArgumentsFrom(i);

        // Assert
        verify((CapturesArguments)m).captureFrom(i.getArguments()[0]);
    }
}
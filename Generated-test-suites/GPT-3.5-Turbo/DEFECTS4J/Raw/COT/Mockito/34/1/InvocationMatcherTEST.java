import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.mockito.internal.invocation.InvocationMatcher;
import org.hamcrest.Matcher;

public class InvocationMatcherTest {

    @Test
    public void testGetMethod() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        Method method = invocationMatcher.getMethod();

        // Assert
        assertNotNull(method);
    }

    @Test
    public void testGetInvocation() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        Invocation actualInvocation = invocationMatcher.getInvocation();

        // Assert
        assertEquals(invocation, actualInvocation);
    }

    @Test
    public void testGetMatchers() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        List<Matcher> matchers = invocationMatcher.getMatchers();

        // Assert
        assertTrue(matchers.isEmpty());
    }

    // Additional test cases for potential edge cases and exception handling scenarios
    @Test
    public void testMatches_WhenArgumentsMatch() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        boolean matches = invocationMatcher.matches(invocation);

        // Assert
        assertTrue(matches);
    }

    @Test
    public void testMatches_WhenArgumentsDoNotMatch() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation actualInvocation = mock(Invocation.class);
        when(actualInvocation.getMock()).thenReturn(mock(Object.class));
        when(actualInvocation.getMethod()).thenReturn(mock(Method.class));

        // Act
        boolean matches = invocationMatcher.matches(actualInvocation);

        // Assert
        assertFalse(matches);
    }

    @Test
    public void testHasSimilarMethod_WhenMethodIsSame() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(invocation.getMethod());
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn(invocation.getMock());

        // Act
        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidate);

        // Assert
        assertTrue(hasSimilarMethod);
    }

    @Test
    public void testHasSimilarMethod_WhenMethodIsOverloadedWithDifferentArguments() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn(invocation.getMock());
        when(candidate.getArguments()).thenReturn(new Object[] {1, 2});

        // Act
        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidate);

        // Assert
        assertFalse(hasSimilarMethod);
    }

    @Test
    public void testHasSameMethod_WhenMethodIsSame() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(invocation.getMethod());

        // Act
        boolean hasSameMethod = invocationMatcher.hasSameMethod(candidate);

        // Assert
        assertTrue(hasSameMethod);
    }

    @Test
    public void testHasSameMethod_WhenMethodIsDifferent() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(mock(Method.class));

        // Act
        boolean hasSameMethod = invocationMatcher.hasSameMethod(candidate);

        // Assert
        assertFalse(hasSameMethod);
    }

    @Test
    public void testGetLocation() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        Location actualLocation = invocationMatcher.getLocation();

        // Assert
        assertEquals(location, actualLocation);
    }

    @Test
    public void testToString() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        String toString = invocationMatcher.toString();

        // Assert
        assertNotNull(toString);
    }
}


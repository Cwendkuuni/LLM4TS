
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithMatchers_shouldInitializeMatchers() throws NoSuchMethodException {
        // Arrange
        Invocation invocation = new Invocation(
                mock(Object.class),
                SomeClass.class.getMethod("methodName", Integer.class, String.class),
                new Object[] { 5, "test" }
        );
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(new RefEq(5));
        matchers.add(new IsEqual("test"));

        // Act
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // Assert
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithoutMatchers_shouldInitializeMatchersFromInvocation() throws NoSuchMethodException {
        // Arrange
        Invocation invocation = new Invocation(
                mock(Object.class),
                SomeClass.class.getMethod("methodName", Integer.class, String.class),
                new Object[] { 5, "test" }
        );

        // Act
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Assert
        assertEquals(invocation.argumentsToMatchers(), invocationMatcher.getMatchers());
    }

    @Test
    public void testGetMethod_shouldReturnMethod() throws NoSuchMethodException {
        // Arrange
        Invocation invocation = new Invocation(
                mock(Object.class),
                SomeClass.class.getMethod("methodName", Integer.class, String.class),
                new Object[] { 5, "test" }
        );
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        Method method = invocationMatcher.getMethod();

        // Assert
        assertEquals(SomeClass.class.getMethod("methodName", Integer.class, String.class), method);
    }

    @Test
    public void testGetInvocation_shouldReturnInvocation() throws NoSuchMethodException {
        // Arrange
        Invocation invocation = new Invocation(
                mock(Object.class),
                SomeClass.class.getMethod("methodName", Integer.class, String.class),
                new Object[] { 5, "test" }
        );
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        Invocation result = invocationMatcher.getInvocation();

        // Assert
        assertEquals(invocation, result);
    }

    @Test
    public void testToString_shouldReturnInvocationStringRepresentation() throws NoSuchMethodException {
        // Arrange
        Invocation invocation = new Invocation(
                mock(Object.class),
                SomeClass.class.getMethod("methodName", Integer.class, String.class),
                new Object[] { 5, "test" }
        );
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        String result = invocationMatcher.toString();

        // Assert
        assertEquals("[Mockito] methodName(5, test)", result);
    }

    @Test
    public void testMatches_withMatchingInvocation_shouldReturnTrue() throws NoSuchMethodException {
        // Arrange
        Invocation invocation = new Invocation(
                mock(Object.class),
                SomeClass.class.getMethod("methodName", Integer.class, String.class),
                new Object[] { 5, "test" }
        );
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation actualInvocation = new Invocation(
                mock(Object.class),
                SomeClass.class.getMethod("methodName", Integer.class, String.class),
                new Object[] { 5, "test" }
        );

        // Act
        boolean result = invocationMatcher.matches(actualInvocation);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testMatches_withDifferentMock_shouldReturnFalse() throws NoSuchMethodException {
        // Arrange
        Invocation invocation = new Invocation(
                mock(Object.class),
                SomeClass.class.getMethod("methodName", Integer.class, String.class),
                new Object[] { 5, "test" }
        );
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation actualInvocation = new Invocation(
                mock(Object.class), // Different mock
                SomeClass.class.getMethod("methodName", Integer.class, String.class),
                new Object[] { 5, "test" }
        );

        // Act
        boolean result = invocationMatcher.matches(actualInvocation);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testMatches_withDifferentMethod_shouldReturnFalse() throws NoSuchMethodException {
        // Arrange
        Invocation invocation = new Invocation(
                mock(Object.class),
                SomeClass.class.getMethod("methodName", Integer.class, String.class),
                new Object[] { 5, "test" }
        );
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation actualInvocation = new Invocation(
                mock(Object.class),
                SomeClass.class.getMethod("differentMethodName", Integer.class, String.class), // Different method
                new Object[] { 5, "test" }
        );

        // Act
        boolean result = invocationMatcher.matches(actualInvocation);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_withSameMethodNameAndUnverified_shouldReturnTrue() throws NoSuchMethodException {
        // Arrange
        Invocation invocation = new Invocation(
                mock(Object.class),
                SomeClass.class.getMethod("methodName", Integer.class, String.class),
                new Object[] { 5, "test" }
        );
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation candidate = new Invocation(
                mock(Object.class),
                SomeClass.class.getMethod("methodName", Integer.class, String.class),
                new Object[] { 5, "test" }
        );

        // Act
        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testHasSimilarMethod_withDifferentMethodName_shouldReturnFalse() throws NoSuchMethodException {
        // Arrange
        Invocation invocation = new Invocation(
                mock(Object.class),
                SomeClass.class.getMethod("methodName", Integer.class, String.class),
                new Object[] { 5, "test" }
        );
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation candidate = new Invocation(
                mock(Object.class),
                SomeClass.class.getMethod("differentMethodName", Integer.class, String.class),
                new Object[] { 5, "test" }
        );

        // Act
        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_withVerifiedCandidate_shouldReturnFalse() throws NoSuchMethodException {
        // Arrange
        Invocation invocation = new Invocation(
                mock(Object.class),
                SomeClass.class.getMethod("methodName", Integer.class, String.class),
                new Object[] { 5, "test" }
        );
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation candidate = new Invocation(
                mock(Object.class),
                SomeClass.class.getMethod("methodName", Integer.class, String.class),
                new Object[] { 5, "test" }
        );
        candidate.markVerified();

        // Act
        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_withDifferentMock_shouldReturnFalse() throws NoSuchMethodException {
        // Arrange
        Invocation invocation = new Invocation(
                mock(Object.class),
                SomeClass.class.getMethod("methodName", Integer.class, String.class),
                new Object[] { 5, "test" }
        );
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation candidate = new Invocation(
                mock(Object.class), // Different mock
                SomeClass.class.getMethod("methodName", Integer.class, String.class),
                new Object[] { 5, "test" }
        );

        // Act
        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_withSameMethodAndOverloadedButDifferentArgs_shouldReturnFalse() throws NoSuchMethodException {
        // Arrange
        Invocation invocation = new Invocation(
                mock(Object.class),
                SomeClass.class.getMethod("methodName", Integer.class),
                new Object[] { 5 }
        );
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation candidate = new Invocation(
                mock(Object.class),
                SomeClass.class.getMethod("methodName", String.class), // Overloaded method with different args
                new Object[] { "test" }
        );

        // Act
        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testHasSameMethod_withSameMethod_shouldReturnTrue() throws NoSuchMethodException {
        // Arrange
        Invocation invocation = new Invocation(
                mock(Object.class),
                SomeClass.class.getMethod("methodName", Integer.class, String.class),
                new Object[] { 5, "test" }
        );
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation candidate = new Invocation(
                mock(Object.class),
                SomeClass.class.getMethod("methodName", Integer.class, String.class),
                new Object[] { 5, "test" }
        );

        // Act
        boolean result = invocationMatcher.hasSameMethod(candidate);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testHasSameMethod_withDifferentMethod_shouldReturnFalse() throws NoSuchMethodException {
        // Arrange
        Invocation invocation = new Invocation(
                mock(Object.class),
                SomeClass.class.getMethod("methodName", Integer.class, String.class),
                new Object[] { 5, "test" }
        );
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation candidate = new Invocation(
                mock(Object.class),
                SomeClass.class.getMethod("differentMethodName", Integer.class, String.class),
                new Object[] { 5, "test" }
        );

        // Act
        boolean result = invocationMatcher.hasSameMethod(candidate);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testGetLocation_shouldReturnInvocationLocation() throws NoSuchMethodException {
        // Arrange
        SomeClass mock = mock(SomeClass.class);
        Invocation invocation = new Invocation(
                mock,
                SomeClass.class.getMethod("methodName", Integer.class, String.class),
                new Object[] { 5, "test" }
        );
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        Location location = invocationMatcher.getLocation();

        // Assert
        assertEquals(mock, location.getMock());
        assertEquals(SomeClass.class.getMethod("methodName", Integer.class, String.class), location.getMethod());
    }

    @Test
    public void testToString_withPrintSettings_shouldReturnInvocationStringRepresentationWithPrintSettings() throws NoSuchMethodException {
        // Arrange
        Invocation invocation = new Invocation(
                mock(Object.class),
                SomeClass.class.getMethod("methodName", Integer.class, String.class),
                new Object[] { 5, "test" }
        );
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        PrintSettings printSettings = new PrintSettings();
        printSettings.setMultiline(true);

        // Act
        String result = invocationMatcher.toString(printSettings);

        // Assert
        assertEquals("[Mockito]\nmethodName(\n    5,\n    test\n)", result);
    }

    @Test
    public void testCaptureArgumentsFrom_shouldCaptureArgumentsFromInvocation() throws NoSuchMethodException {
        // Arrange
        Invocation invocation = new Invocation(
                mock(Object.class),
                SomeClass.class.getMethod("methodName", Integer.class, String.class),
                new Object[] { 5, "test" }
        );

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation i = new Invocation(
                mock(Object.class),
                SomeClass.class.getMethod("methodName", Integer.class, String.class),
                null
        );
        i.setArguments(new Object[] { 10, "captured" });
        invocationMatcher.captureArgumentsFrom(i);

        // Act
        List<Matcher> matchers = invocationMatcher.getMatchers();

        // Assert
        assertEquals(new RefEq(10), matchers.get(0));
        assertEquals(new IsEqual("captured"), matchers.get(1));
    }

    private static class SomeClass {
        public void methodName(Integer arg1, String arg2) {}
        public void differentMethodName(Integer arg1, String arg2) {}
    }
}

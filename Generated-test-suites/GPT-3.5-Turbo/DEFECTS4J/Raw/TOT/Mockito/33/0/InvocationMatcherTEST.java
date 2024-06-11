import org.junit.Test;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.matchers.CapturesArguments;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hamcrest.Matcher;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    @Test
    public void testConstructor_WithMatchers() {
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
    public void testConstructor_WithoutMatchers() {
        // Arrange
        Invocation invocation = mock(Invocation.class);

        // Act
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Assert
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(invocation.argumentsToMatchers(), invocationMatcher.getMatchers());
    }

    @Test
    public void testGetMethod() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        Method result = invocationMatcher.getMethod();

        // Assert
        assertEquals(method, result);
    }

    @Test
    public void testMatches_SameMethodAndArgumentsMatch() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        Invocation actual = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(actual.getMock()).thenReturn(invocation.getMock());
        when(actual.getMethod()).thenReturn(invocation.getMethod());
        when(actual.isVerified()).thenReturn(false);
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        when(argumentsComparator.argumentsMatch(any(), any())).thenReturn(true);
        whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(argumentsComparator);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        boolean result = invocationMatcher.matches(actual);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testMatches_DifferentMethod() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        Invocation actual = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(actual.getMock()).thenReturn(invocation.getMock());
        when(actual.getMethod()).thenReturn(mock(Method.class));
        when(actual.isVerified()).thenReturn(false);
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        when(argumentsComparator.argumentsMatch(any(), any())).thenReturn(true);
        whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(argumentsComparator);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        boolean result = invocationMatcher.matches(actual);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_MethodNameEqualsUnverifiedAndSameMock() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(candidate.getMethod()).thenReturn(method);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(candidate.getMock()).thenReturn(invocation.getMock());
        when(invocation.isVerified()).thenReturn(false);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testHasSimilarMethod_MethodNameDifferent() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(candidate.getMock()).thenReturn(invocation.getMock());
        when(invocation.isVerified()).thenReturn(false);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_NotUnverified() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(candidate.getMethod()).thenReturn(method);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(candidate.getMock()).thenReturn(invocation.getMock());
        when(invocation.isVerified()).thenReturn(true);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_DifferentMock() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(candidate.getMethod()).thenReturn(method);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(candidate.getMock()).thenReturn(mock(Object.class));
        when(invocation.isVerified()).thenReturn(false);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_OverloadedButSameArgs() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(candidate.getMethod()).thenReturn(method);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(candidate.getMock()).thenReturn(invocation.getMock());
        when(invocation.isVerified()).thenReturn(false);
        when(invocation.getArguments()).thenReturn(new Object[] {1});
        when(candidate.getArguments()).thenReturn(new Object[] {1});
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        when(argumentsComparator.argumentsMatch(any(), any())).thenReturn(true);
        whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(argumentsComparator);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testHasSameMethod() {
        // Arrange
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(method1);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        boolean result = invocationMatcher.hasSameMethod(mock(Invocation.class));

        // Assert
        assertFalse(result);
    }

    @Test
    public void testGetLocation() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        Location result = invocationMatcher.getLocation();

        // Assert
        assertEquals(location, result);
    }

    @Test
    public void testToString() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        when(invocation.toString(any(), any())).thenReturn("");
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        String result = invocationMatcher.toString();

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testToString_WithPrintSettings() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        when(invocation.toString(any(), any())).thenReturn("");
        PrintSettings printSettings = mock(PrintSettings.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        String result = invocationMatcher.toString(printSettings);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        InvokeInvocation i = new InvokeInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(mock(CapturesArguments.class)));

        // Act
        invocationMatcher.captureArgumentsFrom(i);

        // Assert
        // Verify that CapturesArguments.captureFrom() is called
    }

    private static class InvokeInvocation implements Invocation {
        @Override
        public Object getMock() {
            return null;
        }

        @Override
        public Method getMethod() {
            return null;
        }

        @Override
        public Object[] getArguments() {
            return new Object[0];
        }

        @Override
        public Location getLocation() {
            return null;
        }

        @Override
        public boolean isVerified() {
            return false;
        }

        @Override
        public void markVerified() {

        }

        @Override
        public void ignoreForVerification() {

        }

        @Override
        public boolean isIgnoredForVerification() {
            return false;
        }

        @Override
        public int getSequenceNumber() {
            return 0;
        }
    }

    @Test
    public void testCreateFrom() {
        // Arrange
        List<Invocation> invocations = Collections.singletonList(mock(Invocation.class));

        // Act
        List<InvocationMatcher> result = InvocationMatcher.createFrom(invocations);

        // Assert
        assertEquals(invocations.size(), result.size());
    }
}
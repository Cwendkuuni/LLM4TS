import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    @Test
    public void constructor_withInvocationAndMatchers_initializesFields() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Collections.emptyList();

        // Act
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        // Assert
        assertSame(invocation, matcher.getInvocation());
        assertSame(matchers, matcher.getMatchers());
    }

    @Test
    public void constructor_withInvocation_onlyInitializesInvocationField() {
        // Arrange
        Invocation invocation = mock(Invocation.class);

        // Act
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        // Assert
        assertSame(invocation, matcher.getInvocation());
        assertEquals(Collections.emptyList(), matcher.getMatchers());
    }

    @Test
    public void getMethod_returnsMethodFromInvocation() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        Method method = Object.class.getDeclaredMethods()[0];
        when(invocation.getMethod()).thenReturn(method);
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        // Act
        Method result = matcher.getMethod();

        // Assert
        assertSame(method, result);
    }

    @Test
    public void toString_returnsPrintSettingsPrintResult() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Collections.emptyList();
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);
        String expectedResult = "Expected result";

        PrintSettings printSettings = mock(PrintSettings.class);
        when(printSettings.print(matchers, invocation)).thenReturn(expectedResult);

        try {
            FieldUtils.writeDeclaredStaticField(InvocationMatcher.class, "PRINT_SETTINGS", printSettings, true);

            // Act
            String result = matcher.toString();

            // Assert
            assertEquals(expectedResult, result);
        } finally {
            FieldUtils.writeDeclaredStaticField(InvocationMatcher.class, "PRINT_SETTINGS", new PrintSettings(), true);
        }
    }

    @Test
    public void matches_withMatchingInvocation_returnsTrue() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        Invocation actual = mock(Invocation.class);
        when(invocation.getMock()).thenReturn("mock");
        when(invocation.getMethod()).thenReturn(Object.class.getDeclaredMethods()[0]);
        when(invocation.getArguments()).thenReturn(new Object[]{1, 2, 3});
        when(actual.getMock()).thenReturn("mock");
        when(actual.getMethod()).thenReturn(Object.class.getDeclaredMethods()[0]);
        when(actual.getArguments()).thenReturn(new Object[]{1, 2, 3});
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        // Act
        boolean result = matcher.matches(actual);

        // Assert
        assertTrue(result);
    }

    @Test
    public void matches_withNonMatchingInvocation_returnsFalse() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        Invocation actual = mock(Invocation.class);
        when(invocation.getMock()).thenReturn("mock");
        when(invocation.getMethod()).thenReturn(Object.class.getDeclaredMethods()[0]);
        when(invocation.getArguments()).thenReturn(new Object[]{1, 2, 3});
        when(actual.getMock()).thenReturn("differentMock");
        when(actual.getMethod()).thenReturn(Object.class.getDeclaredMethods()[0]);
        when(actual.getArguments()).thenReturn(new Object[]{1, 2, 4});
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        // Act
        boolean result = matcher.matches(actual);

        // Assert
        assertFalse(result);
    }

    @Test
    public void hasSimilarMethod_withDifferentMethodName_returnsFalse() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(Object.class.getDeclaredMethods()[0]);
        when(candidate.getMethod()).thenReturn(Object.class.getDeclaredMethods()[1]);
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        // Act
        boolean result = matcher.hasSimilarMethod(candidate);

        // Assert
        assertFalse(result);
    }

    @Test
    public void hasSimilarMethod_withVerifiedCandidate_returnsFalse() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(Object.class.getDeclaredMethods()[0]);
        when(invocation.isVerified()).thenReturn(false);
        when(candidate.getMethod()).thenReturn(Object.class.getDeclaredMethods()[0]);
        when(candidate.isVerified()).thenReturn(true);
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        // Act
        boolean result = matcher.hasSimilarMethod(candidate);

        // Assert
        assertFalse(result);
    }

    @Test
    public void hasSimilarMethod_withDifferentMock_returnsFalse() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(Object.class.getDeclaredMethods()[0]);
        when(invocation.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn("mock1");
        when(candidate.getMethod()).thenReturn(Object.class.getDeclaredMethods()[0]);
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn("mock2");
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        // Act
        boolean result = matcher.hasSimilarMethod(candidate);

        // Assert
        assertFalse(result);
    }

    @Test
    public void hasSimilarMethod_withDifferentArgumentsSameMethodNotOverloaded_returnsTrue() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(Object.class.getDeclaredMethods()[0]);
        when(invocation.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn("mock");
        when(invocation.getArguments()).thenReturn(new Object[]{1, 2, 3});
        when(candidate.getMethod()).thenReturn(Object.class.getDeclaredMethods()[0]);
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn("mock");
        when(candidate.getArguments()).thenReturn(new Object[]{1, 2, 4});
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        // Act
        boolean result = matcher.hasSimilarMethod(candidate);

        // Assert
        assertTrue(result);
    }

    @Test
    public void hasSimilarMethod_withDifferentArgumentsOverloadedMethod_returnsFalse() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(Object.class.getDeclaredMethods()[0]);
        when(invocation.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn("mock");
        when(invocation.getArguments()).thenReturn(new Object[]{1, 2, 3});
        when(candidate.getMethod()).thenReturn(Object.class.getDeclaredMethods()[0]);
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn("mock");
        when(candidate.getArguments()).thenReturn(new Object[]{1, 2, 3});
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        // Act
        boolean result = matcher.hasSimilarMethod(candidate);

        // Assert
        assertFalse(result);
    }

    @Test
    public void hasSameMethod_withMatchingMethods_returnsTrue() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(Object.class.getDeclaredMethods()[0]);
        when(candidate.getMethod()).thenReturn(Object.class.getDeclaredMethods()[0]);
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        // Act
        boolean result = matcher.hasSameMethod(candidate);

        // Assert
        assertTrue(result);
    }

    @Test
    public void hasSameMethod_withDifferentMethods_returnsFalse() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(Object.class.getDeclaredMethods()[0]);
        when(candidate.getMethod()).thenReturn(Object.class.getDeclaredMethods()[1]);
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        // Act
        boolean result = matcher.hasSameMethod(candidate);

        // Assert
        assertFalse(result);
    }

    @Test
    public void getLocation_returnsInvocationLocation() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        // Act
        Location result = matcher.getLocation();

        // Assert
        assertSame(location, result);
    }

    @Test
    public void captureArgumentsFrom_withVarArgs_invokesCaptureFromForAllArguments() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(Object.class.getDeclaredMethods()[0]);
        when(invocation.isVarArgs()).thenReturn(true);
        when(invocation.getRawArguments()).thenReturn(new Object[]{1, 2, 3, new int[]{4, 5}});
        CapturesArguments firstMatcher = mock(CapturesArguments.class);
        CapturesArguments secondMatcher = mock(CapturesArguments.class);
        List<Matcher> matchers = Arrays.asList(firstMatcher, secondMatcher);
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        // Act
        matcher.captureArgumentsFrom(invocation);

        // Assert
        verify(firstMatcher).captureFrom(1);
        verify(firstMatcher).captureFrom(2);
        verify(firstMatcher).captureFrom(3);
        verify(secondMatcher).captureFrom(4);
        verify(secondMatcher).captureFrom(5);
    }

    @Test
    public void captureArgumentsFrom_withoutVarArgs_invokesCaptureFromForEachArgument() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(Object.class.getDeclaredMethods()[0]);
        when(invocation.isVarArgs()).thenReturn(false);
        when(invocation.getArgumentAt(0, Object.class)).thenReturn(1);
        when(invocation.getArgumentAt(1, Object.class)).thenReturn(2);
        when(invocation.getArgumentAt(2, Object.class)).thenReturn(3);
        CapturesArguments firstMatcher = mock(CapturesArguments.class);
        CapturesArguments secondMatcher = mock(CapturesArguments.class);
        CapturesArguments thirdMatcher = mock(CapturesArguments.class);
        List<Matcher> matchers = Arrays.asList(firstMatcher, secondMatcher, thirdMatcher);
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        // Act
        matcher.captureArgumentsFrom(invocation);

        // Assert
        verify(firstMatcher).captureFrom(1);
        verify(secondMatcher).captureFrom(2);
        verify(thirdMatcher).captureFrom(3);
    }

    @Test
    public void createFrom_convertsListOfInvocationsToInvocationMatchers() {
        // Arrange
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<Invocation> invocations = Arrays.asList(invocation1, invocation2);

        // Act
        List<InvocationMatcher> matchers = InvocationMatcher.createFrom(invocations);

        // Assert
        assertEquals(2, matchers.size());
        assertSame(invocation1, matchers.get(0).getInvocation());
        assertSame(invocation2, matchers.get(1).getInvocation());
    }

}


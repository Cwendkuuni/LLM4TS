import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class InvocationMatcherTest {

    private Invocation invocation;
    private InvocationMatcher invocationMatcher;
    private CapturesArguments mockMatcher;

    @Before
    public void setup() {
        invocation = mock(Invocation.class);
        mockMatcher = mock(CapturesArguments.class);
        invocationMatcher = new InvocationMatcher(invocation);
    }

    @Test
    public void constructor_withInvocationAndMatchArgs_shouldCorrectlyInitializeInvocationMatchers() {
        // Arrange
        List<Matcher> matchers = Arrays.asList(mockMatcher);

        // Act
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // Assert
        MatcherAssert.assertThat(invocationMatcher.getInvocation(), CoreMatchers.equalTo(invocation));
        MatcherAssert.assertThat(invocationMatcher.getMatchers(), CoreMatchers.equalTo(matchers));
    }

    @Test
    public void constructor_withInvocationOnly_shouldCorrectlyInitializeInvocationMatchers() {
        // Act
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Assert
        MatcherAssert.assertThat(invocationMatcher.getInvocation(), CoreMatchers.equalTo(invocation));
        MatcherAssert.assertThat(invocationMatcher.getMatchers(), CoreMatchers.equalTo(invocation.argumentsToMatchers()));
    }

    @Test
    public void getMethod_shouldReturnInvocationMethod() {
        // Arrange
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);

        // Act
        Method result = invocationMatcher.getMethod();

        // Assert
        MatcherAssert.assertThat(result, CoreMatchers.equalTo(method));
    }

    @Test
    public void getInvocation_shouldReturnInvocation() {
        // Act
        Invocation result = invocationMatcher.getInvocation();

        // Assert
        MatcherAssert.assertThat(result, CoreMatchers.equalTo(invocation));
    }

    @Test
    public void getMatchers_shouldReturnMatchers() {
        // Act
        List<Matcher> result = invocationMatcher.getMatchers();

        // Assert
        MatcherAssert.assertThat(result, CoreMatchers.equalTo(invocation.argumentsToMatchers()));
    }

    @Test
    public void toString_shouldReturnInvocationToStringRepresentation() {
        // Arrange
        PrintSettings printSettings = new PrintSettings();
        String toStringResult = "InvocationToStringResult";
        when(invocation.toString(invocationMatcher.getMatchers(), printSettings)).thenReturn(toStringResult);

        // Act
        String result = invocationMatcher.toString(printSettings);

        // Assert
        MatcherAssert.assertThat(result, CoreMatchers.equalTo(toStringResult));
    }

    @Test
    public void matches_withMatchingInvocation_shouldReturnTrue() {
        // Arrange
        Invocation actual = mock(Invocation.class);
        when(invocation.getMock()).thenReturn("MockObject");
        when(invocation.getMethod()).thenReturn(method);
        when(actual.getMock()).thenReturn("MockObject");
        when(actual.getMethod()).thenReturn(method);
        when(actual.getArguments()).thenReturn(new Object[]{1, 2, 3});
        when(new ArgumentsComparator().argumentsMatch(invocationMatcher, actual)).thenReturn(true);

        // Act
        boolean result = invocationMatcher.matches(actual);

        // Assert
        MatcherAssert.assertThat(result, CoreMatchers.equalTo(true));
    }

    @Test
    public void matches_withNonMatchingInvocation_shouldReturnFalse() {
        // Arrange
        Invocation actual = mock(Invocation.class);
        when(invocation.getMock()).thenReturn("MockObject");
        when(invocation.getMethod()).thenReturn(method);
        when(actual.getMock()).thenReturn("AnotherMockObject");
        when(actual.getMethod()).thenReturn(method);
        when(actual.getArguments()).thenReturn(new Object[]{1, 2, 3});
        when(new ArgumentsComparator().argumentsMatch(invocationMatcher, actual)).thenReturn(false);

        // Act
        boolean result = invocationMatcher.matches(actual);

        // Assert
        MatcherAssert.assertThat(result, CoreMatchers.equalTo(false));
    }

    @Test
    public void hasSimilarMethod_withMatchingMethod_shouldReturnTrue() {
        // Arrange
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(method);
        when(method.getName()).thenReturn("methodName");
        when(candidate.getMethod()).thenReturn(method);
        when(candidate.getMethod().getName()).thenReturn("methodName");
        when(candidate.isVerified()).thenReturn(false);
        when(getInvocation().getMock() == candidate.getMock()).thenReturn(true);
        when(hasSameMethod(candidate)).thenReturn(true);
        when(safelyArgumentsMatch(candidate.getArguments())).thenReturn(false);

        // Act
        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        // Assert
        MatcherAssert.assertThat(result, CoreMatchers.equalTo(true));
    }

    @Test
    public void hasSimilarMethod_withMethodNameNotEqual_shouldReturnFalse() {
        // Arrange
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(method);
        when(method.getName()).thenReturn("methodName");
        when(candidate.getMethod()).thenReturn(method);
        when(candidate.getMethod().getName()).thenReturn("anotherMethodName");

        // Act
        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        // Assert
        MatcherAssert.assertThat(result, CoreMatchers.equalTo(false));
    }

    @Test
    public void hasSimilarMethod_withVerifiedCandidate_shouldReturnFalse() {
        // Arrange
        Invocation candidate = mock(Invocation.class);
        when(candidate.isVerified()).thenReturn(true);

        // Act
        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        // Assert
        MatcherAssert.assertThat(result, CoreMatchers.equalTo(false));
    }

    @Test
    public void hasSimilarMethod_withDifferentMocks_shouldReturnFalse() {
        // Arrange
        Invocation candidate = mock(Invocation.class);
        when(getInvocation().getMock() == candidate.getMock()).thenReturn(false);

        // Act
        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        // Assert
        MatcherAssert.assertThat(result, CoreMatchers.equalTo(false));
    }

    @Test
    public void hasSimilarMethod_withSameMethodAndSameCandidatesArguments_shouldReturnFalse() {
        // Arrange
        Invocation candidate = mock(Invocation.class);
        when(safelyArgumentsMatch(candidate.getArguments())).thenReturn(true);
        when(hasSameMethod(candidate)).thenReturn(true);

        // Act
        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        // Assert
        MatcherAssert.assertThat(result, CoreMatchers.equalTo(false));
    }

    @Test
    public void getLocation_shouldReturnInvocationLocation() {
        // Arrange
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);

        // Act
        Location result = invocationMatcher.getLocation();

        // Assert
        MatcherAssert.assertThat(result, CoreMatchers.equalTo(location));
    }

    @Test
    public void toString_withPrintSettings_shouldReturnInvocationToStringRepresentation() {
        // Arrange
        PrintSettings printSettings = mock(PrintSettings.class);
        String toStringResult = "InvocationToStringResult";
        when(invocation.toString(invocationMatcher.getMatchers(), printSettings)).thenReturn(toStringResult);

        // Act
        String result = invocationMatcher.toString(printSettings);

        // Assert
        MatcherAssert.assertThat(result, CoreMatchers.equalTo(toStringResult));
    }

    @Test
    public void captureArgumentsFrom_withCapturerMatcher_shouldCaptureArguments() {
        // Arrange
        Invocation i = mock(Invocation.class);
        Object[] arguments = new Object[]{1, 2, 3};
        when(i.getArguments()).thenReturn(arguments);
        when(matcher instanceof CapturesArguments).thenReturn(true);

        // Act
        invocationMatcher.captureArgumentsFrom(i);

        // Assert
        MatcherAssert.assertThat(invocationMatcher.getMatchers(), CoreMatchers.equalTo(arguments));
    }

    @Test
    public void createFrom_withInvocationList_shouldReturnInvocationMatchersList() {
        // Arrange
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<Invocation> invocations = Arrays.asList(invocation1, invocation2);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        // Act
        List<InvocationMatcher> result = InvocationMatcher.createFrom(invocations);

        // Assert
        MatcherAssert.assertThat(result, CoreMatchers.hasItems(invocationMatcher1, invocationMatcher2));
    }
}
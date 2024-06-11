import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;

@SuppressWarnings("unchecked")
public class InvocationMatcherTest {

    @Test
    public void testConstructor_withMatchersNotEmpty() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Collections.singletonList(mock(Matcher.class));

        // Act
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // Assert
        assertThat(invocationMatcher.getInvocation(), is(invocation));
        assertThat(invocationMatcher.getMatchers(), is(matchers));
    }
    
    @Test
    public void testConstructor_withMatchersEmpty() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        when(invocation.argumentsToMatchers()).thenReturn(Collections.emptyList());

        // Act
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Assert
        assertThat(invocationMatcher.getInvocation(), is(invocation));
        assertThat(invocationMatcher.getMatchers(), is(invocation.argumentsToMatchers()));
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
        assertThat(result, is(method));
    }

    @Test
    public void testMatches_withMatchingInvocation() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        Invocation actualInvocation = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(actualInvocation.getMock()).thenReturn(invocation.getMock());
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(actualInvocation.getMethod()).thenReturn(invocation.getMethod());
        when(invocation.argumentsMatch(actualInvocation)).thenReturn(true);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        boolean result = invocationMatcher.matches(actualInvocation);

        // Assert
        assertThat(result, is(true));
    }

    @Test
    public void testMatches_withNonMatchingInvocation() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        Invocation actualInvocation = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(actualInvocation.getMock()).thenReturn(invocation.getMock());
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(actualInvocation.getMethod()).thenReturn(invocation.getMethod());
        when(invocation.argumentsMatch(actualInvocation)).thenReturn(false);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        boolean result = invocationMatcher.matches(actualInvocation);

        // Assert
        assertThat(result, is(false));
    }

    @Test
    public void testHasSimilarMethod_withMethodNameNotEquals() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        // Assert
        assertThat(result, is(false));
    }

    @Test
    public void testHasSimilarMethod_withIsVerified() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(candidate.isVerified()).thenReturn(true);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        // Assert
        assertThat(result, is(false));
    }

    @Test
    public void testHasSimilarMethod_withMockIsNotTheSame() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getMock()).thenReturn(mock1);
        when(candidate.getMock()).thenReturn(mock2);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        // Assert
        assertThat(result, is(false));
    }

    @Test
    public void testHasSimilarMethod_withOverloadedButSameArgs() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getArguments()).thenReturn(new Object[]{1});
        when(candidate.getArguments()).thenReturn(new Object[]{1});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        // Assert
        assertThat(result, is(false));
    }

    @Test
    public void testHasSimilarMethod_withSameMethod() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        // Assert
        assertThat(result, is(true));
    }

    @Test
    public void testHasSameMethod_withSameMethod() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(candidate.getMethod()).thenReturn(method);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        boolean result = invocationMatcher.hasSameMethod(candidate);

        // Assert
        assertThat(result, is(true));
    }

    @Test
    public void testHasSameMethod_withDifferentMethods() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        boolean result = invocationMatcher.hasSameMethod(candidate);

        // Assert
        assertThat(result, is(false));
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
        assertThat(result, is(location));
    }

    @Test
    public void testToString_withDefaultSettings() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Collections.singletonList(mock(Matcher.class));
        String invocationString = "invocation";
        PrintSettings printSettings = new PrintSettings();
        when(invocation.toString(matchers, printSettings)).thenReturn(invocationString);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // Act
        String result = invocationMatcher.toString();

        // Assert
        assertThat(result, is(invocationString));
    }

    @Test
    public void testToString_withCustomSettings() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Collections.singletonList(mock(Matcher.class));
        String invocationString = "invocation";
        PrintSettings printSettings = mock(PrintSettings.class);
        when(invocation.toString(matchers, printSettings)).thenReturn(invocationString);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // Act
        String result = invocationMatcher.toString(printSettings);

        // Assert
        assertThat(result, is(invocationString));
    }

    @Test
    public void testCaptureArgumentsFrom_withCapturesArgumentsMatcher() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        Invocation i = mock(Invocation.class);
        Matcher matcher = mock(Matcher.class);
        CapturesArguments capturesArguments = mock(CapturesArguments.class);
        when(matcher instanceof CapturesArguments).thenReturn(true);
        when(i.getArguments()).thenReturn(new Object[]{1});
        when(invocation.getArgumentsToMatchers()).thenReturn(Collections.singletonList(matcher));
        when(invocation.getArguments()).thenReturn(new Object[]{1});
        when(matcher).thenReturn(capturesArguments);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        invocationMatcher.captureArgumentsFrom(i);

        // Assert
        verify(capturesArguments).captureFrom(1);
    }

    @Test
    public void testCaptureArgumentsFrom_withNonCapturesArgumentsMatcher() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        Invocation i = mock(Invocation.class);
        Matcher matcher = mock(Matcher.class);
        when(matcher instanceof CapturesArguments).thenReturn(false);
        when(invocation.getArgumentsToMatchers()).thenReturn(Collections.singletonList(matcher));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        invocationMatcher.captureArgumentsFrom(i);

        // Assert
        verify(matcher).captureFrom(i);
    }

    @Test
    public void testCreateFrom() {
        // Arrange
        Invocation i1 = mock(Invocation.class);
        Invocation i2 = mock(Invocation.class);
        List<Invocation> invocations = Arrays.asList(i1, i2);

        // Act
        List<InvocationMatcher> result = InvocationMatcher.createFrom(invocations);

        // Assert
        assertThat(result.size(), is(2));
        assertThat(result.get(0).getInvocation(), is(i1));
        assertThat(result.get(1).getInvocation(), is(i2));
    }
}
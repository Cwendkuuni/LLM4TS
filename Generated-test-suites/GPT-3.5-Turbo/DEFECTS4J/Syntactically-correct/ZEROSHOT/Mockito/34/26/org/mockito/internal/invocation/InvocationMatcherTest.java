import org.junit.Test;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.ArgumentsComparator;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.exceptions.PrintableInvocation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithMatchers() {
        // Given
        Invocation invocation = new Invocation();
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(any());

        // When
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // Then
        assertThat(invocationMatcher.getInvocation(), is(invocation));
        assertThat(invocationMatcher.getMatchers(), is(matchers));
    }

    @Test
    public void testConstructorWithoutMatchers() {
        // Given
        Invocation invocation = new Invocation();

        // When
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Then
        assertThat(invocationMatcher.getInvocation(), is(invocation));
        assertThat(invocationMatcher.getMatchers(), is(invocation.argumentsToMatchers()));
    }

    @Test
    public void testGetMethod() {
        // Given
        Method method = new Method();

        Invocation invocation = new Invocation();
        invocation.setMethod(method);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // When
        Method result = invocationMatcher.getMethod();

        // Then
        assertThat(result, is(method));
    }

    @Test
    public void testToString() {
        // Given
        Invocation invocation = new Invocation();

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // When
        String result = invocationMatcher.toString();

        // Then
        assertThat(result, notNullValue());
        // TODO: Add more assertions
    }

    @Test
    public void testMatches() {
        // Given
        Invocation methodInvocation = new Invocation();
        methodInvocation.setMock(new Object());
        methodInvocation.setArguments(new Object[]{"arg1", "arg2"});

        InvocationMatcher invocationMatcher = new InvocationMatcher(methodInvocation);

        Invocation actualInvocation = new Invocation();
        actualInvocation.setMock(new Object());
        actualInvocation.setArguments(new Object[]{"arg1", "arg2"});

        // When
        boolean result = invocationMatcher.matches(actualInvocation);

        // Then
        assertThat(result, is(true));
    }

    @Test
    public void testHasSimilarMethod() {
        // Given
        Invocation invocation = new Invocation();
        invocation.setMethod(new Method());
        invocation.setMock(new Object());
        invocation.setArguments(new Object[]{"arg1", "arg2"});

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation candidateInvocation = new Invocation();
        candidateInvocation.setMethod(new Method());
        candidateInvocation.setMock(new Object());
        candidateInvocation.setArguments(new Object[]{"arg1", "arg2"});

        // When
        boolean result = invocationMatcher.hasSimilarMethod(candidateInvocation);

        // Then
        assertThat(result, is(false));
    }

    @Test
    public void testHasSameMethod() {
        // Given
        Invocation invocation = new Invocation();
        invocation.setMethod(new Method());

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation candidateInvocation = new Invocation();
        candidateInvocation.setMethod(new Method());

        // When
        boolean result = invocationMatcher.hasSameMethod(candidateInvocation);

        // Then
        assertThat(result, is(true));
    }

    @Test
    public void testGetLocation() {
        // Given
        Location location = new Location();

        Invocation invocation = new Invocation();
        invocation.setLocation(location);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // When
        Location result = invocationMatcher.getLocation();

        // Then
        assertThat(result, is(location));
    }

    @Test
    public void testToStringWithPrintSettings() {
        // Given
        Invocation invocation = new Invocation();

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        PrintSettings printSettings = new PrintSettings();

        // When
        String result = invocationMatcher.toString(printSettings);

        // Then
        assertThat(result, notNullValue());
        // TODO: Add more assertions
    }

    @Test
    public void testCaptureArgumentsFrom() {
        // Given
        Invocation invocation = new Invocation();
        invocation.setArguments(new Object[]{"arg1", "arg2"});

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation i = new Invocation();

        // When
        invocationMatcher.captureArgumentsFrom(i);

        // Then
        // TODO: Add assertions for capture arguments
    }
}
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsComparator;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.mockitoSession;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;
    private Invocation invocation;
    private List<Matcher> matchers;
    private MockitoSession session;

    @Before
    public void setUp() {
        session = new MockitoSession();
        invocation = session.createInvocation("mockObject", new Class[0], new Object[0]);
        matchers = Arrays.asList(is(1), is("test"));
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void shouldCreateInvocationMatcherWithEmptyList() {
        //Given
        InvocationMatcher invocationMatcherEmptyList = new InvocationMatcher(invocation);

        //Then
        assertThat(invocationMatcherEmptyList.getMatchers(), is(Collections.emptyList()));
    }

    @Test
    public void shouldCreateInvocationMatcherWithListOfMatchers() {
        //Given

        //When
        InvocationMatcher invocationMatcherWithMatchers = new InvocationMatcher(invocation, matchers);

        //Then
        assertThat(invocationMatcherWithMatchers.getMatchers(), sameInstance(matchers));
    }

    @Test
    public void testGetMethod() {
        //Given

        //When
        Method method = invocationMatcher.getMethod();

        //Then
        assertThat(method, notNullValue());
    }

    @Test
    public void testGetInvocation() {
        //Given

        //When
        Invocation invocationFromMatcher = invocationMatcher.getInvocation();

        //Then
        assertThat(invocationFromMatcher, sameInstance(invocation));
    }

    @Test
    public void testGetMatchers() {
        //Given

        //When
        List<Matcher> matchersFromMatcher = invocationMatcher.getMatchers();

        //Then
        assertThat(matchersFromMatcher, sameInstance(matchers));
    }

    @Test
    public void testToString() {
        //Given

        //When
        String toString = invocationMatcher.toString();

        //Then
        assertThat(toString, notNullValue());
    }

    @Test
    public void testMatches() {
        //Given
        Invocation actual = session.createInvocation("mockObject", new Class[0], new Object[0]);

        //When
        boolean matches = invocationMatcher.matches(actual);

        //Then
        assertThat(matches, is(true));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        //Given
        Object[] actualArgs = new Object[0];

        //When
        boolean result = invocationMatcher.safelyArgumentsMatch(actualArgs);

        //Then
        assertThat(result, is(true));
    }

    @Test
    public void testHasSimilarMethod() {
        //Given
        Invocation candidate = session.createInvocation("mockObject", new Class[0], new Object[0]);
        InvocationMatcher invocationMatcherCandidate = new InvocationMatcher(candidate);

        //When
        boolean result = invocationMatcher.hasSimilarMethod(invocationMatcherCandidate);

        //Then
        assertThat(result, is(true));
    }

    @Test
    public void testHasSameMethod() {
        //Given
        Invocation candidate = session.createInvocation("mockObject", new Class[0], new Object[0]);
        InvocationMatcher invocationMatcherCandidate = new InvocationMatcher(candidate);

        //When
        boolean result = invocationMatcher.hasSameMethod(invocationMatcherCandidate);

        //Then
        assertThat(result, is(true));
    }

    @Test
    public void testGetLocation() {
        //Given
        Location location = invocation.getLocation();

        //When
        Location locationFromMatcher = invocationMatcher.getLocation();

        //Then
        assertThat(locationFromMatcher, sameInstance(location));
    }

    @Test
    public void testToStringWithPrintSettings() {
        //Given
        PrintSettings printSettings = new PrintSettings();

        //When
        String toString = invocationMatcher.toString(printSettings);

        //Then
        assertThat(toString, notNullValue());
    }

    @Test
    public void testCaptureArgumentsFrom() {
        //Given
        Invocation target = session.createInvocation("mockObject", new Class[0], new Object[0]);

        //When
        invocationMatcher.captureArgumentsFrom(target);

        //Then
        ArgumentMatcher argumentMatcher = matchers.get(0);
        Object capturedArgument = target.getArguments()[0];
        argumentMatcher.verify(capturedArgument);
    }

    @Test(expected = Throwable.class)
    public void testCaptureArgumentsFromWithException() {
        //Given
        Invocation target = session.createInvocation("mockObject", new Class[0], new Object[]{new Exception()});

        //When
        invocationMatcher.captureArgumentsFrom(target);

        //Then
        //Assertion error is expected
    }
}
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsComparator;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.mock.MockMethod;
import org.mockito.mockitoSession;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockMethod;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class InvocationMatcherTest {

    private InvocationMatcher matcher;
    private Invocation invocation;
    private Invocation candidateInvocation;
    private Invocation spyInvocation;
    private MockMethod mockMethod;
    private MockitoSession session;

    @Before
    public void setup() {
        session = new MockitoSession();
        mockMethod = mockMethod(int.class);
        invocation = new Invocation(session, mockMethod, Arrays.asList(1, 2), new Location(1, 1, "TestClass.java"));
        candidateInvocation = new Invocation(session, mockMethod, Arrays.asList(1, 3), new Location(1, 2, "TestClass.java"));
        spyInvocation = spy(invocation);

        matcher = new InvocationMatcher(invocation, Arrays.asList(is(1)));
    }

    @Test
    public void testGetMethod() {
        assertThat(matcher.getMethod(), is(notNullValue()));
    }

    @Test
    public void testGetInvocation() {
        assertThat(matcher.getInvocation(), is(invocation));
    }

    @Test
    public void testGetMatchers() {
        List<Matcher> matchers = matcher.getMatchers();
        assertThat(matchers.size(), is(1));
        assertThat(matchers.get(0), is(is(1)));
    }

    @Test
    public void testToString() {
        String printableInvocation = matcher.toString();
        String expectedPrintableInvocation = "Invocation on TestClass.TestMethod(int)";
        String printSettings = new PrintSettings().withDefaultValueFormat();
        String actualToString = invocation.toString(matchers, printSettings);
        String expectedToString = "Invocation on TestClass.TestMethod(int) with arguments: [1]";

        assertThat(printableInvocation, is(expectedPrintableInvocation));
        assertThat(actualToString, is(expectedToString));
    }

    @Test
    public void testMatches() {
        assertThat(matcher.matches(candidateInvocation), is(false));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        InvocationMatcher matcherWithArguments = new InvocationMatcher(invocation, Arrays.asList(anyInt(), eq(2)));
        Object[] candidateArguments = new Object[]{1, 3};

        assertThat(matcherWithArguments.safelyArgumentsMatch(candidateArguments), is(false));
    }

    @Test
    public void testHasSameMethod() {
        assertThat(matcher.hasSameMethod(candidateInvocation), is(false));
    }

    @Test
    public void testHasSimilarMethod() {
        InvocationMatcher candidateMatcher = new InvocationMatcher(candidateInvocation, Collections.emptyList());
        assertThat(matcher.hasSimilarMethod(candidateMatcher), is(false));

        matcher = new InvocationMatcher(invocation);
        candidateMatcher = new InvocationMatcher(candidateInvocation);
        assertThat(matcher.hasSimilarMethod(candidateMatcher), is(true));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        List<Object> capturedArguments = new ArrayList<>();

        matcher.captureArgumentsFrom(spyInvocation);

        for (Matcher matcher : matcher.getMatchers()) {
            if (matcher instanceof CapturesArguments) {
                capturedArguments.add(((CapturesArguments) matcher).capturedValue());
            }
        }

        Object arg = invocation.getArguments()[0];
        verify(spyInvocation).getArguments()[0] = arg;

        assertThat(capturedArguments.get(0), is(arg));
    }
}
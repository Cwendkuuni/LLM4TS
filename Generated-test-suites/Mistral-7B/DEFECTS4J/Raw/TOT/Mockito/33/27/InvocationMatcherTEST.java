import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.invocation.InvocationOnMockArguments;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class InvocationMatcherTest {

    private InvocationMatcher matcher;
    private Invocation targetInvocation;
    private Invocation mockInvocation;

    @Before
    public void setup() {
        Object mock = mock(Object.class);
        Method method = mock(Method.class);
        Object[] arguments = new Object[]{1, "str"};

        targetInvocation = new Invocation(mock, method, arguments);
        matcher = new InvocationMatcher(targetInvocation);

        mockInvocation = new InvocationOnMockArguments(mock, method, arguments);
    }

    @Test
    public void testMatchesSameInvocation() {
        assertTrue(matcher.matches(targetInvocation));
    }

    @Test
    public void testMatchesSameMethodAndArguments() {
        Invocation otherInvocation = new Invocation(mock, method, arguments);
        InvocationMatcher otherMatcher = new InvocationMatcher(otherInvocation);

        assertTrue(matcher.matches(otherMatcher));
    }

    @Test
    public void testMatchesSameMethodButDifferentArguments() {
        Object[] otherArguments = new Object[]{2, "str"};
        Invocation otherInvocation = new Invocation(mock, method, otherArguments);
        InvocationMatcher otherMatcher = new InvocationMatcher(otherInvocation);

        assertFalse(matcher.matches(otherMatcher));
    }

    @Test
    public void testMatchesSameMethodButVerifiedInvocation() {
        verify(mock).someMethod(1, "str");
        Invocation verifiedInvocation = new Invocation(mock, method, arguments);
        InvocationMatcher verifiedMatcher = new InvocationMatcher(verifiedInvocation);

        assertFalse(matcher.matches(verifiedMatcher));
    }

    @Test
    public void testMatchesDifferentMethods() {
        Method otherMethod = mock(Method.class);
        Object[] otherArguments = new Object[]{1, "str"};

        Invocation otherInvocation = new Invocation(mock, otherMethod, otherArguments);
        InvocationMatcher otherMatcher = new InvocationMatcher(otherInvocation);

        assertFalse(matcher.matches(otherMatcher));
    }

    @Test(expected = AssertionError.class)
    public void testArgumentsComparisonFails() {
        Object[] otherArguments = new Object[]{new Object(), new Object()};
        Invocation otherInvocation = new Invocation(mock, method, otherArguments);
        InvocationMatcher otherMatcher = new InvocationMatcher(otherInvocation);

        matcher.matches(otherMatcher);
    }

    @Test
    public void testCaptureArgumentsFromInvocation() {
        Invocation captureMock = mock(Invocation.class);
        matcher.captureArgumentsFrom(captureMock);

        List<Matcher> matchers = matcher.getMatchers();
        for (Matcher matcher : matchers) {
            if (matcher instanceof CapturesArguments) {
                CapturesArguments captureMatcher = (CapturesArguments) matcher;
                Object capturedArg = captureMatcher.getValue();

                assertTrue(capturedArg instanceof Object);
                assertTrue(capturedArg.getClass().isInstance(captureMock.getArguments()[0]));
            }
        }
    }

    @Test
    public void testCreateFromList() {
        List<Invocation> invocations = Arrays.asList(targetInvocation, targetInvocation);
        List<InvocationMatcher> matchers = InvocationMatcher.createFrom(invocations);

        assertTrue(matchers.size() == 2);
    }

    @Test
    public void testMatchesNullInvocation() {
        assertFalse(matcher.matches(nullInvocation));
    }

    @Test
    public void testMatchesNullMatchers() {
        Invocation nullInvocation = new Invocation(mock, method, arguments);

        InvocationMatcher nullMatcher = new InvocationMatcher(nullInvocation, Collections.emptyList());

        assertFalse(matcher.matches(nullMatcher));
    }
}
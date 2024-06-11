import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InternalVerifier;
import org.mockito.Mockito;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsComparator;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.internal.matchers.TypeMatchers.*;

public class InvocationMatcherTest {
    private InvocationMatcher invocationMatcher;
    private Invocation mockInvocation;
    private List<Matcher> matchers;

    @Test
    public void setUp() {
        mockInvocation = mock(Invocation.class);
        matchers = Arrays.asList(is("test"), isInstanceOf(Integer.class));
        invocationMatcher = new InvocationMatcher(mockInvocation, matchers);
    }

    @Test
    public void testInvocationMatcherWithInvocationAndMatchers() {
        Method method = invocationMatcher.getMethod();
        Invocation invocation = invocationMatcher.getInvocation();
        List<Matcher> matchersList = invocationMatcher.getMatchers();
        Location location = invocationMatcher.getLocation();

        String toString = invocationMatcher.toString();
        assertThat(toString, is(invocationMatcher.toString(new PrintSettings())));

        InvocationMatcher anotherInvocationMatcher = new InvocationMatcher(mockInvocation, Arrays.asList(is("test"), is(1)));
        assertTrue(invocationMatcher.matches(anotherInvocationMatcher));

        Invocation differentInvocation = mock(Invocation.class);
        InvocationMatcher differentMatcher = new InvocationMatcher(differentInvocation, matchers);
        assertFalse(invocationMatcher.matches(differentMatcher));

        Invocation differentInvocationWithSameMethod = new Invocation(mockInvocation.getMock(), "testMethod", method, null, null, null, location);
        InvocationMatcher differentMatcherWithSameMethod = new InvocationMatcher(differentInvocationWithSameMethod);
        assertFalse(invocationMatcher.hasSameMethod(differentMatcherWithSameMethod));

        Object[] arguments = new Object[]{1};
        assertTrue(new ArgumentsComparator().argumentsMatch(invocationMatcher, arguments));

        Object[] incorrectArguments = new Object[]{1, "test"};
        assertFalse(new ArgumentsComparator().argumentsMatch(invocationMatcher, incorrectArguments));

        InvocationMatcher anotherInvocationMatcherWithDifferentMethod = new InvocationMatcher(mockInvocation, Arrays.asList(is("test"), is(2)));
        assertFalse(invocationMatcher.matches(anotherInvocationMatcherWithDifferentMethod));

        Method anotherMethod = mock(Method.class);
        Invocation anotherInvocation = new Invocation(mockInvocation.getMock(), "anotherMethod", anotherMethod, null, null, null, location);
        InvocationMatcher anotherMatcher = new InvocationMatcher(anotherInvocation, matchers);
        assertFalse(invocationMatcher.hasSameMethod(anotherMatcher));

        Object[] incorrectArgumentsWithDifferentTypes = new Object[]{1, "test", new Object()};
        assertFalse(new ArgumentsComparator().argumentsMatch(invocationMatcher, incorrectArgumentsWithDifferentTypes));

        // Test exception handling
        InvocationMatcher nullInvocationMatcher = new InvocationMatcher(null, matchers);

        List<Matcher> nullMatchers = null;
        InvocationMatcher nullMatchersMatcher = new InvocationMatcher(mockInvocation, nullMatchers);
    }

    @Test
    public void testInvocationMatcherWithDifferentInvocations() {
        // Test matching with different Invocations
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation, matchers);
        Invocation differentInvocation = mock(Invocation.class);
        InvocationMatcher differentMatcher = new InvocationMatcher(differentInvocation, matchers);
        assertFalse(invocationMatcher.matches(differentMatcher));

        Invocation differentInvocationWithSameMethod = new Invocation(mockInvocation.getMock(), "testMethod", method, null, null, null, location);
        InvocationMatcher differentMatcherWithSameMethod = new InvocationMatcher(differentInvocationWithSameMethod);
        assertFalse(invocationMatcher.hasSameMethod(differentMatcherWithSameMethod));

        Object[] incorrectArguments = new Object[]{1, "test"};
        assertFalse(new ArgumentsComparator().argumentsMatch(invocationMatcher, incorrectArguments));

        // Test ArgumentsComparator with incorrect arguments
        Invocation incorrectInvocation = new Invocation(mockInvocation.getMock(), "testMethod", method, null, null, null, location);
        incorrectInvocation.setArguments(incorrectArguments);
        InvocationMatcher incorrectMatcher = new InvocationMatcher(incorrectInvocation);
        assertFalse(new ArgumentsComparator().argumentsMatch(invocationMatcher, incorrectMatcher.getArguments()));
    }

    @Test
    public void testInvocationMatcherWithNullInvocation() {
        List<Matcher> matchers = Arrays.asList(is("test"), isInstanceOf(Integer.class));
        new InvocationMatcher(null, matchers);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvocationMatcherWithNullMatchers() {
        Invocation mockInvocation = mock(Invocation.class);
        new InvocationMatcher(mockInvocation, null);
    }

    @Captor
    ArgumentCaptor<InvocationMatcher> invocationMatcherCaptor;

    @Test
    public void testCaptureArgumentsFrom() {
        Object[] testArguments = new Object[]{"test", 1};
        when(mockInvocation.getArguments()).thenReturn(testArguments);

        invocationMatcher.captureArgumentsFrom(mockInvocation);
        for (int i = 0; i < matchers.size(); i++) {
            Matcher matcher = matchers.get(i);
            if (matcher instanceof CapturesArguments) {
                CapturesArguments capturedArguments = (CapturesArguments) matcher;
                Object capturedArgument = capturedArguments.getValue();
                assertEquals(testArguments[i], capturedArgument);
            }
        }
    }

    @Test
    public void testToString() {
        String toString = invocationMatcher.toString();
        InvocationMatcher anotherMatcher = new InvocationMatcher(mockInvocation, matchers);
        String anotherToString = anotherMatcher.toString();

        assertTrue(toString.equals(anotherToString));
    }

    @Test
    public void testHasSameMethod() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation, matchers);
        InvocationMatcher anotherInvocationMatcher = new InvocationMatcher(mockInvocation, matchers);
        assertTrue(invocationMatcher.hasSameMethod(anotherInvocationMatcher));

        InvocationMatcher differentMatcher = new InvocationMatcher(mockInvocation, Collections.singletonList(is("test")));
        assertFalse(invocationMatcher.hasSameMethod(differentMatcher));
    }

    @Test
    public void testToStringWithPrintSettings() {
        PrintSettings printSettings = new PrintSettings();
        String toString = invocationMatcher.toString(printSettings);

        String toStringWithLocation = invocationMatcher.toString(new PrintSettings().withLocation(true));
        assertTrue(toString.equals(toStringWithLocation));
    }
}
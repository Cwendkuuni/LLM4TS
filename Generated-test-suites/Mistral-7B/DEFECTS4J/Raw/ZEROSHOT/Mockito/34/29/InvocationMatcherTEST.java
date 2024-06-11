import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsComparator;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InvocationMatcherTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    private InvocationMatcher invocationMatcher;
    private InvocationMatcher invocationMatcherEmpty;
    private Invocation invocation;
    private Invocation matchingInvocation;
    private Invocation differentInvocation;
    private Invocation overloadedInvocation;
    private Invocation unverifiedInvocation;
    private Object[] arguments;

    @Before
    public void setUp() {
        Mockito.initMocks(this);
        invocation = Mockito.spy(new Object() {
            @SuppressWarnings("unused")
            public void methodWithTwoArguments(int arg1, String arg2) {
            }
        });

        matchingInvocation = Mockito.spy(new Object() {
            @SuppressWarnings("unused")
            public void methodWithTwoArguments(int arg1, String arg2) {
            }
        });

        differentInvocation = Mockito.spy(new Object() {
            @SuppressWarnings("unused")
            public void methodWithTwoDifferentArguments(int arg1, String arg2) {
            }
        });

        overloadedInvocation = Mockito.spy(new Object() {
            @SuppressWarnings("unused")
            public void methodWithOneArgument(String arg) {
            }

            @SuppressWarnings("unused")
            public void methodWithTwoArguments(int arg1, String arg2) {
            }
        });

        unverifiedInvocation = Mockito.spy(new Object() {
            @SuppressWarnings("unused")
            public void methodWithTwoArguments(int arg1, String arg2) {
            }
        });
        Mockito.doNothing().when(unverifiedInvocation);

        arguments = new Object[]{1, "test"};

        invocationMatcher = new InvocationMatcher(invocation, Collections.emptyList());
        invocationMatcherEmpty = new InvocationMatcher(invocation);
    }

    @Test
    public void shouldCreateInvocationMatcherWithEmptyMatcherList() {
        assertTrue(invocationMatcher.matches(invocation));
    }

    @Test
    public void shouldCreateInvocationMatcherWithMatcherList() {
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq(1), ArgumentMatchers.eq("test"));
        InvocationMatcher invocationMatcherWithMatchers = new InvocationMatcher(invocation, matchers);
        assertTrue(invocationMatcherWithMatchers.matches(new InvocationOnMock(invocation, new Method(invocation.getClass().getDeclaredMethod("methodWithTwoArguments")), arguments)));
    }

    @Test
    public void shouldReturnSameInvocationObject() {
        assertTrue(invocationMatcher.getInvocation().equals(invocation));
    }

    @Test
    public void shouldReturnSameMethodObject() {
        Method method = invocation.getMethod();
        assertTrue(invocationMatcher.getMethod().equals(method));
    }

    @Test
    public void shouldReturnSameMatcherList() {
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq(1), ArgumentMatchers.eq("test"));
        InvocationMatcher invocationMatcherWithMatchers = new InvocationMatcher(invocation, matchers);
        List<Matcher> matchersFromInvocationMatcher = invocationMatcherWithMatchers.getMatchers();

        assertTrue(matchers.equals(matchersFromInvocationMatcher));
    }

    @Test
    public void shouldReturnToStringWithMatchers() {
        String printSettings = "PrintingTest";
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq(1), ArgumentMatchers.eq("test"));
        InvocationMatcher invocationMatcherWithMatchers = new InvocationMatcher(invocation, matchers);

        String toString = invocationMatcherWithMatchers.toString(new PrintSettings(printSettings));

        assertTrue(toString.contains(printSettings));
        assertTrue(toString.contains("methodWithTwoArguments"));
        assertTrue(toString.contains("args: ["));
        assertTrue(toString.contains("1"));
        assertTrue(toString.contains("test"));
        assertTrue(toString.contains("]"));
    }

    @Test
    public void shouldReturnToStringWithoutMatchers() {
        String printSettings = "PrintingTest";
        InvocationMatcher invocationMatcherEmpty = new InvocationMatcher(invocation);

        String toString = invocationMatcherEmpty.toString(new PrintSettings(printSettings));

        assertTrue(toString.contains(printSettings));
        assertTrue(toString.contains("methodWithTwoArguments"));
        assertTrue(toString.contains("args: []"));
    }

    @Test
    public void shouldReturnTrueForSameInvocation() {
        assertTrue(invocationMatcher.matches(invocation));
    }

    @Test
    public void shouldReturnFalseForDifferentInvocation() {
        assertFalse(invocationMatcher.matches(differentInvocation));
    }

    @Test
    public void shouldReturnFalseForDifferentMethod() {
        assertFalse(invocationMatcher.hasSameMethod(differentInvocation));
    }

    @Test
    public void shouldReturnTrueForUnverifiedInvocation() {
        assertTrue(invocationMatcher.matches(unverifiedInvocation));
    }

    @Test
    public void shouldReturnFalseForOverloadedMethodsWithSameArguments() {
        InvocationMatcher invocationMatcherForOverloadedMethod = new InvocationMatcher(overloadedInvocation);
        assertTrue(invocationMatcherForOverloadedMethod.hasSameMethod(overloadedInvocation));
        assertFalse(invocationMatcherForOverloadedMethod.hasSameMethod(invocation));
        assertFalse(invocationMatcherForOverloadedMethod.matches(invocation));
        assertTrue(invocationMatcherForOverloadedMethod.safelyArgumentsMatch(arguments));
        assertFalse(invocationMatcherForOverloadedMethod.matches(matchingInvocation));
    }

    @Test
    public void shouldCaptureArgumentsFromInvocation() {
        List<Matcher> matchers = Arrays.asList(new CapturesArguments<>("arg1", ArgumentMatchers.eq(1)), new CapturesArguments<>("arg2", ArgumentMatchers.eq("test")));
        InvocationMatcher invocationMatcherWithMatchers = new InvocationMatcher(invocation, matchers);

        invocationMatcherWithMatchers.captureArgumentsFrom(new InvocationOnMock(invocation, new Method(invocation.getClass().getDeclaredMethod("methodWithTwoArguments")), arguments));

        assertEquals(1, ((CapturesArguments) matchers.get(0)).getValue(0).intValue());
        assertEquals("test", ((CapturesArguments) matchers.get(1)).getValue(0));
    }
}
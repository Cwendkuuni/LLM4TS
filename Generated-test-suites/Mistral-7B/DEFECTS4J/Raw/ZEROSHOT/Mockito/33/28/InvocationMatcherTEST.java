package org.mockito.internal.invocation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockComponent;
import org.mockito.verification.VerificationMode;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class InvocationMatcherTest {

    private MockComponent mockComponent;
    private InvocationMatcher invocationMatcher;

    @Before
    public void setup() {
        mockComponent = mock(MockComponent.class);
        Invocation invocation = new InvocationOnMock(mockComponent, new MethodTestClass(), "testMethod", new Object[]{1, "abc"});
        invocationMatcher = new InvocationMatcher(invocation);
    }

    @Test
    public void testInvocationMatcherConstructionWithNoMatchers() {
        // when
        InvocationMatcher invocationMatcherWithoutMatchers = new InvocationMatcher(invocationMatcher.getInvocation());

        // then
        assertThat(invocationMatcherWithoutMatchers, is(notNullValue()));
    }

    @Test
    public void testToStringMethod() {
        // when
        String toStringResult = invocationMatcher.toString();

        // then
        assertThat(toStringResult, is(notNullValue()));
    }

    @Test
    public void testMatchesMethodWithSameInvocation() {
        // when
        boolean result = invocationMatcher.matches(invocationMatcher);

        // then
        assertThat(result, is(true));
    }

    @Test
    public void testMatchesMethodWithDifferentInvocationSameMethodSameArguments() {
        // given
        InvocationOnMock differentInvocation = new InvocationOnMock(mockComponent, new MethodTestClass(), "testMethod", new Object[]{1, "abc"});

        // when
        boolean result = invocationMatcher.matches(new InvocationMatcher(differentInvocation));

        // then
        assertThat(result, is(true));
    }

    @Test
    public void testMatchesMethodWithDifferentInvocationSameMethodDifferentArguments() {
        // given
        InvocationOnMock differentInvocation = new InvocationOnMock(mockComponent, new MethodTestClass(), "testMethod", new Object[]{2, "def"});

        // when
        boolean result = invocationMatcher.matches(new InvocationMatcher(differentInvocation));

        // then
        assertThat(result, is(false));
    }

    @Test
    public void testHasSameMethodMethod() {
        // given
        InvocationOnMock differentInvocationWithSameMethod = new InvocationOnMock(mockComponent, new MethodTestClass(), "testAnotherMethod", new Object[]{1, "abc"});

        // when
        boolean result = invocationMatcher.hasSameMethod(new InvocationMatcher(differentInvocationWithSameMethod));

        // then
        assertThat(result, is(false));
    }

    @Test
    public void testHasSimilarMethodMethod() {
        // given
        InvocationOnMock differentInvocationWithSameMethodAndSameArguments = new InvocationOnMock(mockComponent, new MethodTestClass(), "testMethod", new Object[]{1, "abc"});

        // when
        boolean result = invocationMatcher.hasSimilarMethod(new InvocationMatcher(differentInvocationWithSameMethodAndSameArguments));

        // then
        assertThat(result, is(true));
    }

    @Test
    public void testCaptureArgumentsFromMethod() {
        // given
        InvocationMatcher invocationMatcherWithCapturingMatcher = new InvocationMatcher(invocationMatcher.getInvocation(), Arrays.asList(ArgumentMatchers.argThat(arg -> arg.equals(1))));

        // when
        invocationMatcherWithCapturingMatcher.captureArgumentsFrom(invocationMatcher.getInvocation());

        // then
        Object capturedArgument = invocationMatcherWithCapturingMatcher.getMatchers().get(0).getCapturedValue();

        // then
        assertThat(capturedArgument, is(1));
    }

    @Test
    public void testCreateFromMethod() {
        // given
        List<Invocation> invocations = Arrays.asList(invocationMatcher.getInvocation(), new InvocationOnMock(mockComponent, new MethodTestClass(), "testMethod", new Object[]{2, "def"}));

        // when
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        // then
        assertThat(invocationMatchers.size(), is(2));
    }

    @Test
    public void testToStringWithPrintSettings() {
        // given
        PrintSettings printSettings = new PrintSettings();

        // when
        String toStringResult = invocationMatcher.toString(printSettings);

        // then
        assertThat(toStringResult, is(notNullValue()));
    }

    @Test
    public void testMatchesWithDifferentInvocationsDifferentMethods() {
        // given
        InvocationOnMock differentInvocation = new InvocationOnMock(mockComponent, new MethodTestClass(), "testAnotherMethod", new Object[]{1, "abc"});

        // when
        boolean result = invocationMatcher.matches(new InvocationMatcher(differentInvocation));

        // then
        assertThat(result, is(false));
    }

    @Test
    public void testHasSameMethodWithSameInvocationsButDifferentArguments() {
        // given
        InvocationOnMock differentInvocationWithSameMethodButDifferentArguments = new InvocationOnMock(mockComponent, new MethodTestClass(), "testMethod", new Object[]{2, "def"});

        // when
        boolean result = invocationMatcher.hasSameMethod(new InvocationMatcher(differentInvocationWithSameMethodButDifferentArguments));

        // then
        assertThat(result, is(false));
    }

    @Test
    public void testHasSimilarMethodWithDifferentInvocationsSameMethodDifferentArguments() {
        // given
        InvocationOnMock differentInvocationWithSameMethodDifferentArguments = new InvocationOnMock(mockComponent, new MethodTestClass(), "testMethod", new Object[]{2, "def"});

        // when
        boolean result = invocationMatcher.hasSimilarMethod(new InvocationMatcher(differentInvocationWithSameMethodDifferentArguments));

        // then
        assertThat(result, is(false));
    }

    @Test
    public void testMatchesWithUnverifiedInvocation() {
        // given
        InvocationOnMock unverifiedInvocation = new InvocationOnMock(mockComponent, new MethodTestClass(), "testMethod", new Object[]{1, "abc"}, VerificationMode.RETURNED_FIRST);

        // when
        boolean result = invocationMatcher.matches(new InvocationMatcher(unverifiedInvocation));

        // then
        assertThat(result, is(true));
    }

    @Test
    public void testMatchesWithVerifiedInvocation() {
        // given
        InvocationOnMock verifiedInvocation = new InvocationOnMock(mockComponent, new MethodTestClass(), "testMethod", new Object[]{1, "abc"});
        verify(mockComponent).testMethod(1, "abc");

        // when
        boolean result = invocationMatcher.matches(new InvocationMatcher(verifiedInvocation));

        // then
        assertThat(result, is(true));
    }

    @Test
    public void testMatchesWithVerifiedInvocationWithDifferentArguments() {
        // given
        InvocationOnMock verifiedInvocationWithDifferentArguments = new InvocationOnMock(mockComponent, new MethodTestClass(), "testMethod", new Object[]{2, "def"});
        verify(mockComponent).testMethod(2, "def");

        // when
        boolean result = invocationMatcher.matches(new InvocationMatcher(verifiedInvocationWithDifferentArguments));

        // then
        assertThat(result, is(false));
    }

    @Test
    public void testMatchesWithVerifiedInvocationWithSameMethodAndSameArguments() {
        // given
        InvocationOnMock verifiedInvocationWithSameMethodAndSameArguments = new InvocationOnMock(mockComponent, new MethodTestClass(), "testMethod", new Object[]{1, "abc"});
        verify(mockComponent).testMethod(1, "abc");

        // when
        boolean result = invocationMatcher.matches(new InvocationMatcher(verifiedInvocationWithSameMethodAndSameArguments));

        // then
        assertThat(result, is(true));
    }

    @Test
    public void testMatchesWithVerifiedInvocationWithDifferentMethod() {
        // given
        InvocationOnMock verifiedInvocationWithDifferentMethod = new InvocationOnMock(mockComponent, new MethodTestClass(), "testAnotherMethod", new Object[]{1, "abc"});
        verify(mockComponent).testAnotherMethod(1, "abc");

        // when
        boolean result = invocationMatcher.matches(new InvocationMatcher(verifiedInvocationWithDifferentMethod));

        // then
        assertThat(result, is(false));
    }

    @Test
    public void testToStringWithPrintSettingsWithCustomMessage() {
        // given
        PrintSettings printSettings = new PrintSettings() {
            @Override
            public String getCustomMessage() {
                return "Custom Message";
            }
        };

        // when
        String toStringResult = invocationMatcher.toString(printSettings);

        // then
        assertThat(toStringResult, is(notNullValue()));
        assertThat(toStringResult, containsString("Custom Message"));
    }

    @Test
    public void testToStringWithPrintSettingsWithCustomMatcherName() {
        // given
        Matcher customMatcher = ArgumentMatchers.argThat((arg) -> arg.equals("X"));
        InvocationMatcher invocationMatcherWithCustomMatcher = new InvocationMatcher(invocationMatcher.getInvocation(), Arrays.asList(customMatcher));
        PrintSettings printSettings = new PrintSettings() {
            @Override
            public String getMatcherNameFor(Matcher matcher) {
                return "Custom Name";
            }
        };

        // when
        String toStringResult = invocationMatcherWithCustomMatcher.toString(printSettings);

        // then
        assertThat(toStringResult, is(notNullValue()));
        assertThat(toStringResult, containsString("Custom Name"));
    }

    @Test
    public void testToStringWithPrintSettingsWithCustomMatcherNameAndCustomMessage() {
        // given
        Matcher customMatcher = ArgumentMatchers.argThat((arg) -> arg.equals("X"));
        InvocationMatcher invocationMatcherWithCustomMatcher = new InvocationMatcher(invocationMatcher.getInvocation(), Arrays.asList(customMatcher));
        PrintSettings printSettings = new PrintSettings() {
            @Override
            public String getMatcherNameFor(Matcher matcher) {
                return "Custom Name";
            }

            @Override
            public String getCustomMessage() {
                return "Custom Message";
            }
        };

        // when
        String toStringResult = invocationMatcherWithCustomMatcher.toString(printSettings);

        // then
        assertThat(toStringResult, is(notNullValue()));
        assertThat(toStringResult, containsString("Custom Name"));
        assertThat(toStringResult, containsString("Custom Message"));
    }

    @Test
    public void testToStringWithPrintSettingsWithCustomMatcherNameAndCustomMessageWithEmptyListOfMatchers() {
        // given
        PrintSettings printSettings = new PrintSettings() {
            @Override
            public String getMatcherNameFor(Matcher matcher) {
                return "Custom Name";
            }

            @Override
            public String getCustomMessage() {
                return "Custom Message";
            }
        };

        // when
        String toStringResult = new InvocationMatcher(invocationMatcher.getInvocation()).toString(printSettings);

        // then
        assertThat(toStringResult, is(notNullValue()));
        assertThat(toStringResult, containsString("Custom Name"));
        assertThat(toStringResult, containsString("Custom Message"));
    }

    @Test
    public void testToStringWithNullPrintSettings() {
        // when
        String toStringResult = invocationMatcher.toString((PrintSettings) null);

        // then
        assertThat(toStringResult, is(notNullValue()));
        assertThat(toStringResult, containsString("Invocation [this="));
    }

    private static class MethodTestClass {
        public void testMethod(int i, String s) {
        }

        public void testAnotherMethod(int i, String s) {
        }
    }
}
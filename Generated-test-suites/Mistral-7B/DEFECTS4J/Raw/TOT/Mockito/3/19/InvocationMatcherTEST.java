import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.invocation.InvocationOnMockImpl;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.Arguments;
import org.mockito.internal.matchers.ArgumentsProcessor;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    private Invoker mockInvoker;

    @Before
    public void setup() {
        mockInvoker = mock(Invoker.class);
    }

    @Test
    public void testSimpleMatcher() {
        // given
        List<Matcher> matchers = Arrays.asList(eq(1));
        InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMockImpl(mockInvoker, getClass().getMethod("simpleMatcher", int.class)), matchers);

        // when
        Invocation realInvocation = new InvocationOnMockImpl(mockInvoker, getClass().getMethod("simpleMatcher", int.class), 1);
        invocationMatcher.matches(realInvocation);

        // then
        verify(mockInvoker).simpleMatcher(1);
        assertTrue(invocationMatcher.matches(realInvocation));
    }

    @Test
    public void testEmptyMatcherList() {
        // given
        InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMockImpl(mockInvoker, getClass().getMethod("emptyMatcherList")), Collections.emptyList());

        // when
        Invocation realInvocation = new InvocationOnMockImpl(mockInvoker, getClass().getMethod("emptyMatcherList"));

        // then
        assertFalse(invocationMatcher.matches(realInvocation));
    }

    @Test
    public void testMultipleMatchers() {
        // given
        List<Matcher> matchers = Arrays.asList(eq(1), eq("test"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMockImpl(mockInvoker, getClass().getMethod("multipleMatchers", int.class, String.class)), matchers);

        // when
        Invocation realInvocation = new InvocationOnMockImpl(mockInvoker, getClass().getMethod("multipleMatchers", int.class, String.class), 1, "test");

        // then
        verify(mockInvoker).multipleMatchers(1, "test");
        assertTrue(invocationMatcher.matches(realInvocation));
    }

    @Test
    public void testVarargsMatcher() {
        // given
        List<Matcher> matchers = Arrays.asList(eq(1), ArgumentMatchers.contains("test"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMockImpl(mockInvoker, getClass().getMethod("varargsMatcher", int[].class)), matchers);

        // when
        int[] args = {1, 2, "test"};
        Invocation realInvocation = new InvocationOnMockImpl(mockInvoker, getClass().getMethod("varargsMatcher", int[].class), args);

        // then
        verify(mockInvoker).varargsMatcher(args);
        assertTrue(invocationMatcher.matches(realInvocation));
    }

    @Test
    public void testSameMethodDifferentMocks() {
        // given
        Invoker mockInvoker1 = mock(Invoker.class);
        Invoker mockInvoker2 = mock(Invoker.class);
        List<Matcher> matchers = Arrays.asList(eq(1));

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(new InvocationOnMockImpl(mockInvoker1, getClass().getMethod("sameMethod", int.class)), matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(new InvocationOnMockImpl(mockInvoker2, getClass().getMethod("sameMethod", int.class)), matchers);

        // when
        Invocation realInvocation = new InvocationOnMockImpl(mockInvoker1, getClass().getMethod("sameMethod", int.class), 1);

        // then
        assertFalse(invocationMatcher1.matches(realInvocation));
        assertFalse(invocationMatcher2.matches(realInvocation));
    }

    @Test
    public void testDifferentMethods() {
        // given
        List<Matcher> matchers = Arrays.asList(eq(1));

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(new InvocationOnMockImpl(mockInvoker, getClass().getMethod("differentMethods1", int.class)), matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(new InvocationOnMockImpl(mockInvoker, getClass().getMethod("differentMethods2", String.class)), matchers);

        // when
        // no verification needed, the test is just about checking that matches method returns false

        // then
        assertFalse(invocationMatcher1.matches(new InvocationOnMockImpl(mockInvoker, getClass().getMethod("differentMethods2", String.class))));
        assertFalse(invocationMatcher2.matches(new InvocationOnMockImpl(mockInvoker, getClass().getMethod("differentMethods1", int.class))));
    }

    @Test
    public void testUnverifiedInvocation() {
        // given
        Invoker mockInvoker = mock(Invoker.class);
        List<Matcher> matchers = Arrays.asList(eq(1));
        InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMockImpl(mockInvoker, getClass().getMethod("unverifiedInvocation", int.class)), matchers);

        Invocation unverifiedInvocation = new InvocationOnMock(mockInvoker, getClass().getMethod("unverifiedInvocation", int.class), 1);
        unverifiedInvocation.markVerified();

        // when

        // then
        assertFalse(invocationMatcher.matches(unverifiedInvocation));
    }

    @Test
    public void testCapturingArguments() {
        // given
        Invoker mockInvoker = mock(Invoker.class);
        List<Matcher> matchers = Arrays.asList(new CapturesArguments() {
            @Override
            public void captureFrom(Object argument) {
                assertEquals(1, argument);
            }
        });

        InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMockImpl(mockInvoker, getClass().getMethod("testCapturingArguments", int.class)), matchers);

        // when
        int arg = 1;
        Invocation invocation = new InvocationOnMockImpl(mockInvoker, getClass().getMethod("testCapturingArguments", int.class), arg);
        invocationMatcher.captureArgumentsFrom(invocation);

        // then
        verify(mockInvoker).testCapturingArguments(Arguments.capture(ArgumentsProcessor.forObject(1)));
        verify(mockInvoker).testCapturingArguments(eq(1));
    }

    @Test(expected = Throwable.class)
    public void testThrowingMethod() {
        // given
        Invoker mockInvoker = mock(Invoker.class);
        List<Matcher> matchers = Arrays.asList(eq(1));

        InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMockImpl(mockInvoker, getClass().getMethod("throwingMethod", int.class)), matchers);

        // when
        Invocation invocation = new InvocationOnMockImpl(mockInvoker, getClass().getMethod("throwingMethod", int.class), 1);

        // then
        throw new RuntimeException(); // this test case is supposed to fail with the exception thrown by the mocked method
    }

    interface Invoker {
        void simpleMatcher(int i);
        void emptyMatcherList();
        void multipleMatchers(int i, String s);
        void varargsMatcher(int[] args);
        void sameMethod(int i);
        void differentMethods1(int i);
        void differentMethods2(String s);
        void unverifiedInvocation(int i);
        void testCapturingArguments(int i);
        void throwingMethod(int i) throws Throwable;
    }
}
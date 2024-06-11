package org.mockito.internal;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.internal.creation.MockSettingsImpl.defaultAnswer;
import static org.mockito.internal.invocation.InvocationMatcher.isNull;
import static org.mockito.internal.invocation.InvocationMatcher.isSame;
import static org.mockito.internal.invocation.InvocationMatcher.isA;
import static org.mockito.internal.progress.ThreadSafeMockingProgress.VerificationMode.RETURNED_LIST;
import static org.mockito.internal.progress.ThreadSafeMockingProgress.VerificationMode.RETURNED_NULL;
import static org.mockito.internal.progress.ThreadSafeMockingProgress.VerificationMode.VERIFIED_WITH_ARGUMENTS;
import static org.mockito.internal.progress.ThreadSafeMockingProgress.VerificationMode.VERIFIED_WITH_NO_ARGUMENTS;
import static org.mockito.internal.verification.VerificationDataImpl.verificationWith;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.MockSettingsImpl;
import org.mockito.internal.creation.MockSettingsImpl.MockSettingsBuilder;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.stubbing.Stub;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.stubbing.VoidMethodStubbable;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.verification.VerificationModeFactory;

public class MockHandlerTest {

    private MockSettingsBuilder mockSettingsBuilder;
    private MockSettingsImpl mockSettings;
    private MockHandler<Void> mockHandler;
    private Invocation invocation;

    @Before
    public void setup() {
        mockSettingsBuilder = new MockSettingsImpl.MockSettingsBuilder();
        mockSettings = mockSettingsBuilder.build();
        mockHandler = new MockHandler<>(mockSettings);
        invocation = Invocation.mocked(MockitoInvocationHandler.class, new Object[0]);
    }

    @Test
    public void testHandleInvocationWhenStubbingVoidMethods() {
        VerificationMode verificationMode = VERIFIED_WITH_NO_ARGUMENTS;
        VerificationModeFactory verificationModeFactory = mockingProgress -> VERIFIED_WITH_NO_ARGUMENTS;
        mockingProgress = new ThreadSafeMockingProgress(verificationModeFactory);
        mockSettingsBuilder.answers(Arrays.asList(new Stub() {
            @Override
            public Answer answer(Invocation invocation) {
                return invocation -> null;
            }
        }));

        InvocationMatcher invocationMatcher = InvocationMatcher.isMethodCalledOn(MockitoInvocationHandler.class).withAnyArguments();
        mockHandler.handle(invocation);

        // Assert that invocation was stubbed and verification was skipped
        assertNull(mockingProgress.pullVerificationMode());
        assertNull(invocationContainer.getInvocations().isEmpty());
        StubbedInvocationMatcher stubbedInvocationMatcher = invocationContainer.findAnswerFor(invocationMatcher);
        assertNotNull(stubbedInvocationMatcher);
    }

    @Test
    public void testHandleInvocationWhenVerifyingNoArguments() {
        VerificationMode verificationMode = VERIFIED_WITH_NO_ARGUMENTS;
        mockingProgress = new ThreadSafeMockingProgress(verificationModeFactory);

        InvocationMatcher invocationMatcher = InvocationMatcher.isMethodCalledOn(MockitoInvocationHandler.class);
        verificationMode = VERIFIED_WITH_NO_ARGUMENTS;
        VerificationDataImpl verificationData = verificationWith(invocationMatcher, verificationMode);

        mockHandler.handle(invocation);

        // Assert that verification was executed
        assertNull(mockingProgress.pullVerificationMode());
        assertNull(invocationContainer.getInvocations().isEmpty());
        assertEquals(verificationData, mockingProgress.getVerificationData());
    }

    @Test
    public void testHandleInvocationWhenVerifyingArguments() {
        VerificationMode verificationMode = RETURNED_LIST;
        mockingProgress = new ThreadSafeMockingProgress(verificationModeFactory);

        List<String> expectedArguments = Arrays.asList("arg1", "arg2");
        InvocationMatcher invocationMatcher = InvocationMatcher.isMethodCalledOn(MockitoInvocationHandler.class).withArgument("arg0", isA(String[].class))
                .withArgumentAt(0, isA(String.class)).withArgumentAt(1, isA(String.class));

        VerificationDataImpl verificationData = verificationWith(invocationMatcher, verificationMode, expectedArguments.toArray());

        mockSettingsBuilder.answers(Collections.singletonList(new Stub() {
            @Override
            public Answer answer(Invocation invocation) {
                return invocation -> expectedArguments;
            }
        }));
        mockHandler.handle(invocation);

        // Assert that verification was executed with the correct arguments
        assertNull(mockingProgress.pullVerificationMode());
        assertNull(invocationContainer.getInvocations().isEmpty());
        assertEquals(verificationData, mockingProgress.getVerificationData());
    }

    @Test
    public void testHandleInvocationWhenReturningDefaultAnswer() {
        mockingProgress = new ThreadSafeMockingProgress(verificationModeFactory);
        VerificationMode verificationMode = RETURNED_NULL;
        VerificationDataImpl verificationData = verificationWith(isNull(), verificationMode);

        mockHandler.handle(invocation);

        // Assert that verification was skipped and default answer was returned
        assertNull(mockingProgress.pullVerificationMode());
        assertNull(invocationContainer.getInvocations().isEmpty());
        assertEquals(verificationData, mockingProgress.getVerificationData());
        assertNull(mockHandler.handle(invocation));
    }

    @Test
    public void testHandleInvocationWhenAnswerIsNull() {
        mockingProgress = new ThreadSafeMockingProgress(verificationModeFactory);
        VerificationMode verificationMode = RETURNED_NULL;
        VerificationDataImpl verificationData = verificationWith(isNull(), verificationMode);

        mockSettingsBuilder.answers(Collections.singletonList(new Stub() {
            @Override
            public Answer answer(Invocation invocation) {
                return null;
            }
        }));
        mockHandler.handle(invocation);

        // Assert that verification was skipped and default answer was returned
        assertNull(mockingProgress.pullVerificationMode());
        assertNull(invocationContainer.getInvocations().isEmpty());
        assertEquals(verificationData, mockingProgress.getVerificationData());
        assertNull(mockHandler.handle(invocation));
    }

    @Test
    public void testHandleInvocationWhenAnswerIsNotNull() {
        mockingProgress = new ThreadSafeMockingProgress(verificationModeFactory);
        VerificationMode verificationMode = RETURNED_LIST;
        VerificationDataImpl verificationData = verificationWith(isNull(), verificationMode);

        mockSettingsBuilder.answers(Collections.singletonList(new Stub() {
            @Override
            public Answer answer(Invocation invocation) {
                return invocation -> Arrays.asList("result1", "result2");
            }
        }));
        mockHandler.handle(invocation);

        // Assert that verification was skipped and answer was returned
        assertNull(mockingProgress.pullVerificationMode());
        assertNull(invocationContainer.getInvocations().isEmpty());
        assertEquals(verificationData, mockingProgress.getVerificationData());
        Object result = mockHandler.handle(invocation);
        assertEquals(Arrays.asList("result1", "result2"), result);
    }

    @Test
    public void testHandleInvocationWhenAnswerThrowsException() {
        mockingProgress = new ThreadSafeMockingProgress(verificationModeFactory);
        VerificationMode verificationMode = VERIFIED_WITH_NO_ARGUMENTS;

        InvocationMatcher invocationMatcher = InvocationMatcher.isMethodCalledOn(MockitoInvocationHandler.class);

        mockSettingsBuilder.answers(Collections.singletonList(new Stub() {
            @Override
            public Answer answer(Invocation invocation) {
                throw new RuntimeException("Answer exception");
            }
        }));
        mockHandler.handle(invocation);

        // Assert that verification was skipped and exception was thrown
        assertNull(mockingProgress.pullVerificationMode());
        assertNull(invocationContainer.getInvocations().isEmpty());
        assertNull(invocationContainer.getInvocations().get(0));
        verifyThrowable(new RuntimeException("Answer exception"));
    }

    @Test(expected = IllegalStateException.class)
    public void testHandleInvocationWhenVerificationModeIsNotNullAndNoAnswerFound() {
        mockingProgress = new ThreadSafeMockingProgress(verificationModeFactory);
        VerificationMode verificationMode = RETURNED_NULL;

        InvocationMatcher invocationMatcher = InvocationMatcher.isMethodCalledOn(MockitoInvocationHandler.class);

        mockHandler.handle(invocation);

        // Assert that verification was skipped, and an exception is thrown
        assertNull(mockingProgress.pullVerificationMode());
        assertNull(invocationContainer.getInvocations().isEmpty());
        assertNull(invocationContainer.getInvocations().get(0));
        verificationMode = VERIFIED_WITH_ARGUMENTS;
        VerificationDataImpl verificationData = verificationWith(invocationMatcher, verificationMode);
        throw new IllegalStateException("Verification mode is not null, but no answer found.");
    }

    @Test
    public void testHandleInvocationWhenStubbingMethodWithMultipleAnswers() {
        mockingProgress = new ThreadSafeMockingProgress(verificationModeFactory);
        VerificationMode verificationMode = RETURNED_LIST;

        List<String> expectedAnswer1 = Arrays.asList("answer1");
        List<String> expectedAnswer2 = Arrays.asList("answer2");

        mockSettingsBuilder.answers(Arrays.asList(new Stub() {
            @Override
            public Answer answer(Invocation invocation) {
                return invocation -> expectedAnswer1;
            }
        }, new Stub() {
            @Override
            public Answer answer(Invocation invocation) {
                return invocation -> expectedAnswer2;
            }
        }));

        InvocationMatcher invocationMatcher = InvocationMatcher.isMethodCalledOn(MockitoInvocationHandler.class).withArgument("arg0", isSame(MockitoInvocationHandler.class));

        mockHandler.handle(invocation);

        // Assert that the first answer was returned
        assertNull(mockingProgress.pullVerificationMode());
        assertNull(invocationContainer.getInvocations().isEmpty());
        StubbedInvocationMatcher stubbedInvocationMatcher = invocationContainer.findAnswerFor(invocationMatcher);
        assertEquals(expectedAnswer1, stubbedInvocationMatcher.arguments());

        // Assert that the second answer was returned when calling handle again
        Object result = mockHandler.handle(invocation);
        assertEquals(expectedAnswer2, result);
    }

    @Test
    public void testHandleInvocationWithVoidMethodStubbable() {
        mockingProgress = new ThreadSafeMockingProgress(verificationModeFactory);
        VerificationMode verificationMode = VERIFIED_WITH_NO_ARGUMENTS;

        InvocationMatcher invocationMatcher = InvocationMatcher.isMethodCalledOn(MockitoInvocationHandler.class).withArgument("arg0", isNull());

        mockHandler.handle(invocation);

        mockSettingsBuilder.answers(Collections.singletonList(new Stub() {
            @Override
            public Answer answer(Invocation invocation) {
                return VoidMethodStubbable.VOID_ANSWER;
            }
        }));

        mockHandler.voidMethodStubbable(MockitoInvocationHandler.class).stubVoid();

        mockHandler.handle(invocation);

        // Assert that the void method was stubbed
        assertNull(mockingProgress.pullVerificationMode());
        assertNull(invocationContainer.getInvocations().isEmpty());
        verifyNoMoreInteractions(MockitoInvocationHandler.class);
    }

    private void verifyNoMoreInteractions(Class<?> clazz) {
        List<Invocation> invocations = invocationContainer.getInvocations();
        if (invocations.isEmpty()) {
            return;
        }

        Invocation lastInvocation = invocations.get(invocations.size() - 1);
        assertSame(clazz, lastInvocation.target().getClass());
        assertEquals(lastInvocation.method().getName(), "voidMethod()");
        assertEquals(lastInvocation.arguments().length, 0);
    }

    private void verifyThrowable(Throwable throwable) {
        List<Invocation> invocations = invocationContainer.getInvocations();
        if (invocations.isEmpty()) {
            throw new AssertionError("No exception was thrown.");
        }

        Invocation lastInvocation = invocations.get(invocations.size() - 1);
        assertEquals(lastInvocation.target().getClass(), Throwable.class);
        assertSame(lastInvocation.arguments()[0], throwable);
    }
}
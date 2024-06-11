package org.mockito.internal.handler;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.Answer;
import org.mockito.Invocation;
import org.mockito.invocation.InvocationMatcher;
import org.mockito.stubbing.AnswerStub;
import org.mockito.stubbing.AnswerStubber;
import org.mockito.verification.VerificationMode;

import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.InvocationMatcherImpl;
import org.mockito.internal.invocation.InvocationStubber;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.verification.MockAwareVerificationMode;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.stubbing.AnswerImpl;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.stubbing.VoidMethodStubbableImpl;
import org.mockito.verification.VerificationModeFactory;

public class MockHandlerTest {

    @Test
    public void testHandle_NoAnswersOrVerification() {
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);
        Object mock = mock(Object.class, mockHandler);

        Invocation invocation = mock(Invocation.class);
        Object result = mockHandler.handle(invocation);

        assertNull(result);
        verifyNoMoreInteractions(mockSettings, mockingProgress, invocationContainerImpl);
    }

    @Test
    public void testHandle_StubbingWithAnswer() {
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);

        Object mock = mock(Object.class, mockHandler);

        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = ArgumentMatchers.argThat(new ArgumentMatcher<Object>() {
            @Override
            public boolean matches(Object argument) {
                return argument.equals("test");
            }
        });

        Answer answer = new AnswerStub() {
            @Override
            public Object answer(Invocation invocation) throws Throwable {
                return "stubbed";
            }
        };

        mockHandler.setAnswersForStubbing(Collections.singletonList(answer));

        when(mockHandler.handle(invocation)).thenReturn("stubbed");
        assertEquals("stubbed", mockHandler.handle(invocation));

        ArgumentMatcher stubbedInvocationMatcher = mockHandler.matchersBinder.getStubbedInvocationMatcher();
        assertSame(invocationMatcher, stubbedInvocationMatcher);

        verify(mockSettings).getDefaultAnswer(invocation);
        verifyZeroInteractions(mockingProgress, invocationContainerImpl);
    }

    @Test
    public void testHandle_StubbingWithVoidAnswer() {
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);

        Object mock = mock(Object.class, mockHandler);

        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = ArgumentMatchers.any();

        mockHandler.setAnswersForStubbing(Collections.singletonList(mock(Answer.class)));
        mockHandler.voidMethodStubbable(mock).stubVoid();

        verify(mockHandler).handle(invocation);
        verifyNoMoreInteractions(mockSettings, mockingProgress, invocationContainerImpl);
    }

    @Test
    public void testHandle_Verification() {
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);

        Object mock = mock(Object.class, mockHandler);

        VerificationMode verificationMode = VerificationModeFactory.verifyNoMoreInteractions(mock);
        mockingProgress.setVerificationMode(verificationMode);

        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = ArgumentMatchers.any();

        when(mockHandler.handle(invocation)).thenReturn(null);

        mockHandler.handle(invocation);
        verify(mock).equals(mock);
    }

    @Test
    public void testHandle_VerificationAware() {
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);

        Object mock = mock(Object.class, mockHandler);

        VerificationMode verificationMode = new MockAwareVerificationMode(VerificationMode.verifyNoMoreInteractions(mock), mock);
        mockingProgress.setVerificationMode(verificationMode);

        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = ArgumentMatchers.any();

        when(mockHandler.handle(invocation)).thenReturn(null);

        mockHandler.handle(invocation);
        verify(mock).equals(mock);
    }
}
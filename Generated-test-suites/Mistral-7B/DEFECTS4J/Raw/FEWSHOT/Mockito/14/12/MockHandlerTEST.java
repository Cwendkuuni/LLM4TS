package org.mockito.internal.test;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.stubbing.VoidMethodStubbable;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.VoidMethodStubbableImpl;
import org.mockito.verification.VerificationMode;

import org.mockito.internal.handler.MockHandler;

public class MockHandlerTest {
    private MockHandler<Object> mockHandler;
    private InvocationMatcher invocationMatcher;
    private Invocation invocation;
    private InvocationContainer invocationContainer;

    @Before
    public void setUp() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockHandler = new MockHandler<>(mockSettings);
        invocationMatcher = mock(InvocationMatcher.class);
        invocation = mock(Invocation.class);
        invocationContainer = mock(InvocationContainer.class);
        when(invocation.getTarget()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(mock(java.lang.reflect.Method.class));
        when(invocation.getArguments()).thenReturn(new Object[0]);
    }

    @Test
    public void testHandle_invocationContainerHasAnswersForStubbing() {
        MatchersBinder matchersBinder = mockHandler.matchersBinder;
        MockingProgress mockingProgress = mockHandler.mockingProgress;
        InvocationContainerImpl invocationContainerImpl = mockHandler.invocationContainerImpl;
        OngoingStubbingImpl<Object> ongoingStubbing = mock(OngoingStubbingImpl.class);
        when(mockingProgress.pullVerificationMode()).thenReturn(null);
        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(new ArrayList<>());
        when(matchersBinder.bindMatchers(mockingProgress.getArgumentMatcherStorage(), invocation)).thenReturn(invocationMatcher);
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(null);
        when(invocationContainerImpl.getInvocations()).thenReturn(new ArrayList<>());
        when(invocationContainerImpl.setAnswersForStubbing(anyList())).thenReturn(null);
        when(invocationContainerImpl.setInvocationForPotentialStubbing(any(InvocationMatcher.class))).thenReturn(null);
        when(invocationContainerImpl.resetInvocationForPotentialStubbing(any(InvocationMatcher.class))).thenReturn(null);
        when(invocationContainer.setMethodForStubbing(any(InvocationMatcher.class))).thenReturn(null);

        mockHandler.handle(invocation);

        verify(mockingProgress).reportOngoingStubbing(ongoingStubbing);
        verify(invocationContainerImpl).setInvocationForPotentialStubbing(invocationMatcher);
        verify(invocationContainerImpl).setAnswersForStubbing(anyList());
    }

    @Test
    public void testHandle_verificationModeIsNotNull() {
        VerificationMode verificationMode = mock(VerificationMode.class);
        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);

        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);

        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(new ArrayList<>());
        when(matchersBinder.bindMatchers(mockingProgress.getArgumentMatcherStorage(), invocation)).thenReturn(invocationMatcher);

        VerificationDataImpl data = new VerificationDataImpl(new ArrayList<>(), invocationMatcher);
        when(verificationMode.verify(data)).thenReturn(null);

        mockHandler.handle(invocation);

        verifyNoMoreInteractions(invocationMatcher);
        verifyNoMoreInteractions(invocationContainer);
    }

    @Test
    public void testHandle_verificationModeIsNull() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);

        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(new ArrayList<>());
        when(matchersBinder.bindMatchers(mockingProgress.getArgumentMatcherStorage(), invocation)).thenReturn(invocationMatcher);

        mockHandler.handle(invocation);

        verifyNoMoreInteractions(invocationMatcher);
        verifyNoMoreInteractions(invocationContainer);
    }

    @Test
    public void testHandle_noAnswerFound() {
        Answer answer = mock(Answer.class);
        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(stubbedInvocation);
        when(stubbedInvocation.answer(invocation)).thenReturn(answer);

        mockHandler.handle(invocation);

        verify(stubbedInvocation).captureArgumentsFrom(invocation);
        verify(answer).answer(invocation);
        verifyNoMoreInteractions(invocationMatcher);
        verifyNoMoreInteractions(invocationContainer);
    }

    @Test
    public void testHandle_defaultAnswer() {
        Answer answer = mock(Answer.class);
        when(mockSettings.getDefaultAnswer()).thenReturn(answer);

        mockHandler.handle(invocation);

        verifyNoMoreInteractions(invocationMatcher);
        verifyNoMoreInteractions(invocationContainer);
    }

    @Test
    public void testVoidMethodStubbable() {
        Object mock = mock(Object.class);
        VoidMethodStubbable<Object> voidMethodStubbable = mockHandler.voidMethodStubbable(mock);

        verify(voidMethodStubbable).mock(eq(mock));
        verify(voidMethodStubbable).answer(eq(null));
    }
}
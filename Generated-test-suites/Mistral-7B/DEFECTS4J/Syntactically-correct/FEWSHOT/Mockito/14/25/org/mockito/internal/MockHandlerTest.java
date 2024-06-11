package org.mockito.internal;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.invocation.MockingProgress;
import org.mockito.internal.invocation.StubbedInvocationMatcher;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.VoidMethodStubbable;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.VoidMethodStubbableImpl;
import org.mockito.verification.VerificationMode;

public class MockHandlerTest {

    @Test
    public void testHandleNullInvocation() {
        MockHandler<Object> mockHandler = new MockHandler<>(new MockSettingsImpl());
        assertNull(mockHandler.handle(null));
    }

    @Test
    public void testHandleNoAnswerOrVerification() {
        MockHandler<Object> mockHandler = new MockHandler<>(new MockSettingsImpl());
        Invocation invocation = mock(Invocation.class);
        Object result = mockHandler.handle(invocation);
        assertNull(result);
    }

    @Test
    public void testHandleVerification() {
        MockHandler<Object> mockHandler = new MockHandler<>(new MockSettingsImpl());
        MockingProgress mockingProgress = mock(MockingProgress.class);
        VerificationDataImpl verificationData = mock(VerificationDataImpl.class);
        VerificationMode verificationMode = mock(VerificationMode.class);
        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(verificationMode.verify(verificationData)).thenReturn(null);

        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        when(invocation.getMethod()).thenReturn(mock(java.lang.reflect.Method.class));

        InvocationContainer invocationContainer = mock(InvocationContainer.class);
        InvocationContainerImpl invocationContainerImpl = new InvocationContainerImpl(mockingProgress);
        when(mockHandler.getInvocationContainer()).thenReturn(invocationContainerImpl);

        mockHandler = new MockHandler<>(mockHandler);

        when(invocation.getTarget()).thenReturn(mock(Object.class));
        when(invocation.getArguments()).thenReturn(new Object[0]);

        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        when(mockSettings.getDefaultAnswer()).thenReturn(mock(Answer.class));

        List<Answer> answers = new ArrayList<>();
        when(mockSettings.getDefaultAnswer()).thenReturn(answers.get(0));

        when(matchersBinder.bindMatchers(mockingProgress.getArgumentMatcherStorage(), invocation)).thenReturn(invocationMatcher);

        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(null);

        Object result = mockHandler.handle(invocation);
        assertNull(result);
    }

    @Test
    public void testHandleAnswer() {
        MockHandler<Object> mockHandler = new MockHandler<>(new MockSettingsImpl());
        MockingProgress mockingProgress = mock(MockingProgress.class);
        Invocation invocation = mock(Invocation.class);
        Object answer = "answer";

        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        when(invocation.getMethod()).thenReturn(mock(java.lang.reflect.Method.class));
        when(matchersBinder.bindMatchers(mockingProgress.getArgumentMatcherStorage(), invocation)).thenReturn(invocationMatcher);

        InvocationContainer invocationContainer = mock(InvocationContainer.class);
        InvocationContainerImpl invocationContainerImpl = new InvocationContainerImpl(mockingProgress);
        when(mockHandler.getInvocationContainer()).thenReturn(invocationContainerImpl);

        when(invocationContainerImpl.setInvocationForPotentialStubbing(invocationMatcher)).thenReturn(null);
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(new StubbedInvocationMatcher(invocationMatcher, answer));

        Object result = mockHandler.handle(invocation);
        assertSame(answer, result);

        verify(invocationContainerImpl).setInvocationForPotentialStubbing(invocationMatcher);
    }

    @Test
    public void testHandleAnswerWithDefaultAnswer() {
        MockHandler<Object> mockHandler = new MockHandler<>(new MockSettingsImpl());
        MockingProgress mockingProgress = mock(MockingProgress.class);
        Invocation invocation = mock(Invocation.class);
        Object answer = "answer";

        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        when(invocation.getMethod()).thenReturn(mock(java.lang.reflect.Method.class));
        when(matchersBinder.bindMatchers(mockingProgress.getArgumentMatcherStorage(), invocation)).thenReturn(invocationMatcher);

        InvocationContainer invocationContainer = mock(InvocationContainer.class);
        InvocationContainerImpl invocationContainerImpl = new InvocationContainerImpl(mockingProgress);
        when(mockHandler.getInvocationContainer()).thenReturn(invocationContainerImpl);

        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(null);

        Object defaultAnswer = "default answer";
        Answer answerObject = mock(Answer.class);
        when(answerObject.answer(invocation)).thenReturn(defaultAnswer);
        when(mockSettings.getDefaultAnswer()).thenReturn(answerObject);

        when(invocationContainerImpl.resetInvocationForPotentialStubbing(invocationMatcher)).thenReturn(null);

        Object result = mockHandler.handle(invocation);
        assertSame(defaultAnswer, result);

        verify(invocationContainerImpl).resetInvocationForPotentialStubbing(invocationMatcher);
    }

    @Test
    public void testHandleAnswerWithOngoingStubbing() {
        MockHandler<Object> mockHandler = new MockHandler<>(new MockSettingsImpl());
        MockingProgress mockingProgress = mock(MockingProgress.class);
        Invocation invocation = mock(Invocation.class);
        Object answer = "answer";

        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        when(invocation.getMethod()).thenReturn(mock(java.lang.reflect.Method.class));
        when(matchersBinder.bindMatchers(mockingProgress.getArgumentMatcherStorage(), invocation)).thenReturn(invocationMatcher);

        InvocationContainer invocationContainer = mock(InvocationContainer.class);
        InvocationContainerImpl invocationContainerImpl = new InvocationContainerImpl(mockingProgress);
        when(mockHandler.getInvocationContainer()).thenReturn(invocationContainerImpl);

        OngoingStubbingImpl<Object> ongoingStubbing = mock(OngoingStubbingImpl.class);
        when(invocationContainerImpl.setInvocationForPotentialStubbing(invocationMatcher)).thenReturn(ongoingStubbing);
        when(ongoingStubbing.getMethod()).thenReturn(invocation.getMethod());

        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(null);

        Object result = mockHandler.handle(invocation);
        assertNull(result);

        verify(ongoingStubbing).reportStubbing(invocationMatcher, answer);
    }

    @Test
    public void testHandleAnswerWithVoidMethodStubbable() {
        MockHandler<Object> mockHandler = new MockHandler<>(new MockSettingsImpl());
        MockingProgress mockingProgress = mock(MockingProgress.class);
        Invocation invocation = mock(Invocation.class);

        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        when(invocation.getMethod()).thenReturn(mock(java.lang.reflect.Method.class));
        when(matchersBinder.bindMatchers(mockingProgress.getArgumentMatcherStorage(), invocation)).thenReturn(invocationMatcher);

        InvocationContainer invocationContainer = mock(InvocationContainer.class);
        InvocationContainerImpl invocationContainerImpl = new InvocationContainerImpl(mockingProgress);
        when(mockHandler.getInvocationContainer()).thenReturn(invocationContainerImpl);

        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(null);

        VoidMethodStubbable<Object> voidMethodStubbable = mockHandler.voidMethodStubbable(mock(Object.class));
        when(voidMethodStubbable.answers(invocationMatcher)).thenReturn(mock(VoidMethodStubbableImpl.class));

        Object result = mockHandler.handle(invocation);
        assertNull(result);

        verify(voidMethodStubbable).answers(invocationMatcher);
    }
}
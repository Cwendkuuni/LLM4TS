
/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

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
import org.mockito.internal.stubbing.VoidMethodStubbableImpl;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.stubbing.Answer;
import org.mockito.verification.VerificationMode;

public class MockHandlerTest {

    MockHandler<MockHandlerInterface> mockHandler;
    MockingProgress mockingProgress;
    MatchersBinder matchersBinder;
    InvocationContainerImpl invocationContainerImpl;

    @Before
    public void setup() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockingProgress = new ThreadSafeMockingProgress();
        matchersBinder = new MatchersBinder();
        invocationContainerImpl = new InvocationContainerImpl(mockingProgress);
        mockHandler = new MockHandler<MockHandlerInterface>(mockSettings);
        mockHandler.mockingProgress = mockingProgress;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.invocationContainerImpl = invocationContainerImpl;
    }

    @Test
    public void testHandle_WithStubForVoidMethod() throws Throwable {
        Invocation voidInvocation = new Invocation(mock(MockHandlerInterface.class), VoidMethodStubbable.class.getMethod("thenCallRealMethod"), null);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(null);
        when(matchersBinder.bindMatchers(mockingProgress.getArgumentMatcherStorage(), voidInvocation)).thenReturn(invocationMatcher);
        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(true);

        Object result = mockHandler.handle(voidInvocation);

        assertEquals(null, result);
        verify(invocationContainerImpl).setMethodForStubbing(invocationMatcher);
        verify(mockingProgress).validateState();
    }

    @Test
    public void testHandle_WithVerificationMode() throws Throwable {
        Invocation invocation = new Invocation(mock(MockHandlerInterface.class), VoidMethodStubbable.class.getMethod("thenCallRealMethod"), null);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(null);
        when(matchersBinder.bindMatchers(mockingProgress.getArgumentMatcherStorage(), invocation)).thenReturn(invocationMatcher);
        when(mockingProgress.pullVerificationMode()).thenReturn(mock(VerificationMode.class));

        Object result = mockHandler.handle(invocation);

        assertEquals(null, result);
        verify(mockingProgress).pullVerificationMode();
        verify(invocationContainerImpl).getInvocations();
        verify(mockingProgress).validateState();
    }

    @Test
    public void testHandle_WithStubbedInvocation() throws Throwable {
        Invocation invocation = new Invocation(mock(MockHandlerInterface.class), VoidMethodStubbable.class.getMethod("thenCallRealMethod"), null);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(null);
        when(matchersBinder.bindMatchers(mockingProgress.getArgumentMatcherStorage(), invocation)).thenReturn(invocationMatcher);
        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(stubbedInvocation);

        Object result = mockHandler.handle(invocation);

        assertEquals(null, result);
        verify(stubbedInvocation).captureArgumentsFrom(invocation);
        verify(stubbedInvocation).answer(invocation);
    }

    @Test
    public void testHandle_WithoutStubbedInvocation() throws Throwable {
        Invocation invocation = new Invocation(mock(MockHandlerInterface.class), VoidMethodStubbable.class.getMethod("thenCallRealMethod"), null);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(null);
        when(matchersBinder.bindMatchers(mockingProgress.getArgumentMatcherStorage(), invocation)).thenReturn(invocationMatcher);
        OngoingStubbingImpl<MockHandlerInterface> ongoingStubbing = mock(OngoingStubbingImpl.class);
        when(ongoingStubbing.answer(invocation)).thenReturn("Answer");
        when(ongoingStubbing.invocationContainer(invocationContainerImpl)).thenReturn(ongoingStubbing);
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(null);
        when(mockHandler.getMockSettings().getDefaultAnswer()).thenReturn(ongoingStubbing);

        Object result = mockHandler.handle(invocation);

        assertEquals("Answer", result);
        verify(invocationContainerImpl).setInvocationForPotentialStubbing(invocationMatcher);
        verify(mockingProgress).reportOngoingStubbing(ongoingStubbing);
        verify(invocationContainerImpl).resetInvocationForPotentialStubbing(invocationMatcher);
    }

    @Test
    public void testVoidMethodStubbable() {
        Invocation invocation = new Invocation(mock(MockHandlerInterface.class), VoidMethodStubbable.class.getMethod("thenCallRealMethod"), null);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        VoidMethodStubbableImpl<MockHandlerInterface> voidMethodStubbable = mock(VoidMethodStubbableImpl.class);
        when(invocationContainerImpl.getInvocationForPotentialStubbing().getInvocation()).thenReturn(invocation);
        when(invocationContainerImpl.getInvocationForPotentialStubbing().getMatchersBinder()).thenReturn(matchersBinder);
        when(matchersBinder.bindMatchers(matchersBinder.getMatcherStorage(), invocation)).thenReturn(invocationMatcher);
        when(voidMethodStubbable.invocationContainer(invocationContainerImpl)).thenReturn(voidMethodStubbable);

        VoidMethodStubbable<MockHandlerInterface> result = mockHandler.voidMethodStubbable(mock(MockHandlerInterface.class));

        assertEquals(voidMethodStubbable, result);
    }

    @Test
    public void testSetAnswersForStubbing() {
        List<Answer> answers = new ArrayList<Answer>();
        mockHandler.setAnswersForStubbing(answers);
        verify(invocationContainerImpl).setAnswersForStubbing(answers);
    }

    @Test
    public void testGetInvocationContainer() {
        InvocationContainer result = mockHandler.getInvocationContainer();
        assertEquals(invocationContainerImpl, result);
    }
}

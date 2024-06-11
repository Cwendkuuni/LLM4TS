
/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

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

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class MockHandlerTest {

    private MockHandler<Object> mockHandler;
    private MockSettingsImpl mockSettings;
    private InvocationContainerImpl invocationContainerImpl;
    private MatchersBinder matchersBinder;
    private MockingProgress mockingProgress;

    @Before
    public void setUp() {
        mockSettings = mock(MockSettingsImpl.class);
        invocationContainerImpl = mock(InvocationContainerImpl.class);
        matchersBinder = mock(MatchersBinder.class);
        mockingProgress = mock(ThreadSafeMockingProgress.class);
        mockHandler = new MockHandler<>(mockSettings, invocationContainerImpl, matchersBinder, mockingProgress);
    }

    @Test
    public void testHandle_WithAnswersForStubbing_InvokesStubbingAndReturnsNull() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);

        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(true);
        when(matchersBinder.bindMatchers(any(), eq(invocation))).thenReturn(invocationMatcher);

        assertNull(mockHandler.handle(invocation));

        verify(invocationContainerImpl).setMethodForStubbing(invocationMatcher);
        verifyZeroInteractions(mockingProgress);
    }

    @Test
    public void testHandle_WithVerificationMode_StubsInvocationAndReturnsNull() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        when(mockingProgress.getVerificationMode()).thenReturn(mock(VerificationMode.class));

        when(matchersBinder.bindMatchers(any(), eq(invocation))).thenReturn(invocationMatcher);
        VerificationDataImpl verificationData = mock(VerificationDataImpl.class);
        whenNew(VerificationDataImpl.class).withArguments(any(), eq(invocationMatcher)).thenReturn(verificationData);

        assertNull(mockHandler.handle(invocation));

        verify(invocationContainerImpl).setInvocationForPotentialStubbing(invocationMatcher);
        verify(mockingProgress).reportOngoingStubbing(any(OngoingStubbingImpl.class));
        verifyZeroInteractions(invocationContainerImpl);
    }

    @Test
    public void testHandle_WithoutStubbedInvocation_ReturnsDefaultAnswer() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);
        Answer defaultAnswer = mock(Answer.class);

        when(matchersBinder.bindMatchers(any(), eq(invocation))).thenReturn(invocationMatcher);
        when(mockSettings.getDefaultAnswer()).thenReturn(defaultAnswer);
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(null);

        assertNull(mockHandler.handle(invocation));

        verify(invocationContainerImpl).setInvocationForPotentialStubbing(invocationMatcher);
        verify(mockingProgress).reportOngoingStubbing(any(OngoingStubbingImpl.class));
        verify(invocationContainerImpl).resetInvocationForPotentialStubbing(invocationMatcher);
        verify(defaultAnswer).answer(invocation);
    }

    @Test
    public void testVoidMethodStubbable_ReturnsVoidMethodStubbableImpl() {
        Object mock = new Object();
        VoidMethodStubbable<T> expected = new VoidMethodStubbableImpl<>(mock, invocationContainerImpl);

        VoidMethodStubbable<T> actual = mockHandler.voidMethodStubbable(mock);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetMockSettings_ReturnsMockSettings() {
        MockSettingsImpl expected = mockSettings;

        MockSettingsImpl actual = mockHandler.getMockSettings();

        assertEquals(expected, actual);
    }

    @Test
    public void testSetAnswersForStubbing_DelegatesToInvocationContainerImpl() {
        List<Answer> answers = new ArrayList<>();

        mockHandler.setAnswersForStubbing(answers);

        verify(invocationContainerImpl).setAnswersForStubbing(answers);
    }

    @Test
    public void testGetInvocationContainer_ReturnsInvocationContainerImpl() {
        InvocationContainer expected = invocationContainerImpl;

        InvocationContainer actual = mockHandler.getInvocationContainer();

        assertEquals(expected, actual);
    }
}

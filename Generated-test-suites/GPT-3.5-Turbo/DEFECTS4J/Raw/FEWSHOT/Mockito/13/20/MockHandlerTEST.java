
package org.mockito.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

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
import org.mockito.internal.verification.MockAwareVerificationMode;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.stubbing.Answer;
import org.mockito.verification.VerificationMode;

public class MockHandlerTest {
    private MockHandlerInterface<MockHandler> mockHandler;
    private InvocationContainerImpl invocationContainerImpl;
    private MatchersBinder matchersBinder;
    private MockingProgress mockingProgress;
    private MockSettingsImpl mockSettings;

    @Before
    public void setUp() {
        this.mockSettings = mock(MockSettingsImpl.class);
        this.mockingProgress = mock(ThreadSafeMockingProgress.class);
        this.matchersBinder = mock(MatchersBinder.class);
        this.invocationContainerImpl = mock(InvocationContainerImpl.class);

        this.mockHandler = new MockHandler<MockHandler>(mockSettings);
        this.mockHandler.mockingProgress = mockingProgress;
        this.mockHandler.matchersBinder = matchersBinder;
        this.mockHandler.invocationContainerImpl = invocationContainerImpl;
    }

    @Test
    public void testHandleWithAnswer() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        VerificationDataImpl verificationDataImpl = mock(VerificationDataImpl.class);
        OngoingStubbingImpl<MockHandler> ongoingStubbing = mock(OngoingStubbingImpl.class);
        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);

        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(true);
        when(matchersBinder.bindMatchers(any(), eq(invocation))).thenReturn(invocationMatcher);
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(stubbedInvocation);
        when(mockingProgress.pullVerificationMode()).thenReturn(null);
        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(null);
        when(ongoingStubbing.answer(invocation)).thenReturn(new Object());
        when(mockSettings.getDefaultAnswer()).thenReturn(mock(Answer.class));

        assertNull(mockHandler.handle(invocation));

        verify(invocationContainerImpl).setMethodForStubbing(invocationMatcher);
        verify(stubbedInvocation).captureArgumentsFrom(invocation);
        verify(mockingProgress).validateState();
        verify(ongoingStubbing).answer(invocation);
        verify(invocationContainerImpl).resetInvocationForPotentialStubbing(invocationMatcher);
        verify(invocationContainerImpl, never()).setInvocationForPotentialStubbing(invocationMatcher);
        verify(mockingProgress, never()).reportOngoingStubbing(ongoingStubbing);
        verify(mockSettings, never()).getDefaultAnswer();
    }

    @Test
    public void testHandleWithVerificationMode() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        VerificationDataImpl verificationDataImpl = mock(VerificationDataImpl.class);
        OngoingStubbingImpl<MockHandler> ongoingStubbing = mock(OngoingStubbingImpl.class);
        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);
        MockAwareVerificationMode verificationMode = mock(MockAwareVerificationMode.class);

        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(false);
        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(matchersBinder.bindMatchers(any(), eq(invocation))).thenReturn(invocationMatcher);

        mockHandler.handle(invocation);

        verify(invocationContainerImpl, never()).setMethodForStubbing(invocationMatcher);
        verify(mockingProgress).validateState();
        verify(invocationContainerImpl, never()).resetInvocationForPotentialStubbing(invocationMatcher);
        verify(invocationContainerImpl).setInvocationForPotentialStubbing(invocationMatcher);
        verify(mockingProgress).reportOngoingStubbing(ongoingStubbing);
        verify(invocationContainerImpl).findAnswerFor(invocation);

        verify(verificationMode).getMock();
        verify(verificationMode).verify(verificationDataImpl);
    }

    @Test
    public void testHandleWithoutAnswer() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        VerificationDataImpl verificationDataImpl = mock(VerificationDataImpl.class);
        OngoingStubbingImpl<MockHandler> ongoingStubbing = mock(OngoingStubbingImpl.class);
        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);
        Answer answer = mock(Answer.class);

        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(false);
        when(mockingProgress.pullVerificationMode()).thenReturn(null);
        when(matchersBinder.bindMatchers(any(), eq(invocation))).thenReturn(invocationMatcher);
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(stubbedInvocation);
        when(stubbedInvocation.answer(invocation)).thenReturn(new Object());
        when(mockSettings.getDefaultAnswer()).thenReturn(answer);

        assertNull(mockHandler.handle(invocation));

        verify(invocationContainerImpl, never()).setMethodForStubbing(invocationMatcher);
        verify(stubbedInvocation).captureArgumentsFrom(invocation);
        verify(mockingProgress).validateState();
        verify(mockingProgress).reportOngoingStubbing(ongoingStubbing);
        verify(invocationContainerImpl, never()).resetInvocationForPotentialStubbing(invocationMatcher);
        verify(invocationContainerImpl, never()).setInvocationForPotentialStubbing(invocationMatcher);
        verify(mockSettings, never()).getDefaultAnswer();
    }

    @Test
    public void testVoidMethodStubbable() {
        T mock = mock(MockHandler.class);
        VoidMethodStubbableImpl<T> voidMethodStubbable = mock(VoidMethodStubbableImpl.class);

        when(mockHandler.voidMethodStubbable(mock)).thenReturn(voidMethodStubbable);

        assertEquals(voidMethodStubbable, mockHandler.voidMethodStubbable(mock));
        verify(mockHandler).voidMethodStubbable(mock);
    }

    @Test
    public void testGetMockSettings() {
        assertEquals(mockSettings, mockHandler.getMockSettings());
    }

    @Test
    public void testSetAnswersForStubbing() {
        List<Answer> answers = mock(List.class);

        mockHandler.setAnswersForStubbing(answers);

        verify(invocationContainerImpl).setAnswersForStubbing(answers);
    }

    @Test
    public void testGetInvocationContainer() {
        assertEquals(invocationContainerImpl, mockHandler.getInvocationContainer());
    }
}

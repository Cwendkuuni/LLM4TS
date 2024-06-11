
package org.mockito.internal;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.internal.matchers.Matchers.*;
import org.mockito.invocation.Invocation;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.stubbing.VoidMethodStubbableImpl;
import org.mockito.internal.verification.MockAwareVerificationMode;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationMode;
import org.junit.Test;

public class MockHandlerTest {
    @Test
    public void testHandleInvocationContainerImpl() throws Throwable {
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MockingProgress mockingProgress = mock(ThreadSafeMockingProgress.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        VerificationMode verificationMode = mock(MockAwareVerificationMode.class);
        VerificationDataImpl verificationDataImpl = mock(VerificationDataImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        Answer<Object> defaultAnswer = mock(Answer.class);

        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(true);
        when(matchersBinder.bindMatchers(any(), eq(invocation))).thenReturn(invocationMatcher);
        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(any());
        when(verificationMode.verify(verificationDataImpl)).thenReturn(null);
        when(mockSettings.getDefaultAnswer()).thenReturn(defaultAnswer);
        when(mockSettings.getDefaultAnswer().answer(invocation)).thenReturn(null);
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(stubbedInvocationMatcher);
        when(stubbedInvocationMatcher.answer(invocation)).thenReturn(null);

        MockHandler<MockSettingsImpl> mockHandler = new MockHandler<>(mockSettings);
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;

        Object result = mockHandler.handle(invocation);
        
        assertNull(result);
        verify(invocationContainerImpl).setMethodForStubbing(invocationMatcher);
        verify(mockingProgress).pullVerificationMode();
        verify(invocationContainerImpl).setInvocationForPotentialStubbing(invocationMatcher);
        verify(mockingProgress).reportOngoingStubbing(any());
        verify(invocationContainerImpl).resetInvocationForPotentialStubbing(invocationMatcher);
    }

    @Test
    public void testHandle() throws Throwable {
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MockingProgress mockingProgress = mock(ThreadSafeMockingProgress.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        VerificationMode verificationMode = mock(MockAwareVerificationMode.class);
        VerificationDataImpl verificationDataImpl = mock(VerificationDataImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        Answer<Object> defaultAnswer = mock(Answer.class);

        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(matchersBinder.bindMatchers(any(), eq(invocation))).thenReturn(invocationMatcher);
        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(any());
        when(verificationMode.verify(verificationDataImpl)).thenReturn(null);
        when(mockSettings.getDefaultAnswer()).thenReturn(defaultAnswer);
        when(mockSettings.getDefaultAnswer().answer(invocation)).thenReturn(null);
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(stubbedInvocationMatcher);
        when(stubbedInvocationMatcher.answer(invocation)).thenReturn(null);

        MockHandler<MockSettingsImpl> mockHandler = new MockHandler<>(mockSettings);
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;

        Object result = mockHandler.handle(invocation);

        assertNull(result);
        verify(mockingProgress).pullVerificationMode();
        verify(invocationContainerImpl).setInvocationForPotentialStubbing(invocationMatcher);
        verify(mockingProgress).reportOngoingStubbing(any());
        verify(invocationContainerImpl).resetInvocationForPotentialStubbing(invocationMatcher);
    }

    @Test
    public void testVoidMethodStubbable() {
        MockHandlerInterface<Object> oldMockHandler = mock(MockHandlerInterface.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        Object mock = new Object();

        when(oldMockHandler.getMockSettings()).thenReturn(any());
        when(invocationContainerImpl.ongoingStubbingVoid(any())).thenReturn(null);

        MockHandler<Object> mockHandler = new MockHandler<>(oldMockHandler);
        mockHandler.invocationContainerImpl = invocationContainerImpl;

        VoidMethodStubbable<Object> voidMethodStubbable = mockHandler.voidMethodStubbable(mock);

        assertEquals(mockHandler.invocationContainerImpl, voidMethodStubbable.invocationContainerImpl);
    }

    @Test
    public void testGetMockSettings() {
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);

        MockHandler<MockSettingsImpl> mockHandler = new MockHandler<>(mockSettings);

        assertEquals(mockSettings, mockHandler.getMockSettings());
    }

    @Test
    public void testSetAnswersForStubbing() {
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        List<Answer> answers = mock(List.class);

        MockHandler<MockSettingsImpl> mockHandler = new MockHandler<>();
        mockHandler.invocationContainerImpl = invocationContainerImpl;

        mockHandler.setAnswersForStubbing(answers);

        verify(invocationContainerImpl).setAnswersForStubbing(answers);    
    }

    @Test
    public void testGetInvocationContainer() {
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);

        MockHandler<MockSettingsImpl> mockHandler = new MockHandler<>();
        mockHandler.invocationContainerImpl = invocationContainerImpl;

        assertEquals(invocationContainerImpl, mockHandler.getInvocationContainer());
    }
}

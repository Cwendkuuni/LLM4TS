package org.mockito.internal;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;

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
import org.mockito.internal.stubbing.VoidMethodStubbableImpl;
import org.mockito.internal.verification.MockAwareVerificationMode;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationMode;

public class MockHandlerTest {

    @Test
    public void testHandle() throws Throwable {
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MockHandler<MockSettingsImpl> mockHandler = new MockHandler<>(mockSettings);
        Invocation invocation = mock(Invocation.class);
        
        // Test stubbing voids with stubVoid() or doAnswer() style
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        List<Answer> answers = mock(List.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        OngoingStubbingImpl<MockSettingsImpl> ongoingStubbing = mock(OngoingStubbingImpl.class);
        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);
        Answer<Object> defaultAnswer = mock(Answer.class);
        
        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(true);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        when(invocation.getMock()).thenReturn(mockSettings);
        when(mockingProgress.pullVerificationMode()).thenReturn(null);
        when(mockSettings.getDefaultAnswer()).thenReturn(defaultAnswer);
        when(invocationContainerImpl.findAnswerFor(any())).thenReturn(stubbedInvocation);
        
        Object result = mockHandler.handle(invocation);
        
        assertNull(result);
        verify(invocationContainerImpl).setMethodForStubbing(invocationMatcher);
        
        // Test verification mode
        VerificationDataImpl data = mock(VerificationDataImpl.class);
        VerificationMode verificationMode = mock(VerificationMode.class);
        MockAwareVerificationMode mockAwareVerificationMode = mock(MockAwareVerificationMode.class);
        
        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(verificationMode instanceof MockAwareVerificationMode).thenReturn(true);
        when(((MockAwareVerificationMode) verificationMode).getMock()).thenReturn(mockSettings);
        when(invocationContainerImpl.findAnswerFor(any())).thenReturn(null);
        
        result = mockHandler.handle(invocation);
        
        assertNull(result);
        verify(mockingProgress).validateState();
        verify(invocationContainerImpl).setInvocationForPotentialStubbing(invocationMatcher);
        verify(verificationMode).verify(data);
        
        // Test finding answer for invocation
        when(invocationContainerImpl.findAnswerFor(any())).thenReturn(stubbedInvocation);
        when(stubbedInvocation.answer(any())).thenReturn(mockSettings);
        
        result = mockHandler.handle(invocation);
        
        assertEquals(mockSettings, result);
        verify(stubbedInvocation).captureArgumentsFrom(invocation);
        
        // Test finding answer for invocation with null result
        when(invocationContainerImpl.findAnswerFor(any())).thenReturn(null);
        when(mockSettings.getDefaultAnswer()).thenReturn(defaultAnswer);
        
        result = mockHandler.handle(invocation);
        
        assertEquals(mockSettings, result);
        verify(invocationContainerImpl).resetInvocationForPotentialStubbing(invocationMatcher);
    }

    @Test
    public void testVoidMethodStubbable() {
        MockHandler<MockSettingsImpl> mockHandler = new MockHandler<>();
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        VoidMethodStubbableImpl<MockSettingsImpl> voidMethodStubbable = mock(VoidMethodStubbableImpl.class);
        
        when(voidMethodStubbable.getInvocationContainer()).thenReturn(invocationContainerImpl);
        when(voidMethodStubbable.getMockSettings()).thenReturn(mockSettings);
        
        VoidMethodStubbableImpl<MockSettingsImpl> result = (VoidMethodStubbableImpl<MockSettingsImpl>) mockHandler
                .voidMethodStubbable(mockSettings);
        
        assertEquals(voidMethodStubbable, result);
    }

    @Test
    public void testGetMockSettings() {
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MockHandler<MockSettingsImpl> mockHandler = new MockHandler<>(mockSettings);
        
        MockSettingsImpl result = mockHandler.getMockSettings();
        
        assertEquals(mockSettings, result);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testSetAnswersForStubbing() {
        MockHandler<MockSettingsImpl> mockHandler = new MockHandler<>();
        List<Answer> answers = mock(List.class);
        
        mockHandler.setAnswersForStubbing(answers);
        
        verify(mockHandler.invocationContainerImpl).setAnswersForStubbing(answers);
    }

    @Test
    public void testGetInvocationContainer() {
        MockHandler<MockSettingsImpl> mockHandler = new MockHandler<>();
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        
        when(mockHandler.getInvocationContainer()).thenReturn(invocationContainerImpl);
        
        InvocationContainer result = mockHandler.getInvocationContainer();
        
        assertEquals(invocationContainerImpl, result);
    }
}
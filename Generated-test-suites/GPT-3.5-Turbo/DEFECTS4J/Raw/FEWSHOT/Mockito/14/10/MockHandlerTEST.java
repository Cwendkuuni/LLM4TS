package org.mockito.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.verification.VerificationMode;

public class MockHandlerTest {
    
    private MockHandler<Object> mockHandler;
    
    @Before
    public void setUp() {
        mockHandler = new MockHandler<Object>();
    }
    
    @Test
    public void testHandle_withAnswersForStubbing() throws Throwable {
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;
        
        Invocation invocation = mock(Invocation.class);
        
        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(true);
        
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        when(matchersBinder.bindMatchers(any(), eq(invocation))).thenReturn(invocationMatcher);
        
        mockHandler.handle(invocation);
        
        verify(matchersBinder).bindMatchers(any(), eq(invocation));
        verify(invocationContainerImpl).setMethodForStubbing(invocationMatcher);
    }
    
    @Test
    public void testHandle_withVerificationMode() throws Throwable {
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        VerificationMode verificationMode = mock(VerificationMode.class);
        VerificationDataImpl verificationData = mock(VerificationDataImpl.class);
        
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;
        
        Invocation invocation = mock(Invocation.class);
        
        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(matchersBinder.bindMatchers(any(), eq(invocation))).thenReturn(mock(InvocationMatcher.class));
        when(invocationContainerImpl.getInvocations()).thenReturn(Arrays.asList(invocation));
        when(invocationContainerImpl.findAnswerFor(eq(invocation))).thenReturn(mock(StubbedInvocationMatcher.class));
        
        mockHandler.handle(invocation);
        
        verify(mockingProgress).pullVerificationMode();
        verify(matchersBinder).bindMatchers(any(), eq(invocation));
        verify(invocationContainerImpl).getInvocations();
        verify(verificationMode).verify(eq(verificationData));
    }
    
    @Test
    public void testHandle_withStubbedInvocation() throws Throwable {
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        OngoingStubbingImpl<Object> ongoingStubbing = mock(OngoingStubbingImpl.class);
        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);
        
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;
        mockHandler.mockSettings = mockSettings;
        
        Invocation invocation = mock(Invocation.class);
        
        when(mockSettings.getDefaultAnswer()).thenReturn(mock(Answer.class));
        when(matchersBinder.bindMatchers(any(), eq(invocation))).thenReturn(invocationMatcher);
        when(invocationContainerImpl.findAnswerFor(eq(invocation))).thenReturn(stubbedInvocation);
        when(ongoingStubbing.answer(eq(invocation))).thenReturn(mock(Object.class));
        
        mockHandler.handle(invocation);
        
        verify(mockSettings).getDefaultAnswer();
        verify(matchersBinder).bindMatchers(any(), eq(invocation));
        verify(invocationContainerImpl).findAnswerFor(eq(invocation));
        verify(stubbedInvocation).answer(eq(invocation));
    }
    
    @Test
    public void testHandle_withoutStubbedInvocation() throws Throwable {
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        OngoingStubbingImpl<Object> ongoingStubbing = mock(OngoingStubbingImpl.class);
        
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;
        mockHandler.mockSettings = mockSettings;
        
        Invocation invocation = mock(Invocation.class);
        
        when(mockSettings.getDefaultAnswer()).thenReturn(mock(Answer.class));
        when(matchersBinder.bindMatchers(any(), eq(invocation))).thenReturn(invocationMatcher);
        when(invocationContainerImpl.findAnswerFor(eq(invocation))).thenReturn(null);
        when(mockSettings.getDefaultAnswer().answer(eq(invocation))).thenReturn(mock(Object.class));
        
        mockHandler.handle(invocation);
        
        verify(mockSettings).getDefaultAnswer();
        verify(matchersBinder).bindMatchers(any(), eq(invocation));
        verify(invocationContainerImpl).findAnswerFor(eq(invocation));
        verify(mockSettings.getDefaultAnswer()).answer(eq(invocation));
    }
    
    @Test
    public void testVoidMethodStubbable() {
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        Object mock = new Object();
        
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        
        mockHandler.voidMethodStubbable(mock);
        
        verify(invocationContainerImpl).getMock();
    }
    
    @Test
    public void testGetMockSettings() {
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        mockHandler.mockSettings = mockSettings;
        
        assertEquals(mockSettings, mockHandler.getMockSettings());
    }
    
    @Test
    public void testSetAnswersForStubbing() {
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        List<Answer> answers = Arrays.asList(mock(Answer.class));
        
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        
        mockHandler.setAnswersForStubbing(answers);
        
        verify(invocationContainerImpl).setAnswersForStubbing(answers);
    }
    
    @Test
    public void testGetInvocationContainer() {
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        
        assertEquals(invocationContainerImpl, mockHandler.getInvocationContainer());
    }
}
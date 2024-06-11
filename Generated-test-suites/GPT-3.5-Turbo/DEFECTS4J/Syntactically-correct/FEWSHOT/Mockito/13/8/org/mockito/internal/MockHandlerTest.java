package org.mockito.internal;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.VoidMethodStubbable;

public class MockHandlerTest {
    
    private MockHandler mockHandler;
    private MockSettingsImpl mockSettings;
    private InvocationContainerImpl invocationContainerImpl;
    private MatchersBinder matchersBinder;
    private MockingProgress mockingProgress;

    @Before
    public void setUp() {
        mockSettings = mock(MockSettingsImpl.class);
        mockHandler = new MockHandler(mockSettings);
        invocationContainerImpl = mock(InvocationContainerImpl.class);
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        matchersBinder = mock(MatchersBinder.class);
        mockHandler.matchersBinder = matchersBinder;
        mockingProgress = mock(ThreadSafeMockingProgress.class);
        mockHandler.mockingProgress = mockingProgress;
    }
    
    @Test
    public void testHandle_whenHasAnswersForStubbing() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(true);
        when(matchersBinder.bindMatchers(mockingProgress.getArgumentMatcherStorage(), invocation)).thenReturn(invocationMatcher);
        
        mockHandler.handle(invocation);
        
        verify(invocationContainerImpl).setMethodForStubbing(invocationMatcher);
    }
    
    @Test
    public void testHandle_whenVerificationModeIsNotNull_andInstanceOfMockAwareVerificationMode() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        VerificationMode verificationMode = mock(MockAwareVerificationMode.class);
        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(matchersBinder.bindMatchers(mockingProgress.getArgumentMatcherStorage(), invocation)).thenReturn(mock(InvocationMatcher.class));
        when(((MockAwareVerificationMode) verificationMode).getMock()).thenReturn(invocation.getMock());
        
        mockHandler.handle(invocation);
        
        verify(verificationMode).verify(any(VerificationDataImpl.class));
    }
    
    @Test
    public void testHandle_whenStubbedInvocationNotNull() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        when(matchersBinder.bindMatchers(mockingProgress.getArgumentMatcherStorage(), invocation)).thenReturn(invocationMatcher);
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(mock(StubbedInvocationMatcher.class));
        
        mockHandler.handle(invocation);
        
        verify(invocationContainerImpl).findAnswerFor(invocation);
    }
    
    @Test
    public void testHandle_whenStubbedInvocationNull() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        when(matchersBinder.bindMatchers(mockingProgress.getArgumentMatcherStorage(), invocation)).thenReturn(invocationMatcher);
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(null);
        when(mockSettings.getDefaultAnswer()).thenReturn(mock(Answer.class));
        
        mockHandler.handle(invocation);
        
        verify(mockSettings).getDefaultAnswer();
    }
    
    @Test
    public void testVoidMethodStubbable() {
        List<Answer> answers = new ArrayList<Answer>();
        mockHandler.voidMethodStubbable(mock(Object.class));
        
        verify(invocationContainerImpl).setAnswersForStubbing(answers);
    }
    
    @Test
    public void testGetMockSettings() {
        assertEquals(mockSettings, mockHandler.getMockSettings());
    }
    
    @Test
    public void testSetAnswersForStubbing() {
        List<Answer> answers = new ArrayList<Answer>();
        mockHandler.setAnswersForStubbing(answers);
        
        verify(invocationContainerImpl).setAnswersForStubbing(answers);
    }
    
    @Test
    public void testGetInvocationContainer() {
        assertEquals(invocationContainerImpl, mockHandler.getInvocationContainer());
    }
}
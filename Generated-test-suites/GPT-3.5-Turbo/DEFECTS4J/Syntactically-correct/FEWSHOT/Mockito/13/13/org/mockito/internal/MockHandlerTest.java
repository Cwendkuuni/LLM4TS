package org.mockito.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

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
    public void testHandle_withStubbingVoid() throws Throwable {
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        
        MockHandler<Object> handler = new MockHandler<Object>(mockSettings);
        handler.invocationContainerImpl = container;
        handler.matchersBinder = matchersBinder;
        handler.mockingProgress = mockingProgress;
        
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        
        when(container.hasAnswersForStubbing()).thenReturn(true);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        
        assertNull(handler.handle(invocation));
        
        verify(container).setMethodForStubbing(invocationMatcher);
    }
    
    @Test
    public void testHandle_withVerificationMode() throws Throwable {
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        
        MockHandler<Object> handler = new MockHandler<Object>(mockSettings);
        handler.invocationContainerImpl = container;
        handler.matchersBinder = matchersBinder;
        handler.mockingProgress = mockingProgress;
        
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        VerificationMode verificationMode = mock(VerificationMode.class);
        
        when(container.findAnswerFor(invocation)).thenReturn(null);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        when(verificationMode instanceof MockAwareVerificationMode).thenReturn(true);
        when(((MockAwareVerificationMode) verificationMode).getMock()).thenReturn(invocation.getMock());
        
        handler.mockingProgress.pushVerificationMode(verificationMode);
        
        assertNull(handler.handle(invocation));
        
        verify(mockingProgress).pullVerificationMode();
        verify(container).setInvocationForPotentialStubbing(invocationMatcher);
    }
    
    @Test
    public void testHandle_withStubbedInvocation() throws Throwable {
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        
        MockHandler<Object> handler = new MockHandler<Object>(mockSettings);
        handler.invocationContainerImpl = container;
        handler.matchersBinder = matchersBinder;
        handler.mockingProgress = mockingProgress;
        
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);
        
        when(container.findAnswerFor(invocation)).thenReturn(stubbedInvocation);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        
        assertEquals(stubbedInvocation.answer(invocation), handler.handle(invocation));
        
        verify(stubbedInvocation).captureArgumentsFrom(invocation);
    }
    
    @Test
    public void testHandle_withDefaultAnswer() throws Throwable {
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        
        MockHandler<Object> handler = new MockHandler<Object>(mockSettings);
        handler.invocationContainerImpl = container;
        handler.matchersBinder = matchersBinder;
        handler.mockingProgress = mockingProgress;
        
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        
        when(container.findAnswerFor(invocation)).thenReturn(null);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        when(mockSettings.getDefaultAnswer().answer(invocation)).thenReturn(null);
        
        assertNull(handler.handle(invocation));
        
        verify(invocationContainerImpl).resetInvocationForPotentialStubbing(invocationMatcher);
    }
    
    @Test
    public void testVoidMethodStubbable() {
        MockHandler<Object> handler = new MockHandler<Object>();
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        Object mock = new Object();
        
        handler.invocationContainerImpl = container;
        
        VoidMethodStubbable<T> stubbable = handler.voidMethodStubbable(mock);
        
        assertSame(mock, stubbable.getMock());
        assertSame(container, stubbable.getInvocationContainer());
    }
    
    @Test
    public void testSetAnswersForStubbing() {
        MockHandler<Object> handler = new MockHandler<Object>();
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        List<Answer> answers = mock(List.class);
        
        handler.invocationContainerImpl = container;
        
        handler.setAnswersForStubbing(answers);
        
        verify(container).setAnswersForStubbing(answers);
    }
    
    @Test
    public void testGetInvocationContainer() {
        MockHandler<Object> handler = new MockHandler<Object>();
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        
        handler.invocationContainerImpl = container;
        
        assertSame(container, handler.getInvocationContainer());
    }
    
    @Test
    public void testConstructors() {
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        
        MockHandler<Object> handler = new MockHandler<Object>(mockSettings);
        
        assertNotNull(handler.mockingProgress);
        assertNotNull(handler.matchersBinder);
        assertNotNull(handler.invocationContainerImpl);
        assertSame(mockSettings, handler.mockSettings);
        
        handler = new MockHandler<Object>();
        
        assertTrue(handler.mockingProgress instanceof ThreadSafeMockingProgress);
        assertTrue(handler.matchersBinder instanceof MatchersBinder);
        assertTrue(handler.invocationContainerImpl instanceof InvocationContainerImpl);
    }
}
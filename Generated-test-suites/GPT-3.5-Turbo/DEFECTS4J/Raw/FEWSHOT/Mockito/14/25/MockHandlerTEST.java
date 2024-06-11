package org.mockito.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

public class MockHandlerTest {
    
    private MockHandler<Object> mockHandler;
    
    @Before
    public void setup() {
        mockHandler = new MockHandler<>(new MockSettingsImpl());
    }
    
    @Test
    public void testHandle_withInvocationContainerImplHasAnswersForStubbing() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        when(mockHandler.invocationContainerImpl.hasAnswersForStubbing()).thenReturn(true);
        when(mockHandler.matchersBinder.bindMatchers(mockHandler.mockingProgress.getArgumentMatcherStorage(), invocation)).thenReturn(mock(InvocationMatcher.class));
        
        assertNull(mockHandler.handle(invocation));
        
        verify(mockHandler.invocationContainerImpl).setMethodForStubbing(mock(InvocationMatcher.class));
    }
    
    @Test
    public void testHandle_withPullVerificationModeNotNull() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        when(mockHandler.mockingProgress.pullVerificationMode()).thenReturn(mock(VerificationMode.class));
        when(mockHandler.matchersBinder.bindMatchers(mockHandler.mockingProgress.getArgumentMatcherStorage(), invocation)).thenReturn(mock(InvocationMatcher.class));
        
        assertNull(mockHandler.handle(invocation));
        
        verify(mockHandler.invocationContainerImpl).getInvocations();
        verify(mockHandler.mockingProgress).validateState();
        verify(mockHandler.mockingProgress).pullVerificationMode();
        verify(mockHandler.invocationContainerImpl).setInvocationForPotentialStubbing(mock(InvocationMatcher.class));
    }
    
    @Test
    public void testHandle_withStubbedInvocationNotNull() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        when(mockHandler.mockingProgress.pullVerificationMode()).thenReturn(null);
        when(mockHandler.matchersBinder.bindMatchers(mockHandler.mockingProgress.getArgumentMatcherStorage(), invocation)).thenReturn(mock(InvocationMatcher.class));
        when(mockHandler.invocationContainerImpl.findAnswerFor(invocation)).thenReturn(mock(StubbedInvocationMatcher.class));
        
        assertNotNull(mockHandler.handle(invocation));
        
        verify(mockHandler.mockingProgress).resetInvocationForPotentialStubbing(mock(InvocationMatcher.class));
    }
    
    @Test
    public void testHandle_withStubbedInvocationNull() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        when(mockHandler.mockingProgress.pullVerificationMode()).thenReturn(null);
        when(mockHandler.matchersBinder.bindMatchers(mockHandler.mockingProgress.getArgumentMatcherStorage(), invocation)).thenReturn(mock(InvocationMatcher.class));
        when(mockHandler.invocationContainerImpl.findAnswerFor(invocation)).thenReturn(null);
        when(mockHandler.mockSettings.getDefaultAnswer().answer(invocation)).thenReturn("default");
        
        assertEquals("default", mockHandler.handle(invocation));
        
        verify(mockHandler.mockingProgress).resetInvocationForPotentialStubbing(mock(InvocationMatcher.class));
    }
    
    @Test
    public void testVoidMethodStubbable() {
        Object mock = new Object();
        
        assertNotNull(mockHandler.voidMethodStubbable(mock));
    }
    
    @Test
    public void testGetMockSettings() {
        assertNotNull(mockHandler.getMockSettings());
    }
    
    @Test
    public void testSetAnswersForStubbing() {
        assertNotNull(mockHandler.setAnswersForStubbing(Arrays.asList(mock(Answer.class))));
    }
    
    @Test
    public void testGetInvocationContainer() {
        assertNotNull(mockHandler.getInvocationContainer());
    }
}
package org.mockito.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;
import org.junit.Test;

public class MockHandlerTest {
    
    @Test
    public void testConstructorWithMockSettings() {
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);
        assertNotNull(mockHandler);
    }
    
    @Test
    public void testDefaultConstructor() {
        MockHandler<Object> mockHandler = new MockHandler<>();
        assertNotNull(mockHandler);
    }
    
    @Test
    public void testConstructorWithMockHandlerInterface() {
        MockHandlerInterface<Object> oldMockHandler = mock(MockHandlerInterface.class);
        when(oldMockHandler.getMockSettings()).thenReturn(mock(MockSettingsImpl.class));
        MockHandler<Object> mockHandler = new MockHandler<>(oldMockHandler);
        assertNotNull(mockHandler);
    }
    
    @Test
    public void testHandleWhenHasAnswersForStubbing() throws Throwable {
        MockHandler<Object> mockHandler = new MockHandler<>();
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(mockHandler.getInvocationContainer().hasAnswersForStubbing()).thenReturn(true);
        mockHandler.handle(invocation);
        verify(mockHandler.getInvocationContainer(), times(1)).setMethodForStubbing(any(InvocationMatcher.class));
    }
    
    @Test
    public void testHandleWithVerificationMode() throws Throwable {
        MockHandler<Object> mockHandler = new MockHandler<>();
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        doReturn(mock(MockAwareVerificationMode.class)).when(mockHandler.mockingProgress).pullVerificationMode();
        mockHandler.handle(invocation);
        verify(mockHandler.mockingProgress, times(1)).validateState();
        verify(mockHandler.mockingProgress, times(1)).pullVerificationMode();
        verify(mockHandler.mockingProgress, never()).reportOngoingStubbing(any(OngoingStubbingImpl.class));
        verify(mockHandler.getInvocationContainer(), never()).findAnswerFor(any(Invocation.class));
        verify(mockHandler.getMockSettings(), never()).getDefaultAnswer();
    }
    
    @Test
    public void testHandleWithoutStubbedInvocation() throws Throwable {
        MockHandler<Object> mockHandler = new MockHandler<>();
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        doReturn(null).when(mockHandler.getInvocationContainer()).findAnswerFor(any(Invocation.class));
        mockHandler.handle(invocation);
        verify(mockHandler.mockingProgress, times(1)).validateState();
        verify(mockHandler.mockingProgress, times(1)).pullVerificationMode();
        verify(mockHandler.getInvocationContainer(), times(1)).setInvocationForPotentialStubbing(any(InvocationMatcher.class));
        verify(mockHandler.mockingProgress, times(1)).reportOngoingStubbing(any(OngoingStubbingImpl.class));
        verify(mockHandler.getMockSettings(), times(1)).getDefaultAnswer();
        verify(mockHandler.getInvocationContainer(), times(1)).resetInvocationForPotentialStubbing(any(InvocationMatcher.class));
    }
    
    @Test
    public void testVoidMethodStubbable() {
        MockHandler<Object> mockHandler = new MockHandler<>();
        Object mock = mock(Object.class);
        VoidMethodStubbable stubbable = mockHandler.voidMethodStubbable(mock);
        assertNotNull(stubbable);
    }
    
    @Test
    public void testSetAnswersForStubbing() {
        MockHandler<Object> mockHandler = new MockHandler<>();
        List<Answer> answers = mock(List.class);
        mockHandler.setAnswersForStubbing(answers);
        verify(mockHandler.getInvocationContainer(), times(1)).setAnswersForStubbing(answers);
    }
    
    @Test
    public void testGetInvocationContainer() {
        MockHandler<Object> mockHandler = new MockHandler<>();
        InvocationContainer container = mockHandler.getInvocationContainer();
        assertNotNull(container);
        assertEquals(mockHandler.invocationContainerImpl, container);
    }
    
}
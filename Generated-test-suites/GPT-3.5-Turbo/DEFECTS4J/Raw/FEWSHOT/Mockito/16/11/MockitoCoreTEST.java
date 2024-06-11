package org.mockito.internal;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

public class MockitoCoreTest {
    
    @Test
    public void testMock() {
        MockitoCore mockito = new MockitoCore();
        Object mockedObj = mockito.mock(Object.class);
        assertNotNull(mockedObj);
    }
    
    @Test
    public void testMockWithSettings() {
        MockitoCore mockito = new MockitoCore();
        MockSettings settings = mock(MockSettings.class);
        settings.stubOnly();
        Object mockedObj = mockito.mock(Object.class, settings);
        assertNotNull(mockedObj);
    }
    
    @Test
    public void testStub() {
        MockitoCore mockito = new MockitoCore();
        IOngoingStubbing stubbing = mockito.stub();
        assertNull(stubbing);
    }
    
    @Test
    public void testDeprecatedStub() {
        MockitoCore mockito = new MockitoCore();
        DeprecatedOngoingStubbing<String> stubbing = mockito.stub("methodCall");
        assertNull(stubbing);
    }
    
    @Test
    public void testWhen() {
        MockitoCore mockito = new MockitoCore();
        OngoingStubbing<String> stubbing = mockito.when("methodCall");
        assertNull(stubbing);
    }
    
    @Test
    public void testVerify() {
        MockitoCore mockito = new MockitoCore();
        String mock = mock(String.class);
        VerificationMode mode = mock(VerificationMode.class);
        String result = mockito.verify(mock, mode);
        assertSame(mock, result);
    }
    
    @Test
    public void testReset() {
        MockitoCore mockito = new MockitoCore();
        String mock = mock(String.class);
        mockito.reset(mock);
        verify(mockito.mockUtil).resetMock(mock);
    }
    
    @Test
    public void testVerifyNoMoreInteractions() {
        MockitoCore mockito = new MockitoCore();
        String mock = mock(String.class);
        mockito.verifyNoMoreInteractions(mock);
        verify(mockito.reporter).notAMockPassedToVerifyNoMoreInteractions();
    }
    
    @Test
    public void testInOrder() {
        MockitoCore mockito = new MockitoCore();
        String mock1 = mock(String.class);
        String mock2 = mock(String.class);
        InOrder inOrder = mockito.inOrder(mock1, mock2);
        assertNotNull(inOrder);
    }
    
    @Test
    public void testDoAnswer() {
        MockitoCore mockito = new MockitoCore();
        Answer answer = mock(Answer.class);
        Stubber stubber = mockito.doAnswer(answer);
        assertNotNull(stubber);
    }
    
    @Test
    public void testStubVoid() {
        MockitoCore mockito = new MockitoCore();
        String mock = mock(String.class);
        VoidMethodStubbable<String> stubbable = mockito.stubVoid(mock);
        assertNotNull(stubbable);
        assertSame(mockito.mockUtil.getMockHandler(mock), stubbable);
    }
    
    @Test
    public void testValidateMockitoUsage() {
        MockitoCore mockito = new MockitoCore();
        mockito.validateMockitoUsage();
        verify(mockito.mockingProgress).validateState();
    }
    
    @Test
    public void testGetLastInvocation() {
        MockitoCore mockito = new MockitoCore();
        OngoingStubbingImpl ongoingStubbing = mock(OngoingStubbingImpl.class);
        when(mockito.mockingProgress.pullOngoingStubbing()).thenReturn(ongoingStubbing);
        List<Invocation> allInvocations = mock(List.class);
        Invocation lastInvocation = mock(Invocation.class);
        when(ongoingStubbing.getRegisteredInvocations()).thenReturn(allInvocations);
        when(allInvocations.get(anyInt())).thenReturn(lastInvocation);
        
        Invocation result = mockito.getLastInvocation();
        assertSame(lastInvocation, result);
    }
}
package org.mockito.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Test;

public class MockitoCoreTest {
    @Test
    public void testMock() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        
        Object result = mockitoCore.mock(Object.class, any(MockitoCore.MockSettings.class), anyBoolean());
        
        // Verify the mockitoCore method is called with the expected parameters
        verify(mockitoCore).mock(Object.class, any(MockitoCore.MockSettings.class), anyBoolean());
        
        // Verify the returned result is not null
        assertNotNull(result);
    }
    
    @Test
    public void testStub() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        MockitoCore.IOngoingStubbing ongoingStubbing = mock(MockitoCore.IOngoingStubbing.class);
        
        when(mockitoCore.stub()).thenReturn(ongoingStubbing);
        
        MockitoCore.IOngoingStubbing result = mockitoCore.stub();
        
        // Verify the mockitoCore method is called
        verify(mockitoCore).stub();
        
        // Verify the returned result is the same as the mocked ongoingStubbing
        assertEquals(ongoingStubbing, result);
    }
    
    @Test
    public void testDeprecatedStub() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        MockitoCore.DeprecatedOngoingStubbing ongoingStubbing = mock(MockitoCore.DeprecatedOngoingStubbing.class);
        Object methodCall = new Object();
        
        when(mockitoCore.stub(methodCall)).thenReturn(ongoingStubbing);
        
        MockitoCore.DeprecatedOngoingStubbing result = mockitoCore.stub(methodCall);
        
        // Verify the mockitoCore method is called with the expected parameter
        verify(mockitoCore).stub(methodCall);
        
        // Verify the returned result is the same as the mocked ongoingStubbing
        assertEquals(ongoingStubbing, result);
    }
    
    @Test
    public void testWhen() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        MockitoCore.OngoingStubbing ongoingStubbing = mock(MockitoCore.OngoingStubbing.class);
        Object methodCall = new Object();
        
        when(mockitoCore.when(methodCall)).thenReturn(ongoingStubbing);
        
        MockitoCore.OngoingStubbing result = mockitoCore.when(methodCall);
        
        // Verify the mockitoCore method is called with the expected parameter
        verify(mockitoCore).when(methodCall);
        
        // Verify the returned result is the same as the mocked ongoingStubbing
        assertEquals(ongoingStubbing, result);
    }
    
    @Test
    public void testVerify() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        Object mock = mock(Object.class);
        MockitoCore.VerificationMode mode = mock(MockitoCore.VerificationMode.class);
        
        when(mockitoCore.verify(mock, mode)).thenReturn(mock);
        
        Object result = mockitoCore.verify(mock, mode);
        
        // Verify the mockitoCore method is called with the expected parameters
        verify(mockitoCore).verify(mock, mode);
        
        // Verify the returned result is the same as the mocked object
        assertEquals(mock, result);
    }
    
    @Test
    public void testReset() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        Object[] mocks = new Object[2];
        mocks[0] = mock(Object.class);
        mocks[1] = mock(Object.class);
        
        mockitoCore.reset(mocks);
        
        // Verify the mockitoCore method is called with the expected mocks
        verify(mockitoCore).reset(eq(mocks[0]), eq(mocks[1]));
    }
    
    @Test
    public void testVerifyNoMoreInteractions() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        Object[] mocks = new Object[2];
        mocks[0] = mock(Object.class);
        mocks[1] = mock(Object.class);
        
        mockitoCore.verifyNoMoreInteractions(mocks);
        
        // Verify the mockitoCore method is called with the expected mocks
        verify(mockitoCore).verifyNoMoreInteractions(eq(mocks[0]), eq(mocks[1]));
    }
    
    @Test
    public void testInOrder() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        Object[] mocks = new Object[2];
        mocks[0] = mock(Object.class);
        mocks[1] = mock(Object.class);
        
        InOrderImpl inOrderImpl = mock(InOrderImpl.class);
        
        when(mockitoCore.inOrder(mocks)).thenReturn(inOrderImpl);
        
        InOrderImpl result = mockitoCore.inOrder(mocks);
        
        // Verify the mockitoCore method is called with the expected mocks
        verify(mockitoCore).inOrder(eq(mocks[0]), eq(mocks[1]));
        
        // Verify the returned result is the same as the mocked inOrderImpl
        assertEquals(inOrderImpl, result);
    }
    
    @Test
    public void testDoAnswer() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        MockitoCore.Stubber stubber = mock(MockitoCore.Stubber.class);
        MockitoCore.Answer answer = mock(MockitoCore.Answer.class);
        
        when(mockitoCore.doAnswer(answer)).thenReturn(stubber);
        
        MockitoCore.Stubber result = mockitoCore.doAnswer(answer);
        
        // Verify the mockitoCore method is called with the expected answer
        verify(mockitoCore).doAnswer(eq(answer));
        
        // Verify the returned result is the same as the mocked stubber
        assertEquals(stubber, result);
    }
    
    @Test
    public void testStubVoid() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        MockitoCore.MockHandlerInterface mockHandler = mock(MockitoCore.MockHandlerInterface.class);
        Object mock = mock(Object.class);
        
        when(mockitoCore.stubVoid(mock)).thenReturn(mockHandler);
        
        MockitoCore.VoidMethodStubbable result = mockitoCore.stubVoid(mock);
        
        // Verify the mockitoCore method is called with the expected mock
        verify(mockitoCore).stubVoid(eq(mock));
        
        // Verify the returned result is the same as the mocked mockHandler
        assertEquals(mockHandler, result);
    }
    
    @Test
    public void testValidateMockitoUsage() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        
        mockitoCore.validateMockitoUsage();
        
        // Verify the mockitoCore method is called
        verify(mockitoCore).validateMockitoUsage();
    }
    
    @Test
    public void testGetLastInvocation() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        MockitoCore.Invocation invocation = mock(MockitoCore.Invocation.class);
        OngoingStubbingImpl ongoingStubbing = mock(OngoingStubbingImpl.class);
        List<MockitoCore.Invocation> allInvocations = Arrays.asList(invocation);
        
        when(mockitoCore.getLastInvocation()).thenReturn(invocation);
        when(mockitoCore.mockingProgress.pullOngoingStubbing()).thenReturn(ongoingStubbing);
        when(ongoingStubbing.getRegisteredInvocations()).thenReturn(allInvocations);
        
        MockitoCore.Invocation result = mockitoCore.getLastInvocation();
        
        // Verify the mockitoCore method is called
        verify(mockitoCore).getLastInvocation();
        verify(mockitoCore.mockingProgress).pullOngoingStubbing();
        verify(ongoingStubbing).getRegisteredInvocations();
        
        // Verify the returned result is the same as the mocked invocation
        assertEquals(invocation, result);
    }
}
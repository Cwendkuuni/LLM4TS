package org.mockito.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Test;

public class MockitoCoreTest {
    
    @Test
    public void testMock() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        
        // Test mock() method
        assertEquals("mock", mockitoCore.mock(String.class, null));
        assertEquals("mock", mockitoCore.mock(String.class, null, false));
        assertEquals("mock", mockitoCore.mock(String.class, null, true));
        
        // Test mock() method with mockSettings
        assertEquals("mock", mockitoCore.mock(Integer.class, mock(MockSettings.class)));
        assertEquals("mock", mockitoCore.mock(Integer.class, mock(MockSettings.class), false));
        assertEquals("mock", mockitoCore.mock(Integer.class, mock(MockSettings.class), true));
    }
    
    @Test
    public void testStub() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        
        // Test stub() method
        IOngoingStubbing ongoingStubbing = mockitoCore.stub();
        assertNull(ongoingStubbing);
        
        // Test deprecated stub(T) method
        DeprecatedOngoingStubbing<String> deprecatedOngoingStubbing = mockitoCore.stub("");
        assertNull(deprecatedOngoingStubbing);
    }
    
    @Test
    public void testWhen() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        
        // Test when() method
        OngoingStubbing<String> ongoingStubbing = mockitoCore.when("");
        assertNull(ongoingStubbing);
    }
    
    @Test
    public void testVerify() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        String mock = "mock";
        
        // Test verify() method
        assertEquals("mock", mockitoCore.verify(mock, null));
    }
    
    @Test
    public void testReset() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        String mock1 = "mock1";
        String mock2 = "mock2";
        
        // Test reset() method
        mockitoCore.reset(mock1, mock2);
        
        // Verify resetMock() method is called for each mock
        verify(mockitoCore, times(1)).reset(mock1);
        verify(mockitoCore, times(1)).reset(mock2);
    }
    
    @Test
    public void testVerifyNoMoreInteractions() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        String mock = "mock";
        
        // Test verifyNoMoreInteractions() method
        mockitoCore.verifyNoMoreInteractions(mock);
        
        // Verify getMockHandler() method is called for each mock
        verify(mockitoCore, times(1)).getMockHandler(mock);
    }
    
    @Test
    public void testInOrder() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        String mock1 = "mock1";
        String mock2 = "mock2";
        
        // Test inOrder() method
        InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
        
        // Verify isMock() method is called for each mock
        verify(mockitoCore, times(1)).isMock(mock1);
        verify(mockitoCore, times(1)).isMock(mock2);
        
        // Verify new InOrderImpl() is called with list of mocks
        verifyNew(InOrderImpl.class).withArguments(Arrays.asList(mock1, mock2));
    }
    
    @Test
    public void testDoAnswer() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        
        // Test doAnswer() method
        Answer answer = mock(Answer.class);
        StubberImpl stubber = mockitoCore.doAnswer(answer);
        
        // Verify stubbingStarted() method is called
        verify(mockitoCore, times(1)).stubbingStarted();
        
        // Verify resetOngoingStubbing() method is called
        verify(mockitoCore, times(1)).resetOngoingStubbing();
        
        // Verify doAnswer() method is called on StubberImpl
        verify(stubber, times(1)).doAnswer(answer);
    }
    
    @Test
    public void testStubVoid() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        String mock = "mock";
        
        // Test stubVoid() method
        VoidMethodStubbableStub stub = mock(VoidMethodStubbableStub.class);
        when(mockitoCore.stubVoid(mock)).thenReturn(stub);
        
        // Stubbed method call should return the stub
        assertEquals(stub, mockitoCore.stubVoid(mock));
        
        // Verify stubbingStarted() method is called
        verify(mockitoCore, times(1)).stubbingStarted();
        
        // Verify getMockHandler() method is called
        verify(mockitoCore, times(1)).getMockHandler(mock);
    }
    
    @Test
    public void testValidateMockitoUsage() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        
        // Test validateMockitoUsage() method
        mockitoCore.validateMockitoUsage();
        
        // Verify validateState() method is called
        verify(mockitoCore, times(1)).validateState();
    }
    
    @Test
    public void testGetLastInvocation() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        OngoingStubbingImpl ongoingStubbing = mock(OngoingStubbingImpl.class);
        Invocation invocation = mock(Invocation.class);
        
        // Stub pullOngoingStubbing() to return an ongoing stubbing
        when(mockitoCore.pullOngoingStubbing()).thenReturn(ongoingStubbing);
        
        // Stub getRegisteredInvocations() to return a list of invocations
        List<Invocation> invocations = Arrays.asList(invocation);
        when(ongoingStubbing.getRegisteredInvocations()).thenReturn(invocations);
        
        // Test getLastInvocation() method
        assertEquals(invocation, mockitoCore.getLastInvocation());
        
        // Verify pullOngoingStubbing() method is called
        verify(mockitoCore, times(1)).pullOngoingStubbing();
    }
}
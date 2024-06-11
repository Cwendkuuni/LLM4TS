package org.mockito.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.MockSettings;
import org.mockito.stubbing.Answer;

public class MockitoCoreTest {
    @Test
    public void testMock() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        
        MockSettings mockSettings = mock(MockSettings.class);
        assertEquals("foo", mockitoCore.mock(String.class, mockSettings, true));
        assertEquals("bar", mockitoCore.mock(String.class, mockSettings));
    }
    
    @Test
    public void testStub() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        
        IOngoingStubbing ongoingStubbing = mock(IOngoingStubbing.class);
        when(mockitoCore.stub()).thenReturn(ongoingStubbing);
        
        assertEquals(ongoingStubbing, mockitoCore.stub());
    }
    
    @Test
    public void testStubDeprecated() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        
        DeprecatedOngoingStubbing deprecatedOngoingStubbing = mock(DeprecatedOngoingStubbing.class);
        when(mockitoCore.stub(anyString())).thenReturn(deprecatedOngoingStubbing);
        
        assertEquals(deprecatedOngoingStubbing, mockitoCore.stub("foo"));
    }
    
    @Test
    public void testWhen() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        
        OngoingStubbing ongoingStubbing = mock(OngoingStubbing.class);
        when(mockitoCore.when(anyString())).thenReturn(ongoingStubbing);
        
        assertEquals(ongoingStubbing, mockitoCore.when("foo"));
    }
    
    @Test
    public void testVerify() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        
        VerificationMode verificationMode = mock(VerificationMode.class);
        String mock = "mock";
        when(mockitoCore.verify(mock, verificationMode)).thenReturn(mock);
        
        assertEquals(mock, mockitoCore.verify("mock", verificationMode));
    }
    
    @Test
    public void testReset() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        
        String[] mocks = new String[]{"mock1", "mock2"};
        mockitoCore.reset(mocks);
        
        verify(mockitoCore, times(2)).resetMock(anyString());
    }
    
    @Test
    public void testVerifyNoMoreInteractions() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        
        Object[] mocks = new Object[]{"mock1", null};
        mockitoCore.verifyNoMoreInteractions(mocks);
        
        verify(mockitoCore).getMockHandler("mock1");
        verify(mockitoCore).nullPassedToVerifyNoMoreInteractions();
    }
    
    @Test
    public void testInOrder() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        
        Object[] mocks = new Object[]{"mock1", "mock2"};
        InOrderImpl inOrder = mock(InOrderImpl.class);
        
        when(mockitoCore.inOrder(mocks)).thenReturn(inOrder);
        
        assertEquals(inOrder, mockitoCore.inOrder("mock1", "mock2"));
        
        verify(mockitoCore).getMockHandler(anyString());
        verify(mockitoCore).mocksHaveToBePassedWhenCreatingInOrder();
        verify(mockitoCore).nullPassedWhenCreatingInOrder();
        verify(mockitoCore).notAMockPassedWhenCreatingInOrder();
    }
    
    @Test
    public void testDoAnswer() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        
        Answer answer = mock(Answer.class);
        StubberImpl stubber = mock(StubberImpl.class);
        
        when(mockitoCore.doAnswer(answer)).thenReturn(stubber);
        
        assertEquals(stubber, mockitoCore.doAnswer(answer));
        
        verify(mockitoCore).stubbingStarted();
        verify(mockitoCore).resetOngoingStubbing();
    }
    
    @Test
    public void testStubVoid() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        
        String mock = "mock";
        MockHandlerInterface<String> handler = mock(MockHandlerInterface.class);
        
        when(mockitoCore.stubVoid(mock)).thenReturn(handler);
        
        assertEquals(handler, mockitoCore.stubVoid(mock));
        
        verify(mockitoCore).getMockHandler(mock);
        verify(mockitoCore).stubbingStarted();
    }
    
    @Test
    public void testValidateMockitoUsage() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        
        mockitoCore.validateMockitoUsage();
        
        verify(mockitoCore).validateState();
    }
    
    @Test
    public void testGetLastInvocation() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        
        Invocation invocation = mock(Invocation.class);
        OngoingStubbingImpl ongoingStubbing = mock(OngoingStubbingImpl.class);
        
        when(mockitoCore.getLastInvocation()).thenReturn(invocation);
        when(mockitoCore.stub()).thenReturn(ongoingStubbing);
        when(ongoingStubbing.getRegisteredInvocations()).thenReturn(Arrays.asList(invocation));
        
        assertEquals(invocation, mockitoCore.getLastInvocation());
        
        verify(mockitoCore).pullOngoingStubbing();
        verify(mockitoCore).reset();
        verify(mockitoCore).missingMethodInvocation();
    }
}
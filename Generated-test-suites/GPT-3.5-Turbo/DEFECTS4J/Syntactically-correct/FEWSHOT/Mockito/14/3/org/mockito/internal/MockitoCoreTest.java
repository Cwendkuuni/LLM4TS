package org.mockito.internal;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.MockSettings;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.DeprecatedOngoingStubbing;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.stubbing.Stubber;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationMode;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockitoCoreTest {
    @Test
    public void testMock(){
        MockitoCore core = new MockitoCore();
        MockSettings mockSettings = mock(MockSettings.class);
        
        // Test valid input
        assertNotNull(core.mock(String.class, mockSettings)); 
        
        // Test null input
        assertNull(core.mock(null, mockSettings));
    }
    
    @Test
    public void testStub(){
        MockitoCore core = new MockitoCore();
        
        // Test stubbingStarted null OngoingStubbing
        assertNull(core.stub());
        
        // Test stub(T)
        assertEquals(mock(DeprecatedOngoingStubbing.class), core.stub(null));
        
        // Test when(T)
        assertEquals(mock(OngoingStubbing.class), core.when(null));
    }
    
    @Test
    public void testVerify(){
        MockitoCore core = new MockitoCore();
        VerificationMode mode = mock(VerificationMode.class);
        
        // Test null input
        assertNull(core.verify(null, mode));
        
        // Test notAMock input
        assertNull(core.verify(new Object(), mode));
        
        // Test valid input
        String mockString = mock(String.class);
        assertEquals(mockString, core.verify(mockString, mode));
    }
    
    @Test
    public void testReset(){
        MockitoCore core = new MockitoCore();
        String[] mocks = {"mock1", "mock2"};
        
        // Test null input
        core.reset();
        
        // Test valid input
        core.reset(mocks);
    }
    
    @Test
    public void testVerifyNoMoreInteractions(){
        MockitoCore core = new MockitoCore();
        Object[] mocks = {"mock1", "mock2"};
        
        // Test mocks null or empty
        core.verifyNoMoreInteractions();
        
        // Test null mock
        core.verifyNoMoreInteractions(mocks);
        
        // Test valid input
        core.verifyNoMoreInteractions(mocks);
    }
    
    @Test
    public void testVerifyNoMoreInteractionsInOrder(){
        MockitoCore core = new MockitoCore();
        List<Object> mocks = mock(List.class);
        InOrderContext inOrderContext = mock(InOrderContext.class);
        
        core.verifyNoMoreInteractionsInOrder(mocks, inOrderContext);
    }
    
    @Test
    public void testInOrder(){
        MockitoCore core = new MockitoCore();
        Object[] mocks = {"mock1", "mock2"};
        
        // Test mocks null or empty
        core.inOrder();
        
        // Test null mock
        core.inOrder(null);
        
        // Test notAMock mock
        core.inOrder(new Object());
        
        // Test valid input
        assertNotNull(core.inOrder(mocks));
    }
    
    @Test
    public void testDoAnswer(){
        MockitoCore core = new MockitoCore();
        Answer answer = mock(Answer.class);
        
        // Test valid input
        assertNotNull(core.doAnswer(answer));
    }
    
    @Test
    public void testStubVoid(){
        MockitoCore core = new MockitoCore();
        String mockString = mock(String.class);
        
        // Test valid input
        assertNotNull(core.stubVoid(mockString));  
    }
    
    @Test
    public void testValidateMockitoUsage(){
        MockitoCore core = new MockitoCore();
        
        // Test valid input
        core.validateMockitoUsage();
    }
    
    @Test
    public void testGetLastInvocation(){
        MockitoCore core = new MockitoCore();
        
        // Test valid input
        Invocation lastInvocation = core.getLastInvocation();
    }
}
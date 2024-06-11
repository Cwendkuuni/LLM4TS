package org.mockito.internal;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class MockitoCoreTest {
    @Test
    public void testMock() {
        MockitoCore mockito = new MockitoCore();
        
        // Test case 1: mock a class with the provided classToMock and mockSettings
        ClassToMock classToMock = mockito.mock(ClassToMock.class, new MockSettings(), true);
        assertNotNull(classToMock);
        
        // Test case 2: mock a class with the provided classToMock only
        classToMock = mockito.mock(ClassToMock.class, new MockSettings());
        assertNotNull(classToMock);
    }

    @Test
    public void testStub() {
        MockitoCore mockito = new MockitoCore();
        
        // Test case 1: stubbing started and IOngoingStubbing is returned
        IOngoingStubbing stubbing = mockito.stub();
        assertNotNull(stubbing);
        
        // Test case 2: deprecated version of stub method
        DeprecatedOngoingStubbing<String> deprecatedStubbing = mockito.stub("methodCall");
        assertNotNull(deprecatedStubbing);
        
        // Test case 3: stubbing started and OngoingStubbing is returned
        OngoingStubbing<String> ongoingStubbing = mockito.when("methodCall");
        assertNotNull(ongoingStubbing);
    }
    
    @Test
    public void testVerify() {
        MockitoCore mockito = new MockitoCore();
        ClassToMock mock = mock(ClassToMock.class);
        
        // Test case 1: verify with a valid mock and mode
        ClassToMock verifiedMock = mockito.verify(mock, VerificationMode.mode);
        assertEquals(mock, verifiedMock);
        
        // Test case 2: verify with null mock
        try {
            mockito.verify(null, VerificationMode.mode);
            fail("Exception expected");
        } catch (NullPointerException e) {
            // expected
        }
        
        // Test case 3: verify with non-mock object
        try {
            mockito.verify(new Object(), VerificationMode.mode);
            fail("Exception expected");
        } catch (NotAMockException e) {
            // expected
        }
    }
    
    @Test
    public void testReset() {
        MockitoCore mockito = new MockitoCore();
        
        // Prepare mock objects
        ClassToMock mock1 = mock(ClassToMock.class);
        ClassToMock mock2 = mock(ClassToMock.class);
        
        // Test case 1: reset with provided mocks
        mockito.reset(mock1, mock2);
        
        // Test case 2: reset with empty mocks
        mockito.reset();
    }
    
    @Test
    public void testVerifyNoMoreInteractions() {
        MockitoCore mockito = new MockitoCore();
        ClassToMock mock = mock(ClassToMock.class);
        
        // Test case 1: verify no more interactions with a valid mock
        mockito.verifyNoMoreInteractions(mock);
        
        // Test case 2: verify no more interactions with null mock
        try {
            mockito.verifyNoMoreInteractions(null);
            fail("Exception expected");
        } catch (NullPointerException e) {
            // expected
        }
        
        // Test case 3: verify no more interactions with non-mock object
        try {
            mockito.verifyNoMoreInteractions(new Object());
            fail("Exception expected");
        } catch (NotAMockException e) {
            // expected
        }
    }
    
    @Test
    public void testInOrder() {
        MockitoCore mockito = new MockitoCore();
        ClassToMock mock1 = mock(ClassToMock.class);
        ClassToMock mock2 = mock(ClassToMock.class);
        
        // Test case 1: in order with valid mocks
        InOrder inOrder = mockito.inOrder(mock1, mock2);
        assertNotNull(inOrder);
        
        // Test case 2: in order with null mocks
        try {
            mockito.inOrder(null, null);
            fail("Exception expected");
        } catch (NullPointerException e) {
            // expected
        }
        
        // Test case 3: in order with non-mock object
        try {
            mockito.inOrder(new Object(), new Object());
            fail("Exception expected");
        } catch (NotAMockException e) {
            // expected
        }
    }

    @Test
    public void testDoAnswer() {
        MockitoCore mockito = new MockitoCore();
        
        // Test case: stubbing started and Stubber is returned
        Stubber stubber = mockito.doAnswer(answer);
        assertNotNull(stubber);
    }
    
    @Test
    public void testStubVoid() {
        MockitoCore mockito = new MockitoCore();
        ClassToMock mock = mock(ClassToMock.class);
        
        // Test case: stubbing started and VoidMethodStubbable is returned
        VoidMethodStubbable<ClassToMock> stubbable = mockito.stubVoid(mock);
        assertNotNull(stubbable);
    }
    
    @Test
    public void testValidateMockitoUsage() {
        MockitoCore mockito = new MockitoCore();
        
        // Test case: validate mockito usage
        mockito.validateMockitoUsage();
    }
    
    @Test
    public void testGetLastInvocation() {
        MockitoCore mockito = new MockitoCore();
        
        // Test case: get last invocation
        Invocation invocation = mockito.getLastInvocation();
        assertNull(invocation);
    }
    
    private static class ClassToMock {}
}
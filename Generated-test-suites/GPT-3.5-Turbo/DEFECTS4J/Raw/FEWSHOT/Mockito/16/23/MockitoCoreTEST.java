package org.mockito.internal;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class MockitoCoreTest {
    
    @Mock
    private Object mockObject;
    
    private MockitoCore mockitoCore;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockitoCore = new MockitoCore();
    }
    
    @Test
    public void testMock() {
        Object mock = mockitoCore.mock(Object.class);
        assertNotNull(mock);
    }
    
    @Test
    public void testStub() {
        IOngoingStubbing stubbing = mockitoCore.stub();
        assertNull(stubbing);
    }
    
    @Test
    public void testWhen() {
        OngoingStubbing stubbing = mockitoCore.when(mockObject);
        assertNull(stubbing);
    }
    
    @Test
    public void testVerify() {
        Object result = mockitoCore.verify(mockObject, null);
        assertSame(mockObject, result);
    }
    
    @Test
    public void testReset() {
        mockitoCore.reset(mockObject);
        assertTrue(true); // Just to indicate that the method does not throw an exception
    }
    
    @Test
    public void testVerifyNoMoreInteractions() {
        mockitoCore.verifyNoMoreInteractions(mockObject);
        assertTrue(true); // Just to indicate that the method does not throw an exception
    }
    
    @Test
    public void testInOrder() {
        InOrder inOrder = mockitoCore.inOrder(mockObject);
        assertNotNull(inOrder);
    }
    
    @Test
    public void testDoAnswer() {
        Stubber stubber = mockitoCore.doAnswer(null);
        assertNotNull(stubber);
    }
    
    @Test
    public void testStubVoid() {
        VoidMethodStubbable<Void> stubbable = mockitoCore.stubVoid(mockObject);
        assertNotNull(stubbable);
    }
    
    @Test
    public void testValidateMockitoUsage() {
        mockitoCore.validateMockitoUsage();
        assertTrue(true); // Just to indicate that the method does not throw an exception
    }
    
    @Test
    public void testGetLastInvocation() {
        Invocation invocation = mockitoCore.getLastInvocation();
        assertNull(invocation);
    }
}
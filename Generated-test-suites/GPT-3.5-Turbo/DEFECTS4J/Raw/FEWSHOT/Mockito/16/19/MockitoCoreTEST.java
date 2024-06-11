package org.mockito.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.stubbing.IOngoingStubbing;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.stubbing.Stubber;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationMode;

public class MockitoCoreTest {
    @Test
    public void testMock() {
        Class<String> classToMock = String.class;
        MockitoCore mockitoCore = new MockitoCore();

        Object result = mockitoCore.mock(classToMock, null);
        
        assertEquals(classToMock, result.getClass());
    }

    @Test
    public void testStub() {
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.stub();
        assertNull(mockitoCore.stub());

        MockitoCore spyMockitoCore = mock(MockitoCore.class);
        when(spyMockitoCore.stub()).thenReturn(null);

        assertNull(spyMockitoCore.stub());
    }

    @Test
    public void testWhen() {
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.when(null);
        assertNull(mockitoCore.when(null));
        
        MockitoCore spyMockitoCore = mock(MockitoCore.class);
        when(spyMockitoCore.when(null)).thenReturn(null);

        assertNull(spyMockitoCore.when(null));
    }

    @Test
    public void testVerify() {
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.verify(null, null);
        verify(mockitoCore).verify(null, null);
        
        MockitoCore spyMockitoCore = mock(MockitoCore.class);
        when(spyMockitoCore.verify(null, null)).thenReturn(null);

        assertNull(spyMockitoCore.verify(null, null));
    }

    @Test
    public void testReset() {
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.reset(null);
        verify(mockitoCore).reset(null);
        
        MockitoCore spyMockitoCore = mock(MockitoCore.class);
        when(spyMockitoCore.reset(null)).thenReturn(null);

        assertNull(spyMockitoCore.reset(null));
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.verifyNoMoreInteractions(null);
        verify(mockitoCore).verifyNoMoreInteractions(null);
        
        MockitoCore spyMockitoCore = mock(MockitoCore.class);
        when(spyMockitoCore.verifyNoMoreInteractions(null)).thenReturn(null);

        assertNull(spyMockitoCore.verifyNoMoreInteractions(null));
    }

    @Test
    public void testInOrder() {
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.inOrder(null);
        verify(mockitoCore).inOrder(null);
        
        MockitoCore spyMockitoCore = mock(MockitoCore.class);
        when(spyMockitoCore.inOrder(null)).thenReturn(null);

        assertNull(spyMockitoCore.inOrder(null));
    }

    @Test
    public void testDoAnswer() {
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.doAnswer(null);
        verify(mockitoCore).doAnswer(null);
        
        MockitoCore spyMockitoCore = mock(MockitoCore.class);
        when(spyMockitoCore.doAnswer(null)).thenReturn(null);

        assertNull(spyMockitoCore.doAnswer(null));
    }

    @Test
    public void testStubVoid() {
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.stubVoid(null);
        verify(mockitoCore).stubVoid(null);
        
        MockitoCore spyMockitoCore = mock(MockitoCore.class);
        when(spyMockitoCore.stubVoid(null)).thenReturn(null);

        assertNull(spyMockitoCore.stubVoid(null));
    }

    @Test
    public void testValidateMockitoUsage() {
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.validateMockitoUsage();
        verify(mockitoCore).validateMockitoUsage();
        
        MockitoCore spyMockitoCore = mock(MockitoCore.class);
        when(spyMockitoCore.validateMockitoUsage()).thenReturn(null);

        assertNull(spyMockitoCore.validateMockitoUsage());
    }

    @Test
    public void testGetLastInvocation() {
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.getLastInvocation();
        verify(mockitoCore).getLastInvocation();
        
        MockitoCore spyMockitoCore = mock(MockitoCore.class);
        when(spyMockitoCore.getLastInvocation()).thenReturn(null);

        assertNull(spyMockitoCore.getLastInvocation());
    }
}
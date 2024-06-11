
package org.mockito.internal;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class MockitoCoreTest {

    @Test
    public void testMock() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        assertNotNull(mockitoCore);
    }

    @Test
    public void testVerify() {
        MockitoCore mockitoCore = mock(MockitoCore.class);

        MockitoCore mock = mock(MockitoCore.class);
        MockitoCore verify = mockitoCore.verify(mock, times(1));
        assertNotNull(verify);
    }

    @Test
    public void testReset() {
        MockitoCore mockitoCore = mock(MockitoCore.class);

        MockitoCore mock = mock(MockitoCore.class);
        mockitoCore.reset(mock);
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        MockitoCore mockitoCore = mock(MockitoCore.class);

        MockitoCore mock = mock(MockitoCore.class);
        mockitoCore.verifyNoMoreInteractions(mock);
    }

    @Test
    public void testInOrder() {
        MockitoCore mockitoCore = mock(MockitoCore.class);

        MockitoCore mock1 = mock(MockitoCore.class);
        MockitoCore mock2 = mock(MockitoCore.class);

        InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
        assertNotNull(inOrder);
    }

    @Test
    public void testDoAnswer() {
        MockitoCore mockitoCore = mock(MockitoCore.class);

        Answer answer = mock(Answer.class);
        Stubber stubber = mockitoCore.doAnswer(answer);
        assertNotNull(stubber);
    }

    @Test
    public void testStubVoid() {
        MockitoCore mockitoCore = mock(MockitoCore.class);

        MockitoCore mock = mock(MockitoCore.class);
        VoidMethodStubbable stubbable = mockitoCore.stubVoid(mock);
        assertNotNull(stubbable);
    }

    @Test
    public void testValidateMockitoUsage() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        mockitoCore.validateMockitoUsage();
    }

    @Test
    public void testGetLastInvocation() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        Invocation invocation = mockitoCore.getLastInvocation();
        assertNull(invocation);
    }

}

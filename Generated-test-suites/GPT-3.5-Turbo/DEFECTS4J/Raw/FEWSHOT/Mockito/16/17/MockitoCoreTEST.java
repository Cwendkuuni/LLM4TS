package org.mockito.internal;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;

public class MockitoCoreTest {
    @Mock
    private OngoingStubbing ongoingStubbing;

    @Test
    public void testMock() {
        MockitoCore mockitoCore = new MockitoCore();
        Object result = mockitoCore.mock(Object.class, null, false);

        assertNotNull(result);
    }

    @Test
    public void testStub() {
        MockitoCore mockitoCore = new MockitoCore();
        OngoingStubbing result = mockitoCore.stub();

        assertNull(result);
    }

    @Test
    public void testDeprecatedStub() {
        MockitoCore mockitoCore = new MockitoCore();
        DeprecatedOngoingStubbing result = mockitoCore.stub(null);

        assertNull(result);
    }

    @Test
    public void testWhen() {
        MockitoCore mockitoCore = new MockitoCore();
        OngoingStubbing result = mockitoCore.when(null);

        assertNull(result);
    }

    @Test
    public void testVerify() {
        MockitoCore mockitoCore = new MockitoCore();
        Object result = mockitoCore.verify(null, null);

        assertNull(result);
    }

    @Test
    public void testReset() {
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.reset(null);
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.verifyNoMoreInteractions(null);
    }

    @Test
    public void testInOrder() {
        MockitoCore mockitoCore = new MockitoCore();
        InOrder result = mockitoCore.inOrder(null);

        assertNull(result);
    }

    @Test
    public void testDoAnswer() {
        MockitoCore mockitoCore = new MockitoCore();
        Stubber result = mockitoCore.doAnswer(null);

        assertNotNull(result);
    }

    @Test
    public void testStubVoid() {
        MockitoCore mockitoCore = new MockitoCore();
        VoidMethodStubbable result = mockitoCore.stubVoid(null);

        assertNotNull(result);
    }

    @Test
    public void testValidateMockitoUsage() {
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.validateMockitoUsage();
    }

    @Test
    public void testGetLastInvocation() {
        MockitoCore mockitoCore = new MockitoCore();
        Invocation result = mockitoCore.getLastInvocation();

        assertNull(result);
    }
}
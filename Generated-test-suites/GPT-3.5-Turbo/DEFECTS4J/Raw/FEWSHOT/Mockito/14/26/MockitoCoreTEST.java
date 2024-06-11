package org.mockito.internal;

import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockSettings;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.DeprecatedOngoingStubbing;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.stubbing.Stubber;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationMode;

import java.util.Arrays;
import java.util.List;

public class MockitoCoreTest {

    @Test
    public void testMock() {
        Class<T> classToMock = T.class;
        MockSettings mockSettings = new MockSettingsImpl();

        MockitoCore mockitoCore = new MockitoCore();
        T mock = mockitoCore.mock(classToMock, mockSettings);

        assertNotNull(mock);
        assertTrue(mockUtil.isMock(mock));
    }

    @Test
    public void testStub() {
        OngoingStubbing stubbing = new OngoingStubbingImpl();

        MockitoCore mockitoCore = new MockitoCore();
        IOngoingStubbing stub = mockitoCore.stub();

        assertNotNull(stub);
        assertEquals(stubbing, stub);
    }

    @Test
    public void testStubWithMethodCall() {
        T methodCall = Mockito.mock(T.class);

        MockitoCore mockitoCore = new MockitoCore();
        DeprecatedOngoingStubbing<T> stub = mockitoCore.stub(methodCall);

        assertNotNull(stub);
    }

    @Test
    public void testWhen() {
        T methodCall = Mockito.mock(T.class);

        MockitoCore mockitoCore = new MockitoCore();
        OngoingStubbing<T> stub = mockitoCore.when(methodCall);

        assertNotNull(stub);
    }

    @Test
    public void testVerify() {
        T mock = Mockito.mock(T.class);
        VerificationMode mode = Mockito.verifyNoMoreInteractions();

        MockitoCore mockitoCore = new MockitoCore();
        T verifiedMock = mockitoCore.verify(mock, mode);

        assertNotNull(verifiedMock);
    }

    @Test
    public void testReset() {
        T[] mocks = {Mockito.mock(T.class), Mockito.mock(T.class)};

        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.reset(mocks);

        for (T mock : mocks) {
            assertNotNull(mock);
            verify(mock).reset();
        }
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        Object[] mocks = {Mockito.mock(T.class), Mockito.mock(T.class)};

        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.verifyNoMoreInteractions(mocks);

        for (Object mock : mocks) {
            assertNotNull(mock);
            verify(mock).verifyNoMoreInteractions();
        }
    }

    @Test
    public void testVerifyNoMoreInteractionsInOrder() {
        List<Object> mocks = Arrays.asList(Mockito.mock(T.class), Mockito.mock(T.class));
        InOrderContext inOrderContext = Mockito.inOrder(mocks);

        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.verifyNoMoreInteractionsInOrder(mocks, inOrderContext);

        for (Object mock : mocks) {
            assertNotNull(mock);
            verify(mock).verifyNoMoreInteractionsInOrder();
        }
    }

    @Test
    public void testInOrder() {
        Object[] mocks = {Mockito.mock(T.class), Mockito.mock(T.class)};

        MockitoCore mockitoCore = new MockitoCore();
        InOrder inOrder = mockitoCore.inOrder(mocks);

        assertNotNull(inOrder);
    }

    @Test
    public void testDoAnswer() {
        Answer answer = Mockito.mock(Answer.class);

        MockitoCore mockitoCore = new MockitoCore();
        Stubber stubber = mockitoCore.doAnswer(answer);

        assertNotNull(stubber);
    }

    @Test
    public void testStubVoid() {
        T mock = Mockito.mock(T.class);

        MockitoCore mockitoCore = new MockitoCore();
        VoidMethodStubbable<T> stub = mockitoCore.stubVoid(mock);

        assertNotNull(stub);
    }

    @Test
    public void testValidateMockitoUsage() {
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.validateMockitoUsage();

        // No assertion, just testing if the method runs without errors
    }

    @Test
    public void testGetLastInvocation() {
        Invocation invocation = Mockito.mock(Invocation.class);

        OngoingStubbing ongoingStubbing = new OngoingStubbingImpl();
        ongoingStubbing.registerInvocation(invocation);

        MockitoCore mockitoCore = new MockitoCore();
        Invocation lastInvocation = mockitoCore.getLastInvocation();

        assertNotNull(lastInvocation);
        assertEquals(invocation, lastInvocation);
    }
}
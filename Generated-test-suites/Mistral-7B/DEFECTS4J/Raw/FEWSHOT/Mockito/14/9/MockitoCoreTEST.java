package org.mockito.internal;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.InvocationOnMock;
import org.mockito.MockSettings;
import org.mockito.Mockito;
import org.mockito.MockSettingsImpl;
import org.mockito.internal.creation.MockSettingsImplTestSpy;
import org.mockito.internal.invocation.MockInvocation;
import org.mockito.internal.invocation.VerificationDataImpl;
import org.mockito.internal.invocation.VerificationModeFactory;
import org.mockito.internal.progress.IOngoingStubbing;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubberImpl;
import org.mockito.internal.util.MockUtil;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.DeprecatedOngoingStubbing;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.stubbing.Stubber;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationData;
import org.mockito.verification.VerificationMode;

public class MockitoCoreTest {

    @Test
    public void testMock() {
        Class<MockSettingsImplTestSpy> mockClass = MockSettingsImplTestSpy.class;
        MockSettings mockSettings = mock(MockSettings.class);
        MockSettingsImplTestSpy mock = new MockSettingsImplTestSpy();

        MockitoCore mockitoCore = new MockitoCore();
        T mockedMock = mockitoCore.mock(mockClass, mockSettings);
        verify(mockitoCore).mock(eq(mockClass), eq(mockSettings));
        assertSame(mock, mockedMock);
    }

    @Test
    public void testStub() {
        MockitoCore mockitoCore = new MockitoCore();
        IOngoingStubbing stubbing = mockitoCore.stub();
        verify(mockitoCore).stub();
        assertNotNull(stubbing);
    }

    @Test
    public void testDeprecatedStub() {
        MockitoCore mockitoCore = new MockitoCore();
        DeprecatedOngoingStubbing<Object> stubbing = mockitoCore.stub(new Object());
        verify(mockitoCore).stub(any());
        assertNotNull(stubbing);
    }

    @Test
    public void testWhen() {
        Object mock = mock(Object.class);
        MockitoCore mockitoCore = new MockitoCore();
        OngoingStubbing<Void> stubbing = mockitoCore.when(mock);
        verify(mockitoCore).when(eq(mock));
        assertNotNull(stubbing);
    }

    @Test
    public void testVerify() {
        Object mock = mock(Object.class);
        MockitoCore mockitoCore = new MockitoCore();
        VerificationData verificationData = mockitoCore.verify(mock, eq(VerificationMode.times(1)));
        verify(mockitoCore).verify(eq(mock), eq(VerificationMode.times(1)));
        assertNotNull(verificationData);
    }

    @Test
    public void testReset() {
        Object mock = mock(Object.class);
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.reset(mock);
        verify(mockitoCore).reset(eq(mock));
        MockHandlerInterface handler = Mockito.mock(MockHandlerInterface.class);
        handler.reset();
        verify(handler).reset();
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        Object mock = mock(Object.class);
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.verifyNoMoreInteractions(mock);
        verify(mockitoCore).verifyNoMoreInteractions(eq(mock));
    }

    @Test
    public void testVerifyNoMoreInteractionsInOrder() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        InOrder inOrder = inOrder(mock1, mock2);
        MockitoCore mockitoCore = new MockitoCore();
        List<Object> mocks = Arrays.asList(mock1, mock2);
        mockitoCore.verifyNoMoreInteractionsInOrder(mocks, inOrder);
        verify(mockitoCore).verifyNoMoreInteractionsInOrder(eq(mocks), any(InOrder.class));
    }

    @Test
    public void testVerifyNoInteractions() {
        Object mock = mock(Object.class);
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.verifyNoInteractions(mock);
        verify(mockitoCore).verifyNoInteractions(eq(mock));
    }

    @Test
    public void testInOrder() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
        verify(mockitoCore).inOrder(eq(mock1), eq(mock2));
        assertNotNull(inOrder);
    }

    @Test
    public void testDoAnswer() {
        Object mock = mock(Object.class);
        MockitoCore mockitoCore = new MockitoCore();
        Answer<Integer> answer = new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                return 1;
            }
        };
        Stubber stubber = mockitoCore.doAnswer(answer);
        verify(mockitoCore).doAnswer(answer);
        assertNotNull(stubber);
    }

    @Test
    public void testStubVoid() {
        Object mock = mock(Object.class);
        MockitoCore mockitoCore = new MockitoCore();
        VoidMethodStubbable stubbable = mockitoCore.stubVoid(mock);
        verify(mockitoCore).stubVoid(eq(mock));
        assertNotNull(stubbable);
    }

    @Test
    public void testValidateMockitoUsage() {
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.validateMockitoUsage();
        verify(mockitoCore).validateMockitoUsage();
    }

    @Test(expected = AssertionError.class)
    public void testVerifyWithNullMock() {
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.verify(null, eq(VerificationMode.times(1)));
    }

    @Test(expected = AssertionError.class)
    public void testVerifyNoMoreInteractionsWithNullMock() {
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.verifyNoMoreInteractions(null);
    }

    @Test(expected = AssertionError.class)
    public void testVerifyNoMoreInteractionsInOrderWithNullMock() {
        MockitoCore mockitoCore = new MockitoCore();
        Object[] mocks = new Object[0];
        mockitoCore.verifyNoMoreInteractionsInOrder(Arrays.asList(mocks), any(InOrder.class));
    }

    @Test(expected = AssertionError.class)
    public void testVerifyNoInteractionsWithNullMock() {
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.verifyNoInteractions(null);
    }

    @Test(expected = AssertionError.class)
    public void testVerifyNoInteractionsInOrderWithNullMock() {
        MockitoCore mockitoCore = new MockitoCore();
        Object[] mocks = new Object[0];
        mockitoCore.verifyNoInteractionsInOrder(Arrays.asList(mocks), any(InOrder.class));
    }

    @Test
    public void testGetLastInvocation() {
        Object mock = mock(Object.class);
        MockitoCore mockitoCore = new MockitoCore();
        when(mock).thenCallRealMethod();
        doCallRealMethod().when(mock);
        mockitoCore.verify(mock).someVoidMethod();
        Invocation invocation = mockitoCore.getLastInvocation();
        assertNotNull(invocation);
        verify(mockitoCore).getLastInvocation();
    }
}
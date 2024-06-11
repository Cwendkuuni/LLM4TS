package org.mockito.internal;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.MockSettingsImpl;
import org.mockito.internal.progress.IOngoingStubbing;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubberImpl;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.stubbing.Stub;
import org.mockito.verification.api.VerificationMode;

import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MockitoCoreTest {

    @Mock private MockHandlerInterface<Object> mockHandler;
    @Mock private Object mock;
    @Mock private Object anotherMock;

    private MockSettings mockSettings = new MockSettingsImpl();
    private IOngoingStubbing ongoingStubbing;
    private MockingProgress mockingProgress = new ThreadSafeMockingProgress();

    @Test
    public void testMock() {
        Object mockedObject = mockitoCore.mock(Object.class, mockSettings);
        assertSame(mockHandler, mockitoCore.getMockHandler(mockedObject));
        verify(mockHandler).createMock(Object.class, eq(mockSettings));
    }

    @Test
    public void testMockWithSettings() {
        Object mockedObject = mockitoCore.mock(Object.class, mockSettings);
        assertSame(mockHandler, mockitoCore.getMockHandler(mockedObject));
        verify(mockHandler).createMock(Object.class, eq(mockSettings));

        verify(mockHandler).validateMockSettings(eq(mockSettings));
    }

    @Test
    public void testMockWithResetOngoingStubbingTrue() {
        Object mockedObject = mockitoCore.mock(Object.class, mockSettings, true);
        assertSame(mockHandler, mockitoCore.getMockHandler(mockedObject));
        verify(mockHandler).createMock(Object.class, eq(mockSettings));
        verify(mockHandler).reset();
    }

    @Test
    public void testMockWithResetOngoingStubbingFalse() {
        Object mockedObject = mockitoCore.mock(Object.class, mockSettings, false);
        assertSame(mockHandler, mockitoCore.getMockHandler(mockedObject));
        verify(mockHandler).createMock(Object.class, eq(mockSettings));
        verifyNoInteractions(mockHandler);
    }

    @Test
    public void testStub() {
        ongoingStubbing = mockitoCore.stub();
        stub(mock, any(Integer.class)).returns(7);
        verify(mockHandler).registerStubbing(eq(mock), any(Stub.class));
        verify(mock).equals(mock);
        verifyZeroInteractions(mock);
        Integer returnValue = (Integer) mockitoCore.getLastInvocation().getArgument(0);
        assertEquals(7, returnValue.intValue());
    }

    @Test
    public void testStubWithRegisteredStub() {
        ongoingStubbing = mockitoCore.stub();
        Stub<Object> stub = stub(mock, any(Integer.class));
        stub.thenReturn(7);
        verify(mockHandler).registerStubbing(eq(mock), eq(stub));
        verify(mock).equals(mock);
        verifyZeroInteractions(mock);
        Integer returnValue = (Integer) mockitoCore.getLastInvocation().getArgument(0);
        assertEquals(7, returnValue.intValue());
    }

    @Test
    public void testWhen() {
        ongoingStubbing = mockitoCore.stub();
        when(mock).thenReturn(7);
        verify(mockHandler).registerStubbing(eq(mock), eq(new OngoingStubbingImpl()));
        verify(mock).equals(mock);
        verifyZeroInteractions(mock);
        Integer returnValue = (Integer) mockitoCore.getLastInvocation().getArgument(0);
        assertEquals(7, returnValue.intValue());
    }

    @Test
    public void testWhenWithRegisteredStub() {
        ongoingStubbing = mockitoCore.stub();
        when(mock).thenReturn(7);
        verify(mockHandler).registerStubbing(eq(mock), eq(new OngoingStubbingImpl()));
        verify(mock).equals(mock);
        verifyZeroInteractions(mock);
        Integer returnValue = (Integer) mockitoCore.getLastInvocation().getArgument(0);
        assertEquals(7, returnValue.intValue());

        when(mock).thenReturn(14);
        Integer returnValue2 = (Integer) mockitoCore.getLastInvocation().getArgument(0);
        assertEquals(14, returnValue2.intValue());
    }

    @Test
    public void testVerify() {
        mockitoCore.verify(mock, eq(VerificationMode.RETURN_MATCHES));
        verify(mockHandler).verify(eq(mock), eq(VerificationMode.RETURN_MATCHES));
        verifyNoInteractions(mock);
    }

    @Test(expected = AssertionError.class)
    public void testVerifyOnNullMock() {
        mockitoCore.verify(null, eq(VerificationMode.RETURN_MATCHES));
    }

    @Test(expected = AssertionError.class)
    public void testVerifyOnNonMockObject() {
        mockitoCore.verify(new Object(), eq(VerificationMode.RETURN_MATCHES));
    }

    @Test
    public void testVerifyNoInteractions() {
        mockitoCore.verifyNoInteractions(mock);
        verifyNoInteractions(mockHandler);
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        ArgumentCaptor<Object> argumentCaptor = ArgumentCaptor.forClass(Object.class);
        when(mock).thenReturn(7);
        when(mock).thenReturn(14);
        mockitoCore.verifyNoMoreInteractions(mock);
        verifyNoMoreInteractions(mockHandler);
        verify(mockHandler).verifyNoMoreInteractions();
        verify(mockHandler).getMockInvocations().isEmpty();
    }

    @Test
    public void testReset() {
        when(mock).thenReturn(7);
        mockitoCore.reset(mock);
        verifyZeroInteractions(mock);
        verifyNoInteractions(mockHandler);
    }

    @Test
    public void testResetWithMultipleMocks() {
        when(mock).thenReturn(7);
        when(anotherMock).thenReturn(14);
        mockitoCore.reset(mock, anotherMock);
        verifyZeroInteractions(mock);
        verifyZeroInteractions(anotherMock);
        verifyNoInteractions(mockHandler);
    }

    @Test
    public void testVerifyOrder() {
        when(mock).thenReturn(7);
        when(mock).thenReturn(14);
        InOrder inOrder = mockitoCore.inOrder(mock);

        inOrder.verify(mock).method1();
        inOrder.verify(mock).method2();

        verify(mockHandler).verify(eq(mock), eq(VerificationMode.IN_ORDER));
    }

    @Test(expected = AssertionError.class)
    public void testVerifyOrderNullMock() {
        InOrder inOrder = mockitoCore.inOrder(null);
        inOrder.verify(mock).method1();
    }

    @Test(expected = AssertionError.class)
    public void testVerifyOrderNonMockObject() {
        InOrder inOrder = mockitoCore.inOrder(new Object());
        inOrder.verify(mock).method1();
    }

    @Test
    public void testDoAnswer() {
        Answer<Integer> answer = invocation -> 14;
        Stubber stubber = mockitoCore.doAnswer(answer);
        when(mock).thenAnswer(answer);

        assertEquals(14, mockitoCore.getLastInvocation().getReturnValue());
        verify(mockHandler).verifyAnswer(eq(mock), eq(answer));
    }

    @Test
    public void testVoidMethodStubbable() {
        when(mock.voidMethod()).thenCallRealMethod();
        mockitoCore.stubVoid(mock);

        verify(mockHandler).voidMethodStubbable(mock);
        verifyZeroInteractions(mock);
        mock.voidMethod();
    }

    @Test
    public void testValidateMockitoUsage() {
        mockitoCore.validateMockitoUsage();
        verify(mockHandler).validateState();
    }

}
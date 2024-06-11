package org.mockito.internal;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class MockitoCoreTest {

    @Test
    public void testMock() {
        MockSettings mockSettings = new MockSettingsImpl();
        Object mock = MockitoCore.mock(Mockito.class, mockSettings);
        assertNull(MockitoCore.getLastInvocation());
        assertSame(Mockito.class, mock.getClass());
    }

    @Test
    public void testMockWithSettings() {
        MockSettings mockSettings = new MockSettingsImpl();
        Object mock = MockitoCore.mock(Mockito.class, mockSettings);
        assertNull(MockitoCore.getLastInvocation());
    }

    @Test
    public void testStub() {
        Object mock = mock(Object.class);
        Object result = MockitoCore.stub().thenReturn("result");
        when(mock).thenReturn(result);
        assertSame("result", Mockito.invokeMethod(mock, "toString"));
    }

    @Test
    public void testVerify() {
        Object mock = mock(Object.class);
        verify(mock).toString();
    }

    @Test(expected = AssertionError.class)
    public void testVerifyNull() {
        verify(null);
    }

    @Test(expected = AssertionError.class)
    public void testVerifyNotAMock() {
        Object notAMock = new Object();
        verify(notAMock);
    }

    @Test
    public void testReset() {
        Object mock = mock(Object.class);
        MockitoCore.reset(mock);
        assertNull(MockitoCore.getLastInvocation());
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        Object mock = mock(Object.class);
        verifyNoMoreInteractions(mock);
    }

    @Test(expected = AssertionError.class)
    public void testVerifyNoMoreInteractionsNull() {
        verifyNoMoreInteractions(null);
    }

    @Test(expected = AssertionError.class)
    public void testVerifyNoMoreInteractionsNotAMock() {
        Object notAMock = new Object();
        verifyNoMoreInteractions(notAMock);
    }

    @Test
    public void testInOrder() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        InOrder inOrder = MockitoCore.inOrder(mock1, mock2);
        inOrder.verify(mock1).toString();
        inOrder.verify(mock2).toString();
    }

    @Test(expected = AssertionError.class)
    public void testInOrderNull() {
        InOrder inOrder = MockitoCore.inOrder(null);
    }

    @Test(expected = AssertionError.class)
    public void testInOrderNotAMock() {
        Object notAMock = new Object();
        InOrder inOrder = MockitoCore.inOrder(notAMock);
    }

    @Test
    public void testDoAnswer() {
        Object mock = mock(Object.class);
        when(mock).thenAnswer(invocation -> invocation.getArgument(0).toString());
        assertSame("mock", Mockito.invokeMethod(mock, "toString"));
    }

    @Test
    public void testStubVoid() {
        Object mock = mock(Object.class);
        MockitoCore.stubVoid(mock).andReturn("result");
        verify(mock).voidMethod();
        Mockito.verifyNoMoreInteractions(mock);
        assertSame("result", mock);
    }

    @Test
    public void testValidateMockitoUsage() {
        MockitoCore validateMockitoUsage = new MockitoCore();
        validateMockitoUsage.validateMockitoUsage();
    }

    @Test
    public void testGetLastInvocation() {
        Object mock = mock(Object.class);
        Object result = Mockito.invokeMethod(mock, "toString");
        when(mock).thenReturn(result);
        Invocation lastInvocation = MockitoCore.getLastInvocation();
        assertSame("toString", lastInvocation.getMethod().getName());
        assertSame(mock, lastInvocation.getTarget());
    }

    @Test
    public void testGetLastInvocationWhenNoInvocation() {
        Object mock = mock(Object.class);
        assertNull(MockitoCore.getLastInvocation());
    }

    @Test
    public void testStubbingStartedAndReseted() {
        Object mock = mock(Object.class);
        IOngoingStubbing stubbing = MockitoCore.stub();
        stubbing.thenReturn("result1");
        assertSame("result1", Mockito.invokeMethod(mock, "toString"));

        MockitoCore.reset();
        stubbing = MockitoCore.stub();
        stubbing.thenReturn("result2");
        assertSame("result2", Mockito.invokeMethod(mock, "toString"));
    }

    @Test
    public void testStubbingStartedAndResetedWithDeprecatedStub() {
        Object mock = mock(Object.class);
        IOngoingStubbing stubbing = MockitoCore.stub(mock);
        stubbing.thenReturn("result1");
        assertSame("result1", Mockito.invokeMethod(mock, "toString"));

        MockitoCore.reset();
        stubbing = MockitoCore.stub(mock);
        stubbing.thenReturn("result2");
        assertSame("result2", Mockito.invokeMethod(mock, "toString"));
    }

    @Test
    public void testStubbingStartedWithOngoingStubbing() {
        Object mock = mock(Object.class);
        IOngoingStubbing stubbing = MockitoCore.stub();
        stubbing.thenReturn("result1");
        when(mock).thenReturn(stubbing);
        assertSame("result1", Mockito.invokeMethod(mock, "toString"));
    }

    @Test
    public void testVerifyVerificationStarted() {
        Object mock = mock(Object.class);
        MockitoCore.verify(mock, Mockito.eq("toString"));
        MockitoCore.verify(mock, Mockito.times(2));
        MockitoCore.verify(mock, Mockito.never());
        MockitoCore.verify(mock, Mockito.atLeastOnce());
        MockitoCore.verify(mock, Mockito.atLeast(3));
        MockitoCore.verify(mock, Mockito.atMost(3));
        MockitoCore.verify(mock, Mockito.atMostOnce());
        MockitoCore.verify(mock, Mockito.eq(null));
    }
}
package org.mockito.internal;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Arrays;

import org.junit.Test;
import org.mockito.Answer;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.progress.IOngoingStubbing;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubberImpl;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.AnswerStub;
import org.mockito.stubbing.StubWith;

public class MockitoCoreTest {

    @Test
    public void testMock() {
        Class<Object> clazz = Object.class;
        MockSettings mockSettings = new MockSettingsImpl();
        Object mock = MockitoCore.mock(clazz, mockSettings);
        assertNull(MockitoCore.getLastInvocation());
        verify(mock).toString();
    }

    @Test
    public void testMockWithCustomSettings() {
        Class<Object> clazz = Object.class;
        MockSettings mockSettings = new MockSettingsImpl();
        Object mock = MockitoCore.mock(clazz, mockSettings);
        assertNull(MockitoCore.getLastInvocation());
        verify(mock).toString();

        mockSettings.customization().numberOfTimesToReturn(1);
        when(mock).thenReturn("result");
        verify(mock).toString();
        verify(mock).toString(); // should return "result"
    }

    @Test
    public void testStubbing() {
        Object mock = mock(Object.class);
        IOngoingStubbing<Object> stubbing = MockitoCore.stub();
        stubbing.thenReturn("result");
        assertSame("result", MockitoCore.verify(mock).toString());
    }

    @Test(expected = RuntimeException.class)
    public void testStubbingError() {
        Object mock = mock(Object.class);
        MockitoCore.stub(); // Should throw an error because no stubbing has been started
        MockitoCore.verify(mock).toString();
    }

    @Test
    public void testVerify() {
        Object mock = mock(Object.class);
        verify(mock).toString();
    }

    @Test(expected = AssertionError.class)
    public void testVerifyNull() {
        MockitoCore.verify(null, org.mockito.ArgumentMatchers.eq(""));
    }

    @Test(expected = AssertionError.class)
    public void testVerifyNotAMock() {
        Object notAMock = new Object() {};
        MockitoCore.verify(notAMock);
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
        when(mock).thenReturn("result");
        verifyNoMoreInteractions(mock);
        verify(mock).toString(); // Should throw an error
    }

    @Test(expected = AssertionError.class)
    public void testVerifyNoMoreInteractionsNull() {
        MockitoCore.verifyNoMoreInteractions(null);
    }

    @Test(expected = AssertionError.class)
    public void testVerifyNoMoreInteractionsNotAMock() {
        Object notAMock = new Object() {};
        MockitoCore.verifyNoMoreInteractions(notAMock);
    }

    @Test
    public void testInOrder() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        InOrder inOrder = MockitoCore.inOrder(mock1, mock2);
        inOrder.verify(mock1).toString();
        inOrder.verify(mock1).toString();
        inOrder.verify(mock2).toString();
    }

    @Test(expected = AssertionError.class)
    public void testInOrderNull() {
        InOrder inOrder = MockitoCore.inOrder(null);
    }

    @Test(expected = AssertionError.class)
    public void testInOrderNotAMock() {
        Object notAMock = new Object() {};
        InOrder inOrder = MockitoCore.inOrder(notAMock);
    }

    @Test
    public void testAnswer() {
        Object mock = mock(Object.class);
        Answer<String> answer = new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                return "answer";
            }
        };
        StubberImpl stubber = MockitoCore.doAnswer(answer);
        String result = (String) stubber.when(mock).toString();
        assertSame("answer", result);
    }

    @Test
    public void testVoidMethodStubbable() {
        Object mock = mock(Object.class);
        MockitoCore.stubVoid(mock).andReturn("result");
        verify(mock).toString();
        verify(mock).toString(); // should return "result"
    }

    @Test
    public void testStubbingProgressValidation() {
        MockitoCore.mock(Object.class, new MockSettingsImpl(), false);
        MockitoCore.stub();
    }

    @Test(expected = RuntimeException.class)
    public void testStubbingProgressValidationError() {
        MockitoCore.mock(Object.class, new MockSettingsImpl(), false);
        MockitoCore.stub();
        MockitoCore.stub(); // should throw an error
    }

    @Test
    public void testGetLastInvocation() {
        Object mock = mock(Object.class);
        IOngoingStubbing<Object> stubbing = MockitoCore.stub();
        stubbing.thenReturn("result");
        Invocation lastInvocation = MockitoCore.getLastInvocation();
        verify(mock).toString();
        assertSame("result", lastInvocation.getMethod().getReturnValue());
    }

    @Test
    public void testStubWith() {
        Object mock = mock(Object.class);
        StubWith<Object, Integer> stub = MockitoCore.stub(mock).toReturn(1);
        verify(mock).toString();
        verify(mock).toString(); // should return "1"
    }

    @Test
    public void testAnswerStub() {
        Object mock = mock(Object.class);
        Answer<?> answer = new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return "answer";
            }
        };
        IOngoingStubbing<Object> stubbing = MockitoCore.stub(mock);
        stubbing.thenAnswer(answer);
        Object result = MockitoCore.verify(mock).toString();
        assertSame("answer", result);
    }
}
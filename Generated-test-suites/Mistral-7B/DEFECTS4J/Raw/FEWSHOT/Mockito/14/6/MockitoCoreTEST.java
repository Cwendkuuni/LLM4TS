package org.mockito.internal;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class MockitoCoreTest {

    @Test
    public void testMock() {
        MyMock mock = mock(MyMock.class);
        assertNotNull(mock);
    }

    @Test
    public void testMockWithSettings() {
        MyMock mock = mock(MyMock.class, withSettings()->retries(5));
        assertNotNull(mock);
    }

    @Test
    public void testStub() {
        MyMock mock = mock(MyMock.class);
        when(mock.myMethod()).thenReturn("result");
        assertEquals("result", mock.myMethod());
    }

    @Test
    public void testStubMultipleCalls() {
        MyMock mock = mock(MyMock.class);
        when(mock.myMethod()).thenReturn("result1").thenReturn("result2");
        assertEquals("result1", mock.myMethod());
        assertEquals("result2", mock.myMethod());
    }

    @Test
    public void testStubWithArguments() {
        MyMock mock = mock(MyMock.class);
        when(mock.myMethod(eq(1), anyInt())).thenReturn(10);
        assertEquals(10, mock.myMethod(1, 2));
    }

    @Test
    public void testVerify() {
        MyMock mock = mock(MyMock.class);
        verify(mock);
    }

    @Test
    public void testVerifyWithMode() {
        MyMock mock = mock(MyMock.class);
        verify(mock).myMethod();
        verify(mock).myOtherMethod();
        verifyNoMoreInteractions(mock);
    }

    @Test
    public void testReset() {
        MyMock mock = mock(MyMock.class);
        when(mock.myMethod()).thenReturn(1);
        assertEquals(1, mock.myMethod());
        reset(mock);
        when(mock.myMethod()).thenReturn(2);
        assertEquals(2, mock.myMethod());
    }

    @Test
    public void testResetAll() {
        MyMock mock1 = mock(MyMock1.class);
        MyMock mock2 = mock(MyMock2.class);
        when(mock1.myMethod1()).thenReturn("result1");
        when(mock2.myMethod2()).thenReturn("result2");

        reset(mock1, mock2);

        assertFalse(mock1.myMethod1().wasCalleds());
        assertFalse(mock2.myMethod2().wasCalleds());
    }

    @Test(expected = AssertionError.class)
    public void testVerifyNoMoreInteractionsNull() {
        verifyNoMoreInteractions(null);
    }

    @Test(expected = NotAMockException.class)
    public void testVerifyNoMoreInteractionsNotAMock() {
        Object notAMock = new Object();
        verifyNoMoreInteractions(notAMock);
    }

    @Test(expected = AssertionError.class)
    public void testVerifyNoMoreInteractionsOrderNoMocks() {
        List<Object> mocks = Arrays.asList();
        InOrder inOrder = inOrder();
        verifyNoMoreInteractionsInOrder(mocks, inOrder);
    }

    @Test(expected = AssertionError.class)
    public void testVerifyNoMoreInteractionsOrderNullMock() {
        List<Object> mocks = Arrays.asList(null);
        InOrder inOrder = inOrder(mocks.toArray(new Object[0]));
        verifyNoMoreInteractionsInOrder(mocks, inOrder);
    }

    @Test(expected = AssertionError.class)
    public void testVerifyNoMoreInteractionsOrderNotAMock() {
        List<Object> mocks = Arrays.asList(new Object(), new NonMockObject());
        InOrder inOrder = inOrder(mocks.toArray(new Object[0]));
        verifyNoMoreInteractionsInOrder(mocks, inOrder);
    }

    @Test
    public void testInOrder() {
        MyMock mock1 = mock(MyMock.class);
        MyMock mock2 = mock(MyMock.class);

        InOrder inOrder = inOrder(mock1, mock2);
        inOrder.verify(mock1).myMethod1();
        inOrder.verify(mock2).myMethod2();

        verifyNoMoreInteractionsInOrder(Arrays.asList(mock1, mock2), inOrder);
    }

    @Test
    public void testDoAnswer() {
        MyMock mock = mock(MyMock.class);
        Answer<String> answer = invocation -> invocation.getArgument(0) + " World!";
        doAnswer(answer).when(mock).myMethod(anyString());
        assertEquals("Hello World!", mock.myMethod("Hello"));
    }

    @Test
    public void testStubVoid() {
        MyVoidMock mock = mock(MyVoidMock.class);
        doNothing().when(mock).myVoidMethod();
        mock.myVoidMethod();
        verifyNoMoreInteractions(mock);
    }

    @Test
    public void testValidateMockitoUsage() {
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.validateMockitoUsage();
    }

    @Test
    public void testGetLastInvocation() {
        MyMock mock = mock(MyMock.class);
        when(mock.myMethod()).thenReturn("result1").thenReturn("result2");
        Invocation lastInvocation = mockitoCore.getLastInvocation();
        assertEquals("result2", lastInvocation.getArgument(0));
        assertEquals("result2", lastInvocation.getMethod().getName());
    }

}

class MyMock {
    public int myMethod() {
        return 1;
    }
}

class MyMock1 {
    public void myMethod1() {
    }
}

class MyMock2 {
    public void myMethod2() {
    }
}

class NonMockObject {
}
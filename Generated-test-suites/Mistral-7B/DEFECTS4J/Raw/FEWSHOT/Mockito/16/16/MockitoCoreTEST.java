package org.mockito.internal;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class MockitoCoreTest {

    private MockitoCore mockitoCore;

    @Before
    public void setup() {
        mockitoCore = new MockitoCore();
    }

    @Test
    public void testMock() {
        MockSettings mockSettings = mock(MockSettings.class);
        Class<String> classToMock = String.class;
        String mock = mockitoCore.mock(classToMock, mockSettings);
        assertEquals(mock, mock);
    }

    @Test(expected = AssertionError.class)
    public void testMockThrowsErrorWhenMockSettingsIsNull() {
        Class<String> classToMock = String.class;
        mockitoCore.mock(classToMock, null);
    }

    @Test
    public void testMockWithStubbing() {
        MockSettings mockSettings = mock(MockSettings.class);
        Class<String> classToMock = String.class;
        String mock = mockitoCore.mock(classToMock, mockSettings);
        OngoingStubbing<String> stubbing = mockitoCore.stub(mock);
        stubbing.thenReturn("foo");
        assertEquals("foo", mockitoCore.verify(mock).toString());
    }

    @Test
    public void testStub() {
        String mock = mockitoCore.mock(String.class);
        IOngoingStubbing stubbing = mockitoCore.stub();
        stubbing.thenReturn("foo");
        assertEquals("foo", mockitoCore.stub(mock).thenReturn("foo").getMock());
        assertEquals("foo", mockitoCore.verify(mock).toString());
    }

    @Test
    public void testWhen() {
        String mock = mockitoCore.mock(String.class);
        OngoingStubbing<String> stubbing = mockitoCore.when(mock);
        stubbing.thenReturn("foo");
        assertEquals("foo", mockitoCore.verify(mock).toString());
    }

    @Test
    public void testVerify() {
        String mock = mockitoCore.mock(String.class);
        assertEquals(mock, mockitoCore.verify(mock).toString());
    }

    @Test(expected = AssertionError.class)
    public void testVerifyNullMock() {
        mockitoCore.verify(null);
    }

    @Test(expected = AssertionError.class)
    public void testVerifyNotAMock() {
        Object notAMock = new Object();
        mockitoCore.verify(notAMock);
    }

    @Test
    public void testReset() {
        String mock1 = mockitoCore.mock(String.class);
        String mock2 = mockitoCore.mock(String.class);
        mockitoCore.reset(mock1, mock2);
        mockitoCore.verifyNoMoreInteractions(mock1, mock2);
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        String mock = mockitoCore.mock(String.class);
        mockitoCore.verifyNoMoreInteractions(mock);
    }

    @Test(expected = AssertionError.class)
    public void testVerifyNoMoreInteractionsNullMock() {
        mockitoCore.verifyNoMoreInteractions(null);
    }

    @Test
    public void testVerifyNoMoreInteractionsNotAMock() {
        Object notAMock = new Object();
        mockitoCore.verifyNoMoreInteractions(notAMock);
    }

    @Test
    public void testInOrder() {
        String mock1 = mockitoCore.mock(String.class);
        String mock2 = mockitoCore.mock(String.class);
        InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
        inOrder.verify(mock1).toString();
        inOrder.verify(mock2).toString();
        inOrder.verifyNoMoreInteractions();
    }

    @Test(expected = AssertionError.class)
    public void testInOrderNullMock() {
        InOrder inOrder = mockitoCore.inOrder(null);
    }

    @Test(expected = AssertionError.class)
    public void testInOrderNotAMock() {
        Object notAMock = new Object();
        InOrder inOrder = mockitoCore.inOrder(notAMock);
    }

    @Test
    public void testAnswer() {
        String mock = mockitoCore.mock(String.class);
        Answer<String> answer = invocation -> "answer";
        Stubber stubber = mockitoCore.doAnswer(answer);
        assertEquals("answer", stubber.thenReturn(mock).getMock().toString());
    }

    @Test
    public void testVoidMethodStubbable() {
        String mock = mockitoCore.mock(String.class);
        MockitoCore mockitoCore = spy(mockitoCore);
        doAnswer(invocation -> {}).when(mockitoCore).stubVoid(mockitoCore.mock(String.class));
        mockitoCore.stubVoid(mockitoCore.mock(String.class));
        verify(mockitoCore, times(1)).stubVoid(mockitoCore.mock(String.class));
    }

    @Test
    public void testValidateMockitoUsage() {
        mockitoCore.validateMockitoUsage();
    }
}
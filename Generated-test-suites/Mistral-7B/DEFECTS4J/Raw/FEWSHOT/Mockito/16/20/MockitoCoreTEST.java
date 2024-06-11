package org.mockito.internal;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

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
        Class<?> mockClass = String.class;
        Object mock = mockitoCore.mock(mockClass, null, false);
        assertTrue(mockitoCore.mockUtil.isMock(mock));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMockNullClass() {
        mockitoCore.mock(null, null, false);
    }

    @Test
    public void testMockWithMockSettings() {
        Class<?> mockClass = String.class;
        MockSettings mockSettings = new MockSettingsImpl();
        Object mock = mockitoCore.mock(mockClass, mockSettings, false);
        assertTrue(mockitoCore.mockUtil.isMock(mock));
    }

    @Test
    public void testResetOngoingStubbing() {
        Object mock = mock(String.class, null, false);
        IOngoingStubbing stubbing = mockitoCore.stub();
        mockitoCore.resetOngoingStubbing();
        assertNull(mockitoCore.stub());
    }

    @Test
    public void testStub() {
        Object mock = mock(String.class, null, false);
        IOngoingStubbing stubbing = mockitoCore.stub();
        stubbing.thenReturn("foo");
        assertEquals("foo", stubbing.mock.toString());
    }

    @Test
    public void testWhen() {
        Object mock = mock(String.class, null, false);
        IOngoingStubbing stubbing = mockitoCore.when(mock);
        stubbing.thenReturn("foo");
        assertEquals("foo", mockitoCore.verify(mock).toString());
    }

    @Test
    public void testVerify() {
        Object mock = mock(String.class, null, false);
        verify(mock).toString();
    }

    @Test(expected = NullPointerException.class)
    public void testVerifyNullMock() {
        verify(null);
    }

    @Test(expected = org.mockito.exceptions.misusing.NotAMockException.class)
    public void testNotAMockPassedToVerify() {
        Object notAMock = new Object();
        verify(notAMock);
    }

    @Test
    public void testReset() {
        Object mock1 = mock(String.class, null, false);
        Object mock2 = mock(String.class, null, false);
        mockitoCore.reset(mock1, mock2);
        assertNull(mockitoCore.verify(mock1).toString());
        assertNull(mockitoCore.verify(mock2).toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifyNullMockArray() {
        mockitoCore.verify(null);
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        Object mock = mock(String.class, null, false);
        verifyNoMoreInteractions(mock);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifyNoMoreInteractionsNull() {
        verifyNoMoreInteractions();
    }

    @Test(expected = org.mockito.exceptions.misusing.NotAMockException.class)
    public void testNotAMockPassedToVerifyNoMoreInteractions() {
        Object notAMock = new Object();
        verifyNoMoreInteractions(notAMock);
    }

    @Test
    public void testInOrder() {
        Object mock1 = mock(String.class, null, false);
        Object mock2 = mock(String.class, null, false);
        InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
        inOrder.verify(mock1).toString();
        inOrder.verify(mock2).toString();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInOrderNull() {
        InOrder inOrder = mockitoCore.inOrder(null);
    }

    @Test(expected = org.mockito.exceptions.misusing.NotAMockException.class)
    public void testNotAMockPassedWhenCreatingInOrder() {
        Object notAMock = new Object();
        mockitoCore.inOrder(notAMock);
    }

    @Test
    public void testAnswer() {
        Object mock = mock(String.class, null, false);
        when(mock).thenAnswer(i -> i.get(0).toString());
        assertEquals("mock", mockitoCore.verify(mock).toString());
    }

    @Test
    public void testVoidMethodStubbable() {
        Object mock = mock(String.class, null, false);
        mockitoCore.stubVoid(mock).thenCallRealMethod();
        mockitoCore.verify(mock).toString();
    }

    @Test
    public void testValidateMockitoUsage() {
        mockitoCore.validateMockitoUsage();
    }

    @Test
    public void testGetLastInvocation() {
        Object mock = mock(String.class, null, false);
        IOngoingStubbing stubbing = mockitoCore.stub();
        stubbing.thenReturn("foo");
        Invocation invocation = mockitoCore.getLastInvocation();
        assertEquals("foo", invocation.getArgument(0));
    }

    @Test
    public void testSpy() {
        Object original = spy(new Object());
        Object mock = mockitoCore.mock(Object.class, null, false);
        verify(mock).equals(original);
    }
}
package org.mockito.internal.test;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Answer;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answerstub;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class MockitoCoreTest {

    @Test
    public void testMock() {
        MockSettings mockSettings = new MockSettingsImpl();
        Object mock = MockitoCore.mock(String.class, mockSettings);
        Assert.assertNotNull(mock);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMockWithNullClass() {
        MockSettings mockSettings = new MockSettingsImpl();
        MockitoCore.mock(null, mockSettings);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMockWithNullMockSettings() {
        Class<String> stringClass = String.class;
        MockitoCore.mock(stringClass, null);
    }

    @Test
    public void testStub() {
        Object mock = Mockito.mock(String.class);
        IOngoingStubbing stubbing = MockitoCore.stub(mock);
        stubbing.thenReturn("Hello World!");
        String result = (String) Mockito.invoke(mock, "toString");
        Assert.assertEquals("Hello World!", result);
    }

    @Test
    public void testStubDeprecated() {
        Object mock = Mockito.mock(String.class);
        @SuppressWarnings("unchecked")
        DeprecatedOngoingStubbing<String> stubbing = (DeprecatedOngoingStubbing<String>) MockitoCore.stub(mock);
        stubbing.thenReturn("Hello World!");
        String result = (String) Mockito.invoke(mock, "toString");
        Assert.assertEquals("Hello World!", result);
    }

    @Test
    public void testWhen() {
        Object mock = Mockito.mock(String.class);
        Object arg = "Arg";
        when(mock).thenReturn("Hello World!");
        String result = (String) Mockito.invoke(mock, "toString", arg);
        Assert.assertEquals("Hello World!", result);
    }

    @Test
    public void testVerify() {
        Object mock = Mockito.mock(String.class);
        verify(mock).toString();
    }

    @Test(expected = NullPointerException.class)
    public void testVerifyWithNullMock() {
        verify(null);
    }

    @Test(expected = org.mockito.exceptions.misusing.NotAMockException.class)
    public void testVerifyNotAMock() {
        Object arg = new Object();
        verify(arg);
    }

    @Test
    public void testReset() {
        Object mock = Mockito.mock(String.class);
        verify(mock).toString();
        MockitoCore.reset(mock);
        verify(mock, Mockito.never()).toString();
    }

    @Test(expected = NullPointerException.class)
    public void testResetWithNullMockArray() {
        MockitoCore.reset((Object[]) null);
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        Object mock = Mockito.mock(String.class);
        verify(mock).toString();
        verifyNoMoreInteractions(mock);
    }

    @Test(expected = NullPointerException.class)
    public void testVerifyNoMoreInteractionsWithNullMock() {
        verifyNoMoreInteractions(null);
    }

    @Test(expected = org.mockito.exceptions.misusing.NotAMockException.class)
    public void testVerifyNoMoreInteractionsWithNotAMock() {
        Object arg = new Object();
        verifyNoMoreInteractions(arg);
    }

    @Test
    public void testInOrder() {
        Object mock1 = Mockito.mock(String.class);
        Object mock2 = Mockito.mock(String.class);
        InOrder inOrder = MockitoCore.inOrder(mock1, mock2);
        inOrder.verify(mock1).toString();
        inOrder.verify(mock2).toString();
    }

    @Test(expected = NullPointerException.class)
    public void testInOrderWithNullMockArray() {
        MockitoCore.inOrder((Object[]) null);
    }

    @Test(expected = org.mockito.exceptions.misusing.NotAMockException.class)
    public void testInOrderWithNotAMock() {
        Object arg = new Object();
        InOrder inOrder = MockitoCore.inOrder(arg);
    }

    @Test
    public void testDoAnswer() {
        Object mock = Mockito.mock(String.class);
        Answer answer = new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                return "Answer";
            }
        };
        Object result = MockitoCore.doAnswer(answer).when(mock).toString();
        Assert.assertEquals("Answer", result);
    }

    @Test
    public void testStubVoid() {
        Object mock = Mockito.mock(String.class);
        MockitoCore.stubVoid(mock).toString();
        String result = (String) Mockito.invoke(mock, "toString");
        Assert.assertNull(result);
    }

    @Test(expected = NullPointerException.class)
    public void testStubVoidWithNullMock() {
        MockitoCore.stubVoid(null);
    }

    @Test
    public void testGetLastInvocation() {
        Object mock = Mockito.mock(String.class);
        when(mock).thenReturn("Hello World!");
        Invocation lastInvocation = MockitoCore.getLastInvocation();
        Assert.assertNotNull(lastInvocation);
        Assert.assertEquals("toString", lastInvocation.getMethod().getName());
        Assert.assertEquals(mock, lastInvocation.getMock());
    }

    @Test
    public void testValidateMockitoUsage() {
        MockitoCore.validateMockitoUsage();
    }

    @Test(expected = IllegalStateException.class)
    public void testValidateMockitoUsageWithOngoingStubbing() {
        Object mock = Mockito.mock(String.class);
        MockitoCore.stub(mock).thenReturn("Hello World!");
        MockitoCore.validateMockitoUsage();
    }
}
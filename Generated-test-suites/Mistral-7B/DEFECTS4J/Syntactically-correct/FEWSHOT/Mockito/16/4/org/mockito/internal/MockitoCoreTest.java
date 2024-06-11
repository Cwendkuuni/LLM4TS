package org.mockito.internal;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Answer;
import org.mockito.AnswerWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockSettings;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.AnswerStub;
import org.mockito.stubbing.Stubber;

import java.util.Arrays;
import java.util.List;

public class MockitoCoreTest {

    @Test
    public void testMock() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = mockitoCore.mock(String.class, new MockSettings(), false);
        Assert.assertNotNull(mock);
    }

    @Test
    public void testMockWithMockSettings() {
        MockitoCore mockitoCore = new MockitoCore();
        MockSettings mockSettings = new MockSettings();
        Object mock = mockitoCore.mock(String.class, mockSettings, false);
        Assert.assertNotNull(mock);
        Assert.assertEquals(mockSettings, mockitoCore.getMockSettings(mock));
    }

    @Test
    public void testStub() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = mockitoCore.mock(String.class);
        Stubber stubber = mockitoCore.stub();
        stubber.thenReturn("mock return value");
        String result = (String) mockitoCore.verify(mock).invoke((InvocationOnMock) ArgumentMatchers.any());
        Assert.assertEquals("mock return value", result);
    }

    @Test
    public void testStubWithVerificationMode() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = mockitoCore.mock(String.class);
        Stubber stubber = mockitoCore.stub();
        stubber.thenReturn("mock return value");
        VerificationMode verificationMode = VerificationMode.times(1);
        mockitoCore.verify(mock, verificationMode);
        String result = (String) mockitoCore.verify(mock).invoke((InvocationOnMock) ArgumentMatchers.any());
        Assert.assertEquals("mock return value", result);
    }

    @Test(expected = NotAMockException.class)
    public void testVerifyNotAMock() {
        MockitoCore mockitoCore = new MockitoCore();
        Object notAMock = new Object();
        mockitoCore.verify(notAMock);
    }

    @Test(expected = AssertionError.class)
    public void testVerifyNull() {
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.verify(null);
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock1 = mockitoCore.mock(String.class);
        Object mock2 = mockitoCore.mock(String.class);
        InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
        Object mock3 = mockitoCore.mock(String.class);

        inOrder.verify(mock1).foo();
        inOrder.verify(mock1).bar();
        inOrder.verify(mock2).baz();

        mockitoCore.verifyNoMoreInteractions(mock1, mock2);

        // Should not throw an exception
        mockitoCore.verifyNoMoreInteractions(mock3);
    }

    @Test(expected = AssertionError.class)
    public void testVerifyNoMoreInteractionsNull() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = mockitoCore.mock(String.class);
        mockitoCore.verifyNoMoreInteractions(mock, (Object[]) null);
    }

    @Test
    public void testVerifyNoMoreInteractionsEmptyArray() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = mockitoCore.mock(String.class);
        mockitoCore.verifyNoMoreInteractions();
    }

    @Test
    public void testInOrder() {
        MockitoCore mockitoCore = new MockitoCore();

        Object mock1 = mockitoCore.mock(String.class);
        Object mock2 = mockitoCore.mock(String.class);

        InOrder inOrder = mockitoCore.inOrder(mock1, mock2);

        inOrder.verify(mock1).foo();
        inOrder.verify(mock1).bar();
        inOrder.verify(mock2).baz();
    }

    @Test
    public void testAnswer() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = mockitoCore.mock(String.class);
        Answer answer = new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return "answer return value";
            }
        };
        Stubber stubber = mockitoCore.stub();
        stubber.thenAnswer(answer);
        String result = (String) mockitoCore.verify(mock).invoke((InvocationOnMock) ArgumentMatchers.any());
        Assert.assertEquals("answer return value", result);
    }

    @Test
    public void testAnswerWith() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = mockitoCore.mock(String.class);
        AnswerWith<String> answer = new AnswerWith<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                return "answer return value";
            }
        };
        Stubber stubber = mockitoCore.stub();
        stubber.thenAnswer(answer);
        String result = (String) mockitoCore.verify(mock).invoke((InvocationOnMock) ArgumentMatchers.any());
        Assert.assertEquals("answer return value", result);
    }

    @Test
    public void testDoAnswer() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = mockitoCore.mock(String.class);
        Answer answer = new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] arguments = invocation.getArguments();
                return arguments[0].toString() + "answer return value";
            }
        };
        Stubber stubber = mockitoCore.doAnswer(answer);
        String result = (String) stubber.thenReturn("mock return value").thenReturn("mock return value 2").invoke(mock, "test");
        Assert.assertEquals("testanswer return value", result);
    }

    @Test
    public void testReset() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock1 = mockitoCore.mock(String.class);
        Object mock2 = mockitoCore.mock(String.class);

        Stubber stubber = mockitoCore.stub();
        stubber.thenReturn("mock return value 1");
        String result1 = (String) mockitoCore.verify(mock1).invoke((InvocationOnMock) ArgumentMatchers.any());
        Assert.assertEquals("mock return value 1", result1);

        mockitoCore.reset();

        stubber.thenReturn("mock return value 2");
        String result2 = (String) mockitoCore.verify(mock1).invoke((InvocationOnMock) ArgumentMatchers.any());
        Assert.assertEquals("mock return value 2", result2);

        mockitoCore.verifyNoMoreInteractions(mock1, mock2);
    }

    @Test
    public void testResetAll() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock1 = mockitoCore.mock(String.class);
        Object mock2 = mockitoCore.mock(String.class);

        Stubber stubber = mockitoCore.stub();
        stubber.thenReturn("mock return value 1");
        String result1 = (String) mockitoCore.verify(mock1).invoke((InvocationOnMock) ArgumentMatchers.any());
        Assert.assertEquals("mock return value 1", result1);

        mockitoCore.resetAll();

        stubber.thenReturn("mock return value 2");
        String result2 = (String) mockitoCore.verify(mock1).invoke((InvocationOnMock) ArgumentMatchers.any());
        Assert.assertEquals("mock return value 2", result2);

        mockitoCore.verifyNoMoreInteractions(mock1, mock2);
    }

    @Test
    public void testVerifyNoInteractions() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = mockitoCore.mock(String.class);
        mockitoCore.verifyNoInteractions(mock);
    }

    @Test(expected = AssertionError.class)
    public void testVerifyInteractions() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = mockitoCore.mock(String.class);
        InOrder inOrder = mockitoCore.inOrder(mock);
        inOrder.verify(mock).foo();
        mockitoCore.verifyInteractions(mock);
    }

    @Test
    public void testGetLastInvocation() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = mockitoCore.mock(String.class);
        Stubber stubber = mockitoCore.stub();
        stubber.thenReturn("mock return value 1");
        stubber.thenReturn("mock return value 2");
        Invocation lastInvocation = mockitoCore.getLastInvocation();
        Assert.assertNotNull(lastInvocation);
        Assert.assertEquals("mock return value 2", lastInvocation.getMethod().invoke(mock, new Object[0]));
    }

    @Test
    public void testGetLastInvocationWhenNoInvocations() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = mockitoCore.mock(String.class);
        Assert.assertNull(mockitoCore.getLastInvocation());
    }
}
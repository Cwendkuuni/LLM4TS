package org.mockito.internal;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Answer;
import org.mockito.ArgumentMatchers;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.AnswerCall;
import org.mockito.stubbing.OngoingStubbing;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class MockitoCoreTest {

    @Test
    public void testMock() {
        MockitoCore mockitoCore = new MockitoCore();
        MyClass mock = mockitoCore.mock(MyClass.class);
        Assert.assertNotNull(mock);
    }

    @Test
    public void testMockWithMockSettings() {
        MockitoCore mockitoCore = new MockitoCore();
        MyClass mock = mockitoCore.mock(MyClass.class, MockSettings.lenient());
        Assert.assertNotNull(mock);
    }

    @Test
    public void testStubbing() {
        MockitoCore mockitoCore = new MockitoCore();
        MyClass mock = mockitoCore.mock(MyClass.class);
        OngoingStubbing<MyClass> ongoingStubbing = mockitoCore.stub(mock);
        ongoingStubbing.thenReturn("test");
        String result = mockitoCore.verify(mock).someMethod();
        Assert.assertEquals("test", result);
    }

    @Test
    public void testStubbingDeprecated() {
        MockitoCore mockitoCore = new MockitoCore();
        MyClass mock = mockitoCore.mock(MyClass.class);
        MyClass methodCall = ArgumentMatchers.any(MyClass.class);
        DeprecatedOngoingStubbing<MyClass> deprecatedStubbing = mockitoCore.stub(methodCall);
        deprecatedStubbing.thenReturn("test");
        String result = mockitoCore.verify(mock).someMethod();
        Assert.assertEquals("test", result);
    }

    @Test
    public void testWhen() {
        MockitoCore mockitoCore = new MockitoCore();
        MyClass mock = mockitoCore.mock(MyClass.class);
        OngoingStubbing<MyClass> ongoingStubbing = mockitoCore.when(mock);
        ongoingStubbing.thenReturn("test");
        String result = mockitoCore.verify(mock).someMethod();
        Assert.assertEquals("test", result);
    }

    @Test
    public void testVerify() {
        MockitoCore mockitoCore = new MockitoCore();
        MyClass mock = mockitoCore.mock(MyClass.class);
        String result = mockitoCore.verify(mock).someMethod();
        Assert.assertNull(result);
    }

    @Test
    public void testVerifyWithVerificationMode() {
        MockitoCore mockitoCore = new MockitoCore();
        MyClass mock = mockitoCore.mock(MyClass.class);
        mockitoCore.verify(mock, Mockito.times(2)).someMethod();
    }

    @Test
    public void testReset() {
        MockitoCore mockitoCore = new MockitoCore();
        MyClass mock1 = mockitoCore.mock(MyClass.class);
        MyClass mock2 = mockitoCore.mock(MyClass.class);
        mockitoCore.reset(mock1, mock2);
        MyClass resetMock1 = mockitoCore.mock(MyClass.class);
        MyClass resetMock2 = mockitoCore.mock(MyClass.class);
        Assert.assertNotSame(mock1, resetMock1);
        Assert.assertNotSame(mock2, resetMock2);
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        MockitoCore mockitoCore = new MockitoCore();
        MyClass mock = mockitoCore.mock(MyClass.class);
        verifyNoMoreInteractions(mock);
    }

    @Test
    public void testVerifyNoMoreInteractionsWithObjects() {
        MockitoCore mockitoCore = new MockitoCore();
        MyClass mock1 = mockitoCore.mock(MyClass.class);
        MyClass mock2 = mockitoCore.mock(MyClass.class);
        verifyNoMoreInteractions(mock1, mock2);
    }

    @Test
    public void testInOrder() {
        MockitoCore mockitoCore = new MockitoCore();
        MyClass mock1 = mockitoCore.mock(MyClass.class);
        MyClass mock2 = mockitoCore.mock(MyClass.class);
        InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
    }

    @Test
    public void testDoAnswer() {
        MockitoCore mockitoCore = new MockitoCore();

        MyClass mock = mockitoCore.mock(MyClass.class);
        Answer<String> answer = new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                return "test";
            }
        };
        Stubber stubber = mockitoCore.doAnswer(answer);
        stubber.when(mock).someMethod();
        String result = mockitoCore.verify(mock).someMethod();
        Assert.assertEquals("test", result);
    }

    @Test
    public void testVoidMethodStubbable() {
        MockitoCore mockitoCore = new MockitoCore();
        MyClass mock = mockitoCore.mock(MyClass.class);
        MyClass mockWithStubbable = mockitoCore.mock(MyClass.class, new MockSettingsImpl());
        MyClass mockWithStubbableAndAnswer = mockitoCore.mock(MyClass.class, new MockSettingsImpl(), false);

        StubberImpl stubber = (StubberImpl) mockitoCore.doAnswer(new AnswerCall<VoidMethodStubbable<MyClass>>() {
            @Override
            public VoidMethodStubbable<MyClass> answer(InvocationOnMock invocation) throws Throwable {
                return mockitoCore.stubVoid(mock);
            }
        }).when(mock);
        stubber.doCall(mockWithStubbable).someVoidMethod();

        StubberImpl stubber2 = (StubberImpl) mockitoCore.doAnswer(new AnswerCall<VoidMethodStubbable<MyClass>>() {
            @Override
            public VoidMethodStubbable<MyClass> answer(InvocationOnMock invocation) throws Throwable {
                return mockitoCore.stubVoid(mockWithStubbableAndAnswer);
            }
        }).when(mockWithStubbableAndAnswer).someVoidMethod();
        stubber2.thenThrow(new RuntimeException());

        try {
            mockitoCore.verify(mockWithStubbable).someVoidMethod();
        } catch (RuntimeException e) {
            // expected
        }
    }

    @Test
    public void testValidateMockitoUsage() {
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.validateMockitoUsage();
    }

    @Test
    public void testGetLastInvocation() {
        MockitoCore mockitoCore = new MockitoCore();
        MyClass mock = mockitoCore.mock(MyClass.class);
        OngoingStubbing<MyClass> ongoingStubbing = mockitoCore.stub(mock);
        ongoingStubbing.thenReturn("test");
        Invocation invocation = mockitoCore.getLastInvocation();
        Assert.assertNotNull(invocation);
        Assert.assertEquals(mock, invocation.getMock());
        Assert.assertEquals("someMethod", invocation.getMethod().getName());
    }
}

class MyClass {
    public String someMethod() {
        return null;
    }

    public void someVoidMethod() {

    }
}
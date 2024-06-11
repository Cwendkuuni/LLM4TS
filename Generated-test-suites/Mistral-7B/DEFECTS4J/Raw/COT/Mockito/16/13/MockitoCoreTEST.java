package org.mockito.internal.tests;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Answer;
import org.mockito.ArgumentMatchers;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.MockitoCore;
import org.mockito.MockSettings;
import org.mockito.MockitoException;
import org.mockito.MockUtil;
import org.mockito.stubbing.AnswerStub;
import org.mockito.stubbing.Stubber;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class MockitoCoreTest {

    @Test
    public void testMock() {
        MockSettings mockSettings = new MockSettingsImpl();
        MockitoCore mockitoCore = new MockitoCore();
        Class<Object> classToMock = Object.class;
        Object mock = mockitoCore.mock(classToMock, mockSettings, false);
        Assert.assertNotNull(mock);
    }

    @Test(expected = MockitoException.class)
    public void testMockThrowsExceptionWhenNullPassedToMock() {
        MockitoCore mockitoCore = new MockitoCore();
        MockitoCore.MockSettings mockSettings = new MockitoCore.MockSettingsImpl();
        MockitoCore.MockingProgress mockingProgress = mockitoCore.new ThreadSafeMockingProgress();
        mockitoCore.mockingProgress = mockingProgress;
        mockitoCore.reporter = new MockitoCore.Reporter() {};
        mockitoCore.mockUtil = new MockUtil();
        MockitoCore.Invocation invocation = new MockitoCore.Invocation() {};
        mockitoCore.mockingProgress.pushOngoingStubbing(new MockitoCore.OngoingStubbingImpl(mockitoCore, invocation));

        mockitoCore.mock(null, mockSettings, false);
    }

    @Test
    public void testStub() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
        Stubber stubber = mockitoCore.stub();
        stubber.thenReturn("test");

        String result = mockitoCore.when(mock).foo();
        Assert.assertEquals("test", result);
    }

    @Test
    public void testVerify() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
        mockitoCore.verify(mock, Mockito.eq(ArgumentMatchers.anyInt()));
    }

    @Test(expected = MockitoException.class)
    public void testVerifyThrowsExceptionWhenNullPassedToVerify() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
        mockitoCore.verify(null, Mockito.any());
    }

    @Test(expected = MockitoException.class)
    public void testVerifyThrowsExceptionWhenNotAMockPassedToVerify() {
        Object mock = new Object();
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.verify(mock, Mockito.eq(ArgumentMatchers.anyInt()));
    }

    @Test
    public void testReset() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock1 = mockitoCore.mock(Object.class, new MockSettingsImpl());
        Object mock2 = mockitoCore.mock(Object.class, new MockSettingsImpl());

        mockitoCore.reset(mock1, mock2);

        verify(mock1).foo();
        verify(mock2).bar();

        mockitoCore.reset(mock1, mock2);

        verifyNoMoreInteractions(mock1, mock2);
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
        mockitoCore.verifyNoMoreInteractions(mock);
    }

    @Test(expected = MockitoException.class)
    public void testVerifyNoMoreInteractionsThrowsExceptionWhenNullPassed() {
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.verifyNoMoreInteractions(null);
    }

    @Test(expected = MockitoException.class)
    public void testVerifyNoMoreInteractionsThrowsExceptionWhenNotAMockPassed() {
        Object mock = new Object();
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.verifyNoMoreInteractions(mock);
    }

    @Test
    public void testInOrder() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock1 = mockitoCore.mock(Object.class, new MockSettingsImpl());
        Object mock2 = mockitoCore.mock(Object.class, new MockSettingsImpl());

        InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
        inOrder.verify(mock1).foo();
        inOrder.verify(mock2).bar();
    }

    @Test
    public void testDoAnswer() {
        Answer<String> answer = new AnswerStub<Object, String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                return "answer";
            }
        };
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
        Stubber stubber = mockitoCore.doAnswer(answer);

        String result = mockitoCore.when(mock).foo();
        Assert.assertEquals("answer", result);
    }

    @Test
    public void testStubVoid() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
        Stubber stubber = mockitoCore.stubVoid(mock);
        stubber.thenCallRealMethod();

        try {
            mockitoCore.when(mock).foo();
            mockitoCore.getLastInvocation().proceed();
        } catch (InvocationTargetException e) {
            // expected
        }
    }

    @Test
    public void testValidateMockitoUsage() {
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.validateMockitoUsage();
    }

    @Test(expected = MockitoException.class)
    public void testStubThrowsExceptionWhenNullPassedToStub() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
        mockitoCore.stub(mock);
    }

    @Test(expected = MockitoException.class)
    public void testStubThrowsExceptionWhenStubbingNull() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
        mockitoCore.stub(null);
    }

    @Test(expected = MockitoException.class)
    public void testStubThrowsExceptionWhenStubbingNonMock() {
        Object mock = new Object();
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.stub(mock);
    }

    @Test(expected = MockitoException.class)
    public void testWhenThrowsExceptionWhenNullPassed() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
        mockitoCore.when(mock).foo();
    }

    @Test(expected = MockitoException.class)
    public void testWhenThrowsExceptionWhenNotAMockPassed() {
        Object mock = new Object();
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.when(mock).foo();
    }

    @Test(expected = MockitoException.class)
    public void testVerifyThrowsExceptionWhenVerifyingNull() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
        mockitoCore.verify(null, Mockito.any());
    }

    @Test(expected = MockitoException.class)
    public void testVerifyThrowsExceptionWhenVerifyingNonMock() {
        Object mock = new Object();
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.verify(mock, Mockito.any());
    }

    @Test(expected = MockitoException.class)
    public void testVerifyNoMoreInteractionsThrowsExceptionWhenNullPassed() {
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.verifyNoMoreInteractions(null);
    }

    @Test(expected = MockitoException.class)
    public void testVerifyNoMoreInteractionsThrowsExceptionWhenNotAMockPassed() {
        Object mock = new Object();
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.verifyNoMoreInteractions(mock);
    }

    @Test
    public void testGetLastInvocation() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
        Stubber stubber = mockitoCore.stub(mock);
        stubber.thenReturn("test");
        Object result = mockitoCore.when(mock).foo();

        MockitoCore.Invocation lastInvocation = mockitoCore.getLastInvocation();
        Assert.assertEquals("foo", lastInvocation.getMethod().getName());
        Assert.assertEquals(mock, lastInvocation.getMock());
        Assert.assertEquals(Optional.of("test"), lastInvocation.getArguments());
    }

    @Test
    public void testStubbingMultipleMethods() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
        Stubber stubber = mockitoCore.stub(mock);

        stubber.thenReturn("test1").when(mock).foo();
        stubber.thenReturn("test2").when(mock).bar();

        Assert.assertEquals("test1", mockitoCore.when(mock).foo().toString());
        Assert.assertEquals("test2", mockitoCore.when(mock).bar().toString());
    }
}
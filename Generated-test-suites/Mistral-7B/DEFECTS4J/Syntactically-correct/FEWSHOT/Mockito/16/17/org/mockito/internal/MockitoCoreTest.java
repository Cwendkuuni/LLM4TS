package org.mockito.internal;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Answer;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockHandler;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.AnswerImpl;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.stubbing.Stubber;
import org.mockito.verification.VerificationMode;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class MockitoCoreTest {

    @Test
    public void testMock() {
        MockHandler<Object> mockHandlerSpy = spy(Mockito.mock(MockHandler.class));
        Object mock = mock(Object.class, new MockSettingsImpl(), false);
        Mockito.doReturn(mockHandlerSpy).when(mockUtil).getMockHandler(mock);

        Assert.assertNotNull(mock);
        Assert.assertSame(mockHandlerSpy, Mockito.getMockHandler(mock));
    }

    @Test
    public void testMockWithSettings() {
        MockHandler<Object> mockHandlerSpy = spy(Mockito.mock(MockHandler.class));
        Object mock = mock(Object.class, new MockSettingsImpl(), false);
        Mockito.doReturn(mockHandlerSpy).when(mockUtil).getMockHandler(mock);

        MockSettings settings = new MockSettingsImpl();
        settings.useDefaultAnswer(true);
        Object mockWithSettings = mock(Object.class, settings, false);

        Assert.assertSame(mockHandlerSpy, Mockito.getMockHandler(mockWithSettings));
        Assert.assertTrue(Mockito.isDefaultAnswer(mockWithSettings));
    }

    @Test
    public void testStub() {
        Object mock = mock(Object.class);
        Stubber stubber = Mockito.stub(mock);

        Assert.assertNotNull(stubber);
        Assert.assertSame(mock, stubber.getMock());
        Assert.assertSame(stubber, Mockito.stub(mock));
    }

    @Test
    public void testWhen() {
        Object mock = mock(Object.class);
        Stubber stubber = Mockito.stub(mock);
        OngoingStubbing<Object> ongoingStubbing = stubber.when(mock);

        Assert.assertSame(mock, ongoingStubbing.getMock());
        Assert.assertSame(stubber, ongoingStubbing.getStubber());
        Assert.assertSame(mock, ongoingStubbing.getTarget());

        ArgumentCaptor<Integer> argumentCaptor = ArgumentCaptor.forClass(Integer.class);

        ongoingStubbing.thenReturn(1);
        ongoingStubbing.thenAnswer(Answer.returnValues(2, 3));
        ongoingStubbing.thenThrow(new RuntimeException("Exception message"));

        Assert.assertEquals(1, ongoingStubbing.getNumberOfCalls());
        Assert.assertEquals(1, ongoingStubbing.getInvocations().size());

        ongoingStubbing.thenReturn(4);

        verify(mock).someMethod(argumentCaptor.capture());
        int capturedArgument = argumentCaptor.getValue();
        Assert.assertEquals(1, capturedArgument.intValue());
        Assert.assertEquals(4, ongoingStubbing.getNumberOfCalls());

        ongoingStubbing.thenCallRealMethod();

        Assert.assertEquals(5, ongoingStubbing.getNumberOfCalls());
        Assert.assertEquals(2, mock.someMethod(1));
        Assert.assertEquals(3, mock.someMethod(1));
        Assert.assertEquals(RuntimeException.class, ongoingStubbing.getThrown());
    }

    @Test
    public void testVerify() {
        Object mock = mock(Object.class);

        Assert.assertNull(Mockito.verify(mock).someMethod(1));
        Assert.assertNull(Mockito.verifyNoMoreInteractions(mock));
        Assert.assertNull(Mockito.verifyZeroInteractions(mock));

        when(mock).someMethod().thenReturn(1);

        Assert.assertEquals(1, Mockito.verify(mock).someMethod());

        when(mock).someMethod().thenThrow(new RuntimeException("Exception message"));

        try {
            Mockito.verify(mock).someMethod();
            Assert.fail();
        } catch (org.mockito.exceptions.verification.VerificationError e) {
            Assert.assertEquals("Expected one call to: someMethod()\nMockitoCoreTest.java:[line]-[line]\n-> at Object.<someMethod>()", e.getMessage());
        }

        when(mock).someMethod().thenAnswer(Answer.returnValues(1, 2));

        ArgumentCaptor<Integer> argumentCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(mock).someMethod(argumentCaptor.capture());
        int capturedArgument = argumentCaptor.getValue();
        Assert.assertEquals(1, capturedArgument.intValue());

        verify(mock, VerificationMode.times(0)).someOtherMethod();
    }

    @Test
    public void testReset() {
        Object mock = mock(Object.class);

        when(mock).someMethod().thenReturn(1);
        Assert.assertEquals(1, Mockito.verify(mock).someMethod());

        reset(mock);

        Assert.assertNull(Mockito.verify(mock).someMethod());
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        Object mock = mock(Object.class);

        verifyNoMoreInteractions(mock);

        when(mock).someMethod().thenReturn(1);

        try {
            verifyNoMoreInteractions(mock);
            Assert.fail();
        } catch (org.mockito.exceptions.verification.VerificationError e) {
            Assert.assertEquals("Interaction with mock has been already verified.\nMockitoCoreTest.java:[line]-[line]\n-> at Object.someMethod()", e.getMessage());
        }

        verifyNoMoreInteractions(mock, times(2).any());
    }

    @Test
    public void testInOrder() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);

        InOrder inOrder = inOrder(mock1, mock2);

        Assert.assertSame(mock1, inOrder.getMock(0));
        Assert.assertSame(mock2, inOrder.getMock(1));

        inOrder.verify(mock1).someMethod();
        inOrder.verify(mock2).someOtherMethod();
        inOrder.verify(mock1, times(2).any()).someMethod();

        inOrder.verify(mock1).someMethod();
        inOrder.verify(mock2).someMethod();

        inOrder.verify(mock1, never()).someOtherMethod();
    }

    @Test
    public void testDoAnswer() {
        Object mock = mock(Object.class);

        Stubber stubber = Mockito.stub(mock);
        Answer<Integer> answer = Answer.returnValues(1, 2, 3);

        OngoingStubbing<Object> ongoingStubbing = stubber.doAnswer(answer);

        Assert.assertEquals(1, Mockito.verify(mock).someMethod());
        Assert.assertEquals(2, Mockito.verify(mock).someMethod());
        Assert.assertEquals(3, Mockito.verify(mock).someMethod());
    }

    @Test
    public void testThreadSafeMockingProgress() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);

        Stubber stubber1 = Mockito.stub(mock1);
        Stubber stubber2 = Mockito.stub(mock2);

        OngoingStubbing<Object> ongoingStubbing1 = stubber1.when(mock1).someMethod();
        OngoingStubbing<Object> ongoingStubbing2 = stubber2.when(mock2).someMethod();

        ongoingStubbing1.thenReturn(1);
        ongoingStubbing2.thenReturn(2);

        verify(mock1).someMethod();
        verify(mock2).someMethod();

        Assert.assertEquals(1, Mockito.verify(mock1).someMethod());
        Assert.assertEquals(2, Mockito.verify(mock2).someMethod());
    }
}
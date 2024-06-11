package org.mockito.internal;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.mockito.Answer;
import org.mockito.ArgumentMatchers;
import org.mockito.MockHandler;
import org.mockito.Mockito;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.MockUtil;
import org.mockito.internal.progress.IOngoingStubbing;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.verification.api.VerificationMode;
import org.mockito.stubbing.AnswerReceiver;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.stubbing.Stubber;

public class MockitoCoreTest {

    @Test
    public void testMock() {
        MockHandlerInterface<String> mockHandler = Mockito.mock(String.class);
        String mock = mock(String.class, new MockSettingsImpl(), false);
        assertSame(mockHandler, mockUtil.getMockHandler(mock));
    }

    @Test
    public void testMockWithSettings() {
        MockHandlerInterface<String> mockHandler = Mockito.mock(String.class);
        String mock = mock(String.class, new MockSettingsImpl(), false);
        assertSame(mockHandler, mockUtil.getMockHandler(mock));
    }

    @Test(expected = NotAMockException.class)
    public void testMockNull() {
        mock(null, new MockSettingsImpl(), false);
    }

    @Test(expected = NotAMockException.class)
    public void testVerifyNull() {
        verify(null);
    }

    @Test
    public void testStub() {
        IOngoingStubbing stubbing = stubbing(ArgumentMatchers.anyString());
        assertSame(stubbing, mockingProgress.pullOngoingStubbing());
        verify(stubbing).thenReturn("foo");
        verify(stubbing).thenThrow(new RuntimeException());
        verify(stubbing).thenAnswer(answer("bar"));
    }

    @Test(expected = NotAMockException.class)
    public void testStubWithNull() {
        stub(null);
    }

    @Test
    public void testWhen() {
        String mock = mock(String.class);
        when(mock).thenReturn("foo");
        verify(mock).equals("foo");
    }

    @Test(expected = NotAMockException.class)
    public void testWhenWithNull() {
        when(null);
    }

    @Test
    public void testVerify() {
        String mock = mock(String.class);
        verify(mock).equals("foo");
    }

    @Test(expected = NotAMockException.class)
    public void testVerifyWithNull() {
        verify(null);
    }

    @Test
    public void testReset() {
        String mock = mock(String.class);
        reset(mock);
        verify(mock).equals(null);
    }

    @Test(expected = NotAMockException.class)
    public void testVerifyNoMoreInteractionsNull() {
        verifyNoMoreInteractions(null);
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        String mock1 = mock(String.class);
        String mock2 = mock(String.class);
        verifyNoMoreInteractions(mock1, mock2);
    }

    @Test(expected = NotAMockException.class)
    public void testVerifyNoMoreInteractionsEmptyArray() {
        verifyNoMoreInteractions();
    }

    @Test
    public void testInOrder() {
        String mock1 = mock(String.class);
        String mock2 = mock(String.class);
        InOrder inOrder = inOrder(mock1, mock2);
        inOrder.verify(mock1).equals(mock2);
    }

    @Test(expected = NotAMockException.class)
    public void testInOrderWithNull() {
        inOrder(null);
    }

    @Test(expected = NotAMockException.class)
    public void testInOrderEmptyArray() {
        inOrder();
    }

    @Test
    public void testDoAnswer() {
        String mock = mock(String.class);
        Answer<String> answer = invocation -> "answer";
        Stubber stubber = doAnswer(answer);
        when(mock).then(stubber);
        verify(mock).equals("answer");
    }

    @Test(expected = NotAMockException.class)
    public void testDoAnswerWithNull() {
        doAnswer(null);
    }

    @Test
    public void testStubVoid() {
        String mock = mock(String.class);
        StubberImpl stubber = (StubberImpl) stub();
        stubVoid(mock).thenReturnVoid();
        verify(mock).doSomething();
    }

    @Test(expected = NotAMockException.class)
    public void testStubVoidWithNull() {
        stubVoid(null);
    }

    @Test
    public void testValidateMockitoUsage() {
        validateMockitoUsage();
    }

    @Test
    public void testGetLastInvocation() {
        String mock = mock(String.class);
        when(mock).thenReturn("foo");
        Invocation lastInvocation = getLastInvocation();
        assertSame("foo", lastInvocation.getArguments()[0]);
    }

    @Test(expected = NotAMockException.class)
    public void testGetLastInvocationWithNull() {
        getLastInvocation();
    }

    private IOngoingStubbing stubbing(Object argument) {
        return mockingProgress.pullOngoingStubbing();
    }

    private Answer<Object> answer(final Object answerValue) {
        return new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                AnswerReceiver receiver = (AnswerReceiver) invocation.getMock();
                receiver.answers(answerValue);
                return answerValue;
            }
        };
    }

}
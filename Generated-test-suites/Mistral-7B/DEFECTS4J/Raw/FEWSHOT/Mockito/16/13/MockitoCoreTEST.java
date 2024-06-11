package org.mockito.internal;

import org.junit.Test;
import org.mockito.Answer;
import org.mockito.ArgumentMatchers;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.AnswerStub;
import org.mockito.verification.VerificationMode;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class MockitoCoreTest {

    @Test
    public void testMock() {
        MockitoCore core = new MockitoCore();
        Object mock = core.mock(String.class, new MockSettingsImpl(), false);
        assertNull(mock);
    }

    @Test
    public void testMockWithSettings() {
        MockitoCore core = new MockitoCore();
        MockSettings mockSettings = new MockSettingsImpl();
        Object mock = core.mock(String.class, mockSettings, false);
        assertNull(mock);
    }

    @Test
    public void testMockWithResetOngoingStubbing() {
        MockitoCore core = new MockitoCore();
        MockSettings mockSettings = new MockSettingsImpl();
        Object mock = core.mock(String.class, mockSettings, true);
        assert mock != null;

        MockHandlerInterface<String> handler = Mockito.mock(MockHandlerInterface.class);
        core.mockingProgress.setMockHandler(mock, handler);

        verify(handler).createMock(String.class, mockSettings);
    }

    @Test
    public void testStub() {
        MockitoCore core = new MockitoCore();
        Object mock = core.stub();
        assertNull(mock);
    }

    @Test(expected = RuntimeException.class)
    public void testDeprecatedStub() {
        MockitoCore core = new MockitoCore();
        String methodCall = ArgumentMatchers.anyString();
        core.stub(methodCall);
    }

    @Test
    public void testWhen() {
        MockitoCore core = new MockitoCore();
        Object mock = core.when(ArgumentMatchers.any()).thenReturn(ArgumentMatchers.any()).getMock();
        assertNull(mock);
    }

    @Test
    public void testVerify() {
        MockitoCore core = new MockitoCore();
        Object mock = new Object();
        core.verify(mock, VerificationMode.RETURN);
    }

    @Test(expected = RuntimeException.class)
    public void testVerifyNull() {
        MockitoCore core = new MockitoCore();
        core.verify(null, VerificationMode.RETURN);
    }

    @Test(expected = RuntimeException.class)
    public void testVerifyNotAMock() {
        MockitoCore core = new MockitoCore();
        Object notAMock = new Object();
        core.verify(notAMock, VerificationMode.RETURN);
    }

    @Test
    public void testReset() {
        MockitoCore core = new MockitoCore();
        Object mock1 = mock(String.class);
        Object mock2 = mock(String.class);
        core.reset(mock1, mock2);

        MockHandlerInterface<String> handler1 = Mockito.mock(MockHandlerInterface.class);
        MockHandlerInterface<String> handler2 = Mockito.mock(MockHandlerInterface.class);
        core.mockingProgress.setMockHandler(mock1, handler1);
        core.mockingProgress.setMockHandler(mock2, handler2);

        handler1.reset();
        handler2.reset();
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        MockitoCore core = new MockitoCore();
        Object mock = mock(String.class);
        core.verifyNoMoreInteractions(mock);
    }

    @Test(expected = RuntimeException.class)
    public void testVerifyNoMoreInteractionsNull() {
        MockitoCore core = new MockitoCore();
        core.verifyNoMoreInteractions(null);
    }

    @Test(expected = RuntimeException.class)
    public void testVerifyNoMoreInteractionsNotAMock() {
        MockitoCore core = new MockitoCore();
        Object notAMock = new Object();
        core.verifyNoMoreInteractions(notAMock);
    }

    @Test
    public void testInOrder() {
        MockitoCore core = new MockitoCore();
        Object mock1 = mock(String.class);
        Object mock2 = mock(String.class);
        InOrder inOrder = core.inOrder(mock1, mock2);
        inOrder.verify(mock1).foo();
        inOrder.verify(mock2).bar();
    }

    @Test(expected = RuntimeException.class)
    public void testInOrderNull() {
        MockitoCore core = new MockitoCore();
        core.inOrder(null);
    }

    @Test(expected = RuntimeException.class)
    public void testInOrderNotAMock() {
        MockitoCore core = new MockitoCore();
        Object notAMock = new Object();
        core.inOrder(notAMock);
    }

    @Test
    public void testDoAnswer() {
        MockitoCore core = new MockitoCore();
        Object mock = mock(String.class);
        Answer<Integer> answer = new AnswerStub<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                return 42;
            }
        };
        Stubber stubber = core.doAnswer(answer);
        stubber.thenReturn(ArgumentMatchers.anyString()).thenAnswer(answer);
        when(mock).foo().thenReturn("Hello");
        assertEquals(42, stubber.foo(mock));
        assertEquals("Hello", mock.foo());
    }

    @Test
    public void testStubVoid() {
        MockitoCore core = new MockitoCore();
        Object mock = mock(String.class);
        StubberImpl stubber = (StubberImpl) core.stubbing();
        stubber.then(() -> {});
        MockHandlerInterface<String> handler = Mockito.mock(MockHandlerInterface.class);
        core.mockingProgress.setMockHandler(mock, handler);
        core.stubVoid(mock);
        handler.voidMethodStubbable(mock).invoke();
    }

    @Test
    public void testValidateMockitoUsage() {
        MockitoCore core = new MockitoCore();
        core.validateMockitoUsage();
    }

    @Test
    public void testGetLastInvocation() {
        MockitoCore core = new MockitoCore();
        Object mock = mock(String.class);
        when(mock).foo().thenReturn("Hello");
        Invocation invocation = core.getLastInvocation();
        assertEquals("Hello", invocation.getArgument(0));
    }
}
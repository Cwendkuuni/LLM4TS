package org.mockito.internal;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class MockitoCoreTest {

    private Reporter reporter = new Reporter();
    private MockUtil mockUtil = new MockUtil();
    private MockitoCore mockitoCore = new MockitoCore();

    @Test
    public void testMockCreation() {
        Class<String> stringClass = String.class;
        String mock = mockitoCore.mock(stringClass, new MockSettingsImpl());
        assertTrue(mockitoCore.mockingProgress.isMockingInProgress(mock));
        assertTrue(mockitoCore.mockingProgress.hasMock(mock));
    }

    @Test(expected = NullPointerException.class)
    public void testNullMockSettings() {
        mockitoCore.mock(String.class, null);
    }

    @Test
    public void testStubbing() {
        String mock = mock(String.class);
        mockitoCore.stub(mock);
        assertFalse(mockitoCore.mockingProgress.isStubbingInProgress());
        assertNull(mockitoCore.mockingProgress.getCurrentStubbing());
    }

    @Test
    public void testOngoingStubbing() {
        String mock = mock(String.class);
        IOngoingStubbing stubbing = mockitoCore.stub(mock);
        assertTrue(mockitoCore.mockingProgress.isStubbingInProgress());
        assertSame(stubbing, mockitoCore.mockingProgress.getCurrentStubbing());
    }

    @Test
    public void testDeprecatedOngoingStubbing() {
        String mock = mock(String.class);
        DeprecatedOngoingStubbing stubbing = mockitoCore.stub(mock);
        assertTrue(mockitoCore.mockingProgress.isStubbingInProgress());
        assertSame(stubbing, mockitoCore.mockingProgress.getCurrentStubbing());
    }

    @Test
    public void testWhenStubbing() {
        String mock = mock(String.class);
        OngoingStubbing whenStubbing = mockitoCore.when(mock);
        assertTrue(mockitoCore.mockingProgress.isStubbingInProgress());
        assertSame(whenStubbing, mockitoCore.mockingProgress.getCurrentStubbing());
    }

    @Test
    public void testVerify() {
        String mock = mock(String.class);
        verify(mock);
        assertTrue(mockitoCore.mockingProgress.isVerificationInProgress());
    }

    @Test
    public void testVerifyNullMock() {
        verify(null);
        reporter.assertNullPassedToVerify();
    }

    @Test
    public void testNotAMock() {
        Object notAMock = new Object();
        verify(notAMock);
        reporter.notAMockPassedToVerify();
    }

    @Test
    public void testReset() {
        String mock = mock(String.class);
        mockitoCore.reset(mock);
        assertFalse(mockitoCore.mockingProgress.isMockingInProgress(mock));
        assertFalse(mockitoCore.mockingProgress.hasMock(mock));
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        String mock1 = mock(String.class);
        String mock2 = mock(String.class);
        verifyNoMoreInteractions(mock1, mock2);
    }

    @Test(expected = NullPointerException.class)
    public void testNullInOrder() {
        verifyNoMoreInteractions(null);
        reporter.mocksHaveToBePassedToVerifyNoMoreInteractions();
    }

    @Test
    public void testVerifyNoMoreInteractionsWithInOrder() {
        String mock1 = mock(String.class);
        String mock2 = mock(String.class);
        InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
        verifyNoMoreInteractions(inOrder);
    }

    @Test
    public void testVerifyNoMoreInteractionsWithMocks() {
        String mock1 = mock(String.class);
        String mock2 = mock(String.class);
        verifyNoMoreInteractions(mock1, mock2);
    }

    @Test
    public void testVerifyNoMoreInteractionsWithInOrderAndMocks() {
        String mock1 = mock(String.class);
        String mock2 = mock(String.class);
        InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
        verifyNoMoreInteractions(inOrder, mock1, mock2);
    }

    @Test
    public void testVerifyNoMoreInteractionsInOrder() {
        String mock1 = mock(String.class);
        String mock2 = mock(String.class);
        InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
        when(mock1.toString()).thenReturn("mock1");
        when(mock2.toString()).thenReturn("mock2");

        inOrder.verify(mock1).someMethod();
        inOrder.verify(mock2).someMethod();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testVerifyNoMoreInteractionsInOrderWithNullMock() {
        String mock1 = mock(String.class);
        InOrder inOrder = mockitoCore.inOrder(mock1);
        inOrder.verify(mock1).someMethod();
        inOrder.verifyNoMoreInteractions();
        inOrder.verifyNoMoreInteractions(null);
        reporter.nullPassedToVerifyNoMoreInteractions();
    }

    @Test
    public void testVerifyNoMoreInteractionsInOrderWithNonMock() {
        String mock = mock(String.class);
        InOrder inOrder = mockitoCore.inOrder(mock);
        inOrder.verify(mock).someMethod();
        inOrder.verifyNoMoreInteractions();
        reporter.notAMockPassedToVerifyNoMoreInteractions();
    }

    @Test
    public void testInOrderCreation() {
        String mock1 = mock(String.class);
        String mock2 = mock(String.class);
        InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
        assertTrue(inOrder.getInOrderMocks().containsAll(Arrays.asList(mock1, mock2)));
    }

    @Test
    public void testInOrderCreationWithNullMock() {
        String mock1 = mock(String.class);
        InOrder inOrder = mockitoCore.inOrder(mock1, null);
        assertTrue(inOrder.getInOrderMocks().contains(mock1));
        assertTrue(inOrder.getInOrderMocks().size() == 1);
    }

    @Test(expected = NullPointerException.class)
    public void testNullMockWhenCreatingInOrder() {
        InOrder inOrder = mockitoCore.inOrder(null);
        reporter.mocksHaveToBePassedWhenCreatingInOrder();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNonMockWhenCreatingInOrder() {
        Object nonMock = new Object();
        InOrder inOrder = mockitoCore.inOrder(nonMock);
        reporter.notAMockPassedWhenCreatingInOrder();
    }

    @Test
    public void testAnswer() {
        String mock = mock(String.class);
        Answer<String> answer = args -> "answer";
        Stubber stubber = mockitoCore.doAnswer(answer);
        String result = stubber.thenReturn("returnValue").thenCallRealMethod().thenReturn("otherReturnValue").callRealMethod();
        assertEquals("answer", mockitoCore.getLastInvocation().getArgument(0));
        assertEquals("returnValue", result);
    }

    @Test
    public void testStubVoid() {
        String mock = mock(String.class);
        VoidMethodStubbable stubbable = mockitoCore.stubVoid(mock);
        stubbable.answers(answers -> answers.ofVoid());
        mockitoCore.getLastInvocation().proceed();
    }

    @Test
    public void testValidateMockitoUsage() {
       mockitoCore.validateMockitoUsage();
    }
}
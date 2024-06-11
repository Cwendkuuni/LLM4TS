package org.mockito.internal;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.MockSettings;
import org.mockito.MockSettingsImpl;
import org.mockito.internal.creation.MockSettingsImpltest;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.progress.IOngoingStubbing;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.stubbing.DeprecatedOngoingStubbing;
import org.mockito.internal.stubbing.OngoingStubbing;
import org.mockito.internal.stubbing.Stubber;
import org.mockito.internal.stubbing.VoidMethodStubbable;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.internal.verification.api.InOrderContext;
import org.mockito.internal.verification.api.VerificationDataInOrder;
import org.mockito.internal.verification.api.VerificationDataInOrderImpl;
import org.mockito.stubbing.AnswerImpl;
import org.mockito.stubbing.MockitoStubber;

public class MockitoCoreTest {

    @Test
    public void testMock() {
        Class<String> classToMock = String.class;
        MockSettings mockSettings = new MockSettingsImpltest();
        String mock = MockitoCore.mock(classToMock, mockSettings);
        assertNull(mockingProgress.pullOngoingStubbing());
        assertSame(classToMock, mock.getClass());
    }

    @Test
    public void testStub() {
        IOngoingStubbing stubbing = MockitoCore.stub();
        assertNull(stubbing.getRegisteredInvocations());
        assertSame(stubbing, MockitoCore.stub());
    }

    @Test
    public void testWhen() {
        MockitoCore mockitoCoreSpy = spy(MockitoCore.class);
        IOngoingStubbing stubbing = mockitoCoreSpy.stub();
        when(mockitoCoreSpy).stub();

        ArgumentCaptor<Runnable> actionCaptor = ArgumentCaptor.forClass(Runnable.class);
       stubbing.then(actionCaptor.capture());

        Runnable action = actionCaptor.getValue();
        action.run();

        verify(mockitoCoreSpy).stub();
        verify(mockitoCoreSpy).stub(); // verify the second call as well
    }

    @Test
    public void testVerify() {
        Object mock = mock(Object.class);
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.verify(mock, VerificationMode.times(1));
    }

    @Test
    public void testReset() {
        Object mock = mock(Object.class);
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.reset(mock);
        verifyNoInteractions(mock);
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        Object mock = mock(Object.class);
        MockitoCore mockitoCore = new MockitoCore();
        verifyNoMoreInteractions(mock);
    }

    @Test
    public void testVerifyNoMoreInteractionsInOrder() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);

        InOrder inOrder = inOrder(mock1, mock2);
        verifyNoMoreInteractionsInOrder(Arrays.asList(mock1, mock2), inOrder);
    }

    @Test
    public void testVerifyNoInteractions() {
        Object mock = mock(Object.class);
        MockitoCore mockitoCore = new MockitoCore();
        verifyNoInteractions(mock);
    }

    @Test
    public void testVerificationModeFactory() {
        MockitoCore mockitoCore = new MockitoCore();
        VerificationMode verificationMode = VerificationModeFactory.atLeastOnce();
        assertEquals(VerificationMode.atLeastOnce(), verificationMode);
    }

    @Test
    public void testAssertMocksNotEmpty() {
        MockitoCore mockitoCore = new MockitoCore();
        Object[] mocks = new Object[0];
        mockitoCore.assertMocksNotEmpty(mocks);
        assertThrows(IllegalArgumentException.class, () -> mockitoCore.verifyNoMoreInteractions(mocks));
    }

    @Test
    public void testInOrder() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);

        InOrder inOrder = Mockito.inOrder(mock1, mock2);

        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        Object[] arguments = new Object[2];
        arguments[0] = "foo";
        arguments[1] = "bar";

        verify(mock1).method1(eq("foo"), eq("bar"));
        inOrder.verify(mock1).method2(anyString());
        inOrder.verify(mock2).method3(stringArgumentCaptor.capture());
        assertEquals("bar", stringArgumentCaptor.getValue());
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testStubAnswer() {
        MockitoCore mockitoCore = new MockitoCore();
        Stubber stubber = mockitoCore.stub();
        Answer answer = new AnswerImpl(1);

        stubber.thenAnswer(answer);
        verify(stubber).thenAnswer(answer);
    }

    @Test
    public void testStubVoid() {
        Object mock = mock(Object.class);
        MockitoCore mockitoCore = new MockitoCore();
        VoidMethodStubbable voidMethodStubbable = mockitoCore.stubVoid(mock);

        voidMethodStubbable.answer();

        verify(mock).answer();
    }

    @Test
    public void testValidateMockitoUsage() {
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.validateMockitoUsage();
    }

    @Test(expected = IllegalStateException.class)
    public void testGetLastInvocationWhenNoOngoingStubbing() {
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.getLastInvocation();
    }

    @Test
    public void testVerificationDataImpl() {
        Object mock = mock(Object.class);
        List<Invocation> invocations = Arrays.asList(new Invocation(null, null, null, null, null, null));
        VerificationDataImpl verificationDataImpl = new VerificationDataImpl(invocations, null);
        assertEquals(1, verificationDataImpl.getInvocations().size());
        assertEquals(mock, verificationDataImpl.getObject());
        assertFalse(verificationDataImpl.hasErrors());
    }

    @Test
    public void testVerificationDataInOrderImpl() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        List<Invocation> invocations = Arrays.asList(new Invocation(mock1, null, "method1", null, null, null),
                new Invocation(mock2, null, "method2", null, null, null));
        InOrder inOrder = inOrder(mock1, mock2);
        VerificationDataInOrderImpl verificationDataInOrderImpl = new VerificationDataInOrderImpl(inOrder, invocations, null);
        assertEquals(2, verificationDataInOrderImpl.getInvocations().size());
        assertEquals(mock1, verificationDataInOrderImpl.getInvocations().get(0).getMock());
        assertEquals(mock2, verificationDataInOrderImpl.getInvocations().get(1).getMock());
        assertEquals("method1", verificationDataInOrderImpl.getInvocations().get(0).getMethod().getName());
        assertEquals("method2", verificationDataInOrderImpl.getInvocations().get(1).getMethod().getName());
        assertFalse(verificationDataInOrderImpl.hasErrors());
    }

    @Test(expected = NotAMockException.class)
    public void testVerifyWhenPassedNonMockObject() {
        Object nonMockObject = new Object();
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.verify(nonMockObject, VerificationMode.times(1));
    }
}
package org.mockito.internal;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.mockito.Answer;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.MockSettings;
import org.mockito.MockSettingsImpl;
import org.mockito.OngoingStubbing;
import org.mockito.Stubber;
import org.mockito.internal.MockUtil;
import org.mockito.internal.invocation.AllInvocationsFinder;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.progress.IOngoingStubbing;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubberImpl;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.internal.verification.api.InOrderContext;
import org.mockito.internal.verification.api.VerificationDataInOrder;
import org.mockito.internal.verification.api.VerificationDataInOrderImpl;
import org.mockito.stubbing.AnswerMock;
import org.mockito.stubbing.DeprecatedOngoingStubbing;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.stubbing.Stub;
import org.mockito.stubbing.VoidMethodStubbable;

public class MockitoCoreTest {

    @Test
    public void testMock() {
        MockSettings mockSettings = new MockSettingsImpl();
        Object mock = new MockitoCore().mock(String.class, mockSettings);
        assertNull(mockingProgress.getLastOngoingStubbing(mock));
        verify(mockUtil).createMock(String.class, mockSettings);
        verify(mockingProgress).mockingStarted(mock, String.class, mockSettings);
    }

    @Test
    public void testStub() {
        Object mock = mock(String.class);
        IOngoingStubbing ongoingStubbing = new OngoingStubbingImpl();
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.mockingProgress.stubbingStarted();
        DeprecatedOngoingStubbing deprecatedOngoingStubbing = (DeprecatedOngoingStubbing) mockitoCore.stub(mock);
        assertSame(mockitoCore.mockingProgress, deprecatedOngoingStubbing.mockingProgress);
        assertSame(ongoingStubbing, deprecatedOngoingStubbing.stubber);
    }

    @Test
    public void testWhen() {
        Object mock = mock(String.class);
        IOngoingStubbing ongoingStubbing = new OngoingStubbingImpl();
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.mockingProgress.stubbingStarted();
        OngoingStubbing ongoingStubbingWhen = mockitoCore.when(mock);
        assertSame(mockitoCore.mockingProgress, ongoingStubbingWhen.mockingProgress);
        assertSame(ongoingStubbing, ongoingStubbingWhen);
    }

    @Test
    public void testVerify() {
        Object mock = mock(String.class);
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.mockingProgress.verifyStarted(VerificationModeFactory.verifyNoMoreInteractions());
        verify(mockitoCore.reporter).nullPassedToVerify();
    }

    @Test(expected = NotAMockException.class)
    public void testVerifyNotAMock() {
        Object notAMock = new Object();
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.verify(notAMock, VerificationModeFactory.verifyNoMoreInteractions());
    }

    @Test
    public void testReset() {
        Object mock = mock(String.class);
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.mockingProgress.reset();
        verify(mockUtil).resetMock(mock);
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        Object mock = mock(String.class);
        MockitoCore mockitoCore = new MockitoCore();
        List<Invocation> invocations = Arrays.asList(new Invocation(mock, "methodCall1"));
        VerificationDataImpl verificationData = new VerificationDataImpl(invocations, null);
        VerificationModeFactory.noMoreInteractions().verify(verificationData);
        mockitoCore.mockingProgress.verificationStarted(VerificationModeFactory.noMoreInteractions());
    }

    @Test
    public void testVerifyNoMoreInteractionsInOrder() {
        Object mock1 = mock(String.class);
        Object mock2 = mock(String.class);
        InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
        AllInvocationsFinder finder = new AllInvocationsFinder();
        List<Object> mocks = Arrays.asList(mock1, mock2);
        VerificationDataInOrder data = new VerificationDataInOrderImpl(inOrder, finder.find(mocks), null);
        VerificationModeFactory.noMoreInteractions().verifyInOrder(data);
        mockitoCore.mockingProgress.verificationStarted(VerificationModeFactory.noMoreInteractions());
    }

    @Test
    public void testAnswer() {
        Object mock = mock(String.class);
        AnswerMock answerMock = new AnswerMock();
        StubberImpl stubber = new StubberImpl();
        stubber.doAnswer(answerMock);
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.mockingProgress.stubbingStarted();
        Stubber stubberAnswer = mockitoCore.doAnswer(answerMock);
        assertSame(stubber, stubberAnswer);
    }

    @Test
    public void testStubVoid() {
        Object mock = mock(String.class);
        MockitoCore mockitoCore = new MockitoCore();
        MockHandlerInterface<String> handler = mockUtil.getMockHandler(mock);
        mockitoCore.mockingProgress.stubbingStarted();
        VoidMethodStubbable<String> voidMethodStubbable = handler.voidMethodStubbable(mock);
        assertSame(handler, voidMethodStubbable.mockHandler);
    }

    @Test
    public void testValidateMockitoUsage() {
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.mockingProgress.validateState();
    }

    @Test
    public void testGetLastInvocation() {
        Object mock = mock(String.class);
        IOngoingStubbing ongoingStubbing = new OngoingStubbingImpl();
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.mockingProgress.stubbingStarted();
        OngoingStubbing ongoingStubbingWhen = mockitoCore.when(mock);
        List<Invocation> invocations = Arrays.asList(new Invocation(mock, "methodCall1"));
        ongoingStubbingWhen.thenAnswer(new AnswerMock());
        ongoingStubbingWhen.thenReturn("result");
        List<Invocation> allInvocations = Arrays.asList(new Invocation(mock, "methodCall1"), new Invocation(mock, "methodCall2"));
        OngoingStubbingImpl ongoingStubbingImpl = (OngoingStubbingImpl) mockitoCore.mockingProgress.pullOngoingStubbing();
        Invocation lastInvocation = mockitoCore.getLastInvocation();
        assertSame(lastInvocation, allInvocations.get(allInvocations.size() - 1));
    }
}
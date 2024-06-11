package org.mockito.internal;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.MockSettings;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.MockSettingsImpl;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.AllInvocationsFinder;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.progress.IOngoingStubbing;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.progress.StubbingProgress;
import org.mockito.internal.progress.StubbingProgressImpl;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.stubbing.DeprecatedOngoingStubbing;
import org.mockito.internal.stubbing.OngoingStubbing;
import org.mockito.internal.stubbing.Stubber;
import org.mockito.internal.stubbing.VoidMethodStubbable;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.internal.verification.api.InOrderContext;
import org.mockito.internal.verification.api.VerificationDataInOrder;
import org.mockito.internal.verification.api.VerificationDataInOrderImpl;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.DeprecatedOngoingStubbing;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.stubbing.Stubber;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationMode;

public class MockitoCoreTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private Reporter reporter;
    private MockUtil mockUtil;
    private MockingProgress mockingProgress;

    @Before
    public void setUp() {
        this.reporter = new Reporter();
        this.mockUtil = mockitoRule.getMockitoContext().getMockUtil();
        this.mockingProgress = mockitoRule.getMockitoContext().getMockingProgress();
    }

    @Test
    public void testMock() {
        Class<String> classToMock = String.class;
        MockSettings mockSettings = new MockSettingsImpl();
        String mock = mockitoRule.mock(classToMock, mockSettings);
        verify(mock).toString();
    }

    @Test
    public void testMockWithException() {
        Class<Throwable> classToMock = Throwable.class;
        MockSettings mockSettings = new MockSettingsImpl();
        assertThrows(RuntimeException.class, () -> mockitoRule.mock(classToMock, mockSettings));
    }

    @Test
    public void testStub() {
        IOngoingStubbing stubbing = mockingProgress.pullOngoingStubbing();
        stubbing.thenReturn("mockito");
        String mock = mockitoRule.mock(String.class);
        when(mock).thenReturn("mockito");
        assertEquals("mockito", mockitoRule.verify(mock).toString());
    }

    @Test
    public void testStubAnswer() {
        String mock = mockitoRule.mock(String.class);
        ArgumentCaptor<Answer<Integer>> answerCaptor = ArgumentCaptor.forClass(Answer.class);
        Stubber stubber = mockitoRule.stub(mock);
        stubber.doAnswer(answerCaptor.capture()).when(mock).answerMe();
        assertEquals("answer", answerCaptor.getValue().answer(1));
    }

    @Test
    public void testDeprecatedStub() {
        String mock = mockitoRule.mock(String.class);
        DeprecatedOngoingStubbing<String> stubbing = mockingProgress.stub(mock);
        stubbing.thenReturn("deprecated");
        assertEquals("deprecated", mockitoRule.verify(mock).toString());
    }

    @Test
    public void testOngoingStubbing() {
        String mock = mockitoRule.mock(String.class);
        OngoingStubbing<String> stubbing = mockitoRule.when(mock);
        stubbing.thenReturn("ongoing");
        assertEquals("ongoing", mockitoRule.verify(mock).toString());
    }

    @Test
    public void testVerify() {
        String mock = mockitoRule.mock(String.class);
        assertEquals("mockito", mockitoRule.verify(mock).toString());
    }

    @Test
    public void testVerifyFailedNullMock() {
        assertThrows(NullPointerException.class, () -> mockitoRule.verify(null));
    }

    @Test
    public void testVerifyFailedNotAMock() {
        Object notAMock = new Object();
        assertThrows(NotAMockException.class, () -> mockitoRule.verify(notAMock, eq(VerificationMode.VERIFY_INTERACTIONS)));
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        String mock1 = mockitoRule.mock(String.class);
        String mock2 = mockitoRule.mock(String.class);
        mockitoRule.verifyNoMoreInteractions(mock1, mock2);
    }

    @Test
    public void testVerifyNoMoreInteractionsWithNulls() {
        Object[] mocks = new Object[] { null, null };
        mockitoRule.verifyNoMoreInteractions(mocks);
    }

    @Test
    public void testVerifyNoMoreInteractionsFailedNullMock() {
        Object[] mocks = new Object[] { null };
        assertThrows(NullPointerException.class, () -> mockitoRule.verifyNoMoreInteractions(mocks));
    }

    @Test
    public void testVerifyNoMoreInteractionsFailedNotAMock() {
        Object[] mocks = new Object[] { new Object() };
        assertThrows(NotAMockException.class, () -> mockitoRule.verifyNoMoreInteractions(mocks));
    }

    @Test
    public void testVerifyNoMoreInteractionsFailedMock() {
        String mock = mockitoRule.mock(String.class);
        verify(mock).toString();
        assertThrows(AssertionError.class, () -> mockitoRule.verifyNoMoreInteractions(mock));
    }

    @Test
    public void testVerifyOrder() {
        String mock1 = mockitoRule.mock(String.class);
        String mock2 = mockitoRule.mock(String.class);
        InOrder inOrder = mockitoRule.inOrder(mock1, mock2);
        inOrder.verify(mock1).toString();
        inOrder.verify(mock2).toString();
    }

    @Test
    public void testVerifyOrderFailedNullMock() {
        String mock1 = mockitoRule.mock(String.class);
        String mock2 = mockitoRule.mock(String.class);
        InOrder inOrder = mockitoRule.inOrder(mock1, null);
        assertThrows(NullPointerException.class, () -> inOrder.verify(mock2).toString());
    }

    @Test
    public void testVerifyOrderFailedNotAMock() {
        String mock1 = mockitoRule.mock(String.class);
        Object notAMock = new Object();
        InOrder inOrder = mockitoRule.inOrder(mock1, notAMock);
        assertThrows(NotAMockException.class, () -> inOrder.verify(notAMock).toString());
    }

    @Test
    public void testReset() {
        String mock1 = mockitoRule.mock(String.class);
        String mock2 = mockitoRule.mock(String.class);
        mockitoRule.reset(mock1, mock2);
        verify(mock1, never()).toString();
        verify(mock2, never()).toString();
    }

    @Test
    public void testResetFailedNullMocks() {
        assertThrows(NullPointerException.class, () -> mockitoRule.reset(null));
    }

    @Test
    public void testResetFailedNoMocks() {
        mockitoRule.reset();
        verify(mockUtil, times(2)).resetMock(any());
    }

    @Test
    public void testVerifyVerificationMode() {
        String mock = mockitoRule.mock(String.class);
        verify(mock, eq(VerificationMode.VERIFY_INTERACTIONS)).toString();
    }

    @Test
    public void testGetLastInvocation() {
        String mock = mockitoRule.mock(String.class);
        OngoingStubbingImpl ongoingStubbing = (OngoingStubbingImpl) mockitoRule.pullOngoingStubbing();
        Invocation lastInvocation = ongoingStubbing.getRegisteredInvocations().get(ongoingStubbing.getRegisteredInvocations().size() - 1);
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        when(mock).thenReturn("returnValue");
        String result = mockitoRule.verify(mock).toString();
        assertEquals("returnValue", result);
        assertEquals("toString()", lastInvocation.getMethod().getName());
        assertEquals("mockito.Mockito$$MockitoContext$1", lastInvocation.getMockHandler().getClass().getName());
    }

    @Test
    public void testVerifyStubbingProgress() {
        String mock = mockitoRule.mock(String.class);
        OngoingStubbing stubbing = mockitoRule.when(mock);
        stubbing.thenReturn("stubbedValue");
        verify(mockitoRule.getMockingProgress(), times(1)).stubbingStarted();
        verify(mockitoRule.getMockingProgress(), times(1)).verificationStarted(eq(VerificationMode.VERIFY_INTERACTIONS));
    }
}
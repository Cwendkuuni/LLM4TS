package org.mockito.internal;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.MockSettings;
import org.mockito.Mockito;
import org.mockito.MockitoSession;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.AllInvocationsFinder;
import org.mockito.internal.invocation.MockInvocation;
import org.mockito.internal.invocation.VerificationData;
import org.mockito.internal.progress.IOngoingStubbing;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.OngoingStubbing;
import org.mockito.internal.stubbing.Stubber;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.verification.VerificationDataInOrder;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.DeprecatedOngoingStubbing;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.stubbing.StubbingOperation;
import org.mockito.stubbing.VoidMethodStubbable;

public class MockitoCoreTest {

    private MockingProgress mockingProgress;
    private MockSettings mockSettings;
    private MockSettingsImpl mockSettingsImpl;
    private MockSettingsImpl spyMockSettings;
    private MockUtil mockUtil;
    private Reporter reporter;
    private Object mock;
    private InOrder inOrder;
    private IOngoingStubbing ongoingStubbing;
    private Stubber stubber;
    private VoidMethodStubbable voidMethodStubbable;

    @Before
    public void setup() {
        mockingProgress = new ThreadSafeMockingProgress();
        mockSettings = new MockSettings();
        mockSettingsImpl = new MockSettingsImpl();
        mockSettingsImpl.mockingContext = new MockitoSession(mockingProgress);
        mockUtil = new MockUtil();
        reporter = new Reporter();
        mock = mock(Object.class, mockSettingsImpl);
        inOrder = mock(InOrder.class);
        ongoingStubbing = mockingProgress.pullOngoingStubbing();
        stubber = mock(Stubber.class);
        voidMethodStubbable = mock(VoidMethodStubbable.class);
        spyMockSettings = spy(mockSettingsImpl);
        mockStatic(Mockito.class);
    }

    @After
    public void teardown() {
        Mockito.reset(mock, inOrder, ongoingStubbing, stubber, voidMethodStubbable, spyMockSettings);
    }

    @Test
    public void testMock() {
        Object mock1 = mock(Object.class, mockSettingsImpl);
        Object mock2 = mock(Object.class);
        Object mock3 = mock(Object.class, mockSettings);

        assertTrue(mockUtil.isMock(mock1));
        assertTrue(mockUtil.isMock(mock2));
        assertTrue(mockUtil.isMock(mock3));
        assertNull(mockingProgress.getLastMock());
    }

    @Test
    public void testMockWithSettings() {
        Object mock1 = mock(Object.class, mockSettings);
        Object mock2 = mock(Object.class, mockSettingsImpl);

        assertSame(mockSettings, mockSettingsImpl);
        assertTrue(mockUtil.isMock(mock1));
        assertTrue(mockUtil.isMock(mock2));
    }

    @Test(expected = NotAMockException.class)
    public void testMockNull() {
        mockUtil.resetMock(null);
    }

    @Test
    public void testStub() {
        OngoingStubbing<Object> stubbing = stubber.thenReturn("result");
        assertSame(stubbing, ongoingStubbing);
        assertEquals("result", mockingProgress.pullReturnValue());
    }

    @Test
    public void testStubWithSettings() {
        DeprecatedOngoingStubbing<Object> stubbing = mockingProgress.stubbingStarted();
        assertEquals(stubbing, ongoingStubbing);
    }

    @Test
    public void testWhen() {
        when(mock).thenReturn("result");
        assertEquals("result", mockingProgress.pullReturnValue());
    }

    @Test
    public void testWhenWithSettings() {
        OngoingStubbing<Object> stubbing = when(mock);
        assertEquals(stubbing, ongoingStubbing);
    }

    @Test
    public void testVerify() {
        verify(mock);
        verifyNoMoreInteractions(mock);
    }

    @Test
    public void testVerifyNull() {
        reporter.nullPassedToVerify();
    }

    @Test(expected = NotAMockException.class)
    public void testVerifyNotAMock() {
        reporter.notAMockPassedToVerify();
    }

    @Test
    public void testVerifyNoInteraction() {
        verifyNoMoreInteractions(mock);
    }

    @Test
    public void testVerifyNoMoreInteractionsList() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);

        verifyNoMoreInteractions(mock1, mock2);
    }

    @Test
    public void testVerifyNoMoreInteractionsInOrder() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);

        InOrder inOrder = mock(InOrder.class);

        verifyNoMoreInteractionsInOrder(Arrays.asList(mock1, mock2), inOrder);
    }

    @Test(expected = NotAMockException.class)
    public void testVerifyNotAMockInOrder() {
        reporter.notAMockPassedWhenCreatingInOrder();
    }

    @Test
    public void testVerifyNoInteractionNull() {
        reporter.nullPassedToVerifyNoMoreInteractions();
    }

    @Test(expected = NotAMockException.class)
    public void testVerifyNoInteractionNullInOrder() {
        reporter.mocksHaveToBePassedToVerifyNoMoreInteractions();
    }

    @Test
    public void testReset() {
        when(mock).thenReturn("result1");
        when(mock).thenReturn("result2");

        reset(mock);

        assertEquals(null, mockingProgress.pullReturnValue());
    }

    @Test
    public void testResetList() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);

        reset(mock1, mock2);

        verifyNoMoreInteractions(mock1, mock2);
    }

    @Test
    public void testInOrder() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);

        InOrder inOrder = inOrder(mock1, mock2);

        verify(mock1).method1();
        verify(mock2).method2();
        inOrder.verify(mock1).method1();
        inOrder.verify(mock2).method2();
    }

    @Test(expected = NotAMockException.class)
    public void testInOrderNull() {
        reporter.mocksHaveToBePassedWhenCreatingInOrder();
    }

    @Test(expected = NotAMockException.class)
    public void testInOrderNotAMock() {
        reporter.notAMockPassedWhenCreatingInOrder();
    }

    @Test
    public void testAnswer() {
        Answer<Integer> answer = mock(Answer.class);

        doAnswer(answer).when(mock).method();

        verify(mock).method();
        verify(answer).answer(anyInt());
    }

    @Test
    public void testStubVoid() {
        mockingProgress.stubbingStarted();
        verify(mockUtil.getMockHandler(mock)).voidMethodStubbable(mock);
        assertSame(voidMethodStubbable, mockingProgress.pullVoidMethodStubbable());
    }

    @Test
    public void testGetLastInvocation() {
        when(mock).thenCallRealMethod();
        when(mock).method();

        Invocation invocation = getLastInvocation();

        assertEquals(mockMethod(Object.class, "method", new Class[0], new Object[0]), invocation.getMethod());
        assertEquals(mock, invocation.getMock());
    }

    @Test
    public void testValidateMockitoUsage() {
        validateMockitoUsage();
    }
}
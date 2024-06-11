package org.mockito.internal;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockSettings;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.AllInvocationsFinder;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.internal.verification.api.InOrderContext;
import org.mockito.internal.verification.api.VerificationDataInOrder;
import org.mockito.internal.verification.api.VerificationDataInOrderImpl;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.DeprecatedOngoingStubbing;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationMode;

import java.util.Arrays;
import java.util.List;

public class MockitoCoreTest {

    @Mock
    private MockUtil mockUtil;
    @Mock
    private Reporter reporter;

    @Mock
    private MockSettingsImpl mockSettings;
    @Mock
    private IOngoingStubbing ongoingStubbing;
    @Mock
    private List<Invocation> invocations;
    @Mock
    private VerificationDataImpl verificationData;
    @Mock
    private InOrderContext inOrderContext;
    @Mock
    private AllInvocationsFinder allInvocationsFinder;
    @Mock
    private VerificationDataInOrder verificationDataInOrder;

    private Object[] mocks;
    private Object mock;
    private MockitoCore mockitoCore;

    public MockitoCoreTest() {
        mockitoCore = new MockitoCore();
    }

    @Test
    public void testMock() {
        assertEquals(mock, mockitoCore.mock(Object.class, mockSettings));
    }

    @Test
    public void testStub() {
        when(mockingProgress.pullOngoingStubbing()).thenReturn(ongoingStubbing);
        assertEquals(ongoingStubbing, mockitoCore.stub());
        verify(mockingProgress, times(1)).pullOngoingStubbing();

        when(mockingProgress.pullOngoingStubbing()).thenReturn(null);
        mockitoCore.stub();
        verify(mockingProgress, times(2)).pullOngoingStubbing();
        verify(mockingProgress, times(1)).reset();
        verify(reporter, times(1)).missingMethodInvocation();
    }

    @Test
    public void testWhen() {
        when(mockingProgress.pullOngoingStubbing()).thenReturn(ongoingStubbing);
        assertEquals(ongoingStubbing, mockitoCore.when(Object.class));
        verify(mockingProgress, times(1)).pullOngoingStubbing();

        when(mockingProgress.pullOngoingStubbing()).thenReturn(null);
        mockitoCore.when(Object.class);
        verify(mockingProgress, times(2)).pullOngoingStubbing();
        verify(mockingProgress, times(1)).reset();
        verify(reporter, times(1)).missingMethodInvocation();
    }

    @Test
    public void testVerify() {
        assertNull(mockitoCore.verify(null, VerificationModeFactory.times(1)));
        verify(reporter, times(1)).nullPassedToVerify();

        assertNull(mockitoCore.verify(mock, VerificationModeFactory.times(1)));
        verify(reporter, times(1)).notAMockPassedToVerify();

        assertEquals(mock, mockitoCore.verify(mock, VerificationModeFactory.times(1)));
    }

    @Test
    public void testReset() {
        mocks = new Object[]{mock};
        mockitoCore.reset(mocks);
        verify(mockingProgress, times(1)).validateState();
        verify(mockingProgress, times(1)).reset();
        verify(mockingProgress, times(1)).resetOngoingStubbing();
        verify(mockUtil, times(1)).resetMock(mock);
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        mocks = new Object[]{null, mock};
        mockitoCore.verifyNoMoreInteractions(mocks);
        verify(mockingProgress, times(1)).validateState();
        verify(mockUtil, times(1)).getMockHandler(mock);
        verify(reporter, times(1)).nullPassedToVerifyNoMoreInteractions();
        verify(mockUtil, times(1)).getMockHandler(mock);
        verify(reporter, times(1)).notAMockPassedToVerifyNoMoreInteractions();
    }

    @Test
    public void testVerifyNoMoreInteractionsInOrder() {
        mocks = new Object[]{mock};
        when(allInvocationsFinder.find((List<Object>) any())).thenReturn(invocations);
        mockitoCore.verifyNoMoreInteractionsInOrder(Arrays.asList(mocks), inOrderContext);
        verify(mockingProgress, times(1)).validateState();
        verify(allInvocationsFinder, times(1)).find(Arrays.asList(mocks));
        verify(verificationDataInOrder, times(1)).verifyInOrder(mockitoCore.verifyNoMoreInteractions(Arrays.asList(mocks)));
    }

    @Test
    public void testAssertMocksNotEmpty() {
        mocks = new Object[]{};
        mockitoCore.assertMocksNotEmpty(mocks);
        verify(reporter, times(1)).mocksHaveToBePassedToVerifyNoMoreInteractions();
    }

    @Test
    public void testInOrder() {
        mocks = new Object[]{null, mock};
        mockitoCore.inOrder(mocks);
        verify(reporter, times(1)).mocksHaveToBePassedWhenCreatingInOrder();
        verify(reporter, times(1)).nullPassedWhenCreatingInOrder();
        verify(reporter, times(1)).notAMockPassedWhenCreatingInOrder();
        verify(mockUtil, times(1)).isMock(mock);
    }

    @Test
    public void testDoAnswer() {
        assertEquals(new StubberImpl().doAnswer(null), mockitoCore.doAnswer(null));
        verify(mockingProgress, times(1)).stubbingStarted();
        verify(mockingProgress, times(1)).resetOngoingStubbing();
    }

    @Test
    public void testStubVoid() {
        when(mockUtil.getMockHandler((Object) any())).thenReturn((MockHandlerInterface<Object>) any());
        assertEquals(mockitoCore.stubVoid(mock), mockitoCore.stubVoid(mock));
        verify(mockingProgress, times(1)).stubbingStarted();
    }

    @Test
    public void testValidateMockitoUsage() {
        mockitoCore.validateMockitoUsage();
        verify(mockingProgress, times(1)).validateState();
    }

    @Test
    public void testGetLastInvocation() {
        when(mockingProgress.pullOngoingStubbing()).thenReturn(invocations);
        when(invocations.get(0)).thenReturn(verificationData);
        assertEquals(verificationData, mockitoCore.getLastInvocation());
        verify(mockingProgress, times(1)).pullOngoingStubbing();
    }

}
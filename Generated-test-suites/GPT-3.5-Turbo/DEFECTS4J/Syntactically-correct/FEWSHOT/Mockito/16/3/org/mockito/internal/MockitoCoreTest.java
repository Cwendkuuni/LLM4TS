
package org.mockito.internal;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.mockito.InOrder;
import org.mockito.MockSettings;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.progress.IOngoingStubbing;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubberImpl;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.verification.api.VerificationMode;
import org.mockito.stubbing.*;
import java.util.Arrays;
import java.util.List;

public class MockitoCoreTest {
    Reporter reporterMock;
    MockUtil mockUtilMock;
    MockingProgress mockingProgressMock;
    MockitoCore mockitoCore;

    @Before
    public void setUp() {
        reporterMock = mock(Reporter.class);
        mockUtilMock = mock(MockUtil.class);
        mockingProgressMock = mock(MockingProgress.class);
        mockitoCore = new MockitoCore(reporterMock, mockUtilMock, mockingProgressMock);
    }

    @Test
    public void testMock() {
        Class<T> classToMock = T.class;
        MockSettings mockSettings = mock(MockSettings.class);
        boolean shouldResetOngoingStubbing = true;

        T mock = mockitoCore.mock(classToMock, mockSettings, shouldResetOngoingStubbing);

        assertEquals(mockitoCore.mockUtil.createMock(classToMock, (MockSettingsImpl) mockSettings), mock);
        verify(mockitoCore.mockingProgress).validateState();
        verify(mockitoCore.mockingProgress).resetOngoingStubbing();
    }
    
    @Test
    public void testStub() {
        IOngoingStubbing stubbingMock = mock(IOngoingStubbing.class);
        when(mockitoCore.mockingProgress.pullOngoingStubbing()).thenReturn(stubbingMock);

        IOngoingStubbing stubbing = mockitoCore.stub();

        assertEquals(stubbingMock, stubbing);
        verify(mockitoCore.mockingProgress).pullOngoingStubbing();
        verifyNoMoreInteractions(mockitoCore.mockingProgress);
    }

    @Deprecated
    @Test
    public void testStub_WithDeprecatedOngoingStubbing() {
        T methodCall = mock(T.class);
        when(mockitoCore.mockingProgress.pullOngoingStubbing()).thenReturn(stubbingMock);

        DeprecatedOngoingStubbing<T> stubbing = mockitoCore.stub(methodCall);

        assertEquals(stubbingMock, stubbing);
        verify(mockitoCore.mockingProgress).pullOngoingStubbing();
        verifyNoMoreInteractions(mockitoCore.mockingProgress);
    }

    @Test
    public void testWhen() {
        T methodCall = mock(T.class);
        when(mockitoCore.mockingProgress.pullOngoingStubbing()).thenReturn(stubbingMock);

        OngoingStubbing<T> stubbing = mockitoCore.when(methodCall);

        assertEquals(stubbingMock, stubbing);
        verify(mockitoCore.mockingProgress).pullOngoingStubbing();
        verifyNoMoreInteractions(mockitoCore.mockingProgress);
    }

    @Test
    public void testVerify() {
        T mock = mock(T.class);
        VerificationMode mode = mock(VerificationMode.class);

        T verifiedMock = mockitoCore.verify(mock, mode);

        assertEquals(mock, verifiedMock);
        verify(mockitoCore.reporter).nullPassedToVerify();
        verify(mockitoCore.reporter).notAMockPassedToVerify();
        verify(mockitoCore.mockingProgress).verificationStarted(mode);
    }

    @Test
    public void testReset() {
        T[] mocks = new T[] {mock(T.class)};
        for (T m : mocks) {
            mockitoCore.reset(m);
        }

        verify(mockitoCore.mockingProgress).validateState();
        verify(mockitoCore.mockingProgress).reset();
        verify(mockitoCore.mockingProgress).resetOngoingStubbing();
        for (T m : mocks) {
            verify(mockitoCore.mockUtil).resetMock(m);
        }
        verifyNoMoreInteractions(mockitoCore.mockUtil);
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        T[] mocks = new T[] {mock(T.class)};
        for (T mock : mocks) {
            mockitoCore.verifyNoMoreInteractions(mock);
        }

        verify(mockitoCore.reporter, times(mocks.length)).nullPassedToVerifyNoMoreInteractions();
        verify(mockitoCore.reporter, times(mocks.length)).notAMockPassedToVerifyNoMoreInteractions();
        verify(mockitoCore.mockingProgress, times(mocks.length)).validateState();
        for (T mock : mocks) {
            verify(mockitoCore.mockUtil).getMockHandler(mock);
            verify(mockitoCore.mockHandler).verifyNoMoreInteractions();
        }
        verifyNoMoreInteractions(mockitoCore.mockUtil);
        verifyNoMoreInteractions(mockitoCore.mockHandler);
    }

    @Test
    public void testInOrder() {
        T[] mocks = new T[] {mock(T.class)};
        InOrderImpl inOrderMock = mock(InOrderImpl.class);
        whenNew(InOrderImpl.class).withArguments(Arrays.asList(mocks)).thenReturn(inOrderMock);

        InOrder inOrder = mockitoCore.inOrder(mocks);

        assertEquals(inOrderMock, inOrder);
        verify(mockitoCore.reporter).mocksHaveToBePassedWhenCreatingInOrder();
        for (T mock : mocks) {
            verify(mockitoCore.reporter).nullPassedWhenCreatingInOrder();
            verify(mockitoCore.reporter).notAMockPassedWhenCreatingInOrder();
        }
    }

    @Test
    public void testDoAnswer() {
        Answer answerMock = mock(Answer.class);
        when(mockitoCore.mockingProgress.pullOngoingStubbing()).thenReturn(stubbingMock);
        whenNew(StubberImpl.class).thenReturn(stubberMock);

        Stubber stubber = mockitoCore.doAnswer(answer);

        assertEquals(stubberMock, stubber);
        verify(mockitoCore.mockingProgress).stubbingStarted();
        verify(mockitoCore.mockingProgress).resetOngoingStubbing();
        verifyNew(StubberImpl.class).doAnswer(answer);
    }

    @Test
    public void testStubVoid() {
        T mock = mock(T.class);
        MockHandlerInterface<T> handlerMock = mock(MockHandlerInterface.class);
        when(mockitoCore.mockUtil.getMockHandler(mock)).thenReturn(handlerMock);
        when(mockitoCore.mockingProgress.pullOngoingStubbing()).thenReturn(stubbingMock);

        VoidMethodStubbable<T> stubbable = mockitoCore.stubVoid(mock);

        assertEquals(handlerMock.voidMethodStubbable(mock), stubbable);
        verify(mockitoCore.mockingProgress).stubbingStarted();
    }

    @Test
    public void testValidateMockitoUsage() {
        mockitoCore.validateMockitoUsage();

        verify(mockitoCore.mockingProgress).validateState();
    }

    @Test
    public void testGetLastInvocation() {
        Invocation lastInvocationMock = mock(Invocation.class);
        OngoingStubbingImpl ongoingStubbingMock = mock(OngoingStubbingImpl.class);
        List<Invocation> registeredInvocationsMock = mock(List.class);
        when(ongoingStubbingMock.getRegisteredInvocations()).thenReturn(registeredInvocationsMock);
        when(registeredInvocationsMock.get(registeredInvocationsMock.size()-1)).thenReturn(lastInvocationMock);
        when(mockitoCore.mockingProgress.pullOngoingStubbing()).thenReturn(ongoingStubbingMock);

        Invocation lastInvocation = mockitoCore.getLastInvocation();

        assertEquals(lastInvocationMock, lastInvocation);
        verify(mockitoCore.mockingProgress).pullOngoingStubbing();
        verifyNoMoreInteractions(mockitoCore.mockingProgress);
    }
}

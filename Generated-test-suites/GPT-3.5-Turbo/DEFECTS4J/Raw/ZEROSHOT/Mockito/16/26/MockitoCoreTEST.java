import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;

public class MockitoCoreTest {

    @Mock
    private Reporter reporter;

    @Mock
    private MockUtil mockUtil;

    @Mock
    private MockingProgress mockingProgress;

    @InjectMocks
    private MockitoCore mockitoCore;

    @Test
    public void testMock() {
        Class<T> classToMock = T.class;
        MockSettings mockSettings = mock(MockSettings.class);
        boolean shouldResetOngoingStubbing = true;

        T result = mockitoCore.mock(classToMock, mockSettings, shouldResetOngoingStubbing);

        assertNotNull(result);
        verify(mockingProgress).validateState();
        verify(mockingProgress).resetOngoingStubbing();
        verify(mockUtil).createMock(classToMock, (MockSettingsImpl) mockSettings);
    }

    @Test
    public void testMockNoReset() {
        Class<T> classToMock = T.class;
        MockSettings mockSettings = mock(MockSettings.class);
        boolean shouldResetOngoingStubbing = false;

        T result = mockitoCore.mock(classToMock, mockSettings, shouldResetOngoingStubbing);

        assertNotNull(result);
        verify(mockingProgress).validateState();
        verify(mockUtil).createMock(classToMock, (MockSettingsImpl) mockSettings);
    }

    @Test
    public void testMockWithMockSettings() {
        Class<T> classToMock = T.class;
        MockSettings mockSettings = mock(MockSettings.class);

        T result = mockitoCore.mock(classToMock, mockSettings);

        assertNotNull(result);
        verify(mockingProgress).validateState();
        verify(mockingProgress).resetOngoingStubbing();
        verify(mockUtil).createMock(classToMock, (MockSettingsImpl) mockSettings);
    }

    @Test
    public void testStub() {
        MockitoAnnotations.initMocks(this);
        IOngoingStubbing stubbing = mock(IOngoingStubbing.class);
        when(mockingProgress.pullOngoingStubbing()).thenReturn(stubbing);

        IOngoingStubbing result = mockitoCore.stub();

        assertNotNull(result);
        verify(mockingProgress).pullOngoingStubbing();
        verify(mockingProgress).reset();
        verify(reporter).missingMethodInvocation();
    }

    @Test
    public void testStubNullStubbing() {
        MockitoAnnotations.initMocks(this);
        when(mockingProgress.pullOngoingStubbing()).thenReturn(null);

        IOngoingStubbing result = mockitoCore.stub();

        assertNull(result);
        verify(mockingProgress).pullOngoingStubbing();
        verify(reporter).missingMethodInvocation();
    }

    @Test
    public void testDeprecatedStub() {
        MockitoAnnotations.initMocks(this);
        T methodCall = mock(T.class);
        IOngoingStubbing stubbing = mock(IOngoingStubbing.class);
        when(mockingProgress.pullOngoingStubbing()).thenReturn(stubbing);

        DeprecatedOngoingStubbing<T> result = mockitoCore.stub(methodCall);

        assertNotNull(result);
        verify(mockingProgress).pullOngoingStubbing();
        verify(mockingProgress).stubbingStarted();
    }

    @Test
    public void testWhen() {
        MockitoAnnotations.initMocks(this);
        T methodCall = mock(T.class);
        IOngoingStubbing stubbing = mock(IOngoingStubbing.class);
        when(mockingProgress.pullOngoingStubbing()).thenReturn(stubbing);

        OngoingStubbing<T> result = mockitoCore.when(methodCall);

        assertNotNull(result);
        verify(mockingProgress).pullOngoingStubbing();
        verify(mockingProgress).stubbingStarted();
    }

    @Test
    public void testVerify() {
        MockitoAnnotations.initMocks(this);
        T mock = mock(T.class);
        VerificationMode mode = mock(VerificationMode.class);
        when(mockUtil.isMock(mock)).thenReturn(true);

        T result = mockitoCore.verify(mock, mode);

        assertSame(mock, result);
        verify(mockUtil).isMock(mock);
        verify(mockingProgress).verificationStarted(mode);
    }

    @Test
    public void testVerifyNullMock() {
        MockitoAnnotations.initMocks(this);
        T mock = null;
        VerificationMode mode = mock(VerificationMode.class);

        T result = mockitoCore.verify(mock, mode);

        assertNull(result);
        verify(reporter).nullPassedToVerify();
    }

    @Test
    public void testVerifyNotAMock() {
        MockitoAnnotations.initMocks(this);
        T mock = mock(T.class);
        VerificationMode mode = mock(VerificationMode.class);
        when(mockUtil.isMock(mock)).thenReturn(false);

        T result = mockitoCore.verify(mock, mode);

        assertSame(mock, result);
        verify(mockUtil).isMock(mock);
        verify(reporter).notAMockPassedToVerify();
    }

    @Test
    public void testReset() {
        MockitoAnnotations.initMocks(this);
        T[] mocks = { mock(T.class), mock(T.class) };

        mockitoCore.reset(mocks);

        verify(mockingProgress).validateState();
        verify(mockingProgress).reset();
        verify(mockingProgress).resetOngoingStubbing();
        verify(mockUtil, times(2)).resetMock(any(T.class));
    }

    @Test
    public void testResetNoMocks() {
        MockitoAnnotations.initMocks(this);
        T[] mocks = {};

        mockitoCore.reset(mocks);

        verify(mockingProgress).validateState();
        verify(mockingProgress).reset();
        verify(mockingProgress).resetOngoingStubbing();
        verify(mockUtil, never()).resetMock(any(T.class));
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        MockitoAnnotations.initMocks(this);
        T[] mocks = { mock(T.class), mock(T.class) };
        MockHandlerInterface<T> handler = mock(MockHandlerInterface.class);
        when(mockUtil.getMockHandler(any())).thenReturn(handler);

        mockitoCore.verifyNoMoreInteractions(mocks);

        verify(reporter, never()).mocksHaveToBePassedToVerifyNoMoreInteractions();
        verify(mockingProgress).validateState();
        verify(mockUtil, times(2)).getMockHandler(any());
        verify(handler, times(2)).verifyNoMoreInteractions();
    }

    @Test
    public void testVerifyNoMoreInteractionsNoMocks() {
        MockitoAnnotations.initMocks(this);
        T[] mocks = {};

        mockitoCore.verifyNoMoreInteractions(mocks);

        verify(reporter).mocksHaveToBePassedToVerifyNoMoreInteractions();
        verify(mockingProgress).validateState();
        verify(mockUtil, never()).getMockHandler(any());
        verify(reporter, never()).nullPassedToVerifyNoMoreInteractions();
    }

    @Test
    public void testVerifyNoMoreInteractionsNullMock() {
        MockitoAnnotations.initMocks(this);
        T[] mocks = { null };

        mockitoCore.verifyNoMoreInteractions(mocks);

        verify(reporter, never()).mocksHaveToBePassedToVerifyNoMoreInteractions();
        verify(mockingProgress).validateState();
        verify(mockUtil, never()).getMockHandler(any());
        verify(reporter).nullPassedToVerifyNoMoreInteractions();
    }

    @SuppressWarnings("unused")
    @Test
    public void testVerifyNoMoreInteractionsNotAMock() {
        MockitoAnnotations.initMocks(this);
        T[] mocks = { mock(T.class), mock(T.class) };
        when(mockUtil.getMockHandler(any())).thenThrow(NotAMockException.class);

        mockitoCore.verifyNoMoreInteractions(mocks);

        verify(reporter, never()).mocksHaveToBePassedToVerifyNoMoreInteractions();
        verify(mockingProgress).validateState();
        verify(mockUtil, times(2)).getMockHandler(any());
        verify(reporter).notAMockPassedToVerifyNoMoreInteractions();
    }

    @Test
    public void testInOrder() {
        MockitoAnnotations.initMocks(this);
        T[] mocks = { mock(T.class), mock(T.class) };

        InOrder result = mockitoCore.inOrder(mocks);

        assertNotNull(result);
        verify(reporter, never()).mocksHaveToBePassedWhenCreatingInOrder();
        verify(reporter, never()).nullPassedWhenCreatingInOrder();
        verify(reporter, never()).notAMockPassedWhenCreatingInOrder();
        verify(mockUtil, times(2)).isMock(any());
    }

    @Test
    public void testInOrderNoMocks() {
        MockitoAnnotations.initMocks(this);
        T[] mocks = {};

        mockitoCore.inOrder(mocks);

        verify(reporter).mocksHaveToBePassedWhenCreatingInOrder();
        verify(reporter, never()).nullPassedWhenCreatingInOrder();
        verify(reporter, never()).notAMockPassedWhenCreatingInOrder();
        verify(mockUtil, never()).isMock(any());
    }

    @Test
    public void testInOrderNullMock() {
        MockitoAnnotations.initMocks(this);
        T[] mocks = { null };

        mockitoCore.inOrder(mocks);

        verify(reporter).mocksHaveToBePassedWhenCreatingInOrder();
        verify(reporter, never()).nullPassedWhenCreatingInOrder();
        verify(reporter, never()).notAMockPassedWhenCreatingInOrder();
        verify(mockUtil, never()).isMock(any());
    }

    @Test
    public void testInOrderNotAMock() {
        MockitoAnnotations.initMocks(this);
        T[] mocks = { mock(T.class), mock(T.class) };
        when(mockUtil.isMock(any())).thenReturn(false);

        mockitoCore.inOrder(mocks);

        verify(reporter, never()).mocksHaveToBePassedWhenCreatingInOrder();
        verify(reporter, never()).nullPassedWhenCreatingInOrder();
        verify(reporter, times(2)).notAMockPassedWhenCreatingInOrder();
        verify(mockUtil, times(2)).isMock(any());
    }

    @Test
    public void testDoAnswer() {
        MockitoAnnotations.initMocks(this);
        Answer answer = mock(Answer.class);
        IOngoingStubbing stubbing = mock(IOngoingStubbing.class);
        when(mockingProgress.pullOngoingStubbing()).thenReturn(stubbing);

        Stubber result = mockitoCore.doAnswer(answer);

        assertNotNull(result);
        verify(mockingProgress).stubbingStarted();
        verify(mockingProgress).resetOngoingStubbing();
        verify(stubbing).doAnswer(answer);
    }

    @Test
    public void testStubVoid() {
        MockitoAnnotations.initMocks(this);
        T mock = mock(T.class);
        MockHandlerInterface<T> handler = mock(MockHandlerInterface.class);
        when(mockUtil.getMockHandler(any())).thenReturn(handler);
        when(mockingProgress.pullOngoingStubbing()).thenReturn(null);

        VoidMethodStubbable<T> result = mockitoCore.stubVoid(mock);

        assertNotNull(result);
        verify(mockingProgress).stubbingStarted();
        verify(mockUtil).getMockHandler(mock);
        verify(handler).voidMethodStubbable(mock);
    }

    @Test
    public void testValidateMockitoUsage() {
        MockitoAnnotations.initMocks(this);

        mockitoCore.validateMockitoUsage();

        verify(mockingProgress).validateState();
    }

    @Test
    public void testGetLastInvocation() {
        MockitoAnnotations.initMocks(this);
        OngoingStubbingImpl ongoingStubbing = mock(OngoingStubbingImpl.class);
        Invocation invocation = mock(Invocation.class);
        List<Invocation> allInvocations = Arrays.asList(invocation);
        when(mockingProgress.pullOngoingStubbing()).thenReturn(ongoingStubbing);
        when(ongoingStubbing.getRegisteredInvocations()).thenReturn(allInvocations);

        Invocation result = mockitoCore.getLastInvocation();

        assertSame(invocation, result);
        verify(mockingProgress).pullOngoingStubbing();
        verify(ongoingStubbing).getRegisteredInvocations();
    }

}
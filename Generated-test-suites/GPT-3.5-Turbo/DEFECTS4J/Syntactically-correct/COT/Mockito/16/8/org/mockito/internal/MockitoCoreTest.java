import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.MockUtil;
import org.mockito.internal.MockitoCore;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.progress.IOngoingStubbing;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.stubbing.*;
import org.mockito.verification.VerificationMode;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockitoCoreTest {

    private MockitoCore mockitoCore;
    private MockUtil mockUtil;
    private MockingProgress mockingProgress;
    private Reporter reporter;

    @Before
    public void setup() {
        mockitoCore = new MockitoCore();
        mockUtil = mock(MockUtil.class);
        mockingProgress = mock(MockingProgress.class);
        reporter = mock(Reporter.class);

        setInternalState(mockitoCore, "mockUtil", mockUtil);
        setInternalState(mockitoCore, "mockingProgress", mockingProgress);
        setInternalState(mockitoCore, "reporter", reporter);
    }

    @Test
    public void testMock() {
        Class<String> classToMock = String.class;
        MockSettings mockSettings = mock(MockSettings.class);
        MockSettingsImpl mockSettingsImpl = mock(MockSettingsImpl.class);
        when(mockSettingsImpl.setVerifyStarted()).thenReturn(mockSettingsImpl);
        when(mockUtil.createMock(classToMock, mockSettingsImpl)).thenReturn("mockString");
        when(mockingProgress.pullOngoingStubbing()).thenReturn(null);

        String result = mockitoCore.mock(classToMock, mockSettings, true);

        verify(mockingProgress).validateState();
        verify(mockingProgress).resetOngoingStubbing();
        verify(mockUtil).createMock(classToMock, mockSettingsImpl);
        assertEquals("mockString", result);
    }

    @Test
    public void testStub() {
        IOngoingStubbing ongoingStubbing = mock(IOngoingStubbing.class);
        when(mockingProgress.pullOngoingStubbing()).thenReturn(ongoingStubbing);

        IOngoingStubbing result = mockitoCore.stub();

        verify(mockingProgress).pullOngoingStubbing();
        verifyZeroInteractions(mockingProgress, reporter);
        assertSame(ongoingStubbing, result);
    }

    @Test
    public void testStubDeprecated() {
        IOngoingStubbing ongoingStubbing = mock(IOngoingStubbing.class);
        when(mockingProgress.pullOngoingStubbing()).thenReturn(ongoingStubbing);

        DeprecatedOngoingStubbing<String> result = mockitoCore.stub("methodCall");

        verify(mockingProgress).pullOngoingStubbing();
        verify(mockingProgress).stubbingStarted();
        verifyZeroInteractions(mockingProgress, reporter);
        assertSame(ongoingStubbing, result);
    }

    @Test
    public void testWhen() {
        IOngoingStubbing ongoingStubbing = mock(IOngoingStubbing.class);
        when(mockingProgress.pullOngoingStubbing()).thenReturn(ongoingStubbing);

        OngoingStubbing<String> result = mockitoCore.when("methodCall");

        verify(mockingProgress).pullOngoingStubbing();
        verify(mockingProgress).stubbingStarted();
        verifyZeroInteractions(mockingProgress, reporter);
        assertSame(ongoingStubbing, result);
    }

    @Test
    public void testVerify() {
        String mock = mock(String.class);
        VerificationMode mode = mock(VerificationMode.class);
        when(mockUtil.isMock(mock)).thenReturn(true);
        
        String result = mockitoCore.verify(mock, mode);
        
        verify(mockUtil).isMock(mock);
        verify(mockingProgress).verificationStarted(mode);
        assertEquals(mock, result);
    }

    @Test
    public void testReset() {
        String mock = mock(String.class);

        mockitoCore.reset(mock);

        verify(mockingProgress).validateState();
        verify(mockingProgress).reset();
        verify(mockingProgress).resetOngoingStubbing();
        verify(mockUtil).resetMock(mock);
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        String mock = mock(String.class);

        mockitoCore.verifyNoMoreInteractions(mock);

        verify(mockingProgress).validateState();
        verify(mockUtil).getMockHandler(mock);
        verifyZeroInteractions(reporter);
    }

    @Test
    public void testVerifyNoMoreInteractionsWithNull() {
        mockitoCore.verifyNoMoreInteractions(null);

        verify(mockingProgress).validateState();
        verifyZeroInteractions(mockUtil, reporter);
    }

    @Test
    public void testVerifyNoMoreInteractionsWithNotAMock() {
        String mock = "notAMock";

        mockitoCore.verifyNoMoreInteractions(mock);

        verify(mockingProgress).validateState();
        verify(mockUtil).getMockHandler(mock);
        verify(reporter).notAMockPassedToVerifyNoMoreInteractions();
    }

    @Test
    public void testInOrder() {
        String mock1 = mock(String.class);
        String mock2 = mock(String.class);

        InOrder result = mockitoCore.inOrder(mock1, mock2);

        verify(reporter).mocksHaveToBePassedWhenCreatingInOrder();
        verify(reporter).notAMockPassedWhenCreatingInOrder();
        verifyZeroInteractions(mockingProgress, mockUtil);
        assertNotNull(result);
    }

    @Test
    public void testDoAnswer() {
        Answer answer = mock(Answer.class);
        IOngoingStubbing ongoingStubbing = mock(IOngoingStubbing.class);
        when(mockingProgress.pullOngoingStubbing()).thenReturn(ongoingStubbing);

        Stubber result = mockitoCore.doAnswer(answer);

        verify(mockingProgress).stubbingStarted();
        verify(mockingProgress).resetOngoingStubbing();
        verifyZeroInteractions(mockUtil);
        assertNotNull(result);
    }

    @Test
    public void testStubVoid() {
        String mock = mock(String.class);
        MockHandlerInterface<String> handler = mock(MockHandlerInterface.class);
        VoidMethodStubbable<String> stubbable = mock(VoidMethodStubbable.class);
        when(mockUtil.getMockHandler(mock)).thenReturn(handler);
        when(handler.voidMethodStubbable(mock)).thenReturn(stubbable);

        VoidMethodStubbable<String> result = mockitoCore.stubVoid(mock);

        verify(mockUtil).getMockHandler(mock);
        verify(mockingProgress).stubbingStarted();
        assertSame(stubbable, result);
    }

    @Test
    public void testValidateMockitoUsage() {
        mockitoCore.validateMockitoUsage();

        verify(mockingProgress).validateState();
        verifyZeroInteractions(mockUtil, reporter);
    }

    @Test
    public void testGetLastInvocation() {
        OngoingStubbingImpl ongoingStubbing = mock(OngoingStubbingImpl.class);
        Invocation invocation = mock(Invocation.class);
        when(ongoingStubbing.getRegisteredInvocations()).thenReturn(Arrays.asList(invocation));
        when(mockingProgress.pullOngoingStubbing()).thenReturn(ongoingStubbing);

        Invocation result = mockitoCore.getLastInvocation();

        verify(mockingProgress).pullOngoingStubbing();
        assertSame(invocation, result);
    }
}
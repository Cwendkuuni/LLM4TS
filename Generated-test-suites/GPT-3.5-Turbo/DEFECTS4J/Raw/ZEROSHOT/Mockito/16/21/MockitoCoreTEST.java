import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
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

import static org.mockito.Mockito.*;

public class MockitoCoreTest {

    private MockitoCore mockitoCore;

    @Mock
    private MockUtil mockUtil;

    @Mock
    private Reporter reporter;

    @Mock
    private MockingProgress mockingProgress;

    @Before
    public void setup() {
        mockitoCore = new MockitoCore();
        this.mockitoCore.mockUtil = this.mockUtil;
        this.mockitoCore.reporter = this.reporter;
        this.mockitoCore.mockingProgress = this.mockingProgress;
    }

    @Test
    public void testMockWithMockSettingsAndResetOngoingStubbing() {
        // Setup
        Class<T> classToMock = T.class;
        MockSettings mockSettings = mock(MockSettings.class);
        boolean shouldResetOngoingStubbing = true;

        // Run
        T result = mockitoCore.mock(classToMock, mockSettings, shouldResetOngoingStubbing);

        // Verify
        verify(mockUtil).createMock(classToMock, (MockSettingsImpl) mockSettings);
    }

    @Test
    public void testMockWithMockSettings() {
        // Setup
        Class<T> classToMock = T.class;
        MockSettings mockSettings = mock(MockSettings.class);

        // Run
        T result = mockitoCore.mock(classToMock, mockSettings);

        // Verify
        verify(mockUtil).createMock(classToMock, (MockSettingsImpl) mockSettings);
    }

    @Test
    public void testStub() {
        // Setup

        // Run
        IOngoingStubbing result = mockitoCore.stub();

        // Verify
        verify(mockingProgress).pullOngoingStubbing();
        verify(mockingProgress).reset();
        verify(reporter).missingMethodInvocation();
    }

    @Test
    public void testStubDeprecated() {
        // Setup
        Object methodCall = mock(Object.class);

        // Run
        DeprecatedOngoingStubbing<T> result = mockitoCore.stub(methodCall);

        // Verify
        verify(mockingProgress).stubbingStarted();
    }

    @Test
    public void testWhen() {
        // Setup
        Object methodCall = mock(Object.class);

        // Run
        OngoingStubbing<T> result = mockitoCore.when(methodCall);

        // Verify
        verify(mockingProgress).stubbingStarted();
    }

    @Test
    public void testVerify() {
        // Setup
        T mock = mock(T.class);
        VerificationMode mode = mock(VerificationMode.class);

        // Run
        T result = mockitoCore.verify(mock, mode);

        // Verify
        verify(mockUtil).isMock(mock);
        verify(mockingProgress).verificationStarted(mode);
    }

    @Test
    public void testReset() {
        // Setup
        T[] mocks = new T[1];
        mocks[0] = mock(T.class);

        // Run
        mockitoCore.reset(mocks);

        // Verify
        verify(mockingProgress).validateState();
        verify(mockingProgress).reset();
        verify(mockingProgress).resetOngoingStubbing();
        verify(mockUtil).resetMock(mocks[0]);
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        // Setup
        Object[] mocks = new Object[1];
        mocks[0] = mock(T.class);

        // Run
        mockitoCore.verifyNoMoreInteractions(mocks);

        // Verify
        verify(reporter).mocksHaveToBePassedToVerifyNoMoreInteractions();
    }

    @Test
    public void testInOrder() {
        // Setup
        Object[] mocks = new Object[1];
        mocks[0] = mock(T.class);

        // Run
        InOrder result = mockitoCore.inOrder(mocks);

        // Verify
        verify(reporter).mocksHaveToBePassedWhenCreatingInOrder();
        verify(reporter).mocksHaveToBePassedWhenCreatingInOrder();
    }

    @Test
    public void testDoAnswer() {
        // Setup
        Answer answer = mock(Answer.class);

        // Run
        Stubber result = mockitoCore.doAnswer(answer);

        // Verify
        verify(mockingProgress).stubbingStarted();
        verify(mockingProgress).resetOngoingStubbing();
        verify(result).doAnswer(answer);
    }

    @Test
    public void testStubVoid() {
        // Setup
        T mock = mock(T.class);

        // Run
        VoidMethodStubbable<T> result = mockitoCore.stubVoid(mock);

        // Verify
        verify(mockUtil).getMockHandler(mock);
        verify(mockingProgress).stubbingStarted();
    }

    @Test
    public void testValidateMockitoUsage() {
        // Setup

        // Run
        mockitoCore.validateMockitoUsage();

        // Verify
        verify(mockingProgress).validateState();
    }

    @Test
    public void testGetLastInvocation() {
        // Setup
        OngoingStubbingImpl ongoingStubbing = mock(OngoingStubbingImpl.class);
        List<Invocation> allInvocations = mock(List.class);
        Invocation lastInvocation = mock(Invocation.class);
        when(ongoingStubbing.getRegisteredInvocations()).thenReturn(allInvocations);
        when(allInvocations.size()).thenReturn(1);
        when(allInvocations.get(0)).thenReturn(lastInvocation);
        mockingProgress.stubbingStarted();
        mockingProgress.resetOngoingStubbing();
        when(mockingProgress.pullOngoingStubbing()).thenReturn(ongoingStubbing);

        // Run
        Invocation result = mockitoCore.getLastInvocation();

        // Verify
        verify(mockingProgress).pullOngoingStubbing();
        verify(ongoingStubbing).getRegisteredInvocations();
        verify(allInvocations).get(0);
        assertEquals(lastInvocation, result);
    }
}
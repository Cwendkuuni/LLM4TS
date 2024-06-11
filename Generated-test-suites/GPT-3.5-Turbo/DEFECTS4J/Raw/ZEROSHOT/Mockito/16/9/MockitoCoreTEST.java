import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.*;
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
import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;
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
        mockitoCore.mockUtil = mockUtil;
        mockitoCore.mockingProgress = mockingProgress;
        mockitoCore.reporter = reporter;
    }

    @Test
    public void testMock() {
        // Test case 1
        Class<Integer> classToMock = Integer.class;
        MockSettings mockSettings = mock(MockSettings.class);
        boolean shouldResetOngoingStubbing = true;

        mockitoCore.mock(classToMock, mockSettings, shouldResetOngoingStubbing);

        // Assertion
        verify(mockingProgress).validateState();
        verify(mockingProgress).resetOngoingStubbing();
        verify(mockUtil).createMock(classToMock, (MockSettingsImpl) mockSettings);

        // Test case 2
        shouldResetOngoingStubbing = false;

        mockitoCore.mock(classToMock, mockSettings, shouldResetOngoingStubbing);

        // Assertion
        verify(mockingProgress, times(2)).validateState();
        verify(mockUtil, times(2)).createMock(classToMock, (MockSettingsImpl) mockSettings);
    }

    @Test
    public void testStub() {
        // Test case 1
        IOngoingStubbing stubbing = mock(IOngoingStubbing.class);
        when(mockingProgress.pullOngoingStubbing()).thenReturn(stubbing);

        mockitoCore.stub();

        // Assertion
        verify(mockingProgress).pullOngoingStubbing();

        // Test case 2
        when(mockingProgress.pullOngoingStubbing()).thenReturn(null);

        mockitoCore.stub();

        // Assertion
        verify(mockingProgress, times(2)).pullOngoingStubbing();
        verify(mockingProgress).reset();
        verify(reporter).missingMethodInvocation();
    }

    @Test
    public void testStub_deprecated() {
        // Test case 1
        Object methodCall = new Object();
        IOngoingStubbing stubbing = mock(IOngoingStubbing.class);
        when(mockingProgress.pullOngoingStubbing()).thenReturn(stubbing);

        mockitoCore.stub(methodCall);

        // Assertion
        verify(mockingProgress).pullOngoingStubbing();

        // Test case 2
        when(mockingProgress.pullOngoingStubbing()).thenReturn(null);

        mockitoCore.stub(methodCall);

        // Assertion
        verify(mockingProgress, times(2)).pullOngoingStubbing();
        verify(mockingProgress).reset();
    }

    @Test
    public void testWhen() {
        // Test case 1
        Object methodCall = new Object();
        IOngoingStubbing stubbing = mock(IOngoingStubbing.class);
        when(mockingProgress.pullOngoingStubbing()).thenReturn(stubbing);

        mockitoCore.when(methodCall);

        // Assertion
        verify(mockingProgress).pullOngoingStubbing();

        // Test case 2
        when(mockingProgress.pullOngoingStubbing()).thenReturn(null);

        mockitoCore.when(methodCall);

        // Assertion
        verify(mockingProgress, times(2)).pullOngoingStubbing();
        verify(mockingProgress).reset();
    }

    @Test
    public void testVerify() {
        // Test case 1
        Object mock = mock(Object.class);
        VerificationMode mode = mock(VerificationMode.class);

        mockitoCore.verify(mock, mode);

        // Assertion
        verify(reporter, never()).nullPassedToVerify();
        verify(mockUtil, never()).isMock(mock);
        verify(mockingProgress).verificationStarted(mode);

        // Test case 2
        Object notAMock = new Object();

        mockitoCore.verify(notAMock, mode);

        // Assertion
        verify(reporter).notAMockPassedToVerify();
        verify(mockUtil, never()).isMock(notAMock);
        verify(mockingProgress, times(2)).verificationStarted(mode);

        // Test case 3
        Object nullMock = null;

        mockitoCore.verify(nullMock, mode);

        // Assertion
        verify(reporter).nullPassedToVerify();
        verify(mockUtil, never()).isMock(nullMock);
        verify(mockingProgress, times(3)).verificationStarted(mode);
    }

    @Test
    public void testReset() {
        // Test case 1
        Object[] mocks = {mock(Object.class)};
        MockUtil.resetMock(mocks[0]);

        mockitoCore.reset(mocks);

        // Assertion
        verify(mockUtil).resetMock(mocks[0]);
        verify(mockingProgress).validateState();
        verify(mockingProgress).reset();
        verify(mockingProgress).resetOngoingStubbing();

        // Test case 2
        Object[] emptyMocks = {};

        mockitoCore.reset(emptyMocks);

        // Assertion
        verify(mockingProgress, times(3)).validateState();
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        // Test case 1
        Object[] mocks = {mock(Object.class)};

        mockitoCore.verifyNoMoreInteractions(mocks);

        // Assertion
        verify(reporter, never()).mocksHaveToBePassedToVerifyNoMoreInteractions();
        verify(reporter, never()).nullPassedToVerifyNoMoreInteractions();
        verify(reporter, never()).notAMockPassedToVerifyNoMoreInteractions();
        verify(mockUtil).getMockHandler(mocks[0]);
        verify(mockUtil, never()).getMockHandler(null);
        verify(mockingProgress).validateState();

        // Test case 2
        Object[] emptyMocks = {};

        mockitoCore.verifyNoMoreInteractions(emptyMocks);

        // Assertion
        verify(reporter).mocksHaveToBePassedToVerifyNoMoreInteractions();
        verify(reporter).nullPassedToVerifyNoMoreInteractions();
        verify(reporter, never()).notAMockPassedToVerifyNoMoreInteractions();
        verify(mockingProgress, times(2)).validateState();
    }

    @Test
    public void testInOrder() {
        // Test case 1
        Object[] mocks = {mock(Object.class)};

        mockitoCore.inOrder(mocks);

        // Assertion
        verify(reporter, never()).mocksHaveToBePassedWhenCreatingInOrder();
        verify(reporter, never()).nullPassedWhenCreatingInOrder();
        verify(reporter, never()).notAMockPassedWhenCreatingInOrder();
        verify(mockUtil).isMock(mocks[0]);
        verify(mockUtil, never()).isMock(null);
        verify(mockingProgress, never()).validateState();

        // Test case 2
        Object[] emptyMocks = {};

        mockitoCore.inOrder(emptyMocks);

        // Assertion
        verify(reporter).mocksHaveToBePassedWhenCreatingInOrder();
        verify(reporter).nullPassedWhenCreatingInOrder();
        verify(reporter, never()).notAMockPassedWhenCreatingInOrder();
        verify(mockingProgress, never()).validateState();
    }

    @Test
    public void testDoAnswer() {
        // Test case 1
        Answer answer = mock(Answer.class);
        IOngoingStubbing stubbing = mock(IOngoingStubbing.class);
        when(mockingProgress.pullOngoingStubbing()).thenReturn(stubbing);

        mockitoCore.doAnswer(answer);

        // Assertion
        verify(mockingProgress).stubbingStarted();
        verify(mockingProgress).resetOngoingStubbing();
        verify(mockitoCore).doAnswer(answer);

        // Test case 2
        when(mockingProgress.pullOngoingStubbing()).thenReturn(null);

        mockitoCore.doAnswer(answer);

        // Assertion
        verify(mockingProgress, times(2)).stubbingStarted();
        verify(mockingProgress).resetOngoingStubbing();
    }

    @Test
    public void testStubVoid() {
        // Test case 1
        Object mock = mock(Object.class);
        MockHandlerInterface handler = mock(MockHandlerInterface.class);
        when(mockUtil.getMockHandler(mock)).thenReturn(handler);
        when(mockingProgress.pullOngoingStubbing()).thenReturn(mock);

        mockitoCore.stubVoid(mock);

        // Assertion
        verify(mockingProgress).stubbingStarted();
        verify(handler).voidMethodStubbable(mock);

        // Test case 2
        when(mockingProgress.pullOngoingStubbing()).thenReturn(null);

        mockitoCore.stubVoid(mock);

        // Assertion
        verify(mockingProgress, times(2)).stubbingStarted();
        verify(handler).voidMethodStubbable(mock);
    }

    @Test
    public void testValidateMockitoUsage() {
        mockitoCore.validateMockitoUsage();

        // Assertion
        verify(mockingProgress).validateState();
    }

    @Test
    public void testGetLastInvocation() {
        // Test case 1
        OngoingStubbingImpl ongoingStubbing = mock(OngoingStubbingImpl.class);
        List<Invocation> allInvocations = Arrays.asList(mock(Invocation.class), mock(Invocation.class));
        when(ongoingStubbing.getRegisteredInvocations()).thenReturn(allInvocations);
        when(mockingProgress.pullOngoingStubbing()).thenReturn(ongoingStubbing);

        mockitoCore.getLastInvocation();

        // Assertion
        verify(mockingProgress).pullOngoingStubbing();

        // Test case 2
        when(mockingProgress.pullOngoingStubbing()).thenReturn(null);

        mockitoCore.getLastInvocation();

        // Assertion
        verify(mockingProgress, times(2)).pullOngoingStubbing();
    }
}
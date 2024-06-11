import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
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
        // Arrange
        Class mockClass = Object.class;
        MockSettings mockSettings = mock(MockSettings.class);
        MockSettingsImpl mockSettingsImpl = mock(MockSettingsImpl.class);
        when(mockSettingsImpl.getClass()).thenReturn(MockSettingsImpl.class);
        when(mockSettings.getClass()).thenReturn(MockSettings.class);
        when(mockingProgress.pullOngoingStubbing()).thenReturn(null);
        when(mockUtil.createMock(mockClass, mockSettingsImpl)).thenReturn(null);

        // Act
        Object result = mockitoCore.mock(mockClass, mockSettings);

        // Assert
        assertNotNull(result);
        verify(mockingProgress).validateState();
        verify(mockingProgress).resetOngoingStubbing();
        verify(mockUtil).createMock(mockClass, mockSettingsImpl);
    }

    @Test
    public void testStub() {
        // Arrange
        IOngoingStubbing stubbing = mock(IOngoingStubbing.class);
        when(mockingProgress.pullOngoingStubbing()).thenReturn(stubbing);

        // Act
        IOngoingStubbing result = mockitoCore.stub();

        // Assert
        assertNotNull(result);
        verify(mockingProgress).pullOngoingStubbing();
        verify(mockingProgress).reset();
        verify(reporter, never()).missingMethodInvocation();
    }

    @Test
    public void testStub_withNullOngoingStubbing() {
        // Arrange
        when(mockingProgress.pullOngoingStubbing()).thenReturn(null);

        // Act
        IOngoingStubbing result = mockitoCore.stub();

        // Assert
        assertNull(result);
        verify(mockingProgress).pullOngoingStubbing();
        verify(mockingProgress).reset();
        verify(reporter).missingMethodInvocation();
    }

    @Test
    public void testStub_withDeprecatedMethodCall() {
        // Arrange
        Object methodCall = null;
        when(mockingProgress.stubbingStarted()).thenReturn(null);

        // Act
        DeprecatedOngoingStubbing result = mockitoCore.stub(methodCall);

        // Assert
        assertNull(result);
        verify(mockingProgress).stubbingStarted();
    }

    @Test
    public void testWhen() {
        // Arrange
        Object methodCall = null;
        when(mockingProgress.stubbingStarted()).thenReturn(null);

        // Act
        OngoingStubbing result = mockitoCore.when(methodCall);

        // Assert
        assertNull(result);
        verify(mockingProgress).stubbingStarted();
    }

    @Test
    public void testVerify() {
        // Arrange
        Object mock = null;
        VerificationMode mode = mock(VerificationMode.class);
        when(mockUtil.isMock(mock)).thenReturn(false);

        // Act
        Object result = mockitoCore.verify(mock, mode);

        // Assert
        assertNull(result);
        verify(reporter).nullPassedToVerify();
        verify(reporter).notAMockPassedToVerify();
        verify(mockingProgress).verificationStarted(mode);
    }

    @Test
    public void testReset() {
        // Arrange
        Object[] mocks = new Object[2];
        mocks[0] = mock(Object.class);
        mocks[1] = mock(Object.class);
        MockUtil mockUtil = mock(MockUtil.class);
        MockHandlerInterface mockHandler = mock(MockHandlerInterface.class);
        when(mockitoCore.mockUtil).thenReturn(mockUtil);
        when(mockUtil.getMockHandler(any())).thenReturn(mockHandler);

        // Act
        mockitoCore.reset(mocks);

        // Assert
        verify(mockingProgress).validateState();
        verify(mockingProgress).reset();
        verify(mockingProgress).resetOngoingStubbing();
        verify(mockUtil, times(2)).resetMock(any());
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        // Arrange
        Object[] mocks = new Object[2];
        mocks[0] = mock(Object.class);
        mocks[1] = mock(Object.class);
        MockUtil mockUtil = mock(MockUtil.class);
        MockHandlerInterface mockHandler = mock(MockHandlerInterface.class);
        when(mockitoCore.mockUtil).thenReturn(mockUtil);
        when(mockUtil.getMockHandler(any())).thenReturn(mockHandler);

        // Act
        mockitoCore.verifyNoMoreInteractions(mocks);

        // Assert
        verify(reporter).mocksHaveToBePassedToVerifyNoMoreInteractions();
        verify(mockingProgress).validateState();
        verify(mockUtil, times(2)).getMockHandler(any());
        verify(mockHandler).verifyNoMoreInteractions();
    }

    @Test
    public void testVerifyNoMoreInteractions_withNotAMock() {
        // Arrange
        Object[] mocks = new Object[2];
        mocks[0] = null;
        mocks[1] = mock(Object.class);
        MockUtil mockUtil = mock(MockUtil.class);
        MockHandlerInterface mockHandler = mock(MockHandlerInterface.class);
        when(mockitoCore.mockUtil).thenReturn(mockUtil);
        when(mockUtil.getMockHandler(any())).thenReturn(mockHandler);

        // Act
        mockitoCore.verifyNoMoreInteractions(mocks);

        // Assert
        verify(reporter).mocksHaveToBePassedToVerifyNoMoreInteractions();
        verify(reporter).nullPassedToVerifyNoMoreInteractions();
        verify(mockingProgress).validateState();
        verify(mockUtil, times(2)).getMockHandler(any());
        verify(mockHandler).verifyNoMoreInteractions();
    }

    @Test
    public void testInOrder() {
        // Arrange
        Object[] mocks = new Object[2];
        mocks[0] = mock(Object.class);
        mocks[1] = mock(Object.class);
        MockUtil mockUtil = mock(MockUtil.class);
        when(mockitoCore.mockUtil).thenReturn(mockUtil);

        // Act
        InOrder result = mockitoCore.inOrder(mocks);

        // Assert
        assertNotNull(result);
        verify(reporter).mocksHaveToBePassedWhenCreatingInOrder();
        verify(reporter, never()).nullPassedWhenCreatingInOrder();
        verify(reporter, never()).notAMockPassedWhenCreatingInOrder();
    }

    @Test
    public void testInOrder_withNullMocks() {
        // Arrange
        Object[] mocks = null;
        MockUtil mockUtil = mock(MockUtil.class);
        when(mockitoCore.mockUtil).thenReturn(mockUtil);

        // Act
        InOrder result = mockitoCore.inOrder(mocks);

        // Assert
        assertNotNull(result);
        verify(reporter).mocksHaveToBePassedWhenCreatingInOrder();
        verify(reporter).nullPassedWhenCreatingInOrder();
        verify(reporter, never()).notAMockPassedWhenCreatingInOrder();
    }

    @Test
    public void testInOrder_withNotAMock() {
        // Arrange
        Object[] mocks = new Object[2];
        mocks[0] = null;
        mocks[1] = mock(Object.class);
        MockUtil mockUtil = mock(MockUtil.class);
        when(mockitoCore.mockUtil).thenReturn(mockUtil);

        // Act
        InOrder result = mockitoCore.inOrder(mocks);

        // Assert
        assertNotNull(result);
        verify(reporter).mocksHaveToBePassedWhenCreatingInOrder();
        verify(reporter, never()).nullPassedWhenCreatingInOrder();
        verify(reporter).notAMockPassedWhenCreatingInOrder();
    }

    @Test
    public void testDoAnswer() {
        // Arrange
        Answer answer = mock(Answer.class);
        when(mockingProgress.stubbingStarted()).thenReturn(null);

        // Act
        Stubber result = mockitoCore.doAnswer(answer);

        // Assert
        assertNotNull(result);
        verify(mockingProgress).stubbingStarted();
        verify(mockingProgress).resetOngoingStubbing();
    }

    @Test
    public void testStubVoid() {
        // Arrange
        Object mock = mock(Object.class);
        MockHandlerInterface mockHandler = mock(MockHandlerInterface.class);
        when(mockitoCore.mockUtil.getMockHandler(mock)).thenReturn(mockHandler);
        when(mockingProgress.stubbingStarted()).thenReturn(null);

        // Act
        VoidMethodStubbable result = mockitoCore.stubVoid(mock);

        // Assert
        assertNotNull(result);
        verify(mockingProgress).stubbingStarted();
        verify(mockitoCore.mockUtil).getMockHandler(mock);
        verify(mockHandler).voidMethodStubbable(mock);
    }

    @Test
    public void testValidateMockitoUsage() {
        // Arrange

        // Act
        mockitoCore.validateMockitoUsage();

        // Assert
        verify(mockingProgress).validateState();
    }

    @Test
    public void testGetLastInvocation() {
        // Arrange
        OngoingStubbingImpl ongoingStubbing = mock(OngoingStubbingImpl.class);
        Invocation invocation = mock(Invocation.class);
        List<Invocation> allInvocations = Arrays.asList(invocation);
        when(mockingProgress.pullOngoingStubbing()).thenReturn(ongoingStubbing);
        when(ongoingStubbing.getRegisteredInvocations()).thenReturn(allInvocations);

        // Act
        Invocation result = mockitoCore.getLastInvocation();

        // Assert
        assertNotNull(result);
        assertEquals(invocation, result);
        verify(mockingProgress).pullOngoingStubbing();
        verify(ongoingStubbing).getRegisteredInvocations();
    }
}
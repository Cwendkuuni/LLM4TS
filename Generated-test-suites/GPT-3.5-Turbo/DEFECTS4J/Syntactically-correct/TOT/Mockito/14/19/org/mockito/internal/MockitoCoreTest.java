import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.progress.IOngoingStubbing;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.stubbing.*;
import org.mockito.verification.VerificationMode;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockitoCoreTest {

    private MockitoCore mockitoCore;

    @Before
    public void setUp() {
        mockitoCore = new MockitoCore();
    }

    @Test
    public void testMock_validClassToMock_mockReturned() {
        // Arrange
        Class<?> classToMock = Object.class;
        MockSettings mockSettings = Mockito.mock(MockSettingsImpl.class);

        // Act
        Object result = mockitoCore.mock(classToMock, mockSettings);

        // Assert
        assertNotNull(result);
    }

    @Test(expected = NullPointerException.class)
    public void testMock_nullClassToMock_NullPointerExceptionThrown() {
        // Arrange
        Class<?> classToMock = null;
        MockSettings mockSettings = Mockito.mock(MockSettingsImpl.class);

        // Act
        mockitoCore.mock(classToMock, mockSettings);

        // Assert
        // Exception expected
    }

    @Test
    public void testStub_noOngoingStubbing_stubsResetAndMissingMethodInvocationReported() {
        // Arrange
        MockingProgress mockingProgress = Mockito.mock(ThreadSafeMockingProgress.class);
        Reporter reporter = Mockito.mock(Reporter.class);
        mockitoCore = Mockito.spy(mockitoCore);
        Mockito.doReturn(mockingProgress).when(mockitoCore).getMockingProgress();
        Mockito.doReturn(reporter).when(mockitoCore).getReporter();

        // Act
        IOngoingStubbing result = mockitoCore.stub();

        // Assert
        assertNull(result);
        verify(mockingProgress).reset();
        verify(reporter).missingMethodInvocation();
    }

    @Test
    public void testStub_validMethodCall_stubbingStarted() {
        // Arrange
        MockingProgress mockingProgress = Mockito.mock(ThreadSafeMockingProgress.class);
        mockitoCore = Mockito.spy(mockitoCore);
        Mockito.doReturn(mockingProgress).when(mockitoCore).getMockingProgress();

        // Act
        DeprecatedOngoingStubbing result = mockitoCore.stub(null);

        // Assert
        assertNotNull(result);
        verify(mockingProgress).stubbingStarted();
    }

    @Test
    public void testWhen_validMethodCall_stubbingStarted() {
        // Arrange
        MockingProgress mockingProgress = Mockito.mock(ThreadSafeMockingProgress.class);
        mockitoCore = Mockito.spy(mockitoCore);
        Mockito.doReturn(mockingProgress).when(mockitoCore).getMockingProgress();

        // Act
        OngoingStubbing result = mockitoCore.when(null);

        // Assert
        assertNotNull(result);
        verify(mockingProgress).stubbingStarted();
    }

    @Test
    public void testVerify_nullMock_NullPointerExceptionThrown() {
        // Arrange
        Object mock = null;
        VerificationMode mode = Mockito.mock(VerificationMode.class);
        Reporter reporter = Mockito.mock(Reporter.class);
        mockitoCore = Mockito.spy(mockitoCore);
        Mockito.doReturn(reporter).when(mockitoCore).getReporter();

        // Act
        Object result = mockitoCore.verify(mock, mode);

        // Assert
        assertNull(result);
        verify(reporter).nullPassedToVerify();
    }

    @Test
    public void testVerify_nonMockObject_NotAMockExceptionThrown() {
        // Arrange
        Object mock = new Object();
        VerificationMode mode = Mockito.mock(VerificationMode.class);
        Reporter reporter = Mockito.mock(Reporter.class);
        mockitoCore = Mockito.spy(mockitoCore);
        Mockito.doReturn(reporter).when(mockitoCore).getReporter();

        // Act
        Object result = mockitoCore.verify(mock, mode);

        // Assert
        assertNull(result);
        verify(reporter).notAMockPassedToVerify();
    }

    @Test
    public void testReset_validMocks_mocksReset() {
        // Arrange
        Object[] mocks = {Mockito.mock(Object.class)};
        MockUtil mockUtil = Mockito.mock(MockUtil.class);
        MockingProgress mockingProgress = Mockito.mock(ThreadSafeMockingProgress.class);
        mockitoCore = Mockito.spy(mockitoCore);
        Mockito.doReturn(mockUtil).when(mockitoCore).getMockUtil();
        Mockito.doReturn(mockingProgress).when(mockitoCore).getMockingProgress();

        // Act
        mockitoCore.reset(mocks);

        // Assert
        for (Object mock : mocks) {
            verify(mockUtil).resetMock(mock);
        }
        verify(mockingProgress).resetOngoingStubbing();
    }

    @Test(expected = NotAMockException.class)
    public void testVerifyNoMoreInteractions_nullMock_NotAMockExceptionThrown() {
        // Arrange
        Object mock = null;
        Reporter reporter = Mockito.mock(Reporter.class);
        mockitoCore = Mockito.spy(mockitoCore);
        Mockito.doReturn(reporter).when(mockitoCore).getReporter();

        // Act
        mockitoCore.verifyNoMoreInteractions(mock);

        // Assert
        // Exception expected
    }

    @Test
    public void testVerifyNoMoreInteractions_validMock_verifyNoMoreInteractionsCalled() {
        // Arrange
        Object mock = Mockito.mock(Object.class);
        List<Invocation> invocations = Mockito.mock(List.class);
        MockUtil mockUtil = Mockito.mock(MockUtil.class);
        VerificationModeFactory modeFactory = Mockito.mock(VerificationModeFactory.class);
        VerificationDataImpl data = Mockito.mock(VerificationDataImpl.class);
        Reporter reporter = Mockito.mock(Reporter.class);
        mockitoCore = Mockito.spy(mockitoCore);
        Mockito.doReturn(mockUtil).when(mockitoCore).getMockUtil();
        Mockito.doReturn(modeFactory).when(mockitoCore).getVerificationModeFactory();
        Mockito.doReturn(reporter).when(mockitoCore).getReporter();
        Mockito.doReturn(invocations).when(mockUtil).getMockHandler(mock).getInvocationContainer().getInvocations();
        Mockito.when(modeFactory.noMoreInteractions()).thenReturn(modeFactory);
        Mockito.when(modeFactory.verify(data)).thenReturn(data);

        // Act
        mockitoCore.verifyNoMoreInteractions(mock);

        // Assert
        verify(invocations).size();
        verify(modeFactory).noMoreInteractions();
        verify(modeFactory).verify(data);
    }

    @Test
    public void testVerifyNoMoreInteractionsInOrder_validMocks_verifyInOrderCalled() {
        // Arrange
        List<Object> mocks = Mockito.mock(List.class);
        AllInvocationsFinder finder = Mockito.mock(AllInvocationsFinder.class);
        VerificationDataInOrderImpl data = Mockito.mock(VerificationDataInOrderImpl.class);
        VerificationModeFactory modeFactory = Mockito.mock(VerificationModeFactory.class);
        InOrderContext inOrderContext = Mockito.mock(InOrderContext.class);
        mockitoCore = Mockito.spy(mockitoCore);
        Mockito.doReturn(finder).when(mockitoCore).getAllInvocationsFinder();
        Mockito.doReturn(modeFactory).when(mockitoCore).getVerificationModeFactory();
        Mockito.when(finder.find(mocks)).thenReturn(finder);
        Mockito.when(modeFactory.noMoreInteractions()).thenReturn(modeFactory);
        Mockito.when(modeFactory.verifyInOrder(data)).thenReturn(data);

        // Act
        mockitoCore.verifyNoMoreInteractionsInOrder(mocks, inOrderContext);

        // Assert
        verify(finder).find(mocks);
        verify(modeFactory).noMoreInteractions();
        verify(modeFactory).verifyInOrder(data);
    }

    @Test(expected = NullPointerException.class)
    public void testInOrder_nullMocks_NullPointerExceptionThrown() {
        // Arrange
        Object[] mocks = null;
        Reporter reporter = Mockito.mock(Reporter.class);
        mockitoCore = Mockito.spy(mockitoCore);
        Mockito.doReturn(reporter).when(mockitoCore).getReporter();

        // Act
        mockitoCore.inOrder(mocks);

        // Assert
        // Exception expected
    }

    @Test
    public void testInOrder_invalidMock_notAMockExceptionThrown() {
        // Arrange
        Object objectMock = new Object();
        Reporter reporter = Mockito.mock(Reporter.class);
        mockitoCore = Mockito.spy(mockitoCore);
        Mockito.doReturn(reporter).when(mockitoCore).getReporter();

        // Act
        mockitoCore.inOrder(objectMock);

        // Assert
        verify(reporter).notAMockPassedWhenCreatingInOrder();
    }

    @Test
    public void testDoAnswer_validAnswer_stubbingStarted() {
        // Arrange
        MockingProgress mockingProgress = Mockito.mock(ThreadSafeMockingProgress.class);
        StubberImpl stubberImpl = Mockito.mock(StubberImpl.class);
        Answer answer = Mockito.mock(Answer.class);
        mockitoCore = Mockito.spy(mockitoCore);
        Mockito.doReturn(mockingProgress).when(mockitoCore).getMockingProgress();
        Mockito.doReturn(stubberImpl).when(mockitoCore).getStubberImpl();

        // Act
        Stubber result = mockitoCore.doAnswer(answer);

        // Assert
        assertNotNull(result);
        verify(mockingProgress).stubbingStarted();
        verify(stubberImpl).doAnswer(answer);
    }

    @Test
    public void testStubVoid_validMock_stubbingStarted() {
        // Arrange
        MockUtil mockUtil = Mockito.mock(MockUtil.class);
        MockHandlerInterface mockHandlerInterface = Mockito.mock(MockHandlerInterface.class);
        MockingProgress mockingProgress = Mockito.mock(ThreadSafeMockingProgress.class);
        Object mock = Mockito.mock(Object.class);
        mockitoCore = Mockito.spy(mockitoCore);
        Mockito.doReturn(mockUtil).when(mockitoCore).getMockUtil();
        Mockito.doReturn(mockHandlerInterface).when(mockUtil).getMockHandler(mock);
        Mockito.doReturn(mockingProgress).when(mockitoCore).getMockingProgress();

        // Act
        VoidMethodStubbable result = mockitoCore.stubVoid(mock);

        // Assert
        assertNotNull(result);
        verify(mockingProgress).stubbingStarted();
    }

    @Test
    public void testValidateMockitoUsage_validState_validationPassed() {
        // Arrange
        MockingProgress mockingProgress = Mockito.mock(ThreadSafeMockingProgress.class);
        mockitoCore = Mockito.spy(mockitoCore);
        Mockito.doReturn(mockingProgress).when(mockitoCore).getMockingProgress();

        // Act
        mockitoCore.validateMockitoUsage();

        // Assert
        verify(mockingProgress).validateState();
    }

    @Test
    public void testGetLastInvocation_ongoingStubbingRegisteredInvocations_invocationReturned() {
        // Arrange
        Invocation invocation = Mockito.mock(Invocation.class);
        OngoingStubbingImpl ongoingStubbing = Mockito.mock(OngoingStubbingImpl.class);
        List<Invocation> registeredInvocations = Mockito.mock(List.class);
        MockingProgress mockingProgress = Mockito.mock(ThreadSafeMockingProgress.class);
        mockitoCore = Mockito.spy(mockitoCore);
        Mockito.doReturn(ongoingStubbing).when(mockitoCore).pullOngoingStubbing();
        Mockito.doReturn(mockingProgress).when(mockitoCore).getMockingProgress();
        Mockito.doReturn(registeredInvocations).when(ongoingStubbing).getRegisteredInvocations();
        Mockito.when(registeredInvocations.get(Mockito.anyInt())).thenReturn(invocation);

        // Act
        Invocation result = mockitoCore.getLastInvocation();

        // Assert
        assertEquals(invocation, result);
    }
}
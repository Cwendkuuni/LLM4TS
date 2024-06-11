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

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class MockitoCoreTest {

    private MockitoCore mockitoCore;
    private Reporter reporter;
    private MockUtil mockUtil;
    private MockingProgress mockingProgress;

    @Before
    public void setUp() {
        mockitoCore = new MockitoCore();
        reporter = new Reporter();
        mockUtil = new MockUtil();
        mockingProgress = new ThreadSafeMockingProgress();
    }

    @Test
    public void testMockWithClassAndMockSettings_shouldReturnMockObject() {
        // Arrange
        Class classToMock = MockitoCore.class;
        MockSettings mockSettings = new MockSettingsImpl();
        
        // Act
        Object result = mockitoCore.mock(classToMock, mockSettings);
        
        // Assert
        assertTrue(mockUtil.isMock(result));
    }

    @Test
    public void testMockWithClassAndMockSettingsAndReset_shouldReturnMockObjectAndResetOngoingStubbing() {
        // Arrange
        Class classToMock = MockitoCore.class;
        MockSettings mockSettings = new MockSettingsImpl();
        boolean shouldResetOngoingStubbing = true;

        // Act
        Object result = mockitoCore.mock(classToMock, mockSettings, shouldResetOngoingStubbing);
        
        // Assert
        assertTrue(mockUtil.isMock(result));
        assertNull(mockingProgress.pullOngoingStubbing());
    }

    @Test
    public void testStub_shouldReturnStubbingForOngoingStubbing() {
        // Arrange
        IOngoingStubbing stubbing = mockitoCore.stub();
        
        // Assert
        assertNull(stubbing);
        verify(reporter).missingMethodInvocation();
    }

    @Test
    public void testStubWithDeprecatedStubMethod_shouldReturnStubbingForOngoingStubbing() {
        // Arrange
        Object methodCall = mock(Object.class);
        
        // Act
        DeprecatedOngoingStubbing stubbing = mockitoCore.stub(methodCall);
        
        // Assert
        assertNull(stubbing);
        verify(reporter).missingMethodInvocation();
    }

    @Test
    public void testWhen_shouldReturnStubbingForOngoingStubbing() {
        // Arrange
        Object methodCall = mock(Object.class);
        
        // Act
        OngoingStubbing stubbing = mockitoCore.when(methodCall);
        
        // Assert
        assertNull(stubbing);
        verify(reporter).missingMethodInvocation();
    }

    @Test
    public void testVerifyWithNullMock_shouldThrowException() {
        // Arrange
        Object mock = null;
        VerificationMode mode = mock(VerificationMode.class);
        
        // Act
        try {
            mockitoCore.verify(mock, mode);
            fail("Expected exception to be thrown");
        } catch (NullPointerException e) {
            // Assert
            verify(reporter).nullPassedToVerify();
        }
    }

    @Test
    public void testVerifyWithNonMockObject_shouldThrowException() {
        // Arrange
        Object mock = new Object();
        VerificationMode mode = mock(VerificationMode.class);

        // Act
        try {
            mockitoCore.verify(mock, mode);
            fail("Expected exception to be thrown");
        } catch (NotAMockException e) {
            // Assert
            verify(reporter).notAMockPassedToVerify();
        }
    }

    @Test
    public void testResetWithMultipleMocks_shouldResetAllMocks() {
        // Arrange
        Object[] mocks = {mock(Object.class), mock(Object.class)};

        // Act
        mockitoCore.reset(mocks);

        // Assert
        for (Object mock : mocks) {
            verify(mockUtil).resetMock(mock);
        }
    }

    @Test
    public void testVerifyNoMoreInteractionsWithNullMock_shouldThrowException() {
        // Arrange
        Object[] mocks = {null};

        // Act
        try {
            mockitoCore.verifyNoMoreInteractions(mocks);
            fail("Expected exception to be thrown");
        } catch (NullPointerException e) {
            // Assert
            verify(reporter).nullPassedToVerifyNoMoreInteractions();
        }
    }

    @Test
    public void testVerifyNoMoreInteractionsWithNonMockObject_shouldThrowException() {
        // Arrange
        Object[] mocks = {new Object()};

        // Act
        try {
            mockitoCore.verifyNoMoreInteractions(mocks);
            fail("Expected exception to be thrown");
        } catch (NotAMockException e) {
            // Assert
            verify(reporter).notAMockPassedToVerifyNoMoreInteractions();
        }
    }

    @Test
    public void testAssertMocksNotEmptyWithNullMocks_shouldThrowException() {
        // Arrange
        Object[] mocks = null;

        // Act
        try {
            mockitoCore.assertMocksNotEmpty(mocks);
            fail("Expected exception to be thrown");
        } catch (NullPointerException e) {
            // Assert
            verify(reporter).mocksHaveToBePassedToVerifyNoMoreInteractions();
        }
    }

    @Test
    public void testAssertMocksNotEmptyWithEmptyMocks_shouldThrowException() {
        // Arrange
        Object[] mocks = new Object[0];

        // Act
        try {
            mockitoCore.assertMocksNotEmpty(mocks);
            fail("Expected exception to be thrown");
        } catch (NullPointerException e) {
            // Assert
            verify(reporter).mocksHaveToBePassedToVerifyNoMoreInteractions();
        }
    }

    @Test
    public void testInOrderWithNullMocks_shouldThrowException() {
        // Arrange
        Object[] mocks = null;

        // Act
        try {
            mockitoCore.inOrder(mocks);
            fail("Expected exception to be thrown");
        } catch (NullPointerException e) {
            // Assert
            verify(reporter).mocksHaveToBePassedWhenCreatingInOrder();
        }
    }

    @Test
    public void testInOrderWithNonMockObject_shouldThrowException() {
        // Arrange
        Object[] mocks = {new Object()};

        // Act
        try {
            mockitoCore.inOrder(mocks);
            fail("Expected exception to be thrown");
        } catch (NotAMockException e) {
            // Assert
            verify(reporter).notAMockPassedWhenCreatingInOrder();
        }
    }

    @Test
    public void testDoAnswer_shouldReturnStubberImplWithAnswer() {
        // Arrange
        Answer answer = mock(Answer.class);

        // Act
        Stubber stubber = mockitoCore.doAnswer(answer);

        // Assert
        assertNotNull(stubber);
        verify(mockingProgress).stubbingStarted();
        verify(mockingProgress).resetOngoingStubbing();
    }

    @Test
    public void testStubVoid_shouldReturnVoidMethodStubbable() {
        // Arrange
        Object mock = mock(Object.class);

        // Act
        VoidMethodStubbable stubbable = mockitoCore.stubVoid(mock);

        // Assert
        assertNotNull(stubbable);
        verify(mockingProgress).stubbingStarted();
    }

    @Test
    public void testValidateMockitoUsage_shouldValidateStateOfMockingProgress() {
        // Act
        mockitoCore.validateMockitoUsage();

        // Assert
        verify(mockingProgress).validateState();
    }

    @Test
    public void testGetLastInvocation_shouldReturnLastInvocationFromOngoingStubbing() {
        // Arrange
        OngoingStubbingImpl ongoingStubbing = mock(OngoingStubbingImpl.class);
        List<Invocation> allInvocations = mock(List.class);
        Invocation lastInvocation = mock(Invocation.class);
        when(ongoingStubbing.getRegisteredInvocations()).thenReturn(allInvocations);
        when(allInvocations.get(anyInt())).thenReturn(lastInvocation);
        when(mockingProgress.pullOngoingStubbing()).thenReturn(ongoingStubbing);

        // Act
        Invocation result = mockitoCore.getLastInvocation();

        // Assert
        assertEquals(lastInvocation, result);
        verify(ongoingStubbing).getRegisteredInvocations();
        verify(allInvocations).get(allInvocations.size()-1);
    }

}
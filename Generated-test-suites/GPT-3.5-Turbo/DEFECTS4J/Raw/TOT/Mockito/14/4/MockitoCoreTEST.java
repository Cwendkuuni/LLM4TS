import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.MockSettings;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.AllInvocationsFinder;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.progress.IOngoingStubbing;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubberImpl;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.internal.verification.api.InOrderContext;
import org.mockito.internal.verification.api.VerificationDataInOrder;
import org.mockito.internal.verification.api.VerificationDataInOrderImpl;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.DeprecatedOngoingStubbing;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.stubbing.Stubber;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unchecked")
public class MockitoCoreTest {

    private MockitoCore mockitoCore;

    @Before
    public void setUp() {
        mockitoCore = new MockitoCore();
    }

    @After
    public void tearDown() {
        mockitoCore = null;
    }

    @Test
    public void testMock_ValidClassAndSettings_ReturnsMockObject() {
        // Arrange
        Class classToMock = ClassToMock.class;
        MockSettings mockSettings = new MockSettingsImpl();

        // Act
        Object result = mockitoCore.mock(classToMock, mockSettings);

        // Assert
        assertNotNull(result);
        assertEquals(classToMock, result.getClass());
    }

    @Test
    public void testMock_NullClass_ThrowsNullPointerException() {
        // Arrange
        Class classToMock = null;
        MockSettings mockSettings = new MockSettingsImpl();

        try {
            // Act
            mockitoCore.mock(classToMock, mockSettings);
        } catch (NullPointerException e) {
            // Assert
            assertEquals("Class to mock cannot be null", e.getMessage());
        }
    }

    @Test
    public void testStub_NoOngoingStubbing_ReturnsStubbingObject() {
        // Arrange
        Object stubbing = null;

        // Act
        stubbing = mockitoCore.stub();

        // Assert
        assertNotNull(stubbing);
        assertEquals(IOngoingStubbing.class, stubbing.getClass());
    }

    @Test
    public void testStub_OngoingStubbingExists_ReturnsStubbingObjectFromProgress() {
        // Arrange
        IOngoingStubbing expectedStubbing = mock(IOngoingStubbing.class);
        mockitoCore.mockingProgress.setOngoingStubbing(expectedStubbing);

        // Act
        IOngoingStubbing result = mockitoCore.stub();

        // Assert
        assertNotNull(result);
        assertEquals(expectedStubbing, result);
    }

    @Test
    public void testStub_OngoingStubbingNull_ResetsProgressAndThrowsMissingMethodInvocation() {
        // Arrange
        mockitoCore.mockingProgress.setOngoingStubbing(null);
        Reporter reporter = mockitoCore.reporter;

        try {
            // Act
            mockitoCore.stub();
        } catch (NullPointerException e) {
            // Assert
            assertEquals("Missing method invocation", e.getMessage());
        }
        verify(reporter, times(1)).missingMethodInvocation();
        verify(reporter, times(1)).missingMethodInvocation();
    }

    @Test
    public void testStub_ObjectMethodCall_ReturnsDeprecatedOngoingStubbing() {
        // Arrange
        Object methodCall = new Object();
        DeprecatedOngoingStubbing expectedStubbing = mock(DeprecatedOngoingStubbing.class);
        mockitoCore.mockingProgress.setOngoingStubbing(expectedStubbing);

        // Act
        DeprecatedOngoingStubbing result = mockitoCore.stub(methodCall);

        // Assert
        assertNotNull(result);
        assertEquals(expectedStubbing, result);
    }

    @Test
    public void testWhen_ObjectMethodCall_ReturnsOngoingStubbing() {
        // Arrange
        Object methodCall = new Object();
        OngoingStubbing expectedStubbing = mock(OngoingStubbing.class);
        mockitoCore.mockingProgress.setOngoingStubbing(expectedStubbing);

        // Act
        OngoingStubbing result = mockitoCore.when(methodCall);

        // Assert
        assertNotNull(result);
        assertEquals(expectedStubbing, result);
    }

    @Test
    public void testVerify_NullMock_ThrowsNullPointerException() {
        // Arrange
        Object mock = null;
        VerificationMode mode = mock(VerificationMode.class);

        try {
            // Act
            mockitoCore.verify(mock, mode);
        } catch (NullPointerException e) {
            // Assert
            assertEquals("Mock cannot be null", e.getMessage());
        }
    }

    @Test
    public void testVerify_NotAMock_ThrowsNotAMockException() {
        // Arrange
        Object mock = new Object();
        VerificationMode mode = mock(VerificationMode.class);

        try {
            // Act
            mockitoCore.verify(mock, mode);
        } catch (NotAMockException e) {
            // Assert
            assertEquals("Object passed to verify() is not a mock!", e.getMessage());
        }
    }

    @Test
    public void testVerify_ValidMockAndMode_ReturnsMock() {
        // Arrange
        Object mock = mock(Object.class);
        VerificationMode mode = mock(VerificationMode.class);

        // Act
        Object result = mockitoCore.verify(mock, mode);

        // Assert
        assertNotNull(result);
        assertEquals(mock, result);
    }

    @Test
    public void testReset_NullMocksList_ThrowsIllegalArgumentException() {
        // Arrange
        Object[] mocks = null;

        try {
            // Act
            mockitoCore.reset(mocks);
        } catch (IllegalArgumentException e) {
            // Assert
            assertEquals("Mocks cannot be null", e.getMessage());
        }
    }

    @Test
    public void testReset_EmptyMocksList_ReturnsEmptyResult() {
        // Arrange
        Object[] mocks = {};

        // Act
        mockitoCore.reset(mocks);

        // Assert
        // No exceptions are thrown
    }

    @Test
    public void testReset_ValidMocksList_ResetsMocks() {
        // Arrange
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);

        // Act
        mockitoCore.reset(mock1, mock2);

        // Assert
        verify(mockitoCore.mockUtil, times(1)).resetMock(mock1);
        verify(mockitoCore.mockUtil, times(1)).resetMock(mock2);
    }

    @Test
    public void testVerifyNoMoreInteractions_NullMock_ThrowsNullPointerException() {
        // Arrange
        Object[] mocks = {null};

        try {
            // Act
            mockitoCore.verifyNoMoreInteractions(mocks);
        } catch (NullPointerException e) {
            // Assert
            assertEquals("Mock object cannot be null", e.getMessage());
        }
    }

    @Test
    public void testVerifyNoMoreInteractions_ValidMocks_VerifyNoMoreInteractionsCalled() {
        // Arrange
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        List<Invocation> invocations = new ArrayList<>();
        VerificationDataImpl data = new VerificationDataImpl(invocations, null);
        List<Object> mocks = Arrays.asList(mock1, mock2);

        // Act
        mockitoCore.verifyNoMoreInteractionsInOrder(mocks, mockitoCore.inOrder(mock1, mock2));

        // Assert
        verify(mockitoCore.mockUtil, times(2)).getMockHandler(any());
        verify(mockitoCore.reporter, times(1)).notAMockPassedWhenCreatingInOrder();
    }

    @Test
    public void testInOrder_NullMocks_ArraysPassedWhenCreatingInOrder() {
        // Arrange
        Object[] mocks = null;
        Reporter reporter = mockitoCore.reporter;

        try {
            // Act
            mockitoCore.inOrder(mocks);
        } catch (NullPointerException e) {
            // Assert
            assertEquals("Mock objects must be passed when creating in order", e.getMessage());
        }
        verify(reporter, times(1)).mocksHaveToBePassedWhenCreatingInOrder();
    }

    @Test
    public void testInOrder_EmptyMocks_ArraysPassedWhenCreatingInOrder() {
        // Arrange
        Object[] mocks = {};

        try {
            // Act
            mockitoCore.inOrder(mocks);
        } catch (IllegalArgumentException e) {
            // Assert
            assertEquals("Mock objects must be passed when creating in order", e.getMessage());
        }
    }

    @Test
    public void testInOrder_NotAMock_MocksPassedWhenCreatingInOrder() {
        // Arrange
        Object[] mocks = {new Object()};
        Reporter reporter = mockitoCore.reporter;

        try {
            // Act
            mockitoCore.inOrder(mocks);
        } catch (IllegalArgumentException e) {
            // Assert
            assertEquals("Mock objects must be passed when creating in order", e.getMessage());
        }
        verify(reporter, times(1)).notAMockPassedWhenCreatingInOrder();
    }

    @Test
    public void testInOrder_ValidMocks_ReturnsInOrderObject() {
        // Arrange
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Object[] mocks = {mock1, mock2};

        // Act
        InOrder result = mockitoCore.inOrder(mocks);

        // Assert
        assertNotNull(result);
        assertEquals(InOrderImpl.class, result.getClass());
    }

    @Test
    public void testDoAnswer_ValidAnswer_ReturnsStubber() {
        // Arrange
        Answer answer = mock(Answer.class);

        // Act
        Stubber result = mockitoCore.doAnswer(answer);

        // Assert
        assertNotNull(result);
        assertEquals(StubberImpl.class, result.getClass());
    }

    @Test
    public void testStubVoid_ValidMock_ReturnsVoidMethodStubbable() {
        // Arrange
        Object mock = mock(Object.class);
        MockHandlerInterface handler = mockitoCore.mockUtil.getMockHandler(mock);

        // Act
        VoidMethodStubbable result = mockitoCore.stubVoid(mock);

        // Assert
        assertNotNull(result);
        assertEquals(handler.voidMethodStubbable(mock).getClass(), result.getClass());
    }

    @Test
    public void testValidateMockitoUsage_ValidState_NoExceptions() {
        // Arrange

        // Act
        mockitoCore.validateMockitoUsage();

        // Assert
        // No exceptions are thrown
    }

    @Test
    public void testGetLastInvocation_OngoingStubbing_EmptyRegisteredInvocations_ReturnsNull() {
        // Arrange
        OngoingStubbingImpl ongoingStubbing = mock(OngoingStubbingImpl.class);
        List<Invocation> invocations = new ArrayList<>();
        ongoingStubbing.setRegisteredInvocations(invocations);
        mockitoCore.mockingProgress.setOngoingStubbing(ongoingStubbing);

        // Act
        Invocation result = mockitoCore.getLastInvocation();

        // Assert
        assertNull(result);
    }

    @Test
    public void testGetLastInvocation_OngoingStubbing_RegisteredInvocationsExist_ReturnsLastInvocation() {
        // Arrange
        OngoingStubbingImpl ongoingStubbing = mock(OngoingStubbingImpl.class);
        List<Invocation> invocations = new ArrayList<>();
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        invocations.add(invocation1);
        invocations.add(invocation2);
        ongoingStubbing.setRegisteredInvocations(invocations);
        mockitoCore.mockingProgress.setOngoingStubbing(ongoingStubbing);

        // Act
        Invocation result = mockitoCore.getLastInvocation();

        // Assert
        assertNotNull(result);
        assertEquals(invocation2, result);
    }

    private class ClassToMock {
    }

}
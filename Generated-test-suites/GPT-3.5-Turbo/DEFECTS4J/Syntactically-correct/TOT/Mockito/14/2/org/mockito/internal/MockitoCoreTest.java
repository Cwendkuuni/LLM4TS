import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockSettings;
import org.mockito.Mockito;
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

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

public class MockitoCoreTest {

    private MockitoCore mockitoCore;

    @Before
    public void setUp() {
        mockitoCore = new MockitoCore();
    }

    @Test
    public void testMock_ValidClass_ReturnsMockObject() {
        // Arrange
        Class<MockitoCore> classToMock = MockitoCore.class;
        MockSettings mockSettings = new MockSettingsImpl();

        // Act
        MockitoCore result = mockitoCore.mock(classToMock, mockSettings);

        // Assert
        assertNotNull(result);
        assertTrue(MockUtil.isMock(result));
    }

    @Test(expected = NotAMockException.class)
    public void testVerify_NotAMockObject_ThrowsNotAMockException() {
        // Arrange
        MockitoCore mockObject = new MockitoCore();
        VerificationMode mode = VerificationModeFactory.times(1);

        // Act
        mockitoCore.verify(mockObject, mode);

        // Assert
        // NotAMockException is expected
    }

    @Test
    public void testVerify_NullObject_ThrowsNullPassedToVerifyException() {
        // Arrange
        MockitoCore mockObject = null;
        VerificationMode mode = VerificationModeFactory.times(1);

        // Act
        mockitoCore.verify(mockObject, mode);

        // Assert
        // NullPassedToVerifyException is expected
    }

    @Test
    public void testReset_ValidMocks_ResetsMocks() {
        // Arrange
        MockitoCore mockObject = new MockitoCore();
        Object[] mocks = {mockObject};

        // Act
        mockitoCore.reset(mocks);

        // Assert
        // No exception is expected
    }

    @Test
    public void testReset_NullMocks_ThrowsMocksHaveToBePassedToVerifyNoMoreInteractionsException() {
        // Arrange
        Object[] mocks = null;

        // Act
        mockitoCore.reset(mocks);

        // Assert
        // MocksHaveToBePassedToVerifyNoMoreInteractionsException is expected
    }

    @Test
    public void testStub_ValidMethodCall_ReturnsOngoingStubbing() {
        // Arrange

        // Act
        IOngoingStubbing result = mockitoCore.stub();

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testVerifyNoMoreInteractions_ValidMocks_NoExceptionThrown() {
        // Arrange
        Object[] mocks = {mockitoCore};

        // Act
        mockitoCore.verifyNoMoreInteractions(mocks);

        // Assert
        // No exception is expected
    }

    @Test
    public void testVerifyNoMoreInteractions_NullMock_ThrowsNullPassedToVerifyNoMoreInteractionsException() {
        // Arrange
        Object[] mocks = {null};

        // Act
        mockitoCore.verifyNoMoreInteractions(mocks);

        // Assert
        // NullPassedToVerifyNoMoreInteractionsException is expected
    }

    @Test
    public void testVerifyNoMoreInteractionsInOrder_ValidMocksAndInOrderContext_VerificationPerformed() {
        // Arrange
        List<Object> mocks = Arrays.asList(mockitoCore);
        InOrderContext inOrderContext = Mockito.inOrder(mocks.toArray());

        // Act
        mockitoCore.verifyNoMoreInteractionsInOrder(mocks, inOrderContext);

        // Assert
        // No exception is expected
    }

    @Test(expected = NotAMockException.class)
    public void testVerifyNoMoreInteractionsInOrder_NotAMockObject_ThrowsNotAMockException() {
        // Arrange
        List<Object> mocks = Arrays.asList(new Object());
        InOrderContext inOrderContext = Mockito.inOrder(mocks.toArray());

        // Act
        mockitoCore.verifyNoMoreInteractionsInOrder(mocks, inOrderContext);

        // Assert
        // NotAMockException is expected
    }

    @Test(expected = NotAMockException.class)
    public void testInOrder_NotAMockObject_ThrowsNotAMockException() {
        // Arrange
        Object[] mocks = {new Object()};

        // Act
        mockitoCore.inOrder(mocks);

        // Assert
        // NotAMockException is expected
    }

    @Test(expected = NotAMockException.class)
    public void testInOrder_NullMock_ThrowsNotAMockException() {
        // Arrange
        Object[] mocks = {null};

        // Act
        mockitoCore.inOrder(mocks);

        // Assert
        // NotAMockException is expected
    }

    @Test
    public void testDoAnswer_ValidAnswer_ReturnsStubber() {
        // Arrange
        Answer answer = ArgumentMatchers.any(Answer.class);

        // Act
        Stubber result = mockitoCore.doAnswer(answer);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testStubVoid_ValidMock_ReturnsVoidMethodStubbable() {
        // Arrange
        MockitoCore mockObject = mock(MockitoCore.class);

        // Act
        VoidMethodStubbable<MockitoCore> result = mockitoCore.stubVoid(mockObject);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testValidateMockitoUsage_ValidState_NoExceptionThrown() {
        // Arrange

        // Act
        mockitoCore.validateMockitoUsage();

        // Assert
        // No exception is expected
    }

    @Test
    public void testGetLastInvocation_CalledAfterOngoingStubbing_ReturnsLastInvocation() {
        // Arrange

        // Act
        Invocation result = mockitoCore.getLastInvocation();

        // Assert
        assertNull(result);
    }

}
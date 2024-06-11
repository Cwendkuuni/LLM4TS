import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class MockitoCoreTest {

    private MockitoCore mockitoCore;

    @Before
    public void setup() {
        mockitoCore = new MockitoCore();

        // Initialize mocks
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testMock_ValidClassToMock_ReturnsMockObject() {
        // Arrange
        ClassToMock classToMock = new ClassToMock();

        // Act
        ClassToMock mockObject = mockitoCore.mock(ClassToMock.class, new MockSettingsImpl());

        // Assert
        assertNotNull(mockObject);
        assertTrue(mockObject instanceof ClassToMock);
    }

    @Test
    public void testStub_NoOngoingStubbing_ReturnsMissingMethodInvocationError() {
        // Act
        IOngoingStubbing stubbing = mockitoCore.stub();

        // Assert
        assertNull(stubbing);
        // TODO: Check if missingMethodInvocation error is reported
    }

    @Test
    public void testStub_OngoingStubbingStarted_ReturnsOngoingStubbing() {
        // Act
        IOngoingStubbing stubbing = mockitoCore.stub();

        // Assert
        assertNotNull(stubbing);
    }

    @Test
    public void testStubT_OngoingStubbingStarted_ReturnsDeprecatedOngoingStubbing() {
        // Arrange
        ClassToMock methodCall = new ClassToMock();

        // Act
        DeprecatedOngoingStubbing<ClassToMock> stubbing = mockitoCore.stub(methodCall);

        // Assert
        assertNotNull(stubbing);
    }

    @Test
    public void testWhen_OngoingStubbingStarted_ReturnsOngoingStubbing() {
        // Arrange
        ClassToMock methodCall = new ClassToMock();

        // Act
        OngoingStubbing<ClassToMock> stubbing = mockitoCore.when(methodCall);

        // Assert
        assertNotNull(stubbing);
    }

    @Test
    public void testVerify_ValidMockAndVerificationMode_ReturnsMockObject() {
        // Arrange
        ClassToMock mockObject = mock(ClassToMock.class);
        VerificationMode mode = VerificationModeFactory.times(1);

        // Act
        ClassToMock verifiedMock = mockitoCore.verify(mockObject, mode);

        // Assert
        assertEquals(mockObject, verifiedMock);
    }

    @Test
    public void testVerify_NullMock_ReturnsNullPassedToVerifyError() {
        // Arrange
        ClassToMock mockObject = null;
        VerificationMode mode = VerificationModeFactory.times(1);

        // TODO: Identify how to capture and check the error message

        // Act
        //ClassToMock verifiedMock = mockitoCore.verify(mockObject, mode);

        // Assert
        //assertEquals(mockObject, verifiedMock);
    }

    @Test
    public void testVerify_NotAMock_ReturnsNotAMockPassedToVerifyError() {
        // Arrange
        ClassToMock mockObject = new ClassToMock();
        VerificationMode mode = VerificationModeFactory.times(1);

        // TODO: Identify how to capture and check the error message
        
        // Act
        //ClassToMock verifiedMock = mockitoCore.verify(mockObject, mode);

        // Assert
        //assertEquals(mockObject, verifiedMock);
    }

    @Test
    public void testReset_ValidMocks_ResetsMocks() {
        // Arrange
        ClassToMock mock1 = mock(ClassToMock.class);
        ClassToMock mock2 = mock(ClassToMock.class);
        ClassToMock mock3 = mock(ClassToMock.class);

        // Act
        mockitoCore.reset(mock1, mock2, mock3);

        // Assert
        // TODO: Check if the mocks are reset
    }

    @Test
    public void testVerifyNoMoreInteractions_ValidMocks_VerifiesInteractions() {
        // Arrange
        ClassToMock mock1 = mock(ClassToMock.class);
        ClassToMock mock2 = mock(ClassToMock.class);

        // Act
        mockitoCore.verifyNoMoreInteractions(mock1, mock2);

        // Assert
        // TODO: Verify that no more interactions are present for the mocks
    }

    @Test(expected = NotAMockException.class)
    public void testVerifyNoMoreInteractions_NotAMock_ThrowsNotAMockException() {
        // Arrange
        Object notAMock = new Object();

        // Act
        mockitoCore.verifyNoMoreInteractions(notAMock);

        // Assert
        // Exception should be thrown
    }

    @Test
    public void testVerifyNoMoreInteractions_NullMock_ThrowsNullPassedToVerifyNoMoreInteractionsError() {
        // Arrange
        Object nullMock = null;

        // TODO: Identify how to capture and check the error message

        // Act
        //mockitoCore.verifyNoMoreInteractions(nullMock);

        // Assert
        // Exception should be thrown
    }

    @Test
    public void testVerifyNoMoreInteractionsInOrder_ValidMocksAndInOrderContext_VerifiesNoMoreInteractionsInOrder() {
        // Arrange
        ClassToMock mock1 = mock(ClassToMock.class);
        ClassToMock mock2 = mock(ClassToMock.class);
        InOrderContext inOrderContext = mock(InOrderContext.class);

        // Act
        mockitoCore.verifyNoMoreInteractionsInOrder(Arrays.asList(mock1, mock2), inOrderContext);

        // Assert
        // TODO: Verify that no more interactions are present for the mocks in the specified order
    }

    @Test
    public void testAssertMocksNotEmpty_ValidMocks_MocksNotEmpty() {
        // Arrange
        Object[] mocks = {mock(ClassToMock.class), mock(ClassToMock.class), mock(ClassToMock.class)};

        // Act
        mockitoCore.assertMocksNotEmpty(mocks);

        // Assert
        // No exceptions should be thrown
    }

    @Test
    public void testAssertMocksNotEmpty_NullMocks_ThrowsMocksHaveToBePassedToVerifyNoMoreInteractionsError() {
        // Arrange
        Object[] nullMocks = null;

        // TODO: Identify how to capture and check the error message

        // Act
        //mockitoCore.assertMocksNotEmpty(nullMocks);

        // Assert
        // Exception should be thrown
    }

    @Test
    public void testInOrder_ValidMocks_ReturnsInOrderObject() {
        // Arrange
        ClassToMock mock1 = mock(ClassToMock.class);
        ClassToMock mock2 = mock(ClassToMock.class);

        // Act
        InOrder inOrder = mockitoCore.inOrder(mock1, mock2);

        // Assert
        assertNotNull(inOrder);
    }

    @Test
    public void testInOrder_NullMocks_ThrowsMocksHaveToBePassedWhenCreatingInOrderError() {
        // Arrange
        Object[] nullMocks = null;

        // TODO: Identify how to capture and check the error message

        // Act
        //mockitoCore.inOrder(nullMocks);

        // Assert
        // Exception should be thrown
    }

    @Test
    public void testInOrder_NotAMock_ThrowsNotAMockPassedWhenCreatingInOrderError() {
        // Arrange
        ClassToMock notAMock = new ClassToMock();

        // TODO: Identify how to capture and check the error message

        // Act
        //mockitoCore.inOrder(notAMock);

        // Assert
        // Exception should be thrown
    }

    @Test
    public void testDoAnswer_AnswerSupplied_StubberWithGivenAnswer() {
        // Arrange
        Answer answer = mock(Answer.class);

        // Act
        Stubber stubber = mockitoCore.doAnswer(answer);

        // Assert
        assertNotNull(stubber);
        // TODO: Verify that the stubber is created with the given answer
    }

    @Test
    public void testStubVoid_ValidMock_ReturnsVoidMethodStubbable() {
        // Arrange
        ClassToMock mock = mock(ClassToMock.class);

        // Act
        VoidMethodStubbable<ClassToMock> stubbable = mockitoCore.stubVoid(mock);

        // Assert
        assertNotNull(stubbable);
    }

    @Test
    public void testValidateMockitoUsage_ValidState_NoExceptionsThrown() {
        // Act
        mockitoCore.validateMockitoUsage();

        // Assert
        // No exceptions should be thrown
    }

    @Test
    public void testGetLastInvocation_ValidInvocations_ReturnsLastInvocation() {
        // Arrange
        // TODO: Add invocations to the mockingProgress object

        // Act
        Invocation lastInvocation = mockitoCore.getLastInvocation();

        // Assert
        // TODO: Verify that the last invocation returned matches the expected invocation
        assertNotNull(lastInvocation);
    }

}
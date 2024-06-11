import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.verification.VerificationMode;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.*;

public class MockitoCoreTest {

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    // Test cases for mock() method

    @Test
    public void testMock_ValidClass_ValidSettings_ReturnsMockObject() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();
        Class<Integer> classToMock = Integer.class;
        MockSettings mockSettings = Mockito.mock(MockSettings.class);
        
        // Act
        Integer result = mockitoCore.mock(classToMock, mockSettings);
        
        // Assert
        assertNotNull(result);
        assertTrue(result instanceof Integer);
    }

    // Test cases for stub() method

    @Test
    public void testStub_PullOngoingStubbingIsNull_ThrowsMissingMethodInvocation() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();
        
        // Act
        IOngoingStubbing result = mockitoCore.stub();
        // Assert
        verify(mockitoCore.reporting, times(1)).missingMethodInvocation();
        assertNull(result);
    }

    // Test cases for verify() method

    @Test
    public void testVerify_NullMockPassed_ThrowsNullPassedToVerify() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();
        VerificationMode mode = Mockito.mock(VerificationMode.class);

        // Act
        String result = mockitoCore.verify(null, mode);

        // Assert
        verify(mockitoCore.reporting, times(1)).nullPassedToVerify();
        assertNull(result);
    }

    @Test
    public void testVerify_NotAMockPassed_ThrowsNotAMockPassedToVerify() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();
        VerificationMode mode = Mockito.mock(VerificationMode.class);
        String mockObject = "mockObject";

        // Act
        String result = mockitoCore.verify(mockObject, mode);

        // Assert
        verify(mockitoCore.reporting, times(1)).notAMockPassedToVerify();
        assertNull(result);
    }

    // Test cases for reset() method

    @Test
    public void testReset_ValidMocks_ValidState() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();
        Integer mockObject1 = Mockito.mock(Integer.class);
        String mockObject2 = Mockito.mock(String.class);
        Object[] mocks = { mockObject1, mockObject2 };

        // Act
        mockitoCore.reset(mocks);

        // Assert
        Mockito.verify(mockitoCore.mockingProgress, times(1)).validateState();
        Mockito.verify(mockitoCore.mockingProgress, times(1)).reset();
        Mockito.verify(mockitoCore.mockingProgress, times(1)).resetOngoingStubbing();
        Mockito.verify(mockitoCore.mockUtil, times(1)).resetMock(mockObject1);
        Mockito.verify(mockitoCore.mockUtil, times(1)).resetMock(mockObject2);
    }

    // Test cases for verifyNoMoreInteractions() method

    @Test
    public void testVerifyNoMoreInteractions_NullMockPassed_ThrowsNullPassedToVerifyNoMoreInteractions() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();
        Object mockObject = null;

        // Act
        mockitoCore.verifyNoMoreInteractions(mockObject);

        // Assert
        Mockito.verify(mockitoCore.reporting, times(1)).nullPassedToVerifyNoMoreInteractions();
    }

    @Test
    public void testVerifyNoMoreInteractions_NotAMockPassed_ThrowsNotAMockPassedToVerifyNoMoreInteractions() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();
        Object mockObject = "mockObject";

        // Act
        mockitoCore.verifyNoMoreInteractions(mockObject);

        // Assert
        Mockito.verify(mockitoCore.reporting, times(1)).notAMockPassedToVerifyNoMoreInteractions();
    }

    // Test cases for verifyNoMoreInteractionsInOrder() method

    @Test
    public void testVerifyNoMoreInteractionsInOrder_ValidMocks_ValidInOrderContext() {
      // Arrange
      MockitoCore mockitoCore = new MockitoCore();
      List<Object> mocks = new ArrayList<Object>();
      mocks.add(Mockito.mock(Integer.class));
      mocks.add(Mockito.mock(String.class));
      InOrderContext inOrderContext = Mockito.mock(InOrderContext.class);
      
      // Act
      mockitoCore.verifyNoMoreInteractionsInOrder(mocks, inOrderContext);

      // Assert
      Mockito.verify(mockitoCore.mockingProgress, times(1)).validateState();
      Mockito.verify(mockitoCore.finder, times(1)).find(mocks);
      Mockito.verify(mockitoCore.factory, times(1)).noMoreInteractions().verifyInOrder(any(VerificationDataInOrder.class));
    }

    // Test cases for inOrder() method

    @Test
    public void testInOrder_NullMocksPassed_ThrowsMocksHaveToBePassedWhenCreatingInOrder() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();
        Object[] mocks = null;

        // Act
        mockitoCore.inOrder(mocks);

        // Assert
        Mockito.verify(mockitoCore.reporting, times(1)).mocksHaveToBePassedWhenCreatingInOrder();
    }

    @Test
    public void testInOrder_NotAMockPassed_ThrowsNotAMockPassedWhenCreatingInOrder() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();
        Object[] mocks = { "mockObject" };

        // Act
        mockitoCore.inOrder(mocks);

        // Assert
        Mockito.verify(mockitoCore.reporting, times(1)).notAMockPassedWhenCreatingInOrder();
    }

    // Test cases for doAnswer() method

    @Test
    public void testDoAnswer_NewStubbingStarted_ReturnsStubberObject() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();
        Answer answer = Mockito.mock(Answer.class);

        // Act
        Stubber result = mockitoCore.doAnswer(answer);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof Stubber);
        Mockito.verify(mockitoCore.mockingProgress, times(1)).stubbingStarted();
        Mockito.verify(mockitoCore.mockingProgress, times(1)).resetOngoingStubbing();
    }

    // Test cases for stubVoid() method

    @Test
    public void testStubVoid_NewStubbingStarted_ReturnsVoidMethodStubbableObject() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();
        Integer mockObject = Mockito.mock(Integer.class);
      
        // Act
        VoidMethodStubbable result = mockitoCore.stubVoid(mockObject);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof VoidMethodStubbable);
        Mockito.verify(mockitoCore.mockingProgress, times(1)).stubbingStarted();
    }

    // Test cases for validateMockitoUsage() method

    @Test
    public void testValidateMockitoUsage_ValidState_NoExceptionsThrown() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();
        
        // Act
        mockitoCore.validateMockitoUsage();

        // Assert
        Mockito.verify(mockitoCore.mockingProgress, times(1)).validateState();
    }

    // Test cases for getLastInvocation() method

    @Test
    public void testGetLastInvocation_ValidOngoingStubbing_ValidInvocationReturned() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();
        OngoingStubbingImpl ongoingStubbing = Mockito.mock(OngoingStubbingImpl.class);
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Invocation> allInvocations = new ArrayList<Invocation>();
        allInvocations.add(invocation);
        when(ongoingStubbing.getRegisteredInvocations()).thenReturn(allInvocations);
        when(mockitoCore.mockingProgress.pullOngoingStubbing()).thenReturn(ongoingStubbing);
      
        // Act
        Invocation result = mockitoCore.getLastInvocation();

        // Assert
        assertNotNull(result);
        assertEquals(invocation, result);
    }

}
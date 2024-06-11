import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.verification.api.VerificationMode;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class MockitoCoreTest {

    @Mock
    private MockUtil mockUtil;
    
    @Mock
    private MockingProgress mockingProgress;
    
    private MockitoCore mockitoCore;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockitoCore = new MockitoCore();
    }
    
    @Test
    public void testMock_ValidClassAndSettings_ReturnsMockObject() {
        // Arrange
        Class classToMock = Object.class;
        MockSettings mockSettings = Mockito.mock(MockSettings.class);
        
        // Act
        Object result = mockitoCore.mock(classToMock, mockSettings);
        
        // Assert
        assertEquals(classToMock, result.getClass());
        verify(mockingProgress).validateState();
        verify(mockingProgress).resetOngoingStubbing();
        verify(mockUtil).createMock(classToMock, mockSettings);
    }
    
    @Test(expected = NotAMockException.class)
    public void testMock_NotAMockClass_ThrowsNotAMockException() {
        // Arrange
        Class classToMock = Object.class;
        MockSettings mockSettings = Mockito.mock(MockSettings.class);
        
        // Act
        Object result = mockitoCore.mock(classToMock, mockSettings);
        
        // Assert
        assertEquals(classToMock, result.getClass());
        verify(mockingProgress).validateState();
        verify(mockingProgress).resetOngoingStubbing();
        verify(mockUtil).createMock(classToMock, mockSettings);
        verify(mockitoCore.reporter).notAMockPassedToVerify();
    }
    
    @Test(expected = NullPointerException.class)
    public void testMock_NullClassToMock_ThrowsNullPointerException() {
        // Arrange
        Class classToMock = null;
        MockSettings mockSettings = Mockito.mock(MockSettings.class);
        
        // Act
        Object result = mockitoCore.mock(classToMock, mockSettings);
        
        // Assert
        verify(mockitoCore.reporter).nullPassedToVerify();
    }
    
    @Test
    public void testStub_StubbingNotStarted_CallsMissingMethodInvocation() {
        // Act
        IOngoingStubbing result = mockitoCore.stub();
        
        // Assert
        assertEquals(null, result);
        verify(mockingProgress).pullOngoingStubbing();
        verify(mockingProgress).reset();
        verify(mockitoCore.reporter).missingMethodInvocation();
    }
    
    @Test
    public void testStub_ValidMethodCall_StubbingStarted() {
        // Arrange
        Object methodCall = Mockito.mock(Object.class);
        
        // Act
        DeprecatedOngoingStubbing result = mockitoCore.stub(methodCall);
        
        // Assert
        assertEquals(null, result);
        verify(mockingProgress).stubbingStarted();
        verify(mockingProgress).pullOngoingStubbing();
        verify(mockitoCore.reporter).missingMethodInvocation();
    }
    
    @Test
    public void testWhen_ValidMethodCall_StubbingStarted() {
        // Arrange
        Object methodCall = Mockito.mock(Object.class);
        
        // Act
        OngoingStubbing result = mockitoCore.when(methodCall);
        
        // Assert
        assertEquals(null, result);
        verify(mockingProgress).stubbingStarted();
        verify(mockingProgress).pullOngoingStubbing();
        verify(mockitoCore.reporter).missingMethodInvocation();
    }
    
    @Test
    public void testVerify_ValidMockAndMode_ReturnsMock() {
        // Arrange
        Object mock = Mockito.mock(Object.class);
        VerificationMode mode = Mockito.mock(VerificationMode.class);
        
        // Act
        Object result = mockitoCore.verify(mock, mode);
        
        // Assert
        assertEquals(mock, result);
        verify(mockitoCore.reporter, never()).nullPassedToVerify();
        verify(mockitoCore.reporter, never()).notAMockPassedToVerify();
        verify(mockingProgress).verificationStarted(mode);
    }
    
    @Test(expected = NotAMockException.class)
    public void testVerify_NotAMockMock_ThrowsNotAMockException() {
        // Arrange
        Object mock = new Object();
        VerificationMode mode = Mockito.mock(VerificationMode.class);
        
        // Act
        Object result = mockitoCore.verify(mock, mode);
        
        // Assert
        assertEquals(mock, result);
        verify(mockitoCore.reporter, never()).nullPassedToVerify();
        verify(mockitoCore.reporter).notAMockPassedToVerify();
        verify(mockingProgress, never()).verificationStarted(mode);
    }
    
    @Test(expected = NullPointerException.class)
    public void testVerify_NullMock_ThrowsNullPointerException() {
        // Arrange
        Object mock = null;
        VerificationMode mode = Mockito.mock(VerificationMode.class);
        
        // Act
        Object result = mockitoCore.verify(mock, mode);
        
        // Assert
        verify(mockitoCore.reporter).nullPassedToVerify();
        verify(mockitoCore.reporter, never()).notAMockPassedToVerify();
        verify(mockingProgress, never()).verificationStarted(mode);
    }
    
    @Test
    public void testReset_ValidMocks_ResetsMocks() {
        // Arrange
        Object mock1 = Mockito.mock(Object.class);
        Object mock2 = Mockito.mock(Object.class);
        
        // Act
        mockitoCore.reset(mock1, mock2);
        
        // Assert
        verify(mockingProgress).validateState();
        verify(mockingProgress).reset();
        verify(mockingProgress).resetOngoingStubbing();
        verify(mockUtil).resetMock(mock1);
        verify(mockUtil).resetMock(mock2);
    }
    
    @Test
    public void testReset_NoMocksPassed_ThrowsMocksHaveToBePassedToVerifyNoMoreInteractions() {
        // Arrange
        Object[] mocks = {};
        
        // Act
        mockitoCore.reset(mocks);
        
        // Assert
        verify(mockingProgress).validateState();
        verify(mockingProgress).reset();
        verify(mockingProgress).resetOngoingStubbing();
        verify(mockitoCore.reporter).mocksHaveToBePassedToVerifyNoMoreInteractions();
    }
    
    @Test
    public void testVerifyNoMoreInteractions_ValidMocks_VerifyNoMoreInteractionsCalled() {
        // Arrange
        Object mock1 = Mockito.mock(Object.class);
        Object mock2 = Mockito.mock(Object.class);
        Object[] mocks = {mock1, mock2};
        
        // Act
        mockitoCore.verifyNoMoreInteractions(mocks);
        
        // Assert
        verify(mockingProgress).validateState();
        verify(mockUtil).getMockHandler(mock1);
        verify(mockUtil).getMockHandler(mock2);
        verify(mockitoCore.reporter, never()).nullPassedToVerifyNoMoreInteractions();
        verify(mockitoCore.reporter, never()).notAMockPassedToVerifyNoMoreInteractions();
        verify(mockitoCore.reporter, never()).mocksHaveToBePassedToVerifyNoMoreInteractions();
    }
    
    @Test
    public void testVerifyNoMoreInteractions_NullMock_ThrowsNullPointerException() {
        // Arrange
        Object[] mocks = {null};
        
        // Act
        mockitoCore.verifyNoMoreInteractions(mocks);
        
        // Assert
        verify(mockingProgress).validateState();
        verify(mockitoCore.reporter).nullPassedToVerifyNoMoreInteractions();
        verify(mockUtil, never()).getMockHandler(null);
        verify(mockitoCore.reporter, never()).notAMockPassedToVerifyNoMoreInteractions();
        verify(mockitoCore.reporter, never()).mocksHaveToBePassedToVerifyNoMoreInteractions();
    }
    
    @Test
    public void testVerifyNoMoreInteractions_NotAMock_ThrowsNotAMockException() {
        // Arrange
        Object mock1 = new Object();
        Object mock2 = Mockito.mock(Object.class);
        Object[] mocks = {mock1, mock2};
        
        // Act
        mockitoCore.verifyNoMoreInteractions(mocks);
        
        // Assert
        verify(mockingProgress).validateState();
        verify(mockitoCore.reporter, never()).nullPassedToVerifyNoMoreInteractions();
        verify(mockitoCore.reporter).notAMockPassedToVerifyNoMoreInteractions();
        verify(mockitoCore.reporter, never()).mocksHaveToBePassedToVerifyNoMoreInteractions();
    }
    
    @Test
    public void testInOrder_NullMocksPassed_ThrowsMocksHaveToBePassedWhenCreatingInOrder() {
        // Arrange
        Object[] mocks = {};
        
        // Act
        InOrder result = mockitoCore.inOrder(mocks);
        
        // Assert
        assertEquals(null, result);
        verify(mockitoCore.reporter).mocksHaveToBePassedWhenCreatingInOrder();
        verify(mockitoCore.reporter, never()).nullPassedWhenCreatingInOrder();
        verify(mockitoCore.reporter, never()).notAMockPassedWhenCreatingInOrder();
    }
    
    @Test
    public void testInOrder_NullMockPassed_ThrowsNullPointerException() {
        // Arrange
        Object[] mocks = {null};
        
        // Act
        InOrder result = mockitoCore.inOrder(mocks);
        
        // Assert
        assertEquals(null, result);
        verify(mockitoCore.reporter).mocksHaveToBePassedWhenCreatingInOrder();
        verify(mockitoCore.reporter).nullPassedWhenCreatingInOrder();
        verify(mockitoCore.reporter, never()).notAMockPassedWhenCreatingInOrder();
    }
    
    @Test
    public void testInOrder_NotAMockPassed_ThrowsNotAMockException() {
        // Arrange
        Object mock1 = new Object();
        Object mock2 = Mockito.mock(Object.class);
        Object[] mocks = {mock1, mock2};
        
        // Act
        InOrder result = mockitoCore.inOrder(mocks);
        
        // Assert
        assertEquals(null, result);
        verify(mockitoCore.reporter).mocksHaveToBePassedWhenCreatingInOrder();
        verify(mockitoCore.reporter, never()).nullPassedWhenCreatingInOrder();
        verify(mockitoCore.reporter).notAMockPassedWhenCreatingInOrder();
    }
    
    @Test
    public void testDoAnswer_ValidAnswer_StubbingStarted() {
        // Arrange
        Answer answer = Mockito.mock(Answer.class);
        
        // Act
        Stubber result = mockitoCore.doAnswer(answer);
        
        // Assert
        assertEquals(null, result);
        verify(mockingProgress).stubbingStarted();
        verify(mockingProgress).resetOngoingStubbing();
    }
    
    @Test
    public void testStubVoid_ValidMock_StubbingStarted() {
        // Arrange
        Object mock = Mockito.mock(Object.class);
        
        // Act
        VoidMethodStubbable result = mockitoCore.stubVoid(mock);
        
        // Assert
        assertEquals(null, result);
        verify(mockingProgress).stubbingStarted();
        verify(mockitoCore.reporter, never()).missingMethodInvocation();
    }
    
    @Test
    public void testValidateMockitoUsage_ValidateState() {
        // Act
        mockitoCore.validateMockitoUsage();
        
        // Assert
        verify(mockingProgress).validateState();
    }
    
    @Test
    public void testGetLastInvocation_PullOngoingStubbing_CallsGetRegisteredInvocations() {
        // Arrange
        OngoingStubbingImpl ongoingStubbing = Mockito.mock(OngoingStubbingImpl.class);
        when(mockingProgress.pullOngoingStubbing()).thenReturn(ongoingStubbing);
        Invocation invocation = Mockito.mock(Invocation.class);
        when(ongoingStubbing.getRegisteredInvocations()).thenReturn(Arrays.asList(invocation));
        
        // Act
        Invocation result = mockitoCore.getLastInvocation();
        
        // Assert
        assertEquals(invocation, result);
        verify(mockingProgress).pullOngoingStubbing();
        verify(ongoingStubbing).getRegisteredInvocations();
    }
}
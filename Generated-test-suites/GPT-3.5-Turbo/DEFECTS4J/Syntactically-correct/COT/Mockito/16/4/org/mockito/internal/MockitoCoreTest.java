
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.mockito.exceptions.NotAMockException;
import org.mockito.internal.MockHandlerInterface;

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

        // Inject mocks into MockitoCore
        mockitoCore.setMockUtil(mockUtil);
        mockitoCore.setMockingProgress(mockingProgress);
        mockitoCore.setReporter(reporter);
    }
    
    @Test
    public void testMock_withMockSettings_shouldReturnMockObject() {
        // Arrange
        Class<?> classToMock = Object.class;
        MockSettings mockSettings = mock(MockSettings.class);
        
        // Act
        Object result = mockitoCore.mock(classToMock, mockSettings);
        
        // Assert
        assertEquals(classToMock, result.getClass());
        verify(mockUtil).createMock(classToMock, (MockSettingsImpl) mockSettings);
    }
    
    @Test
    public void testMock_withClassToMockAndMockSettings_shouldReturnMockObject() {
        // Arrange
        Class<?> classToMock = Object.class;
        MockSettings mockSettings = mock(MockSettings.class);
        boolean shouldResetOngoingStubbing = true;
        
        // Act
        Object result = mockitoCore.mock(classToMock, mockSettings, shouldResetOngoingStubbing);
        
        // Assert
        assertEquals(classToMock, result.getClass());
        verify(mockUtil).createMock(classToMock, (MockSettingsImpl) mockSettings);
    }
    
    @Test(expected = NotAMockException.class)
    public void testVerify_withNullMock_shouldThrowNotAMockException() {
        // Arrange
        Object mock = null;
        VerificationMode mode = mock(VerificationMode.class);
        
        // Act
        mockitoCore.verify(mock, mode);
        
        // Assert
        verify(reporter).nullPassedToVerify();
    }
    
    @Test(expected = NotAMockException.class)
    public void testVerify_withNonMockObject_shouldThrowNotAMockException() {
        // Arrange
        Object mock = new Object();
        VerificationMode mode = mock(VerificationMode.class);
        
        // Act
        mockitoCore.verify(mock, mode);
        
        // Assert
        verify(reporter).notAMockPassedToVerify();
    }
    
    @Test
    public void testVerify_withMockAndMode_shouldReturnMockObject() {
        // Arrange
        Object mock = mock(Object.class);
        VerificationMode mode = mock(VerificationMode.class);
        
        // Act
        Object result = mockitoCore.verify(mock, mode);
        
        // Assert
        assertEquals(mock, result);
    }
    
    @Test
    public void testReset_withMocks_shouldResetMocks() {
        // Arrange
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Object[] mocks = {mock1, mock2};
        
        // Act
        mockitoCore.reset(mocks);
        
        // Assert
        verify(mockingProgress).validateState();
        verify(mockingProgress).reset();
        verify(mockingProgress).resetOngoingStubbing();
        verify(mockUtil).resetMock(mock1);
        verify(mockUtil).resetMock(mock2);
    }
    
    @Test(expected = NotAMockException.class)
    public void testVerifyNoMoreInteractions_withNullMock_shouldThrowNotAMockException() {
        // Arrange
        Object mock = null;
        
        // Act
        mockitoCore.verifyNoMoreInteractions(mock);
        
        // Assert
        verify(reporter).nullPassedToVerifyNoMoreInteractions();
    }
    
    @Test(expected = NotAMockException.class)
    public void testVerifyNoMoreInteractions_withNonMockObject_shouldThrowNotAMockException() {
        // Arrange
        Object mock = new Object();
        
        // Act
        mockitoCore.verifyNoMoreInteractions(mock);
        
        // Assert
        verify(reporter).notAMockPassedToVerifyNoMoreInteractions();
    }
    
    @Test
    public void testVerifyNoMoreInteractions_withMocks_shouldPass() {
        // Arrange
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Object[] mocks = {mock1, mock2};
        
        // Act
        mockitoCore.verifyNoMoreInteractions(mocks);
        
        // Assert
        verify(reporter, never()).nullPassedToVerifyNoMoreInteractions();
        verify(reporter, never()).notAMockPassedToVerifyNoMoreInteractions();
        verify(mockUtil, times(2)).getMockHandler(any());
        verify(mockUtil).getMockHandler(mock1).verifyNoMoreInteractions();
        verify(mockUtil).getMockHandler(mock2).verifyNoMoreInteractions();
    }
    
    @Test(expected = NotAMockException.class)
    public void testInOrder_withNullMocks_shouldThrowNotAMockException() {
        // Arrange
        Object[] mocks = null;
        
        // Act
        mockitoCore.inOrder(mocks);
        
        // Assert
        verify(reporter).mocksHaveToBePassedWhenCreatingInOrder();
    }
    
    @Test(expected = NotAMockException.class)
    public void testInOrder_withNonMockObjects_shouldThrowNotAMockException() {
        // Arrange
        Object nonMock = new Object();
        Object[] mocks = {nonMock};
        
        // Act
        mockitoCore.inOrder(mocks);
        
        // Assert
        verify(reporter).notAMockPassedWhenCreatingInOrder();
    }
    
    @Test(expected = NotAMockException.class)
    public void testInOrder_withNullMock_shouldThrowNotAMockException() {
        // Arrange
        Object[] mocks = {null};
        
        // Act
        mockitoCore.inOrder(mocks);
        
        // Assert
        verify(reporter).nullPassedWhenCreatingInOrder();
    }
    
    @Test
    public void testInOrder_withMocks_shouldReturnInOrderObject() {
        // Arrange
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Object[] mocks = {mock1, mock2};
        
        // Act
        InOrder result = mockitoCore.inOrder(mocks);
        
        // Assert
        assertNotNull(result);
    }
    
    @Test
    public void testDoAnswer_withAnswer_shouldReturnStubber() {
        // Arrange
        Answer answer = mock(Answer.class);
        
        // Act
        Stubber result = mockitoCore.doAnswer(answer);
        
        // Assert
        assertNotNull(result);
        verify(mockingProgress).stubbingStarted();
        verify(mockingProgress).resetOngoingStubbing();
    }
    
    @Test
    public void testStubVoid_withMock_shouldReturnVoidMethodStubbable() {
        // Arrange
        Object mock = mock(Object.class);
        
        // Act
        VoidMethodStubbable result = mockitoCore.stubVoid(mock);
        
        // Assert
        assertNotNull(result);
        verify(mockingProgress).stubbingStarted();
    }
    
    @Test
    public void testValidateMockitoUsage() {
        // Act
        mockitoCore.validateMockitoUsage();
        
        // Assert
        verify(mockingProgress).validateState();
    }
    
    @Test
    public void testGetLastInvocation() {
        // Arrange
        MockitoCore mockitoCoreSpy = spy(mockitoCore);
        OngoingStubbingImpl ongoingStubbing = mock(OngoingStubbingImpl.class);
        List<Invocation> invocations = mock(List.class);
        Invocation lastInvocation = mock(Invocation.class);
        when(mockingProgress.pullOngoingStubbing()).thenReturn(ongoingStubbing);
        when(ongoingStubbing.getRegisteredInvocations()).thenReturn(invocations);
        when(invocations.size()).thenReturn(1);
        when(invocations.get(0)).thenReturn(lastInvocation);
        
        // Act
        Invocation result = mockitoCoreSpy.getLastInvocation();
        
        // Assert
        assertEquals(lastInvocation, result);
    }
}

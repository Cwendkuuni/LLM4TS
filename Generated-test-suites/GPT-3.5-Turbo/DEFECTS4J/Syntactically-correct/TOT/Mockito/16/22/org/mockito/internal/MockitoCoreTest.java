import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.MockSettings;
import org.mockito.exceptions.Reporter;
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

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

public class MockitoCoreTest {
    
    private MockitoCore mockitoCore;
    
    @Before
    public void setup() {
        mockitoCore = new MockitoCore();
    }
    
    @Test
    public void testMock_ReturnsMockObject() {
        // Arrange
        Class<?> classToMock = Object.class;
        MockSettings mockSettings = mock(MockSettings.class);
        
        // Act
        Object result = mockitoCore.mock(classToMock, mockSettings);
        
        // Assert
        assertNotNull(result);
        assertTrue(result.getClass().isAssignableFrom(classToMock));
        verify(mockitoCore.mockUtil, times(1)).createMock(classToMock, (MockSettingsImpl) mockSettings);
    }
    
    @Test
    public void testStub_ReturnsOngoingStubbing() {
        // Arrange
        when(mockitoCore.mockingProgress.pullOngoingStubbing()).thenReturn(mock(IOngoingStubbing.class));
        
        // Act
        IOngoingStubbing result = mockitoCore.stub();
        
        // Assert
        assertNotNull(result);
        verify(mockitoCore.mockingProgress, times(1)).pullOngoingStubbing();
        verify(mockitoCore.reporter, never()).missingMethodInvocation();
    }
    
    @Test
    public void testStub_ThrowsExceptionWhenOngoingStubbingIsNull() {
        // Arrange
        when(mockitoCore.mockingProgress.pullOngoingStubbing()).thenReturn(null);
        
        // Act
        IOngoingStubbing result = mockitoCore.stub();
        
        // Assert
        assertNull(result);
        verify(mockitoCore.mockingProgress, times(1)).pullOngoingStubbing();
        verify(mockitoCore.mockingProgress, times(1)).reset();
        verify(mockitoCore.reporter, times(1)).missingMethodInvocation();
    }
    
    @Test
    public void testWhen_ReturnsOngoingStubbing() {
        // Arrange
        when(mockitoCore.mockingProgress.pullOngoingStubbing()).thenReturn(mock(OngoingStubbing.class));
        
        // Act
        OngoingStubbing<Object> result = mockitoCore.when(any());
        
        // Assert
        assertNotNull(result);
        verify(mockitoCore.mockingProgress, times(1)).pullOngoingStubbing();
        verify(mockitoCore.mockingProgress, never()).reset();
        verify(mockitoCore.reporter, never()).missingMethodInvocation();
    }
    
    @Test
    public void testVerify_ValidMockObject_VerificationModeUsed() {
        // Arrange
        Object mockObject = mock(Object.class);
        VerificationMode verificationMode = mock(VerificationMode.class);
        
        // Act
        Object result = mockitoCore.verify(mockObject, verificationMode);
        
        // Assert
        assertNotNull(result);
        assertEquals(mockObject, result);
        verify(mockitoCore.mockingProgress, times(1)).verificationStarted(verificationMode);
        verify(mockitoCore.reporter, never()).nullPassedToVerify();
        verify(mockitoCore.reporter, never()).notAMockPassedToVerify();
    }
    
    @Test
    public void testVerify_NullMockObject_ReportError() {
        // Arrange
        VerificationMode verificationMode = mock(VerificationMode.class);
        
        // Act
        Object result = mockitoCore.verify(null, verificationMode);
        
        // Assert
        assertNull(result);
        verify(mockitoCore.mockingProgress, never()).verificationStarted(verificationMode);
        verify(mockitoCore.reporter, times(1)).nullPassedToVerify();
        verify(mockitoCore.reporter, never()).notAMockPassedToVerify();
    }
    
    @Test
    public void testVerify_NotAMockObject_ReportError() {
        // Arrange
        Object mockObject = new Object();
        VerificationMode verificationMode = mock(VerificationMode.class);
        
        // Act
        Object result = mockitoCore.verify(mockObject, verificationMode);
        
        // Assert
        assertNull(result);
        verify(mockitoCore.mockingProgress, never()).verificationStarted(verificationMode);
        verify(mockitoCore.reporter, never()).nullPassedToVerify();
        verify(mockitoCore.reporter, times(1)).notAMockPassedToVerify();
    }
    
    @Test
    public void testReset_ResetsAllMocks() {
        // Arrange
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Object[] mocks = {mock1, mock2};
        
        // Act
        mockitoCore.reset(mocks);
        
        // Assert
        verify(mockitoCore.mockingProgress, times(1)).validateState();
        verify(mockitoCore.mockingProgress, times(1)).reset();
        verify(mockitoCore.mockingProgress, times(1)).resetOngoingStubbing();
        verify(mockitoCore.mockUtil, times(2)).resetMock(any());
    }
    
    @Test
    public void testReset_NoMocksPassed_ReportError() {
        // Arrange
        
        // Act
        mockitoCore.reset();
        
        // Assert
        verify(mockitoCore.mockingProgress, times(1)).validateState();
        verify(mockitoCore.mockingProgress, times(1)).reset();
        verify(mockitoCore.mockingProgress, never()).resetOngoingStubbing();
        verify(mockitoCore.mockUtil, times(0)).resetMock(any());
        verify(mockitoCore.reporter, times(1)).mocksHaveToBePassedToVerifyNoMoreInteractions();
    }
    
    @Test
    public void testVerifyNoMoreInteractions_ValidMocks_NoErrorReported() {
        // Arrange
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Object[] mocks = {mock1, mock2};
        
        // Act
        mockitoCore.verifyNoMoreInteractions(mocks);
        
        // Assert
        verify(mockitoCore.reporter, never()).nullPassedToVerifyNoMoreInteractions();
        verify(mockitoCore.reporter, never()).notAMockPassedToVerifyNoMoreInteractions();
        verify(mockitoCore.mockUtil, times(2)).getMockHandler(any()).verifyNoMoreInteractions();
    }
    
    @Test
    public void testVerifyNoMoreInteractions_NullMock_ReportError() {
        // Arrange
        Object[] mocks = {null};
        
        // Act
        mockitoCore.verifyNoMoreInteractions(mocks);
        
        // Assert
        verify(mockitoCore.mockUtil, times(0)).getMockHandler(any()).verifyNoMoreInteractions();
        verify(mockitoCore.reporter, times(1)).nullPassedToVerifyNoMoreInteractions();
        verify(mockitoCore.reporter, never()).notAMockPassedToVerifyNoMoreInteractions();
    }
    
    @Test
    public void testVerifyNoMoreInteractions_NotAMock_ReportError() {
        // Arrange
        Object mock = new Object();
        Object[] mocks = {mock};
        
        // Act
        mockitoCore.verifyNoMoreInteractions(mocks);
        
        // Assert
        verify(mockitoCore.mockUtil, times(1)).getMockHandler(mock);
        verify(mockitoCore.mockUtil, times(1)).isMock(mock);
        verify(mockitoCore.reporter, never()).nullPassedToVerifyNoMoreInteractions();
        verify(mockitoCore.reporter, times(1)).notAMockPassedToVerifyNoMoreInteractions();
    }
    
    @Test
    public void testInOrder_ValidMocks_ObjectCreated() {
        // Arrange
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Object[] mocks = {mock1, mock2};
        
        // Act
        InOrder result = mockitoCore.inOrder(mocks);
        
        // Assert
        assertNotNull(result);
        verify(mockitoCore.reporter, never()).mocksHaveToBePassedWhenCreatingInOrder();
        verify(mockitoCore.reporter, never()).nullPassedWhenCreatingInOrder();
        verify(mockitoCore.reporter, never()).notAMockPassedWhenCreatingInOrder();
    }
    
    @Test
    public void testInOrder_NoMocksPassed_ReportError() {
        // Arrange
        Object[] mocks = {};
        
        // Act
        InOrder result = mockitoCore.inOrder(mocks);
        
        // Assert
        assertNull(result);
        verify(mockitoCore.reporter, times(1)).mocksHaveToBePassedWhenCreatingInOrder();
        verify(mockitoCore.reporter, never()).nullPassedWhenCreatingInOrder();
        verify(mockitoCore.reporter, never()).notAMockPassedWhenCreatingInOrder();
    }
    
    @Test
    public void testInOrder_NullMockPassed_ReportError() {
        // Arrange
        Object[] mocks = {null};
        
        // Act
        InOrder result = mockitoCore.inOrder(mocks);
        
        // Assert
        assertNull(result);
        verify(mockitoCore.reporter, never()).mocksHaveToBePassedWhenCreatingInOrder();
        verify(mockitoCore.reporter, times(1)).nullPassedWhenCreatingInOrder();
        verify(mockitoCore.reporter, never()).notAMockPassedWhenCreatingInOrder();
    }
    
    @Test
    public void testInOrder_NotAMockPassed_ReportError() {
        // Arrange
        Object mock = new Object();
        Object[] mocks = {mock};
        
        // Act
        InOrder result = mockitoCore.inOrder(mocks);
        
        // Assert
        assertNull(result);
        verify(mockitoCore.reporter, never()).mocksHaveToBePassedWhenCreatingInOrder();
        verify(mockitoCore.reporter, never()).nullPassedWhenCreatingInOrder();
        verify(mockitoCore.reporter, times(1)).notAMockPassedWhenCreatingInOrder();
    }
    
    @Test
    public void testDoAnswer_CreatesStubber() {
        // Arrange
        Answer answer = mock(Answer.class);
        
        // Act
        Stubber result = mockitoCore.doAnswer(answer);
        
        // Assert
        assertNotNull(result);
        verify(mockitoCore.mockingProgress, times(1)).stubbingStarted();
        verify(mockitoCore.mockingProgress, times(1)).resetOngoingStubbing();
        verify(mockitoCore.reporter, never()).missingMethodInvocation();
    }
    
    @Test
    public void testStubVoid_CreatesVoidMethodStubbable() {
        // Arrange
        Object mock = mock(Object.class);
        MockHandlerInterface<Object> handler = mock(MockHandlerInterface.class);
        when(mockitoCore.mockUtil.getMockHandler(mock)).thenReturn(handler);
        
        // Act
        VoidMethodStubbable<Object> result = mockitoCore.stubVoid(mock);
        
        // Assert
        assertNotNull(result);
        verify(mockitoCore.mockingProgress, times(1)).stubbingStarted();
        verify(mockitoCore.reporter, never()).missingMethodInvocation();
        verify(mockitoCore.mockUtil, times(1)).getMockHandler(mock);
    }
    
    @Test
    public void testValidateMockitoUsage_ValidState_NoErrorReported() {
        // Arrange
        
        // Act
        mockitoCore.validateMockitoUsage();
        
        // Assert
        verify(mockitoCore.mockingProgress, times(1)).validateState();
    }
    
    @Test
    public void testGetLastInvocation_ReturnsLastInvocation() {
        // Arrange
        List<Invocation> registeredInvocations = Arrays.asList(mock(Invocation.class), mock(Invocation.class));
        OngoingStubbingImpl ongoingStubbing = mock(OngoingStubbingImpl.class);
        when(ongoingStubbing.getRegisteredInvocations()).thenReturn(registeredInvocations);
        when(mockitoCore.mockingProgress.pullOngoingStubbing()).thenReturn(ongoingStubbing);
        
        // Act
        Invocation result = mockitoCore.getLastInvocation();
        
        // Assert
        assertNotNull(result);
        verify(mockitoCore.mockingProgress, times(1)).pullOngoingStubbing();
        assertEquals(registeredInvocations.get(1), result);
    }
}
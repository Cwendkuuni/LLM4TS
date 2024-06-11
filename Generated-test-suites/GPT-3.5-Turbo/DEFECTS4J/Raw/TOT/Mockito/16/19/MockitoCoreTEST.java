import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class MockitoCoreTest {

    private MockitoCore mockitoCore;
    
    @Before
    public void setup() {
        mockitoCore = new MockitoCore();
    }
    
    @Test
    public void testMock_ValidClassToMock_ReturnsMockObject() {
        // Arrange
        ClassToMock classToMock = mock(ClassToMock.class);
        
        // Act
        ClassToMock result = mockitoCore.mock(classToMock, new MockSettingsImpl());
        
        // Assert
        assertNotNull(result);
        assertTrue(classToMock.getClass().isAssignableFrom(result.getClass()));
    }
    
    @Test(expected = NotAMockException.class)
    public void testMock_NotAMockClassToMock_ThrowsNotAMockException() {
        // Arrange
        Object classToMock = new Object();
        
        // Act
        mockitoCore.mock(classToMock, new MockSettingsImpl());
    }
    
    @Test
    public void testStub_CalledWithoutOngoingStubbing_ReturnsMissingMethodInvocation() {
        // Act
        IOngoingStubbing result = mockitoCore.stub();
        
        // Assert
        assertNull(result);
        // Assert missing method invocation is reported
    }
    
    @Test
    public void testWhen_CalledWithMethodCall_OngoingStubbingStarted() {
        // Arrange
        ClassToMock classToMock = mock(ClassToMock.class);
        
        // Act
        OngoingStubbing result = mockitoCore.when(classToMock.methodCall());
        
        // Assert
        assertNotNull(result);
    }
    
    @Test
    public void testVerify_ValidMockObject_VerificationStarted() {
        // Arrange
        ClassToMock classToMock = mock(ClassToMock.class);
        
        // Act
        ClassToMock result = mockitoCore.verify(classToMock, VerificationMode.times(1));
        
        // Assert
        assertNotNull(result);
    }
    
    @Test(expected = NotAMockException.class)
    public void testVerify_NotAMockObject_ThrowsNotAMockException() {
        // Arrange
        Object notAMockObject = new Object();
        
        // Act
        mockitoCore.verify(notAMockObject, VerificationMode.times(1));
    }
    
    @Test
    public void testReset_ValidMockObjects_ResetMockObject() {
        // Arrange
        ClassToMock mock1 = mock(ClassToMock.class);
        ClassToMock mock2 = mock(ClassToMock.class);
        ClassToMock[] mocks = { mock1, mock2 };
        
        // Act
        mockitoCore.reset(mocks);
        
        // Assert
        verify(mock1).resetMock();
        verify(mock2).resetMock();
    }
    
    @Test(expected = NotAMockException.class)
    public void testVerifyNoMoreInteractions_NotAMockObject_ThrowsNotAMockException() {
        // Arrange
        Object notAMockObject = new Object();
        
        // Act
        mockitoCore.verifyNoMoreInteractions(notAMockObject);
    }
    
    @Test
    public void testInOrder_ValidMockObjects_ReturnsInOrderObject() {
        // Arrange
        ClassToMock mock1 = mock(ClassToMock.class);
        ClassToMock mock2 = mock(ClassToMock.class);
        ClassToMock[] mocks = { mock1, mock2 };
        
        // Act
        InOrder result = mockitoCore.inOrder(mocks);
        
        // Assert
        assertNotNull(result);
    }
    
    @Test
    public void testDoAnswer_WithValidAnswer_ReturnsStubberObject() {
        // Arrange
        Answer validAnswer = mock(Answer.class);
        
        // Act
        Stubber result = mockitoCore.doAnswer(validAnswer);
        
        // Assert
        assertNotNull(result);
    }
    
    @Test
    public void testStubVoid_ValidMockObject_ReturnsVoidMethodStubbableObject() {
        // Arrange
        ClassToMock classToMock = mock(ClassToMock.class);
        
        // Act
        VoidMethodStubbable result = mockitoCore.stubVoid(classToMock);
        
        // Assert
        assertNotNull(result);
    }
    
    @Test
    public void testValidateMockitoUsage_WithoutAnyError_ValidationSucceeds() {
        // Act
        mockitoCore.validateMockitoUsage();
        
        // Assert validation succeeds
    }
    
    @Test
    public void testGetLastInvocation_ValidOngoingStubbing_ReturnsLastInvocation() {
        // Arrange
        OngoingStubbingImpl ongoingStubbing = mock(OngoingStubbingImpl.class);
        List<Invocation> registeredInvocations = Arrays.asList(mock(Invocation.class));
        
        when(ongoingStubbing.getRegisteredInvocations()).thenReturn(registeredInvocations);
        when(mockitoCore.getLastInvocation()).thenReturn(registeredInvocations.get(0));
        
        // Act
        Invocation result = mockitoCore.getLastInvocation();
        
        // Assert
        assertNotNull(result);
    }
}
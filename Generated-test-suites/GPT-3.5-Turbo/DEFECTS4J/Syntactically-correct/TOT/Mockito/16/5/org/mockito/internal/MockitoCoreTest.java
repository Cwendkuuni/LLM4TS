
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.MockSettings;
import org.mockito.exceptions.misusing.NotAMockException;

import static org.mockito.Mockito.*;

public class MockitoCoreTest {

    private MockitoCore mockitoCore;

    @Before
    public void setup() {
        mockitoCore = new MockitoCore();
    }
    
    @Test
    public void testMock_ValidClassToMock_ReturnsMock() {
        // Arrange
        ClassToMock classToMock = new ClassToMock();
        MockSettings mockSettings = mock(MockSettings.class);
        
        // Act
        ClassToMock mock = mockitoCore.mock(ClassToMock.class, mockSettings);
        
        // Assert
        assertNotNull(mock);
        assertNotSame(classToMock, mock);
        assertTrue(mock instanceof ClassToMock);
    }
    
    @Test(expected = NullPointerException.class)
    public void testMock_NullClassToMock_ThrowsNullPointerException() {
        // Arrange
        MockSettings mockSettings = mock(MockSettings.class);
        
        // Act
        mockitoCore.mock(null, mockSettings);
        
        // Assert
        // Exception expected: NullPointerException
    }
    
    @Test
    public void testVerify_ValidMock_VerificationSuccessful() {
        // Arrange
        ClassToMock mock = mock(ClassToMock.class);
        
        // Act
        ClassToMock result = mockitoCore.verify(mock, times(1));
        
        // Assert
        assertSame(mock, result);
        verify(mock, times(1));
    }
    
    @Test(expected = NullPointerException.class)
    public void testVerify_NullMock_ThrowsNullPointerException() {
        // Arrange
        
        // Act
        mockitoCore.verify(null, times(1));
        
        // Assert
        // Exception expected: NullPointerException
    }
    
    @Test(expected = NotAMockException.class)
    public void testVerify_NotAMock_ThrowsNotAMockException() {
        // Arrange
        ClassToMock classToMock = new ClassToMock();
        
        // Act
        mockitoCore.verify(classToMock, times(1));
        
        // Assert
        // Exception expected: NotAMockException
    }
    
    @Test
    public void testReset_ValidMocks_MocksReset() {
        // Arrange
        ClassToMock mock1 = mock(ClassToMock.class);
        ClassToMock mock2 = mock(ClassToMock.class);
        
        // Act
        mockitoCore.reset(mock1, mock2);
        
        // Assert
        verify(mock1).resetMock();
        verify(mock2).resetMock();
    }
    
    @Test
    public void testVerifyNoMoreInteractions_ValidMocks_VerificationSuccessful() {
        // Arrange
        ClassToMock mock1 = mock(ClassToMock.class);
        ClassToMock mock2 = mock(ClassToMock.class);
        
        // Act
        mockitoCore.verifyNoMoreInteractions(mock1, mock2);
        
        // Assert
        verifyNoMoreInteractions(mock1, mock2);
    }
    
    @Test(expected = NullPointerException.class)
    public void testVerifyNoMoreInteractions_NullMocks_ThrowsNullPointerException() {
        // Arrange
        
        // Act
        mockitoCore.verifyNoMoreInteractions((ClassToMock) null);
        
        // Assert
        // Exception expected: NullPointerException
    }
    
    @Test(expected = NotAMockException.class)
    public void testVerifyNoMoreInteractions_NotAMock_ThrowsNotAMockException() {
        // Arrange
        ClassToMock classToMock = new ClassToMock();
        
        // Act
        mockitoCore.verifyNoMoreInteractions(classToMock);
        
        // Assert
        // Exception expected: NotAMockException
    }
    
    @Test
    public void testInOrder_ValidMocks_MocksInOrder() {
        // Arrange
        ClassToMock mock1 = mock(ClassToMock.class);
        ClassToMock mock2 = mock(ClassToMock.class);
        
        // Act
        InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
        
        // Assert
        inOrder.verify(mock1).method1();
        inOrder.verify(mock2).method2();
    }
    
    @Test(expected = NullPointerException.class)
    public void testInOrder_NullMocks_ThrowsNullPointerException() {
        // Arrange
        
        // Act
        mockitoCore.inOrder((ClassToMock) null);
        
        // Assert
        // Exception expected: NullPointerException
    }
    
    @Test(expected = NotAMockException.class)
    public void testInOrder_NotAMock_ThrowsNotAMockException() {
        // Arrange
        ClassToMock classToMock = new ClassToMock();
        
        // Act
        mockitoCore.inOrder(classToMock);
        
        // Assert
        // Exception expected: NotAMockException
    }
    
    @Test
    public void testDoAnswer_ValidAnswer_StubbingSuccessful() {
        // Arrange
        Answer answer = mock(Answer.class);
        
        // Act
        Object result = mockitoCore.doAnswer(answer);
        
        // Assert
        assertNotNull(result);
        assertTrue(result instanceof Stubber);
    }
    
    @Test
    public void testDoAnswer_ResetOngoingStubbing() {
        // Arrange
        Answer answer = mock(Answer.class);
        
        // Act
        Object result1 = mockitoCore.doAnswer(answer);
        Object result2 = mockitoCore.stub();
        
        // Assert
        assertNotNull(result1);
        assertTrue(result1 instanceof Stubber);
        assertNull(result2);
    }
    
    @Test
    public void testStubVoid_ValidMock_StubbingSuccessful() {
        // Arrange
        ClassToMock mock = mock(ClassToMock.class);
        
        // Act
        VoidMethodStubbable stubbable = mockitoCore.stubVoid(mock);
        
        // Assert
        assertNotNull(stubbable);
        verify(mockitoCore.mockUtil.getMockHandler(mock)).voidMethodStubbable(mock);
    }
    
    @Test
    public void testValidateMockitoUsage_Called_ValidationSuccessful() {
        // Arrange
        
        // Act
        mockitoCore.validateMockitoUsage();
        
        // Assert
        // No exception expected
    }
    
    @Test
    public void testGetLastInvocation_RegisteredInvocations_ReturnsLastInvocation() {
        // Arrange
        OngoingStubbingImpl ongoingStubbing = mock(OngoingStubbingImpl.class);
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        when(ongoingStubbing.getRegisteredInvocations()).thenReturn(Arrays.asList(invocation1, invocation2));
        
        // Act
        Invocation result = mockitoCore.getLastInvocation();
        
        // Assert
        assertSame(invocation2, result);
    }
    
    @Test(expected = NullPointerException.class)
    public void testGetLastInvocation_InvocationListIsNull_ThrowsNullPointerException() {
        // Arrange
        OngoingStubbingImpl ongoingStubbing = mock(OngoingStubbingImpl.class);
        when(ongoingStubbing.getRegisteredInvocations()).thenReturn(null);
        
        // Act
        Invocation result = mockitoCore.getLastInvocation();
        
        // Assert
        // Exception expected: NullPointerException
    }
    
    class ClassToMock {
        public void method1() {}
        public void method2() {}
        public void resetMock() {}
    }
}

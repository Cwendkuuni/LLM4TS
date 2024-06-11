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

public class MockitoCoreTest {

    private MockitoCore mockitoCore = new MockitoCore();
    
    @Test
    public void testMock() {
        // Arrange
        Class<TestInterface> classToMock = TestInterface.class;
        MockSettings mockSettings = mock(MockSettingsImpl.class);
        boolean shouldResetOngoingStubbing = true;
        
        // Act
        TestInterface result = mockitoCore.mock(classToMock, mockSettings, shouldResetOngoingStubbing);
        
        // Assert
        assertNotNull(result);
    }
    
    @Test
    public void testMockWithMockSettings() {
        // Arrange
        Class<TestInterface> classToMock = TestInterface.class;
        MockSettings mockSettings = mock(MockSettingsImpl.class);
        
        // Act
        TestInterface result = mockitoCore.mock(classToMock, mockSettings);
        
        // Assert
        assertNotNull(result);
    }
    
    @Test
    public void testStub() {
        // Arrange
        
        // Act
        IOngoingStubbing result = mockitoCore.stub();
        
        // Assert
        assertNotNull(result);
    }
    
    @Test
    public void testStub_WithDeprecatedMethod() {
        // Arrange
        Object methodCall = new Object();
        
        // Act
        DeprecatedOngoingStubbing<Object> result = mockitoCore.stub(methodCall);
        
        // Assert
        assertNotNull(result);
    }
    
    @Test
    public void testWhen() {
        // Arrange
        Object methodCall = new Object();
        
        // Act
        OngoingStubbing<Object> result = mockitoCore.when(methodCall);
        
        // Assert
        assertNotNull(result);
    }
    
    @Test
    public void testVerify() {
        // Arrange
        TestInterface mock = mock(TestInterface.class);
        VerificationMode mode = mock(VerificationMode.class);
        
        // Act
        TestInterface result = mockitoCore.verify(mock, mode);
        
        // Assert
        assertEquals(mock, result);
    }
    
    @Test
    public void testVerify_WithNullMock() {
        // Arrange
        TestInterface mock = null;
        VerificationMode mode = mock(VerificationMode.class);
        
        // Act
        TestInterface result = mockitoCore.verify(mock, mode);
        
        // Assert
        assertNull(result);
        verify(reporter).nullPassedToVerify();
    }
    
    @Test
    public void testVerify_WithNotAMock() {
        // Arrange
        TestInterface mock = new TestInterfaceImpl();
        VerificationMode mode = mock(VerificationMode.class);
        
        // Act
        TestInterface result = mockitoCore.verify(mock, mode);
        
        // Assert
        assertNull(result);
        verify(reporter).notAMockPassedToVerify();
    }
    
    @Test
    public void testReset() {
        // Arrange
        TestInterface mock1 = mock(TestInterface.class);
        TestInterface mock2 = mock(TestInterface.class);
        
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
    public void testVerifyNoMoreInteractions() {
        // Arrange
        TestInterface mock1 = mock(TestInterface.class);
        TestInterface mock2 = mock(TestInterface.class);
        Object[] mocks = {mock1, mock2};
        
        // Act
        mockitoCore.verifyNoMoreInteractions(mock1, mock2);
        
        // Assert
        verify(assertMocksNotEmpty).invoke(mocks);
        verify(mockingProgress).validateState();
        verify(mockUtil).getMockHandler(mock1);
        verify(mockUtil).getMockHandler(mock2);
        verify(reporter, never()).nullPassedToVerifyNoMoreInteractions();
        verify(reporter, never()).notAMockPassedToVerifyNoMoreInteractions();
    }
    
    @Test
    public void testVerifyNoMoreInteractions_WithNullMock() {
        // Arrange
        TestInterface mock1 = mock(TestInterface.class);
        TestInterface mock2 = null;
        Object[] mocks = {mock1, mock2};
        
        // Act
        mockitoCore.verifyNoMoreInteractions(mock1, mock2);
        
        // Assert
        verify(assertMocksNotEmpty).invoke(mocks);
        verify(mockingProgress).validateState();
        verify(mockUtil).getMockHandler(mock1);
        verify(reporter).nullPassedToVerifyNoMoreInteractions();
        verify(reporter, never()).notAMockPassedToVerifyNoMoreInteractions();
    }
    
    @Test
    public void testVerifyNoMoreInteractions_WithNotAMock() {
        // Arrange
        TestInterface mock1 = mock(TestInterface.class);
        TestInterface mock2 = new TestInterfaceImpl();
        Object[] mocks = {mock1, mock2};
        
        // Act
        mockitoCore.verifyNoMoreInteractions(mock1, mock2);
        
        // Assert
        verify(assertMocksNotEmpty).invoke(mocks);
        verify(mockingProgress).validateState();
        verify(mockUtil).getMockHandler(mock1);
        verify(mockUtil).getMockHandler(mock2);
        verify(reporter, never()).nullPassedToVerifyNoMoreInteractions();
        verify(reporter).notAMockPassedToVerifyNoMoreInteractions();
    }
    
    @Test
    public void testInOrder() {
        // Arrange
        TestInterface mock1 = mock(TestInterface.class);
        TestInterface mock2 = mock(TestInterface.class);
        Object[] mocks = {mock1, mock2};
        
        // Act
        InOrder result = mockitoCore.inOrder(mock1, mock2);
        
        // Assert
        assertNotNull(result);
        verify(assertMocksNotEmpty).invoke(mocks);
        verify(mockUtil).isMock(mock1);
        verify(mockUtil).isMock(mock2);
        verify(reporter, never()).nullPassedWhenCreatingInOrder();
        verify(reporter, never()).notAMockPassedWhenCreatingInOrder();
    }
    
    @Test
    public void testInOrder_WithNullMock() {
        // Arrange
        TestInterface mock1 = null;
        TestInterface mock2 = mock(TestInterface.class);
        Object[] mocks = {mock1, mock2};
        
        // Act
        InOrder result = mockitoCore.inOrder(mock1, mock2);
        
        // Assert
        assertNull(result);
        verify(assertMocksNotEmpty).invoke(mocks);
        verify(mockUtil, never()).isMock(mock1);
        verify(mockUtil).isMock(mock2);
        verify(reporter).nullPassedWhenCreatingInOrder();
        verify(reporter, never()).notAMockPassedWhenCreatingInOrder();
    }
    
    @Test
    public void testInOrder_WithNotAMock() {
        // Arrange
        TestInterface mock1 = mock(TestInterface.class);
        TestInterface mock2 = new TestInterfaceImpl();
        Object[] mocks = {mock1, mock2};
        
        // Act
        InOrder result = mockitoCore.inOrder(mock1, mock2);
        
        // Assert
        assertNull(result);
        verify(assertMocksNotEmpty).invoke(mocks);
        verify(mockUtil).isMock(mock1);
        verify(mockUtil).isMock(mock2);
        verify(reporter, never()).nullPassedWhenCreatingInOrder();
        verify(reporter).notAMockPassedWhenCreatingInOrder();
    }
    
    @Test
    public void testDoAnswer() {
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
    public void testStubVoid() {
        // Arrange
        TestInterface mock = mock(TestInterface.class);
        
        // Act
        VoidMethodStubbable<Object> result = mockitoCore.stubVoid(mock);
        
        // Assert
        assertNotNull(result);
        verify(mockingProgress).stubbingStarted();
    }
    
    @Test
    public void testValidateMockitoUsage() {
        // Arrange
        
        // Act
        mockitoCore.validateMockitoUsage();
        
        // Assert
        verify(mockingProgress).validateState();
    }
    
    @Test
    public void testGetLastInvocation() {
        // Arrange
        OngoingStubbingImpl ongoingStubbing = mock(OngoingStubbingImpl.class);
        Invocation invocation = mock(Invocation.class);
        List<Invocation> allInvocations = Arrays.asList(invocation);
        when(ongoingStubbing.getRegisteredInvocations()).thenReturn(allInvocations);
        when(mockingProgress.pullOngoingStubbing()).thenReturn(ongoingStubbing);
        
        // Act
        Invocation result = mockitoCore.getLastInvocation();
        
        // Assert
        assertEquals(invocation, result);
    }
    
    private interface TestInterface {
    }
    
    private class TestInterfaceImpl implements TestInterface {
    }
}
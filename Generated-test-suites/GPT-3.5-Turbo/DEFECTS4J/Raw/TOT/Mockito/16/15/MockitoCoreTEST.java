import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
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
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.stubbing.Stubber;
import org.mockito.stubbing.VoidMethodStubbable;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockitoCoreTest {

    private MockitoCore mockitoCore;

    @Before
    public void setup() {
        mockitoCore = new MockitoCore();
    }

    @Test
    public void testMock_ClassToMockAndMockSettings_ReturnsMockObject() {
        // Arrange
        Class<TestObject> classToMock = TestObject.class;
        MockSettings mockSettings = new MockSettingsImpl();
        
        // Act
        TestObject result = mockitoCore.mock(classToMock, mockSettings);
        
        // Assert
        assertNotNull(result);
        assertTrue(result instanceof TestObject);
    }

    @Test
    public void testMock_ClassToMockAndMockSettingsWithReset_ReturnsMockObjectAndResetsOngoingStubbing() {
        // Arrange
        Class<TestObject> classToMock = TestObject.class;
        MockSettings mockSettings = new MockSettingsImpl();
        
        // Act
        TestObject result = mockitoCore.mock(classToMock, mockSettings, true);
        
        // Assert
        assertNotNull(result);
        assertTrue(result instanceof TestObject);
        verify(mockitoCore.mockingProgress).resetOngoingStubbing();
    }

    @Test
    public void testStub_NoOngoingStubbing_ReturnsNull() {
        // Arrange
        
        // Act
        IOngoingStubbing result = mockitoCore.stub();
        
        // Assert
        assertNull(result);
        verify(mockitoCore.mockingProgress).reset();
        verify(mockitoCore.reporter).missingMethodInvocation();
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testStub_MethodCallDeprecated_ReturnsDeprecatedOngoingStubbing() {
        // Arrange
        TestObject methodCall = new TestObject();
        mockitoCore.mockingProgress.stubbingStarted();
        
        // Act
        DeprecatedOngoingStubbing result = mockitoCore.stub(methodCall);
        
        // Assert
        assertNotNull(result);
        assertTrue(result instanceof DeprecatedOngoingStubbing);
        verify(mockitoCore.mockingProgress).stubbingStarted();
    }

    @Test
    public void testWhen_MethodCall_ReturnsOngoingStubbing() {
        // Arrange
        TestObject methodCall = new TestObject();
        mockitoCore.mockingProgress.stubbingStarted();
        
        // Act
        OngoingStubbing<TestObject> result = mockitoCore.when(methodCall);
        
        // Assert
        assertNotNull(result);
        assertTrue(result instanceof OngoingStubbing);
        verify(mockitoCore.mockingProgress).stubbingStarted();
    }

    @Test
    public void testVerify_MockAndVerificationMode_ReturnsMockObject() {
        // Arrange
        TestObject mock = mock(TestObject.class);
        VerificationMode mode = VerificationMode.times(1);
        
        // Act
        TestObject result = mockitoCore.verify(mock, mode);
        
        // Assert
        assertNotNull(result);
        assertEquals(mock, result);
        verify(mockitoCore.mockingProgress).verificationStarted(mode);
    }

    @Test
    public void testVerify_NullPassedToVerify_ThrowsException() {
        // Arrange
        TestObject mock = null;
        VerificationMode mode = VerificationMode.times(1);
        
        // Act & Assert
        assertThrows(NullPointerException.class, () -> mockitoCore.verify(mock, mode));
        verify(mockitoCore.reporter).nullPassedToVerify();
    }

    @Test
    public void testVerify_NotAMockPassedToVerify_ThrowsException() {
        // Arrange
        TestObject mock = new TestObject();
        VerificationMode mode = VerificationMode.times(1);
        
        // Act & Assert
        assertThrows(NotAMockException.class, () -> mockitoCore.verify(mock, mode));
        verify(mockitoCore.reporter).notAMockPassedToVerify();
    }

    @Test
    public void testReset_Mocks_ResetMocks() {
        // Arrange
        TestObject mock1 = mock(TestObject.class);
        TestObject mock2 = mock(TestObject.class);
        TestObject[] mocks = new TestObject[] {mock1, mock2};
        
        // Act
        mockitoCore.reset(mocks);
        
        // Assert
        verify(mockitoCore.mockingProgress).validateState();
        verify(mockitoCore.mockingProgress).reset();
        verify(mockitoCore.mockingProgress).resetOngoingStubbing();
        verify(mockitoCore.mockUtil).resetMock(mock1);
        verify(mockitoCore.mockUtil).resetMock(mock2);
    }

    @Test
    public void testVerifyNoMoreInteractions_NullPassedToVerifyNoMoreInteractions_ThrowsException() {
        // Arrange
        Object[] mocks = new Object[] {null};
        
        // Act & Assert
        assertThrows(NullPointerException.class, () -> mockitoCore.verifyNoMoreInteractions(mocks));
        verify(mockitoCore.reporter).nullPassedToVerifyNoMoreInteractions();
    }

    @Test
    public void testVerifyNoMoreInteractions_NotAMockPassedToVerifyNoMoreInteractions_ThrowsException() {
        // Arrange
        Object[] mocks = new Object[] {new TestObject()};
        
        // Act & Assert
        assertThrows(NotAMockException.class, () -> mockitoCore.verifyNoMoreInteractions(mocks));
        verify(mockitoCore.reporter).notAMockPassedToVerifyNoMoreInteractions();
    }

    @Test
    public void testAssertMocksNotEmpty_EmptyMocksList_ThrowsException() {
        // Arrange
        Object[] mocks = new Object[] {};
        
        // Act & Assert
        assertThrows(NullPointerException.class, () -> invokePrivateMethod(mockitoCore, "assertMocksNotEmpty", new Object[] {mocks}));
        verify(mockitoCore.reporter).mocksHaveToBePassedToVerifyNoMoreInteractions();
    }

    @Test
    public void testInOrder_NullPassedWhenCreatingInOrder_ThrowsException() {
        // Arrange
        Object[] mocks = new Object[] {null};
        
        // Act & Assert
        assertThrows(NullPointerException.class, () -> mockitoCore.inOrder(mocks));
        verify(mockitoCore.reporter).nullPassedWhenCreatingInOrder();
    }

    @Test
    public void testInOrder_NotAMockPassedWhenCreatingInOrder_ThrowsException() {
        // Arrange
        Object[] mocks = new Object[] {new TestObject()};
        
        // Act & Assert
        assertThrows(NotAMockException.class, () -> mockitoCore.inOrder(mocks));
        verify(mockitoCore.reporter).notAMockPassedWhenCreatingInOrder();
    }

    @Test
    public void testInOrder_ValidMocksList_CreatesInOrderObject() {
        // Arrange
        Object[] mocks = new Object[] {mock(TestObject.class)};
        
        // Act
        InOrder result = mockitoCore.inOrder(mocks);
        
        // Assert
        assertNotNull(result);
        assertTrue(result instanceof InOrderImpl);
    }

    @Test
    public void testDoAnswer_Answer_ReturnsStubber() {
        // Arrange
        Answer answer = mock(Answer.class);
        mockitoCore.mockingProgress.stubbingStarted();
        
        // Act
        Stubber result = mockitoCore.doAnswer(answer);
        
        // Assert
        assertNotNull(result);
        assertTrue(result instanceof StubberImpl);
        verify(mockitoCore.mockingProgress).stubbingStarted();
        verify(mockitoCore.mockingProgress).resetOngoingStubbing();
    }

    @Test
    public void testStubVoid_Mock_CreatesVoidMethodStubbable() {
        // Arrange
        TestObject mock = mock(TestObject.class);
        mockitoCore.mockingProgress.stubbingStarted();
        
        // Act
        VoidMethodStubbable<TestObject> result = mockitoCore.stubVoid(mock);
        
        // Assert
        assertNotNull(result);
        assertTrue(result instanceof VoidMethodStubbable);
        verify(mockitoCore.mockingProgress).stubbingStarted();
    }

    @Test
    public void testValidateMockitoUsage_ValidateState() {
        // Arrange
        
        // Act
        mockitoCore.validateMockitoUsage();
        
        // Assert
        verify(mockitoCore.mockingProgress).validateState();
    }

    @Test
    public void testGetLastInvocation_ReturnsLastInvocation() {
        // Arrange
        OngoingStubbingImpl ongoingStubbing = mock(OngoingStubbingImpl.class);
        List<Invocation> registeredInvocations = Arrays.asList(mock(Invocation.class), mock(Invocation.class));
        when(ongoingStubbing.getRegisteredInvocations()).thenReturn(registeredInvocations);
        when(mockitoCore.mockingProgress.pullOngoingStubbing()).thenReturn(ongoingStubbing);
        
        // Act
        Invocation result = mockitoCore.getLastInvocation();
        
        // Assert
        assertNotNull(result);
        assertEquals(registeredInvocations.get(1), result);
        verify(mockitoCore.mockingProgress).pullOngoingStubbing();
    }

    private void invokePrivateMethod(MockitoCore obj, String methodName, Object[] args) {
        try {
            Class<?>[] argTypes = new Class<?>[args.length];
            for (int i = 0; i < args.length; i++) {
                argTypes[i] = args[i].getClass();
            }
            java.lang.reflect.Method method = MockitoCore.class.getDeclaredMethod(methodName, argTypes);
            method.setAccessible(true);
            method.invoke(obj, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class TestObject {
    // Sample class for mocking
}
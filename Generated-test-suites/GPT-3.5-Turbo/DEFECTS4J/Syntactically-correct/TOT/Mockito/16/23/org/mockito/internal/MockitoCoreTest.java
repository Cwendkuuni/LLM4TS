import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.stubbing.VoidMethodStubbable;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class MockitoCoreTest {

    private MockitoCore mockitoCore;

    @Before
    public void setUp() {
        mockitoCore = new MockitoCore();
    }

    @Test
    public void testMock_withClassToMockAndMockSettings_shouldReturnMockObject() {
        // Arrange
        Class<ExampleClass> classToMock = ExampleClass.class;
        MockSettings mockSettings = new MockSettingsImpl();
        
        // Act
        ExampleClass result = mockitoCore.mock(classToMock, mockSettings);

        // Assert
        assertNotNull(result);
        assertTrue(MockUtil.isMock(result));
    }

    @Test
    public void testMock_withClassToMockAndMockSettingsAndShouldResetOngoingStubbing_true_shouldReturnMockObject() {
        // Arrange
        Class<ExampleClass> classToMock = ExampleClass.class;
        MockSettings mockSettings = new MockSettingsImpl();
        
        // Act
        ExampleClass result = mockitoCore.mock(classToMock, mockSettings, true);

        // Assert
        assertNotNull(result);
        assertTrue(MockUtil.isMock(result));
    }

    @Test
    public void testStub_whenOngoingStubbingExists_shouldReturnOngoingStubbingObject() {
        // Arrange
        when(mockitoCore.stub()).thenReturn(mock(IOngoingStubbing.class));

        // Act
        IOngoingStubbing result = mockitoCore.stub();

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testStub_whenOngoingStubbingIsNull_shouldResetMockingProgressAndInvokeMissingMethodInvocation() {
        // Arrange
        mockitoCore.stub(); // Clear the ongoing stubbing
        doNothing().when(mockitoCore.mockingProgress).reset();
        doNothing().when(mockitoCore.reporter).missingMethodInvocation();

        // Act
        IOngoingStubbing result = mockitoCore.stub();

        // Assert
        assertNull(result);
        verify(mockitoCore.mockingProgress).reset();
        verify(mockitoCore.reporter).missingMethodInvocation();
    }

    @Test
    public void testDeprecatedStub_withMethodCall_shouldReturnDeprecatedOngoingStubbingObject() {
        // Arrange
        Object methodCall = new Object();
        when(mockitoCore.stub()).thenReturn(mock(DeprecatedOngoingStubbing.class));

        // Act
        DeprecatedOngoingStubbing result = mockitoCore.stub(methodCall);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testWhen_withMethodCall_shouldReturnOngoingStubbingObject() {
        // Arrange
        Object methodCall = new Object();
        when(mockitoCore.stub()).thenReturn(mock(OngoingStubbing.class));

        // Act
        OngoingStubbing result = mockitoCore.when(methodCall);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testVerify_withMockAndVerificationMode_shouldReturnMockObject() {
        // Arrange
        ExampleClass mock = mock(ExampleClass.class);
        VerificationMode mode = VerificationModeFactory.times(1);

        // Act
        ExampleClass result = mockitoCore.verify(mock, mode);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testVerify_withNullMock_shouldInvokeNullPassedToVerify() {
        // Arrange
        ExampleClass mock = null;
        VerificationMode mode = VerificationModeFactory.times(1);
        doNothing().when(mockitoCore.reporter).nullPassedToVerify();

        // Act
        ExampleClass result = mockitoCore.verify(mock, mode);

        // Assert
        assertNull(result);
        verify(mockitoCore.reporter).nullPassedToVerify();
    }

    @Test
    public void testVerify_withNonMockObject_shouldInvokeNotAMockPassedToVerify() {
        // Arrange
        ExampleClass nonMockObject = new ExampleClass();
        VerificationMode mode = VerificationModeFactory.times(1);
        doNothing().when(mockitoCore.reporter).notAMockPassedToVerify();

        // Act
        ExampleClass result = mockitoCore.verify(nonMockObject, mode);

        // Assert
        assertNull(result);
        verify(mockitoCore.reporter).notAMockPassedToVerify();
    }

    @Test
    public void testReset_withMockObjects_shouldResetMockingProgressAndResetMocks() {
        // Arrange
        ExampleClass mock1 = mock(ExampleClass.class);
        ExampleClass mock2 = mock(ExampleClass.class);
        ExampleClass mock3 = mock(ExampleClass.class);
        Object[] mocks = {mock1, mock2, mock3};
        doNothing().when(mockitoCore.mockingProgress).reset();
        verify(mockitoCore.mockingProgress, times(3)).resetOngoingStubbing();
        doNothing().when(mockitoCore.mockUtil).resetMock(any());

        // Act
        mockitoCore.reset(mocks);

        // Assert
        verify(mockitoCore.mockingProgress).reset();
        verify(mockitoCore.mockingProgress, times(3)).resetOngoingStubbing();
        verify(mockitoCore.mockUtil, times(3)).resetMock(any());
    }

    @Test
    public void testReset_withEmptyMocksArray_shouldInvokeMocksHaveToBePassedToVerifyNoMoreInteractions() {
        // Arrange
        Object[] mocks = {};
        doNothing().when(mockitoCore.reporter).mocksHaveToBePassedToVerifyNoMoreInteractions();

        // Act
        mockitoCore.reset(mocks);

        // Assert
        verify(mockitoCore.reporter).mocksHaveToBePassedToVerifyNoMoreInteractions();
    }

    @Test
    public void testVerifyNoMoreInteractions_withMockObjects_shouldInvokeValidateStateAndVerifyNoMoreInteractionsForMocks() {
        // Arrange
        ExampleClass mock1 = mock(ExampleClass.class);
        ExampleClass mock2 = mock(ExampleClass.class);
        ExampleClass mock3 = mock(ExampleClass.class);
        Object[] mocks = {mock1, mock2, mock3};
        doNothing().when(mockitoCore.mockingProgress).validateState();
        doNothing().when(mockitoCore.mockUtil.getMockHandler(any())).verifyNoMoreInteractions();

        // Act
        mockitoCore.verifyNoMoreInteractions(mocks);

        // Assert
        verify(mockitoCore.mockingProgress).validateState();
        verify(mockitoCore.mockUtil, times(3)).getMockHandler(any());
    }

    @Test
    public void testVerifyNoMoreInteractions_withNullMock_shouldInvokeNullPassedToVerifyNoMoreInteractions() {
        // Arrange
        ExampleClass nullMock = null;
        Object[] mocks = {nullMock};
        doNothing().when(mockitoCore.reporter).nullPassedToVerifyNoMoreInteractions();

        // Act
        mockitoCore.verifyNoMoreInteractions(mocks);

        // Assert
        verify(mockitoCore.reporter).nullPassedToVerifyNoMoreInteractions();
    }

    @Test
    public void testVerifyNoMoreInteractions_withNonMockObject_shouldInvokeNotAMockPassedToVerifyNoMoreInteractions() {
        // Arrange
        ExampleClass nonMockObject = new ExampleClass();
        Object[] mocks = {nonMockObject};
        doThrow(NotAMockException.class).when(mockitoCore.mockUtil.getMockHandler(any())).verifyNoMoreInteractions();
        doNothing().when(mockitoCore.reporter).notAMockPassedToVerifyNoMoreInteractions();

        // Act
        mockitoCore.verifyNoMoreInteractions(mocks);

        // Assert
        verify(mockitoCore.mockUtil).getMockHandler(any());
        verify(mockitoCore.reporter).notAMockPassedToVerifyNoMoreInteractions();
    }

    @Test
    public void testInOrder_withMockObjects_shouldReturnInOrderObject() {
        // Arrange
        ExampleClass mock1 = mock(ExampleClass.class);
        ExampleClass mock2 = mock(ExampleClass.class);
        ExampleClass mock3 = mock(ExampleClass.class);
        Object[] mocks = {mock1, mock2, mock3};
        doNothing().when(mockitoCore.reporter).mocksHaveToBePassedWhenCreatingInOrder();
        doNothing().when(mockitoCore.reporter).nullPassedWhenCreatingInOrder();
        doNothing().when(mockitoCore.reporter).notAMockPassedWhenCreatingInOrder();

        // Act
        InOrder result = mockitoCore.inOrder(mocks);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testInOrder_withEmptyMocksArray_shouldInvokeMocksHaveToBePassedWhenCreatingInOrder() {
        // Arrange
        Object[] mocks = {};
        doNothing().when(mockitoCore.reporter).mocksHaveToBePassedWhenCreatingInOrder();

        // Act
        mockitoCore.inOrder(mocks);

        // Assert
        verify(mockitoCore.reporter).mocksHaveToBePassedWhenCreatingInOrder();
    }

    @Test
    public void testInOrder_withNullMock_shouldInvokeNullPassedWhenCreatingInOrder() {
        // Arrange
        ExampleClass nullMock = null;
        Object[] mocks = {nullMock};
        doNothing().when(mockitoCore.reporter).nullPassedWhenCreatingInOrder();

        // Act
        mockitoCore.inOrder(mocks);

        // Assert
        verify(mockitoCore.reporter).nullPassedWhenCreatingInOrder();
    }

    @Test
    public void testInOrder_withNonMockObject_shouldInvokeNotAMockPassedWhenCreatingInOrder() {
        // Arrange
        ExampleClass nonMockObject = new ExampleClass();
        Object[] mocks = {nonMockObject};
        doNothing().when(mockitoCore.reporter).notAMockPassedWhenCreatingInOrder();

        // Act
        mockitoCore.inOrder(mocks);

        // Assert
        verify(mockitoCore.reporter).notAMockPassedWhenCreatingInOrder();
    }

    @Test
    public void testDoAnswer_withAnswer_shouldReturnStubberObject() {
        // Arrange
        Answer answer = mock(Answer.class);
        when(mockitoCore.stub()).thenReturn(mock(StubberImpl.class));

        // Act
        Stubber result = mockitoCore.doAnswer(answer);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testStubVoid_withMockObject_shouldReturnVoidMethodStubbableObject() {
        // Arrange
        ExampleClass mock = mock(ExampleClass.class);
        MockHandlerInterface<ExampleClass> mockHandler = mock(MockHandlerInterface.class);
        when(mockitoCore.mockUtil.getMockHandler(any())).thenReturn(mockHandler);
        when(mockHandler.voidMethodStubbable(mock)).thenReturn(mock(VoidMethodStubbable.class));

        // Act
        VoidMethodStubbable result = mockitoCore.stubVoid(mock);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testValidateMockitoUsage_shouldInvokeValidateState() {
        // Arrange
        doNothing().when(mockitoCore.mockingProgress).validateState();

        // Act
        mockitoCore.validateMockitoUsage();

        // Assert
        verify(mockitoCore.mockingProgress).validateState();
    }

    @Test
    public void testGetLastInvocation_whenOngoingStubbingExists_shouldReturnLastInvocation() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        OngoingStubbingImpl ongoingStubbing = mock(OngoingStubbingImpl.class);
        List<Invocation> allInvocations = Arrays.asList(invocation);
        when(ongoingStubbing.getRegisteredInvocations()).thenReturn(allInvocations);
        when(mockitoCore.mockingProgress.pullOngoingStubbing()).thenReturn(ongoingStubbing);

        // Act
        Invocation result = mockitoCore.getLastInvocation();

        // Assert
        assertNotNull(result);
        assertEquals(invocation, result);
    }

    @Test
    public void testGetLastInvocation_whenOngoingStubbingIsNull_shouldReturnNull() {
        // Arrange
        when(mockitoCore.mockingProgress.pullOngoingStubbing()).thenReturn(null);

        // Act
        Invocation result = mockitoCore.getLastInvocation();

        // Assert
        assertNull(result);
    }
}
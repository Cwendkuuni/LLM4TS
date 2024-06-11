import org.junit.Before;
import org.junit.Test;
import org.mockito.MockSettings;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.*;

import static org.mockito.Mockito.*;

public class MockitoCoreTest {

    private MockitoCore mockitoCore;
    private Reporter reporter;
    private MockUtil mockUtil;
    private MockingProgress mockingProgress;

    @Before
    public void setup() {
        mockitoCore = new MockitoCore();
        reporter = mockitoCore.getReporter();
        mockUtil = mockitoCore.getMockUtil();
        mockingProgress = mockitoCore.getMockingProgress();
    }

    @Test
    public void testMock_ValidClass_ReturnsMockObject() {
        // Arrange
        Class classToMock = MyClass.class;
        MockSettings mockSettings = new MockSettings();

        // Act
        MyClass result = mockitoCore.mock(classToMock, mockSettings);

        // Assert
        assertNotNull(result);
        assertTrue(mockUtil.isMock(result));
    }
    
    @Test
    public void testMock_InvalidClass_ReturnsError() {
        // Arrange
        Class invalidClassToMock = null;
        MockSettings mockSettings = new MockSettings();

        // Act
        try {
            MyClass result = mockitoCore.mock(invalidClassToMock, mockSettings);
            fail("Expected NotAMockException to be thrown");
        } catch (NotAMockException ex) {
            // Assert
            verify(reporter).notAMockPassedToVerify();
        }
    }

    @Test
    public void testStub_ExistingStubbing_ReturnsStubbingObject() {
        // Arrange
        OngoingStubbingImpl ongoingStubbing = mock(OngoingStubbingImpl.class);
        mockingProgress.pushOngoingStubbing(ongoingStubbing);

        // Act
        IOngoingStubbing result = mockitoCore.stub();

        // Assert
        assertEquals(ongoingStubbing, result);
    }
    
    @Test
    public void testStub_NoExistingStubbing_ReturnsError() {
        // Arrange
        mockingProgress.pullOngoingStubbing();

        // Act
        IOngoingStubbing result = mockitoCore.stub();

        // Assert
        assertNull(result);
        verify(reporter).missingMethodInvocation();
    }

    @Test
    public void testWhen_ExistingStubbing_ReturnsStubbingObject() {
        // Arrange
        OngoingStubbingImpl ongoingStubbing = mock(OngoingStubbingImpl.class);
        mockingProgress.pushOngoingStubbing(ongoingStubbing);

        // Act
        OngoingStubbing result = mockitoCore.when(any());

        // Assert
        assertEquals(ongoingStubbing, result);
    }
    
    @Test
    public void testWhen_NoExistingStubbing_ReturnsError() {
        // Arrange
        mockingProgress.pullOngoingStubbing();

        // Act
        OngoingStubbing result = mockitoCore.when(any());

        // Assert
        assertNull(result);
        verify(reporter).missingMethodInvocation();
    }

    @Test
    public void testVerify_ValidMock_ReturnsMockObject() {
        // Arrange
        MyClass mock = mock(MyClass.class);
        VerificationMode mode = mock(VerificationMode.class);

        // Act
        MyClass result = mockitoCore.verify(mock, mode);

        // Assert
        assertEquals(mock, result);
    }
    
    @Test
    public void testVerify_NullMock_ReturnsError() {
        // Arrange
        MyClass nullMock = null;
        VerificationMode mode = mock(VerificationMode.class);

        // Act
        try {
            MyClass result = mockitoCore.verify(nullMock, mode);
            fail("Expected NotAMockException to be thrown");
        } catch (NotAMockException ex) {
            // Assert
            verify(reporter).nullPassedToVerify();
        }
    }
    
    @Test
    public void testVerify_NotAMock_ReturnsError() {
        // Arrange
        MyClass notAMock = new MyClass();
        VerificationMode mode = mock(VerificationMode.class);

        // Act
        try {
            MyClass result = mockitoCore.verify(notAMock, mode);
            fail("Expected NotAMockException to be thrown");
        } catch (NotAMockException ex) {
            // Assert
            verify(reporter).notAMockPassedToVerify();
        }
    }

    @Test
    public void testReset_ValidMocks_ResetsMocks() {
        // Arrange
        MyClass mock1 = mock(MyClass.class);
        MyClass mock2 = mock(MyClass.class);

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
    public void testReset_NoMocks_ReturnsError() {
        // Arrange
        Object[] noMocks = new Object[0];

        // Act
        mockitoCore.reset(noMocks);

        // Assert
        verify(reporter).mocksHaveToBePassedToVerifyNoMoreInteractions();
    }

    @Test
    public void testVerifyNoMoreInteractions_ValidMocks_VerifiesNoMoreInteractions() {
        // Arrange
        MyClass mock1 = mock(MyClass.class);
        MyClass mock2 = mock(MyClass.class);
        Object[] mocks = {mock1, mock2};

        // Act
        mockitoCore.verifyNoMoreInteractions(mocks);

        // Assert
        verify(mockingProgress).validateState();
        verify(mockUtil, times(2)).getMockHandler(any());
        verify(reporter, never()).nullPassedToVerifyNoMoreInteractions();
        verify(reporter, never()).notAMockPassedToVerifyNoMoreInteractions();
        verify(mockUtil).getMockHandler(mock1).verifyNoMoreInteractions();
        verify(mockUtil).getMockHandler(mock2).verifyNoMoreInteractions();
    }

    @Test
    public void testVerifyNoMoreInteractions_NullMock_ReturnsError() {
        // Arrange
        MyClass nullMock = null;
        Object[] mocks = {nullMock, nullMock};

        // Act
        mockitoCore.verifyNoMoreInteractions(mocks);

        // Assert
        verify(reporter, times(2)).nullPassedToVerifyNoMoreInteractions();
        verify(reporter, never()).notAMockPassedToVerifyNoMoreInteractions();
    }

    @Test
    public void testVerifyNoMoreInteractions_NotAMock_ReturnsError() {
        // Arrange
        MyClass notAMock = new MyClass();
        Object[] mocks = {notAMock, notAMock};

        // Act
        mockitoCore.verifyNoMoreInteractions(mocks);

        // Assert
        verify(reporter, times(2)).notAMockPassedToVerifyNoMoreInteractions();
        verify(reporter, never()).nullPassedToVerifyNoMoreInteractions();
    }

    @Test
    public void testInOrder_ValidMocks_CreatesInOrderObject() {
        // Arrange
        MyClass mock1 = mock(MyClass.class);
        MyClass mock2 = mock(MyClass.class);
        Object[] mocks = {mock1, mock2};

        // Act
        InOrder result = mockitoCore.inOrder(mocks);

        // Assert
        assertNotNull(result);
        verify(reporter, never()).mocksHaveToBePassedWhenCreatingInOrder();
        verify(reporter, never()).nullPassedWhenCreatingInOrder();
        verify(reporter, never()).notAMockPassedWhenCreatingInOrder();
    }

    @Test
    public void testInOrder_NoMocks_ReturnsError() {
        // Arrange
        Object[] noMocks = new Object[0];

        // Act
        mockitoCore.inOrder(noMocks);

        // Assert
        verify(reporter).mocksHaveToBePassedWhenCreatingInOrder();
    }

    @Test
    public void testInOrder_NullMock_ReturnsError() {
        // Arrange
        MyClass nullMock = null;
        Object[] mocks = {nullMock, nullMock};

        // Act
        mockitoCore.inOrder(mocks);

        // Assert
        verify(reporter, times(2)).nullPassedWhenCreatingInOrder();
        verify(reporter, never()).mocksHaveToBePassedWhenCreatingInOrder();
        verify(reporter, never()).notAMockPassedWhenCreatingInOrder();
    }

    @Test
    public void testInOrder_NotAMock_ReturnsError() {
        // Arrange
        MyClass notAMock = new MyClass();
        Object[] mocks = {notAMock, notAMock};

        // Act
        mockitoCore.inOrder(mocks);

        // Assert
        verify(reporter, times(2)).notAMockPassedWhenCreatingInOrder();
        verify(reporter, never()).mocksHaveToBePassedWhenCreatingInOrder();
        verify(reporter, never()).nullPassedWhenCreatingInOrder();
    }

    @Test
    public void testDoAnswer_ExistingStubbing_ReturnsStubberObject() {
        // Arrange
        Answer answer = mock(Answer.class);
        OngoingStubbingImpl ongoingStubbing = mock(OngoingStubbingImpl.class);
        mockingProgress.pushOngoingStubbing(ongoingStubbing);

        // Act
        Stubber result = mockitoCore.doAnswer(answer);

        // Assert
        assertNotNull(result);
        verify(mockingProgress).resetOngoingStubbing();
        verify(answer).validateReturn();
        verify(answer).validateThrowable();
    }

    @Test
    public void testStubVoid_ValidMock_ReturnsVoidMethodStubbableObject() {
        // Arrange
        MyClass mock = mock(MyClass.class);
        MockHandlerInterface handler = mock(MockHandlerInterface.class);
        when(mockitoCore.getMockUtil().getMockHandler(mock)).thenReturn(handler);

        // Act
        VoidMethodStubbable result = mockitoCore.stubVoid(mock);

        // Assert
        assertNotNull(result);
        verify(mockingProgress).stubbingStarted();
        verify(mockUtil).getMockHandler(mock);
        verify(handler).voidMethodStubbable(mock);
    }

    @Test
    public void testValidateMockitoUsage_ValidateState_ReturnsNoError() {
        // Act
        mockitoCore.validateMockitoUsage();

        // Assert
        verify(mockingProgress).validateState();
    }

    @Test
    public void testGetLastInvocation_ExistingInvocations_ReturnsLastInvocation() {
        // Arrange
        OngoingStubbingImpl ongoingStubbing = mock(OngoingStubbingImpl.class);
        List<Invocation> invocations = new ArrayList<>();
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        invocations.add(invocation1);
        invocations.add(invocation2);
        when(ongoingStubbing.getRegisteredInvocations()).thenReturn(invocations);
        mockingProgress.pushOngoingStubbing(ongoingStubbing);

        // Act
        Invocation result = mockitoCore.getLastInvocation();

        // Assert
        assertEquals(invocation2, result);
    }

}
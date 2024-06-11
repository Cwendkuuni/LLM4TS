import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.progress.IOngoingStubbing;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.stubbing.Stubber;
import org.mockito.stubbing.StubberImpl;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationMode;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockitoCoreTest {

    @Mock
    private MockUtil mockUtil;

    @Mock
    private MockingProgress mockingProgress;

    @InjectMocks
    private final MockitoCore mockitoCore = new MockitoCore();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testMock_ValidClassAndMockSettings_ShouldReturnMockObject() {
        // Arrange
        Class<TestClass> classToMock = TestClass.class;
        MockSettings mockSettings = new MockSettingsImpl();
        
        // Act
        TestClass mockObject = mockitoCore.mock(classToMock, mockSettings, false);
        
        // Assert
        assertTrue(mockObject instanceof TestClass);
    }

    @Test(expected = InvalidUseOfMatchersException.class)
    public void testMock_NotMockableClass_ShouldThrowException() {
        // Arrange
        Class<NotMockableClass> classToMock = NotMockableClass.class;
        MockSettings mockSettings = new MockSettingsImpl();

        // Act
        NotMockableClass mockObject = mockitoCore.mock(classToMock, mockSettings, false);

        // Assert
        // Exception is expected
    }

    @Test
    public void testStub_ExistingOngoingStubbing_ShouldReturnOngoingStubbing() {
        // Arrange
        IOngoingStubbing expected = mock(IOngoingStubbing.class);
        when(mockingProgress.pullOngoingStubbing()).thenReturn(expected);

        // Act
        IOngoingStubbing result = mockitoCore.stub();

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void testStub_NoExistingOngoingStubbing_ShouldResetAndReportMissingInvocation() {
        // Arrange
        when(mockingProgress.pullOngoingStubbing()).thenReturn(null);
        
        // Assert
        try {
            // Act
            mockitoCore.stub();
            fail("Expected exception to be thrown");
        } catch (MockitoAssertionError e) {
            assertEquals("Missing method invocation", e.getMessage());
        }

        // Assert
        verify(mockingProgress).reset();
    }

    @Test
    public void testWhen_ValidMethodCall_ShouldReturnOngoingStubbing() {
        // Arrange
        IOngoingStubbing expected = mock(IOngoingStubbing.class);
        when(mockingProgress.pullOngoingStubbing()).thenReturn(expected);

        // Act
        OngoingStubbingImpl<?> result = mockitoCore.when("test");

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void testVerify_NullMockPassed_ShouldThrowException() {
        // Act
        try {
            mockitoCore.verify(null, times(1));
            fail("Expected exception to be thrown");
        } catch (NullPointerException e) {
            assertEquals("Null mock passed to verify()", e.getMessage());
        }

        // Assert
        verify(reporter).nullPassedToVerify();
        verify(mockingProgress).verificationStarted(times(1));
    }

    @Test
    public void testVerify_NotAMockPassed_ShouldThrowException() {
        // Arrange
        Object mock = new Object();

        // Act
        try {
            mockitoCore.verify(mock, times(1));
            fail("Expected exception to be thrown");
        } catch (NotAMockException e) {
            assertEquals("Argument should be a mock, but is not", e.getMessage());
        }

        // Assert
        verify(reporter).notAMockPassedToVerify();
        verify(mockingProgress).verificationStarted(times(1));
    }

    @Test
    public void testReset_ValidMocks_ShouldResetMocks() {
        // Arrange
        TestClass mock1 = mock(TestClass.class);
        TestClass mock2 = mock(TestClass.class);

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
    public void testReset_NoMocks_ShouldThrowException() {
        // Act
        try {
            mockitoCore.reset();
            fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("Mocks have to be passed to verifyNoMoreInteractions()"));
        }

        // Assert
        verify(reporter).mocksHaveToBePassedToVerifyNoMoreInteractions();
    }

    @Test
    public void testVerifyNoMoreInteractions_ValidMocks_ShouldVerifyNoMoreInteractions() {
        // Arrange
        TestClass mock1 = mock(TestClass.class);
        TestClass mock2 = mock(TestClass.class);

        // Act
        mockitoCore.verifyNoMoreInteractions(mock1, mock2);

        // Assert
        verify(mockingProgress).validateState();
        verify(mockingProgress, times(2)).verifyNoMoreInteractions();
        verify(mockUtil).getMockHandler(mock1);
        verify(mockUtil).getMockHandler(mock2);
    }

    @Test
    public void testVerifyNoMoreInteractions_NullMockPassed_ShouldThrowException() {
        // Act
        try {
            mockitoCore.verifyNoMoreInteractions((TestClass) null);
            fail("Expected exception to be thrown");
        } catch (NullPointerException e) {
            assertEquals("Null mock passed to verifyNoMoreInteractions()", e.getMessage());
        }

        // Assert
        verify(reporter).nullPassedToVerifyNoMoreInteractions();
    }

    @Test
    public void testVerifyNoMoreInteractions_NotAMockPassed_ShouldThrowException() {
        // Arrange
        Object mock = new Object();

        // Act
        try {
            mockitoCore.verifyNoMoreInteractions(mock);
            fail("Expected exception to be thrown");
        } catch (NotAMockException e) {
            assertEquals("Argument should be a mock, but is not", e.getMessage());
        }

        // Assert
        verify(reporter).notAMockPassedToVerifyNoMoreInteractions();
    }

    @Test
    public void testInOrder_ValidMocks_ShouldCreateInOrderInstance() {
        // Arrange
        TestClass mock1 = mock(TestClass.class);
        TestClass mock2 = mock(TestClass.class);
        Object[] mocks = {mock1, mock2};

        // Act
        InOrder result = mockitoCore.inOrder(mocks);

        // Assert
        assertNotNull(result);
        verify(reporter, never()).mocksHaveToBePassedWhenCreatingInOrder();
        verify(reporter, never()).nullPassedWhenCreatingInOrder();
        verify(reporter, never()).notAMockPassedWhenCreatingInOrder();
        verify(mockUtil, times(2)).isMock(any());
    }

    @Test
    public void testInOrder_NullMocksPassed_ShouldThrowException() {
        // Act
        try {
            mockitoCore.inOrder(null);
            fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("Mocks have to be passed when creating in order"));
        }

        // Assert
        verify(reporter).mocksHaveToBePassedWhenCreatingInOrder();
    }

    @Test
    public void testInOrder_NullMockPassed_ShouldThrowException() {
        // Arrange
        Object mock = null;

        // Act
        try {
            mockitoCore.inOrder(mock);
            fail("Expected exception to be thrown");
        } catch (NullPointerException e) {
            assertEquals("Null mock passed when creating in order", e.getMessage());
        }

        // Assert
        verify(reporter).nullPassedWhenCreatingInOrder();
    }

    @Test
    public void testInOrder_NotAMockPassed_ShouldThrowException() {
        // Arrange
        Object mock = new Object();

        // Act
        try {
            mockitoCore.inOrder(mock);
            fail("Expected exception to be thrown");
        } catch (NotAMockException e) {
            assertEquals("Argument should be a mock, but is not", e.getMessage());
        }

        // Assert
        verify(reporter).notAMockPassedWhenCreatingInOrder();
    }

    @Test
    public void testDoAnswer_ValidAnswer_ShouldReturnStubber() {
        // Arrange
        Answer expectedAnswer = (InvocationOnMock invocation) -> null;

        // Act
        Stubber result = mockitoCore.doAnswer(expectedAnswer);

        // Assert
        assertNotNull(result);
        verify(mockingProgress).stubbingStarted();
        verify(mockingProgress).resetOngoingStubbing();
        verify(mockitoCore, never()).stub();
    }

    @Test
    public void testStubVoid_ValidMock_ShouldReturnVoidMethodStubbable() {
        // Arrange
        TestClass mock = mock(TestClass.class);
        
        // Act
        VoidMethodStubbable<TestClass> result = mockitoCore.stubVoid(mock);
        
        // Assert
        assertNotNull(result);
        verify(mockingProgress).stubbingStarted();
    }

    @Test
    public void testValidateMockitoUsage_ShouldValidateState() {
        // Act
        mockitoCore.validateMockitoUsage();

        // Assert
        verify(mockingProgress).validateState();
    }

    @Test
    public void testGetLastInvocation_HasRegisteredInvocations_ShouldReturnLastInvocation() {
        // Arrange
        OngoingStubbingImpl ongoingStubbing = mock(OngoingStubbingImpl.class);
        List<Invocation> allInvocations = Arrays.asList(mock(Invocation.class), mock(Invocation.class));
        when(ongoingStubbing.getRegisteredInvocations()).thenReturn(allInvocations);
        when(mockingProgress.pullOngoingStubbing()).thenReturn(ongoingStubbing);

        // Act
        Invocation result = mockitoCore.getLastInvocation();

        // Assert
        assertSame(allInvocations.get(allInvocations.size() - 1), result);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetLastInvocation_NoRegisteredInvocations_ShouldThrowException() {
        // Arrange
        OngoingStubbingImpl ongoingStubbing = mock(OngoingStubbingImpl.class);
        List<Invocation> allInvocations = Arrays.asList();
        when(ongoingStubbing.getRegisteredInvocations()).thenReturn(allInvocations);
        when(mockingProgress.pullOngoingStubbing()).thenReturn(ongoingStubbing);

        // Act
        mockitoCore.getLastInvocation();

        // Assert
        // Exception is expected
    }

}

class TestClass {}

class NotMockableClass {}
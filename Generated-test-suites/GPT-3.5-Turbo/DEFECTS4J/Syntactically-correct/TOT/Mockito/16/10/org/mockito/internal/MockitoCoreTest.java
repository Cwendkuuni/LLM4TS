import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.verification.api.VerificationMode;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.DeprecatedOngoingStubbing;
import org.mockito.stubbing.IOngoingStubbing;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.stubbing.Stubber;
import org.mockito.stubbing.VoidMethodStubbable;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockitoCoreTest {

    private MockitoCore mockitoCore;

    @Before
    public void setUp() {
        mockitoCore = new MockitoCore();
    }

    @Test
    public void testMock_withValidClassAndMockSettings_shouldReturnMockInstance() {
        // Arrange
        ClassToMock classToMock = new ClassToMock();
        MockSettings mockSettings = Mockito.mock(MockSettings.class);
            
        // Act
        ClassToMock mock = mockitoCore.mock(ClassToMock.class, mockSettings);

        // Assert
        assertNotNull(mock);
        assertTrue(mock instanceof ClassToMock);
        verifyNoMoreInteractions(mockSettings);
    }

    @Test
    public void testMock_withValidClassAndMockSettingsAndReset_shouldReturnMockInstance() {
        // Arrange
        ClassToMock classToMock = new ClassToMock();
        MockSettings mockSettings = Mockito.mock(MockSettings.class);
            
        // Act
        ClassToMock mock = mockitoCore.mock(ClassToMock.class, mockSettings, true);

        // Assert
        assertNotNull(mock);
        assertTrue(mock instanceof ClassToMock);
        verifyNoMoreInteractions(mockSettings);
    }

    @Test
    public void testStub_withOngoingStubbingAvailable_shouldReturnOngoingStubbing() {
        // Arrange
        MockitoCore spy = Mockito.spy(mockitoCore);        
        IOngoingStubbing stubbing = Mockito.mock(IOngoingStubbing.class);        
        when(spy.stub()).thenReturn(stubbing);
        
        // Act
        IOngoingStubbing result = spy.stub();

        // Assert
        assertNotNull(result);
        assertSame(stubbing, result);
        verify(spy).stub();
    }

    @Test
    public void testStub_withNoOngoingStubbingAvailable_shouldResetAndThrowException() {
        // Arrange
        MockitoCore spy = Mockito.spy(mockitoCore);
        when(spy.stub()).thenReturn(null);
        Reporter reporter = Mockito.mock(Reporter.class);
        doThrow(new RuntimeException()).when(reporter).missingMethodInvocation();
        Whitebox.setInternalState(spy, reporter);

        // Act and Assert
        try {
            spy.stub();
            fail("Expected exception was not thrown.");
        } catch (RuntimeException e) {
            verify(spy).stub();
            verify(reporter).missingMethodInvocation();
        }
    }

    @Test
    public void testStub_withMethodCallAndOngoingStubbingAvailable_shouldReturnOngoingStubbing() {
        // Arrange
        MockitoCore spy = Mockito.spy(mockitoCore);
        IOngoingStubbing stubbing = Mockito.mock(IOngoingStubbing.class);
        when(spy.stub()).thenReturn(stubbing);

        // Act
        OngoingStubbing<String> result = spy.when("test");

        // Assert
        assertNotNull(result);
        assertSame(stubbing, result);
        verify(spy).when("test");
    }

    @Test
    public void testVerify_withValidMockAndVerificationMode_shouldVerifyAndReturnMock() {
        // Arrange
        ClassToMock mock = Mockito.mock(ClassToMock.class);
        VerificationMode mode = times(1);

        // Act
        ClassToMock result = mockitoCore.verify(mock, mode);

        // Assert
        assertNotNull(result);
        assertSame(mock, result);
        verifyNoMoreInteractions(mode);
    }

    @Test
    public void testVerify_withNullMock_shouldThrowException() {
        // Act and Assert
        try {
            mockitoCore.verify(null, times(1));
            fail("Expected exception was not thrown.");
        } catch (Exception e) {
            assertTrue(e instanceof NullPointerException);
            verifyNoMoreInteractions(MockitoCore.class);
        }
    }

    @Test
    public void testVerify_withNotAMockMock_shouldThrowException() {
        // Arrange
        ClassToMock mock = new ClassToMock();

        // Act and Assert
        try {
            mockitoCore.verify(mock, times(1));
            fail("Expected exception was not thrown.");
        } catch (Exception e) {
            assertTrue(e instanceof NotAMockException);
            verifyNoMoreInteractions(MockitoCore.class);
        }
    }

    @Test
    public void testReset_withMultipleMocks_shouldResetMocks() {
        // Arrange
        ClassToMock mock1 = Mockito.mock(ClassToMock.class);
        ClassToMock mock2 = Mockito.mock(ClassToMock.class);
        ClassToMock mock3 = Mockito.mock(ClassToMock.class);

        // Act
        mockitoCore.reset(mock1, mock2, mock3);

        // Assert
        verify(mockitoCore).reset(mock1, mock2, mock3);
        verify(mockitoCore, times(3)).mockUtil.resetMock(any());
    }

    @Test
    public void testReset_withNoMocks_shouldNotResetMocks() {
        // Act
        mockitoCore.reset();

        // Assert
        verify(mockitoCore).reset();
    }

    @Test
    public void testVerifyNoMoreInteractions_withValidMocks_shouldVerifyNoMoreInteractions() {
        // Arrange
        ClassToMock mock1 = Mockito.mock(ClassToMock.class);
        ClassToMock mock2 = Mockito.mock(ClassToMock.class);

        // Act
        mockitoCore.verifyNoMoreInteractions(mock1, mock2);

        // Assert
        verify(mockitoCore).verifyNoMoreInteractions(mock1, mock2);
        verify(mockitoCore, times(2)).mockUtil.getMockHandler(any());
    }

    @Test
    public void testVerifyNoMoreInteractions_withNullMock_shouldThrowException() {
        // Act and Assert
        try {
            mockitoCore.verifyNoMoreInteractions((ClassToMock) null);
            fail("Expected exception was not thrown.");
        } catch (Exception e) {
            assertTrue(e instanceof NotAMockException);
            verifyNoMoreInteractions(MockitoCore.class);
        }
    }

    @Test
    public void testVerifyNoMoreInteractions_withNotAMockMock_shouldThrowException() {
        // Arrange
        ClassToMock mock = new ClassToMock();

        // Act and Assert
        try {
            mockitoCore.verifyNoMoreInteractions(mock);
            fail("Expected exception was not thrown.");
        } catch (Exception e) {
            assertTrue(e instanceof NotAMockException);
            verifyNoMoreInteractions(MockitoCore.class);
        }
    }

    @Test
    public void testInOrder_withValidMocks_shouldCreateInOrderInstance() {
        // Arrange
        ClassToMock mock1 = Mockito.mock(ClassToMock.class);
        ClassToMock mock2 = Mockito.mock(ClassToMock.class);

        // Act
        InOrder result = mockitoCore.inOrder(mock1, mock2);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof InOrder);
    }

    @Test
    public void testInOrder_withNullMock_shouldThrowException() {
        // Arrange
        ClassToMock mock = null;

        // Act and Assert
        try {
            mockitoCore.inOrder(mock);
            fail("Expected exception was not thrown.");
        } catch (Exception e) {
            assertTrue(e instanceof NullPointerException);
            verifyNoMoreInteractions(MockitoCore.class);
        }
    }

    @Test
    public void testInOrder_withNotAMockMock_shouldThrowException() {
        // Arrange
        ClassToMock mock = new ClassToMock();

        // Act and Assert
        try {
            mockitoCore.inOrder(mock);
            fail("Expected exception was not thrown.");
        } catch (Exception e) {
            assertTrue(e instanceof NotAMockException);
            verifyNoMoreInteractions(MockitoCore.class);
        }
    }

    @Test
    public void testDoAnswer_withValidAnswer_shouldReturnStubber() {
        // Arrange
        Answer answer = Mockito.mock(Answer.class);

        // Act
        Stubber result = mockitoCore.doAnswer(answer);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof Stubber);
        verify(mockitoCore).doAnswer(answer);
        verify(mockitoCore).mockingProgress.stubbingStarted();
        verify(mockitoCore).mockingProgress.resetOngoingStubbing();
    }

    @Test
    public void testStubVoid_withValidMock_shouldReturnVoidMethodStubbable() {
        // Arrange
        ClassToMock mock = Mockito.mock(ClassToMock.class);
        MockHandlerInterface handler = Mockito.mock(MockHandlerInterface.class);
        when(mockitoCore.mockUtil.getMockHandler(mock)).thenReturn(handler);
        when(handler.voidMethodStubbable(mock)).thenReturn(Mockito.mock(VoidMethodStubbable.class));
        
        // Act
        VoidMethodStubbable<Void> result = mockitoCore.stubVoid(mock);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof VoidMethodStubbable);
    }

    @Test
    public void testValidateMockitoUsage_shouldValidateState() {
        // Act
        mockitoCore.validateMockitoUsage();

        // Assert
        verify(mockitoCore).validateMockitoUsage();
    }

    @Test
    public void testGetLastInvocation_shouldReturnLastInvocation() {
        // Arrange
        OngoingStubbingImpl ongoingStubbing = Mockito.mock(OngoingStubbingImpl.class);
        when(mockitoCore.mockingProgress.pullOngoingStubbing()).thenReturn(ongoingStubbing);
        when(ongoingStubbing.getRegisteredInvocations()).thenReturn(Arrays.asList(Mockito.mock(Invocation.class), Mockito.mock(Invocation.class)));

        // Act
        Invocation result = mockitoCore.getLastInvocation();

        // Assert
        assertNotNull(result);
        verify(mockitoCore).getLastInvocation();
        verify(ongoingStubbing).getRegisteredInvocations();
    }
}
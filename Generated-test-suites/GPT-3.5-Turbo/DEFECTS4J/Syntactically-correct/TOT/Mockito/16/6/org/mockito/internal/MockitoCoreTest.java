import org.junit.Test;
import org.mockito.*;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.verification.api.VerificationMode;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.stubbing.Stubber;
import org.mockito.stubbing.VoidMethodStubbable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockitoCoreTest {

    @Test
    public void testMock_shouldCreateMockObject() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();
        ClassToMock classToMock = mock(ClassToMock.class);

        // Act
        ClassToMock result = mockitoCore.mock(classToMock.getClass(), new MockSettingsImpl(), false);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof ClassToMock);
        assertEquals(classToMock.getClass(), result.getClass());
    }

    @Test
    public void testStub_shouldReturnStubbingObject() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();
        OngoingStubbing ongoingStubbing = mock(OngoingStubbing.class);
        mockitoCore.mockingProgress.stubbingStarted();
        mockitoCore.mockingProgress.ongoingStubbing = ongoingStubbing;

        // Act
        IOngoingStubbing result = mockitoCore.stub();

        // Assert
        assertNotNull(result);
        assertEquals(ongoingStubbing, result);
    }

    @Test(expected = NotAMockException.class)
    public void testStub_shouldThrowNotAMockException() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();

        // Act
        mockitoCore.stub();

        // Assert
        // Exception expected
    }

    @Test
    public void testWhen_shouldReturnOngoingStubbing() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();
        OngoingStubbing ongoingStubbing = mock(OngoingStubbing.class);
        mockitoCore.mockingProgress.stubbingStarted();
        mockitoCore.mockingProgress.ongoingStubbing = ongoingStubbing;

        // Act
        OngoingStubbing result = mockitoCore.when(null);

        // Assert
        assertNotNull(result);
        assertEquals(ongoingStubbing, result);
    }

    @Test(expected = NotAMockException.class)
    public void testWhen_shouldThrowNotAMockException() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();

        // Act
        mockitoCore.when(null);

        // Assert
        // Exception expected
    }

    @Test
    public void testVerify_shouldValidateMockObject() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();
        ClassToMock mock = mock(ClassToMock.class);
        VerificationMode mode = VerificationModeFactory.times(1);

        // Act
        ClassToMock result = mockitoCore.verify(mock, mode);

        // Assert
        assertNotNull(result);
        assertEquals(mock, result);
        verify(mock, mode);
    }

    @Test(expected = NotAMockException.class)
    public void testVerify_shouldThrowNotAMockException() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();
        ClassToMock obj = new ClassToMock();
        VerificationMode mode = VerificationModeFactory.times(1);

        // Act
        mockitoCore.verify(obj, mode);

        // Assert
        // Exception expected
    }

    @Test
    public void testReset_shouldResetMockObjects() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();
        ClassToMock mock1 = mock(ClassToMock.class);
        ClassToMock mock2 = mock(ClassToMock.class);

        // Act
        mockitoCore.reset(mock1, mock2);

        // Assert
        verify(mock1).reset();
        verify(mock2).reset();
    }

    @Test
    public void testReset_shouldResetOngoingStubbing() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();
        OngoingStubbing ongoingStubbing = mock(OngoingStubbing.class);
        mockitoCore.mockingProgress.ongoingStubbing = ongoingStubbing;

        // Act
        mockitoCore.reset();

        // Assert
        verify(ongoingStubbing).reset();
    }

    @Test
    public void testVerifyNoMoreInteractions_withValidMocks_shouldNotThrowExceptions() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();
        ClassToMock mock1 = mock(ClassToMock.class);
        ClassToMock mock2 = mock(ClassToMock.class);

        // Act
        mockitoCore.verifyNoMoreInteractions(mock1, mock2);

        // Assert
        verifyNoMoreInteractions(mock1, mock2);
    }

    @Test(expected = NotAMockException.class)
    public void testVerifyNoMoreInteractions_withNotAMock_shouldThrowNotAMockException() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();
        ClassToMock obj = new ClassToMock();

        // Act
        mockitoCore.verifyNoMoreInteractions(obj);

        // Assert
        // Exception expected
    }

    @Test(expected = NotAMockException.class)
    public void testVerifyNoMoreInteractions_withNull_shouldThrowNotAMockException() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();

        // Act
        mockitoCore.verifyNoMoreInteractions(null);

        // Assert
        // Exception expected
    }

    @Test(expected = AssertionError.class)
    public void testVerifyNoMoreInteractions_withEmptyMocks_shouldThrowAssertionError() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();
        ClassToMock[] mocks = new ClassToMock[0];

        // Act
        mockitoCore.verifyNoMoreInteractions(mocks);

        // Assert
        // Exception expected
    }

    @Test
    public void testInOrder_withValidMocks_shouldCreateInOrderObject() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();
        ClassToMock mock1 = mock(ClassToMock.class);
        ClassToMock mock2 = mock(ClassToMock.class);
        Object[] mocks = {mock1, mock2};

        // Act
        InOrder result = mockitoCore.inOrder(mock1, mock2);

        // Assert
        assertNotNull(result);
    }

    @Test(expected = AssertionError.class)
    public void testInOrder_withEmptyMocks_shouldThrowAssertionError() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();
        ClassToMock[] mocks = new ClassToMock[0];

        // Act
        mockitoCore.inOrder(mocks);

        // Assert
        // Exception expected
    }

    @Test(expected = AssertionError.class)
    public void testInOrder_withNullMock_shouldThrowAssertionError() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();
        ClassToMock[] mocks = {null};

        // Act
        mockitoCore.inOrder(mocks);

        // Assert
        // Exception expected
    }

    @Test(expected = NotAMockException.class)
    public void testInOrder_withNotAMock_shouldThrowNotAMockException() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();
        ClassToMock obj = new ClassToMock();

        // Act
        mockitoCore.inOrder(obj);

        // Assert
        // Exception expected
    }

    @Test
    public void testDoAnswer_shouldReturnStubber() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();
        Answer answer = mock(Answer.class);
        mockitoCore.mockingProgress.stubbingStarted();
        mockitoCore.mockingProgress.resetOngoingStubbing();

        // Act
        Stubber result = mockitoCore.doAnswer(answer);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testStubVoid_shouldReturnVoidMethodStubbable() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();
        ClassToMock mock = mock(ClassToMock.class);
        VoidMethodStubbable<Void> stubbable = mock(VoidMethodStubbable.class);
        when(mockitoCore.mockUtil.getMockHandler(mock)).thenReturn(stubbable);
        mockitoCore.mockingProgress.stubbingStarted();

        // Act
        VoidMethodStubbable<Void> result = mockitoCore.stubVoid(mock);

        // Assert
        assertNotNull(result);
        assertEquals(stubbable, result);
    }

    @Test
    public void testValidateMockitoUsage_shouldValidateState() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();

        // Act
        mockitoCore.validateMockitoUsage();

        // Assert
        // No exceptions expected
    }

    @Test
    public void testGetLastInvocation_shouldReturnLastInvocation() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();
        OngoingStubbingImpl ongoingStubbing = mock(OngoingStubbingImpl.class);
        LinkedList<Invocation> invocations = new LinkedList<>();
        Invocation lastInvocation = mock(Invocation.class);
        invocations.add(mock(Invocation.class));
        invocations.add(lastInvocation);

        // Act
        when(mockitoCore.mockingProgress.pullOngoingStubbing()).thenReturn(ongoingStubbing);
        when(ongoingStubbing.getRegisteredInvocations()).thenReturn(invocations);
        Invocation result = mockitoCore.getLastInvocation();

        // Assert
        assertNotNull(result);
        assertEquals(lastInvocation, result);
    }
}


class ClassToMock {
    public void reset() {}
}
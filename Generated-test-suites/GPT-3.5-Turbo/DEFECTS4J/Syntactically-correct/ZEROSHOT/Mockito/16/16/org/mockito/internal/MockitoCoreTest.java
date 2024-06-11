import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.MockSettings;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubberImpl;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.verification.api.VerificationMode;
import org.mockito.stubbing.DeprecatedOngoingStubbing;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.stubbing.Stubber;
import org.mockito.stubbing.VoidMethodStubbable;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
public class MockitoCoreTest {

    private MockitoCore mockitoCore;

    @Before
    public void setup() {
        mockitoCore = new MockitoCore();
    }

    @Test
    public void testMock_validClassToMockAndMockSettings_returnsMock() {
        // Arrange
        Class<Object> classToMock = Object.class;
        MockSettings mockSettings = new MockSettingsImpl();

        // Act
        Object result = mockitoCore.mock(classToMock, mockSettings);

        // Assert
        assertNotNull(result);
    }

    @Test(expected = NotAMockException.class)
    public void testVerify_invalidMockObject_throwsNotAMockException() {
        // Arrange
        Object mock = new Object();
        VerificationMode mode = VerificationModeFactory.times(1);

        // Act
        mockitoCore.verify(mock, mode);

        // Assert
        // Exception expected
    }

    @Test
    public void testReset_validMocks_resetsMocks() {
        // Arrange
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);

        // Act
        mockitoCore.reset(mock1, mock2);

        // Assert
        verify(mock1).reset();
        verify(mock2).reset();
    }

    @Test(expected = NotAMockException.class)
    public void testVerifyNoMoreInteractions_invalidMockObject_throwsNotAMockException() {
        // Arrange
        Object mock = new Object();

        // Act
        mockitoCore.verifyNoMoreInteractions(mock);

        // Assert
        // Exception expected
    }

    @Test
    public void testInOrder_validMocks_returnsInOrderObject() {
        // Arrange
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);

        // Act
        InOrder inOrder = mockitoCore.inOrder(mock1, mock2);

        // Assert
        assertNotNull(inOrder);
    }

    @Test
    public void testDoAnswer_validAnswer_returnsStubber() {
        // Arrange
        Answer answer = mock(Answer.class);

        // Act
        Stubber stubber = mockitoCore.doAnswer(answer);

        // Assert
        assertNotNull(stubber);
    }

    @Test
    public void testStubVoid_validMock_returnsVoidMethodStubbable() {
        // Arrange
        Object mock = mock(Object.class);

        // Act
        VoidMethodStubbable stubbable = mockitoCore.stubVoid(mock);

        // Assert
        assertNotNull(stubbable);
    }

    @Test
    public void testValidateMockitoUsage_validateUsage_doesNotThrowException() {
        // Arrange

        // Act
        mockitoCore.validateMockitoUsage();

        // Assert
        // No exception expected
    }

    @Test
    public void testGetLastInvocation_multipleInvocations_returnsLastInvocation() {
        // Arrange
        OngoingStubbingImpl ongoingStubbing = mock(OngoingStubbingImpl.class);
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        when(ongoingStubbing.getRegisteredInvocations()).thenReturn(Arrays.asList(invocation1, invocation2));
        MockingProgress mockingProgress = mock(ThreadSafeMockingProgress.class);
        when(mockingProgress.pullOngoingStubbing()).thenReturn(ongoingStubbing);
        MagicMockHandler<Object> mockHandler = mock(MagicMockHandler.class);
        Object mock = mock(Object.class);
        when(mockHandler.verifyNoMoreInteractions()).thenReturn(mockHandler);
        MockUtil mockUtil = mock(MockUtil.class);
        when(mockUtil.getMockHandler(mock)).thenReturn(mockHandler);
        mockitoCore.setMockUtil(mockUtil);
        mockitoCore.setMockingProgress(mockingProgress);

        // Act
        Invocation result = mockitoCore.getLastInvocation();

        // Assert
        assertEquals(invocation2, result);
    }
}
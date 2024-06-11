import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockSettings;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.MockitoCore;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockitoCoreTest {

    private MockitoCore mockitoCore;

    @Before
    public void setUp() {
        mockitoCore = new MockitoCore();
    }

    @Test
    public void testMock_Success() {
        // Arrange
        Class<?> classToMock = String.class;
        MockSettings mockSettings = Mockito.mock(MockSettings.class);

        // Act
        Object result = mockitoCore.mock(classToMock, mockSettings);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof String);
    }

    @Test
    public void testMock_NullClassToMock() {
        // Arrange
        Class<?> classToMock = null;
        MockSettings mockSettings = Mockito.mock(MockSettings.class);

        // Act
        try {
            mockitoCore.mock(classToMock, mockSettings);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Assert
            assertEquals("classToMock cannot be null", e.getMessage());
        }
    }

    @Test
    public void testStub_Success() {
        // Arrange
        IOngoingStubbing expectedStubbing = Mockito.mock(IOngoingStubbing.class);

        // Act
        IOngoingStubbing result = mockitoCore.stub();

        // Assert
        assertEquals(expectedStubbing, result);
    }

    @Test
    public void testStub_NoOngoingStubbing() {
        // Arrange
        Reporter reporter = mock(Reporter.class);
        MockitoCore mockitoCore = spy(new MockitoCore());
        doReturn(reporter).when(mockitoCore).getReporter();

        // Act
        IOngoingStubbing result = mockitoCore.stub();

        // Assert
        assertNull(result);
        verify(reporter).missingMethodInvocation();
    }

    @Test
    public void testStub_Generic_Success() {
        // Arrange
        IOngoingStubbing expectedStubbing = Mockito.mock(IOngoingStubbing.class);

        // Act
        IOngoingStubbing result = mockitoCore.stub();

        // Assert
        assertEquals(expectedStubbing, result);
    }

    @Test
    public void testWhen_Success() {
        // Arrange
        IOngoingStubbing expectedStubbing = Mockito.mock(IOngoingStubbing.class);

        // Act
        IOngoingStubbing result = mockitoCore.when(any());

        // Assert
        assertEquals(expectedStubbing, result);
    }

    @Test
    public void testWhen_Generic_Success() {
        // Arrange
        IOngoingStubbing expectedStubbing = Mockito.mock(IOngoingStubbing.class);

        // Act
        IOngoingStubbing result = mockitoCore.when(any());

        // Assert
        assertEquals(expectedStubbing, result);
    }

    @Test
    public void testVerify_Success() {
        // Arrange
        Object mock = Mockito.mock(Object.class);
        VerificationMode mode = Mockito.mock(VerificationMode.class);

        // Act
        Object result = mockitoCore.verify(mock, mode);

        // Assert
        assertEquals(mock, result);
    }

    @Test
    public void testVerify_NullMock() {
        // Arrange
        VerificationMode mode = Mockito.mock(VerificationMode.class);

        // Act
        try {
            mockitoCore.verify(null, mode);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Assert
            assertEquals("mock cannot be null", e.getMessage());
        }
    }

    @Test
    public void testVerify_NotAMock() {
        // Arrange
        Object mock = new Object();
        VerificationMode mode = Mockito.mock(VerificationMode.class);

        // Act
        try {
            mockitoCore.verify(mock, mode);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Assert
            assertEquals("mock is not a mock object", e.getMessage());
        }
    }

    @Test
    public void testReset_Success() {
        // Arrange
        Object mock = Mockito.mock(Object.class);
        Object[] mocks = {mock};

        // Act
        mockitoCore.reset(mocks);

        // Assert
        verify(mock).resetMock();
    }

    @Test
    public void testReset_NoMocks() {
        // Act
        try {
            mockitoCore.reset();
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Assert
            assertEquals("mocks have to be passed to reset", e.getMessage());
        }
    }

    @Test
    public void testReset_NullMocks() {
        // Act
        try {
            mockitoCore.reset((Object[]) null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Assert
            assertEquals("mocks cannot be null", e.getMessage());
        }
    }

    @Test
    public void testVerifyNoMoreInteractions_Success() {
        // Arrange
        Object mock = Mockito.mock(Object.class);

        // Act
        mockitoCore.verifyNoMoreInteractions(mock);

        // Assert
        verify(mock, times(1)).getMockHandler().getInvocationContainer().getInvocations();
    }

    @Test
    public void testVerifyNoMoreInteractions_NullMock() {
        // Act
        try {
            mockitoCore.verifyNoMoreInteractions(null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Assert
            assertEquals("mock cannot be null", e.getMessage());
        }
    }

    @Test
    public void testVerifyNoMoreInteractions_NotAMock() {
        // Arrange
        Object mock = new Object();

        // Act
        try {
            mockitoCore.verifyNoMoreInteractions(mock);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Assert
            assertEquals("mock is not a mock object", e.getMessage());
        }
    }

    @Test
    public void testVerifyNoMoreInteractionsInOrder_Success() {
        // Arrange
        List<Object> mocks = Mockito.mock(List.class);
        InOrderContext inOrderContext = Mockito.mock(InOrderContext.class);

        // Act
        mockitoCore.verifyNoMoreInteractionsInOrder(mocks, inOrderContext);

        // Assert
        verify(inOrderContext).verifyInOrder();
    }

    @Test
    public void testInOrder_Success() {
        // Arrange
        Object mock = Mockito.mock(Object.class);

        // Act
        InOrder result = mockitoCore.inOrder(mock);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testInOrder_NullMocks() {
        // Act
        try {
            mockitoCore.inOrder(null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Assert
            assertEquals("mocks cannot be null", e.getMessage());
        }
    }

    @Test
    public void testInOrder_NotAMock() {
        // Arrange
        Object mock = new Object();

        // Act
        try {
            mockitoCore.inOrder(mock);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Assert
            assertEquals("mock is not a mock object", e.getMessage());
        }
    }

    @Test
    public void testDoAnswer_Success() {
        // Arrange
        Answer answer = Mockito.mock(Answer.class);

        // Act
        Stubber result = mockitoCore.doAnswer(answer);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testStubVoid_Success() {
        // Arrange
        Object mock = Mockito.mock(Object.class);
        VoidMethodStubbable expectedStubbing = Mockito.mock(VoidMethodStubbable.class);

        // Act
        VoidMethodStubbable result = mockitoCore.stubVoid(mock);

        // Assert
        assertEquals(expectedStubbing, result);
    }

    @Test
    public void testValidateMockitoUsage_Success() {
        // Act
        mockitoCore.validateMockitoUsage();

        // Assert
        // No exception should be thrown
    }

    @Test
    public void testGetLastInvocation_Success() {
        // Arrange
        Invocation expectedInvocation = Mockito.mock(Invocation.class);

        // Act
        Invocation result = mockitoCore.getLastInvocation();

        // Assert
        assertEquals(expectedInvocation, result);
    }

}
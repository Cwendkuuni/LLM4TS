import org.junit.Before;
import org.junit.Test;
import org.mockito.cglib.proxy.MethodProxy;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.MockHandler;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.reflection.LenientCopyTool;

import java.io.Serializable;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MockUtilTest {

    private MockUtil mockUtil;

    @Before
    public void setup() {
        mockUtil = new MockUtil(new MockCreationValidator());
    }

    @Test
    public void testCreateMock() {
        // Arrange
        Class<TestInterface> classToMock = TestInterface.class;
        MockSettingsImpl settings = new MockSettingsImpl();

        // Act
        TestInterface mock = mockUtil.createMock(classToMock, settings);

        // Assert
        assertNotNull(mock);
    }

    @Test(expected = NotAMockException.class)
    public void testResetMock_NullMock_ThrowsNotAMockException() {
        // Arrange
        TestInterface mock = null;

        // Act
        mockUtil.resetMock(mock);

        // Assert - Expects exception
    }

    @Test
    public void testResetMock_ValidMock_ResetMockHandlerCallback() {
        // Arrange
        TestInterface mock = mock(TestInterface.class);
        MockHandler mockHandler = mock(MockHandler.class);
        MethodInterceptorFilter methodInterceptorFilter = mock(MethodInterceptorFilter.class, CALLS_REAL_METHODS);
        when(methodInterceptorFilter.getHandler()).thenReturn(mockHandler);
        ((Factory) mock).setCallback(0, methodInterceptorFilter);

        // Act
        mockUtil.resetMock(mock);

        // Assert
        verify(mockHandler).reset();
        verify(((Factory) mock), times(1)).setCallback(any(Integer.class), any(MethodInterceptorFilter.class));
    }

    @Test
    public void testGetMockHandler_NullMock_ThrowsNotAMockException() {
        // Arrange
        TestInterface mock = null;

        // Act
        try {
            mockUtil.getMockHandler(mock);
        } catch (NotAMockException e) {
            // Assert
            assertEquals("Argument should be a mock, but is null!", e.getMessage());
        }
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_NonMockitoMock_ThrowsNotAMockException() {
        // Arrange
        TestInterface mock = mock(TestInterface.class);

        // Act
        mockUtil.getMockHandler(mock);

        // Assert - Expects exception
    }

    @Test
    public void testGetMockHandler_MockitoMock_ReturnsMockHandler() {
        // Arrange
        TestInterface mock = mock(TestInterface.class);
        MockHandler mockHandler = mock(MockHandler.class);
        MethodInterceptorFilter methodInterceptorFilter = mock(MethodInterceptorFilter.class, CALLS_REAL_METHODS);
        when(methodInterceptorFilter.getHandler()).thenReturn(mockHandler);
        ((Factory) mock).setCallback(0, methodInterceptorFilter);

        // Act
        MockHandler result = mockUtil.getMockHandler(mock);

        // Assert
        assertEquals(mockHandler, result);
    }

    @Test
    public void testIsMock_MockitoMock_ReturnsTrue() {
        // Arrange
        TestInterface mock = mock(TestInterface.class);

        // Act
        boolean result = mockUtil.isMock(mock);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsMock_Null_ReturnsFalse() {
        // Arrange
        TestInterface mock = null;

        // Act
        boolean result = mockUtil.isMock(mock);

        // Assert
        assertFalse(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsMock_NonMockObject_ThrowsIllegalArgumentException() {
        // Arrange
        Object notAMock = new Object();

        // Act
        mockUtil.isMock(notAMock);

        // Assert - Expects exception
    }

    @Test
    public void testGetMockName_ReturnsMockName() {
        // Arrange
        TestInterface mock = mock(TestInterface.class);
        MethodInterceptorFilter methodInterceptorFilter = mock(MethodInterceptorFilter.class, CALLS_REAL_METHODS);
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        when(methodInterceptorFilter.getMockSettings()).thenReturn(mockSettings);
        ((Factory) mock).setCallback(0, methodInterceptorFilter);

        // Act
        MockName result = mockUtil.getMockName(mock);

        // Assert
        assertNotNull(result);
        assertEquals(mockSettings.getMockName(), result);
    }

    public interface TestInterface {
    }
}
import org.junit.Before;
import org.junit.Test;
import org.mockito.cglib.proxy.Callback;
import org.mockito.cglib.proxy.Factory;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.InvocationNotifierHandler;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.reflection.LenientCopyTool;

import java.io.Serializable;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockUtilTest {

    private MockUtil mockUtil;

    @Before
    public void setUp() {
        mockUtil = new MockUtil();
    }

    @Test
    public void createMock_ValidClassAndSettings_ReturnsMock() {
        // Arrange
        Class<?> classToMock = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();

        // Act
        Object result = mockUtil.createMock(classToMock, settings);

        // Assert
        assertNotNull(result);
        assertTrue(classToMock.isInstance(result));
        assertTrue(result instanceof Factory);
    }

    @Test
    public void createMock_NullClassToMock_ThrowsIllegalArgumentException() {
        // Arrange
        Class<?> classToMock = null;
        MockSettingsImpl settings = new MockSettingsImpl();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> mockUtil.createMock(classToMock, settings));
    }

    @Test
    public void resetMock_ValidMock_ResetsMockHandler() {
        // Arrange
        Object mock = mock(String.class);

        // Act
        mockUtil.resetMock(mock);

        // Assert
        MockHandlerInterface<?> mockHandler = mockUtil.getMockHandler(mock);
        assertNotNull(mockHandler);
    }

    @Test
    public void resetMock_NullMock_ThrowsNotAMockException() {
        // Arrange
        Object mock = null;

        // Act & Assert
        assertThrows(NotAMockException.class, () -> mockUtil.resetMock(mock));
    }

    @Test
    public void getMockHandler_ValidMock_ReturnsMockHandler() {
        // Arrange
        Object mock = mock(String.class);

        // Act
        MockHandlerInterface<?> result = mockUtil.getMockHandler(mock);

        // Assert
        assertNotNull(result);
        assertEquals(result.getClass(), MockHandler.class);
    }

    @Test
    public void getMockHandler_NullMock_ThrowsNotAMockException() {
        // Arrange
        Object mock = null;

        // Act & Assert
        assertThrows(NotAMockException.class, () -> mockUtil.getMockHandler(mock));
    }

    @Test
    public void isMock_ValidMock_ReturnsTrue() {
        // Arrange
        Object mock = mock(String.class);

        // Act
        boolean result = mockUtil.isMock(mock);

        // Assert
        assertTrue(result);
    }

    @Test
    public void isMock_NullMock_ReturnsFalse() {
        // Arrange
        Object mock = null;

        // Act
        boolean result = mockUtil.isMock(mock);

        // Assert
        assertFalse(result);
    }

    @Test
    public void getMockName_ValidMock_ReturnsMockName() {
        // Arrange
        Object mock = mock(String.class);

        // Act
        MockName result = mockUtil.getMockName(mock);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void getMockName_NullMock_ThrowsNotAMockException() {
        // Arrange
        Object mock = null;

        // Act & Assert
        assertThrows(NotAMockException.class, () -> mockUtil.getMockName(mock));
    }

}
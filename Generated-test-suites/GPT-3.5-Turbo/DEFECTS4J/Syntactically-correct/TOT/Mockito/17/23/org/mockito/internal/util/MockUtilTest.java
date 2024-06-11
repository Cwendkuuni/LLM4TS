import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockHandler;
import org.mockito.internal.util.MockUtil;

import static org.junit.Assert.*;
import static org.mockito.Mockito.RETURNS_DEFAULTS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.withSettings;

public class MockUtilTest {
    
    private MockUtil mockUtil;
    private MockSettingsImpl mockSettings;

    @Before
    public void setup() {
        mockUtil = new MockUtil();
        mockSettings = new MockSettingsImpl();
    }

    @Test
    public void createMock_ValidClass_ValidMock() {
        // Arrange
        Class<?> classToMock = String.class;

        // Act
        Object mock = mockUtil.createMock(classToMock, mockSettings);

        // Assert
        assertNotNull(mock);
    }

    @Test(expected = NotAMockException.class)
    public void createMock_NullClass_ThrowsNotAMockException() {
        // Arrange
        Class<?> classToMock = null;

        // Act
        mockUtil.createMock(classToMock, mockSettings);

        // Assert
        // Not expecting any assertion as the exception is expected
    }

    @Test
    public void resetMock_ValidMock_MockReset() {
        // Arrange
        Object mock = mock(String.class);

        // Act
        mockUtil.resetMock(mock);

        // Assert
        verify(mock).setCallback(0, Mockito.any());
    }

    @Test(expected = NotAMockException.class)
    public void resetMock_NullMock_ThrowsNotAMockException() {
        // Arrange
        Object mock = null;

        // Act
        mockUtil.resetMock(mock);

        // Assert
        // Not expecting any assertion as the exception is expected
    }

    @Test
    public void getMockHandler_ValidMock_ReturnsMockHandler() {
        // Arrange
        Object mock = mock(String.class);

        // Act
        MockHandler handler = mockUtil.getMockHandler(mock);

        // Assert
        assertNotNull(handler);
    }

    @Test(expected = NotAMockException.class)
    public void getMockHandler_NullMock_ThrowsNotAMockException() {
        // Arrange
        Object mock = null;

        // Act
        mockUtil.getMockHandler(mock);

        // Assert
        // Not expecting any assertion as the exception is expected
    }

    @Test(expected = NotAMockException.class)
    public void getMockHandler_NonMockObject_ThrowsNotAMockException() {
        // Arrange
        Object nonMockObject = "Not a mock";

        // Act
        mockUtil.getMockHandler(nonMockObject);

        // Assert
        // Not expecting any assertion as the exception is expected
    }

    @Test
    public void isMock_ValidMock_ReturnsTrue() {
        // Arrange
        Object mock = mock(String.class);

        // Act
        boolean isMock = mockUtil.isMock(mock);

        // Assert
        assertTrue(isMock);
    }

    @Test
    public void isMock_NullObject_ReturnsFalse() {
        // Arrange
        Object mock = null;

        // Act
        boolean isMock = mockUtil.isMock(mock);

        // Assert
        assertFalse(isMock);
    }

    @Test
    public void getMockName_ValidMock_ReturnsMockName() {
        // Arrange
        Object mock = mock(String.class);

        // Act
        MockName mockName = mockUtil.getMockName(mock);

        // Assert
        assertNotNull(mockName);
    }

    @Test(expected = NotAMockException.class)
    public void getMockName_NullMock_ThrowsNotAMockException() {
        // Arrange
        Object mock = null;

        // Act
        mockUtil.getMockName(mock);

        // Assert
        // Not expecting any assertion as the exception is expected
    }
}
import org.junit.Before;
import org.junit.Test;
import org.mockito.cglib.proxy.Factory;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MockSettingsImpl;

import static org.mockito.Mockito.*;

public class MockUtilTest {

    private MockUtil mockUtil;

    @Before
    public void setup() {
        mockUtil = new MockUtil();
    }

    @Test
    public void testCreateMock_ValidClass_ValidSettings_ReturnsMock() {
        // Arrange
        Class<?> classToMock = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();

        // Act
        Object result = mockUtil.createMock(classToMock, settings);

        // Assert
        assertNotNull(result);
        assertTrue(classToMock.isInstance(result));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateMock_NullClass_ThrowsIllegalArgumentException() {
        // Arrange
        Class<?> classToMock = null;
        MockSettingsImpl settings = new MockSettingsImpl();

        // Act
        mockUtil.createMock(classToMock, settings);

        // Assert
        // Exception expected
    }

    @Test
    public void testResetMock_ValidMock_MockResetSuccessfully() {
        // Arrange
        String mock = mock(String.class);

        // Act
        mockUtil.resetMock(mock);

        // Assert
        verify(mock, times(1)).getClass();
    }

    @Test(expected = NotAMockException.class)
    public void testResetMock_NullMock_ThrowsNotAMockException() {
        // Arrange
        String mock = null;

        // Act
        mockUtil.resetMock(mock);

        // Assert
        // Exception expected
    }

    @Test
    public void testGetMockHandler_ValidMock_ReturnsMockHandlerInterface() {
        // Arrange
        String mock = mock(String.class);

        // Act
        MockHandlerInterface<String> result = mockUtil.getMockHandler(mock);

        // Assert
        assertNotNull(result);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_NullMock_ThrowsNotAMockException() {
        // Arrange
        String mock = null;

        // Act
        mockUtil.getMockHandler(mock);

        // Assert
        // Exception expected
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_NotAMockObject_ThrowsNotAMockException() {
        // Arrange
        String notAMock = "not a mock";

        // Act
        mockUtil.getMockHandler(notAMock);

        // Assert
        // Exception expected
    }

    @Test
    public void testIsMock_ValidMock_ReturnsTrue() {
        // Arrange
        String mock = mock(String.class);

        // Act
        boolean result = mockUtil.isMock(mock);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsMock_NullObject_ReturnsFalse() {
        // Arrange
        String nullObject = null;

        // Act
        boolean result = mockUtil.isMock(nullObject);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsMock_NotAMockObject_ReturnsFalse() {
        // Arrange
        String notAMock = "not a mock";

        // Act
        boolean result = mockUtil.isMock(notAMock);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testGetMockName_ValidMock_ReturnsMockName() {
        // Arrange
        String mock = mock(String.class);

        // Act
        MockName result = mockUtil.getMockName(mock);

        // Assert
        assertNotNull(result);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockName_NullMock_ThrowsNotAMockException() {
        // Arrange
        String mock = null;

        // Act
        mockUtil.getMockName(mock);

        // Assert
        // Exception expected
    }

}
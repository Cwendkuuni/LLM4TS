import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class MockUtilTest {

    private MockCreationValidator mockCreationValidator;
    private MockUtil mockUtil;

    @Before
    public void setUp() {
        mockCreationValidator = mock(MockCreationValidator.class);
        mockUtil = new MockUtil(mockCreationValidator);
    }

    @Test
    public void testCreateMock_ValidClassAndSettings_CreatesMock() {
        // Arrange
        Class<?> classToMock = Object.class;
        MockSettingsImpl settings = mock(MockSettingsImpl.class);

        // Act
        Object result = mockUtil.createMock(classToMock, settings);

        // Assert
        assertNotNull(result);
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMock_InvalidClass_ThrowsException() {
        // Arrange
        Class<?> classToMock = null;
        MockSettingsImpl settings = mock(MockSettingsImpl.class);

        // Act
        mockUtil.createMock(classToMock, settings);

        // Assert
        // Exception expected
    }

    @Test
    public void testResetMock_ValidMock_ResetsMock() {
        // Arrange
        Object mock = mock(Object.class);

        // Act
        mockUtil.resetMock(mock);

        // Assert
        verify(mock, times(1)).setCallback(0, any(MethodInterceptorFilter.class));
    }

    @Test(expected = NotAMockException.class)
    public void testResetMock_NullMock_ThrowsException() {
        // Arrange
        Object mock = null;

        // Act
        mockUtil.resetMock(mock);

        // Assert
        // Exception expected
    }

    @Test(expected = NotAMockException.class)
    public void testResetMock_InvalidMock_ThrowsException() {
        // Arrange
        Object mock = new Object();

        // Act
        mockUtil.resetMock(mock);

        // Assert
        // Exception expected
    }

    @Test
    public void testGetMockHandler_ValidMock_ReturnsMockHandler() {
        // Arrange
        Object mock = mock(Object.class);

        // Act
        MockHandlerInterface result = mockUtil.getMockHandler(mock);

        // Assert
        assertNotNull(result);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_NullMock_ThrowsException() {
        // Arrange
        Object mock = null;

        // Act
        mockUtil.getMockHandler(mock);

        // Assert
        // Exception expected
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_InvalidMock_ThrowsException() {
        // Arrange
        Object mock = new Object();

        // Act
        mockUtil.getMockHandler(mock);

        // Assert
        // Exception expected
    }

    @Test
    public void testIsMock_ValidMock_ReturnsTrue() {
        // Arrange
        Object mock = mock(Object.class);

        // Act
        boolean result = mockUtil.isMock(mock);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsMock_NullMock_ReturnsFalse() {
        // Arrange
        Object mock = null;

        // Act
        boolean result = mockUtil.isMock(mock);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testGetMockName_ValidMock_ReturnsMockName() {
        // Arrange
        Object mock = mock(Object.class);

        // Act
        MockName result = mockUtil.getMockName(mock);

        // Assert
        assertNotNull(result);
    }

}
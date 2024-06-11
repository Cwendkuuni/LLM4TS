import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockUtilTest {

    private MockUtil mockUtil;
    private MockCreationValidator creationValidator;

    @Before
    public void setUp() {
        creationValidator = mock(MockCreationValidator.class);
        mockUtil = new MockUtil(creationValidator);
    }

    @Test
    public void createMock_validClassAndSettings_mockCreated() {
        // Arrange
        Class<?> classToMock = String.class;
        MockSettingsImpl settings = mock(MockSettingsImpl.class);
        Object expectedMock = mock(classToMock);

        // Act
        Object actualMock = mockUtil.createMock(classToMock, settings);

        // Assert
        assertNotNull(actualMock);
        assertEquals(expectedMock.getClass(), actualMock.getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void createMock_nullClassToMock_throwIllegalArgumentException() {
        // Arrange
        Class<?> classToMock = null;
        MockSettingsImpl settings = mock(MockSettingsImpl.class);

        // Act
        mockUtil.createMock(classToMock, settings);

        // Assert
        // Should throw exception
    }

    @Test(expected = IllegalArgumentException.class)
    public void createMock_invalidExtraInterfaces_throwIllegalArgumentException() {
        // Arrange
        Class<?> classToMock = String.class;
        Class<?>[] extraInterfaces = {Integer.class};
        MockSettingsImpl settings = mock(MockSettingsImpl.class);
        when(settings.getExtraInterfaces()).thenReturn(extraInterfaces);

        // Act
        mockUtil.createMock(classToMock, settings);

        // Assert
        // Should throw exception
    }

    @Test(expected = IllegalArgumentException.class)
    public void createMock_invalidMockedType_throwIllegalArgumentException() {
        // Arrange
        Class<?> classToMock = String.class;
        Object spiedInstance = 5;
        MockSettingsImpl settings = mock(MockSettingsImpl.class);
        when(settings.getSpiedInstance()).thenReturn(spiedInstance);

        // Act
        mockUtil.createMock(classToMock, settings);

        // Assert
        // Should throw exception
    }

    @Test
    public void resetMock_mockMockHandlerReset() {
        // Arrange
        Object mock = mock(Object.class);
        MockHandlerInterface mockHandler = mock(MockHandlerInterface.class);
        when(mockUtil.getMockHandler(mock)).thenReturn(mockHandler);

        // Act
        mockUtil.resetMock(mock);

        // Assert
        verify(mockHandler).reset();
    }

    @Test(expected = IllegalArgumentException.class)
    public void resetMock_nullMock_throwIllegalArgumentException() {
        // Arrange
        Object mock = null;

        // Act
        mockUtil.resetMock(mock);

        // Assert
        // Should throw exception
    }

    @Test
    public void getMockHandler_validMock_returnMockHandler() {
        // Arrange
        Object mock = mock(Object.class);
        MockHandlerInterface expectedMockHandler = mock(MockHandlerInterface.class);
        when(mockUtil.isMockitoMock(mock)).thenReturn(true);
        when(mockUtil.getInterceptor(mock)).thenReturn(expectedMockHandler);

        // Act
        MockHandlerInterface actualMockHandler = mockUtil.getMockHandler(mock);

        // Assert
        assertNotNull(actualMockHandler);
        assertEquals(expectedMockHandler, actualMockHandler);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getMockHandler_nullMock_throwIllegalArgumentException() {
        // Arrange
        Object mock = null;

        // Act
        mockUtil.getMockHandler(mock);

        // Assert
        // Should throw exception
    }

    @Test(expected = IllegalArgumentException.class)
    public void getMockHandler_nonMockitoMock_throwIllegalArgumentException() {
        // Arrange
        Object mock = mock(Object.class);
        when(mockUtil.isMockitoMock(mock)).thenReturn(false);

        // Act
        mockUtil.getMockHandler(mock);

        // Assert
        // Should throw exception
    }

    @Test
    public void isMock_validMock_returnTrue() {
        // Arrange
        Object mock = mock(Object.class);
        when(mockUtil.isMockitoMock(mock)).thenReturn(true);

        // Act
        boolean result = mockUtil.isMock(mock);

        // Assert
        assertTrue(result);
    }

    @Test
    public void isMock_nullMock_returnFalse() {
        // Arrange
        Object mock = null;

        // Act
        boolean result = mockUtil.isMock(mock);

        // Assert
        assertFalse(result);
    }

    @Test
    public void getMockName_validMock_returnMockName() {
        // Arrange
        Object mock = mock(Object.class);
        MockName expectedMockName = mock(MockName.class);
        MockHandlerInterface mockHandler = mock(MockHandlerInterface.class);
        when(mockUtil.getMockHandler(mock)).thenReturn(mockHandler);
        when(mockHandler.getMockSettings().getMockName()).thenReturn(expectedMockName);

        // Act
        MockName actualMockName = mockUtil.getMockName(mock);

        // Assert
        assertNotNull(actualMockName);
        assertEquals(expectedMockName, actualMockName);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getMockName_nullMock_throwIllegalArgumentException() {
        // Arrange
        Object mock = null;

        // Act
        mockUtil.getMockName(mock);

        // Assert
        // Should throw exception
    }
}
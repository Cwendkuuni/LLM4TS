import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.MockHandler;
import org.mockito.internal.util.MockHandlerInterface;
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
    public void createMock_shouldReturnMockObject() {
        // Arrange
        Class<?> classToMock = Object.class;
        MockSettingsImpl settings = new MockSettingsImpl();

        // Act
        Object mock = mockUtil.createMock(classToMock, settings);

        // Assert
        assertNotNull(mock);
        assertTrue(mockUtil.isMock(mock));
    }

    @Test(expected = NotAMockException.class)
    public void resetMock_withNullMock_shouldThrowNotAMockException() {
        // Act
        mockUtil.resetMock(null);

        // Assert
        // NotAMockException is expected to be thrown
    }

    @Test
    public void resetMock_withMock_shouldResetMock() {
        // Arrange
        Object mock = mock(Object.class);

        // Act
        mockUtil.resetMock(mock);

        // Assert
        assertTrue(mockUtil.isMock(mock));
    }

    @Test(expected = NotAMockException.class)
    public void getMockHandler_withNullMock_shouldThrowNotAMockException() {
        // Act
        mockUtil.getMockHandler(null);

        // Assert
        // NotAMockException is expected to be thrown
    }

    @Test(expected = NotAMockException.class)
    public void getMockHandler_withNonMockMock_shouldThrowNotAMockException() {
        // Arrange
        Object nonMock = new Object();

        // Act
        mockUtil.getMockHandler(nonMock);

        // Assert
        // NotAMockException is expected to be thrown
    }

    @Test
    public void getMockHandler_withMock_shouldReturnMockHandler() {
        // Arrange
        Object mock = mock(Object.class);

        // Act
        MockHandlerInterface<?> mockHandler = mockUtil.getMockHandler(mock);

        // Assert
        assertNotNull(mockHandler);
        assertTrue(mockHandler instanceof MockHandlerInterface);
    }

    @Test
    public void isMock_withNullMock_shouldReturnFalse() {
        // Arrange
        Object nullMock = null;

        // Act
        boolean isMock = mockUtil.isMock(nullMock);

        // Assert
        assertFalse(isMock);
    }

    @Test
    public void isMock_withNonMockMock_shouldReturnFalse() {
        // Arrange
        Object nonMock = new Object();

        // Act
        boolean isMock = mockUtil.isMock(nonMock);

        // Assert
        assertFalse(isMock);
    }

    @Test
    public void isMock_withMock_shouldReturnTrue() {
        // Arrange
        Object mock = mock(Object.class);

        // Act
        boolean isMock = mockUtil.isMock(mock);

        // Assert
        assertTrue(isMock);
    }

    @Test
    public void getMockName_withMock_shouldReturnMockName() {
        // Arrange
        Object mock = mock(Object.class);

        // Act
        MockName mockName = mockUtil.getMockName(mock);

        // Assert
        assertNotNull(mockName);
    }

    @Test
    public void createMock_withSerializableSettings_shouldReturnSerializableMock() {
        // Arrange
        Class<?> classToMock = Object.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.serializable(true);

        // Act
        Object mock = mockUtil.createMock(classToMock, settings);

        // Assert
        assertNotNull(mock);
        assertTrue(mockUtil.isMock(mock));
        assertTrue(mock instanceof Serializable);
    }

    @Test
    public void createMock_withSpiedInstance_shouldCopyToMock() {
        // Arrange
        Class<?> classToMock = Object.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.spiedInstance(new Object());

        // Act
        Object mock = mockUtil.createMock(classToMock, settings);

        // Assert
        assertNotNull(mock);
        assertTrue(mockUtil.isMock(mock));
        // Additional assertion to verify that spiedInstance is copied to mock
        assertEquals(settings.getSpiedInstance(), mock);
    }

}
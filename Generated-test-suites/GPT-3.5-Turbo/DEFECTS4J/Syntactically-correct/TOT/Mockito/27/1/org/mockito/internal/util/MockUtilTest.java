import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.MockUtil;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.util.reflection.LenientCopyTool;

import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockUtilTest {

    private MockUtil mockUtil;
    private MockCreationValidator mockCreationValidator;

    @Before
    public void setUp() {
        mockCreationValidator = mock(MockCreationValidator.class);
        mockUtil = new MockUtil(mockCreationValidator);
    }

    @Test
    public void testCreateMock_ValidClassAndSettings_ReturnsMockObject() {
        // Arrange
        Class<?> classToMock = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();

        // Act
        Object mock = mockUtil.createMock(classToMock, settings);

        // Assert
        assertNotNull(mock);
        assertTrue(classToMock.isInstance(mock));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateMock_InvalidClass_ThrowsException() {
        // Arrange
        Class<?> classToMock = null;
        MockSettingsImpl settings = new MockSettingsImpl();

        // Act
        mockUtil.createMock(classToMock, settings);
    }

    @Test
    public void testCreateMock_ClassWithSerializableSettings_ReturnsSerializableMockObject() {
        // Arrange
        Class<?> classToMock = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setSerializable(true);

        // Act
        Object mock = mockUtil.createMock(classToMock, settings);

        // Assert
        assertNotNull(mock);
        assertTrue(classToMock.isInstance(mock));
        assertTrue(mock instanceof Serializable);
    }

    @Test
    public void testCreateMock_ClassWithSpiedInstance_ReturnsMockObjectWithCopiedValues() {
        // Arrange
        Class<?> classToMock = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        String spiedInstance = "example";
        settings.setSpiedInstance(spiedInstance);

        // Act
        Object mock = mockUtil.createMock(classToMock, settings);

        // Assert
        assertNotNull(mock);
        assertTrue(classToMock.isInstance(mock));
        assertNotEquals(spiedInstance, mock);
        assertEquals(spiedInstance, mock);
    }

    @Test
    public void testResetMock_ValidMock_ResetsMockToDefaultAnswer() {
        // Arrange
        Object mock = mock(Factory.class);
        when(((Factory) mock).getCallback(0)).thenReturn(mock(MethodInterceptorFilter.class));

        // Act
        mockUtil.resetMock(mock);

        // Assert
        verify(((Factory) mock), times(1)).setCallback(0, new MethodInterceptorFilter(any(MockHandler.class), any(MockSettingsImpl.class)));
    }

    @Test(expected = NotAMockException.class)
    public void testResetMock_NullMock_ThrowsNotAMockException() {
        // Arrange
        Object mock = null;

        // Act
        mockUtil.resetMock(mock);
    }

    @Test(expected = NotAMockException.class)
    public void testResetMock_NonMockObject_ThrowsNotAMockException() {
        // Arrange
        Object mock = new Object();

        // Act
        mockUtil.resetMock(mock);
    }

    @Test
    public void testGetMockHandler_ValidMock_ReturnsMockHandlerInterface() {
        // Arrange
        Object mock = mock(Factory.class);
        MethodInterceptorFilter methodInterceptorFilter = mock(MethodInterceptorFilter.class);
        when(((Factory) mock).getCallback(0)).thenReturn(methodInterceptorFilter);

        // Act
        MockHandlerInterface<?> mockHandler = mockUtil.getMockHandler(mock);

        // Assert
        assertNotNull(mockHandler);
        assertEquals(methodInterceptorFilter.getHandler(), mockHandler);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_NullMock_ThrowsNotAMockException() {
        // Arrange
        Object mock = null;

        // Act
        mockUtil.getMockHandler(mock);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_NonMockObject_ThrowsNotAMockException() {
        // Arrange
        Object mock = new Object();

        // Act
        mockUtil.getMockHandler(mock);
    }

    @Test
    public void testIsMock_ValidMock_ReturnsTrue() {
        // Arrange
        Object mock = mock(Factory.class);
        when(((Factory) mock).getCallback(0)).thenReturn(mock(MethodInterceptorFilter.class));

        // Act
        boolean result = mockUtil.isMock(mock);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsMock_NullObject_ReturnsFalse() {
        // Arrange
        Object mock = null;

        // Act
        boolean result = mockUtil.isMock(mock);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsMock_NonMockObject_ReturnsFalse() {
        // Arrange
        Object mock = new Object();

        // Act
        boolean result = mockUtil.isMock(mock);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testGetMockName_ValidMock_ReturnsMockName() {
        // Arrange
        Object mock = mock(Factory.class);
        MethodInterceptorFilter methodInterceptorFilter = mock(MethodInterceptorFilter.class);
        when(((Factory) mock).getCallback(0)).thenReturn(methodInterceptorFilter);
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        when(methodInterceptorFilter.getHandler()).thenReturn(mock(MockHandlerInterface.class));
        when(methodInterceptorFilter.getHandler().getMockSettings()).thenReturn(mockSettings);
        MockName mockName = mock(MockName.class);
        when(mockSettings.getMockName()).thenReturn(mockName);

        // Act
        MockName result = mockUtil.getMockName(mock);

        // Assert
        assertEquals(mockName, result);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockName_NullMock_ThrowsNotAMockException() {
        // Arrange
        Object mock = null;

        // Act
        mockUtil.getMockName(mock);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockName_NonMockObject_ThrowsNotAMockException() {
        // Arrange
        Object mock = new Object();

        // Act
        mockUtil.getMockName(mock);
    }
}
import org.junit.Test;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.cglib.proxy.Factory;

import java.io.Serializable;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockUtilTest {

    MockUtil mockUtil = new MockUtil();

    @Test
    public void testCreateMock_ValidClass_ReturnsMock() {
        // Arrange
        MockCreationValidator creationValidatorMock = mock(MockCreationValidator.class);
        MockUtil mockUtil = new MockUtil(creationValidatorMock);
        Class<TestClass> classToMock = TestClass.class;
        MockSettingsImpl settings = mock(MockSettingsImpl.class);
        settings.setExtraInterfaces(null);
        when(settings.isSerializable()).thenReturn(false);
        when(settings.getSpiedInstance()).thenReturn(null);

        // Act
        TestClass mock = mockUtil.createMock(classToMock, settings);

        // Assert
        assertNotNull(mock);
        assertTrue(mock instanceof TestClass);
        verify(creationValidatorMock).validateType(classToMock);
        verify(creationValidatorMock).validateExtraInterfaces(classToMock, null);
        verify(creationValidatorMock).validateMockedType(classToMock, null);
    }

    @Test
    public void testResetMock_ValidMock_MockResetSuccessfully() {
        // Arrange
        TestClass mock = mock(TestClass.class);
        MockHandlerInterface<Test> oldMockHandlerMock = mock(MockHandlerInterface.class);
        MockHandler<Test> newMockHandlerMock = mock(MockHandler.class);
        MethodInterceptorFilter newFilterMock = mock(MethodInterceptorFilter.class);
        Factory factoryMock = mock(Factory.class);

        when(mockUtil.getMockHandler(mock)).thenReturn(oldMockHandlerMock);
        when(oldMockHandlerMock.getHandler()).thenReturn(factoryMock);
        when(newMockHandlerMock.getDefaultAnswer()).thenReturn(org.mockito.Mockito.RETURNS_DEFAULTS);
        when(newFilterMock.getNewMockHandler()).thenReturn(newMockHandlerMock);
        when(factoryMock.getCallback(0)).thenReturn(newFilterMock);

        // Act
        mockUtil.resetMock(mock);

        // Assert
        verify(factoryMock).setCallback(0, newFilterMock);
    }

    @Test(expected = NotAMockException.class)
    public void testResetMock_NullMock_ThrowsNotAMockException() {
        // Arrange
        TestClass nullMock = null;

        // Act
        mockUtil.resetMock(nullMock);

        // Assert
        // Exception expected
    }

    @Test(expected = NotAMockException.class)
    public void testResetMock_NonMockMock_ThrowsNotAMockException() {
        // Arrange
        TestClass nonMockMock = new TestClass();

        // Act
        mockUtil.resetMock(nonMockMock);

        // Assert
        // Exception expected
    }

    @Test
    public void testGetMockHandler_ValidMock_ReturnsMockHandler() {
        // Arrange
        TestClass mock = mock(TestClass.class);
        MockHandlerInterface<Test> mockHandlerMock = mock(MockHandlerInterface.class);

        when(mockUtil.isMockitoMock(mock)).thenReturn(true);
        when(mockUtil.getInterceptor(mock)).thenReturn(mockHandlerMock);

        // Act
        MockHandlerInterface<Test> result = mockUtil.getMockHandler(mock);

        // Assert
        assertEquals(mockHandlerMock, result);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_NullMock_ThrowsNotAMockException() {
        // Arrange
        TestClass nullMock = null;

        // Act
        mockUtil.getMockHandler(nullMock);

        // Assert
        // Exception expected
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_NonMockMock_ThrowsNotAMockException() {
        // Arrange
        TestClass nonMockMock = new TestClass();

        // Act
        mockUtil.getMockHandler(nonMockMock);

        // Assert
        // Exception expected
    }

    @Test
    public void testIsMock_ValidMock_ReturnsTrue() {
        // Arrange
        TestClass mock = mock(TestClass.class);

        when(mockUtil.isMockitoMock(mock)).thenReturn(true);

        // Act
        boolean result = mockUtil.isMock(mock);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsMock_NullMock_ReturnsFalse() {
        // Arrange
        TestClass nullMock = null;

        // Act
        boolean result = mockUtil.isMock(nullMock);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsMock_NonMockMock_ReturnsFalse() {
        // Arrange
        TestClass nonMockMock = new TestClass();

        when(mockUtil.isMockitoMock(nonMockMock)).thenReturn(false);

        // Act
        boolean result = mockUtil.isMock(nonMockMock);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testGetMockName_ValidMock_ReturnsMockName() {
        // Arrange
        TestClass mock = mock(TestClass.class);
        MockHandlerInterface<Test> mockHandlerMock = mock(MockHandlerInterface.class);
        MockSettingsImpl mockSettingsMock = mock(MockSettingsImpl.class);
        MockName mockNameMock = mock(MockName.class);

        when(mockUtil.getMockHandler(mock)).thenReturn(mockHandlerMock);
        when(mockHandlerMock.getMockSettings()).thenReturn(mockSettingsMock);
        when(mockSettingsMock.getMockName()).thenReturn(mockNameMock);

        // Act
        MockName result = mockUtil.getMockName(mock);

        // Assert
        assertEquals(mockNameMock, result);
    }

    private class TestClass {}

}
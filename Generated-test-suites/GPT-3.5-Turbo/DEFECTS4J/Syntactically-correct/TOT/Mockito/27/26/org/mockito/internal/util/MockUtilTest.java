import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.MockUtil;
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
    public void testCreateMock_validClass_validSettings_mockCreated() {
        // Arrange
        Class<?> classToMock = Serializable.class;
        MockSettingsImpl settings = new MockSettingsImpl();

        // Act
        Object result = mockUtil.createMock(classToMock, settings);

        // Assert
        assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateMock_nullClass_throwIllegalArgumentException() {
        // Arrange
        Class<?> classToMock = null;
        MockSettingsImpl settings = new MockSettingsImpl();

        // Act
        mockUtil.createMock(classToMock, settings);
    }

    @Test
    public void testCreateMock_extraInterfacesExist_validSettings_mockCreated() {
        // Arrange
        Class<?> classToMock = Serializable.class;
        Class<?>[] extraInterfaces = {Cloneable.class};
        MockSettingsImpl settings = new MockSettingsImpl().extraInterfaces(extraInterfaces);

        // Act
        Object result = mockUtil.createMock(classToMock, settings);

        // Assert
        assertNotNull(result);
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMock_invalidExtraInterface_throwNotAMockException() {
        // Arrange
        Class<?> classToMock = Serializable.class;
        Class<?>[] extraInterfaces = {String.class};
        MockSettingsImpl settings = new MockSettingsImpl().extraInterfaces(extraInterfaces);

        // Act
        mockUtil.createMock(classToMock, settings);
    }

    @Test
    public void testCreateMock_spiedInstanceExists_validSettings_mockCreated() {
        // Arrange
        Class<?> classToMock = Serializable.class;
        Object spiedInstance = mock(Serializable.class);
        MockSettingsImpl settings = new MockSettingsImpl().spiedInstance(spiedInstance);

        // Act
        Object result = mockUtil.createMock(classToMock, settings);

        // Assert
        assertNotNull(result);
        assertNotSame(spiedInstance, result);
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMock_invalidSpiedInstance_throwNotAMockException() {
        // Arrange
        Class<?> classToMock = Serializable.class;
        Object spiedInstance = new Object();
        MockSettingsImpl settings = new MockSettingsImpl().spiedInstance(spiedInstance);

        // Act
        mockUtil.createMock(classToMock, settings);
    }

    @Test
    public void testResetMock_validMock_mockReset() {
        // Arrange
        Serializable mock = mock(Serializable.class);
        MethodInterceptorFilter filter = new MethodInterceptorFilter(mock, null);

        // Act
        mockUtil.resetMock(mock);

        // Assert
        assertNotEquals(filter, mockUtil.getInterceptor(mock));
    }

    @Test(expected = NotAMockException.class)
    public void testResetMock_nullMock_throwNotAMockException() {
        // Arrange
        Serializable mock = null;

        // Act
        mockUtil.resetMock(mock);
    }

    @Test
    public void testGetMockHandler_validMock_validMockHandler() {
        // Arrange
        Serializable mock = mock(Serializable.class);

        // Act
        MockHandlerInterface result = mockUtil.getMockHandler(mock);

        // Assert
        assertNotNull(result);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_nullMock_throwNotAMockException() {
        // Arrange
        Serializable mock = null;

        // Act
        mockUtil.getMockHandler(mock);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_nonMockitoMock_throwNotAMockException() {
        // Arrange
        Object mock = new Object();

        // Act
        mockUtil.getMockHandler(mock);
    }

    @Test
    public void testIsMock_validMock_true() {
        // Arrange
        Serializable mock = mock(Serializable.class);

        // Act
        boolean result = mockUtil.isMock(mock);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsMock_nonMock_false() {
        // Arrange
        Object nonMock = new Object();

        // Act
        boolean result = mockUtil.isMock(nonMock);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testGetMockName_validMock_validMockName() {
        // Arrange
        Serializable mock = mock(Serializable.class);

        // Act
        MockName result = mockUtil.getMockName(mock);

        // Assert
        assertNotNull(result);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockName_nullMock_throwNotAMockException() {
        // Arrange
        Serializable mock = null;

        // Act
        mockUtil.getMockName(mock);
    }

}
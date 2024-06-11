import org.junit.Test;
import org.mockito.*;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockHandler;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.LenientCopyTool;

import static org.mockito.Mockito.*;

public class MockUtilTest {

    @Test
    public void testCreateMock_ValidClass_ValidSettings_ReturnsMock() {
        // Arrange
        MockUtil mockUtil = new MockUtil();
        Class<?> classToMock = MyClass.class;
        MockSettingsImpl settings = withSettings().defaultAnswer(RETURNS_DEFAULTS);

        // Act
        MyClass mock = mockUtil.createMock(classToMock, settings);

        // Assert
        assertNotNull(mock);
    }

    @Test
    public void testCreateMock_InvalidClass_ThrowsException() {
        // Arrange
        MockUtil mockUtil = new MockUtil();
        Class<?> invalidClassToMock = null;
        MockSettingsImpl settings = withSettings().defaultAnswer(RETURNS_DEFAULTS);

        // Act & Assert
        assertThrows(NotAMockException.class, () -> mockUtil.createMock(invalidClassToMock, settings));
    }

    @Test
    public void testResetMock_ValidMock_ResetsMock() {
        // Arrange
        MockUtil mockUtil = new MockUtil();
        MyClass mock = mock(MyClass.class);

        // Act
        mockUtil.resetMock(mock);

        // Assert
        verify(mock).setCallback(eq(0), any(MethodInterceptorFilter.class));
    }

    @Test
    public void testResetMock_NullMock_ThrowsException() {
        // Arrange
        MockUtil mockUtil = new MockUtil();
        MyClass nullMock = null;

        // Act & Assert
        assertThrows(NotAMockException.class, () -> mockUtil.resetMock(nullMock));
    }

    @Test
    public void testGetMockHandler_ValidMock_ReturnsMockHandler() {
        // Arrange
        MockUtil mockUtil = new MockUtil();
        MyClass mock = mock(MyClass.class);
        MockHandlerInterface<MyClass> expectedMockHandler = ((MockitoMock) mock).getMockHandler();

        // Act
        MockHandlerInterface<MyClass> mockHandler = mockUtil.getMockHandler(mock);

        // Assert
        assertEquals(expectedMockHandler, mockHandler);
    }

    @Test
    public void testGetMockHandler_NullMock_ThrowsException() {
        // Arrange
        MockUtil mockUtil = new MockUtil();
        MyClass nullMock = null;

        // Act & Assert
        assertThrows(NotAMockException.class, () -> mockUtil.getMockHandler(nullMock));
    }

    @Test
    public void testIsMock_ValidMock_ReturnsTrue() {
        // Arrange
        MockUtil mockUtil = new MockUtil();
        MyClass mock = mock(MyClass.class);

        // Act
        boolean isMock = mockUtil.isMock(mock);

        // Assert
        assertTrue(isMock);
    }

    @Test
    public void testIsMock_NullMock_ReturnsFalse() {
        // Arrange
        MockUtil mockUtil = new MockUtil();
        MyClass nullMock = null;

        // Act
        boolean isMock = mockUtil.isMock(nullMock);

        // Assert
        assertFalse(isMock);
    }

    @Test
    public void testIsMock_EnhancedNonMock_ReturnsFalse() {
        // Arrange
        MockUtil mockUtil = new MockUtil();
        MyClass enhancedNonMock = new MyClass();

        // Act
        boolean isMock = mockUtil.isMock(enhancedNonMock);

        // Assert
        assertFalse(isMock);
    }

    @Test
    public void testGetMockName_ValidMock_ReturnsMockName() {
        // Arrange
        MockUtil mockUtil = new MockUtil();
        MyClass mock = mock(MyClass.class);
        String expectedMockName = ((MockitoMock) mock).getMockHandler().getMockSettings().getMockName().toString();

        // Act
        MockName mockName = mockUtil.getMockName(mock);

        // Assert
        assertEquals(expectedMockName, mockName.toString());
    }

    @Test
    public void testGetMockName_NullMock_ThrowsException() {
        // Arrange
        MockUtil mockUtil = new MockUtil();
        MyClass nullMock = null;

        // Act & Assert
        assertThrows(NotAMockException.class, () -> mockUtil.getMockName(nullMock));
    }

    private static class MyClass {
        // class details
    }

}
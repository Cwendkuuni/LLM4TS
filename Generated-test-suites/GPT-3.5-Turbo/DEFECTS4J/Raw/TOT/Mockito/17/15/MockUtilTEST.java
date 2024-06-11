import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class MockUtilTest {

    @Mock
    CreationValidator creationValidatorMock;
    MockUtil mockUtil = new MockUtil(creationValidatorMock);

    @Test
    public void testCreateMock_WithValidClassAndSettings_ReturnsMock() {
        // Arrange
        ClassToMock classToMock = new ClassToMock();
        MockSettingsImpl settings = new MockSettingsImpl();

        // Act
        Object result = mockUtil.createMock(classToMock.getClass(), settings);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof ClassToMock);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateMock_WithInvalidClass_ThrowsException() {
        // Arrange
        ClassToMock classToMock = null;
        MockSettingsImpl settings = new MockSettingsImpl();

        // Act
        mockUtil.createMock(classToMock.getClass(), settings);
    }

    @Test
    public void testResetMock_WithValidMock_ResetsMock() {
        // Arrange
        ClassToMock mock = Mockito.mock(ClassToMock.class);

        // Act
        mockUtil.resetMock(mock);

        // Assert
        verify(mock, times(1)).setCallback(0, any(MethodInterceptorFilter.class));
    }

    @Test(expected = NotAMockException.class)
    public void testResetMock_WithNullMock_ThrowsException() {
        // Arrange
        ClassToMock mock = null;

        // Act
        mockUtil.resetMock(mock);
    }

    @Test(expected = NotAMockException.class)
    public void testResetMock_WithNonMockObject_ThrowsException() {
        // Arrange
        ClassToMock classToMock = new ClassToMock();
        
        // Act
        mockUtil.resetMock(classToMock);
    }

    @Test
    public void testGetMockHandler_WithValidMock_ReturnsMockHandler() {
        // Arrange
        ClassToMock mock = Mockito.mock(ClassToMock.class);

        // Act
        MockHandlerInterface<ClassToMock> result = mockUtil.getMockHandler(mock);

        // Assert
        assertNotNull(result);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_WithNullMock_ThrowsException() {
        // Arrange
        ClassToMock mock = null;

        // Act
        mockUtil.getMockHandler(mock);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_WithNonMockObject_ThrowsException() {
        // Arrange
        ClassToMock classToMock = new ClassToMock();
        
        // Act
        mockUtil.getMockHandler(classToMock);
    }

    @Test
    public void testIsMock_WithValidMock_ReturnsTrue() {
        // Arrange
        ClassToMock mock = Mockito.mock(ClassToMock.class);

        // Act
        boolean result = mockUtil.isMock(mock);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsMock_WithNonMockObject_ReturnsFalse() {
        // Arrange
        ClassToMock classToMock = new ClassToMock();

        // Act
        boolean result = mockUtil.isMock(classToMock);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testGetMockName_WithValidMock_ReturnsMockName() {
        // Arrange
        ClassToMock mock = Mockito.mock(ClassToMock.class);

        // Act
        MockName result = mockUtil.getMockName(mock);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testGetMockName_WithNullMock_ReturnsNull() {
        // Arrange
        ClassToMock mock = null;

        // Act
        MockName result = mockUtil.getMockName(mock);

        // Assert
        assertNull(result);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockName_WithNonMockObject_ThrowsException() {
        // Arrange
        ClassToMock classToMock = new ClassToMock();

        // Act
        mockUtil.getMockName(classToMock);
    }

    private class ClassToMock {
        // Test class to be mocked
    }
}
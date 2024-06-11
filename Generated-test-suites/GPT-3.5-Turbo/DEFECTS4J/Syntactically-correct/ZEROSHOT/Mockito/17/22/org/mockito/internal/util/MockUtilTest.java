import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.reflection.LenientCopyTool;

public class MockUtilTest {

    private MockUtil mockUtil;
    
    @Before
    public void setup() {
        mockUtil = new MockUtil();
    }

    @Test
    public void testCreateMock_validClass_validSettings() {
        // Arrange
        Class<?> classToMock = Object.class;
        MockSettingsImpl settings = mock(MockSettingsImpl.class);

        // Act
        Object mock = mockUtil.createMock(classToMock, settings);

        // Assert
        assertNotNull(mock);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateMock_invalidClass_validSettings() {
        // Arrange
        Class<?> classToMock = null;
        MockSettingsImpl settings = mock(MockSettingsImpl.class);

        // Act
        mockUtil.createMock(classToMock, settings);

        // Assert (Exception expected)
    }

    @Test
    public void testResetMock_validMock() {
        // Arrange
        Object mock = mock(Object.class);

        // Act
        mockUtil.resetMock(mock);

        // Assert
        // No assertions needed as no exception is thrown
    }

    @Test(expected = NotAMockException.class)
    public void testResetMock_nullMock() {
        // Arrange
        Object mock = null;

        // Act
        mockUtil.resetMock(mock);

        // Assert (Exception expected)
    }

    @Test(expected = NotAMockException.class)
    public void testResetMock_nonMockObject() {
        // Arrange
        Object mock = new Object();

        // Act
        mockUtil.resetMock(mock);

        // Assert (Exception expected)
    }

    @Test
    public void testGetMockHandler_validMock() {
        // Arrange
        Object mock = mock(Object.class);

        // Act
        MockHandlerInterface<?> mockHandler = mockUtil.getMockHandler(mock);

        // Assert
        assertNotNull(mockHandler);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_nullMock() {
        // Arrange
        Object mock = null;

        // Act
        mockUtil.getMockHandler(mock);

        // Assert (Exception expected)
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_nonMockObject() {
        // Arrange
        Object mock = new Object();

        // Act
        mockUtil.getMockHandler(mock);

        // Assert (Exception expected)
    }

    @Test
    public void testIsMock_validMock() {
        // Arrange
        Object mock = mock(Object.class);

        // Act
        boolean isMock = mockUtil.isMock(mock);

        // Assert
        assertTrue(isMock);
    }

    @Test
    public void testIsMock_nonMockObject() {
        // Arrange
        Object mock = new Object();

        // Act
        boolean isMock = mockUtil.isMock(mock);

        // Assert
        assertFalse(isMock);
    }

    @Test
    public void testGetMockName_validMock() {
        // Arrange
        Object mock = mock(Object.class);

        // Act
        MockName mockName = mockUtil.getMockName(mock);

        // Assert
        assertNotNull(mockName);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockName_nullMock() {
        // Arrange
        Object mock = null;

        // Act
        mockUtil.getMockName(mock);

        // Assert (Exception expected)
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockName_nonMockObject() {
        // Arrange
        Object mock = new Object();

        // Act
        mockUtil.getMockName(mock);

        // Assert (Exception expected)
    }
}
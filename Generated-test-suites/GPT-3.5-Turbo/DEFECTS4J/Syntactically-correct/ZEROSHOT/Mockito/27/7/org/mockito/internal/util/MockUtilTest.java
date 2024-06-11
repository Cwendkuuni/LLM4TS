import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.InvocationNotifierHandler;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
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
    public void testCreateMock_ValidClass_ValidSettings() {
        // Arrange
        Class<String> classToMock = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();

        // Act
        String mock = mockUtil.createMock(classToMock, settings);

        // Assert
        assertNotNull(mock);
        assertTrue(mock instanceof String);
        assertTrue(isMockitoMock(mock));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateMock_InvalidClass_ValidSettings() {
        // Arrange
        Class<Object> classToMock = Object.class;
        MockSettingsImpl settings = new MockSettingsImpl();

        // Act
        Object mock = mockUtil.createMock(classToMock, settings);

        // Assert
        fail("Should throw IllegalArgumentException");
    }

    @Test
    public void testResetMock_ValidMock() {
        // Arrange
        String mock = mock(String.class);

        // Act
        mockUtil.resetMock(mock);

        // Assert
        assertNotNull(mock);
    }

    @Test(expected = NotAMockException.class)
    public void testResetMock_NullMock() {
        // Arrange
        String mock = null;

        // Act
        mockUtil.resetMock(mock);

        // Assert
        fail("Should throw NotAMockException");
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_NullMock() {
        // Arrange
        String mock = null;

        // Act
        mockUtil.getMockHandler(mock);

        // Assert
        fail("Should throw NotAMockException");
    }
    
    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_NonMockitoMock() {
        // Arrange
        Object mock = new Object();

        // Act
        mockUtil.getMockHandler(mock);

        // Assert
        fail("Should throw NotAMockException");
    }
    
    @Test
    public void testGetMockHandler_ValidMock() {
        // Arrange
        String mock = mock(String.class);

        // Act
        MockHandlerInterface<String> mockHandler = mockUtil.getMockHandler(mock);

        // Assert
        assertNotNull(mockHandler);
    }

    @Test
    public void testIsMock_ValidMock() {
        // Arrange
        String mock = mock(String.class);

        // Act
        boolean isMock = mockUtil.isMock(mock);

        // Assert
        assertTrue(isMock);
    }

    @Test
    public void testIsMock_NullMock() {
        // Arrange
        String mock = null;

        // Act
        boolean isMock = mockUtil.isMock(mock);

        // Assert
        assertFalse(isMock);
    }

    @Test
    public void testGetMockName_ValidMock() {
        // Arrange
        String mock = mock(String.class);

        // Act
        MockName mockName = mockUtil.getMockName(mock);

        // Assert
        assertNotNull(mockName);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockName_NullMock() {
        // Arrange
        String mock = null;

        // Act
        mockUtil.getMockName(mock);

        // Assert
        fail("Should throw NotAMockException");
    }
}
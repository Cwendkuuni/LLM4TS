
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.MockHandlerInterface;
import org.mockito.internal.util.MockName;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.LenientCopyTool;

import java.io.Serializable;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockUtilTest {

    private MockUtil mockUtil;

    @Before
    public void setup() {
        MockCreationValidator creationValidator = new MockCreationValidator();
        mockUtil = new MockUtil(creationValidator);
    }

    @Test
    public void testCreateMock_typicalCase() {
        // Arrange
        Class<Foo> classToMock = Foo.class;
        MockSettingsImpl settings = new MockSettingsImpl();

        // Act
        Foo mock = mockUtil.createMock(classToMock, settings);

        // Assert
        assertNotNull(mock);
        assertTrue(mock instanceof Foo);
    }

    @Test
    public void testCreateMock_classToMockIsNull() {
        // Arrange
        Class<Foo> classToMock = null;
        MockSettingsImpl settings = new MockSettingsImpl();

        try {
            // Act
            Foo mock = mockUtil.createMock(classToMock, settings);
            fail("Expected exception was not thrown");
        } catch (IllegalArgumentException e) {
            // Assert
            assertEquals("Class to mock cannot be null", e.getMessage());
        }
    }

    @Test
    public void testCreateMock_extraInterfacesInvalid() {
        // Arrange
        Class<Foo> classToMock = Foo.class;
        Class<?>[] extraInterfaces = {Serializable.class, Cloneable.class};
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setExtraInterfaces(extraInterfaces);

        try {
            // Act
            Foo mock = mockUtil.createMock(classToMock, settings);
            fail("Expected exception was not thrown");
        } catch (IllegalArgumentException e) {
            // Assert
            assertEquals("Invalid extra interfaces specified", e.getMessage());
        }
    }

    @Test
    public void testCreateMock_spiedInstanceInvalid() {
        // Arrange
        Class<Foo> classToMock = Foo.class;
        Foo spiedInstance = new Foo();
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setSpiedInstance(spiedInstance);

        try {
            // Act
            Foo mock = mockUtil.createMock(classToMock, settings);
            fail("Expected exception was not thrown");
        } catch (IllegalArgumentException e) {
            // Assert
            assertEquals("Invalid spied instance specified", e.getMessage());
        }
    }

    @Test
    public void testResetMock_typicalCase() {
        // Arrange
        Foo mock = mock(Foo.class);

        // Act
        mockUtil.resetMock(mock);

        // Assert
        // No exception is thrown
    }

    @Test
    public void testResetMock_notAMock() {
        // Arrange
        Foo notAMock = new Foo();

        try {
            // Act
            mockUtil.resetMock(notAMock);
            fail("Expected exception was not thrown");
        } catch (NotAMockException e) {
            // Assert
            assertEquals("Argument should be a mock, but is: Foo", e.getMessage());
        }
    }

    @Test
    public void testGetMockHandler_typicalCase() {
        // Arrange
        Foo mock = mock(Foo.class);

        // Act
        MockHandlerInterface<Foo> handler = mockUtil.getMockHandler(mock);

        // Assert
        assertNotNull(handler);
    }

    @Test
    public void testGetMockHandler_nullMock() {
        // Arrange
        Foo mock = null;

        try {
            // Act
            MockHandlerInterface<Foo> handler = mockUtil.getMockHandler(mock);
            fail("Expected exception was not thrown");
        } catch (NotAMockException e) {
            // Assert
            assertEquals("Argument should be a mock, but is null!", e.getMessage());
        }
    }

    @Test
    public void testGetMockHandler_notAMock() {
        // Arrange
        Foo notAMock = new Foo();

        try {
            // Act
            MockHandlerInterface<Foo> handler = mockUtil.getMockHandler(notAMock);
            fail("Expected exception was not thrown");
        } catch (NotAMockException e) {
            // Assert
            assertEquals("Argument should be a mock, but is: Foo", e.getMessage());
        }
    }

    @Test
    public void testIsMock_typicalCase() {
        // Arrange
        Foo mock = mock(Foo.class);

        // Act
        boolean isMock = mockUtil.isMock(mock);

        // Assert
        assertTrue(isMock);
    }

    @Test
    public void testIsMock_notAMock() {
        // Arrange
        Foo notAMock = new Foo();

        // Act
        boolean isMock = mockUtil.isMock(notAMock);

        // Assert
        assertFalse(isMock);
    }

    @Test
    public void testGetMockName_typicalCase() {
        // Arrange
        Foo mock = mock(Foo.class);

        // Act
        MockName mockName = mockUtil.getMockName(mock);

        // Assert
        assertNotNull(mockName);
    }

    @Test
    public void testGetMockName_notAMock() {
        // Arrange
        Foo notAMock = new Foo();

        try {
            // Act
            MockName mockName = mockUtil.getMockName(notAMock);
            fail("Expected exception was not thrown");
        } catch (NotAMockException e) {
            // Assert
            assertEquals("Argument should be a mock, but is: Foo", e.getMessage());
        }
    }

    // Helper class for testing
    static class Foo {
        // Empty class
    }

}

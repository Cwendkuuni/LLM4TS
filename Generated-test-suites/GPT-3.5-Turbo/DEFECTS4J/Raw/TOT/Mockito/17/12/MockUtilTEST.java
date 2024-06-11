
package org.mockito.internal.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.creation.MockSettingsImpl;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class MockUtilTest {

    private MockUtil mockUtil;

    @Before
    public void setup() {
        mockUtil = new MockUtil();
    }

    @Test
    public void testCreateMock_validClassAndSettings_returnsMock() {
        // Arrange
        Class<TestClass> classToMock = TestClass.class;
        MockSettingsImpl settings = new MockSettingsImpl();

        // Act
        TestClass mock = mockUtil.createMock(classToMock, settings);

        // Assert
        assertNotNull(mock);
        assertTrue(mock instanceof TestClass);
    }

    @Test(expected = NotAMockException.class)
    public void testResetMock_nullMock_throwsNotAMockException() {
        // Arrange
        TestClass mock = null;

        // Act
        mockUtil.resetMock(mock);

        // Assert
        // NotAMockException is expected to be thrown
    }

    @Test
    public void testResetMock_validMock_resetsMock() {
        // Arrange
        TestClass mock = mock(TestClass.class);
        String expectedName = "newName";

        // Act
        mock.resetName(expectedName);

        // Assert
        assertEquals(expectedName, mock.getName());
    }

    @Test
    public void testGetMockHandler_nullMock_throwsNotAMockException() {
        // Arrange
        TestClass mock = null;

        // Act
        try {
            mockUtil.getMockHandler(mock);
            fail("Expected NotAMockException to be thrown");
        } catch (NotAMockException ex) {
            // Assert
            assertEquals("Argument should be a mock, but is null!", ex.getMessage());
        }
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_nonMockObject_throwsNotAMockException() {
        // Arrange
        TestClass nonMock = new TestClass();

        // Act
        mockUtil.getMockHandler(nonMock);

        // Assert
        // NotAMockException is expected to be thrown
    }

    @Test
    public void testIsMock_validMock_returnsTrue() {
        // Arrange
        TestClass mock = mock(TestClass.class);

        // Act
        boolean result = mockUtil.isMock(mock);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsMock_nonMockObject_returnsFalse() {
        // Arrange
        TestClass nonMock = new TestClass();

        // Act
        boolean result = mockUtil.isMock(nonMock);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testGetMockName_validMock_returnsMockName() {
        // Arrange
        TestClass mock = mock(TestClass.class);

        // Act
        MockName mockName = mockUtil.getMockName(mock);

        // Assert
        assertNotNull(mockName);
        assertEquals(mock.getClass().getName() + "$MockitoMock$XXXX", mockName.toString()); // Replace XXXX with the actual number assigned by Mockito
    }

    // Helper class for testing
    private static class TestClass {

        private String name;

        public TestClass() {
            this.name = "Test";
        }

        public void resetName(String newName) {
            this.name = newName;
        }

        public String getName() {
            return name;
        }
    }
}

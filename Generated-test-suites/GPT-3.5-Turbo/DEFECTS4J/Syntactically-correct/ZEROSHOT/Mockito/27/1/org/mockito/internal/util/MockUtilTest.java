import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.misusing.NotAMockException;

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
    public void testCreateMock() {
        // Arrange
        Class<MyClass> classToMock = MyClass.class;
        MockSettingsImpl settings = new MockSettingsImpl();

        // Act
        MyClass mock = mockUtil.createMock(classToMock, settings);

        // Assert
        assertNotNull(mock);
        assertTrue(mock instanceof MyClass);
    }

    @Test
    public void testResetMock() {
        // Arrange
        MyClass mock = mock(MyClass.class);
        MockHandlerInterface<MyClass> oldMockHandler = mockUtil.getMockHandler(mock);

        // Act
        mockUtil.resetMock(mock);
        MockHandlerInterface<MyClass> newMockHandler = mockUtil.getMockHandler(mock);

        // Assert
        assertNotNull(newMockHandler);
        assertNotSame(oldMockHandler, newMockHandler);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNull() {
        // Arrange
        Object mock = null;

        // Act
        mockUtil.getMockHandler(mock);

        // Assert (Exception expected)
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNonMockitoMock() {
        // Arrange
        Object mock = new Object();

        // Act
        mockUtil.getMockHandler(mock);

        // Assert (Exception expected)
    }

    @Test
    public void testIsMockWithMockitoMock() {
        // Arrange
        Object mock = mock(Object.class);

        // Act
        boolean isMock = mockUtil.isMock(mock);

        // Assert
        assertTrue(isMock);
    }

    @Test
    public void testIsMockWithNonMockitoMock() {
        // Arrange
        Object mock = new Object();

        // Act
        boolean isMock = mockUtil.isMock(mock);

        // Assert
        assertFalse(isMock);
    }

    @Test
    public void testGetMockName() {
        // Arrange
        Object mock = mock(Object.class);
        MockName expectedMockName = mockUtil.getMockHandler(mock).getMockSettings().getMockName();

        // Act
        MockName mockName = mockUtil.getMockName(mock);

        // Assert
        assertNotNull(mockName);
        assertEquals(expectedMockName, mockName);
    }
}
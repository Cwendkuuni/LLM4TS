import org.junit.Before;
import org.junit.Test;
import org.mockito.MockSettings;
import org.mockito.internal.MockHandler;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.CreationValidator;
import org.mockito.internal.util.MockName;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.cglib.proxy.Factory;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class MockUtilTest {

    private MockUtil mockUtil;
    private CreationValidator creationValidator;

    @Before
    public void setUp() {
        creationValidator = new CreationValidator();
        mockUtil = new MockUtil(creationValidator);
    }

    @Test
    public void testCreateMock_ValidClass_ReturnsMockObject() {
        // Arrange
        Class<TestClass> classToMock = TestClass.class;
        MockSettingsImpl settings = new MockSettingsImpl();

        // Act
        TestClass mock = mockUtil.createMock(classToMock, settings);

        // Assert
        assertNotNull(mock);
        assertTrue(mock instanceof TestClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateMock_NullClass_ThrowsException() {
        // Arrange
        Class<TestNullClass> classToMock = null;
        MockSettingsImpl settings = new MockSettingsImpl();

        // Act
        mockUtil.createMock(classToMock, settings);

        // Assert
        // Exception expected
    }

    @Test
    public void testResetMock_ValidMock_CallsSetCallback() {
        // Arrange
        TestClass mock = mock(TestClass.class);
        MockHandler<Testable> oldMockHandler = mockUtil.getMockHandler(mock);
        MockHandler<Testable> newMockHandler = new MockHandler<>(oldMockHandler);
        MethodInterceptorFilter newFilter = new MethodInterceptorFilter(newMockHandler,
                (MockSettingsImpl) withSettings().defaultAnswer(RETURNS_DEFAULTS));

        // Act
        mockUtil.resetMock(mock);

        // Assert
        verify((Factory) mock).setCallback(0, newFilter);
    }

    @Test(expected = NotAMockException.class)
    public void testResetMock_NullMock_ThrowsException() {
        // Arrange
        TestClass mock = null;

        // Act
        mockUtil.resetMock(mock);

        // Assert
        // Exception expected
    }

    @Test
    public void testGetMockHandler_ValidMock_ReturnsMockHandler() {
        // Arrange
        TestClass mock = mock(TestClass.class);

        // Act
        MockHandler<Testable> mockHandler = mockUtil.getMockHandler(mock);

        // Assert
        assertNotNull(mockHandler);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_NullMock_ThrowsException() {
        // Arrange
        TestClass mock = null;

        // Act
        mockUtil.getMockHandler(mock);

        // Assert
        // Exception expected
    }

    @Test
    public void testIsMock_ValidMock_ReturnsTrue() {
        // Arrange
        TestClass mock = mock(TestClass.class);

        // Act
        boolean isMock = mockUtil.isMock(mock);

        // Assert
        assertTrue(isMock);
    }

    @Test
    public void testIsMock_NonMock_ReturnsFalse() {
        // Arrange
        TestClass nonMock = new TestClass();

        // Act
        boolean isMock = mockUtil.isMock(nonMock);

        // Assert
        assertFalse(isMock);
    }

    @Test
    public void testMockName_ValidMock_ReturnsMockName() {
        // Arrange
        TestClass mock = mock(TestClass.class);
        MockName expectedMockName = mockUtil.getMockHandler(mock).getMockSettings().getMockName();

        // Act
        MockName actualMockName = mockUtil.getMockName(mock);

        // Assert
        assertEquals(expectedMockName, actualMockName);
    }

    @Test(expected = NotAMockException.class)
    public void testMockName_NullMock_ThrowsException() {
        // Arrange
        TestClass mock = null;

        // Act
        mockUtil.getMockName(mock);

        // Assert
        // Exception expected
    }

    private static class TestClass implements Testable {

    }

    private static class TestNullClass implements Testable {

    }

    private interface Testable {

    }

}
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.exceptions.NotAMockException;
import org.mockito.internal.util.MockHandlerInterface;
import org.mockito.internal.util.reflection.LenientCopyTool;

public class MockUtilTest {

    private MockUtil mockUtil;
    private CreationValidator creationValidator;

    @Before
    public void setUp() {
        creationValidator = mock(CreationValidator.class);
        mockUtil = new MockUtil(creationValidator);
    }

    @Test
    public void testCreateMock_ValidClass_ValidSettings_ReturnsMock() {
        // Arrange
        Class<TestInterface> classToMock = TestInterface.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.initiateMockName(classToMock);

        // Act
        TestInterface result = mockUtil.createMock(classToMock, settings);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof TestInterface);
        assertNotNull(result.getClass());
        assertNotNull(result.getClass().getInterfaces());
        assertNotNull(result.getClass().getSuperclass());
    }

    @Test(expected = NotAMockException.class)
    public void testResetMock_NullMock_ThrowsNotAMockException() {
        // Arrange
        TestInterface mock = null;

        // Act
        mockUtil.resetMock(mock);

        // Assert
        // NotAMockException is expected to be thrown
    }

    @Test
    public void testResetMock_ValidMock_ResetsMockHandler() {
        // Arrange
        TestInterface mock = mock(TestInterface.class);
        MockHandlerInterface<TestInterface> oldMockHandler = mockUtil.getMockHandler(mock);
        MockHandler<TestInterface> newMockHandler = new MockHandler<>(oldMockHandler);
        MethodInterceptorFilter newFilter = new MethodInterceptorFilter(newMockHandler, new MockSettingsImpl());
        ((Factory) mock).setCallback(0, newFilter);

        // Act
        mockUtil.resetMock(mock);

        // Assert
        assertNotSame(oldMockHandler, mockUtil.getMockHandler(mock));
        assertEquals(RETURNS_DEFAULTS, newFilter.getSettings().getDefaultAnswer());
    }

    @Test
    public void testGetMockHandler_NullMock_ThrowsNotAMockException() {
        // Arrange
        TestInterface mock = null;

        // Act
        try {
            mockUtil.getMockHandler(mock);
            fail("NotAMockException expected");
        } catch (NotAMockException e) {
            assertEquals("Argument should be a mock, but is null!", e.getMessage());
        }

        // Assert
        // NotAMockException is expected to be thrown
    }

    @Test
    public void testGetMockHandler_ValidMock_ReturnsMockHandler() {
        // Arrange
        TestInterface mock = mock(TestInterface.class);

        // Act
        MockHandlerInterface<TestInterface> result = mockUtil.getMockHandler(mock);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof MockHandlerInterface);
        assertEquals(mock, result.getMock());
    }

    @Test
    public void testIsMock_NullMock_ReturnsFalse() {
        // Arrange
        Object mock = null;

        // Act
        boolean result = mockUtil.isMock(mock);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsMock_ValidMock_ReturnsTrue() {
        // Arrange
        Object mock = mock(TestInterface.class);

        // Act
        boolean result = mockUtil.isMock(mock);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testGetMockName_ValidMock_ReturnsMockName() {
        // Arrange
        Object mock = mock(TestInterface.class);

        // Act
        MockName result = mockUtil.getMockName(mock);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof MockName);
    }

    private interface TestInterface {
        void method1();
        void method2();
    }
}
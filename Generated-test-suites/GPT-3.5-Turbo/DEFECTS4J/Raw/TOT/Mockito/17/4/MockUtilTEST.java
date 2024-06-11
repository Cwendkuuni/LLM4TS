import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.util.CreationValidator;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.LenientCopyTool;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockUtilTest {

    private MockUtil mockUtil;

    @Mock
    private CreationValidator creationValidator;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockUtil = new MockUtil(creationValidator);
    }

    @Test
    public void createMock_ValidClassToMock_ReturnsMock() {
        // Arrange
        Class<TestClass> classToMock = TestClass.class;
        MockSettingsImpl settings = new MockSettingsImpl();

        // Act
        TestClass result = mockUtil.createMock(classToMock, settings);

        // Assert
        assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createMock_NullClassToMock_ThrowsIllegalArgumentException() {
        // Arrange
        Class<TestInterface> classToMock = null;
        MockSettingsImpl settings = new MockSettingsImpl();

        // Act
        mockUtil.createMock(classToMock, settings);

        // Assert
        // Exception expected
    }

    @Test(expected = IllegalArgumentException.class)
    public void createMock_ClassToMockWithExtraInterfaces_ThrowsIllegalArgumentException() {
        // Arrange
        Class<TestInterface> classToMock = TestInterface.class;
        Class<?>[] extraInterfaces = new Class<?>[]{ExtraInterface.class};
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setExtraInterfaces(extraInterfaces);

        // Act
        mockUtil.createMock(classToMock, settings);

        // Assert
        // Exception expected
    }

    @Test(expected = IllegalArgumentException.class)
    public void createMock_ClassToMockWithSpiedInstance_ThrowsIllegalArgumentException() {
        // Arrange
        Class<TestClass> classToMock = TestClass.class;
        Object spiedInstance = new TestClass();
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setSpiedInstance(spiedInstance);

        // Act
        mockUtil.createMock(classToMock, settings);

        // Assert
        // Exception expected
    }

    @Test
    public void resetMock_ValidMock_ResetsMock() {
        // Arrange
        TestClass mock = mock(TestClass.class);

        // Act
        mockUtil.resetMock(mock);

        // Assert
        // Reset should not throw any exceptions
    }

    @Test(expected = NotAMockException.class)
    public void resetMock_NullMock_ThrowsNotAMockException() {
        // Arrange
        TestClass mock = null;

        // Act
        mockUtil.resetMock(mock);

        // Assert
        // Exception expected
    }

    @Test
    public void getMockHandler_ValidMock_ReturnsMockHandler() {
        // Arrange
        TestClass mock = mock(TestClass.class);
        MockHandlerInterface<TestInterface> mockHandler = mock(MockHandlerInterface.class);
        when(mockHandler.getHandler()).thenReturn(mockHandler);
        MethodInterceptorFilter filter = new MethodInterceptorFilter(mockHandler, new MockSettingsImpl());
        ((Factory) mock).setCallback(0, filter);

        // Act
        MockHandlerInterface<TestInterface> result = mockUtil.getMockHandler(mock);

        // Assert
        assertSame(mockHandler, result);
    }

    @Test(expected = NotAMockException.class)
    public void getMockHandler_NullMock_ThrowsNotAMockException() {
        // Arrange
        TestClass mock = null;

        // Act
        mockUtil.getMockHandler(mock);

        // Assert
        // Exception expected
    }

    @Test(expected = NotAMockException.class)
    public void getMockHandler_NonMockObject_ThrowsNotAMockException() {
        // Arrange
        TestClass mock = new TestClass();

        // Act
        mockUtil.getMockHandler(mock);

        // Assert
        // Exception expected
    }

    @Test(expected = NotAMockException.class)
    public void isMock_NullObject_ReturnsFalse() {
        // Arrange
        Object mock = null;

        // Act
        boolean result = mockUtil.isMock(mock);

        // Assert
        assertFalse(result);
    }

    @Test
    public void isMock_ValidMock_ReturnsTrue() {
        // Arrange
        TestClass mock = mock(TestClass.class);

        // Act
        boolean result = mockUtil.isMock(mock);

        // Assert
        assertTrue(result);
    }

    @Test
    public void isMock_NonMockObject_ReturnsFalse() {
        // Arrange
        TestClass mock = new TestClass();

        // Act
        boolean result = mockUtil.isMock(mock);

        // Assert
        assertFalse(result);
    }

    @Test
    public void getMockName_ValidMock_ReturnsMockName() {
        // Arrange
        TestClass mock = mock(TestClass.class);
        MockName mockName = mock(MockName.class);
        MockHandlerInterface<TestInterface> mockHandler = mock(MockHandlerInterface.class);
        when(mockHandler.getMockSettings()).thenReturn(new MockSettingsImpl());
        when(mockHandler.getMockSettings().getMockName()).thenReturn(mockName);
        when(mockHandler.getHandler()).thenReturn(mockHandler);
        MethodInterceptorFilter filter = new MethodInterceptorFilter(mockHandler, new MockSettingsImpl());
        ((Factory) mock).setCallback(0, filter);

        // Act
        MockName result = mockUtil.getMockName(mock);

        // Assert
        assertSame(mockName, result);
    }

    @Test(expected = NotAMockException.class)
    public void getMockName_NullMock_ThrowsNotAMockException() {
        // Arrange
        TestClass mock = null;

        // Act
        mockUtil.getMockName(mock);

        // Assert
        // Exception expected
    }

    private interface TestInterface {}

    private class TestClass implements TestInterface {}

    private interface ExtraInterface {}

}
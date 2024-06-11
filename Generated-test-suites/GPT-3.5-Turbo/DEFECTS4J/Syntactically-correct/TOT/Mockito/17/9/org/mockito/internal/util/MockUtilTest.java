import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.creation.MockSettingsImpl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockUtilTest {

    private MockUtil mockUtil;

    @Before
    public void setUp() {
        mockUtil = new MockUtil();
    }

    @Test
    public void testCreateMock_typicalCase() {
        // Arrange
        Class<MyClass> classToMock = MyClass.class;
        MockSettingsImpl settings = new MockSettingsImpl();

        // Act
        MyClass mock = mockUtil.createMock(classToMock, settings);

        // Assert
        assertNotNull(mock);
        assertTrue(mockUtil.isMock(mock));
        assertFalse(mockUtil.isMock(new Object()));
        assertEquals(classToMock, mock.getClass());
    }

    @Test
    public void testCreateMock_extraInterfaces() {
        // Arrange
        Class<MyClass> classToMock = MyClass.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {InterfaceOne.class, InterfaceTwo.class};
        settings.extraInterfaces(extraInterfaces);

        // Act
        MyClass mock = mockUtil.createMock(classToMock, settings);

        // Assert
        for (Class<?> interfaceClass : extraInterfaces) {
            assertTrue(interfaceClass.isInstance(mock));
        }
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMock_invalidMockType() {
        // Arrange
        Class<InvalidClass> classToMock = InvalidClass.class;
        MockSettingsImpl settings = new MockSettingsImpl();

        // Act
        MyClass mock = mockUtil.createMock(classToMock, settings);
    }

    @Test
    public void testResetMock() {
        // Arrange
        MyClass mock = mock(MyClass.class);

        // Act
        mockUtil.resetMock(mock);

        // Assert
        verifyZeroInteractions(mock);
    }

    @Test(expected = NotAMockException.class)
    public void testResetMock_notAMock() {
        // Arrange
        MyClass notAMock = new MyClass();

        // Act
        mockUtil.resetMock(notAMock);
    }

    @Test
    public void testGetMockHandler() {
        // Arrange
        MyClass mock = mock(MyClass.class);

        // Act
        MockHandlerInterface<MyClass> mockHandler = mockUtil.getMockHandler(mock);

        // Assert
        assertNotNull(mockHandler);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_notAMock() {
        // Arrange
        MyClass notAMock = new MyClass();

        // Act
        MockHandlerInterface<MyClass> mockHandler = mockUtil.getMockHandler(notAMock);
    }

    @Test
    public void testIsMock() {
        // Arrange
        MyClass mock = mock(MyClass.class);
        MyClass notAMock = new MyClass();

        // Act
        boolean isMock = mockUtil.isMock(mock);
        boolean isNotAMock = mockUtil.isMock(notAMock);

        // Assert
        assertTrue(isMock);
        assertFalse(isNotAMock);
    }

    @Test
    public void testGetMockName() {
        // Arrange
        MyClass mock = mock(MyClass.class);

        // Act
        MockName mockName = mockUtil.getMockName(mock);

        // Assert
        assertNotNull(mockName);
    }

    private interface InterfaceOne {}

    private interface InterfaceTwo {}

    private class MyClass {}

    private class InvalidClass {}
}
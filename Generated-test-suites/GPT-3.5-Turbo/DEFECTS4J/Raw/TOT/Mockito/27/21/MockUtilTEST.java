import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDefaults;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.Serializable;

import static org.junit.Assert.*;

public class MockUtilTest {

    private MockUtil mockUtil;

    @Before
    public void setUp() {
        mockUtil = new MockUtil();
    }

    @After
    public void tearDown() {
        mockUtil = null;
    }

    @Test
    public void testCreateMock_ValidClass_ValidSettings_ReturnsMockObject() {
        // Arrange
        Class<TestClass> classToMock = TestClass.class;
        MockSettingsImpl settings = new MockSettingsImpl();

        // Act
        TestClass mock = mockUtil.createMock(classToMock, settings);

        // Assert
        assertNotNull(mock);
        assertTrue(classToMock.isInstance(mock));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateMock_NullClass_ThrowsIllegalArgumentException() {
        // Arrange
        Class<TestNullClass> classToMock = null;
        MockSettingsImpl settings = new MockSettingsImpl();

        // Act
        mockUtil.createMock(classToMock, settings);

        // Assert (Exception expected)
    }

    @Test
    public void testCreateMock_ExtraInterfaces_ReturnsMockObjectImplementingExtraInterfaces() {
        // Arrange
        Class<TestExtraInterface> classToMock = TestExtraInterface.class;
        Class<?>[] extraInterfaces = {Serializable.class};
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.extraInterfaces(extraInterfaces);

        // Act
        TestExtraInterface mock = mockUtil.createMock(classToMock, settings);

        // Assert
        assertTrue(extraInterfaces.length > 0);
        for (Class<?> interfaceClass : extraInterfaces) {
            assertTrue(interfaceClass.isInstance(mock));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateMock_NullExtraInterface_ThrowsIllegalArgumentException() {
        // Arrange
        Class<TestClass> classToMock = TestClass.class;
        Class<?>[] extraInterfaces = null;
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.extraInterfaces(extraInterfaces);

        // Act
        mockUtil.createMock(classToMock, settings);

        // Assert (Exception expected)
    }

    @Test
    public void testCreateMock_SerializableSetting_ReturnsMockObjectImplementingSerializable() {
        // Arrange
        Class<TestSerializableClass> classToMock = TestSerializableClass.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.serializable();

        // Act
        TestSerializableClass mock = mockUtil.createMock(classToMock, settings);

        // Assert
        assertTrue(mock instanceof Serializable);
    }

    @Test
    public void testCreateMock_SpiedInstance_ReturnsMockObjectCopyingStateFromSpiedInstance() {
        // Arrange
        TestClass spiedInstance = new TestClass();
        spiedInstance.setNumber(100);
        Class<TestClass> classToMock = TestClass.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.spiedInstance(spiedInstance);

        // Act
        TestClass mock = mockUtil.createMock(classToMock, settings);

        // Assert
        assertNotNull(mock);
        assertEquals(spiedInstance.getNumber(), mock.getNumber());
    }

    @Test(expected = ClassCastException.class)
    public void testCreateMock_InvalidExtraInterface_ThrowsClassCastException() {
        // Arrange
        Class<TestInvalidInterface> classToMock = TestInvalidInterface.class;
        Class<?>[] extraInterfaces = {Serializable.class};
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.extraInterfaces(extraInterfaces);

        // Act
        mockUtil.createMock(classToMock, settings);

        // Assert (Exception expected)
    }

    @Test
    public void testResetMock_ValidMock_ResetMockCallback() {
        // Arrange
        TestClass mock = Mockito.mock(TestClass.class);
        MockHandlerInterface<TestInterface> oldMockHandler = mockUtil.getMockHandler(mock);
        MethodInterceptorFilter newFilter = newMethodInterceptorFilter(mock, Mockito.withSettings().defaultAnswer(Mockito.RETURNS_DEFAULTS));

        // Act
        mockUtil.resetMock(mock);
        MethodInterceptorFilter updatedFilter = mockUtil.getInterceptor(mock);

        // Assert
        assertNotSame(oldMockHandler, updatedFilter);
        assertEquals(newFilter.getClass(), updatedFilter.getClass());
    }

    @Test(expected = NotAMockException.class)
    public void testResetMock_NullMock_ThrowsNotAMockException() {
        // Arrange
        TestClass mock = null;

        // Act
        mockUtil.resetMock(mock);

        // Assert (Exception expected)
    }

    @Test
    public void testGetMockHandler_ValidMock_ReturnsMockHandlerInterface() {
        // Arrange
        TestClass mock = Mockito.mock(TestClass.class);
        MockHandlerInterface<TestClass> expectedMockHandler = mockUtil.getMockHandler(mock);

        // Act
        MockHandlerInterface<TestClass> actualMockHandler = mockUtil.getMockHandler(mock);

        // Assert
        assertEquals(expectedMockHandler, actualMockHandler);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_NullMock_ThrowsNotAMockException() {
        // Arrange
        TestClass mock = null;

        // Act
        mockUtil.getMockHandler(mock);

        // Assert (Exception expected)
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_NonMockObject_ThrowsNotAMockException() {
        // Arrange
        TestClass mock = new TestClass();

        // Act
        mockUtil.getMockHandler(mock);

        // Assert (Exception expected)
    }

    @Test
    public void testIsMock_ValidMock_ReturnsTrue() {
        // Arrange
        TestClass mock = Mockito.mock(TestClass.class);

        // Act
        boolean isMock = mockUtil.isMock(mock);

        // Assert
        assertTrue(isMock);
    }

    @Test
    public void testIsMock_NullMock_ReturnsFalse() {
        // Arrange
        TestClass mock = null;

        // Act
        boolean isMock = mockUtil.isMock(mock);

        // Assert
        assertFalse(isMock);
    }

    @Test
    public void testIsMock_NonMockObject_ReturnsFalse() {
        // Arrange
        TestClass mock = new TestClass();

        // Act
        boolean isMock = mockUtil.isMock(mock);

        // Assert
        assertFalse(isMock);
    }

    @Test
    public void testGetInterceptor_ValidMock_ReturnsMethodInterceptorFilter() {
        // Arrange
        TestClass mock = Mockito.mock(TestClass.class);
        MethodInterceptorFilter expectedInterceptor = mockUtil.getInterceptor(mock);

        // Act
        MethodInterceptorFilter actualInterceptor = mockUtil.getInterceptor(mock);

        // Assert
        assertEquals(expectedInterceptor, actualInterceptor);
    }

    @Test
    public void testGetInterceptor_NonMockObject_ReturnsNull() {
        // Arrange
        TestClass mock = new TestClass();

        // Act
        MethodInterceptorFilter interceptor = mockUtil.getInterceptor(mock);

        // Assert
        assertNull(interceptor);
    }

    @Test(expected = NotAMockException.class)
    public void testGetInterceptor_NullMock_ThrowsNotAMockException() {
        // Arrange
        TestClass mock = null;

        // Act
        mockUtil.getInterceptor(mock);

        // Assert (Exception expected)
    }

    @Test
    public void testGetMockName_ValidMock_ReturnsMockName() {
        // Arrange
        TestClass mock = Mockito.mock(TestClass.class);
        MockName expectedMockName = mockUtil.getMockHandler(mock).getMockSettings().getMockName();

        // Act
        MockName actualMockName = mockUtil.getMockName(mock);

        // Assert
        assertEquals(expectedMockName, actualMockName);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockName_NullMock_ThrowsNotAMockException() {
        // Arrange
        TestClass mock = null;

        // Act
        mockUtil.getMockName(mock);

        // Assert (Exception expected)
    }

    private <T> MethodInterceptorFilter newMethodInterceptorFilter(T mock, MockSettingsImpl settings) {
        MockHandler<T> mockHandler = new MockHandler<>(settings);
        InvocationNotifierHandler<T> invocationNotifierHandler = new InvocationNotifierHandler<>(mockHandler, settings);
        return new MethodInterceptorFilter(invocationNotifierHandler, settings);
    }
}

class TestClass {
    private int number;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}

interface TestInterface {
    void testMethod();
}

interface TestExtraInterface {
    void testExtraMethod();
}

class TestSerializableClass implements Serializable {
    private static final long serialVersionUID = 1L;
}

interface TestInvalidInterface {
    void testInvalidMethod();
}

class TestNullClass {
}
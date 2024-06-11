import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.internal.creation.instance.InstantiationException;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

import java.lang.reflect.Constructor;

import static org.mockito.Mockito.*;

public class ByteBuddyMockMakerTest {
    
    @Test
    public void testCreateMock_ValidInputs_ReturnsMockInstance() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<TestClass> settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);

        // Act
        TestClass mock = mockMaker.createMock(settings, handler);

        // Assert
        Assert.assertNotNull(mock);
    }

    @Test
    public void testCreateMock_SerializableModeAcrossClassLoaders_ThrowsMockitoException() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<TestClass> settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);
        when(settings.getSerializableMode()).thenReturn(SerializableMode.ACROSS_CLASSLOADERS);

        // Act & Assert
        Assert.assertThrows(MockitoException.class, () -> mockMaker.createMock(settings, handler));
    }

    @Test
    public void testCreateMock_ClassCastException_ThrowsMockitoException() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<TestClass> settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);
        when(settings.getTypeToMock()).thenReturn(Object.class);

        // Act & Assert
        Assert.assertThrows(MockitoException.class, () -> mockMaker.createMock(settings, handler));
    }

    @Test
    public void testCreateMock_InstantiationException_ThrowsMockitoException() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<TestClass> settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);
        when(settings.getTypeToMock()).thenReturn(TestClass.class);
        doThrow(InstantiationException.class).when(mockMaker.classInstantiator).instantiate(TestClass.class);

        // Act & Assert
        Assert.assertThrows(MockitoException.class, () -> mockMaker.createMock(settings, handler));
    }

    @Test
    public void testEnsureMockIsAssignableToMockedType_ValidInputs_ReturnsMock() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<TestClass> settings = mock(MockCreationSettings.class);
        TestClass mock = mock(TestClass.class);

        // Act
        TestClass result = mockMaker.ensureMockIsAssignableToMockedType(settings, mock);

        // Assert
        Assert.assertEquals(mock, result);
    }

    @Test
    public void testEnsureMockIsAssignableToMockedType_InvalidInputs_ThrowsClassCastException() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<TestClass> settings = mock(MockCreationSettings.class);
        Object mock = new Object();

        // Act & Assert
        Assert.assertThrows(ClassCastException.class, () -> mockMaker.ensureMockIsAssignableToMockedType(settings, mock));
    }

    @Test
    public void testDescribeClass_ClassType_ReturnsDescription() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Class<TestEnum> type = TestEnum.class;

        // Act
        String result = mockMaker.describeClass(type);

        // Assert
        Assert.assertEquals("'org.example.TestEnum', loaded by classloader : 'null'", result);
    }

    @Test
    public void testDescribeClass_NullType_ReturnsNullDescription() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Class type = null;

        // Act
        String result = mockMaker.describeClass(type);

        // Assert
        Assert.assertEquals("null", result);
    }

    @Test
    public void testDescribeClass_ObjectInstance_ReturnsDescription() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        TestEnum instance = TestEnum.VALUE1;

        // Act
        String result = mockMaker.describeClass(instance);

        // Assert
        Assert.assertEquals("'org.example.TestEnum', loaded by classloader : 'null'", result);
    }

    @Test
    public void testDescribeClass_NullObjectInstance_ReturnsNullDescription() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object instance = null;

        // Act
        String result = mockMaker.describeClass(instance);

        // Assert
        Assert.assertEquals("null", result);
    }

    @Test
    public void testGetHandler_ValidMock_ReturnsMockHandler() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        TestClass mock = mock(TestClass.class);

        // Act
        MockHandler result = mockMaker.getHandler(mock);

        // Assert
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof InternalMockHandler);
    }

    @Test
    public void testGetHandler_InvalidType_ReturnsNull() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object mock = new Object();

        // Act
        MockHandler result = mockMaker.getHandler(mock);

        // Assert
        Assert.assertNull(result);
    }

    @Test
    public void testResetMock_ValidMock_ValidInputs_UpdatesMockitoInterceptor() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        TestClass mock = mock(TestClass.class);
        MockHandler newHandler = mock(MockHandler.class);
        MockCreationSettings settings = mock(MockCreationSettings.class);

        // Act
        mockMaker.resetMock(mock, newHandler, settings);

        // Assert
        verify(mock, times(1)).setMockitoInterceptor(any());
    }

    @Test
    public void testResetMock_InvalidMock_ThrowsClassCastException() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object mock = new Object();
        MockHandler newHandler = mock(MockHandler.class);
        MockCreationSettings settings = mock(MockCreationSettings.class);

        // Act & Assert
        Assert.assertThrows(ClassCastException.class, () -> mockMaker.resetMock(mock, newHandler, settings));
    }

    @Test
    public void testInitializeClassInstantiator_ObjenesisClassLoaderExists_ReturnsClassInstantiatorUsingObjenesis() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();

        // Act
        ClassInstantiator result = mockMaker.initializeClassInstantiator();

        // Assert
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof ClassInstantiator.UsingObjenesis);
    }

    @Test
    public void testInitializeClassInstantiator_ObjenesisClassLoaderDoesNotExist_ThrowsIllegalStateException() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        mockMaker.classInstantiator = null;

        // Act & Assert
        Assert.assertThrows(IllegalStateException.class, () -> mockMaker.initializeClassInstantiator());
    }

    @Test
    public void testAsInternalMockHandler_ValidHandler_ReturnsInternalMockHandler() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        InternalMockHandler handler = mock(InternalMockHandler.class);

        // Act
        InternalMockHandler result = mockMaker.asInternalMockHandler(handler);

        // Assert
        Assert.assertNotNull(result);
        Assert.assertEquals(handler, result);
    }

    @Test
    public void testAsInternalMockHandler_InvalidHandler_ThrowsMockitoException() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockHandler handler = mock(MockHandler.class);

        // Act & Assert
        Assert.assertThrows(MockitoException.class, () -> mockMaker.asInternalMockHandler(handler));
    }

    private static class TestClass {}

    private enum TestEnum {
        VALUE1,
        VALUE2
    }
}
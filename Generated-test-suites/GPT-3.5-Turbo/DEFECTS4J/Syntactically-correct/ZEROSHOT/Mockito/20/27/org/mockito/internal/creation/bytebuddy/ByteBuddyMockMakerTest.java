
package org.mockito.internal.creation.bytebuddy;

import org.junit.After;
import org.junit.Before;
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

import static org.mockito.internal.util.StringJoiner.join;
import static org.mockito.Mockito.*;

public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker byteBuddyMockMaker;
    private ClassInstantiator classInstantiator;
    private CachingMockBytecodeGenerator cachingMockBytecodeGenerator;

    @Before
    public void setup() {
        byteBuddyMockMaker = new ByteBuddyMockMaker();
        classInstantiator = new ClassInstantiator();
        cachingMockBytecodeGenerator = new CachingMockBytecodeGenerator();
    }

    @After
    public void teardown() {
        byteBuddyMockMaker = null;
        classInstantiator = null;
        cachingMockBytecodeGenerator = null;
    }

    @Test(expected = MockitoException.class)
    public void createMock_ThrowException_WhenSerializableModeIsACROSS_CLASSLOADERS() {
        // Arrange
        MockCreationSettings<String> settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);
        when(settings.getSerializableMode()).thenReturn(SerializableMode.ACROSS_CLASSLOADERS);

        // Act
        byteBuddyMockMaker.createMock(settings, handler);

        // Assert (exception expected)
    }

    @Test
    public void createMock_ReturnsMockInstance_WhenSettingsAreValid() {
        // Arrange
        MockCreationSettings<String> settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);
        when(settings.getTypeToMock()).thenReturn(String.class);
        when(settings.getExtraInterfaces()).thenReturn(new Class[] {});

        // Act
        String mockInstance = byteBuddyMockMaker.createMock(settings, handler);

        // Assert
        assertNotNull(mockInstance);
        verify(settings).getTypeToMock();
        verify(settings).getExtraInterfaces();
    }

    @Test(expected = MockitoException.class)
    public void createMock_ThrowException_WhenClassCastExceptionOccurs() {
        // Arrange
        MockCreationSettings<Byte> settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);
        when(settings.getTypeToMock()).thenReturn(Byte.class);
        when(settings.getExtraInterfaces()).thenReturn(new Class[] {});

        // Act
        byteBuddyMockMaker.createMock(settings, handler);

        // Assert (exception expected)
    }

    @Test(expected = MockitoException.class)
    public void createMock_ThrowException_WhenInstantiationExceptionOccurs() {
        // Arrange
        MockCreationSettings<String> settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);
        when(settings.getTypeToMock()).thenReturn(String.class);
        when(settings.getExtraInterfaces()).thenReturn(new Class[] {});
        when(classInstantiator.instantiate(any(Class.class))).thenThrow(new InstantiationException());

        // Act
        byteBuddyMockMaker.createMock(settings, handler);

        // Assert (exception expected)
    }

    @Test
    public void ensureMockIsAssignableToMockedType_ReturnsMock_WhenMockIsAssignableToMockedType() {
        // Arrange
        MockCreationSettings<String> settings = mock(MockCreationSettings.class);
        String mock = "mock";
        when(settings.getTypeToMock()).thenReturn(String.class);

        // Act
        String result = byteBuddyMockMaker.ensureMockIsAssignableToMockedType(settings, mock);

        // Assert
        assertNotNull(result);
        assertEquals(String.class, result.getClass());
    }

    @Test(expected = ClassCastException.class)
    public void ensureMockIsAssignableToMockedType_ThrowClassCastException_WhenMockIsNotAssignableToMockedType() {
        // Arrange
        MockCreationSettings<String> settings = mock(MockCreationSettings.class);
        Integer mock = 10;
        when(settings.getTypeToMock()).thenReturn(String.class);

        // Act
        byteBuddyMockMaker.ensureMockIsAssignableToMockedType(settings, mock);

        // Assert (exception expected)
    }

    @Test
    public void describeClass_ReturnsDescription_WhenClassIsNull() {
        // Arrange
        Class aClass = null;
        String expected = "null";

        // Act
        String result = byteBuddyMockMaker.describeClass(aClass);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void describeClass_ReturnsDescription_WhenClassIsNotNull() {
        // Arrange
        Class aClass = String.class;
        String expected = "'java.lang.String', loaded by classloader : '" + String.class.getClassLoader() + "'";

        // Act
        String result = byteBuddyMockMaker.describeClass(aClass);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void describeClass_ReturnsDescription_WhenInstanceIsNull() {
        // Arrange
        Object instance = null;
        String expected = "null";

        // Act
        String result = byteBuddyMockMaker.describeClass(instance);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void describeClass_ReturnsDescription_WhenInstanceIsNotNull() {
        // Arrange
        Object instance = new Object();
        String expected = "'java.lang.Object', loaded by classloader : '" + instance.getClass().getClassLoader() + "'";

        // Act
        String result = byteBuddyMockMaker.describeClass(instance);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void getHandler_ReturnsMockHandler_WhenMockIsInstanceOfMockMethodInterceptorMockAccess() {
        // Arrange
        Object mock = mock(Object.class);

        // Act
        MockHandler result = byteBuddyMockMaker.getHandler(mock);

        // Assert
        assertNull(result);
    }

    @Test
    public void resetMock_SetsNewMockHandler_WhenMockIsInstanceOfMockMethodInterceptorMockAccess() {
        // Arrange
        Object mock = mock(Object.class);
        MockHandler newHandler = mock(MockHandler.class);
        MockCreationSettings settings = mock(MockCreationSettings.class);

        // Act
        byteBuddyMockMaker.resetMock(mock, newHandler, settings);

        // Assert
        verify(mock).setMockitoInterceptor(any(MockMethodInterceptor.class));
    }

    @Test(expected = MockitoException.class)
    public void resetMock_ThrowException_WhenMockHandlerIsNotInstanceOfInternalMockHandler() {
        // Arrange
        Object mock = mock(Object.class);
        MockHandler newHandler = mock(MockHandler.class);
        MockCreationSettings settings = mock(MockCreationSettings.class);

        // Act
        byteBuddyMockMaker.resetMock(mock, newHandler, settings);

        // Assert (exception expected)
    }

    @Test
    public void initializeClassInstantiator_ReturnsClassInstantiator_WhenObjenesisIsAvailable() {
        // Arrange
        GlobalConfiguration globalConfiguration = new GlobalConfiguration();
        globalConfiguration.enableClassCache();

        // Act
        ClassInstantiator result = byteBuddyMockMaker.initializeClassInstantiator();

        // Assert
        assertNotNull(result);
    }
}

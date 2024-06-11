import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Constructor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker mockMaker;

    @Before
    public void setUp() {
        mockMaker = new ByteBuddyMockMaker();
    }

    @Test(expected = MockitoException.class)
    public void createMock_SerializableAcrossClassLoadersNotSupported_ExceptionThrown() {
        // Arrange
        MockCreationSettings settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);
        when(settings.getSerializableMode()).thenReturn(SerializableMode.ACROSS_CLASSLOADERS);

        // Act
        mockMaker.createMock(settings, handler);

        // Assert
        // An exception should be thrown
    }

    @Test
    public void createMock_ValidSettings_SuccessfulCreation() {
        // Arrange
        MockCreationSettings settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);
        when(settings.getSerializableMode()).thenReturn(null);
        when(settings.getTypeToMock()).thenReturn(String.class);
        when(settings.getExtraInterfaces()).thenReturn(new Class[]{Comparable.class});

        // Act
        Object mock = mockMaker.createMock(settings, handler);

        // Assert
        assertNotNull(mock);
        assertTrue(mock instanceof String);
        verify(handler, times(1)).getMockHandler();
    }

    @Test(expected = MockitoException.class)
    public void createMock_ClassCastException_ClassCastExceptionThrown() {
        // Arrange
        MockCreationSettings settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);
        when(settings.getSerializableMode()).thenReturn(null);
        when(settings.getTypeToMock()).thenReturn(String.class);
        when(settings.getExtraInterfaces()).thenReturn(new Class[]{Comparable.class});
        ClassInstantiator classInstantiator = mock(ClassInstantiator.class);
        when(classInstantiator.instantiate(any(Class.class))).thenThrow(ClassCastException.class);
        Constructor mockedConstructor = mock(Constructor.class);
        Class<Object> mockedProxyType = mock(Class.class);
        Object mockInstance = mock(Object.class);
        when(mockInstance.getClass()).thenReturn(mockedProxyType);
        CachingMockBytecodeGenerator cachingMockBytecodeGenerator = mock(CachingMockBytecodeGenerator.class);
        when(cachingMockBytecodeGenerator.get(any(Class.class), any(Class[].class))).thenReturn(mockedProxyType);
        when(settings.getSerializableMode()).thenReturn(null);
        when(settings.getTypeToMock()).thenReturn(String.class);
        when(settings.getExtraInterfaces()).thenReturn(new Class[]{Comparable.class});
        mockMaker = spy(mockMaker);
        doReturn(classInstantiator).when(mockMaker).initializeClassInstantiator();
        doReturn(mockedProxyType).when(cachingMockBytecodeGenerator).get(any(Class.class), any(Class[].class));
        doReturn(mockInstance).when(classInstantiator).instantiate(any(Class.class));

        // Act
        mockMaker.createMock(settings, handler);

        // Assert
        // An exception should be thrown
    }

    @Test(expected = MockitoException.class)
    public void createMock_InstantiationException_InstantiationExceptionThrown() {
        // Arrange
        MockCreationSettings settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);
        when(settings.getSerializableMode()).thenReturn(null);
        when(settings.getTypeToMock()).thenReturn(String.class);
        when(settings.getExtraInterfaces()).thenReturn(new Class[]{Comparable.class});
        ClassInstantiator classInstantiator = mock(ClassInstantiator.class);
        when(classInstantiator.instantiate(any(Class.class))).thenThrow(org.mockito.internal.creation.instance.InstantiationException.class);
        Constructor mockedConstructor = mock(Constructor.class);
        Class<Object> mockedProxyType = mock(Class.class);
        Object mockInstance = mock(Object.class);
        when(mockInstance.getClass()).thenReturn(mockedProxyType);
        CachingMockBytecodeGenerator cachingMockBytecodeGenerator = mock(CachingMockBytecodeGenerator.class);
        when(cachingMockBytecodeGenerator.get(any(Class.class), any(Class[].class))).thenReturn(mockedProxyType);
        when(settings.getSerializableMode()).thenReturn(null);
        when(settings.getTypeToMock()).thenReturn(String.class);
        when(settings.getExtraInterfaces()).thenReturn(new Class[]{Comparable.class});
        mockMaker = spy(mockMaker);
        doReturn(classInstantiator).when(mockMaker).initializeClassInstantiator();
        doReturn(mockedProxyType).when(cachingMockBytecodeGenerator).get(any(Class.class), any(Class[].class));
        doReturn(mockInstance).when(classInstantiator).instantiate(any(Class.class));

        // Act
        mockMaker.createMock(settings, handler);

        // Assert
        // An exception should be thrown
    }

    @Test
    public void ensureMockIsAssignableToMockedType_MockIsAssignable_Success() {
        // Arrange
        MockCreationSettings settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);
        Class<String> typeToMock = String.class;
        String mock = "mock";
        when(settings.getTypeToMock()).thenReturn(typeToMock);

        // Act
        String result = mockMaker.ensureMockIsAssignableToMockedType(settings, mock);

        // Assert
        assertEquals(mock, result);
    }

    @Test(expected = ClassCastException.class)
    public void ensureMockIsAssignableToMockedType_MockIsNotAssignable_ClassCastExceptionThrown() {
        // Arrange
        MockCreationSettings settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);
        Class<String> typeToMock = String.class;
        Integer mock = 123;
        when(settings.getTypeToMock()).thenReturn(typeToMock);

        // Act
        mockMaker.ensureMockIsAssignableToMockedType(settings, mock);

        // Assert
        // An exception should be thrown
    }

    @Test
    public void describeClass_TypeIsNull_ReturnsNull() {
        // Arrange
        Class type = null;

        // Act
        String result = mockMaker.describeClass(type);

        // Assert
        assertEquals("null", result);
    }

    @Test
    public void describeClass_TypeIsNotNull_ReturnsCanonicalNameAndClassLoader() {
        // Arrange
        Class type = String.class;

        // Act
        String result = mockMaker.describeClass(type);

        // Assert
        assertEquals("'java.lang.String', loaded by classloader : 'null'", result);
    }

    @Test
    public void describeClass_InstanceIsNull_ReturnsNull() {
        // Arrange
        Object instance = null;

        // Act
        String result = mockMaker.describeClass(instance);

        // Assert
        assertEquals("null", result);
    }

    @Test
    public void describeClass_InstanceIsNotNull_ReturnsClassDescription() {
        // Arrange
        Object instance = "mock";

        // Act
        String result = mockMaker.describeClass(instance);

        // Assert
        assertEquals("'java.lang.String', loaded by classloader : 'null'", result);
    }

    @Test
    public void getHandler_MockIsNotMockAccess_ReturnsNull() {
        // Arrange
        Object mock = new Object();

        // Act
        MockHandler result = mockMaker.getHandler(mock);

        // Assert
        assertNull(result);
    }

    @Test
    public void getHandler_MockIsMockAccess_ReturnsMockHandler() {
        // Arrange
        MockMethodInterceptor.MockAccess mockAccess = mock(MockMethodInterceptor.MockAccess.class);
        MockHandler expectedHandler = mock(MockHandler.class);
        when(mockAccess.getMockitoInterceptor()).thenReturn(MockitoInterceptor.class.cast(expectedHandler));
        Object mock = mockAccess;

        // Act
        MockHandler result = mockMaker.getHandler(mock);

        // Assert
        assertEquals(expectedHandler, result);
    }

    @Test(expected = MockitoException.class)
    public void resetMock_MockIsNotMockAccess_ExceptionThrown() {
        // Arrange
        Object mock = new Object();
        MockHandler newHandler = mock(MockHandler.class);
        MockCreationSettings settings = mock(MockCreationSettings.class);

        // Act
        mockMaker.resetMock(mock, newHandler, settings);

        // Assert
        // An exception should be thrown
    }

    @Test
    public void resetMock_MockIsMockAccess_SuccessfulReset() {
        // Arrange
        MockMethodInterceptor.MockAccess mockAccess = mock(MockMethodInterceptor.MockAccess.class);
        MockHandler newHandler = mock(MockHandler.class);
        MockCreationSettings settings = mock(MockCreationSettings.class);
        Object mock = mockAccess;
        MockMethodInterceptor mockitoInterceptor = mock(MockMethodInterceptor.class);
        when(mockAccess.getMockitoInterceptor()).thenReturn(mockitoInterceptor);

        // Act
        mockMaker.resetMock(mock, newHandler, settings);

        // Assert
        verify(mockitoInterceptor, times(1)).setMockHandler(newHandler);
    }

    @Test
    public void initializeClassInstantiator_ObjenesisInClasspath_ReturnsUsingObjenesisClassInstantiator() {
        // Arrange
        ByteBuddyMockMaker byteBuddyMockMaker = mock(ByteBuddyMockMaker.class);
        GlobalConfiguration globalConfiguration = mock(GlobalConfiguration.class);
        ClassInstantiator expectedClassInstantiator = mock(ClassInstantiator.class);
        Class<?> objenesisClassLoader = mock(Class.class);
        Constructor<?> usingClassCacheConstructor = mock(Constructor.class);
        Object usingClassCacheInstance = mock(Object.class);
        when(byteBuddyMockMaker.initializeClassInstantiator()).thenCallRealMethod();
        when(byteBuddyMockMaker.getClass()).thenReturn(ByteBuddyMockMaker.class);
        when(byteBuddyMockMaker.new GlobalConfiguration()).thenReturn(globalConfiguration);
        when(globalConfiguration.enableClassCache()).thenReturn(true);
        when(byteBuddyMockMaker.loadClass("org.mockito.internal.creation.bytebuddy.ClassInstantiator$UsingObjenesis")).thenReturn(objenesisClassLoader);
        when(byteBuddyMockMaker.getDeclaredConstructor(objenesisClassLoader, boolean.class)).thenReturn(usingClassCacheConstructor);
        when(byteBuddyMockMaker.newInstance(usingClassCacheConstructor, true)).thenReturn(usingClassCacheInstance);
        when(byteBuddyMockMaker.cast(usingClassCacheInstance)).thenReturn(expectedClassInstantiator);

        // Act
        ClassInstantiator result = byteBuddyMockMaker.initializeClassInstantiator();

        // Assert
        assertEquals(expectedClassInstantiator, result);
    }

    @Test(expected = IllegalStateException.class)
    public void initializeClassInstantiator_ObjenesisNotInClasspath_ExceptionThrown() {
        // Arrange
        ByteBuddyMockMaker byteBuddyMockMaker = mock(ByteBuddyMockMaker.class);
        when(byteBuddyMockMaker.initializeClassInstantiator()).thenCallRealMethod();
        when(byteBuddyMockMaker.getClass()).thenReturn(ByteBuddyMockMaker.class);
        when(byteBuddyMockMaker.loadClass("org.mockito.internal.creation.bytebuddy.ClassInstantiator$UsingObjenesis")).thenThrow(new ClassNotFoundException());

        // Act
        byteBuddyMockMaker.initializeClassInstantiator();

        // Assert
        // An exception should be thrown
    }

    @Test(expected = MockitoException.class)
    public void asInternalMockHandler_HandlerIsNotInternalMockHandler_ExceptionThrown() {
        // Arrange
        MockHandler handler = mock(MockHandler.class);

        // Act
        mockMaker.asInternalMockHandler(handler);

        // Assert
        // An exception should be thrown
    }

    @Test
    public void asInternalMockHandler_HandlerIsInternalMockHandler_SuccessfulCasting() {
        // Arrange
        InternalMockHandler expectedInternalMockHandler = mock(InternalMockHandler.class);
        MockHandler handler = expectedInternalMockHandler;

        // Act
        InternalMockHandler result = mockMaker.asInternalMockHandler(handler);

        // Assert
        assertEquals(expectedInternalMockHandler, result);
    }

}
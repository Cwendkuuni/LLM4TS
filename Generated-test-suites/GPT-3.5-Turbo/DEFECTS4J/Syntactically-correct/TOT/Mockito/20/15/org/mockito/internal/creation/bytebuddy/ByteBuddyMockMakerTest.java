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
    private ClassInstantiator classInstantiator;
    private CachingMockBytecodeGenerator cachingMockBytecodeGenerator;
    
    @Before
    public void setup() {
        mockMaker = new ByteBuddyMockMaker();
        classInstantiator = mock(ClassInstantiator.class);
        cachingMockBytecodeGenerator = mock(CachingMockBytecodeGenerator.class);
    }
    
    @Test
    public void createMock_ValidSettings_ReturnsMockInstance() {
        // Arrange
        @SuppressWarnings("unchecked")
        MockCreationSettings<Object> settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);
        Class<?> mockedProxyType = Object.class;
        Object mockInstance = new Object();
        
        when(settings.getSerializableMode()).thenReturn(SerializableMode.NONE);
        when(settings.getTypeToMock()).thenReturn((Class<Object>)Object.class);
        when(settings.getExtraInterfaces()).thenReturn(new Class<?>[]{});
        when(cachingMockBytecodeGenerator.get(any(Class.class), any(Class[].class))).thenReturn(mockedProxyType);
        when(classInstantiator.instantiate(mockedProxyType)).thenReturn(mockInstance);
        
        // Act
        Object result = mockMaker.createMock(settings, handler);
        
        // Assert
        assertNotNull(result);
        assertEquals(mockInstance, result);
        verify(cachingMockBytecodeGenerator).get(Object.class, new Class<?>[]{});
        verify(classInstantiator).instantiate(mockedProxyType);
        verify(handler).getMockitoInterceptor();
    }
    
    @Test(expected = MockitoException.class)
    public void createMock_SerializableModeAcrossClassLoaders_ThrowsMockitoException() {
        // Arrange
        @SuppressWarnings("unchecked")
        MockCreationSettings<Object> settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);
        
        when(settings.getSerializableMode()).thenReturn(SerializableMode.ACROSS_CLASSLOADERS);
        
        // Act
        Object result = mockMaker.createMock(settings, handler);
        
        // Assert
        fail("Expected MockitoException to be thrown");
    }
    
    @Test(expected = MockitoException.class)
    public void createMock_ClassCastException_ThrowsMockitoException() {
        // Arrange
        @SuppressWarnings("unchecked")
        MockCreationSettings<Object> settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);
        Class<?> mockedProxyType = Object.class;
        
        when(settings.getSerializableMode()).thenReturn(SerializableMode.NONE);
        when(settings.getTypeToMock()).thenReturn((Class<Object>)Object.class);
        when(settings.getExtraInterfaces()).thenReturn(new Class<?>[]{});
        when(cachingMockBytecodeGenerator.get(any(Class.class), any(Class[].class))).thenReturn(mockedProxyType);
        when(classInstantiator.instantiate(mockedProxyType)).thenThrow(ClassCastException.class);
        
        // Act
        Object result = mockMaker.createMock(settings, handler);
        
        // Assert
        fail("Expected MockitoException to be thrown");
    }
    
    @Test(expected = MockitoException.class)
    public void createMock_InstantiationException_ThrowsMockitoException() {
        // Arrange
        @SuppressWarnings("unchecked")
        MockCreationSettings<Object> settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);
        Class<?> mockedProxyType = Object.class;
        
        when(settings.getSerializableMode()).thenReturn(SerializableMode.NONE);
        when(settings.getTypeToMock()).thenReturn((Class<Object>)Object.class);
        when(settings.getExtraInterfaces()).thenReturn(new Class<?>[]{});
        when(cachingMockBytecodeGenerator.get(any(Class.class), any(Class[].class))).thenReturn(mockedProxyType);
        when(classInstantiator.instantiate(mockedProxyType)).thenThrow(org.mockito.internal.creation.instance.InstantiationException.class);
        
        // Act
        Object result = mockMaker.createMock(settings, handler);
        
        // Assert
        fail("Expected MockitoException to be thrown");
    }
    
    @Test
    public void ensureMockIsAssignableToMockedType_ValidSettings_ReturnsMockInstance() {
        // Arrange
        @SuppressWarnings("unchecked")
        MockCreationSettings<Object> settings = mock(MockCreationSettings.class);
        Object mock = new Object();
        Class<Object> typeToMock = Object.class;
        
        when(settings.getTypeToMock()).thenReturn(typeToMock);
        
        // Act
        Object result = mockMaker.ensureMockIsAssignableToMockedType(settings, mock);
        
        // Assert
        assertNotNull(result);
        assertEquals(mock, result);
    }
    
    @Test
    public void ensureMockIsAssignableToMockedType_NullSettings_ReturnsNull() {
        // Arrange
        @SuppressWarnings("unchecked")
        MockCreationSettings<Object> settings = mock(MockCreationSettings.class);
        Object mock = null;
        Class<Object> typeToMock = Object.class;
        
        when(settings.getTypeToMock()).thenReturn(typeToMock);
        
        // Act
        Object result = mockMaker.ensureMockIsAssignableToMockedType(settings, mock);
        
        // Assert
        assertNull(result);
    }
    
    @Test(expected = ClassCastException.class)
    public void ensureMockIsAssignableToMockedType_InvalidSettings_ThrowsClassCastException() {
        // Arrange
        @SuppressWarnings("unchecked")
        MockCreationSettings<Object> settings = mock(MockCreationSettings.class);
        Object mock = new Object();
        Class<String> typeToMock = String.class;
        
        when(settings.getTypeToMock()).thenReturn((Class<Object>)typeToMock);
        
        // Act
        Object result = mockMaker.ensureMockIsAssignableToMockedType(settings, mock);
        
        // Assert
        fail("Expected ClassCastException to be thrown");
    }
    
    @SuppressWarnings("rawtypes")
    @Test
    public void describeClass_ClassType_ReturnsDescription() {
        // Arrange
        Class type = Object.class;
        
        // Act
        String result = ByteBuddyMockMaker.describeClass(type);
        
        // Assert
        assertNotNull(result);
        assertEquals("'java.lang.Object', loaded by classloader : 'null'", result);
    }
    
    @Test
    public void describeClass_NullType_ReturnsNullDescription() {
        // Arrange
        Class<?> type = null;
        
        // Act
        String result = ByteBuddyMockMaker.describeClass(type);
        
        // Assert
        assertNotNull(result);
        assertEquals("null", result);
    }
    
    @Test
    public void describeClass_ObjectInstance_ReturnsDescription() {
        // Arrange
        Object instance = new Object();
        
        // Act
        String result = ByteBuddyMockMaker.describeClass(instance);
        
        // Assert
        assertNotNull(result);
        assertEquals("'java.lang.Object', loaded by classloader : 'java.net.FactoryURLClassLoader'", result);
    }
    
    @Test
    public void describeClass_NullObjectInstance_ReturnsNullDescription() {
        // Arrange
        Object instance = null;
        
        // Act
        String result = ByteBuddyMockMaker.describeClass(instance);
        
        // Assert
        assertNotNull(result);
        assertEquals("null", result);
    }
    
    @Test(expected = MockitoException.class)
    public void getHandler_NoMockAccess_ReturnsNull() {
        // Arrange
        Object mock = new Object();
        
        // Act
        MockHandler result = mockMaker.getHandler(mock);
        
        // Assert
        fail("Expected MockitoException to be thrown");
    }
    
    @Test
    public void getHandler_MockAccess_ReturnsMockHandler() {
        // Arrange
        MockMethodInterceptor.MockAccess mockAccess = mock(MockMethodInterceptor.MockAccess.class);
        MockHandler mockHandler = mock(MockHandler.class);
        Object mock = mockAccess;
        
        when(mockAccess.getMockitoInterceptor()).thenReturn(new MockMethodInterceptor(mockHandler, null));
        
        // Act
        MockHandler result = mockMaker.getHandler(mock);
        
        // Assert
        assertNotNull(result);
        assertEquals(mockHandler, result);
        verify(mockAccess).getMockitoInterceptor();
    }
    
    @Test
    public void resetMock_ValidSettings_ResetsMockHandler() {
        // Arrange
        MockMethodInterceptor.MockAccess mockAccess = mock(MockMethodInterceptor.MockAccess.class);
        MockHandler newHandler = mock(MockHandler.class);
        MockCreationSettings settings = mock(MockCreationSettings.class);
        Object mock = mockAccess;
        
        when(settings.getTypeToMock()).thenReturn((Class<Object>)Object.class);
        when(mockAccess.getMockitoInterceptor()).thenReturn(new MockMethodInterceptor(newHandler, settings));
        
        // Act
        mockMaker.resetMock(mock, newHandler, settings);
        
        // Assert
        verify(mockAccess).setMockitoInterceptor(any(MockMethodInterceptor.class));
    }
    
    @Test(expected = MockitoException.class)
    public void resetMock_NoMockAccess_ThrowsMockitoException() {
        // Arrange
        Object mock = new Object();
        MockHandler newHandler = mock(MockHandler.class);
        MockCreationSettings settings = mock(MockCreationSettings.class);
        
        // Act
        mockMaker.resetMock(mock, newHandler, settings);
        
        // Assert
        fail("Expected MockitoException to be thrown");
    }
    
    @Test(expected = MockitoException.class)
    public void asInternalMockHandler_NotInternalMockHandler_ThrowsMockitoException() {
        // Arrange
        MockHandler mockHandler = mock(MockHandler.class);
        
        // Act
        InternalMockHandler result = mockMaker.asInternalMockHandler(mockHandler);
        
        // Assert
        fail("Expected MockitoException to be thrown");
    }
    
    @Test
    public void asInternalMockHandler_InternalMockHandler_ReturnsInternalMockHandler() {
        // Arrange
        InternalMockHandler mockHandler = mock(InternalMockHandler.class);
        
        // Act
        InternalMockHandler result = mockMaker.asInternalMockHandler(mockHandler);
        
        // Assert
        assertNotNull(result);
        assertEquals(mockHandler, result);
    }

}
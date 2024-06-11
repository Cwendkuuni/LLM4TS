package org.mockito.internal.creation.bytebuddy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.internal.creation.instance.CachingMockBytecodeGenerator;
import org.mockito.internal.creation.instance.InstantiationException;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker byteBuddyMockMaker;
    private ClassInstantiator classInstantiator;
    private CachingMockBytecodeGenerator cachingMockBytecodeGenerator;

    @Before
    public void setup() {
        byteBuddyMockMaker = new ByteBuddyMockMaker();
        classInstantiator = mock(ClassInstantiator.class);
        cachingMockBytecodeGenerator = mock(CachingMockBytecodeGenerator.class);
    }

    @Test
    public void testCreateMock_SerializableModeAcrossClassloaders() {
        MockCreationSettings mockCreationSettings = mock(MockCreationSettings.class);
        MockHandler mockHandler = mock(MockHandler.class);
        when(mockCreationSettings.getSerializableMode()).thenReturn(SerializableMode.ACROSS_CLASSLOADERS);

        try {
            byteBuddyMockMaker.createMock(mockCreationSettings, mockHandler);
        } catch (Exception e) {
            assertEquals(MockitoException.class, e.getClass());
            assertEquals("Serialization across classloaders not yet supported with ByteBuddyMockMaker", e.getMessage());
        }
    }

    @Test
    public void testCreateMock_Normal() {
        MockCreationSettings mockCreationSettings = mock(MockCreationSettings.class);
        MockHandler mockHandler = mock(MockHandler.class);
        Class<?> mockedProxyType = mock(Class.class);
        Object mockInstance = mock(Object.class);
        ByteBuddyMockMaker byteBuddyMockMakerSpy = spy(byteBuddyMockMaker);
        when(mockCreationSettings.getSerializableMode()).thenReturn(SerializableMode.NONE);
        when(mockCreationSettings.getTypeToMock()).thenReturn(mockedProxyType);
        when(mockCreationSettings.getExtraInterfaces()).thenReturn(null);
        when(classInstantiator.instantiate(mockedProxyType)).thenReturn(mockInstance);
        when(cachingMockBytecodeGenerator.get(mockedProxyType, null)).thenReturn(mockedProxyType);
        doReturn(null).when(byteBuddyMockMakerSpy).ensureMockIsAssignableToMockedType(mockCreationSettings, mockInstance);

        Object result = byteBuddyMockMakerSpy.createMock(mockCreationSettings, mockHandler);

        assertEquals(mockInstance, result);
        verify(mockInstance, times(1)).setMockitoInterceptor(anyObject());
        verify(classInstantiator, times(1)).instantiate(mockedProxyType);
        verify(cachingMockBytecodeGenerator, times(1)).get(mockedProxyType, null);
        verify(byteBuddyMockMakerSpy, times(1)).ensureMockIsAssignableToMockedType(mockCreationSettings, mockInstance);
    }

    @Test
    public void testCreateMock_ClassCastException() {
        MockCreationSettings mockCreationSettings = mock(MockCreationSettings.class);
        MockHandler mockHandler = mock(MockHandler.class);
        Class<?> mockedProxyType = mock(Class.class);
        Object mockInstance = mock(Object.class);
        ByteBuddyMockMaker byteBuddyMockMakerSpy = spy(byteBuddyMockMaker);
        when(mockCreationSettings.getSerializableMode()).thenReturn(SerializableMode.NONE);
        when(mockCreationSettings.getTypeToMock()).thenReturn(mockedProxyType);
        when(mockCreationSettings.getExtraInterfaces()).thenReturn(null);
        when(classInstantiator.instantiate(mockedProxyType)).thenReturn(mockInstance);
        when(cachingMockBytecodeGenerator.get(mockedProxyType, null)).thenReturn(mockedProxyType);
        doThrow(ClassCastException.class).when(byteBuddyMockMakerSpy).ensureMockIsAssignableToMockedType(mockCreationSettings, mockInstance);

        try {
            byteBuddyMockMakerSpy.createMock(mockCreationSettings, mockHandler);
        } catch (Exception e) {
            assertEquals(MockitoException.class, e.getClass());
            assertEquals("ClassCastException occurred while creating the mockito mock :\n  class to mock : '"
                    + mockedProxyType.getCanonicalName() + "', loaded by classloader : '" + mockedProxyType.getClassLoader() + "'\n  created class : '"
                    + mockedProxyType.getCanonicalName() + "', loaded by classloader : '" + mockedProxyType.getClassLoader() + "'\n  proxy instance class : '"
                    + mockedProxyType.getCanonicalName() + "', loaded by classloader : '" + mockedProxyType.getClassLoader() + "'\n  instance creation by : ClassInstantiator\n\nYou might experience classloading issues, please ask the mockito mailing-list.\n", e.getMessage());
        }
    }

    @Test
    public void testCreateMock_InstantiationException() {
        MockCreationSettings mockCreationSettings = mock(MockCreationSettings.class);
        MockHandler mockHandler = mock(MockHandler.class);
        Class<?> mockedProxyType = mock(Class.class);
        Object mockInstance = mock(Object.class);
        ByteBuddyMockMaker byteBuddyMockMakerSpy = spy(byteBuddyMockMaker);
        when(mockCreationSettings.getSerializableMode()).thenReturn(SerializableMode.NONE);
        when(mockCreationSettings.getTypeToMock()).thenReturn(mockedProxyType);
        when(mockCreationSettings.getExtraInterfaces()).thenReturn(null);
        when(classInstantiator.instantiate(mockedProxyType)).thenThrow(InstantiationException.class);

        try {
            byteBuddyMockMakerSpy.createMock(mockCreationSettings, mockHandler);
        } catch (Exception e) {
            assertEquals(MockitoException.class, e.getClass());
            assertEquals("Unable to create mock instance of type '" + mockedProxyType.getSuperclass().getSimpleName() + "'", e.getMessage());
        }
    }

    @Test
    public void testEnsureMockIsAssignableToMockedType() {
        MockCreationSettings mockCreationSettings = mock(MockCreationSettings.class);
        Class<?> mockedProxyType = mock(Class.class);
        Object mockInstance = mock(Object.class);
        when(mockCreationSettings.getTypeToMock()).thenReturn(mockedProxyType);

        Object result = byteBuddyMockMaker.ensureMockIsAssignableToMockedType(mockCreationSettings, mockInstance);

        assertEquals(mockedProxyType.cast(mockInstance), result);
        verify(mockCreationSettings, times(1)).getTypeToMock();
    }

    @Test
    public void testDescribeClass() {
        Class<?> type = mock(Class.class);
        when(type.getCanonicalName()).thenReturn("CanonicalName");
        when(type.getClassLoader()).thenReturn(ClassLoader.getSystemClassLoader());

        String result = byteBuddyMockMaker.describeClass(type);

        assertEquals("'CanonicalName', loaded by classloader : 'sun.misc.Launcher$AppClassLoader@123456'", result);
        verify(type, times(1)).getCanonicalName();
        verify(type, times(1)).getClassLoader();
    }

    @Test
    public void testDescribeClass_Null() {
        Class<?> type = null;

        String result = byteBuddyMockMaker.describeClass(type);

        assertEquals("null", result);
    }

    @Test
    public void testDescribeClass_Instance() {
        Object instance = new Object();
        Class<?> type = instance.getClass();

        String result = byteBuddyMockMaker.describeClass(instance);

        assertEquals(type.getCanonicalName(), result);
    }

    @Test
    public void testDescribeClass_Instance_Null() {
        Object instance = null;

        String result = byteBuddyMockMaker.describeClass(instance);

        assertEquals("null", result);
    }

    @Test
    public void testInitializeClassInstantiator() throws Exception {
        GlobalConfiguration globalConfiguration = spy(GlobalConfiguration.class);
        Class<?> objenesisClassLoader = mock(Class.class);
        Constructor<?> usingClassCacheConstructor = mock(Constructor.class);
        when(objenesisClassLoader.getDeclaredConstructor(boolean.class)).thenReturn(usingClassCacheConstructor);
        when(usingClassCacheConstructor.newInstance(globalConfiguration.enableClassCache())).thenReturn(mock(ClassInstantiator.UsingObjenesis.class));
        PowerMockito.whenNew(GlobalConfiguration.class).withNoArguments().thenReturn(globalConfiguration);
        PowerMockito.when(Class.forName("org.mockito.internal.creation.bytebuddy.ClassInstantiator$UsingObjenesis")).thenReturn(objenesisClassLoader);

        byteBuddyMockMaker.initializeClassInstantiator();

        verify(globalConfiguration, times(1)).enableClassCache();
        verify(objenesisClassLoader, times(1)).getDeclaredConstructor(boolean.class);
        verify(usingClassCacheConstructor, times(1)).newInstance(globalConfiguration.enableClassCache());
    }

    @Test
    public void testInitializeClassInstantiator_Exception() throws Exception {
        PowerMockito.mockStatic(Class.class);
        PowerMockito.when(Class.forName(anyString())).thenThrow(Exception.class);

        try {
            byteBuddyMockMaker.initializeClassInstantiator();
        } catch (Exception e) {
            assertEquals(IllegalStateException.class, e.getClass());
            assertEquals("Mockito could not create mock: Objenesis is missing on the classpath.\nPlease add Objenesis on the classpath.\n",e.getMessage());
        }
    }

    @Test
    public void testAsInternalMockHandler_Normal() {
        MockHandler mockHandler = mock(MockHandler.class);
        InternalMockHandler result = byteBuddyMockMaker.asInternalMockHandler(mockHandler);

        assertEquals(mockHandler, result);
    }

    @Test
    public void testAsInternalMockHandler_Exception() {
        MockHandler mockHandler = mock(MockHandler.class);
        when(mockHandler instanceof InternalMockHandler).thenReturn(false);

        try {
            byteBuddyMockMaker.asInternalMockHandler(mockHandler);
        } catch (Exception e) {
            assertEquals(MockitoException.class, e.getClass());
            assertEquals("At the moment you cannot provide own implementations of MockHandler.\nPlease see the javadocs for the MockMaker interface.", e.getMessage());
        }
    }

    @Test
    public void testGetHandler() {
        Object mock = mock(Object.class);
        InternalMockHandler internalMockHandler = mock(InternalMockHandler.class);
        when(mock instanceof InternalMockHandler.MockAccess).thenReturn(true);
        when(((InternalMockHandler.MockAccess) mock).getMockitoInterceptor()).thenReturn(internalMockHandler);

        MockHandler result = byteBuddyMockMaker.getHandler(mock);

        assertEquals(internalMockHandler, result);
        verify(mock, times(1)).getMockitoInterceptor();
    }

    @Test
    public void testResetMock() {
        Object mock = mock(Object.class);
        MockHandler newHandler = mock(MockHandler.class);
        MockCreationSettings settings = mock(MockCreationSettings.class);
        ByteBuddyMockMaker byteBuddyMockMakerSpy = spy(byteBuddyMockMaker);
        InternalMockHandler internalMockHandler = mock(InternalMockHandler.class);
        when(mock instanceof InternalMockHandler.MockAccess).thenReturn(true);
        when(((InternalMockHandler.MockAccess) mock).getMockitoInterceptor()).thenReturn(internalMockHandler);

        byteBuddyMockMakerSpy.resetMock(mock, newHandler, settings);

        verify(mock, times(1)).setMockitoInterceptor(anyObject());
        verify(byteBuddyMockMakerSpy, times(1)).asInternalMockHandler(newHandler);
        verify(mock, times(1)).getMockitoInterceptor();
        verify(internalMockHandler, times(1)).getMockHandler();
    }
}
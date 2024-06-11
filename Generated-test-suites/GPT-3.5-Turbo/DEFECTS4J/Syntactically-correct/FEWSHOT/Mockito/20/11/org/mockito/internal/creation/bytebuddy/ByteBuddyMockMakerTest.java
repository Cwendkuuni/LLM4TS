package org.mockito.internal.creation.bytebuddy;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

public class ByteBuddyMockMakerTest {
    
    private ByteBuddyMockMaker mockMaker;
    private MockCreationSettings<String> settings;
    private InternalMockHandler handler;
    
    @Before
    public void setup() {
        mockMaker = new ByteBuddyMockMaker();
        settings = mock(MockCreationSettings.class);
        handler = mock(InternalMockHandler.class);
    }

    @Test(expected = MockitoException.class)
    public void testCreateMock_SerializableModeAcrossClassLoaders() {
        when(settings.getSerializableMode()).thenReturn(SerializableMode.ACROSS_CLASSLOADERS);
        
        mockMaker.createMock(settings, handler);
    }

    @Test
    public void testCreateMock_NormalMode() {
        when(settings.getSerializableMode()).thenReturn(SerializableMode.NONE);
        when(settings.getTypeToMock()).thenReturn(String.class);
        when(settings.getExtraInterfaces()).thenReturn(new Class[]{});
        
        String mock = mockMaker.createMock(settings, handler);
        
        verify(settings).getTypeToMock();
        verify(settings).getExtraInterfaces();
        verify(handler).getMockitoInterceptor();
        verify(handler, times(2)).getMockHandler();
        verify((MockMethodInterceptor.MockAccess)mock).setMockitoInterceptor(any(MockMethodInterceptor.class));
        verify(settings).getTypeToMock();
        
        assertEquals("String", mock.getClass().getSimpleName());
    }
    
    @Test(expected = MockitoException.class)
    public void testCreateMock_ExceptionThrown() {
        when(settings.getSerializableMode()).thenReturn(SerializableMode.NONE);
        when(settings.getTypeToMock()).thenReturn(null);
        
        mockMaker.createMock(settings, handler);
    }
    
    @Test
    public void testEnsureMockIsAssignableToMockedType() {
        when(settings.getTypeToMock()).thenReturn(String.class);
        String mock = mock(String.class);
        
        String result = mockMaker.ensureMockIsAssignableToMockedType(settings, mock);
        
        assertEquals(mock, result);
    }
    
    @Test
    public void testDescribeClass_ClassIsNull() {
        String result = mockMaker.describeClass(null);
        
        assertEquals("null", result);
    }
    
    @Test
    public void testDescribeClass_ClassIsNotNull() {
        String result = mockMaker.describeClass(String.class);
        
        assertEquals("'java.lang.String', loaded by classloader : 'null'", result);
    }
    
    @Test
    public void testGetHandler() {
        MockMethodInterceptor.MockAccess mock = mock(MockMethodInterceptor.MockAccess.class);
        when(handler instanceof MockMethodInterceptor.MockAccess).thenReturn(true);
        when(handler).thenReturn(mock);
        
        InternalMockHandler result = mockMaker.getHandler(handler);
        
        assertEquals(handler, result);
    }
    
    @Test
    public void testGetHandler_NoMockAccessor() {
        when(handler instanceof MockMethodInterceptor.MockAccess).thenReturn(false);
        
        InternalMockHandler result = mockMaker.getHandler(handler);
        
        assertNull(result);
    }
    
    @Test
    public void testResetMock() {
        MockMethodInterceptor.MockAccess mock = mock(MockMethodInterceptor.MockAccess.class);
        when(handler instanceof MockMethodInterceptor.MockAccess).thenReturn(true);
        when(handler).thenReturn(mock);
        when(settings.getTypeToMock()).thenReturn(String.class);
        
        mockMaker.resetMock(handler, handler, settings);
        
        verify(settings).getTypeToMock();
        verify(handler).getMockitoInterceptor();
        verify((MockMethodInterceptor.MockAccess)mock).setMockitoInterceptor(any(MockMethodInterceptor.class));
    }
    
    @Test(expected = MockitoException.class)
    public void testResetMock_InvalidHandler() {
        when(handler instanceof MockMethodInterceptor.MockAccess).thenReturn(false);
        
        mockMaker.resetMock(handler, handler, settings);
    }
    
    @Test
    public void testInitializeClassInstantiator_UsingObjenesis() throws Exception {
        GlobalConfiguration globalConfiguration = mock(GlobalConfiguration.class);
        ByteBuddyMockMaker mockMaker = spy(new ByteBuddyMockMaker());
        Class<?> objenesisClassLoader = Class.forName("org.mockito.internal.creation.bytebuddy.ClassInstantiator$UsingObjenesis");
        Constructor<?> usingClassCacheConstructor = objenesisClassLoader.getDeclaredConstructor(boolean.class);
        Object classInstantiatorObj = mock(ClassInstantiator.class);
        when(globalConfiguration.enableClassCache()).thenReturn(true);
        when(objenesisClassLoader.getDeclaredConstructor(boolean.class)).thenReturn(usingClassCacheConstructor);
        when(usingClassCacheConstructor.newInstance(globalConfiguration.enableClassCache())).thenReturn(classInstantiatorObj);
        doReturn(globalConfiguration).when(mockMaker).initializeGlobalConfiguration();
        
        ClassInstantiator result = mockMaker.initializeClassInstantiator();
        
        verify(globalConfiguration).enableClassCache();
        verify(mockMaker).initializeGlobalConfiguration();
        verify(objenesisClassLoader).getDeclaredConstructor(boolean.class);
        verify(usingClassCacheConstructor).newInstance(globalConfiguration.enableClassCache());
        assertEquals(classInstantiatorObj, result);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testInitializeClassInstantiator_ExceptionThrown() throws Exception {
        GlobalConfiguration globalConfiguration = mock(GlobalConfiguration.class);
        ByteBuddyMockMaker mockMaker = spy(new ByteBuddyMockMaker());
        Class<?> objenesisClassLoader = Class.forName("org.mockito.internal.creation.bytebuddy.ClassInstantiator$UsingObjenesis");
        Constructor<?> usingClassCacheConstructor = objenesisClassLoader.getDeclaredConstructor(boolean.class);
        when(globalConfiguration.enableClassCache()).thenReturn(true);
        when(objenesisClassLoader.getDeclaredConstructor(boolean.class)).thenReturn(usingClassCacheConstructor);
        doReturn(globalConfiguration).when(mockMaker).initializeGlobalConfiguration();
        
        mockMaker.initializeClassInstantiator();
    }
    
    @Test(expected = MockitoException.class)
    public void testAsInternalMockHandler_ExceptionThrown() {
        InternalMockHandler handler = mock(InternalMockHandler.class);
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        
        mockMaker.asInternalMockHandler(handler);
    }
}
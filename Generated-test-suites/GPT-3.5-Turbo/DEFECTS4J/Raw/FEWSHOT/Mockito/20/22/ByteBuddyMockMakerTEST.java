package org.mockito.internal.creation.bytebuddy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.*;
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

    @Test
    public void testCreateMock() {
        MockCreationSettings<Object> settings = mock(MockCreationSettings.class);
        Object mockInstance = mockMaker.createMock(settings, mock(MockHandler.class));
        assertNotNull(mockInstance);
    }
    
    @Test(expected = MockitoException.class)
    public void testCreateMockWithSerializableModeAcrossClassLoaders() {
        MockCreationSettings<Object> settings = mock(MockCreationSettings.class);
        when(settings.getSerializableMode()).thenReturn(SerializableMode.ACROSS_CLASSLOADERS);
        
        mockMaker.createMock(settings, mock(MockHandler.class));
    }
    
    @Test(expected = MockitoException.class)
    public void testCreateMockWithClassCastException() {
        MockCreationSettings<?> settings = mock(MockCreationSettings.class);
        when(settings.getTypeToMock()).thenReturn((Class) String.class);
        when(settings.getExtraInterfaces()).thenReturn(new Class[]{List.class});
        
        mockMaker.createMock(settings, mock(MockHandler.class));
    }
    
    @Test(expected = MockitoException.class)
    public void testCreateMockWithInstantiationException() {
        MockCreationSettings<?> settings = mock(MockCreationSettings.class);
        when(settings.getTypeToMock()).thenReturn((Class) List.class); // non-serializable class
        
        mockMaker.createMock(settings, mock(MockHandler.class));
    }
    
    @Test
    public void testEnsureMockIsAssignableToMockedType() {
        MockCreationSettings<Object> settings = mock(MockCreationSettings.class);
        when(settings.getTypeToMock()).thenReturn(Object.class);
        Object mock = mockMaker.createMock(settings, mock(MockHandler.class));
        
        assertEquals(Object.class, mock.getClass());
    }
    
    @Test
    public void testGetHandler() {
        Object mock = mock(Object.class);
        InternalMockHandler handler = mock(mockMaker.getHandler(mock).getClass());
        when(mock).thenReturn(handler);
        
        assertEquals(handler, mockMaker.getHandler(mock));
    }
    
    @Test
    public void testGetHandlerWithNonMockInstance() {
        Object instance = new Object();
        assertNull(mockMaker.getHandler(instance));
    }
    
    @Test
    public void testResetMock() {
        Object mock = mock(Object.class);
        MockHandler newHandler = mock(MockHandler.class);
        MockCreationSettings<Object> settings = mock(MockCreationSettings.class);
        when(settings.getSerializableMode()).thenReturn(SerializableMode.ACROSS_CLASSLOADERS);
        
        mockMaker.resetMock(mock, newHandler, settings);
        
        verify(mock, times(1)).setMockitoInterceptor(any(MockMethodInterceptor.class));
    }
    
    @Test(expected = MockitoException.class)
    public void testResetMockWithNonInternalMockHandler() {
        Object mock = mock(Object.class);
        MockCreationSettings<Object> settings = mock(MockCreationSettings.class);
        
        mockMaker.resetMock(mock, mock(MockHandler.class), settings);
    }
    
    @Test
    public void testInitializeClassInstantiator() {
        ClassInstantiator classInstantiator = mockMaker.initializeClassInstantiator();
        
        assertNotNull(classInstantiator);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testInitializeClassInstantiatorWithMissingObjenesis() {
        try {
            Class<?> objenesisClassLoader = mock(Class.class);
            when(objenesisClassLoader.getDeclaredConstructor(boolean.class)).thenThrow(NoSuchMethodException.class);
            when(Class.forName("org.mockito.internal.creation.bytebuddy.ClassInstantiator$UsingObjenesis")).thenReturn(objenesisClassLoader);
        } catch (ClassNotFoundException ignored) {
        }
        
        mockMaker.initializeClassInstantiator();
    }
    
    @Test
    public void testAsInternalMockHandler() {
        MockHandler handler = mock(MockHandler.class);
        InternalMockHandler internalHandler = mockMaker.asInternalMockHandler(handler);
        
        assertNotNull(internalHandler);
    }
    
    @Test(expected = MockitoException.class)
    public void testAsInternalMockHandlerWithNonInternalMockHandler() {
        MockHandler handler = mock(MockHandler.class);
        
        mockMaker.asInternalMockHandler(handler);
    }

}
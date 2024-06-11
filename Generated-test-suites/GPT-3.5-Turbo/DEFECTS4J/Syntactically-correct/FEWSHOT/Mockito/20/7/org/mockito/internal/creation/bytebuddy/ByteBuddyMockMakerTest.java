package org.mockito.internal.creation.bytebuddy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;
import java.lang.reflect.Constructor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.creation.instance.InstantiationException;
import org.mockito.internal.configuration.InjectMocks;
import org.mockito.internal.util.reflection.FieldReader;

import org.junit.Before;
import org.junit.Test;

public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker byteBuddyMockMaker;

    @Before
    public void setUp() {
        byteBuddyMockMaker = new ByteBuddyMockMaker();
    }

    @Test
    public void testCreateMock() throws InstantiationException {
        // Create mock settings
        MockCreationSettings<T> mockSettings = Mockito.mock(MockCreationSettings.class);
        MockHandler mockHandler = Mockito.mock(MockHandler.class);
        SerializableMode serializableMode = Mockito.mock(SerializableMode.class);
        Mockito.when(mockSettings.getSerializableMode()).thenReturn(serializableMode);
        Class<T> mockedProxyType = Mockito.mock(Class.class);
        Class<T> typeToMock = Mockito.mock(Class.class);
        InvocationHandler invocationHandler = Mockito.mock(InvocationHandler.class);
        Mockito.when(mockSettings.getTypeToMock()).thenReturn(typeToMock);
        Mockito.when(mockSettings.getExtraInterfaces()).thenReturn(new Class[0]);
        Mockito.when(typeToMock.cast(Mockito.any())).thenReturn(typeToMock);
        Mockito.when(cachingMockBytecodeGenerator.get(Mockito.any(), Mockito.any())).thenReturn(mockedProxyType);
        
        // Mock the classInstantiator and classInstantiator.instantiate() method
        ClassInstantiator classInstantiator = Mockito.mock(ClassInstantiator.class);
        T mockInstance = Mockito.mock(T.class);
        Mockito.when(classInstantiator.instantiate(Mockito.any())).thenReturn(mockInstance);
        
        // Mock the MockMethodInterceptor.MockAccess and setMockitoInterceptor method
        MockMethodInterceptor.MockAccess mockAccess = Mockito.mock(MockMethodInterceptor.MockAccess.class);
        Mockito.when(mockInstance).thenReturn(mockAccess);
        Mockito.when(mockAccess.setMockitoInterceptor(Mockito.any())).thenReturn(null);
        
        // Call the createMock() method
        T result = byteBuddyMockMaker.createMock(mockSettings, mockHandler);
        
        // Verify the interactions
        Mockito.verify(mockSettings, Mockito.times(1)).getSerializableMode();
        Mockito.verify(cachingMockBytecodeGenerator, Mockito.times(1)).get(Mockito.any(), Mockito.any());
        Mockito.verify(mockSettings, Mockito.times(1)).getTypeToMock();
        Mockito.verify(mockSettings, Mockito.times(1)).getExtraInterfaces();
        Mockito.verify(classInstantiator, Mockito.times(1)).instantiate(Mockito.any());
        Mockito.verify(mockAccess, Mockito.times(1)).setMockitoInterceptor(Mockito.any());
        Mockito.verify(typeToMock.cast(Mockito.any()), Mockito.times(1));
        Mockito.verify(Mockito.nullable(T.class), Mockito.times(1));
        Mockito.verify(typeToMock.cast(Mockito.any()), Mockito.times(1));

        // Assert the result
        assertEquals(mockInstance, result);
    }

    @Test
    public void testCreateMockWithSerializableModeAcrossClassloaders() {
        // Create mock settings
        MockCreationSettings<T> mockSettings = Mockito.mock(MockCreationSettings.class);
        MockHandler mockHandler = Mockito.mock(MockHandler.class);
        SerializableMode serializableMode = Mockito.mock(SerializableMode.class);
        Mockito.when(mockSettings.getSerializableMode()).thenReturn(serializableMode);
        Mockito.when(serializableMode == SerializableMode.ACROSS_CLASSLOADERS).thenReturn(true);

        // Call the createMock() method
        Exception exception = null;
        try {
            byteBuddyMockMaker.createMock(mockSettings, mockHandler);
        } catch (MockitoException e) {
            exception = e;
        }

        // Assert the exception
        assertTrue(exception.getMessage().contains("Serialization across classloaders not yet supported with ByteBuddyMockMaker"));
    }

    @Test
    public void testEnsureMockIsAssignableToMockedType() {
        // Create mock settings
        MockCreationSettings<T> mockSettings = Mockito.mock(MockCreationSettings.class);
        Class<T> typeToMock = Mockito.mock(Class.class);
        T mockInstance = Mockito.mock(T.class);

        // Call the ensureMockIsAssignableToMockedType() method
        T result = byteBuddyMockMaker.ensureMockIsAssignableToMockedType(mockSettings, mockInstance);

        // Verify the interactions
        Mockito.verify(typeToMock.cast(Mockito.any()), Mockito.times(1));

        // Assert the result
        assertEquals(mockInstance, result);
    }

    @Test
    public void testDescribeClassWithTypeNull() {
        // Call the describeClass() method
        String result = byteBuddyMockMaker.describeClass(null);

        // Assert the result
        assertEquals("null", result);
    }

    @Test
    public void testDescribeClassWithInstanceNull() {
        // Call the describeClass() method
        String result = byteBuddyMockMaker.describeClass(null);

        // Assert the result
        assertEquals("null", result);
    }

    @Test
    public void testGetHandlerWithNonMockMethodInterceptorMockAccess() {
        // Test with non-MockMethodInterceptor.MockAccess mock instance
        Object mock = Mockito.mock(Object.class);

        // Call the getHandler() method
        MockHandler result = byteBuddyMockMaker.getHandler(mock);

        // Assert the result
        assertNull(result);
    }

    @Test
    public void testGetHandlerWithMockMethodInterceptorMockAccess() {
        // Test with MockMethodInterceptor.MockAccess mock instance
        MockMethodInterceptor.MockAccess mockAccess = Mockito.mock(MockMethodInterceptor.MockAccess.class);
        Mockito.when(mockAccess.getMockitoInterceptor().getMockHandler()).thenReturn(Mockito.mock(MockHandler.class));
        Object mock = mockAccess;

        // Call the getHandler() method
        MockHandler result = byteBuddyMockMaker.getHandler(mock);

        // Assert the result
        assertNotNull(result);
    }

    @Test
    public void testResetMock() {
        // Create mock settings
        MockCreationSettings<T> mockSettings = Mockito.mock(MockCreationSettings.class);
        MockHandler newHandler = Mockito.mock(MockHandler.class);
        InvocationHandler invocationHandler = Mockito.mock(InvocationHandler.class);

        // Mock the MockMethodInterceptor.MockAccess and setMockitoInterceptor method
        MockMethodInterceptor.MockAccess mockAccess = Mockito.mock(MockMethodInterceptor.MockAccess.class);
        Mockito.when(mockAccess.setMockitoInterceptor(Mockito.any())).thenReturn(null);
        
        // Call the resetMock() method
        byteBuddyMockMaker.resetMock(mockAccess, newHandler, mockSettings);

        // Verify the interactions
        Mockito.verify(mockAccess, Mockito.times(1)).setMockitoInterceptor(Mockito.any());
    }

    @Test
    public void testInitializeClassInstantiatorWithObjenesis() throws Exception {
        // Mock the forName(), getDeclaredConstructor() and newInstance() methods
        ClassLoader objenesisClassLoader = Mockito.mock(ClassLoader.class);
        Class<?> objenesisClass = Mockito.mock(Class.class);
        Constructor<?> usingClassCacheConstructor = Mockito.mock(Constructor.class);
        ClassInstantiator classInstantiator = Mockito.mock(ClassInstantiator.class);
        Mockito.when(Class.forName("org.mockito.internal.creation.bytebuddy.ClassInstantiator$UsingObjenesis", false, objenesisClassLoader)).thenReturn(objenesisClass);
        Mockito.when(objenesisClass.getDeclaredConstructor(boolean.class)).thenReturn(usingClassCacheConstructor);
        Mockito.when(usingClassCacheConstructor.newInstance(Mockito.any())).thenReturn(classInstantiator);
        
        // Call the initializeClassInstantiator() method
        ClassInstantiator result = byteBuddyMockMaker.initializeClassInstantiator();

        // Verify the interactions
        Mockito.verify(Class.forName("org.mockito.internal.creation.bytebuddy.ClassInstantiator$UsingObjenesis", false, objenesisClassLoader), Mockito.times(1));
        Mockito.verify(objenesisClass.getDeclaredConstructor(boolean.class), Mockito.times(1));
        Mockito.verify(usingClassCacheConstructor, Mockito.times(1)).newInstance(Mockito.any());

        // Assert the result
        assertEquals(classInstantiator, result);
    }

    @Test(expected = IllegalStateException.class)
    public void testInitializeClassInstantiatorWithoutObjenesis() throws Exception {
        // Mock the forName() and getDeclaredConstructor() methods
        ClassLoader objenesisClassLoader = Mockito.mock(ClassLoader.class);
        Mockito.when(Class.forName("org.mockito.internal.creation.bytebuddy.ClassInstantiator$UsingObjenesis", false, objenesisClassLoader)).thenThrow(new ClassNotFoundException());

        // Call the initializeClassInstantiator() method
        byteBuddyMockMaker.initializeClassInstantiator();
    }

    @Test(expected = MockitoException.class)
    public void testAsInternalMockHandlerWithNonInternalMockHandler() {
        // Test with non-InternalMockHandler mock instance
        MockHandler handler = Mockito.mock(MockHandler.class);

        // Call the asInternalMockHandler() method
        byteBuddyMockMaker.asInternalMockHandler(handler);
    }

    @Test
    public void testAsInternalMockHandlerWithInternalMockHandler() {
        // Test with InternalMockHandler mock instance
        InternalMockHandler handler = Mockito.mock(InternalMockHandler.class);

        // Call the asInternalMockHandler() method
        InternalMockHandler result = byteBuddyMockMaker.asInternalMockHandler(handler);

        // Assert the result
        assertEquals(handler, result);
    }
}
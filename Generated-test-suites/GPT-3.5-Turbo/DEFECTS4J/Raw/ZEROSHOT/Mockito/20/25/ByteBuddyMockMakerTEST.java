import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.bytebuddy.*;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

import java.lang.reflect.Field;
import java.lang.reflect.Constructor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker byteBuddyMockMaker;

    @Before
    public void setUp() {
        byteBuddyMockMaker = new ByteBuddyMockMaker();
    }

    @Test(expected = MockitoException.class)
    public void testCreateMock_SerializationAcrossClassloaders() {
        MockCreationSettings mockCreationSettings = mock(MockCreationSettings.class);
        MockHandler mockHandler = mock(MockHandler.class);

        when(mockCreationSettings.getSerializableMode()).thenReturn(SerializableMode.ACROSS_CLASSLOADERS);

        byteBuddyMockMaker.createMock(mockCreationSettings, mockHandler);
    }

    @Test
    public void testCreateMock_ValidSettings() {
        MockCreationSettings mockCreationSettings = mock(MockCreationSettings.class);
        MockHandler mockHandler = mock(MockHandler.class);
        Class mockType = mock(Class.class);
        Class mockedProxyType = mock(Class.class);
        Object mockInstance = new Object();

        when(mockCreationSettings.getSerializableMode()).thenReturn(SerializableMode.NONE);
        when(mockCreationSettings.getTypeToMock()).thenReturn(mockType);
        when(mockCreationSettings.getExtraInterfaces()).thenReturn(new Class[] {});
        when(mockedProxyType.cast(any())).thenReturn(mockInstance);
        when(mockHandler.getClass()).thenReturn(MockMethodInterceptor.MockAccess.class);

        try {
            Field cachingMockBytecodeGeneratorField = ByteBuddyMockMaker.class.getDeclaredField("cachingMockBytecodeGenerator");
            cachingMockBytecodeGeneratorField.setAccessible(true);
            cachingMockBytecodeGeneratorField.set(byteBuddyMockMaker, mockedProxyType);

            Field classInstantiatorField = ByteBuddyMockMaker.class.getDeclaredField("classInstantiator");
            classInstantiatorField.setAccessible(true);
            classInstantiatorField.set(byteBuddyMockMaker, mock(ClassInstantiator.class));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Object result = byteBuddyMockMaker.createMock(mockCreationSettings, mockHandler);

        assertNotNull(result);
        assertTrue(result instanceof MockMethodInterceptor.MockAccess);
    }

    @Test(expected = MockitoException.class)
    public void testCreateMock_ClassCastException() {
        MockCreationSettings mockCreationSettings = mock(MockCreationSettings.class);
        MockHandler mockHandler = mock(MockHandler.class);
        Class mockedProxyType = mock(Class.class);
        Object mockInstance = new Object();

        when(mockCreationSettings.getSerializableMode()).thenReturn(SerializableMode.NONE);
        when(mockCreationSettings.getTypeToMock()).thenReturn(mock(Class.class));
        when(mockCreationSettings.getExtraInterfaces()).thenReturn(new Class[] {});
        when(mockedProxyType.cast(any())).thenThrow(ClassCastException.class);

        try {
            Field cachingMockBytecodeGeneratorField = ByteBuddyMockMaker.class.getDeclaredField("cachingMockBytecodeGenerator");
            cachingMockBytecodeGeneratorField.setAccessible(true);
            cachingMockBytecodeGeneratorField.set(byteBuddyMockMaker, mockedProxyType);

            Field classInstantiatorField = ByteBuddyMockMaker.class.getDeclaredField("classInstantiator");
            classInstantiatorField.setAccessible(true);
            classInstantiatorField.set(byteBuddyMockMaker, mock(ClassInstantiator.class));
        } catch (Exception e) {
            e.printStackTrace();
        }

        byteBuddyMockMaker.createMock(mockCreationSettings, mockHandler);
    }

    @Test(expected = MockitoException.class)
    public void testCreateMock_InstantiationException() {
        MockCreationSettings mockCreationSettings = mock(MockCreationSettings.class);
        MockHandler mockHandler = mock(MockHandler.class);
        Class mockType = mock(Class.class);
        Class mockedProxyType = mock(Class.class);
        Object mockInstance = new Object();

        when(mockCreationSettings.getSerializableMode()).thenReturn(SerializableMode.NONE);
        when(mockCreationSettings.getTypeToMock()).thenReturn(mockType);
        when(mockCreationSettings.getExtraInterfaces()).thenReturn(new Class[] {});
        when(mockedProxyType.cast(any())).thenReturn(mockInstance);

        try {
            Field cachingMockBytecodeGeneratorField = ByteBuddyMockMaker.class.getDeclaredField("cachingMockBytecodeGenerator");
            cachingMockBytecodeGeneratorField.setAccessible(true);
            cachingMockBytecodeGeneratorField.set(byteBuddyMockMaker, mockedProxyType);

            Field classInstantiatorField = ByteBuddyMockMaker.class.getDeclaredField("classInstantiator");
            classInstantiatorField.setAccessible(true);
            classInstantiatorField.set(byteBuddyMockMaker, mock(ClassInstantiator.class));
        } catch (Exception e) {
            e.printStackTrace();
        }

        byteBuddyMockMaker.createMock(mockCreationSettings, mockHandler);
    }

    @Test
    public void testEnsureMockIsAssignableToMockedType() {
        MockCreationSettings mockCreationSettings = mock(MockCreationSettings.class);
        Object mock = mock(Object.class);

        when(mockCreationSettings.getTypeToMock()).thenReturn(Object.class);

        Object result = byteBuddyMockMaker.ensureMockIsAssignableToMockedType(mockCreationSettings, mock);

        assertNotNull(result);
        assertEquals(Object.class, result.getClass());
    }

    @Test
    public void testGetHandler_ValidMock() {
        MockHandler mockHandler = mock(MockHandler.class);
        MockMethodInterceptor.MockAccess mock = mock(MockMethodInterceptor.MockAccess.class);
        mock.setMockitoInterceptor(new MockMethodInterceptor(mockHandler, mock(MockCreationSettings.class)));

        MockHandler result = byteBuddyMockMaker.getHandler(mock);

        assertNotNull(result);
        assertEquals(mockHandler, result);
    }

    @Test
    public void testGetHandler_InvalidMock() {
        Object mock = mock(Object.class);

        MockHandler result = byteBuddyMockMaker.getHandler(mock);

        assertNull(result);
    }

    @Test
    public void testResetMock() {
        MockHandler newHandler = mock(MockHandler.class);
        MockMethodInterceptor.MockAccess mock = mock(MockMethodInterceptor.MockAccess.class);
        MockCreationSettings mockCreationSettings = mock(MockCreationSettings.class);
        byteBuddyMockMaker.resetMock(mock, newHandler, mockCreationSettings);

        assertNotNull(mock.getMockitoInterceptor());
        assertEquals(newHandler, mock.getMockitoInterceptor().getMockHandler());
    }

    @Test(expected = MockitoException.class)
    public void testResetMock_InvalidHandler() {
        MockHandler newHandler = mock(MockHandler.class);
        MockMethodInterceptor.MockAccess mock = mock(MockMethodInterceptor.MockAccess.class);
        MockCreationSettings mockCreationSettings = mock(MockCreationSettings.class);
        byteBuddyMockMaker.resetMock(mock, newHandler, mockCreationSettings);
    }

    @Test
    public void testInitializeClassInstantiator_Success() {
        GlobalConfiguration globalConfiguration = mock(GlobalConfiguration.class);
        when(globalConfiguration.enableClassCache()).thenReturn(true);

        Constructor constructor = mock(Constructor.class);
        when(constructor.newInstance(true)).thenReturn(mock(ClassInstantiator.class));

        try {
            Class objenesisClassLoader = mock(Class.class);
            when(Class.forName("org.mockito.internal.creation.bytebuddy.ClassInstantiator$UsingObjenesis")).thenReturn(objenesisClassLoader);
            when(objenesisClassLoader.getDeclaredConstructor(boolean.class)).thenReturn(constructor);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassInstantiator result = ByteBuddyMockMaker.initializeClassInstantiator();

        assertNotNull(result);
    }

    @Test(expected = IllegalStateException.class)
    public void testInitializeClassInstantiator_Exception() throws Exception {
        Class objenesisClassLoader = mock(Class.class);
        when(Class.forName("org.mockito.internal.creation.bytebuddy.ClassInstantiator$UsingObjenesis")).thenThrow(ClassNotFoundException.class);

        ByteBuddyMockMaker.initializeClassInstantiator();
    }

    @Test
    public void testAsInternalMockHandler_ValidHandler() {
        InternalMockHandler handler = mock(InternalMockHandler.class);
        MockHandler result = ByteBuddyMockMaker.asInternalMockHandler(handler);

        assertNotNull(result);
        assertEquals(handler, result);
    }

    @Test(expected = MockitoException.class)
    public void testAsInternalMockHandler_InvalidHandler() {
        MockHandler handler = mock(MockHandler.class);
        ByteBuddyMockMaker.asInternalMockHandler(handler);
    }
}
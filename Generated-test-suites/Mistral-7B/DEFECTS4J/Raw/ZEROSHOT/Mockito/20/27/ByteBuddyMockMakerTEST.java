package org.mockito.internal.creation.bytebuddy;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.*;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

public class ByteBuddyMockMakerTest {

    @Test
    public void testCreateMock() {
        MockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<String> settings = MockCreationSettings.builder()
                .forClass(String.class)
                .serializableMode(SerializableMode.SERIAL)
                .build();
        MockHandler handler = mock(MockHandler.class);

        try {
            String mockedProxyType = byteBuddyMockMaker.createMock(settings, handler).getClass().getSuperclass();
            assertEquals("java.lang.String", mockedProxyType.getName());
            assertEquals(SerializableMode.SERIAL, settings.getSerializableMode());
            assertEquals(handler, byteBuddyMockMaker.getHandler(byteBuddyMockMaker.createMock(settings, handler)));

            // Test resetMock method
            MockHandler newHandler = mock(MockHandler.class);
            byteBuddyMockMaker.resetMock(byteBuddyMockMaker.createMock(settings, handler), newHandler, settings);
            assertEquals(newHandler, byteBuddyMockMaker.getHandler(byteBuddyMockMaker.createMock(settings, handler)));

        } catch (MockitoException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockSerializableAcrossClassloaders() {
        MockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<String> settings = MockCreationSettings.builder()
                .forClass(String.class)
                .serializableMode(SerializableMode.ACROSS_CLASSLOADERS)
                .build();
        MockHandler handler = mock(MockHandler.class);
        byteBuddyMockMaker.createMock(settings, handler);
    }

    @Test
    public void testClassCastException() {
        MockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<Integer> settings = MockCreationSettings.builder()
                .forClass(String.class)
                .serializableMode(SerializableMode.SERIAL)
                .build();
        MockHandler handler = mock(MockHandler.class);

        try {
            byteBuddyMockMaker.createMock(settings, handler);
            fail("Expected ClassCastException");
        } catch (ClassCastException e) {
            assertEquals("ClassCastException occurred while creating the mockito mock :", e.getMessage().startsWith("ClassCastException occurred while creating the mockito mock :"));
        }
    }

    @Test
    public void testInstantiationException() {
        MockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<Object> settings = MockCreationSettings.builder()
                .forClass(UnsupportedOperationException.class)
                .serializableMode(SerializableMode.SERIAL)
                .build();
        MockHandler handler = mock(MockHandler.class);

        try {
            byteBuddyMockMaker.createMock(settings, handler);
            fail("Expected InstantiationException");
        } catch (MockitoException e) {
            MockitoException cause = e.getCause();
            assertEquals("Unable to create mock instance of type 'java.lang.UnsupportedOperationException'", cause.getMessage());
        }
    }

    @Test
    public void testDescribeClass() {
        assertEquals("java.lang.String, loaded by classloader : 'sun.misc.Launcher$AppClassLoader@18b4aac2'", describeClass(byteBuddyMockMaker.createMock(MockCreationSettings.builder()
                .forClass(String.class)
                .serializableMode(SerializableMode.SERIAL)
                .build())));
        assertEquals("java.lang.Object, loaded by classloader : 'sun.misc.Launcher$AppClassLoader@18b4aac2'", describeClass(new Object()));
    }

    @Test
    public void testHandlerGet() {
        MockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<MockHandler> settings = MockCreationSettings.builder()
                .forClass(MockHandler.class)
                .serializableMode(SerializableMode.SERIAL)
                .build();
        MockHandler handler = byteBuddyMockMaker.createMock(settings, mock(InternalMockHandler.class));
        assertEquals(mock(InternalMockHandler.class), byteBuddyMockMaker.getHandler(handler));
    }

    @Test
    public void testHandlerReset() {
        MockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<MockHandler> settings = MockCreationSettings.builder()
                .forClass(MockHandler.class)
                .serializableMode(SerializableMode.SERIAL)
                .build();
        MockHandler handler = byteBuddyMockMaker.createMock(settings, mock(InternalMockHandler.class));
        InternalMockHandler internalMockHandler = mock(InternalMockHandler.class);
        byteBuddyMockMaker.resetMock(handler, internalMockHandler, settings);
        assertEquals(internalMockHandler, byteBuddyMockMaker.getHandler(handler));
    }

    @Test
    public void testInitializeClassInstantiator() {
        try {
            Class<?> objenesisClassLoader = Class.forName("org.mockito.internal.creation.bytebuddy.ClassInstantiator$UsingObjenesis");
            Constructor<?> usingClassCacheConstructor = objenesisClassLoader.getDeclaredConstructor(boolean.class);
            Object instance = usingClassCacheConstructor.newInstance(true);
            assertEquals(ClassInstantiator.class, instance.getClass());
        } catch (Throwable throwable) {
            fail("Unexpected exception: " + throwable.getMessage());
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testMissingObjenesis() {
        MockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        try {
            byteBuddyMockMaker.createMock(MockCreationSettings.builder()
                    .forClass(String.class)
                    .serializableMode(SerializableMode.SERIAL)
                    .build(), mock(MockHandler.class));
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            assertEquals("Mockito could not create mock: Objenesis is missing on the classpath.", e.getMessage());
        }
    }

    private String describeClass(Object instance) {
        return ByteBuddyMockMaker.describeClass(instance);
    }

}
package org.mockito.internal.creation.bytebuddy;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.internal.util.StringJoiner.join;
import static org.mockito.internal.util.reflection.Whitebox.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.internal.creation.instance.CachingMockBytecodeGenerator;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

public class ByteBuddyMockMakerTest {

    @Test
    public void testCreateMock() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Class<Object> mockableClass = Object.class;

        MockCreationSettings<Object> settings = new MockCreationSettings.Builder()
                .forClass(mockableClass)
                .serializable()
                .build();

        MockHandler handler = mock(MockHandler.class);

        try {
            Object mock = mockMaker.createMock(settings, handler);
            assertNotNull(mock);

            Type mockType = mockMaker.getHandler(mock).getMockType();
            assertEquals(mockableClass, mockType);
        } catch (MockitoException ex) {
            // expected exception
        }
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockSerializationAcrossClassloaders() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Class<Object> mockableClass = Object.class;

        MockCreationSettings<Object> settings = new MockCreationSettings.Builder()
                .forClass(mockableClass)
                .serializableAcrossClassLoaders()
                .build();

        MockHandler handler = mock(MockHandler.class);

        mockMaker.createMock(settings, handler);
    }

    @Test
    public void testGetHandler() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();

        Class<Object> mockableClass = Object.class;
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder()
                .forClass(mockableClass)
                .build();

        Object mock = createMock(mockableClass);
        MockHandler handler = mockMaker.getHandler(mock);

        assertNotNull(handler);
    }

    @Test
    public void testResetMock() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();

        Class<Object> mockableClass = Object.class;
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder()
                .forClass(mockableClass)
                .build();

        Object mock = createMock(mockableClass);

        MockHandler handler = mock(MockHandler.class);

        mockMaker.resetMock(mock, handler, settings);

        MockHandler oldHandler = mockMaker.getHandler(mock);
        assertNull(oldHandler);
        MockHandler newHandler = mockMaker.getHandler(mock);
        assertEquals(handler, newHandler);
    }

    @Test
    public void testInitializeClassInstantiator() throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, SecurityException {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Constructor<ClassInstantiator> constructor = Class.forName("org.mockito.internal.creation.bytebuddy.ClassInstantiator$UsingObjenesis")
                .getDeclaredConstructor(boolean.class);

        ClassInstantiator classInstantiator = mockMaker.initializeClassInstantiator();
        assertEquals(ClassInstantiator.class, classInstantiator.getClass());
        assertNotNull(Whitebox.getInternalState(classInstantiator, "classCache"));
        assertTrue(Whitebox.getBoolean(classInstantiator, "enableClassCache"));
        assertEquals(constructor.newInstance(true), Whitebox.getInternalState(classMaker, "classInstantiator"));
    }

    @Test(expected = IllegalStateException.class)
    public void testInitializeClassInstantiatorMissingObjenesis() throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, SecurityException {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        mockMaker.initializeClassInstantiator();
    }

    private Object createMock(Class<Object> mockableClass) {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder()
                .forClass(mockableClass)
                .build();
        MockHandler handler = mock(MockHandler.class);

        return mockMaker.createMock(settings, handler);
    }
}
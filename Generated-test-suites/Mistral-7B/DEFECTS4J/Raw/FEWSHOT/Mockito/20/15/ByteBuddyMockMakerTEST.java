package org.mockito.internal.creation.bytebuddy;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockHandler;
import org.mockito.Mockito;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.MockCreationSettings;
import org.mockito.internal.creation.instance.MockMethodInterceptor;
import org.mockito.invocation.MockHandlerStub;
import org.mockito.plugins.MockMaker;

public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker mockMaker;
    private CachingMockBytecodeGenerator cachingMockBytecodeGenerator;
    private ClassInstantiator classInstantiator;
    private InternalMockHandler mockHandler;

    @Before
    public void setup() {
        mockHandler = mock(InternalMockHandler.class);
        mockMaker = new ByteBuddyMockMaker();
        cachingMockBytecodeGenerator = mockMaker.new CachingMockBytecodeGenerator();
        classInstantiator = mockMaker.new ClassInstantiator();
    }

    @Test
    public void testCreateMock_serializableModeNotSupported() {
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder<Object>()
                .forClass(String.class)
                .withSerializableMode(SerializableMode.ACROSS_CLASSLOADERS)
                .build();

        assertThrows(MockitoException.class, () -> mockMaker.createMock(settings, mockHandler));
    }

    @Test
    public void testCreateMock_success() throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        Type type = String[].class;
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder<Object>()
                .forClass(String[].class)
                .build();

        Class<? extends Object> mockedProxyType = mockMaker.cachingMockBytecodeGenerator.get(String[].class, new Class[0]);
        Object mockInstance = mockMaker.classInstantiator.instantiate(mockedProxyType);

        MockMethodInterceptor mockAccess = (MockMethodInterceptor.MockAccess) mockInstance;
        MockHandler handler = mockHandler;
        Object result = mockMaker.createMock(settings, handler);

        assertSame(mockInstance, result);
    }

    @Test
    public void testGetHandler() {
        Object mock = mock(String[].class);
        MockHandler handler = mockHandler;
        mockMaker.resetMock(mock, handler, new MockCreationSettings.Builder<Object>().forClass(String[].class).build());

        MockHandler returnedHandler = mockMaker.getHandler(mock);
        assertTrue(returnedHandler == handler);
    }

    @Test
    public void testResetMock() {
        Object mock = mock(String[].class);
        MockHandler handler = mockHandler;
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder<Object>().forClass(String[].class).build();

        mockMaker.resetMock(mock, handler, settings);
        MockHandler returnedHandler = mockMaker.getHandler(mock);

        assertTrue(returnedHandler == handler);
    }

    @Test
    public void testInitializeClassInstantiator() {
        assertFalse(mockMaker.classInstantiator instanceof ClassInstantiator$UsingObjenesis);

        try {
            mockMaker.initializeClassInstantiator();
        } catch (Throwable throwable) {
            assertThrows(IllegalStateException.class, () -> mockMaker.initializeClassInstantiator());
        }

        Class<?> objenesisClassLoader = Class.forName("org.mockito.internal.creation.bytebuddy.ClassInstantiator$UsingObjenesis");
        Constructor<?> constructor = objenesisClassLoader.getDeclaredConstructor(boolean.class);
        ClassInstantiator classInstantiator = (ClassInstantiator) constructor.newInstance(new GlobalConfiguration().enableClassCache());
        assertTrue(classInstantiator instanceof ClassInstantiator$UsingObjenesis);

        mockMaker = new ByteBuddyMockMaker();
        mockMaker.classInstantiator = classInstantiator;

        assertSame(classInstantiator, mockMaker.classInstantiator);
    }

    @Test
    public void testAsInternalMockHandler() {
        MockHandler handler = mockHandler;
        assertSame(handler, mockMaker.asInternalMockHandler(handler));
    }
}
package org.mockito.internal.creation.bytebuddy;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.mockito.internal.util.StringJoiner.join;
import static org.powermock.reflect.internal.Whitebox.getInternalState;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredExceptionException;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;
import org.powermock.reflection.Whitebox;

public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker mockMaker;

    @Test
    public void ByteBuddyMockMaker_constructor() {
        mockMaker = new ByteBuddyMockMaker();
        assertNotNull(mockMaker.classInstantiator);
        assertNotNull(mockMaker.cachingMockBytecodeGenerator);
    }

    @Test
    public void createMock_serializableModeACrossClassloaders() {
        MockCreationSettings settings = new MockCreationSettings(MockCreationSettings.DEFAULT_SERIALIZATION_MODE, null, SerializableMode.ACROSS_CLASSLOADERS, null, null);
        try {
            mockMaker.createMock(settings, mockHandler());
            fail("Expected MockitoException");
        } catch (MockitoException e) {
            // Expected
        }
    }

    @Test
    public void createMock_success() throws InstantiationException, IllegalAccessException, ClassCastException {
        MockCreationSettings settings = new MockCreationSettings(MockCreationSettings.DEFAULT_SERIALIZATION_MODE, Int.class, null, null, null);
        Class<?> mockedProxyType = mockMaker.cachingMockBytecodeGenerator.get(Integer.TYPE, new Class[0]);
        Object mockInstance = mockMaker.classInstantiator.instantiate(mockedProxyType);
        MockHandler handler = mockHandler();
        Object result = mockMaker.createMock(settings, handler);

        assertInstanceOf(Integer.class, result);
        assertInstanceOf(mockedProxyType, result);
        assertInstanceOf(org.mockito.internal.creation.bytebuddy.MockMethodInterceptor.MockAccess.class, result);

        MockMethodInterceptor.MockAccess mockAccess = (MockMethodInterceptor.MockAccess) result;
        assertEquals(handler, mockAccess.getMockitoInterceptor().getMockHandler());
    }

    @Test
    public void createMock_classCastException() throws InstantiationException, IllegalAccessException {
        MockCreationSettings settings = new MockCreationSettings(MockCreationSettings.DEFAULT_SERIALIZATION_MODE, String.class, null, null, null);
        Class<?> mockedProxyType = mockMaker.cachingMockBytecodeGenerator.get(String.class, new Class[0]);
        Object mockInstance = mockMaker.classInstantiator.instantiate(Integer.TYPE);
        MockHandler handler = mockHandler();

        try {
            mockMaker.createMock(settings, handler);
            fail("Expected ClassCastException");
        } catch (ClassCastException e) {
            // Expected
        }
    }

    @Test
    public void getHandler_success() {
        MockCreationSettings settings = new MockCreationSettings(MockCreationSettings.DEFAULT_SERIALIZATION_MODE, Integer.class, null, null, null);
        Object mock = mockMaker.createMock(settings, mockHandler());
        MockHandler handler = mockMaker.getHandler(mock);
        assertNotNull(handler);
    }

    @Test
    public void getHandler_fail() {
        Object mock = new Object();
        assertNull(mockMaker.getHandler(mock));
    }

    @Test
    public void resetMock_success() throws InstantiationException, IllegalAccessException {
        MockCreationSettings settings = new MockCreationSettings(MockCreationSettings.DEFAULT_SERIALIZATION_MODE, Integer.class, null, null, null);
        Object mock = mockMaker.createMock(settings, mockHandler());
        MockHandler newHandler = mockHandler();

        mockMaker.resetMock(mock, newHandler, settings);

        MockHandler resetHandler = mockMaker.getHandler(mock);
        assertEquals(newHandler, resetHandler);
    }

    @Test
    public void resetMock_fail() {
        Object mock = new Object();
        MockHandler newHandler = mockHandler();

        try {
            mockMaker.resetMock(mock, newHandler, new MockCreationSettings(MockCreationSettings.DEFAULT_SERIALIZATION_MODE, Integer.class, null, null, null));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void initializeClassInstantiator_success() {
        mockMaker = new ByteBuddyMockMaker();
        assertNotNull(mockMaker.classInstantiator);
    }

    @Test(expected = MockitoException.class)
    public void initializeClassInstantiator_fail() {
        mockMaker = new ByteBuddyMockMaker();
    }

    @Test
    public void asInternalMockHandler_success() {
        MockHandler handler = mockHandler();
        InternalMockHandler result = mockMaker.asInternalMockHandler(handler);
        assertInstanceOf(InternalMockHandler.class, result);
    }

    @Test(expected = MockitoException.class)
    public void asInternalMockHandler_fail() {
        MockHandler handler = mockHandler();
        if (!(handler instanceof InternalMockHandler)) {
            // Expected
        }
    }

    private MockHandler mockHandler() {
        return new MockHandler() {
            @Override
            public void call(Object invocation) {}
        };
    }

    private static class ClassInstantiatorTest {
        @Test
        public void ClassInstantiator_describeClass_null() {
            assertEquals("null", mockMaker.describeClass(null));
        }

        @Test
        public void ClassInstantiator_describeClass_Class() {
            Class<?> testClass = String.class;
            assertEquals("class '" + testClass.getCanonicalName() + "'", mockMaker.describeClass(testClass));
        }

        @Test
        public void ClassInstantiator_describeClass_Object() {
            Object testObject = new Object();
            assertEquals("class '" + testObject.getClass().getCanonicalName() + "'", mockMaker.describeClass(testObject));
        }
    }
}
package org.mockito.internal.creation.bytebuddy;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.internal.util.StringJoiner.join;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.internal.creation.instance.CachingMockBytecodeGenerator;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.MockCreationSettings.SerializableMode;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockMaker.CreationTime;

public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker byteBuddyMockMaker;
    private CachingMockBytecodeGenerator cachingMockBytecodeGeneratorMock;
    private ClassInstantiator classInstantiatorMock;

    @Before
    public void setUp() {
        cachingMockBytecodeGeneratorMock = mock(CachingMockBytecodeGenerator.class);
        classInstantiatorMock = mock(ClassInstantiator.class);
        byteBuddyMockMaker = new ByteBuddyMockMaker() {
            {
                this.cachingMockBytecodeGenerator = cachingMockBytecodeGeneratorMock;
                this.classInstantiator = classInstantiatorMock;
            }
        };
    }

    @Test
    public void testCreateMock_withValidSettingsAndHandler_returnsMockInstance() {
        Class<?> mockedType = mock(String.class);
        MockHandler mockHandler = mock(MockHandler.class);
        MockCreationSettings<String> settings = new MockCreationSettings<String>(String.class);
        String mockedProxyType = "mockito.mock.StringMock";

        when(cachingMockBytecodeGeneratorMock.get(mockedType, Collections.emptyList())).thenReturn(mockedProxyType);
        when(classInstantiatorMock.instantiate(mockedProxyType)).thenReturn("mockInstance");

        String result = byteBuddyMockMaker.createMock(settings, mockHandler);

        verify(classInstantiatorMock).instantiate(mockedProxyType);
        verifyNoMoreInteractions(cachingMockBytecodeGeneratorMock);
        verify(mockHandler).handle(eq(mockedType), eq(settings), any());

        assertEquals("mockInstance", result);
    }

    @Test(expected = MockitoException.class)
    public void testCreateMock_withSerializableModeSetAcrossClassloaders_throwsMockitoException() {
        MockCreationSettings<Object> settings = new MockCreationSettings<Object>(Object.class);
        settings.setSerializableMode(SerializableMode.ACROSS_CLASSLOADERS);
        MockHandler mockHandler = mock(MockHandler.class);

        byteBuddyMockMaker.createMock(settings, mockHandler);

        verify(cachingMockBytecodeGeneratorMock, never()).get(any(), any());
        verify(classInstantiatorMock, never()).instantiate(any());
        verify(mockHandler, never()).handle(any(), any(), any());
    }

    @Test
    public void testCreateMock_withClassCastExceptionDuringInstanceCreation_throwsMockitoException() {
        Class<?> mockedType = mock(String.class);
        MockHandler mockHandler = mock(MockHandler.class);
        MockCreationSettings<String> settings = new MockCreationSettings<String>(String.class);
        String mockedProxyType = "mockito.mock.StringMock";

        when(cachingMockBytecodeGeneratorMock.get(mockedType, Collections.emptyList())).thenReturn(mockedProxyType);
        when(classInstantiatorMock.instantiate(mockedProxyType)).thenThrow(new ClassCastException());

        try {
            byteBuddyMockMaker.createMock(settings, mockHandler);
        } catch (MockitoException e) {
            String expectedMessage = join(
                    "ClassCastException occurred while creating the mockito mock :",
                    "  class to mock : " + describeClass(mockedType),
                    "  created class : " + mockedProxyType,
                    "  proxy instance class : null",
                    "  instance creation by : ByteBuddyMockMaker"
            );
            String actualMessage = e.getMessage();

            assertEquals(expectedMessage, actualMessage);
        }

        verify(cachingMockBytecodeGeneratorMock).get(mockedType, Collections.emptyList());
        verify(classInstantiatorMock).instantiate(mockedProxyType);
        verifyZeroInteractions(mockHandler);
    }

    @Test(expected = MockitoException.class)
    public void testCreateMock_withInstantiationExceptionDuringInstanceCreation_throwsMockitoException() {
        Class<?> mockedType = mock(String.class);
        MockHandler mockHandler = mock(MockHandler.class);
        MockCreationSettings<String> settings = new MockCreationSettings<String>(String.class);
        String mockedProxyType = "mockito.mock.StringMock";

        when(cachingMockBytecodeGeneratorMock.get(mockedType, Collections.emptyList())).thenReturn(mockedProxyType);
        when(classInstantiatorMock.instantiate(mockedProxyType)).thenThrow(new InstantiationException());

        byteBuddyMockMaker.createMock(settings, mockHandler);

        verify(cachingMockbytecodeGeneratorMock).get(mockedType, Collections.emptyList());
        verify(classInstantiatorMock).instantiate(mockedProxyType);
        verifyZeroInteractions(mockHandler);

        String expectedMessage = "Unable to create mock instance of type 'Object'";
        String actualMessage = byteBuddyMockMaker.getClass().getConstructors()[0].getExceptionTypes()[0].getSimpleName();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testGetHandler_returnsMockHandler() {
        Object mock = mock(String.class);

        MockHandler expectedMockHandler = mock(MockHandler.class);
        MockMethodInterceptor mockAccess = spy(mock);
        mockAccess.setMockitoInterceptor(new MockMethodInterceptor(expectedMockHandler, new MockCreationSettings<String>(String.class)));

        MockHandler result = byteBuddyMockMaker.getHandler(mockAccess);

        assertEquals(expectedMockHandler, result);
    }

    @Test
    public void testResetMock_resetsMockHandler() {
        Object mock = mock(String.class);
        MockHandler oldMockHandler = mock(MockHandler.class);
        MockHandler newMockHandler = mock(MockHandler.class);
        MockCreationSettings<String> settings = new MockCreationSettings<String>(String.class);

        MockMethodInterceptor mockAccess = spy(mock);
        mockAccess.setMockitoInterceptor(new MockMethodInterceptor(oldMockHandler, settings));

        byteBuddyMockMaker.resetMock(mock, newMockHandler, settings);

        verify(mockAccess).setMockitoInterceptor(new MockMethodInterceptor(newMockHandler, settings));
        verifyNoMoreInteractions(oldMockHandler);
    }

    @Test
    public void testInitializeClassInstantiator_returnsClassInstantiatorInstance() {
        Class<?> objenesisClassLoader = mock(Class.class);
        Constructor<?> usingClassCacheConstructor = mock(Constructor.class);
        when(objenesisClassLoader.getDeclaredConstructor(boolean.class)).thenReturn(usingClassCacheConstructor);
        when(usingClassCacheConstructor.newInstance(anyBoolean())).thenReturn(classInstantiatorMock);

        ClassInstantiator result = byteBuddyMockMaker.initializeClassInstantiator();

        assertEquals(classInstantiatorMock, result);
    }

    @Test(expected = MockitoException.class)
    public void testInitializeClassInstantiator_throwsIllegalStateException() {
        try {
            byteBuddyMockMaker.initializeClassInstantiator();
        } catch (IllegalStateException e) {
            String expectedMessage = join(
                    "Mockito could not create mock: Objenesis is missing on the classpath.",
                    "Please add Objenesis on the classpath.",
                    ""
            );
            String actualMessage = e.getMessage();

            assertEquals(expectedMessage, actualMessage);
        }

        verify(cachingMockbytecodeGeneratorMock, never()).get(any(), any());
        verify(classInstantiatorMock, never()).instantiate(any());
        verifyZeroInteractions(mockHandler);
    }

    @Test
    public void testAsInternalMockHandler_returnsInternalMockHandler() {
        MockHandler mockHandler = mock(MockHandler.class);
        InternalMockHandler expectedInternalMockHandler = mock(InternalMockHandler.class);

        when(mockHandler).thenReturn(expectedInternalMockHandler);

        InternalMockHandler result = byteBuddyMockMaker.asInternalMockHandler(mockHandler);

        assertEquals(expectedInternalMockHandler, result);
    }

    private String describeClass(Class type) {
        return type == null ? "null" : "'" + type.getCanonicalName() + "', loaded by classloader : '" + type.getClassLoader() + "'";
    }

    private String describeClass(Object instance) {
        return instance == null ? "null" : describeClass(instance.getClass());
    }

    private static class InstantiationException extends Exception {
        InstantiationException() {
            super();
        }

        InstantiationException(String message) {
            super(message);
        }

        InstantiationException(String message, Throwable cause) {
            super(message, cause);
        }

        InstantiationException(Throwable cause) {
            super(cause);
        }

        InstantiationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }
}
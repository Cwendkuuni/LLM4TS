package org.mockito.internal.creation.bytebuddy;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.argWhich;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.isInstanceOf;
import static org.mockito.Mockito.same;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.argWhich;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;

public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker byteBuddyMockMaker;
    private ClassInstantiator classInstantiator;
    private CachingMockBytecodeGenerator cachingMockBytecodeGenerator;
    private InternalMockHandler mockHandler;

    @Before
    public void setUp() {
        byteBuddyMockMaker = new ByteBuddyMockMaker();
        classInstantiator = mock(ClassInstantiator.class);
        cachingMockBytecodeGenerator = mock(CachingMockBytecodeGenerator.class);
        mockHandler = mock(InternalMockHandler.class);
        when(byteBuddyMockMaker.classInstantiator).thenReturn(classInstantiator);
        when(byteBuddyMockMaker.cachingMockBytecodeGenerator).thenReturn(cachingMockBytecodeGenerator);
    }

    @Test
    public void testCreateMock() {
        Class<String> typeToMock = String.class;
        MockHandler handler = mockHandler;
        MockCreationSettings<String> settings = new MockCreationSettings<>(typeToMock, handler, SerializableMode.DEFAULT);

        Class<? extends String> mockedProxyType = byteBuddyMockMaker.cachingMockBytecodeGenerator.get(typeToMock, new Class[0]);
        String mockInstance = byteBuddyMockMaker.createMock(settings, handler);

        verify(cachingMockBytecodeGenerator).get(typeToMock, new Class[0]);
        verify(classInstantiator).instantiate(mockedProxyType);
        verify(mockInstance).setMockitoInterceptor(argThat(i -> i instanceof MockMethodInterceptor.MockAccess));
        assertNotNull(mockInstance);

        assertEquals(typeToMock, byteBuddyMockMaker.getTypeToMock(mockInstance));
        assertEquals(handler, byteBuddyMockMaker.getHandler(mockInstance));
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockWithSerializableModeAcrossClassloaders() {
        Class<String> typeToMock = String.class;
        MockHandler handler = mockHandler;
        MockCreationSettings<String> settings = new MockCreationSettings<>(typeToMock, handler, SerializableMode.ACROSS_CLASSLOADERS);

        byteBuddyMockMaker.createMock(settings, handler);
    }

    @Test
    public void testGetHandler() {
        Object mock = mock(String.class);
        assertEquals(mockHandler, byteBuddyMockMaker.getHandler(mock));
    }

    @Test
    public void testResetMock() {
        Object mock = mock(String.class);
        MockHandler newHandler = mockHandler;
        MockCreationSettings<String> settings = new MockCreationSettings<>(String.class, newHandler, SerializableMode.DEFAULT);

        byteBuddyMockMaker.setMockitoInterceptor(mock, newHandler, settings);
        byteBuddyMockMaker.getHandler(mock);

        byteBuddyMockMaker.resetMock(mock, newHandler, settings);
        assertEquals(newHandler, byteBuddyMockMaker.getHandler(mock));
    }

    @Test
    public void testInitializeClassInstantiator() {
        try {
            byteBuddyMockMaker.initializeClassInstantiator();
        } catch (ClassNotFoundException e) {
            fail("Expected ClassNotFoundException, but got: " + e.getMessage());
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testUnableToCreateMockInstance() {
        Class<String> typeToMock = String.class;
        MockHandler handler = mockHandler;
        MockCreationSettings<String> settings = new MockCreationSettings<>(typeToMock, handler, SerializableMode.DEFAULT);

        doThrow(new InstantiationException("Test Exception"))
                .when(classInstantiator)
                .instantiate(any(Class.class));

        byteBuddyMockMaker.createMock(settings, handler);
    }

    @Test
    public void testClassCastException() {
        Class<String> typeToMock = String.class;
        MockHandler handler = mockHandler;
        MockCreationSettings<String> settings = new MockCreationSettings<>(typeToMock, handler, SerializableMode.DEFAULT);

        doAnswer(i -> {
            throw new ClassCastException("Test ClassCastException");
        })
                .when(classInstantiator)
                .instantiate(any(Class.class));

        byteBuddyMockMaker.createMock(settings, handler);
    }

    @After
    public void tearDown() {
        reset(classInstantiator, cachingMockBytecodeGenerator, mockHandler);
    }
}
package org.mockito.internal.creation.bytebuddy;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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

    private MockMaker mockMaker = new ByteBuddyMockMaker();

    @Test(expected = MockitoException.class)
    public void testCreateMock_serializableModeAcrossClassloaders() {
        MockCreationSettings<Integer> settings = new MockCreationSettings.Builder<Integer>()
                .forClass(Integer.class)
                .serializableMode(SerializableMode.ACROSS_CLASSLOADERS)
                .build();
        mockMaker.createMock(settings, mock(MockHandler.class));
    }

    @Test
    public void testCreateMock_regularCase() {
        MockCreationSettings<Integer> settings = new MockCreationSettings.Builder<Integer>()
                .forClass(Integer.class)
                .build();
        Integer mock = mockMaker.createMock(settings, mock(MockHandler.class));
        assertInstanceOf(Integer.class, mock);
        verifyZeroInteractions(mock);
    }

    @Test
    public void testCreateMock_extraInterfaces() {
        MockCreationSettings<AnonymousClass> settings = new MockCreationSettings.Builder<AnonymousClass>()
                .forClass(AnonymousClass.class)
                .extraInterfaces(AnotherClass.class)
                .build();
        AnonymousClass mock = mockMaker.createMock(settings, mock(MockHandler.class));
        assertInstanceOf(AnonymousClass.class, mock);
        verifyZeroInteractions(mock);
    }

    @Test(expected = ClassCastException.class)
    public void testCreateMock_classCastException() {
        MockCreationSettings<String> settings = new MockCreationSettings.Builder<String>()
                .forClass(String[].class)
                .build();
        mockMaker.createMock(settings, mock(MockHandler.class));
    }

    @Test(expected = InstantiationException.class)
    public void testCreateMock_instantiationException() {
        MockCreationSettings<String> settings = new MockCreationSettings.Builder<String>()
                .forClass(String[].class)
                .build();
        mockMaker = spy(mockMaker);
        when(mockMaker.initializeClassInstantiator()).thenThrow(new InstantiationException());
        mockMaker.createMock(settings, mock(MockHandler.class));
    }

    @Test
    public void testGetHandler() {
        Integer mock = mockMaker.createMock(new MockCreationSettings.Builder<Integer>().build(), mock(MockHandler.class));
        MockHandler handler = mockMaker.getHandler(mock);
        assertNull(handler);
    }

    @Test
    public void testResetMock() {
        Integer mock = mockMaker.createMock(new MockCreationSettings.Builder<Integer>().build(), mock(MockHandler.class));
        MockHandler newHandler = mock(MockHandler.class);
        mockMaker.resetMock(mock, newHandler, new MockCreationSettings.Builder<Integer>().build());
        MockHandler resetHandler = mockMaker.getHandler(mock);
        assertInstanceOf(newHandler.getClass(), resetHandler);
    }

    private class AnonymousClass {
    }

    private static class AnotherClass {
    }

    private static class InstantiationException extends Exception {
        public InstantiationException() {
        }

        public InstantiationException(String message) {
            super(message);
        }

        public InstantiationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
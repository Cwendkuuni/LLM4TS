package org.mockito.internal.creation.bytebuddy;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.invocation.MockHandler;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

public class ByteBuddyMockMakerTest {

    @Test
    public void testCreateMock_SerializableMode_ACROSS_CLASSLOADERS_throwsException() {
        MockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockHandler mockHandler = mock(MockHandler.class);
        MockCreationSettings settings = new MockCreationSettings<Object>()
                .withSerializableMode(SerializableMode.ACROSS_CLASSLOADERS)
                .forType(Object.class);

        assertThrows(MockitoException.class, () -> byteBuddyMockMaker.createMock(settings, mockHandler));
    }

    @Test
    public void testCreateMock_validCall() {
        MockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockHandler mockHandler = mock(MockHandler.class);
        MockCreationSettings settings = new MockCreationSettings<Object>()
                .withSerializableMode(SerializableMode.DEFAULT)
                .forType(Object.class);

        Object mock = byteBuddyMockMaker.createMock(settings, mockHandler);
        assertInstanceOf(Object.class, mock);
    }

    @Test
    public void testCreateMock_ClassCastException() {
        MockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockHandler mockHandler = mock(MockHandler.class);
        MockCreationSettings settings = new MockCreationSettings<String>()
                .withSerializableMode(SerializableMode.DEFAULT)
                .forType(Object.class);

        try {
            byteBuddyMockMaker.createMock(settings, mockHandler);
        } catch (MockitoException ex) {
            ClassCastException classCastException = ex.getCause();
            assertInstanceOf(ClassCastException.class, classCastException);
        }
    }

    @Test
    public void testCreateMock_InstantiationException() {
        MockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockHandler mockHandler = mock(MockHandler.class);
        MockCreationSettings settings = new MockCreationSettings<String>()
                .withSerializableMode(SerializableMode.DEFAULT)
                .forType(String.class);

        try {
            byteBuddyMockMaker.createMock(settings, mockHandler);
        } catch (MockitoException e) {
            Throwable cause = e.getCause();
            assertInstanceOf(InvocationTargetException.class, cause);
            InvocationTargetException ite = (InvocationTargetException) cause;
            assertInstanceOf(InstantiationException.class, ite.getCause());
        }
    }

    @Test
    public void testGetHandler_validCall() {
        MockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        Object mock = spy(new Object());

        MockHandler handler = byteBuddyMockMaker.getHandler(mock);
        assertInstanceOf(MockHandler.class, handler);
    }

    @Test
    public void testGetHandler_nullMock() {
        MockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();

        assertNull(byteBuddyMockMaker.getHandler(null));
    }

    @Test
    public void testResetMock_validCall() {
        MockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        Object mock = mock(Object.class);
        MockHandler newHandler = mock(MockHandler.class);
        MockCreationSettings settings = new MockCreationSettings<Object>()
                .forType(Object.class);

        byteBuddyMockMaker.resetMock(mock, newHandler, settings);
        MockHandler oldHandler = byteBuddyMockMaker.getHandler(mock);
        assertInstanceOf(newHandler.getClass(), oldHandler.getClass());
    }

    @Test
    public void testResetMock_nullMock() {
        MockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        Object mock = null;
        MockHandler newHandler = mock(MockHandler.class);
        MockCreationSettings settings = new MockCreationSettings<Object>()
                .forType(Object.class);

        byteBuddyMockMaker.resetMock(mock, newHandler, settings);
        // No verification as null mock
    }

    @Test
    public void testInitializeClassInstantiator_ClassNotFoundException() {
        try {
            new ByteBuddyMockMaker();
            fail("Should throw ClassNotFoundException");
        } catch (IllegalStateException e) {
            assertInstanceOf(IllegalStateException.class, e);
        }
    }
}
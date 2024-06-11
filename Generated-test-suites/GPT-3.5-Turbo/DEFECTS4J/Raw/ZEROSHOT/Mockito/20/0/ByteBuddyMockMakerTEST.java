package org.mockito.internal.creation.bytebuddy;

import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.internal.util.StringJoiner;
import java.lang.reflect.Constructor;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.*;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

public class ByteBuddyMockMakerTest {

    @Test
    public void testCreateMock() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<Object> settings = new MockCreationSettings<>();
        settings.setSerializableMode(SerializableMode.ACROSS_CLASSLOADERS);
        InternalMockHandler mockHandler = new InternalMockHandler();
        Object mock = byteBuddyMockMaker.createMock(settings, mockHandler);
        assertTrue(mock instanceof Object);
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockWhenSerializableModeAcrossClassloaders() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<Object> settings = new MockCreationSettings<>();
        settings.setSerializableMode(SerializableMode.ACROSS_CLASSLOADERS);
        InternalMockHandler mockHandler = new InternalMockHandler();
        Object mock = byteBuddyMockMaker.createMock(settings, mockHandler);
    }

    @Test
    public void testGetHandler() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        InternalMockHandler mockHandler = new InternalMockHandler();
        Object mock = new Object();
        assertNull(byteBuddyMockMaker.getHandler(mock));
        mock = new MockMethodInterceptor.MockAccess();
        assertNull(byteBuddyMockMaker.getHandler(mock));
    }

    @Test
    public void testResetMock() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        InternalMockHandler mockHandler = new InternalMockHandler();
        Object mock = new Object();
        byteBuddyMockMaker.resetMock(mock, mockHandler, new MockCreationSettings<>());
        assertTrue(mock instanceof Object);
    }

    @Test(expected = MockitoException.class)
    public void testAsInternalMockHandlerWhenMockHandlerNotInstanceOfInternalMockHandler() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockHandler mockHandler = new MockHandler();
        byteBuddyMockMaker.asInternalMockHandler(mockHandler);
    }
}
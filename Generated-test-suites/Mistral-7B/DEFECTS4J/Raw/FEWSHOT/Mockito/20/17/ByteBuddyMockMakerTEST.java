package org.mockito.internal.creation.bytebuddy;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.internal.util.StringJoiner.join;

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

    private ByteBuddyMockMaker byteBuddyMockMaker;

    @Test
    public void ByteBuddyMockMaker_constructor() {
        byteBuddyMockMaker = new ByteBuddyMockMaker();
        assertNull(byteBuddyMockMaker.classInstantiator);
        assertNull(byteBuddyMockMaker.cachingMockBytecodeGenerator);
    }

    @Test
    public void createMock_withNullMockHandler_throwsMockitoException() {
        byteBuddyMockMaker = new ByteBuddyMockMaker();
        try {
            byteBuddyMockMaker.createMock(new MockCreationSettings<>(String.class), null);
            assertFalse("Expected a MockitoException to be thrown");
        } catch (MockitoException e) {
            assertSame("Expected 'Unable to create mock instance of type 'java.lang.String''", e.getMessage(),
                    "Unexpected MockitoException message");
        }
    }

    @Test
    public void createMock_withNullTypeToMock_throwsMockitoException() {
        byteBuddyMockMaker = new ByteBuddyMockMaker();
        try {
            byteBuddyMockMaker.createMock(new MockCreationSettings<>(null), mock(MockHandler.class));
            assertFalse("Expected a MockitoException to be thrown");
        } catch (MockitoException e) {
            assertSame("Expected 'Unable to create mock instance of type null''", e.getMessage(),
                    "Unexpected MockitoException message");
        }
    }

    @Test
    public void createMock_withNullTypeToMockAndNullMockHandler_throwsMockitoException() {
        byteBuddyMockMaker = new ByteBuddyMockMaker();
        try {
            byteBuddyMockMaker.createMock(new MockCreationSettings<>(null), null);
            assertFalse("Expected a MockitoException to be thrown");
        } catch (MockitoException e) {
            assertSame("Expected 'Unable to create mock instance of type null''", e.getMessage(),
                    "Unexpected MockitoException message");
        }
    }

    @Test
    public void createMock_withNullExtraInterfaces_returnsMockOfExpectedType() {
        byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<String> settings = new MockCreationSettings<>(String.class);
        MockHandler handler = mock(MockHandler.class);
        String mock = byteBuddyMockMaker.createMock(settings, handler);
        assertEquals("Expected mock of String class", String.class, mock.getClass());
    }

    @Test
    public void createMock_withExtraInterfaces_returnsMockOfExpectedTypeWithExtraInterfaces() {
        byteBuddyMockMaker = new ByteBuddyMockMaker();
        Class<?> extraInterface1 = Mockito.class;
        Class<?> extraInterface2 = MockitoSession.class;
        MockCreationSettings<String> settings = new MockCreationSettings<>(String.class, extraInterface1, extraInterface2);
        MockHandler handler = mock(MockHandler.class);
        String mock = byteBuddyMockMaker.createMock(settings, handler);
        assertEquals("Expected mock of String class with extra interfaces", String.class, mock.getClass());
        assertSame("Expected mock to implement extra interfaces", extraInterface1, mock.getClass().getInterfaces()[0]);
        assertSame("Expected mock to implement extra interfaces", extraInterface2, mock.getClass().getInterfaces()[1]);
    }

    @Test
    public void createMock_withSerializableMode_throwsUnsupportedException() {
        byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<String> settings = new MockCreationSettings<>(String.class, SerializableMode.ACROSS_CLASSLOADERS);
        MockHandler handler = mock(MockHandler.class);
        try {
            byteBuddyMockMaker.createMock(settings, handler);
            assertFalse("Expected a MockitoException to be thrown");
        } catch (MockitoException e) {
            assertSame("Expected 'Serialization across classloaders not yet supported with ByteBuddyMockMaker''", e.getMessage(),
                    "Unexpected MockitoException message");
        }
    }

    @Test
    public void getHandler_returnsNullForNullMock() {
        byteBuddyMockMaker = new ByteBuddyMockMaker();
        Object mock = spy(String.class);
        assertNull("Expected null for getHandler with null mock", byteBuddyMockMaker.getHandler(mock));
    }

    @Test
    public void getHandler_returnsMockHandlerForMock() {
        byteBuddyMockMaker = new ByteBuddyMockMaker();
        Object mock = spy(MockMethodInterceptor.class);
        MockHandler handler = mock(MockHandler.class);
        byteBuddyMockMaker.createMock(new MockCreationSettings<>(MockMethodInterceptor.class), handler);
        assertSame("Expected mockHandler for getHandler with mock", handler, byteBuddyMockMaker.getHandler(mock));
    }

    @Test
    public void resetMock_resetsMockHandlerForMock() {
        byteBuddyMockMaker = new ByteBuddyMockMaker();
        Object mock = spy(MockMethodInterceptor.class);
        MockHandler newHandler = mock(MockHandler.class);
        MockHandler oldHandler = mock(MockHandler.class);
        byteBuddyMockMaker.createMock(new MockCreationSettings<>(MockMethodInterceptor.class), oldHandler);
        byteBuddyMockMaker.resetMock(mock, newHandler, new MockCreationSettings<>(MockMethodInterceptor.class));
        assertSame("Expected newHandler for getHandler with mock after reset", newHandler, byteBuddyMockMaker.getHandler(mock));
    }

    @Test
    public void describeClass_returnsNullForNullClass() {
        byteBuddyMockMaker = new ByteBuddyMockMaker();
        assertNull("Expected null for describeClass with null class", byteBuddyMockMaker.describeClass(null));
    }

    @Test
    public void describeClass_returnsDescriptionForClass() {
        byteBuddyMockMaker = new ByteBuddyMockMaker();
        Class<?> clazz = String.class;
        String expectedDescription = join("'java.lang.String', loaded by classloader : '', classloader name : 'bootstrap'");
        String description = byteBuddyMockMaker.describeClass(clazz);
        assertEquals("Expected description for describeClass with class", expectedDescription, description);
    }
}
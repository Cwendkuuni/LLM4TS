import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.MockHandler;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker;
import org.mockito.internal.creation.bytebuddy.ClassInstantiator;
import org.mockito.internal.creation.bytebuddy.CachingMockBytecodeGenerator;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.*;
import org.mockito.invocation.MockHandlerImpl;
import org.mockito.internal.util.StringJoiner;
import org.mockito.plugins.MockMaker;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

public class ByteBuddyMockMakerTest {

    @Test(expected = MockitoException.class)
    public void testCreateMockWithSerializableModeAcrossClassloaders() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<Object> settings = MockCreationSettings.newBuilder()
                .serializableMode(SerializableMode.ACROSS_CLASSLOADERS)
                .build();
        MockHandler handler = Mockito.mock(MockHandler.class);
        byteBuddyMockMaker.createMock(settings, handler);
    }

    @Test
    public void testCreateMock() throws InstantiationException {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        Class<String> stringType = String.class;
        MockCreationSettings<String> settings = MockCreationSettings.newBuilder()
                .typeToMock(stringType)
                .build();
        MockHandler handler = Mockito.mock(MockHandler.class);
        String mock = byteBuddyMockMaker.createMock(settings, handler);
        assertEquals(mock, Mockito.mock(String.class));
    }

    @Test(expected = ClassCastException.class)
    public void testCreateMockClassCastException() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<Object> settings = MockCreationSettings.newBuilder()
                .typeToMock(String.class)
                .build();
        MockHandler handler = Mockito.mock(MockHandler.class);
        Object mock = byteBuddyMockMaker.createMock(settings, handler);
        assertNotSame(mock, "hello world");
        Class<?> mockType = mock.getClass();
        assertNotSame(String.class, mockType);
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockInstantiationException() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        Class<Integer> intType = Integer.class;
        MockCreationSettings<Integer> settings = MockCreationSettings.newBuilder()
                .typeToMock(intType)
                .build();
        MockHandler handler = Mockito.mock(MockHandler.class);
        try {
            byteBuddyMockMaker.createMock(settings, handler);
            fail();
        } catch (InstantiationException e) {
            // expected
        }
    }

    @Test
    public void testGetHandler() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        Object mock = Mockito.mock(Object.class);
        MockHandler handler = byteBuddyMockMaker.getHandler(mock);
        assertNull(handler);

        MockHandler internalHandler = Mockito.mock(InternalMockHandler.class);
        MockCreationSettings<Object> settings = MockCreationSettings.newBuilder()
                .typeToMock(Object.class)
                .build();
        byteBuddyMockMaker.createMock(settings, internalHandler);
        handler = byteBuddyMockMaker.getHandler(mock);
        assertSame(internalHandler, handler);
    }

    @Test
    public void testResetMock() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        Object mock = Mockito.mock(Object.class);
        MockHandler handler1 = Mockito.mock(MockHandler.class);
        MockHandler handler2 = Mockito.mock(MockHandler.class);

        byteBuddyMockMaker.createMock(MockCreationSettings.newBuilder().build(), handler1);
        byteBuddyMockMaker.resetMock(mock, handler2, MockCreationSettings.newBuilder().build());

        MockHandler resetHandler = byteBuddyMockMaker.getHandler(mock);
        assertSame(handler2, resetHandler);
    }

    @Test
    public void testInitializeClassInstantiator() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        try {
            byteBuddyMockMaker.initializeClassInstantiator();
        } catch (ClassNotFoundException e) {
            fail("Expected no exception");
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testInitializeClassInstantiatorMissingObjenesis() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        byteBuddyMockMaker.initializeClassInstantiator();
    }

    @Test
    public void testAsInternalMockHandler() {
        MockHandler handler = Mockito.mock(MockHandler.class);
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        InternalMockHandler internalHandler = byteBuddyMockMaker.asInternalMockHandler(handler);
        assertSame(handler, internalHandler);
    }

    @Test(expected = MockitoException.class)
    public void testAsInternalMockHandlerNonInternalMockHandler() {
        MockHandler handler = Mockito.mock(Object.class);
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        byteBuddyMockMaker.asInternalMockHandler(handler);
    }
}
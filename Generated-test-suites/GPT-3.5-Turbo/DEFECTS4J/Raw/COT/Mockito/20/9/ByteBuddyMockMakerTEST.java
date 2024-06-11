import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.*;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;

import static org.junit.Assert.*;

public class ByteBuddyMockMakerTest {

    @Test
    public void testCreateMock() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<String> settings = new MockCreationSettings<>(String.class);
        MockHandler handler = new InternalMockHandler();

        String mock = mockMaker.createMock(settings, handler);

        assertNotNull(mock);
        assertEquals(String.class, mock.getClass());
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockWithSerializableModeAcrossClassLoaders() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<String> settings = new MockCreationSettings<>(String.class);
        settings.setSerializableMode(SerializableMode.ACROSS_CLASSLOADERS);
        MockHandler handler = new InternalMockHandler();

        mockMaker.createMock(settings, handler);
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockWithInvalidHandlerType() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<String> settings = new MockCreationSettings<>(String.class);
        MockHandler handler = new MockHandler() {
            @Override
            public Object handle(MockHandlerInvocation invocation) throws Throwable {
                return null;
            }
        };

        mockMaker.createMock(settings, handler);
    }

    @Test
    public void testEnsureMockIsAssignableToMockedType() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<String> settings = new MockCreationSettings<>(String.class);

        String mock = "mock";
        String result = mockMaker.ensureMockIsAssignableToMockedType(settings, mock);

        assertEquals(mock, result);
    }

    @Test
    public void testGetHandler() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockHandler handler = new InternalMockHandler();
        Object mock = mockMaker.createMock(new MockCreationSettings<>(String.class), handler);

        MockHandler result = mockMaker.getHandler(mock);

        assertEquals(handler, result);
    }

    @Test
    public void testGetHandlerWithNonMockObject() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object mock = new Object();

        MockHandler result = mockMaker.getHandler(mock);

        assertNull(result);
    }

    @Test
    public void testResetMock() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockHandler handler = new InternalMockHandler();
        Object mock = mockMaker.createMock(new MockCreationSettings<>(String.class), handler);
        MockHandler newHandler = new InternalMockHandler();

        mockMaker.resetMock(mock, newHandler, new MockCreationSettings<>());

        MockHandler result = mockMaker.getHandler(mock);
        assertEquals(newHandler, result);
    }

    @Test(expected = MockitoException.class)
    public void testResetMockWithInvalidHandlerType() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockHandler handler = new InternalMockHandler();
        Object mock = mockMaker.createMock(new MockCreationSettings<>(String.class), handler);

        mockMaker.resetMock(mock, new MockHandler() {
            @Override
            public Object handle(MockHandlerInvocation invocation) throws Throwable {
                return null;
            }
        }, new MockCreationSettings<>());
    }

    @Test
    public void testInitializeClassInstantiator() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        ClassInstantiator classInstantiator = ByteBuddyMockMaker.initializeClassInstantiator();

        assertNotNull(classInstantiator);
    }

    @Test
    public void testAsInternalMockHandler() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockHandler handler = new InternalMockHandler();

        InternalMockHandler result = ByteBuddyMockMaker.asInternalMockHandler(handler);

        assertEquals(handler, result);
    }

    @Test(expected = MockitoException.class)
    public void testAsInternalMockHandlerWithInvalidHandlerType() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockHandler handler = new MockHandler() {
            @Override
            public Object handle(MockHandlerInvocation invocation) throws Throwable {
                return null;
            }
        };

        ByteBuddyMockMaker.asInternalMockHandler(handler);
    }
}
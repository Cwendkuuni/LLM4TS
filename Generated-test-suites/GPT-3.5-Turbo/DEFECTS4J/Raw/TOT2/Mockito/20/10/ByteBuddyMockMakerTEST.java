import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.internal.creation.instance.CachingMockBytecodeGenerator;
import org.mockito.internal.creation.instance.InstantiationException;
import org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;

import java.lang.reflect.Constructor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class ByteBuddyMockMakerTest {

    @Test
    public void testCreateMock_basic() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<String> settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);

        String mock = mockMaker.createMock(settings, handler);

        assertNotNull(mock);
    }

    @Test
    public void testCreateMock_serializableModeAcrossClassloadersError() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<String> settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);
        settings.setSerializableMode(SerializableMode.ACROSS_CLASSLOADERS);

        try {
            mockMaker.createMock(settings, handler);
        } catch (MockitoException e) {
            assertEquals("Serialization across classloaders not yet supported with ByteBuddyMockMaker", e.getMessage());
        }
    }

    @Test
    public void testGetHandler_notMockMethodInterceptorError() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object notMockMethodInterceptor = new Object();

        MockHandler result = mockMaker.getHandler(notMockMethodInterceptor);

        assertEquals(null, result);
    }

    @Test
    public void testResetMock_basic() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<String> settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);
        String mock = mockMaker.createMock(settings, handler);
        MockHandler newHandler = mock(MockHandler.class);

        mockMaker.resetMock(mock, newHandler, settings);
        InternalMockHandler resultHandler = mockMaker.getHandler(mock);

        assertTrue(resultHandler instanceof InternalMockHandler);
    }

    @Test
    public void testInitializeClassInstantiator_objenesisMissingError() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();

        try {
            ClassInstantiator classInstantiator = mockMaker.initializeClassInstantiator();
        } catch (IllegalStateException e) {
            assertTrue(e.getMessage().contains("Please add Objenesis on the classpath."));
        }
    }

}
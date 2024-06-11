import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.internal.creation.instance.InstantiationException;
import org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class ByteBuddyMockMakerTest {

    @Test
    public void testCreateMock_WithSerializableAcrossClassloaders() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<String> settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);

        try {
            mockMaker.createMock(settings, handler);
            fail("Expected MockitoException to be thrown");
        } catch (MockitoException e) {
            assertEquals("Serialization across classloaders not yet supported with ByteBuddyMockMaker", e.getMessage());
        }
    }

    @Test
    public void testCreateMock_ClassCastException() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<String> settings = mock(MockCreationSettings.class);
        settings.setTypeToMock(String.class);
        MockHandler handler = mock(MockHandler.class);

        try {
            mockMaker.createMock(settings, handler);
            fail("Expected MockitoException to be thrown");
        } catch (MockitoException e) {
            assertTrue(e.getMessage().contains("ClassCastException occurred while creating the mockito mock"));
        }
    }

    @Test
    public void testCreateMock_InstantiationException() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<Number> settings = mock(MockCreationSettings.class);
        settings.setTypeToMock(Number.class);
        MockHandler handler = mock(MockHandler.class);

        try {
            mockMaker.createMock(settings, handler);
            fail("Expected MockitoException to be thrown");
        } catch (MockitoException e) {
            assertTrue(e.getMessage().contains("Unable to create mock instance of type 'Number'"));
        }
    }

    @Test
    public void testEnsureMockIsAssignableToMockedType() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<String> settings = mock(MockCreationSettings.class);
        String mock = "mock";

        assertEquals(mock, mockMaker.ensureMockIsAssignableToMockedType(settings, mock));
    }

    @Test
    public void testGetHandler() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object mock = mock(Object.class);

        assertNull(mockMaker.getHandler(mock));
    }

    @Test
    public void testResetMock() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object mock = mock(Object.class);
        MockHandler newHandler = mock(MockHandler.class);
        MockCreationSettings<Object> settings = mock(MockCreationSettings.class);

        mockMaker.resetMock(mock, newHandler, settings);
    }
}
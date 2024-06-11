
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.internal.creation.instance.InstantiationException;
import org.mockito.internal.util.StringJoiner;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ByteBuddyMockMakerTest {

    @Test
    public void testCreateMock() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<Object> settings = new MockCreationSettings<>(Object.class);
        MockHandler handler = new MockHandler() {};

        Object mock = mockMaker.createMock(settings, handler);

        assertNotNull(mock);
        assertTrue(mock instanceof Object);
    }

    @Test
    public void testCreateMockWithSerializableModeAcrossClassloaders() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<Object> settings = new MockCreationSettings<>(Object.class);
        settings.serializableMode(SerializableMode.ACROSS_CLASSLOADERS);
        MockHandler handler = new MockHandler() {};

        try {
            mockMaker.createMock(settings, handler);
        } catch (MockitoException e) {
            assertEquals("Serialization across classloaders not yet supported with ByteBuddyMockMaker", e.getMessage());
        }
    }

    @Test
    public void testGetHandler() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object mock = mockMaker.createMock(new MockCreationSettings<>(Object.class), new MockHandler() {});

        MockHandler handler = mockMaker.getHandler(mock);

        assertNotNull(handler);
        assertTrue(handler instanceof MockHandler);
    }

}

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.invocation.MockHandler;
import org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker;
import org.mockito.internal.creation.bytebuddy.ClassInstantiator;
import org.mockito.internal.creation.bytebuddy.CachingMockBytecodeGenerator;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.InstantiationException;
import org.mockito.internal.util.StringJoiner;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.Spy;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker mockMaker;
    @Captor
    private ArgumentCaptor<MockCreationSettings> settingsCaptor;
    private MockHandler mockHandler;
    private MockCreationSettings settings;

    @Before
    public void setup() {
        mockMaker = new ByteBuddyMockMaker();
        settings = new MockCreationSettings<>(String.class, SerializableMode.PROXY);
        mockHandler = mock(MockHandler.class);
    }

    @Test
    public void testCreateMock() {
        // Happy path
        ByteBuddyMockMaker byteBuddyMockMaker = spy(mockMaker);
        when(byteBuddyMockMaker.createMock(settingsCaptor.capture(settings), mockHandler)).thenCallRealMethod();
        String mock = byteBuddyMockMaker.createMock(settings, mockHandler);
        verify(byteBuddyMockMaker, times(1)).createMock(settingsCaptor.capture(settings), mockHandler);
        assertEquals(String.class, settingsCaptor.getValue().getTypeToMock());
        assertTrue(mock instanceof String);

        // Edge case: SerializableMode.ACROSS_CLASSLOADERS
        settings.setSerializableMode(SerializableMode.ACROSS_CLASSLOADERS);
        try {
            byteBuddyMockMaker.createMock(settings, mockHandler);
            fail();
        } catch (MockitoException e) {
            assertEquals("Serialization across classloaders not yet supported with ByteBuddyMockMaker", e.getMessage());
        }

        // Error case: InstantiationException
        doThrow(new InstantiationException("Unable to create mock instance of type 'ClassA'")).when(byteBuddyMockMaker)
                .createMock(settings, mockHandler);
        try {
            byteBuddyMockMaker.createMock(settings, mockHandler);
            fail();
        } catch (MockitoException e) {
            assertEquals("Unable to create mock instance of type 'ClassA'", e.getMessage());
        }
    }

    @Test
    public void testGetHandler() {
        String mock = mockMaker.createMock(settings, mockHandler);
        MockHandler handler = mockMaker.getHandler(mock);
        assertNotNull(handler);
        assertEquals(mockHandler, handler);
    }

    @Test
    public void testResetMock() {
        String mock = mockMaker.createMock(settings, mockHandler);
        MockHandler newHandler = mock(MockHandler.class);
        mockMaker.resetMock(mock, newHandler, settings);
        MockHandler newMockHandler = mockMaker.getHandler(mock);
        assertEquals(newHandler, newMockHandler);
    }
}
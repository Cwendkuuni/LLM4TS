import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.creation.instance.*;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;
import org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker;

public class ByteBuddyMockMakerTest {

    @Test
    public void testCreateMock_basicFunctionality() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();

        MockCreationSettings settings = Mockito.mock(MockCreationSettings.class);
        MockHandler handler = Mockito.mock(MockHandler.class);

        assertNotNull(byteBuddyMockMaker.createMock(settings, handler));
    }

    @Test
    public void testCreateMock_SerializationAcrossClassLoadersException() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();

        MockCreationSettings settings = Mockito.mock(MockCreationSettings.class);
        MockHandler handler = Mockito.mock(MockHandler.class);
        when(settings.getSerializableMode()).thenReturn(SerializableMode.ACROSS_CLASSLOADERS);

        try {
            byteBuddyMockMaker.createMock(settings, handler);
            fail("Expected MockitoException not thrown");
        } catch (MockitoException e) {
            assertEquals("Serialization across classloaders not yet supported with ByteBuddyMockMaker", e.getMessage());
        }
    }

    @Test
    public void testGetHandler_NotInstanceOfMockAccess() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();

        Object mock = new Object();

        assertNull(byteBuddyMockMaker.getHandler(mock));
    }

    @Test
    public void testEnsureMockIsAssignableToMockedType() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();

        MockCreationSettings settings = Mockito.mock(MockCreationSettings.class);
        Object mock = new Object();

        assertNotNull(byteBuddyMockMaker.ensureMockIsAssignableToMockedType(settings, mock));
    }

    @Test
    public void testResetMock() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();

        Object mock = mock(Object.class);
        MockHandler newHandler = mock(MockHandler.class);
        MockCreationSettings settings = mock(MockCreationSettings.class);

        byteBuddyMockMaker.resetMock(mock, newHandler, settings);

        assertNotNull(byteBuddyMockMaker.getHandler(mock));
    }
}
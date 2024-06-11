
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ByteBuddyMockMakerTest {

    @Test
    public void createMock_validSettings_mockCreated() {
        // Create the mock creation settings
        MockCreationSettings<String> settings = mock(MockCreationSettings.class);
        when(settings.getSerializableMode()).thenReturn(SerializableMode.NONE);
        when(settings.getTypeToMock()).thenReturn(String.class);

        // Create the mock handler
        MockMaker.MockHandler handler = mock(MockMaker.MockHandler.class);

        // Create the ByteBuddyMockMaker instance
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();

        // Create the mock
        String mock = mockMaker.createMock(settings, handler);

        // Verify that the mock was created
        assertNotNull(mock);
        assertTrue(mock instanceof String);
    }

    @Test(expected = MockitoException.class)
    public void createMock_serializableSettings_exceptionThrown() {
        // Create the mock creation settings
        MockCreationSettings<Object> settings = mock(MockCreationSettings.class);
        when(settings.getSerializableMode()).thenReturn(SerializableMode.ACROSS_CLASSLOADERS);

        // Create the mock handler
        MockMaker.MockHandler handler = mock(MockMaker.MockHandler.class);

        // Create the ByteBuddyMockMaker instance
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();

        // Try to create the mock
        mockMaker.createMock(settings, handler);
    }

    @Test
    public void ensureMockIsAssignableToMockedType_mockIsValid_mockIsAssignable() {
        // Create the mock creation settings
        MockCreationSettings<String> settings = mock(MockCreationSettings.class);
        when(settings.getTypeToMock()).thenReturn(String.class);

        // Create the ByteBuddyMockMaker instance
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();

        // Create the mock
        String mock = mockMaker.createMock(settings, null);

        // Ensure the mock is assignable to the mocked type
        String assignedMock = mockMaker.ensureMockIsAssignableToMockedType(settings, mock);
        assertNotNull(assignedMock);
    }

    @Test(expected = ClassCastException.class)
    public void ensureMockIsAssignableToMockedType_mockIsNotAssignable_exceptionThrown() {
        // Create the mock creation settings
        MockCreationSettings<Integer> settings = mock(MockCreationSettings.class);
        when(settings.getTypeToMock()).thenReturn(Integer.class);

        // Create the ByteBuddyMockMaker instance
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();

        // Create the mock
        String mock = "mock";

        // Ensure the mock is assignable to the mocked type
        mockMaker.ensureMockIsAssignableToMockedType(settings, mock);
    }

    @Test
    public void getHandler_validMock_mockHandlerReturned() {
        // Create the mock
        MockMaker.MockAccess mock = mock(MockMaker.MockAccess.class);
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();

        // Get the mock handler
        MockMaker.MockHandler handler = mockMaker.getHandler(mock);

        // Assert that the mock handler is returned
        assertNotNull(handler);
        assertTrue(handler instanceof MockMaker.MockHandler);
    }

    @Test
    public void getHandler_invalidMock_nullReturned() {
        // Create the mock
        Object mock = new Object();
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();

        // Get the mock handler
        MockMaker.MockHandler handler = mockMaker.getHandler(mock);

        // Assert that null is returned
        assertNull(handler);
    }

    @Test
    public void resetMock_validMock_mockReset() {
        // Create the mock
        MockMaker.MockAccess mock = mock(MockMaker.MockAccess.class);
        MockMaker.MockHandler newHandler = mock(MockMaker.MockHandler.class);
        MockCreationSettings settings = mock(MockCreationSettings.class);
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();

        // Reset the mock
        mockMaker.resetMock(mock, newHandler, settings);

        // Verify that the mock's handler is set to the new handler
        verify(mock).setMockitoInterceptor(any());
    }
}

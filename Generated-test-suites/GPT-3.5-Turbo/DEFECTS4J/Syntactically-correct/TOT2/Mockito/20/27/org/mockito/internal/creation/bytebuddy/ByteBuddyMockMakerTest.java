import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ByteBuddyMockMakerTest {

    @Test
    public void testCreateMock() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<?> settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);

        // Testing if a mock instance is created successfully
        assertNotNull(mockMaker.createMock(settings, handler));
    }

    @Test
    public void testEnsureMockIsAssignableToMockedType() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<?> settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);
        Object mock = mockMaker.createMock(settings, handler);

        // Testing if the created mock is assignable to the type it was supposed to mock
        assertTrue(settings.getTypeToMock().isInstance(mockMaker.ensureMockIsAssignableToMockedType(settings, mock)));
    }

    @Test
    public void testGetHandler() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockHandler handler = mock(MockHandler.class);
        Object mock = mockMaker.createMock(mock(MockCreationSettings.class), handler);

        // Testing if the mock handler retrieved is correct
        assertEquals(handler, mockMaker.getHandler(mock));
    }

    @Test
    public void testResetMock() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockHandler handler = mock(MockHandler.class);
        Object mock = mockMaker.createMock(mock(MockCreationSettings.class), handler);

        // Testing if the mock handler is successfully reset
        MockHandler newHandler = mock(MockHandler.class);
        mockMaker.resetMock(mock, newHandler, mock(MockCreationSettings.class));
        assertEquals(newHandler, mockMaker.getHandler(mock));
    }

    @Test(expected = MockitoException.class)
    public void testAsInternalMockHandler() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockHandler handler = mock(MockHandler.class);

        // Testing if an exception is thrown when providing own implementation of MockHandler
        mockMaker.asInternalMockHandler(handler);
    }
}
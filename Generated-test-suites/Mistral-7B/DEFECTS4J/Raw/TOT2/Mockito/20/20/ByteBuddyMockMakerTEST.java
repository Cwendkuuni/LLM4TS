import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockHandler;
import org.mockito.MockCreationSettings;

public class ByteBuddyMockMakerTest {

    @Test
    public void testCreateMock() {
        MockHandler mockHandler = Mockito.mock(MockHandler.class);
        MockCreationSettings settings = new MockCreationSettings<Object>() {};
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        Object mock = byteBuddyMockMaker.createMock(settings, mockHandler);
        assertNotNull(mock);
        assertEquals(mockHandler.getClass(), mock.getClass());
    }
}
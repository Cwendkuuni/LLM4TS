import org.junit.Test;
import static org.mockito.Mockito.*;

public class MockitoCoreTest {
    @Test
    public void testMock() {
        // Test normal usage
        MockSettings mockSettings = new MockSettings();
        MyMock mock = mock(MyMock.class, mockSettings);
        // Use the mock as desired

        // Test null classToMock
        assertNull(mock(null, mockSettings));

        // Test null mockSettings
        assertNull(mock(MyMock.class, null));

        // Test already mocked object
        MyMock existingMock = mock(MyMock.class, mockSettings);
        assertSame(existingMock, mock(MyMock.class, mockSettings));

        // Test exception reporting
        try {
            mock(Object.class, mockSettings);
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
}
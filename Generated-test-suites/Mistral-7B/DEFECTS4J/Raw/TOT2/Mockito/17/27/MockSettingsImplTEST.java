import static org.junit.Assert.*;
import org.mockito.MockSettings;

public class MockSettingsImplTest {

    @Test
    public void testSerializable() {
        MockSettings mockSettings = new MockSettingsImpl().serializable();
        assertTrue(mockSettings.isSerializable());
    }
}
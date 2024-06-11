import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

public class MockSettingsImplTest {

    @Test
    public void testSerializableMethod() {
        MockSettings mockSettings = Mockito.spy(new MockSettingsImpl());
        mockSettings.serializable();
        verify(mockSettings).extraInterfaces(java.io.Serializable.class);
    }
}
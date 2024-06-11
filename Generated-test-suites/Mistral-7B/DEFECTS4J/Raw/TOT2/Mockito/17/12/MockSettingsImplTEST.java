import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.assertThat;

public class MockSettingsImplTest {
    @Test
    public void testSerializable() {
        MockSettings settings = Mockito.spy(MockSettingsImpl.class);
        settings.serializable();
        assertThat(settings.isSerializable()).isTrue();
    }
}
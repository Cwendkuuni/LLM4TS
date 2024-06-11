import org.junit.Test;
import org.mockito.MockSettings;
import org.mockito.internal.creation.MockSettingsImpl;

public class MockSettingsImplTest {

    @Test
    public void testSerializable() {
        MockSettings mockSettings = new MockSettingsImpl().serializable();
        Class<?>[] serializableInterfaces = mockSettings.getExtraInterfaces();
        boolean isSerializable = mockSettings.isSerializable();

        assertNotNull(serializableInterfaces);
        assertTrue(isSerializable);
        assertArrayEquals(new Class<?>[]{java.io.Serializable.class}, serializableInterfaces);
    }
}
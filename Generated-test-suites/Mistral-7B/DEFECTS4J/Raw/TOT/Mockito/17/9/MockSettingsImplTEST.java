// Test case for serializable() method
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import org.mockito.MockSettings;

public class MockSettingsImplTest {

  @Test
  public void testSerializable() {
    MockSettings settings = mock(MockSettings.class);
    MockSettings serializableSettings = settings.serializable();

    // Test serializable() returns the same instance
    assertSame(settings, serializableSettings);

    // Test serializable() sets the serializable flag correctly
    Class<?> serializableInterface = java.io.Serializable.class;
    settings.extraInterfaces(serializableInterface);
    assertTrue(serializableSettings.isSerializable());

    // Test serializable() allows setting multiple serializable interfaces
    Class<?>[] serializableInterfaces = new Class<?>[]{java.io.Serializable.class, java.io.Externalizable.class};
    settings.extraInterfaces(serializableInterfaces);
    assertTrue(serializableSettings.isSerializable());
  }

  // Test case for extraInterfaces() method goes here
  // ...
}
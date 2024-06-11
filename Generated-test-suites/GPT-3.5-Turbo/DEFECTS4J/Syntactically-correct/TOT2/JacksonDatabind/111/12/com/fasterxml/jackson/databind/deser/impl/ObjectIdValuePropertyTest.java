import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectIdValuePropertyTest {

  @Test
  public void testWithNullProvider() {
    ObjectIdReader objectIdReader = new ObjectIdReader("id", Long.class);
    ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
    
    ObjectIdValueProperty newProperty = objectIdValueProperty.withNullProvider(NullProvider.getInstance());
    
    assertNotNull(newProperty);
    assertEquals(NullProvider.getInstance(), newProperty.getNullProvider());
  }

  @Test
  public void testWithDifferentValueDeserializer() {
    ObjectIdReader objectIdReader = new ObjectIdReader("id", Long.class);
    ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

    JsonDeserializer<?> deserializer = new CustomDeserializer();
    ObjectIdValueProperty newProperty = objectIdValueProperty.withValueDeserializer(deserializer);
    
    assertNotNull(newProperty);
    assertEquals(deserializer, newProperty.getValueDeserializer());
  }

  /**
   * Custom Deserializer class used for testing
   */
  private static class CustomDeserializer extends JsonDeserializer<Object> {
    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
      // Custom implementation for testing
      return null;
    }
  }
}
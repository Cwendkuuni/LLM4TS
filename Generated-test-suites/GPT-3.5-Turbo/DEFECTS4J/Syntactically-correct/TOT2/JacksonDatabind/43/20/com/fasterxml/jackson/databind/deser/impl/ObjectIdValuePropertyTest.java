import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectIdValuePropertyTest {
  
  @Test
  public void testWithName() {
    // Test the withName method of ObjectIdValueProperty
    ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
    ObjectIdValueProperty newProperty = property.withName(new PropertyName("newName"));
    
    assertEquals(new PropertyName("newName"), newProperty.getFullName());
  }
  
  @Test
  public void testWithValueDeserializer() {
    // Test the withValueDeserializer method of ObjectIdValueProperty
    ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
    JsonDeserializer<?> deserializer = new StdDeserializer<>(Object.class);
    ObjectIdValueProperty newProperty = property.withValueDeserializer(deserializer);
    
    assertEquals(deserializer, newProperty.getValueDeserializer());
  }
  
  // More test cases for other methods
  
}
import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectIdValuePropertyTest {
  
  @Test
  public void testWithNullProvider() {
    // Create test object
    ObjectIdReader objectIdReader = new ObjectIdReader();
    PropertyMetadata metadata = new PropertyMetadata();
    ObjectIdValueProperty objectIdProperty = new ObjectIdValueProperty(objectIdReader, metadata);

    // Test withNullProvider method
    NullValueProvider nva = new NullValueProvider();
    SettableBeanProperty newProperty = objectIdProperty.withNullProvider(nva);

    // Check if the method returns a new SettableBeanProperty
    assertNotNull(newProperty);
    assertEquals(nva, newProperty.getNullProvider());
  }

  // Add more test cases for other public methods

  @Test
  public void testSetAndReturn_exception() {
    // Create test object
    ObjectIdReader objectIdReader = new ObjectIdReader();
    PropertyMetadata metadata = new PropertyMetadata();
    ObjectIdValueProperty objectIdProperty = new ObjectIdValueProperty(objectIdReader, metadata);

    // Create dummy instance and value
    Object instance = new Object();
    Object value = new Object();

    try {
      // Try calling setAndReturn which should throw UnsupportedOperationException
      objectIdProperty.setAndReturn(instance, value);
      fail("Expected UnsupportedOperationException was not thrown");
    } catch (UnsupportedOperationException e) {
      // Expected exception
    }
  }
}
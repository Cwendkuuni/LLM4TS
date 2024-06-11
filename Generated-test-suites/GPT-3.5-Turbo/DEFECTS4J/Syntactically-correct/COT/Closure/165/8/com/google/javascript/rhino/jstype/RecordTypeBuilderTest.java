import org.junit.Test;

public class RecordTypeBuilderTest {

  // Test addProperty method
  @Test
  public void testAddProperty() {
    // Create a JSTypeRegistry mock object
    JSTypeRegistry registry = mock(JSTypeRegistry.class);

    // Create a RecordTypeBuilder object
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);

    // Add new property with name and type
    String propertyName = "property";
    JSType propertyType = new JSType();
    Node propertyNode = new Node();

    // Test adding a new property
    RecordTypeBuilder result = builder.addProperty(propertyName, propertyType, propertyNode);
    assertNotNull(result);
  }

  // Test build method
  @Test
  public void testBuild() {
    // Create a JSTypeRegistry mock object
    JSTypeRegistry registry = mock(JSTypeRegistry.class);

    // Create a RecordTypeBuilder object
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);

    // Test building an empty record
    JSType result = builder.build();
    assertNotNull(result);

    // Test building a non-empty record

    // Add new property with name and type
    String propertyName = "property";
    JSType propertyType = new JSType();
    Node propertyNode = new Node();

    builder.addProperty(propertyName, propertyType, propertyNode);

    result = builder.build();
    assertNotNull(result);
  }
}
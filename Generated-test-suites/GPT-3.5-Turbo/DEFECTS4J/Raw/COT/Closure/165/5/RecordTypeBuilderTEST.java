import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeBuilderTest {
  private RecordTypeBuilder builder;

  @Before
  public void setUp() {
    builder = new RecordTypeBuilder(new JSTypeRegistry());
  }

  @Test
  public void testAddProperty() {
    JSType type = new JSType();
    Node propertyNode = new Node();
    RecordTypeBuilder returnedBuilder = builder.addProperty("name", type, propertyNode);
    assertNotNull(returnedBuilder);
    // Property should be added to properties map
    assertEquals(builder.properties.size(), 1);
    // Property should have the correct type and property node
    assertEquals(builder.properties.get("name").getType(), type);
    assertEquals(builder.properties.get("name").getPropertyNode(), propertyNode);
  }

  @Test
  public void testBuildEmptyRecord() {
    JSType type = builder.build();
    // If no properties were added, should return native object type
    assertEquals(type, builder.registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
  }

  @Test
  public void testBuildNonEmptyRecord() {
    // Add a property to the builder
    JSType type = new JSType();
    Node propertyNode = new Node();
    builder.addProperty("name", type, propertyNode);
    // Build the record
    JSType recordType = builder.build();
    // Record type should not be native object type
    assertNotEquals(recordType, builder.registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
    // Record type should be a RecordType object
    assertTrue(recordType instanceof RecordType);
    // Record type should have the correct properties
    assertEquals(((RecordType) recordType).getProperties().size(), 1);
  }
}
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class RecordTypeBuilderTest {

  private RecordTypeBuilder builder;
  private JSTypeRegistry registry;
  private JSType type;
  private Node propertyNode;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    builder = new RecordTypeBuilder(registry);
    type = registry.createStringType();
    propertyNode = new Node();
  }

  @Test
  public void testAddProperty() {
    RecordTypeBuilder result = builder.addProperty("name", type, propertyNode);
    assertNotNull(result);
  }

  @Test
  public void testAddDuplicateProperty() {
    builder.addProperty("name", type, propertyNode);
    RecordTypeBuilder result = builder.addProperty("name", type, propertyNode);
    assertNull(result);
  }

  @Test
  public void testBuildEmptyRecord() {
    JSType result = builder.build();
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), result);
  }

  @Test
  public void testBuildNonEmptyRecord() {
    builder.addProperty("name", type, propertyNode);
    JSType result = builder.build();
    
    // Test if result is an instance of RecordType
    assertTrue(result instanceof RecordType);
    
    // Test if properties are set correctly
    HashMap<String, RecordProperty> properties = ((RecordType) result).getProperties();
    assertEquals(1, properties.size());
    assertTrue(properties.containsKey("name"));
    assertEquals(type, properties.get("name").getType());
    assertEquals(propertyNode, properties.get("name").getPropertyNode());
  }

}
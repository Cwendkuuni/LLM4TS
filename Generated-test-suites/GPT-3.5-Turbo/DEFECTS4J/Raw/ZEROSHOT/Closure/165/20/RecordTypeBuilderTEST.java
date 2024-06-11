import org.junit.*;
import static org.junit.Assert.*;

public class RecordTypeBuilderTest {

  @Test
  public void testAddProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    
    // Test adding a new property
    JSType type1 = new JSType();
    Node propertyNode1 = new Node();
    builder.addProperty("property1", type1, propertyNode1);
    HashMap<String, RecordTypeBuilder.RecordProperty> properties1 = builder.getProperties();
    assertTrue(properties1.containsKey("property1"));
    assertEquals(type1, properties1.get("property1").getType());
    assertEquals(propertyNode1, properties1.get("property1").getPropertyNode());
    
    // Test adding a duplicate property
    JSType type2 = new JSType();
    Node propertyNode2 = new Node();
    RecordTypeBuilder builder2 = new RecordTypeBuilder(registry);
    builder2.addProperty("property1", type2, propertyNode2);
    assertNull(builder2.addProperty("property1", type2, propertyNode2));
    HashMap<String, RecordTypeBuilder.RecordProperty> properties2 = builder2.getProperties();
    assertTrue(properties2.containsKey("property1"));
    assertEquals(type2, properties2.get("property1").getType());
    assertEquals(propertyNode2, properties2.get("property1").getPropertyNode());
  }

  @Test
  public void testBuild() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder emptyBuilder = new RecordTypeBuilder(registry);
    
    // Test building an empty record
    JSType emptyRecord = emptyBuilder.build();
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), emptyRecord);
    
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    JSType type1 = new JSType();
    Node propertyNode1 = new Node();
    builder.addProperty("property1", type1, propertyNode1);
    
    // Test building a non-empty record
    JSType record = builder.build();
    assertNotNull(record);
    assertEquals(builder.getProperties(), record.getProperties());
  }

}
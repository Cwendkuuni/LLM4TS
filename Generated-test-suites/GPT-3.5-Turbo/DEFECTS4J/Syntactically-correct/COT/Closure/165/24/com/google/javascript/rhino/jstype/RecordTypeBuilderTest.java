import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeBuilderTest {

  @Test
  public void testAddProperty() {
    // Create JSTypeRegistry object
    JSTypeRegistry registry = new JSTypeRegistry();
    // Create RecordTypeBuilder object
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    
    // Test adding a property with unique name and type
    JSType type = new JSType(registry);
    Node propertyNode = new Node();
    RecordTypeBuilder result = builder.addProperty("name1", type, propertyNode);
    assertNotNull(result); // The builder should be returned as result
    // Check if the property was added correctly
    assertEquals(builder.getProperties().size(), 1);
    
    // Test adding a property with duplicate name
    JSType type2 = new JSType(registry);
    Node propertyNode2 = new Node();
    RecordTypeBuilder result2 = builder.addProperty("name1", type2, propertyNode2);
    assertNull(result2); // null should be returned as result
    assertEquals(builder.getProperties().size(), 1); // The property should not be added
  }

  @Test
  public void testBuild() {
    // Create JSTypeRegistry object
    JSTypeRegistry registry = new JSTypeRegistry();
    // Create RecordTypeBuilder object
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    
    // Test building an empty record
    JSType result1 = builder.build();
    assertNotNull(result1); // A JSType object should be returned
    assertEquals(result1.getType(), JSTypeNative.OBJECT_TYPE); // The type should be OBJECT_TYPE
    
    // Test building a non-empty record
    JSType type1 = new JSType(registry);
    Node propertyNode1 = new Node();
    builder.addProperty("name1", type1, propertyNode1);
    JSType result2 = builder.build();
    assertNotNull(result2); // A JSType object should be returned
    assertEquals(result2.getType(), JSTypeNative.RECORD_TYPE); // The type should be RECORD_TYPE
  }

}
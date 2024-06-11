import static org.junit.Assert.*;
import org.junit.Test;

public class RecordTypeBuilderTest {
  
  @Test
  public void testAddProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    JSType type = new JSType();
    Node node = new Node();
    
    // Adding a property to the record type
    RecordTypeBuilder result = builder.addProperty("testProperty", type, node);
    
    // Checking if the property was successfully added
    assertNotNull(result);
  }

  @Test
  public void testBuildEmptyRecord() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    
    // Building an empty record
    JSType record = builder.build();
    
    // Checking if an empty record returns the object type
    assertEquals(JSTypeNative.OBJECT_TYPE, record.getType());
  }

  @Test
  public void testBuildRecordWithProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    JSType type = new JSType();
    Node node = new Node();
    
    // Adding properties to the record type
    builder.addProperty("property1", type, node);
    builder.addProperty("property2", type, node);
    
    // Building the record with properties
    JSType record = builder.build();
    
    // Checking if the record contains the added properties
    assertTrue(record.getProperties().containsKey("property1"));
    assertTrue(record.getProperties().containsKey("property2"));
  }
  
}
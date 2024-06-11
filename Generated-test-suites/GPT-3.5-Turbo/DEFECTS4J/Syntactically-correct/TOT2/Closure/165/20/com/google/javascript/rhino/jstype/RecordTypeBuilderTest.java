import static org.junit.Assert.*;
import org.junit.Test;

public class RecordTypeBuilderTest {

  @Test
  public void testAddProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    JSType stringType = new JSType("String");
    Node propertyNode = new Node();
    
    // Test adding a property with valid name and type
    RecordTypeBuilder result = builder.addProperty("name", stringType, propertyNode);
    assertNotNull(result);
    
    // Test adding a property with duplicate name
    result = builder.addProperty("name", stringType, propertyNode);
    assertNull(result);
  }

  @Test
  public void testBuild() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    JSType stringType = new JSType("String");
    Node propertyNode = new Node();
    
    // Test building an empty record
    JSType emptyRecord = builder.build();
    assertEquals(JSTypeNative.OBJECT_TYPE, emptyRecord.getJSType());
    
    // Test building a record with properties
    builder.addProperty("name", stringType, propertyNode);
    JSType recordWithProperties = builder.build();
    // Assert record type
    assertEquals(JSTypeNative.RECORD_TYPE, recordWithProperties.getJSType());
    // Assert properties count
    assertTrue(recordWithProperties.getProperties().containsKey("name"));
  }
  
}
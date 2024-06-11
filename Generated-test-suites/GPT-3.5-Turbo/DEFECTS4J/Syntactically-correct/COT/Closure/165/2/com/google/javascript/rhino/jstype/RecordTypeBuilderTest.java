import junit.framework.TestCase;

public class RecordTypeBuilderTest extends TestCase {
  
  public void testAddProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    
    // Test adding a new property
    assertTrue(builder.addProperty("name", registry.getNativeType(JSTypeNative.STRING_TYPE), new Node()));
    
    // Test adding a duplicate property
    assertFalse(builder.addProperty("name", registry.getNativeType(JSTypeNative.STRING_TYPE), new Node()));
  }
  
  public void testBuild() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    
    // Test building an empty record
    JSType emptyRecord = builder.build();
    assertTrue(emptyRecord instanceof ObjectType);
    
    // Test building a record with properties
    builder.addProperty("name", registry.getNativeType(JSTypeNative.STRING_TYPE), new Node());
    JSType record = builder.build();
    assertTrue(record instanceof RecordType);
  }
  
  public void testRecordProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder.RecordProperty property = new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), new Node());
    
    // Test getting the type of the record property
    assertEquals(registry.getNativeType(JSTypeNative.STRING_TYPE), property.getType());
    
    // Test getting the property node of the record property
    assertNotNull(property.getPropertyNode());
  }
  
  // Additional test cases for edge cases and exception handling scenarios
  // ...
  
}
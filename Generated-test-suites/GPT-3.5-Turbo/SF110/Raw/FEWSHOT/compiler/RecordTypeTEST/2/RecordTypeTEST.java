import org.junit.Test;

public class RecordTypeTest {
  @Test
  public void testConstructorWithProperties() {
    RecordProperty property1 = new RecordProperty();
    property1.setType(JSTypeRegistry.get().getNativeType(JSTypeNative.STRING_TYPE));
    property1.setPropertyNode(null);
    
    RecordProperty property2 = new RecordProperty();
    property2.setType(JSTypeRegistry.get().getNativeType(JSTypeNative.BOOLEAN_TYPE));
    property2.setPropertyNode(null);
    
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("property1", property1);
    properties.put("property2", property2);
    
    RecordType recordType = new RecordType(JSTypeRegistry.get(), properties);
    
    assertTrue(recordType.hasOwnProperty("property1"));
    assertTrue(recordType.hasOwnProperty("property2"));
    assertEquals(JSTypeRegistry.get().getNativeType(JSTypeNative.STRING_TYPE), recordType.getPropertyType("property1"));
    assertEquals(JSTypeRegistry.get().getNativeType(JSTypeNative.BOOLEAN_TYPE), recordType.getPropertyType("property2"));
  }
  
  @Test
  public void testConstructorWithPropertiesAndDeclaredFalse() {
    RecordProperty property1 = new RecordProperty();
    property1.setType(JSTypeRegistry.get().getNativeType(JSTypeNative.STRING_TYPE));
    property1.setPropertyNode(null);
    
    RecordProperty property2 = new RecordProperty();
    property2.setType(JSTypeRegistry.get().getNativeType(JSTypeNative.BOOLEAN_TYPE));
    property2.setPropertyNode(null);
    
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("property1", property1);
    properties.put("property2", property2);
    
    RecordType recordType = new RecordType(JSTypeRegistry.get(), properties, false);
    
    assertTrue(recordType.hasOwnProperty("property1"));
    assertTrue(recordType.hasOwnProperty("property2"));
    assertEquals(JSTypeRegistry.get().getNativeType(JSTypeNative.STRING_TYPE), recordType.getPropertyType("property1"));
    assertEquals(JSTypeRegistry.get().getNativeType(JSTypeNative.BOOLEAN_TYPE), recordType.getPropertyType("property2"));
    assertTrue(recordType.isSynthetic());
  }
  
  @Test
  public void testIsSubtype() {
    RecordProperty property1 = new RecordProperty();
    property1.setType(JSTypeRegistry.get().getNativeType(JSTypeNative.STRING_TYPE));
    property1.setPropertyNode(null);
    
    RecordProperty property2 = new RecordProperty();
    property2.setType(JSTypeRegistry.get().getNativeType(JSTypeNative.BOOLEAN_TYPE));
    property2.setPropertyNode(null);
    
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("property1", property1);
    properties.put("property2", property2);
    
    RecordType recordType1 = new RecordType(JSTypeRegistry.get(), properties);
    
    // Subtype with same properties
    RecordType recordType2 = new RecordType(JSTypeRegistry.get(), properties);
    assertTrue(recordType1.isSubtype(recordType2));
    
    // Subtype with additional property
    RecordProperty property3 = new RecordProperty();
    property3.setType(JSTypeRegistry.get().getNativeType(JSTypeNative.NUMBER_TYPE));
    property3.setPropertyNode(null);
    
    Map<String, RecordProperty> properties2 = new HashMap<>();
    properties2.putAll(properties);
    properties2.put("property3", property3);
    
    RecordType recordType3 = new RecordType(JSTypeRegistry.get(), properties2);
    assertTrue(recordType1.isSubtype(recordType3));
    
    // Not a subtype
    RecordProperty property4 = new RecordProperty();
    property4.setType(JSTypeRegistry.get().getNativeType(JSTypeNative.STRING_TYPE));
    property4.setPropertyNode(null);
    
    Map<String, RecordProperty> properties3 = new HashMap<>();
    properties3.put("property4", property4);
    
    RecordType recordType4 = new RecordType(JSTypeRegistry.get(), properties3);
    assertFalse(recordType1.isSubtype(recordType4));
  }
}
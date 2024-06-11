import static org.junit.Assert.*;
import org.junit.Test;

public class RecordTypeTest {
  
  @Test
  public void testDefineDeclaredProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
    properties.put("property1", new RecordTypeBuilder.RecordProperty(
        registry.getNativeType(JSTypeNative.STRING_TYPE), null));
    RecordType recordType = new RecordType(registry, properties);
    
    assertTrue(recordType.hasOwnProperty("property1"));
  }
  
  @Test
  public void testDefineSynthesizedProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
    properties.put("property1", new RecordTypeBuilder.RecordProperty(
        registry.getNativeType(JSTypeNative.STRING_TYPE), null));
    RecordType recordType = new RecordType(registry, properties, false);
    
    assertTrue(recordType.hasOwnProperty("property1"));
  }
  
  @Test
  public void testIsSynthetic() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
    properties.put("property1", new RecordTypeBuilder.RecordProperty(
        registry.getNativeType(JSTypeNative.STRING_TYPE), null));
    RecordType recordType = new RecordType(registry, properties, true);
    
    assertFalse(recordType.isSynthetic());
  }
  
  @Test
  public void testCheckRecordEquivalenceHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
    properties.put("property1", new RecordTypeBuilder.RecordProperty(
        registry.getNativeType(JSTypeNative.STRING_TYPE), null));
    RecordType recordType1 = new RecordType(registry, properties);
    RecordType recordType2 = new RecordType(registry, properties);
    
    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.IGNORE));
  }
  
  @Test
  public void testGetPropertyType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
    properties.put("property1", new RecordTypeBuilder.RecordProperty(
        registry.getNativeType(JSTypeNative.STRING_TYPE), null));
    RecordType recordType = new RecordType(registry, properties);
    
    assertEquals(registry.getNativeType(JSTypeNative.STRING_TYPE),
                 recordType.getPropertyType("property1"));
  }
  
  @Test
  public void testGetOwnPropertyNames() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
    properties.put("property1", new RecordTypeBuilder.RecordProperty(
        registry.getNativeType(JSTypeNative.STRING_TYPE), null));
    RecordType recordType = new RecordType(registry, properties);
    Set<String> expected = new HashSet<>();
    expected.add("property1");
    
    assertEquals(expected, recordType.getOwnPropertyNames());
  }
  
  @Test
  public void testDefineProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
    properties.put("property1", new RecordTypeBuilder.RecordProperty(
        registry.getNativeType(JSTypeNative.STRING_TYPE), null));
    RecordType recordType = new RecordType(registry, properties);
    
    assertFalse(recordType.defineProperty("property2", 
                                          registry.getNativeType(JSTypeNative.BOOLEAN_TYPE), 
                                          true, null));
  }
  
  @Test
  public void testGetGreatestSubtypeHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
    properties.put("property1", new RecordTypeBuilder.RecordProperty(
        registry.getNativeType(JSTypeNative.STRING_TYPE), null));
    properties.put("property2", new RecordTypeBuilder.RecordProperty(
        registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
    RecordType recordType = new RecordType(registry, properties);
    JSType that = new JSType(registry, registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
    
    assertEquals(registry.getNativeType(JSTypeNative.OBJECT_TYPE), 
                 recordType.getGreatestSubtypeHelper(that));
  }
  
  @Test
  public void testToMaybeRecordType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
    properties.put("property1", new RecordTypeBuilder.RecordProperty(
        registry.getNativeType(JSTypeNative.STRING_TYPE), null));
    RecordType recordType = new RecordType(registry, properties);
    
    assertEquals(recordType, recordType.toMaybeRecordType());
  }
  
  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
    properties.put("property1", new RecordTypeBuilder.RecordProperty(
        registry.getNativeType(JSTypeNative.STRING_TYPE), null));
    RecordType recordType1 = new RecordType(registry, properties);
    RecordType recordType2 = new RecordType(registry, properties);
    
    assertTrue(recordType1.isSubtype(recordType2));
  }
  
  @Test
  public void testIsSubtypeWithNonRecordType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
    properties.put("property1", new RecordTypeBuilder.RecordProperty(
        registry.getNativeType(JSTypeNative.STRING_TYPE), null));
    RecordType recordType = new RecordType(registry, properties);
    JSType that = new JSType(registry, registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
    
    assertTrue(recordType.isSubtype(that));
  }
  
  @Test
  public void testIsSubtypeWithRecordType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordTypeBuilder.RecordProperty> properties1 = new HashMap<>();
    properties1.put("property1", new RecordTypeBuilder.RecordProperty(
        registry.getNativeType(JSTypeNative.STRING_TYPE), null));
    RecordType recordType1 = new RecordType(registry, properties1);
    Map<String, RecordTypeBuilder.RecordProperty> properties2 = new HashMap<>();
    properties2.put("property1", new RecordTypeBuilder.RecordProperty(
        registry.getNativeType(JSTypeNative.STRING_TYPE), null));
    properties2.put("property2", new RecordTypeBuilder.RecordProperty(
        registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
    RecordType recordType2 = new RecordType(registry, properties2);
    
    assertTrue(recordType1.isSubtype(recordType2));
  }
  
  @Test
  public void testIsSubtypeWithNonEquivalentRecordType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordTypeBuilder.RecordProperty> properties1 = new HashMap<>();
    properties1.put("property1", new RecordTypeBuilder.RecordProperty(
        registry.getNativeType(JSTypeNative.STRING_TYPE), null));
    RecordType recordType1 = new RecordType(registry, properties1);
    Map<String, RecordTypeBuilder.RecordProperty> properties2 = new HashMap<>();
    properties2.put("property1", new RecordTypeBuilder.RecordProperty(
        registry.getNativeType(JSTypeNative.STRING_TYPE), null));
    properties2.put("property2", new RecordTypeBuilder.RecordProperty(
        registry.getNativeType(JSTypeNative.OBJECT_TYPE), null));
    RecordType recordType2 = new RecordType(registry, properties2);
    
    assertFalse(recordType1.isSubtype(recordType2));
  }
  
  @Test
  public void testIsSubtypeWithNonMatchingProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordTypeBuilder.RecordProperty> properties1 = new HashMap<>();
    properties1.put("property1", new RecordTypeBuilder.RecordProperty(
        registry.getNativeType(JSTypeNative.STRING_TYPE), null));
    RecordType recordType1 = new RecordType(registry, properties1);
    Map<String, RecordTypeBuilder.RecordProperty> properties2 = new HashMap<>();
    properties2.put("property1", new RecordTypeBuilder.RecordProperty(
        registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
    properties2.put("property2", new RecordTypeBuilder.RecordProperty(
        registry.getNativeType(JSTypeNative.OBJECT_TYPE), null));
    RecordType recordType2 = new RecordType(registry, properties2);
    
    assertFalse(recordType1.isSubtype(recordType2));
  }
}
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashMap;
import java.util.Map;

public class RecordTypeTest {

  @Test
  public void testRecordType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("property1", new RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
    properties.put("property2", new RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), null));
    RecordType recordType = new RecordType(registry, properties);

    // Test getPropertyType
    assertEquals(registry.getNativeType(JSTypeNative.NUMBER_TYPE), recordType.getPropertyType("property1"));
    assertEquals(registry.getNativeType(JSTypeNative.STRING_TYPE), recordType.getPropertyType("property2"));
  }
  
  @Test
  public void testDefineProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("property1", new RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
    properties.put("property2", new RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), null));
    RecordType recordType = new RecordType(registry, properties);
    
    // Test defineProperty
    assertFalse(recordType.defineProperty("property1", registry.getNativeType(JSTypeNative.NUMBER_TYPE), true, null));
  }
  
  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties1 = new HashMap<>();
    properties1.put("property1", new RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
    properties1.put("property2", new RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), null));
    RecordType recordType1 = new RecordType(registry, properties1);
    
    Map<String, RecordProperty> properties2 = new HashMap<>();
    properties2.put("property1", new RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
    properties2.put("property2", new RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), null));
    RecordType recordType2 = new RecordType(registry, properties2);
    
    // Test isSubtype
    assertTrue(recordType1.isSubtype(recordType2));
  }

  @Test
  public void testIsSynthetic() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("property1", new RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
    properties.put("property2", new RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), null));
    RecordType recordType = new RecordType(registry, properties);

    // Test isSynthetic
    assertFalse(recordType.isSynthetic());
  }
  
  @Test(expected=IllegalStateException.class)
  public void testCheckRecordEquivalenceHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties1 = new HashMap<>();
    properties1.put("property1", new RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
    properties1.put("property2", new RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), null));
    RecordType recordType1 = new RecordType(registry, properties1);
    
    Map<String, RecordProperty> properties2 = new HashMap<>();
    properties2.put("property1", new RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
    properties2.put("property2", new RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), null));
    RecordType recordType2 = new RecordType(registry, properties2);
    
    // Test checkRecordEquivalenceHelper
    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, null));
    
    Map<String, RecordProperty> properties3 = new HashMap<>();
    properties3.put("property1", new RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
    properties3.put("property2", new RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), null));
    properties3.put("property3", new RecordProperty(registry.getNativeType(JSTypeNative.BOOLEAN_TYPE), null));
    RecordType recordType3 = new RecordType(registry, properties3);
    
    // Test checkRecordEquivalenceHelper
    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType3, null));
  }
}
import org.junit.Assert;
import org.junit.Test;

public class RecordTypeTest {

  @Test
  public void testRecordTypeConstructor1() {
    // Typical Use Case
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("prop1", new RecordProperty(new JSType("type1"), new Node()));
    properties.put("prop2", new RecordProperty(new JSType("type2"), new Node()));
    
    RecordType recordType = new RecordType(registry, properties);
    
    Assert.assertEquals(2, recordType.getProperties().keySet().size());
    Assert.assertEquals(new JSType("type1"), recordType.getProperties().get("prop1"));
    Assert.assertEquals(new JSType("type2"), recordType.getProperties().get("prop2"));
    Assert.assertTrue(recordType.isDeclared());
    Assert.assertFalse(recordType.isSynthetic());
  }
  
  @Test
  public void testRecordTypeConstructor2() {
    // Edge Case: Missing RecordProperty in properties
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("prop1", new RecordProperty(new JSType("type1"), new Node()));
    properties.put("prop2", null);
    
    try {
      RecordType recordType = new RecordType(registry, properties);
      Assert.fail("Exception not thrown.");
    } catch (IllegalStateException e) {
      // success
    }
  }
  
  @Test
  public void testCheckRecordEquivalenceHelper1() {
    // Typical Use Case
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties1 = new HashMap<>();
    properties1.put("prop1", new RecordProperty(new JSType("type1"), new Node()));
    properties1.put("prop2", new RecordProperty(new JSType("type2"), new Node()));
    
    Map<String, RecordProperty> properties2 = new HashMap<>();
    properties2.put("prop1", new RecordProperty(new JSType("type1"), new Node()));
    properties2.put("prop2", new RecordProperty(new JSType("type2"), new Node()));
    
    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);
    
    Assert.assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, false));
  }
  
  @Test
  public void testCheckRecordEquivalenceHelper2() {
    // Edge Case: Different property keys
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties1 = new HashMap<>();
    properties1.put("prop1", new RecordProperty(new JSType("type1"), new Node()));
    properties1.put("prop2", new RecordProperty(new JSType("type2"), new Node()));
    
    Map<String, RecordProperty> properties2 = new HashMap<>();
    properties2.put("prop1", new RecordProperty(new JSType("type1"), new Node()));
    properties2.put("prop3", new RecordProperty(new JSType("type3"), new Node()));
    
    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);
    
    Assert.assertFalse(recordType1.checkRecordEquivalenceHelper(recordType2, false));
  }
  
  @Test
  public void testCheckRecordEquivalenceHelper3() {
    // Edge Case: Different property types
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties1 = new HashMap<>();
    properties1.put("prop1", new RecordProperty(new JSType("type1"), new Node()));
    properties1.put("prop2", new RecordProperty(new JSType("type2"), new Node()));
    
    Map<String, RecordProperty> properties2 = new HashMap<>();
    properties2.put("prop1", new RecordProperty(new JSType("type1"), new Node()));
    properties2.put("prop2", new RecordProperty(new JSType("type3"), new Node()));
    
    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);
    
    Assert.assertFalse(recordType1.checkRecordEquivalenceHelper(recordType2, false));
  }
  
  @Test
  public void testIsSubtype1() {
    // Typical Use Case: recordType is a subtype of typeB
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("prop1", new RecordProperty(new JSType("type1"), new Node()));
    properties.put("prop2", new RecordProperty(new JSType("type2"), new Node()));
    
    RecordType recordType = new RecordType(registry, properties);
    ObjectType typeB = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    
    Assert.assertTrue(recordType.isSubtype(typeB));
  }
  
  @Test
  public void testIsSubtype2() {
    // Edge Case: typeA is not a record type
    JSTypeRegistry registry = new JSTypeRegistry();
    ObjectType typeA = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("prop1", new RecordProperty(new JSType("type1"), new Node()));
    properties.put("prop2", new RecordProperty(new JSType("type2"), new Node()));
    
    RecordType recordType = new RecordType(registry, properties);
    
    Assert.assertFalse(recordType.isSubtype(typeA));
  }
  
  @Test
  public void testIsSubtype3() {
    // Edge Case: typeA has missing properties
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties1 = new HashMap<>();
    properties1.put("prop1", new RecordProperty(new JSType("type1"), new Node()));
    properties1.put("prop2", new RecordProperty(new JSType("type2"), new Node()));
    
    Map<String, RecordProperty> properties2 = new HashMap<>();
    properties2.put("prop1", new RecordProperty(new JSType("type1"), new Node()));
    
    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);
    
    Assert.assertFalse(recordType1.isSubtype(recordType2));
  }

}
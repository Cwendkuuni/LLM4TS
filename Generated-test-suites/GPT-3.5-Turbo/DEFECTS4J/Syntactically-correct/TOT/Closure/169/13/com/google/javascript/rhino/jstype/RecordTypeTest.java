import com.google.javascript.rhino.*; 
import com.google.javascript.rhino.jstype.*; 

import org.junit.*;

public class RecordTypeTest {

  @Test
  public void testConstructorDeclared() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("prop1", new RecordProperty(JSTypeNative.NUMBER_TYPE, null));
    properties.put("prop2", new RecordProperty(JSTypeNative.STRING_TYPE, null));
    RecordType recordType = new RecordType(JSTypeRegistry.getInstance(), properties, true);
    
    Assert.assertNotNull(recordType);
    Assert.assertTrue(recordType.isDeclared());
    Assert.assertTrue(recordType.isFrozen());
    Assert.assertEquals(2, recordType.getProperties().size());
    
    Assert.assertTrue(recordType.hasProperty("prop1"));
    Assert.assertTrue(recordType.hasProperty("prop2"));
    
    Assert.assertEquals(JSTypeNative.NUMBER_TYPE, recordType.getPropertyType("prop1"));
    Assert.assertEquals(JSTypeNative.STRING_TYPE, recordType.getPropertyType("prop2"));
  }
  
  @Test
  public void testConstructorSynthesized() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("prop1", new RecordProperty(JSTypeNative.BOOLEAN_TYPE, null));
    properties.put("prop2", new RecordProperty(JSTypeNative.OBJECT_TYPE, null));
    RecordType recordType = new RecordType(JSTypeRegistry.getInstance(), properties, false);
    
    Assert.assertNotNull(recordType);
    Assert.assertFalse(recordType.isDeclared());
    Assert.assertTrue(recordType.isFrozen());
    Assert.assertEquals(2, recordType.getProperties().size());
    
    Assert.assertTrue(recordType.hasProperty("prop1"));
    Assert.assertTrue(recordType.hasProperty("prop2"));
    
    Assert.assertEquals(JSTypeNative.BOOLEAN_TYPE, recordType.getPropertyType("prop1"));
    Assert.assertEquals(JSTypeNative.OBJECT_TYPE, recordType.getPropertyType("prop2"));
  }
  
  @Test
  public void testCheckRecordEquivalenceHelper() {
    Map<String, RecordProperty> properties1 = new HashMap<>();
    properties1.put("prop1", new RecordProperty(JSTypeNative.NUMBER_TYPE, null));
    properties1.put("prop2", new RecordProperty(JSTypeNative.STRING_TYPE, null));
    RecordType recordType1 = new RecordType(JSTypeRegistry.getInstance(), properties1);
    
    Map<String, RecordProperty> properties2 = new HashMap<>();
    properties2.put("prop1", new RecordProperty(JSTypeNative.NUMBER_TYPE, null));
    properties2.put("prop2", new RecordProperty(JSTypeNative.STRING_TYPE, null));
    RecordType recordType2 = new RecordType(JSTypeRegistry.getInstance(), properties2);
    
    boolean result = recordType1.checkRecordEquivalenceHelper(recordType2, false);
    Assert.assertTrue(result);
  }
  
  @Test
  public void testGetGreatestSubtypeHelper() {
    Map<String, RecordProperty> properties1 = new HashMap<>();
    properties1.put("prop1", new RecordProperty(JSTypeNative.BOOLEAN_TYPE, null));
    properties1.put("prop2", new RecordProperty(JSTypeNative.STRING_TYPE, null));
    RecordType recordType1 = new RecordType(JSTypeRegistry.getInstance(), properties1);
    
    Map<String, RecordProperty> properties2 = new HashMap<>();
    properties2.put("prop1", new RecordProperty(JSTypeNative.BOOLEAN_TYPE, null));
    properties2.put("prop2", new RecordProperty(JSTypeNative.OBJECT_TYPE, null));
    RecordType recordType2 = new RecordType(JSTypeRegistry.getInstance(), properties2);
    
    JSType result = recordType1.getGreatestSubtypeHelper(recordType2);
    Assert.assertEquals(recordType1, result);
  }
  
  @Test
  public void testIsSubtype() {
    Map<String, RecordProperty> properties1 = new HashMap<>();
    properties1.put("prop1", new RecordProperty(JSTypeNative.NUMBER_TYPE, null));
    properties1.put("prop2", new RecordProperty(JSTypeNative.STRING_TYPE, null));
    RecordType recordType1 = new RecordType(JSTypeRegistry.getInstance(), properties1);
    
    Map<String, RecordProperty> properties2 = new HashMap<>();
    properties2.put("prop1", new RecordProperty(JSTypeNative.NUMBER_TYPE, null));
    properties2.put("prop2", new RecordProperty(JSTypeNative.STRING_TYPE, null));
    RecordType recordType2 = new RecordType(JSTypeRegistry.getInstance(), properties2);
    
    boolean result = recordType1.isSubtype(recordType2);
    Assert.assertTrue(result);
  }
  
  @Test
  public void testResolveInternal() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("prop1", new RecordProperty(JSTypeNative.BOOLEAN_TYPE, null));
    properties.put("prop2", new RecordProperty(JSTypeNative.STRING_TYPE, null));
    RecordType recordType = new RecordType(JSTypeRegistry.getInstance(), properties);
    
    JSType resolvedType = recordType.resolveInternal(null, null);
    
    Assert.assertEquals(recordType, resolvedType);
  }
}
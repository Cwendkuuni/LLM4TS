import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

  @Test
  public void testConstructorDeclared() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("property1", new RecordProperty(JSTypeNative.STRING_TYPE, null));
    properties.put("property2", new RecordProperty(JSTypeNative.NUMBER_TYPE, null));
    RecordType recordType = new RecordType(registry, properties);
    
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), recordType.getImplicitPrototype());
    assertTrue(recordType.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
    assertTrue(recordType.checkRecordEquivalenceHelper(recordType, null));
    assertFalse(recordType.isSynthetic());
    
    assertTrue(recordType.defineProperty("property3", JSTypeNative.STRING_TYPE, true, null));
    assertFalse(recordType.defineProperty("property4", JSTypeNative.BOOLEAN_TYPE, true, null));
    
    JSType expected = registry.getNativeType(JSTypeNative.NULL_TYPE);
    JSType actual = recordType.getGreatestSubtypeHelper(registry.getNativeType(JSTypeNative.NULL_TYPE));
    assertEquals(expected, actual);
    
    actual = recordType.toMaybeRecordType();
    assertEquals(recordType, actual);
  }

  @Test
  public void testConstructorSynthesized() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("property1", new RecordProperty(JSTypeNative.STRING_TYPE, null));
    properties.put("property2", new RecordProperty(JSTypeNative.NUMBER_TYPE, null));
    RecordType recordType = new RecordType(registry, properties, false);
    
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), recordType.getImplicitPrototype());
    assertTrue(recordType.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
    assertTrue(recordType.checkRecordEquivalenceHelper(recordType, null));
    assertTrue(recordType.isSynthetic());
    
    assertFalse(recordType.defineProperty("property3", JSTypeNative.STRING_TYPE, false, null));
    assertFalse(recordType.defineProperty("property4", JSTypeNative.BOOLEAN_TYPE, false, null));
    
    JSType expected = registry.getNativeType(JSTypeNative.NO_OBJECT_TYPE);
    JSType actual = recordType.getGreatestSubtypeHelper(registry.getNativeType(JSTypeNative.NO_OBJECT_TYPE));
    assertEquals(expected, actual);
    
    actual = recordType.toMaybeRecordType();
    assertEquals(recordType, actual);
  }

  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties1 = new HashMap<>();
    properties1.put("property1", new RecordProperty(JSTypeNative.STRING_TYPE, null));
    properties1.put("property2", new RecordProperty(JSTypeNative.NUMBER_TYPE, null));
    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = new HashMap<>();
    properties2.put("property1", new RecordProperty(JSTypeNative.STRING_TYPE, null));
    properties2.put("property2", new RecordProperty(JSTypeNative.NUMBER_TYPE, null));
    RecordType recordType2 = new RecordType(registry, properties2);

    assertTrue(recordType1.isSubtype(recordType2));
    assertTrue(recordType2.isSubtype(recordType1));
  }

  @Test
  public void testIsSubtypeFalse() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties1 = new HashMap<>();
    properties1.put("property1", new RecordProperty(JSTypeNative.STRING_TYPE, null));
    properties1.put("property2", new RecordProperty(JSTypeNative.NUMBER_TYPE, null));
    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = new HashMap<>();
    properties2.put("property1", new RecordProperty(JSTypeNative.STRING_TYPE, null));
    RecordType recordType2 = new RecordType(registry, properties2);

    assertFalse(recordType1.isSubtype(recordType2));
    assertFalse(recordType2.isSubtype(recordType1));
  }

  @Test
  public void testIsSubtypeNotRecordType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("property1", new RecordProperty(JSTypeNative.STRING_TYPE, null));
    properties.put("property2", new RecordProperty(JSTypeNative.NUMBER_TYPE, null));
    RecordType recordType = new RecordType(registry, properties);

    assertFalse(recordType.isSubtype(registry.getNativeObjectType(JSTypeNative.NULL_TYPE)));
    assertFalse(recordType.isSubtype(registry.getNativeObjectType(JSTypeNative.UNDEFINED_TYPE)));
    assertFalse(recordType.isSubtype(registry.getNativeObjectType(JSTypeNative.VOID_TYPE)));
    assertFalse(recordType.isSubtype(registry.getNativeObjectType(JSTypeNative.NUMBER_TYPE)));
  }

  @Test
  public void testIsSubtypeEmptyRecord() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new HashMap<>();
    RecordType recordType = new RecordType(registry, properties);

    assertTrue(recordType.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
    assertTrue(recordType.isSubtype(registry.getNativeObjectType(JSTypeNative.NULL_TYPE)));
    assertTrue(recordType.isSubtype(registry.getNativeObjectType(JSTypeNative.UNDEFINED_TYPE)));
    assertTrue(recordType.isSubtype(registry.getNativeObjectType(JSTypeNative.VOID_TYPE)));
    assertTrue(recordType.isSubtype(registry.getNativeObjectType(JSTypeNative.STRING_TYPE)));
  }

  @Test
  public void testIsSubtypeEmptyRecordWithProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("property1", new RecordProperty(JSTypeNative.STRING_TYPE, null));
    properties.put("property2", new RecordProperty(JSTypeNative.NUMBER_TYPE, null));
    RecordType recordType = new RecordType(registry, properties);

    assertTrue(recordType.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
    assertFalse(recordType.isSubtype(registry.getNativeObjectType(JSTypeNative.NULL_TYPE)));
    assertFalse(recordType.isSubtype(registry.getNativeObjectType(JSTypeNative.UNDEFINED_TYPE)));
    assertFalse(recordType.isSubtype(registry.getNativeObjectType(JSTypeNative.VOID_TYPE)));
    assertFalse(recordType.isSubtype(registry.getNativeObjectType(JSTypeNative.STRING_TYPE)));
  }

  @Test
  public void testIsSubtypeDifferentPropertyTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties1 = new HashMap<>();
    properties1.put("property1", new RecordProperty(JSTypeNative.STRING_TYPE, null));
    properties1.put("property2", new RecordProperty(JSTypeNative.NUMBER_TYPE, null));
    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = new HashMap<>();
    properties2.put("property1", new RecordProperty(JSTypeNative.BOOLEAN_TYPE, null));
    properties2.put("property2", new RecordProperty(JSTypeNative.NUMBER_TYPE, null));
    RecordType recordType2 = new RecordType(registry, properties2);

    assertFalse(recordType1.isSubtype(recordType2));
    assertFalse(recordType2.isSubtype(recordType1));
  }
  
  @Test
  public void testCheckRecordEquivalenceHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties1 = new HashMap<>();
    properties1.put("property1", new RecordProperty(JSTypeNative.STRING_TYPE, null));
    properties1.put("property2", new RecordProperty(JSTypeNative.NUMBER_TYPE, null));
    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = new HashMap<>();
    properties2.put("property1", new RecordProperty(JSTypeNative.STRING_TYPE, null));
    properties2.put("property2", new RecordProperty(JSTypeNative.NUMBER_TYPE, null));
    RecordType recordType2 = new RecordType(registry, properties2);

    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, null));
  }
  
  @Test
  public void testCheckRecordEquivalenceHelperNotEqual() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties1 = new HashMap<>();
    properties1.put("property1", new RecordProperty(JSTypeNative.STRING_TYPE, null));
    properties1.put("property2", new RecordProperty(JSTypeNative.NUMBER_TYPE, null));
    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = new HashMap<>();
    properties2.put("property1", new RecordProperty(JSTypeNative.STRING_TYPE, null));
    properties2.put("property2", new RecordProperty(JSTypeNative.UNKNOWN_TYPE, null));
    RecordType recordType2 = new RecordType(registry, properties2);

    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType2, null));
  }
  
  @Test
  public void testCheckRecordEquivalenceHelperSizeMismatch() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties1 = new HashMap<>();
    properties1.put("property1", new RecordProperty(JSTypeNative.STRING_TYPE, null));
    properties1.put("property2", new RecordProperty(JSTypeNative.NUMBER_TYPE, null));
    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = new HashMap<>();
    properties2.put("property1", new RecordProperty(JSTypeNative.STRING_TYPE, null));
    RecordType recordType2 = new RecordType(registry, properties2);

    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType2, null));
  }
  
  @Test
  public void testCheckRecordEquivalenceHelperKeySetMismatch() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties1 = new HashMap<>();
    properties1.put("property1", new RecordProperty(JSTypeNative.STRING_TYPE, null));
    properties1.put("property2", new RecordProperty(JSTypeNative.NUMBER_TYPE, null));
    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = new HashMap<>();
    properties2.put("property1", new RecordProperty(JSTypeNative.STRING_TYPE, null));
    properties2.put("property3", new RecordProperty(JSTypeNative.NUMBER_TYPE, null));
    RecordType recordType2 = new RecordType(registry, properties2);

    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType2, null));
  }
  
  @Test(expected = IllegalStateException.class)
  public void testConstructorNullProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("property1", null);
    RecordType recordType = new RecordType(registry, properties);
  }
}
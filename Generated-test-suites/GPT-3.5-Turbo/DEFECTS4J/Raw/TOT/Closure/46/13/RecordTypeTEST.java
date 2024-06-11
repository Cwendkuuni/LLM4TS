import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

  @Test
  public void testIsEquivalentTo_sameRecordType_true() {
    // Arrange
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new HashMap<>();
    RecordProperty prop = new RecordProperty("a", JSType.UNKNOWN_TYPE, null);
    properties.put("a", prop);
    RecordType recordType1 = new RecordType(registry, properties);
    RecordType recordType2 = new RecordType(registry, properties);

    // Act
    boolean result = recordType1.isEquivalentTo(recordType2);

    // Assert
    assertTrue(result);
  }

  @Test
  public void testIsEquivalentTo_differentRecordType_false() {
    // Arrange
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties1 = new HashMap<>();
    RecordProperty prop1 = new RecordProperty("a", JSType.UNKNOWN_TYPE, null);
    properties1.put("a", prop1);
    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = new HashMap<>();
    RecordProperty prop2 = new RecordProperty("b", JSType.UNKNOWN_TYPE, null);
    properties2.put("b", prop2);
    RecordType recordType2 = new RecordType(registry, properties2);

    // Act
    boolean result = recordType1.isEquivalentTo(recordType2);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testIsSubtype_subtype_true() {
    // Arrange
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties1 = new HashMap<>();
    RecordProperty prop1 = new RecordProperty("a", JSType.UNKNOWN_TYPE, null);
    properties1.put("a", prop1);
    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = new HashMap<>();
    RecordProperty prop2 = new RecordProperty("a", JSType.UNKNOWN_TYPE, null);
    properties2.put("a", prop2);
    RecordType recordType2 = new RecordType(registry, properties2);

    // Act
    boolean result = recordType1.isSubtype(recordType2);

    // Assert
    assertTrue(result);
  }

  @Test
  public void testIsSubtype_notSubtype_false() {
    // Arrange
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties1 = new HashMap<>();
    RecordProperty prop1 = new RecordProperty("a", JSType.UNKNOWN_TYPE, null);
    properties1.put("a", prop1);
    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = new HashMap<>();
    RecordProperty prop2 = new RecordProperty("b", JSType.UNKNOWN_TYPE, null);
    properties2.put("b", prop2);
    RecordType recordType2 = new RecordType(registry, properties2);

    // Act
    boolean result = recordType1.isSubtype(recordType2);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testIsSubtype_objectTypeSubtype_true() {
    // Arrange
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new HashMap<>();
    RecordProperty prop = new RecordProperty("a", JSType.UNKNOWN_TYPE, null);
    properties.put("a", prop);
    RecordType recordType = new RecordType(registry, properties);

    ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

    // Act
    boolean result = recordType.isSubtype(objectType);

    // Assert
    assertTrue(result);
  }

  @Test
  public void testGetLeastSupertype_supertypeRecordType_true() {
    // Arrange
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties1 = new HashMap<>();
    RecordProperty prop1 = new RecordProperty("a", JSType.UNKNOWN_TYPE, null);
    properties1.put("a", prop1);
    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = new HashMap<>();
    RecordProperty prop2 = new RecordProperty("b", JSType.UNKNOWN_TYPE, null);
    properties2.put("b", prop2);
    RecordType recordType2 = new RecordType(registry, properties2);

    // Act
    JSType result = recordType1.getLeastSupertype(recordType2);

    // Assert
    assertTrue(result instanceof RecordType);
  }

  @Test
  public void testGetLeastSupertype_nonRecordTypeSuperType_true() {
    // Arrange
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new HashMap<>();
    RecordProperty prop = new RecordProperty("a", JSType.UNKNOWN_TYPE, null);
    properties.put("a", prop);
    RecordType recordType = new RecordType(registry, properties);

    JSType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

    // Act
    JSType result = recordType.getLeastSupertype(objectType);

    // Assert
    assertEquals(objectType, result);
  }

  @Test
  public void testGetGreatestSubtype_subtypeRecordType_true() {
    // Arrange
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties1 = new HashMap<>();
    RecordProperty prop1 = new RecordProperty("a", JSType.UNKNOWN_TYPE, null);
    properties1.put("a", prop1);
    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = new HashMap<>();
    RecordProperty prop2 = new RecordProperty("b", JSType.UNKNOWN_TYPE, null);
    properties2.put("b", prop2);
    RecordType recordType2 = new RecordType(registry, properties2);

    // Act
    JSType result = recordType1.getGreatestSubtype(recordType2);

    // Assert
    assertTrue(result instanceof RecordType);
  }

  @Test
  public void testGetGreatestSubtype_nonRecordTypeSubType_true() {
    // Arrange
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new HashMap<>();
    RecordProperty prop = new RecordProperty("a", JSType.UNKNOWN_TYPE, null);
    properties.put("a", prop);
    RecordType recordType = new RecordType(registry, properties);

    JSType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

    // Act
    JSType result = recordType.getGreatestSubtype(objectType);

    // Assert
    assertTrue(result.isFalse());
  }
}
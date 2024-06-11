import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.*;

public class RecordTypeTest {

  @Test
  public void testIsEquivalentTo_sameRecordType_returnsTrue() {
    // Arrange
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> props = new HashMap<>();
    props.put("a", new RecordProperty("a", registry.getNativeType(JSTypeNative.NUMBER_TYPE), propertyNode));
    RecordType recordType1 = new RecordType(registry, props);
    RecordType recordType2 = new RecordType(registry, props);

    // Act
    boolean result = recordType1.isEquivalentTo(recordType2);

    // Assert
    assertTrue(result);
  }

  @Test
  public void testIsEquivalentTo_differentRecordType_returnsFalse() {
    // Arrange
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> props1 = new HashMap<>();
    props1.put("a", new RecordProperty("a", registry.getNativeType(JSTypeNative.NUMBER_TYPE), propertyNode));
    RecordType recordType1 = new RecordType(registry, props1);

    Map<String, RecordProperty> props2 = new HashMap<>();
    props2.put("b", new RecordProperty("b", registry.getNativeType(JSTypeNative.STRING_TYPE), propertyNode));
    RecordType recordType2 = new RecordType(registry, props2);

    // Act
    boolean result = recordType1.isEquivalentTo(recordType2);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testGetImplicitPrototype_returnsCorrectPrototype() {
    // Arrange
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> props = new HashMap<>();
    props.put("a", new RecordProperty("a", registry.getNativeType(JSTypeNative.NUMBER_TYPE), propertyNode));
    RecordType recordType = new RecordType(registry, props);

    // Act
    ObjectType prototype = recordType.getImplicitPrototype();

    // Assert
    assertEquals(JSTypeNative.OBJECT_TYPE, prototype.getName());
  }

  @Test
  public void testDefineProperty_notFrozenAndNotInferred_definesPropertyAndReturnsTrue() {
    // Arrange
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType = new RecordType(registry, new HashMap<String, RecordProperty>());
    String propertyName = "a";
    JSType type = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
    Node propertyNode = new Node();

    // Act
    boolean result = recordType.defineProperty(propertyName, type, false, propertyNode);

    // Assert
    assertTrue(result);
    assertTrue(recordType.hasProperty(propertyName));
    assertEquals(type, recordType.getPropertyType(propertyName));
  }

  @Test
  public void testGetLeastSupertype_unrelatedType_returnsSuperTypeObject() {
    // Arrange
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> props = new HashMap<>();
    props.put("a", new RecordProperty("a", registry.getNativeType(JSTypeNative.NUMBER_TYPE), propertyNode));
    RecordType recordType = new RecordType(registry, props);

    ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

    // Act
    JSType result = recordType.getLeastSupertype(objectType);

    // Assert
    assertEquals(objectType, result);
  }

  @Test
  public void testIsSubtype_relatedRecordType_returnsTrue() {
    // Arrange
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> props1 = new HashMap<>();
    props1.put("a", new RecordProperty("a", registry.getNativeType(JSTypeNative.NUMBER_TYPE), propertyNode));
    props1.put("b", new RecordProperty("b", registry.getNativeType(JSTypeNative.STRING_TYPE), propertyNode));
    RecordType recordType1 = new RecordType(registry, props1);

    Map<String, RecordProperty> props2 = new HashMap<>();
    props2.put("a", new RecordProperty("a", registry.getNativeType(JSTypeNative.NUMBER_TYPE), propertyNode));
    RecordType recordType2 = new RecordType(registry, props2);

    // Act
    boolean result = recordType1.isSubtype(recordType2);

    // Assert
    assertTrue(result);
  }

  @Test
  public void testIsSubtype_unrelatedRecordType_returnsFalse() {
    // Arrange
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> props1 = new HashMap<>();
    props1.put("a", new RecordProperty("a", registry.getNativeType(JSTypeNative.NUMBER_TYPE), propertyNode));
    RecordType recordType1 = new RecordType(registry, props1);

    Map<String, RecordProperty> props2 = new HashMap<>();
    props2.put("b", new RecordProperty("b", registry.getNativeType(JSTypeNative.STRING_TYPE), propertyNode));
    RecordType recordType2 = new RecordType(registry, props2);

    // Act
    boolean result = recordType1.isSubtype(recordType2);

    // Assert
    assertFalse(result);
  }
}
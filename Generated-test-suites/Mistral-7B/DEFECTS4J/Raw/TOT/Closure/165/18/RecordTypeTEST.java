import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class RecordTypeTest {

  @Test
  public void testCreateRecordTypeWithProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();

    // Define a simple record type with two properties: "a" of type String and "b" of type Number.
    RecordProperty aProperty = new RecordProperty("a", registry.getStringType());
    RecordProperty bProperty = new RecordProperty("b", registry.getNumberType());

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", aProperty);
    properties.put("b", bProperty);

    RecordType recordType = new RecordType(registry, properties);

    // Test properties
    assertThat(recordType.properties.size()).isEqualTo(2);
    assertThat(recordType.getPropertyNames()).containsExactly("a", "b");

    // Test getPropertyType and getPropertyNode
    JSType aType = recordType.getPropertyType("a");
    JSType bType = recordType.getPropertyType("b");
    Node aNode = recordType.getPropertyNode("a");
    Node bNode = recordType.getPropertyNode("b");

    assertThat(aType).isEqualTo(registry.getStringType());
    assertThat(bType).isEqualTo(registry.getNumberType());

    // Test hasProperty
    assertThat(recordType.hasProperty("a")).isTrue();
    assertThat(recordType.hasProperty("c")).isFalse();

    // Test defineProperty (inferred = false)
    recordType.defineProperty("c", registry.getStringType(), false, null);
    assertThat(recordType.hasProperty("c")).isTrue();

    // Test defineProperty (inferred = true)
    recordType.defineProperty("d", registry.getNumberType(), true, null);
    assertThat(recordType.hasProperty("d")).isFalse();

    // Test getImplicitPrototype
    ObjectType proto = recordType.getImplicitPrototype();
    assertThat(proto).isEqualTo(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));

    // Test isEquivalentTo
    RecordType recordType1 = new RecordType(registry, properties);
    RecordType recordType2 = new RecordType(registry, properties);
    assertThat(recordType1.isEquivalentTo(recordType2)).isTrue();

    // Test isSubtype
    RecordType recordType3 = new RecordType(registry, Maps.newLinkedHashMap());
    RecordType recordType4 = new RecordType(registry, properties);
    RecordType recordType5 = new RecordType(registry, Maps.newLinkedHashSet(properties.values()));

    assertThat(recordType3.isSubtype(recordType4)).isFalse();
    assertThat(recordType4.isSubtype(recordType3)).isFalse();
    assertThat(recordType4.isSubtype(recordType5)).isTrue();
    assertThat(recordType5.isSubtype(recordType4)).isTrue();

    // Test getGreatestSubtypeHelper
    JSType type1 = new UnionType(registry, JSType.EMPTY_TYPE, registry.getStringType());
    JSType type2 = new UnionType(registry, registry.getStringType(), JSType.EMPTY_TYPE);
    JSType type3 = new RecordType(registry, Maps.newLinkedHashMap());
    JSType type4 = new RecordType(registry, Maps.newLinkedHashMap());
    JSType type5 = new ObjectType(registry, null, null);

    RecordType recordType6 = new RecordType(registry, properties);

    assertThat(recordType6.getGreatestSubtypeHelper(type1)).isEqualTo(type1);
    assertThat(recordType6.getGreatestSubtypeHelper(type2)).isEqualTo(type2);
    assertThat(recordType6.getGreatestSubtypeHelper(type3)).isEqualTo(type4);
    assertThat(recordType6.getGreatestSubtypeHelper(type4)).isEqualTo(type6);
    assertThat(recordType6.getGreatestSubtypeHelper(type5)).isEqualTo(type5);
  }

  @Test
  public void testCreateRecordTypeWithFrozenProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();

    // Define a simple record type with two properties: "a" of type String and "b" of type Number.
    RecordProperty aProperty = new RecordProperty("a", registry.getStringType());
    RecordProperty bProperty = new RecordProperty("b", registry.getNumberType());

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", aProperty);
    properties.put("b", bProperty);

    RecordType recordType = new RecordType(registry, properties);

    // Freeze the record type
    recordType.setFrozen(true);

    // Test defineProperty (inferred = false)
    try {
      recordType.defineProperty("c", registry.getStringType(), false, null);
      // This should not be reached
    } catch (IllegalStateException e) {
      // Expected
    }

    // Test defineProperty (inferred = true)
    try {
      recordType.defineProperty("d", registry.getNumberType(), true, null);
      // This should not be reached
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  @Test
  public void testCreateRecordTypeWithConflictingProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();

    // Define a simple record type with two properties: "a" of type String and "a" of type Number.
    RecordProperty aPropertyString = new RecordProperty("a", registry.getStringType());
    RecordProperty aPropertyNumber = new RecordProperty("a", registry.getNumberType());

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", aPropertyString);
    properties.put("a", aPropertyNumber);

    try {
      new RecordType(registry, properties);
      // This should not be reached
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  @Test
  public void testCreateRecordTypeWithSamePropertiesButDifferentOrder() {
    JSTypeRegistry registry = new JSTypeRegistry();

    // Define a simple record type with two properties: "a" of type String and "b" of type Number.
    RecordProperty aProperty = new RecordProperty("a", registry.getStringType());
    RecordProperty bProperty = new RecordProperty("b", registry.getNumberType());

    Map<String, RecordProperty> properties1 = Maps.newLinkedHashMap();
    properties1.put("a", aProperty);
    properties1.put("b", bProperty);

    Map<String, RecordProperty> properties2 = Maps.newLinkedHashMap();
    properties2.put("b", bProperty);
    properties2.put("a", aProperty);

    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);

    assertThat(recordType1).isEqualTo(recordType2);
  }

  @Test
  public void testCreateRecordTypeWithEmptyProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();

    RecordType recordType = new RecordType(registry, Maps.newLinkedHashMap());

    assertThat(recordType.properties.isEmpty()).isTrue();
  }

  @Test
  public void testCreateRecordTypeWithInheritance() {
    JSTypeRegistry registry = new JSTypeRegistry();

    // Define a simple base record type with one property "a" of type String.
    RecordProperty aProperty = new RecordProperty("a", registry.getStringType());

    Map<String, RecordProperty> baseProperties = Maps.newLinkedHashMap();
    baseProperties.put("a", aProperty);

    RecordType baseRecordType = new RecordType(registry, baseProperties);

    // Define a derived record type with the same property "a" of type Number.
    RecordProperty aPropertyNumber = new RecordProperty("a", registry.getNumberType());

    Map<String, RecordProperty> derivedProperties = new LinkedHashMap<>();
    derivedProperties.putAll(baseProperties);
    derivedProperties.put("a", aPropertyNumber);

    RecordType derivedRecordType = new RecordType(registry, derivedProperties);

    // Test isSubtype
    assertThat(derivedRecordType.isSubtype(baseRecordType)).isTrue();

    // Test getGreatestSubtypeHelper
    JSType baseType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)
        .getGreatestSubtype(baseRecordType);
    JSType derivedType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)
        .getGreatestSubtype(derivedRecordType);

    assertThat(baseType).isEqualTo(derivedType);
  }
}
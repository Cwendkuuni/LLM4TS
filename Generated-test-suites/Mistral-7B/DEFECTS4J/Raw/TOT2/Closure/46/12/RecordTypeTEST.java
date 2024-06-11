import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.testing.TypeError;
import com.google.javascript.rhino.testing.Value;
import com.google.javascript.rhino.testing.assertions.Assert;
import com.google.javascript.rhino.testing.type.UnionType;
import com.google.javascript.rhino.testing.values.JSTypes;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import static com.google.javascript.rhino.testing.assertions.Assert.assertEquals;
import static com.google.javascript.rhino.testing.assertions.Assert.assertFalse;
import static com.google.javascript.rhino.testing.assertions.Assert.assertNotNull;
import static com.google.javascript.rhino.testing.assertions.Assert.assertThrows;
import static com.google.javascript.rhino.testing.assertions.Assert.assertThat;
import static com.google.javascript.rhino.testing.assertions.Assert.assertTrue;
import static com.google.javascript.rhino.testing.assertions.Assert.fail;

public class RecordTypeTest {

  @Test
  public void testRecordTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();

    // Test creating a simple record type.
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("x", new RecordProperty("x", JSTypes.STRING));
    RecordType recordType = new RecordType(registry, properties);

    // Basic functionality test.
    assertEquals(JSTypeRegistry.RecordType.class, recordType.getClass());

    // Test getting the least common supertype with an empty record type.
    JSType emptyRecordType = new RecordType(registry, new HashMap<>());
    assertEquals(JSTypeRegistry.ObjectType.class, recordType.getLeastSupertype(emptyRecordType));

    // Test getting the least common supertype with a record type with the same properties.
    Map<String, RecordProperty> sameProperties = new HashMap<>(properties);
    RecordType samePropertiesRecordType = new RecordType(registry, sameProperties);
    assertEquals(recordType, samePropertiesRecordType);

    // Test getting the least common supertype with a record type with fewer properties.
    Map<String, RecordProperty> fewerProperties = new HashMap<>(properties);
    fewerProperties.remove("x");
    RecordType fewerPropertiesRecordType = new RecordType(registry, fewerProperties);
    UnionType leastCommonSupertype = (UnionType) recordType.getLeastSupertype(fewerPropertiesRecordType);
    assertTrue(leastCommonSupertype.getAlternates().size() > 1);
    assertTrue(leastCommonSupertype.getAlternates().contains(recordType));
    assertTrue(leastCommonSupertype.getAlternates().contains(fewerPropertiesRecordType));

    // Test getting the least common supertype with a record type with more properties.
    Map<String, RecordProperty> moreProperties = new HashMap<>(properties);
    moreProperties.put("y", new RecordProperty("y", JSTypes.NUMBER));
    RecordType morePropertiesRecordType = new RecordType(registry, moreProperties);
    UnionType leastCommonSupertypeMoreProperties = (UnionType) recordType.getLeastSupertype(morePropertiesRecordType);
    assertTrue(leastCommonSupertypeMoreProperties.getAlternates().size() > 1);
    assertTrue(leastCommonSupertypeMoreProperties.getAlternates().contains(recordType));
    assertTrue(leastCommonSupertypeMoreProperties.getAlternates().contains(morePropertiesRecordType));

    // Test getting the least common supertype with a record type with different properties.
    Map<String, RecordProperty> differentProperties = new HashMap<>();
    differentProperties.put("x", new RecordProperty("x", JSTypes.NUMBER));
    RecordType differentPropertiesRecordType = new RecordType(registry, differentProperties);
    assertEquals(JSTypeRegistry.ObjectType.class, recordType.getLeastSupertype(differentPropertiesRecordType));

    // Test getting the greatest subtype.
    JSType greatestSubtype = recordType.getGreatestSubtype(new RecordType(registry, new HashMap<>()));
    assertEquals(recordType, greatestSubtype);

    // Test getting the greatest subtype with a record type that has more properties.
    Map<String, RecordProperty> moreProperties2 = new HashMap<>();
    moreProperties2.put("y", new RecordProperty("y", JSTypes.NUMBER));
    RecordType moreProperties2RecordType = new RecordType(registry, moreProperties2);
    assertEquals(moreProperties2RecordType, recordType.getGreatestSubtype(moreProperties2RecordType));

    // Test getting the greatest subtype with a record type that has fewer properties.
    Map<String, RecordProperty> fewerProperties2 = new HashMap<>();
    fewerProperties2.remove("x");
    RecordType fewerProperties2RecordType = new RecordType(registry, fewerProperties2);
    UnionType greatestSubtypeHelper = (UnionType) recordType.getGreatestSubtypeHelper(fewerProperties2RecordType);
    assertTrue(greatestSubtypeHelper.getAlternates().size() > 1);
    assertTrue(greatestSubtypeHelper.getAlternates().contains(recordType));
    assertTrue(greatestSubtypeHelper.getAlternates().contains(fewerProperties2RecordType));

    // Test getting the greatest subtype with a record type that has different properties.
    Map<String, RecordProperty> differentProperties2 = new HashMap<>();
    differentProperties2.put("y", new RecordProperty("y", JSTypes.STRING));
    RecordType differentProperties2RecordType = new RecordType(registry, differentProperties2);
    assertEquals(JSTypeRegistry.ObjectType.class, recordType.getLeastSupertype(differentProperties2RecordType));

    // Test getting the greatest subtype with a non-record type.
    JSType nonRecordType = JSTypes.NUMBER;
    assertEquals(JSTypeRegistry.ObjectType.class, recordType.getLeastSupertype(nonRecordType));

    // Test getting the greatest subtype with an object type.
    JSType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    assertEquals(recordType, recordType.getGreatestSubtype(objectType));

    // Test getting the greatest subtype with an undefined type.
    JSType undefinedType = registry.getNativeType(JSTypeNative.UNDEFINED);
    assertEquals(JSTypeRegistry.ObjectType.class, recordType.getLeastSupertype(undefinedType));

    // Test getting the greatest subtype with a null type.
    JSType nullType = registry.getNativeType(JSTypeNative.NULL);
    assertEquals(JSTypeRegistry.ObjectType.class, recordType.getLeastSupertype(nullType));

    // Test getting the greatest subtype with a boolean type.
    JSType booleanType = JSTypes.BOOLEAN;
    assertEquals(JSTypeRegistry.ObjectType.class, recordType.getLeastSupertype(booleanType));

    // Test getting the greatest subtype with a number type.
    JSType numberType = JSTypes.NUMBER;
    assertEquals(JSTypeRegistry.ObjectType.class, recordType.getLeastSupertype(numberType));

    // Test getting the greatest subtype with a string type.
    JSType stringType = JSTypes.STRING;
    assertEquals(JSTypeRegistry.ObjectType.class, recordType.getLeastSupertype(stringType));

    // Test getting the greatest subtype with a function type.
    JSType functionType = registry.getFunctionType(new JSType[0], JSTypes.VOID);
    assertEquals(JSTypeRegistry.ObjectType.class, recordType.getLeastSupertype(functionType));

    // Test getting the greatest subtype with a union type.
    JSType unionType = new UnionType(registry, new HashMap<>());
    UnionType leastCommonSupertypeUnionType = (UnionType) recordType.getLeastSupertype(unionType);
    assertTrue(leastCommonSupertypeUnionType.getAlternates().isEmpty());

    // Test getting the greatest subtype with an interface type.
    JSType interfaceType = registry.getNativeType(JSTypeNative.OBJECT_TYPE)
        .getDeclaredProperty("__interfaces__").getValue().getStaticValue();
    assertEquals(JSTypeRegistry.ObjectType.class, recordType.getLeastSupertype(interfaceType));

    // Test getting the greatest subtype with an array type.
    JSType arrayType = JSTypes.ARRAY.ofType(JSTypes.NUMBER);
    UnionType greatestSubtypeArrayType = (UnionType) recordType.getGreatestSubtypeHelper(arrayType);
    assertTrue(greatestSubtypeArrayType.getAlternates().size() > 1);
    assertTrue(greatestSubtypeArrayType.getAlternates().contains(recordType));

    // Test getting the greatest subtype with a nullable array type.
    JSType nullableArrayType = JSTypes.ARRAY.ofType(JSTypes.NUMBER).nullable();
    UnionType greatestSubtypeNullableArrayType = (UnionType) recordType.getGreatestSubtypeHelper(nullableArrayType);
    assertTrue(greatestSubtypeNullableArrayType.getAlternates().size() > 1);
    assertTrue(greatestSubtypeNullableArrayType.getAlternates().contains(recordType));

    // Test getting the greatest subtype with a generic array type.
    JSType genericArrayType = JSTypes.ARRAY.ofType(registry.getTypeByInternalName("MyType"));
    UnionType greatestSubtypeGenericArrayType = (UnionType) recordType.getGreatestSubtypeHelper(genericArrayType);
    assertTrue(greatestSubtypeGenericArrayType.getAlternates().isEmpty());

    // Test isSubtype with types that have the same properties.
    RecordType samePropertiesRecordType2 = new RecordType(registry, sameProperties);
    assertTrue(recordType.isSubtype(samePropertiesRecordType2));

    // Test isSubtype with types that have fewer properties.
    RecordType fewerPropertiesRecordType2 = new RecordType(registry, fewerProperties);
    assertFalse(recordType.isSubtype(fewerPropertiesRecordType2));

    // Test isSubtype with types that have more properties.
    RecordType morePropertiesRecordType2 = new RecordType(registry, moreProperties);
    assertTrue(morePropertiesRecordType2.isSubtype(recordType));

    // Test isSubtype with a record type that has different properties.
    RecordType differentPropertiesRecordType2 = new RecordType(registry, differentProperties);
    assertFalse(recordType.isSubtype(differentPropertiesRecordType2));

    // Test isSubtype with a non-record type.
    assertFalse(recordType.isSubtype(JSTypes.NUMBER));

    // Test isSubtype with an object type.
    assertTrue(JSTypeRegistry.ObjectType.class.isAssignableFrom(recordType.getClass()));

    // Test isSubtype with a record type with fewer properties.
    RecordType fewerPropertiesRecordType3 = new RecordType(registry, fewerProperties2);
    assertTrue(recordType.isSubtype(fewerPropertiesRecordType3));

    // Test isSubtype with a record type that has the same properties but a different order.
    Map<String, RecordProperty> propertiesOrderDifferent = new HashMap<>(properties);
    String x = propertiesOrderDifferent.remove("x").getKey();
    String y = propertiesOrderDifferent.remove("y").getKey();
    RecordType propertiesOrderDifferentRecordType = new RecordType(registry, propertiesOrderDifferent);
    assertTrue(recordType.isSubtype(propertiesOrderDifferentRecordType));

    // Test isSubtype with a record type that has different property names.
    Map<String, RecordProperty> propertiesDifferentNames = new HashMap<>(properties);
    String z = "z";
    propertiesDifferentNames.put(z, properties.get("x"));
    RecordType propertiesDifferentNamesRecordType = new RecordType(registry, propertiesDifferentNames);
    assertFalse(recordType.isSubtype(propertiesDifferentNamesRecordType));
  }

  @Test
  public void testRecordTypePropertyAccess() {
    JSTypeRegistry registry = new JSTypeRegistry();

    // Test creating a simple record type.
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("x", new RecordProperty("x", JSTypes.STRING));
    RecordType recordType = new RecordType(registry, properties);

    // Test property access with valid property names.
    assertNotNull(recordType.getProperty("x"));

    // Test property access with invalid property names.
    String invalidProperty = "invalidPropertyName";
    assertThrows(TypeError.class, () -> recordType.getProperty(invalidProperty));

    // Test setting a property with a valid property name.
    recordType.setProperty("x", Value.string("foo"));
    assertEquals("foo", recordType.getProperty("x").getValue().toString());

    // Test setting a property with an invalid property name.
    assertThrows(TypeError.class, () -> recordType.setProperty(invalidProperty, Value.string("foo")));
  }

  @Test
  public void testRecordTypeFrozen() {
    JSTypeRegistry registry = new JSTypeRegistry();

    // Test creating a simple record type.
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("x", new RecordProperty("x", JSTypes.STRING));
    RecordType recordType = new RecordType(registry, properties);

    // Test adding a new property to a frozen record type.
    recordType.setFrozen(true);
    Map<String, RecordProperty> newProperties = new HashMap<>(properties);
    newProperties.put("y", new RecordProperty("y", JSTypes.NUMBER));
    RecordType newRecordType = new RecordType(registry, newProperties);
    UnionType leastCommonSupertype = (UnionType) recordType.getLeastSupertype(newRecordType);
    assertTrue(leastCommonSupertype.getAlternates().size() > 1);
    assertTrue(leastCommonSupertype.getAlternates().contains(recordType));
    assertTrue(leastCommonSupertype.getAlternates().contains(newRecordType));

    // Test adding a new property to a non-frozen record type.
    Map<String, RecordProperty> newProperties2 = new HashMap<>(properties);
    newProperties2.put("y", new RecordProperty("y", JSTypes.NUMBER));
    RecordType newRecordType2 = new RecordType(registry, newProperties2);
    assertEquals(newRecordType2, recordType.getGreatestSubtype(newRecordType2));

    // Test modifying an existing property in a frozen record type.
    recordType.setFrozen(true);
    String propertyToModify = "x";
    RecordProperty newPropertyType = new RecordProperty(propertyToModify, JSTypes.NUMBER);
    try {
      recordType.setProperty(propertyToModify, newPropertyType);
      fail();
    } catch (TypeError expected) {
      // Expected
    }

    // Test modifying an existing property in a non-frozen record type.
    RecordProperty newPropertyValue = new RecordProperty(propertyToModify, JSTypes.NUMBER);
    recordType.setProperty(propertyToModify, newPropertyValue);
    assertEquals(JSTypes.NUMBER, recordType.getProperty(propertyToModify).getType());
  }
}
package com.google.javascript.rhino.jstype;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.PrototypeObjectType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;

/**
 * Test the functionality of RecordType class.
 */
public class RecordTypeTest {

  private ErrorReporter errorReporter;
  private JSTypeRegistry registry;

  @Before
  public void setup() {
    errorReporter = new ErrorReporter();
    registry = new JSTypeRegistry(errorReporter);
  }

  /**
   * Test constructor.
   */
  @Test
  public void testConstructor() {
    // Given
    Map<String, RecordProperty> recordProperties = new TreeMap<>();
    recordProperties.put("prop1", new RecordProperty("prop1", registry.getStringType()));
    recordProperties.put("prop2", new RecordProperty("prop2", registry.getIntType()));

    // When
    RecordType recordType = new RecordType(registry, recordProperties, true);

    // Then
    assertEquals("RecordType with 2 properties", 2, recordType.getPropertyCount());
    assertEquals("RecordType with correct properties",
        Arrays.asList("prop1", "prop2"), recordType.getPropertyNames());
  }

  /**
   * Test getImplicitPrototype method.
   */
  @Test
  public void testGetImplicitPrototype() {
    // Given
    RecordType recordType = new RecordType(registry, Collections.emptyMap(), false);

    // When
    ObjectType implicitPrototype = recordType.getImplicitPrototype();

    // Then
    assertSame("RecordType's implicit prototype should be Object type",
        registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), implicitPrototype);
  }

  /**
   * Test checkRecordEquivalenceHelper method.
   */
  @Test
  public void testCheckRecordEquivalenceHelper() {
    // Given
    Map<String, RecordProperty> recordProperties1 = new TreeMap<>();
    recordProperties1.put("prop1", new RecordProperty("prop1", registry.getStringType()));
    recordProperties1.put("prop2", new RecordProperty("prop2", registry.getIntType()));

    Map<String, RecordProperty> recordProperties2 = new TreeMap<>();
    recordProperties2.put("prop1", new RecordProperty("prop1", registry.getIntType()));
    recordProperties2.put("prop2", new RecordProperty("prop2", registry.getIntType()));

    RecordType recordType1 = new RecordType(registry, recordProperties1, true);
    RecordType recordType2 = new RecordType(registry, recordProperties2, true);

    // When
    boolean result = recordType1.checkRecordEquivalenceHelper(recordType2, false);

    // Then
    assertTrue("Record types should be equivalent", result);
  }

  /**
   * Test checkRecordEquivalenceHelper with different properties.
   */
  @Test
  public void testCheckRecordEquivalenceHelperWithDifferentProperties() {
    // Given
    Map<String, RecordProperty> recordProperties1 = new TreeMap<>();
    recordProperties1.put("prop1", new RecordProperty("prop1", registry.getStringType()));
    recordProperties1.put("prop2", new RecordProperty("prop2", registry.getIntType()));

    Map<String, RecordProperty> recordProperties2 = new TreeMap<>();
    recordProperties2.put("prop1", new RecordProperty("prop1", registry.getIntType()));
    recordProperties2.put("prop3", new RecordProperty("prop3", registry.getIntType()));

    RecordType recordType1 = new RecordType(registry, recordProperties1, true);
    RecordType recordType2 = new RecordType(registry, recordProperties2, true);

    // When
    boolean result = recordType1.checkRecordEquivalenceHelper(recordType2, false);

    // Then
    assertFalse("Record types should not be equivalent", result);
  }

  /**
   * Test defineProperty method.
   */
  @Test
  public void testDefineProperty() {
    // Given
    RecordType recordType = new RecordType(registry, Collections.emptyMap(), false);

    // When
    String propertyName = "property";
    JSType propertyType = registry.getNativeObjectType(JSTypeNative.NUMBER_TYPE);
    Node propertyNode = Node.NULL();
    boolean result = recordType.defineProperty(propertyName, propertyType, false, propertyNode);

    // Then
    assertTrue("Should define a new property", result);
    assertTrue("RecordType should have the property", recordType.hasProperty(propertyName));
  }

  /**
   * Test defineProperty with same property name.
   */
  @Test(expected = IllegalStateException.class)
  public void testDefinePropertyWithSamePropertyName() {
    // Given
    RecordType recordType = new RecordType(registry, Collections.emptyMap(), false);

    // When
    String propertyName = "property";
    JSType propertyType = registry.getNativeObjectType(JSTypeNative.NUMBER_TYPE);
    Node propertyNode = Node.NULL();
    recordType.defineProperty(propertyName, propertyType, false, propertyNode);

    // Then
    // Throws an exception as the property is already defined
    recordType.defineProperty(propertyName, propertyType, false, propertyNode);
  }

  /**
   * Test defineProperty with frozen recordType.
   */
  @Test(expected = IllegalStateException.class)
  public void testDefinePropertyWithFrozenRecordType() {
    // Given
    RecordType recordType = new RecordType(registry, Collections.emptyMap(), false);
    recordType.isFrozen = true;

    // When
    String propertyName = "property";
    JSType propertyType = registry.getNativeObjectType(JSTypeNative.NUMBER_TYPE);
    Node propertyNode = Node.NULL();

    // Then
    // Throws an exception as the recordType is frozen
    recordType.defineProperty(propertyName, propertyType, false, propertyNode);
  }

  /**
   * Test getGreatestSubtypeHelper method.
   */
  @Test
  public void testGetGreatestSubtypeHelper() {
    // Given
    JSType type1 = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    JSType type2 = registry.getNativeType(JSTypeNative.INT_TYPE);
    RecordType recordType1 = new RecordType(registry, Collections.emptyMap(), false);
    RecordType recordType2 = new RecordType(registry, Collections.emptyMap(), false);

    // When
    JSType greatestSubtype = recordType1.getGreatestSubtype(type1);

    // Then
    assertEquals("Greatest subtype of Object type should be Object type",
        registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), greatestSubtype);

    greatestSubtype = recordType1.getGreatestSubtype(type2);

    assertEquals("Greatest subtype of Int type should be Object type",
        registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), greatestSubtype);

    greatestSubtype = recordType1.getGreatestSubtype(recordType2);

    assertTrue("recordType1 should be a subtype of recordType2",
        recordType1.isSubtype(recordType2));

    greatestSubtype = recordType2.getGreatestSubtype(recordType1);

    assertTrue("recordType2 should be a subtype of recordType1",
        recordType2.isSubtype(recordType1));

    greatestSubtype = recordType1.getGreatestSubtype(recordType1);

    // Record type is a subtype of itself
    assertEquals(recordType1, greatestSubtype);
  }

  /**
   * Test isSubtype method.
   */
  @Test
  public void testIsSubtype() {
    // Given
    JSType type1 = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    JSType type2 = registry.getNativeType(JSTypeNative.INT_TYPE);
    RecordType recordType1 = new RecordType(registry, Collections.emptyMap(), false);
    RecordType recordType2 = new RecordType(registry, Collections.emptyMap(), false);

    // When
    // Record type is a subtype of itself
    assertTrue("Record type should be a subtype of itself",
        recordType1.isSubtype(recordType1));

    // Object type is a subtype of any record type
    assertTrue("Object type should be a subtype of any record type",
        recordType1.isSubtype(type1));

    assertTrue("Record type1 should be a subtype of record type2 when they're the same",
        recordType1.isSubtype(recordType2));

    assertTrue("Record type1 should be a subtype of record type2 when record type2 is a subtype of record type1",
        recordType1.isSubtype(recordType2.addProperty("prop1", registry.getIntType(), Node.NULL())));

    // Int type is not a subtype of Object type
    assertFalse("Int type should not be a subtype of Object type",
        recordType1.isSubtype(type2));

    // Record type with a property is not a subtype of record type without that property
    recordType1 = new RecordType(registry, Collections.singletonMap("prop1", registry.getStringType()), false);
    assertFalse("Record type with a property should not be a subtype of record type without that property",
        recordType1.isSubtype(recordType2));

    recordType2 = new RecordType(registry, Collections.singletonMap("prop1", registry.getStringType()), false);
    assertTrue("Record type with a property should be a subtype of record type with the same property",
        recordType1.isSubtype(recordType2));
  }

  /**
   * Test resolveInternal method.
   */
  @Test
  public void testResolveInternal() {
    // Given
    RecordType recordType = new RecordType(registry, Collections.emptyMap(), false);
    JSType propertyType = registry.getNativeStringType();
    String propertyName = "property";

    // When
    JSType resolvedType = recordType.resolveInternal(errorReporter, null);

    // Then
    // No properties are resolved yet, so the record type remains the same
    assertEquals("Record type should not be resolved", recordType, resolvedType);

    // When
    JSType propertyTypeToResolve = new RecordType(registry, Collections.singletonMap(propertyName, propertyType), false);
    recordType.properties = new TreeMap<>(recordType.properties);
    recordType.properties.put(propertyName, propertyTypeToResolve);

    resolvedType = recordType.resolveInternal(errorReporter, null);

    // Then
    assertEquals("Record type should be resolved with the added property",
        propertyTypeToResolve, recordType.getPropertyType(propertyName));
    assertEquals("Record type should be resolved to itself", recordType, resolvedType);
  }

  /**
   * Test getGreatestSubtype method.
   */
  @Test
  public void testGetGreatestSubtype() {
    // Given
    RecordType recordType1 = new RecordType(registry, Collections.singletonMap("prop1", registry.getStringType()), false);
    RecordType recordType2 = new RecordType(registry, Collections.singletonMap("prop2", registry.getIntType()), false);

    // When
    JSType greatestSubtype = recordType1.getGreatestSubtype(recordType2);

    // Then
    assertTrue("Record type1 with a property should be a subtype of record type2 without a property",
        recordType1.isSubtype(recordType2));

    // Both record types should have their properties included in the greatest subtype
    UnionTypeBuilder unionTypeBuilder = new UnionTypeBuilder(registry);
    unionTypeBuilder.addAlternate(recordType1);
    unionTypeBuilder.addAlternate(recordType2);
    JSType greatestSubtypeExpected = unionTypeBuilder.build();

    assertEquals("Record types should have the same greatest subtype", greatestSubtypeExpected, greatestSubtype);
  }
}
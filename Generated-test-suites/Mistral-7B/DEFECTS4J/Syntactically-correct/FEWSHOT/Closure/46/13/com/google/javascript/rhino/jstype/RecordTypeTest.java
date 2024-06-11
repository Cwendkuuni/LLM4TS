package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.PrototypeObjectType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import org.junit.Test;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Test cases for {@code RecordType} class.
 */
public class RecordTypeTest {

  @Test
  public void testCreateRecordType() {
    JSTypeRegistry registry = new JSTypeRegistry();

    // Create a record type with properties "a" and "b".
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getNativeIntType()));
    properties.put("b", new RecordProperty("b", registry.getNativeStringType()));
    RecordType recordType = new RecordType(registry, properties);

    // Check properties.
    assertEquals(2, recordType.properties.size());
    assertTrue(recordType.properties.containsKey("a"));
    assertTrue(recordType.properties.containsKey("b"));
    assertEquals(registry.getNativeIntType(), recordType.getPropertyType("a"));
    assertEquals(registry.getNativeStringType(), recordType.getPropertyType("b"));

    // Check frozen state.
    assertFalse(recordType.isFrozen());

    // Freeze the record type.
    recordType.freeze();
    assertTrue(recordType.isFrozen());
  }

  @Test
  public void testIsEquivalentTo() {
    JSTypeRegistry registry = new JSTypeRegistry();

    // Create two record types with identical properties.
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty("a", registry.getNativeIntType()));
    properties1.put("b", new RecordProperty("b", registry.getNativeStringType()));
    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("a", new RecordProperty("a", registry.getNativeIntType()));
    properties2.put("b", new RecordProperty("b", registry.getNativeStringType()));
    RecordType recordType2 = new RecordType(registry, properties2);

    // Check equivalence.
    assertTrue(recordType1.isEquivalentTo(recordType2));

    // Create a record type with an additional property.
    properties2.put("c", new RecordProperty("c", registry.getNativeBooleanType()));
    RecordType recordType3 = new RecordType(registry, properties2);

    // Check inequality.
    assertFalse(recordType1.isEquivalentTo(recordType3));
  }

  @Test
  public void testGetImplicitPrototype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    RecordType recordType = new RecordType(registry, Maps.newHashMap());

    // Check implicit prototype.
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE),
        recordType.getImplicitPrototype());
  }

  @Test
  public void testDefineProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getNativeIntType()));

    RecordType recordType = new RecordType(registry, properties);

    // Define a new property "b".
    recordType.defineProperty("b", registry.getNativeStringType(),
        false, null);

    // Check new property.
    assertEquals(2, recordType.properties.size());
    assertTrue(recordType.properties.containsKey("a"));
    assertTrue(recordType.properties.containsKey("b"));
    assertEquals(registry.getNativeIntType(), recordType.getPropertyType("a"));
    assertEquals(registry.getNativeStringType(), recordType.getPropertyType("b"));
  }

  @Test
  public void testGetLeastSupertype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty("a", registry.getNativeIntType()));
    RecordType recordTypeA = new RecordType(registry, propertiesA);

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("a", new RecordProperty("a", registry.getNativeIntType()));
    propertiesB.put("b", new RecordProperty("b", registry.getNativeStringType()));
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    // Check least supertype.
    assertEquals(recordTypeB, recordTypeA.getLeastSupertype(recordTypeB));

    // Check least supertype with a different record type.
    Map<String, RecordProperty> propertiesC = Maps.newHashMap();
    propertiesC.put("c", new RecordProperty("c", registry.getNativeBooleanType()));
    RecordType recordTypeC = new RecordType(registry, propertiesC);

    assertNull(recordTypeA.getLeastSupertype(recordTypeC));

    // Check least supertype with a primitive type.
    JSType primitiveType = registry.getNativeIntType();
    assertNull(recordTypeA.getLeastSupertype(primitiveType));
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty("a", registry.getNativeIntType()));
    RecordType recordTypeA = new RecordType(registry, propertiesA);

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("a", new RecordProperty("a", registry.getNativeIntType()));
    propertiesB.put("b", new RecordProperty("b", registry.getNativeStringType()));
    propertiesB.put("c", new RecordProperty("c", registry.getNativeBooleanType()));
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    // Check greatest subtype helper.
    JSType greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeB);

    // Check greatest subtype has the same properties as the record type B.
    assertEquals(3, greatestSubtype.getDeclaredProperties().size());
    assertTrue(greatestSubtype.getDeclaredPropertyNames().contains("a"));
    assertTrue(greatestSubtype.getDeclaredPropertyNames().contains("b"));
    assertTrue(greatestSubtype.getDeclaredPropertyNames().contains("c"));
  }

  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    // Create record types.
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty("a", registry.getNativeIntType()));
    propertiesA.put("b", new RecordProperty("b", registry.getNativeStringType()));
    RecordType recordTypeA = new RecordType(registry, propertiesA);

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("a", new RecordProperty("a", registry.getNativeIntType()));
    propertiesB.put("b", new RecordProperty("b", registry.getNativeStringType()));
    propertiesB.put("c", new RecordProperty("c", registry.getNativeBooleanType()));
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    Map<String, RecordProperty> propertiesC = Maps.newHashMap();
    propertiesC.put("a", new RecordProperty("a", registry.getNativeIntType()));
    propertiesC.put("d", new RecordProperty("d", registry.getNativeBooleanType()));
    RecordType recordTypeC = new RecordType(registry, propertiesC);

    // Check subtype relationships.
    assertTrue(recordTypeA.isSubtype(recordTypeA));
    assertTrue(recordTypeA.isSubtype(recordTypeB));
    assertFalse(recordTypeB.isSubtype(recordTypeA));
    assertFalse(recordTypeA.isSubtype(recordTypeC));
    assertFalse(recordTypeC.isSubtype(recordTypeA));
  }

  @Test
  public void testIsSubtypeWithUnionType() {
    JSTypeRegistry registry = new JSTypeRegistry();

    // Create record types.
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty("a", registry.getNativeIntType()));
    RecordType recordTypeA = new RecordType(registry, propertiesA);

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("a", new RecordProperty("a", registry.getNativeIntType()));
    propertiesB.put("b", new RecordProperty("b", registry.getNativeStringType()));
    propertiesB.put("c", new RecordProperty("c", registry.getNativeBooleanType()));
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    Map<String, RecordProperty> propertiesC = Maps.newHashMap();
    propertiesC.put("a", new RecordProperty("a", registry.getNativeIntType()));
    propertiesC.put("d", new RecordProperty("d", registry.getNativeBooleanType()));
    RecordType recordTypeD = new RecordType(registry, propertiesC);

    UnionTypeBuilder unionBuilder = new UnionTypeBuilder(registry);
    unionBuilder.addAlternate(recordTypeB);
    unionBuilder.addAlternate(recordTypeD);
    JSType unionType = unionBuilder.build();

    // Check subtype relationships.
    assertTrue(recordTypeA.isSubtype(recordTypeA));
    assertTrue(recordTypeA.isSubtype(unionType));
    assertFalse(unionType.isSubtype(recordTypeA));
  }

  @Test
  public void testResolve() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getNativeIntType()));
    properties.put("b", new RecordProperty("b", registry.getNativeStringType()));

    RecordType recordType = new RecordType(registry, properties);

    ErrorReporter errorReporter = new ErrorReporter() {
      @Override
      public void error(int line, int character, String message) {
        throw new RuntimeException("Error at line " + line + ":" + character +
            ": " + message);
      }

      @Override
      public void warning(int line, int character, String message) {
        System.out.println("Warning at line " + line + ":" + character +
            ": " + message);
      }
    };

    StaticScope<JSType> scope = new StaticScope<JSType>(null, null, null);

    JSType resolvedType = recordType.resolve(errorReporter, scope);

    // Check resolved type.
    assertEquals(recordType, resolvedType);
  }
}
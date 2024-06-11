package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import com.google.javascript.rhino.jstype.UnionType;
import com.google.javascript.rhino.jstype.NativeObjectType;
import com.google.javascript.rhino.jstype.NativeType;
import com.google.javascript.rhino.jstype.NativeTypeNative;

public class RecordTypeTest {

  @Test
  public void testIsEquivalentTo() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getPrimitiveType(JSTypeNative.INT_TYPE)));
    properties.put("b", new RecordProperty("b", registry.getPrimitiveType(JSTypeNative.BOOL_TYPE)));

    RecordType recordType = new RecordType(registry, properties);

    // Test with same record type
    RecordType recordType1 = new RecordType(registry, properties);
    assertTrue(recordType.isEquivalentTo(recordType1));

    // Test with different record type
    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("a", new RecordProperty("a", registry.getPrimitiveType(JSTypeNative.INT_TYPE)));
    properties2.put("c", new RecordProperty("c", registry.getPrimitiveType(JSTypeNative.INT_TYPE)));

    RecordType recordType2 = new RecordType(registry, properties2);
    assertFalse(recordType.isEquivalentTo(recordType2));

    // Test with different order of properties
    Map<String, RecordProperty> properties3 = Maps.newHashMap();
    properties3.put("c", new RecordProperty("c", registry.getPrimitiveType(JSTypeNative.INT_TYPE)));
    properties3.put("a", new RecordProperty("a", registry.getPrimitiveType(JSTypeNative.INT_TYPE)));

    RecordType recordType3 = new RecordType(registry, properties3);
    assertTrue(recordType.isEquivalentTo(recordType1));
  }

  @Test
  public void testGetImplicitPrototype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getPrimitiveType(JSTypeNative.INT_TYPE)));

    RecordType recordType = new RecordType(registry, properties);
    ObjectType implicitPrototype = recordType.getImplicitPrototype();
    assertTrue(implicitPrototype instanceof NativeObjectType);
    assertEquals(NativeObjectType.OBJECT_TYPE, implicitPrototype.getId());
  }

  @Test
  public void testDefineProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getPrimitiveType(JSTypeNative.INT_TYPE)));

    RecordType recordType = new RecordType(registry, properties);
    JSType type = registry.getPrimitiveType(JSTypeNative.STRING_TYPE);

    String propertyName = "b";
    boolean inferred = false;
    Node propertyNode = new Node_Property(); // Dummy node

    boolean result = recordType.defineProperty(propertyName, type, inferred, propertyNode);
    assertTrue(result);

    JSType propertyType = recordType.getPropertyType(propertyName);
    assertEquals(type, propertyType);
  }

  @Test
  public void testDefinePropertyFrozen() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getPrimitiveType(JSTypeNative.INT_TYPE)));

    RecordType recordType = new RecordType(registry, properties);
    recordType.setFrozen(true);

    JSType type = registry.getPrimitiveType(JSTypeNative.STRING_TYPE);

    String propertyName = "b";
    boolean inferred = false;
    Node propertyNode = new Node_Property(); // Dummy node

    boolean result = recordType.defineProperty(propertyName, type, inferred, propertyNode);
    assertFalse(result);
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty("a", registry.getPrimitiveType(JSTypeNative.INT_TYPE)));
    RecordType recordTypeA = new RecordType(registry, propertiesA);

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("a", new RecordProperty("a", registry.getPrimitiveType(JSTypeNative.INT_TYPE)));
    propertiesB.put("b", new RecordProperty("b", registry.getPrimitiveType(JSTypeNative.BOOL_TYPE)));
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    UnionType greatestSubtypeAB = recordTypeA.getGreatestSubtypeHelper(recordTypeB);
    assertTrue(greatestSubtypeAB instanceof UnionType);

    UnionTypeBuilder builder = new UnionTypeBuilder(registry);
    builder.addAlternate(recordTypeB);
    UnionType expected = builder.build();
    assertEquals(expected, greatestSubtypeAB);
  }

  @Test
  public void testGetGreatestSubtypeHelperEmptyRecord() {
    JSTypeRegistry registry = new JSTypeRegistry();

    RecordType recordTypeA = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    JSType greatestSubtypeAB = recordTypeA.getGreatestSubtypeHelper(recordTypeA);
    assertTrue(greatestSubtypeAB instanceof NativeObjectType);
    assertEquals(NativeObjectType.OBJECT_TYPE, greatestSubtypeAB.getId());
  }

  @Test
  public void testGetGreatestSubtypeHelperNative() {
    JSTypeRegistry registry = new JSTypeRegistry();

    JSType typeA = registry.getPrimitiveType(JSTypeNative.INT_TYPE);
    JSType typeB = registry.getPrimitiveType(JSTypeNative.STRING_TYPE);

    JSType greatestSubtypeAB = typeA.getGreatestSubtypeHelper(typeB);
    assertTrue(greatestSubtypeAB instanceof NativeType);
    assertEquals(NativeTypeNative.NO_OBJECT_TYPE, greatestSubtypeAB.getId());
  }

  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty("a", registry.getPrimitiveType(JSTypeNative.INT_TYPE)));
    RecordType recordTypeA = new RecordType(registry, propertiesA);

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("a", new RecordProperty("a", registry.getPrimitiveType(JSTypeNative.INT_TYPE)));
    propertiesB.put("b", new RecordProperty("b", registry.getPrimitiveType(JSTypeNative.BOOL_TYPE)));
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    assertTrue(recordTypeA.isSubtype(recordTypeA));
    assertTrue(recordTypeA.isSubtype(recordTypeB));

    Map<String, RecordProperty> propertiesC = Maps.newHashMap();
    propertiesC.put("a", new RecordProperty("a", registry.getPrimitiveType(JSTypeNative.INT_TYPE)));
    propertiesC.put("c", new RecordProperty("c", registry.getPrimitiveType(JSTypeNative.INT_TYPE)));
    RecordType recordTypeC = new RecordType(registry, propertiesC);

    assertFalse(recordTypeA.isSubtype(recordTypeC));

    Map<String, RecordProperty> propertiesD = Maps.newHashMap();
    propertiesD.put("a", new RecordProperty("a", registry.getPrimitiveType(JSTypeNative.INT_TYPE)));
    propertiesD.put("b", new RecordProperty("b", registry.getPrimitiveType(JSTypeNative.BOOL_TYPE)));
    propertiesD.put("c", new RecordProperty("c", registry.getPrimitiveType(JSTypeNative.INT_TYPE)));
    RecordType recordTypeD = new RecordType(registry, propertiesD);

    assertTrue(recordTypeA.isSubtype(recordTypeD));
  }

  @Test
  public void testIsSubtypeNative() {
    JSTypeRegistry registry = new JSTypeRegistry();

    JSType typeA = registry.getPrimitiveType(JSTypeNative.INT_TYPE);
    JSType typeB = registry.getPrimitiveType(JSTypeNative.BOOL_TYPE);

    RecordType recordType = new RecordType(registry, Maps.newHashMap());
    recordType.defineProperty("a", typeA, false, null);

    assertTrue(typeA.isSubtype(recordType));
  }

  @Test
  public void testResolveInternal() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getPrimitiveType(JSTypeNative.INT_TYPE)));

    RecordType recordType = new RecordType(registry, properties);

    JSType typeA = registry.getPrimitiveType(JSTypeNative.STRING_TYPE);
    JSType typeB = registry.getPrimitiveType(JSTypeNative.INT_TYPE);

    properties.put("a", new RecordProperty("a", typeB));

    ErrorReporter errorReporter = new ErrorReporter() {
      @Override
      public void error(String message, Object... args) {
        fail(message + ": " + Arrays.toString(args));
      }
    };

    StaticScope<JSType> scope = new StaticScope<>();
    JSType resolvedType = recordType.resolveInternal(errorReporter, scope);
    assertEquals(typeB, resolvedType);
  }
}
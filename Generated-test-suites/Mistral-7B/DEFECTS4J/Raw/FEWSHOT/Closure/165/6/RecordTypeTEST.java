package com.google.javascript.rhino.jstype.test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeFactory;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionType;
import com.google.javascript.rhino.jstype.TypeRegistry;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class RecordTypeTest {

  private static final TypeRegistry REGISTRY = new TypeRegistry() {};
  private RecordType type;
  private RecordTypeBuilder builder;

  @Before
  public void setUp() {
    builder = new RecordTypeBuilder(REGISTRY);

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("x", new RecordProperty("x", JSTypeFactory.intType()));
    properties.put("y", new RecordProperty("y", JSTypeFactory.booleanType()));

    type = new RecordType(REGISTRY, properties);
  }

  @Test
  public void testIsSubtype() {
    RecordType subType = new RecordType(REGISTRY, properties);
    assertTrue(type.isSubtype(subType));

    JSType otherType = JSTypeFactory.objectType();
    assertFalse(type.isSubtype(otherType));

    JSType subTypeWithExtraProperty = new RecordType(REGISTRY,
        Maps.<String, RecordProperty>newHashMap(properties)
            .put("z", new RecordProperty("z", JSTypeFactory.intType())));
    assertFalse(type.isSubtype(subTypeWithExtraProperty));

    JSType subTypeWithIncompatibleProperty = new RecordType(REGISTRY,
        Maps.<String, RecordProperty>newHashMap(properties)
            .put("x", JSTypeFactory.doubleType()));
    assertFalse(type.isSubtype(subTypeWithIncompatibleProperty));

    JSType subTypeWithMissingProperty = new RecordType(REGISTRY,
        Maps.<String, RecordProperty>newHashMap(Maps.difference(
            properties.keySet(), subType.properties.keySet())));
    assertFalse(type.isSubtype(subTypeWithMissingProperty));

    UnionType unionType = new UnionType(REGISTRY, SortedMap.copyOf(type.properties));
    assertTrue(type.isSubtype(unionType));
  }

  @Test
  public void testDefineProperty() {
    type.defineProperty("z", JSTypeFactory.intType(), false, null);

    assertTrue(type.hasProperty("z"));
  }

  @Test(expected = IllegalStateException.class)
  public void testDefinePropertyWithNullRecordProperty() {
    type.defineProperty("x", null, false, null);
  }

  @Test
  public void testDefinePropertyTwice() {
    type.defineProperty("x", JSTypeFactory.intType(), false, null);
    type.defineProperty("x", JSTypeFactory.integerType(), false, null);

    assertTrue(type.getPropertyType("x").isEquivalentTo(JSTypeFactory.integerType()));
  }

  @Test
  public void testIsEquivalentTo() {
    RecordType other = new RecordType(REGISTRY, properties);
    assertTrue(type.isEquivalentTo(other));
  }

  @Test
  public void testIsEquivalentToDifferentOrderProperties() {
    Map<String, RecordProperty> reversedProperties = Maps.newHashMap(properties);
    reversedProperties.keySet().forEach(reversedProperties::put);

    RecordType reversedType = new RecordType(REGISTRY, reversedProperties);
    assertTrue(type.isEquivalentTo(reversedType));
  }

  @Test
  public void testGetImplicitPrototype() {
    assertEquals(JSTypeFactory.objectType(), type.getImplicitPrototype());
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    JSType anotherRecordType = new RecordType(REGISTRY, properties);

    JSType greatestSubtype = type.getGreatestSubtypeHelper(anotherRecordType);
    assertEquals(type, greatestSubtype);

    JSType anotherRecordTypeWithExtraProperty = new RecordType(REGISTRY,
        Maps.<String, RecordProperty>newHashMap(properties)
            .put("z", new RecordProperty("z", JSTypeFactory.intType())));

    JSType greatestSubtypeWithExtraProperty = type.getGreatestSubtypeHelper(anotherRecordTypeWithExtraProperty);
    assertEquals(type, greatestSubtypeWithExtraProperty);

    JSType anotherRecordTypeWithIncompatibleProperty = new RecordType(REGISTRY,
        Maps.<String, RecordProperty>newHashMap(properties)
            .put("x", new RecordProperty("x", JSTypeFactory.doubleType())));

    UnionType greatestSubtypeWithIncompatibleProperty = (UnionType) type.getGreatestSubtypeHelper(anotherRecordTypeWithIncompatibleProperty);
    assertEquals(JSTypeFactory.objectType(), greatestSubtypeWithIncompatibleProperty);
  }
}
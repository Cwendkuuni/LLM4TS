package com.google.javascript.rhino.jstype.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeFactory;
import com.google.javascript.rhino.jstype.PrototypeObjectType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.TypeRegistry;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class RecordTypeTest {

  private static TypeRegistry registry;
  private static ErrorReporter errorReporter;

  @org.junit.BeforeClass
  public static void setup() {
    registry = new TypeRegistry();
    errorReporter = ErrorReporter.NULL;
  }

  @org.junit.Test
  public void testRecordTypeConstruction() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", JSTypeFactory.createStringType()));
    properties.put("b", new RecordProperty("b", JSTypeFactory.createNumberType()));

    RecordType recordType = new RecordType(registry, properties);
    assertNotNull(recordType);
  }

  @org.junit.Test
  public void testRecordTypeEquivalence() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty("a", JSTypeFactory.createStringType()));
    properties1.put("b", new RecordProperty("b", JSTypeFactory.createNumberType()));

    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("a", new RecordProperty("a", JSTypeFactory.createStringType()));
    properties2.put("b", new RecordProperty("b", JSTypeFactory.createNumberType()));

    RecordType recordType2 = new RecordType(registry, properties2);

    assertEquals(recordType1, recordType2);
  }

  @org.junit.Test
  public void testRecordTypeInequivalence() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty("a", JSTypeFactory.createStringType()));
    properties1.put("b", new RecordProperty("b", JSTypeFactory.createNumberType()));

    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("a", new RecordProperty("a", JSTypeFactory.createNumberType()));
    properties2.put("b", new RecordProperty("b", JSTypeFactory.createStringType()));

    RecordType recordType2 = new RecordType(registry, properties2);

    assertFalse(recordType1.equals(recordType2));
  }

  @org.junit.Test
  public void testRecordTypeIsSubtypeOfRecordType() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty("a", JSTypeFactory.createStringType()));
    properties1.put("b", new RecordProperty("b", JSTypeFactory.createNumberType()));

    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("b", new RecordProperty("b", JSTypeFactory.createNumberType()));
    properties2.put("a", new RecordProperty("a", JSTypeFactory.createStringType()));

    RecordType recordType2 = new RecordType(registry, properties2);

    assertTrue(recordType1.isSubtype(recordType2));
  }

  @org.junit.Test
  public void testRecordTypeIsNotSubtypeOfRecordType() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty("a", JSTypeFactory.createStringType()));
    properties1.put("b", new RecordProperty("b", JSTypeFactory.createNumberType()));

    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("a", new RecordProperty("a", JSTypeFactory.createNumberType()));

    RecordType recordType2 = new RecordType(registry, properties2);

    assertFalse(recordType1.isSubtype(recordType2));
  }

  @org.junit.Test
  public void testRecordTypeIsSubtypeOfUnknownType() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    RecordType recordType = new RecordType(registry, properties);

    assertFalse(recordType.isSubtype(JSTypeNative.UNDEFINED_TYPE));
    assertFalse(recordType.isSubtype(JSTypeNative.NULL_TYPE));
    assertFalse(recordType.isSubtype(JSTypeNative.NO_TYPE));
    assertFalse(recordType.isSubtype(JSTypeNative.OBJECT_TYPE));
  }

  @org.junit.Test
  public void testRecordTypeIsSubtypeOfObjectType() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", JSTypeFactory.createStringType()));

    RecordType recordType = new RecordType(registry, properties);

    assertTrue(recordType.isSubtype(JSTypeNative.OBJECT_TYPE));
  }

  @org.junit.Test
  public void testRecordTypeIsSubtypeOfNativeType() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", JSTypeFactory.createStringType()));

    RecordType recordType = new RecordType(registry, properties);

    assertTrue(recordType.isSubtype(JSTypeFactory.createStringType()));
    assertTrue(recordType.isSubtype(JSTypeFactory.createObjectTypeWithProperty("a", JSTypeFactory.createStringType())));
    assertTrue(recordType.isSubtype(JSTypeFactory.createRecordTypeWithProperties("a", JSTypeFactory.createStringType())));

    assertFalse(recordType.isSubtype(JSTypeFactory.createNumberType()));
    assertFalse(recordType.isSubtype(JSTypeFactory.createBooleanType()));
    assertFalse(recordType.isSubtype(JSTypeFactory.createNullType()));
    assertFalse(recordType.isSubtype(JSTypeFactory.createUndefinedType()));
  }

  @org.junit.Test
  public void testRecordTypeIsSubtypeOfUnionType() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", JSTypeFactory.createStringType()));

    RecordType recordType = new RecordType(registry, properties);

    JSType unionType = new UnionTypeBuilder(registry)
        .addAlternate(JSTypeFactory.createStringType())
        .addAlternate(JSTypeFactory.createNumberType())
        .build();

    assertTrue(recordType.isSubtype(unionType));
  }

  @org.junit.Test
  public void testRecordTypeIsSubtypeOfEmptyRecordType() {
    Map<String, RecordProperty> properties = Maps.newHashMap();

    RecordType recordType = new RecordType(registry, properties);

    assertTrue(recordType.isSubtype(JSTypeNative.OBJECT_TYPE));
  }

  @org.junit.Test
  public void testRecordTypeIsFrozen() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", JSTypeFactory.createStringType()));

    RecordType recordType = new RecordType(registry, properties);

    assertFalse(recordType.isFrozen());

    recordType.freeze();

    assertTrue(recordType.isFrozen());
  }

  @org.junit.Test
  public void testRecordTypeHasProperty() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", JSTypeFactory.createStringType()));

    RecordType recordType = new RecordType(registry, properties);

    assertTrue(recordType.hasProperty("a"));
    assertFalse(recordType.hasProperty("b"));
  }

  @org.junit.Test
  public void testRecordTypeGetPropertyType() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", JSTypeFactory.createStringType()));

    RecordType recordType = new RecordType(registry, properties);

    assertEquals(JSTypeFactory.createStringType(), recordType.getPropertyType("a"));
    assertNull(recordType.getPropertyType("b"));
  }

  @org.junit.Test
  public void testRecordTypeGetPropertyNode() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", JSTypeFactory.createStringType()));

    RecordType recordType = new RecordType(registry, properties);

    assertEquals(Node.NULLOBJECT, recordType.getPropertyNode("a"));
    assertNull(recordType.getPropertyNode("b"));
  }

  @org.junit.Test
  public void testRecordTypeGetLeastSupertype() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty("a", JSTypeFactory.createStringType()));
    properties1.put("b", new RecordProperty("b", JSTypeFactory.createNumberType()));

    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("a", new RecordProperty("a", JSTypeFactory.createStringType()));

    RecordType recordType2 = new RecordType(registry, properties2);

    Map<String, RecordProperty> properties3 = Maps.newHashMap();
    properties3.put("a", new RecordProperty("a", JSTypeFactory.createStringType()));
    properties3.put("b", new RecordProperty("b", JSTypeFactory.createStringType()));

    RecordType recordType3 = new RecordType(registry, properties3);

    assertEquals(recordType1, recordType1.getLeastSupertype(recordType1));
    assertEquals(recordType1, recordType1.getLeastSupertype(recordType2));
    assertEquals(recordType1, recordType1.getLeastSupertype(recordType3));

    assertEquals(recordType1, recordType2.getLeastSupertype(recordType1));
    assertEquals(recordType1, recordType2.getLeastSupertype(recordType2));
    assertEquals(recordType1, recordType2.getLeastSupertype(recordType3));

    assertEquals(recordType1, recordType3.getLeastSupertype(recordType1));
    assertEquals(recordType1, recordType3.getLeastSupertype(recordType2));
    assertEquals(recordType1, recordType3.getLeastSupertype(recordType3));

    assertEquals(JSTypeNative.OBJECT_TYPE, recordType1.getLeastSupertype(JSTypeNative.OBJECT_TYPE));
    assertEquals(JSTypeNative.OBJECT_TYPE, recordType1.getLeastSupertype(JSTypeFactory.createStringType()));
  }

  @org.junit.Test
  public void testRecordTypeGetGreatestSubtype() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty("a", JSTypeFactory.createStringType()));
    properties1.put("b", new RecordProperty("b", JSTypeFactory.createNumberType()));

    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("a", new RecordProperty("a", JSTypeFactory.createStringType()));
    properties2.put("c", new RecordProperty("c", JSTypeFactory.createNumberType()));

    RecordType recordType2 = new RecordType(registry, properties2);

    assertEquals(recordType1, recordType1.getGreatestSubtype(recordType1));
    assertEquals(recordType1, recordType1.getGreatestSubtype(recordType2));
    assertEquals(recordType2, recordType2.getGreatestSubtype(recordType1));
    assertEquals(recordType2, recordType2.getGreatestSubtype(recordType2));

    assertEquals(recordType1, recordType1.getGreatestSubtype(JSTypeFactory.createRecordTypeWithProperties("a", JSTypeFactory.createStringType())));
    assertEquals(recordType2, recordType2.getGreatestSubtype(JSTypeFactory.createRecordTypeWithProperties("a", JSTypeFactory.createStringType(), "c", JSTypeFactory.createNumberType())));

    assertNull(recordType1.getGreatestSubtype(JSTypeFactory.createStringType()));
    assertNull(recordType1.getGreatestSubtype(JSTypeFactory.createNumberType()));
  }

  @org.junit.Test
  public void testRecordTypeToString() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", JSTypeFactory.createStringType()));

    RecordType recordType = new RecordType(registry, properties);

    String recordTypeString = recordType.toString();

    String expectedRecordTypeString = "{a:String}";

    assertEquals(expectedRecordTypeString, recordTypeString);
  }
}
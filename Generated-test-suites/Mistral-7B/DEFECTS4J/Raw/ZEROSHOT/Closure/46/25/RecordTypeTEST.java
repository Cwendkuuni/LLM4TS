package com.google.javascript.rhino.jstype.test;

import static org.junit.Assert.*;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.PrototypeObjectType;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;

import java.util.Map;
import java.util.SortedMap;

public class RecordTypeTest {

  private static JSTypeRegistry registry;
  private static ErrorReporter errorReporter;

  @org.junit.BeforeClass
  public static void setUpBeforeClass() throws Exception {
    registry = new JSTypeRegistry();
    errorReporter = new ErrorReporter() {
      @Override
      public void error(int line, int lineNumber, String message) {
        fail("Unexpected error: " + message);
      }
    };
  }

  @org.junit.After
  public void tearDown() {
    registry = null; // to avoid memory leaks
  }

  @org.junit.Test
  public void testRecordTypeCreation() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("name", new RecordProperty("name", registry.StringType()));
    recordProperties.put("age", new RecordProperty("age", registry.NumberType()));

    RecordType recordType = new RecordType(registry, recordProperties);
    assertNotNull(recordType);
    assertEquals("RecordType", recordType.getClassName());
    assertEquals(2, recordType.getDeclaredProperties().length);

    JSType primitiveType = registry.getStringType();
    JSType recordTypeInstance = recordType.getInstanceType();
    JSType recordTypeDeclared = recordType.getDeclaredType();

    // recordTypeDeclared and recordTypeInstance should be the same
    assertEquals(recordTypeDeclared, recordTypeInstance);
    // recordTypeInstance should be a subtype of recordTypeDeclared
    assertTrue(recordTypeInstance.isSubtype(recordTypeDeclared));
    // recordTypeDeclared should be a supertype of recordTypeInstance
    assertTrue(recordTypeDeclared.isSupertype(recordTypeInstance));
    // recordTypeInstance should be a subtype of ObjectType
    assertTrue(recordTypeInstance.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
    // recordTypeDeclared should be a supertype of ObjectType
    assertTrue(recordTypeDeclared.isSupertype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
  }

  @org.junit.Test
  public void testRecordTypeProperties() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("name", new RecordProperty("name", registry.StringType()));
    recordProperties.put("age", new RecordProperty("age", registry.NumberType()));

    RecordType recordType = new RecordType(registry, recordProperties);
    assertEquals(2, recordType.getDeclaredProperties().length);

    String nameProperty = "name";
    assertNotNull(recordType.getPropertyType(nameProperty));
    assertEquals(registry.StringType(), recordType.getPropertyType(nameProperty));

    String nonExistentProperty = "nonexistentProperty";
    assertFalse(recordType.hasProperty(nonExistentProperty));
  }

  @org.junit.Test
  public void testRecordTypeEquivalence() {
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("name", new RecordProperty("name", registry.StringType()));
    recordProperties1.put("age", new RecordProperty("age", registry.NumberType()));

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("name", new RecordProperty("name", registry.StringType()));
    recordProperties2.put("age", new RecordProperty("age", registry.NumberType()));

    RecordType recordType1 = new RecordType(registry, recordProperties1);
    RecordType recordType2 = new RecordType(registry, recordProperties2);

    assertTrue(recordType1.isEquivalentTo(recordType2));
  }

  @org.junit.Test
  public void testRecordTypeSubtyping() {
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("name", new RecordProperty("name", registry.StringType()));
    recordProperties1.put("age", new RecordProperty("age", registry.NumberType()));

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("name", new RecordProperty("name", registry.StringType()));
    recordProperties2.put("age", new RecordProperty("age", registry.NumberType()));
    recordProperties2.put("sex", new RecordProperty("sex", registry.StringType()));

    RecordType recordType1 = new RecordType(registry, recordProperties1);
    RecordType recordType2 = new RecordType(registry, recordProperties2);

    // recordType1 is a supertype of recordType2 because it has all the properties of recordType2
    assertTrue(recordType1.isSupertype(recordType2));

    // recordType2 is a subtype of recordType1 because it has all the properties of recordType1
    assertTrue(recordType2.isSubtype(recordType1));
  }

  @org.junit.Test
  public void testRecordTypeLeastSupertype() {
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("name", new RecordProperty("name", registry.StringType()));
    recordProperties1.put("age", new RecordProperty("age", registry.NumberType()));

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("name", new RecordProperty("name", registry.StringType()));
    recordProperties2.put("age", new RecordProperty("age", registry.NumberType()));
    recordProperties2.put("sex", new RecordProperty("sex", registry.StringType()));

    RecordType recordType1 = new RecordType(registry, recordProperties1);
    RecordType recordType2 = new RecordType(registry, recordProperties2);

    RecordType leastCommonSupertype = recordType1.getLeastSupertype(recordType2);

    // leastCommonSupertype should be a record type with properties "name" and "age"
    assertEquals(2, leastCommonSupertype.getDeclaredProperties().length);
  }

  @org.junit.Test
  public void testRecordTypeGreatestSubtype() {
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("name", new RecordProperty("name", registry.StringType()));
    recordProperties1.put("age", new RecordProperty("age", registry.NumberType()));

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("name", new RecordProperty("name", registry.StringType()));
    recordProperties2.put("age", new RecordProperty("age", registry.NumberType()));
    recordProperties2.put("sex", new RecordProperty("sex", registry.StringType()));

    RecordType recordType1 = new RecordType(registry, recordProperties1);
    RecordType recordType2 = new RecordType(registry, recordProperties2);

    RecordType greatestSubtype = recordType1.getGreatestSubtype(recordType2);

    // greatestSubtype should be a record type with properties "name" and "age"
    assertEquals(2, greatestSubtype.getDeclaredProperties().length);
  }

  @org.junit.Test
  public void testRecordTypeIsFrozen() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("name", new RecordProperty("name", registry.StringType()));
    recordProperties.put("age", new RecordProperty("age", registry.NumberType()));

    RecordType recordType = new RecordType(registry, recordProperties);
    assertFalse(recordType.isFrozen());

    recordType.freeze();
    assertTrue(recordType.isFrozen());
  }

  @org.junit.Test
  public void testRecordTypeFreeze() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("name", new RecordProperty("name", registry.StringType()));
    recordProperties.put("age", new RecordProperty("age", registry.NumberType()));

    RecordType recordType = new RecordType(registry, recordProperties);

    try {
      recordType.freeze(); // should not be able to freeze twice
      fail("Expected an IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  @org.junit.Test
  public void testRecordTypeHasProperty() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("name", new RecordProperty("name", registry.StringType()));
    recordProperties.put("age", new RecordProperty("age", registry.NumberType()));

    RecordType recordType = new RecordType(registry, recordProperties);

    String nameProperty = "name";
    assertTrue(recordType.hasProperty(nameProperty));

    String nonExistentProperty = "nonexistentProperty";
    assertFalse(recordType.hasProperty(nonExistentProperty));
  }

  @org.junit.Test
  public void testRecordTypeIsSupertypeOfObjectType() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("name", new RecordProperty("name", registry.StringType()));
    recordProperties.put("age", new RecordProperty("age", registry.NumberType()));

    RecordType recordType = new RecordType(registry, recordProperties);

    assertTrue(recordType.isSupertype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
  }

  @org.junit.Test
  public void testRecordTypeIsSubtypeOfStringType() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("name", new RecordProperty("name", registry.StringType()));

    RecordType recordType = new RecordType(registry, recordProperties);

    JSType stringType = registry.getStringType();

    assertFalse(recordType.isSubtype(stringType));
  }

  @org.junit.Test
  public void testRecordTypeIsSubtypeOfRecordTypeWithLessProperties() {
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("name", new RecordProperty("name", registry.StringType()));
    recordProperties1.put("age", new RecordProperty("age", registry.NumberType()));

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("name", new RecordProperty("name", registry.StringType()));

    RecordType recordType1 = new RecordType(registry, recordProperties1);
    RecordType recordType2 = new RecordType(registry, recordProperties2);

    assertTrue(recordType2.isSubtype(recordType1));
  }

  @org.junit.Test
  public void testRecordTypeIsSubtypeOfRecordTypeWithMoreProperties() {
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("name", new RecordProperty("name", registry.StringType()));
    recordProperties1.put("age", new RecordProperty("age", registry.NumberType()));

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("name", new RecordProperty("name", registry.StringType()));
    recordProperties2.put("age", new RecordProperty("age", registry.NumberType()));
    recordProperties2.put("sex", new RecordProperty("sex", registry.StringType()));

    RecordType recordType1 = new RecordType(registry, recordProperties1);
    RecordType recordType2 = new RecordType(registry, recordProperties2);

    assertFalse(recordType1.isSubtype(recordType2));
  }

  @org.junit.Test
  public void testRecordTypeIsSubtypeOfRecordTypeWithSamePropertiesButDifferentTypes() {
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("name", new RecordProperty("name", registry.StringType()));
    recordProperties1.put("age", new RecordProperty("age", registry.NumberType()));

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("name", new RecordProperty("name", registry.BooleanType()));
    recordProperties2.put("age", new RecordProperty("age", registry.NumberType()));

    RecordType recordType1 = new RecordType(registry, recordProperties1);
    RecordType recordType2 = new RecordType(registry, recordProperties2);

    assertFalse(recordType1.isSubtype(recordType2));
  }

  @org.junit.Test
  public void testRecordTypeIsSubtypeOfRecordTypeWithIncompatibleTypes() {
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("name", new RecordProperty("name", registry.StringType()));
    recordProperties1.put("age", new RecordProperty("age", registry.NumberType()));

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("name", new RecordProperty("name", registry.NumberType()));
    recordProperties2.put("age", new RecordProperty("age", registry.StringType()));

    RecordType recordType1 = new RecordType(registry, recordProperties1);
    RecordType recordType2 = new RecordType(registry, recordProperties2);

    assertFalse(recordType1.isSubtype(recordType2));
  }

  @org.junit.Test
  public void testRecordTypeIsSubtypeOfRecordTypeWithNullTypes() {
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("name", new RecordProperty("name", registry.StringType()));
    recordProperties1.put("age", new RecordProperty("age", registry.NumberType()));

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("name", new RecordProperty("name", null));
    recordProperties2.put("age", new RecordProperty("age", null));

    RecordType recordType1 = new RecordType(registry, recordProperties1);
    RecordType recordType2 = new RecordType(registry, recordProperties2);

    assertTrue(recordType1.isSubtype(recordType2));
  }

  @org.junit.Test
  public void testRecordTypeIsSubtypeOfRecordTypeWithUnionTypes() {
    UnionTypeBuilder nameUnionBuilder = new UnionTypeBuilder(registry);
    nameUnionBuilder.addAlternate(registry.getNativeObjectType(JSTypeNative.STRING_TYPE));
    nameUnionBuilder.addAlternate(registry.getNativeObjectType(JSTypeNative.NUMBER_TYPE));
    JSType nameUnionType = nameUnionBuilder.build();

    UnionTypeBuilder ageUnionBuilder = new UnionTypeBuilder(registry);
    ageUnionBuilder.addAlternate(registry.getNativeObjectType(JSTypeNative.STRING_TYPE));
    ageUnionBuilder.addAlternate(registry.getNativeObjectType(JSTypeNative.NUMBER_TYPE));
    ageUnionBuilder.addAlternate(registry.getNativeObjectType(JSTypeNative.BOOLEAN_TYPE));
    JSType ageUnionType = ageUnionBuilder.build();

    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("name", new RecordProperty("name", registry.StringType()));
    recordProperties1.put("age", new RecordProperty("age", registry.NumberType()));

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("name", new RecordProperty("name", nameUnionType));
    recordProperties2.put("age", new RecordProperty("age", ageUnionType));

    RecordType recordType1 = new RecordType(registry, recordProperties1);
    RecordType recordType2 = new RecordType(registry, recordProperties2);

    assertTrue(recordType1.isSubtype(recordType2));
  }
}
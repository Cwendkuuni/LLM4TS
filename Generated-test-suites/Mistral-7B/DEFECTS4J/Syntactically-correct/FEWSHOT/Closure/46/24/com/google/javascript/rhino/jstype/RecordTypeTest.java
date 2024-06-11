package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class RecordTypeTest {

  private JSTypeRegistry registry;

  @org.junit.Before
  public void setUp() {
    registry = new JSTypeRegistry();
  }

  @Test
  public void testRecordTypeCreation() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", registry.stringType()));
    properties.put("age", new RecordProperty("age", registry.numberType()));

    RecordType recordType = new RecordType(registry, properties);

    assertNotNull(recordType);
    assertEquals("Object", recordType.getClassName());
    assertEquals("Record[name: String, age: Number]", recordType.toString());
  }

  @Test
  public void testRecordTypePropertyAccess() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", registry.stringType()));
    properties.put("age", new RecordProperty("age", registry.numberType()));

    RecordType recordType = new RecordType(registry, properties);

    assertFalse(recordType.isFrozen());

    RecordProperty nameProperty = recordType.getProperty("name");
    assertEquals(registry.stringType(), nameProperty.getType());

    RecordProperty ageProperty = recordType.getProperty("age");
    assertEquals(registry.numberType(), ageProperty.getType());

    assertFalse(recordType.hasProperty("unknown"));

    recordType.freeze();
    assertTrue(recordType.isFrozen());

    assertFalse(recordType.defineProperty("newProperty", registry.stringType(), false, null));
  }

  @Test
  public void testRecordTypeEquivalence() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("name", new RecordProperty("name", registry.stringType()));
    properties1.put("age", new RecordProperty("age", registry.numberType()));

    Map<String, RecordProperty> properties2 = Maps.newHashMap(properties1);

    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);

    assertTrue(recordType1.isEquivalentTo(recordType2));

    properties2.put("age", new RecordProperty("height", registry.numberType()));

    assertFalse(recordType1.isEquivalentTo(recordType2));
  }

  @Test
  public void testRecordTypeLeastSupertype() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("name", new RecordProperty("name", registry.stringType()));
    properties1.put("age", new RecordProperty("age", registry.numberType()));

    Map<String, RecordProperty> properties2 = Maps.newHashMap(properties1);
    properties2.put("age", new RecordProperty("height", registry.numberType()));

    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);

    RecordType leastSupertype = recordType1.getLeastSupertype(recordType2);

    assertNotNull(leastSupertype);
    assertEquals("Record[name: String, height: Number]", leastSupertype.toString());
  }

  @Test
  public void testRecordTypeGreatestSubtype() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("name", new RecordProperty("name", registry.stringType()));
    properties1.put("age", new RecordProperty("age", registry.numberType()));

    Map<String, RecordProperty> properties2 = Maps.newHashMap(properties1);
    properties2.put("age", new RecordProperty("height", registry.numberType()));

    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);

    JSType greatestSubtype = recordType1.getGreatestSubtype(recordType2);

    assertNotNull(greatestSubtype);
    assertEquals("Record[name: String]", greatestSubtype.toString());
  }

  @Test
  public void testRecordTypeIsSubtype() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("name", new RecordProperty("name", registry.stringType()));
    properties1.put("age", new RecordProperty("age", registry.numberType()));

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("name", new RecordProperty("name", registry.stringType()));
    properties2.put("age", new RecordProperty("height", registry.numberType()));
    properties2.put("height", new RecordProperty("height", registry.numberType()));

    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);

    assertTrue(recordType1.isSubtype(recordType2));
  }

  @Test
  public void testRecordTypeResolveInternal() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", registry.stringType()));
    properties.put("age", new RecordProperty("age", registry.numberType()));

    RecordType recordType = new RecordType(registry, properties);

    SortedMap<String, JSType> resolvedProperties = new TreeMap<>();
    resolvedProperties.put("name", registry.stringType());
    resolvedProperties.put("age", registry.numberType());

    RecordType resolvedRecordType = new RecordType(registry, resolvedProperties);

    RecordType resolved = recordType.resolveInternal(new ErrorReporter() {}, null);

    assertTrue(resolved.isEquivalentTo(resolvedRecordType));
  }

  @Test
  public void testRecordTypeUnionSubtype() {
    JSType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", registry.stringType()));

    RecordType recordType = new RecordType(registry, properties);

    UnionTypeBuilder unionBuilder = new UnionTypeBuilder(registry);

    ObjectType stringTypeObject = registry.getNativeObjectType(JSTypeNative.STRING_TYPE_OBJECT);
    unionBuilder.addAlternate(stringTypeObject);

    ObjectType numberTypeObject = registry.getNativeObjectType(JSTypeNative.NUMBER_TYPE_OBJECT);
    unionBuilder.addAlternate(numberTypeObject);

    JSType unionType = unionBuilder.build();

    JSType greatestSubtype = recordType.getGreatestSubtype(unionType);

    assertTrue(unionType.isSubtype(greatestSubtype));
  }

  @Test
  public void testRecordTypeUnionSupertype() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("name", new RecordProperty("name", registry.stringType()));

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("name", new RecordProperty("name", registry.stringType()));
    properties2.put("age", new RecordProperty("age", registry.numberType()));

    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);

    UnionTypeBuilder unionBuilder = new UnionTypeBuilder(registry);

    unionBuilder.addAlternate(recordType1);

    JSType unionType = unionBuilder.build();

    JSType leastSupertype = recordType2.getLeastSupertype(unionType);

    assertEquals(recordType2, leastSupertype);
  }
}
package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Maps;
import org.junit.Test;

import java.util.Map;
import java.util.SortedMap;

public class RecordTypeTest {

  @Test
  public void testBasicConstruction() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("foo", new RecordProperty(registry.getIntType(), null));
    properties.put("bar", new RecordProperty(registry.getStringType(), null));

    RecordType recordType = new RecordType(registry, properties, true);
    assertTrue(recordType.isDeclared());

    SortedMap<String, JSType> propertiesMap = recordType.properties;
    assertEquals(2, propertiesMap.size());
    assertEquals(registry.getIntType(), propertiesMap.get("foo"));
    assertEquals(registry.getStringType(), propertiesMap.get("bar"));
  }

  @Test
  public void testBasicConstructionWithNullRecordProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("foo", null);

    try {
      new RecordType(registry, properties, true);
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      assertEquals("RecordProperty associated with a property should not be null!", e.getMessage());
    }
  }

  @Test
  public void testIsSynthetic() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("foo", new RecordProperty(registry.getIntType(), null));

    RecordType declaredRecordType = new RecordType(registry, properties, true);
    RecordType synthesizedRecordType = new RecordType(registry, properties, false);

    assertTrue(declaredRecordType.isDeclared());
    assertFalse(synthesizedRecordType.isDeclared());
  }

  @Test
  public void testCheckRecordEquivalenceHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> propA = Maps.newHashMap();
    propA.put("foo", new RecordProperty(registry.getIntType(), null));
    Map<String, RecordProperty> propB = Maps.newHashMap();
    propB.put("foo", new RecordProperty(registry.getIntType(), null));
    propB.put("bar", new RecordProperty(registry.getStringType(), null));

    RecordType recordTypeA = new RecordType(registry, propA, true);
    RecordType recordTypeB = new RecordType(registry, propB, true);

    assertTrue(recordTypeA.checkRecordEquivalenceHelper(recordTypeB, false));
  }

  @Test
  public void testCheckRecordEquivalenceHelperWithDifferentProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> propA = Maps.newHashMap();
    propA.put("foo", new RecordProperty(registry.getIntType(), null));
    Map<String, RecordProperty> propB = Maps.newHashMap();
    propB.put("bar", new RecordProperty(registry.getStringType(), null));

    RecordType recordTypeA = new RecordType(registry, propA, true);
    RecordType recordTypeB = new RecordType(registry, propB, true);

    assertFalse(recordTypeA.checkRecordEquivalenceHelper(recordTypeB, false));
  }

  @Test
  public void testGetImplicitPrototype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType = new RecordType(registry, Maps.newHashMap(), true);

    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), recordType.getImplicitPrototype());
  }

  @Test
  public void testDefineProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType = new RecordType(registry, Maps.newHashMap(), true);

    recordType.defineProperty("foo", registry.getIntType(), false, null);
    assertTrue(recordType.hasProperty("foo"));
  }

  @Test
  public void testDefinePropertyFrozen() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType = new RecordType(registry, Maps.newHashMap(), true);
    recordType.isFrozen = true;

    assertFalse(recordType.defineProperty("foo", registry.getIntType(), false, null));
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> propA = Maps.newHashMap();
    propA.put("foo", new RecordProperty(registry.getIntType(), null));

    Map<String, RecordProperty> propB = Maps.newHashMap();
    propB.put("foo", new RecordProperty(registry.getIntType(), null));
    propB.put("bar", new RecordProperty(registry.getStringType(), null));

    RecordType recordTypeA = new RecordType(registry, propA, true);
    RecordType recordTypeB = new RecordType(registry, propB, true);

    JSType greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeB);

    assertEquals(recordTypeB, greatestSubtype);
  }

  @Test
  public void testGetGreatestSubtypeHelperWithDifferentTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> propA = Maps.newHashMap();
    propA.put("foo", new RecordProperty(registry.getIntType(), null));

    Map<String, RecordProperty> propB = Maps.newHashMap();
    propB.put("foo", new RecordProperty(registry.getStringType(), null));

    RecordType recordTypeA = new RecordType(registry, propA, true);
    RecordType recordTypeB = new RecordType(registry, propB, true);

    JSType greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeB);

    assertEquals(registry.getNativeObjectType(JSTypeNative.NO_TYPE), greatestSubtype);
  }

  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> propA = Maps.newHashMap();
    propA.put("foo", new RecordProperty(registry.getIntType(), null));

    Map<String, RecordProperty> propB = Maps.newHashMap();
    propB.put("foo", new RecordProperty(registry.getIntType(), null));
    propB.put("bar", new RecordProperty(registry.getStringType(), null));

    RecordType recordTypeA = new RecordType(registry, propA, true);
    RecordType recordTypeB = new RecordType(registry, propB, true);

    assertTrue(recordTypeA.isSubtype(recordTypeB));
  }

  @Test
  public void testIsSubtypeWithDifferentPropertyTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> propA = Maps.newHashMap();
    propA.put("foo", new RecordProperty(registry.getIntType(), null));

    Map<String, RecordProperty> propB = Maps.newHashMap();
    propB.put("foo", new RecordProperty(registry.getStringType(), null));

    RecordType recordTypeA = new RecordType(registry, propA, true);
    RecordType recordTypeB = new RecordType(registry, propB, true);

    assertFalse(recordTypeA.isSubtype(recordTypeB));
  }
}
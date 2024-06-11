package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class RecordTypeTest {

  @Test
  public void testIsSubtype_sameRecordType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("a", new RecordProperty("a", registry.getNativeStringType()));
    RecordType recordTypeA = new RecordType(registry, recordProperties);

    assertTrue(recordTypeA.isEquivalentTo(recordTypeA));
  }

  @Test
  public void testIsSubtype_subclassIsRecordType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("a", new RecordProperty("a", registry.getNativeStringType()));
    RecordType recordTypeA = new RecordType(registry, recordProperties);

    Map<String, RecordProperty> subProperties = Maps.newHashMap();
    subProperties.putAll(recordProperties);
    subProperties.put("b", new RecordProperty("b", registry.getNativeNumberType()));
    RecordType recordTypeB = new RecordType(registry, subProperties);

    assertTrue(recordTypeA.isSubtype(recordTypeB));
  }

  @Test
  public void testIsSubtype_recordTypeIsSubclass() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordPropertiesA = Maps.newHashMap();
    recordPropertiesA.put("a", new RecordProperty("a", registry.getNativeStringType()));
    RecordType recordTypeA = new RecordType(registry, recordPropertiesA);

    Map<String, RecordProperty> recordPropertiesB = Maps.newHashMap();
    recordPropertiesB.put("a", new RecordProperty("a", registry.getNativeStringType()));
    recordPropertiesB.put("b", new RecordProperty("b", registry.getNativeNumberType()));
    RecordType recordTypeB = new RecordType(registry, recordPropertiesB);

    assertTrue(recordTypeB.isSubtype(recordTypeA));
  }

  @Test
  public void testIsSubtype_differentRecordTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordPropertiesA = Maps.newHashMap();
    recordPropertiesA.put("a", new RecordProperty("a", registry.getNativeStringType()));
    RecordType recordTypeA = new RecordType(registry, recordPropertiesA);

    Map<String, RecordProperty> recordPropertiesB = Maps.newHashMap();
    recordPropertiesB.put("b", new RecordProperty("b", registry.getNativeNumberType()));
    RecordType recordTypeB = new RecordType(registry, recordPropertiesB);

    assertFalse(recordTypeA.isSubtype(recordTypeB));
    assertFalse(recordTypeB.isSubtype(recordTypeA));
  }

  @Test
  public void testIsSubtype_nonRecordType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("a", new RecordProperty("a", registry.getNativeStringType()));
    RecordType recordType = new RecordType(registry, recordProperties);

    JSType nonRecordType = registry.getNativeObjectType(JSTypeNative.NUMBER_TYPE);

    assertFalse(recordType.isSubtype(nonRecordType));
  }

  @Test
  public void testIsSubtype_null() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("a", new RecordProperty("a", registry.getNativeStringType()));
    RecordType recordType = new RecordType(registry, recordProperties);

    assertFalse(recordType.isSubtype(null));
  }
}
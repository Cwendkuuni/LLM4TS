package com.google.javascript.rhino.jstype;

import static org.junit.Assert.*;

import org.junit.Test;

public class RecordTypeTest {

  @Test
  public void testRecordTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", IntType.INSTANCE));
    properties.put("b", new RecordProperty("b", BoolType.INSTANCE));

    RecordType recordType = new RecordType(registry, properties, true);
    assertEquals("Record<a: Int, b: Boolean>", recordType.toString());
  }

  @Test
  public void testRecordTypeEquivalence() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty("a", IntType.INSTANCE));
    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("a", new RecordProperty("a", IntType.INSTANCE));
    propertiesB.put("b", new RecordProperty("b", BoolType.INSTANCE));

    RecordType recordTypeA = new RecordType(registry, propertiesA, true);
    RecordType recordTypeB = new RecordType(registry, propertiesB, true);

    assertTrue(recordTypeA.checkRecordEquivalenceHelper(recordTypeB, false));
  }

  @Test
  public void testRecordTypeUnequivalenceDueToPropertyCount() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty("a", IntType.INSTANCE));
    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("a", new RecordProperty("a", IntType.INSTANCE));
    propertiesB.put("b", new RecordProperty("b", BoolType.INSTANCE));
    propertiesB.put("c", new RecordProperty("c", StringType.INSTANCE));

    RecordType recordTypeA = new RecordType(registry, propertiesA, true);
    RecordType recordTypeB = new RecordType(registry, propertiesB, true);

    assertFalse(recordTypeA.checkRecordEquivalenceHelper(recordTypeB, false));
  }

  @Test
  public void testRecordTypeUnequivalenceDueToPropertyType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty("a", IntType.INSTANCE));
    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("a", new RecordProperty("a", StringType.INSTANCE));

    RecordType recordTypeA = new RecordType(registry, propertiesA, true);
    RecordType recordTypeB = new RecordType(registry, propertiesB, true);

    assertFalse(recordTypeA.checkRecordEquivalenceHelper(recordTypeB, false));
  }

  @Test
  public void testRecordTypeSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty("a", IntType.INSTANCE));
    propertiesA.put("b", new RecordProperty("b", StringType.INSTANCE));
    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("a", new RecordProperty("a", IntType.INSTANCE));
    propertiesB.put("b", new RecordProperty("b", StringType.INSTANCE));
    propertiesB.put("c", new RecordProperty("c", BoolType.INSTANCE));

    RecordType recordTypeA = new RecordType(registry, propertiesA, true);
    RecordType recordTypeB = new RecordType(registry, propertiesB, true);

    assertTrue(recordTypeA.isSubtype(recordTypeB));
  }

  @Test
  public void testRecordTypeSubtypeFailureDueToPropertyCount() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty("a", IntType.INSTANCE));
    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("a", new RecordProperty("a", IntType.INSTANCE));
    propertiesB.put("b", new RecordProperty("b", StringType.INSTANCE));

    RecordType recordTypeA = new RecordType(registry, propertiesA, true);
    RecordType recordTypeB = new RecordType(registry, propertiesB, true);

    assertFalse(recordTypeB.isSubtype(recordTypeA));
  }

  @Test
  public void testRecordTypeSubtypeFailureDueToPropertyType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty("a", IntType.INSTANCE));
    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("a", new RecordProperty("a", StringType.INSTANCE));

    RecordType recordTypeA = new RecordType(registry, propertiesA, true);
    RecordType recordTypeB = new RecordType(registry, propertiesB, true);

    assertFalse(recordTypeA.isSubtype(recordTypeB));
  }

  @Test
  public void testRecordTypeSubtypeFailureDueToInvariantPropertyType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty("a", IntType.INSTANCE));
    propertiesA.put("b", new RecordProperty("b", new IntType() {
      @Override public boolean isInvariant(JSType other) {
        return false;
      }
    }));
    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("a", new RecordProperty("a", IntType.INSTANCE));
    propertiesB.put("b", new RecordProperty("b", IntType.INSTANCE));

    RecordType recordTypeA = new RecordType(registry, propertiesA, true);
    RecordType recordTypeB = new RecordType(registry, propertiesB, true);

    assertFalse(recordTypeA.isSubtype(recordTypeB));
  }

  @Test
  public void testRecordTypeGetGreatestSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty("a", IntType.INSTANCE));
    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("a", new RecordProperty("a", IntType.INSTANCE));
    propertiesB.put("b", new RecordProperty("b", StringType.INSTANCE));

    RecordType recordTypeA = new RecordType(registry, propertiesA, true);
    RecordType recordTypeB = new RecordType(registry, propertiesB, true);

    JSType greatestSubtypeAB = recordTypeA.getGreatestSubtype(recordTypeB);

    assertEquals(recordTypeB, greatestSubtypeAB);
  }

  @Test
  public void testRecordTypeGetGreatestSubtypeFailure() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty("a", IntType.INSTANCE));
    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("a", new RecordProperty("a", StringType.INSTANCE));
    propertiesB.put("b", new RecordProperty("b", StringType.INSTANCE));

    RecordType recordTypeA = new RecordType(registry, propertiesA, true);
    RecordType recordTypeB = new RecordType(registry, propertiesB, true);

    JSType greatestSubtypeAB = recordTypeA.getGreatestSubtype(recordTypeB);

    assertNotNull(greatestSubtypeAB);
    assertFalse(recordTypeA.isEquivalentTo(greatestSubtypeAB));
    assertFalse(recordTypeB.isEquivalentTo(greatestSubtypeAB));
  }
}
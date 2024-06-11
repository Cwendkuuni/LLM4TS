package com.google.javascript.rhino.jstype;

import static org.junit.Assert.*;

import org.junit.Test;

public class RecordTypeTest {

  @Test
  public void testRecordTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    RecordProperty recordPropertyA = new RecordProperty("A", registry.getNumberType());
    RecordProperty recordPropertyB = new RecordProperty("B", registry.getStringType());
    recordProperties.put("A", recordPropertyA);
    recordProperties.put("B", recordPropertyB);
    RecordType recordType = new RecordType(registry, recordProperties, true);

    assertEquals(recordType.getClass(), RecordType.class);
    assertEquals(recordType.isSynthetic(), false);
    assertEquals(recordType.getPrettyPrint(), true);
    assertFalse(recordType.isFrozen());
    assertEquals(recordType.getProperties().size(), 2);

    RecordProperty propertyA = recordType.getProperty("A");
    RecordProperty propertyB = recordType.getProperty("B");

    assertEquals(propertyA.getType(), registry.getNumberType());
    assertEquals(propertyB.getType(), registry.getStringType());
  }

  @Test
  public void testRecordTypeEquivalence() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordPropertiesA = Maps.newHashMap();
    Map<String, RecordProperty> recordPropertiesB = Maps.newHashMap();

    RecordProperty recordPropertyA1 = new RecordProperty("A1", registry.getIntType());
    RecordProperty recordPropertyA2 = new RecordProperty("A2", registry.getStringType());
    recordPropertiesA.put("A1", recordPropertyA1);
    recordPropertiesA.put("A2", recordPropertyA2);

    RecordProperty recordPropertyB1 = new RecordProperty("B1", registry.getIntType());
    RecordProperty recordPropertyB2 = new RecordProperty("B2", registry.getStringType());
    recordPropertiesB.put("B1", recordPropertyB1);
    recordPropertiesB.put("B2", recordPropertyB2);

    RecordType recordTypeA = new RecordType(registry, recordPropertiesA, true);
    RecordType recordTypeB = new RecordType(registry, recordPropertiesB, true);

    assertTrue(recordTypeA.checkRecordEquivalenceHelper(recordTypeB, false));
    assertTrue(recordTypeB.checkRecordEquivalenceHelper(recordTypeA, false));

    recordPropertiesB.put("A1", null);
    assertFalse(recordTypeA.checkRecordEquivalenceHelper(recordTypeB, false));
    assertFalse(recordTypeB.checkRecordEquivalenceHelper(recordTypeA, false));

    recordPropertiesB.put("C", new RecordProperty("C", registry.getBooleanType()));
    assertFalse(recordTypeA.checkRecordEquivalenceHelper(recordTypeB, false));
    assertFalse(recordTypeB.checkRecordEquivalenceHelper(recordTypeA, false));
  }

  @Test
  public void testRecordTypeSubtyping() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordPropertiesA = Maps.newHashMap();
    Map<String, RecordProperty> recordPropertiesB = Maps.newHashMap();

    RecordProperty recordPropertyA1 = new RecordProperty("A1", registry.getIntType());
    RecordProperty recordPropertyA2 = new RecordProperty("A2", registry.getStringType());
    recordPropertiesA.put("A1", recordPropertyA1);
    recordPropertiesA.put("A2", recordPropertyA2);

    RecordProperty recordPropertyB1 = new RecordProperty("B1", registry.getIntType());
    RecordProperty recordPropertyB2 = new RecordProperty("B2", registry.getStringType());
    RecordProperty recordPropertyB3 = new RecordProperty("B3", registry.getBooleanType());
    recordPropertiesB.put("B1", recordPropertyB1);
    recordPropertiesB.put("B2", recordPropertyB2);
    recordPropertiesB.put("B3", recordPropertyB3);

    RecordType recordTypeA = new RecordType(registry, recordPropertiesA, true);
    RecordType recordTypeB = new RecordType(registry, recordPropertiesB, true);

    RecordType recordTypeC = new RecordType(registry, recordPropertiesA, false);
    RecordType recordTypeD = new RecordType(registry, recordPropertiesB, false);

    assertTrue(recordTypeA.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
    assertTrue(recordTypeB.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));

    assertTrue(recordTypeA.isSubtype(recordTypeA));
    assertTrue(recordTypeB.isSubtype(recordTypeB));

    assertTrue(recordTypeA.isSubtype(recordTypeC));
    assertTrue(recordTypeC.isSubtype(recordTypeA));

    assertTrue(recordTypeB.isSubtype(recordTypeD));
    assertTrue(recordTypeD.isSubtype(recordTypeB));

    assertFalse(recordTypeA.isSubtype(recordTypeB));
    assertFalse(recordTypeB.isSubtype(recordTypeA));
  }

  @Test
  public void testRecordTypeGetGreatestSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordPropertiesA = Maps.newHashMap();
    Map<String, RecordProperty> recordPropertiesB = Maps.newHashMap();

    RecordProperty recordPropertyA1 = new RecordProperty("A1", registry.getIntType());
    RecordProperty recordPropertyA2 = new RecordProperty("A2", registry.getStringType());
    recordPropertiesA.put("A1", recordPropertyA1);
    recordPropertiesA.put("A2", recordPropertyA2);

    RecordProperty recordPropertyB1 = new RecordProperty("B1", registry.getIntType());
    RecordProperty recordPropertyB2 = new RecordProperty("B2", registry.getStringType());
    RecordProperty recordPropertyB3 = new RecordProperty("B3", registry.getBooleanType());
    recordPropertiesB.put("B1", recordPropertyB1);
    recordPropertiesB.put("B2", recordPropertyB2);
    recordPropertiesB.put("B3", recordPropertyB3);

    RecordType recordTypeA = new RecordType(registry, recordPropertiesA, true);
    RecordType recordTypeB = new RecordType(registry, recordPropertiesB, true);

    UnionType recordTypeUnionA = new UnionType(registry);
    recordTypeUnionA.addAlternate(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
    recordTypeUnionA.addAlternate(recordTypeA);

    UnionType recordTypeUnionB = new UnionType(registry);
    recordTypeUnionB.addAlternate(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
    recordTypeUnionB.addAlternate(recordTypeB);

    UnionType recordTypeUnionC = new UnionType(registry);
    recordTypeUnionC.addAlternate(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
    recordTypeUnionC.addAlternate(recordTypeB);
    recordTypeUnionC.addAlternate(registry.getBooleanType());

    JSType greatestSubtypeA = recordTypeA.getGreatestSubtypeHelper(recordTypeB);
    JSType greatestSubtypeB = recordTypeB.getGreatestSubtypeHelper(recordTypeA);

    assertEquals(greatestSubtypeA, recordTypeUnionA);
    assertEquals(greatestSubtypeB, recordTypeUnionB);

    JSType greatestSubtypeAB = recordTypeA.getGreatestSubtypeHelper(recordTypeB);
    JSType greatestSubtypeBA = recordTypeB.getGreatestSubtypeHelper(recordTypeA);

    assertEquals(greatestSubtypeAB, greatestSubtypeB);

    JSType greatestSubtypeABC = recordTypeA.getGreatestSubtypeHelper(recordTypeC);
    JSType greatestSubtypeBCA = recordTypeC.getGreatestSubtypeHelper(recordTypeA);

    assertEquals(greatestSubtypeABC, greatestSubtypeBCA);

    assertEquals(greatestSubtypeABC, recordTypeUnionC);
  }

  @Test(expected = IllegalStateException.class)
  public void testRecordTypeNullProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    RecordType recordType = new RecordType(registry, recordProperties, true);
  }
}
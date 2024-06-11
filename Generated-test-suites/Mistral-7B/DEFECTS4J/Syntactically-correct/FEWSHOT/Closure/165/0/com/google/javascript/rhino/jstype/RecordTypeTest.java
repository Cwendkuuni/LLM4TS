package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import org.junit.Test;

public class RecordTypeTest {

  private static JSTypeRegistry registry;

  @SuppressWarnings("unchecked")
  @BeforeClass
  public static void setup() {
    registry = new JSTypeRegistry();
  }

  @Test
  public void testCreateRecordType() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", JSType.getIntType()));
    properties.put("b", new RecordProperty("b", JSType.getStringType()));

    RecordType recordType = new RecordType(registry, properties);
    assertNotNull(recordType);
    assertTrue(recordType.isRecordType());
  }

  @Test
  public void testGetPropertyType() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", JSType.getIntType()));
    properties.put("b", new RecordProperty("b", JSType.getStringType()));

    RecordType recordType = new RecordType(registry, properties);
    JSType typeA = recordType.getPropertyType("a");
    JSType typeB = recordType.getPropertyType("b");

    assertTrue(typeA.isIntType());
    assertTrue(typeB.isStringType());
  }

  @Test
  public void testIsEquivalentTo() {
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty("a", JSType.getIntType()));
    propertiesA.put("b", new RecordProperty("b", JSType.getStringType()));

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("b", new RecordProperty("b", JSType.getStringType()));
    propertiesB.put("a", new RecordProperty("a", JSType.getIntType()));

    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    assertTrue(recordTypeA.isEquivalentTo(recordTypeB));
  }

  @Test
  public void testIsNotEquivalentToDifferentProperties() {
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty("a", JSType.getIntType()));
    propertiesA.put("b", new RecordProperty("b", JSType.getStringType()));

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("c", new RecordProperty("c", JSType.getIntType()));
    propertiesB.put("d", new RecordProperty("d", JSType.getStringType()));

    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    assertFalse(recordTypeA.isEquivalentTo(recordTypeB));
  }

  @Test
  public void testIsFrozen() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", JSType.getIntType()));

    RecordType recordType = new RecordType(registry, properties);
    assertFalse(recordType.isFrozen());

    recordType.freeze();
    assertTrue(recordType.isFrozen());
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty("a", JSType.getIntType()));
    propertiesA.put("b", new RecordProperty("b", JSType.getStringType()));

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("a", new RecordProperty("a", JSType.getIntType()));
    propertiesB.put("c", new RecordProperty("c", JSType.getStringType()));

    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    JSType greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeB);
    assertTrue(recordTypeA.isEquivalentTo(greatestSubtype));
  }

  @Test
  public void testGetGreatestSubtypeDifferentProperties() {
    Map<String, RecordProperty> propertiesA = Maps
        .newHashMap();
    propertiesA.put("a", new RecordProperty("a", JSType.getIntType()));
    propertiesA.put("b", new RecordProperty("b", JSType.getStringType()));

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("c", new RecordProperty("c", JSType.getIntType()));
    propertiesB.put("d", new RecordProperty("d", JSType.getStringType()));

    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    JSType greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeB);
    assertNull(greatestSubtype);
  }

  @Test
  public void testIsSubtype() {
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty("a", JSType.getIntType()));
    propertiesA.put("b", new RecordProperty("b", JSType.getStringType()));

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("a", new RecordProperty("a", JSType.getIntType()));

    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    assertTrue(recordTypeA.isSubtype(recordTypeB));
  }

  @Test
  public void testIsSubtypeDifferentProperties() {
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty("a", JSType.getIntType()));
    propertiesA.put("b", new RecordProperty("b", JSType.getStringType()));

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("c", new RecordProperty("c", JSType.getIntType()));
    propertiesB.put("d", new RecordProperty("d", JSType.getStringType()));

    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    assertFalse(recordTypeA.isSubtype(recordTypeB));
  }

  @Test
  public void testIsSubtypeSelf() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", JSType.getIntType()));

    RecordType recordType = new RecordType(registry, properties);
    assertTrue(recordType.isSubtype(recordType));
  }

  @Test
  public void testIsSubtypeNativeObjectType() {
    RecordType recordType = new RecordType(registry, Maps.newHashMap());
    assertTrue(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE).isSubtype(recordType));
  }

  @Test
  public void testIsSubtypeEmptyRecordType() {
    RecordType recordType = new RecordType(registry, Maps.newHashMap());
    assertTrue(recordType.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
  }

  @Test
  public void testIsSubtypeUnknownType() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", JSType.getUnknownType()));

    RecordType recordType = new RecordType(registry, properties);
    assertFalse(JSType.getUnknownType().isSubtype(recordType));
  }

  @Test
  public void testIsSubtypeRecordWithSameProperties() {
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty("a", JSType.getIntType()));
    propertiesA.put("b", new RecordProperty("b", JSType.getStringType()));

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("a", new RecordProperty("a", JSType.getIntType()));
    propertiesB.put("b", new RecordProperty("b", JSType.getStringType()));

    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    assertTrue(recordTypeA.isSubtype(recordTypeB));
  }

  @Test
  public void testIsSubtypeRecordWithDifferentProperties() {
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty("a", JSType.getIntType()));
    propertiesA.put("b", new RecordProperty("b", JSType.getStringType()));

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("c", new RecordProperty("c", JSType.getIntType()));
    propertiesB.put("d", new RecordProperty("d", JSType.getStringType()));

    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    assertFalse(recordTypeA.isSubtype(recordTypeB));
  }

  @Test
  public void testIsSubtypeRecordWithSubtypeProperties() {
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty("a", JSType.getIntType()));
    propertiesA.put("b", new RecordProperty("b", JSType.getStringType()));

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("a", new RecordProperty("a", JSType.getIntType()));
    propertiesB.put("c", new RecordProperty("c", JSType.getStringType()));

    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    assertFalse(recordTypeA.isSubtype(recordTypeB));

    JSType subtypePropertyC = new ArrayType(JSType.getStringType(), 0);
    propertiesB.put("c", new RecordProperty("c", subtypePropertyC));

    RecordType recordTypeC = new RecordType(registry, propertiesB);
    assertTrue(recordTypeA.isSubtype(recordTypeC));
  }

  @Test
  public void testIsSubtypeRecordWithSupertypeProperties() {
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty("a", JSType.getIntType()));
    propertiesA.put("b", new RecordProperty("b", JSType.getStringType()));

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("a", new RecordProperty("a", JSType.getIntType()));
    propertiesB.put("b", new RecordProperty("b", JSType.getIntType()));

    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    assertTrue(recordTypeA.isSubtype(recordTypeB));
  }
}
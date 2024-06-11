package com.google.javascript.rhino.jstype;

import static org.junit.Assert.*;

import org.junit.Test;

public class RecordTypeTest {

  @Test
  public void testCreateRecordType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getIntegerType()));
    properties.put("b", new RecordProperty("b", registry.getStringType()));

    RecordType recordType = new RecordType(registry, properties);

    assertEquals("{a: int, b: string}", recordType.toString());
    assertEquals("int", recordType.getPropertyType("a").getName());
    assertEquals("string", recordType.getPropertyType("b").getName());
  }

  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> propsA = Maps.newHashMap();
    propsA.put("a", new RecordProperty("a", registry.getIntegerType()));
    RecordType recordTypeA = new RecordType(registry, propsA);

    Map<String, RecordProperty> propsB = Maps.newHashMap();
    propsB.put("a", new RecordProperty("a", registry.getIntegerType()));
    propsB.put("b", new RecordProperty("b", registry.getStringType()));
    RecordType recordTypeB = new RecordType(registry, propsB);

    RecordType recordTypeC = new RecordType(registry, Maps.<String, RecordProperty>newHashMap());

    assertTrue(recordTypeA.isSubtype(recordTypeA));
    assertTrue(recordTypeA.isSubtype(recordTypeB));
    assertFalse(recordTypeA.isSubtype(recordTypeC));
    assertFalse(recordTypeB.isSubtype(recordTypeC));

    // Test with null properties
    RecordType recordTypeD = new RecordType(registry, null);
    assertFalse(recordTypeA.isSubtype(recordTypeD));
    assertFalse(recordTypeD.isSubtype(recordTypeA));
  }

  @Test
  public void testGetGreatestSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> propsA = Maps.newHashMap();
    propsA.put("a", new RecordProperty("a", registry.getIntegerType()));
    RecordType recordTypeA = new RecordType(registry, propsA);

    Map<String, RecordProperty> propsB = Maps.newHashMap();
    propsB.put("a", new RecordProperty("a", registry.getIntegerType()));
    propsB.put("b", new RecordProperty("b", registry.getStringType()));
    RecordType recordTypeB = new RecordType(registry, propsB);

    Map<String, RecordProperty> propsC = Maps.newHashMap();
    propsC.put("a", new RecordProperty("a", registry.getBooleanType()));
    RecordType recordTypeC = new RecordType(registry, propsC);

    RecordType greatestSubtype = recordTypeA.getGreatestSubtype(recordTypeB);
    assertEquals(recordTypeB, greatestSubtype);

    greatestSubtype = recordTypeB.getGreatestSubtype(recordTypeC);
    assertEquals(recordTypeB, greatestSubtype);

    greatestSubtype = recordTypeC.getGreatestSubtype(recordTypeA);
    assertEquals(recordTypeA, greatestSubtype);

    greatestSubtype = recordTypeA.getGreatestSubtype(recordTypeA);
    assertEquals(recordTypeA, greatestSubtype);
  }

  @Test
  public void testDefineProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> props = Maps.newHashMap();
    RecordType recordType = new RecordType(registry, props);

    recordType.defineProperty("a", registry.getIntegerType(), false, null);
    assertTrue(recordType.hasProperty("a"));

    recordType.defineProperty("b", registry.getStringType(), false, null);
    assertTrue(recordType.hasProperty("b"));
  }

  @Test(expected = IllegalStateException.class)
  public void testDefinePropertyNullProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> props = Maps.newHashMap();
    RecordType recordType = new RecordType(registry, props);

    recordType.defineProperty(null, registry.getIntegerType(), false, null);
  }
}
package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.PrototypeObjectType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class RecordTypeTest {

  @Test
  public void testCreateRecordType() {
    JSTypeRegistry registry = new JSTypeRegistry();

    // Define property types used in the record type definition.
    JSType stringType = registry.getType("String");
    JSType numberType = registry.getType("Number");
    JSType booleanType = registry.getType("Boolean");

    // Define record type properties and build the record type.
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", stringType));
    properties.put("age", new RecordProperty("age", numberType));
    properties.put("isMale", new RecordProperty("isMale", booleanType));

    RecordType recordType = new RecordType(registry, properties);

    // Test properties.
    assertEquals("name", recordType.getPropertyNames().get(0));
    assertEquals("age", recordType.getPropertyNames().get(1));
    assertEquals("isMale", recordType.getPropertyNames().get(2));
    assertEquals(stringType, recordType.getPropertyType("name"));
    assertEquals(numberType, recordType.getPropertyType("age"));
    assertEquals(booleanType, recordType.getPropertyType("isMale"));

    // Test isEquivalentTo.
    JSType recordType1 = new RecordType(registry, properties);
    assertTrue(recordType.isEquivalentTo(recordType1));

    // Test getImplicitPrototype.
    JSType objType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    assertEquals(objType, recordType.getImplicitPrototype());

    // Test defineProperty.
    recordType.defineProperty("color", stringType, false, null);
    assertFalse(recordType.hasProperty("color"));
    assertNull(recordType.getPropertyType("color"));

    // Test isSubtype.
    RecordType subType = new RecordType(registry, Maps.newHashMap());
    assertTrue(recordType.isSubtype(subType));

    RecordType anotherRecordType = new RecordType(registry, properties);
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    builder.addProperty("color", stringType, null, null);
    JSType anotherRecordTypeWithColor = builder.build();
    assertFalse(recordType.isSubtype(anotherRecordTypeWithColor));

    // Test getLeastSupertype.
    JSType recordTypeWithColor = new RecordType(registry, Maps.<String, RecordProperty>of("name", new RecordProperty("name", stringType),
        "age", new RecordProperty("age", numberType),
        "isMale", new RecordProperty("isMale", booleanType),
        "color", new RecordProperty("color", stringType, null, null)));
    JSType leastSuperType = recordType.getLeastSupertype(recordTypeWithColor);
    assertEquals(recordType, leastSuperType);

    // Test getGreatestSubtypeHelper.
    JSType recordTypeWithExtraProperty = new RecordType(registry, Maps.<String, RecordProperty>of("name", new RecordProperty("name", stringType),
        "age", new RecordProperty("age", numberType),
        "isMale", new RecordProperty("isMale", booleanType),
        "extraProperty", new RecordProperty("extraProperty", stringType, null, null)));
    JSType greatestSubtype = recordType.getGreatestSubtypeHelper(recordTypeWithExtraProperty);
    assertNull(greatestSubtype);

    // Test isFrozen.
    assertFalse(recordType.isFrozen());

    recordType = new RecordType(registry, properties);
    recordType.isFrozen = true;
    try {
      recordType.defineProperty("color", stringType, false, null);
      fail("Expected an IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  private void assertEquals(String propertyName, Object expected) {
    Object actual = ((RecordType) getRecordType()).getPropertyNames().get(0);
    assertEquals(propertyName, actual);
    assertEquals(expected, getRecordType().getPropertyType(propertyName));
  }

  private static RecordType getRecordType() {
    JSTypeRegistry registry = new JSTypeRegistry();

    // Define property types used in the record type definition.
    JSType stringType = registry.getType("String");
    JSType numberType = registry.getType("Number");
    JSType booleanType = registry.getType("Boolean");

    // Define record type properties and build the record type.
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", stringType));
    properties.put("age", new RecordProperty("age", numberType));
    properties.put("isMale", new RecordProperty("isMale", booleanType));

    RecordType recordType = new RecordType(registry, properties);
    return recordType;
  }
}
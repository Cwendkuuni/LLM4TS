package com.google.javascript.rhino.jstype.test;

import static org.junit.Assert.*;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionType;
import com.google.javascript.rhino.jstype.types.ObjectType;

import java.util.Map;
import java.util.SortedMap;

/**
 * Tests for {@link RecordType}.
 */
public class RecordTypeTest {

  private static final JSTypeRegistry registry = new JSTypeRegistry();

  /**
   * Test creating a simple record type.
   */
  @org.junit.Test
  public void testSimpleRecordType() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("x", new RecordProperty("x", registry.getBooleanType()));
    properties.put("y", new RecordProperty("y", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, properties);

    assertEquals("RecordType{x=boolean, y=number}", recordType.toString());

    ObjectType prototype = recordType.getImplicitPrototype();
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), prototype);

    // Check properties
    assertEquals(2, recordType.getDeclaredPropertyNames().length);
    assertEquals("x", recordType.getDeclaredPropertyName(0));
    assertEquals("y", recordType.getDeclaredPropertyName(1));
    assertEquals(registry.getBooleanType(), recordType.getPropertyType("x"));
    assertEquals(registry.getNumberType(), recordType.getPropertyType("y"));

    // Test equals
    RecordType recordType1 = new RecordType(registry, properties);
    RecordType recordType2 = new RecordType(registry, properties);
    assertTrue(recordType1.equals(recordType2));
  }

  /**
   * Test creating a record type with inferred properties.
   */
  @org.junit.Test
  public void testRecordTypeWithInferredProperties() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("x", new RecordProperty("x", null));
    properties.put("y", new RecordProperty("y", null));

    RecordType recordType = new RecordType(registry, properties);

    assertEquals("RecordType{x=undefined, y=undefined}", recordType.toString());

    ObjectType prototype = recordType.getImplicitPrototype();
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), prototype);

    // Check properties
    assertEquals(2, recordType.getDeclaredPropertyNames().length);
    assertEquals("x", recordType.getDeclaredPropertyName(0));
    assertEquals("y", recordType.getDeclaredPropertyName(1));
    assertNull(recordType.getPropertyType("x"));
    assertNull(recordType.getPropertyType("y"));

    // Test equals
    RecordType recordType1 = new RecordType(registry, properties);
    RecordType recordType2 = new RecordType(registry, properties);
    assertTrue(recordType1.equals(recordType2));
  }

  /**
   * Test assigning an object to a record type.
   */
  @org.junit.Test
  public void testAssignObjectToRecordType() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("x", new RecordProperty("x", registry.getBooleanType()));
    properties.put("y", new RecordProperty("y", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, properties);

    Object obj = new Object() {
      boolean x = true;
      int y = 42;
    };

    // Assign
    Object assignedObj = recordType.valueOf(obj);

    // Check
    RecordType recordTypeAssigned = (RecordType) assignedObj;
    assertEquals("RecordType{x=boolean, y=number}", recordTypeAssigned.toString());

    // Test equals
    RecordType recordType1 = new RecordType(registry, properties);
    RecordType recordType2 = recordTypeAssigned;
    assertTrue(recordType1.equals(recordType2));
  }

  /**
   * Test subtyping of record types.
   */
  @org.junit.Test
  public void testSubtypingOfRecordTypes() {
    // Create record types
    Map<String, RecordProperty> prop1 = Maps.newHashMap();
    prop1.put("x", new RecordProperty("x", registry.getBooleanType()));
    RecordType recordType1 = new RecordType(registry, prop1);

    Map<String, RecordProperty> prop2 = Maps.newHashMap();
    prop2.put("x", new RecordProperty("x", registry.getBooleanType()));
    prop2.put("y", new RecordProperty("y", registry.getNumberType()));
    RecordType recordType2 = new RecordType(registry, prop2);

    // Check subtyping
    assertTrue(recordType1.isSubtype(recordType2));
  }

  /**
   * Test greatest subtype of record types.
   */
  @org.junit.Test
  public void testGreatestSubtypeOfRecordTypes() {
    // Create record types
    Map<String, RecordProperty> recordType1Props = Maps.newHashMap();
    recordType1Props.put("x", new RecordProperty("x", registry.getBooleanType()));
    RecordType recordType1 = new RecordType(registry, recordType1Props);

    Map<String, RecordProperty> recordType2Props = Maps.newHashMap();
    recordType2Props.put("x", new RecordProperty("x", registry.getNumberType()));
    recordType2Props.put("y", new RecordProperty("y", registry.getNumberType()));
    RecordType recordType2 = new RecordType(registry, recordType2Props);

    Map<String, RecordProperty> recordType3Props = Maps.newHashMap();
    recordType3Props.put("x", new RecordProperty("x", registry.getNumberType()));
    recordType3Props.put("y", new RecordProperty("y", registry.getBooleanType()));
    RecordType recordType3 = new RecordType(registry, recordType3Props);

    Map<String, RecordProperty> recordType4Props = Maps.newHashMap();
    recordType4Props.put("z", new RecordProperty("z", registry.getNumberType()));
    recordType4Props.put("x", new RecordProperty("x", registry.getNumberType()));
    recordType4Props.put("y", new RecordProperty("y", registry.getBooleanType()));
    RecordType recordType4 = new RecordType(registry, recordType4Props);

    // Check greatest subtypes
    RecordType greatestSubtype = recordType1.getGreatestSubtype(recordType2);
    assertEquals("RecordType{x=number}", greatestSubtype.toString());

    greatestSubtype = greatestSubtype.getGreatestSubtype(recordType3);
    assertEquals("RecordType{x=number, y=boolean}", greatestSubtype.toString());

    greatestSubtype = greatestSubtype.getGreatestSubtype(recordType4);
    assertEquals("RecordType{x=number, y=boolean, z=number}", greatestSubtype.toString());
  }

  /**
   * Test assigning a record to a record type.
   */
  @org.junit.Test
  public void testAssignRecordToRecordType() {
    Map<String, RecordProperty> prop1 = Maps.newHashMap();
    prop1.put("x", new RecordProperty("x", registry.getBooleanType()));
    RecordType recordType1 = new RecordType(registry, prop1);

    Map<String, RecordProperty> prop2 = Maps.newHashMap();
    prop2.put("x", new RecordProperty("x", registry.getBooleanType()));
    prop2.put("y", new RecordProperty("y", registry.getNumberType()));
    RecordType recordType2 = new RecordType(registry, prop2);

    Map<String, Object> record = Maps.newHashMap();
    record.put("x", true);
    record.put("y", 42.0);

    // Assign
    Object assignedRecord = recordType2.valueOf(record);

    // Check
    RecordType recordTypeAssigned = (RecordType) assignedRecord;

    assertEquals("RecordType{x=boolean, y=number}", recordTypeAssigned.toString());

    // Test equals
    RecordType recordType1Assigned = new RecordType(registry, prop1);
    RecordType recordType2Assigned = recordTypeAssigned;
    assertTrue(recordType1Assigned.equals(recordType2Assigned));
  }

  /**
   * Test setting a property on a record.
   */
  @org.junit.Test
  public void testSetPropertyOnRecord() {
    Map<String, RecordProperty> prop1 = Maps.newHashMap();
    prop1.put("x", new RecordProperty("x", registry.getBooleanType()));
    RecordType recordType1 = new RecordType(registry, prop1);

    Map<String, RecordProperty> prop2 = Maps.newHashMap();
    prop2.put("x", new RecordProperty("x", registry.getBooleanType()));
    prop2.put("y", new RecordProperty("y", registry.getNumberType()));
    RecordType recordType2 = new RecordType(registry, prop2);

    Map<String, Object> record = Maps.newHashMap();
    record.put("x", true);

    // Set property
    recordType2.setProperty(record, "y", registry.getNumberType(), new Node());
    record.put("y", 42.0);

    // Check
    RecordType recordTypeAssigned = new RecordType(registry, prop2);
    assertEquals("RecordType{x=boolean, y=number}", recordTypeAssigned.toString());
    assertEquals(registry.getNumberType(), recordTypeAssigned.getPropertyType("y"));
  }

  /**
   * Test frozen record types.
   */
  @org.junit.Test
  public void testFrozenRecordTypes() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("x", new RecordProperty("x", registry.getBooleanType()));
    properties.put("y", new RecordProperty("y", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, properties);

    // Freeze
    recordType.freeze();

    // Should not allow setting a property
    try {
      recordType.setProperty(recordType.createInstance(), "z", registry.getNumberType(), new Node());
      fail();
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  /**
   * Test record types with inferred properties and frozen record types.
   */
  @org.junit.Test
  public void testFrozenRecordTypesWithInferredProperties() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("x", new RecordProperty("x", null));
    properties.put("y", new RecordProperty("y", null));

    RecordType recordType = new RecordType(registry, properties);

    // Freeze
    recordType.freeze();

    // Should not allow setting a property
    try {
      recordType.setProperty(recordType.createInstance(), "z", registry.getNumberType(), new Node());
      fail();
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  /**
   * Test record types with a union property type.
   */
  @org.junit.Test
  public void testRecordTypeWithUnionPropertyType() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("x", new RecordProperty("x", registry.getNumberType()));
    properties.put("y", new RecordProperty("y", registry.getUnionType(
        registry.getIntegerType(), registry.getStringType())));

    RecordType recordType = new RecordType(registry, properties);

    Object record = recordType.createInstance();
    // Should be able to assign an integer to the property
    recordType.setProperty(record, "y", registry.getIntegerType(), new Node());
    record.put("y", 42);

    // Should be able to assign a string to the property
    recordType.setProperty(record, "y", registry.getStringType(), new Node());
    record.put("y", "hello");

    RecordType recordTypeAssigned = new RecordType(registry, properties);
    assertEquals("RecordType{x=number, y=(int|string)}", recordTypeAssigned.toString());
    assertEquals(registry.getIntegerType(), recordTypeAssigned.getPropertyType("y"));
    assertEquals(registry.getStringType(), recordTypeAssigned.getPropertyType("y"));
  }
}
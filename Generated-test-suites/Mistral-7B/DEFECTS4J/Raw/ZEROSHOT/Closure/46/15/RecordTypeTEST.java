package com.google.javascript.rhino.jstype;

import static org.junit.Assert.*;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.PrototypeObjectType;
import com.google.javascript.rhino.jstype.RecordProperty;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.SortedMap;

/**
 * Test class for {@link RecordType}.
 */
public class RecordTypeTest {

  private JSTypeRegistry registry;
  private ErrorReporter errorReporter;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    errorReporter = new ErrorReporter() {};
  }

  @Test
  public void testCreateRecordType() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("name", new RecordProperty("name", String.class));
    recordProperties.put("age", new RecordProperty("age", Integer.class));

    JSType recordType = new RecordType(registry, recordProperties);
    assertEquals(String.class, recordType.getName());
    assertEquals(RecordType.class, recordType.getClass());

    // Check properties
    SortedMap<String, JSType> properties = recordType.getProperties();
    assertEquals(2, properties.size());
    assertEquals("name", properties.keySet().iterator().next());
    assertEquals("age", properties.keySet().iterator().next());
    assertEquals(String.class, properties.get("name"));
    assertEquals(Integer.class, properties.get("age"));
  }

  @Test
  public void testIsEquivalentTo() {
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("name", new RecordProperty("name", String.class));
    recordProperties1.put("age", new RecordProperty("age", Integer.class));

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("name", new RecordProperty("name", String.class));
    recordProperties2.put("age", new RecordProperty("age", Integer.class));

    JSType recordType1 = new RecordType(registry, recordProperties1);
    JSType recordType2 = new RecordType(registry, recordProperties2);

    assertTrue(recordType1.isEquivalentTo(recordType2));
  }

  @Test
  public void testIsNotEquivalentToDifferentProperties() {
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("name", new RecordProperty("name", String.class));
    recordProperties1.put("age", new RecordProperty("age", Integer.class));

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("name", new RecordProperty("name", String.class));
    recordProperties2.put("age", new RecordProperty("age", Double.class));

    JSType recordType1 = new RecordType(registry, recordProperties1);
    JSType recordType2 = new RecordType(registry, recordProperties2);

    assertFalse(recordType1.isEquivalentTo(recordType2));
  }

  @Test
  public void testIsNotEquivalentToDifferentOrder() {
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("age", new RecordProperty("age", Integer.class));
    recordProperties1.put("name", new RecordProperty("name", String.class));

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("name", new RecordProperty("name", String.class));
    recordProperties2.put("age", new RecordProperty("age", Integer.class));

    JSType recordType1 = new RecordType(registry, recordProperties1);
    JSType recordType2 = new RecordType(registry, recordProperties2);

    assertFalse(recordType1.isEquivalentTo(recordType2));
  }

  @Test
  public void testIsSubtype() {
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("name", new RecordProperty("name", String.class));
    recordProperties1.put("age", new RecordProperty("age", Integer.class));

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("name", new RecordProperty("name", String.class));

    JSType recordType1 = new RecordType(registry, recordProperties1);
    JSType recordType2 = new RecordType(registry, recordProperties2);

    // RecordType is a subtype of itself
    assertTrue(recordType1.isSubtype(recordType1));

    // RecordType is a subtype of the Object type
    assertTrue(recordType1.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));

    // RecordType with fewer properties is not a subtype of a RecordType with more
    assertFalse(recordType1.isSubtype(new RecordType(registry, Maps.newHashMap())));

    // RecordType with the same properties as a subtype is a subtype
    assertTrue(recordType1.isSubtype(recordType2));

    // RecordType with additional properties is not a subtype of a RecordType with fewer
    assertFalse(recordType2.isSubtype(recordType1));
  }

  @Test
  public void testGetLeastSupertype() {
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("name", new RecordProperty("name", String.class));
    recordProperties1.put("age", new RecordProperty("age", Integer.class));

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("name", new RecordProperty("name", String.class));
    recordProperties2.put("age", new RecordProperty("age", Integer.class));
    recordProperties2.put("city", new RecordProperty("city", String.class));

    JSType recordType1 = new RecordType(registry, recordProperties1);
    JSType recordType2 = new RecordType(registry, recordProperties2);

    JSType leastSupertype = recordType1.getLeastSupertype(recordType2);

    assertEquals(recordType1.getClass(), leastSupertype.getClass());
  }

  @Test
  public void testGetGreatestSubtype() {
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("name", new RecordProperty("name", String.class));
    recordProperties1.put("age", new RecordProperty("age", Integer.class));

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("name", new RecordProperty("name", String.class));
    recordProperties2.put("age", new RecordProperty("age", Integer.class));
    recordProperties2.put("city", new RecordProperty("city", String.class));

    JSType recordType1 = new RecordType(registry, recordProperties1);
    JSType recordType2 = new RecordType(registry, recordProperties2);

    JSType greatestSubtype = recordType2.getGreatestSubtype(recordType1);

    assertEquals(recordType2.getClass(), greatestSubtype.getClass());
  }

  @Test
  public void testIsFrozen() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("name", new RecordProperty("name", String.class));

    JSType recordType = new RecordType(registry, recordProperties);

    // Initially, the record is not frozen
    assertFalse(recordType.isFrozen());

    // Freeze the record
    recordType.freeze();

    // After freezing, the record is frozen
    assertTrue(recordType.isFrozen());

    // Attempting to add a property after freezing fails
    assertFalse(recordType.defineProperty("newProperty", String.class, false, null));
  }
}
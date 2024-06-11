package com.google.javascript.rhino.jstype.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;

/**
 * Test cases for RecordTypeBuilder class.
 *
 */
public class RecordTypeBuilderTest {
  private JSTypeRegistry registry;
  private RecordTypeBuilder builder;

  @Before
  public void setup() {
    registry = new JSTypeRegistry();
    builder = new RecordTypeBuilder(registry);
  }

  @Test
  public void testAddProperty() {
    // Test adding a property with valid arguments
    JSType type = registry.getSimpleType("String");
    RecordProperty property = builder.addProperty("name", type, null);
    assertNotNull(property);

    // Test adding a property with same name
    RecordProperty duplicate = builder.addProperty("name", type, null);
    assertNull(duplicate);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddPropertyWithNullType() {
    JSType type = null;
    builder.addProperty("name", type, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddPropertyWithNullRegistry() {
    JSTypeRegistry registry = null;
    new RecordTypeBuilder(registry);
  }

  @Test
  public void testBuildEmptyRecord() {
    JSType recordType = builder.build();
    assertTrue(recordType instanceof JSTypeNative);
    assertTrue(recordType.getClass() == JSTypeNative.OBJECT_TYPE);
  }

  @Test
  public void testBuildRecordWithProperties() {
    JSType stringType = registry.getSimpleType("String");
    JSType recordType = builder.addProperty("name", stringType, null).build();
    assertTrue(recordType instanceof RecordType);
  }

  @Test(expected = IllegalStateException.class)
  public void testBuildRecordTwice() {
    JSType recordType = builder.build();
    builder.build();
  }
}
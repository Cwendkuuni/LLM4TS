package com.google.javascript.rhino.jstype.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;

public class RecordTypeBuilderTest {
  private JSTypeRegistry registry;
  private RecordTypeBuilder builder;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    builder = new RecordTypeBuilder(registry);
  }

  @Test
  public void testEmptyRecord() {
    JSType recordType = builder.build();
    assertNotNull(recordType);
    assertNotNull(recordType.getClass());
    assertNotNull(recordType.getPrototype());
    assertNotNull(recordType.getPrototype().getClass());
    assertNotNull(recordType.getPrototype().getPrototype());
    assertNotNull(recordType.getPrototype().getPrototype().getClass());
    assertNull(builder.isEmpty);
  }

  @Test
  public void testAddProperty() {
    JSType type = registry.getNullType();
    RecordProperty property = builder.addProperty("prop", type, null).getProperty();
    assertNotNull(property);
    assertNotNull(property.getType());
    assertNull(property.getPropertyNode());
    JSType recordType = builder.build();
    assertNotNull(recordType);
    assertNotNull(recordType.getOwnPropertyNames());
    assertNotNull(recordType.getOwnPropertyDescriptor("prop"));
  }

  @Test
  public void testAddDuplicateProperty() {
    JSType type = registry.getNullType();
    builder.addProperty("prop", type, null);
    assertNull(builder.addProperty("prop", type, null));
    JSType recordType = builder.build();
    assertNotNull(recordType);
    assertNotNull(recordType.getOwnPropertyNames());
    assertNotNull(recordType.getOwnPropertyDescriptor("prop"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullRegistry() {
    new RecordTypeBuilder(null);
  }

  @Test
  public void testNullType() {
    JSType type = null;
    assertNull(builder.addProperty("prop", type, null));
  }

  @Test
  public void testNullPropertyNode() {
    assertNull(builder.addProperty("prop", JSType.OBJECT, null));
  }

  @Test
  public void testAddPropertyWithDuplicateNameAndDifferentPropertyNode() {
    JSType type1 = registry.newArrayType(JSType.STRING, 1);
    JSType type2 = registry.newArrayType(JSType.NUMBER, 1);
    builder.addProperty("prop", type1, null);
    RecordProperty property = builder.addProperty("prop", type2, null).getProperty();
    assertNotNull(property);
    JSType recordType = builder.build();
    assertNotNull(recordType);
    assertNotNull(recordType.getOwnPropertyNames());
    assertNotNull(recordType.getOwnPropertyDescriptor("prop"));
  }
}
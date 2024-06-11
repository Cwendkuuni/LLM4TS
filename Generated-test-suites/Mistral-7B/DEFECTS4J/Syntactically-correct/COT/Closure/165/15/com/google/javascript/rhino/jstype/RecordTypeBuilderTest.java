package com.google.javascript.rhino.jstype.test;

import static org.junit.Assert.*;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class RecordTypeBuilderTest {
  private JSTypeRegistry registry;
  private RecordTypeBuilder recordTypeBuilder;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    recordTypeBuilder = new RecordTypeBuilder(registry);
  }

  // Test addProperty method functionality

  @Test
  public void testAddProperty() {
    String name = "name";
    JSType type = registry.getGenericType(String.class);
    RecordProperty expectedResult = new RecordProperty(type, null);

    RecordProperty result = recordTypeBuilder.addProperty(name, type, null).get(0);

    assertEquals(expectedResult.getType(), result.getType());
    assertEquals(name, result.getPropertyNode().toString());
  }

  // Test build method functionality

  @Test
  public void testBuildEmptyRecord() {
    RecordType expectedResult = new RecordType(registry, Collections.emptyMap());

    RecordType result = recordTypeBuilder.build();

    assertEquals(expectedResult, result);
  }

  @Test
  public void testBuildRecordWithProperties() {
    String name1 = "name1";
    String name2 = "name2";

    JSType StringType = registry.getGenericType(String.class);
    JSType NumberType = registry.getNativeType(JSTypeNative.NUMBER_TYPE);

    RecordProperty property1 = recordTypeBuilder.addProperty(name1, StringType, null).get(0);
    RecordProperty property2 = recordTypeBuilder.addProperty(name2, NumberType, null).get(0);

    RecordType expectedResult = new RecordType(
        registry, Maps.newHashMap(Collections.singletonMap(name1, property1), Collections.singletonMap(name2, property2)));

    RecordType result = recordTypeBuilder.build();

    assertEquals(expectedResult, result);
  }

  // Test addProperty method with duplicate property

  @Test
  public void testAddPropertyDuplicate() {
    String name = "name";
    JSType type = registry.getGenericType(String.class);

    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    builder.addProperty(name, type, null);
    builder.addProperty(name, type, null);

    assertTrue(builder.isEmpty());
  }

  // Test build method with empty builder

  @Test
  public void testBuildEmptyBuilder() {
    RecordType expectedResult = new RecordType(
        registry, Collections.emptyMap());

    RecordType result = new RecordTypeBuilder(registry).build();

    assertEquals(expectedResult, result);
  }

  // Test addProperty method with null type

  @Test(expected = IllegalArgumentException.class)
  public void testAddPropertyNullType() {
    String name = "name";
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
    recordTypeBuilder.addProperty(name, null, null);
  }

  // Test build method with null type registry

  @Test(expected = NullPointerException.class)
  public void testBuildNullRegistry() {
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(null);
    recordTypeBuilder.build();
  }
}
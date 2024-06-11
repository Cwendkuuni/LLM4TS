package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.testing.Asserts;

import java.util.Collections;
import java.util.HashMap;

public class RecordTypeBuilderTest {

  private RecordTypeBuilder builder;
  private JSTypeRegistry registry;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    builder = new RecordTypeBuilder(registry);
  }

  @Test
  public void testAddProperty() {
    JSType type = registry.getNativeType(JSTypeNative.VOID_TYPE);
    Node propertyNode = new Node(Token.NAME);
    RecordTypeBuilder.RecordProperty property =
        builder.addProperty("name", type, propertyNode);

    assertNotNull(property);
    assertEquals(type, property.getType());
    assertEquals(propertyNode, property.getPropertyNode());

    // Test duplicate property
    property = builder.addProperty("name", type, propertyNode);
    assertNull(property);
    assertTrue(builder.isEmpty);
  }

  @Test
  public void testBuildEmpty() {
    JSType result = builder.build();

    assertTrue(result instanceof ObjectType);
    assertFalse(result instanceof RecordType);
    assertNotNull(result.getNominalType());
    assertEquals(JSTypeNative.OBJECT_TYPE, result.getNominalType());
    assertTrue(result.getObjectType() instanceof ObjectType);
  }

  @Test
  public void testBuildNonEmpty() {
    JSType type = registry.getNativeType(JSTypeNative.VOID_TYPE);
    Node propertyNode = new Node(Token.NAME);
    builder.addProperty("name", type, propertyNode);

    JSType result = builder.build();

    assertTrue(result instanceof RecordType);
    assertNotNull(result.getNominalType());
    assertEquals(JSTypeNative.RECORD_TYPE, result.getNominalType());
    assertTrue(result.getObjectType() instanceof ObjectType);
    assertTrue(result.getProperties().containsKey("name"));
  }
}
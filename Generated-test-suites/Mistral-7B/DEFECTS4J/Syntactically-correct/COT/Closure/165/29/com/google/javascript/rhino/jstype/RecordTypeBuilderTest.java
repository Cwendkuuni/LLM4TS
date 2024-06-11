package com.google.javascript.rhino.jstype.tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;

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
    String name = "propName";
    JSType type = registry.getStringType();
    Node propertyNode = Node.newLiteralNode("propName");

    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    assertNull(builder.addProperty(name, type, propertyNode));

    builder = new RecordTypeBuilder(registry);
    assertNull(builder.addProperty(name, type, propertyNode));

    builder = new RecordTypeBuilder(registry);
    RecordProperty recordProperty = builder.addProperty(name, type, propertyNode);
    assertNotNull(recordProperty);
  }

  @Test
  public void testBuildEmptyRecord() {
    JSType recordType = builder.build();
    assertNotNull(recordType);
    assertNotNull(recordType.getClass());
    assertInstanceOf(JSTypeNative.OBJECT_TYPE.getClass(), recordType.getClass());
  }

  @Test
  public void testBuildRecordWithProperties() {
    String name1 = "propName1";
    JSType type1 = registry.getStringType();
    Node propertyNode1 = Node.newLiteralNode("propName1");

    String name2 = "propName2";
    JSType type2 = registry.getStringType();
    Node propertyNode2 = Node.newLiteralNode("propName2");

    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    builder.addProperty(name1, type1, propertyNode1);
    builder.addProperty(name2, type2, propertyNode2);

    JSType recordType = builder.build();
    assertNotNull(recordType);
    assertNotNull(recordType.getClass());
    assertFalse(recordType.getClass().isInstance(JSTypeNative.OBJECT_TYPE));

    Map<String, RecordProperty> properties = recordType.getProperties();
    assertEquals(2, properties.size());

    RecordProperty recordProperty1 = properties.get(name1);
    RecordProperty recordProperty2 = properties.get(name2);

    assertEquals(name1, recordProperty1.getPropertyNode().toString());
    assertEquals(name1, recordProperty1.getType().getName());

    assertEquals(name2, recordProperty2.getPropertyNode().toString());
    assertEquals(name2, recordProperty2.getType().getName());
  }

  @Test(expected = IllegalStateException.class)
  public void testAddDuplicateProperty() {
    String name = "propName";
    JSType type = registry.getStringType();
    Node propertyNode = Node.newLiteralNode("propName");

    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    builder.addProperty(name, type, propertyNode);
    builder.addProperty(name, type, propertyNode);
  }
}
package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;

public class RecordTypeBuilderTest {
  @Test
  public void testBuildEmptyRecord() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
    JSType recordType = recordTypeBuilder.build();
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), recordType);
  }

  @Test
  public void testAddProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);

    String name = "name";
    JSType type = registry.getIntType();
    Node propertyNode = new Node();

    RecordTypeBuilder builder = recordTypeBuilder.addProperty(name, type, propertyNode);
    assertNotNull(builder);

    JSType recordType = recordTypeBuilder.build();
    Map<String, RecordProperty> properties = recordType.getProperties();
    assertEquals(1, properties.size());
    RecordProperty recordProperty = properties.get(name);
    assertEquals(type, recordProperty.getType());
    assertEquals(propertyNode, recordProperty.getPropertyNode());
  }

  @Test
  public void testAddDuplicateProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);

    String name = "name";
    JSType type = registry.getIntType();
    Node propertyNode = new Node();

    RecordTypeBuilder builder1 = recordTypeBuilder.addProperty(name, type, propertyNode);
    assertNull(recordTypeBuilder.addProperty(name, type, propertyNode));
  }

  @Test
  public void testBuildRecordWithProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);

    String name1 = "name1";
    JSType type1 = registry.getIntType();
    Node propertyNode1 = new Node();

    String name2 = "name2";
    JSType type2 = registry.getStringType();
    Node propertyNode2 = new Node();

    RecordTypeBuilder builder = recordTypeBuilder
        .addProperty(name1, type1, propertyNode1)
        .addProperty(name2, type2, propertyNode2);

    JSType recordType = builder.build();
    Map<String, RecordProperty> properties = recordType.getProperties();
    assertEquals(2, properties.size());
  }
}
package com.example;

import static org.junit.Assert.*;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;

public class RecordTypeBuilderTest {

  @Test
  public void testAddProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);

    String name = "propName";
    JSType type = JSType.STRING;
    Node propertyNode = new Node(); // assume a valid propertyNode

    RecordProperty expectedProperty = new RecordProperty(type, propertyNode);
    RecordProperty actualProperty = builder.addProperty(name, type, propertyNode);

    assertNotNull(actualProperty);
    assertEquals(expectedProperty.type, actualProperty.type);
    assertEquals(expectedProperty.propertyNode, actualProperty.propertyNode);

    String duplicateName = "propName";
    JSType duplicateType = JSType.NUMBER;
    Node duplicatePropertyNode = new Node(); // assume a valid propertyNode

    assertNull(builder.addProperty(duplicateName, duplicateType, duplicatePropertyNode));
  }

  @Test
  public void testAddPropertyEmptyName() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);

    String emptyName = "";
    JSType type = JSType.STRING;
    Node propertyNode = new Node(); // assume a valid propertyNode

    RecordProperty expectedProperty = null;
    RecordProperty actualProperty = builder.addProperty(emptyName, type, propertyNode);

    assertNull(actualProperty);
    assertEquals(expectedProperty, actualProperty);
  }

  @Test
  public void testAddPropertyNullType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);

    String name = "propName";
    JSType type = null; // null type
    Node propertyNode = new Node(); // assume a valid propertyNode

    RecordProperty expectedProperty = null;
    RecordProperty actualProperty = builder.addProperty(name, type, propertyNode);

    assertNull(actualProperty);
    assertEquals(expectedProperty, actualProperty);
  }

  @Test
  public void testAddPropertyNullPropertyNode() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);

    String name = "propName";
    JSType type = JSType.STRING;
    Node propertyNode = null; // null propertyNode

    RecordProperty expectedProperty = null;
    RecordProperty actualProperty = builder.addProperty(name, type, propertyNode);

    assertNull(actualProperty);
    assertEquals(expectedProperty, actualProperty);
  }

  @Test
  public void testBuildEmptyRecord() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);

    JSType expectedRecord = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    JSType actualRecord = builder.build();

    assertEquals(expectedRecord, actualRecord);
  }

  @Test
  public void testBuildRecordWithProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);

    String name1 = "prop1";
    JSType type1 = JSType.STRING;
    Node propertyNode1 = new Node(); // assume a valid propertyNode

    String name2 = "prop2";
    JSType type2 = JSType.NUMBER;
    Node propertyNode2 = new Node(); // assume a valid propertyNode

    builder.addProperty(name1, type1, propertyNode1);
    builder.addProperty(name2, type2, propertyNode2);

    RecordType expectedRecord = new RecordType(
        registry, Collections.unmodifiableMap(
            new HashMap<String, RecordProperty>() {{
              put(name1, new RecordProperty(type1, propertyNode1));
              put(name2, new RecordProperty(type2, propertyNode2));
            }}
        ));

    JSType actualRecord = builder.build();

    assertEquals(expectedRecord.getClass(), actualRecord.getClass());
    assertEquals(expectedRecord.getPropertyNames().size(), actualRecord.getPropertyNames().size());
    assertEquals(expectedRecord.getProperty(name1).getType(), actualRecord.getProperty(name1).getType());
    assertEquals(expectedRecord.getProperty(name2).getType(), actualRecord.getProperty(name2).getType());
  }

  @Test
  public void testBuildEmptyRecordAfterAddingProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);

    String name = "propName";
    JSType type = JSType.STRING;
    Node propertyNode = new Node(); // assume a valid propertyNode

    builder.addProperty(name, type, propertyNode);

    JSType expectedRecord = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    JSType actualRecord = builder.build();

    assertEquals(expectedRecord, actualRecord);
  }
}
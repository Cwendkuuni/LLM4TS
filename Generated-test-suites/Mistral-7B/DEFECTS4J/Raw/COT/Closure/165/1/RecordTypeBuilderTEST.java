package com.google.javascript.rhino.jstype.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;

/**
 * Tests for the RecordTypeBuilder class.
 *
 */
public class RecordTypeBuilderTest {

  @Test
  public void testNewInstance() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    assertNotNull(builder);
  }

  @Test
  public void testAddProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    String name = "name";
    JSType type = new IntType();
    Node propertyNode = new Node(); // a dummy node
    RecordTypeBuilder result = builder.addProperty(name, type, propertyNode);
    assertNotNull(result);
    assertNull(result.build()); // build should return null before adding all properties
    RecordProperty expected = new RecordProperty(type, propertyNode);
    RecordProperty actual = result.properties.get(name);
    assertNotNull(actual);
    assertSame(expected.getType(), actual.getType());
    assertSame(expected.getPropertyNode(), actual.getPropertyNode());
  }

  @Test
  public void testAddPropertyDuplicate() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    String name = "name";
    JSType type = new IntType();
    Node propertyNode = new Node(); // a dummy node
    RecordTypeBuilder result1 = builder.addProperty(name, type, propertyNode);
    RecordTypeBuilder result2 = builder.addProperty(name, type, propertyNode);
    assertNull(result2);
  }

  @Test(expected = IllegalStateException.class)
  public void testBuildEmptyRecord() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    JSType result = builder.build();
    assertNotNull(result);
    assertInstanceOf(JSTypeNative.class, result);
  }

  @Test
  public void testBuildRecordWithProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    String name1 = "name1";
    JSType type1 = new IntType();
    Node propertyNode1 = new Node(); // a dummy node
    builder.addProperty(name1, type1, propertyNode1);

    String name2 = "name2";
    JSType type2 = new StringType();
    Node propertyNode2 = new Node(); // a dummy node
    builder.addProperty(name2, type2, propertyNode2);

    JSType result = builder.build();
    assertNotNull(result);
    assertInstanceOf(RecordType.class, result);
    RecordType record = (RecordType) result;
    Map<String, JSType> expected = Maps.newHashMap();
    expected.put(name1, type1);
    expected.put(name2, type2);
    Map<String, JSType> actual = record.getProperties();
    assertSame(expected, actual);
  }
}
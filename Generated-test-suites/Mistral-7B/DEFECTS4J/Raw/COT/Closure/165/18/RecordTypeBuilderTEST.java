package com.google.javascript.rhino.jstype.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;

/**
 * Tests for {@link RecordTypeBuilder} class.
 *
 */
public class RecordTypeBuilderTest {

  @Test
  public void testRecordTypeBuilder() {
    // Test constructor
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    assertNotNull(builder);

    // Test addProperty method
    String propertyName = "name";
    JSType propertyType = registry.getNumberType();
    Node propertyNode = Node.newIdent("name");
    RecordTypeBuilder recordBuilder = builder.addProperty(propertyName, propertyType, propertyNode);
    assertNotNull(recordBuilder);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddPropertyWithDuplicateName() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);

    String propertyName = "name";
    JSType propertyType = registry.getNumberType();
    Node propertyNode = Node.newIdent("name");
    RecordProperty property = builder.addProperty(propertyName, propertyType, propertyNode);

    // Test that adding the same property with the same name leads to an error
    RecordProperty duplicateProperty = builder.addProperty(propertyName, propertyType, propertyNode);
    assertNull(duplicateProperty);
  }

  @Test
  public void testBuildEmptyRecord() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);

    JSType recordType = builder.build();
    assertTrue(recordType instanceof JSTypeNative);
    assertTrue(recordType.getClass() == JSTypeNative.class);
    assertTrue(recordType.getClassName().equals(JSTypeNative.OBJECT_TYPE));
  }

  @Test
  public void testBuildRecordWithProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);

    String propertyName1 = "name1";
    JSType propertyType1 = registry.getNumberType();
    Node propertyNode1 = Node.newIdent("name1");
    RecordProperty property1 = builder.addProperty(propertyName1, propertyType1, propertyNode1);

    String propertyName2 = "name2";
    JSType propertyType2 = registry.getBooleanType();
    Node propertyNode2 = Node.newIdent("name2");
    RecordProperty property2 = builder.addProperty(propertyName2, propertyType2, propertyNode2);

    JSType recordType = builder.build();

    Map<String, RecordProperty> properties = recordType.getRecordProperties();
    assertTrue(properties.size() == 2);
    assertTrue(properties.containsKey(propertyName1));
    assertTrue(properties.containsKey(propertyName2));
    RecordProperty property = properties.get(propertyName1);
    assertNotNull(property);
    assertEquals(propertyType1, property.getType());
    assertEquals(propertyNode1, property.getPropertyNode());

    property = properties.get(propertyName2);
    assertNotNull(property);
    assertEquals(propertyType2, property.getType());
    assertEquals(propertyNode2, property.getPropertyNode());
  }
}
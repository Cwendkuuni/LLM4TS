package com.google.javascript.rhino.jstype;

import static org.junit.Assert.*;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Test class for RecordTypeBuilder.
 */
public class RecordTypeBuilderTest {

  @SuppressWarnings("unchecked")
  private JSTypeRegistry createMockRegistry() {
    return mock(JSTypeRegistry.class);
  }

  /**
   * Test method for {@link RecordTypeBuilder#RecordTypeBuilder(JSTypeRegistry)}.
   */
  @Test
  public void testRecordTypeBuilder() {
    JSTypeRegistry registry = createMockRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    assertEquals(true, builder.isEmpty);
  }

  /**
   * Test method for {@link RecordTypeBuilder#addProperty(String, JSType, Node)}.
   */
  @Test
  public void testAddPropertySuccess() {
    String propertyName = "name";
    JSType propertyType = Boolean.TYPE;
    Node propertyNode = mock(Node.class);

    JSTypeRegistry registry = createMockRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    RecordProperty recordProperty = builder.addProperty(propertyName, propertyType, propertyNode);

    assertFalse("Should not return null if property was added", recordProperty == null);
    assertEquals(propertyType, recordProperty.getType());
    assertEquals(propertyNode, recordProperty.getPropertyNode());
    assertEquals(false, builder.isEmpty);
  }

  /**
   * Test method for {@link RecordTypeBuilder#addProperty(String, JSType, Node)}.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testAddPropertyFailureDuplicate() {
    String propertyName = "name";
    JSType propertyType = Boolean.TYPE;
    Node propertyNode = mock(Node.class);

    JSTypeRegistry registry = createMockRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    builder.addProperty(propertyName, propertyType, propertyNode);

    // This should fail with an IllegalArgumentException
    builder.addProperty(propertyName, propertyType, propertyNode);
  }

  /**
   * Test method for {@link RecordTypeBuilder#build()}.
   */
  @Test
  public void testBuildEmptyRecord() {
    JSTypeRegistry registry = createMockRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    JSType recordType = builder.build();

    assertEquals(JSTypeNative.OBJECT_TYPE, recordType.getClass());
  }

  /**
   * Test method for {@link RecordTypeBuilder#build()}.
   */
  @Test
  public void testBuildRecordWithProperties() {
    String propertyName1 = "name1";
    JSType propertyType1 = Boolean.TYPE;
    Node propertyNode1 = mock(Node.class);

    String propertyName2 = "name2";
    JSType propertyType2 = Byte.TYPE;
    Node propertyNode2 = mock(Node.class);

    JSTypeRegistry registry = createMockRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);

    builder.addProperty(propertyName1, propertyType1, propertyNode1);
    builder.addProperty(propertyName2, propertyType2, propertyNode2);

    Map<String, RecordProperty> expectedProperties = Maps.newHashMap();
    expectedProperties.put(propertyName1, new RecordProperty(propertyType1, propertyNode1));
    expectedProperties.put(propertyName2, new RecordProperty(propertyType2, propertyNode2));

    RecordType recordType = builder.build();
    assertEquals(expectedProperties.getClass(), recordType.getProperties().getClass());
    assertEquals(2, recordType.getProperties().size());
  }

  /**
   * Test method for {@link RecordTypeBuilder#build()}.
   */
  @Test(expected = IllegalStateException.class)
  public void testBuildRecordWithEmptyBuilder() {
    JSTypeRegistry registry = createMockRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);

    // This should fail with an IllegalStateException
    builder.build();
  }
}
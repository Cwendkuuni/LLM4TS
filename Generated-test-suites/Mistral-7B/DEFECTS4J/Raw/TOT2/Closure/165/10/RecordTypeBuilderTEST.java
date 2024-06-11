package com.example.recordtypebuilder;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.jstype.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeBuilderTest {
  private RecordTypeBuilder recordTypeBuilder;
  private JSTypeRegistry registry;
  private RecordProperty recordProperty;
  private RecordProperty recordProperty1;
  private RecordProperty recordProperty2;
  private RecordType recordType;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    recordTypeBuilder = new RecordTypeBuilder(registry);

    String name1 = "testProperty1";
    JSType type1 = JSType.STRING;
    Node propertyNode1 = Node.newText("propertyNode1");
    recordProperty1 = recordTypeBuilder.addProperty(name1, type1, propertyNode1);

    String name2 = "testProperty2";
    JSType type2 = JSType.NUMBER;
    Node propertyNode2 = Node.newText("propertyNode2");
    recordProperty2 = recordTypeBuilder.addProperty(name2, type2, propertyNode2);

    recordType = recordTypeBuilder.build();
  }

  @Test
  public void testConstructor() {
    // Test the constructor with a valid registry
    assertNotNull(recordTypeBuilder);
  }

  @Test
  public void testAddProperty() {
    // Check if the property was added
    assertTrue(recordTypeBuilder.properties.containsKey(name1));
    // Check if the property has the correct type
    assertEquals(type1, recordProperty1.type);
  }

  @Test
  public void testBuild() {
    // Check if the build method returns a valid RecordType
    assertTrue(recordType instanceof RecordType);

    // Check if the properties were added to the RecordType
    HashMap<String, Object> expectedMap = Maps.newHashMap();
    expectedMap.put(recordProperty1.getName(), recordProperty1.getType());
    expectedMap.put(recordProperty2.getName(), recordProperty2.getType());
    assertEquals(expectedMap, recordType.getProperties());
  }

  @Test
  public void testAddPropertyWithSameNameTwice() {
    String name = "testProperty";
    JSType type = JSType.STRING;
    Node propertyNode = Node.newText("propertyNode");

    RecordProperty recordProperty1 = recordTypeBuilder.addProperty(name, type, propertyNode);
    RecordProperty recordProperty2 = recordTypeBuilder.addProperty(name, null, null);

    // Check if the second addProperty call returned null
    assertNull(recordProperty2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddPropertyWithNullName() {
    String name = null;
    JSType type = JSType.STRING;
    Node propertyNode = Node.newText("propertyNode");

    recordTypeBuilder.addProperty(name, type, propertyNode);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddPropertyWithNullType() {
    String name = "testProperty";
    JSType type = null;
    Node propertyNode = Node.newText("propertyNode");

    recordTypeBuilder.addProperty(name, type, propertyNode);
  }

  @Test
  public void testBuildWithEmptyPropertiesMap() {
    RecordType recordType = recordTypeBuilder.build();
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), recordType);
  }

  @Test(expected = NullPointerException.class)
  public void testBuildWithNullRegistry() {
    JSTypeRegistry registry = null;
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);

    recordTypeBuilder.addProperty("testProperty", JSType.STRING, Node.newText("propertyNode"));

    recordTypeBuilder.build();
  }
}